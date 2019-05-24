package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SumTest extends TestCase {
    @Test
    public void testSumLong() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(5, Linq.asEnumerable(numbers).sumLong());

        Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong());

        Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong();
            fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloat() {
        Float[] numbers = {null, 0f, 2f, 3f};
        assertEquals(5f, Linq.asEnumerable(numbers).sumFloat());

        Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat());
    }

    @Test
    public void testSumDouble() {
        Double[] numbers = {null, 0d, 2d, 3d};
        assertEquals(5d, Linq.asEnumerable(numbers).sumDouble());

        Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble());
    }

    @Test
    public void testSumDecimal() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal());

        BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal());
    }

    @Test
    public void testSumIntWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(5, Linq.asEnumerable(numbers).sumInt(n -> n));

        Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        assertEquals(Integer.MAX_VALUE, Linq.asEnumerable(numbers2).sumInt(n -> n));

        Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        try {
            int num = Linq.asEnumerable(numbers3).sumInt(n -> n);
            fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumLongWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(5, Linq.asEnumerable(numbers).sumLong(n -> n));

        Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong(n -> n));

        Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong(n -> n);
            fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloatWithSelector() {
        Float[] numbers = {null, 0f, 2f, 3f};
        assertEquals(5f, Linq.asEnumerable(numbers).sumFloat(n -> n));

        Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat(n -> n));
    }

    @Test
    public void testSumDoubleWithSelector() {
        Double[] numbers = {null, 0d, 2d, 3d};
        assertEquals(5d, Linq.asEnumerable(numbers).sumDouble(n -> n));

        Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble(n -> n));
    }

    @Test
    public void testSumDecimalWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal(n -> n));

        BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal(n -> n));
    }
}
