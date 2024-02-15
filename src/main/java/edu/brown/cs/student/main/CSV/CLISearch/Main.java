package edu.brown.cs.student.main.CSV.CLISearch;

import edu.brown.cs.student.main.CLISearch.QuerySearch.Query;
import edu.brown.cs.student.main.CSVParserLibrary.CSVParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main class of our project. This is where execution begins. Runs the command line interface
 * for the user.
 */
public final class Main {

  //constants
  public static final String HEADER_FLAG = "-h";
  public static final String QUERY_FLAG = "-q";
  public static final String CASE_FLAG = "-c";
  public static final String DASH = "-";


  //class variables
  private boolean hasHeaders;
  private boolean hasQuery;
  private boolean ignoreCase;
  private final ArrayList<String> arguments;


  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {

    new Main(args).run();
    System.exit(0);
  }


  /**
   * constructor for class main
   *
   * @param args arguments from command line
   */
  public Main(String[] args) {
    this.hasHeaders = false;
    this.hasQuery = false;
    this.ignoreCase = false;

    this.arguments = this.parseForFlags(args); //also resets hasHeaders and hasQuery if necessary
  }

  @NotNull
  public List<Integer> run() {
    this.checkArgs(this.arguments);
    System.out.println("ARGUMENTS: "+this.arguments);
    System.out.println("FLAGS: hasHeaders="+this.hasHeaders+" ignoreCase="+this.ignoreCase+" query="+this.hasQuery);
    String filepath = arguments.get(0);

    CSVParser<String> csvParser = new CSVParser<>(filepath, this.hasHeaders);
    List<List<String>> parsedRows = csvParser.parseToStringsFromFilepath();
    // public Searcher(List<List<String>> rows,  boolean hasHeaders, boolean ignoreCase) {

    Searcher searcher = new Searcher(parsedRows, this.hasHeaders, this.ignoreCase);
    String searchVal = arguments.get(1);


    List<Integer> foundRowIndices = new ArrayList<>();

    if (this.hasQuery) {
      System.out.println("SEARCHING CSV " + filepath + " FOR QUERY: " + searchVal);
      if (arguments.size() > 2) {
        System.out.println("ERROR: too many args for query. Make sure there are no spaces in the query");
      }
      foundRowIndices = searcher.searchUsingQuery(searchVal);

    } else {
      System.out.println("SEARCHING CSV " + filepath + " FOR " + searchVal);

      if (arguments.size() > 2) {
        String columnIdentifier = arguments.get(2);
        searcher.searchColumn(searchVal, columnIdentifier);
      } else {
      foundRowIndices = searcher.search(searchVal); //returned from search
      }
    }

    System.out.println("CSV SEARCH COMPLETE");
    if (!foundRowIndices.isEmpty()) {
      System.out.println("ITEM " + searchVal + " found in row(s) " + foundRowIndices);
      for (Integer foundRowIndex : foundRowIndices) {
        System.out.println(parsedRows.get(foundRowIndex));
      }
    } else {
      System.out.println("ITEM " + searchVal + " not found.");
    }

    return foundRowIndices;
  }

    /**
     * helper method to check number of arguments and whether the filepath is valid
     * @param arguments String passed via CLI after being processed by main
     */
    private void checkArgs (@NotNull ArrayList<String> arguments) {
      int numArgs = arguments.size();
      if (numArgs < 1) {
        // TODO: throw error for not enough arguments
        System.out.println("ERROR: please input a filepath to desired csv.");
        System.exit(0);
        return;
      }

      if (numArgs < 2) {
        // TODO: throw error for not enough arguments
        System.out.println("ERROR: please input a value to search for");
        System.exit(0);
      }

      //check if filepath is in the correct directory
      String filepath = arguments.get(0);
      File file = new File(filepath);
      String absoluteFilepath = file.getAbsolutePath();
      String projectDirectory = System.getProperty("user.dir");
      boolean isInProjectDirectory = absoluteFilepath.startsWith(projectDirectory);
      if (!isInProjectDirectory) {
        System.out.println("ERROR: file must be within current directory");
        System.exit(0);
      }

      // check if filepath is valid
      if (!file.isFile()) {
        System.out.println("ERROR: file does not exist");
        System.exit(0);
      }
    }

    /**
     * helper method to parse arguments for flags
     * take args, read flags if found, return args without flags as an array list
     * @param args arguments from command line interface
     */
    private ArrayList<String> parseForFlags (String[]args){
      ArrayList<String> argsWithoutFlags = new ArrayList<>();
        assert args != null;
        for (String arg:args) {
        if (arg.equals(HEADER_FLAG)) {
          this.hasHeaders = true;
        } else if (arg.equals(QUERY_FLAG)) {
          this.hasQuery = true;
        } else if (arg.equals(CASE_FLAG)) {
          this.ignoreCase = true;
        } else if (arg.startsWith(DASH)){
          System.out.println("ERROR: flag not accepted"); //TODO throw error?
        } else {
          argsWithoutFlags.add(arg);
        }
      }
      return argsWithoutFlags;
    }

    private Query searchUsingQuery (String text){
      return null;
    }





}



