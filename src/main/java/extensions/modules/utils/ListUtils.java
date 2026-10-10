package extensions.modules.utils;

import java.util.*;

public class ListUtils {

  public static List<List<Float>> deepCopyListOfListOfFloats(final List<List<Float>> inputList) {
    List<List<Float>> output = new ArrayList<List<Float>>();
    for (List<Float> listOfFloats : inputList) {
      output.add(new ArrayList<>(listOfFloats));
    }
    return output;
  }



}
