package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class handles retrieving broadband accessibility information from the US census when given a
 * county and state in plain english from the broadband endpoint
 */
public class BroadbandDataHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    try {
      ACSProxy datasource = new ACSProxy();
      // Just split apart the user args and pass them to the ACS proxy. The proxy will deal
      // with calling the api and returning stuff as needed

      System.out.println("getting broadband data...");
      String request_type = "S2802_C03_022E";
      String[] optionalGet = request.queryParamsValues("get");
      // Combine all the request types given plus the broadband request type
      if (!(optionalGet == null || optionalGet.length == 0)) {
        request_type = request_type + String.join(",", optionalGet);
      }
      LocationData loc = datasource.get(request);
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("access_date_time", LocalDateTime.now().toString());
      responseMap.put("response_type", request_type);
      responseMap.put(loc.getNAME(), loc.toList());
      return new SuccessResponse(responseMap).serialize();
    } catch (IOException e) {
      return new IOExceptionFailureResponse(e.toString()).serialize();
    } catch (URISyntaxException e) {
      return new URISyntaxFailureResponse(e.toString()).serialize();
    } catch (InterruptedException e) {
      return new InterruptionFailureResponse(e.toString()).serialize();
    }
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
   * Generate response for IOExceptions
   *
   * @param response_type     failure code
   * @param exception_message IOException thrown from elsewhere, converted to a string
   */
  public record IOExceptionFailureResponse(String response_type, String exception_message) {

    public IOExceptionFailureResponse(String exception_message) {
      this("205", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(IOExceptionFailureResponse.class).toJson(this);
    }
  }

  /**
   * Generate response for URI Syntax failures
   *
   * @param response_type     response code value
   * @param exception_message toString of URI exception, thrown from elsewhere
   */
  public record URISyntaxFailureResponse(String response_type, String exception_message) {

    public URISyntaxFailureResponse(String exception_message) {
      this("206", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(URISyntaxFailureResponse.class).toJson(this);
    }
  }

  /**
   * Generate response for interruption exceptions
   *
   * @param response_type     response code
   * @param exception_message toString of interruption exception, thrown from other internal
   *                          functions
   */
  public record InterruptionFailureResponse(String response_type, String exception_message) {

    public InterruptionFailureResponse(String exception_message) {
      this("207", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(InterruptionFailureResponse.class).toJson(this);
    }
  }
}