package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MinTest extends IteratorTest {

    @Test
    public void testMinInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt());

        final Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNull() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull());
    }

    @Test
    public void testMinLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong());

        final Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNull() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull());

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull());
    }

    @Test
    public void testMinFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull());
    }

    @Test
    public void testMinDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull());
    }

    @Test
    public void testMinDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull());
    }

    @Test
    public void testMin() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).min(), 0d);

        final Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).minNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minNull());
    }

    @Test
    public void testMinIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt(n -> n));

        final Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull(n -> n));

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull(n -> n));
    }

    @Test
    public void testMinLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong(n -> n));

        final Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull(n -> n));

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull(n -> n));
    }

    @Test
    public void testMinFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull(n -> n));
    }

    @Test
    public void testMinDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull(n -> n));
    }

    @Test
    public void testMinDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull(n -> n));
    }

    @Test
    public void testMinWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).min(n -> n);
        Assert.assertEquals(0f, f, 0d);

        final Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).minNull(n -> n);
        Assert.assertEquals(0f, f, 0d);

        final Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).minNull(n -> n);
        Assert.assertEquals(null, f2);
    }

}