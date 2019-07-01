package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.Test;

import java.util.Date;

/**
 * Created by 许崇雷 on 2019-05-09.
 */
public class FirstOrDefaultTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> ieInt = Linq.range(0, 0);
        IEnumerable<Integer> q = ieInt.select(x -> x);

        assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    private <T> void TestEmptyIList(Class<T> clazz) {
        T[] source = ArrayUtils.empty(clazz);
        T expected = null;

        assertTrue(IList.class.isAssignableFrom(Linq.of(source).getClass()));
        assertEquals(expected, Linq.of(source).runOnce().firstOrDefault());
    }

    @Test
    public void EmptyIListT() {
        this.TestEmptyIList(Integer.class);
        this.TestEmptyIList(String.class);
        this.TestEmptyIList(Date.class);
        this.TestEmptyIList(FirstOrDefaultTest.class);
    }

    @Test
    public void IListTOneElement() {
        int[] source = new int[]{5};
        int expected = 5;

        assertTrue(IList.class.isAssignableFrom(Linq.of(source).getClass()));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    @Test
    public void IListTManyElementsFirstIsDefault() {
        Integer[] source = {null, -10, 2, 4, 3, 0, 2};
        Integer expected = null;

        assertTrue(IList.class.isAssignableFrom(Linq.of(source).getClass()));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    @Test
    public void IListTManyElementsFirstIsNotDefault() {
        Integer[] source = {19, null, -10, 2, 4, 3, 0, 2};
        Integer expected = 19;

        assertTrue(IList.class.isAssignableFrom(Linq.of(source).getClass()));
        assertEquals(expected, Linq.of(source).firstOrDefault());
    }

    private <T> IEnumerable<T> EmptySourceGeneric() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySourceGeneric();
        T expected = null;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.runOnce().firstOrDefault());
    }

    @Test
    public void EmptyNotIListT() {
        this.<Integer>TestEmptyNotIList();
        this.<String>TestEmptyNotIList();
        this.<Date>TestEmptyNotIList();
        this.<FirstOrDefaultTest>TestEmptyNotIList();
    }

    @Test
    public void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.firstOrDefault());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 3;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.firstOrDefault());
    }

    @Test
    public void EmptySource() {
        Integer[] source = {};

        assertNull(Linq.of(source).firstOrDefault(x -> true));
        assertNull(Linq.of(source).firstOrDefault(x -> false));
    }

    @Test
    public void OneElementTruePredicate() {
        int[] source = {4};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    public void ManyElementsPredicateFalseForAll() {
        int[] source = {9, 5, 1, 3, 17, 21};
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueOnlyForLast() {
        int[] source = {9, 5, 1, 3, 17, 21, 50};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueForSome() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueForSomeRunOnce() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).runOnce().firstOrDefault(predicate));
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).firstOrDefault());
    }

    @Test
    public void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).firstOrDefault(i -> i != 2));
    }

    @Test
    public void NullPredicate() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).firstOrDefault(predicate));
    }

    @Test
    public void testFirstOrDefault() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] empty = {};
        Integer[] numbers = {5, 10, 15, 20, 25};

        assertEquals(people[0], Linq.of(people).firstOrDefault());
        assertEquals(numbers[0], Linq.of(numbers).firstOrDefault());

        assertNull(Linq.of(empty).firstOrDefault());
    }

    @Test
    public void testFirstOrDefaultPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        assertEquals(people[1], Linq.of(people).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.of(numbers).firstOrDefault(i -> i > 15));
        assertNull(Linq.of(peopleWithoutCharS).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
    }
}
