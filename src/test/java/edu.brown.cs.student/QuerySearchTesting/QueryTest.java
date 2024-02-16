package edu.brown.cs.student.QuerySearchTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import edu.brown.cs.student.main.CSV.CLISearch.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryTest {

  @BeforeEach
  public void setup() {
    System.out.println("Starting test for querys");
  }

  @AfterEach
  public void teardown() {
    System.out.println("Test complete");
  }

  @Test
  public void orTest() {
    String filepath = "data/census.csv";
    String text = "or(Barrington,Coventry)";
    String[] args = {filepath, text, "-q"};

    List<Integer> foundIndices = new Main(args).run();
    List<Integer> trueIndices = List.of(2,7);
    assertEquals(foundIndices, trueIndices);
  }

  @Test
  public void andNestedOrTest() {
    String filepath = "data/stardata.csv";
    String text = "and(or(1,5),Zita)" ;
    String[] args = {filepath, text, "-q"};

    List<Integer> foundIndices = new Main(args).run();

    List<Integer> trueIndices = List.of(6);

    assertEquals(foundIndices, trueIndices);
  }
}
