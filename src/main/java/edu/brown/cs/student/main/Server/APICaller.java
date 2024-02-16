package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.BroadbandDataAPI.BroadbandDataAPIUtilities;
import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class handles retrieving broadband accessibility information from the US census when given a
 * county and state in plain english from the broadband endpoint
 */
public class APICaller implements LocationDataFinder {

  private final List<LocationData> stateList;
  private String stateCode;
  private String countyCode;

  /**
   * Initialize the stateList, allows the lookup of states in later queries without having to ping
   * the census api each time. This list should always exist (no need to remove due to caching)
   *
   * @throws IOException thrown when deserialization of the census api fails
   * @throws URISyntaxException thrown when the provided URI is not in a valid format for the census
   *     api
   * @throws InterruptedException thrown when the get request fails (e.g. connection to census is
   *     lost)
   */
  APICaller() throws IOException, URISyntaxException, InterruptedException {
    this.stateList = fetchAllStates();
  }

  @Override
  public LocationData find(String state, String county, String dataVars) {
    try{
      // Step 1: populate state and county codes for specified state and county
      findMatchingCodes(state, county);
      // Step 2: grab the broadband data for that location
      List<LocationData> tempBBLocs = BroadbandDataAPIUtilities.deserializeLocData(broadbandRequest(this.stateCode, this.countyCode));
      LocationData bbLoc = null;
      for (LocationData loc : tempBBLocs){
        if (loc.getState().equals(this.stateCode) && loc.getCounty().equals(this.countyCode)){
          bbLoc = loc;
        }
      }
      // should not be null here but just in case other components fail, still need to assert
      assert bbLoc != null;

      // Step 3: grab any optional variable the user provided
      List<String> requestedData = new ArrayList<>(bbLoc.getData());
      // Adds the variable data to the list of strings
      if(!dataVars.isBlank()) {
        List<LocationData> varData = BroadbandDataAPIUtilities.deserializeLocData(
            optionalVarRequest(dataVars, state, county));
        for (LocationData loc : varData) {
          if (loc.getState().equals(this.stateCode) && loc.getCounty().equals(this.countyCode)) {
            requestedData.addAll(loc.getData());
          }
        }
      }

      // Step 4: combine into a single LocationData obj and return
      return new LocationData(bbLoc.getNAME(), requestedData, this.stateCode, this.countyCode);

    } catch (Exception e){
      System.err.println("Exception encountered: " + e.toString());
      return null;
    }
  }

  /**
   * given the internal census state and county codes, make the final request to retrieve the
   * broadband data associated with that location
   *
   * @param state internal code associated with state provided by user from the endpoint
   * @param county internal code associated with county provided by user from the endpoint
   * @return the json associated with that state and county code containing the broadband data at
   *     that location
   * @throws URISyntaxException thrown when the uri is in the wrong format for the census api
   * @throws IOException thrown when HTTP response fails to build
   * @throws InterruptedException thrown when connection to census fails
   */
  private String broadbandRequest(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {

    String baseUrl =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,,S2802_C03_022E"
            + "&for=county:";
    String fullUrl = baseUrl + county + "&in=state:" + state;
    System.out.println("Full url: " + fullUrl);
    HttpRequest buildBoredApiRequest = HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();

    // Send that API request then store the response in this variable
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, BodyHandlers.ofString());
    // TODO: add error handling for bad response types
    System.out.println("Final response: \n" + sentBoredApiResponse.body());
    return sentBoredApiResponse.body();
  }

  private String optionalVarRequest(String vars, String state, String county)
      throws URISyntaxException, IOException, InterruptedException {

    String fullUrl =
        "https://api.census.gov/data/2021/acs/acs1/subject/profile?get=NAME," + vars
            + "&for=county:" + county + "&in=state:" + state;
    System.out.println("Full optional var url: " + fullUrl);
    HttpRequest buildBoredApiRequest = HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();

    // Send that API request then store the response in this variable
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, BodyHandlers.ofString());
    System.out.println("Final optional var response: \n" + sentBoredApiResponse.body());
    return sentBoredApiResponse.body();
  }
  /**
   * This method finds the associated state code and county code for an endpoint user's query within
   * the census api then populates the global code variables with those codes
   *
   * @param state plain english state provided by endpoint
   * @param county plain english county provided by endpoint
   * @throws URISyntaxException thrown when URI is in the improper format
   * @throws IOException thrown when the HTTP response fails
   * @throws InterruptedException thrown when the connection is interrupted with the census api
   */
  private void findMatchingCodes(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {
    for (LocationData s : stateList) {
      if (s.getNAME().equals(state)) {
        // System.out.println(s.getNAME());
        // System.out.println(state);
        this.stateCode = s.getState();
        // Now that a state code is defined, we can also find the county code
        try {
          String baseUrl =
              "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                  + this.stateCode;
          // Same api request/response format as get states method
          HttpRequest buildBoredApiRequest =
              HttpRequest.newBuilder().uri(new URI(baseUrl)).GET().build();

          // Send that API request then store the response in this variable
          HttpResponse<String> sentBoredApiResponse =
              HttpClient.newBuilder()
                  .build()
                  .send(buildBoredApiRequest, BodyHandlers.ofString());
          // System.out.println("Response: \n " + sentBoredApiResponse.body());
          List<LocationData> countiesInState =
              BroadbandDataAPIUtilities.deserializeLocData(sentBoredApiResponse.body());
          for (LocationData c : countiesInState) {
            // NAME contains "<county> county|city, <state>", splitting on comma and grabbing the
            // first index retrieves only county name
            String countyOnly = c.getNAME().split(",")[0];
            if (countyOnly.equals(county)) {
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
              .send(buildBoredApiRequest, BodyHandlers.ofString());
      // System.out.println("repsonse: " + sentBoredApiResponse.body());
      return BroadbandDataAPIUtilities.deserializeLocData(sentBoredApiResponse.body());

      // TODO: modify to look at response status for error handling
    } catch (Exception e) {
      System.out.println("Error occurred fetching initial location set: " + e);
      throw e;
    }
  }
}
