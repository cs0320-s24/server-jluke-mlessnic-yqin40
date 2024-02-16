package edu.brown.cs.student.CSVParserTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVRow;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.CreatorFromRow;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.Exceptions.FactoryFailureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class User3Test {

  @BeforeEach
  public void setup() {
    System.out.println("Starting test for user story 3");
  }

  @AfterEach
  public void teardown() {
    System.out.println("Test complete");
  }
//
//  @Test
//  public void objectCreationTest() throws IOException {
//    String filepath = "data/census/dol_ri_earnings_disparity.csv";
//    String searchVal = "Black";
//    List<Demographic> output;
//
//    try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
//      CSVParser<Demographic> myParser = new CSVParser<Demographic>(br);
//      CreatorFromRow<Demographic> myCreator = new DemographicFromRow();
//      output = myParser.parseIntoCSVRowObject(br, true, myCreator);
//    }
//
//    Demographic correctInfo = new Demographic("RI", "White", 1058.47, 395773.6521, 1.00, .75);
//    Demographic objectInfo = output.get(0);
//
//    assertEquals(objectInfo, correctInfo);
//  }
//}
//
//class Demographic implements CSVRow {
//
//  //    State,Data Type,Average Weekly Earnings,Number of Workers,Earnings Disparity,Employed
//  // Percent
//  //    RI,White," $1,058.47 ",395773.6521, $1.00 ,75%
//
//  public String state;
//  public String dataType;
//  public Double avgWeeklyEarnings;
//  public Double numWorkers;
//  public Double earningDisparity;
//  public Double percentEmployed;
//
//  public Demographic(
//      String state,
//      String dataType,
//      double avgWeeklyEarnings,
//      double numWorkers,
//      double earningDisparity,
//      double percentEmployed) {
//    this.state = state;
//    this.dataType = dataType;
//    this.avgWeeklyEarnings = avgWeeklyEarnings;
//    this.numWorkers = numWorkers;
//    this.earningDisparity = earningDisparity;
//    this.percentEmployed = percentEmployed;
//  }
//
//  @Override
//  public boolean equals(Object obj) {
//
//    if (this == obj) {
//      return true;
//    }
//
//    if (obj == null || getClass() != obj.getClass()) {
//      return false;
//    }
//
//    Demographic d = (Demographic) obj;
//
//    if (this.state.equals(d.state)
//        && this.dataType.equals(d.dataType)
//        && this.avgWeeklyEarnings.equals(d.avgWeeklyEarnings)
//        && this.numWorkers.equals(d.numWorkers)
//        && this.earningDisparity.equals(earningDisparity)
//        && this.percentEmployed.equals(d.percentEmployed)) {
//      return true;
//    } else {
//
//      return false;
//    }
//  }
//
//  @Override
//  public boolean contains(Object value) {
//    return false;
//  }
//
//  @Override
//  public int indexOf(Object value) {
//    return 0;
//  }
//
//  @Override
//  public Object get(int columnIndex) {
//    return null;
//  }
//}
//
//class DemographicFromRow implements CreatorFromRow<Demographic> {
//  public DemographicFromRow() {}
//
//  @Override
//  public Demographic create(List<String> row) throws FactoryFailureException {
//    String state = row.get(0);
//    String dataType = row.get(1);
//    double avgWeeklyEarnings = dollarStringToDouble(row.get(2));
//    double numWorkers = dollarStringToDouble(row.get(3));
//    double earningDisparity = dollarStringToDouble(row.get(4));
//    double percentEmployed = percentStringToDouble(row.get(5));
//    return new Demographic(
//        state, dataType, avgWeeklyEarnings, numWorkers, earningDisparity, percentEmployed);
//  }
//
//  private double dollarStringToDouble(String dollarString) {
//    String cleanedString = dollarString.replace("$", "");
//    cleanedString = cleanedString.replace(",", "");
//
//    return Double.parseDouble(cleanedString);
//  }
//
//  private double percentStringToDouble(String percentString) {
//    String cleanedString = percentString.replace("%", "");
//    return Double.parseDouble(cleanedString) / 100;
//  }
}
