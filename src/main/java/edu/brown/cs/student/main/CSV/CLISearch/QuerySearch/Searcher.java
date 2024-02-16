package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

import java.util.ArrayList;
import java.util.List;

public class Searcher {

  List<List<String>> rowsAsStringList;
  boolean hasHeaders;
  boolean ignoreCase;

  // default constructor
  //  public Searcher() {}

  public Searcher(List<List<String>> rows, boolean hasHeaders, boolean ignoreCase) {
    this.rowsAsStringList = rows;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = ignoreCase;
  }

  /**
   * method that searches csvs that have been converted to lists of strings
   *
   * @param searchVal string to search for
   * @return List of indices that the value is found in
   */
  public List<Integer> search(String searchVal) {
    ArrayList<Integer> indicesFound = new ArrayList<>();
    for (int i = 0; i < this.rowsAsStringList.size(); i++) {
      if (this.searchRow(this.rowsAsStringList.get(i), searchVal)) {
        indicesFound.add(i);
      }
    }
    if (this.hasHeaders && indicesFound.contains(0)) {
      indicesFound.remove(0);
    }
    //    System.out.println("Searcher.search("+searchVal+") complete");
    //    System.out.println(indicesFound);
    return indicesFound;
  }

  public List<Integer> searchColumn(String searchVal, String columnIdentifier) {
    if (isNumeric(columnIdentifier)) {
      int columnIndex = Integer.parseInt(columnIdentifier);
      return this.searchColumnIndex(searchVal, columnIndex); // returned from search
    } else {
      return this.searchColumnName(searchVal, columnIdentifier); // returned from search
    }
  }

  public List<Integer> searchUsingQuery(String text) {
    QueryBuilder rootQueryBuilder = new QueryBuilder(text);
    Query rootQuery = rootQueryBuilder.build();
    return rootQuery.findRows(this);
  }

  public List<List<String>> getRowsAsStringList() {
    return this.rowsAsStringList;
  }

  // list of possible row indices a value could be found in
  public List<Integer> getPool() {
    List<Integer> integerList = new ArrayList<>();
    int numRows = this.rowsAsStringList.size();
    for (int i = 1; i <= numRows; i++) {
      integerList.add(i);
    }
    return integerList;
  }

  // helper functions

  private List<Integer> searchColumnIndex(String searchVal, Integer columnIndex) {
    ArrayList<Integer> indicesFound = new ArrayList<>();
    for (int i = 0; i < this.rowsAsStringList.size(); i++) {
      if (compareStrings(
          this.rowsAsStringList.get(i).get(columnIndex), searchVal, this.ignoreCase)) {
        indicesFound.add(i);
      }
    }
    if (this.hasHeaders && indicesFound.contains(0)) {
      indicesFound.remove(0);
    }
    return indicesFound;
  }

  private List<Integer> searchColumnName(String searchVal, String columnName) {
    if (this.hasHeaders) {
      List<String> headers = this.rowsAsStringList.get(0);
      int index = headers.indexOf(columnName);
      if (index == -1) {
        // throw error because name is not in list
      }
      return searchColumnIndex(searchVal, index);
    }
    // throw error because name can't be used if no headers
    return null;
  }

  // Method to check if a string is numeric

  /**
   * method to see if a string is a number, used to
   *
   * @param str string to test if its a number
   * @return true if a String can be read as a number
   */
  public static boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      // If the string cannot be parsed as an integer, it's not a number
      return false;
    }
  }

  /**
   * helper method to find a string value in a row
   *
   * @param rowVals string items in the row
   * @param searchVal string to search for
   * @return true if item is found
   */
  private boolean searchRow(List<String> rowVals, String searchVal) {
    for (String rowVal : rowVals) {
      if (compareStrings(rowVal, searchVal, this.ignoreCase)) {
        return true;
      }
    }
    return false;
  }

  /**
   * helper method to compare strings based on whether case should be ignored
   *
   * @param s1 string 1
   * @param s2 string 2
   * @param ignoreCase boolean true is ignore the case of letters in a string
   * @return true if strings are the same
   */
  private boolean compareStrings(String s1, String s2, boolean ignoreCase) {
    if (ignoreCase) {
      return s1.equalsIgnoreCase(s2);
    } else {
      return s1.equals(s2);
    }
  }
}
