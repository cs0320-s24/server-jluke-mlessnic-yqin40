package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;

public class ProxyCache implements LocationDataFinder{
    public ProxyCache() {
    }

    @Override
    public LocationData find(String state, String county) {
        return null;
    }
}
