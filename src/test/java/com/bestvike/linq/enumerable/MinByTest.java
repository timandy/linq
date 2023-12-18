package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class MinByTest extends TestCase {
    private static <TSource, TKey> Object[] WrapArgs(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        return new Object[]{source, keySelector, comparer, expected};
    }

    private static IEnumerable<Object[]> MinBy_Generic_TestData() {
        ArgsList argsList = new ArgsList();

        argsList.add(WrapArgs(
                Linq.<Integer>empty(),
                x -> x,
                null,
                null));

        argsList.add(WrapArgs(
                Linq.<Integer>empty(),
                x -> x,
                (x, y) -> 0,
                null));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                x -> x,
                null,
                0));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                x -> x,
                (x, y) -> -x.compareTo(y),
                9));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                x -> x,
                (x, y) -> 0,
                0));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                x -> x,
                null,
                "Aardvark"));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                x -> x,
                (x, y) -> -x.compareTo(y),
                "Zyzzyva"));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem2(),
                null,
                Tuple.create("Harry", 20)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem2(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Dick", 55)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                null,
                Tuple.create("Dick", 55)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Tom", 43)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create((String) null, 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Harry", 20)));

        return argsList;
    }

    @Test
    void MinBy_Generic_NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;

        assertThrows(NullPointerException.class, () -> source.minBy(x -> x));
        assertThrows(NullPointerException.class, () -> source.minBy(x -> x, null));
        assertThrows(NullPointerException.class, () -> source.minBy(x -> x, (x, y) -> 0));
    }

    @Test
    void MinBy_Generic_NullKeySelector_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = Linq.empty();
        Func1<Integer, Integer> keySelector = null;

        assertThrows(ArgumentNullException.class, () -> source.minBy(keySelector));
        assertThrows(ArgumentNullException.class, () -> source.minBy(keySelector, null));
        assertThrows(ArgumentNullException.class, () -> source.minBy(keySelector, (x, y) -> 0));
    }

    @Test
    void MinBy_Generic_EmptyStructSource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minBy(x -> x.toString()));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minBy(x -> x.toString(), null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minBy(x -> x.toString(), (x, y) -> 0));
    }

    @Test
    void MinBy_Generic_EmptyNullableSource_ReturnsNull() {
        assertNull(Linq.<Integer>empty().minByNull(x -> x.hashCode()));
        assertNull(Linq.<Integer>empty().minByNull(x -> x.hashCode(), null));
        assertNull(Linq.<Integer>empty().minByNull(x -> x.hashCode(), (x, y) -> 0));
    }

    @Test
    void MinBy_Generic_EmptyReferenceSource_ReturnsNull() {
        assertNull(Linq.<String>empty().minByNull(x -> x.hashCode()));
        assertNull(Linq.<String>empty().minByNull(x -> x.hashCode(), null));
        assertNull(Linq.<String>empty().minByNull(x -> x.hashCode(), (x, y) -> 0));
    }

    @Test
    void MinBy_Generic_StructSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals(4, Linq.range(0, 5).minByNull(x -> null));
        assertEquals(4, Linq.range(0, 5).minByNull(x -> null, null));
        assertEquals(4, Linq.range(0, 5).minByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
    }

    @Test
    void MinBy_Generic_NullableSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).minByNull(x -> null));
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).minByNull(x -> null, null));
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).minByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
        //
        assertEquals(4, Linq.range(0, 5).minByIntNull(x -> null));
        assertEquals(0, Linq.range(0, 5).minByIntNull(x -> 0));
        assertEquals(1, Linq.of(Tuple.create((Integer) null, 0), Tuple.create(0, 1), Tuple.create((Integer) null, 2), Tuple.create(0, 3), Tuple.create((Integer) null, 4)).minByIntNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).minByLongNull(x -> null));
        assertEquals(0, Linq.range(0, 5).minByLongNull(x -> 0L));
        assertEquals(1, Linq.of(Tuple.create((Long) null, 0), Tuple.create(0L, 1), Tuple.create((Long) null, 2), Tuple.create(0L, 3), Tuple.create((Long) null, 4)).minByLongNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).minByFloatNull(x -> null));
        assertEquals(0, Linq.range(0, 5).minByFloatNull(x -> Float.NaN));
        assertEquals(1, Linq.of(Tuple.create((Float) null, 0), Tuple.create(Float.NaN, 1), Tuple.create((Float) null, 2), Tuple.create(Float.NaN, 3), Tuple.create((Float) null, 4)).minByFloatNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).minByDoubleNull(x -> null));
        assertEquals(0, Linq.range(0, 5).minByDoubleNull(x -> Double.NaN));
        assertEquals(1, Linq.of(Tuple.create((Double) null, 0), Tuple.create(Double.NaN, 1), Tuple.create((Double) null, 2), Tuple.create(Double.NaN, 3), Tuple.create((Double) null, 4)).minByDoubleNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).minByDecimalNull(x -> null));
        assertEquals(0, Linq.range(0, 5).minByDecimalNull(x -> m(0)));
        assertEquals(1, Linq.of(Tuple.create((BigDecimal) null, 0), Tuple.create(m(0), 1), Tuple.create((BigDecimal) null, 2), Tuple.create(m(0), 3), Tuple.create((BigDecimal) null, 4)).minByDecimalNull(x -> x.getItem1()).getItem2());
    }

    @Test
    void MinBy_Generic_ReferenceSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).minByNull(x -> null));
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).minByNull(x -> null, null));
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).minByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
    }

    @ParameterizedTest
    @MethodSource("MinBy_Generic_TestData")
    <TSource, TKey> void MinBy_Generic_HasExpectedOutput(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        assertEquals(expected, source.minByNull(keySelector, comparer));
    }

    @ParameterizedTest
    @MethodSource("MinBy_Generic_TestData")
    <TSource, TKey> void MinBy_Generic_RunOnce_HasExpectedOutput(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        assertEquals(expected, source.runOnce().minByNull(keySelector, comparer));
    }

    @Test
    void testMinByInt() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minByInt(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Integer>empty()).minByInt(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minByInt(x -> x));

        Tuple1[] tuple1s = {Tuple.create(3), Tuple.create(2), Tuple.create(0)};
        assertEquals(Tuple.create(0), Linq.of(tuple1s).minByInt(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByInt(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMinByIntNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minByIntNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Integer>empty()).minByIntNull(null));
        assertNull(Linq.<Integer>empty().minByIntNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(3), Tuple.create(2), Tuple.create(0)};
        assertEquals(Tuple.create(0), Linq.of(tuple1s).minByIntNull(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMinByLong() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minByLong(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Long>empty()).minByLong(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().minByLong(x -> x));

        Tuple1[] tuple1s = {Tuple.create(3L), Tuple.create(2L), Tuple.create(0L)};
        assertEquals(Tuple.create(0L), Linq.of(tuple1s).minByLong(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByLong(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMinByLongNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minByLongNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Long>empty()).minByLongNull(null));
        assertNull(Linq.<Long>empty().minByLongNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(3L), Tuple.create(2L), Tuple.create(0L)};
        assertEquals(Tuple.create(0L), Linq.of(tuple1s).minByLongNull(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMinByFloat() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minByFloat(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Float>empty()).minByFloat(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().minByFloat(x -> x));

        Tuple1[] tuple1s = {Tuple.create(2f), Tuple.create(0f)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN), Tuple.create(1f)};
        assertSame(tuple1s4[0], Linq.of(tuple1s4).minByFloat(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByFloatNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minByFloatNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Float>empty()).minByFloatNull(null));
        assertNull(Linq.<Float>empty().minByFloatNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(2f), Tuple.create(0f)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN), Tuple.create(1f)};
        assertSame(tuple1s4[1], Linq.of(tuple1s4).minByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByDouble() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minByDouble(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Double>empty()).minByDouble(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().minByDouble(x -> x));

        Tuple1[] tuple1s = {Tuple.create(2d), Tuple.create(0d)};
        assertEquals(Tuple.create(0d), Linq.of(tuple1s).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN), Tuple.create(1d)};
        assertSame(tuple1s4[0], Linq.of(tuple1s4).minByDouble(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMinByDoubleNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minByDoubleNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Double>empty()).minByDoubleNull(null));
        assertNull(Linq.<Double>empty().minByDoubleNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(2d), Tuple.create(0d)};
        assertEquals(Tuple.create(0d), Linq.of(tuple1s).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN), Tuple.create(1d)};
        assertSame(tuple1s4[1], Linq.of(tuple1s4).minByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMinByDecimal() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minByDecimal(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<BigDecimal>empty()).minByDecimal(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().minByDecimal(x -> x));

        Tuple1[] tuple1s = {Tuple.create(m("3")), Tuple.create(m("2")), Tuple.create(m("0"))};
        assertEquals(Tuple.create(m("0")), Linq.of(tuple1s).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMinByDecimalNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minByDecimalNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<BigDecimal>empty()).minByDecimalNull(null));
        assertNull(Linq.<BigDecimal>empty().minByDecimalNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(m("3")), Tuple.create(m("2")), Tuple.create(m("0"))};
        assertEquals(Tuple.create(m("0")), Linq.of(tuple1s).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMinBy() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).minBy(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.empty()).minBy(null));
        assertThrows(InvalidOperationException.class, () -> Linq.empty().minBy(x -> x));

        Tuple1[] tuple1s = {Tuple.create(2f), Tuple.create(0f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s3).minBy(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).minByNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.empty()).minByNull(null));
        assertNull(Linq.empty().minByNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(2f), Tuple.create(0f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertEquals(tuple1s3[0], Linq.of(tuple1s3).minByNull(tuple -> (Float) tuple.getItem1()));
    }
}
