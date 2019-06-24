package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class CountTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(a -> a > Integer.MIN_VALUE);

        assertEquals(q.count(), q.count());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(a -> !IsNullOrEmpty(a));

        assertEquals(q.count(), q.count());
    }

    private IEnumerable<Object[]> Int_TestData() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;

        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(new int[0]), null, 0},

                new Object[]{Linq.asEnumerable(new int[0]), isEvenFunc, 0},
                new Object[]{Linq.asEnumerable(new int[]{4}), isEvenFunc, 1},
                new Object[]{Linq.asEnumerable(new int[]{5}), isEvenFunc, 0},
                new Object[]{Linq.asEnumerable(new int[]{2, 5, 7, 9, 29, 10}), isEvenFunc, 2},
                new Object[]{Linq.asEnumerable(new int[]{2, 20, 22, 100, 50, 10}), isEvenFunc, 6},

                new Object[]{RepeatedNumberGuaranteedNotCollectionType(0, 0), null, 0},
                new Object[]{RepeatedNumberGuaranteedNotCollectionType(5, 1), null, 1},
                new Object[]{RepeatedNumberGuaranteedNotCollectionType(5, 10), null, 10}
        );
    }

    @Test
    public void Int() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.Int((IEnumerable<Integer>) objects[0], (Predicate1<Integer>) objects[1], (int) objects[2]);
    }

    private void Int(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        if (predicate == null) {
            assertEquals(expected, source.count());
        } else {
            assertEquals(expected, source.count(predicate));
        }
    }

    @Test
    public void IntRunOnce() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.IntRunOnce((IEnumerable<Integer>) objects[0], (Predicate1<Integer>) objects[1], (int) objects[2]);
    }

    private void IntRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        if (predicate == null) {
            assertEquals(expected, source.runOnce().count());
        } else {
            assertEquals(expected, source.runOnce().count(predicate));
        }
    }

    @Test
    public void NullableIntArray_IncludesNullObjects() {
        Integer[] data = {-10, 4, 9, null, 11};
        assertEquals(5, Linq.asEnumerable(data).count());
    }

    private <T> IEnumerable<Object[]> EnumerateCollectionTypesAndCounts(int count, IEnumerable<T> enumerable) {
        Stack<T> stack = new Stack<>();
        enumerable.forEach(stack::push);

        return Linq.asEnumerable(
                new Object[]{count, enumerable},
                new Object[]{count, enumerable.toArray()},
                new Object[]{count, Linq.asEnumerable(enumerable.toList())},
                new Object[]{count, Linq.asEnumerable(stack)}
        );
    }

    private IEnumerable<Object[]> CountsAndTallies() {
        int count = 5;
        IEnumerable<Integer> range = Linq.range(1, count);

        List<Object[]> list = new ArrayList<>();
        for (Object[] variant : this.EnumerateCollectionTypesAndCounts(count, range))
            list.add(variant);
        for (Object[] variant : this.EnumerateCollectionTypesAndCounts(count, range.select(i -> (float) i)))
            list.add(variant);
        for (Object[] variant : this.EnumerateCollectionTypesAndCounts(count, range.select(i -> (double) i)))
            list.add(variant);
        for (Object[] variant : this.EnumerateCollectionTypesAndCounts(count, range.select(i -> new BigDecimal(i))))
            list.add(variant);
        return Linq.asEnumerable(list);
    }

    @Test
    public void CountMatchesTally() {
        for (Object[] objects : this.CountsAndTallies())
            //noinspection unchecked
            this.CountMatchesTally((int) objects[0], (IEnumerable) objects[1]);
    }

    private <T> void CountMatchesTally(int count, IEnumerable<T> enumerable) {
        assertEquals(count, enumerable.count());
    }

    @Test
    public void RunOnce() {
        for (Object[] objects : this.CountsAndTallies())
            //noinspection unchecked
            this.RunOnce((int) objects[0], (IEnumerable) objects[1]);
    }

    private <T> void RunOnce(int count, IEnumerable<T> enumerable) {
        assertEquals(count, enumerable.runOnce().count());
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Character>) null).count());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Character>) null).count(i -> i != 0));
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((Character[]) null).count());
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((Character[]) null).count(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).count(predicate));
    }

    @Test
    public void testCount() {
        int count = Linq.asEnumerable(depts).count();
        assertEquals(3, count);
    }

    @Test
    public void testCountPredicate() {
        int count = Linq.asEnumerable(depts).count(dept -> dept.employees.size() > 0);
        assertEquals(2, count);
    }
}
