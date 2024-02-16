package edu.brown.cs.student.CSVParserTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.brown.cs.student.main.CSV.CLISearch.QuerySearch.Searcher;
import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
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
    String filepath = "data/census.csv";
    String searchVal = "Barrington";
    List<List<String>> output;

    try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
      CSVParser<List<String>> myParser = new CSVParser<>(br);
      output = myParser.parseToStringsFromReader(br, true);
    }

    Searcher mySearcher = new Searcher(output, true, false);
    List<Integer> foundIndices = mySearcher.search(searchVal);
    List<Integer> trueIndices = List.of(2);
    assertEquals(foundIndices, trueIndices);
  }
}
