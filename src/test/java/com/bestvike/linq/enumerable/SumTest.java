package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SumTest extends IteratorTest {

    @Test
    public void testSumLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumLong());

        final Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        Assert.assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong());

        final Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong();
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloat() {
        final Float[] numbers = {null, 0f, 2f, 3f};
        Assert.assertEquals(5f, Linq.asEnumerable(numbers).sumFloat(), 0f);

        final Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        Assert.assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat(), 0f);
    }

    @Test
    public void testSumDouble() {
        final Double[] numbers = {null, 0d, 2d, 3d};
        Assert.assertEquals(5d, Linq.asEnumerable(numbers).sumDouble(), 0d);

        final Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        Assert.assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble(), 0f);
    }

    @Test
    public void testSumDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal());

        final BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal());
    }

    @Test
    public void testSumIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumInt(n -> n));

        final Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        Assert.assertEquals(Integer.MAX_VALUE, Linq.asEnumerable(numbers2).sumInt(n -> n));

        final Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        try {
            int num = Linq.asEnumerable(numbers3).sumInt(n -> n);
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumLong(n -> n));

        final Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        Assert.assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong(n -> n));

        final Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong(n -> n);
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, 3f};
        Assert.assertEquals(5f, Linq.asEnumerable(numbers).sumFloat(n -> n), 0f);

        final Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        Assert.assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat(n -> n), 0f);
    }

    @Test
    public void testSumDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, 3d};
        Assert.assertEquals(5d, Linq.asEnumerable(numbers).sumDouble(n -> n), 0d);

        final Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        Assert.assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble(n -> n), 0f);
    }

    @Test
    public void testSumDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal(n -> n));

        final BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal(n -> n));
    }

}