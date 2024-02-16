package edu.brown.cs.student.ServerTesting;

import edu.brown.cs.student.main.Server.BroadbandDataHandler;
import edu.brown.cs.student.main.Server.LoadCSVHandler;
import edu.brown.cs.student.main.Server.SearchCSVHandler;
import edu.brown.cs.student.main.Server.ViewCSVHandler;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerTestingUtilities {

    public static void setup_before_everything() {
        // Set the Spark port number. This can only be done once, and has to
        // happen before any route maps are added. Hence using @BeforeClass.
        // Setting port 0 will cause Spark to use an arbitrary available port.
        Spark.port(0);
        // Don't try to remember it. Spark won't actually give Spark.port() back
        // until route mapping has started. Just get the port number later. We're using
        // a random _free_ port to remove the chances that something is already using a
        // specific port on the system used for testing.

        // Remove the logging spam during tests
        //   This is surprisingly difficult. (Notes to self omitted to avoid complicating things.)

        // SLF4J doesn't let us change the logging level directly (which makes sense,
        //   given that different logging frameworks have different level labels etc.)
        // Changing the JDK *ROOT* logger's level (not global) will block messages
        //   (assuming using JDK, not Log4J)
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    public static void setupSpark() throws Exception {

        // In fact, restart the entire Spark server for every test!
        Spark.get("loadcsv", new LoadCSVHandler());
        Spark.get("viewcsv", new ViewCSVHandler());
        Spark.get("searchcsv", new SearchCSVHandler());
//        Spark.get("broadband", new BroadbandDataHandler()); //TODO ask micah about making the constructor public

        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening

    }

    public static void teardownSpark() {
        // Gracefully stop Spark listening on both endpoints after each test
        Spark.unmap("loadcsv");
        Spark.unmap("viewcsv");
        Spark.unmap("searchcsv");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Helper to start a connection to a specific API endpoint/params
     *
     * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
     *     structure!)
     * @return the connection for the given URL, just after connecting
     * @throws IOException if the connection fails for some reason
     */
    public static HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        // The default method is "GET", which is what we're using here.
        // If we were using "POST", we'd need to say so.
        clientConnection.setRequestMethod("GET");

        clientConnection.connect();
        return clientConnection;
    }
}
