package edu.brown.cs.student.main.CSV.CSVParserLibrary;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.Exceptions.FactoryFailureException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/** object that takes in a csv, turns it into searchable rows */
public class CSVParser<T> {

  // from livecode example to split up rows
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  public String filepath;
  public Reader reader;
  public CreatorFromRow<T> creatorFromRow;
  public boolean hasHeaders;
  public boolean ignoreCase;
  public List<List<String>> data;


// constructor (user story 1)
  public CSVParser(String filepath, boolean hasHeaders, boolean ignoreCase) {
    this.filepath = filepath;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = ignoreCase;
  }

  public CSVParser(String filepath, boolean hasHeaders) {
    this.filepath = filepath;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = false;
  }

  public CSVParser(String filepath) {
    this.filepath = filepath;
    this.hasHeaders = false;
    this.ignoreCase = false;

  }

  // constructor (user story 2)
  public CSVParser(Reader reader, boolean hasHeaders, boolean ignoreCase) {
    this.reader = reader;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = ignoreCase;
  }
  public CSVParser(Reader reader, boolean hasHeaders) {
    this.reader = reader;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = false;

  }
  public CSVParser(Reader reader) {
    this.reader = reader;
    this.hasHeaders = false;
    this.ignoreCase = false;
  }

  // constructor (user story 3)
  public CSVParser(Reader reader, CreatorFromRow<T> creatorFromRow) {
    this.reader = reader;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = false;
    this.ignoreCase = false;
  }

  public CSVParser(Reader reader, CreatorFromRow<T> creatorFromRow, boolean hasHeaders) {
    this.reader = reader;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = false;
  }

  public CSVParser(Reader reader, CreatorFromRow<T> creatorFromRow, boolean hasHeaders, boolean ignoreCase) {
    this.reader = reader;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = ignoreCase;
  }

  public CSVParser(String filepath, CreatorFromRow<T> creatorFromRow) {
    this.filepath = filepath;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = false;
    this.ignoreCase = false;
  }

  public CSVParser(String filepath, CreatorFromRow<T> creatorFromRow, boolean hasHeaders) {
    this.filepath = filepath;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = false;
  }

  public CSVParser(String filepath, CreatorFromRow<T> creatorFromRow, boolean hasHeaders, boolean ignoreCase) {
    this.filepath = filepath;
    this.creatorFromRow = creatorFromRow;
    this.hasHeaders = hasHeaders;
    this.ignoreCase = ignoreCase;
  }


  /**
   * method to read the csv file located at the given filepath and returns the data as strings
   * input: filename (string)
   * output: List of list of strings that contain the csv data
   *
   *  code adapted from <a href="https://www.baeldung.com/java-csv-file-array">
   *
   */

  public List<List<String>> parseToStringsFromFilepath() {
    try (BufferedReader br = new BufferedReader(new FileReader(this.filepath))) {
      return createRowsFromReader(br);
    } catch (IOException e) { // TODO: figure out what to add here for the exception
      throw new RuntimeException(e);
    }
  }

  public List<List<String>> loadCSV() {
    data = this.parseToStringsFromFilepath();
    return data;
  }


  /* *method to read csv data as designated by a reader object
   *
   * @param reader
   * @param hasHeaders
   * @return List of List of strings that contain the csv data
   */
  private List<List<String>> parseToStringsFromReader(Reader reader, boolean hasHeaders) {
    try (BufferedReader br = new BufferedReader(reader)){
      return createRowsFromReader(br);
    } catch (IOException e) { // TODO: figure out what to add here for the exception
      throw new RuntimeException(e);
    }
  }

  public List<List<String>> loadCSV(Reader reader, boolean hasHeaders) {
    data = this.parseToStringsFromReader(reader, hasHeaders);
    return data;
  }

  /**
   *
   * @param br BufferedReader object
   * @return List of List of row data as strings
   * @throws IOException
   */
  public List<List<String>> createRowsFromReader(BufferedReader br) throws IOException {
    List<List<String>> rows = new ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
      String[] values = regexSplitCSVRow.split(line);
      for (int i=0; i<values.length; i++) {
        values[i] = this.postprocess(values[i]);
      }
      rows.add(Arrays.asList(values));
    }
    return rows;
  }


  //TODO: deal with malformed csv files

  /**
   *
   * @param reader Reader object that reads csv data
   * @param hasHeaders boolean to designate whether a csv has headers as the first row
   * @param creatorFromRow implementation to turn row data into a designated object type
   * @return List of objects
   */
  public List<T> parseIntoCSVRowObject(Reader reader, boolean hasHeaders, CreatorFromRow<T> creatorFromRow) {
    List<T> rows = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(reader)){
      String line;
      if(hasHeaders) {
        line = br.readLine(); //skip first line
      }

      while ((line = br.readLine()) != null) {
        String[] values = regexSplitCSVRow.split(line);
        for (int i=0; i<values.length; i++) {
          values[i] = this.postprocess(values[i]);
        }
        // String[] values = line.split(COMMA_DELIMITER);
        T newObject = creatorFromRow.create(Arrays.asList(values));
        rows.add(newObject);
      }
    } catch (IOException | FactoryFailureException e) { // TODO: figure out what to add here for the exception
      throw new RuntimeException(e);
    }
    return rows;
  }


  /**
   * Eliminate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   *
   * code from CSV handout example
   */
  public String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }

}
