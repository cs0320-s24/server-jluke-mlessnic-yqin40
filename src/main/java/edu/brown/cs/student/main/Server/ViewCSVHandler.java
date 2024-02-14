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

/**
 * Handler class for viewing CSV content via API endpoint.
 */
public class ViewCSVHandler implements Route {

//    BufferedReader br = new BufferedReader(new FileReader(filepath));
//    CSVParser<Star> parser = new CSVParser<>(br, (List<String> row) -> {
//        Star star = new Star();
//        star.setStarID(row.get(0));
//        star.setProperName(row.get(1));
//        star.setX(row.get(2));
//        star.setY(row.get(3));
//        star.setZ(row.get(4));
//        return star;
//    }, true, false);
//
//    List<Star> stars = parser.parseIntoCSVRowObject(br, true, (List<String> row) -> {
//        Star star = new Star();
//        star.setStarID(row.get(0));
//        star.setProperName(row.get(1));
//        star.setX(row.get(2));
//        star.setY(row.get(3));
//        star.setZ(row.get(4));
//        return star;
//    });

    public ViewCSVHandler() throws FileNotFoundException {
    }

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
            return "No data available. Please load a CSV file first.";
        } else {
            return new CSVContentSuccessResponse(censusListInfo).serialize();
        }
    }

    /**
     * Success response structure for CSV content viewing.
     */
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

        public String getResponse_type() {
            return response_type;
        }
        public List<Map<String, Object>> getcensusListinfo() {
            return censusListinfo;
        }
    }

    /**
     * Failure response structure for CSV content viewing when no match is found.
     */
    public static class CSVContentNoMatchFailureResponse {
        private final String response_type;

        public CSVContentNoMatchFailureResponse() {
            this.response_type = "error";
        }

        public String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<CSVContentNoMatchFailureResponse> jsonAdapter = moshi.adapter(CSVContentNoMatchFailureResponse.class);
            return jsonAdapter.toJson(this);
        }

        public String getResponse_type() {
            return response_type;
        }
    }
}
