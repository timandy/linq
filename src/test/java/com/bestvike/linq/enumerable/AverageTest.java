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
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AverageTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.averageInt(), q.averageInt());
    }

    @Test
    public void SameResultsRepeatCallsNullableLongQuery() {
        IEnumerable<Long> q = Linq.asEnumerable((long) Integer.MAX_VALUE, (long) 0, (long) 255, (long) 127, (long) 128, (long) 1, (long) 33, (long) 99, null, (long) Integer.MIN_VALUE);

        assertEquals(q.averageLongNull(), q.averageLongNull());
    }

    @Test
    public void NullableFoat() {
        this.NullableFoat(Linq.empty(), null);
        this.NullableFoat(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoat(Linq.asEnumerable(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoat(Linq.asEnumerable(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoat(Linq.asEnumerable(null, null, null, null, 45f), 45f);
        this.NullableFoat(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableFoat(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.averageFloatNull());
        assertEquals(expected, source.averageFloatNull(x -> x));
    }

    @Test
    public void NullableFoatRunOnce() {
        this.NullableFoatRunOnce(Linq.empty(), null);
        this.NullableFoatRunOnce(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoatRunOnce(Linq.asEnumerable(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoatRunOnce(Linq.asEnumerable(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoatRunOnce(Linq.asEnumerable(null, null, null, null, 45f), 45f);
        this.NullableFoatRunOnce(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableFoatRunOnce(IEnumerable<Float> source, Float expected) {
        assertEquals(expected, source.runOnce().averageFloatNull());
        assertEquals(expected, source.runOnce().averageFloatNull(x -> x));
    }

    @Test
    public void NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull(i -> i));
    }

    @Test
    public void NullableFloat_NullSelector_ThrowsArgumentNullException() {
        NullableFloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().averageFloatNull(selector));
    }

    @Test
    public void NullableFloat_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", null));
        Float expected = 10.5f;

        assertEquals(expected, source.averageFloatNull(e -> e.getItem2()));
    }

    @Test
    public void Int_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Integer> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageInt());
        assertThrows(InvalidOperationException.class, () -> source.averageInt(i -> i));
    }

    @Test
    public void Int_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageInt());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageInt(i -> i));
    }

    @Test
    public void Int_NullSelector_ThrowsArgumentNullException() {
        IntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().averageInt(selector));
    }

    @Test
    public void Int() {
        this.Int(Linq.singleton(5), 5);
        this.Int(Linq.asEnumerable(0, 0, 0, 0, 0), 0);
        this.Int(Linq.asEnumerable(5, -10, 15, 40, 28), 15.6);
    }

    private void Int(IEnumerable<Integer> source, double expected) {
        assertEquals(expected, source.averageInt());
        assertEquals(expected, source.averageInt(x -> x));
    }

    @Test
    public void IntRunOnce() {
        this.IntRunOnce(Linq.singleton(5), 5);
        this.IntRunOnce(Linq.asEnumerable(0, 0, 0, 0, 0), 0);
        this.IntRunOnce(Linq.asEnumerable(5, -10, 15, 40, 28), 15.6);
    }

    private void IntRunOnce(IEnumerable<Integer> source, double expected) {
        assertEquals(expected, source.runOnce().averageInt());
        assertEquals(expected, source.runOnce().averageInt(x -> x));
    }

    @Test
    public void Int_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.asEnumerable(Tuple.create("Tim", 10), Tuple.create("John", -10), Tuple.create("Bob", 15));
        double expected = 5;

        assertEquals(expected, source.averageInt(e -> e.getItem2()));
    }

    @Test
    public void NullableInt() {
        this.NullableInt(Linq.empty(), null);
        this.NullableInt(Linq.singleton(-5), -5.0);
        this.NullableInt(Linq.asEnumerable(0, 0, 0, 0, 0), 0.0);
        this.NullableInt(Linq.asEnumerable(5, -10, null, null, null, 15, 40, 28, null, null), 15.6);
        this.NullableInt(Linq.asEnumerable(null, null, null, null, 50), 50.0);
        this.NullableInt(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableInt(IEnumerable<Integer> source, Double expected) {
        assertEquals(expected, source.averageIntNull());
        assertEquals(expected, source.averageIntNull(x -> x));
    }

    @Test
    public void NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull(i -> i));
    }

    @Test
    public void NullableInt_NullSelector_ThrowsArgumentNullException() {
        NullableIntFunc1<Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().averageIntNull(selector));
    }

    @Test
    public void NullableInt_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.asEnumerable(Tuple.create("Tim", 10), Tuple.create("John", null), Tuple.create("Bob", 10));
        Double expected = 10d;

        assertEquals(expected, source.averageIntNull(e -> e.getItem2()));
    }

    @Test
    public void Long_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Long> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageLong());
        assertThrows(InvalidOperationException.class, () -> source.averageLong(i -> i));
    }

    @Test
    public void Long_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLong());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLong(i -> i));
    }

    @Test
    public void Long_NullSelector_ThrowsArgumentNullException() {
        LongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLong(selector));
    }

    @Test
    public void Long() {
        this.Long(Linq.singleton(Long.MAX_VALUE), Long.MAX_VALUE);
        this.Long(Linq.asEnumerable(0L, 0L, 0L, 0L, 0L), 0);
        this.Long(Linq.asEnumerable(5L, -10L, 15L, 40L, 28L), 15.6);
    }

    private void Long(IEnumerable<Long> source, double expected) {
        assertEquals(expected, source.averageLong());
        assertEquals(expected, source.averageLong(x -> x));
    }

    @Test
    public void Long_FromSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.asEnumerable(Tuple.create("Tim", 40L), Tuple.create("John", 50L), Tuple.create("Bob", 60L));
        double expected = 50;

        assertEquals(expected, source.averageLong(e -> e.getItem2()));
    }

    @Test
    public void Long_SumTooLarge_ThrowsOverflowException() {
        IEnumerable<Long> source = Linq.asEnumerable(Long.MAX_VALUE, Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> source.averageLong());
    }

    @Test
    public void NullableLong() {
        this.NullableLong(Linq.empty(), null);
        this.NullableLong(Linq.singleton(Long.MAX_VALUE), (double) Long.MAX_VALUE);
        this.NullableLong(Linq.asEnumerable(0L, 0L, 0L, 0L, 0L), 0.0);
        this.NullableLong(Linq.asEnumerable(5L, -10L, null, null, null, 15L, 40L, 28L, null, null), 15.6);
        this.NullableLong(Linq.asEnumerable(null, null, null, null, 50L), 50.0);
        this.NullableLong(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableLong(IEnumerable<Long> source, Double expected) {
        assertEquals(expected, source.averageLongNull());
        assertEquals(expected, source.averageLongNull(x -> x));
    }

    @Test
    public void NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull(i -> i));
    }

    @Test
    public void NullableLong_NullSelector_ThrowsArgumentNullException() {
        NullableLongFunc1<Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLongNull(selector));
    }

    @Test
    public void NullableLong_WithSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.asEnumerable(Tuple.create("Tim", 40L), Tuple.create("John", null), Tuple.create("Bob", 30L));
        Double expected = 35d;

        assertEquals(expected, source.averageLongNull(e -> e.getItem2()));
    }

    @Test
    public void Double_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Double> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageDouble());
        assertThrows(InvalidOperationException.class, () -> source.averageDouble(i -> i));
    }

    @Test
    public void Double_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDouble());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDouble(i -> i));
    }

    @Test
    public void Double_NullSelector_ThrowsArgumentNullException() {
        DoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().averageDouble(selector));
    }

    @Test
    public void Average_Double() {
        this.Average_Double(Linq.singleton(Double.MAX_VALUE), Double.MAX_VALUE);
        this.Average_Double(Linq.asEnumerable(0.0, 0.0, 0.0, 0.0, 0.0), 0);
        this.Average_Double(Linq.asEnumerable(5.5, -10d, 15.5, 40.5, 28.5), 16);
        this.Average_Double(Linq.asEnumerable(5.58, Double.NaN, 30d, 4.55, 19.38), Double.NaN);
    }

    private void Average_Double(IEnumerable<Double> source, double expected) {
        assertEquals(expected, source.averageDouble());
        assertEquals(expected, source.averageDouble(x -> x));
    }

    @Test
    public void Double_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", 3.0));
        double expected = 8.0;

        assertEquals(expected, source.averageDouble(e -> e.getItem2()));
    }

    @Test
    public void NullableDouble() {
        this.NullableDouble(Linq.empty(), null);
        this.NullableDouble(Linq.singleton(Double.MIN_VALUE), Double.MIN_VALUE);
        this.NullableDouble(Linq.asEnumerable(0d, 0d, 0d, 0d, 0d), 0.0);
        this.NullableDouble(Linq.asEnumerable(5.5d, 0d, null, null, null, 15.5d, 40.5d, null, null, -23.5d), 7.6d);
        this.NullableDouble(Linq.asEnumerable(null, null, null, null, 45d), 45.0);
        this.NullableDouble(Linq.asEnumerable(-23.5, 0d, Double.NaN, 54.3, 0.56), Double.NaN);
        this.NullableDouble(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableDouble(IEnumerable<Double> source, Double expected) {
        assertEquals(expected, source.averageDoubleNull());
        assertEquals(expected, source.averageDoubleNull(x -> x));
    }

    @Test
    public void NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull(i -> i));
    }

    @Test
    public void NullableDouble_NullSelector_ThrowsArgumentNullException() {
        NullableDoubleFunc1<Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().averageDoubleNull(selector));
    }

    @Test
    public void NullableDouble_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", null));
        Double expected = 10.5;

        assertEquals(expected, source.averageDoubleNull(e -> e.getItem2()));
    }

    @Test
    public void Decimal_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<BigDecimal> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageDecimal());
        assertThrows(InvalidOperationException.class, () -> source.averageDecimal(i -> i));
    }

    @Test
    public void Decimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimal());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimal(i -> i));
    }

    @Test
    public void Decimal_NullSelector_ThrowsArgumentNullException() {
        DecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().averageDecimal(selector));
    }

    @Test
    public void Decimal() {
        this.Decimal(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.Decimal(Linq.asEnumerable(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.Decimal(Linq.asEnumerable(m("5.5"), m("-10"), m("15.5"), m("40.5"), m("28.5")), m("16.0"));
    }

    private void Decimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.averageDecimal());
        assertEquals(expected, source.averageDecimal(x -> x));
    }

    @Test
    public void Decimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.asEnumerable(Tuple.create("Tim", m("5.5")), Tuple.create("John", m("15.5")), Tuple.create("Bob", m("3.0")));
        BigDecimal expected = m("8.0");

        assertEquals(expected, source.averageDecimal(e -> e.getItem2()));
    }

    @Test
    public void NullableDecimal() {
        this.NullableDecimal(Linq.empty(), null);
        this.NullableDecimal(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimal(Linq.asEnumerable(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimal(Linq.asEnumerable(m("5.5"), BigDecimal.ZERO, null, null, null, m("15.5"), m("40.5"), null, null, m("-23.5")), m("7.6"));
        this.NullableDecimal(Linq.asEnumerable(null, null, null, null, m("45")), m("45"));
        this.NullableDecimal(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableDecimal(IEnumerable<BigDecimal> source, BigDecimal expected) {
        assertEquals(expected, source.averageDecimalNull());
        assertEquals(expected, source.averageDecimalNull(x -> x));
    }

    @Test
    public void NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull(i -> i));
    }

    @Test
    public void NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        NullableDecimalFunc1<BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<BigDecimal>empty().averageDecimalNull(selector));
    }

    @Test
    public void NullableDecimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.asEnumerable(Tuple.create("Tim", m("5.5")), Tuple.create("John", m("15.5")), Tuple.create("Bob", null));
        BigDecimal expected = m("10.5");

        assertEquals(expected, source.averageDecimalNull(e -> e.getItem2()));
    }

    @Test
    public void NullableDecimal_SumTooLarge_ThrowsOverflowException() {
        IEnumerable<BigDecimal> source = Linq.asEnumerable(MAX_DECIMAL, MAX_DECIMAL);

        assertEquals(MAX_DECIMAL, source.averageDecimal());
    }

    @Test
    public void Float_EmptySource_ThrowsInvalidOperationException() {
        IEnumerable<Float> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.averageFloat());
        assertThrows(InvalidOperationException.class, () -> source.averageFloat(i -> i));
    }

    @Test
    public void Float_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloat());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloat(i -> i));
    }

    @Test
    public void Float_NullSelector_ThrowsArgumentNullException() {
        FloatFunc1<Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().averageFloat(selector));
    }

    @Test
    public void Float_TestData() {
        this.Float(Linq.singleton(Float.MAX_VALUE), Float.MAX_VALUE);
        this.Float(Linq.asEnumerable(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), 0f);
        this.Float(Linq.asEnumerable(5.5f, -10f, 15.5f, 40.5f, 28.5f), 16f);
    }

    private void Float(IEnumerable<Float> source, float expected) {
        assertEquals(expected, source.averageFloat());
        assertEquals(expected, source.averageFloat(x -> x));
    }

    @Test
    public void Float_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", 3.0f));
        float expected = 8.0f;

        assertEquals(expected, source.averageFloat(e -> e.getItem2()));
    }

    @Test
    public void testAverageInt() {
        Integer[] numbers = {0, 3, 3};
        assertEquals(2d, Linq.asEnumerable(numbers).averageInt());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageInt());
    }

    @Test
    public void testAverageIntNull() {
        Integer[] numbers = {null, 0, 3, 3};
        assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull());
    }

    @Test
    public void testAverageLong() {
        Long[] numbers = {0L, 3L, 3L};
        assertEquals(2d, Linq.asEnumerable(numbers).averageLong());

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageLong());
    }

    @Test
    public void testAverageLongNull() {
        Long[] numbers = {null, 0L, 3L, 3L};
        assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull());

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull());
    }

    @Test
    public void testAverageFloat() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat());

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageFloat());
    }

    @Test
    public void testAverageFloatNull() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull());

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull());
    }

    @Test
    public void testAverageDouble() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble());

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageDouble());
    }

    @Test
    public void testAverageDoubleNull() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull());

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull());
    }

    @Test
    public void testAverageDecimal() {
        BigDecimal[] numbers = {m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.asEnumerable(numbers).averageDecimal());

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageDecimal());
    }

    @Test
    public void testAverageDecimalNull() {
        BigDecimal[] numbers = {null, m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.asEnumerable(numbers).averageDecimalNull());

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull());
    }

    @Test
    public void testAverageIntWithSelector() {
        Integer[] numbers = {0, 3, 3};
        assertEquals(2d, Linq.asEnumerable(numbers).averageInt(n -> n));

        Integer[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageInt(n -> n));
    }

    @Test
    public void testAverageIntNullWithSelector() {
        Integer[] numbers = {null, 0, 3, 3};
        assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(n -> n));

        Integer[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull(n -> n));
    }

    @Test
    public void testAverageLongWithSelector() {
        Long[] numbers = {0L, 3L, 3L};
        assertEquals(2d, Linq.asEnumerable(numbers).averageLong(n -> n));

        Long[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageLong(n -> n));
    }

    @Test
    public void testAverageLongNullWithSelector() {
        Long[] numbers = {null, 0L, 3L, 3L};
        assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(n -> n));

        Long[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull(n -> n));
    }

    @Test
    public void testAverageFloatWithSelector() {
        Float[] numbers = {0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(n -> n));

        Float[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageFloat(n -> n));
    }

    @Test
    public void testAverageFloatNullWithSelector() {
        Float[] numbers = {null, 0f, 2f, Float.NaN};
        assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(n -> n));

        Float[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull(n -> n));
    }

    @Test
    public void testAverageDoubleWithSelector() {
        Double[] numbers = {0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(n -> n));

        Double[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageDouble(n -> n));
    }

    @Test
    public void testAverageDoubleNullWithSelector() {
        Double[] numbers = {null, 0d, 2d, Double.NaN};
        assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(n -> n));

        Double[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull(n -> n));
    }

    @Test
    public void testAverageDecimalWithSelector() {
        BigDecimal[] numbers = {m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.asEnumerable(numbers).averageDecimal(n -> n));

        BigDecimal[] numbers2 = {null};
        assertThrows(NullPointerException.class, () -> Linq.asEnumerable(numbers2).averageDecimal(n -> n));
    }

    @Test
    public void testAverageDecimalNullWithSelector() {
        BigDecimal[] numbers = {null, m("0"), m("3"), m("3")};
        assertEquals(m("2"), Linq.asEnumerable(numbers).averageDecimalNull(n -> n));

        BigDecimal[] numbers2 = {null};
        assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull(n -> n));
    }
}
