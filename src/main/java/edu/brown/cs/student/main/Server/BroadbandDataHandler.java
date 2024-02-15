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
public class BroadbandDataHandler implements Route {

  private final List<LocationData> stateList;
  private String stateCode;
  private String countyCode;

  /**
   * Initialize the stateList, allows the lookup of states in later queries without having to ping
   * the census api each time. This list should always exist (no need to remove due to caching)
   *
   * @throws IOException          thrown when deserialization of the census api fails
   * @throws URISyntaxException   thrown when the provided URI is not in a valid format for the
   *                              census api
   * @throws InterruptedException thrown when the get request fails (e.g. connection to census is
   *                              lost)
   */
  BroadbandDataHandler() throws IOException, URISyntaxException, InterruptedException {
    this.stateList = fetchAllStates();
  }

  /**
   * Core driver of the broadband request and response loop. Deals with converting endpoint requests
   * into internal census requests and finding the desired location's broadband information, or
   * returning a bad response otherwise
   *
   * @param request  endpoint requests, in the format of "?county=some%20county&state=some%20state"
   *                 Additional params are ignored
   * @param response HTTP response to send information to (unused here)
   * @return a serialized map of response, code, and location data (or exception)
   */
  @Override
  public Object handle(Request request, Response response) {
    String county = request.queryParams("county");
    String state = request.queryParams("state");

    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Step 1: find the matching state and county codes for the user query
      findMatchingCodes(state, county);
      // Step 2: send the full request and get back the json (including bb data)
      String broadbandJson = sendRequest(this.stateCode, this.countyCode);
      // Step 3: deserialize and pass that data back to the user in the respMap
      List<LocationData> bbLoc = BroadbandDataAPIUtilities.deserializeLocData(broadbandJson);
      for (LocationData loc : bbLoc) {
        if (loc.getState().equals(this.stateCode) && loc.getCounty().equals(this.countyCode)) {
          responseMap.put(loc.getNAME(), loc.toList());
          // System.out.println("Response map \n" + responseMap);
          // Reset state and county code so if user enters a bad request next, they will
          // get a new (bad) response rather than just getting the old (good) response
          this.stateCode = null;
          this.countyCode = null;
          return new SuccessResponse(responseMap).serialize();
        }
      }
    } catch (Exception e) {
      return new NoRecordFailureResponse().serialize();
    }

    return new NoRecordFailureResponse().serialize();
  }

  /**
   * Convert a response map of String, Object into a serialized json for handling with spark
   *
   * @param response_type success or failure as a code, given here to allow users to define a custom
   *                      code optionally or uses 200 as a default
   * @param responseMap   a map from the handler containing a defining string mapped to the response
   *                      object. For this case, the object is a list of strings built from a
   *                      location data object
   */
  public record SuccessResponse(String response_type, Map<String, Object> responseMap) {

    /**
     * construct the response with a default code of 200
     *
     * @param responseMap the actual information to be serialized
     */
    public SuccessResponse(Map<String, Object> responseMap) {
      this("200", responseMap);
    }

    /**
     * serialize the response code and response map
     *
     * @return serialized location objects that match user's query
     */
    String serialize() {
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
   * @param response_type success or failure as a code, given here to allow users to define a custom
   *                      code optionally or uses 204 as a default (for failure)
   */
  public record NoRecordFailureResponse(String response_type) {

    public NoRecordFailureResponse() {
      this("204");
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(NoRecordFailureResponse.class).toJson(this);
    }
  }

  /**
   * given the internal census state and county codes, make the final request to retrieve the
   * broadband data associated with that location
   *
   * @param state  internal code associated with state provided by user from the endpoint
   * @param county internal code associated with county provided by user from the endpoint
   * @return the json associated with that state and county code containing the broadband data at
   * that location
   * @throws URISyntaxException   thrown when the uri is in the wrong format for the census api
   * @throws IOException          thrown when HTTP response fails to build
   * @throws InterruptedException thrown when connection to census fails
   */
  private String sendRequest(String state, String county)
      throws URISyntaxException, IOException, InterruptedException {

    String baseUrl =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:";
    String fullUrl = baseUrl + county + "&in=state:" + state;
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

  /**
   * This method finds the associated state code and county code for an endpoint user's query within
   * the census api then populates the global code variables with those codes
   *
   * @param state  plain english state provided by endpoint
   * @param county plain english county provided by endpoint
   * @throws URISyntaxException   thrown when URI is in the improper format
   * @throws IOException          thrown when the HTTP response fails
   * @throws InterruptedException thrown when the connection is interrupted with the census api
   */
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
              "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                  + this.stateCode;
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
   * name should only contain the plain english name of the state, and state should contain the
   * internal census code for that state
   * @throws IOException          thrown when deserialization fails
   * @throws InterruptedException thrown when request not fulfilled
   * @throws URISyntaxException   thrown when URI not in proper format
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
