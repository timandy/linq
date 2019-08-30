package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class DefaultIfEmptyTest extends TestCase {
    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), null, new Integer[]{null});
        argsList.add(Linq.of(new int[0]), 0, new Integer[]{0});
        argsList.add(Linq.of(new int[]{3}), 0, new Integer[]{3});
        argsList.add(Linq.of(new int[]{3, -1, 0, 10, 15}), 0, new Integer[]{3, -1, 0, 10, 15});

        argsList.add(Linq.of(new int[0]), -10, new Integer[]{-10});
        argsList.add(Linq.of(new int[]{3}), 9, new Integer[]{3});
        argsList.add(Linq.of(new int[]{3, -1, 0, 10, 15}), 9, new Integer[]{3, -1, 0, 10, 15});
        argsList.add(Linq.empty(), 0, new Integer[]{0});
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsNonEmptyQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(a -> a > Integer.MIN_VALUE);

        assertEquals(q.defaultIfEmpty(5), q.defaultIfEmpty(5));
    }

    @Test
    void SameResultsRepeatCallsEmptyQuery() {
        IEnumerable<Integer> q = NumberRangeGuaranteedNotCollectionType(0, 0).select(a -> a);

        assertEquals(q.defaultIfEmpty(88), q.defaultIfEmpty(88));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void DefaultIfEmpty(IEnumerable<Integer> source, Integer defaultValue, Integer[] expected) {
        IEnumerable<Integer> result;
        if (defaultValue == null) {
            result = source.defaultIfEmpty();
            assertEquals(result, result);
            assertEquals(Linq.of(expected), result);
            assertEquals(expected.length, result.count());
            assertEquals(Linq.of(expected), Linq.of(result.toList()));
            assertEquals(Linq.of(expected), result.toArray());
        }
        result = source.defaultIfEmpty(defaultValue);
        assertEquals(result, result);
        assertEquals(Linq.of(expected), result);
        assertEquals(expected.length, result.count());
        assertEquals(Linq.of(expected), Linq.of(result.toList()));
        assertEquals(Linq.of(expected), result.toArray());
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void DefaultIfEmptyRunOnce(IEnumerable<Integer> source, Integer defaultValue, Integer[] expected) {
        if (defaultValue == null) {
            assertEquals(Linq.of(expected), source.runOnce().defaultIfEmpty());
        }

        assertEquals(Linq.of(expected), source.runOnce().defaultIfEmpty(defaultValue));
    }

    @Test
    void NullableArray_Empty_WithoutDefaultValue() {
        Integer[] source = new Integer[0];
        assertEquals(Linq.of(new Integer[]{null}), Linq.of(source).defaultIfEmpty());
    }

    @Test
    void NullableArray_Empty_WithDefaultValue() {
        Integer[] source = new Integer[0];
        Integer defaultValue = 9;
        //noinspection RedundantArrayCreation
        assertEquals(Linq.of(new Integer[]{defaultValue}), Linq.of(source).defaultIfEmpty(defaultValue));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).defaultIfEmpty());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).defaultIfEmpty(42));
        assertThrows(ArgumentNullException.class, () -> Linq.of((Integer[]) null).defaultIfEmpty());
        assertThrows(ArgumentNullException.class, () -> Linq.of((Integer[]) null).defaultIfEmpty(42));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).defaultIfEmpty();
        // Don't insist on this behaviour, but check it's correct if it happens
        //noinspection unchecked
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void testDefaultIfEmpty() {
        List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        IEnumerable<String> notEmptyEnumerable = Linq.of(experience).defaultIfEmpty();
        IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        assertEquals("noel", notEmptyEnumerator.current());

        IEnumerable<String> emptyEnumerable = Linq.of(Linq.<String>empty()).defaultIfEmpty();
        IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        assertTrue(emptyEnumerator.moveNext());
        assertNull(emptyEnumerator.current());
        assertFalse(emptyEnumerator.moveNext());
    }

    @Test
    void testDefaultIfEmptyWithDefaultValue() {
        List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        IEnumerable<String> notEmptyEnumerable = Linq.of(experience).defaultIfEmpty("dummy");
        IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        assertEquals("noel", notEmptyEnumerator.current());

        IEnumerable<String> emptyEnumerable = Linq.of(Linq.<String>empty()).defaultIfEmpty("N/A");
        IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        assertTrue(emptyEnumerator.moveNext());
        assertEquals("N/A", emptyEnumerator.current());
        assertFalse(emptyEnumerator.moveNext());
    }
}
