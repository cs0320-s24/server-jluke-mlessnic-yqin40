package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSV.Census.Census;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class for viewing CSV content via API endpoint. */
public class ViewCSVHandler implements Route {
  /**
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return Return CSV content if csv file load successfully before.
   * @throws NOCSVDataResponse if no CSV file did not load before.

   */

  public ViewCSVHandler() throws FileNotFoundException {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // Directly access the updated Server.stars list here
    List<Map<String, Object>> censusListInfo = new ArrayList<>();
    for (Census census : Server.getcensusList()) {
      Map<String, Object> censusInfo = new HashMap<>();
      censusInfo.put("City/Town", census.getCity());
      censusInfo.put("Median Household Income", census.getMedianHouseholdIncome());
      censusInfo.put("Median Family Income", census.getMedianFamilyIncome());
      censusInfo.put("Per Capital Income", census.getPerCapitalIncome());
      censusListInfo.add(censusInfo);
    }

    // Serialize and return the stars info
    if (censusListInfo.isEmpty()) {
      return new NOCSVDataResponse("error_datasource, Try to load CSV File").serialize();
    } else {
      return new CSVContentSuccessResponse(censusListInfo).serialize();
    }
  }

  /** Success response structure for CSV content viewing. */
  public static class CSVContentSuccessResponse {
    private final String response_type;
    private final List<Map<String, Object>> censusListinfo;

    public CSVContentSuccessResponse(List<Map<String, Object>> censusListinfo) {
      this.response_type = "success";
      this.censusListinfo = censusListinfo;
    }

    public String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        Type listType = Types.newParameterizedType(List.class, Map.class);
        JsonAdapter<List<Map<String, Object>>> jsonAdapter = moshi.adapter(listType);
        return jsonAdapter.toJson(this.censusListinfo);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  public record NOCSVDataResponse(String response_type, String exception_message) {

    public NOCSVDataResponse(String exception_message) {
      this("201", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ViewCSVHandler.NOCSVDataResponse.class).toJson(this);
    }
  }


  /** Failure response structure for CSV content viewing when no match is found. */

}
