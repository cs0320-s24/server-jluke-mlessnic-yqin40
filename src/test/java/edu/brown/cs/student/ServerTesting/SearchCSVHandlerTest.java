package edu.brown.cs.student.ServerTesting;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.Server.SearchCSVHandler;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchCSVHandlerTest {

    @BeforeAll


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
        HttpURLConnection clientConnection = ServerTestingUtilities.tryRequest(apiRequest); //needs to be the whole order name
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(200, clientConnection.getResponseCode()); //success code
    }

    @Test
    public void simpleSearch() throws IOException {
        this.loadCsvHelper();

        String searchVal = "39,603.00"; //should be first row
        String apiRequest = "searchcsv?search="+searchVal;
        HttpURLConnection clientConnection = ServerTestingUtilities.tryRequest(apiRequest);
        assertEquals(200, clientConnection.getResponseCode()); //success code


        Moshi moshi = new Moshi.Builder().build();

        // We'll use okio's Buffer class here
        System.out.println(clientConnection.getInputStream());
        SearchCSVHandler.CSVSearchSuccessResponse response =
                moshi.adapter(SearchCSVHandler.CSVSearchSuccessResponse.class).fromJson(new
                        Buffer().readFrom(clientConnection.getInputStream()));
//
//        Soup carrot = new Soup("Carrot", Arrays.asList("carrot", "onion", "celery",
//                "garlic" , "ginger", "vegetable broth"), false);
//        Map<String, Object> result = (Map<String, Object>) response.responseMap().get("Carrot");
//        System.out.println(result.get("ingredients"));
//        assertEquals(carrot.getIngredients(), result.get("ingredients"));
        clientConnection.disconnect();
    }
}

