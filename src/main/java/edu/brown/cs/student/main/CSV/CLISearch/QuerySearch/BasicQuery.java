package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

import java.util.List;

public class BasicQuery implements Query {
  String searchVal;

  public BasicQuery() {}

  public BasicQuery(String text) {
    this.parseText(text);
  }

  @Override
  public String getText() {
    return this.searchVal;
  }

  @Override
  // sets parameters for searching
  public void parseText(String text) {
    this.searchVal = text;
  }

  @Override
  public List<Integer> findRows(Searcher searcher) {
    return searcher.search(this.searchVal);
  }
}
