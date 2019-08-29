package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-26.
 */
public class OfTypeTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.ofType(Integer.class), q.ofType(Integer.class));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(TestCase::IsNullOrEmpty);

        assertEquals(q.ofType(Integer.class), q.ofType(Integer.class));
    }

    @Test
    public void EmptySource() {
        IEnumerable<Object> source = Linq.empty();
        assertEquals(0, source.ofType(Integer.class).count());
    }

    @Test
    public void LongSequenceFromIntSource() {
        IEnumerable<Integer> source = Linq.of(new int[]{99, 45, 81});
        assertEquals(0, source.ofType(Long.class).count());
    }

    @Test
    public void HeterogenousSourceNoAppropriateElements() {
        IEnumerable<Object> source = Linq.of("Hello", 3.5, "Test");
        assertEquals(0, source.ofType(Integer.class).count());
    }

    @Test
    public void HeterogenousSourceOnlyFirstOfType() {
        IEnumerable<Object> source = Linq.of(10, "Hello", 3.5, "Test");
        IEnumerable<Integer> expected = Linq.of(new int[]{10});

        assertEquals(expected, source.ofType(Integer.class));
    }

    @Test
    public void AllElementsOfNullableTypeNullsSkipped() {
        IEnumerable<Object> source = Linq.of(10, -4, null, null, 4, 9);
        IEnumerable<Integer> expected = Linq.of(10, -4, 4, 9);

        assertEquals(expected, source.ofType(Integer.class));
    }

    @Test
    public void HeterogenousSourceSomeOfType() {
        IEnumerable<Object> source = Linq.of(m("3.5"), -4, "Test", "Check", 4, 8.0, 10.5, 9);
        IEnumerable<Integer> expected = Linq.of(new int[]{-4, 4, 9});

        assertEquals(expected, source.ofType(Integer.class));
    }

    @Test
    public void RunOnce() {
        IEnumerable<Object> source = Linq.of(m("3.5"), -4, "Test", "Check", 4, 8.0, 10.5, 9);
        IEnumerable<Integer> expected = Linq.of(new int[]{-4, 4, 9});

        assertEquals(expected, source.runOnce().ofType(Integer.class));
    }

    @Test
    public void IntFromNullableInt() {
        IEnumerable<Integer> source = Linq.of(new int[]{-4, 4, 9});
        IEnumerable<Integer> expected = Linq.of(-4, 4, 9);

        assertEquals(expected, source.ofType(Integer.class));
    }

    @Test
    public void IntFromNullableIntWithNulls() {
        IEnumerable<Integer> source = Linq.of(null, -4, 4, null, 9);
        IEnumerable<Integer> expected = Linq.of(new int[]{-4, 4, 9});

        assertEquals(expected, source.ofType(Integer.class));
    }

    @Test
    public void NullableDecimalFromString() {
        IEnumerable<String> source = Linq.of("Test1", "Test2", "Test9");
        assertEquals(0, source.ofType(BigDecimal.class).count());
    }

    @Test
    public void LongFromDouble() {
        IEnumerable<Long> source = Linq.of(new long[]{99L, 45L, 81L});
        assertEquals(0, source.ofType(Double.class).count());
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).ofType(String.class));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).ofType(Integer.class);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testOfType() {
        List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        IEnumerator<Integer> enumerator = Linq.of(numbers)
                .ofType(Integer.class)
                .enumerator();
        assertTrue(enumerator.moveNext());
        assertEquals(Integer.valueOf(2), enumerator.current());
        assertTrue(enumerator.moveNext());
        assertEquals(Integer.valueOf(5), enumerator.current());
        assertFalse(enumerator.moveNext());
    }
}
