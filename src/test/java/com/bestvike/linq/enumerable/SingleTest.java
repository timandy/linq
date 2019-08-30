package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class SingleTest extends TestCase {
    private static IEnumerable<Object[]> FindSingleMatch_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1, 100);
        argsList.add(42, 100);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<BigDecimal> q = Linq.of(new BigDecimal[]{m("999.9")}).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^"}).where(x -> !IsNullOrEmpty(x)).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    void SameResultsRepeatCallsIntQueryWithZero() {
        IEnumerable<Integer> q = Linq.of(new int[]{0}).select(x -> x);

        assertEquals(q.single(), q.single());
    }

    @Test
    void EmptyIList() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single());
    }

    @Test
    void SingleElementIList() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).single());
    }

    @Test
    void ManyElementIList() {
        int[] source = {4, 4, 4, 4, 4};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single());
    }

    @Test
    void EmptyNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(0, 0);

        assertThrows(InvalidOperationException.class, () -> source.single());
    }

    @Test
    void SingleElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertEquals(expected, source.single());
    }

    @Test
    void ManyElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(3, 5);

        assertThrows(InvalidOperationException.class, () -> source.single());
    }

    @Test
    void EmptySourceWithPredicate() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    void SingleElementPredicateTrue() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    void SingleElementPredicateFalse() {
        int[] source = {3};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateFalseForAll() {
        int[] source = {3, 1, 7, 9, 13, 19};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateTrueForLast() {
        int[] source = {3, 1, 7, 9, 13, 19, 20};
        int expected = 20;

        assertEquals(expected, Linq.of(source).single(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateTrueForFirstAndLast() {
        int[] source = {2, 3, 1, 7, 9, 13, 19, 10};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).single(i -> i % 2 == 0));
    }

    @ParameterizedTest
    @MethodSource("FindSingleMatch_TestData")
    void FindSingleMatch(int target, int range) {
        assertEquals(target, Linq.range(0, range).single(i -> i == target));
    }

    @ParameterizedTest
    @MethodSource("FindSingleMatch_TestData")
    void RunOnce(int target, int range) {
        assertEquals(target, Linq.range(0, range).runOnce().single(i -> i == target));
    }

    @Test
    void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;

        assertThrows(NullPointerException.class, () -> source.single());
        assertThrows(NullPointerException.class, () -> source.single(i -> i % 2 == 0));
    }

    @Test
    void ThrowsOnNullPredicate() {
        int[] source = {};
        Predicate1<Integer> nullPredicate = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).single(nullPredicate));
    }

    @Test
    void testSingle() {
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
    void testSinglePredicate() {
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
