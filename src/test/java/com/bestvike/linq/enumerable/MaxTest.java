package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MaxTest extends EnumerableTest {
    @Test
    public void testMaxInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(3, Linq.asEnumerable(numbers).maxInt());

        final Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNull() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 3, Linq.asEnumerable(numbers).maxIntNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull());
    }

    @Test
    public void testMaxLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(3L, Linq.asEnumerable(numbers).maxLong());

        final Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNull() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 3L, Linq.asEnumerable(numbers).maxLongNull());

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull());
    }

    @Test
    public void testMaxFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull());
    }

    @Test
    public void testMaxDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull());
    }

    @Test
    public void testMaxDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull());
    }

    @Test
    public void testMax() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).max(), 0d);

        final Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).maxNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxNull());
    }

    @Test
    public void testMaxIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(3, Linq.asEnumerable(numbers).maxInt(n -> n));

        final Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 3, Linq.asEnumerable(numbers).maxIntNull(n -> n));

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull(n -> n));
    }

    @Test
    public void testMaxLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(3L, Linq.asEnumerable(numbers).maxLong(n -> n));

        final Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 3L, Linq.asEnumerable(numbers).maxLongNull(n -> n));

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull(n -> n));
    }

    @Test
    public void testMaxFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull(n -> n));
    }

    @Test
    public void testMaxDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull(n -> n));
    }

    @Test
    public void testMaxDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull(n -> n));
    }

    @Test
    public void testMaxWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).max(n -> n);
        Assert.assertEquals(Float.NaN, f, 0d);

        final Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).maxNull(n -> n);
        Assert.assertEquals(Float.NaN, f, 0d);

        final Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).maxNull(n -> n);
        Assert.assertEquals(null, f2);
    }
}
