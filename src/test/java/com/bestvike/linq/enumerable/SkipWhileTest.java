package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
public class SkipWhileTest extends TestCase {
    @Test
    public void SkipWhileAllTrue() {
        assertEquals(Linq.<Integer>empty(), Linq.range(0, 20).skipWhile(i -> i < 40));
        assertEquals(Linq.<Integer>empty(), Linq.range(0, 20).skipWhile((i, idx) -> i == idx));
    }

    @Test
    public void SkipWhileAllFalse() {
        assertEquals(Linq.range(0, 20), Linq.range(0, 20).skipWhile(i -> i != 0));
        assertEquals(Linq.range(0, 20), Linq.range(0, 20).skipWhile((i, idx) -> i != idx));
    }

    @Test
    public void SkipWhileThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).skipWhile(i -> i < 40));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).skipWhile((i, idx) -> i == idx));
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 20).skipWhile((IndexPredicate2<Integer>) null));
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 20).skipWhile((Predicate1<Integer>) null));
    }

    @Test
    public void SkipWhilePassesPredicateExceptionWhenEnumerated() {
        IEnumerable<Integer> source = Linq.range(-2, 5).skipWhile(i -> 1 / i <= 0);
        try (IEnumerator<Integer> en = source.enumerator()) {
            assertThrows(ArithmeticException.class, () -> en.moveNext());
        }
    }

    @Test
    public void SkipWhileHalf() {
        assertEquals(Linq.range(10, 10), Linq.range(0, 20).skipWhile(i -> i < 10));
        assertEquals(Linq.range(10, 10), Linq.range(0, 20).skipWhile((i, idx) -> idx < 10));
    }

    @Test
    public void RunOnce() {
        assertEquals(Linq.range(10, 10), Linq.range(0, 20).runOnce().skipWhile(i -> i < 10));
        assertEquals(Linq.range(10, 10), Linq.range(0, 20).runOnce().skipWhile((i, idx) -> idx < 10));
    }

    @Test
    public void SkipErrorWhenSourceErrors() {
        IEnumerable<BigDecimal> source = NumberRangeGuaranteedNotCollectionType(-2, 5).select(i -> m(i.toString())).select(m -> m("1").divide(m, MathContext.DECIMAL128)).skip(4);
        try (IEnumerator<BigDecimal> en = source.enumerator()) {
            assertThrows(ArithmeticException.class, () -> en.moveNext());
        }
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.skipWhile(x -> true), q.skipWhile(x -> true));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.skipWhile(x -> true), q.skipWhile(x -> true));
    }

    @Test
    public void PredicateManyFalseOnSecond() {
        int[] source = {8, 3, 12, 4, 6, 10};
        int[] expected = {3, 12, 4, 6, 10};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile(e -> e % 2 == 0));
    }

    @Test
    public void PredicateManyFalseOnSecondIndex() {
        int[] source = {8, 3, 12, 4, 6, 10};
        int[] expected = {3, 12, 4, 6, 10};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile((e, i) -> e % 2 == 0));
    }

    @Test
    public void PredicateTrueOnSecondFalseOnFirstAndOthers() {
        int[] source = {3, 2, 4, 12, 6};
        int[] expected = {3, 2, 4, 12, 6};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile(e -> e % 2 == 0));
    }

    @Test
    public void PredicateTrueOnSecondFalseOnFirstAndOthersIndex() {
        int[] source = {3, 2, 4, 12, 6};
        int[] expected = {3, 2, 4, 12, 6};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile((e, i) -> e % 2 == 0));
    }

    @Test
    public void FirstExcludedByIndex() {
        int[] source = {6, 2, 5, 3, 8};
        int[] expected = {2, 5, 3, 8};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile((element, index) -> index == 0));
    }

    @Test
    public void AllButLastExcludedByIndex() {
        int[] source = {6, 2, 5, 3, 8};
        int[] expected = {8};

        assertEquals(Linq.of(expected), Linq.of(source).skipWhile((element, index) -> index < source.length - 1));
    }

    @Test
    public void IndexSkipWhileOverflowBeyondIntMaxValueElements() {
        IEnumerable<Integer> skipped = new FastInfiniteEnumerator<Integer>().skipWhile((e, i) -> true);

        try (IEnumerator<Integer> en = skipped.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).skipWhile(e -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).skipWhile((e, i) -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testSkipWhile() {
        assertEquals(2, Linq.of(depts).skipWhile(dept -> dept.name.equals("Sales")).count());
        assertEquals(3, Linq.of(depts).skipWhile(dept -> !dept.name.equals("Sales")).count());
    }

    @Test
    public void testSkipWhileIndexed() {
        int count = Linq.of(depts)
                .skipWhile((dept, index) -> dept.name.equals("Sales") || index == 1)
                .count();
        assertEquals(1, count);
    }
}
