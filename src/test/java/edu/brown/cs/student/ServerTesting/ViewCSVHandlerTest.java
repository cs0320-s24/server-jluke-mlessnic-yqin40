package edu.brown.cs.student.ServerTesting;

import edu.brown.cs.student.main.Server.ViewCSVHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

import java.io.FileNotFoundException;

public class ViewCSVHandlerTest {

    private Request mockRequest;
    private Response mockResponse;
    private ViewCSVHandler handler;

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
    public void testViewCSVWithLoadedData() throws Exception {
        Object result = handler.handle(mockRequest, mockResponse);
        String expectedDataFragment = "\"City/Town\":\"Barrington\",\"Per Capital Income\":\"69,917.00\"";
        String resultString = result.toString();
        assertTrue(resultString.contains(expectedDataFragment),
                "Expected data fragment not found in the response.");
    }

    @Test
    public void testViewCSVWithNoData() throws Exception {
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("No data loaded"));
    }

    @Test
    public void testViewCSVErrorHandling() throws Exception {
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("Error"));
    }

}

