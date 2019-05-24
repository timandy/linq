package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-05-20.
 */
public class LongCountTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.longCount(), q.longCount());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.longCount(), q.longCount());
    }

    private IEnumerable<Object[]> LongCount_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(new int[0]), null, 0L});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{3}), null, 1L});
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        lst.add(new Object[]{Linq.asEnumerable(new int[0]), isEvenFunc, 0L});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{4}), isEvenFunc, 1L});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{5}), isEvenFunc, 0L});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{2, 5, 7, 9, 29, 10}), isEvenFunc, 2L});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{2, 20, 22, 100, 50, 10}), isEvenFunc, 6L});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void LongCount() {
        for (Object[] objects : this.LongCount_TestData()) {
            this.LongCount((IEnumerable<Integer>) objects[0], (Func1<Integer, Boolean>) objects[1], (long) objects[2]);
        }
    }

    private void LongCount(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, long expected) {
        if (predicate == null) {
            assertEquals(expected, source.longCount());
        } else {
            assertEquals(expected, source.longCount(predicate));
        }
    }

    @Test
    public void LongCountRunOnce() {
        for (Object[] objects : this.LongCount_TestData()) {
            this.LongCountRunOnce((IEnumerable<Integer>) objects[0], (Func1<Integer, Boolean>) objects[1], (long) objects[2]);
        }
    }

    private void LongCountRunOnce(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, long expected) {
        if (predicate == null) {
            assertEquals(expected, source.runOnce().longCount());
        } else {
            assertEquals(expected, source.runOnce().longCount(predicate));
        }
    }

    @Test
    public void NullableArray_IncludesNullValues() {
        Integer[] data = {-10, 4, 9, null, 11};
        assertEquals(5, Linq.asEnumerable(data).longCount());
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).longCount());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).longCount(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).longCount(predicate));
    }
}
