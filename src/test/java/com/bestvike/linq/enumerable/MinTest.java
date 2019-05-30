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
public class MinTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.min(), q.min());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.min(), q.min());
    }

    private IEnumerable<Object[]> Min_Int_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42, 1), 42});
        lst.add(new Object[]{Linq.range(1, 10).toArray(), 1});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-1, -10, 10, 200, 1000}), -10});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{3000, 100, 200, 1000}), 100});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{3000, 100, 200, 1000}).concat(Linq.repeat(Integer.MIN_VALUE, 1)), Integer.MIN_VALUE});

        lst.add(new Object[]{Linq.repeat(20, 1), 20});
        lst.add(new Object[]{Linq.repeat(-2, 5), -2});
        lst.add(new Object[]{Linq.range(1, 10).toArray(), 1});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{6, 9, 10, 7, 8}), 6});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{6, 9, 10, 0, -5}), -5});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{6, 0, 9, 0, 10, 0}), 0});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_Int() {
        for (Object[] objects : this.Min_Int_TestData()) {
            this.Min_Int((IEnumerable<Integer>) objects[0], (int) objects[1]);
        }
    }

    private void Min_Int(IEnumerable<Integer> source, int expected) {
        assertEquals(expected, source.minInt());
        assertEquals(expected, source.minInt(x -> x));
    }

    @Test
    public void Min_Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minInt(x -> x));
    }

    @Test
    public void Min_Int_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minInt());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minInt(x -> x));
    }

    private IEnumerable<Object[]> Min_Long_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42L, 1), 42L});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (long) i).toArray(), 1L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{-1, -10, 10, 200, 1000}), -10L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{3000, 100, 200, 1000}), 100L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{3000, 100, 200, 1000}).concat(Linq.repeat(Long.MIN_VALUE, 1)), Long.MIN_VALUE});

        lst.add(new Object[]{Linq.repeat(Integer.MAX_VALUE + 10L, 1), Integer.MAX_VALUE + 10L});
        lst.add(new Object[]{Linq.repeat(500L, 5), 500L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{-250, 49, 130, 47, 28}), -250L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{6, 9, 10, 0, -Integer.MAX_VALUE - 50L}), -Integer.MAX_VALUE - 50L});
        lst.add(new Object[]{Linq.asEnumerable(new long[]{6, -5, 9, -5, 10, -5}), -5L});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_Long() {
        for (Object[] objects : this.Min_Long_TestData()) {
            this.Min_Long((IEnumerable<Long>) objects[0], (long) objects[1]);
        }
    }

    private void Min_Long(IEnumerable<Long> source, long expected) {
        assertEquals(expected, source.minLong());
        assertEquals(expected, source.minLong(x -> x));
    }

    @Test
    public void Min_Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLong(x -> x));
    }

    @Test
    public void Min_Long_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().minLong());
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().minLong(x -> x));
    }

    private IEnumerable<Object[]> Min_Float_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42f, 1), 42f});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).toArray(), 1f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-1, -10, 10, 200, 1000}), -10f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{3000, 100, 200, 1000}), 100f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{3000, 100, 200, 1000}).concat(Linq.repeat(Float.MIN_VALUE, 1)), Float.MIN_VALUE});

        lst.add(new Object[]{Linq.repeat(5.5f, 1), 5.5f});
        lst.add(new Object[]{Linq.repeat(Float.NaN, 5), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-2.5f, 4.9f, 130f, 4.7f, 28f}), -2.5f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{6.8f, 9.4f, 10f, 0, -5.6f}), -5.6f});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-5.5f, Float.NEGATIVE_INFINITY, 9.9f, Float.NEGATIVE_INFINITY}), Float.NEGATIVE_INFINITY});

        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f}), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{6.8f, 9.4f, 10f, 0, -5.6f, Float.NaN}), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NaN, Float.NEGATIVE_INFINITY}), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.NEGATIVE_INFINITY, Float.NaN}), Float.NaN});

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Float.NaN in the array,
        // as nothing can be less than Float.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        lst.add(new Object[]{Linq.repeat(Float.NaN, Integer.MAX_VALUE), Float.NaN});
        lst.add(new Object[]{Linq.repeat(Float.NaN, 3), Float.NaN});

        // Normally NaN < anything is false, as is anything < NaN
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).concat(Linq.repeat(Float.NaN, 1)).toArray(), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{-1F, -10, Float.NaN, 10, 200, 1000}), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new float[]{Float.MIN_VALUE, 3000F, 100, 200, Float.NaN, 1000}), Float.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_Float() {
        for (Object[] objects : this.Min_Float_TestData()) {
            this.Min_Float((IEnumerable<Float>) objects[0], (float) objects[1]);
        }
    }

    private void Min_Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.minFloat());
        assertEquals(expected, source.minFloat(x -> x));
    }

    @Test
    public void Min_Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloat(x -> x));
    }

    @Test
    public void Min_Float_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().minFloat());
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().minFloat(x -> x));
    }

    private IEnumerable<Object[]> Min_Double_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(42.0, 1), 42.0});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).toArray(), 1.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-1, -10, 10, 200, 1000}), -10.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{3000, 100, 200, 1000}), 100.0});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{3000, 100, 200, 1000}).concat(Linq.repeat(Double.MIN_VALUE, 1)), Double.MIN_VALUE});

        lst.add(new Object[]{Linq.repeat(5.5, 1), 5.5});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-2.5, 4.9, 130, 4.7, 28}), -2.5});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{6.8, 9.4, 10, 0, -5.6}), -5.6});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-5.5, Double.NEGATIVE_INFINITY, 9.9, Double.NEGATIVE_INFINITY}), Double.NEGATIVE_INFINITY});

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Double.NaN in the array,
        // as nothing can be less than Double.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        lst.add(new Object[]{Linq.repeat(Double.NaN, Integer.MAX_VALUE), Double.NaN});
        lst.add(new Object[]{Linq.repeat(Double.NaN, 3), Double.NaN});

        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NaN, 6.8, 9.4, 10, 0, -5.6}), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{6.8, 9.4, 10, 0, -5.6, Double.NaN}), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NaN, Double.NEGATIVE_INFINITY}), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.NEGATIVE_INFINITY, Double.NaN}), Double.NaN});

        // Normally NaN < anything is false, as is anything < NaN
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).concat(Linq.repeat(Double.NaN, 1)).toArray(), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{-1, -10, Double.NaN, 10, 200, 1000}), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(new double[]{Double.MIN_VALUE, 3000F, 100, 200, Double.NaN, 1000}), Double.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_Double() {
        for (Object[] objects : this.Min_Double_TestData()) {
            this.Min_Double((IEnumerable<Double>) objects[0], (double) objects[1]);
        }
    }

    private void Min_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.minDouble());
        assertEquals(expected, source.minDouble(x -> x));
    }

    @Test
    public void Min_Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDouble(x -> x));
    }

    @Test
    public void Min_Double_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().minDouble());
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().minDouble(x -> x));
    }

    private IEnumerable<Object[]> Min_Decimal_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.repeat(m("42"), 1), m("42")});
        lst.add(new Object[]{Linq.range(1, 10).select(i -> m(i)).toArray(), m("1")});
        lst.add(new Object[]{Linq.asEnumerable(m(-1), m(-10), m(10), m(200), m(1000)), m("-10")});
        lst.add(new Object[]{Linq.asEnumerable(m(3000), m(100), m(200), m(1000)), m("100")});
        lst.add(new Object[]{Linq.asEnumerable(m(3000), m(100), m(200), m(1000)).concat(Linq.repeat(MIN_DECIMAL, 1)), MIN_DECIMAL});

        lst.add(new Object[]{Linq.repeat(m("5.5"), 1), m("5.5")});
        lst.add(new Object[]{Linq.repeat(m("-3.4"), 5), m("-3.4")});
        lst.add(new Object[]{Linq.asEnumerable(m("-2.5"), m("4.9"), m("130"), m("4.7"), m("28")), m("-2.5")});
        lst.add(new Object[]{Linq.asEnumerable(m("6.8"), m("9.4"), m("10"), m("0"), m("0"), MIN_DECIMAL), MIN_DECIMAL});
        lst.add(new Object[]{Linq.asEnumerable(m("-5.5"), m("0"), m("9.9"), m("-5.5"), m("5")), m("-5.5")});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_Decimal() {
        for (Object[] objects : this.Min_Decimal_TestData()) {
            this.Min_Decimal((IEnumerable<BigDecimal>) objects[0], (BigDecimal) objects[1]);
        }
    }

    private void Min_Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.minDecimal());
        assertEquals(expected, source.minDecimal(x -> x));
    }

    @Test
    public void Min_Decimal_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().minDecimal());
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().minDecimal(x -> x));
    }

    @Test
    public void Min_Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimal(x -> x));
    }

    private IEnumerable<Object[]> Min_NullableInt_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> i).toArray(), 1});
        lst.add(new Object[]{Linq.asEnumerable(null, -1, -10, 10, 200, 1000), -10});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000, 100, 200, 1000), 100});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000, 100, 200, 1000).concat(Linq.repeat(Integer.MIN_VALUE, 1)), Integer.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.repeat(42, 1), 42});

        lst.add(new Object[]{Linq.<Integer>empty(), null});
        lst.add(new Object[]{Linq.repeat(20, 1), 20});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(6, null, 9, 10, null, 7, 8), 6});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, -5), -5});
        lst.add(new Object[]{Linq.asEnumerable(6, null, null, 0, 9, 0, 10, 0), 0});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_NullableInt() {
        for (Object[] objects : this.Min_NullableInt_TestData()) {
            this.Min_NullableInt((IEnumerable<Integer>) objects[0], (Integer) objects[1]);
        }
    }

    private void Min_NullableInt(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.minIntNull());
    }

    @Test
    public void Min_NullableIntRunOnce() {
        for (Object[] objects : this.Min_NullableInt_TestData()) {
            this.Min_NullableIntRunOnce((IEnumerable<Integer>) objects[0], (Integer) objects[1]);
        }
    }

    private void Min_NullableIntRunOnce(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.runOnce().minIntNull());
    }

    @Test
    public void Min_NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minIntNull(x -> x));
    }

    private IEnumerable<Object[]> Min_NullableLong_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (long) i).toArray(), 1L});
        lst.add(new Object[]{Linq.asEnumerable(null, -1L, -10L, 10L, 200L, 1000L), -10L});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000L, 100L, 200L, 1000L), 100L});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000L, 100L, 200L, 1000L).concat(Linq.repeat(Long.MIN_VALUE, 1)), Long.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.repeat(42L, 1), 42L});

        lst.add(new Object[]{Linq.<Long>empty(), null});
        lst.add(new Object[]{Linq.repeat(Long.MAX_VALUE, 1), Long.MAX_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(Long.MIN_VALUE, null, 9L, 10L, null, 7L, 8L), Long.MIN_VALUE});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, -Long.MAX_VALUE), -Long.MAX_VALUE});
        lst.add(new Object[]{Linq.asEnumerable(6L, null, null, 0L, 9L, 0L, 10L, 0L), 0L});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_NullableLong() {
        for (Object[] objects : this.Min_NullableLong_TestData()) {
            this.Min_NullableLong((IEnumerable<Long>) objects[0], (Long) objects[1]);
        }
    }

    private void Min_NullableLong(IEnumerable<Long> source, Long expected) {
        assertEquals(expected, source.minLongNull());
    }

    @Test
    public void Min_NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLongNull(x -> x));
    }

    private IEnumerable<Object[]> Min_NullableFloat_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (float) i).toArray(), 1f});
        lst.add(new Object[]{Linq.asEnumerable(null, -1f, -10f, 10f, 200f, 1000f), -10f});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000f, 100f, 200f, 1000f), 100f});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000f, 100f, 200f, 1000f).concat(Linq.repeat(Float.MIN_VALUE, 1)), Float.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.repeat(42f, 1), 42f});

        lst.add(new Object[]{Linq.<Float>empty(), null});
        lst.add(new Object[]{Linq.repeat(Float.MIN_VALUE, 1), Float.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.asEnumerable(-4.50f, null, 10.98f, null, 7.5f, 8.6f), -4.5f});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, 0f), 0f});
        lst.add(new Object[]{Linq.asEnumerable(6.4f, null, null, -0.5f, 9.4f, -0.5f, 10.9f, -0.5f), -0.5f});

        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, 6.8f, 9.4f, 10f, 0f, null, -5.6f), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(6.8f, 9.4f, 10f, 0f, null, -5.6f, Float.NaN), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, Float.NEGATIVE_INFINITY), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Float.NEGATIVE_INFINITY, Float.NaN), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Float.NaN, null, null, null), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, Float.NaN), Float.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, Float.NaN, null), Float.NaN});

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Float.NaN in the array,
        // as nothing can be less than Float.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time. 
        lst.add(new Object[]{Linq.repeat(Float.NaN, Integer.MAX_VALUE), Float.NaN});
        lst.add(new Object[]{Linq.repeat(Float.NaN, 3), Float.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_NullableFloat() {
        for (Object[] objects : this.Min_NullableFloat_TestData()) {
            this.Min_NullableFloat((IEnumerable<Float>) objects[0], (Float) objects[1]);
        }
    }

    private void Min_NullableFloat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.minFloatNull());
        assertEquals(expected, source.minFloatNull(x -> x));
    }

    @Test
    public void Min_NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloatNull(x -> x));
    }

    private IEnumerable<Object[]> Min_NullableDouble_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> (double) i).toArray(), 1.0});
        lst.add(new Object[]{Linq.asEnumerable(null, -1d, -10d, 10d, 200d, 1000d), -10.0});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000d, 100d, 200d, 1000d), 100.0});
        lst.add(new Object[]{Linq.asEnumerable(null, 3000d, 100d, 200d, 1000d).concat(Linq.repeat(Double.MIN_VALUE, 1)), Double.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.repeat(42d, 1), 42.0});

        lst.add(new Object[]{Linq.<Double>empty(), null});
        lst.add(new Object[]{Linq.repeat(Double.MIN_VALUE, 1), Double.MIN_VALUE});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(-4.50, null, 10.98, null, 7.5, 8.6), -4.5});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, 0d), 0.0});
        lst.add(new Object[]{Linq.asEnumerable(6.4, null, null, -0.5, 9.4, -0.5, 10.9, -0.5), -0.5});

        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, 6.8, 9.4, 10.0, 0.0, null, -5.6), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(6.8, 9.4, 10d, 0.0, null, -5.6d, Double.NaN), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, Double.NEGATIVE_INFINITY), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Double.NEGATIVE_INFINITY, Double.NaN), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(Double.NaN, null, null, null), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, Double.NaN), Double.NaN});
        lst.add(new Object[]{Linq.asEnumerable(null, Double.NaN, null), Double.NaN});

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Double.NaN in the array,
        // as nothing can be less than Double.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        lst.add(new Object[]{Linq.repeat(Double.NaN, Integer.MAX_VALUE), Double.NaN});
        lst.add(new Object[]{Linq.repeat(Double.NaN, 3), Double.NaN});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_NullableDouble() {
        for (Object[] objects : this.Min_NullableDouble_TestData()) {
            this.Min_NullableDouble((IEnumerable<Double>) objects[0], (Double) objects[1]);
        }
    }

    private void Min_NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.minDoubleNull());
        assertEquals(expected, source.minDoubleNull(x -> x));
    }

    @Test
    public void Min_NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDoubleNull());
    }

    private IEnumerable<Object[]> Min_NullableDecimal_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> m(i)).toArray(), m("1")});
        lst.add(new Object[]{Linq.asEnumerable(null, m(-1), m(-10), m(10), m(200), m(1000)), m("-10")});
        lst.add(new Object[]{Linq.asEnumerable(null, m(3000), m(100), m(200), m(1000)), m("100")});
        lst.add(new Object[]{Linq.asEnumerable(null, m(3000), m(100), m(200), m(1000)).concat(Linq.repeat(MIN_DECIMAL, 1)), MIN_DECIMAL});
        lst.add(new Object[]{Linq.repeat(null, 100), null});
        lst.add(new Object[]{Linq.repeat(m("42"), 1), m("42")});

        lst.add(new Object[]{Linq.<BigDecimal>empty(), null});
        lst.add(new Object[]{Linq.repeat(MAX_DECIMAL, 1), MAX_DECIMAL});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        lst.add(new Object[]{Linq.asEnumerable(m("-4.50"), null, null, m("10.98"), null, m("7.5"), m("8.6")), m("-4.5")});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, null, m("0")), m("0")});
        lst.add(new Object[]{Linq.asEnumerable(m("6.4"), null, null, MIN_DECIMAL, m("9.4"), MIN_DECIMAL, m("10.9"), MIN_DECIMAL), MIN_DECIMAL});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_NullableDecimal() {
        for (Object[] objects : this.Min_NullableDecimal_TestData()) {
            this.Min_NullableDecimal((IEnumerable<BigDecimal>) objects[0], (BigDecimal) objects[1]);
        }
    }

    private void Min_NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.minDecimalNull());
        assertEquals(expected, source.minDecimalNull(x -> x));
    }

    @Test
    public void Min_NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimalNull(x -> x));
    }

    private IEnumerable<Object[]> Min_DateTime_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> newDate(2000, 1, i)).toArray(), newDate(2000, 1, 1)});
        lst.add(new Object[]{Linq.asEnumerable(newDate(2000, 12, 1), newDate(2000, 1, 1), newDate(2000, 1, 12)), newDate(2000, 1, 1)});

        Date[] hundred = new Date[]
                {
                        newDate(3000, 1, 1),
                        newDate(100, 1, 1),
                        newDate(200, 1, 1),
                        newDate(1000, 1, 1)
                };
        lst.add(new Object[]{Linq.asEnumerable(hundred), newDate(100, 1, 1)});
        lst.add(new Object[]{Linq.asEnumerable(hundred).concat(Linq.repeat(MIN_DATE, 1)), MIN_DATE});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_DateTime() {
        for (Object[] objects : this.Min_DateTime_TestData()) {
            this.Min_DateTime((IEnumerable<Date>) objects[0], (Date) objects[1]);
        }
    }

    private void Min_DateTime(IEnumerable<Date> source, Date expected) {
        assertEquals(expected, source.min());
        assertEquals(expected, source.min(x -> x));
    }

    @Test
    public void Min_DateTime_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).min());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).min(x -> x));
    }

    @Test
    public void Min_DateTime_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().min());
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().min(x -> x));
    }

    private IEnumerable<Object[]> Min_String_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.range(1, 10).select(i -> i.toString()).toArray(), "1"});
        lst.add(new Object[]{Linq.asEnumerable("Alice", "Bob", "Charlie", "Eve", "Mallory", "Trent", "Victor"), "Alice"});
        lst.add(new Object[]{Linq.asEnumerable(null, "Charlie", null, "Victor", "Trent", null, "Eve", "Alice", "Mallory", "Bob"), "Alice"});

        lst.add(new Object[]{Linq.<String>empty(), null});
        lst.add(new Object[]{Linq.repeat("Hello", 1), "Hello"});
        lst.add(new Object[]{Linq.repeat("hi", 5), "hi"});
        lst.add(new Object[]{Linq.asEnumerable("aaa", "abcd", "bark", "temp", "cat"), "aaa"});
        lst.add(new Object[]{Linq.asEnumerable(null, null, null, null, "aAa"), "aAa"});
        lst.add(new Object[]{Linq.asEnumerable("ooo", "www", "www", "ooo", "ooo", "ppp"), "ooo"});
        lst.add(new Object[]{Linq.repeat(null, 5), null});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Min_String() {
        for (Object[] objects : this.Min_String_TestData()) {
            this.Min_String((IEnumerable<String>) objects[0], (String) objects[1]);
        }
    }

    private void Min_String(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.minNull());
        assertEquals(expected, source.minNull(x -> x));
    }

    @Test
    public void Min_StringRunOnce() {
        for (Object[] objects : this.Min_String_TestData()) {
            this.Min_StringRunOnce((IEnumerable<String>) objects[0], (String) objects[1]);
        }
    }

    private void Min_StringRunOnce(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.runOnce().minNull());
        assertEquals(expected, source.runOnce().minNull(x -> x));
    }

    @Test
    public void Min_String_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).minNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).minNull(x -> x));
    }

    @Test
    public void Min_Int_WithSelectorAccessingProperty() {
        NameNum<Integer>[] source = new NameNum[]
                {
                        new NameNum("Tim", 10),
                        new NameNum("John", -105),
                        new NameNum("Bob", -30)
                };
        assertEquals(-105, Linq.asEnumerable(source).minInt(e -> e.num));
    }

    @Test
    public void Min_Int_NullSelector_ThrowsArgumentNullException() {
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().minInt(selector));
    }

    @Test
    public void Min_Long_WithSelectorAccessingProperty() {
        NameNum<Long>[] source = new NameNum[]
                {
                        new NameNum("Tim", 10L),
                        new NameNum("John", Long.MIN_VALUE),
                        new NameNum("Bob", -10L)
                };

        assertEquals(Long.MIN_VALUE, Linq.asEnumerable(source).minLong(e -> e.num));
    }

    @Test
    public void Min_Long_NullSelector_ThrowsArgumentNullException() {
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().minLong(selector));
    }

    @Test
    public void Min_Float_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]
                {
                        new NameNum("Tim", -45.5f),
                        new NameNum("John", -132.5f),
                        new NameNum("Bob", 20.45f)
                };
        assertEquals(-132.5f, Linq.asEnumerable(source).minFloat(e -> e.num));
    }

    @Test
    public void Min_Float_NullSelector_ThrowsArgumentNullException() {
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().minFloat(selector));
    }

    @Test
    public void Min_Double_WithSelectorAccessingProperty() {
        NameNum<Double>[] source = new NameNum[]
                {
                        new NameNum("Tim", -45.5),
                        new NameNum("John", -132.5),
                        new NameNum("Bob", 20.45)
                };
        assertEquals(-132.5, Linq.asEnumerable(source).minDouble(e -> e.num));
    }

    @Test
    public void Min_Double_NullSelector_ThrowsArgumentNullException() {
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().minDouble(selector));
    }

    @Test
    public void Min_Decimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]
                {
                        new NameNum("Tim", m("100.45")),
                        new NameNum("John", m("10.5")),
                        new NameNum("Bob", m("0.05"))
                };
        assertEquals(m("0.05"), Linq.asEnumerable(source).minDecimal(e -> e.num));
    }

    @Test
    public void Min_Decimal_NullSelector_ThrowsArgumentNullException() {
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().minDecimal(selector));
    }

    @Test
    public void Min_NullableInt_WithSelectorAccessingProperty() {
        NameNum<Integer>[] source = new NameNum[]
                {
                        new NameNum("Tim", 10),
                        new NameNum("John", null),
                        new NameNum("Bob", -30)
                };
        assertEquals(-30, Linq.asEnumerable(source).minIntNull(e -> e.num));
    }

    @Test
    public void Min_NullableInt_NullSelector_ThrowsArgumentNullException() {
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().minIntNull(selector));
    }

    @Test
    public void Min_NullableLong_WithSelectorAccessingProperty() {
        NameNum<Long>[] source = new NameNum[]
                {
                        new NameNum("Tim", null),
                        new NameNum("John", Long.MIN_VALUE),
                        new NameNum("Bob", -10L)
                };
        assertEquals(Long.MIN_VALUE, Linq.asEnumerable(source).minLongNull(e -> e.num));
    }

    @Test
    public void Min_NullableLong_NullSelector_ThrowsArgumentNullException() {
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().minLongNull(selector));
    }

    @Test
    public void Min_NullableFloat_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]
                {
                        new NameNum("Tim", -45.5f),
                        new NameNum("John", -132.5f),
                        new NameNum("Bob", null)
                };

        assertEquals(-132.5f, Linq.asEnumerable(source).minFloatNull(e -> e.num));
    }

    @Test
    public void Min_NullableFloat_NullSelector_ThrowsArgumentNullException() {
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().minFloatNull(selector));
    }

    @Test
    public void Min_NullableDouble_WithSelectorAccessingProperty() {
        NameNum<Double>[] source = new NameNum[]
                {
                        new NameNum("Tim", -45.5d),
                        new NameNum("John", -132.5d),
                        new NameNum("Bob", null)
                };
        assertEquals(-132.5, Linq.asEnumerable(source).minDoubleNull(e -> e.num));
    }

    @Test
    public void Min_NullableDouble_NullSelector_ThrowsArgumentNullException() {
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().minDoubleNull(selector));
    }

    @Test
    public void Min_NullableDecimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]
                {
                        new NameNum("Tim", m("100.45")),
                        new NameNum("John", m("10.5")),
                        new NameNum("Bob", null)
                };
        assertEquals(m("10.5"), Linq.asEnumerable(source).minDecimalNull(e -> e.num));
    }

    @Test
    public void Min_NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().minDecimalNull(selector));
    }

    @Test
    public void Min_DateTime_NullSelector_ThrowsArgumentNullException() {
        Func1<Date, Date> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().min(selector));
    }

    @Test
    public void Min_String_WithSelectorAccessingProperty() {
        NameNum<String>[] source = new NameNum[]
                {
                        new NameNum("Tim", m("100.45")),
                        new NameNum("John", m("10.5")),
                        new NameNum("Bob", m("0.05"))
                };
        assertEquals("Bob", Linq.asEnumerable(source).minNull(e -> e.name));
    }

    @Test
    public void Min_String_NullSelector_ThrowsArgumentNullException() {
        Func1<String, String> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<String>empty().minNull(selector));
    }

    @Test
    public void Min_Bool_EmptySource_ThrowsInvalodOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Boolean>empty().min());
    }

    @Test
    public void testMinInt() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.asEnumerable(numbers).minInt());

        Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNull() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.asEnumerable(numbers).minIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minIntNull());
    }

    @Test
    public void testMinLong() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.asEnumerable(numbers).minLong());

        Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNull() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.asEnumerable(numbers).minLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minLongNull());
    }

    @Test
    public void testMinFloat() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat());

        Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull());
    }

    @Test
    public void testMinDouble() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble());

        Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNull() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull());
    }

    @Test
    public void testMinDecimal() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal());

        Integer[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNull() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull());
    }

    @Test
    public void testMin() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(0f, Linq.asEnumerable(numbers).min());

        Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min();
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(0f, Linq.asEnumerable(numbers).minNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minNull());
    }

    @Test
    public void testMinIntWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.asEnumerable(numbers).minInt(n -> n));

        Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNullWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.asEnumerable(numbers).minIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minIntNull(n -> n));
    }

    @Test
    public void testMinLongWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.asEnumerable(numbers).minLong(n -> n));

        Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNullWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.asEnumerable(numbers).minLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minLongNull(n -> n));
    }

    @Test
    public void testMinFloatWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(n -> n));

        Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull(n -> n));
    }

    @Test
    public void testMinDoubleWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(n -> n));

        Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNullWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull(n -> n));
    }

    @Test
    public void testMinDecimalWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull(n -> n));
    }

    @Test
    public void testMinWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).min(n -> n);
        assertEquals(0f, f);

        Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min(n -> n);
            fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).minNull(n -> n);
        assertEquals(0f, f);

        Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).minNull(n -> n);
        assertEquals(null, f2);
    }

    private static class NameNum<T> extends ValueType {
        final String name;
        final T num;

        NameNum(String name, T num) {
            this.name = name;
            this.num = num;
        }
    }
}
