package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class FirstTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.first(), q.first());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.first(), q.first());
    }

    private <T> void TestEmptyIList(Class<T> clazz) {
        T[] source = ArrayUtils.empty(clazz);

        assertNotNull(as(Linq.of(source), IList.class));
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).runOnce().first());
    }

    @Test
    void EmptyIListT() {
        this.TestEmptyIList(Integer.class);
        this.TestEmptyIList(String.class);
        this.TestEmptyIList(Date.class);
        this.TestEmptyIList(FirstTest.class);
    }

    @Test
    void IListTOneElement() {
        int[] source = {5};
        int expected = 5;

        assertNotNull(as(Linq.of(source), IList.class));
        assertEquals(expected, Linq.of(source).first());
    }

    @Test
    void IListTManyElementsFirstIsDefault() {
        Integer[] source = {null, -10, 2, 4, 3, 0, 2};
        Integer expected = null;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).first());
    }

    @Test
    void IListTManyElementsFirstIsNotDefault() {
        Integer[] source = {19, null, -10, 2, 4, 3, 0, 2};
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, Linq.of(source));
        assertEquals(expected, Linq.of(source).first());
    }

    private <T> IEnumerable<T> EmptySourceGeneric() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySourceGeneric();

        assertNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().first());
    }

    @Test
    void EmptyNotIListT() {
        this.<Integer>TestEmptyNotIList();
        this.<String>TestEmptyNotIList();
        this.<Date>TestEmptyNotIList();
        this.<FirstTest>TestEmptyNotIList();
    }

    @Test
    void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.first());
    }

    @Test
    void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 3;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.first());
    }

    @Test
    void EmptySource() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).first(x -> true));
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).first(x -> false));
    }

    @Test
    void OneElementTruePredicate() {
        int[] source = {4};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, Linq.of(source).first(predicate));
    }

    @Test
    void ManyElementsPredicateFalseForAll() {
        int[] source = {9, 5, 1, 3, 17, 21};
        Predicate1<Integer> predicate = TestCase::IsEven;

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).first(predicate));
    }

    @Test
    void PredicateTrueOnlyForLast() {
        int[] source = {9, 5, 1, 3, 17, 21, 50};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, Linq.of(source).first(predicate));
    }

    @Test
    void PredicateTrueForSome() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).first(predicate));
    }

    @Test
    void PredicateTrueForSomeRunOnce() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 10;

        assertEquals(expected, Linq.of(source).runOnce().first(predicate));
    }

    @Test
    void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).first());
    }

    @Test
    void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).first(i -> i != 2));
    }

    @Test
    void NullPredicate() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).first(predicate));
    }

    @Test
    void testFirst() {
        Employee e = emps[0];
        assertEquals(e, emps[0]);
        assertEquals(e, Linq.of(emps).first());

        Department d = depts[0];
        assertEquals(d, depts[0]);
        assertEquals(d, Linq.of(depts).first());

        try {
            String s = Linq.<String>empty().first();
            fail("expected exception, got " + s);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first throws
        try {
            Long num = Linq.of(new CountIterable(0)).first();
            fail("expected exception, got " + num);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first does not throw
        Long num = Linq.of(new CountIterable(1)).first();
        assertEquals(1L, num);
    }

    @Test
    void testFirstPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        assertEquals(people[1], Linq.of(people).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.of(numbers).first(i -> i > 15));

        try {
            String ss = Linq.of(peopleWithoutCharS).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
    }
}
