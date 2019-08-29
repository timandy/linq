package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class CastTest extends TestCase {
    @Test
    public void CastIntToLongThrows() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        IEnumerable<Long> rst = q.cast(long.class);
        assertThrows(ClassCastException.class, () -> {
            for (Long ignored : rst) ;
        });
    }

    @Test
    public void CastByteToUShortThrows() {
        IEnumerable<Byte> q = Linq.of(new byte[]{0, (byte) 255, 127, (byte) 128, 1, 33, 99});

        IEnumerable<Short> rst = q.cast(short.class);
        assertThrows(ClassCastException.class, () -> {
            for (Short ignored : rst) ;
        });
    }

    @Test
    public void EmptySource() {
        IEnumerable<Object> source = Linq.empty();
        assertEquals(0, source.cast(int.class).count());
    }

    @Test
    public void NullableIntFromAppropriateObjects() {
        Integer i = 10;
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, i);
        IEnumerable<Integer> expected = Linq.of(-4, 1, 2, 3, 9, i);

        assertEquals(expected, source.cast(Integer.class));
    }

    @Test
    public void NullableIntFromAppropriateObjectsRunOnce() {
        Integer i = 10;
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, i);
        IEnumerable<Integer> expected = Linq.of(-4, 1, 2, 3, 9, i);

        assertEquals(expected, source.runOnce().cast(Integer.class));
    }

    @Test
    public void LongFromNullableIntInObjectsThrows() {
        Integer i = 10;
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, i);

        IEnumerable<Long> cast = source.cast(long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void LongFromNullableIntInObjectsIncludingNullThrows() {
        Integer i = 10;
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, null, i);

        IEnumerable<Long> cast = source.cast(Long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void NullableIntFromAppropriateObjectsIncludingNull() {
        Integer i = 10;
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, null, i);
        IEnumerable<Integer> expected = Linq.of(-4, 1, 2, 3, 9, null, i);

        assertEquals(expected, source.cast(Integer.class));
    }

    @Test
    public void ThrowOnUncastableItem() {
        IEnumerable<Object> source = Linq.of(-4, 1, 2, 3, 9, "45");
        IEnumerable<Integer> expectedBeginning = Linq.of(new int[]{-4, 1, 2, 3, 9});

        IEnumerable<Integer> cast = source.cast(Integer.class);
        assertThrows(ClassCastException.class, cast::toList);
        assertEquals(expectedBeginning, cast.take(5));
        assertThrows(ClassCastException.class, () -> cast.elementAt(5));
    }

    @Test
    public void ThrowCastingIntToDouble() {
        IEnumerable<Integer> source = Linq.of(new int[]{-4, 1, 2, 9});

        IEnumerable<Double> cast = source.cast(double.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    private <T> void TestCastThrow(Object o, Class<T> clazz) {
        Byte i = 10;
        IEnumerable<Object> source = Linq.of(-1, 0, o, i);

        IEnumerable<T> cast = source.cast(clazz);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void ThrowOnHeterogenousSource() {
        this.TestCastThrow(null, Long.class);
        this.TestCastThrow(9L, Long.class);
    }

    @Test
    public void CastToString() {
        IEnumerable<Object> source = Linq.of("Test1", "4.5", null, "Test2");
        IEnumerable<String> expected = Linq.of("Test1", "4.5", null, "Test2");

        assertEquals(Linq.of(expected), source.cast(String.class));
    }

    @Test
    public void CastToStringRunOnce() {
        IEnumerable<Object> source = Linq.of("Test1", "4.5", null, "Test2");
        IEnumerable<String> expected = Linq.of("Test1", "4.5", null, "Test2");

        assertEquals(Linq.of(expected), source.runOnce().cast(String.class));
    }

    @Test
    public void ArrayConversionThrows() {
        assertThrows(ClassCastException.class, () -> Linq.of(new int[]{-4}).cast(Long.class).toList());
    }

    @Test
    public void FirstElementInvalidForCast() {
        IEnumerable<Object> source = Linq.of("Test", 3, 5, 10);

        IEnumerable<Integer> cast = source.cast(Integer.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void LastElementInvalidForCast() {
        IEnumerable<Object> source = Linq.of(-5, 9, 0, 5, 9, "Test");

        IEnumerable<Integer> cast = source.cast(Integer.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void NullableIntFromNullsAndInts() {
        IEnumerable<Object> source = Linq.of(3, null, 5, -4, 0, null, 9);
        IEnumerable<Integer> expected = Linq.of(3, null, 5, -4, 0, null, 9);

        assertEquals(Linq.of(expected), source.cast(Integer.class));
    }

    @Test
    public void ThrowCastingIntToLong() {
        IEnumerable<Integer> source = Linq.of(new int[]{-4, 1, 2, 3, 9});

        IEnumerable<Long> cast = source.cast(long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void ThrowCastingIntToNullableLong() {
        IEnumerable<Integer> source = Linq.of(new int[]{-4, 1, 2, 3, 9});

        IEnumerable<Long> cast = source.cast(Long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void ThrowCastingNullableIntToLong() {
        IEnumerable<Integer> source = Linq.of(-4, 1, 2, 3, 9);

        IEnumerable<Long> cast = source.cast(long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void ThrowCastingNullableIntToNullableLong() {
        IEnumerable<Integer> source = Linq.of(-4, 1, 2, 3, 9, null);

        IEnumerable<Long> cast = source.cast(Long.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void CastingNullToNonnullableIsNullReferenceException() {
        IEnumerable<Integer> source = Linq.of(-4, 1, null, 3);

        IEnumerable<Integer> cast = source.cast(int.class);
        assertThrows(ClassCastException.class, cast::toList);
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).cast(String.class));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<String> iterator = Linq.empty().where(Objects::nonNull).cast(String.class);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testCast() {
        List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        IEnumerator<Integer> enumerator = Linq.of(numbers)
                .cast(Integer.class)
                .enumerator();

        assertTrue(enumerator.moveNext());
        assertEquals(2, enumerator.current());
        assertTrue(enumerator.moveNext());
        assertEquals(null, enumerator.current());
        try {
            enumerator.moveNext();
            fail("cast() fail");
        } catch (ClassCastException ignored) {
        }
    }
}
