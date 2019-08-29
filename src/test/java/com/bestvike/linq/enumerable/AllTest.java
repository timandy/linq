package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
class AllTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Predicate1<Integer> predicate = TestCase::IsEven;
        assertEquals(q.all(predicate), q.all(predicate));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Predicate1<String> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.all(predicate), q.all(predicate));
    }

    @Test
    void All() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        this.All(Linq.singleton(0), isEvenFunc, true);
        this.All(Linq.singleton(3), isEvenFunc, false);
        this.All(Linq.singleton(4), isEvenFunc, true);
        this.All(Linq.singleton(3), isEvenFunc, false);

        this.All(Linq.of(4, 8, 3, 5, 10, 20, 12), isEvenFunc, false);
        this.All(Linq.of(4, 2, 10, 12, 8, 6, 3), isEvenFunc, false);
        this.All(Linq.of(4, 2, 10, 12, 8, 6, 14), isEvenFunc, true);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.All(range, i -> i > 0, true);
        for (int j = 1; j <= 10; j++) {
            int k = j;
            this.All(range, i -> i > k, false);
        }
    }

    private void All(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        assertEquals(expected, source.all(predicate));
    }

    @Test
    void AllRunOnce() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        this.AllRunOnce(Linq.singleton(0), isEvenFunc, true);
        this.AllRunOnce(Linq.singleton(3), isEvenFunc, false);
        this.AllRunOnce(Linq.singleton(4), isEvenFunc, true);
        this.AllRunOnce(Linq.singleton(3), isEvenFunc, false);

        this.AllRunOnce(Linq.of(4, 8, 3, 5, 10, 20, 12), isEvenFunc, false);
        this.AllRunOnce(Linq.of(4, 2, 10, 12, 8, 6, 3), isEvenFunc, false);
        this.AllRunOnce(Linq.of(4, 2, 10, 12, 8, 6, 14), isEvenFunc, true);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.AllRunOnce(range.runOnce(), i -> i > 0, true);
        for (int j = 1; j <= 10; j++) {
            int k = j;
            this.AllRunOnce(range, i -> i > k, false);
        }
    }

    private void AllRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, boolean expected) {
        assertEquals(expected, source.runOnce().all(predicate));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).all(i -> i != 0));
    }

    @Test
    void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).all(predicate));
    }

    @Test
    void testAllPredicate() {
        assertTrue(Linq.of(emps).all(emp -> emp.empno >= 100));
        assertFalse(Linq.of(emps).all(emp -> emp.empno > 100));
    }
}
