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
class MinByTest extends TestCase {
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
        assertEquals(null, Linq.of(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));

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
        assertEquals(null, Linq.of(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

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
        assertEquals(null, Linq.of(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
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
        assertEquals(null, Linq.of(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(1f), Tuple.create(null)};
        assertEquals(tuple1s3[0], Linq.of(tuple1s3).minByNull(tuple -> (Float) tuple.getItem1()));
    }
}
