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
class MaxByTest extends TestCase {
    private static <TSource, TKey> Object[] WrapArgs(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        return new Object[]{source, keySelector, comparer, expected};
    }

    private static IEnumerable<Object[]> MaxBy_Generic_TestData() {
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
                9));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                x -> x,
                (x, y) -> -x.compareTo(y),
                0));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                x -> x,
                (x, y) -> 0,
                0));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                x -> x,
                null,
                "Zyzzyva"));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                x -> x,
                (x, y) -> -x.compareTo(y),
                "Aardvark"));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem2(),
                null,
                Tuple.create("Dick", 55)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem2(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Harry", 20)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                null,
                Tuple.create("Tom", 43)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create("Dick", 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Dick", 55)));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 43), Tuple.create((String) null, 55), Tuple.create("Harry", 20)),
                x -> x.getItem1(),
                (x, y) -> -x.compareTo(y),
                Tuple.create("Harry", 20)));

        return argsList;
    }

    @Test
    void MaxBy_Generic_NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;

        assertThrows(NullPointerException.class, () -> source.maxBy(x -> x));
        assertThrows(NullPointerException.class, () -> source.maxBy(x -> x, null));
        assertThrows(NullPointerException.class, () -> source.maxBy(x -> x, (x, y) -> 0));
    }

    @Test
    void MaxBy_Generic_NullKeySelector_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = Linq.empty();
        Func1<Integer, Integer> keySelector = null;

        assertThrows(ArgumentNullException.class, () -> source.maxBy(keySelector));
        assertThrows(ArgumentNullException.class, () -> source.maxBy(keySelector, null));
        assertThrows(ArgumentNullException.class, () -> source.maxBy(keySelector, (x, y) -> 0));
    }

    @Test
    void MaxBy_Generic_EmptyStructSource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxBy(x -> x.toString()));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxBy(x -> x.toString(), null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxBy(x -> x.toString(), (x, y) -> 0));
    }

    @Test
    void MaxBy_Generic_EmptyNullableSource_ReturnsNull() {
        assertNull(Linq.<Integer>empty().maxByNull(x -> x.hashCode()));
        assertNull(Linq.<Integer>empty().maxByNull(x -> x.hashCode(), null));
        assertNull(Linq.<Integer>empty().maxByNull(x -> x.hashCode(), (x, y) -> 0));
    }

    @Test
    void MaxBy_Generic_EmptyReferenceSource_ReturnsNull() {
        assertNull(Linq.<String>empty().maxByNull(x -> x.hashCode()));
        assertNull(Linq.<String>empty().maxByNull(x -> x.hashCode(), null));
        assertNull(Linq.<String>empty().maxByNull(x -> x.hashCode(), (x, y) -> 0));
    }

    @Test
    void MaxBy_Generic_StructSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals(4, Linq.range(0, 5).maxByNull(x -> null));
        assertEquals(4, Linq.range(0, 5).maxByNull(x -> null, null));
        assertEquals(4, Linq.range(0, 5).maxByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
    }

    @Test
    void MaxBy_Generic_NullableSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).maxByNull(x -> null));
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).maxByNull(x -> null, null));
        assertEquals(4, Linq.range(0, 5).cast(Integer.class).maxByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
        //
        assertEquals(4, Linq.range(0, 5).maxByIntNull(x -> null));
        assertEquals(0, Linq.range(0, 5).maxByIntNull(x -> 0));
        assertEquals(1, Linq.of(Tuple.create((Integer) null, 0), Tuple.create(0, 1), Tuple.create((Integer) null, 2), Tuple.create(0, 3), Tuple.create((Integer) null, 4)).maxByIntNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).maxByLongNull(x -> null));
        assertEquals(0, Linq.range(0, 5).maxByLongNull(x -> 0L));
        assertEquals(1, Linq.of(Tuple.create((Long) null, 0), Tuple.create(0L, 1), Tuple.create((Long) null, 2), Tuple.create(0L, 3), Tuple.create((Long) null, 4)).maxByLongNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).maxByFloatNull(x -> null));
        assertEquals(0, Linq.range(0, 5).maxByFloatNull(x -> Float.NaN));
        assertEquals(1, Linq.of(Tuple.create((Float) null, 0), Tuple.create(Float.NaN, 1), Tuple.create((Float) null, 2), Tuple.create(Float.NaN, 3), Tuple.create((Float) null, 4)).maxByFloatNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).maxByDoubleNull(x -> null));
        assertEquals(0, Linq.range(0, 5).maxByDoubleNull(x -> Double.NaN));
        assertEquals(1, Linq.of(Tuple.create((Double) null, 0), Tuple.create(Double.NaN, 1), Tuple.create((Double) null, 2), Tuple.create(Double.NaN, 3), Tuple.create((Double) null, 4)).maxByDoubleNull(x -> x.getItem1()).getItem2());
        //
        assertEquals(4, Linq.range(0, 5).maxByDecimalNull(x -> null));
        assertEquals(0, Linq.range(0, 5).maxByDecimalNull(x -> m(0)));
        assertEquals(1, Linq.of(Tuple.create((BigDecimal) null, 0), Tuple.create(m(0), 1), Tuple.create((BigDecimal) null, 2), Tuple.create(m(0), 3), Tuple.create((BigDecimal) null, 4)).maxByDecimalNull(x -> x.getItem1()).getItem2());
    }

    @Test
    void MaxBy_Generic_ReferenceSourceAllKeysAreNull_ReturnsLastElement() {
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).maxByNull(x -> null));
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).maxByNull(x -> null, null));
        assertEquals("4", Linq.range(0, 5).select(x -> x.toString()).maxByNull(x -> null, (x, y) -> {
            throw new InvalidOperationException("comparer should not be called.");
        }));
    }

    @ParameterizedTest
    @MethodSource("MaxBy_Generic_TestData")
    <TSource, TKey> void MaxBy_Generic_HasExpectedOutput(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        assertEquals(expected, source.maxByNull(keySelector, comparer));
    }

    @ParameterizedTest
    @MethodSource("MaxBy_Generic_TestData")
    <TSource, TKey> void MaxBy_Generic_RunOnce_HasExpectedOutput(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer, TSource expected) {
        assertEquals(expected, source.runOnce().maxByNull(keySelector, comparer));
    }

    @Test
    void testMaxByInt() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxByInt(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Integer>empty()).maxByInt(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxByInt(x -> x));

        Tuple1[] tuple1s = {Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(3), Linq.of(tuple1s).maxByInt(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxByInt(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMaxByIntNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxByIntNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Integer>empty()).maxByIntNull(null));
        assertNull(Linq.<Integer>empty().maxByIntNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(3), Linq.of(tuple1s).maxByIntNull(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMaxByLong() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxByLong(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Long>empty()).maxByLong(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().maxByLong(x -> x));

        Tuple1[] tuple1s = {Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(3L), Linq.of(tuple1s).maxByLong(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxByLong(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMaxByLongNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxByLongNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Long>empty()).maxByLongNull(null));
        assertNull(Linq.<Long>empty().maxByLongNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(3L), Linq.of(tuple1s).maxByLongNull(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMaxByFloat() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxByFloat(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Float>empty()).maxByFloat(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().maxByFloat(x -> x));

        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(2f), Linq.of(tuple1s).maxByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).maxByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN), Tuple.create(1f)};
        assertSame(tuple1s4[2], Linq.of(tuple1s4).maxByFloat(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMaxByFloatNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxByFloatNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Float>empty()).maxByFloatNull(null));
        assertNull(Linq.<Float>empty().maxByFloatNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(2f), Linq.of(tuple1s).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN), Tuple.create(1f)};
        assertSame(tuple1s4[3], Linq.of(tuple1s4).maxByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMaxByDouble() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxByDouble(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Double>empty()).maxByDouble(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().maxByDouble(x -> x));

        Tuple1[] tuple1s = {Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(2d), Linq.of(tuple1s).maxByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).maxByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN), Tuple.create(1d)};
        assertSame(tuple1s4[2], Linq.of(tuple1s4).maxByDouble(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMaxByDoubleNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxByDoubleNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<Double>empty()).maxByDoubleNull(null));
        assertNull(Linq.<Double>empty().maxByDoubleNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(2d), Linq.of(tuple1s).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s4 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN), Tuple.create(1d)};
        assertSame(tuple1s4[3], Linq.of(tuple1s4).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMaxByDecimal() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxByDecimal(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<BigDecimal>empty()).maxByDecimal(null));
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().maxByDecimal(x -> x));

        Tuple1[] tuple1s = {Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("3")), Linq.of(tuple1s).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMaxByDecimalNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxByDecimalNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.<BigDecimal>empty()).maxByDecimalNull(null));
        assertNull(Linq.<BigDecimal>empty().maxByDecimalNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("3")), Linq.of(tuple1s).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMaxBy() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).maxBy(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.empty()).maxBy(null));
        assertThrows(InvalidOperationException.class, () -> Linq.empty().maxBy(x -> x));

        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.of(tuple1s).maxBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).maxBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s3).maxBy(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMaxByNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).maxByNull(x -> x));
        assertThrows(ArgumentNullException.class, () -> (Linq.empty()).maxByNull(null));
        assertNull(Linq.empty().maxByNull(x -> x));

        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.of(tuple1s).maxByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertSame(tuple1s2[0], Linq.of(tuple1s2).maxByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertEquals(tuple1s3[0], Linq.of(tuple1s3).maxByNull(tuple -> (Float) tuple.getItem1()));
    }
}
