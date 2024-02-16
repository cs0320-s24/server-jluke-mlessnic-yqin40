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

//  @Test
//  public void andNestedOrTest() {
//    String filepath = "data/census.csv";
//    String text = "and(or(Bristol,Wakerly),'80,727.00')" ;
//    String[] args = {filepath, text, "-q"};
//
//    List<Integer> foundIndices = new Main(args).run();
//
//    List<Integer> trueIndices = List.of(6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
//
//    assertEquals(foundIndices, trueIndices);
//  }
}
