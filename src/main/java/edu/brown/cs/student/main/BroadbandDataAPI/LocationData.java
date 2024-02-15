package edu.brown.cs.student.main.BroadbandDataAPI;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

/**
 * A class to hold the location information to later build a map of location names to internal
 * codes, which can then be used to query the census api for the broadband data itself. This object
 * will also hold the broadband data directly so the deserializer can just return a list of this
 * object type all at once
 */
@JsonClass(generateAdapter = true)
public class LocationData {
  @Json(name = "NAME")
  private final String NAME;

  @Json(name = "state")
  private final String state;

  @Json(name = "county")
  private final String county;

  @Json(name = "S2802_C03_022E")
  private final float S2802_C03_022E;

  public LocationData(String NAME, String state, String county, float S2802_C03_022E) {
    this.NAME = NAME;
    this.state = state;
    this.county = county;
    this.S2802_C03_022E = S2802_C03_022E;
  }

  public String getNAME() {
    return this.NAME;
  }

  public String getState() {
    return this.state;
  }

  public String getCounty() {
    return this.county;
  }

  public float getBroadBand() {
    return this.S2802_C03_022E;
  }
}
