package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MinTest extends EnumerableTest {
    @Test
    public void testMinInt() {
        Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt());

        Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNull() {
        Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull());

        Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull());
    }

    @Test
    public void testMinLong() {
        Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong());

        Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNull() {
        Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull());

        Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull());
    }

    @Test
    public void testMinFloat() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(), 0d);

        Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(), 0d);

        Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull());
    }

    @Test
    public void testMinDouble() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(), 0d);

        Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNull() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(), 0d);

        Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull());
    }

    @Test
    public void testMinDecimal() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal());

        Integer[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNull() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull());

        Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull());
    }

    @Test
    public void testMin() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).min(), 0d);

        Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).minNull(), 0d);

        Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minNull());
    }

    @Test
    public void testMinIntWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt(n -> n));

        Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNullWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull(n -> n));

        Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull(n -> n));
    }

    @Test
    public void testMinLongWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong(n -> n));

        Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNullWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull(n -> n));

        Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull(n -> n));
    }

    @Test
    public void testMinFloatWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(n -> n), 0d);

        Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(n -> n), 0d);

        Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull(n -> n));
    }

    @Test
    public void testMinDoubleWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(n -> n), 0d);

        Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNullWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(n -> n), 0d);

        Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull(n -> n));
    }

    @Test
    public void testMinDecimalWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull(n -> n));
    }

    @Test
    public void testMinWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).min(n -> n);
        Assert.assertEquals(0f, f, 0d);

        Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).minNull(n -> n);
        Assert.assertEquals(0f, f, 0d);

        Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).minNull(n -> n);
        Assert.assertEquals(null, f2);
    }
}
