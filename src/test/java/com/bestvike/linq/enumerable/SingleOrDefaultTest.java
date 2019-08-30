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

/**
 * Created by 许崇雷 on 2019-06-06.
 */
class SingleOrDefaultTest extends TestCase {
    private static IEnumerable<Object[]> FindSingleMatch_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1, 100);
        argsList.add(42, 100);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Float> q = Linq.of(new float[]{0.12335f}).select(x -> x);

        assertEquals(q.singleOrDefault(), q.singleOrDefault());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{""}).select(x -> x);

        assertEquals(q.singleOrDefault(TestCase::IsNullOrEmpty), q.singleOrDefault(TestCase::IsNullOrEmpty));
    }

    @Test
    void EmptyIList() {
        Integer[] source = {};
        Integer expected = null;

        assertEquals(expected, Linq.of(source).singleOrDefault());
    }

    @Test
    void SingleElementIList() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).singleOrDefault());
    }

    @Test
    void ManyElementIList() {
        int[] source = {4, 4, 4, 4, 4};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).singleOrDefault());
    }

    @Test
    void EmptyNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(0, 0);
        Integer expected = null;

        assertEquals(expected, source.singleOrDefault());
    }

    @Test
    void SingleElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertEquals(expected, source.singleOrDefault());
    }

    @Test
    void ManyElementNotIList() {
        IEnumerable<Integer> source = RepeatedNumberGuaranteedNotCollectionType(3, 5);

        assertThrows(InvalidOperationException.class, () -> source.singleOrDefault());
    }

    @Test
    void EmptySourceWithPredicate() {
        int[] source = {};
        Integer expected = null;

        assertEquals(expected, Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void SingleElementPredicateTrue() {
        int[] source = {4};
        int expected = 4;

        assertEquals(expected, Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void SingleElementPredicateFalse() {
        int[] source = {3};
        Integer expected = null;

        assertEquals(expected, Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateFalseForAll() {
        int[] source = {3, 1, 7, 9, 13, 19};
        Integer expected = null;

        assertEquals(expected, Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateTrueForLast() {
        int[] source = {3, 1, 7, 9, 13, 19, 20};
        int expected = 20;

        assertEquals(expected, Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void ManyElementsPredicateTrueForFirstAndFifth() {
        int[] source = {2, 3, 1, 7, 10, 13, 19, 9};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).singleOrDefault(i -> i % 2 == 0));
    }

    @ParameterizedTest
    @MethodSource("FindSingleMatch_TestData")
    void FindSingleMatch(int target, int range) {
        assertEquals(target, Linq.range(0, range).singleOrDefault(i -> i == target));
    }

    @ParameterizedTest
    @MethodSource("FindSingleMatch_TestData")
    void RunOnce(int target, int range) {
        assertEquals(target, Linq.range(0, range).runOnce().singleOrDefault(i -> i == target));
    }

    @Test
    void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.singleOrDefault());
        assertThrows(NullPointerException.class, () -> source.singleOrDefault(i -> i % 2 == 0));
    }

    @Test
    void ThrowsOnNullPredicate() {
        IEnumerable<Integer> source = Linq.empty();
        Predicate1<Integer> nullPredicate = null;
        assertThrows(ArgumentNullException.class, () -> source.singleOrDefault(nullPredicate));
    }

    @Test
    void testSingleOrDefault() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        assertEquals(person[0], Linq.of(person).singleOrDefault());
        assertEquals(number[0], Linq.of(number).singleOrDefault());
        try {
            String s = Linq.of(people).singleOrDefault();
            fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            Integer i = Linq.of(numbers).singleOrDefault();
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    void testSingleOrDefaultPredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};
        Integer[] numbersWithoutGT15 = {5, 10, 15};

        assertEquals(people[1], Linq.of(people).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.of(numbers).singleOrDefault(n -> n > 15));
        try {
            String ss = Linq.of(twoPeopleWithCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
        try {

            Integer i = Linq.of(numbersWithTwoGT15).singleOrDefault(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
        assertNull(Linq.of(peopleWithoutCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertNull(Linq.of(numbersWithoutGT15).singleOrDefault(n -> n > 15));
    }
}
