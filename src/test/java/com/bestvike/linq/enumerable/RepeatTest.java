package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2019-05-30.
 */
class RepeatTest extends TestCase {
    @Test
    void Repeat_ProduceCorrectSequence() {
        IEnumerable<Integer> repeatSequence = Linq.repeat(1, 100);
        int count = 0;
        for (int val : repeatSequence) {
            count++;
            assertEquals(1, val);
        }

        assertEquals(100, count);
    }

    @Test
    void Repeat_ToArray_ProduceCorrectResult() {
        Array<Integer> array = Linq.repeat(1, 100).toArray();
        assertEquals(100, array._getCount());
        for (int i = 0; i < array._getCount(); i++)
            assertEquals(1, array.get(i));

        Integer[] array2 = Linq.repeat(1, 100).toArray(Integer.class);
        assertEquals(100, array2.length);
        for (Integer integer : array2)
            assertEquals(1, integer);
    }

    @Test
    void Repeat_ToList_ProduceCorrectResult() {
        List<Integer> list = Linq.repeat(1, 100).toList();
        assertEquals(100, list.size());
        for (int i = 0; i < list.size(); i++)
            assertEquals(1, list.get(i));
    }

    @Test
    void Repeat_ProduceSameObject() {
        Object objectInstance = new Object();
        Array<Object> array = Linq.repeat(objectInstance, 100).toArray();
        assertEquals(100, array._getCount());
        for (int i = 0; i < array._getCount(); i++)
            assertSame(objectInstance, array.get(i));
    }

    @Test
    void Repeat_WorkWithNullElement() {
        Object objectInstance = null;
        Array<Object> array = Linq.repeat(objectInstance, 100).toArray();
        assertEquals(100, array._getCount());
        for (int i = 0; i < array._getCount(); i++)
            assertNull(array.get(i));
    }

    @Test
    void Repeat_ZeroCountLeadToEmptySequence() {
        Array<Integer> array = Linq.repeat(1, 0).toArray();
        assertEquals(0, array._getCount());
    }

    @Test
    void Repeat_ThrowExceptionOnNegativeCount() {
        assertThrows(ArgumentOutOfRangeException.class, () -> Linq.repeat(1, -1));
    }

    @Test
    void Repeat_NotEnumerateAfterEnd() {
        try (IEnumerator<Integer> repeatEnum = Linq.repeat(1, 1).enumerator()) {
            assertTrue(repeatEnum.moveNext());
            assertFalse(repeatEnum.moveNext());
            assertFalse(repeatEnum.moveNext());
        }
    }

    @Test
    void Repeat_EnumerableAndEnumeratorAreSame() {
        IEnumerable<Integer> repeatEnumerable = Linq.repeat(1, 1);
        try (IEnumerator<Integer> repeatEnumerator = repeatEnumerable.enumerator()) {
            assertSame(repeatEnumerable, repeatEnumerator);
        }
    }

    @Test
    void Repeat_GetEnumeratorReturnUniqueInstances() {
        IEnumerable<Integer> repeatEnumerable = Linq.repeat(1, 1);
        try (IEnumerator<Integer> enum1 = repeatEnumerable.enumerator();
             IEnumerator<Integer> enum2 = repeatEnumerable.enumerator()) {
            assertNotSame(enum1, enum2);
        }
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        assertEquals(Linq.repeat(-3, 0), Linq.repeat(-3, 0));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        assertEquals(Linq.repeat("SSS", 99), Linq.repeat("SSS", 99));
    }

    @Test
    void CountOneSingleResult() {
        int[] expected = {-15};

        assertEquals(Linq.of(expected), Linq.repeat(-15, 1));
    }

    @Test
    void RepeatArbitraryCorrectResults() {
        int[] expected = {12, 12, 12, 12, 12, 12, 12, 12};

        assertEquals(Linq.of(expected), Linq.repeat(12, 8));
    }

    @Test
    void RepeatNull() {
        Integer[] expected = {null, null, null, null};

        assertEquals(Linq.of(expected), Linq.repeat((Integer) null, 4));
    }

    @Test
    void Take() {
        assertEquals(Linq.repeat(12, 8), Linq.repeat(12, 12).take(8));
    }

    @Test
    void TakeExcessive() {
        assertEquals(Linq.repeat("", 4), Linq.repeat("", 4).take(22));
    }

    @Test
    void Skip() {
        assertEquals(Linq.repeat(12, 8), Linq.repeat(12, 12).skip(4));
    }

    @Test
    void SkipExcessive() {
        assertEmpty(Linq.repeat(12, 8).skip(22));
    }

    @Test
    void TakeCanOnlyBeOne() {
        assertEquals(Linq.of(new int[]{1}), Linq.repeat(1, 10).take(1));
        assertEquals(Linq.of(new int[]{1}), Linq.repeat(1, 10).skip(1).take(1));
        assertEquals(Linq.of(new int[]{1}), Linq.repeat(1, 10).take(3).skip(2));
        assertEquals(Linq.of(new int[]{1}), Linq.repeat(1, 10).take(3).take(1));
    }

    @Test
    void SkipNone() {
        assertEquals(Linq.repeat(12, 8), Linq.repeat(12, 8).skip(0));
    }

    @Test
    void First() {
        assertEquals("Test", Linq.repeat("Test", 42).first());
    }

    @Test
    void FirstOrDefault() {
        assertEquals("Test", Linq.repeat("Test", 42).firstOrDefault());
    }

    @Test
    void Last() {
        assertEquals("Test", Linq.repeat("Test", 42).last());
    }

    @Test
    void LastOrDefault() {
        assertEquals("Test", Linq.repeat("Test", 42).lastOrDefault());
    }

    @Test
    void ElementAt() {
        assertEquals("Test", Linq.repeat("Test", 42).elementAt(13));
    }

    @Test
    void ElementAtOrDefault() {
        assertEquals("Test", Linq.repeat("Test", 42).elementAtOrDefault(13));
    }

    @Test
    void ElementAtExcessive() {
        assertThrows(ArgumentOutOfRangeException.class, () -> Linq.repeat(3, 3).elementAt(100));
    }

    @Test
    void ElementAtOrDefaultExcessive() {
        assertEquals(null, Linq.repeat(3, 3).elementAtOrDefault(100));
    }

    @Test
    void Count() {
        assertEquals(42, Linq.repeat("Test", 42).count());
    }
}
