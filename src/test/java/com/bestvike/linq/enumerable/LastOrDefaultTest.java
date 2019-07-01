package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by 许崇雷 on 2019-05-17.
 */
public class LastOrDefaultTest extends TestCase {
    @Test
    public void SameResultsrepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    @Test
    public void SameResultsrepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    private <T> void TestEmptyIList() {
        IEnumerable<T> source = Linq.of(Collections.EMPTY_LIST);
        T expected = null;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.runOnce().lastOrDefault());
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
        IEnumerable<Integer> source = Linq.of(new int[]{5});
        int expected = 5;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    public void IListTManyElementsLastIsDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null);
        Integer expected = null;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    public void IListTManyElementsLastIsNotDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null, 19);
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    private <T> IEnumerable<T> EmptySource() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySource();
        T expected = null;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.runOnce().lastOrDefault());
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

        assertNull(as(source, IList.class));
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 12;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    public void EmptyIListSource() {
        IEnumerable<Integer> source = Linq.empty();

        assertNull(source.lastOrDefault(x -> true));
        assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    public void OneElementIListTruePredicate() {
        IEnumerable<Integer> source = Linq.of(new int[]{4});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void ManyElementsIListPredicateFalseForAll() {
        IEnumerable<Integer> source = Linq.of(new int[]{9, 5, 1, 3, 17, 21});
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueForSome() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void IListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().lastOrDefault(predicate));
    }

    @Test
    public void EmptyNotIListSource() {
        IEnumerable<Integer> source = Linq.repeat(4, 0);

        assertNull(source.lastOrDefault(x -> true));
        assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    public void OneElementNotIListTruePredicate() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{4}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void ManyElementsNotIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSome() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().lastOrDefault(predicate));
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
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).lastOrDefault(predicate));
    }

    @Test
    public void testLastOrDefault() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("mitch", enumerable.lastOrDefault());

        IEnumerable emptyEnumerable = Linq.of(Collections.emptyList());
        assertNull(emptyEnumerable.lastOrDefault());
    }

    @Test
    public void testLastOrDefaultWithPredicate() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch", "ming"));
        assertEquals("mitch", enumerable.lastOrDefault(x -> x.startsWith("mit")));
        assertNull(enumerable.lastOrDefault(x -> false));
        IEnumerable<String> emptyEnumerable = Linq.of(Collections.emptyList());
        assertNull(emptyEnumerable.lastOrDefault(x -> {
            fail("should not run at here");
            return false;
        }));
    }
}
