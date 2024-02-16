package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch.Exceptions;

public class SearchArgumentException extends Exception {

  public SearchArgumentException(String message) {
    super(message);
  }
}

// public class FactoryFailureException extends Exception {
//    final List<String> row;
//
//    public FactoryFailureException(String message, List<String> row) {
//        super(message);
//        this.row = new ArrayList<>(row);
//    }
// }
