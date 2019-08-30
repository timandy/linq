package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by 许崇雷 on 2019-05-20.
 */
class LongCountTest extends TestCase {
    private static IEnumerable<Object[]> LongCount_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), null, 0L);
        argsList.add(Linq.of(new int[]{3}), null, 1L);
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        argsList.add(Linq.of(new int[0]), isEvenFunc, 0L);
        argsList.add(Linq.of(new int[]{4}), isEvenFunc, 1L);
        argsList.add(Linq.of(new int[]{5}), isEvenFunc, 0L);
        argsList.add(Linq.of(new int[]{2, 5, 7, 9, 29, 10}), isEvenFunc, 2L);
        argsList.add(Linq.of(new int[]{2, 20, 22, 100, 50, 10}), isEvenFunc, 6L);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.longCount(), q.longCount());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.longCount(), q.longCount());
    }

    @ParameterizedTest
    @MethodSource("LongCount_TestData")
    void LongCount(IEnumerable<Integer> source, Predicate1<Integer> predicate, long expected) {
        if (predicate == null) {
            assertEquals(expected, source.longCount());
        } else {
            assertEquals(expected, source.longCount(predicate));
        }
    }

    @ParameterizedTest
    @MethodSource("LongCount_TestData")
    void LongCountRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, long expected) {
        if (predicate == null) {
            assertEquals(expected, source.runOnce().longCount());
        } else {
            assertEquals(expected, source.runOnce().longCount(predicate));
        }
    }

    @Test
    void NullableArray_IncludesNullValues() {
        Integer[] data = {-10, 4, 9, null, 11};
        assertEquals(5, Linq.of(data).longCount());
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).longCount());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).longCount(i -> i != 0));
    }

    @Test
    void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).longCount(predicate));
    }

    @Test
    void testLongCount() {
        long count = Linq.of(depts).longCount();
        assertEquals(3, count);

        long count2 = Linq.of(new CountIterable(10)).longCount();
        assertEquals(10, count2);
    }

    @Test
    void testLongCountPredicate() {
        long count = Linq.of(depts).longCount(dept -> dept.employees.size() > 0);
        assertEquals(2, count);

        long count2 = Linq.of(new CountIterable(10L)).longCount(s -> s > 9);
        assertEquals(1, count2);
    }
}
