package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

public interface ParentQuery extends Query {
    ChildQuery createChildQuery();
    ChildQuery getChildQuery1();
    ChildQuery getChildQuery2();

}
