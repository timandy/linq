package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AnyTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Predicate1<Integer> predicate = TestCase::IsEven;
        assertEquals(q.any(predicate), q.any(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Predicate1<String> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.any(predicate), q.any(predicate));
    }

    private IEnumerable<Object[]> TestData() {
        List<Object[]> result = new ArrayList<>();
        for (int count : new int[]{0, 1, 2}) {
            boolean expected = count > 0;

            Integer[] arr = new Integer[count];
            IEnumerable<Integer>[] collectionTypes = new IEnumerable[]{
                    Linq.of(arr),
                    Linq.of(Arrays.asList(arr)),
                    Linq.of(new LinkedList<>(Arrays.asList(arr))),
                    new TestCollection<Integer>(arr),
                    NumberRangeGuaranteedNotCollectionType(0, count),
            };

            for (IEnumerable<Integer> source : collectionTypes) {
                result.add(new Object[]{source, expected});
                result.add(new Object[]{source.select(i -> i), expected});
                result.add(new Object[]{source.where(i -> true), expected});
                result.add(new Object[]{source.where(i -> false), false});
            }
        }
        return Linq.of(result);
    }

    @Test
    public void Any() {
        for (Object[] data : this.TestData()) {
            this.Any((IEnumerable<Integer>) data[0], (boolean) data[1]);
        }
    }

    private void Any(IEnumerable<Integer> source, boolean expected) {
        assertEquals(expected, source.any());
    }

    private IEnumerable<Object[]> TestDataWithPredicate() {
        List<Object[]> result = new ArrayList<>();
        result.add(new Object[]{Linq.empty(), null, false});
        result.add(new Object[]{Linq.singleton(3), null, true});

        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        result.add(new Object[]{Linq.empty(), isEvenFunc, false});
        result.add(new Object[]{Linq.singleton(4), isEvenFunc, true});
        result.add(new Object[]{Linq.singleton(5), isEvenFunc, false});
        result.add(new Object[]{Linq.of(5, 9, 3, 7, 4), isEvenFunc, true});
        result.add(new Object[]{Linq.of(5, 8, 9, 3, 7, 11), isEvenFunc, true});

        Array<Integer> range = Linq.range(1, 10).toArray();
        result.add(new Object[]{range, (Predicate1<Integer>) i -> i > 10, false});
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            result.add(new Object[]{range, (Predicate1<Integer>) i -> i > k, true});
        }
        return Linq.of(result);
    }

    @Test
    public void Any2() {
        for (Object[] data : this.TestDataWithPredicate()) {
            this.Any2((IEnumerable<Integer>) data[0], (Predicate1<Integer>) data[1], (boolean) data[2]);
        }
    }

    private void Any2(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        if (predicate == null)
            assertEquals(expected, source.any());
        else
            assertEquals(expected, source.any(predicate));
    }

    @Test
    public void AnyRunOnce() {
        for (Object[] data : this.TestDataWithPredicate()) {
            this.AnyRunOnce((IEnumerable<Integer>) data[0], (Predicate1<Integer>) data[1], (boolean) data[2]);
        }
    }

    private void AnyRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        if (predicate == null)
            assertEquals(expected, source.runOnce().any());
        else
            assertEquals(expected, source.runOnce().any(predicate));
    }

    @Test
    public void NullObjectsInArray_Included() {
        IEnumerable<Integer> source = Linq.of(null, null, null, null);
        assertTrue(source.any());
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).any(predicate));
    }

    @Test
    public void testAny() {
        assertFalse(Linq.of(Collections.emptyList()).any());
        assertTrue(Linq.of(emps).any());
    }

    @Test
    public void testAnyPredicate() {
        assertFalse(Linq.of(depts).any(dept -> dept.name != null && dept.name.equals("IT")));
        assertTrue(Linq.of(depts).any(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
