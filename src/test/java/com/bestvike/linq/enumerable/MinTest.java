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
import java.util.Comparator;
import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class MinTest extends TestCase {
    private static IEnumerable<Object[]> Min_Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42, 1), 42);
        argsList.add(Linq.range(1, 10).toArray(), 1);
        argsList.add(Linq.of(new int[]{-1, -10, 10, 200, 1000}), -10);
        argsList.add(Linq.of(new int[]{3000, 100, 200, 1000}), 100);
        argsList.add(Linq.of(new int[]{3000, 100, 200, 1000}).concat(Linq.repeat(Integer.MIN_VALUE, 1)), Integer.MIN_VALUE);

        argsList.add(Linq.repeat(20, 1), 20);
        argsList.add(Linq.repeat(-2, 5), -2);
        argsList.add(Linq.range(1, 10).toArray(), 1);
        argsList.add(Linq.of(new int[]{6, 9, 10, 7, 8}), 6);
        argsList.add(Linq.of(new int[]{6, 9, 10, 0, -5}), -5);
        argsList.add(Linq.of(new int[]{6, 0, 9, 0, 10, 0}), 0);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_Long_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42L, 1), 42L);
        argsList.add(Linq.range(1, 10).select(i -> (long) i).toArray(), 1L);
        argsList.add(Linq.of(new long[]{-1, -10, 10, 200, 1000}), -10L);
        argsList.add(Linq.of(new long[]{3000, 100, 200, 1000}), 100L);
        argsList.add(Linq.of(new long[]{3000, 100, 200, 1000}).concat(Linq.repeat(Long.MIN_VALUE, 1)), Long.MIN_VALUE);

        argsList.add(Linq.repeat(Integer.MAX_VALUE + 10L, 1), Integer.MAX_VALUE + 10L);
        argsList.add(Linq.repeat(500L, 5), 500L);
        argsList.add(Linq.of(new long[]{-250, 49, 130, 47, 28}), -250L);
        argsList.add(Linq.of(new long[]{6, 9, 10, 0, -Integer.MAX_VALUE - 50L}), -Integer.MAX_VALUE - 50L);
        argsList.add(Linq.of(new long[]{6, -5, 9, -5, 10, -5}), -5L);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_Float_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42f, 1), 42f);
        argsList.add(Linq.range(1, 10).select(i -> (float) i).toArray(), 1f);
        argsList.add(Linq.of(new float[]{-1, -10, 10, 200, 1000}), -10f);
        argsList.add(Linq.of(new float[]{3000, 100, 200, 1000}), 100f);
        argsList.add(Linq.of(new float[]{3000, 100, 200, 1000}).concat(Linq.repeat(Float.MIN_VALUE, 1)), Float.MIN_VALUE);

        argsList.add(Linq.repeat(5.5f, 1), 5.5f);
        argsList.add(Linq.repeat(Float.NaN, 5), Float.NaN);
        argsList.add(Linq.of(new float[]{-2.5f, 4.9f, 130f, 4.7f, 28f}), -2.5f);
        argsList.add(Linq.of(new float[]{6.8f, 9.4f, 10f, 0, -5.6f}), -5.6f);
        argsList.add(Linq.of(new float[]{-5.5f, Float.NEGATIVE_INFINITY, 9.9f, Float.NEGATIVE_INFINITY}), Float.NEGATIVE_INFINITY);

        argsList.add(Linq.of(new float[]{Float.NaN, 6.8f, 9.4f, 10f, 0, -5.6f}), Float.NaN);
        argsList.add(Linq.of(new float[]{6.8f, 9.4f, 10f, 0, -5.6f, Float.NaN}), Float.NaN);
        argsList.add(Linq.of(new float[]{Float.NaN, Float.NEGATIVE_INFINITY}), Float.NaN);
        argsList.add(Linq.of(new float[]{Float.NEGATIVE_INFINITY, Float.NaN}), Float.NaN);

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Float.NaN in the array,
        // as nothing can be less than Float.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        argsList.add(Linq.repeat(Float.NaN, Integer.MAX_VALUE), Float.NaN);
        argsList.add(Linq.repeat(Float.NaN, 3), Float.NaN);

        // Normally NaN < anything is false, as is anything < NaN
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        argsList.add(Linq.range(1, 10).select(i -> (float) i).concat(Linq.repeat(Float.NaN, 1)).toArray(), Float.NaN);
        argsList.add(Linq.of(new float[]{-1F, -10, Float.NaN, 10, 200, 1000}), Float.NaN);
        argsList.add(Linq.of(new float[]{Float.MIN_VALUE, 3000F, 100, 200, Float.NaN, 1000}), Float.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_Double_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(42.0, 1), 42.0);
        argsList.add(Linq.range(1, 10).select(i -> (double) i).toArray(), 1.0);
        argsList.add(Linq.of(new double[]{-1, -10, 10, 200, 1000}), -10.0);
        argsList.add(Linq.of(new double[]{3000, 100, 200, 1000}), 100.0);
        argsList.add(Linq.of(new double[]{3000, 100, 200, 1000}).concat(Linq.repeat(Double.MIN_VALUE, 1)), Double.MIN_VALUE);

        argsList.add(Linq.repeat(5.5, 1), 5.5);
        argsList.add(Linq.of(new double[]{-2.5, 4.9, 130, 4.7, 28}), -2.5);
        argsList.add(Linq.of(new double[]{6.8, 9.4, 10, 0, -5.6}), -5.6);
        argsList.add(Linq.of(new double[]{-5.5, Double.NEGATIVE_INFINITY, 9.9, Double.NEGATIVE_INFINITY}), Double.NEGATIVE_INFINITY);

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Double.NaN in the array,
        // as nothing can be less than Double.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        argsList.add(Linq.repeat(Double.NaN, Integer.MAX_VALUE), Double.NaN);
        argsList.add(Linq.repeat(Double.NaN, 3), Double.NaN);

        argsList.add(Linq.of(new double[]{Double.NaN, 6.8, 9.4, 10, 0, -5.6}), Double.NaN);
        argsList.add(Linq.of(new double[]{6.8, 9.4, 10, 0, -5.6, Double.NaN}), Double.NaN);
        argsList.add(Linq.of(new double[]{Double.NaN, Double.NEGATIVE_INFINITY}), Double.NaN);
        argsList.add(Linq.of(new double[]{Double.NEGATIVE_INFINITY, Double.NaN}), Double.NaN);

        // Normally NaN < anything is false, as is anything < NaN
        // However, this leads to some irksome outcomes in Min and Max.
        // If we use those semantics then Min(NaN, 5.0) is NaN, but
        // Min(5.0, NaN) is 5.0!  To fix this, we impose a total
        // ordering where NaN is smaller than every value, including
        // negative infinity.
        argsList.add(Linq.range(1, 10).select(i -> (double) i).concat(Linq.repeat(Double.NaN, 1)).toArray(), Double.NaN);
        argsList.add(Linq.of(new double[]{-1, -10, Double.NaN, 10, 200, 1000}), Double.NaN);
        argsList.add(Linq.of(new double[]{Double.MIN_VALUE, 3000F, 100, 200, Double.NaN, 1000}), Double.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_Decimal_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(m("42"), 1), m("42"));
        argsList.add(Linq.range(1, 10).select(i -> m(i)).toArray(), m("1"));
        argsList.add(Linq.of(m(-1), m(-10), m(10), m(200), m(1000)), m("-10"));
        argsList.add(Linq.of(m(3000), m(100), m(200), m(1000)), m("100"));
        argsList.add(Linq.of(m(3000), m(100), m(200), m(1000)).concat(Linq.repeat(MIN_DECIMAL, 1)), MIN_DECIMAL);

        argsList.add(Linq.repeat(m("5.5"), 1), m("5.5"));
        argsList.add(Linq.repeat(m("-3.4"), 5), m("-3.4"));
        argsList.add(Linq.of(m("-2.5"), m("4.9"), m("130"), m("4.7"), m("28")), m("-2.5"));
        argsList.add(Linq.of(m("6.8"), m("9.4"), m("10"), m("0"), m("0"), MIN_DECIMAL), MIN_DECIMAL);
        argsList.add(Linq.of(m("-5.5"), m("0"), m("9.9"), m("-5.5"), m("5")), m("-5.5"));
        return argsList;
    }

    private static IEnumerable<Object[]> Min_NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> i).toArray(), 1);
        argsList.add(Linq.of(null, -1, -10, 10, 200, 1000), -10);
        argsList.add(Linq.of(null, 3000, 100, 200, 1000), 100);
        argsList.add(Linq.of(null, 3000, 100, 200, 1000).concat(Linq.repeat(Integer.MIN_VALUE, 1)), Integer.MIN_VALUE);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.repeat(42, 1), 42);

        argsList.add(Linq.<Integer>empty(), null);
        argsList.add(Linq.repeat(20, 1), 20);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(6, null, 9, 10, null, 7, 8), 6);
        argsList.add(Linq.of(null, null, null, null, null, -5), -5);
        argsList.add(Linq.of(6, null, null, 0, 9, 0, 10, 0), 0);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_NullableLong_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> (long) i).toArray(), 1L);
        argsList.add(Linq.of(null, -1L, -10L, 10L, 200L, 1000L), -10L);
        argsList.add(Linq.of(null, 3000L, 100L, 200L, 1000L), 100L);
        argsList.add(Linq.of(null, 3000L, 100L, 200L, 1000L).concat(Linq.repeat(Long.MIN_VALUE, 1)), Long.MIN_VALUE);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.repeat(42L, 1), 42L);

        argsList.add(Linq.<Long>empty(), null);
        argsList.add(Linq.repeat(Long.MAX_VALUE, 1), Long.MAX_VALUE);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(Long.MIN_VALUE, null, 9L, 10L, null, 7L, 8L), Long.MIN_VALUE);
        argsList.add(Linq.of(null, null, null, null, null, -Long.MAX_VALUE), -Long.MAX_VALUE);
        argsList.add(Linq.of(6L, null, null, 0L, 9L, 0L, 10L, 0L), 0L);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_NullableFloat_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> (float) i).toArray(), 1f);
        argsList.add(Linq.of(null, -1f, -10f, 10f, 200f, 1000f), -10f);
        argsList.add(Linq.of(null, 3000f, 100f, 200f, 1000f), 100f);
        argsList.add(Linq.of(null, 3000f, 100f, 200f, 1000f).concat(Linq.repeat(Float.MIN_VALUE, 1)), Float.MIN_VALUE);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.repeat(42f, 1), 42f);

        argsList.add(Linq.<Float>empty(), null);
        argsList.add(Linq.repeat(Float.MIN_VALUE, 1), Float.MIN_VALUE);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.of(-4.50f, null, 10.98f, null, 7.5f, 8.6f), -4.5f);
        argsList.add(Linq.of(null, null, null, null, null, 0f), 0f);
        argsList.add(Linq.of(6.4f, null, null, -0.5f, 9.4f, -0.5f, 10.9f, -0.5f), -0.5f);

        argsList.add(Linq.of(Float.NaN, 6.8f, 9.4f, 10f, 0f, null, -5.6f), Float.NaN);
        argsList.add(Linq.of(6.8f, 9.4f, 10f, 0f, null, -5.6f, Float.NaN), Float.NaN);
        argsList.add(Linq.of(Float.NaN, Float.NEGATIVE_INFINITY), Float.NaN);
        argsList.add(Linq.of(Float.NEGATIVE_INFINITY, Float.NaN), Float.NaN);
        argsList.add(Linq.of(Float.NaN, null, null, null), Float.NaN);
        argsList.add(Linq.of(null, null, null, Float.NaN), Float.NaN);
        argsList.add(Linq.of(null, Float.NaN, null), Float.NaN);

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Float.NaN in the array,
        // as nothing can be less than Float.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        argsList.add(Linq.repeat(Float.NaN, Integer.MAX_VALUE), Float.NaN);
        argsList.add(Linq.repeat(Float.NaN, 3), Float.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_NullableDouble_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> (double) i).toArray(), 1.0);
        argsList.add(Linq.of(null, -1d, -10d, 10d, 200d, 1000d), -10.0);
        argsList.add(Linq.of(null, 3000d, 100d, 200d, 1000d), 100.0);
        argsList.add(Linq.of(null, 3000d, 100d, 200d, 1000d).concat(Linq.repeat(Double.MIN_VALUE, 1)), Double.MIN_VALUE);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.repeat(42d, 1), 42.0);

        argsList.add(Linq.<Double>empty(), null);
        argsList.add(Linq.repeat(Double.MIN_VALUE, 1), Double.MIN_VALUE);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(-4.50, null, 10.98, null, 7.5, 8.6), -4.5);
        argsList.add(Linq.of(null, null, null, null, null, 0d), 0.0);
        argsList.add(Linq.of(6.4, null, null, -0.5, 9.4, -0.5, 10.9, -0.5), -0.5);

        argsList.add(Linq.of(Double.NaN, 6.8, 9.4, 10.0, 0.0, null, -5.6), Double.NaN);
        argsList.add(Linq.of(6.8, 9.4, 10d, 0.0, null, -5.6d, Double.NaN), Double.NaN);
        argsList.add(Linq.of(Double.NaN, Double.NEGATIVE_INFINITY), Double.NaN);
        argsList.add(Linq.of(Double.NEGATIVE_INFINITY, Double.NaN), Double.NaN);
        argsList.add(Linq.of(Double.NaN, null, null, null), Double.NaN);
        argsList.add(Linq.of(null, null, null, Double.NaN), Double.NaN);
        argsList.add(Linq.of(null, Double.NaN, null), Double.NaN);

        // In .NET Core, Enumerable.Min shortcircuits if it finds any Double.NaN in the array,
        // as nothing can be less than Double.NaN. See https://github.com/dotnet/corefx/pull/2426.
        // Without this optimization, we would iterate through Integer.MAX_VALUE elements, which takes
        // a long time.
        argsList.add(Linq.repeat(Double.NaN, Integer.MAX_VALUE), Double.NaN);
        argsList.add(Linq.repeat(Double.NaN, 3), Double.NaN);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_NullableDecimal_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> m(i)).toArray(), m("1"));
        argsList.add(Linq.of(null, m(-1), m(-10), m(10), m(200), m(1000)), m("-10"));
        argsList.add(Linq.of(null, m(3000), m(100), m(200), m(1000)), m("100"));
        argsList.add(Linq.of(null, m(3000), m(100), m(200), m(1000)).concat(Linq.repeat(MIN_DECIMAL, 1)), MIN_DECIMAL);
        argsList.add(Linq.repeat(null, 100), null);
        argsList.add(Linq.repeat(m("42"), 1), m("42"));

        argsList.add(Linq.<BigDecimal>empty(), null);
        argsList.add(Linq.repeat(MAX_DECIMAL, 1), MAX_DECIMAL);
        argsList.add(Linq.repeat(null, 5), null);
        argsList.add(Linq.of(m("-4.50"), null, null, m("10.98"), null, m("7.5"), m("8.6")), m("-4.5"));
        argsList.add(Linq.of(null, null, null, null, null, m("0")), m("0"));
        argsList.add(Linq.of(m("6.4"), null, null, MIN_DECIMAL, m("9.4"), MIN_DECIMAL, m("10.9"), MIN_DECIMAL), MIN_DECIMAL);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_DateTime_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> newDate(2000, 1, i)).toArray(), newDate(2000, 1, 1));
        argsList.add(Linq.of(newDate(2000, 12, 1), newDate(2000, 1, 1), newDate(2000, 1, 12)), newDate(2000, 1, 1));

        Date[] hundred = new Date[]{
                newDate(3000, 1, 1),
                newDate(100, 1, 1),
                newDate(200, 1, 1),
                newDate(1000, 1, 1)
        };
        argsList.add(Linq.of(hundred), newDate(100, 1, 1));
        argsList.add(Linq.of(hundred).concat(Linq.repeat(MIN_DATE, 1)), MIN_DATE);
        return argsList;
    }

    private static IEnumerable<Object[]> Min_String_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(1, 10).select(i -> i.toString()).toArray(), "1");
        argsList.add(Linq.of("Alice", "Bob", "Charlie", "Eve", "Mallory", "Trent", "Victor"), "Alice");
        argsList.add(Linq.of(null, "Charlie", null, "Victor", "Trent", null, "Eve", "Alice", "Mallory", "Bob"), "Alice");

        argsList.add(Linq.<String>empty(), null);
        argsList.add(Linq.repeat("Hello", 1), "Hello");
        argsList.add(Linq.repeat("hi", 5), "hi");
        argsList.add(Linq.of("aaa", "abcd", "bark", "temp", "cat"), "aaa");
        argsList.add(Linq.of(null, null, null, null, "aAa"), "aAa");
        argsList.add(Linq.of("ooo", "www", "www", "ooo", "ooo", "ppp"), "ooo");
        argsList.add(Linq.repeat(null, 5), null);
        return argsList;
    }

    private static <TSource> Object[] WrapArgs(IEnumerable<TSource> source, Comparator<TSource> comparer, TSource expected) {
        return new Object[]{source, comparer, expected};
    }

    private static IEnumerable<Object[]> Min_Generic_TestData() {
        ArgsList argsList = new ArgsList();

        argsList.add(WrapArgs(
                Linq.<Integer>empty(),
                null,
                null));

        argsList.add(WrapArgs(
                Linq.<Integer>empty(),
                (x, y) -> 0,
                null));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                null,
                0));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                (x, y) -> -x.compareTo(y),
                9));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                (x, y) -> 0,
                0));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                null,
                "Aardvark"));

        argsList.add(WrapArgs(
                Linq.of("Aardvark", "Zyzzyva", "Zebra", "Antelope"),
                (x, y) -> -x.compareTo(y),
                "Zyzzyva"));

        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.min(), q.min());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.min(), q.min());
    }

    @ParameterizedTest
    @MethodSource("Min_Int_TestData")
    void Min_Int(IEnumerable<Integer> source, int expected) {
        assertEquals(expected, source.minInt());
        assertEquals(expected, source.minInt(x -> x));
    }

    @Test
    void Min_Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minInt(x -> x));
    }

    @Test
    void Min_Int_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minInt());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().minInt(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_Long_TestData")
    void Min_Long(IEnumerable<Long> source, long expected) {
        assertEquals(expected, source.minLong());
        assertEquals(expected, source.minLong(x -> x));
    }

    @Test
    void Min_Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLong(x -> x));
    }

    @Test
    void Min_Long_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().minLong());
        assertThrows(InvalidOperationException.class, () -> Linq.<Long>empty().minLong(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_Float_TestData")
    void Min_Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.minFloat());
        assertEquals(expected, source.minFloat(x -> x));
    }

    @Test
    void Min_Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloat(x -> x));
    }

    @Test
    void Min_Float_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().minFloat());
        assertThrows(InvalidOperationException.class, () -> Linq.<Float>empty().minFloat(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_Double_TestData")
    void Min_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.minDouble());
        assertEquals(expected, source.minDouble(x -> x));
    }

    @Test
    void Min_Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDouble(x -> x));
    }

    @Test
    void Min_Double_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().minDouble());
        assertThrows(InvalidOperationException.class, () -> Linq.<Double>empty().minDouble(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_Decimal_TestData")
    void Min_Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.minDecimal());
        assertEquals(expected, source.minDecimal(x -> x));
    }

    @Test
    void Min_Decimal_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().minDecimal());
        assertThrows(InvalidOperationException.class, () -> Linq.<BigDecimal>empty().minDecimal(x -> x));
    }

    @Test
    void Min_Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimal(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_NullableInt_TestData")
    void Min_NullableInt(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.minIntNull());
    }

    @ParameterizedTest
    @MethodSource("Min_NullableInt_TestData")
    void Min_NullableIntRunOnce(IEnumerable<Integer> source, Integer expected) {
        assertEquals(expected, source.runOnce().minIntNull());
    }

    @Test
    void Min_NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).minIntNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_NullableLong_TestData")
    void Min_NullableLong(IEnumerable<Long> source, Long expected) {
        assertEquals(expected, source.minLongNull());
    }

    @Test
    void Min_NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).minLongNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_NullableFloat_TestData")
    void Min_NullableFloat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.minFloatNull());
        assertEquals(expected, source.minFloatNull(x -> x));
    }

    @Test
    void Min_NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).minFloatNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_NullableDouble_TestData")
    void Min_NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.minDoubleNull());
        assertEquals(expected, source.minDoubleNull(x -> x));
    }

    @Test
    void Min_NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).minDoubleNull());
    }

    @ParameterizedTest
    @MethodSource("Min_NullableDecimal_TestData")
    void Min_NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.minDecimalNull());
        assertEquals(expected, source.minDecimalNull(x -> x));
    }

    @Test
    void Min_NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).minDecimalNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_DateTime_TestData")
    void Min_DateTime(IEnumerable<Date> source, Date expected) {
        assertEquals(expected, source.min());
        assertEquals(expected, source.min(x -> x));
    }

    @Test
    void Min_DateTime_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).min());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).min(x -> x));
    }

    @Test
    void Min_DateTime_EmptySource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().min());
        assertThrows(InvalidOperationException.class, () -> Linq.<Date>empty().min(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_String_TestData")
    void Min_String(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.minNull());
        assertEquals(expected, source.minNull(x -> x));
    }

    @ParameterizedTest
    @MethodSource("Min_String_TestData")
    void Min_StringRunOnce(IEnumerable<String> source, String expected) {
        assertEquals(expected, source.runOnce().minNull());
        assertEquals(expected, source.runOnce().minNull(x -> x));
    }

    @Test
    void Min_String_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).minNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).minNull(x -> x));
    }

    @Test
    void Min_Int_WithSelectorAccessingProperty() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum("Tim", 10),
                new NameNum("John", -105),
                new NameNum("Bob", -30)
        };
        assertEquals(-105, Linq.of(source).minInt(e -> e.num));
    }

    @Test
    void Min_Int_NullSelector_ThrowsArgumentNullException() {
        IntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().minInt(selector));
    }

    @Test
    void Min_Long_WithSelectorAccessingProperty() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum("Tim", 10L),
                new NameNum("John", Long.MIN_VALUE),
                new NameNum("Bob", -10L)
        };

        assertEquals(Long.MIN_VALUE, Linq.of(source).minLong(e -> e.num));
    }

    @Test
    void Min_Long_NullSelector_ThrowsArgumentNullException() {
        LongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().minLong(selector));
    }

    @Test
    void Min_Float_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum("Tim", -45.5f),
                new NameNum("John", -132.5f),
                new NameNum("Bob", 20.45f)
        };
        assertEquals(-132.5f, Linq.of(source).minFloat(e -> e.num));
    }

    @Test
    void Min_Float_NullSelector_ThrowsArgumentNullException() {
        FloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().minFloat(selector));
    }

    @Test
    void Min_Double_WithSelectorAccessingProperty() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum("Tim", -45.5),
                new NameNum("John", -132.5),
                new NameNum("Bob", 20.45)
        };
        assertEquals(-132.5, Linq.of(source).minDouble(e -> e.num));
    }

    @Test
    void Min_Double_NullSelector_ThrowsArgumentNullException() {
        DoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().minDouble(selector));
    }

    @Test
    void Min_Decimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum("Tim", m("100.45")),
                new NameNum("John", m("10.5")),
                new NameNum("Bob", m("0.05"))
        };
        assertEquals(m("0.05"), Linq.of(source).minDecimal(e -> e.num));
    }

    @Test
    void Min_Decimal_NullSelector_ThrowsArgumentNullException() {
        DecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().minDecimal(selector));
    }

    @Test
    void Min_NullableInt_WithSelectorAccessingProperty() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum("Tim", 10),
                new NameNum("John", null),
                new NameNum("Bob", -30)
        };
        assertEquals(-30, Linq.of(source).minIntNull(e -> e.num));
    }

    @Test
    void Min_NullableInt_NullSelector_ThrowsArgumentNullException() {
        NullableIntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().minIntNull(selector));
    }

    @Test
    void Min_NullableLong_WithSelectorAccessingProperty() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum("Tim", null),
                new NameNum("John", Long.MIN_VALUE),
                new NameNum("Bob", -10L)
        };
        assertEquals(Long.MIN_VALUE, Linq.of(source).minLongNull(e -> e.num));
    }

    @Test
    void Min_NullableLong_NullSelector_ThrowsArgumentNullException() {
        NullableLongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().minLongNull(selector));
    }

    @Test
    void Min_NullableFloat_WithSelectorAccessingProperty() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum("Tim", -45.5f),
                new NameNum("John", -132.5f),
                new NameNum("Bob", null)
        };

        assertEquals(-132.5f, Linq.of(source).minFloatNull(e -> e.num));
    }

    @Test
    void Min_NullableFloat_NullSelector_ThrowsArgumentNullException() {
        NullableFloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().minFloatNull(selector));
    }

    @Test
    void Min_NullableDouble_WithSelectorAccessingProperty() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum("Tim", -45.5d),
                new NameNum("John", -132.5d),
                new NameNum("Bob", null)
        };
        assertEquals(-132.5, Linq.of(source).minDoubleNull(e -> e.num));
    }

    @Test
    void Min_NullableDouble_NullSelector_ThrowsArgumentNullException() {
        NullableDoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().minDoubleNull(selector));
    }

    @Test
    void Min_NullableDecimal_WithSelectorAccessingProperty() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum("Tim", m("100.45")),
                new NameNum("John", m("10.5")),
                new NameNum("Bob", null)
        };
        assertEquals(m("10.5"), Linq.of(source).minDecimalNull(e -> e.num));
    }

    @Test
    void Min_NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        NullableDecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().minDecimalNull(selector));
    }

    @Test
    void Min_DateTime_NullSelector_ThrowsArgumentNullException() {
        Func1<Date, Date> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().min(selector));
    }

    @Test
    void Min_String_WithSelectorAccessingProperty() {
        NameNum<String>[] source = new NameNum[]{
                new NameNum("Tim", m("100.45")),
                new NameNum("John", m("10.5")),
                new NameNum("Bob", m("0.05"))
        };
        assertEquals("Bob", Linq.of(source).minNull(e -> e.name));
    }

    @Test
    void Min_String_NullSelector_ThrowsArgumentNullException() {
        Func1<String, String> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<String>empty().minNull(selector));
    }

    @Test
    void Min_Bool_EmptySource_ThrowsInvalodOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Boolean>empty().min());
    }

    @Test
    void Min_Generic_NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;

        assertThrows(NullPointerException.class, () -> source.min());
        assertThrows(NullPointerException.class, () -> source.min((Comparator<Integer>) null));
        assertThrows(NullPointerException.class, () -> source.min((x, y) -> 0));
    }

    @Test
    void Min_Generic_EmptyStructSource_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().min());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().min((Comparator<Integer>) null));
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().min((x, y) -> 0));
    }

    @ParameterizedTest
    @MethodSource("Min_Generic_TestData")
    <TSource> void Min_Generic_HasExpectedOutput(IEnumerable<TSource> source, Comparator<TSource> comparer, TSource expected) {
        assertEquals(expected, source.minNull(comparer));
    }

    @ParameterizedTest
    @MethodSource("Min_Generic_TestData")
    <TSource> void Min_Generic_RunOnce_HasExpectedOutput(IEnumerable<TSource> source, Comparator<TSource> comparer, TSource expected) {
        assertEquals(expected, source.runOnce().minNull(comparer));
    }

    @Test
    void testMinInt() {
        Integer[] numbers = {0, 2, 3};
        assertEquals(0, Linq.of(numbers).minInt());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minInt());
    }

    @Test
    void testMinIntNull() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.of(numbers).minIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minIntNull());
    }

    @Test
    void testMinLong() {
        Long[] numbers = {0L, 2L, 3L};
        assertEquals(0L, Linq.of(numbers).minLong());

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minLong());
    }

    @Test
    void testMinLongNull() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.of(numbers).minLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minLongNull());
    }

    @Test
    void testMinFloat() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).minFloat());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minFloat());
    }

    @Test
    void testMinFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).minFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minFloatNull());
    }

    @Test
    void testMinDouble() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).minDouble());

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minDouble());
    }

    @Test
    void testMinDoubleNull() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).minDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minDoubleNull());
    }

    @Test
    void testMinDecimal() {
        BigDecimal[] numbers = {m("0"), m("2"), m("3")};
        assertEquals(m("0"), Linq.of(numbers).minDecimal());

        BigDecimal[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minDecimal());
    }

    @Test
    void testMinDecimalNull() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("0"), Linq.of(numbers).minDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minDecimalNull());
    }

    @Test
    void testMin() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(0f, Linq.of(numbers).min());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).min());
    }

    @Test
    void testMinNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(0f, Linq.of(numbers).minNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minNull());
    }

    @Test
    void testMinIntWithSelector() {
        Integer[] numbers = {0, 2, 3};
        assertEquals(0, Linq.of(numbers).minInt(n -> n));

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minInt(n -> n));
    }

    @Test
    void testMinIntNullWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(0, Linq.of(numbers).minIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minIntNull(n -> n));
    }

    @Test
    void testMinLongWithSelector() {
        Long[] numbers = {0L, 2L, 3L};
        assertEquals(0L, Linq.of(numbers).minLong(n -> n));

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minLong(n -> n));
    }

    @Test
    void testMinLongNullWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(0L, Linq.of(numbers).minLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minLongNull(n -> n));
    }

    @Test
    void testMinFloatWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).minFloat(n -> n));

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minFloat(n -> n));
    }

    @Test
    void testMinFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).minFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minFloatNull(n -> n));
    }

    @Test
    void testMinDoubleWithSelector() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).minDouble(n -> n));

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minDouble(n -> n));
    }

    @Test
    void testMinDoubleNullWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).minDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minDoubleNull(n -> n));
    }

    @Test
    void testMinDecimalWithSelector() {
        BigDecimal[] numbers = {m("0"), m("2"), m("3")};
        assertEquals(m("0"), Linq.of(numbers).minDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).minDecimal(n -> n));
    }

    @Test
    void testMinDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("0"), Linq.of(numbers).minDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).minDecimalNull(n -> n));
    }

    @Test
    void testMinWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        Float f = Linq.of(numbers).min(n -> n);
        assertEquals(0f, f);

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).min(n -> n));
    }

    @Test
    void testMinNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.of(numbers).minNull(n -> n);
        assertEquals(0f, f);

        Float[] numbers2 = {null};
        Float f2 = Linq.of(numbers2).minNull(n -> n);
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
