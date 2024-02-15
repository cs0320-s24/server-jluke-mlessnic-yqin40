package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.CSV.Census.Census;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCSVHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
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
            return new ViewCSVHandler.CSVContentSuccessResponse(censusListInfo).serialize();
        }


    }
}
