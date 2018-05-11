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
public class MaxByTest extends EnumerableTest {

    @Test
    public void testMaxByInt() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByInt(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByInt(tuple -> (Integer) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByIntNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByIntNull(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMaxByLong() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLong(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByLong(tuple -> (Long) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByLongNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLongNull(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMaxByFloat() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloat(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByFloat(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByFloatNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByDouble() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDouble(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByDouble(tuple -> (Double) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByDoubleNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMaxByDecimal() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("3")), Linq.asEnumerable(tuple1s).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByDecimalNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("3")), Linq.asEnumerable(tuple1s).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMaxBy() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxBy(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxBy(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxByNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByNull(tuple -> (Float) tuple.getItem1()));
    }

}