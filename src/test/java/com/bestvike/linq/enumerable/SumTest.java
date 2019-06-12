package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SumTest extends TestCase {
    @Test
    public void SumOfInt_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> sourceInt = null;
        assertThrows(NullPointerException.class, () -> sourceInt.sumInt());
        assertThrows(NullPointerException.class, () -> sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> sourceNullableInt = null;
        assertThrows(NullPointerException.class, () -> sourceNullableInt.sumInt());
        assertThrows(NullPointerException.class, () -> sourceNullableInt.sumInt(x -> x));
    }

    @Test
    public void SumOfLong_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Long> sourceLong = null;
        assertThrows(NullPointerException.class, () -> sourceLong.sumLong());
        assertThrows(NullPointerException.class, () -> sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Long> sourceNullableLong = null;
        assertThrows(NullPointerException.class, () -> sourceNullableLong.sumLong());
        assertThrows(NullPointerException.class, () -> sourceNullableLong.sumLong(x -> x));
    }

    @Test
    public void SumOfFloat_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceFloat = null;
        assertThrows(NullPointerException.class, () -> sourceFloat.sumFloat());
        assertThrows(NullPointerException.class, () -> sourceFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfNullableOfFloat_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceNullableFloat = null;
        assertThrows(NullPointerException.class, () -> sourceNullableFloat.sumFloat());
        assertThrows(NullPointerException.class, () -> sourceNullableFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfDouble_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceDouble = null;
        assertThrows(NullPointerException.class, () -> sourceDouble.sumDouble());
        assertThrows(NullPointerException.class, () -> sourceDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfNullableOfDouble_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceNullableDouble = null;
        assertThrows(NullPointerException.class, () -> sourceNullableDouble.sumDouble());
        assertThrows(NullPointerException.class, () -> sourceNullableDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfDecimal_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceDecimal = null;
        assertThrows(NullPointerException.class, () -> sourceDecimal.sumDecimal());
        assertThrows(NullPointerException.class, () -> sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceNullableDecimal = null;
        assertThrows(NullPointerException.class, () -> sourceNullableDecimal.sumDecimal());
        assertThrows(NullPointerException.class, () -> sourceNullableDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfInt_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> sourceInt = Linq.empty();
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceInt.sumInt(selector));
    }

    @Test
    public void SumOfNullableOfInt_SelectorIsNull_ArgumentNullExceptionThrown() {

        IEnumerable<Integer> sourceNullableInt = Linq.empty();
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableInt.sumInt(selector));
    }

    @Test
    public void SumOfLong_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Long> sourceLong = Linq.empty();
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceLong.sumLong(selector));
    }

    @Test
    public void SumOfNullableOfLong_SelectorIsNull_ArgumentNullExceptionThrown() {

        IEnumerable<Long> sourceNullableLong = Linq.empty();
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableLong.sumLong(selector));
    }

    @Test
    public void SumOfFloat_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceFloat = Linq.empty();
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceFloat.sumFloat(selector));
    }

    @Test
    public void SumOfNullableOfFloat_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceNullableFloat = Linq.empty();
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableFloat.sumFloat(selector));
    }

    @Test
    public void SumOfDouble_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceDouble = Linq.empty();
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceDouble.sumDouble(selector));
    }

    @Test
    public void SumOfNullableOfDouble_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceNullableDouble = Linq.empty();
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableDouble.sumDouble(selector));
    }

    @Test
    public void SumOfDecimal_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.empty();
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceDecimal.sumDecimal(selector));
    }

    @Test
    public void SumOfNullableOfDecimal_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.empty();
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableDecimal.sumDecimal(selector));
    }

    @Test
    public void SumOfInt_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Integer> sourceInt = Linq.empty();
        assertEquals(0, sourceInt.sumInt());
        assertEquals(0, sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Integer> sourceNullableInt = Linq.empty();
        assertEquals(0, sourceNullableInt.sumInt());
        assertEquals(0, sourceNullableInt.sumInt(x -> x));
    }

    @Test
    public void SumOfLong_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Long> sourceLong = Linq.empty();
        assertEquals(0L, sourceLong.sumLong());
        assertEquals(0L, sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Long> sourceNullableLong = Linq.empty();
        assertEquals(0L, sourceNullableLong.sumLong());
        assertEquals(0L, sourceNullableLong.sumLong(x -> x));
    }

    @Test
    public void SumOfFloat_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Float> sourceFloat = Linq.empty();
        assertEquals(0f, sourceFloat.sumFloat());
        assertEquals(0f, sourceFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfNullableOfFloat_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Float> sourceNullableFloat = Linq.empty();
        assertEquals(0f, sourceNullableFloat.sumFloat());
        assertEquals(0f, sourceNullableFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfDouble_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Double> sourceDouble = Linq.empty();
        assertEquals(0d, sourceDouble.sumDouble());
        assertEquals(0d, sourceDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfNullableOfDouble_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<Double> sourceNullableDouble = Linq.empty();
        assertEquals(0d, sourceNullableDouble.sumDouble());
        assertEquals(0d, sourceNullableDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfDecimal_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.empty();
        assertEquals(BigDecimal.ZERO, sourceDecimal.sumDecimal());
        assertEquals(BigDecimal.ZERO, sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceIsEmptyCollection_ZeroReturned() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.empty();
        assertEquals(BigDecimal.ZERO, sourceNullableDecimal.sumDecimal());
        assertEquals(BigDecimal.ZERO, sourceNullableDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfInt_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Integer> sourceInt = Linq.asEnumerable(new int[]{1, -2, 3, -4});
        assertEquals(-2, sourceInt.sumInt());
        assertEquals(-2, sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Integer> sourceNullableInt = Linq.asEnumerable(1, -2, null, 3, -4, null);
        assertEquals(-2, sourceNullableInt.sumInt());
        assertEquals(-2, sourceNullableInt.sumInt(x -> x));
    }

    @Test
    public void SumOfLong_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Long> sourceLong = Linq.asEnumerable(new long[]{1L, -2L, 3L, -4L});
        assertEquals(-2L, sourceLong.sumLong());
        assertEquals(-2L, sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Long> sourceNullableLong = Linq.asEnumerable(1L, -2L, null, 3L, -4L, null);
        assertEquals(-2L, sourceNullableLong.sumLong());
        assertEquals(-2L, sourceNullableLong.sumLong(x -> x));
    }

    @Test
    public void SumOfFloat_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Float> sourceFloat = Linq.asEnumerable(new float[]{1f, 0.5f, -1f, 0.5f});
        assertEquals(1f, sourceFloat.sumFloat());
        assertEquals(1f, sourceFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfNullableOfFloat_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Float> sourceNullableFloat = Linq.asEnumerable(1f, 0.5f, null, -1f, 0.5f, null);
        assertEquals(1f, sourceNullableFloat.sumFloat());
        assertEquals(1f, sourceNullableFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfDouble_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Double> sourceDouble = Linq.asEnumerable(new double[]{1d, 0.5d, -1d, 0.5d});
        assertEquals(1d, sourceDouble.sumDouble());
        assertEquals(1d, sourceDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfNullableOfDouble_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Double> sourceNullableDouble = Linq.asEnumerable(1d, 0.5d, null, -1d, 0.5d, null);
        assertEquals(1d, sourceNullableDouble.sumDouble());
        assertEquals(1d, sourceNullableDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfDecimal_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.asEnumerable(m("1"), m("0.5"), m("-1"), m("0.5"));
        assertEquals(m("1"), sourceDecimal.sumDecimal());
        assertEquals(m("1"), sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.asEnumerable(m("1"), m("0.5"), null, m("-1"), m("0.5"), null);
        assertEquals(m("1"), sourceNullableDecimal.sumDecimal());
        assertEquals(m("1"), sourceNullableDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfInt_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Integer> sourceInt = Linq.asEnumerable(new int[]{Integer.MAX_VALUE, 1});
        assertThrows(ArithmeticException.class, () -> sourceInt.sumInt());
        assertThrows(ArithmeticException.class, () -> sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Integer> sourceNullableInt = Linq.asEnumerable(Integer.MAX_VALUE, null, 1);
        assertThrows(ArithmeticException.class, () -> sourceNullableInt.sumInt());
        assertThrows(ArithmeticException.class, () -> sourceNullableInt.sumInt(x -> x));
    }

    @Test
    public void SumOfLong_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Long> sourceLong = Linq.asEnumerable(new long[]{Long.MAX_VALUE, 1L});
        assertThrows(ArithmeticException.class, () -> sourceLong.sumLong());
        assertThrows(ArithmeticException.class, () -> sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Long> sourceNullableLong = Linq.asEnumerable(Long.MAX_VALUE, null, 1L);
        assertThrows(ArithmeticException.class, () -> sourceNullableLong.sumLong());
        assertThrows(ArithmeticException.class, () -> sourceNullableLong.sumLong(x -> x));
    }

    @Test
    public void SumOfFloat_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Float> sourceFloat = Linq.asEnumerable(new float[]{Float.MAX_VALUE, Float.MAX_VALUE});
        assertTrue(Float.isInfinite(sourceFloat.sumFloat()));
        assertTrue(Float.isInfinite(sourceFloat.sumFloat(x -> x)));
    }

    @Test
    public void SumOfNullableOfFloat_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Float> sourceNullableFloat = Linq.asEnumerable(Float.MAX_VALUE, null, Float.MAX_VALUE);
        assertTrue(Float.isInfinite(sourceNullableFloat.sumFloat()));
        assertTrue(Float.isInfinite(sourceNullableFloat.sumFloat(x -> x)));
    }

    @Test
    public void SumOfDouble_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Double> sourceDouble = Linq.asEnumerable(new double[]{Double.MAX_VALUE, Double.MAX_VALUE});
        assertTrue(Double.isInfinite(sourceDouble.sumDouble()));
        assertTrue(Double.isInfinite(sourceDouble.sumDouble(x -> x)));
    }

    @Test
    public void SumOfNullableOfDouble_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Double> sourceNullableDouble = Linq.asEnumerable(Double.MAX_VALUE, null, Double.MAX_VALUE);
        assertTrue(Double.isInfinite(sourceNullableDouble.sumDouble()));
        assertTrue(Double.isInfinite(sourceNullableDouble.sumDouble(x -> x)));
    }

    @Test
    public void SumOfDecimal_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.asEnumerable(MAX_DECIMAL, m("1"));
        assertEquals(MAX_DECIMAL.add(m("1")), sourceDecimal.sumDecimal());
        assertEquals(MAX_DECIMAL.add(m("1")), sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.asEnumerable(MAX_DECIMAL, null, m("1"));
        assertEquals(MAX_DECIMAL.add(m("1")), sourceNullableDecimal.sumDecimal());
        assertEquals(MAX_DECIMAL.add(m("1")), sourceNullableDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, null, -777, 1, 2, -12345).where(x -> x != null && x > Integer.MIN_VALUE);

        assertEquals(q.sumInt(), q.sumInt());
    }

    @Test
    public void SolitaryNullableSingle() {
        Float[] source = {20.51f};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumFloat());
    }

    @Test
    public void NaNFromSingles() {
        Float[] source = {20.45f, 0f, -10.55f, Float.NaN};
        assertTrue(Float.isNaN(Linq.asEnumerable(source).sumFloat()));
    }

    @Test
    public void NullableSingleAllNull() {
        assertEquals(0f, Linq.repeat(0f, 4).sumFloat());
    }

    @Test
    public void NullableSingleToNegativeInfinity() {
        Float[] source = {-Float.MAX_VALUE, -Float.MAX_VALUE};
        assertTrue(Float.isInfinite(Linq.asEnumerable(source).sumFloat()));
    }

    @Test
    public void NullableSingleFromSelector() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5f),
                new NameNum<>("John", 0f),
                new NameNum<>("Bob", 8.5f)
        };
        assertEquals(18.0f, Linq.asEnumerable(source).sumFloat(e -> e.num));
    }

    @Test
    public void SolitaryInt32() {
        int[] source = {20};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumInt());
    }

    @Test
    public void OverflowInt32Negative() {
        int[] source = {-Integer.MAX_VALUE, 0, -5, -20};
        assertThrows(ArithmeticException.class, () -> Linq.asEnumerable(source).sumInt());
    }

    @Test
    public void Int32FromSelector() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", 50),
                new NameNum<>("Bob", -30)
        };
        assertEquals(30, Linq.asEnumerable(source).sumInt(e -> e.num));
    }

    @Test
    public void SolitaryNullableInt32() {
        Integer[] source = {-9};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumInt());
    }

    @Test
    public void NullableInt32AllNull() {
        assertEquals(0, Linq.repeat(0, 5).sumInt());
    }

    @Test
    public void NullableInt32NegativeOverflow() {
        Integer[] source = {-Integer.MAX_VALUE, 0, -5, null, null, -20};
        assertThrows(ArithmeticException.class, () -> Linq.asEnumerable(source).sumInt());
    }

    @Test
    public void NullableInt32FromSelector() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", 0),
                new NameNum<>("Bob", -30)
        };
        assertEquals(-20, Linq.asEnumerable(source).sumInt(e -> e.num));
    }

    @Test
    public void RunOnce() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", 0),
                new NameNum<>("Bob", -30)
        };
        assertEquals(-20, Linq.asEnumerable(source).runOnce().sumInt(e -> e.num));
    }

    @Test
    public void SolitaryInt64() {
        long[] source = {Integer.MAX_VALUE + 20L};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumLong());
    }

    @Test
    public void NullableInt64NegativeOverflow() {
        long[] source = {-Long.MAX_VALUE, 0, -5, 20, -16};
        assertThrows(ArithmeticException.class, () -> Linq.asEnumerable(source).sumLong());
    }

    @Test
    public void Int64FromSelector() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", 10L),
                new NameNum<>("John", (long) Integer.MAX_VALUE),
                new NameNum<>("Bob", 40L)
        };

        assertEquals(Integer.MAX_VALUE + 50L, Linq.asEnumerable(source).sumLong(e -> e.num));
    }

    @Test
    public void SolitaryNullableInt64() {
        Long[] source = {-Integer.MAX_VALUE - 20L};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumLong());
    }

    @Test
    public void NullableInt64AllNull() {
        assertEquals(0, Linq.repeat(0L, 5).sumLong());
    }

    @Test
    public void Int64NegativeOverflow() {
        Long[] source = {-Long.MAX_VALUE, 0L, -5L, -20L, null, null};
        assertThrows(ArithmeticException.class, () -> Linq.asEnumerable(source).sumLong());
    }

    @Test
    public void NullableInt64FromSelector() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", 10L),
                new NameNum<>("John", (long) Integer.MAX_VALUE),
                new NameNum<>("Bob", 0L)
        };

        assertEquals(Integer.MAX_VALUE + 10L, Linq.asEnumerable(source).sumLong(e -> e.num));
    }

    @Test
    public void SolitaryDouble() {
        double[] source = {20.51};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumDouble());
    }

    @Test
    public void DoubleWithNaN() {
        double[] source = {20.45, 0, -10.55, Double.NaN};
        assertTrue(Double.isNaN(Linq.asEnumerable(source).sumDouble()));
    }

    @Test
    public void DoubleToNegativeInfinity() {
        double[] source = {-Double.MAX_VALUE, -Double.MAX_VALUE};
        assertTrue(Double.isInfinite(Linq.asEnumerable(source).sumDouble()));
    }

    @Test
    public void DoubleFromSelector() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5d),
                new NameNum<>("John", 10.5d),
                new NameNum<>("Bob", 3.5d)
        };

        assertEquals(23.5, Linq.asEnumerable(source).sumDouble(e -> e.num));
    }

    @Test
    public void SolitaryNullableDouble() {
        Double[] source = {20.51};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumDouble());
    }

    @Test
    public void NullableDoubleAllNull() {
        assertEquals(0, Linq.repeat(0d, 4).sumDouble());
    }

    @Test
    public void NullableDoubleToNegativeInfinity() {
        Double[] source = {-Double.MAX_VALUE, -Double.MAX_VALUE};
        assertTrue(Double.isInfinite(Linq.asEnumerable(source).sumDouble()));
    }

    @Test
    public void NullableDoubleFromSelector() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5d),
                new NameNum<>("John", 0d),
                new NameNum<>("Bob", 8.5d)
        };
        assertEquals(18.0, Linq.asEnumerable(source).sumDouble(e -> e.num));
    }

    @Test
    public void SolitaryDecimal() {
        BigDecimal[] source = {m("20.51")};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumDecimal());
    }

    @Test
    public void DecimalNegativeOverflow() {
        BigDecimal[] source = {MAX_DECIMAL.negate(), MAX_DECIMAL.negate()};
        assertEquals(MAX_DECIMAL.negate().multiply(m("2")), Linq.asEnumerable(source).sumDecimal());
    }

    @Test
    public void DecimalFromSelector() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("20.51")),
                new NameNum<>("John", m("10")),
                new NameNum<>("Bob", m("2.33"))
        };
        assertEquals(m("32.84"), Linq.asEnumerable(source).sumDecimal(e -> e.num));
    }

    @Test
    public void SolitaryNullableDecimal() {
        BigDecimal[] source = {m("20.51")};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumDecimal());
    }

    @Test
    public void NullableDecimalAllNull() {
        assertEquals(BigDecimal.ZERO, Linq.repeat(0L, 3).select(x -> m(x)).sumDecimal());
    }

    @Test
    public void NullableDecimalNegativeOverflow() {
        BigDecimal[] source = {MAX_DECIMAL.negate(), MAX_DECIMAL.negate()};
        assertEquals(MAX_DECIMAL.negate().multiply(m("2")), Linq.asEnumerable(source).sumDecimal());
    }

    @Test
    public void NullableDecimalFromSelector() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("20.51")),
                new NameNum<>("John", BigDecimal.ZERO),
                new NameNum<>("Bob", m("2.33"))
        };
        assertEquals(m("22.84"), Linq.asEnumerable(source).sumDecimal(e -> e.num));
    }

    @Test
    public void SolitarySingle() {
        float[] source = {20.51f};
        assertEquals(Linq.asEnumerable(source).firstOrDefault(), Linq.asEnumerable(source).sumFloat());
    }

    @Test
    public void SingleToNegativeInfinity() {
        float[] source = {-Float.MAX_VALUE, -Float.MAX_VALUE};
        assertTrue(Float.isInfinite(Linq.asEnumerable(source).sumFloat()));
    }

    @Test
    public void SingleFromSelector() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5f),
                new NameNum<>("John", 10.5f),
                new NameNum<>("Bob", 3.5f)
        };
        assertEquals(23.5f, Linq.asEnumerable(source).sumFloat(e -> e.num));
    }

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


    private static class NameNum<T> extends ValueType {
        final String name;
        final T num;

        NameNum(String name, T num) {
            this.name = name;
            this.num = num;
        }
    }
}
