package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
public class TakeWhileTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.takeWhile(x -> true), q.takeWhile(x -> true));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.takeWhile(x -> true), q.takeWhile(x -> true));
    }

    @Test
    public void SourceEmpty() {
        assertEmpty(Linq.<Integer>empty().takeWhile(e -> true));
    }

    @Test
    public void SourceEmptyIndexed() {
        assertEmpty(Linq.<Integer>empty().takeWhile((e, i) -> true));
    }

    @Test
    public void SourceNonEmptyPredicateFalseForAll() {
        int[] source = {9, 7, 15, 3, 11};
        assertEmpty(Linq.of(source).takeWhile(x -> x % 2 == 0));
    }

    @Test
    public void SourceNonEmptyPredicateFalseForAllWithIndex() {
        int[] source = {9, 7, 15, 3, 11};
        assertEmpty(Linq.of(source).takeWhile((x, i) -> x % 2 == 0));
    }

    @Test
    public void SourceNonEmptyPredicateTrueSomeFalseSecond() {
        int[] source = {8, 3, 12, 4, 6, 10};
        int[] expected = {8};

        assertEquals(Linq.of(expected), Linq.of(source).takeWhile(x -> x % 2 == 0));
    }

    @Test
    public void SourceNonEmptyPredicateTrueSomeFalseSecondWithIndex() {
        int[] source = {8, 3, 12, 4, 6, 10};
        int[] expected = {8};

        assertEquals(Linq.of(expected), Linq.of(source).takeWhile((x, i) -> x % 2 == 0));
    }

    @Test
    public void SourceNonEmptyPredicateTrueSomeFalseFirst() {
        int[] source = {3, 2, 4, 12, 6};
        assertEmpty(Linq.of(source).takeWhile(x -> x % 2 == 0));
    }

    @Test
    public void SourceNonEmptyPredicateTrueSomeFalseFirstWithIndex() {
        int[] source = {3, 2, 4, 12, 6};
        assertEmpty(Linq.of(source).takeWhile((x, i) -> x % 2 == 0));
    }

    @Test
    public void FirstTakenByIndex() {
        int[] source = {6, 2, 5, 3, 8};
        int[] expected = {6};

        assertEquals(Linq.of(expected), Linq.of(source).takeWhile((element, index) -> index == 0));
    }

    @Test
    public void AllButLastTakenByIndex() {
        int[] source = {6, 2, 5, 3, 8};
        int[] expected = {6, 2, 5, 3};

        assertEquals(Linq.of(expected), Linq.of(source).takeWhile((element, index) -> index < source.length - 1));
    }

    @Test
    public void RunOnce() {
        int[] source = {8, 3, 12, 4, 6, 10};
        int[] expected = {8};
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().takeWhile(x -> x % 2 == 0));
        int[] source2 = new int[]{6, 2, 5, 3, 8};
        int[] expected2 = new int[]{6, 2, 5, 3};
        assertEquals(Linq.of(expected2), Linq.of(source2).runOnce().takeWhile((element, index) -> index < source2.length - 1));
    }

    @Test
    public void IndexTakeWhileOverflowBeyondIntMaxValueElements() {
        IEnumerable<Integer> taken = new FastInfiniteEnumerator<Integer>().takeWhile((e, i) -> true);

        try (IEnumerator<Integer> en = taken.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    public void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.takeWhile(x -> true));
    }

    @Test
    public void ThrowsOnNullPredicate() {
        int[] source = {1, 2, 3};
        Predicate1<Integer> nullPredicate = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).takeWhile(nullPredicate));
    }

    @Test
    public void ThrowsOnNullSourceIndexed() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.takeWhile((x, i) -> true));
    }

    @Test
    public void ThrowsOnNullPredicateIndexed() {
        int[] source = {1, 2, 3};
        IndexPredicate2<Integer> nullPredicate = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).takeWhile(nullPredicate));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).takeWhile(e -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).takeWhile((e, i) -> true);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void testTakeWhile() {
        List<Department> deptList = Linq.of(depts).takeWhile(dept -> dept.name.contains("e")).toList();
        // Only one department:
        // 0: Sales --> true
        // 1: HR --> false
        // 2: Marketing --> never get to it (we stop after false)
        assertEquals(1, deptList.size());
        assertEquals(depts[0], deptList.get(0));
    }

    @Test
    public void testTakeWhileIndexed() {
        List<Department> deptList = Linq.of(depts).takeWhile((dept, index) -> index < 2).toList();
        assertEquals(2, deptList.size());
        assertEquals(depts[0], deptList.get(0));
        assertEquals(depts[1], deptList.get(1));
    }
}
