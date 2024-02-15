package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

public interface ChildQuery extends Query {
    Query getParentQuery();

}
