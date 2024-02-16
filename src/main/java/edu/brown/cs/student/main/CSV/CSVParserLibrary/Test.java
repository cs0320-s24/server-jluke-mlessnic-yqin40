package edu.brown.cs.student.main.CSV.CSVParserLibrary;

import edu.brown.cs.student.main.CSV.Star.Star;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Test {
  public static void main(String[] args) throws FileNotFoundException {
    String filepath = "ten-star.csv";
    // 创建 CSVParser 实例，指定 filepath，假设 CSV 文件第一行是表头

    BufferedReader br = new BufferedReader(new FileReader(filepath));

    CSVParser<Star> parser =
        new CSVParser<>(
            br,
            (List<String> row) -> {
              Star star = new Star();
              star.setStarID(row.get(0));
              star.setProperName(row.get(1));
              star.setX(row.get(2));
              star.setY(row.get(3));
              star.setZ(row.get(4));
              return star;
            },
            true,
            false);

    // 解析CSV数据
    List<Star> result =
        parser.parseIntoCSVRowObject(
            br,
            true,
            (List<String> row) -> {
              Star star = new Star();
              star.setStarID(row.get(0));
              star.setProperName(row.get(1));
              star.setX(row.get(2));
              star.setY(row.get(3));
              star.setZ(row.get(4));
              return star;
            });
  }
}
