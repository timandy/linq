package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class LastTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.last(), q.last());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.last(), q.last());
    }

    private <T> void TestEmptyIList() {
        IEnumerable<T> source = Linq.of(Collections.EMPTY_LIST);

        assertNotNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().last());
    }

    @Test
    public void EmptyIListT() {
        this.<Integer>TestEmptyIList();
        this.<String>TestEmptyIList();
        this.<Date>TestEmptyIList();
        this.<LastTest>TestEmptyIList();
    }

    @Test
    public void IListTOneElement() {
        IEnumerable<Integer> source = Linq.of(new int[]{5});
        int expected = 5;

        assertNotNull(as(source, IList.class));
        assertEquals(expected, source.last());
    }

    @Test
    public void IListTManyElementsLastIsDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null);
        Integer expected = null;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.last());
    }

    @Test
    public void IListTManyElementsLastIsNotDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null, 19);
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.last());
    }

    private <T> IEnumerable<T> EmptySource() {
        return Linq.empty();
    }

    private <T> void TestEmptyNotIList() {
        IEnumerable<T> source = this.EmptySource();

        assertNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().last());
    }

    @Test
    public void EmptyNotIListT() {
        this.<Integer>TestEmptyNotIList();
        this.<String>TestEmptyNotIList();
        this.<Date>TestEmptyNotIList();
        this.<LastTest>TestEmptyNotIList();
    }

    @Test
    public void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.last());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 12;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.last());
    }

    @Test
    public void IListEmptySourcePredicate() {
        IEnumerable<Integer> source = Linq.of(Collections.EMPTY_LIST);

        assertThrows(InvalidOperationException.class, () -> source.last(x -> true));
        assertThrows(InvalidOperationException.class, () -> source.last(x -> false));
    }

    @Test
    public void OneElementIListTruePredicate() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{4}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void ManyElementsIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21}));
        Predicate1<Integer> predicate = TestCase::IsEven;

        assertThrows(InvalidOperationException.class, () -> source.last(predicate));
    }

    @Test
    public void IListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void IListPredicateTrueForSome() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void IListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().last(predicate));
    }

    @Test
    public void NotIListIListEmptySourcePredicate() {
        IEnumerable<Integer> source = Linq.range(1, 0);

        assertThrows(InvalidOperationException.class, () -> source.last(x -> true));
        assertThrows(InvalidOperationException.class, () -> source.last(x -> false));
    }

    @Test
    public void OneElementNotIListTruePredicate() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(4, 1);
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void ManyElementsNotIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21}));
        Predicate1<Integer> predicate = TestCase::IsEven;

        assertThrows(InvalidOperationException.class, () -> source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSome() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().last(predicate));
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).last());
    }

    @Test
    public void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).last(i -> i != 2));
    }

    @Test
    public void NullPredicate() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).last(predicate));
    }

    @Test
    public void testLast() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("mitch", enumerable.last());

        IEnumerable emptyEnumerable = Linq.of(Collections.emptyList());
        try {
            emptyEnumerable.last();
            fail("should not run at here");
        } catch (InvalidOperationException ignored) {
        }

        IEnumerable<String> enumerable2 = Linq.of(Collections.unmodifiableCollection(Arrays.asList("jimi", "noel", "mitch")));
        assertEquals("mitch", enumerable2.last());
    }

    @Test
    public void testLastWithPredicate() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch", "ming"));
        assertEquals("mitch", enumerable.last(x -> x.startsWith("mit")));
        try {
            enumerable.last(x -> false);
            fail("should not be here");
        } catch (InvalidOperationException ignored) {
        }

        IEnumerable<String> emptyEnumerable = Linq.of(Collections.emptyList());
        try {
            emptyEnumerable.last(x -> {
                fail("should not run at here");
                return false;
            });
            fail("should not be here");
        } catch (InvalidOperationException ignored) {
        }
    }
}
