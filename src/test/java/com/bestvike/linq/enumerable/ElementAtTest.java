package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ElementAtTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.elementAt(3), q.elementAt(3));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.elementAt(4), q.elementAt(4));
    }

    private IEnumerable<Object[]> TestData() {
        List<Object[]> lst = new ArrayList<>();

        lst.add(new Object[]{NumberRangeGuaranteedNotCollectionType(9, 1), 0, 9});
        lst.add(new Object[]{NumberRangeGuaranteedNotCollectionType(9, 10), 9, 18});
        lst.add(new Object[]{NumberRangeGuaranteedNotCollectionType(-4, 10), 3, -1});

        lst.add(new Object[]{Linq.of(new int[]{-4}), 0, -4});
        lst.add(new Object[]{Linq.of(new int[]{9, 8, 0, -5, 10}), 4, 10});
        return Linq.of(lst);
    }

    @Test
    public void ElementAt() {
        for (Object[] objects : this.TestData()) {
            this.ElementAt((IEnumerable<Integer>) objects[0], (int) objects[1], (Integer) objects[2]);
        }
    }

    private void ElementAt(IEnumerable<Integer> source, int index, Integer expected) {
        assertEquals(expected, source.elementAt(index));
    }

    @Test
    public void ElementAtRunOnce() {
        for (Object[] objects : this.TestData()) {
            this.ElementAtRunOnce((IEnumerable<Integer>) objects[0], (Integer) objects[1], (Integer) objects[2]);
        }
    }

    private void ElementAtRunOnce(IEnumerable<Integer> source, int index, Integer expected) {
        assertEquals(expected, source.runOnce().elementAt(index));
    }

    @Test
    public void InvalidIndex_ThrowsArgumentOutOfRangeException() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[]{1, 2, 3, 4}).elementAt(4));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[0]).elementAt(0));

        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(5, 5).elementAt(5));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(0, 0).elementAt(0));

        assertThrows(ArgumentOutOfRangeException.class, () -> Linq.singleton(true).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> Linq.singleton(true).elementAt(-1));
    }

    @Test
    public void NullableArray_ValidIndex_ReturnsCorrectObject() {
        Integer[] source = {9, 8, null, -5, 10};

        assertNull(Linq.of(source).elementAt(2));
        assertEquals(-5, Linq.of(source).elementAt(3));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAt(2));
    }

    @Test
    public void testElementAt() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("jimi", enumerable.elementAt(0));
        try {
            enumerable.elementAt(2);
            fail("should not be here");
        } catch (IndexOutOfBoundsException ignored) {
            // ok
        }
        try {
            enumerable.elementAt(-1);
            fail("should not be here");
        } catch (IndexOutOfBoundsException ignored) {
        }

        IEnumerable<Long> enumerable2 = Linq.of(new CountIterable(2));
        assertEquals(1L, enumerable2.elementAt(0));
        try {
            enumerable2.elementAt(2);
            fail("should not be here");
        } catch (ArgumentOutOfRangeException ignored) {
        }
        try {
            enumerable2.elementAt(-1);
            fail("should not be here");
        } catch (ArgumentOutOfRangeException ignored) {
        }

        IEnumerable<Integer> one = Linq.singleton(1);
        assertEquals(1, one.elementAt(0));

        IEnumerable<Integer> empty = Linq.empty();
        try {
            Integer num = empty.elementAt(0);
            fail("expect error,but got " + num);
        } catch (ArgumentOutOfRangeException ignored) {
        }
    }
}
