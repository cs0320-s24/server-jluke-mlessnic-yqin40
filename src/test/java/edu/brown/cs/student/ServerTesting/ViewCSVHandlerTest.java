package edu.brown.cs.student.ServerTesting;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.Census.Census;
import edu.brown.cs.student.main.Server.Server;
import edu.brown.cs.student.main.Server.ViewCSVHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.io.TempDir;
import spark.Request;
import spark.Response;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.util.List;
import java.io.FileNotFoundException;

public class ViewCSVHandlerTest {

    private ViewCSVHandler handler;
    private Request mockRequest;
    private Response mockResponse;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        handler = new ViewCSVHandler();
        System.out.println("Starting test for ViewCSVHandler");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Test Complete");
    }

    @Test
    public void testViewCSVWithLoadedData(@TempDir Path tempDir) throws Exception {
        Path csvPath = tempDir.resolve("C:/Users/huo00/Documents/CSCI1340/server-jluke-mlessnic-yqin40/data/census.csv");
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            CSVParser<Census> csvParser = new CSVParser<>(reader, false, false);
            List<Census> censusList = csvParser.parseIntoCSVRowObject(reader, true, row -> {
                Census census = new Census();
                census.setCity(row.get(0));
                census.setMedianHouseholdIncome(row.get(1));
                census.setMedianFamilyIncome(row.get(2));
                census.setPerCapitalIncome(row.get(3));
                return census;
            });

            Server.setcensusList(censusList);
        }

        Object result = handler.handle(mockRequest, mockResponse);
        System.out.println(result.toString());
        assertTrue(result.toString().contains("Median Family Income\":\"95,198.00\",\"City/Town\":\"Rhode Island\",\"Per Capital Income\":\"39,603.00\",\"Median Household Income\":\"74,489.00\"}"));
    }

}

