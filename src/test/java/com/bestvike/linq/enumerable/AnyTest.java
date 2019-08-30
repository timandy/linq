package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class AnyTest extends TestCase {
    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        for (int count : new int[]{0, 1, 2}) {
            boolean expected = count > 0;

            Integer[] arr = new Integer[count];
            IEnumerable<Integer>[] collectionTypes = new IEnumerable[]{
                    Linq.of(arr),
                    Linq.of(Arrays.asList(arr)),
                    Linq.of(new LinkedList<>(Arrays.asList(arr))),
                    new TestCollection<>(arr),
                    NumberRangeGuaranteedNotCollectionType(0, count),
            };

            for (IEnumerable<Integer> source : collectionTypes) {
                argsList.add(source, expected);
                argsList.add(source.select(i -> i), expected);
                argsList.add(source.where(i -> true), expected);
                argsList.add(source.where(i -> false), false);
            }
        }
        return argsList;
    }

    private static IEnumerable<Object[]> TestDataWithPredicate() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.empty(), null, false);
        argsList.add(Linq.singleton(3), null, true);

        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        argsList.add(Linq.empty(), isEvenFunc, false);
        argsList.add(Linq.singleton(4), isEvenFunc, true);
        argsList.add(Linq.singleton(5), isEvenFunc, false);
        argsList.add(Linq.of(5, 9, 3, 7, 4), isEvenFunc, true);
        argsList.add(Linq.of(5, 8, 9, 3, 7, 11), isEvenFunc, true);

        Array<Integer> range = Linq.range(1, 10).toArray();
        argsList.add(range, (Predicate1<Integer>) i -> i > 10, false);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            argsList.add(range, (Predicate1<Integer>) i -> i > k, true);
        }
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Predicate1<Integer> predicate = TestCase::IsEven;
        assertEquals(q.any(predicate), q.any(predicate));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Predicate1<String> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.any(predicate), q.any(predicate));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void Any(IEnumerable<Integer> source, boolean expected) {
        assertEquals(expected, source.any());
    }

    @ParameterizedTest
    @MethodSource("TestDataWithPredicate")
    void Any(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        if (predicate == null)
            assertEquals(expected, source.any());
        else
            assertEquals(expected, source.any(predicate));
    }

    @ParameterizedTest
    @MethodSource("TestDataWithPredicate")
    void AnyRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        if (predicate == null)
            assertEquals(expected, source.runOnce().any());
        else
            assertEquals(expected, source.runOnce().any(predicate));
    }

    @Test
    void NullObjectsInArray_Included() {
        IEnumerable<Integer> source = Linq.of(null, null, null, null);
        assertTrue(source.any());
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any(i -> i != 0));
    }

    @Test
    void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).any(predicate));
    }

    @Test
    void testAny() {
        assertFalse(Linq.of(Collections.emptyList()).any());
        assertTrue(Linq.of(emps).any());
    }

    @Test
    void testAnyPredicate() {
        assertFalse(Linq.of(depts).any(dept -> dept.name != null && dept.name.equals("IT")));
        assertTrue(Linq.of(depts).any(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
