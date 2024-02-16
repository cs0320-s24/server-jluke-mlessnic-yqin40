package edu.brown.cs.student.ServerTesting;

import edu.brown.cs.student.main.Server.LoadCSVHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.HashSet;

public class LoadCSVHandlerTest {


    private Request mockRequest;
    private Response mockResponse;
    private LoadCSVHandler handler;

    @BeforeEach
    public void setUp() {
        System.out.println("Starting test for LoadCSVHandler");
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        handler = new LoadCSVHandler();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Test Complete");
    }

    @Test
    public void testLoadCsvWithCorrectFilePath() throws Exception {
        when(mockRequest.queryParams("filepath")).thenReturn("data/census.csv");
        when(mockRequest.queryParams()).thenReturn(new HashSet<>(Arrays.asList("filepath")));
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("success"));
    }

    @Test
    public void testLoadCsvWithMissingFilePath() throws Exception {
        when(mockRequest.queryParams("filepath")).thenReturn("");
        when(mockRequest.queryParams()).thenReturn(new HashSet<>(Arrays.asList("filepath")));
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("{\"response_type\":\"202\",\"exception_message\":\"error_bad_request: Empty File Path\"}"));
    }

    @Test
    public void testLoadCsvWithNonexistentFilePath() throws Exception {
        when(mockRequest.queryParams("filepath")).thenReturn("data/non_exist.csv");
        when(mockRequest.queryParams()).thenReturn(new HashSet<>(Arrays.asList("filepath")));
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("{\"response_type\":\"201\",\"exception_message\":\"Error_datasource: No file Found at data/non_exist.csv\"}"));
    }

    @Test
    public void testLoadCsvWithIncorrectFilePathParam() throws Exception {
        when(mockRequest.queryParams("filepat")).thenReturn("data/census.csv");
        when(mockRequest.queryParams()).thenReturn(new HashSet<>(Arrays.asList("filepat")));;
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("error_bad_json: Query parameter 'filepath' is missing or incorrect."));
    }
}

