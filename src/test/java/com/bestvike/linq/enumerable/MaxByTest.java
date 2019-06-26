package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MaxByTest extends TestCase {
    @Test
    public void testMaxByInt() {
        Tuple1[] tuple1s = {Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByInt(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxByInt(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMaxByIntNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByIntNull(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMaxByLong() {
        Tuple1[] tuple1s = {Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLong(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxByLong(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMaxByLongNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLongNull(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMaxByFloat() {
        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[0], Linq.asEnumerable(tuple1s3).maxByFloat(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByFloatNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).maxByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByDouble() {
        Tuple1[] tuple1s = {Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[0], Linq.asEnumerable(tuple1s3).maxByDouble(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMaxByDoubleNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMaxByDecimal() {
        Tuple1[] tuple1s = {Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("3")), Linq.asEnumerable(tuple1s).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMaxByDecimalNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(m("0")), Tuple.create(m("2")), Tuple.create(m("3"))};
        assertEquals(Tuple.create(m("3")), Linq.asEnumerable(tuple1s).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMaxBy() {
        Tuple1[] tuple1s = {Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(tuple1s2).maxBy(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).maxByNull(tuple -> (Float) tuple.getItem1()));
    }
}
