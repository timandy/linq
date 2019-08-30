package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.DecimalFunc1;
import com.bestvike.function.DoubleFunc1;
import com.bestvike.function.FloatFunc1;
import com.bestvike.function.Func1;
import com.bestvike.function.IntFunc1;
import com.bestvike.function.LongFunc1;
import com.bestvike.function.NullableDecimalFunc1;
import com.bestvike.function.NullableDoubleFunc1;
import com.bestvike.function.NullableFloatFunc1;
import com.bestvike.function.NullableIntFunc1;
import com.bestvike.function.NullableLongFunc1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class MaxTest extends TestCase {
    private static IEnumerable<Object[]> Max_Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42, 1), 42);
        argsList.add(Linq.range(1, 10).toArray(), 10);
        argsList.add(Linq.of(new int[]{-100, -15, -50, -10}), -10);
        argsList.add(Linq.of(new int[]{-16, 0, 50, 100, 1000}), 1000);
        argsList.add(Linq.of(new int[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Integer.MAX_VALUE, 1)), Integer.MAX_VALUE);

        argsList.add(Linq.repeat(20, 1), 20);
        argsList.add(Linq.repeat(-2, 5), -2);
        argsList.add(Linq.of(new int[]{16, 9, 10, 7, 8}), 16);
        argsList.add(Linq.of(new int[]{6, 9, 10, 0, 50}), 50);
        argsList.add(Linq.of(new int[]{-6, 0, -9, 0, -10, 0}), 0);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_Long_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42L, 1), 42L);
        argsList.add(Linq.range(1, 10).select(i -> (long) i).toArray(), 10L);
        argsList.add(Linq.of(new long[]{-100, -15, -50, -10}), -10L);
        argsList.add(Linq.of(new long[]{-16, 0, 50, 100, 1000}), 1000L);
        argsList.add(Linq.of(new long[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Long.MAX_VALUE, 1)), Long.MAX_VALUE);

        argsList.add(Linq.repeat(Integer.MAX_VALUE + 10L, 1), Integer.MAX_VALUE + 10L);
        argsList.add(Linq.repeat(500L, 5), 500L);
        argsList.add(Linq.of(new long[]{250, 49, 130, 47, 28}), 250L);
        argsList.add(Linq.of(new long[]{6, 9, 10, 0, Integer.MAX_VALUE + 50L}), Integer.MAX_VALUE + 50L);
        argsList.add(Linq.of(new long[]{6, 50, 9, 50, 10, 50}), 50L);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_Float_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42f, 1), 42f);
        argsList.add(Linq.range(1, 10).select(i -> (float) i).toArray(), 10f);
        argsList.add(Linq.of(new float[]{-100, -15, -50, -10}), -10f);
        argsList.add(Linq.of(new float[]{-16, 0, 50, 100, 1000}), 1000f);
        argsList.add(Linq.of(new float[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Float.MAX_VALUE, 1)), Float.MAX_VALUE);

        argsList.add(Linq.repeat(5.5f, 1), 5.5f);
        argsList.add(Linq.of(new float[]{112.5f, 4.9f, 30f, 4.7f, 28f}), 112.5f);
        argsList.add(Linq.of(new float[]{6.8f, 9.4f, -10f, 0f, Float.NaN, 53.6f}), 53.6f);
        argsList.add(Linq.of(new float[]{-5.5f, Float.POSITIVE_INFINITY, 9.9f, Float.POSITIVE_INFINITY}), Float.POSITIVE_INFINITY);

        argsList.add(Linq.repeat(Float.NaN, 5), Float.NaN);
        argsList.add(Linq.of(new float[]{Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f}), 10f);
        argsList.add(Linq.of(new float[]{6.8f, 9.4f, 10f, 0, -5.6f, Float.NaN}), 10f);
        argsList.add(Linq.of(new float[]{Float.NaN, Float.NEGATIVE_INFINITY}), Float.NEGATIVE_INFINITY);
        argsList.add(Linq.of(new float[]{Float.NEGATIVE_INFINITY, Float.NaN}), Float.NEGATIVE_INFINITY);

        // Normally NaN < anything and anything < NaN returns false
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        argsList.add(Linq.range(1, 10).select(i -> (float) i).concat(Linq.repeat(Float.NaN, 1)).toArray(), 10f);
        argsList.add(Linq.of(new float[]{-1f, -10, Float.NaN, 10, 200, 1000}), 1000f);
        argsList.add(Linq.of(new float[]{Float.MIN_VALUE, 3000f, 100, 200, Float.NaN, 1000}), 3000f);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_Double_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42.0, 1), 42.0);
        argsList.add(Linq.range(1, 10).select(i -> (double) i).toArray(), 10.0);
        argsList.add(Linq.of(new double[]{-100, -15, -50, -10}), -10.0);
        argsList.add(Linq.of(new double[]{-16, 0, 50, 100, 1000}), 1000.0);
        argsList.add(Linq.of(new double[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Double.MAX_VALUE, 1)), Double.MAX_VALUE);

        argsList.add(Linq.repeat(5.5, 1), 5.5);
        argsList.add(Linq.repeat(Double.NaN, 5), Double.NaN);
        argsList.add(Linq.of(new double[]{112.5, 4.9, 30, 4.7, 28}), 112.5);
        argsList.add(Linq.of(new double[]{6.8, 9.4, -10, 0, Double.NaN, 53.6}), 53.6);
        argsList.add(Linq.of(new double[]{-5.5, Double.POSITIVE_INFINITY, 9.9, Double.POSITIVE_INFINITY}), Double.POSITIVE_INFINITY);
        argsList.add(Linq.of(new double[]{Double.NaN, 6.8, 9.4, 10.5, 0, -5.6}), 10.5);
        argsList.add(Linq.of(new double[]{6.8, 9.4, 10.5, 0, -5.6, Double.NaN}), 10.5);
        argsList.add(Linq.of(new double[]{Double.NaN, Double.NEGATIVE_INFINITY}), Double.NEGATIVE_INFINITY);
        argsList.add(Linq.of(new double[]{Double.NEGATIVE_INFINITY, Double.NaN}), Double.NEGATIVE_INFINITY);

        // Normally NaN < anything and anything < NaN returns false
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        argsList.add(Linq.range(1, 10).select(i -> (double) i).concat(Linq.repeat(Double.NaN, 1)).toArray(), 10.0);
        argsList.add(Linq.of(new double[]{-1F, -10, Double.NaN, 10, 200, 1000}), 1000.0);
        argsList.add(Linq.of(new double[]{Double.MIN_VALUE, 3000F, 100, 200, Double.NaN, 1000}), 3000.0);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_Decimal_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(m(42), 1), m(42));
        argsList.add(Linq.range(1, 10).select(i -> m(i)).toArray(), m(10));
        argsList.add(Linq.of(m(-100), m(-15), m(-50), m(-10)), m(-10));
        argsList.add(Linq.of(m(-16), m(0), m(50), m(100), m(1000)), m(1000));
        argsList.add(Linq.of(m(-16), m(0), m(50), m(100), m(1000)).concat(Linq.repeat(MAX_DECIMAL, 1)), MAX_DECIMAL);

        argsList.add(Linq.of(m("5.5")), m("5.5"));
        argsList.add(Linq.repeat(m("-3.4"), 5), m("-3.4"));
        argsList.add(Linq.of(m("122.5"), m("4.9"), m("10"), m("4.7"), m("28")), m("122.5"));
        argsList.add(Linq.of(m("6.8"), m("9.4"), m("10"), m("0"), m("0"), MAX_DECIMAL), MAX_DECIMAL);
        argsList.add(Linq.of(m("-5.5"), m("0"), m("9.9"), m("-5.5"), m("9.9")), m("9.9"));
        return argsList;
    }

    private static IEnumerable<Object[]> Max_NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42, 1), 42);
        argsList.add(Linq.range(1, 10).select(i -> i).toArray(), 10);
        argsList.add(Linq.of(null, -100, -15, -50, -10), -10);
        argsList.add(Linq.of(null, -16, 0, 50, 100, 1000), 1000);
        argsList.add(Linq.of(null, -16, 0, 50, 100, 1000).concat(Linq.repeat(Integer.MAX_VALUE, 1)), Integer.MAX_VALUE);
        argsList.add(Linq.repeat(null, 100), null);

        argsList.add(Linq.<Integer>empty(), null);
        argsList.add(Linq.repeat(-20, 1), -20);
        argsList.add(Linq.of(-6, null, -9, -10, null, -17, -18), -6);
        argsList.add(Linq.of(null, null, null, null, null, -5), -5);
        argsList.add(Linq.of(6, null, null, 100, 9, 100, 10, 100), 100);
        argsList.add(Linq.repeat(null, 5), null);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_NullableLong_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42L, 1), 42L);
        argsList.add(Linq.range(1, 10).select(i -> (long) i).toArray(), 10L);
        argsList.add(Linq.of(null, -100L, -15L, -50L, -10L), -10L);
        argsList.add(Linq.of(null, -16L, 0L, 50L, 100L, 1000L), 1000L);
        argsList.add(Linq.of(null, -16L, 0L, 50L, 100L, 1000L).concat(Linq.repeat(Long.MAX_VALUE, 1)), Long.MAX_VALUE);
        argsList.add(Linq.repeat(null, 100), null);

        argsList.add(Linq.<Long>empty(), null);
        argsList.add(Linq.repeat(Long.MAX_VALUE, 1), Long.MAX_VALUE);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(Long.MAX_VALUE, null, 9L, 10L, null, 7L, 8L), Long.MAX_VALUE);
        argsList.add(Linq.of(null, null, null, null, null, -Long.MAX_VALUE), -Long.MAX_VALUE);
        argsList.add(Linq.of(-6L, null, null, 0L, -9L, 0L, -10L, -30L), 0L);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_NullableFloat_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42f, 1), 42f);
        argsList.add(Linq.range(1, 10).select(i -> (float) i).toArray(), 10f);
        argsList.add(Linq.of(null, -100f, -15f, -50f, -10f), -10f);
        argsList.add(Linq.of(null, -16f, 0f, 50f, 100f, 1000f), 1000f);
        argsList.add(Linq.of(null, -16f, 0f, 50f, 100f, 1000f).concat(Linq.repeat(Float.MAX_VALUE, 1)), Float.MAX_VALUE);
        argsList.add(Linq.repeat(null, 100), null);

        argsList.add(Linq.<Float>empty(), null);
        argsList.add(Linq.repeat(Float.MIN_VALUE, 1), Float.MIN_VALUE);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(14.50f, null, Float.NaN, 10.98f, null, 7.5f, 8.6f), 14.50f);
        argsList.add(Linq.of(null, null, null, null, null, 0f), 0f);
        argsList.add(Linq.of(-6.4f, null, null, -0.5f, -9.4f, -0.5f, -10.9f, -0.5f), -0.5f);

        argsList.add(Linq.of(Float.NaN, 6.8f, 9.4f, 10f, 0f, null, -5.6f), 10f);
        argsList.add(Linq.of(6.8f, 9.4f, 10f, 0f, null, -5.6f, Float.NaN), 10f);
        argsList.add(Linq.of(Float.NaN, Float.NEGATIVE_INFINITY), Float.NEGATIVE_INFINITY);
        argsList.add(Linq.of(Float.NEGATIVE_INFINITY, Float.NaN), Float.NEGATIVE_INFINITY);
        argsList.add(Linq.repeat(Float.NaN, 3), Float.NaN);
        argsList.add(Linq.of(Float.NaN, null, null, null), Float.NaN);
        argsList.add(Linq.of(null, null, null, Float.NaN), Float.NaN);
        argsList.add(Linq.of(null, Float.NaN, null), Float.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_NullableDouble_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42d, 1), 42.0);
        argsList.add(Linq.range(1, 10).select(i -> (double) i).toArray(), 10.0);
        argsList.add(Linq.of(null, -100d, -15d, -50d, -10d), -10.0);
        argsList.add(Linq.of(null, -16d, 0d, 50d, 100d, 1000d), 1000.0);
        argsList.add(Linq.of(null, -16d, 0d, 50d, 100d, 1000d).concat(Linq.repeat(Double.MAX_VALUE, 1)), Double.MAX_VALUE);
        argsList.add(Linq.repeat(null, 100), null);

        argsList.add(Linq.<Double>empty(), null);
        argsList.add(Linq.repeat(Double.MIN_VALUE, 1), Double.MIN_VALUE);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(14.50, null, Double.NaN, 10.98, null, 7.5, 8.6), 14.50);
        argsList.add(Linq.of(null, null, null, null, null, 0d), 0.0);
        argsList.add(Linq.of(-6.4, null, null, -0.5, -9.4, -0.5, -10.9, -0.5), -0.5);

        argsList.add(Linq.of(Double.NaN, 6.8, 9.4, 10.5, 0d, null, -5.6), 10.5);
        argsList.add(Linq.of(6.8, 9.4, 10.8, 0d, null, -5.6, Double.NaN), 10.8);
        argsList.add(Linq.of(Double.NaN, Double.NEGATIVE_INFINITY), Double.NEGATIVE_INFINITY);
        argsList.add(Linq.of(Double.NEGATIVE_INFINITY, Double.NaN), Double.NEGATIVE_INFINITY);
        argsList.add(Linq.repeat(Double.NaN, 3), Double.NaN);
        argsList.add(Linq.of(Double.NaN, null, null, null), Double.NaN);
        argsList.add(Linq.of(null, null, null, Double.NaN), Double.NaN);
        argsList.add(Linq.of(null, Double.NaN, null), Double.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_NullableDecimal_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(m(42), 1), m(42));
        argsList.add(Linq.range(1, 10).select(i -> m(i)).toArray(), m(10));
        argsList.add(Linq.of(null, m(-100), m(-15), m(-50), m(-10)), m(-10));
        argsList.add(Linq.of(null, m(-16), m(0), m(50), m(100), m(1000)), m(1000));
        argsList.add(Linq.of(null, m(-16), m(0), m(50), m(100), m(1000)).concat(Linq.repeat(MAX_DECIMAL, 1)), MAX_DECIMAL);
        argsList.add(Linq.repeat(null, 100), null);

        argsList.add(Linq.<BigDecimal>empty(), null);
        argsList.add(Linq.repeat(MAX_DECIMAL, 1), MAX_DECIMAL);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(m("14.50"), null, null, m("10.98"), null, m("7.5"), m("8.6")), m("14.50"));
        argsList.add(Linq.of(null, null, null, null, null, m(0)), m(0));
        argsList.add(Linq.of(m("6.4"), null, null, MAX_DECIMAL, m("9.4"), MAX_DECIMAL, m("10.9"), MAX_DECIMAL), MAX_DECIMAL);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_DateTime_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> newDate(2000, 1, i)).toArray(), newDate(2000, 1, 10));
        argsList.add(Linq.of(newDate(2000, 12, 1), newDate(2000, 12, 31), newDate(2000, 1, 12)), newDate(2000, 12, 31));

        Date[] threeThousand = new Date[]{
                newDate(3000, 1, 1),
                newDate(100, 1, 1),
                newDate(200, 1, 1),
                newDate(1000, 1, 1)
        };
        argsList.add(Linq.of(threeThousand), newDate(3000, 1, 1));
        argsList.add(Linq.of(threeThousand).concat(Linq.repeat(MAX_DATE, 1)), MAX_DATE);
        return argsList;
    }

    private static IEnumerable<Object[]> Max_String_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> i.toString()).toArray(), "9");
        argsList.add(Linq.of("Alice", "Bob", "Charlie", "Eve", "Mallory", "Victor", "Trent"), "Victor");
        argsList.add(Linq.of(null, "Charlie", null, "Victor", "Trent", null, "Eve", "Alice", "Mallory", "Bob"), "Victor");

        argsList.add(Linq.<String>empty(), null);
        argsList.add(Linq.repeat("Hello", 1), "Hello");
        argsList.add(Linq.repeat("hi", 5), "hi");
        argsList.add(Linq.of("zzz", "aaa", "abcd", "bark", "temp", "cat"), "zzz");
        argsList.add(Linq.of(null, null, null, null, "aAa"), "aAa");
        argsList.add(Linq.of("ooo", "ccc", "ccc", "ooo", "ooo", "nnn"), "ooo");
        argsList.add(Linq.repeat(null, 5), null);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.max(), q.max());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.max(), q.max());
    }

    @Test
    void Max_Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxInt(i -> i));
    }

    @Test
    void Max_Int_EmptySource_ThrowsInvalidOpertionException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxInt());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxInt(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_Int_TestData")
    void Max_Int(IEnumerable<Integer> source, int expected) {
        assertEquals(expected, source.maxInt());
        assertEquals(expected, source.maxInt(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_Long_TestData")
    void Max_Long(IEnumerable<Long> source, long expected) {
        assertEquals(expected, source.maxLong());
        assertEquals(expected, source.maxLong(x -> x));
    }

    @Test
    void Max_Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLong(i -> i));
    }

    @Test
    void Max_Long_EmptySource_ThrowsInvalidOpertionException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().maxLong());
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().maxLong(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_Float_TestData")
    void Max_Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.maxFloat());
        assertEquals(expected, source.maxFloat(x -> x));
    }

    @Test
    void Max_Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloat(i -> i));
    }

    @Test
    void Max_Float_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().maxFloat());
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().maxFloat(x -> x));
    }

    @Test
    void Max_Float_SeveralNaNWithSelector() {
        assertTrue(Float.isNaN(Linq.repeat(Float.NaN, 5).maxFloat(i -> i)));
    }

    @Test
    void Max_NullableFloat_SeveralNaNOrNullWithSelector() {
        Float[] source = new Float[]{Float.NaN, null, Float.NaN, null};
        assertTrue(Float.isNaN(Linq.of(source).maxFloatNull(i -> i)));
    }

    @Test
    void Max_Float_NaNAtStartWithSelector() {
        float[] source = {Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f};
        assertEquals(10f, Linq.of(source).maxFloat(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_Double_TestData")
    void Max_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.maxDouble());
        assertEquals(expected, source.maxDouble(x -> x));
    }

    @Test
    void Max_Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDouble(i -> i));
    }

    @Test
    void Max_Double_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().maxDouble());
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().maxDouble(x -> x));
    }

    @Test
    void Max_Double_AllNaNWithSelector() {
        assertTrue(Double.isNaN(Linq.repeat(Double.NaN, 5).maxDouble(i -> i)));
    }

    @Test
    void Max_Double_SeveralNaNOrNullWithSelector() {
        Double[] source = new Double[]{Double.NaN, null, Double.NaN, null};
        assertTrue(Double.isNaN(Linq.of(source).maxDoubleNull(i -> i)));
    }

    @Test
    void Max_Double_NaNThenNegativeInfinityWithSelector() {
        double[] source = {Double.NaN, Double.NEGATIVE_INFINITY};
        assertEquals(Linq.of(source).maxDouble(i -> i), Double.NEGATIVE_INFINITY);
    }

    @ParameterizedTest
    @MethodSource("Max_Decimal_TestData")
    void Max_Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.maxDecimal());
        assertEquals(expected, source.maxDecimal(x -> x));
    }

    @Test
    void Max_Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimal(i -> i));
    }

    @Test
    void Max_Decimal_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().maxDecimal());
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().maxDecimal(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableInt_TestData")
    void Max_NullableInt(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.maxIntNull());
        assertEquals(expected, source.maxIntNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableInt_TestData")
    void Max_NullableIntRunOnce(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.runOnce().maxIntNull());
        assertEquals(expected, source.runOnce().maxIntNull(x -> x));
    }

    @Test
    void Max_NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxIntNull(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableLong_TestData")
    void Max_NullableLong(IEnumerable<Long> source, Long expected) {
        assertEquals(expected, source.maxLongNull());
        assertEquals(expected, source.maxLongNull(x -> x));
    }

    @Test
    void Max_NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLongNull(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableFloat_TestData")
    void Max_NullableFloat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.maxFloatNull());
        assertEquals(expected, source.maxFloatNull(x -> x));
    }

    @Test
    void Max_NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloatNull(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableDouble_TestData")
    void Max_NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.maxDoubleNull());
        assertEquals(expected, source.maxDoubleNull(x -> x));
    }

    @Test
    void Max_NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDoubleNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDoubleNull(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_NullableDecimal_TestData")
    void Max_NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.maxDecimalNull());
        assertEquals(expected, source.maxDecimalNull(x -> x));
    }

    @Test
    void Max_NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimalNull(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_DateTime_TestData")
    void Max_DateTime(IEnumerable<Date> source, Date expected) {
        assertEquals(expected, source.max());
        assertEquals(expected, source.max(x -> x));
    }

    @Test
    void Max_DateTime_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).max());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).max(i -> i));
    }

    @Test
    void Max_DateTime_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().max());
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().max(i -> i));
    }

    @ParameterizedTest
    @MethodSource("Max_String_TestData")
    void Max_String(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.maxNull());
        assertEquals(expected, source.maxNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Max_String_TestData")
    void Max_StringRunOnce(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.runOnce().maxNull());
        assertEquals(expected, source.runOnce().maxNull(x -> x));
    }

    @Test
    void Max_String_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).maxNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).maxNull(i -> i));
    }

    @Test
    void Max_Int_NullSelector_ThrowsArgumentNullException() {
        IntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().maxInt(selector));
    }

    @Test
    void Max_Int_WithSelectorAccessingProperty() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", -105),
                new NameNum<>("Bob", 30)
        };

        assertEquals(30, Linq.of(source).maxInt(e -> e.num));
    }

    @Test
    void Max_Long_NullSelector_ThrowsArgumentNullException() {
        LongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().maxLong(selector));
    }

    @Test
    void Max_Long_WithSelectorAccessingProperty() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", 10L),
                new NameNum<>("John", -105L),
                new NameNum<>("Bob", Long.MAX_VALUE)
        };
        assertEquals(Long.MAX_VALUE, Linq.of(source).maxLong(e -> e.num));
    }

    @Test
    void Max_Float_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 40.5f),
                new NameNum<>("John", -10.25f),
                new NameNum<>("Bob", 100.45f)
        };

        assertEquals(100.45f, Linq.of(source).select(e -> e.num).maxFloat());
    }

    @Test
    void Max_Float_NullSelector_ThrowsArgumentNullException() {
        FloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().maxFloat(selector));
    }

    @Test
    void Max_Double_NullSelector_ThrowsArgumentNullException() {
        DoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().maxDouble(selector));
    }

    @Test
    void Max_Double_WithSelectorAccessingField() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 40.5d),
                new NameNum<>("John", -10.25d),
                new NameNum<>("Bob", 100.45d)
        };
        assertEquals(100.45, Linq.of(source).maxDouble(e -> e.num));
    }

    @Test
    void Max_Decimal_NullSelector_ThrowsArgumentNullException() {
        DecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().maxDecimal(selector));
    }

    @Test
    void Max_Decimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("420.5")),
                new NameNum<>("John", m("900.25")),
                new NameNum<>("Bob", m("10.45"))
        };
        assertEquals(m("900.25"), Linq.of(source).maxDecimal(e -> e.num));
    }

    @Test
    void Max_NullableInt_NullSelector_ThrowsArgumentNullException() {
        NullableIntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().maxIntNull(selector));
    }

    @Test
    void Max_NullableInt_WithSelectorAccessingField() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", -105),
                new NameNum<>("Bob", null)
        };

        assertEquals(10, Linq.of(source).maxIntNull(e -> e.num));
    }

    @Test
    void Max_NullableLong_NullSelector_ThrowsArgumentNullException() {
        NullableLongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().maxLongNull(selector));
    }

    @Test
    void Max_NullableLong_WithSelectorAccessingField() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", null),
                new NameNum<>("John", -105L),
                new NameNum<>("Bob", Long.MAX_VALUE)
        };
        assertEquals(Long.MAX_VALUE, Linq.of(source).maxLongNull(e -> e.num));
    }

    @Test
    void Max_NullableFloat_NullSelector_ThrowsArgumentNullException() {
        NullableFloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().maxFloatNull(selector));
    }

    @Test
    void Max_NullableFloat_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 40.5f),
                new NameNum<>("John", null),
                new NameNum<>("Bob", 100.45f)
        };
        assertEquals(100.45f, Linq.of(source).maxFloatNull(e -> e.num));
    }

    @Test
    void Max_NullableDouble_NullSelector_ThrowsArgumentNullException() {
        NullableDoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().maxDoubleNull(selector));
    }

    @Test
    void Max_NullableDouble_WithSelectorAccessingProperty() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 40.5),
                new NameNum<>("John", null),
                new NameNum<>("Bob", 100.45)
        };
        assertEquals(100.45, Linq.of(source).maxDoubleNull(e -> e.num));
    }

    @Test
    void Max_NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        NullableDecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().maxDecimalNull(selector));
    }

    @Test
    void Max_NullableDecimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("420.5")),
                new NameNum<>("John", null),
                new NameNum<>("Bob", m("10.45"))
        };
        assertEquals(m("420.5"), Linq.of(source).maxDecimalNull(e -> e.num));
    }

    @Test
    void Max_NullableDateTime_EmptySourceWithSelector() {
        assertNull(Linq.<Date>empty().maxNull(x -> x));
    }

    @Test
    void Max_NullableDateTime_NullSelector_ThrowsArgumentNullException() {
        Func1<Date, Date> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().maxNull(selector));
    }

    @Test
    void Max_String_NullSelector_ThrowsArgumentNullException() {
        Func1<String, String> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<String>empty().maxNull(selector));
    }

    @Test
    void Max_String_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("420.5")),
                new NameNum<>("John", m("900.25")),
                new NameNum<>("Bob", m("10.45"))
        };
        assertEquals("Tim", Linq.of(source).maxNull(e -> e.name));
    }

    @Test
    void Max_Boolean_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Boolean>empty().max());
    }

    @Test
    void testMaxInt() {
        Integer[] numbers = {0, 2, 3};
        assertEquals(3, Linq.of(numbers).maxInt());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxInt());
    }

    @Test
    void testMaxIntNull() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.of(numbers).maxIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxIntNull());
    }

    @Test
    void testMaxLong() {
        Long[] numbers = {0L, 2L, 3L};
        assertEquals(3L, Linq.of(numbers).maxLong());

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxLong());
    }

    @Test
    void testMaxLongNull() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.of(numbers).maxLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxLongNull());
    }

    @Test
    void testMaxFloat() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(2f, Linq.of(numbers).maxFloat());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxFloat());
    }

    @Test
    void testMaxFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.of(numbers).maxFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxFloatNull());
    }

    @Test
    void testMaxDouble() {
        Double[] numbers = {2d, Double.NaN};
        assertEquals(2d, Linq.of(numbers).maxDouble());

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxDouble());
    }

    @Test
    void testMaxDoubleNull() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.of(numbers).maxDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxDoubleNull());
    }

    @Test
    void testMaxDecimal() {
        BigDecimal[] numbers = {m("0"), m("2"), m("3")};
        assertEquals(m("3"), Linq.of(numbers).maxDecimal());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxDecimal());
    }

    @Test
    void testMaxDecimalNull() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("3"), Linq.of(numbers).maxDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxDecimalNull());
    }

    @Test
    void testMax() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).max());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).max());
    }

    @Test
    void testMaxNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).maxNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxNull());
    }

    @Test
    void testMaxIntWithSelector() {
        Integer[] numbers = {0, 2, 3};
        assertEquals(3, Linq.of(numbers).maxInt(n -> n));

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxInt(n -> n));
    }

    @Test
    void testMaxIntNullWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.of(numbers).maxIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxIntNull(n -> n));
    }

    @Test
    void testMaxLongWithSelector() {
        Long[] numbers = {0L, 2L, 3L};
        assertEquals(3L, Linq.of(numbers).maxLong(n -> n));

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxLong(n -> n));
    }

    @Test
    void testMaxLongNullWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.of(numbers).maxLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxLongNull(n -> n));
    }

    @Test
    void testMaxFloatWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(2f, Linq.of(numbers).maxFloat(n -> n));

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxFloat(n -> n));
    }

    @Test
    void testMaxFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.of(numbers).maxFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxFloatNull(n -> n));
    }

    @Test
    void testMaxDoubleWithSelector() {
        Double[] numbers = {2d, Double.NaN};
        assertEquals(2d, Linq.of(numbers).maxDouble(n -> n));

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxDouble(n -> n));
    }

    @Test
    void testMaxDoubleNullWithSelector() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.of(numbers).maxDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxDoubleNull(n -> n));
    }

    @Test
    void testMaxDecimalWithSelector() {
        BigDecimal[] numbers = {m("0"), m("2"), m("3")};
        assertEquals(m("3"), Linq.of(numbers).maxDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).maxDecimal(n -> n));
    }

    @Test
    void testMaxDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("3"), Linq.of(numbers).maxDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).maxDecimalNull(n -> n));
    }

    @Test
    void testMaxWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        Float f = Linq.of(numbers).max(n -> n);
        assertEquals(Float.NaN, f);

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).max(n -> n));
    }

    @Test
    void testMaxNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.of(numbers).maxNull(n -> n);
        assertEquals(Float.NaN, f);

        Float[] numbers2 = {null};
        Float f2 = Linq.of(numbers2).maxNull(n -> n);
        assertEquals(null, f2);
    }

    private static class NameNum<T> extends ValueType {
        private final String name;
        private final T num;

        private NameNum(String name, T num) {
            this.name = name;
            this.num = num;
        }
    }
}
