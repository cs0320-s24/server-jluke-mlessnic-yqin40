package edu.brown.cs.student.main.Server;

import edu.brown.cs.student.main.BroadbandDataAPI.LocationData;

public interface LocationDataFinder {

  public LocationData find(String state, String county, String dataVars);
}
