package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

import edu.brown.cs.student.main.CSV.CLISearch.QuerySearch.Exceptions.SearchArgumentException;
import java.util.List;

public class ColumnQuery implements Query {
  String searchVal;
  String columnIdentifier;
  String text;
  Searcher searcher;

  public ColumnQuery() {}

  public ColumnQuery(String text) {
    this.text = text;

    // find search val and column id
    try {
      this.parseText(text);
    } catch (SearchArgumentException e) {
      throw new RuntimeException(e);
    }
  }

  public ColumnQuery(Query parentQuery) {}

  @Override
  public void parseText(String text) throws SearchArgumentException {
    if (text.matches(".*[\\(\\)].*")) {
      throw new SearchArgumentException("Nested arguments are not permitted within ColumnQuery");
    }

    String[] parts = this.text.split(",");

    this.searchVal = parts[0];
    this.columnIdentifier = parts[1];

    if (parts.length > 2) {
      throw new SearchArgumentException("Too many arguments for this type of query");
    }
  }

  @Override
  public List<Integer> findRows(Searcher searcher) {
    return searcher.searchColumn(this.searchVal, this.columnIdentifier);
  }

  @Override
  public String getText() {
    return null;
  }

  //    @Override
  //    public Query getParentQuery() {
  //        return this.parentQuery;
  //    }

}
