package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.Star.Star;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class LoadCSVHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // 从请求中获取文件路径参数
        Server.filepath = request.queryParams("filepath");
//        Server.filepath = "data/stardata.csv";


        // 检查文件路径是否提供
        if (Server.filepath == null || Server.filepath.isEmpty()) {
            return "No CSV file path provided.";
        }

        // 使用提供的文件路径解析CSV文件
        try (BufferedReader br = new BufferedReader(new FileReader(Server.filepath))) {
            CSVParser<Star> parser = new CSVParser<>(br, (List<String> row) -> {
                Star star = new Star();
                star.setStarID(row.get(0));
                star.setProperName(row.get(1));
                star.setX(row.get(2));
                star.setY(row.get(3));
                star.setZ(row.get(4));
                return star;
            }, true, false);
            List<Star> stars = parser.parseIntoCSVRowObject(br, true, (List<String> row) -> {
                Star star = new Star();
                star.setStarID(row.get(0));
                star.setProperName(row.get(1));
                star.setX(row.get(2));
                star.setY(row.get(3));
                star.setZ(row.get(4));
                return star;
            });
            Server.stars = stars;
            return "CSV file loaded successfully.";
        } catch (FileNotFoundException e) {
            return "CSV file not found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error loading CSV file.";
        }
    }
}

