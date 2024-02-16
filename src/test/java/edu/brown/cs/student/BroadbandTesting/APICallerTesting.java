package edu.brown.cs.student.BroadbandTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import edu.brown.cs.student.main.Server.APICaller;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Set up a fake request to be used in testing the APICaller class
 */
class MockRequest {
  private final String state;
  private final String county;
  private final String dataVars;

  public MockRequest(String state, String county, String dataVars) {
    this.state = state;
    this.county = county;
    this.dataVars = dataVars;
  }

  public String queryParams(String param) {
    switch (param) {
      case "state":
        return state;
      case "county":
        return county;
      case "dataVars":
        return dataVars;
      default:
        return null;
    }
  }
}

class MockResponse {
  public static final String BROADBAND_DATA_RESPONSE = "[[\"NAME\",\"S2802_C03_022E\",\"state\",\"county\"],\n" +
      "[\"Los Angeles County, California\",\"89.9\",\"06\",\"037\"]]";
}

class APICallerTest {

  private APICaller apiCaller;
  private MockRequest mockRequest;

  @BeforeEach
  void setUp() {
    try {
      apiCaller = new APICaller();
    } catch (IOException | URISyntaxException | InterruptedException e) {
      e.printStackTrace();
    }
    mockRequest = new MockRequest("California", "Los Angeles County", "DP04_0049E,DP03_0048PE");
    MockResponse mockResponse = new MockResponse();
  }

  @Test
  void testFind() throws IOException, URISyntaxException, InterruptedException {
    // Mock API responses
    String broadbandApiResponse = "Mocked broadband response";
    String optionalVarApiResponse = "[[\"NAME\",\"DP04_0049E\",\"DP03_0048PE\",\"state\",\"county\"],\n" +
        "[\"Los Angeles County, California\",\"2.67\",\"13.0\",\"06\",\"037\"]]";

    // Mock broadbandRequest method
    apiCaller.broadbandRequest("06", "037");
    String actualBroadbandApiResponse = apiCaller.broadbandRequest("06", "037");
    assertEquals(broadbandApiResponse, actualBroadbandApiResponse);

    // Mock optionalVarRequest method
    apiCaller.optionalVarRequest("DP04_0049E,DP03_0048PE", "06", "037");
    String actualOptionalVarApiResponse = apiCaller.optionalVarRequest("DP04_0049E,DP03_0048PE", "06", "037");
    assertEquals(optionalVarApiResponse, actualOptionalVarApiResponse);

    // Call the method under test
    LocationData result = apiCaller.find(
        mockRequest.queryParams("state"),
        mockRequest.queryParams("county"),
        mockRequest.queryParams("dataVars"));

    assertNotNull(result);
    assertEquals("StateCode", result.getState());
    assertEquals("CountyCode", result.getCounty());
    assertEquals("Mocked broadband response", result.getData().get(0));
    assertEquals("Mocked optional var response", result.getData().get(1));
  }

  @Test
  void testFindMatchingCodes() throws IOException, URISyntaxException, InterruptedException {
    // Call the method under test
    apiCaller.findMatchingCodes("California", "Los Angeles County");

    // Perform assertions
    assertEquals("037", apiCaller.getCountyCode());
    assertEquals("06", apiCaller.getStateCode());
  }
}