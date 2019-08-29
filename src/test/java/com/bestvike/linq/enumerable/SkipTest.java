package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.Reflection;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SkipTest extends TestCase {
    private static <T> IEnumerable<T> GuaranteeNotIList(IEnumerable<T> source) {
        return source.select(x -> x);
    }

    @Test
    public void SkipSome() {
        assertEquals(Linq.range(10, 10), NumberRangeGuaranteedNotCollectionType(0, 20).skip(10));
    }

    @Test
    public void SkipSomeIList() {
        assertEquals(Linq.range(10, 10), Linq.of(NumberRangeGuaranteedNotCollectionType(0, 20).toList()).skip(10));
    }

    @Test
    public void RunOnce() {
        assertEquals(Linq.range(10, 10), Linq.range(0, 20).runOnce().skip(10));
        assertEquals(Linq.range(10, 10), Linq.of(Linq.range(0, 20).toList()).runOnce().skip(10));
    }

    @Test
    public void SkipNone() {
        assertEquals(Linq.range(0, 20), NumberRangeGuaranteedNotCollectionType(0, 20).skip(0));
    }

    @Test
    public void SkipNoneIList() {
        assertEquals(Linq.range(0, 20), Linq.of(NumberRangeGuaranteedNotCollectionType(0, 20).toList()).skip(0));
    }

    @Test
    public void SkipExcessive() {
        assertEquals(Linq.<Integer>empty(), NumberRangeGuaranteedNotCollectionType(0, 20).skip(42));
    }

    @Test
    public void SkipExcessiveIList() {
        assertEquals(Linq.<Integer>empty(), Linq.of(NumberRangeGuaranteedNotCollectionType(0, 20).toList()).skip(42));
    }

    @Test
    public void SkipAllExactly() {
        assertFalse(NumberRangeGuaranteedNotCollectionType(0, 20).skip(20).any());
    }

    @Test
    public void SkipAllExactlyIList() {
        assertFalse(Linq.of(NumberRangeGuaranteedNotCollectionType(0, 20).skip(20).toList()).any());
    }

    @Test
    public void SkipThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).skip(3));
    }

    @Test
    public void SkipThrowsOnNullIList() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Date>) null).skip(3));
        assertThrows(NullPointerException.class, () -> ((IList<Date>) null).skip(3));
    }

    @Test
    public void SkipOnEmpty() {
        assertEquals(Linq.<Integer>empty(), GuaranteeNotIList(Linq.<Integer>empty()).skip(0));
        assertEquals(Linq.<String>empty(), GuaranteeNotIList(Linq.<String>empty()).skip(-1));
        assertEquals(Linq.<Double>empty(), GuaranteeNotIList(Linq.<Double>empty()).skip(1));
    }

    @Test
    public void SkipOnEmptyIList() {
        // Enumerable.Empty does return an IList, but not guaranteed as such
        // by the spec.
        assertEquals(Linq.<Integer>empty(), Linq.of(Linq.<Integer>empty().toList()).skip(0));
        assertEquals(Linq.<String>empty(), Linq.of(Linq.<String>empty().toList()).skip(-1));
        assertEquals(Linq.<Double>empty(), Linq.of(Linq.<Double>empty().toList()).skip(1));
    }

    @Test
    public void SkipNegative() {
        assertEquals(Linq.range(0, 20), NumberRangeGuaranteedNotCollectionType(0, 20).skip(-42));
    }

    @Test
    public void SkipNegativeIList() {
        assertEquals(Linq.range(0, 20), Linq.of(NumberRangeGuaranteedNotCollectionType(0, 20).toList()).skip(-42));
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = GuaranteeNotIList(Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE));

        assertEquals(q.skip(0), q.skip(0));
    }

    @Test
    public void SameResultsRepeatCallsIntQueryIList() {
        List<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE).toList();

        assertEquals(Linq.of(q).skip(0), Linq.of(q).skip(0));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = GuaranteeNotIList(Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x)));

        assertEquals(q.skip(0), q.skip(0));
    }

    @Test
    public void SameResultsRepeatCallsStringQueryIList() {
        List<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x)).toList();

        assertEquals(Linq.of(q).skip(0), Linq.of(q).skip(0));
    }

    @Test
    public void SkipOne() {
        Integer[] source = {3, 100, 4, null, 10};
        Integer[] expected = {100, 4, null, 10};

        assertEquals(Linq.of(expected), Linq.of(source).skip(1));
    }

    @Test
    public void SkipOneNotIList() {
        Integer[] source = {3, 100, 4, null, 10};
        Integer[] expected = {100, 4, null, 10};

        assertEquals(Linq.of(expected), GuaranteeNotIList(Linq.of(source)).skip(1));
    }

    @Test
    public void SkipAllButOne() {
        Integer[] source = {3, 100, null, 4, 10};
        Integer[] expected = {10};

        assertEquals(Linq.of(expected), Linq.of(source).skip(source.length - 1));
    }

    @Test
    public void SkipAllButOneNotIList() {
        Integer[] source = {3, 100, null, 4, 10};
        Integer[] expected = {10};

        assertEquals(Linq.of(expected), GuaranteeNotIList(Linq.of(source).skip(source.length - 1)));
    }

    @Test
    public void SkipOneMoreThanAll() {
        int[] source = {3, 100, 4, 10};
        assertEmpty(Linq.of(source).skip(source.length + 1));
    }

    @Test
    public void SkipOneMoreThanAllNotIList() {
        int[] source = {3, 100, 4, 10};
        assertEmpty(GuaranteeNotIList(Linq.of(source)).skip(source.length + 1));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).skip(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIList() {
        IEnumerable<Integer> iterator = Linq.of(new int[]{0, 1, 2}).skip(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void Count() {
        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).skip(1).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).skip(1).count());
    }

    @Test
    public void FollowWithTake() {
        int[] source = new int[]{5, 6, 7, 8};
        int[] expected = new int[]{6, 7};
        assertEquals(Linq.of(expected), Linq.of(source).skip(1).take(2));
    }

    @Test
    public void FollowWithTakeNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(5, 4);
        int[] expected = new int[]{6, 7};
        assertEquals(Linq.of(expected), source.skip(1).take(2));
    }

    @Test
    public void FollowWithTakeThenMassiveTake() {
        int[] source = new int[]{5, 6, 7, 8};
        int[] expected = new int[]{7};
        assertEquals(Linq.of(expected), Linq.of(source).skip(2).take(1).take(Integer.MAX_VALUE));
    }

    @Test
    public void FollowWithTakeThenMassiveTakeNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(5, 4);
        int[] expected = new int[]{7};
        assertEquals(Linq.of(expected), source.skip(2).take(1).take(Integer.MAX_VALUE));
    }

    @Test
    public void FollowWithSkip() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        int[] expected = new int[]{4, 5, 6};
        assertEquals(Linq.of(expected), Linq.of(source).skip(1).skip(2).skip(-4));
    }

    @Test
    public void FollowWithSkipNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(1, 6);
        int[] expected = new int[]{4, 5, 6};
        assertEquals(Linq.of(expected), source.skip(1).skip(2).skip(-4));
    }

    @Test
    public void ElementAt() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> remaining = Linq.of(source).skip(2);
        assertEquals(3, remaining.elementAt(0));
        assertEquals(4, remaining.elementAt(1));
        assertEquals(6, remaining.elementAt(3));
        assertThrows(ArgumentOutOfRangeException.class, () -> remaining.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> remaining.elementAt(4));
    }

    @Test
    public void ElementAtNotIList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> remaining = source.skip(2);
        assertEquals(3, remaining.elementAt(0));
        assertEquals(4, remaining.elementAt(1));
        assertEquals(6, remaining.elementAt(3));
        assertThrows(ArgumentOutOfRangeException.class, () -> remaining.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> remaining.elementAt(4));
    }

    @Test
    public void ElementAtOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> remaining = Linq.of(source).skip(2);
        assertEquals(3, remaining.elementAtOrDefault(0));
        assertEquals(4, remaining.elementAtOrDefault(1));
        assertEquals(6, remaining.elementAtOrDefault(3));
        assertEquals(null, remaining.elementAtOrDefault(-1));
        assertEquals(null, remaining.elementAtOrDefault(4));
    }

    @Test
    public void ElementAtOrDefaultNotIList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> remaining = source.skip(2);
        assertEquals(3, remaining.elementAtOrDefault(0));
        assertEquals(4, remaining.elementAtOrDefault(1));
        assertEquals(6, remaining.elementAtOrDefault(3));
        assertEquals(null, remaining.elementAtOrDefault(-1));
        assertEquals(null, remaining.elementAtOrDefault(4));
    }

    @Test
    public void First() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).skip(0).first());
        assertEquals(3, Linq.of(source).skip(2).first());
        assertEquals(5, Linq.of(source).skip(4).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).first());
    }

    @Test
    public void FirstNotIList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.skip(0).first());
        assertEquals(3, source.skip(2).first());
        assertEquals(5, source.skip(4).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).first());
    }

    @Test
    public void FirstOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).skip(0).firstOrDefault());
        assertEquals(3, Linq.of(source).skip(2).firstOrDefault());
        assertEquals(5, Linq.of(source).skip(4).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).firstOrDefault());
    }

    @Test
    public void FirstOrDefaultNotIList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.skip(0).firstOrDefault());
        assertEquals(3, source.skip(2).firstOrDefault());
        assertEquals(5, source.skip(4).firstOrDefault());
        assertEquals(null, source.skip(5).firstOrDefault());
    }

    @Test
    public void Last() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(5, Linq.of(source).skip(0).last());
        assertEquals(5, Linq.of(source).skip(1).last());
        assertEquals(5, Linq.of(source).skip(4).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).last());
    }

    @Test
    public void LastNotList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(5, source.skip(0).last());
        assertEquals(5, source.skip(1).last());
        assertEquals(5, source.skip(4).last());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).last());
    }

    @Test
    public void LastOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(5, Linq.of(source).skip(0).lastOrDefault());
        assertEquals(5, Linq.of(source).skip(1).lastOrDefault());
        assertEquals(5, Linq.of(source).skip(4).lastOrDefault());
        assertEquals(null, Linq.of(source).skip(5).lastOrDefault());
    }

    @Test
    public void LastOrDefaultNotList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(5, source.skip(0).lastOrDefault());
        assertEquals(5, source.skip(1).lastOrDefault());
        assertEquals(5, source.skip(4).lastOrDefault());
        assertEquals(null, source.skip(5).lastOrDefault());
    }

    @Test
    public void ToArray() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).skip(0).toArray());
        assertEquals(Linq.of(new int[]{2, 3, 4, 5}), Linq.of(source).skip(1).toArray());
        assertEquals(5, Linq.of(source).skip(4).toArray().single());
        assertEmpty(Linq.of(source).skip(5).toArray());
        assertEmpty(Linq.of(source).skip(40).toArray());
    }

    @Test
    public void ToArrayNotList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.skip(0).toArray());
        assertEquals(Linq.of(new int[]{2, 3, 4, 5}), source.skip(1).toArray());
        assertEquals(5, source.skip(4).toArray().single());
        assertEmpty(source.skip(5).toArray());
        assertEmpty(source.skip(40).toArray());
    }

    @Test
    public void ToList() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).skip(0).toList()));
        assertEquals(Linq.of(new int[]{2, 3, 4, 5}), Linq.of(Linq.of(source).skip(1).toList()));
        assertEquals(5, Linq.of(Linq.of(source).skip(4).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).skip(5).toList()));
        assertEmpty(Linq.of(Linq.of(source).skip(40).toList()));
    }

    @Test
    public void ToListNotList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.skip(0).toList()));
        assertEquals(Linq.of(new int[]{2, 3, 4, 5}), Linq.of(source.skip(1).toList()));
        assertEquals(5, Linq.of(source.skip(4).toList()).single());
        assertEmpty(Linq.of(source.skip(5).toList()));
        assertEmpty(Linq.of(source.skip(40).toList()));
    }

    @Test
    public void RepeatEnumerating() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerable<Integer> remaining = Linq.of(source).skip(1);
        assertEquals(remaining, remaining);
    }

    @Test
    public void RepeatEnumeratingNotList() {
        IEnumerable<Integer> source = GuaranteeNotIList(Linq.of(new int[]{1, 2, 3, 4, 5}));
        IEnumerable<Integer> remaining = source.skip(1);
        assertEquals(remaining, remaining);
    }

    @Test
    public void LazySkipMoreThan32Bits() {
        IEnumerable<Integer> range = NumberRangeGuaranteedNotCollectionType(1, 100);
        IEnumerable<Integer> skipped = range.skip(50).skip(Integer.MAX_VALUE); // Could cause an integer overflow.
        assertEmpty(skipped);
        assertEquals(0, skipped.count());
        assertEmpty(skipped.toArray());
        assertEmpty(Linq.of(skipped.toList()));
    }

    @Test
    public void IteratorStateShouldNotChangeIfNumberOfElementsIsUnbounded() throws IllegalAccessException {
        // With https://github.com/dotnet/corefx/pull/13628, Skip and Take return
        // the same type of iterator. For Take, there is a limit, or upper bound,
        // on how many items can be returned from the iterator. An integer field,
        // _state, is incremented to keep track of this and to stop enumerating once
        // we pass that limit. However, for Skip, there is no such limit and the
        // iterator can contain an unlimited number of items (including past Integer.MAX_VALUE).

        // This test makes sure that, in Skip, _state is not incorrectly incremented,
        // so that it does not overflow to a negative number and enumeration does not
        // stop prematurely.

        IEnumerator<Integer> iterator = new FastInfiniteEnumerator<Integer>().skip(1).enumerator();
        iterator.moveNext(); // Make sure the underlying enumerator has been initialized.

        Field state = Linq.of(Reflection.getFields(iterator.getClass())).single(a -> a.getName().equals("state"));
        // On platforms that do not have this change, the optimization may not be present
        // and the iterator may not have a field named _state. In that case, nop.
        if (state != null) {
            state.set(iterator, Integer.MAX_VALUE);

            for (int i = 0; i < 10; i++) {
                assertTrue(iterator.moveNext());
            }
        }
    }

    @Test
    public void DisposeSource() {
        this.DisposeSource(0, -1);
        this.DisposeSource(0, 0);
        this.DisposeSource(1, 0);
        this.DisposeSource(2, 1);
        this.DisposeSource(2, 2);
        this.DisposeSource(2, 3);
    }

    private void DisposeSource(int sourceCount, int count) {
        ref<Integer> state = ref.init(0);

        IEnumerable<Integer> source = new DelegateIterator<>(
                () -> ++state.value <= sourceCount,
                () -> 0,
                () -> state.value = -1);

        IEnumerator<Integer> iterator = source.skip(count).enumerator();
        int iteratorCount = Math.max(0, sourceCount - Math.max(0, count));
        assertAll(Linq.range(0, iteratorCount), x -> assertTrue(iterator.moveNext()));

        assertFalse(iterator.moveNext());
        assertEquals(-1, state.value);
    }

    @Test
    public void testSkip() {
        assertEquals(2, Linq.of(depts).skip(1).count());
        assertEquals(0, Linq.of(depts).skip(3).count());

        try (IEnumerator<Department> e = Linq.of(depts).skip(3).enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testIList_Skip() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).skip(1);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(49, 9, -100, 24, 25, -1, 0), source.select(x -> x / 2));
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2, 0), source.toArray());
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2, 0), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(98, 18, -200, 48, 50, -2, 0), source.toList());
        assertEquals(7, source.count());
        assertEquals(Linq.of(18, -200, 48, 50, -2, 0), source.skip(1));
        assertEquals(Linq.of(98), source.take(1));
        assertEquals(98, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(98, source.first());
        assertEquals(0, source.last());

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).skip(5);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).skip(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testIPartition_SkipLast() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).take(100).skipLast(1);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(55, 49, 9, -100, 24, 25, -1), source.select(x -> x / 2));
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), source.toArray());
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(110, 98, 18, -200, 48, 50, -2), source.toList());
        assertEquals(7, source.count());
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2), source.skip(1));
        assertEquals(Linq.of(110), source.take(1));
        assertEquals(110, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(110, source.first());
        assertEquals(-2, source.last());

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).take(100).skipLast(5);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).take(100).skipLast(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        assertIsType(Linq.empty().getClass(), emptySource2.skip(2));
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testIArrayList_SkipLast() {
        IEnumerable<Integer> source = Linq.of(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0)).skipLast(1);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(55, 49, 9, -100, 24, 25, -1), source.select(x -> x / 2));
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), source.toArray());
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(110, 98, 18, -200, 48, 50, -2), source.toList());
        assertEquals(7, source.count());
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2), source.skip(1));
        assertEquals(Linq.of(110), source.take(1));
        assertEquals(110, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(110, source.first());
        assertEquals(-2, source.last());

        IEnumerable<Integer> emptySource = Linq.of(Arrays.asList(1, 2, 3, 4, 5)).skipLast(5);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(Collections.<Integer>emptyList()).skipLast(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        assertIsType(Linq.empty().getClass(), emptySource2.skip(2));
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testIList_SkipLast() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).skipLast(1);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(55, 49, 9, -100, 24, 25, -1), source.select(x -> x / 2));
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), source.toArray());
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(110, 98, 18, -200, 48, 50, -2), source.toList());
        assertEquals(7, source.count());
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2), source.skip(1));
        assertEquals(Linq.of(110), source.take(1));
        assertEquals(110, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(110, source.first());
        assertEquals(-2, source.last());

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).skipLast(5);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).skipLast(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        assertIsType(Linq.empty().getClass(), emptySource2.skip(2));
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    public void testIEnumerable_SkipLast() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).where(x -> true).skipLast(1);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(55, 49, 9, -100, 24, 25, -1), source.select(x -> x / 2));
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), source.toArray());
        assertEquals(Linq.of(110, 98, 18, -200, 48, 50, -2), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(110, 98, 18, -200, 48, 50, -2), source.toList());
        assertEquals(7, source.count());
        assertEquals(Linq.of(98, 18, -200, 48, 50, -2), source.skip(1));
        assertEquals(Linq.of(110), source.take(1));
        assertEquals(110, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(110, source.first());
        assertEquals(-2, source.last());

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).where(x -> true).skipLast(5);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).where(x -> true).skipLast(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }
}
