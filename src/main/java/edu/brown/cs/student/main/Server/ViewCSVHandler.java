package edu.brown.cs.student.main.Server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSV.Star.Star;  // 确保这是Star类的正确导入路径

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
    private final List<Star> catalog;

    public ViewCSVHandler(List<Star> catalog) {
        this.catalog = catalog;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<Map<String, Object>> starsInfo = new ArrayList<>();

        for (Star star : this.catalog) {
            Map<String, Object> starInfo = new HashMap<>();
            starInfo.put("StarID", star.StarID);
            starInfo.put("ProperName", star.ProperName);
            starInfo.put("X", star.X);
            starInfo.put("Y", star.Y);
            starInfo.put("Z", star.Z);
            starsInfo.add(starInfo);
        }

        return new CSVContentSuccessResponse(starsInfo).serialize();
    }

    /**
     * Success response structure for CSV content viewing.
     */
    public static class CSVContentSuccessResponse {
        private final String response_type;
        private final List<Map<String, Object>> starsInfo;

        public CSVContentSuccessResponse(List<Map<String, Object>> starsInfo) {
            this.response_type = "success";
            this.starsInfo = starsInfo;
        }

        public String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                Type listType = Types.newParameterizedType(List.class, Map.class);
                JsonAdapter<List<Map<String, Object>>> jsonAdapter = moshi.adapter(listType);
                return jsonAdapter.toJson(this.starsInfo);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        // Getters for private fields if needed
        public String getResponse_type() {
            return response_type;
        }

        public List<Map<String, Object>> getStarsInfo() {
            return starsInfo;
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

        // Getter for private field if needed
        public String getResponse_type() {
            return response_type;
        }
    }
}
