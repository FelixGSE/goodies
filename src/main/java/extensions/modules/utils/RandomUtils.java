package extensions.modules.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomUtils {

  public static List<Integer> sampleWithoutReplacementFromRange(int start, int end, int n) {
    if (n < 1) {
      throw new IllegalArgumentException("n must be a positive integer");
    } else if (end < start) {
      throw new IllegalArgumentException("end can't be smaller than start");
    } else if (n > (end - start)) {
      throw new IllegalArgumentException("n can't be larger than provided interval");
    }
    return new Random().ints(start, end).distinct().limit(n).boxed().collect(Collectors.toList());
  }

  public static int zeroOrOne(){
    if(Math.random() > 0.5){
      return 0;
    } else {
      return 1;
    }
  }

  public static List<Integer> sampleWithoutReplacementFromRange(
      List<Integer> listOfIntegers, int n) {
    Collections.shuffle(listOfIntegers);
    return listOfIntegers.stream().limit(n).collect(Collectors.toList());
  }
}
