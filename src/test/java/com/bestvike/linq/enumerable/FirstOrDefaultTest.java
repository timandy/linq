package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Created by 许崇雷 on 2019-05-09.
 */
class FirstOrDefaultTest extends TestCase {
    private static <T> void TestEmptyIList(Class<T> clazz) {
        T[] source = ArrayUtils.empty(clazz);
        T expected = null;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).runOnce().firstOrDefault());
    }

    private static <T> IEnumerable<T> TestEmptyNotIList_EmptySource() {
        return Linq.empty();
    }

    private static <T> void TestEmptyNotIList() {
        IEnumerable<T> source = TestEmptyNotIList_EmptySource();
        T expected = null;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.runOnce().firstOrDefault());
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> ieInt = Linq.range(0, 0);
        IEnumerable<Integer> q = ieInt.select(x -> x);

        assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    @Test
    void EmptyIListT() {
        TestEmptyIList(Integer.class);
        TestEmptyIList(String.class);
        TestEmptyIList(Date.class);
        TestEmptyIList(FirstOrDefaultTest.class);
    }

    @Test
    void IListTOneElement() {
        int[] source = new int[]{5};
        int expected = 5;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    @Test
    void IListTManyElementsFirstIsDefault() {
        Integer[] source = {null, -10, 2, 4, 3, 0, 2};
        Integer expected = null;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    @Test
    void IListTManyElementsFirstIsNotDefault() {
        Integer[] source = {19, null, -10, 2, 4, 3, 0, 2};
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    @Test
    void EmptyNotIListT() {
        FirstOrDefaultTest.<Integer>TestEmptyNotIList();
        FirstOrDefaultTest.<String>TestEmptyNotIList();
        FirstOrDefaultTest.<Date>TestEmptyNotIList();
        FirstOrDefaultTest.<FirstOrDefaultTest>TestEmptyNotIList();
    }

    @Test
    void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.firstOrDefault());
    }

    @Test
    void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 3;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.firstOrDefault());
    }

    @Test
    void EmptySource() {
        Integer[] source = {};

        assertNull(Linq.of(source).firstOrDefault(x -> true));
        assertNull(Linq.of(source).firstOrDefault(x -> false));
    }

    @Test
    void OneElementTruePredicate() {
        int[] source = {4};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    void ManyElementsPredicateFalseForAll() {
        int[] source = {9, 5, 1, 3, 17, 21};
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    void PredicateTrueOnlyForLast() {
        int[] source = {9, 5, 1, 3, 17, 21, 50};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    void PredicateTrueForSome() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    void PredicateTrueForSomeRunOnce() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).runOnce().firstOrDefault(predicate));
    }

    @Test
    void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).firstOrDefault());
    }

    @Test
    void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).firstOrDefault(i -> i != 2));
    }

    @Test
    void NullPredicate() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).firstOrDefault(predicate));
    }

    @Test
    void testFirstOrDefault() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] empty = {};
        Integer[] numbers = {5, 10, 15, 20, 25};

        assertEquals(people[0], Linq.of(people).firstOrDefault());
        assertEquals(numbers[0], Linq.of(numbers).firstOrDefault());

        assertNull(Linq.of(empty).firstOrDefault());
    }

    @Test
    void testFirstOrDefaultPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        assertEquals(people[1], Linq.of(people).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.of(numbers).firstOrDefault(i -> i > 15));
        assertNull(Linq.of(peopleWithoutCharS).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
    }
}
