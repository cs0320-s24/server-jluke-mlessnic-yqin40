package edu.brown.cs.student.main.BroadbandDataAPI;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class shows a possible implementation of serializing and deserializing JSON from the
 * ACSCensusAPI into an CensusData object.
 */
public class BroadbandDataAPIUtilities {

  private BroadbandDataAPIUtilities() {}

  /**
   * Deserialize a json string back into a list. Each row should represent one county from which we
   * can gather "the percentage of households with broadband access for a target location"
   *
   * @param jsonList a json response from the census API containing the percent of broadband access
   *     by household for a given state and county
   * @return a list where each row is a particular location and the percent of broadband access for
   *     that location
   * @throws IOException thrown when the json provided is not in the correct format
   */
  public static List<LocationData> deserializeLocData(String jsonList) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type listType = Types.newParameterizedType(List.class, List.class, String.class);
      // Ends up getting a list of list of locationdata, not just a list of locationdata
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);
      List<List<String>> nestedList = Objects.requireNonNull(adapter.fromJson(jsonList));

      // Convert state list back into a list of locationdata
      List<LocationData> locationDataList = new ArrayList<>();
      // Get rid of header row if there
      if (nestedList.get(0).get(0).equals("NAME")) {
        nestedList.remove(0);
      }
      for (List<String> innerList : nestedList) {
        // System.out.println("Inner list size: " + innerList.size());

        switch (innerList.size()) { // Ensure inner list has at least two elements (NAME and state)
          case 2:
            locationDataList.add(new LocationData(innerList.get(0), innerList.get(1), "", 0.0f));
            break;
          case 3:
            locationDataList.add(
                new LocationData(innerList.get(0), innerList.get(1), innerList.get(2), 0.0f));
            break;
          case 4:
            locationDataList.add(
                new LocationData(
                    innerList.get(0),
                    innerList.get(2),
                    innerList.get(3),
                    Float.parseFloat(innerList.get(1))));
            break;
            // TODO: add default case for unexpected sizes
        }
      }
      return locationDataList;

    } catch (IOException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("BroadbandHandler: string wasn't valid JSON.");
      throw e;
    } catch (JsonDataException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("BroadbandHandler: JSON wasn't in the right format.");
      throw e;
    }
  }

  /**
   * This method converts a list of broadband data information back into a json string. It may be
   * particularly useful when sending the information to the user endpoint
   *
   * @param LocData a list where each row is one location's broadband access information
   * @return a json-formatted string built from the given list of broadband access data
   */
  public static String serializeLocData(List<LocationData> LocData) {
    Moshi moshi = new Moshi.Builder().build();
    Type listOfLocDataType = Types.newParameterizedType(List.class, LocationData.class);
    JsonAdapter<List<LocationData>> adapter = moshi.adapter(listOfLocDataType);
    return adapter.toJson(LocData);
  }
}
