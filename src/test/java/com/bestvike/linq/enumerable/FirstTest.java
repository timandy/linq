package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.entity.IterableDemo;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class FirstTest extends EnumerableTest {
    private static <T> IEnumerable<T> EmptySourceGeneric() {
        return Linq.empty();
    }

    private static <T> void TestEmptyNotIList() {
        IEnumerable<T> source = EmptySourceGeneric();

        Assert.assertNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().first());
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        Assert.assertEquals(q.first(), q.first());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        Assert.assertEquals(q.first(), q.first());
    }

    private <T> void TestEmptyIList(Class<T> clazz) {
        T[] source = ArrayUtils.empty(clazz);

        Assert.assertNotNull(as(Linq.asEnumerable(source), IList.class));
        assertThrows(InvalidOperationException.class, () -> Linq.asEnumerable(source).runOnce().first());
    }

    @Test
    public void EmptyIListT() {
        this.TestEmptyIList(Integer.class);
        this.TestEmptyIList(String.class);
        this.TestEmptyIList(Date.class);
        this.TestEmptyIList(FirstTest.class);
    }

    @Test
    public void IListTOneElement() {
        int[] source = {5};
        int expected = 5;

        Assert.assertNotNull(as(Linq.asEnumerable(source), IList.class));
        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).first());
    }

    @Test
    public void IListTManyElementsFirstIsDefault() {
        Integer[] source = {null, -10, 2, 4, 3, 0, 2};
        Integer expected = null;

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, Linq.asEnumerable(source).first());
    }

    @Test
    public void IListTManyElementsFirstIsNotDefault() {
        Integer[] source = {19, null, -10, 2, 4, 3, 0, 2};
        Integer expected = 19;

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, Linq.asEnumerable(source).first());
    }

    @Test
    public void EmptyNotIListT() {
        FirstTest.<Integer>TestEmptyNotIList();
        FirstTest.<String>TestEmptyNotIList();
        FirstTest.<Date>TestEmptyNotIList();
        FirstTest.<FirstTest>TestEmptyNotIList();
    }

    @Test
    public void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.first());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 3;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.first());
    }

    @Test
    public void EmptySource() {
        int[] source = {};

        assertThrows(InvalidOperationException.class, () -> Linq.asEnumerable(source).first(x -> true));
        assertThrows(InvalidOperationException.class, () -> Linq.asEnumerable(source).first(x -> false));
    }

    @Test
    public void OneElementTruePredicate() {
        int[] source = {4};
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).first(predicate));
    }

    @Test
    public void ManyElementsPredicateFalseForAll() {
        int[] source = {9, 5, 1, 3, 17, 21};
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;

        assertThrows(InvalidOperationException.class, () -> Linq.asEnumerable(source).first(predicate));
    }

    @Test
    public void PredicateTrueOnlyForLast() {
        int[] source = {9, 5, 1, 3, 17, 21, 50};
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).first(predicate));
    }

    @Test
    public void PredicateTrueForSome() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 10;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).first(predicate));
    }

    @Test
    public void PredicateTrueForSomeRunOnce() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 10;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).runOnce().first(predicate));
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).first());
    }

    @Test
    public void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).first(i -> i != 2));
    }

    @Test
    public void NullPredicate() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).first(predicate));
    }

    @Test
    public void testFirst() {
        Employee e = emps[0];
        Assert.assertEquals(e, emps[0]);
        Assert.assertEquals(e, Linq.asEnumerable(emps).first());

        Department d = depts[0];
        Assert.assertEquals(d, depts[0]);
        Assert.assertEquals(d, Linq.asEnumerable(depts).first());

        try {
            String s = Linq.<String>empty().first();
            Assert.fail("expected exception, got " + s);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first throws
        try {
            Long num = Linq.asEnumerable(new IterableDemo(0)).first();
            Assert.fail("expected exception, got " + num);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first does not throw
        final Long num = Linq.asEnumerable(new IterableDemo(1)).first();
        Assert.assertEquals((Long) 1L, num);
    }

    @Test
    public void testFirstPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).first(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
    }
}
