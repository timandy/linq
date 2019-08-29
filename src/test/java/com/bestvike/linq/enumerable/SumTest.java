package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.DecimalFunc1;
import com.bestvike.function.DoubleFunc1;
import com.bestvike.function.FloatFunc1;
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
import org.junit.jupiter.api.Test;

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
        assertThrows(NullPointerException.class, () -> sourceNullableInt.sumIntNull());
        assertThrows(NullPointerException.class, () -> sourceNullableInt.sumIntNull(x -> x));
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
        assertThrows(NullPointerException.class, () -> sourceNullableLong.sumLongNull());
        assertThrows(NullPointerException.class, () -> sourceNullableLong.sumLongNull(x -> x));
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
        assertThrows(NullPointerException.class, () -> sourceNullableFloat.sumFloatNull());
        assertThrows(NullPointerException.class, () -> sourceNullableFloat.sumFloatNull(x -> x));
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
        assertThrows(NullPointerException.class, () -> sourceNullableDouble.sumDoubleNull());
        assertThrows(NullPointerException.class, () -> sourceNullableDouble.sumDoubleNull(x -> x));
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
        assertThrows(NullPointerException.class, () -> sourceNullableDecimal.sumDecimalNull());
        assertThrows(NullPointerException.class, () -> sourceNullableDecimal.sumDecimalNull(x -> x));
    }

    @Test
    public void SumOfInt_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> sourceInt = Linq.empty();
        IntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceInt.sumInt(selector));
    }

    @Test
    public void SumOfNullableOfInt_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> sourceNullableInt = Linq.empty();
        NullableIntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableInt.sumIntNull(selector));
    }

    @Test
    public void SumOfLong_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Long> sourceLong = Linq.empty();
        LongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceLong.sumLong(selector));
    }

    @Test
    public void SumOfNullableOfLong_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Long> sourceNullableLong = Linq.empty();
        NullableLongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableLong.sumLongNull(selector));
    }

    @Test
    public void SumOfFloat_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceFloat = Linq.empty();
        FloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceFloat.sumFloat(selector));
    }

    @Test
    public void SumOfNullableOfFloat_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Float> sourceNullableFloat = Linq.empty();
        NullableFloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableFloat.sumFloatNull(selector));
    }

    @Test
    public void SumOfDouble_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceDouble = Linq.empty();
        DoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceDouble.sumDouble(selector));
    }

    @Test
    public void SumOfNullableOfDouble_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Double> sourceNullableDouble = Linq.empty();
        NullableDoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableDouble.sumDoubleNull(selector));
    }

    @Test
    public void SumOfDecimal_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.empty();
        DecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceDecimal.sumDecimal(selector));
    }

    @Test
    public void SumOfNullableOfDecimal_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.empty();
        NullableDecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> sourceNullableDecimal.sumDecimalNull(selector));
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
        assertEquals(0, sourceNullableInt.sumIntNull());
        assertEquals(0, sourceNullableInt.sumIntNull(x -> x));
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
        assertEquals(0L, sourceNullableLong.sumLongNull());
        assertEquals(0L, sourceNullableLong.sumLongNull(x -> x));
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
        assertEquals(0f, sourceNullableFloat.sumFloatNull());
        assertEquals(0f, sourceNullableFloat.sumFloatNull(x -> x));
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
        assertEquals(0d, sourceNullableDouble.sumDoubleNull());
        assertEquals(0d, sourceNullableDouble.sumDoubleNull(x -> x));
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
        assertEquals(BigDecimal.ZERO, sourceNullableDecimal.sumDecimalNull());
        assertEquals(BigDecimal.ZERO, sourceNullableDecimal.sumDecimalNull(x -> x));
    }

    @Test
    public void SumOfInt_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Integer> sourceInt = Linq.of(new int[]{1, -2, 3, -4});
        assertEquals(-2, sourceInt.sumInt());
        assertEquals(-2, sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Integer> sourceNullableInt = Linq.of(1, -2, null, 3, -4, null);
        assertEquals(-2, sourceNullableInt.sumIntNull());
        assertEquals(-2, sourceNullableInt.sumIntNull(x -> x));
    }

    @Test
    public void SumOfLong_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Long> sourceLong = Linq.of(new long[]{1L, -2L, 3L, -4L});
        assertEquals(-2L, sourceLong.sumLong());
        assertEquals(-2L, sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Long> sourceNullableLong = Linq.of(1L, -2L, null, 3L, -4L, null);
        assertEquals(-2L, sourceNullableLong.sumLongNull());
        assertEquals(-2L, sourceNullableLong.sumLongNull(x -> x));
    }

    @Test
    public void SumOfFloat_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Float> sourceFloat = Linq.of(new float[]{1f, 0.5f, -1f, 0.5f});
        assertEquals(1f, sourceFloat.sumFloat());
        assertEquals(1f, sourceFloat.sumFloat(x -> x));
    }

    @Test
    public void SumOfNullableOfFloat_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Float> sourceNullableFloat = Linq.of(1f, 0.5f, null, -1f, 0.5f, null);
        assertEquals(1f, sourceNullableFloat.sumFloatNull());
        assertEquals(1f, sourceNullableFloat.sumFloatNull(x -> x));
    }

    @Test
    public void SumOfDouble_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Double> sourceDouble = Linq.of(new double[]{1d, 0.5d, -1d, 0.5d});
        assertEquals(1d, sourceDouble.sumDouble());
        assertEquals(1d, sourceDouble.sumDouble(x -> x));
    }

    @Test
    public void SumOfNullableOfDouble_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<Double> sourceNullableDouble = Linq.of(1d, 0.5d, null, -1d, 0.5d, null);
        assertEquals(1d, sourceNullableDouble.sumDoubleNull());
        assertEquals(1d, sourceNullableDouble.sumDoubleNull(x -> x));
    }

    @Test
    public void SumOfDecimal_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.of(m("1"), m("0.5"), m("-1"), m("0.5"));
        assertEquals(m("1"), sourceDecimal.sumDecimal());
        assertEquals(m("1"), sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceIsNotEmpty_ProperSumReturned() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.of(m("1"), m("0.5"), null, m("-1"), m("0.5"), null);
        assertEquals(m("1"), sourceNullableDecimal.sumDecimalNull());
        assertEquals(m("1"), sourceNullableDecimal.sumDecimalNull(x -> x));
    }

    @Test
    public void SumOfInt_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Integer> sourceInt = Linq.of(new int[]{Integer.MAX_VALUE, 1});
        assertThrows(ArithmeticException.class, () -> sourceInt.sumInt());
        assertThrows(ArithmeticException.class, () -> sourceInt.sumInt(x -> x));
    }

    @Test
    public void SumOfNullableOfInt_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Integer> sourceNullableInt = Linq.of(Integer.MAX_VALUE, null, 1);
        assertThrows(ArithmeticException.class, () -> sourceNullableInt.sumIntNull());
        assertThrows(ArithmeticException.class, () -> sourceNullableInt.sumIntNull(x -> x));
    }

    @Test
    public void SumOfLong_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Long> sourceLong = Linq.of(new long[]{Long.MAX_VALUE, 1L});
        assertThrows(ArithmeticException.class, () -> sourceLong.sumLong());
        assertThrows(ArithmeticException.class, () -> sourceLong.sumLong(x -> x));
    }

    @Test
    public void SumOfNullableOfLong_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<Long> sourceNullableLong = Linq.of(Long.MAX_VALUE, null, 1L);
        assertThrows(ArithmeticException.class, () -> sourceNullableLong.sumLongNull());
        assertThrows(ArithmeticException.class, () -> sourceNullableLong.sumLongNull(x -> x));
    }

    @Test
    public void SumOfFloat_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Float> sourceFloat = Linq.of(new float[]{Float.MAX_VALUE, Float.MAX_VALUE});
        assertTrue(Float.isInfinite(sourceFloat.sumFloat()));
        assertTrue(Float.isInfinite(sourceFloat.sumFloat(x -> x)));
    }

    @Test
    public void SumOfNullableOfFloat_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Float> sourceNullableFloat = Linq.of(Float.MAX_VALUE, null, Float.MAX_VALUE);
        assertTrue(Float.isInfinite(sourceNullableFloat.sumFloatNull()));
        assertTrue(Float.isInfinite(sourceNullableFloat.sumFloatNull(x -> x)));
    }

    @Test
    public void SumOfDouble_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Double> sourceDouble = Linq.of(new double[]{Double.MAX_VALUE, Double.MAX_VALUE});
        assertTrue(Double.isInfinite(sourceDouble.sumDouble()));
        assertTrue(Double.isInfinite(sourceDouble.sumDouble(x -> x)));
    }

    @Test
    public void SumOfNullableOfDouble_SourceSumsToOverflow_InfinityReturned() {
        IEnumerable<Double> sourceNullableDouble = Linq.of(Double.MAX_VALUE, null, Double.MAX_VALUE);
        assertTrue(Double.isInfinite(sourceNullableDouble.sumDoubleNull()));
        assertTrue(Double.isInfinite(sourceNullableDouble.sumDoubleNull(x -> x)));
    }

    @Test
    public void SumOfDecimal_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<BigDecimal> sourceDecimal = Linq.of(MAX_DECIMAL, m("1"));
        assertEquals(MAX_DECIMAL.add(m("1")), sourceDecimal.sumDecimal());
        assertEquals(MAX_DECIMAL.add(m("1")), sourceDecimal.sumDecimal(x -> x));
    }

    @Test
    public void SumOfNullableOfDecimal_SourceSumsToOverflow_OverflowExceptionThrown() {
        IEnumerable<BigDecimal> sourceNullableDecimal = Linq.of(MAX_DECIMAL, null, m("1"));
        assertEquals(MAX_DECIMAL.add(m("1")), sourceNullableDecimal.sumDecimalNull());
        assertEquals(MAX_DECIMAL.add(m("1")), sourceNullableDecimal.sumDecimalNull(x -> x));
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, null, -777, 1, 2, -12345).where(x -> x != null && x > Integer.MIN_VALUE);

        assertEquals(q.sumInt(), q.sumInt());
    }

    @Test
    public void SolitaryNullableSingle() {
        Float[] source = {20.51f};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumFloatNull());
    }

    @Test
    public void NaNFromSingles() {
        Float[] source = {20.45f, 0f, -10.55f, Float.NaN};
        assertTrue(Float.isNaN(Linq.of(source).sumFloatNull()));
    }

    @Test
    public void NullableSingleAllNull() {
        assertEquals(0f, Linq.repeat(0f, 4).sumFloatNull());
    }

    @Test
    public void NullableSingleToNegativeInfinity() {
        Float[] source = {-Float.MAX_VALUE, -Float.MAX_VALUE};
        assertTrue(Float.isInfinite(Linq.of(source).sumFloatNull()));
    }

    @Test
    public void NullableSingleFromSelector() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5f),
                new NameNum<>("John", 0f),
                new NameNum<>("Bob", 8.5f)
        };
        assertEquals(18.0f, Linq.of(source).sumFloatNull(e -> e.num));
    }

    @Test
    public void SolitaryInt32() {
        int[] source = {20};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumInt());
    }

    @Test
    public void OverflowInt32Negative() {
        int[] source = {-Integer.MAX_VALUE, 0, -5, -20};
        assertThrows(ArithmeticException.class, () -> Linq.of(source).sumInt());
    }

    @Test
    public void Int32FromSelector() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", 50),
                new NameNum<>("Bob", -30)
        };
        assertEquals(30, Linq.of(source).sumInt(e -> e.num));
    }

    @Test
    public void SolitaryNullableInt32() {
        Integer[] source = {-9};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumIntNull());
    }

    @Test
    public void NullableInt32AllNull() {
        assertEquals(0, Linq.repeat(null, 5).sumIntNull());
    }

    @Test
    public void NullableInt32NegativeOverflow() {
        Integer[] source = {-Integer.MAX_VALUE, 0, -5, null, null, -20};
        assertThrows(ArithmeticException.class, () -> Linq.of(source).sumIntNull());
    }

    @Test
    public void NullableInt32FromSelector() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", null),
                new NameNum<>("Bob", -30)
        };
        assertEquals(-20, Linq.of(source).sumIntNull(e -> e.num));
    }

    @Test
    public void RunOnce() {
        NameNum<Integer>[] source = new NameNum[]{
                new NameNum<>("Tim", 10),
                new NameNum<>("John", 0),
                new NameNum<>("Bob", -30)
        };
        assertEquals(-20, Linq.of(source).runOnce().sumInt(e -> e.num));
    }

    @Test
    public void SolitaryInt64() {
        long[] source = {Integer.MAX_VALUE + 20L};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumLong());
    }

    @Test
    public void NullableInt64NegativeOverflow() {
        long[] source = {-Long.MAX_VALUE, 0, -5, 20, -16};
        assertThrows(ArithmeticException.class, () -> Linq.of(source).sumLong());
    }

    @Test
    public void Int64FromSelector() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", 10L),
                new NameNum<>("John", (long) Integer.MAX_VALUE),
                new NameNum<>("Bob", 40L)
        };

        assertEquals(Integer.MAX_VALUE + 50L, Linq.of(source).sumLong(e -> e.num));
    }

    @Test
    public void SolitaryNullableInt64() {
        Long[] source = {-Integer.MAX_VALUE - 20L};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumLongNull());
    }

    @Test
    public void NullableInt64AllNull() {
        assertEquals(0, Linq.repeat(null, 5).sumLongNull());
    }

    @Test
    public void Int64NegativeOverflow() {
        Long[] source = {-Long.MAX_VALUE, 0L, -5L, -20L, null, null};
        assertThrows(ArithmeticException.class, () -> Linq.of(source).sumLongNull());
    }

    @Test
    public void NullableInt64FromSelector() {
        NameNum<Long>[] source = new NameNum[]{
                new NameNum<>("Tim", 10L),
                new NameNum<>("John", (long) Integer.MAX_VALUE),
                new NameNum<>("Bob", null)
        };

        assertEquals(Integer.MAX_VALUE + 10L, Linq.of(source).sumLongNull(e -> e.num));
    }

    @Test
    public void SolitaryDouble() {
        double[] source = {20.51};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumDouble());
    }

    @Test
    public void DoubleWithNaN() {
        double[] source = {20.45, 0, -10.55, Double.NaN};
        assertTrue(Double.isNaN(Linq.of(source).sumDouble()));
    }

    @Test
    public void DoubleToNegativeInfinity() {
        double[] source = {-Double.MAX_VALUE, -Double.MAX_VALUE};
        assertTrue(Double.isInfinite(Linq.of(source).sumDouble()));
    }

    @Test
    public void DoubleFromSelector() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5d),
                new NameNum<>("John", 10.5d),
                new NameNum<>("Bob", 3.5d)
        };

        assertEquals(23.5, Linq.of(source).sumDouble(e -> e.num));
    }

    @Test
    public void SolitaryNullableDouble() {
        Double[] source = {20.51};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumDoubleNull());
    }

    @Test
    public void NullableDoubleAllNull() {
        assertEquals(0, Linq.repeat(null, 4).sumDoubleNull());
    }

    @Test
    public void NullableDoubleToNegativeInfinity() {
        Double[] source = {-Double.MAX_VALUE, -Double.MAX_VALUE};
        assertTrue(Double.isInfinite(Linq.of(source).sumDoubleNull()));
    }

    @Test
    public void NullableDoubleFromSelector() {
        NameNum<Double>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5d),
                new NameNum<>("John", null),
                new NameNum<>("Bob", 8.5d)
        };
        assertEquals(18.0, Linq.of(source).sumDoubleNull(e -> e.num));
    }

    @Test
    public void SolitaryDecimal() {
        BigDecimal[] source = {m("20.51")};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumDecimal());
    }

    @Test
    public void DecimalNegativeOverflow() {
        BigDecimal[] source = {MAX_DECIMAL.negate(), MAX_DECIMAL.negate()};
        assertEquals(MAX_DECIMAL.negate().multiply(m("2")), Linq.of(source).sumDecimal());
    }

    @Test
    public void DecimalFromSelector() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("20.51")),
                new NameNum<>("John", m("10")),
                new NameNum<>("Bob", m("2.33"))
        };
        assertEquals(m("32.84"), Linq.of(source).sumDecimal(e -> e.num));
    }

    @Test
    public void SolitaryNullableDecimal() {
        BigDecimal[] source = {m("20.51")};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumDecimalNull());
    }

    @Test
    public void NullableDecimalAllNull() {
        assertEquals(BigDecimal.ZERO, Linq.repeat(null, 3).sumDecimalNull());
    }

    @Test
    public void NullableDecimalNegativeOverflow() {
        BigDecimal[] source = {MAX_DECIMAL.negate(), MAX_DECIMAL.negate()};
        assertEquals(MAX_DECIMAL.negate().multiply(m("2")), Linq.of(source).sumDecimalNull());
    }

    @Test
    public void NullableDecimalFromSelector() {
        NameNum<BigDecimal>[] source = new NameNum[]{
                new NameNum<>("Tim", m("20.51")),
                new NameNum<>("John", null),
                new NameNum<>("Bob", m("2.33"))
        };
        assertEquals(m("22.84"), Linq.of(source).sumDecimalNull(e -> e.num));
    }

    @Test
    public void SolitarySingle() {
        float[] source = {20.51f};
        assertEquals(Linq.of(source).firstOrDefault(), Linq.of(source).sumFloat());
    }

    @Test
    public void SingleToNegativeInfinity() {
        float[] source = {-Float.MAX_VALUE, -Float.MAX_VALUE};
        assertTrue(Float.isInfinite(Linq.of(source).sumFloat()));
    }

    @Test
    public void SingleFromSelector() {
        NameNum<Float>[] source = new NameNum[]{
                new NameNum<>("Tim", 9.5f),
                new NameNum<>("John", 10.5f),
                new NameNum<>("Bob", 3.5f)
        };
        assertEquals(23.5f, Linq.of(source).sumFloat(e -> e.num));
    }

    @Test
    public void testSumInt() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(5, Linq.of(numbers).sumIntNull());

        Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        assertEquals(Integer.MAX_VALUE, Linq.of(numbers2).sumIntNull());

        Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers3).sumInt());
    }

    @Test
    public void testSumLong() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(5, Linq.of(numbers).sumLongNull());

        Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        assertEquals(Long.MAX_VALUE, Linq.of(numbers2).sumLongNull());

        Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers3).sumLong());
    }

    @Test
    public void testSumFloat() {
        Float[] numbers = {null, 0f, 2f, 3f};
        assertEquals(5f, Linq.of(numbers).sumFloatNull());

        Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        assertEquals(Float.MAX_VALUE, Linq.of(numbers2).sumFloatNull());
    }

    @Test
    public void testSumDouble() {
        Double[] numbers = {null, 0d, 2d, 3d};
        assertEquals(5d, Linq.of(numbers).sumDoubleNull());

        Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        assertEquals(Double.MAX_VALUE, Linq.of(numbers2).sumDoubleNull());
    }

    @Test
    public void testSumDecimal() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("5"), Linq.of(numbers).sumDecimalNull());

        BigDecimal[] numbers2 = {null, BigDecimal.ZERO, m("2"), m("3")};
        assertEquals(m("5"), Linq.of(numbers2).sumDecimalNull());
    }

    @Test
    public void testSumIntWithSelector() {
        Integer[] numbers = {null, 0, 2, 3};
        assertEquals(5, Linq.of(numbers).sumIntNull(n -> n));

        Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        assertEquals(Integer.MAX_VALUE, Linq.of(numbers2).sumIntNull(n -> n));

        Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers3).sumInt(n -> n));
    }

    @Test
    public void testSumLongWithSelector() {
        Long[] numbers = {null, 0L, 2L, 3L};
        assertEquals(5, Linq.of(numbers).sumLongNull(n -> n));

        Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        assertEquals(Long.MAX_VALUE, Linq.of(numbers2).sumLongNull(n -> n));

        Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers3).sumLong(n -> n));
    }

    @Test
    public void testSumFloatWithSelector() {
        Float[] numbers = {null, 0f, 2f, 3f};
        assertEquals(5f, Linq.of(numbers).sumFloatNull(n -> n));

        Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        assertEquals(Float.MAX_VALUE, Linq.of(numbers2).sumFloatNull(n -> n));
    }

    @Test
    public void testSumDoubleWithSelector() {
        Double[] numbers = {null, 0d, 2d, 3d};
        assertEquals(5d, Linq.of(numbers).sumDoubleNull(n -> n));

        Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        assertEquals(Double.MAX_VALUE, Linq.of(numbers2).sumDoubleNull(n -> n));
    }

    @Test
    public void testSumDecimalWithSelector() {
        BigDecimal[] numbers = {null, m("0"), m("2"), m("3")};
        assertEquals(m("5"), Linq.of(numbers).sumDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null, BigDecimal.ZERO, m("2"), m("3")};
        assertEquals(m("5"), Linq.of(numbers2).sumDecimalNull(n -> n));
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
