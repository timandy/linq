package com.bestvike.linq.enumerable;

import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AverageTest extends EnumerableTest {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Assert.assertEquals(q.averageInt(), q.averageInt(), DELTA);
    }

    @Test
    public void SameResultsRepeatCallsNullableLongQuery() {
        IEnumerable<Long> q = Linq.asEnumerable((long) Integer.MAX_VALUE, (long) 0, (long) 255, (long) 127, (long) 128, (long) 1, (long) 33, (long) 99, null, (long) Integer.MIN_VALUE);

        Assert.assertEquals(q.averageLongNull(), q.averageLongNull(), DELTA);
    }

    @Test
    public void NullableFoat() {
        this.NullableFoatCore(Linq.empty(), null);
        this.NullableFoatCore(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoatCore(Linq.asEnumerable(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoatCore(Linq.asEnumerable(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoatCore(Linq.asEnumerable(null, null, null, null, 45f), 45f);
        this.NullableFoatCore(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableFoatCore(IEnumerable<Float> source, Float expected) {
        Assert.assertEquals(expected, source.averageFloatNull());
        Assert.assertEquals(expected, source.averageFloatNull(x -> x));
    }

    @Test
    public void NullableFoatRunOnce() {
        this.NullableFoatRunOnceCore(Linq.empty(), null);
        this.NullableFoatRunOnceCore(Linq.singleton(Float.MIN_VALUE), Float.MIN_VALUE);
        this.NullableFoatRunOnceCore(Linq.asEnumerable(0f, 0f, 0f, 0f, 0f), 0f);
        this.NullableFoatRunOnceCore(Linq.asEnumerable(5.5f, 0f, null, null, null, 15.5f, 40.5f, null, null, -23.5f), 7.6f);
        this.NullableFoatRunOnceCore(Linq.asEnumerable(null, null, null, null, 45f), 45f);
        this.NullableFoatRunOnceCore(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableFoatRunOnceCore(IEnumerable<Float> source, Float expected) {
        Assert.assertEquals(expected, source.runOnce().averageFloatNull());
        Assert.assertEquals(expected, source.runOnce().averageFloatNull(x -> x));
    }

    @Test
    public void NullableFloat_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Float>) null).averageFloatNull(i -> i));
    }

    @Test
    public void NullableFloat_NullSelector_ThrowsArgumentNullException() {
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Float>empty().averageFloatNull(selector));
    }

    @Test
    public void NullableFloat_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", null));
        Float expected = 10.5f;

        Assert.assertEquals(expected, source.averageFloatNull(e -> e.getItem2()));
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
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Enumerable.<Integer>empty().averageInt(selector));
    }

    @Test
    public void Int() {
        this.IntCore(Linq.singleton(5), 5);
        this.IntCore(Linq.asEnumerable(0, 0, 0, 0, 0), 0);
        this.IntCore(Linq.asEnumerable(5, -10, 15, 40, 28), 15.6);
    }

    private void IntCore(IEnumerable<Integer> source, double expected) {
        Assert.assertEquals(expected, source.averageInt(), DELTA);
        Assert.assertEquals(expected, source.averageInt(x -> x), DELTA);
    }

    @Test
    public void IntRunOnce() {
        this.IntRunOnceCore(Linq.singleton(5), 5);
        this.IntRunOnceCore(Linq.asEnumerable(0, 0, 0, 0, 0), 0);
        this.IntRunOnceCore(Linq.asEnumerable(5, -10, 15, 40, 28), 15.6);
    }

    private void IntRunOnceCore(IEnumerable<Integer> source, double expected) {
        Assert.assertEquals(expected, source.runOnce().averageInt(), DELTA);
        Assert.assertEquals(expected, source.runOnce().averageInt(x -> x), DELTA);
    }

    @Test
    public void Int_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.asEnumerable(Tuple.create("Tim", 10), Tuple.create("John", -10), Tuple.create("Bob", 15));
        double expected = 5;

        Assert.assertEquals(expected, source.averageInt(e -> e.getItem2()), DELTA);
    }

    @Test
    public void NullableInt() {
        this.NullableIntCore(Linq.empty(), null);
        this.NullableIntCore(Linq.singleton(-5), -5.0);
        this.NullableIntCore(Linq.asEnumerable(0, 0, 0, 0, 0), 0.0);
        this.NullableIntCore(Linq.asEnumerable(5, -10, null, null, null, 15, 40, 28, null, null), 15.6);
        this.NullableIntCore(Linq.asEnumerable(null, null, null, null, 50), 50.0);
        this.NullableIntCore(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableIntCore(IEnumerable<Integer> source, Double expected) {
        Assert.assertEquals(expected, source.averageIntNull());
        Assert.assertEquals(expected, source.averageIntNull(x -> x));
    }

    @Test
    public void NullableInt_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).averageIntNull(i -> i));
    }

    @Test
    public void NullableInt_NullSelector_ThrowsArgumentNullException() {
        Func1<Integer, Integer> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Integer>empty().averageIntNull(selector));
    }

    @Test
    public void NullableInt_WithSelector() {
        IEnumerable<Tuple2<String, Integer>> source = Linq.asEnumerable(Tuple.create("Tim", 10), Tuple.create("John", null), Tuple.create("Bob", 10));
        Double expected = 10d;

        Assert.assertEquals(expected, source.averageIntNull(e -> e.getItem2()));
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
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLong(selector));
    }

    @Test
    public void Long() {
        this.LongCore(Linq.singleton(Long.MAX_VALUE), Long.MAX_VALUE);
        this.LongCore(Linq.asEnumerable(0L, 0L, 0L, 0L, 0L), 0);
        this.LongCore(Linq.asEnumerable(5L, -10L, 15L, 40L, 28L), 15.6);
    }

    private void LongCore(IEnumerable<Long> source, double expected) {
        Assert.assertEquals(expected, source.averageLong(), DELTA);
        Assert.assertEquals(expected, source.averageLong(x -> x), DELTA);
    }

    @Test
    public void Long_FromSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.asEnumerable(Tuple.create("Tim", 40L), Tuple.create("John", 50L), Tuple.create("Bob", 60L));
        double expected = 50;

        Assert.assertEquals(expected, source.averageLong(e -> e.getItem2()), DELTA);
    }

    @Test
    public void Long_SumTooLarge_ThrowsOverflowException() {
        IEnumerable<Long> source = Linq.asEnumerable(Long.MAX_VALUE, Long.MAX_VALUE);
        assertThrows(ArithmeticException.class, () -> source.averageLong());
    }

    @Test
    public void NullableLong() {
        this.NullableLongCore(Linq.empty(), null);
        this.NullableLongCore(Linq.singleton(Long.MAX_VALUE), (double) Long.MAX_VALUE);
        this.NullableLongCore(Linq.asEnumerable(0L, 0L, 0L, 0L, 0L), 0.0);
        this.NullableLongCore(Linq.asEnumerable(5L, -10L, null, null, null, 15L, 40L, 28L, null, null), 15.6);
        this.NullableLongCore(Linq.asEnumerable(null, null, null, null, 50L), 50.0);
        this.NullableLongCore(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableLongCore(IEnumerable<Long> source, Double expected) {
        Assert.assertEquals(expected, source.averageLongNull());
        Assert.assertEquals(expected, source.averageLongNull(x -> x));
    }

    @Test
    public void NullableLong_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Long>) null).averageLongNull(i -> i));
    }

    @Test
    public void NullableLong_NullSelector_ThrowsArgumentNullException() {
        Func1<Long, Long> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Long>empty().averageLongNull(selector));
    }

    @Test
    public void NullableLong_WithSelector() {
        IEnumerable<Tuple2<String, Long>> source = Linq.asEnumerable(Tuple.create("Tim", 40L), Tuple.create("John", null), Tuple.create("Bob", 30L));
        Double expected = 35d;

        Assert.assertEquals(expected, source.averageLongNull(e -> e.getItem2()), DELTA);
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
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Double>empty().averageDouble(selector));
    }

    @Test
    public void Average_Double() {
        this.Average_DoubleCore(Linq.singleton(Double.MAX_VALUE), Double.MAX_VALUE);
        this.Average_DoubleCore(Linq.asEnumerable(0.0, 0.0, 0.0, 0.0, 0.0), 0);
        this.Average_DoubleCore(Linq.asEnumerable(5.5, -10d, 15.5, 40.5, 28.5), 16);
        this.Average_DoubleCore(Linq.asEnumerable(5.58, Double.NaN, 30d, 4.55, 19.38), Double.NaN);
    }

    private void Average_DoubleCore(IEnumerable<Double> source, double expected) {
        Assert.assertEquals(expected, source.averageDouble(), DELTA);
        Assert.assertEquals(expected, source.averageDouble(x -> x), DELTA);
    }

    @Test
    public void Double_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", 3.0));
        double expected = 8.0;

        Assert.assertEquals(expected, source.averageDouble(e -> e.getItem2()), DELTA);
    }

    @Test
    public void NullableDouble() {
        this.NullableDoubleCore(Linq.empty(), null);
        this.NullableDoubleCore(Linq.singleton(Double.MIN_VALUE), Double.MIN_VALUE);
        this.NullableDoubleCore(Linq.asEnumerable(0d, 0d, 0d, 0d, 0d), 0.0);
        this.NullableDoubleCore(Linq.asEnumerable(5.5d, 0d, null, null, null, 15.5d, 40.5d, null, null, -23.5d), 7.6d);
        this.NullableDoubleCore(Linq.asEnumerable(null, null, null, null, 45d), 45.0);
        this.NullableDoubleCore(Linq.asEnumerable(-23.5, 0d, Double.NaN, 54.3, 0.56), Double.NaN);
        this.NullableDoubleCore(Linq.asEnumerable(null, null, null, null, null), null);
    }


    private void NullableDoubleCore(IEnumerable<Double> source, Double expected) {
        Assert.assertEquals(expected, source.averageDoubleNull());
        Assert.assertEquals(expected, source.averageDoubleNull(x -> x));
    }

    @Test
    public void NullableDouble_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Double>) null).averageDoubleNull(i -> i));
    }

    @Test
    public void NullableDouble_NullSelector_ThrowsArgumentNullException() {
        Func1<Double, Double> selector = null;
        assertThrows(ArgumentNullException.class, () -> Enumerable.<Double>empty().averageDoubleNull(selector));
    }

    @Test
    public void NullableDouble_WithSelector() {
        IEnumerable<Tuple2<String, Double>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5), Tuple.create("John", 15.5), Tuple.create("Bob", null));
        Double expected = 10.5;

        Assert.assertEquals(expected, source.averageDoubleNull(e -> e.getItem2()));
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
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Enumerable.<BigDecimal>empty().averageDecimal(selector));
    }

    @Test
    public void Decimal() {
        this.DecimalCore(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.DecimalCore(Linq.asEnumerable(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.DecimalCore(Linq.asEnumerable(new BigDecimal("5.5"), new BigDecimal("-10"), new BigDecimal("15.5"), new BigDecimal("40.5"), new BigDecimal("28.5")), new BigDecimal("16.0"));
    }

    private void DecimalCore(IEnumerable<BigDecimal> source, BigDecimal expected) {
        Assert.assertEquals(expected, source.averageDecimal());
        Assert.assertEquals(expected, source.averageDecimal(x -> x));
    }

    @Test
    public void Decimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.asEnumerable(Tuple.create("Tim", new BigDecimal("5.5")), Tuple.create("John", new BigDecimal("15.5")), Tuple.create("Bob", new BigDecimal("3.0")));
        BigDecimal expected = new BigDecimal("8.0");

        Assert.assertEquals(expected, source.averageDecimal(e -> e.getItem2()));
    }

    @Test
    public void NullableDecimal() {
        this.NullableDecimalCore(Linq.empty(), null);
        this.NullableDecimalCore(Linq.singleton(BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimalCore(Linq.asEnumerable(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO);
        this.NullableDecimalCore(Linq.asEnumerable(new BigDecimal("5.5"), BigDecimal.ZERO, null, null, null, new BigDecimal("15.5"), new BigDecimal("40.5"), null, null, new BigDecimal("-23.5")), new BigDecimal("7.6"));
        this.NullableDecimalCore(Linq.asEnumerable(null, null, null, null, new BigDecimal("45")), new BigDecimal("45"));
        this.NullableDecimalCore(Linq.asEnumerable(null, null, null, null, null), null);
    }

    private void NullableDecimalCore(IEnumerable<BigDecimal> source, BigDecimal expected) {
        Assert.assertEquals(expected, source.averageDecimalNull());
        Assert.assertEquals(expected, source.averageDecimalNull(x -> x));
    }

    @Test
    public void NullableDecimal_NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull());
        assertThrows(NullPointerException.class, () -> ((IEnumerable<BigDecimal>) null).averageDecimalNull(i -> i));
    }

    @Test
    public void NullableDecimal_NullSelector_ThrowsArgumentNullException() {
        Func1<BigDecimal, BigDecimal> selector = null;
        assertThrows(ArgumentNullException.class, () -> Enumerable.<BigDecimal>empty().averageDecimalNull(selector));
    }

    @Test
    public void NullableDecimal_WithSelector() {
        IEnumerable<Tuple2<String, BigDecimal>> source = Linq.asEnumerable(Tuple.create("Tim", new BigDecimal("5.5")), Tuple.create("John", new BigDecimal("15.5")), Tuple.create("Bob", null));
        BigDecimal expected = new BigDecimal("10.5");

        Assert.assertEquals(expected, source.averageDecimalNull(e -> e.getItem2()));
    }

    @Test
    public void NullableDecimal_SumTooLarge_ThrowsOverflowException() {
//        IEnumerable<BigDecimal> source =Linq.asEnumerable(decimal.MaxValue, decimal.MaxValue);
//
//        Assert.Throws<OverflowException>(() -> source.Average());
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
        Func1<Float, Float> selector = null;
        assertThrows(ArgumentNullException.class, () -> Enumerable.<Float>empty().averageFloat(selector));
    }

    @Test
    public void Float_TestData() {
        this.FloatCore(Linq.singleton(Float.MAX_VALUE), Float.MAX_VALUE);
        this.FloatCore(Linq.asEnumerable(0.0f, 0.0f, 0.0f, 0.0f, 0.0f), 0f);
        this.FloatCore(Linq.asEnumerable(5.5f, -10f, 15.5f, 40.5f, 28.5f), 16f);
    }

    private void FloatCore(IEnumerable<Float> source, float expected) {
        Assert.assertEquals(expected, source.averageFloat(), DELTA);
        Assert.assertEquals(expected, source.averageFloat(x -> x), DELTA);
    }

    @Test
    public void Float_WithSelector() {
        IEnumerable<Tuple2<String, Float>> source = Linq.asEnumerable(Tuple.create("Tim", 5.5f), Tuple.create("John", 15.5f), Tuple.create("Bob", 3.0f));
        float expected = 8.0f;

        Assert.assertEquals(expected, source.averageFloat(e -> e.getItem2()), DELTA);
    }

    @Test
    public void testAverageInt() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNull() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull());
    }

    @Test
    public void testAverageLong() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNull() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull());
    }

    @Test
    public void testAverageFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull());
    }

    @Test
    public void testAverageDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull());
    }

    @Test
    public void testAverageDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull());
    }

    @Test
    public void testAverageIntWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(n -> n), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(n -> n), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull(n -> n));
    }

    @Test
    public void testAverageLongWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(n -> n), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(n -> n), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull(n -> n));
    }

    @Test
    public void testAverageFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull(n -> n));
    }

    @Test
    public void testAverageDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull(n -> n));
    }

    @Test
    public void testAverageDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull(n -> n));
    }
}