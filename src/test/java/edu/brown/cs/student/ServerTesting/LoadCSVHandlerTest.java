package edu.brown.cs.student.ServerTesting;

import edu.brown.cs.student.main.Server.LoadCSVHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

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
        when(mockRequest.queryParams("filepath")).thenReturn("correct/path/to/csv");
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("success"));
    }

    @Test
    public void testLoadCsvWithMissingFilePath() throws Exception {
        when(mockRequest.queryParams("filepath")).thenReturn(null);
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("error_bad_request"));
    }

    @Test
    public void testLoadCsvWithNonexistentFilePath() throws Exception {
        when(mockRequest.queryParams("filepath")).thenReturn("nonexistent/path/to/csv");
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("error_datasource"));
    }

    @Test
    public void testLoadCsvWithIncorrectFilePathParam() throws Exception {
        when(mockRequest.queryParams("filepat")).thenReturn("correct/path/to/csv");
        Object result = handler.handle(mockRequest, mockResponse);
        assertTrue(result.toString().contains("error_bad_json"));
    }
}

