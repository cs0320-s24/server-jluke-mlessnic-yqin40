package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;
import com.google.common.cache.*;
import com.google.common.graph.Graph;
import spark.Request;
import spark.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
"As a developer calling your web API, I can retrieve the percentage of households with broadband access
for a target location by providing the name of the target state and county in my request."

For CSCI 1340: incoming requests may also include a set of variables (taken from the official list) to be
requested from the ACS. Since the front-end is not under your control, it may provide any arbitrary values
as variable names, so consider error handling and the limitations of the ACS data carefully.

 As a backend developer who will be using your classes in my own server program, I have control over the
 caching of ACS request-responses – especially when entries will be removed, if at all, from the cache.
 I may also wish to perform other configuration or omit the cache entirely.
 Your documentation should tell me how to do all of this.

Acceptance Criteria: For this user story, you are required to cache information retrieved from the
ACS in order to avoid sending excessive network requests. A naive approach, like storing all responses
in a HashMap, is likely to run into memory issues. You will need to decide when and how to remove stale
(outdated or irrelevant) entries from your cache, and allow the developer stakeholder to control how this
 is done. Cache eviction should be automatic, yet configurable to adapt to different requirements.
 We recommend having a separate class that represents the ACS datasource, rather than incorporating it
 directly into your server’s handler. This will help tremendously, as it allows for more flexible and
 specific configuration of cache settings, independent of other server functionalities.

LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
       .expireAfterAccess(10, TimeUnit.MINUTES)
       .build(
           new CacheLoader<Key, Graph>() {
             public Graph load(Key key) { // no checked exception
               return createExpensiveGraph(key);
             }
           });

...
return graphs.getUnchecked(key);

 */

public class ProxyCache implements LocationDataFinder{

    LoadingCache<Request, Response> myCache;

    //note: after instantiation, you cannot change the max size or max time of the cache
    public ProxyCache(long maxSize, long maxTime, TimeUnit timeUnit) {

        // instantiate the cache
        this.myCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize) //evicts after a specified number of entries
                .expireAfterWrite(maxTime, timeUnit) //evicts after a specified amount of time has passed
                //.removalListener(MY_LISTENER)
                .build(
                        //we don't necessarily need this if we are adding completed key, value pairs
                        new CacheLoader<>() {
                            @Override
                            public Response load(Request key) throws Exception {
                                //could call the api from here to load a response
                                return null;
                            }
                        }

                        );

        //end of cache builder

    }

    @Override
    public LocationData find(String state, String county, String dataVars) {
        return null;
    }

    public Response getResponse(Request request) {
        try {
            return this.myCache.get(request);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCache(Request request, Response response) {
        this.myCache.put(request, response);
    }
}
