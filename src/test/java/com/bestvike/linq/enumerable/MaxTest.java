package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class MaxTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.max(), q.max());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.max(), q.max());
    }

    @Test
    public void Max_Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxInt(i -> i));
    }

    @Test
    public void Max_Int_EmptySource_ThrowsInvalidOpertionException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxInt());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().maxInt(x -> x));
    }

    private IEnumerable<Object[]> Max_Int_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42, 1), 42});
        lst.add(new Object[]{Linq.range(1, 10).toArray(), 10});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-100, -15, -50, -10}), -10});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-16, 0, 50, 100, 1000}), 1000});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Integer.MAX_VALUE, 1)), Integer.MAX_VALUE});

        lst.add(new Object[]{Linq.repeat(20, 1), 20});
        lst.add(new Object[]{Linq.repeat(-2, 5), -2});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{16, 9, 10, 7, 8}), 16});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{6, 9, 10, 0, 50}), 50});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-6, 0, -9, 0, -10, 0}), 0});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_Int() {
        for (Object[] objects : this.Max_Int_TestData()) {
            this.Max_Int((IEnumerable<Integer>) objects[0], (int) objects[1]);
        }
    }

    private void Max_Int(IEnumerable<Integer> source, int expected) {
        assertEquals(expected, source.maxInt());
        assertEquals(expected, source.maxInt(x -> x));
    }

    private IEnumerable<Object[]> Max_Long_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42L, 1), 42L});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (long) i).toArray(), 10L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{-100, -15, -50, -10}), -10L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{-16, 0, 50, 100, 1000}), 1000L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Long.MAX_VALUE, 1)), Long.MAX_VALUE});

        lst.add(new Object[]{Linq.repeat(Integer.MAX_VALUE + 10L, 1), Integer.MAX_VALUE + 10L});
        lst.add(new Object[]{Linq.repeat(500L, 5), 500L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{250, 49, 130, 47, 28}), 250L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{6, 9, 10, 0, Integer.MAX_VALUE + 50L}), Integer.MAX_VALUE + 50L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{6, 50, 9, 50, 10, 50}), 50L});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_Long() {
        for (Object[] objects : this.Max_Long_TestData()) {
            this.Max_Long((IEnumerable<Long>) objects[0], (long) objects[1]);
        }
    }

    private void Max_Long(IEnumerable<Long> source, long expected) {
        assertEquals(expected, source.maxLong());
        assertEquals(expected, source.maxLong(x -> x));
    }

    @Test
    public void Max_Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLong(i -> i));
    }

    @Test
    public void Max_Long_EmptySource_ThrowsInvalidOpertionException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().maxLong());
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().maxLong(x -> x));
    }

    private IEnumerable<Object[]> Max_Float_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42f, 1), 42f});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).toArray(), 10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-100, -15, -50, -10}), -10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-16, 0, 50, 100, 1000}), 1000f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Float.MAX_VALUE, 1)), Float.MAX_VALUE});

        lst.add(new Object[]{Linq.repeat(5.5f, 1), 5.5f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{112.5f, 4.9f, 30f, 4.7f, 28f}), 112.5f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{6.8f, 9.4f, -10f, 0f, Float.NaN, 53.6f}), 53.6f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-5.5f, Float.POSITIVE_INFINITY, 9.9f, Float.POSITIVE_INFINITY}), Float.POSITIVE_INFINITY});

        lst.add(new Object[]{Linq.repeat(Float.NaN, 5), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f}), 10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{6.8f, 9.4f, 10f, 0, -5.6f, Float.NaN}), 10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NaN, Float.NEGATIVE_INFINITY}), Float.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NEGATIVE_INFINITY, Float.NaN}), Float.NEGATIVE_INFINITY});

        // Normally NaN < anything and anything < NaN returns false
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).concat(Linq.repeat(Float.NaN, 1)).toArray(), 10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-1f, -10, Float.NaN, 10, 200, 1000}), 1000f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.MIN_VALUE, 3000f, 100, 200, Float.NaN, 1000}), 3000f});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_Float() {
        for (Object[] objects : this.Max_Float_TestData()) {
            this.Max_Float((IEnumerable<Float>) objects[0], (float) objects[1]);
        }
    }

    private void Max_Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.maxFloat());
        assertEquals(expected, source.maxFloat(x -> x));
    }

    @Test
    public void Max_Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloat(i -> i));
    }

    @Test
    public void Max_Float_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().maxFloat());
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().maxFloat(x -> x));
    }

    @Test
    public void Max_Float_SeveralNaNWithSelector() {
        assertTrue(Float.isNaN(Linq.repeat(Float.NaN, 5).maxFloat(i -> i)));
    }

    @Test
    public void Max_NullableFloat_SeveralNaNOrNullWithSelector() {
        Float[] source = new Float[]{Float.NaN, null, Float.NaN, null};
        assertTrue(Float.isNaN(Linq.asEnumerable(source).maxFloatNull(i -> i)));
    }

    @Test
    public void Max_Float_NaNAtStartWithSelector() {
        float[] source = {Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f};
        assertEquals(10f, Linq.asEnumerable(source).maxFloat(i -> i));
    }

    private IEnumerable<Object[]> Max_Double_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42.0, 1), 42.0});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).toArray(), 10.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-100, -15, -50, -10}), -10.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-16, 0, 50, 100, 1000}), 1000.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-16, 0, 50, 100, 1000}).concat(Linq.repeat(Double.MAX_VALUE, 1)), Double.MAX_VALUE});

        lst.add(new Object[]{Linq.repeat(5.5, 1), 5.5});
        lst.add(new Object[]{Linq.repeat(Double.NaN, 5), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{112.5, 4.9, 30, 4.7, 28}), 112.5});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{6.8, 9.4, -10, 0, Double.NaN, 53.6}), 53.6});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-5.5, Double.POSITIVE_INFINITY, 9.9, Double.POSITIVE_INFINITY}), Double.POSITIVE_INFINITY});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NaN, 6.8, 9.4, 10.5, 0, -5.6}), 10.5});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{6.8, 9.4, 10.5, 0, -5.6, Double.NaN}), 10.5});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NaN, Double.NEGATIVE_INFINITY}), Double.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NEGATIVE_INFINITY, Double.NaN}), Double.NEGATIVE_INFINITY});

        // Normally NaN < anything and anything < NaN returns false
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).concat(Linq.repeat(Double.NaN, 1)).toArray(), 10.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-1F, -10, Double.NaN, 10, 200, 1000}), 1000.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.MIN_VALUE, 3000F, 100, 200, Double.NaN, 1000}), 3000.0});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_Double() {
        for (Object[] objects : this.Max_Double_TestData()) {
            this.Max_Double((IEnumerable<Double>) objects[0], (double) objects[1]);
        }
    }

    private void Max_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.maxDouble());
        assertEquals(expected, source.maxDouble(x -> x));
    }

    @Test
    public void Max_Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDouble(i -> i));
    }

    @Test
    public void Max_Double_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().maxDouble());
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().maxDouble(x -> x));
    }

    @Test
    public void Max_Double_AllNaNWithSelector() {
        assertTrue(Double.isNaN(Linq.repeat(Double.NaN, 5).maxDouble(i -> i)));
    }

    @Test
    public void Max_Double_SeveralNaNOrNullWithSelector() {
        Double[] source = new Double[]{Double.NaN, null, Double.NaN, null};
        assertTrue(Double.isNaN(Linq.asEnumerable(source).maxDouble(i -> i)));
    }

    @Test
    public void Max_Double_NaNThenNegativeInfinityWithSelector() {
        double[] source = {Double.NaN, Double.NEGATIVE_INFINITY};
        assertEquals(Linq.asEnumerable(source).maxDouble(i -> i), Double.NEGATIVE_INFINITY);
    }

    private IEnumerable<Object[]> Max_Decimal_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(m(42), 1), m(42)});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> m(i)).toArray(), m(10)});
        lst.add(new Object[]{Linq.asEnumerable(m(-100), m(-15), m(-50), m(-10)), m(-10)});
        lst.add(new Object[]{Linq.asEnumerable(m(-16), m(0), m(50), m(100), m(1000)), m(1000)});
        lst.add(new Object[]{Linq.asEnumerable(m(-16), m(0), m(50), m(100), m(1000)).concat(Linq.repeat(MAX_DECIMAL, 1)), MAX_DECIMAL});

        lst.add(new Object[]{Linq.asEnumerable(m("5.5")), m("5.5")});
        lst.add(new Object[]{Linq.repeat(m("-3.4"), 5), m("-3.4")});
        lst.add(new Object[]{Linq.asEnumerable(m("122.5"), m("4.9"), m("10"), m("4.7"), m("28")), m("122.5")});
        lst.add(new Object[]{Linq.asEnumerable(m("6.8"), m("9.4"), m("10"), m("0"), m("0"), MAX_DECIMAL), MAX_DECIMAL});
        lst.add(new Object[]{Linq.asEnumerable(m("-5.5"), m("0"), m("9.9"), m("-5.5"), m("9.9")), m("9.9")});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_Decimal() {
        for (Object[] objects : this.Max_Decimal_TestData()) {
            this.Max_Decimal((IEnumerable<BigDecimal>) objects[0], (BigDecimal) objects[1]);
        }
    }

    private void Max_Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.maxDecimal());
        assertEquals(expected, source.maxDecimal(x -> x));
    }

    @Test
    public void Max_Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimal(i -> i));
    }

    @Test
    public void Max_Decimal_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().maxDecimal());
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().maxDecimal(x -> x));
    }

    private IEnumerable<Object[]> Max_NullableInt_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42, 1), 42});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> i).toArray(), 10});
        lst.add(new Object[]{Linq.asEnumerable(null, -100, -15, -50, -10), -10});
        lst.add(new Object[]{Linq.asEnumerable(null, -16, 0, 50, 100, 1000), 1000});
        lst.add(new Object[]{Linq.asEnumerable(null, -16, 0, 50, 100, 1000).concat(Linq.repeat(Integer.MAX_VALUE, 1)), Integer.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});

        lst.add(new Object[]{Linq.<Integer>empty(), null});
        lst.add(new Object[]{Linq.repeat(-20, 1), -20});
        lst.add(new Object[]{Linq.asEnumerable(-6, null, -9, -10, null, -17, -18), -6});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, -5), -5});
        lst.add(new Object[]{Linq.asEnumerable(6, null, null, 100, 9, 100, 10, 100), 100});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_NullableInt() {
        for (Object[] objects : this.Max_NullableInt_TestData()) {
            this.Max_NullableInt((IEnumerable<Integer>) objects[0], (Integer) objects[1]);
        }
    }

    private void Max_NullableInt(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.maxIntNull());
        assertEquals(expected, source.maxIntNull(x -> x));
    }

    @Test
    public void Max_NullableIntRunOnce() {
        for (Object[] objects : this.Max_NullableInt_TestData()) {
            this.Max_NullableIntRunOnce((IEnumerable<Integer>) objects[0], (Integer) objects[1]);
        }
    }

    private void Max_NullableIntRunOnce(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.runOnce().maxIntNull());
        assertEquals(expected, source.runOnce().maxIntNull(x -> x));
    }

    @Test
    public void Max_NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).maxIntNull(i -> i));
    }

    private IEnumerable<Object[]> Max_NullableLong_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42L, 1), 42L});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (long) i).toArray(), 10L});
        lst.add(new Object[]{Linq.asEnumerable(null, -100L, -15L, -50L, -10L), -10L});
        lst.add(new Object[]{Linq.asEnumerable(null, -16L, 0L, 50L, 100L, 1000L), 1000L});
        lst.add(new Object[]{Linq.asEnumerable(null, -16L, 0L, 50L, 100L, 1000L).concat(Linq.repeat(Long.MAX_VALUE, 1)), Long.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});

        lst.add(new Object[]{Linq.<Long>empty(), null});
        lst.add(new Object[]{Linq.repeat(Long.MAX_VALUE, 1), Long.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(Long.MAX_VALUE, null, 9L, 10L, null, 7L, 8L), Long.MAX_VALUE});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, -Long.MAX_VALUE), -Long.MAX_VALUE});
        lst.add(new Object[]{Linq.asEnumerable(-6L, null, null, 0L, -9L, 0L, -10L, -30L), 0L});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_NullableLong() {
        for (Object[] objects : this.Max_NullableLong_TestData()) {
            this.Max_NullableLong((IEnumerable<Long>) objects[0], (Long) objects[1]);
        }
    }

    private void Max_NullableLong(IEnumerable<Long> source, Long expected) {
        assertEquals(expected, source.maxLongNull());
        assertEquals(expected, source.maxLongNull(x -> x));
    }

    @Test
    public void Max_NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).maxLongNull(i -> i));
    }

    private IEnumerable<Object[]> Max_NullableFloat_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42f, 1), 42f});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).toArray(), 10f});
        lst.add(new Object[]{Linq.asEnumerable(null, -100f, -15f, -50f, -10f), -10f});
        lst.add(new Object[]{Linq.asEnumerable(null, -16f, 0f, 50f, 100f, 1000f), 1000f});
        lst.add(new Object[]{Linq.asEnumerable(null, -16f, 0f, 50f, 100f, 1000f).concat(Linq.repeat(Float.MAX_VALUE, 1)), Float.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});

        lst.add(new Object[]{Linq.<Float>empty(), null});
        lst.add(new Object[]{Linq.repeat(Float.MIN_VALUE, 1), Float.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(14.50f, null, Float.NaN, 10.98f, null, 7.5f, 8.6f), 14.50f});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, 0f), 0f});
        lst.add(new Object[]{Linq.asEnumerable(-6.4f, null, null, -0.5f, -9.4f, -0.5f, -10.9f, -0.5f), -0.5f});

        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, 6.8f, 9.4f, 10f, 0f, null, -5.6f), 10f});
        lst.add(new Object[]{Linq.asEnumerable(6.8f, 9.4f, 10f, 0f, null, -5.6f, Float.NaN), 10f});
        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, Float.NEGATIVE_INFINITY), Float.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.asEnumerable(Float.NEGATIVE_INFINITY, Float.NaN), Float.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.repeat(Float.NaN, 3), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, null, null, null), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, Float.NaN), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, Float.NaN, null), Float.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_NullableFloat() {
        for (Object[] objects : this.Max_NullableFloat_TestData()) {
            this.Max_NullableFloat((IEnumerable<Float>) objects[0], (Float) objects[1]);
        }
    }

    private void Max_NullableFloat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.maxFloatNull());
        assertEquals(expected, source.maxFloatNull(x -> x));
    }

    @Test
    public void Max_NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).maxFloatNull(i -> i));
    }

    private IEnumerable<Object[]> Max_NullableDouble_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42d, 1), 42.0});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).toArray(), 10.0});
        lst.add(new Object[]{Linq.asEnumerable(null, -100d, -15d, -50d, -10d), -10.0});
        lst.add(new Object[]{Linq.asEnumerable(null, -16d, 0d, 50d, 100d, 1000d), 1000.0});
        lst.add(new Object[]{Linq.asEnumerable(null, -16d, 0d, 50d, 100d, 1000d).concat(Linq.repeat(Double.MAX_VALUE, 1)), Double.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});

        lst.add(new Object[]{Linq.<Double>empty(), null});
        lst.add(new Object[]{Linq.repeat(Double.MIN_VALUE, 1), Double.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(14.50, null, Double.NaN, 10.98, null, 7.5, 8.6), 14.50});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, 0d), 0.0});
        lst.add(new Object[]{Linq.asEnumerable(-6.4, null, null, -0.5, -9.4, -0.5, -10.9, -0.5), -0.5});

        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, 6.8, 9.4, 10.5, 0d, null, -5.6), 10.5});
        lst.add(new Object[]{Linq.asEnumerable(6.8, 9.4, 10.8, 0d, null, -5.6, Double.NaN), 10.8});
        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, Double.NEGATIVE_INFINITY), Double.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.asEnumerable(Double.NEGATIVE_INFINITY, Double.NaN), Double.NEGATIVE_INFINITY});
        lst.add(new Object[]{Linq.repeat(Double.NaN, 3), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, null, null, null), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, Double.NaN), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, Double.NaN, null), Double.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_NullableDouble() {
        for (Object[] objects : this.Max_NullableDouble_TestData()) {
            this.Max_NullableDouble((IEnumerable<Double>) objects[0], (Double) objects[1]);
        }
    }

    private void Max_NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.maxDoubleNull());
        assertEquals(expected, source.maxDoubleNull(x -> x));
    }

    @Test
    public void Max_NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDoubleNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).maxDoubleNull(i -> i));
    }

    private IEnumerable<Object[]> Max_NullableDecimal_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(m(42), 1), m(42)});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> m(i)).toArray(), m(10)});
        lst.add(new Object[]{Linq.asEnumerable(null, m(-100), m(-15), m(-50), m(-10)), m(-10)});
        lst.add(new Object[]{Linq.asEnumerable(null, m(-16), m(0), m(50), m(100), m(1000)), m(1000)});
        lst.add(new Object[]{Linq.asEnumerable(null, m(-16), m(0), m(50), m(100), m(1000)).concat(Linq.repeat(MAX_DECIMAL, 1)), MAX_DECIMAL});
        lst.add(new Object[]{Linq.repeat(null, 100), null});

        lst.add(new Object[]{Linq.<BigDecimal>empty(), null});
        lst.add(new Object[]{Linq.repeat(MAX_DECIMAL, 1), MAX_DECIMAL});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(m("14.50"), null, null, m("10.98"), null, m("7.5"), m("8.6")), m("14.50")});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, m(0)), m(0)});
        lst.add(new Object[]{Linq.asEnumerable(m("6.4"), null, null, MAX_DECIMAL, m("9.4"), MAX_DECIMAL, m("10.9"), MAX_DECIMAL), MAX_DECIMAL});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_NullableDecimal() {
        for (Object[] objects : this.Max_NullableDecimal_TestData()) {
            this.Max_NullableDecimal((IEnumerable<BigDecimal>) objects[0], (BigDecimal) objects[1]);
        }
    }

    private void Max_NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.maxDecimalNull());
        assertEquals(expected, source.maxDecimalNull(x -> x));
    }

    @Test
    public void Max_NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).maxDecimalNull(i -> i));
    }

    private IEnumerable<Object[]> Max_DateTime_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> newDate(2000, 1, i)).toArray(), newDate(2000, 1, 10)});
        lst.add(new Object[]{Linq.asEnumerable(newDate(2000, 12, 1), newDate(2000, 12, 31), newDate(2000, 1, 12)), newDate(2000, 12, 31)});

        Date[] threeThousand = new Date[]
                {
                        newDate(3000, 1, 1),
                        newDate(100, 1, 1),
                        newDate(200, 1, 1),
                        newDate(1000, 1, 1)
                };
        lst.add(new Object[]{Linq.asEnumerable(threeThousand), newDate(3000, 1, 1)});
        lst.add(new Object[]{Linq.asEnumerable(threeThousand).concat(Linq.repeat(MAX_DATE, 1)), MAX_DATE});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_DateTime() {
        for (Object[] objects : this.Max_DateTime_TestData()) {
            this.Max_DateTime((IEnumerable<Date>) objects[0], (Date) objects[1]);
        }
    }

    private void Max_DateTime(IEnumerable<Date> source, Date expected) {
        assertEquals(expected, source.max());
        assertEquals(expected, source.max(x -> x));
    }

    @Test
    public void Max_DateTime_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).max());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).max(i -> i));
    }

    @Test
    public void Max_DateTime_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().max());
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().max(i -> i));
    }

    private IEnumerable<Object[]> Max_String_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> i.toString()).toArray(), "9"});
        lst.add(new Object[]{Linq.asEnumerable("Alice", "Bob", "Charlie", "Eve", "Mallory", "Victor", "Trent"), "Victor"});
        lst.add(new Object[]{Linq.asEnumerable(null, "Charlie", null, "Victor", "Trent", null, "Eve", "Alice", "Mallory", "Bob"), "Victor"});

        lst.add(new Object[]{Linq.<String>empty(), null});
        lst.add(new Object[]{Linq.repeat("Hello", 1), "Hello"});
        lst.add(new Object[]{Linq.repeat("hi", 5), "hi"});
        lst.add(new Object[]{Linq.asEnumerable("zzz", "aaa", "abcd", "bark", "temp", "cat"), "zzz"});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, "aAa"), "aAa"});
        lst.add(new Object[]{Linq.asEnumerable("ooo", "ccc", "ccc", "ooo", "ooo", "nnn"), "ooo"});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Max_String() {
        for (Object[] objects : this.Max_String_TestData()) {
            this.Max_String((IEnumerable<String>) objects[0], (String) objects[1]);
        }
    }

    private void Max_String(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.maxNull());
        assertEquals(expected, source.maxNull(x -> x));
    }

    @Test
    public void Max_StringRunOnce() {
        for (Object[] objects : this.Max_String_TestData()) {
            this.Max_StringRunOnce((IEnumerable<String>) objects[0], (String) objects[1]);
        }
    }

    private void Max_StringRunOnce(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.runOnce().maxNull());
        assertEquals(expected, source.runOnce().maxNull(x -> x));
    }

    @Test
    public void Max_String_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).maxNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).maxNull(i -> i));
    }

    @Test
    public void Max_Int_NullSelector_ThrowsArgumentNullException() {
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().maxInt(selector));
    }

    @Test
    public void Max_Int_WithSelectorAccessingProperty() {
        Obj<Integer>[] source = new Obj[]
                {
                        new Obj<>("Tim", 10),
                        new Obj<>("John", -105),
                        new Obj<>("Bob", 30)
                };

        assertEquals(30, Linq.asEnumerable(source).maxInt(e -> e.num));
    }

    @Test
    public void Max_Long_NullSelector_ThrowsArgumentNullException() {
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().maxLong(selector));
    }

    @Test
    public void Max_Long_WithSelectorAccessingProperty() {
        Obj<Long>[] source = new Obj[]
                {
                        new Obj<>("Tim", 10L),
                        new Obj<>("John", -105L),
                        new Obj<>("Bob", Long.MAX_VALUE)
                };
        assertEquals(Long.MAX_VALUE, Linq.asEnumerable(source).maxLong(e -> e.num));
    }

    @Test
    public void Max_Float_WithSelectorAccessingProperty() {
        Obj<Float>[] source = new Obj[]
                {
                        new Obj<>("Tim", 40.5f),
                        new Obj<>("John", -10.25f),
                        new Obj<>("Bob", 100.45f)
                };

        assertEquals(100.45f, Linq.asEnumerable(source).select(e -> e.num).maxFloat());
    }

    @Test
    public void Max_Float_NullSelector_ThrowsArgumentNullException() {
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().maxFloat(selector));
    }

    @Test
    public void Max_Double_NullSelector_ThrowsArgumentNullException() {
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().maxDouble(selector));
    }

    @Test
    public void Max_Double_WithSelectorAccessingField() {
        Obj<Double>[] source = new Obj[]
                {
                        new Obj<>("Tim", 40.5d),
                        new Obj<>("John", -10.25d),
                        new Obj<>("Bob", 100.45d)
                };
        assertEquals(100.45, Linq.asEnumerable(source).maxDouble(e -> e.num));
    }

    @Test
    public void Max_Decimal_NullSelector_ThrowsArgumentNullException() {
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().maxDecimal(selector));
    }

    @Test
    public void Max_Decimal_WithSelectorAccessingProperty() {
        Obj<BigDecimal>[] source = new Obj[]{
                new Obj<>("Tim", m("420.5")),
                new Obj<>("John", m("900.25")),
                new Obj<>("Bob", m("10.45"))
        };
        assertEquals(m("900.25"), Linq.asEnumerable(source).maxDecimal(e -> e.num));
    }

    @Test
    public void Max_NullableInt_NullSelector_ThrowsArgumentNullException() {
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().maxIntNull(selector));
    }

    @Test
    public void Max_NullableInt_WithSelectorAccessingField() {
        Obj<Integer>[] source = new Obj[]{
                new Obj<>("Tim", 10),
                new Obj<>("John", -105),
                new Obj<>("Bob", null)
        };

        assertEquals(10, Linq.asEnumerable(source).maxIntNull(e -> e.num));
    }

    @Test
    public void Max_NullableLong_NullSelector_ThrowsArgumentNullException() {
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().maxLongNull(selector));
    }

    @Test
    public void Max_NullableLong_WithSelectorAccessingField() {
        Obj<Long>[] source = new Obj[]
                {
                        new Obj<>("Tim", null),
                        new Obj<>("John", -105L),
                        new Obj<>("Bob", Long.MAX_VALUE)
                };
        assertEquals(Long.MAX_VALUE, Linq.asEnumerable(source).maxLongNull(e -> e.num));
    }

    @Test
    public void Max_NullableFloat_NullSelector_ThrowsArgumentNullException() {
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().maxFloatNull(selector));
    }

    @Test
    public void Max_NullableFloat_WithSelectorAccessingProperty() {
        Obj<Float>[] source = new Obj[]
                {
                        new Obj<>("Tim", 40.5f),
                        new Obj<>("John", null),
                        new Obj<>("Bob", 100.45f)
                };
        assertEquals(100.45f, Linq.asEnumerable(source).maxFloatNull(e -> e.num));
    }

    @Test
    public void Max_NullableDouble_NullSelector_ThrowsArgumentNullException() {
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().maxDoubleNull(selector));
    }

    @Test
    public void Max_NullableDouble_WithSelectorAccessingProperty() {
        Obj<Double>[] source = new Obj[]
                {
                        new Obj<>("Tim", 40.5),
                        new Obj<>("John", null),
                        new Obj<>("Bob", 100.45)
                };
        assertEquals(100.45, Linq.asEnumerable(source).maxDoubleNull(e -> e.num));
    }

    @Test
    public void Max_NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().maxDecimalNull(selector));
    }

    @Test
    public void Max_NullableDecimal_WithSelectorAccessingProperty() {
        Obj<BigDecimal>[] source = new Obj[]
                {
                        new Obj<>("Tim", m("420.5")),
                        new Obj<>("John", null),
                        new Obj<>("Bob", m("10.45"))
                };
        assertEquals(m("420.5"), Linq.asEnumerable(source).maxDecimalNull(e -> e.num));
    }

    @Test
    public void Max_NullableDateTime_EmptySourceWithSelector() {
        assertNull(Linq.<Date>empty().maxNull(x -> x));
    }

    @Test
    public void Max_NullableDateTime_NullSelector_ThrowsArgumentNullException() {
        Func1<Date, Date> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().maxNull(selector));
    }

    @Test
    public void Max_String_NullSelector_ThrowsArgumentNullException() {
        Func1<String, String> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<String>empty().maxNull(selector));
    }

    @Test
    public void Max_String_WithSelectorAccessingProperty() {
        Obj<BigDecimal>[] source = new Obj[]
                {
                        new Obj<>("Tim", m("420.5")),
                        new Obj<>("John", m("900.25")),
                        new Obj<>("Bob", m("10.45"))
                };
        assertEquals("Tim", Linq.asEnumerable(source).maxNull(e -> e.name));
    }

    @Test
    public void Max_Boolean_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Boolean>empty().max());
    }

    @Test
    public void testMaxInt() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.asEnumerable(numbers).maxInt());

        Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNull() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.asEnumerable(numbers).maxIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull());
    }

    @Test
    public void testMaxLong() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.asEnumerable(numbers).maxLong());

        Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNull() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.asEnumerable(numbers).maxLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull());
    }

    @Test
    public void testMaxFloat() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.asEnumerable(numbers).maxFloat());

        Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull());
    }

    @Test
    public void testMaxDouble() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.asEnumerable(numbers).maxDouble());

        Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNull() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull());
    }

    @Test
    public void testMaxDecimal() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal());

        Integer[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNull() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull());
    }

    @Test
    public void testMax() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).max());

        Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max();
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).maxNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxNull());
    }

    @Test
    public void testMaxIntWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.asEnumerable(numbers).maxInt(n -> n));

        Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNullWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(3, Linq.asEnumerable(numbers).maxIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull(n -> n));
    }

    @Test
    public void testMaxLongWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.asEnumerable(numbers).maxLong(n -> n));

        Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNullWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(3L, Linq.asEnumerable(numbers).maxLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull(n -> n));
    }

    @Test
    public void testMaxFloatWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.asEnumerable(numbers).maxFloat(n -> n));

        Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull(n -> n));
    }

    @Test
    public void testMaxDoubleWithSelector() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.asEnumerable(numbers).maxDouble(n -> n));

        Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNullWithSelector() {
        Double[] numbers = {null, 2d, Double.NaN};
        assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull(n -> n));
    }

    @Test
    public void testMaxDecimalWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull(n -> n));
    }

    @Test
    public void testMaxWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).max(n -> n);
        assertEquals(Float.NaN, f);

        Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max(n -> n);
            fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).maxNull(n -> n);
        assertEquals(Float.NaN, f);

        Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).maxNull(n -> n);
        assertEquals(null, f2);
    }

    private static class Obj<T> extends ValueType {
        final String name;
        final T num;

        Obj(String name, T num) {
            this.name = name;
            this.num = num;
        }
    }
}
