package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MinByTest extends TestCase {
    @Test
    public void testMinByInt() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByInt(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByInt(tuple -> (Integer) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByIntNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByIntNull(tuple -> (Integer) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMinByLong() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLong(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByLong(tuple -> (Long) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByLongNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLongNull(tuple -> (Long) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMinByFloat() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloat(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByFloat(tuple -> (Float) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).minByFloat(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMinByFloatNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Float.NaN), Tuple.create(Float.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).minByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMinByDouble() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDouble(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDouble(tuple -> (Double) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).minByDouble(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMinByDoubleNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        Tuple1[] tuple1s3 = {Tuple.create(null), Tuple.create(Double.NaN), Tuple.create(Double.NaN)};
        assertSame(tuple1s3[1], Linq.asEnumerable(tuple1s3).minByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMinByDecimal() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDecimal(tuple -> (BigDecimal) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByDecimalNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMinBy() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minBy(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minBy(tuple -> (Float) tuple.getItem1());
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByNull() {
        Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minByNull(tuple -> (Float) tuple.getItem1()));

        Tuple1[] tuple1s2 = {Tuple.create(null)};
        assertEquals(null, Linq.asEnumerable(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));
    }
}
