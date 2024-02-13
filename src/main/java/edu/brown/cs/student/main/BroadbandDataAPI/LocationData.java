package edu.brown.cs.student.main.BroadbandDataAPI;

/**
 * A class to hold the location information to later build a map of location names to
 * internal codes, which can then be used to query the census api for the broadband data
 * itself. This object will also hold the broadband data directly so the deserializer can
 * just return a list of this object type all at once
 */
public class LocationData {
  private final String NAME;
  private final String state;
  private final String county;
  private final float S2802_C03_022E;

  public LocationData(String name, String state, String county, float broadBand){
    this.NAME = name;
    this.state = state;
    this.county = county;
    this.S2802_C03_022E = broadBand;
  }

  public String getNAME(){
    return this.NAME;
  }

  public String getState(){
    return this.state;
  }

  public String getCounty(){
    return this.county;
  }

  public float getBroadBand(){
    return this.S2802_C03_022E;
  }
}
