package edu.brown.cs.student.main.BroadbandDataAPI;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.ArrayList;
import java.util.List;

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
  private final List<String> data;

  /**
   * Construct a location data object with the given fields
   *
   * @param NAME the plain english name of the location (either county, state or just state)
   * @param state the internal state code returned by a call to the census api
   * @param county the internal county code returned by a call to the census api
   * @param data a list where each element is one of the fields requested by the user
   */
  public LocationData(String NAME, List<String> data, String state, String county) {
    this.NAME = NAME;
    this.state = state;
    this.county = county;
    this.data = new ArrayList<>(data);
  }

  /**
   * getter for the name field
   *
   * @return retrieve the NAME field
   */
  public String getNAME() {
    return this.NAME;
  }

  /**
   * getter for the state field
   *
   * @return retrieve the state field
   */
  public String getState() {
    return this.state;
  }

  /**
   * getter for the county field
   *
   * @return retrieve the county field
   */
  public String getCounty() {
    return this.county;
  }

  /**
   * getter for the broadband percentage field (unused)
   *
   * @return retrieve the broadband field as a float
   */
  public List<String> getData() {
    return this.data;
  }

  /**
   * convert the object back into a list of strings, used for moshi serialization because moshi does
   * not know how to serialize the LocationData object directly but does know how to handle a list
   * of strings
   *
   * @return the object as a list of strings, in the same order as it appears in the census api
   *     (NAME, broadband, state, county)
   */
  public List<String> toList() {
    List<String> returnList = new ArrayList<>(this.data);
    // Add the name first, then data, then the state and county code
    returnList.add(0, this.NAME);
    returnList.add(this.state);
    returnList.add(this.county);
    return returnList;
  }
}
