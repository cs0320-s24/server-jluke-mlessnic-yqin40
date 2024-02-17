package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import spark.Request;

/*
This class goes between the Broadband Handler and the API/Cache. Every time the Broadband handler makes a request,
this class will check to see if the item is already in the cache. If it isn't already in the cache, then ping the API.
 */

/**
 * Proxy for ACS api calls, allowing caching to be used if so desired
 */
public class ACSProxy {

  public ProxyCache proxyCache;
  public APICaller apiCaller;

  // constructor

  /**
   * Constructor
   * @throws IOException thrown from api requests
   * @throws URISyntaxException thrown from api requests
   * @throws InterruptedException thrown from api requests
   */
  public ACSProxy() throws IOException, URISyntaxException, InterruptedException {
    this.proxyCache = new ProxyCache(10000, 60, TimeUnit.SECONDS);
    this.apiCaller = new APICaller();
  }

  /**
   * Check the cache for the location, if not found then ping the actual api
   * @param request given by handler
   * @return locaitondata object associated with that request
   * @throws ExecutionException thrown if cache crashes rather than returns null
   */
  public LocationData get(Request request) throws ExecutionException {
    LocationData returnLoc = proxyCache.getLoc(request);
    // No corresponding key found in the cache, go to the apicaller to deal with this
    // then load the request and object into the cache
    if (returnLoc == null){
      String state = request.queryParams("state");
      String county = request.queryParams("county");
      String[] optionalGet = request.queryParamsValues("get");
      String fullGet = null;
      // Only populate fullGet if it was not left empty
      if (!(optionalGet == null || optionalGet.length == 0)){
        fullGet = String.join(",", optionalGet);
      }
      // State and county must never be null
      assert state != null;
      assert county != null;
      returnLoc = apiCaller.find(state, county, fullGet);
      proxyCache.addToCache(request, returnLoc);
    }
    return returnLoc;
  }

}
