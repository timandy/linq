package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by 许崇雷 on 2019-05-17.
 */
public class LastOrDefaultTest extends EnumerableTest {
    @Test
    public void SameResultsrepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        Assert.assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    @Test
    public void SameResultsrepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        Assert.assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    private <T> void TestEmptyIList() {
        IEnumerable<T> source = Linq.asEnumerable(Collections.EMPTY_LIST);
        T expected = null;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, source.runOnce().lastOrDefault());
    }

    @Test
    public void EmptyIListT() {
        this.<Integer>TestEmptyIList();
        this.<String>TestEmptyIList();
        this.<Date>TestEmptyIList();
        this.<LastOrDefaultTest>TestEmptyIList();
    }

    @Test
    public void IListTOneElement() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{5});
        int expected = 5;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, (int) source.lastOrDefault());
    }

    @Test
    public void IListTManyElementsLastIsDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(-10, 2, 4, 3, 0, 2, null);
        Integer expected = null;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, source.lastOrDefault());
    }

    @Test
    public void IListTManyElementsLastIsNotDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(-10, 2, 4, 3, 0, 2, null, 19);
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, source.lastOrDefault());
    }

    private <T> IEnumerable<T> EmptySource() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySource();
        T expected = null;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, source.runOnce().lastOrDefault());
    }

    @Test
    public void EmptyNotIListT() {
        this.<Integer>TestEmptyNotIList();
        this.<String>TestEmptyNotIList();
        this.<Date>TestEmptyNotIList();
        this.<LastOrDefaultTest>TestEmptyNotIList();
    }

    @Test
    public void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.lastOrDefault());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 12;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.lastOrDefault());
    }

    @Test
    public void EmptyIListSource() {
        IEnumerable<Integer> source = Linq.empty();

        Assert.assertNull(source.lastOrDefault(x -> true));
        Assert.assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    public void OneElementIListTruePredicate() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{4});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void ManyElementsIListPredicateFalseForAll() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        Integer expected = null;

        Assert.assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21, 50});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueForSome() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.runOnce().lastOrDefault(predicate));
    }

    @Test
    public void EmptyNotIListSource() {
        IEnumerable<Integer> source = Linq.repeat(4, 0);

        Assert.assertNull(source.lastOrDefault(x -> true));
        Assert.assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    public void OneElementNotIListTruePredicate() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{4}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void ManyElementsNotIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        Integer expected = null;

        Assert.assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSome() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.runOnce().lastOrDefault(predicate));
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).lastOrDefault());
    }

    @Test
    public void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).lastOrDefault(i -> i != 2));
    }

    @Test
    public void NullPredicate() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).lastOrDefault(predicate));
    }

    @Test
    public void testLastOrDefault() {
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("mitch", enumerable.lastOrDefault());

        IEnumerable emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        Assert.assertNull(emptyEnumerable.lastOrDefault());
    }

    @Test
    public void testLastOrDefaultWithPredicate() {
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch", "ming"));
        Assert.assertEquals("mitch", enumerable.lastOrDefault(x -> x.startsWith("mit")));
        Assert.assertNull(enumerable.lastOrDefault(x -> false));
        IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        Assert.assertNull(emptyEnumerable.lastOrDefault(x -> {
            Assert.fail("should not run at here");
            return false;
        }));
    }
}
