package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArrayUtils;
import org.junit.Assert;
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

        Assert.assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x));

        Assert.assertEquals(q.firstOrDefault(), q.firstOrDefault());
    }

    private <T> void TestEmptyIList(Class<T> clazz) {
        T[] source = ArrayUtils.empty(clazz);
        T expected = null;

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, Linq.asEnumerable(source).runOnce().firstOrDefault());
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

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).firstOrDefault());
    }

    @Test
    public void IListTManyElementsFirstIsDefault() {
        Integer[] source = {null, -10, 2, 4, 3, 0, 2};
        Integer expected = null;

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, Linq.asEnumerable(source).firstOrDefault());
    }

    @Test
    public void IListTManyElementsFirstIsNotDefault() {
        Integer[] source = {19, null, -10, 2, 4, 3, 0, 2};
        Integer expected = 19;

        Assert.assertTrue(IList.class.isAssignableFrom(Linq.asEnumerable(source).getClass()));
        Assert.assertEquals(expected, Linq.asEnumerable(source).firstOrDefault());
    }

    private <T> IEnumerable<T> EmptySourceGeneric() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySourceGeneric();
        T expected = null;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, source.runOnce().firstOrDefault());
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

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.firstOrDefault());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 3;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.firstOrDefault());
    }

    @Test
    public void EmptySource() {
        Integer[] source = {};

        Assert.assertNull(Linq.asEnumerable(source).firstOrDefault(x -> true));
        Assert.assertNull(Linq.asEnumerable(source).firstOrDefault(x -> false));
    }

    @Test
    public void OneElementTruePredicate() {
        int[] source = {4};
        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).firstOrDefault(predicate));
    }

    @Test
    public void ManyElementsPredicateFalseForAll() {
        int[] source = {9, 5, 1, 3, 17, 21};
        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        Integer expected = null;

        Assert.assertEquals(expected, Linq.asEnumerable(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueOnlyForLast() {
        int[] source = {9, 5, 1, 3, 17, 21, 50};
        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueForSome() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        int expected = 10;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).firstOrDefault(predicate));
    }

    @Test
    public void PredicateTrueForSomeRunOnce() {
        int[] source = {3, 7, 10, 7, 9, 2, 11, 17, 13, 8};
        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        int expected = 10;

        Assert.assertEquals(expected, (int) Linq.asEnumerable(source).runOnce().firstOrDefault(predicate));
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
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).firstOrDefault(predicate));
    }

    @Test
    public void testFirstOrDefault() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] empty = {};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[0], Linq.asEnumerable(people).firstOrDefault());
        Assert.assertEquals(numbers[0], Linq.asEnumerable(numbers).firstOrDefault());

        Assert.assertNull(Linq.asEnumerable(empty).firstOrDefault());
    }

    @Test
    public void testFirstOrDefaultPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).firstOrDefault(i -> i > 15));
        Assert.assertNull(Linq.asEnumerable(peopleWithoutCharS).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
    }
}
