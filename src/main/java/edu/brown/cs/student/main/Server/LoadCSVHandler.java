package edu.brown.cs.student.main.Server;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.Census.Census;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class LoadCSVHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Server.filepath = request.queryParams("filepath");
//        Server.filepath = "data/stardata.csv";


        if (request.queryParams().contains("filepath")) {
            if (Server.filepath == null || Server.filepath.isEmpty()) {
                return new NoFilePathResponse("error_bad_request: Empty File Path").serialize();
            }
        } else {
            return new BadJSONResponse("error_bad_json: Query parameter 'filepath' is missing or incorrect.").serialize();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(Server.filepath))) {
            CSVParser<Census> parser = new CSVParser<>(br, (List<String> row) -> {
                Census census = new Census();
                census.setCity(row.get(0));
                census.setMedianFamilyIncome(row.get(1));
                census.setMedianFamilyIncome(row.get(2));
                census.setPerCapitalIncome(row.get(3));
                return census;
            }, true, false);
            List<Census> censusList = parser.parseIntoCSVRowObject(br, true, (List<String> row) -> {
                Census census = new Census();
                census.setCity(row.get(0));
                census.setMedianFamilyIncome(row.get(1));
                census.setMedianFamilyIncome(row.get(2));
                census.setPerCapitalIncome(row.get(3));
                return census;
            });
            Server.setcensusList(censusList);
            return new SuccessResponse("CSV file loaded successfully at : " + Server.filepath).serialize();
        } catch (FileNotFoundException e) {
            return new CSVFileNotFoundResponse("Error_datasource: " + Server.filepath).serialize();
        } catch (Exception e) {
            e.printStackTrace();
            return new FileInaccessible("File inaccessible: " + Server.filepath).serialize();
        }
    }

    public record CSVFileNotFoundResponse(String response_type, String exception_message) {

        public CSVFileNotFoundResponse(String exception_message) {
            this("201", exception_message);
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(LoadCSVHandler.CSVFileNotFoundResponse.class).toJson(this);
        }
    }

    public record NoFilePathResponse(String response_type, String exception_message) {

        public NoFilePathResponse(String exception_message) {
            this("202", exception_message);
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(LoadCSVHandler.NoFilePathResponse.class).toJson(this);
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
            return moshi.adapter(LoadCSVHandler.BadJSONResponse.class).toJson(this);
        }
    }

    public record FileInaccessible (String response_type, String exception_message) {

        public FileInaccessible(String exception_message) {
            this("204", exception_message);
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(LoadCSVHandler.FileInaccessible.class).toJson(this);
        }
    }

    public record SuccessResponse(String response_type, String message) {

        public SuccessResponse(String message) {
            this("success", message);
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(LoadCSVHandler.SuccessResponse.class).toJson(this);
        }
    }


}

