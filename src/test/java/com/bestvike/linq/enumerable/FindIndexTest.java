package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-06-17.
 */
public class FindIndexTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        assertEquals(q.findIndex(predicate), q.findIndex(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Func1<String, Boolean> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.findIndex(predicate), q.findIndex(predicate));
    }

    @Test
    public void FindIndex() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.FindIndex(Linq.empty(), isEvenFunc, -1);
        this.FindIndex(Linq.singleton(4), isEvenFunc, 0);
        this.FindIndex(Linq.singleton(5), isEvenFunc, -1);
        this.FindIndex(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.FindIndex(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.FindIndex(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.FindIndex(range, i -> i > k, j);
        }
    }

    private void FindIndex(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.findIndex(predicate));
    }

    @Test
    public void FindIndexRunOnce() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.FindIndexRunOnce(Linq.empty(), isEvenFunc, -1);
        this.FindIndexRunOnce(Linq.singleton(4), isEvenFunc, 0);
        this.FindIndexRunOnce(Linq.singleton(5), isEvenFunc, -1);
        this.FindIndexRunOnce(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.FindIndexRunOnce(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.FindIndexRunOnce(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.FindIndexRunOnce(range, i -> i > k, j);
        }
    }

    private void FindIndexRunOnce(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.runOnce().findIndex(predicate));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException2() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).findIndex(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).findIndex(predicate));
    }

    @Test
    public void testFindIndexPredicate() {
        assertEquals(-1, Linq.asEnumerable(depts).findIndex(dept -> dept.name != null && dept.name.equals("IT")));
        assertEquals(0, Linq.asEnumerable(depts).findIndex(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
