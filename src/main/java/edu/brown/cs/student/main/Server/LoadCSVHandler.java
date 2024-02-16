package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.Census.Census;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Server.filepath = request.queryParams("filepath");
    //        Server.filepath = "data/stardata.csv";

    if (Server.filepath == null || Server.filepath.isEmpty()) {
      return "No CSV file path provided.";
    }

    try (BufferedReader br = new BufferedReader(new FileReader(Server.filepath))) {
      CSVParser<Census> parser =
          new CSVParser<>(
              br,
              (List<String> row) -> {
                Census census = new Census();
                census.setCity(row.get(0));
                census.setMedianFamilyIncome(row.get(1));
                census.setMedianFamilyIncome(row.get(2));
                census.setPerCapitalIncome(row.get(3));
                return census;
              },
              true,
              false);
      List<Census> censusList =
          parser.parseIntoCSVRowObject(
              br,
              true,
              (List<String> row) -> {
                Census census = new Census();
                census.setCity(row.get(0));
                census.setMedianFamilyIncome(row.get(1));
                census.setMedianFamilyIncome(row.get(2));
                census.setPerCapitalIncome(row.get(3));
                return census;
              });
      Server.setcensusList(censusList);
      return "CSV file loaded successfully.";
    } catch (FileNotFoundException e) {
      return "CSV file not found.";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error loading CSV file.";
    }
  }
}
