package extensions.modules.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class RandomUtilsTest {

  @Test
  void sampleWithoutReplacemenFromRangeShouldThrowIllegalArgumentExceptionWhenNIsEqualTo0() {
    // Given
    int n = 0;

    // when
    Throwable thrown =
        catchThrowable(
            () -> {
              RandomUtils.sampleWithoutReplacementFromRange(0, 4, n);
            });

    // then
    assertThat(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("n must be a positive integer");
  }

  @Test
  void
      sampleWithoutReplacementFromRangeThrowIllegalArgumentExceptionWhenNIsEqualToNIsLargerThanInterval() {
    // Given

    // when
    Throwable thrown =
        catchThrowable(
            () -> {
              RandomUtils.sampleWithoutReplacementFromRange(0, 4, 5);
            });

    // then
    assertThat(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("n can't be larger than provided interval");
  }

  @Test
  void sampleWithoutReplacementFromRangeThrowIllegalArgumentExceptionWhenEndIsSmallerThanEnd() {
    // Given

    // when
    Throwable thrown =
        catchThrowable(
            () -> {
              RandomUtils.sampleWithoutReplacementFromRange(4, 3, 2);
            });

    // then
    assertThat(thrown)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("end can't be smaller than start");
  }

  @Test
  void sampleWithoutReplacementFromRangeShouldReturnListWithExactlyNNumbers() {
    // Given
    int n = 4;

    // When
    List<Integer> result = RandomUtils.sampleWithoutReplacementFromRange(0, 4, n);
    // Then
    assertThat(result.size()).isEqualTo(n);
  }

  @Test
  void
      sampleWithoutReplacementFromRangeShouldReturnListWithTheSameEntriesAsRangeIfNIsEqualToSizeOfInterval() {
    // Given
    int n = 4;

    // When
    List<Integer> result = RandomUtils.sampleWithoutReplacementFromRange(0, n, n);
    ;
    // Then
    assertThat(result.stream().sorted().collect(Collectors.toList())).isEqualTo(List.of(0,1,2,3));
  }

  @Test
  void testSampleWithoutReplacement() {}
}
