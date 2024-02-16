package edu.brown.cs.student.main.Server;
import com.google.common.cache.Cache;
import com.google.common.graph.Graph;
import edu.brown.cs.student.main.BroadbandDataAPI.BroadbandDataAPIUtilities;
import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


/*
This class goes between the Broadband Handler and the API/Cache. Every time the Broadband handler makes a request,
this class will check to see if the item is already in the cache. If it isn't already in the cache, then ping the API.
 */
public class ACSProxy {

    public ProxyCache proxyCache;
    public APICaller apiCaller;

    //constructor
    public ACSProxy() throws IOException, URISyntaxException, InterruptedException {
        this.proxyCache = new ProxyCache();
        this.apiCaller = new APICaller();
    }

    public LocationData getFromAPI(String state, String county) {
        return this.apiCaller.find(state, county);
    }

    public LocationData getFromCache(String state, String county){
        return this.proxyCache.find(state, county);

    }

    public void addToCache(String state, String county) {
        LocationData result = this.getFromAPI(state, county);
        // add to proxyCache
    }


}
