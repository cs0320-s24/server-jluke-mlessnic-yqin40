package edu.brown.cs.student.ServerTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Server.SearchCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchCSVHandlerTest {

  @BeforeEach
  public void setup() {
    System.out.println("Starting test for SearchCSVHandler");
    try {
      ServerTestingUtilities.setupSpark();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Re-initialize state, etc. for _every_ test method run
    //        this.menu.clear();
  }

  @AfterEach
  public void teardown() {
    System.out.println("Test complete");
  }

  private void loadCsvHelper() throws IOException {
    String apiRequest = "loadcsv?filepath=data/census.csv";
    HttpURLConnection clientConnection =
        ServerTestingUtilities.tryRequest(apiRequest); // needs to be the whole order name
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, clientConnection.getResponseCode()); // success code
  }

  @Test
  public void simpleSearch() throws IOException {
    this.loadCsvHelper();

    String searchVal = "Barrington"; // should be first row
    String apiRequest = "searchcsv?find=" + searchVal;
    HttpURLConnection clientConnection = ServerTestingUtilities.tryRequest(apiRequest);

    assertEquals(200, clientConnection.getResponseCode()); // success code

    Moshi moshi = new Moshi.Builder().build();

    // We'll use okio's Buffer class here
    //System.out.println(clientConnection.getInputStream());
    SearchCSVHandler.SuccessSearchResponse response =
        moshi
            .adapter(SearchCSVHandler.SuccessSearchResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

            Map<String, Object> result = response.responseMap();
            System.out.println(result);
//            assertEquals(result.get("result"), List.of(List.of("Barrington", "130,455.00", "154,441.00", "69.917.00")));
    clientConnection.disconnect();
  }

  @Test
  public void simpleQuerySearch() throws IOException {
    this.loadCsvHelper();

    String searchVal = "or(Providence,Barrington)"; // should be first row
    String apiRequest = "searchcsv?query=" + searchVal;
    HttpURLConnection clientConnection = ServerTestingUtilities.tryRequest(apiRequest);

    assertEquals(200, clientConnection.getResponseCode()); // success code

    Moshi moshi = new Moshi.Builder().build();

    // We'll use okio's Buffer class here
    //System.out.println(clientConnection.getInputStream());
    SearchCSVHandler.SuccessSearchResponse response =
            moshi
                    .adapter(SearchCSVHandler.SuccessSearchResponse.class)
                    .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    Map<String, Object> result = response.responseMap();
    System.out.println(result);
//            assertEquals(result.get("result"), List.of(List.of("Barrington", "130,455.00", "154,441.00", "69.917.00")));
    clientConnection.disconnect();
  }
}
