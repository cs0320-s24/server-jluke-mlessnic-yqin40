package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSV.CLISearch.QuerySearch.Searcher;
import edu.brown.cs.student.main.CSV.Census.Census;
import java.lang.reflect.Type;
import java.util.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {

  private final List<List<String>> csvRowsAsList =
      new ArrayList<>(); // should this be more general?
  public List<Census> csvRows;

  // getter method
  public List<List<String>> getCsvRowsAsList() {
    return new ArrayList<>(this.csvRowsAsList);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // check if file is loaded
    // get rows
    this.csvRows = Server.getcensusList();


    if (csvRows.isEmpty()) {
      return new CSVFileNotFoundResponse("error_datasource").serialize();
//       return "No data available. Please load a CSV file first.";
    }

    for (Census c : this.csvRows) {
      List<String> r = c.turnIntoRawStrings();
//       convert back into strings
      this.csvRowsAsList.add(r);
    }

    Searcher searcher =
        new Searcher(this.csvRowsAsList, true, false); // save hasHeaders and ignoreCase in Server
    Set<String> queryParams = request.queryParams();

    if (queryParams.size() != 1) {
//      return "too many queries";
      return new BadQueryResponse("incorrect_num_params").serialize();
    }

    List<Integer> foundRowIndices;
    List<List<String>> foundRows;
    Map<String, Object> responseMap = new HashMap<>();


    // check if search value is provided
    if (queryParams.contains("find")) {
      responseMap.put("type", "find");
      String searchVal = request.queryParams("find");
      responseMap.put("searchValue", searchVal);
      if ((searchVal == null) || searchVal.isEmpty()) {
        return new BadQueryResponse("no_search_value_provided").serialize();
//         return "No item to search for provided.";
      } else {
        // perform basic search
        foundRowIndices = searcher.search(searchVal);
        foundRows = getRowsFromIndices(foundRowIndices, this.csvRowsAsList);
        if (foundRows.isEmpty()) {
          responseMap.put("result", "No rows found.");
        } else {
          responseMap.put("result", foundRows);
        }
        return new SuccessSearchResponse(responseMap).serialize();
      }
    } else if (queryParams.contains("query")) {
      responseMap.put("type", "query");
      String query = request.queryParams("query");
      responseMap.put("query", query);
      if (query == null || query.isEmpty()) {
        return new BadQueryResponse("no_query_provided").serialize();
//         return "No query to search for provided.";
      } else {
        // perform query search
        foundRowIndices = searcher.searchUsingQuery(query);
        foundRows = getRowsFromIndices(foundRowIndices, this.csvRowsAsList);
        if (foundRows.isEmpty()) {
          responseMap.put("result", "No rows found.");
        } else {
          responseMap.put("result", foundRows);
        }
        return new SuccessSearchResponse(responseMap).serialize();



      }
    } else {
      return new CSVSearchFailureResponse().serialize();
    }
  }

  private List<List<String>> getRowsFromIndices(List<Integer> indices, List<List<String>> pool) {
    List<List<String>> foundRows = new ArrayList<List<String>>();
    for (int ind : indices) {
      foundRows.add(pool.get(ind));
    }
    return foundRows;
  }


  public record SuccessSearchResponse (String response_type, Map<String, Object> responseMap) {

    public SuccessSearchResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }


    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchCSVHandler.SuccessSearchResponse.class).toJson(this);
    }
  }

  /** Failure response structure for CSV searching when search fails. */
  public static class CSVSearchFailureResponse {
    private final String response_type;

    public CSVSearchFailureResponse() {
      this.response_type = "error";
    }

    public String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<SearchCSVHandler.CSVSearchFailureResponse> jsonAdapter =
          moshi.adapter(SearchCSVHandler.CSVSearchFailureResponse.class);
      return jsonAdapter.toJson(this);
    }

    public String getResponse_type() {
      return response_type;
    }
  }

  //

  public record CSVFileNotFoundResponse(String response_type, String exception_message) {

    public CSVFileNotFoundResponse(String exception_message) {
      this("201", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchCSVHandler.CSVFileNotFoundResponse.class).toJson(this);
    }
  }

  public record BadQueryResponse(String response_type, String exception_message) {

    public BadQueryResponse(String exception_message) {
      this("202", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchCSVHandler.BadQueryResponse.class).toJson(this);
    }
  }

  public record BadJSONResponse(String response_type, String exception_message) {

    public BadJSONResponse(String exception_message) {
      this("203", exception_message);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchCSVHandler.BadJSONResponse.class).toJson(this);
    }
  }



}

