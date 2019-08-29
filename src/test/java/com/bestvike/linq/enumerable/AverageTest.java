package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
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
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class AverageTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.averageInt(), q.averageInt());
    }

    @Test
    void SameResultsRepeatCallsNullableLongQuery() {
        IEnumerable<Long> q = Linq.of((long) Integer.MAX_VALUE, (long) 0, (long) 255, (long) 127, (long) 128, (long) 1, (long) 33, (long) 99, null, (long) Integer.MIN_VALUE);

        assertEquals(q.averageLongNull(), q.averageLongNull());
    }

    @Test
    void NullableFoat() {
        this.NullableFoat(Linq.empty(), null);
        this.NullableFoat(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoat(Linq.of(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoat(Linq.of(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoat(Linq.of(null, null, null, null, 45f), 45f);
        this.NullableFoat(Linq.of(null, null, null, null, null), null);
    }

    private void NullableFoat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.averageFloatNull());
        assertEquals(expected, source.averageFloatNull(x -> x));
    }

    @Test
    void NullableFoatRunOnce() {
        this.NullableFoatRunOnce(Linq.empty(), null);
        this.NullableFoatRunOnce(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoatRunOnce(Linq.of(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoatRunOnce(Linq.of(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoatRunOnce(Linq.of(null, null, null, null, 45f), 45f);
        this.NullableFoatRunOnce(Linq.of(null, null, null, null, null), null);
    }

    private void NullableFoatRunOnce(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.runOnce().averageFloatNull());
        assertEquals(expected, source.runOnce().averageFloatNull(x -> x));
    }

    @Test
    void NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull(i -> i));
    }

    @Test
    void NullableFloat_NullSelector_ThrowsArgumentNullException() {
        NullableFloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().averageFloatNull(selector));
    }

    @Test
    void NullableFloat_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.of(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", null));
        Float expected = 10.5f;

        assertEquals(expected, source.averageFloatNull(e -> e.getItem2()));
    }

    @Test
    void Int_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Integer> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageInt());
        assertThrows(InvalidOperationException.class, () -> source.averageInt(i -> i));
    }

    @Test
    void Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageInt(i -> i));
    }

    @Test
    void Int_NullSelector_ThrowsArgumentNullException() {
        IntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().averageInt(selector));
    }

    @Test
    void Int() {
        this.Int(Linq.singleton(5), 5);
        this.Int(Linq.of(0, 0, 0, 0, 0), 0);
        this.Int(Linq.of(5, -10, 15, 40, 28), 15.6);
    }

    private void Int(IEnumerable<Integer> source, double expected) {
        assertEquals(expected, source.averageInt());
        assertEquals(expected, source.averageInt(x -> x));
    }

    @Test
    void IntRunOnce() {
        this.IntRunOnce(Linq.singleton(5), 5);
        this.IntRunOnce(Linq.of(0, 0, 0, 0, 0), 0);
        this.IntRunOnce(Linq.of(5, -10, 15, 40, 28), 15.6);
    }

    private void IntRunOnce(IEnumerable<Integer> source, double expected) {
        assertEquals(expected, source.runOnce().averageInt());
        assertEquals(expected, source.runOnce().averageInt(x -> x));
    }

    @Test
    void Int_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.of(Tuple.create("Tim", 10), Tuple.create("John", -10), Tuple.create("Bob", 15));
        double expected = 5;

        assertEquals(expected, source.averageInt(e -> e.getItem2()));
    }

    @Test
    void NullableInt() {
        this.NullableInt(Linq.empty(), null);
        this.NullableInt(Linq.singleton(-5), -5.0);
        this.NullableInt(Linq.of(0, 0, 0, 0, 0), 0.0);
        this.NullableInt(Linq.of(5, -10, null, null, null, 15, 40, 28, null, null), 15.6);
        this.NullableInt(Linq.of(null, null, null, null, 50), 50.0);
        this.NullableInt(Linq.of(null, null, null, null, null), null);
    }

    private void NullableInt(IEnumerable<Integer> source, Double expected) {
        assertEquals(expected, source.averageIntNull());
        assertEquals(expected, source.averageIntNull(x -> x));
    }

    @Test
    void NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull(i -> i));
    }

    @Test
    void NullableInt_NullSelector_ThrowsArgumentNullException() {
        NullableIntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().averageIntNull(selector));
    }

    @Test
    void NullableInt_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.of(Tuple.create("Tim", 10), Tuple.create("John", null), Tuple.create("Bob", 10));
        Double expected = 10d;

        assertEquals(expected, source.averageIntNull(e -> e.getItem2()));
    }

    @Test
    void Long_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Long> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageLong());
        assertThrows(InvalidOperationException.class, () -> source.averageLong(i -> i));
    }

    @Test
    void Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLong(i -> i));
    }

    @Test
    void Long_NullSelector_ThrowsArgumentNullException() {
        LongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLong(selector));
    }

    @Test
    void Long() {
        this.Long(Linq.singleton(Long.MAX_VALUE), Long.MAX_VALUE);
        this.Long(Linq.of(0L, 0L, 0L, 0L, 0L), 0);
        this.Long(Linq.of(5L, -10L, 15L, 40L, 28L), 15.6);
    }

    private void Long(IEnumerable<Long> source, double expected) {
        assertEquals(expected, source.averageLong());
        assertEquals(expected, source.averageLong(x -> x));
    }

    @Test
    void Long_FromSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.of(Tuple.create("Tim", 40L), Tuple.create("John", 50L), Tuple.create("Bob", 60L));
        double expected = 50;

        assertEquals(expected, source.averageLong(e -> e.getItem2()));
    }

    @Test
    void Long_SumTooLarge_ThrowsOverflowException() {
        IEnumerable<Long> source = Linq.of(Long.MAX_VALUE, Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> source.averageLong());
    }

    @Test
    void NullableLong() {
        this.NullableLong(Linq.empty(), null);
        this.NullableLong(Linq.singleton(Long.MAX_VALUE), (double) Long.MAX_VALUE);
        this.NullableLong(Linq.of(0L, 0L, 0L, 0L, 0L), 0.0);
        this.NullableLong(Linq.of(5L, -10L, null, null, null, 15L, 40L, 28L, null, null), 15.6);
        this.NullableLong(Linq.of(null, null, null, null, 50L), 50.0);
        this.NullableLong(Linq.of(null, null, null, null, null), null);
    }

    private void NullableLong(IEnumerable<Long> source, Double expected) {
        assertEquals(expected, source.averageLongNull());
        assertEquals(expected, source.averageLongNull(x -> x));
    }

    @Test
    void NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull(i -> i));
    }

    @Test
    void NullableLong_NullSelector_ThrowsArgumentNullException() {
        NullableLongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLongNull(selector));
    }

    @Test
    void NullableLong_WithSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.of(Tuple.create("Tim", 40L), Tuple.create("John", null), Tuple.create("Bob", 30L));
        Double expected = 35d;

        assertEquals(expected, source.averageLongNull(e -> e.getItem2()));
    }

    @Test
    void Double_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Double> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageDouble());
        assertThrows(InvalidOperationException.class, () -> source.averageDouble(i -> i));
    }

    @Test
    void Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDouble(i -> i));
    }

    @Test
    void Double_NullSelector_ThrowsArgumentNullException() {
        DoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().averageDouble(selector));
    }

    @Test
    void Average_Double() {
        this.Average_Double(Linq.singleton(Double.MAX_VALUE), Double.MAX_VALUE);
        this.Average_Double(Linq.of(0.0, 0.0, 0.0, 0.0, 0.0), 0);
        this.Average_Double(Linq.of(5.5, -10d, 15.5, 40.5, 28.5), 16);
        this.Average_Double(Linq.of(5.58, Double.NaN, 30d, 4.55, 19.38), Double.NaN);
    }

    private void Average_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.averageDouble());
        assertEquals(expected, source.averageDouble(x -> x));
    }

    @Test
    void Double_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.of(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", 3.0));
        double expected = 8.0;

        assertEquals(expected, source.averageDouble(e -> e.getItem2()));
    }

    @Test
    void NullableDouble() {
        this.NullableDouble(Linq.empty(), null);
        this.NullableDouble(Linq.singleton(Double.MIN_VALUE), Double.MIN_VALUE);
        this.NullableDouble(Linq.of(0d, 0d, 0d, 0d, 0d), 0.0);
        this.NullableDouble(Linq.of(5.5d, 0d, null, null, null, 15.5d, 40.5d, null, null, -23.5d), 7.6d);
        this.NullableDouble(Linq.of(null, null, null, null, 45d), 45.0);
        this.NullableDouble(Linq.of(-23.5, 0d, Double.NaN, 54.3, 0.56), Double.NaN);
        this.NullableDouble(Linq.of(null, null, null, null, null), null);
    }

    private void NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.averageDoubleNull());
        assertEquals(expected, source.averageDoubleNull(x -> x));
    }

    @Test
    void NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull(i -> i));
    }

    @Test
    void NullableDouble_NullSelector_ThrowsArgumentNullException() {
        NullableDoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().averageDoubleNull(selector));
    }

    @Test
    void NullableDouble_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.of(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", null));
        Double expected = 10.5;

        assertEquals(expected, source.averageDoubleNull(e -> e.getItem2()));
    }

    @Test
    void Decimal_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<BigDecimal> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageDecimal());
        assertThrows(InvalidOperationException.class, () -> source.averageDecimal(i -> i));
    }

    @Test
    void Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimal(i -> i));
    }

    @Test
    void Decimal_NullSelector_ThrowsArgumentNullException() {
        DecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().averageDecimal(selector));
    }

    @Test
    void Decimal() {
        this.Decimal(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.Decimal(Linq.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.Decimal(Linq.of(m("5.5"), m("-10"), m("15.5"), m("40.5"), m("28.5")), m("16.0"));
    }

    private void Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.averageDecimal());
        assertEquals(expected, source.averageDecimal(x -> x));
    }

    @Test
    void Decimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.of(Tuple.create("Tim", m("5.5")), Tuple.create("John", m("15.5")), Tuple.create("Bob", m("3.0")));
        BigDecimal expected = m("8.0");

        assertEquals(expected, source.averageDecimal(e -> e.getItem2()));
    }

    @Test
    void NullableDecimal() {
        this.NullableDecimal(Linq.empty(), null);
        this.NullableDecimal(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimal(Linq.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimal(Linq.of(m("5.5"), BigDecimal.ZERO, null, null, null, m("15.5"), m("40.5"), null, null, m("-23.5")), m("7.6"));
        this.NullableDecimal(Linq.of(null, null, null, null, m("45")), m("45"));
        this.NullableDecimal(Linq.of(null, null, null, null, null), null);
    }

    private void NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.averageDecimalNull());
        assertEquals(expected, source.averageDecimalNull(x -> x));
    }

    @Test
    void NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull(i -> i));
    }

    @Test
    void NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        NullableDecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().averageDecimalNull(selector));
    }

    @Test
    void NullableDecimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.of(Tuple.create("Tim", m("5.5")), Tuple.create("John", m("15.5")), Tuple.create("Bob", null));
        BigDecimal expected = m("10.5");

        assertEquals(expected, source.averageDecimalNull(e -> e.getItem2()));
    }

    @Test
    void NullableDecimal_SumTooLarge_ThrowsOverflowException() {
        IEnumerable<BigDecimal> source = Linq.of(MAX_DECIMAL, MAX_DECIMAL);

        assertEquals(MAX_DECIMAL, source.averageDecimal());
    }

    @Test
    void Float_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Float> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageFloat());
        assertThrows(InvalidOperationException.class, () -> source.averageFloat(i -> i));
    }

    @Test
    void Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloat(i -> i));
    }

    @Test
    void Float_NullSelector_ThrowsArgumentNullException() {
        FloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().averageFloat(selector));
    }

    @Test
    void Float_TestData() {
        this.Float(Linq.singleton(Float.MAX_VALUE), Float.MAX_VALUE);
        this.Float(Linq.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), 0f);
        this.Float(Linq.of(5.5f, -10f, 15.5f, 40.5f, 28.5f), 16f);
    }

    private void Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.averageFloat());
        assertEquals(expected, source.averageFloat(x -> x));
    }

    @Test
    void Float_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.of(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", 3.0f));
        float expected = 8.0f;

        assertEquals(expected, source.averageFloat(e -> e.getItem2()));
    }

    @Test
    void testAverageInt() {
        Integer[] numbers = {0, 3, 3};
        assertEquals(2d, Linq.of(numbers).averageInt());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageInt());
    }

    @Test
    void testAverageIntNull() {
        Integer[] numbers = {null, 0, 3, 3};
        assertEquals(2d, Linq.of(numbers).averageIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageIntNull());
    }

    @Test
    void testAverageLong() {
        Long[] numbers = {0L, 3L, 3L};
        assertEquals(2d, Linq.of(numbers).averageLong());

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageLong());
    }

    @Test
    void testAverageLongNull() {
        Long[] numbers = {null, 0L, 3L, 3L};
        assertEquals(2d, Linq.of(numbers).averageLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageLongNull());
    }

    @Test
    void testAverageFloat() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).averageFloat());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageFloat());
    }

    @Test
    void testAverageFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).averageFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageFloatNull());
    }

    @Test
    void testAverageDouble() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).averageDouble());

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageDouble());
    }

    @Test
    void testAverageDoubleNull() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).averageDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageDoubleNull());
    }

    @Test
    void testAverageDecimal() {
        BigDecimal[] numbers = {m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.of(numbers).averageDecimal());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageDecimal());
    }

    @Test
    void testAverageDecimalNull() {
        BigDecimal[] numbers = {null, m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.of(numbers).averageDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageDecimalNull());
    }

    @Test
    void testAverageIntWithSelector() {
        Integer[] numbers = {0, 3, 3};
        assertEquals(2d, Linq.of(numbers).averageInt(n -> n));

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageInt(n -> n));
    }

    @Test
    void testAverageIntNullWithSelector() {
        Integer[] numbers = {null, 0, 3, 3};
        assertEquals(2d, Linq.of(numbers).averageIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageIntNull(n -> n));
    }

    @Test
    void testAverageLongWithSelector() {
        Long[] numbers = {0L, 3L, 3L};
        assertEquals(2d, Linq.of(numbers).averageLong(n -> n));

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageLong(n -> n));
    }

    @Test
    void testAverageLongNullWithSelector() {
        Long[] numbers = {null, 0L, 3L, 3L};
        assertEquals(2d, Linq.of(numbers).averageLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageLongNull(n -> n));
    }

    @Test
    void testAverageFloatWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).averageFloat(n -> n));

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageFloat(n -> n));
    }

    @Test
    void testAverageFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.of(numbers).averageFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageFloatNull(n -> n));
    }

    @Test
    void testAverageDoubleWithSelector() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).averageDouble(n -> n));

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageDouble(n -> n));
    }

    @Test
    void testAverageDoubleNullWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.of(numbers).averageDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageDoubleNull(n -> n));
    }

    @Test
    void testAverageDecimalWithSelector() {
        BigDecimal[] numbers = {m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.of(numbers).averageDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.of(numbers2).averageDecimal(n -> n));
    }

    @Test
    void testAverageDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.of(numbers).averageDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.of(numbers2).averageDecimalNull(n -> n));
    }
}
