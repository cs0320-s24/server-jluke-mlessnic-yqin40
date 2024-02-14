package edu.brown.cs.student.main.Server;

import static spark.Spark.after;

//import edu.brown.cs.student.main.soup.Soup;
//import edu.brown.cs.student.main.soup.SoupAPIUtilities;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.main.CSV.CSVParserLibrary.CSVParser;
import edu.brown.cs.student.main.CSV.Census.Census;
import edu.brown.cs.student.main.CSV.Star.Star;
import spark.Spark;

/**
 * Top-level class for server project. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 */
public class Server {
    // TODO 0: Read through this class and determine the shape of this project...

  /*
  create port for localhost at designated number
  read in json data that the end user sends to to our server
  send request to soup api
  get information back from soup api and save it as menu
  Server class creates OrderHandler for GET requests
  Server class creates ActivityHandler for activity endpoints
   */

    public static final String CONSTANT1 = "value1";
    public static String filepath;
    private static List<Census> censusList = new ArrayList<>();

    public static void setcensusList(List<Census> censusList) {
        Server.censusList = censusList;
    }

    public static List<Census> getcensusList() {
        return Server.censusList;
    }



    // What are the endpoints that we can access... What happens if you go to them?
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        int port = 3232;
        Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
        after(
                (request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Access-Control-Allow-Methods", "*");
                });

        // Sets up data needed for the OrderHandler. You will likely not read from local
        // JSON in this sprint.

        //from gearup

//        String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
//        List<Soup> menu = new ArrayList<>();
//        try {
//            menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
//        } catch (Exception e) {
//            // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but gets
//            // the job done in the gearup where it is not the focus.
//            e.printStackTrace();
//            System.err.println("Errored while deserializing the menu");
//        }  String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
//        List<Soup> menu = new ArrayList<>();
//        try {
//            menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
//        } catch (Exception e) {
//            // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but gets
//            // the job done in the gearup where it is not the focus.
//            e.printStackTrace();
//            System.err.println("Errored while deserializing the menu");
//        }

        // Setting up the handler for the GET /order and /activity endpoints
//        Spark.get("order", new OrderHandler(menu)); //end point is called order, use OrderHandler handle what comes after order endpoint
//        Spark.get("activity", new ActivityHandler()); //end point is called activity, use ActivityHandler to access stuff after the end point
        //NOTE: This is the part that you will write yourself!

        //endpoints


        Spark.get("loadcsv", new LoadCSVHandler());
        Spark.get("viewcsv", new ViewCSVHandler());
        Spark.get("searchcsv", new SearchCSVHandler());
        Spark.get("broadband", new BroadbandDataHandler());

        // Set up a route to handle GET requests to the root URL "/"
        Spark.get("/", (req, res) -> "Hello, World!");
        // Set up another route to handle GET requests to a specific path "/hello"
        Spark.get("/hello", (req, res) -> "Hello, Spark!");


        Spark.init();
        Spark.awaitInitialization();

        // Notice this link alone leads to a 404... Why is that?
        System.out.println("Server started at http://localhost:" + port);
    }
}
