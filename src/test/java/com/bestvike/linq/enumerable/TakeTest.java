package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.Range;
import com.bestvike.TestCase;
import com.bestvike.function.Func0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class TakeTest extends TestCase {
    private static IEnumerable<Object[]> LazySkipAllTakenForLargeNumbers_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1000);
        argsList.add(1000000);
        argsList.add(Integer.MAX_VALUE);
        return argsList;
    }

    private static IEnumerable<Object[]> CountOfLazySkipTakeChain_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(0, 0, 0);
        argsList.add(1, 1, 1);
        argsList.add(0, Integer.MAX_VALUE, 100);
        argsList.add(Integer.MAX_VALUE, 0, 0);
        argsList.add(0xffff, 1, 0);
        argsList.add(1, 0xffff, 99);
        argsList.add(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        argsList.add(1, Integer.MAX_VALUE, 99); // Regression test: The max index is precisely Integer.MAX_VALUE.
        argsList.add(0, 100, 100);
        argsList.add(10, 100, 90);
        return argsList;
    }

    private static IEnumerable<Object[]> FirstAndLastOfLazySkipTakeChain_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[]{1, 2, 3, 4}), 1, 3, 2, 4);
        argsList.add(Linq.of(new int[]{1}), 0, 1, 1, 1);
        argsList.add(Linq.of(new int[]{1, 2, 3, 5, 8, 13}), 1, Integer.MAX_VALUE, 2, 13); // Regression test: The max index is precisely Integer.MAX_VALUE.
        argsList.add(Linq.of(new int[]{1, 2, 3, 5, 8, 13}), 0, 2, 1, 2);
        argsList.add(Linq.of(new int[]{1, 2, 3, 5, 8, 13}), 500, 2, null, null);
        argsList.add(Linq.of(new int[]{}), 10, 8, null, null);
        return argsList;
    }

    private static IEnumerable<Object[]> ElementAtOfLazySkipTakeChain_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new int[]{1, 2, 3, 4, 5}, 1, 3, new int[]{-1, 0, 1, 2}, new Integer[]{null, 2, 3, 4});
        argsList.add(new int[]{0xfefe, 7000, 123}, 0, 3, new int[]{-1, 0, 1, 2}, new Integer[]{null, 0xfefe, 7000, 123});
        argsList.add(new int[]{0xfefe}, 100, 100, new int[]{-1, 0, 1, 2}, new Integer[]{null, null, null, null});
        argsList.add(new int[]{0xfefe, 123, 456, 7890, 5555, 55}, 1, 10, new int[]{-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new Integer[]{null, 123, 456, 7890, 5555, 55, null, null, null, null, null, null, null});
        return argsList;
    }

    private static IEnumerable<Object[]> DisposeSource_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(0, -1);
        argsList.add(0, 0);
        argsList.add(1, 0);
        argsList.add(2, 1);
        argsList.add(2, 2);
        argsList.add(2, 3);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.take(9), q.take(9));

        assertEquals(q.take(new Range(Index.Start, Index.fromStart(9))), q.take(new Range(Index.Start, Index.fromStart(9))));
        assertEquals(q.take(new Range(Index.fromEnd(9), Index.fromStart(9))), q.take(new Range(Index.fromEnd(9), Index.fromStart(9))));
        assertEquals(q.take(new Range(Index.Start, Index.End)), q.take(new Range(Index.Start, Index.End)));
        assertEquals(q.take(new Range(Index.fromEnd(9), Index.End)), q.take(new Range(Index.fromEnd(9), Index.End)));
    }

    @Test
    void SameResultsRepeatCallsIntQueryIList() {
        List<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE).toList();

        assertEquals(Linq.of(q).take(9), Linq.of(q).take(9));

        assertEquals(Linq.of(q).take(new Range(Index.Start, Index.fromStart(9))), Linq.of(q).take(new Range(Index.Start, Index.fromStart(9))));
        assertEquals(Linq.of(q).take(new Range(Index.fromEnd(9), Index.fromStart(9))), Linq.of(q).take(new Range(Index.fromEnd(9), Index.fromStart(9))));
        assertEquals(Linq.of(q).take(new Range(Index.Start, Index.End)), Linq.of(q).take(new Range(Index.Start, Index.End)));
        assertEquals(Linq.of(q).take(new Range(Index.fromEnd(9), Index.End)), Linq.of(q).take(new Range(Index.fromEnd(9), Index.End)));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.take(7), q.take(7));

        assertEquals(q.take(new Range(Index.Start, Index.fromStart(7))), q.take(new Range(Index.Start, Index.fromStart(7))));
        assertEquals(q.take(new Range(Index.fromEnd(7), Index.fromStart(7))), q.take(new Range(Index.fromEnd(7), Index.fromStart(7))));
        assertEquals(q.take(new Range(Index.Start, Index.End)), q.take(new Range(Index.Start, Index.End)));
        assertEquals(q.take(new Range(Index.fromEnd(7), Index.End)), q.take(new Range(Index.fromEnd(7), Index.End)));
    }

    @Test
    void SameResultsRepeatCallsStringQueryIList() {
        List<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x)).toList();

        assertEquals(Linq.of(q).take(7), Linq.of(q).take(7));

        assertEquals(Linq.of(q).take(new Range(Index.Start, Index.fromStart(7))), Linq.of(q).take(new Range(Index.Start, Index.fromStart(7))));
        assertEquals(Linq.of(q).take(new Range(Index.fromEnd(7), Index.fromStart(7))), Linq.of(q).take(new Range(Index.fromEnd(7), Index.fromStart(7))));
        assertEquals(Linq.of(q).take(new Range(Index.Start, Index.End)), Linq.of(q).take(new Range(Index.Start, Index.End)));
        assertEquals(Linq.of(q).take(new Range(Index.fromEnd(7), Index.End)), Linq.of(q).take(new Range(Index.fromEnd(7), Index.End)));
    }

    @Test
    void SourceEmptyCountPositive() {
        int[] source = {};
        assertEmpty(Linq.of(source).take(5));

        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))));
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(5))));
        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.End)));
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)));
    }

    @Test
    void SourceEmptyCountPositiveNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 0);
        assertEmpty(source.take(5));

        assertEmpty(source.take(new Range(Index.Start, Index.fromStart(5))));
        assertEmpty(source.take(new Range(Index.fromEnd(5), Index.fromStart(5))));
        assertEmpty(source.take(new Range(Index.Start, Index.End)));
        assertEmpty(source.take(new Range(Index.fromEnd(5), Index.End)));
    }

    @Test
    void SourceNonEmptyCountNegative() {
        int[] source = {2, 5, 9, 1};
        assertEmpty(Linq.of(source).take(-5));

        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(9), Index.Start)));
    }

    @Test
    void SourceNonEmptyCountNegativeNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        assertEmpty(source.take(-5));

        assertEmpty(source.take(new Range(Index.fromEnd(9), Index.Start)));
    }

    @Test
    void SourceNonEmptyCountZero() {
        int[] source = {2, 5, 9, 1};
        assertEmpty(Linq.of(source).take(0));

        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.Start)));
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(4), Index.Start)));
        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))));
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromEnd(4))));
    }

    @Test
    void SourceNonEmptyCountZeroNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        assertEmpty(source.take(0));

        assertEmpty(source.take(new Range(Index.Start, Index.Start)));
        assertEmpty(source.take(new Range(Index.fromEnd(4), Index.Start)));
        assertEmpty(source.take(new Range(Index.Start, Index.fromEnd(4))));
        assertEmpty(source.take(new Range(Index.fromEnd(4), Index.fromEnd(4))));
    }

    @Test
    void SourceNonEmptyCountOne() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2};

        assertEquals(Linq.of(expected), Linq.of(source).take(1));

        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromStart(1))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(3))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromEnd(3))));
    }

    @Test
    void SourceNonEmptyCountOneNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        int[] expected = {2};

        assertEquals(Linq.of(expected), source.take(1));

        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(4), Index.fromStart(1))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.fromEnd(3))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(4), Index.fromEnd(3))));
    }

    @Test
    void SourceNonEmptyTakeAllExactly() {
        int[] source = {2, 5, 9, 1};

        assertEquals(Linq.of(source), Linq.of(source).take(source.length));

        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.Start, Index.fromStart(source.length))));
        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.fromEnd(source.length), Index.fromStart(source.length))));
        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.Start, Index.End)));
        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.fromEnd(source.length), Index.End)));
    }

    @Test
    void SourceNonEmptyTakeAllExactlyNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));

        assertEquals(source, source.take(source.count()));

        assertEquals(source, source.take(new Range(Index.Start, Index.fromStart(source.count()))));
        assertEquals(source, source.take(new Range(Index.fromEnd(source.count()), Index.fromStart(source.count()))));
        assertEquals(source, source.take(new Range(Index.Start, Index.End)));
        assertEquals(source, source.take(new Range(Index.fromEnd(source.count()), Index.End)));
    }

    @Test
    void SourceNonEmptyTakeAllButOne() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), Linq.of(source).take(3));

        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromStart(3))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromStart(3))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromEnd(1))));
    }

    @Test
    void RunOnce() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(3));

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(new Range(Index.Start, Index.fromStart(3))));
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(new Range(Index.fromEnd(4), Index.fromStart(3))));
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(new Range(Index.Start, Index.fromEnd(1))));
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(new Range(Index.fromEnd(4), Index.fromEnd(1))));
    }

    @Test
    void SourceNonEmptyTakeAllButOneNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), source.runOnce().take(3));

        assertEquals(Linq.of(expected), source.runOnce().take(new Range(Index.Start, Index.fromStart(3))));
        assertEquals(Linq.of(expected), source.runOnce().take(new Range(Index.fromEnd(4), Index.fromStart(3))));
        assertEquals(Linq.of(expected), source.runOnce().take(new Range(Index.Start, Index.fromEnd(1))));
        assertEquals(Linq.of(expected), source.runOnce().take(new Range(Index.fromEnd(4), Index.fromEnd(1))));
    }

    @Test
    void SourceNonEmptyTakeExcessive() {
        Integer[] source = {2, 5, null, 9, 1};

        assertEquals(Linq.of(source), Linq.of(source).take(source.length + 1));

        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.Start, Index.fromStart(source.length + 1))));
        assertEquals(Linq.of(source), Linq.of(source).take(new Range(Index.fromEnd(source.length + 1), Index.fromStart(source.length + 1))));
    }

    @Test
    void SourceNonEmptyTakeExcessiveNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(2, 5, null, 9, 1));

        assertEquals(source, source.take(source.count() + 1));

        assertEquals(source, source.take(new Range(Index.Start, Index.fromStart(source.count() + 1))));
        assertEquals(source, source.take(new Range(Index.fromEnd(source.count() + 1), Index.fromStart(source.count() + 1))));
    }

    @Test
    void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.take(5));

        assertThrows(NullPointerException.class, () -> source.take(new Range(Index.Start, Index.fromStart(5))));
        assertThrows(NullPointerException.class, () -> source.take(new Range(Index.fromEnd(5), Index.fromStart(5))));
        assertThrows(NullPointerException.class, () -> source.take(new Range(Index.Start, Index.End)));
        assertThrows(NullPointerException.class, () -> source.take(new Range(Index.fromEnd(5), Index.End)));
    }

    @Test
    void ForcedToEnumeratorDoesNotEnumerate() {
        IEnumerable<Integer> iterator1 = NumberRangeGuaranteedNotCollectionType(0, 3).take(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en1 = (IEnumerator<Integer>) iterator1;
        assertFalse(en1 != null && en1.moveNext());

        IEnumerable<Integer> iterator2 = NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.fromStart(2)));
        IEnumerator<Integer> en2 = (IEnumerator<Integer>) iterator2;
        assertFalse(en2 != null && en2.moveNext());

        IEnumerable<Integer> iterator3 = NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.fromStart(2)));
        IEnumerator<Integer> en3 = (IEnumerator<Integer>) iterator3;
        assertFalse(en3 != null && en3.moveNext());

        IEnumerable<Integer> iterator4 = NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.fromEnd(1)));
        IEnumerator<Integer> en4 = (IEnumerator<Integer>) iterator4;
        assertFalse(en4 != null && en4.moveNext());

        IEnumerable<Integer> iterator5 = NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.fromEnd(1)));
        IEnumerator<Integer> en5 = (IEnumerator<Integer>) iterator5;
        assertFalse(en5 != null && en5.moveNext());
    }

    @Test
    void Count() {
        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(2).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(2).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(0).count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.fromStart(2))).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(new Range(Index.Start, Index.fromStart(2))).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.Start)).count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.fromStart(2))).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(new Range(Index.fromEnd(3), Index.fromStart(2))).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.Start)).count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.fromEnd(1))).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(new Range(Index.Start, Index.fromEnd(1))).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.Start, Index.fromEnd(3))).count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.fromEnd(1))).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(new Range(Index.fromEnd(3), Index.fromEnd(1))).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(new Range(Index.fromEnd(3), Index.fromEnd(3))).count());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateIList() {
        IEnumerable<Integer> iterator1 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en1 = (IEnumerator<Integer>) iterator1;
        assertFalse(en1 != null && en1.moveNext());

        IEnumerable<Integer> iterator2 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(new Range(Index.Start, Index.fromStart(2)));
        IEnumerator<Integer> en2 = (IEnumerator<Integer>) iterator2;
        assertFalse(en2 != null && en2.moveNext());

        IEnumerable<Integer> iterator3 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(new Range(Index.fromEnd(3), Index.fromStart(2)));
        IEnumerator<Integer> en3 = (IEnumerator<Integer>) iterator3;
        assertFalse(en3 != null && en3.moveNext());

        IEnumerable<Integer> iterator4 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(new Range(Index.Start, Index.fromEnd(1)));
        IEnumerator<Integer> en4 = (IEnumerator<Integer>) iterator4;
        assertFalse(en4 != null && en4.moveNext());

        IEnumerable<Integer> iterator5 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(new Range(Index.fromEnd(3), Index.fromEnd(1)));
        IEnumerator<Integer> en5 = (IEnumerator<Integer>) iterator5;
        assertFalse(en5 != null && en5.moveNext());
    }

    @Test
    void FollowWithTake() {
        int[] source = new int[]{5, 6, 7, 8};
        int[] expected = new int[]{5, 6};
        assertEquals(Linq.of(expected), Linq.of(source).take(5).take(3).take(2).take(40));

        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).take(new Range(Index.Start, Index.fromStart(3))).take(new Range(Index.Start, Index.fromStart(2))).take(new Range(Index.Start, Index.fromStart(40))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.fromStart(5))).take(new Range(Index.fromEnd(4), Index.fromStart(3))).take(new Range(Index.fromEnd(3), Index.fromStart(2))).take(new Range(Index.fromEnd(2), Index.fromStart(40))));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.End)).take(new Range(Index.Start, Index.fromEnd(1))).take(new Range(Index.Start, Index.fromEnd(1))).take(new Range(Index.Start, Index.End)));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(4), Index.End)).take(new Range(Index.fromEnd(4), Index.fromEnd(1))).take(new Range(Index.fromEnd(3), Index.fromEnd(1))).take(new Range(Index.fromEnd(2), Index.End)));
    }

    @Test
    void FollowWithTakeNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(5, 4);
        int[] expected = new int[]{5, 6};
        assertEquals(Linq.of(expected), source.take(5).take(3).take(2));

        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.fromStart(5))).take(new Range(Index.Start, Index.fromStart(3))).take(new Range(Index.Start, Index.fromStart(2))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(4), Index.fromStart(5))).take(new Range(Index.fromEnd(4), Index.fromStart(3))).take(new Range(Index.fromEnd(3), Index.fromStart(2))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.End)).take(new Range(Index.Start, Index.fromEnd(1))).take(new Range(Index.Start, Index.fromEnd(1))));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(4), Index.End)).take(new Range(Index.fromEnd(4), Index.fromEnd(1))).take(new Range(Index.fromEnd(3), Index.fromEnd(1))));
    }

    @Test
    void FollowWithSkip() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        int[] expected = new int[]{3, 4, 5};
        assertEquals(Linq.of(expected), Linq.of(source).take(5).skip(2).skip(-4));

        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromStart(5))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromEnd(1))).skip(2).skip(-4));
    }

    @Test
    void FollowWithSkipNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(1, 6);
        int[] expected = new int[]{3, 4, 5};
        assertEquals(Linq.of(expected), source.take(5).skip(2).skip(-4));

        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.fromStart(5))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(6), Index.fromStart(5))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), source.take(new Range(Index.Start, Index.fromEnd(1))).skip(2).skip(-4));
        assertEquals(Linq.of(expected), source.take(new Range(Index.fromEnd(6), Index.fromEnd(1))).skip(2).skip(-4));
    }

    @Test
    void ElementAt() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> taken0 = Linq.of(source).take(3);
        assertEquals(1, taken0.elementAt(0));
        assertEquals(3, taken0.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(3));

        IEnumerable<Integer> taken1 = Linq.of(source).take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(1, taken1.elementAt(0));
        assertEquals(3, taken1.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken1.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken1.elementAt(3));

        IEnumerable<Integer> taken2 = Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        assertEquals(1, taken2.elementAt(0));
        assertEquals(3, taken2.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken2.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken2.elementAt(3));

        IEnumerable<Integer> taken3 = Linq.of(source).take(new Range(Index.Start, Index.fromEnd(3)));
        assertEquals(1, taken3.elementAt(0));
        assertEquals(3, taken3.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken3.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken3.elementAt(3));

        IEnumerable<Integer> taken4 = Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        assertEquals(1, taken4.elementAt(0));
        assertEquals(3, taken4.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken4.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken4.elementAt(3));
    }

    @Test
    void ElementAtNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> taken0 = source.take(3);
        assertEquals(1, taken0.elementAt(0));
        assertEquals(3, taken0.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(3));

        IEnumerable<Integer> taken1 = source.take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(1, taken1.elementAt(0));
        assertEquals(3, taken1.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken1.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken1.elementAt(3));

        IEnumerable<Integer> taken2 = source.take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        assertEquals(1, taken2.elementAt(0));
        assertEquals(3, taken2.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken2.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken2.elementAt(3));

        IEnumerable<Integer> taken3 = source.take(new Range(Index.Start, Index.fromEnd(3)));
        assertEquals(1, taken3.elementAt(0));
        assertEquals(3, taken3.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken3.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken3.elementAt(3));

        IEnumerable<Integer> taken4 = source.take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        assertEquals(1, taken4.elementAt(0));
        assertEquals(3, taken4.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken4.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken4.elementAt(3));
    }

    @Test
    void ElementAtOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> taken0 = Linq.of(source).take(3);
        assertEquals(1, taken0.elementAtOrDefault(0));
        assertEquals(3, taken0.elementAtOrDefault(2));
        assertEquals(null, taken0.elementAtOrDefault(-1));
        assertEquals(null, taken0.elementAtOrDefault(3));

        IEnumerable<Integer> taken1 = Linq.of(source).take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(1, taken1.elementAtOrDefault(0));
        assertEquals(3, taken1.elementAtOrDefault(2));
        assertEquals(null, taken1.elementAtOrDefault(-1));
        assertEquals(null, taken1.elementAtOrDefault(3));

        IEnumerable<Integer> taken2 = Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        assertEquals(1, taken2.elementAtOrDefault(0));
        assertEquals(3, taken2.elementAtOrDefault(2));
        assertEquals(null, taken2.elementAtOrDefault(-1));
        assertEquals(null, taken2.elementAtOrDefault(3));

        IEnumerable<Integer> taken3 = Linq.of(source).take(new Range(Index.Start, Index.fromEnd(3)));
        assertEquals(1, taken3.elementAtOrDefault(0));
        assertEquals(3, taken3.elementAtOrDefault(2));
        assertEquals(null, taken3.elementAtOrDefault(-1));
        assertEquals(null, taken3.elementAtOrDefault(3));

        IEnumerable<Integer> taken4 = Linq.of(source).take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        assertEquals(1, taken4.elementAtOrDefault(0));
        assertEquals(3, taken4.elementAtOrDefault(2));
        assertEquals(null, taken4.elementAtOrDefault(-1));
        assertEquals(null, taken4.elementAtOrDefault(3));
    }

    @Test
    void ElementAtOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> taken0 = source.take(3);
        assertEquals(1, taken0.elementAtOrDefault(0));
        assertEquals(3, taken0.elementAtOrDefault(2));
        assertEquals(null, taken0.elementAtOrDefault(-1));
        assertEquals(null, taken0.elementAtOrDefault(3));

        IEnumerable<Integer> taken1 = source.take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(1, taken1.elementAtOrDefault(0));
        assertEquals(3, taken1.elementAtOrDefault(2));
        assertEquals(null, taken1.elementAtOrDefault(-1));
        assertEquals(null, taken1.elementAtOrDefault(3));

        IEnumerable<Integer> taken2 = source.take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        assertEquals(1, taken2.elementAtOrDefault(0));
        assertEquals(3, taken2.elementAtOrDefault(2));
        assertEquals(null, taken2.elementAtOrDefault(-1));
        assertEquals(null, taken2.elementAtOrDefault(3));

        IEnumerable<Integer> taken3 = source.take(new Range(Index.Start, Index.fromEnd(3)));
        assertEquals(1, taken3.elementAtOrDefault(0));
        assertEquals(3, taken3.elementAtOrDefault(2));
        assertEquals(null, taken3.elementAtOrDefault(-1));
        assertEquals(null, taken3.elementAtOrDefault(3));

        IEnumerable<Integer> taken4 = source.take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        assertEquals(1, taken4.elementAtOrDefault(0));
        assertEquals(3, taken4.elementAtOrDefault(2));
        assertEquals(null, taken4.elementAtOrDefault(-1));
        assertEquals(null, taken4.elementAtOrDefault(3));
    }

    @Test
    void First() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).first());
        assertEquals(1, Linq.of(source).take(4).first());
        assertEquals(1, Linq.of(source).take(40).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(0).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(10).first());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(4))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.Start, Index.Start)).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(new Range(Index.Start, Index.fromStart(10))).first());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(4))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(new Range(Index.fromEnd(5), Index.fromStart(10))).first());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.End)).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(new Range(Index.Start, Index.fromEnd(5))).first());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(1))).first());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(new Range(Index.fromEnd(10), Index.End)).first());
    }

    @Test
    void FirstNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).first());
        assertEquals(1, source.take(4).first());
        assertEquals(1, source.take(40).first());
        assertThrows(InvalidOperationException.class, () -> source.take(0).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(10).first());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(1))).first());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(4))).first());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(40))).first());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.Start, Index.Start)).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(new Range(Index.Start, Index.fromStart(10))).first());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).first());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(4))).first());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).first());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.fromEnd(5), Index.Start)).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(new Range(Index.fromEnd(5), Index.fromStart(10))).first());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(4))).first());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(1))).first());
        assertEquals(1, source.take(new Range(Index.Start, Index.End)).first());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.Start, Index.fromEnd(5))).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(new Range(Index.Start, Index.fromEnd(5))).first());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).first());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(1))).first());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.End)).first());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(new Range(Index.fromEnd(10), Index.End)).first());
    }

    @Test
    void FirstOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).firstOrDefault());
        assertEquals(1, Linq.of(source).take(4).firstOrDefault());
        assertEquals(1, Linq.of(source).take(40).firstOrDefault());
        assertEquals(null, Linq.of(source).take(0).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(10).firstOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(4))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).firstOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.Start, Index.Start)).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(new Range(Index.Start, Index.fromStart(10))).firstOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(4))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).firstOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(new Range(Index.fromEnd(10), Index.fromStart(10))).firstOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.End)).firstOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(new Range(Index.Start, Index.fromEnd(10))).firstOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(1))).firstOrDefault());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).firstOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(new Range(Index.fromEnd(10), Index.End)).firstOrDefault());
    }

    @Test
    void FirstOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).firstOrDefault());
        assertEquals(1, source.take(4).firstOrDefault());
        assertEquals(1, source.take(40).firstOrDefault());
        assertEquals(null, source.take(0).firstOrDefault());
        assertEquals(null, source.skip(5).take(10).firstOrDefault());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(1))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(4))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(40))).firstOrDefault());
        assertEquals(null, source.take(new Range(Index.Start, Index.Start)).firstOrDefault());
        assertEquals(null, source.skip(5).take(new Range(Index.Start, Index.fromStart(10))).firstOrDefault());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(4))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).firstOrDefault());
        assertEquals(null, source.take(new Range(Index.fromEnd(5), Index.Start)).firstOrDefault());
        assertEquals(null, source.skip(5).take(new Range(Index.fromEnd(10), Index.fromStart(10))).firstOrDefault());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(4))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(1))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.Start, Index.End)).firstOrDefault());
        assertEquals(null, source.take(new Range(Index.Start, Index.fromEnd(5))).firstOrDefault());
        assertEquals(null, source.skip(5).take(new Range(Index.Start, Index.fromEnd(10))).firstOrDefault());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(1))).firstOrDefault());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.End)).firstOrDefault());
        assertEquals(null, source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).firstOrDefault());
        assertEquals(null, source.skip(5).take(new Range(Index.fromEnd(10), Index.End)).firstOrDefault());
    }

    @Test
    void Last() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).last());
        assertEquals(5, Linq.of(source).take(5).last());
        assertEquals(5, Linq.of(source).take(40).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(0).last());
        assertThrows(InvalidOperationException.class, () -> Linq.empty().take(40).last());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.Start, Index.Start)).last());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().take(new Range(Index.Start, Index.fromStart(40))).last());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(5))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).last());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().take(new Range(Index.fromEnd(5), Index.fromStart(40))).last());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.End)).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromStart(3), Index.End)).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).last());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().take(new Range(Index.Start, Index.End)).last());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).last());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).last());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().take(new Range(Index.fromEnd(40), Index.End)).last());
    }

    @Test
    void LastNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).last());
        assertEquals(5, source.take(5).last());
        assertEquals(5, source.take(40).last());
        assertThrows(InvalidOperationException.class, () -> source.take(0).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.empty()).take(40).last());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(1))).last());
        assertEquals(5, source.take(new Range(Index.Start, Index.fromStart(5))).last());
        assertEquals(5, source.take(new Range(Index.Start, Index.fromStart(40))).last());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.Start, Index.Start)).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.Start, Index.fromStart(40))).last());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).last());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.fromStart(5))).last());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).last());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.fromEnd(5), Index.Start)).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.fromEnd(5), Index.fromStart(40))).last());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(4))).last());
        assertEquals(5, source.take(new Range(Index.Start, Index.End)).last());
        assertEquals(5, source.take(new Range(Index.fromStart(3), Index.End)).last());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.Start, Index.fromEnd(5))).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.Start, Index.End)).last());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).last());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.End)).last());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.End)).last());
        assertThrows(InvalidOperationException.class, () -> source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.fromEnd(40), Index.End)).last());
    }

    @Test
    void LastOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).lastOrDefault());
        assertEquals(5, Linq.of(source).take(5).lastOrDefault());
        assertEquals(5, Linq.of(source).take(40).lastOrDefault());
        assertEquals(null, Linq.of(source).take(0).lastOrDefault());
        assertEquals(null, Linq.empty().take(40).lastOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).lastOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.Start, Index.Start)).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().take(new Range(Index.Start, Index.fromStart(40))).lastOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(5))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).lastOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().take(new Range(Index.fromEnd(5), Index.fromStart(40))).lastOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.Start, Index.End)).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromStart(3), Index.End)).lastOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().take(new Range(Index.Start, Index.End)).lastOrDefault());

        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).lastOrDefault());
        assertEquals(5, Linq.of(source).take(new Range(Index.fromEnd(40), Index.End)).lastOrDefault());
        assertEquals(null, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().take(new Range(Index.fromEnd(40), Index.End)).lastOrDefault());
    }

    @Test
    void LastOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).lastOrDefault());
        assertEquals(5, source.take(5).lastOrDefault());
        assertEquals(5, source.take(40).lastOrDefault());
        assertEquals(null, source.take(0).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.empty()).take(40).lastOrDefault());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(1))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.Start, Index.fromStart(5))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.Start, Index.fromStart(40))).lastOrDefault());
        assertEquals(null, source.take(new Range(Index.Start, Index.Start)).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.Start, Index.fromStart(40))).lastOrDefault());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.fromStart(5))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).lastOrDefault());
        assertEquals(null, source.take(new Range(Index.fromEnd(5), Index.Start)).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.fromEnd(5), Index.fromStart(40))).lastOrDefault());

        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(4))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.Start, Index.End)).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.fromStart(3), Index.End)).lastOrDefault());
        assertEquals(null, source.take(new Range(Index.Start, Index.fromEnd(5))).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.Start, Index.End)).lastOrDefault());

        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.fromEnd(5), Index.End)).lastOrDefault());
        assertEquals(5, source.take(new Range(Index.fromEnd(40), Index.End)).lastOrDefault());
        assertEquals(null, source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.<Integer>empty()).take(new Range(Index.fromEnd(40), Index.End)).lastOrDefault());
    }

    @Test
    void ToArray() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(5).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(6).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(40).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source).take(4).toArray());
        assertEquals(1, Linq.of(source).take(1).toArray().single());
        assertEmpty(Linq.of(source).take(0).toArray());
        assertEmpty(Linq.of(source).take(-10).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(6))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(4))).toArray());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).toArray().single());
        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.Start)).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(5))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(6))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(4))).toArray());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).toArray().single());
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).toArray());
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(15), Index.Start)).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.Start, Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))).toArray());
        assertEquals(1, Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).toArray().single());
        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).toArray());
        assertEmpty(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(15))).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(6), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source).take(new Range(Index.fromEnd(45), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(1))).toArray());
        assertEquals(1, Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).toArray().single());
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).toArray());
        assertEmpty(Linq.of(source).take(new Range(Index.fromEnd(15), Index.fromEnd(5))).toArray());
    }

    @Test
    void ToArrayNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(5).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(6).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(40).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.take(4).toArray());
        assertEquals(1, source.take(1).toArray().single());
        assertEmpty(source.take(0).toArray());
        assertEmpty(source.take(-10).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.Start, Index.fromStart(5))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.Start, Index.fromStart(6))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.Start, Index.fromStart(40))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.take(new Range(Index.Start, Index.fromStart(4))).toArray());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromStart(1))).toArray().single());
        assertEmpty(source.take(new Range(Index.Start, Index.Start)).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(5), Index.fromStart(5))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(5), Index.fromStart(6))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.take(new Range(Index.fromEnd(5), Index.fromStart(4))).toArray());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).toArray().single());
        assertEmpty(source.take(new Range(Index.fromEnd(5), Index.Start)).toArray());
        assertEmpty(source.take(new Range(Index.fromEnd(15), Index.Start)).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.Start, Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.take(new Range(Index.Start, Index.fromEnd(1))).toArray());
        assertEquals(1, source.take(new Range(Index.Start, Index.fromEnd(4))).toArray().single());
        assertEmpty(source.take(new Range(Index.Start, Index.fromEnd(5))).toArray());
        assertEmpty(source.take(new Range(Index.Start, Index.fromEnd(15))).toArray());

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(5), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(6), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), source.take(new Range(Index.fromEnd(45), Index.End)).toArray());
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.take(new Range(Index.fromEnd(5), Index.fromEnd(1))).toArray());
        assertEquals(1, source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).toArray().single());
        assertEmpty(source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).toArray());
        assertEmpty(source.take(new Range(Index.fromEnd(15), Index.fromEnd(5))).toArray());
    }

    @Test
    void ToList() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(5).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(6).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(40).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(Linq.of(source).take(4).toList()));
        assertEquals(1, Linq.of(Linq.of(source).take(1).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).take(0).toList()));
        assertEmpty(Linq.of(Linq.of(source).take(-10).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromStart(5))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromStart(6))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromStart(40))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromStart(4))).toList()));
        assertEquals(1, Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.Start, Index.Start)).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(5))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(6))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(40))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(4))).toList()));
        assertEquals(1, Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.Start)).toList()));
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.fromEnd(15), Index.Start)).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(1))).toList()));
        assertEquals(1, Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(5))).toList()));
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.Start, Index.fromEnd(15))).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(6), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(45), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(1))).toList()));
        assertEquals(1, Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))).toList()).single());
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(5))).toList()));
        assertEmpty(Linq.of(Linq.of(source).take(new Range(Index.fromEnd(15), Index.fromEnd(5))).toList()));
    }

    @Test
    void ToListNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(5).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(6).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(40).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source.take(4).toList()));
        assertEquals(1, Linq.of(source.take(1).toList()).single());
        assertEmpty(Linq.of(source.take(0).toList()));
        assertEmpty(Linq.of(source.take(-10).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.Start, Index.fromStart(5))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.Start, Index.fromStart(6))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.Start, Index.fromStart(40))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source.take(new Range(Index.Start, Index.fromStart(4))).toList()));
        assertEquals(1, Linq.of(source.take(new Range(Index.Start, Index.fromStart(1))).toList()).single());
        assertEmpty(Linq.of(source.take(new Range(Index.Start, Index.Start)).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromStart(5))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromStart(6))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromStart(40))).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromStart(4))).toList()));
        assertEquals(1, Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromStart(1))).toList()).single());
        assertEmpty(Linq.of(source.take(new Range(Index.fromEnd(5), Index.Start)).toList()));
        assertEmpty(Linq.of(source.take(new Range(Index.fromEnd(15), Index.Start)).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.Start, Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source.take(new Range(Index.Start, Index.fromEnd(1))).toList()));
        assertEquals(1, Linq.of(source.take(new Range(Index.Start, Index.fromEnd(4))).toList()).single());
        assertEmpty(Linq.of(source.take(new Range(Index.Start, Index.fromEnd(5))).toList()));
        assertEmpty(Linq.of(source.take(new Range(Index.Start, Index.fromEnd(15))).toList()));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(6), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5}), Linq.of(source.take(new Range(Index.fromEnd(45), Index.End)).toList()));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromEnd(1))).toList()));
        assertEquals(1, Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))).toList()).single());
        assertEmpty(Linq.of(source.take(new Range(Index.fromEnd(5), Index.fromEnd(5))).toList()));
        assertEmpty(Linq.of(source.take(new Range(Index.fromEnd(15), Index.fromEnd(5))).toList()));
    }

    @Test
    void TakeCanOnlyBeOneList() {
        int[] source = new int[]{2, 4, 6, 8, 10};
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(1));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(1));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(3).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(3).take(1));

        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(3))).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.Start, Index.fromStart(3))).take(new Range(Index.Start, Index.fromStart(1))));

        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(new Range(Index.fromEnd(4), Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(3))).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(3))).take(new Range(Index.fromEnd(4), Index.fromStart(1))));

        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(4))));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(new Range(Index.Start, Index.fromEnd(3))));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(2))).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.Start, Index.fromEnd(2))).take(new Range(Index.Start, Index.fromEnd(2))));

        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(new Range(Index.fromEnd(4), Index.fromEnd(3))));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(2))).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(2))).take(new Range(Index.fromEnd(4), Index.fromEnd(2))));
    }

    @Test
    void TakeCanOnlyBeOneNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 4, 6, 8, 10}));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(1));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(1));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(3).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(3).take(1));

        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{4}), source.skip(1).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{6}), source.take(new Range(Index.Start, Index.fromStart(3))).skip(2));
        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.Start, Index.fromStart(3))).take(new Range(Index.Start, Index.fromStart(1))));

        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.fromEnd(5), Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{4}), source.skip(1).take(new Range(Index.fromEnd(4), Index.fromStart(1))));
        assertEquals(Linq.of(new int[]{6}), source.take(new Range(Index.fromEnd(5), Index.fromStart(3))).skip(2));
        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.fromEnd(5), Index.fromStart(3))).take(new Range(Index.fromEnd(4), Index.fromStart(1))));

        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.Start, Index.fromEnd(4))));
        assertEquals(Linq.of(new int[]{4}), source.skip(1).take(new Range(Index.Start, Index.fromEnd(3))));
        assertEquals(Linq.of(new int[]{6}), source.take(new Range(Index.Start, Index.fromEnd(2))).skip(2));
        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.Start, Index.fromEnd(2))).take(new Range(Index.Start, Index.fromEnd(2))));

        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.fromEnd(5), Index.fromEnd(4))));
        assertEquals(Linq.of(new int[]{4}), source.skip(1).take(new Range(Index.fromEnd(4), Index.fromEnd(3))));
        assertEquals(Linq.of(new int[]{6}), source.take(new Range(Index.fromEnd(5), Index.fromEnd(2))).skip(2));
        assertEquals(Linq.of(new int[]{2}), source.take(new Range(Index.fromEnd(5), Index.fromEnd(2))).take(new Range(Index.fromEnd(4), Index.fromEnd(2))));
    }

    @Test
    void RepeatEnumerating() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerable<Integer> taken1 = Linq.of(source).take(3);
        assertEquals(taken1, taken1);

        IEnumerable<Integer> taken2 = Linq.of(source).take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(taken2, taken2);

        IEnumerable<Integer> taken3 = Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromStart(3)));
        assertEquals(taken3, taken3);

        IEnumerable<Integer> taken4 = Linq.of(source).take(new Range(Index.Start, Index.fromEnd(2)));
        assertEquals(taken4, taken4);

        IEnumerable<Integer> taken5 = Linq.of(source).take(new Range(Index.fromEnd(5), Index.fromEnd(2)));
        assertEquals(taken5, taken5);
    }

    @Test
    void RepeatEnumeratingNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        IEnumerable<Integer> taken1 = source.take(3);
        assertEquals(taken1, taken1);

        IEnumerable<Integer> taken2 = source.take(new Range(Index.Start, Index.fromStart(3)));
        assertEquals(taken2, taken2);

        IEnumerable<Integer> taken3 = source.take(new Range(Index.fromEnd(5), Index.fromStart(3)));
        assertEquals(taken3, taken3);

        IEnumerable<Integer> taken4 = source.take(new Range(Index.Start, Index.fromEnd(2)));
        assertEquals(taken4, taken4);

        IEnumerable<Integer> taken5 = source.take(new Range(Index.fromEnd(5), Index.fromEnd(2)));
        assertEquals(taken5, taken5);
    }

    @ParameterizedTest
    @MethodSource("LazySkipAllTakenForLargeNumbers_TestData")
    void LazySkipAllTakenForLargeNumbers(int largeNumber) {
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber).skip(42));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber / 2).skip(largeNumber / 2 + 1));

        assertEmpty(new FastInfiniteEnumerator<Integer>().take(new Range(Index.Start, Index.fromStart(largeNumber))).skip(largeNumber));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(new Range(Index.Start, Index.fromStart(largeNumber))).skip(largeNumber).skip(42));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(new Range(Index.Start, Index.fromStart(largeNumber))).skip(largeNumber / 2).skip(largeNumber / 2 + 1));
    }

    @Test
    void LazyOverflowRegression() {
        IEnumerable<Integer> range = NumberRangeGuaranteedNotCollectionType(1, 100);
        IEnumerable<Integer> skipped = range.skip(42); // Min index is 42.
        IEnumerable<Integer> taken1 = skipped.take(Integer.MAX_VALUE); // May try to calculate max index as 42 + Integer.MAX_VALUE, leading to integer overflow.
        assertEquals(Linq.range(43, 100 - 42), taken1);
        assertEquals(100 - 42, taken1.count());
        assertEquals(Linq.range(43, 100 - 42), taken1.toArray());
        assertEquals(Linq.range(43, 100 - 42), Linq.of(taken1.toList()));

        IEnumerable<Integer> taken2 = NumberRangeGuaranteedNotCollectionType(1, 100).take(new Range(Index.fromStart(42), Index.fromStart(Integer.MAX_VALUE)));
        assertEquals(Linq.range(43, 100 - 42), taken2);
        assertEquals(100 - 42, taken2.count());
        assertEquals(Linq.range(43, 100 - 42), taken2.toArray());
        assertEquals(Linq.range(43, 100 - 42), Linq.of(taken2.toList()));

        IEnumerable<Integer> taken3 = NumberRangeGuaranteedNotCollectionType(1, 100).take(new Range(Index.fromEnd(100 - 42), Index.fromStart(Integer.MAX_VALUE)));
        assertEquals(Linq.range(43, 100 - 42), taken3);
        assertEquals(100 - 42, taken3.count());
        assertEquals(Linq.range(43, 100 - 42), taken3.toArray());
        assertEquals(Linq.range(43, 100 - 42), Linq.of(taken3.toList()));

        IEnumerable<Integer> taken4 = NumberRangeGuaranteedNotCollectionType(1, 100).take(new Range(Index.fromStart(42), Index.End));
        assertEquals(Linq.range(43, 100 - 42), taken4);
        assertEquals(100 - 42, taken4.count());
        assertEquals(Linq.range(43, 100 - 42), taken4.toArray());
        assertEquals(Linq.range(43, 100 - 42), Linq.of(taken4.toList()));

        IEnumerable<Integer> taken5 = NumberRangeGuaranteedNotCollectionType(1, 100).take(new Range(Index.fromEnd(100 - 42), Index.End));
        assertEquals(Linq.range(43, 100 - 42), taken5);
        assertEquals(100 - 42, taken5.count());
        assertEquals(Linq.range(43, 100 - 42), taken5.toArray());
        assertEquals(Linq.range(43, 100 - 42), Linq.of(taken5.toList()));
    }

    @ParameterizedTest
    @MethodSource("CountOfLazySkipTakeChain_TestData")
    void CountOfLazySkipTakeChain(int skip, int take, int expected) {
        int totalCount = 100;
        IEnumerable<Integer> partition = NumberRangeGuaranteedNotCollectionType(1, totalCount).skip(skip).take(take);
        assertEquals(expected, partition.count());
        assertEquals(expected, partition.select(i -> i).count());
        assertEquals(expected, partition.select(i -> i).toArray()._getCount());

        int end;
        try {
            end = Math.addExact(skip, take);
        } catch (ArithmeticException e) {
            end = Integer.MAX_VALUE;
        }

        IEnumerable<Integer> partition2 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(new Range(Index.fromStart(skip), Index.fromStart(end)));
        assertEquals(expected, partition2.count());
        assertEquals(expected, partition2.select(i -> i).count());
        assertEquals(expected, partition2.select(i -> i).toArray()._getCount());

        IEnumerable<Integer> partition3 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(new Range(Index.fromEnd(Math.max(totalCount - skip, 0)), Index.fromStart(end)));
        assertEquals(expected, partition3.count());
        assertEquals(expected, partition3.select(i -> i).count());
        assertEquals(expected, partition3.select(i -> i).toArray()._getCount());

        IEnumerable<Integer> partition4 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(new Range(Index.fromStart(skip), Index.fromEnd(Math.max(totalCount - end, 0))));
        assertEquals(expected, partition4.count());
        assertEquals(expected, partition4.select(i -> i).count());
        assertEquals(expected, partition4.select(i -> i).toArray()._getCount());

        IEnumerable<Integer> partition5 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(new Range(Index.fromEnd(Math.max(totalCount - skip, 0)), Index.fromEnd(Math.max(totalCount - end, 0))));
        assertEquals(expected, partition5.count());
        assertEquals(expected, partition5.select(i -> i).count());
        assertEquals(expected, partition5.select(i -> i).toArray()._getCount());
    }

    @ParameterizedTest
    @MethodSource("FirstAndLastOfLazySkipTakeChain_TestData")
    void FirstAndLastOfLazySkipTakeChain(IEnumerable<Integer> source, int skip, int take, Integer first, Integer last) {
        IEnumerable<Integer> partition1 = ForceNotCollection(source).skip(skip).take(take);

        assertEquals(first, partition1.firstOrDefault());
        assertEquals(first, partition1.elementAtOrDefault(0));
        assertEquals(last, partition1.lastOrDefault());
        assertEquals(last, partition1.elementAtOrDefault(partition1.count() - 1));

        int end;
        try {
            end = Math.addExact(skip, take);
        } catch (ArithmeticException e) {
            end = Integer.MAX_VALUE;
        }

        IEnumerable<Integer> partition2 = ForceNotCollection(source).take(new Range(Index.fromStart(skip), Index.fromStart(end)));

        assertEquals(first, partition2.firstOrDefault());
        assertEquals(first, partition2.elementAtOrDefault(0));
        assertEquals(last, partition2.lastOrDefault());
        assertEquals(last, partition2.elementAtOrDefault(partition2.count() - 1));

        IEnumerable<Integer> partition3 = ForceNotCollection(source).take(new Range(Index.fromEnd(Math.max(source.count() - skip, 0)), Index.fromStart(end)));

        assertEquals(first, partition3.firstOrDefault());
        assertEquals(first, partition3.elementAtOrDefault(0));
        assertEquals(last, partition3.lastOrDefault());
        assertEquals(last, partition3.elementAtOrDefault(partition3.count() - 1));

        IEnumerable<Integer> partition4 = ForceNotCollection(source).take(new Range(Index.fromStart(skip), Index.fromEnd(Math.max(source.count() - end, 0))));

        assertEquals(first, partition4.firstOrDefault());
        assertEquals(first, partition4.elementAtOrDefault(0));
        assertEquals(last, partition4.lastOrDefault());
        assertEquals(last, partition4.elementAtOrDefault(partition4.count() - 1));

        IEnumerable<Integer> partition5 = ForceNotCollection(source).take(new Range(Index.fromEnd(Math.max(source.count() - skip, 0)), Index.fromEnd(Math.max(source.count() - end, 0))));

        assertEquals(first, partition5.firstOrDefault());
        assertEquals(first, partition5.elementAtOrDefault(0));
        assertEquals(last, partition5.lastOrDefault());
        assertEquals(last, partition5.elementAtOrDefault(partition5.count() - 1));
    }

    @ParameterizedTest
    @MethodSource("ElementAtOfLazySkipTakeChain_TestData")
    void ElementAtOfLazySkipTakeChain(int[] source, int skip, int take, int[] indices, Integer[] expectedValues) {
        IEnumerable<Integer> partition1 = ForceNotCollection(Linq.of(source)).skip(skip).take(take);

        assertEquals(indices.length, expectedValues.length);
        for (int i = 0; i < indices.length; i++) {
            assertEquals(expectedValues[i], partition1.elementAtOrDefault(indices[i]));
        }

        int end;
        try {
            end = Math.addExact(skip, take);
        } catch (ArithmeticException e) {
            end = Integer.MAX_VALUE;
        }

        IEnumerable<Integer> partition2 = ForceNotCollection(Linq.of(source)).take(new Range(Index.fromStart(skip), Index.fromStart(end)));
        for (int i = 0; i < indices.length; i++) {
            assertEquals(expectedValues[i], partition2.elementAtOrDefault(indices[i]));
        }

        IEnumerable<Integer> partition3 = ForceNotCollection(Linq.of(source)).take(new Range(Index.fromEnd(Math.max(source.length - skip, 0)), Index.fromStart(end)));
        for (int i = 0; i < indices.length; i++) {
            assertEquals(expectedValues[i], partition3.elementAtOrDefault(indices[i]));
        }

        IEnumerable<Integer> partition4 = ForceNotCollection(Linq.of(source)).take(new Range(Index.fromStart(skip), Index.fromEnd(Math.max(source.length - end, 0))));
        for (int i = 0; i < indices.length; i++) {
            assertEquals(expectedValues[i], partition4.elementAtOrDefault(indices[i]));
        }

        IEnumerable<Integer> partition5 = ForceNotCollection(Linq.of(source)).take(new Range(Index.fromEnd(Math.max(source.length - skip, 0)), Index.fromEnd(Math.max(source.length - end, 0))));
        for (int i = 0; i < indices.length; i++) {
            assertEquals(expectedValues[i], partition5.elementAtOrDefault(indices[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("DisposeSource_TestData")
    void DisposeSource(int sourceCount, int count) {
        boolean[] isIteratorDisposed = new boolean[5];

        List<DelegateIterator<Integer>> source = Repeat(index -> {
            ref<Integer> state = ref.init(0);

            return new DelegateIterator<>(
                    () -> ++state.value <= sourceCount,
                    () -> 0,
                    () -> {
                        state.value = -1;
                        isIteratorDisposed[index] = true;
                    });
        }, 5);


        IEnumerator<Integer> iterator0 = source.get(0).take(count).enumerator();
        int iteratorCount0 = Math.min(sourceCount, Math.max(0, count));
        assertAll(Linq.range(0, iteratorCount0), x -> assertTrue(iterator0.moveNext()));

        assertFalse(iterator0.moveNext());

        // Unlike Skip, Take can tell straightaway that it can return a sequence with no elements if count <= 0.
        // The enumerable it returns is a specialized empty iterator that has no connections to the source. Hence,
        // after MoveNext returns false under those circumstances, it won't invoke Dispose on our enumerator.
        boolean isItertorNotEmpty0 = count > 0;
        assertEquals(isItertorNotEmpty0, isIteratorDisposed[0]);

        int end = Math.max(0, count);
        IEnumerator<Integer> iterator1 = source.get(1).take(new Range(Index.Start, Index.fromStart(end))).enumerator();
        assertAll(Linq.range(0, Math.min(sourceCount, Math.max(0, count))), x -> assertTrue(iterator1.moveNext()));
        assertFalse(iterator1.moveNext());
        // When startIndex end and endIndex are both not from end and startIndex >= endIndex, Take(Range) returns an empty array.
        boolean isItertorNotEmpty1 = end != 0;
        assertEquals(isItertorNotEmpty1, isIteratorDisposed[1]);

        int startIndexFromEnd = Math.max(sourceCount, end);
        int endIndexFromEnd = Math.max(0, sourceCount - end);

        IEnumerator<Integer> iterator2 = source.get(2).take(new Range(Index.fromEnd(startIndexFromEnd), Index.fromStart(end))).enumerator();
        assertAll(Linq.range(0, Math.min(sourceCount, Math.max(0, count))), x -> assertTrue(iterator2.moveNext()));
        assertFalse(iterator2.moveNext());
        // When startIndex is ^0, Take(Range) returns an empty array.
        boolean isIteratorNotEmpty2 = startIndexFromEnd != 0;
        assertEquals(isIteratorNotEmpty2, isIteratorDisposed[2]);

        IEnumerator<Integer> iterator3 = source.get(3).take(new Range(Index.Start, Index.fromEnd(endIndexFromEnd))).enumerator();
        assertAll(Linq.range(0, Math.min(sourceCount, Math.max(0, count))), x -> assertTrue(iterator3.moveNext()));
        assertFalse(iterator3.moveNext());
        assertTrue(isIteratorDisposed[3]);

        IEnumerator<Integer> iterator4 = source.get(4).take(new Range(Index.fromEnd(startIndexFromEnd), Index.fromEnd(endIndexFromEnd))).enumerator();
        assertAll(Linq.range(0, Math.min(sourceCount, Math.max(0, count))), x -> assertTrue(iterator4.moveNext()));
        assertFalse(iterator4.moveNext());
        // When startIndex is ^0,
        // or when startIndex and endIndex are both from end and startIndex <= endIndexFromEnd, Take(Range) returns an empty array.
        boolean isIteratorNotEmpty4 = startIndexFromEnd != 0 && startIndexFromEnd > endIndexFromEnd;
        assertEquals(isIteratorNotEmpty4, isIteratorDisposed[4]);
    }


    @Test
    void DisposeSource_StartIndexFromEnd_ShouldDisposeOnFirstElement() {
        final int count = 5;
        ref<Integer> state = ref.init(0);
        IEnumerable<Integer> source = new DelegateIterator<>(
                () -> ++state.value <= count,
                () -> state.value,
                () -> state.value = -1);

        try (IEnumerator<Integer> e = source.take(Range.startAt(Index.fromEnd(3))).enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(3, e.current());

            assertEquals(-1, state.value);
            assertTrue(e.moveNext());
        }
    }

    @Test
    void DisposeSource_EndIndexFromEnd_ShouldDisposeOnCompletedEnumeration() {
        final int count = 5;
        ref<Integer> state = ref.init(0);
        IEnumerable<Integer> source = new DelegateIterator<>(
                () -> ++state.value <= count,
                () -> state.value,
                () -> state.value = -1);

        try (IEnumerator<Integer> e = source.take(Range.endAt(Index.fromEnd(3))).enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(4, state.value);
            assertEquals(1, e.current());

            assertTrue(e.moveNext());
            assertEquals(5, state.value);
            assertEquals(2, e.current());

            assertFalse(e.moveNext());
        }
        assertEquals(-1, state.value);
    }

    @Test
    void OutOfBoundNoException() {
        Func0<IEnumerable<Integer>> source = () -> Linq.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(source.apply(), source.apply().take(new Range(Index.Start, Index.fromStart(6))));
        assertEquals(source.apply(), source.apply().take(new Range(Index.Start, Index.fromStart(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.apply().take(new Range(Index.fromEnd(10), Index.fromStart(4))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(4))));
        assertEquals(source.apply(), source.apply().take(new Range(Index.fromEnd(10), Index.fromStart(6))));
        assertEquals(source.apply(), source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(6))));
        assertEquals(source.apply(), source.apply().take(new Range(Index.fromEnd(10), Index.fromStart(Integer.MAX_VALUE))));
        assertEquals(source.apply(), source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(Integer.MAX_VALUE))));

        assertEmpty(source.apply().take(new Range(Index.Start, Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.Start, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(4), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(4), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.apply().take(new Range(Index.fromEnd(10), Index.fromEnd(1))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(1))));
        assertEmpty(source.apply().take(new Range(Index.End, Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(1), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(10), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(source.apply().take(new Range(Index.End, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(1), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(5), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));
    }

    @Test
    void OutOfBoundNoExceptionNotList() {
        IEnumerable<Integer> source = Linq.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(source, ForceNotCollection(source).take(new Range(Index.Start, Index.fromStart(6))));
        assertEquals(source, ForceNotCollection(source).take(new Range(Index.Start, Index.fromStart(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromStart(4))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(4))));
        assertEquals(source, ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromStart(6))));
        assertEquals(source, ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(6))));
        assertEquals(source, ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromStart(Integer.MAX_VALUE))));
        assertEquals(source, ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(Integer.MAX_VALUE))));

        assertEmpty(ForceNotCollection(source).take(new Range(Index.Start, Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.Start, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(4), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(4), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromEnd(1))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(1))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.End, Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.End, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(5), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));
    }

    @Test
    void OutOfBoundNoExceptionListPartition() {
        IEnumerable<Integer> source = Linq.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(6))));
        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(4))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(4))));
        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(6))));
        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(6))));
        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(Integer.MAX_VALUE))));
        assertEquals(source, ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(Integer.MAX_VALUE))));

        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(4), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(4), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(1))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(1))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.End, Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.End, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));
    }

    @Test
    void OutOfBoundNoExceptionEnumerablePartition() {
        IEnumerable<Integer> source = Linq.of(new int[]{1, 2, 3, 4, 5});

        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(6))));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(4))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(4))));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(6))));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(6))));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(Integer.MAX_VALUE))));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromStart(Integer.MAX_VALUE))));

        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(4), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(4), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));

        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(1))));
        assertEquals(Linq.of(new int[]{1, 2, 3, 4}), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(1))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.End, Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(6))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.End, Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(Integer.MAX_VALUE))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(Integer.MAX_VALUE), Index.fromEnd(Integer.MAX_VALUE))));
    }

    @Test
    void MutableSource() {
        List<Integer> source1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query1 = Linq.of(source1).take(3);
        source1.remove(0);
        source1.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query1);

        List<Integer> source2 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query2 = Linq.of(source2).take(new Range(Index.Start, Index.fromStart(3)));
        source2.remove(0);
        source2.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query2);

        List<Integer> source3 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query3 = Linq.of(source3).take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        source3.remove(0);
        source3.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query3);

        List<Integer> source4 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query4 = Linq.of(source4).take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        source4.remove(0);
        source4.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query4);
    }

    @Test
    void MutableSourceNotList() {
        List<Integer> source1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query1 = ForceNotCollection(Linq.of(source1)).select(i -> i).take(3);
        source1.remove(0);
        source1.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query1);

        List<Integer> source2 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query2 = ForceNotCollection(Linq.of(source2)).select(i -> i).take(new Range(Index.Start, Index.fromStart(3)));
        source2.remove(0);
        source2.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query2);

        List<Integer> source3 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query3 = ForceNotCollection(Linq.of(source3)).select(i -> i).take(new Range(Index.fromEnd(6), Index.fromStart(3)));
        source3.remove(0);
        source3.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query3);

        List<Integer> source4 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        IEnumerable<Integer> query4 = ForceNotCollection(Linq.of(source4)).select(i -> i).take(new Range(Index.fromEnd(6), Index.fromEnd(3)));
        source4.remove(0);
        source4.addAll(2, Arrays.asList(-1, -2));
        assertEquals(Linq.of(new int[]{1, 2, -1}), query4);
    }

    @Test
    void NonEmptySource_ConsistencyWithCountable() {
        Func0<IEnumerable<Integer>> source = () -> Linq.of(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

        // Multiple elements in the middle.
        assertEquals(Linq.of(1, 2, 3, 4), source.apply().take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEquals(Linq.of(2, 3, 4, 5, 6), source.apply().take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEquals(Linq.of(2, 3, 4, 5), source.apply().take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEquals(Linq.of(3, 4, 5), source.apply().take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEquals(Linq.of(1, 2, 3, 4, 5, 6, 7, 8, 9), source.apply().take(Range.startAt(Index.fromEnd(9))));
        assertEquals(Linq.of(2, 3, 4, 5, 6, 7, 8, 9), source.apply().take(Range.startAt(Index.fromStart(2))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), source.apply().take(Range.endAt(Index.fromEnd(4))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), source.apply().take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), source.apply().take(Range.All));

        // Single element in the middle.
        assertEquals(Linq.of(1), source.apply().take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEquals(Linq.of(2), source.apply().take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEquals(Linq.of(2), source.apply().take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEquals(Linq.of(5), source.apply().take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEquals(Linq.of(0), source.apply().take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEquals(Linq.of(0), source.apply().take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(0), source.apply().take(new Range(Index.Start, Index.fromEnd(9))));
        assertEquals(Linq.of(0), source.apply().take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEquals(Linq.of(9), source.apply().take(new Range(Index.fromEnd(1), Index.fromStart(10))));
        assertEquals(Linq.of(9), source.apply().take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEquals(Linq.of(9), source.apply().take(new Range(Index.fromStart(9), Index.End)));
        assertEquals(Linq.of(9), source.apply().take(new Range(Index.fromEnd(1), Index.End)));

        // No element.
        assertEquals(Linq.empty(), source.apply().take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEquals(Linq.empty(), source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEquals(Linq.empty(), source.apply().take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEquals(Linq.empty(), source.apply().take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEquals(Linq.empty(), source.apply().take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
    }

    @Test
    void NonEmptySource_ConsistencyWithCountable_NotList() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Multiple elements in the middle.
        assertEquals(Linq.of(1, 2, 3, 4), ForceNotCollection(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEquals(Linq.of(2, 3, 4, 5, 6), ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEquals(Linq.of(2, 3, 4, 5), ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEquals(Linq.of(3, 4, 5), ForceNotCollection(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEquals(Linq.of(1, 2, 3, 4, 5, 6, 7, 8, 9), ForceNotCollection(source).take(Range.startAt(Index.fromEnd(9))));
        assertEquals(Linq.of(2, 3, 4, 5, 6, 7, 8, 9), ForceNotCollection(source).take(Range.startAt(Index.fromStart(2))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), ForceNotCollection(source).take(Range.endAt(Index.fromEnd(4))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), ForceNotCollection(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), ForceNotCollection(source).take(Range.All));

        // Single element in the middle.
        assertEquals(Linq.of(1), ForceNotCollection(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEquals(Linq.of(2), ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEquals(Linq.of(2), ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEquals(Linq.of(5), ForceNotCollection(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEquals(Linq.of(0), ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEquals(Linq.of(0), ForceNotCollection(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(0), ForceNotCollection(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEquals(Linq.of(0), ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEquals(Linq.of(9), ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.fromStart(10))));
        assertEquals(Linq.of(9), ForceNotCollection(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEquals(Linq.of(9), ForceNotCollection(source).take(new Range(Index.fromStart(9), Index.End)));
        assertEquals(Linq.of(9), ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.End)));

        // No element.
        assertEquals(Linq.empty(), ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEquals(Linq.empty(), ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEquals(Linq.empty(), ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEquals(Linq.empty(), ForceNotCollection(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEquals(Linq.empty(), ForceNotCollection(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
    }

    @Test
    void NonEmptySource_ConsistencyWithCountable_ListPartition() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Multiple elements in the middle.
        assertEquals(Linq.of(1, 2, 3, 4), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEquals(Linq.of(2, 3, 4, 5, 6), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEquals(Linq.of(2, 3, 4, 5), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEquals(Linq.of(3, 4, 5), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEquals(Linq.of(1, 2, 3, 4, 5, 6, 7, 8, 9), ListPartitionOrEmpty(source).take(Range.startAt(Index.fromEnd(9))));
        assertEquals(Linq.of(2, 3, 4, 5, 6, 7, 8, 9), ListPartitionOrEmpty(source).take(Range.startAt(Index.fromStart(2))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), ListPartitionOrEmpty(source).take(Range.endAt(Index.fromEnd(4))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), ListPartitionOrEmpty(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), ListPartitionOrEmpty(source).take(Range.All));

        // Single element in the middle.
        assertEquals(Linq.of(1), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEquals(Linq.of(2), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEquals(Linq.of(2), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEquals(Linq.of(5), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEquals(Linq.of(0), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEquals(Linq.of(0), ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(0), ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEquals(Linq.of(0), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEquals(Linq.of(9), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromStart(10))));
        assertEquals(Linq.of(9), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEquals(Linq.of(9), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.End)));
        assertEquals(Linq.of(9), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.End)));

        // No element.
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
    }

    @Test
    void NonEmptySource_ConsistencyWithCountable_EnumerablePartition() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Multiple elements in the middle.
        assertEquals(Linq.of(1, 2, 3, 4), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEquals(Linq.of(2, 3, 4, 5, 6), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEquals(Linq.of(2, 3, 4, 5), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEquals(Linq.of(3, 4, 5), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEquals(Linq.of(1, 2, 3, 4, 5, 6, 7, 8, 9), EnumerablePartitionOrEmpty(source).take(Range.startAt(Index.fromEnd(9))));
        assertEquals(Linq.of(2, 3, 4, 5, 6, 7, 8, 9), EnumerablePartitionOrEmpty(source).take(Range.startAt(Index.fromStart(2))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), EnumerablePartitionOrEmpty(source).take(Range.endAt(Index.fromEnd(4))));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), EnumerablePartitionOrEmpty(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), EnumerablePartitionOrEmpty(source).take(Range.All));

        // Single element in the middle.
        assertEquals(Linq.of(1), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEquals(Linq.of(2), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEquals(Linq.of(2), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEquals(Linq.of(5), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEquals(Linq.of(0), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEquals(Linq.of(0), EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEquals(Linq.of(0), EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEquals(Linq.of(0), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEquals(Linq.of(9), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromStart(10))));
        assertEquals(Linq.of(9), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEquals(Linq.of(9), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.End)));
        assertEquals(Linq.of(9), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.End)));

        // No element.
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));
    }

    @Test
    void NonEmptySource_DoNotThrowException() {
        Func0<IEnumerable<Integer>> source = () -> Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void NonEmptySource_DoNotThrowException_NotList() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void NonEmptySource_DoNotThrowException_ListPartition() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void NonEmptySource_DoNotThrowException_EnumerablePartition() {
        IEnumerable<Integer> source = Linq.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void EmptySource_DoNotThrowException() {
        Func0<IEnumerable<Integer>> source = () -> Linq.of(Collections.emptyList());

        // Multiple elements in the middle.
        assertEmpty(source.apply().take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEmpty(source.apply().take(Range.startAt(Index.fromEnd(9))));
        assertEmpty(source.apply().take(Range.startAt(Index.fromStart(2))));
        assertEmpty(source.apply().take(Range.endAt(Index.fromEnd(4))));
        assertEmpty(source.apply().take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.empty(), source.apply().take(Range.All));

        // Single element in the middle.
        assertEmpty(source.apply().take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEmpty(source.apply().take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEmpty(source.apply().take(new Range(Index.Start, Index.fromStart(1))));
        assertEmpty(source.apply().take(new Range(Index.Start, Index.fromEnd(9))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEmpty(source.apply().take(new Range(Index.fromEnd(1), Index.fromEnd(10))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(9), Index.fromEnd(9))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(1), Index.fromEnd(9))));

        // No element.
        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(6), Index.fromEnd(6))));

        // Invalid range.
        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(source.apply().take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(source.apply().take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void EmptySource_DoNotThrowException_NotList() {
        IEnumerable<Integer> source = Linq.of(Collections.emptyList());

        // Multiple elements in the middle.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEmpty(ForceNotCollection(source).take(Range.startAt(Index.fromEnd(9))));
        assertEmpty(ForceNotCollection(source).take(Range.startAt(Index.fromStart(2))));
        assertEmpty(ForceNotCollection(source).take(Range.endAt(Index.fromEnd(4))));
        assertEmpty(ForceNotCollection(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.empty(), ForceNotCollection(source).take(Range.All));

        // Single element in the middle.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.fromEnd(10))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(9), Index.fromEnd(9))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(1), Index.fromEnd(9))));

        // No element.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));

        // Invalid range.
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(ForceNotCollection(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void EmptySource_DoNotThrowException_ListPartition() {
        IEnumerable<Integer> source = Linq.of(Collections.emptyList());

        // Multiple elements in the middle.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEmpty(ListPartitionOrEmpty(source).take(Range.startAt(Index.fromEnd(9))));
        assertEmpty(ListPartitionOrEmpty(source).take(Range.startAt(Index.fromStart(2))));
        assertEmpty(ListPartitionOrEmpty(source).take(Range.endAt(Index.fromEnd(4))));
        assertEmpty(ListPartitionOrEmpty(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.empty(), ListPartitionOrEmpty(source).take(Range.All));

        // Single element in the middle.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(10))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromEnd(9))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(9))));

        // No element.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));

        // Invalid range.
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(ListPartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void EmptySource_DoNotThrowException_EnumerablePartition() {
        IEnumerable<Integer> source = Linq.of(Collections.emptyList());

        // Multiple elements in the middle.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(5))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(7))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(4))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(7), Index.fromEnd(4))));

        // Range with default index.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(Range.startAt(Index.fromEnd(9))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(Range.startAt(Index.fromStart(2))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(Range.endAt(Index.fromEnd(4))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(Range.endAt(Index.fromStart(6))));

        // All.
        assertEquals(Linq.empty(), EnumerablePartitionOrEmpty(source).take(Range.All));

        // Single element in the middle.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(9), Index.fromStart(2))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromStart(3))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(2), Index.fromEnd(7))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(5), Index.fromEnd(4))));

        // Single element at start.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromStart(1))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromStart(1))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.Start, Index.fromEnd(9))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(10), Index.fromEnd(9))));

        // Single element at end.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(10))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromStart(10))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(9), Index.fromEnd(9))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(1), Index.fromEnd(9))));

        // No element.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(3))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(4))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(7))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(3), Index.fromStart(7))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(6))));

        // Invalid range.
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromStart(2))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(6), Index.fromEnd(5))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromStart(3), Index.fromEnd(8))));
        assertEmpty(EnumerablePartitionOrEmpty(source).take(new Range(Index.fromEnd(6), Index.fromEnd(7))));
    }

    @Test
    void testTake() {
        List<Department> enumerableDeptsResult = Linq.of(depts).take(2).toList();
        assertEquals(2, enumerableDeptsResult.size());
        assertEquals(depts[0], enumerableDeptsResult.get(0));
        assertEquals(depts[1], enumerableDeptsResult.get(1));

        List<Department> enumerableDeptsResult5 = Linq.of(depts).take(5).toList();
        assertEquals(3, enumerableDeptsResult5.size());

        try (IEnumerator<Department> e = Linq.of(depts).take(1).enumerator()) {
            assertTrue(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    void testIList_Take() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).take(7);
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

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).skip(5).take(3);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).take(1);
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
    void testIList_TakeLast() {
        IEnumerable<Integer> source = Linq.of(new LinkedList<>(Arrays.asList(110, 98, 18, -200, 48, 50, -2, 0))).takeLast(7);
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

        IEnumerable<Integer> emptySource = Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5))).skip(5).takeLast(3);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());

        IEnumerable<Integer> emptySource2 = Linq.of(new LinkedList<>(Collections.<Integer>emptyList())).takeLast(1);
        assertSame(ArrayUtils.empty(), emptySource2.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource2.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource2.toList());
        assertEquals(0, emptySource2.count());
        assertThrows(InvalidOperationException.class, () -> emptySource2.first());
        assertThrows(InvalidOperationException.class, () -> emptySource2.last());
        assertIsType(EnumerablePartition.class, emptySource2.skip(2));
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }
}
