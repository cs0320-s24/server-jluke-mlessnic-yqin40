package edu.brown.cs.student.main.CSV.CLISearch.Exceptions;

public class SearchArgumentErrorException extends Exception{

    public SearchArgumentErrorException(String message) {
        super(message);
    }

}


//public class FactoryFailureException extends Exception {
//    final List<String> row;
//
//    public FactoryFailureException(String message, List<String> row) {
//        super(message);
//        this.row = new ArrayList<>(row);
//    }
//}