package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AnyTest extends EnumerableTest {
    private static void AnyCore(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, boolean expected) {
        if (predicate == null) {
            Assert.assertEquals(expected, source.any());
        } else {
            Assert.assertEquals(expected, source.any(predicate));
        }
    }

    private static void AnyRunOnceCore(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, boolean expected) {
        if (predicate == null) {
            Assert.assertEquals(expected, source.runOnce().any());
        } else {
            Assert.assertEquals(expected, source.runOnce().any(predicate));
        }
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        Assert.assertEquals(q.any(predicate), q.any(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Func1<String, Boolean> predicate = EnumerableTest::IsNullOrEmpty;
        Assert.assertEquals(q.any(predicate), q.any(predicate));
    }

    @Test
    public void Any() {
        AnyCore(Linq.empty(), null, false);
        AnyCore(Linq.singleton(3), null, true);

        Func1<Integer, Boolean> isEvenFunc = EnumerableTest::IsEven;
        AnyCore(Linq.empty(), isEvenFunc, false);
        AnyCore(Linq.singleton(4), isEvenFunc, true);
        AnyCore(Linq.singleton(5), isEvenFunc, false);
        AnyCore(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, true);
        AnyCore(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, true);

        Array<Integer> range = Linq.range(1, 10).toArray();
        AnyCore(range, i -> i > 10, false);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            AnyCore(range, i -> i > k, true);
        }
    }

    @Test
    public void AnyRunOnce() {
        AnyRunOnceCore(Linq.empty(), null, false);
        AnyRunOnceCore(Linq.singleton(3), null, true);

        Func1<Integer, Boolean> isEvenFunc = EnumerableTest::IsEven;
        AnyRunOnceCore(Linq.empty(), isEvenFunc, false);
        AnyRunOnceCore(Linq.singleton(4), isEvenFunc, true);
        AnyRunOnceCore(Linq.singleton(5), isEvenFunc, false);
        AnyRunOnceCore(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, true);
        AnyRunOnceCore(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, true);

        Array<Integer> range = Linq.range(1, 10).toArray();
        AnyRunOnceCore(range, i -> i > 10, false);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            AnyRunOnceCore(range, i -> i > k, true);
        }
    }

    @Test
    public void NullObjectsInArray_Included() {
        IEnumerable<Integer> source = Linq.asEnumerable(null, null, null, null);
        Assert.assertTrue(source.any());
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).any(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).any(predicate));
    }

    @Test
    public void testAny() {
        Assert.assertFalse(Linq.asEnumerable(Collections.emptyList()).any());
        Assert.assertTrue(Linq.asEnumerable(emps).any());
    }

    @Test
    public void testAnyPredicate() {
        Assert.assertFalse(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("IT")));
        Assert.assertTrue(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("Sales")));
    }

    @Test
    public void testAllPredicate() {
        Assert.assertTrue(Linq.asEnumerable(emps).all(emp -> emp.empno >= 100));
        Assert.assertFalse(Linq.asEnumerable(emps).all(emp -> emp.empno > 100));
    }
}
