package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
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
        Tuple1[] tuple1s = {Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(0), Linq.of(tuple1s).minByInt(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByInt(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMinByIntNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(0), Linq.of(tuple1s).minByIntNull(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    void testMinByLong() {
        Tuple1[] tuple1s = {Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(0L), Linq.of(tuple1s).minByLong(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByLong(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMinByLongNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(0L), Linq.of(tuple1s).minByLongNull(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    void testMinByFloat() {
        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.of(tuple1s).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).minByFloat(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByFloatNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.of(tuple1s).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).minByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByDouble() {
        Tuple1[] tuple1s = {Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(Double.NaN), Linq.of(tuple1s).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[0], Linq.of(tuple1s3).minByDouble(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMinByDoubleNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(Double.NaN), Linq.of(tuple1s).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.of(tuple1s3).minByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    void testMinByDecimal() {
        Tuple1[] tuple1s = {Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("0")), Linq.of(tuple1s).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMinByDecimalNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("0")), Linq.of(tuple1s).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    void testMinBy() {
        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.of(tuple1s2).minBy(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    void testMinByNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.of(tuple1s).minByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.of(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));
    }
}
