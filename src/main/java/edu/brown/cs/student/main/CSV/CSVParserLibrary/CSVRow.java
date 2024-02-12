package edu.brown.cs.student.main.CSV.CSVParserLibrary;

public interface CSVRow {
//same method names as List interface
     boolean contains(Object value);
     int indexOf(Object value);
//     int getColumnName(T value);
     Object get(int columnIndex);
//     Object getValueUnderName(String columnName);
}
