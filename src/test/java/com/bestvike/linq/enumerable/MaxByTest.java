package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class MaxByTest extends TestCase {
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
        assertEquals(null, Linq.of(tuple1s2).maxByIntNull(tuple -> (Integer) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).maxByLongNull(tuple -> (Long) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

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
        assertEquals(null, Linq.of(tuple1s2).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

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
        assertEquals(null, Linq.of(tuple1s2).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).maxByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertEquals(tuple1s3[0], Linq.of(tuple1s3).maxByNull(tuple -> (Float) tuple.getItem1()));
    }
}
