package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2019-05-07.
 */
class ElementAtOrDefaultTest extends TestCase {
    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 1), 0, 9);
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 10), 9, 18);
        argsList.add(NumberRangeGuaranteedNotCollectionType(-4, 10), 3, -1);

        argsList.add(Linq.of(new int[]{1, 2, 3, 4}), 4, null);
        argsList.add(Linq.of(new int[0]), 0, null);
        argsList.add(Linq.of(new int[]{-4}), 0, -4);
        argsList.add(Linq.of(new int[]{9, 8, 0, -5, 10}), 4, 10);

        argsList.add(NumberRangeGuaranteedNotCollectionType(-4, 5), -1, null);
        argsList.add(NumberRangeGuaranteedNotCollectionType(5, 5), 5, null);
        argsList.add(NumberRangeGuaranteedNotCollectionType(0, 0), 0, null);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.elementAtOrDefault(3), q.elementAtOrDefault(3));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.elementAtOrDefault(4), q.elementAtOrDefault(4));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAtOrDefault(IEnumerable<Integer> source, int index, Integer expected) {
        assertEquals(expected, source.elementAtOrDefault(index));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAtOrDefaultRunOnce(IEnumerable<Integer> source, int index, Integer expected) {
        assertEquals(expected, source.runOnce().elementAtOrDefault(index));
    }

    @Test
    void NullableArray_NegativeIndex_ReturnsNull() {
        Integer[] source = {9, 8};
        assertNull(Linq.of(source).elementAtOrDefault(-1));
    }

    @Test
    void NullableArray_ValidIndex_ReturnsCorrectObject() {
        Integer[] source = {9, 8, null, -5, 10};

        assertNull(Linq.of(source).elementAtOrDefault(2));
        assertEquals(-5, Linq.of(source).elementAtOrDefault(3));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAtOrDefault(2));
    }

    @Test
    void testElementAtOrDefault() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("jimi", enumerable.elementAtOrDefault(0));
        assertNull(enumerable.elementAtOrDefault(2));
        assertNull(enumerable.elementAtOrDefault(-1));

        IEnumerable<Long> enumerable2 = Linq.of(new CountIterable(2));
        assertEquals(1L, enumerable2.elementAtOrDefault(0));
        assertNull(enumerable2.elementAtOrDefault(2));
        assertNull(enumerable2.elementAtOrDefault(-1));
    }
}
