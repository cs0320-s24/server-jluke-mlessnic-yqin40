package edu.brown.cs.student.QuerySearchTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.CLISearch.Exceptions.SearchArgumentException;
import edu.brown.cs.student.main.CLISearch.QuerySearch.QueryBuilder;
import org.junit.jupiter.api.Test;

public class NestedParsingTest {
  public NestedParsingTest() {}

  @Test
  public void simpleParsedQuery() throws SearchArgumentException {
    String text = "and(query1,query2)";
    QueryBuilder queryBuilder = new QueryBuilder(text);
    String s = queryBuilder.getTextBetweenParentheses(text);
    assertEquals(s, "query1,query2");
  }

  @Test
  public void nestedString() throws SearchArgumentException {
    String text = "and(or(a,b),c)";
    QueryBuilder queryBuilder = new QueryBuilder(text);
    String s = queryBuilder.getTextBetweenParentheses(text);
    assertEquals(s, "or(a,b),c");
  }

  @Test
  public void doubleNestedString() throws SearchArgumentException {
    String text = "and(not(a),or(b,c))";
    QueryBuilder queryBuilder = new QueryBuilder(text);
    String s = queryBuilder.getTextBetweenParentheses(text);
    assertEquals(s, "not(a),or(b,c)");
  }

  @Test
  public void doubleNestedString2() throws SearchArgumentException {
    String text = "and(not(a),or(b,c))";
    QueryBuilder queryBuilder = new QueryBuilder(text);
    String s = queryBuilder.getTextBetweenParentheses(text);
    assertEquals(s, "not(a),or(b,c)");
  }

  @Test
  public void unbalancedNestedString() throws SearchArgumentException {
    String text = "and(not(a),or(b,c)";
    QueryBuilder queryBuilder = new QueryBuilder(text);
    Throwable exception =
        assertThrows(
            SearchArgumentException.class,
            () -> queryBuilder.getTextBetweenParentheses(text),
            "unbalanced");
  }
}
