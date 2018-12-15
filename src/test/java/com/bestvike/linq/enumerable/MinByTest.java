package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MinByTest extends EnumerableTest {
    @Test
    public void testMinByInt() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByInt(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByInt(tuple -> (Integer) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByIntNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByIntNull(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMinByLong() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLong(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByLong(tuple -> (Long) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByLongNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLongNull(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMinByFloat() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloat(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByFloat(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByFloatNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMinByDouble() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDouble(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDouble(tuple -> (Double) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByDoubleNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMinByDecimal() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDecimal(tuple -> (BigDecimal) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByDecimalNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMinBy() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minBy(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minBy(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minByNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));
    }
}
