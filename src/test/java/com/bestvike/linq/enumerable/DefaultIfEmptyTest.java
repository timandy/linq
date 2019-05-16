package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class DefaultIfEmptyTest extends EnumerableTest {
    private static IEnumerable<Object[]> TestData() {
        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(new int[0]), null, new Integer[]{null}},
                new Object[]{Linq.asEnumerable(new int[0]), 0, new Integer[]{0}},
                new Object[]{Linq.asEnumerable(new int[]{3}), 0, new Integer[]{3}},
                new Object[]{Linq.asEnumerable(new int[]{3, -1, 0, 10, 15}), 0, new Integer[]{3, -1, 0, 10, 15}},

                new Object[]{Linq.asEnumerable(new int[0]), -10, new Integer[]{-10}},
                new Object[]{Linq.asEnumerable(new int[]{3}), 9, new Integer[]{3}},
                new Object[]{Linq.asEnumerable(new int[]{3, -1, 0, 10, 15}), 9, new Integer[]{3, -1, 0, 10, 15}},
                new Object[]{Linq.empty(), 0, new Integer[]{0}}
        );
    }

    private static void DefaultIfEmpty(IEnumerable<Integer> source, Integer defaultValue, Integer[] expected) {
        IEnumerable<Integer> result;
        if (defaultValue == null) {
            result = source.defaultIfEmpty();
            Assert.assertEquals(result, result);
            assertEquals(Linq.asEnumerable(expected), result);
            Assert.assertEquals(expected.length, result.count());
            assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(result.toList()));
            assertEquals(Linq.asEnumerable(expected), result.toArray());
        }
        result = source.defaultIfEmpty(defaultValue);
        Assert.assertEquals(result, result);
        assertEquals(Linq.asEnumerable(expected), result);
        Assert.assertEquals(expected.length, result.count());
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(result.toList()));
        assertEquals(Linq.asEnumerable(expected), result.toArray());
    }

    private static void DefaultIfEmptyRunOnce(IEnumerable<Integer> source, Integer defaultValue, Integer[] expected) {
        if (defaultValue == null) {
            assertEquals(Linq.asEnumerable(expected), source.runOnce().defaultIfEmpty());
        }

        assertEquals(Linq.asEnumerable(expected), source.runOnce().defaultIfEmpty(defaultValue));
    }

    @Test
    public void SameResultsRepeatCallsNonEmptyQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(a -> a > Integer.MIN_VALUE);

        assertEquals(q.defaultIfEmpty(5), q.defaultIfEmpty(5));
    }

    @Test
    public void SameResultsRepeatCallsEmptyQuery() {
        IEnumerable<Integer> q = NumberRangeGuaranteedNotCollectionType(0, 0).select(a -> a);

        assertEquals(q.defaultIfEmpty(88), q.defaultIfEmpty(88));
    }

    @Test
    public void DefaultIfEmpty() {
        for (Object[] objects : TestData())
            //noinspection unchecked
            DefaultIfEmpty((IEnumerable<Integer>) objects[0], (Integer) objects[1], (Integer[]) objects[2]);
    }

    @Test
    public void DefaultIfEmptyRunOnce() {
        for (Object[] objects : TestData())
            //noinspection unchecked
            DefaultIfEmptyRunOnce((IEnumerable<Integer>) objects[0], (Integer) objects[1], (Integer[]) objects[2]);
    }

    @Test
    public void NullableArray_Empty_WithoutDefaultValue() {
        Integer[] source = new Integer[0];
        assertEquals(Linq.asEnumerable(new Integer[]{null}), Linq.asEnumerable(source).defaultIfEmpty());
    }

    @Test
    public void NullableArray_Empty_WithDefaultValue() {
        Integer[] source = new Integer[0];
        Integer defaultValue = 9;
        //noinspection RedundantArrayCreation
        assertEquals(Linq.asEnumerable(new Integer[]{defaultValue}), Linq.asEnumerable(source).defaultIfEmpty(defaultValue));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).defaultIfEmpty());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).defaultIfEmpty(42));
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((Integer[]) null).defaultIfEmpty());
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((Integer[]) null).defaultIfEmpty(42));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).defaultIfEmpty();
        // Don't insist on this behaviour, but check it's correct if it happens
        //noinspection unchecked
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testDefaultIfEmpty() {
        List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty();
        IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty();
        IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertNull(emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }

    @Test
    public void testDefaultIfEmptyWithDefaultValue() {
        List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty("dummy");
        IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty("N/A");
        IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertEquals("N/A", emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }
}
