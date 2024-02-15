package edu.brown.cs.student.QuerySearchTesting;

import edu.brown.cs.student.main.CLISearch.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
    public void andTest() {
        String filepath = "data/census/income_by_race.csv";
        String text = "and(Black,2020)";
        String[] args = {filepath, text, "-q"};

        List<Integer> foundIndices = new Main(args).run();
        List<Integer> trueIndices = List.of(11, 12, 13, 14, 15);
        assertEquals(foundIndices, trueIndices);
    }

    @Test
    public void andNestedOrTest() {
        String filepath = "data/census/income_by_race.csv";
        String text = "and(or(White,Black),2020)";
        String[] args = {filepath, text, "-q"};

        List<Integer> foundIndices = new Main(args).run();

        List<Integer> trueIndices = List.of(6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        assertEquals(foundIndices, trueIndices);
    }
}
