package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.BroadbandDataAPI.BroadbandDataAPIUtilities;
import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandDataHandler implements Route {

  private final List<LocationData> stateList;
  private String stateCode;
  private String countyCode;

  BroadbandDataHandler() throws IOException, URISyntaxException, InterruptedException {
    this.stateList = fetchAllStates();
  }

  @Override
  public Object handle(Request request, Response response)
      throws URISyntaxException, IOException, InterruptedException {
    String county = request.queryParams("county");
    String state = request.queryParams("state");

    Map<String, Object> responseMap = new HashMap<>();

    // Step 1: find the matching state and county codes for the user query
    findMatchingCodes(state, county);
    // Step 2: send the full request and get back the json (including bb data)
    String broadbandJson = sendRequest(this.stateCode, this.countyCode);
    // Step 3: deserialize and pass that data back to the user in the respMap
    List<LocationData> bbLoc = BroadbandDataAPIUtilities.deserializeLocData(broadbandJson);
    for (LocationData loc : bbLoc) {
      if (loc.getState().equals(this.stateCode) && loc.getCounty().equals(this.countyCode)) {
        responseMap.put(loc.getNAME(), loc);
        return new SuccessResponse(responseMap).serialize();
      }
    }
    return new NoRecordFailureResponse().serialize();
  }

  public record SuccessResponse(String response_type, Map<String, Object> responseMap) {

    public SuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * @return serialized location objects that match user's query
     */
    String serialize() {  //TODO specify?
      try {
        // Initialize Moshi which takes in this class and returns it as JSON
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SuccessResponse> adapter = moshi.adapter(SuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * returned when no matching county/state was found from user's query
   *
   * @param response_type
   */
  public record NoRecordFailureResponse(String response_type) {
    public NoRecordFailureResponse() {
      this("error");
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(NoRecordFailureResponse.class).toJson(this);
    }
  }

  private String sendRequest(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {

    String baseUrl =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:";
    String fullUrl = baseUrl + this.countyCode + "&in=state:" + this.stateCode;
    System.out.println("Full url: " + fullUrl);
    HttpRequest buildBoredApiRequest = HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();

    // Send that API request then store the response in this variable
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());
    // TODO: add error handling for bad response types
    System.out.println("Final response: \n" + sentBoredApiResponse.body());
    return sentBoredApiResponse.body();
  }

  private void findMatchingCodes(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {
    for (LocationData s : stateList) {
      if (s.getNAME().equals(state)) {
        System.out.println(s.getNAME());
        System.out.println(state);
        this.stateCode = s.getState();
        // Now that a state code is defined, we can also find the county code
        try {
          String baseUrl =
              "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + this.stateCode;
          // Same api request/response format as get states method
          HttpRequest buildBoredApiRequest =
              HttpRequest.newBuilder().uri(new URI(baseUrl)).GET().build();

          // Send that API request then store the response in this variable
          HttpResponse<String> sentBoredApiResponse =
              HttpClient.newBuilder()
                  .build()
                  .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());
          // System.out.println("Response: \n " + sentBoredApiResponse.body());
          List<LocationData> countiesInState =
              BroadbandDataAPIUtilities.deserializeLocData(sentBoredApiResponse.body());
          for (LocationData c : countiesInState) {
            // NAME contains "<county> county|city, <state>"
            if (c.getNAME().contains(county)) {
              this.countyCode = c.getCounty();
            }
          }
          // TODO: modify to look at response status for error handling
        } catch (Exception e) {
          System.out.println("Error occurred fetching initial location set: " + e);
          throw e;
        }
      }
    }
  }

  /**
   * Fetch a list of all states and their corresponding location codes. This allows less querying of
   * the full census API
   *
   * @return a list of LocationData objects containing a populated state field and name, although
   *     name should only contain the plain english name of the state, and state should contain the
   *     internal census code for that state
   * @throws IOException thrown when deserialization fails
   * @throws InterruptedException thrown when request not fulfilled
   * @throws URISyntaxException thrown when URI not in proper format
   */
  private List<LocationData> fetchAllStates()
      throws IOException, InterruptedException, URISyntaxException {

    try {

      String STATE_CODES_URI = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
      HttpRequest buildBoredApiRequest =
          HttpRequest.newBuilder().uri(new URI(STATE_CODES_URI)).GET().build();

      // Send that API request then store the response in this variable
      HttpResponse<String> sentBoredApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());
      // System.out.println("repsonse: " + sentBoredApiResponse.body());
      return BroadbandDataAPIUtilities.deserializeLocData(sentBoredApiResponse.body());

      // TODO: modify to look at response status for error handling
    } catch (Exception e) {
      System.out.println("Error occurred fetching initial location set: " + e);
      throw e;
    }
  }
}
