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

  public List<List<String>> getCsvRowsAsList() {
    return new ArrayList<>(this.csvRowsAsList);
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    // check if file is loaded
    // get rows
    this.csvRows = Server.getcensusList();
    for (Census c : csvRows) {
      // convert back into strings
      this.csvRowsAsList.add(c.turnIntoRawStrings());
    }

    if (csvRows.isEmpty()) {
      return new CSVSearchFailureResponse().serialize();
      // return "No data available. Please load a CSV file first.";
    }

    Searcher searcher =
        new Searcher(this.csvRowsAsList, true, false); // save hasHeaders and ignoreCase in Server

    Set<String> queryParams = request.queryParams();
    if (queryParams.size() > 1) {
      return new CSVSearchFailureResponse().serialize();
    }

    // check if search value is provided
    if (queryParams.contains("find")) {
      String searchVal = request.queryParams("find");
      if ((searchVal == null) || searchVal.isEmpty()) {
        return new CSVSearchFailureResponse().serialize();
        // return "No item to search for provided.";
      } else {
        // perform basic search
        return new CSVSearchSuccessResponse(searcher.search(searchVal), this.csvRowsAsList)
            .serialize();
      }

    } else if (queryParams.contains("query")) {
      String query = request.queryParams("query");
      if (query == null || query.isEmpty()) {
        return new CSVSearchFailureResponse().serialize();
        // return "No query to search for provided.";
      } else {
        // perform query search
        return new CSVSearchSuccessResponse(searcher.searchUsingQuery(query), this.csvRowsAsList)
            .serialize();
      }
    } else {
      return new CSVSearchFailureResponse().serialize();
    }
  }

  /** Success response structure for CSV content searching. */
  public static class CSVSearchSuccessResponse {
    private final String response_type;
    private final List<List<String>> foundRows;

    public CSVSearchSuccessResponse(
        List<Integer> foundRowIndices, List<List<String>> csvRowsAsList) {
      this.response_type = "success";
      this.foundRows = new ArrayList<List<String>>();
      for (int ind : foundRowIndices) {
        this.foundRows.add(csvRowsAsList.get(ind));
      }
    }

    public String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        Type listType = Types.newParameterizedType(List.class, Map.class);
        JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(listType);
        return jsonAdapter.toJson(this.foundRows);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }

    public String getResponse_type() {
      return response_type;
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
}
