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

import java.util.Stack;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class CountTest extends TestCase {
    private static IEnumerable<Object[]> Int_TestData() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), null, 0);

        argsList.add(Linq.of(new int[0]), isEvenFunc, 0);
        argsList.add(Linq.of(new int[]{4}), isEvenFunc, 1);
        argsList.add(Linq.of(new int[]{5}), isEvenFunc, 0);
        argsList.add(Linq.of(new int[]{2, 5, 7, 9, 29, 10}), isEvenFunc, 2);
        argsList.add(Linq.of(new int[]{2, 20, 22, 100, 50, 10}), isEvenFunc, 6);

        argsList.add(RepeatedNumberGuaranteedNotCollectionType(0, 0), null, 0);
        argsList.add(RepeatedNumberGuaranteedNotCollectionType(5, 1), null, 1);
        argsList.add(RepeatedNumberGuaranteedNotCollectionType(5, 10), null, 10);
        return argsList;
    }

    private static <T> IEnumerable<Object[]> EnumerateCollectionTypesAndCounts(int count, IEnumerable<T> enumerable) {
        Stack<T> stack = new Stack<>();
        enumerable.forEach(stack::push);

        ArgsList argsList = new ArgsList();
        argsList.add(count, enumerable);
        argsList.add(count, enumerable.toArray());
        argsList.add(count, Linq.of(enumerable.toList()));
        argsList.add(count, Linq.of(stack));
        return argsList;
    }

    private static IEnumerable<Object[]> CountsAndTallies() {
        int count = 5;
        IEnumerable<Integer> range = Linq.range(1, count);

        ArgsList argsList = new ArgsList();
        for (Object[] variant : EnumerateCollectionTypesAndCounts(count, range))
            argsList.add(variant);
        for (Object[] variant : EnumerateCollectionTypesAndCounts(count, range.select(i -> (float) i)))
            argsList.add(variant);
        for (Object[] variant : EnumerateCollectionTypesAndCounts(count, range.select(i -> (double) i)))
            argsList.add(variant);
        for (Object[] variant : EnumerateCollectionTypesAndCounts(count, range.select(i -> m(i))))
            argsList.add(variant);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(a -> a > Integer.MIN_VALUE);

        assertEquals(q.count(), q.count());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(a -> !IsNullOrEmpty(a));

        assertEquals(q.count(), q.count());
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void Int(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        if (predicate == null) {
            assertEquals(expected, source.count());
        } else {
            assertEquals(expected, source.count(predicate));
        }
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void IntRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        if (predicate == null) {
            assertEquals(expected, source.runOnce().count());
        } else {
            assertEquals(expected, source.runOnce().count(predicate));
        }
    }

    @Test
    void NullableIntArray_IncludesNullObjects() {
        Integer[] data = {-10, 4, 9, null, 11};
        assertEquals(5, Linq.of(data).count());
    }

    @ParameterizedTest
    @MethodSource("CountsAndTallies")
    <T> void CountMatchesTally(int count, IEnumerable<T> enumerable) {
        assertEquals(count, enumerable.count());
    }

    @ParameterizedTest
    @MethodSource("CountsAndTallies")
    <T> void RunOnce(int count, IEnumerable<T> enumerable) {
        assertEquals(count, enumerable.runOnce().count());
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Character>) null).count());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Character>) null).count(i -> i != 0));
        assertEquals(0, Linq.of((Character[]) null).count());
        assertEquals(0, Linq.of((Character[]) null).count(i -> i != 0));
    }

    @Test
    void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).count(predicate));
    }

    @Test
    void testCount() {
        int count = Linq.of(depts).count();
        assertEquals(3, count);
    }

    @Test
    void testCountPredicate() {
        int count = Linq.of(depts).count(dept -> dept.employees.size() > 0);
        assertEquals(2, count);
    }
}
