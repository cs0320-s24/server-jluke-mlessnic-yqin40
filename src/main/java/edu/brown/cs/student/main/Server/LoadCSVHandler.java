package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.CSV.Star.Star;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.*;

public class LoadCSVHandler implements Route {
    private List<Star> stars;

    public LoadCSVHandler() {

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
