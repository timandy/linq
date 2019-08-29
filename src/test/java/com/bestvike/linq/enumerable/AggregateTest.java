package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class AggregateTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.aggregate((x, y) -> x + y), q.aggregate((x, y) -> x + y));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.aggregate((x, y) -> x + y), q.aggregate((x, y) -> x + y));
    }

    @Test
    void EmptySource() {
        IEnumerable<Integer> source = Linq.empty();

        assertThrows(InvalidOperationException.class, () -> source.aggregate((x, y) -> x + y));
    }

    @Test
    void SingleElement() {
        IEnumerable<Integer> source = Linq.singleton(5);
        Integer expected = 5;

        assertEquals(expected, source.aggregate((x, y) -> x + y));
    }

    @Test
    void SingleElementRunOnce() {
        IEnumerable<Integer> source = Linq.singleton(5);
        Integer expected = 5;

        assertEquals(expected, source.runOnce().aggregate((x, y) -> x + y));
    }

    @Test
    void TwoElements() {
        IEnumerable<Integer> source = Linq.of(5, 6);
        Integer expected = 11;

        assertEquals(expected, source.aggregate((x, y) -> x + y));
    }

    @Test
    void MultipleElements() {
        IEnumerable<Integer> source = Linq.of(5, 6, 0, -4);
        Integer expected = 7;

        assertEquals(expected, source.aggregate((x, y) -> x + y));
    }

    @Test
    void MultipleElementsRunOnce() {
        IEnumerable<Integer> source = Linq.of(5, 6, 0, -4);
        Integer expected = 7;

        assertEquals(expected, source.runOnce().aggregate((x, y) -> x + y));
    }

    @Test
    void EmptySourceAndSeed() {
        IEnumerable<Integer> source = Linq.empty();
        Integer seed = 2;
        Integer expected = 2;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y));
    }

    @Test
    void SingleElementAndSeed() {
        IEnumerable<Integer> source = Linq.singleton(5);
        Integer seed = 2;
        Integer expected = 10;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y));
    }

    @Test
    void TwoElementsAndSeed() {
        IEnumerable<Integer> source = Linq.of(5, 6);
        Integer seed = 2;
        Integer expected = 60;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y));
    }

    @Test
    void MultipleElementsAndSeed() {
        IEnumerable<Integer> source = Linq.of(5, 6, 2, -4);
        Integer seed = 2;
        Integer expected = -480;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y));
    }

    @Test
    void MultipleElementsAndSeedRunOnce() {
        IEnumerable<Integer> source = Linq.of(5, 6, 2, -4);
        Integer seed = 2;
        Integer expected = -480;

        assertEquals(expected, source.runOnce().aggregate(seed, (x, y) -> x * y));
    }

    @Test
    void NoElementsSeedResultSeletor() {
        IEnumerable<Integer> source = Linq.empty();
        Double seed = 2d;
        Double expected = 7d;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y, x -> x + 5.0));
    }

    @Test
    void SingleElementSeedResultSelector() {
        IEnumerable<Integer> source = Linq.singleton(5);
        Double seed = 2d;
        Double expected = 15d;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y, x -> x + 5.0));
    }

    @Test
    void TwoElementsSeedResultSelector() {
        IEnumerable<Integer> source = Linq.of(5, 6);
        Double seed = 2d;
        Double expected = 65d;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y, x -> x + 5.0));
    }

    @Test
    void MultipleElementsSeedResultSelector() {
        IEnumerable<Integer> source = Linq.of(5, 6, 2, -4);
        Double seed = 2d;
        Double expected = -475d;

        assertEquals(expected, source.aggregate(seed, (x, y) -> x * y, x -> x + 5.0));
    }

    @Test
    void MultipleElementsSeedResultSelectorRunOnce() {
        IEnumerable<Integer> source = Linq.of(5, 6, 2, -4);
        Double seed = 2d;
        Double expected = -475d;

        assertEquals(expected, source.runOnce().aggregate(seed, (x, y) -> x * y, x -> x + 5.0));
    }

    @Test
    void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).aggregate((x, y) -> x + y));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).aggregate(0, (x, y) -> x + y));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).aggregate(0, (x, y) -> x + y, i -> i));
    }

    @Test
    void NullFunc() {
        Func2<Integer, Integer, Integer> func = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).aggregate(func));
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).aggregate(0, func));
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).aggregate(0, func, i -> i));
    }

    @Test
    void NullResultSelector() {
        Func1<Integer, Integer> resultSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).aggregate(0, (x, y) -> x + y, resultSelector));
    }

    @Test
    void testAggregate() {
        assertEquals("Sales,HR,Marketing", Linq.of(depts)
                .select(dept -> dept.name)
                .aggregate((res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    void testAggregateWithSeed() {
        assertEquals("A,Sales,HR,Marketing",
                Linq.of(depts)
                        .select(dept -> dept.name)
                        .aggregate("A", (res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    void testAggregateWithSeedWithResultSelector() {
        String s = Linq.of(emps)
                .select(emp -> emp.name)
                .aggregate(null,
                        (res, name) -> res == null ? name : res + "+" + name,
                        res -> "<no key>: " + res);
        assertEquals("<no key>: Fred+Bill+Eric+Janet", s);
    }
}
