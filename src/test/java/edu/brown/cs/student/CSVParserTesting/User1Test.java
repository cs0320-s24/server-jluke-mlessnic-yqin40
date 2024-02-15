package edu.brown.cs.student.CSVParserTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.CLISearch.Main;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class User1Test {

  @BeforeEach
  public void setup() {
    System.out.println("Starting test for user story 1");
  }

  @AfterEach
  public void teardown() {
    System.out.println("Test complete");
  }

  @Test
  public void simpleFindNoColumnID() {
    String filepath = "data/census/dol_ri_earnings_disparity.csv";
    String searchVal = "Black";
    String[] args = {filepath, searchVal};

    List<Integer> foundIndices = new Main(args).run();

    List<Integer> trueIndices = List.of(2);

    assertEquals(foundIndices, trueIndices);
  }
}
