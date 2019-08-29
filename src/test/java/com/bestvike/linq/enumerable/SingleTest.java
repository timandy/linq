package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SingleTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<BigDecimal> q = Linq.of(new BigDecimal[]{m("999.9")}).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^"}).where(x -> !IsNullOrEmpty(x)).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    public void SameResultsRepeatCallsIntQueryWithZero() {
        IEnumerable<Integer> q = Linq.of(new int[]{0}).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    public void EmptyIList() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single());
    }

    @Test
    public void SingleElementIList() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).single());
    }

    @Test
    public void ManyElementIList() {
        int[] source = {4, 4, 4, 4, 4};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single());
    }

    @Test
    public void EmptyNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(0, 0);

        assertThrows(InvalidOperationException.class, () -> source.single());
    }

    @Test
    public void SingleElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertEquals(expected, source.single());
    }

    @Test
    public void ManyElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(3, 5);

        assertThrows(InvalidOperationException.class, () -> source.single());
    }

    @Test
    public void EmptySourceWithPredicate() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void SingleElementPredicateTrue() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void SingleElementPredicateFalse() {
        int[] source = {3};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void ManyElementsPredicateFalseForAll() {
        int[] source = {3, 1, 7, 9, 13, 19};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void ManyElementsPredicateTrueForLast() {
        int[] source = {3, 1, 7, 9, 13, 19, 20};
        int expected = 20;

        assertEquals(expected, Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void ManyElementsPredicateTrueForFirstAndLast() {
        int[] source = {2, 3, 1, 7, 9, 13, 19, 10};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    public void FindSingleMatch() {
        this.FindSingleMatch(1, 100);
        this.FindSingleMatch(42, 100);
    }

    private void FindSingleMatch(int target, int range) {
        assertEquals(target, Linq.range(0, range).single(i -> i == target));
    }

    @Test
    public void RunOnce() {
        this.RunOnce(1, 100);
        this.RunOnce(42, 100);
    }

    private void RunOnce(int target, int range) {
        assertEquals(target, Linq.range(0, range).runOnce().single(i -> i == target));
    }

    @Test
    public void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;

        assertThrows(NullPointerException.class, () -> source.single());
        assertThrows(NullPointerException.class, () -> source.single(i -> i % 2 == 0));
    }

    @Test
    public void ThrowsOnNullPredicate() {
        int[] source = {};
        Predicate1<Integer> nullPredicate = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).single(nullPredicate));
    }

    @Test
    public void testSingle() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        assertEquals(person[0], Linq.of(person).single());
        assertEquals(number[0], Linq.of(number).single());
        try {
            String s = Linq.of(people).single();
            fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            int i = Linq.of(numbers).single();
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSinglePredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithoutGT15 = {5, 10, 15};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};

        assertEquals(people[1], Linq.of(people).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.of(numbers).single(i -> i > 15));

        try {
            String ss = Linq.of(twoPeopleWithCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.of(numbersWithTwoGT15).single(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }

        try {
            String ss = Linq.of(peopleWithoutCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.of(numbersWithoutGT15).single(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }
}
