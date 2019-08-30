package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by 许崇雷 on 2019-05-17.
 */
class LastOrDefaultTest extends TestCase {
    private static <T> void TestEmptyIList() {
        IEnumerable<T> source = Linq.of(Collections.EMPTY_LIST);
        T expected = null;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.runOnce().lastOrDefault());
    }

    private static <T> IEnumerable<T> EmptySource() {
        return Linq.empty();
    }

    private static <T> void TestEmptyNotIList() {
        IEnumerable<T> source = EmptySource();
        T expected = null;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.runOnce().lastOrDefault());
    }

    @Test
    void SameResultsrepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    @Test
    void SameResultsrepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.lastOrDefault(), q.lastOrDefault());
    }

    @Test
    void EmptyIListT() {
        LastOrDefaultTest.<Integer>TestEmptyIList();
        LastOrDefaultTest.<String>TestEmptyIList();
        LastOrDefaultTest.<Date>TestEmptyIList();
        LastOrDefaultTest.<LastOrDefaultTest>TestEmptyIList();
    }

    @Test
    void IListTOneElement() {
        IEnumerable<Integer> source = Linq.of(new int[]{5});
        int expected = 5;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    void IListTManyElementsLastIsDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null);
        Integer expected = null;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    void IListTManyElementsLastIsNotDefault() {
        IEnumerable<Integer> source = Linq.of(-10, 2, 4, 3, 0, 2, null, 19);
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, source);
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    void EmptyNotIListT() {
        LastOrDefaultTest.<Integer>TestEmptyNotIList();
        LastOrDefaultTest.<String>TestEmptyNotIList();
        LastOrDefaultTest.<Date>TestEmptyNotIList();
        LastOrDefaultTest.<LastOrDefaultTest>TestEmptyNotIList();
    }

    @Test
    void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 12;

        assertNull(as(source, IList.class));
        assertEquals(expected, source.lastOrDefault());
    }

    @Test
    void EmptyIListSource() {
        IEnumerable<Integer> source = Linq.empty();

        assertNull(source.lastOrDefault(x -> true));
        assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    void OneElementIListTruePredicate() {
        IEnumerable<Integer> source = Linq.of(new int[]{4});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void ManyElementsIListPredicateFalseForAll() {
        IEnumerable<Integer> source = Linq.of(new int[]{9, 5, 1, 3, 17, 21});
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void IListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void IListPredicateTrueForSome() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void IListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().lastOrDefault(predicate));
    }

    @Test
    void EmptyNotIListSource() {
        IEnumerable<Integer> source = Linq.repeat(4, 0);

        assertNull(source.lastOrDefault(x -> true));
        assertNull(source.lastOrDefault(x -> false));
    }

    @Test
    void OneElementNotIListTruePredicate() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{4}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 4;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void ManyElementsNotIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        Integer expected = null;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void NotIListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 50;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void NotIListPredicateTrueForSome() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.lastOrDefault(predicate));
    }

    @Test
    void NotIListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Predicate1<Integer> predicate = TestCase::IsEven;
        int expected = 18;

        assertEquals(expected, source.runOnce().lastOrDefault(predicate));
    }

    @Test
    void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).lastOrDefault());
    }

    @Test
    void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).lastOrDefault(i -> i != 2));
    }

    @Test
    void NullPredicate() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).lastOrDefault(predicate));
    }

    @Test
    void testLastOrDefault() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("mitch", enumerable.lastOrDefault());

        IEnumerable emptyEnumerable = Linq.of(Collections.emptyList());
        assertNull(emptyEnumerable.lastOrDefault());
    }

    @Test
    void testLastOrDefaultWithPredicate() {
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
