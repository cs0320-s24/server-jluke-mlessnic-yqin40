package edu.brown.cs.student.CSVParserTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import edu.brown.cs.student.main.CSV.CLISearch.Searcher;
import edu.brown.cs.student.main.CSV.CLISearch.QuerySearch.Searcher;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class User2Test {

  @BeforeEach
  public void setup() {
    System.out.println("Starting test for user story 2");
  }

  @AfterEach
  public void teardown() {
    System.out.println("Test complete");
  }

  @Test
  public void basicReader() throws IOException {
    String filepath = "data/census/dol_ri_earnings_disparity.csv";
    String searchVal = "Black";
    List<List<String>> output;

    try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
      CSVParser myParser = new CSVParser(br);
//      output = myParser.parseToStringsFromReader(br, true);
    }

//    Searcher mySearcher = new Searcher();
//    List<Integer> foundIndices = mySearcher.search(output);
//    List<Integer> trueIndices = List.of(2);
//    assertEquals(foundIndices, trueIndices);
  }
}
