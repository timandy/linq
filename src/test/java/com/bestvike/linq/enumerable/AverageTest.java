package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AverageTest extends IteratorTest {

    @Test
    public void testAverageInt() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNull() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull());
    }

    @Test
    public void testAverageLong() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNull() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull());
    }

    @Test
    public void testAverageFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull());
    }

    @Test
    public void testAverageDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull());
    }

    @Test
    public void testAverageDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull());
    }

    @Test
    public void testAverageIntWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(n -> n), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(n -> n), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull(n -> n));
    }

    @Test
    public void testAverageLongWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(n -> n), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(n -> n), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull(n -> n));
    }

    @Test
    public void testAverageFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull(n -> n));
    }

    @Test
    public void testAverageDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull(n -> n));
    }

    @Test
    public void testAverageDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull(n -> n));
    }

}