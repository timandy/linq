package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.Range;
import com.bestvike.TestCase;
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

        assertEquals(q.take(new Range(Index.fromStart(0),Index.fromStart(9))), q.take(new Range(Index.fromStart(0),Index.fromStart(9))));
        assertEquals(q.take(^9..9), q.take(^9..9));
        assertEquals(q.take(0..^0), q.take(0..^0));
        assertEquals(q.take(^9..^0), q.take(^9..^0));
    }

    @Test
    void SameResultsRepeatCallsIntQueryIList() {
        List<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE).toList();

        assertEquals(Linq.of(q).take(9), Linq.of(q).take(9));

        assertEquals(Linq.of(q).take(new Range(Index.fromStart(0),Index.fromStart(9))), Linq.of(q).take(new Range(Index.fromStart(0),Index.fromStart(9))));
        assertEquals(Linq.of(q).take(^9..9), Linq.of(q).take(^9..9));
        assertEquals(Linq.of(q).take(0..^0), Linq.of(q).take(0..^0));
        assertEquals(Linq.of(q).take(^9..^0), Linq.of(q).take(^9..^0));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.take(7), q.take(7));

        assertEquals(q.take(0..7), q.take(0..7));
        assertEquals(q.take(^7..7), q.take(^7..7));
        assertEquals(q.take(0..^0), q.take(0..^0));
        assertEquals(q.take(^7..^0), q.take(^7..^0));
    }

    @Test
    void SameResultsRepeatCallsStringQueryIList() {
        List<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty}).where(x -> !IsNullOrEmpty(x)).toList();

        assertEquals(Linq.of(q).take(7), Linq.of(q).take(7));

        assertEquals(q.take(0..7), q.take(0..7));
        assertEquals(q.take(^7..7), q.take(^7..7));
        assertEquals(q.take(0..^0), q.take(0..^0));
        assertEquals(q.take(^7..^0), q.take(^7..^0));
    }

    @Test
    void SourceEmptyCountPositive() {
        int[] source = {};
        assertEmpty(Linq.of(source).take(5));

        Assert.Empty(source.take(0..5));
        Assert.Empty(source.take(^5..5));
        Assert.Empty(source.take(0..^0));
        Assert.Empty(source.take(^5..^0));
    }

    @Test
    void SourceEmptyCountPositiveNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 0);
        assertEmpty(source.take(5));

        Assert.Empty(source.take(0..5));
        Assert.Empty(source.take(^5..5));
        Assert.Empty(source.take(0..^0));
        Assert.Empty(source.take(^5..^0));
    }

    @Test
    void SourceNonEmptyCountNegative() {
        int[] source = {2, 5, 9, 1};
        assertEmpty(Linq.of(source).take(-5));

        Assert.Empty(source.take(^9..0));
    }

    @Test
    void SourceNonEmptyCountNegativeNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        assertEmpty(source.take(-5));

        Assert.Empty(source.take(^9..0));
    }

    @Test
    void SourceNonEmptyCountZero() {
        int[] source = {2, 5, 9, 1};
        assertEmpty(Linq.of(source).take(0));

        Assert.Empty(source.take(0..0));
        Assert.Empty(source.take(^4..0));
        Assert.Empty(source.take(0..^4));
        Assert.Empty(source.take(^4..^4));
    }

    @Test
    void SourceNonEmptyCountZeroNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        assertEmpty(source.take(0));

        Assert.Empty(source.take(0..0));
        Assert.Empty(source.take(^4..0));
        Assert.Empty(source.take(0..^4));
        Assert.Empty(source.take(^4..^4));
    }

    @Test
    void SourceNonEmptyCountOne() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2};

        assertEquals(Linq.of(expected), Linq.of(source).take(1));

        assertEquals(expected, source.take(0..1));
        assertEquals(expected, source.take(^4..1));
        assertEquals(expected, source.take(0..^3));
        assertEquals(expected, source.take(^4..^3));
    }

    @Test
    void SourceNonEmptyCountOneNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        int[] expected = {2};

        assertEquals(Linq.of(expected), source.take(1));

        assertEquals(expected, source.take(0..1));
        assertEquals(expected, source.take(^4..1));
        assertEquals(expected, source.take(0..^3));
        assertEquals(expected, source.take(^4..^3));
    }

    @Test
    void SourceNonEmptyTakeAllExactly() {
        int[] source = {2, 5, 9, 1};

        assertEquals(Linq.of(source), Linq.of(source).take(source.length));

        assertEquals(source, source.take(0..source.Length));
        assertEquals(source, source.take(^source.Length..source.Length));
        assertEquals(source, source.take(0..^0));
        assertEquals(source, source.take(^source.Length..^0));
    }

    @Test
    void SourceNonEmptyTakeAllExactlyNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));

        assertEquals(source, source.take(source.count()));

        assertEquals(source, source.take(0..source.Count()));
        assertEquals(source, source.take(^source.Count()..source.Count()));
        assertEquals(source, source.take(0..^0));
        assertEquals(source, source.take(^source.Count()..^0));
    }

    @Test
    void SourceNonEmptyTakeAllButOne() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), Linq.of(source).take(3));

        assertEquals(expected, source.take(0..3));
        assertEquals(expected, source.take(^4..3));
        assertEquals(expected, source.take(0..^1));
        assertEquals(expected, source.take(^4..^1));
    }

    @Test
    void RunOnce() {
        int[] source = {2, 5, 9, 1};
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().take(3));

        assertEquals(expected, source.RunOnce().take(0..3));
        assertEquals(expected, source.RunOnce().take(^4..3));
        assertEquals(expected, source.RunOnce().take(0..^1));
        assertEquals(expected, source.RunOnce().take(^4..^1));
    }

    @Test
    void SourceNonEmptyTakeAllButOneNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 5, 9, 1}));
        int[] expected = {2, 5, 9};

        assertEquals(Linq.of(expected), source.runOnce().take(3));

        assertEquals(expected, source.RunOnce().take(0..3));
        assertEquals(expected, source.RunOnce().take(^4..3));
        assertEquals(expected, source.RunOnce().take(0..^1));
        assertEquals(expected, source.RunOnce().take(^4..^1));
    }

    @Test
    void SourceNonEmptyTakeExcessive() {
        Integer[] source = {2, 5, null, 9, 1};

        assertEquals(Linq.of(source), Linq.of(source).take(source.length + 1));

        assertEquals(source, source.take(0..(source.Length + 1)));
        assertEquals(source, source.take(^(source.Length + 1)..(source.Length + 1)));
    }

    @Test
    void SourceNonEmptyTakeExcessiveNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(2, 5, null, 9, 1));

        assertEquals(source, source.take(source.count() + 1));

        assertEquals(source, source.take(0..(source.Count() + 1)));
        assertEquals(source, source.take(^(source.Count() + 1)..(source.Count() + 1)));
    }

    @Test
    void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.take(5));

        Assert.Throws<ArgumentNullException>("source", () => source.take(0..5));
        Assert.Throws<ArgumentNullException>("source", () => source.take(^5..5));
        Assert.Throws<ArgumentNullException>("source", () => source.take(0..^0));
        Assert.Throws<ArgumentNullException>("source", () => source.take(^5..^0));
    }

    @Test
    void ForcedToEnumeratorDoesNotEnumerate() {
        IEnumerable<Integer> iterator1 = NumberRangeGuaranteedNotCollectionType(0, 3).take(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en1 = (IEnumerator<Integer>) iterator1;
        assertFalse(en1 != null && en1.moveNext());

        var iterator2 = NumberRangeGuaranteedNotCollectionType(0, 3).take(0..2);
        var en2 = iterator2 as IEnumerator<int>;
        Assert.False(en2 != null && en2.MoveNext());

        var iterator3 = NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..2);
        var en3 = iterator3 as IEnumerator<int>;
        Assert.False(en3 != null && en3.MoveNext());

        var iterator4 = NumberRangeGuaranteedNotCollectionType(0, 3).take(0..^1);
        var en4 = iterator4 as IEnumerator<int>;
        Assert.False(en4 != null && en4.MoveNext());

        var iterator5 = NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..^1);
        var en5 = iterator5 as IEnumerator<int>;
        Assert.False(en5 != null && en5.MoveNext());
    }

    @Test
    void Count() {
        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(2).count());
        assertEquals(2, Linq.of(new int[]{1, 2, 3}).take(2).count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(0).count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(0..2).Count());
        assertEquals(2, new[] { 1, 2, 3 }.take(0..2).Count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(0..0).Count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..2).Count());
        assertEquals(2, new[] { 1, 2, 3 }.take(^3..2).Count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..0).Count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(0..^1).Count());
        assertEquals(2, new[] { 1, 2, 3 }.take(0..^1).Count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(0..^3).Count());

        assertEquals(2, NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..^1).Count());
        assertEquals(2, new[] { 1, 2, 3 }.take(^3..^1).Count());
        assertEquals(0, NumberRangeGuaranteedNotCollectionType(0, 3).take(^3..^3).Count());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateIList() {
        IEnumerable<Integer> iterator1 = Linq.of(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).take(2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en1 = (IEnumerator<Integer>) iterator1;
        assertFalse(en1 != null && en1.moveNext());

        var iterator2 = NumberRangeGuaranteedNotCollectionType(0, 3).ToList().take(0..2);
        var en2 = iterator2 as IEnumerator<int>;
        Assert.False(en2 != null && en2.MoveNext());

        var iterator3 = NumberRangeGuaranteedNotCollectionType(0, 3).ToList().take(^3..2);
        var en3 = iterator3 as IEnumerator<int>;
        Assert.False(en3 != null && en3.MoveNext());

        var iterator4 = NumberRangeGuaranteedNotCollectionType(0, 3).ToList().take(0..^1);
        var en4 = iterator4 as IEnumerator<int>;
        Assert.False(en4 != null && en4.MoveNext());

        var iterator5 = NumberRangeGuaranteedNotCollectionType(0, 3).ToList().take(^3..^1);
        var en5 = iterator5 as IEnumerator<int>;
        Assert.False(en5 != null && en5.MoveNext());
    }

    @Test
    void FollowWithTake() {
        int[] source = new int[]{5, 6, 7, 8};
        int[] expected = new int[]{5, 6};
        assertEquals(Linq.of(expected), Linq.of(source).take(5).take(3).take(2).take(40));

        assertEquals(expected, source.take(0..5).take(0..3).take(0..2).take(0..40));
        assertEquals(expected, source.take(^4..5).take(^4..3).take(^3..2).take(^2..40));
        assertEquals(expected, source.take(0..^0).take(0..^1).take(0..^1).take(0..^0));
        assertEquals(expected, source.take(^4..^0).take(^4..^1).take(^3..^1).take(^2..^0));
    }

    @Test
    void FollowWithTakeNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(5, 4);
        int[] expected = new int[]{5, 6};
        assertEquals(Linq.of(expected), source.take(5).take(3).take(2));

        assertEquals(expected, source.take(0..5).take(0..3).take(0..2));
        assertEquals(expected, source.take(^4..5).take(^4..3).take(^3..2));
        assertEquals(expected, source.take(0..^0).take(0..^1).take(0..^1));
        assertEquals(expected, source.take(^4..^0).take(^4..^1).take(^3..^1));
    }

    @Test
    void FollowWithSkip() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        int[] expected = new int[]{3, 4, 5};
        assertEquals(Linq.of(expected), Linq.of(source).take(5).skip(2).skip(-4));

        assertEquals(expected, source.take(0..5).Skip(2).Skip(-4));
        assertEquals(expected, source.take(^6..5).Skip(2).Skip(-4));
        assertEquals(expected, source.take(0..^1).Skip(2).Skip(-4));
        assertEquals(expected, source.take(^6..^1).Skip(2).Skip(-4));
    }

    @Test
    void FollowWithSkipNotIList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(1, 6);
        int[] expected = new int[]{3, 4, 5};
        assertEquals(Linq.of(expected), source.take(5).skip(2).skip(-4));

        assertEquals(expected, source.take(0..5).Skip(2).Skip(-4));
        assertEquals(expected, source.take(^6..5).Skip(2).Skip(-4));
        assertEquals(expected, source.take(0..^1).Skip(2).Skip(-4));
        assertEquals(expected, source.take(^6..^1).Skip(2).Skip(-4));
    }

    @Test
    void ElementAt() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> taken0 = Linq.of(source).take(3);
        assertEquals(1, taken0.elementAt(0));
        assertEquals(3, taken0.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(3));

        var taken1 = source.take(0..3);
        assertEquals(1, taken1.ElementAt(0));
        assertEquals(3, taken1.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken1.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken1.ElementAt(3));

        var taken2 = source.take(^6..3);
        assertEquals(1, taken2.ElementAt(0));
        assertEquals(3, taken2.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken2.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken2.ElementAt(3));

        var taken3 = source.take(0..^3);
        assertEquals(1, taken3.ElementAt(0));
        assertEquals(3, taken3.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken3.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken3.ElementAt(3));

        var taken4 = source.take(^6..^3);
        assertEquals(1, taken4.ElementAt(0));
        assertEquals(3, taken4.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken4.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken4.ElementAt(3));
    }

    @Test
    void ElementAtNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> taken0 = source.take(3);
        assertEquals(1, taken0.elementAt(0));
        assertEquals(3, taken0.elementAt(2));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> taken0.elementAt(3));

        var taken1 = source.take(0..3);
        assertEquals(1, taken1.ElementAt(0));
        assertEquals(3, taken1.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken1.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken1.ElementAt(3));

        var taken2 = source.take(^6..3);
        assertEquals(1, taken2.ElementAt(0));
        assertEquals(3, taken2.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken2.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken2.ElementAt(3));

        var taken3 = source.take(0..^3);
        assertEquals(1, taken3.ElementAt(0));
        assertEquals(3, taken3.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken3.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken3.ElementAt(3));

        var taken4 = source.take(^6..^3);
        assertEquals(1, taken4.ElementAt(0));
        assertEquals(3, taken4.ElementAt(2));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken4.ElementAt(-1));
        Assert.Throws<ArgumentOutOfRangeException>("index", () => taken4.ElementAt(3));
    }

    @Test
    void ElementAtOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5, 6};
        IEnumerable<Integer> taken0 = Linq.of(source).take(3);
        assertEquals(1, taken0.elementAtOrDefault(0));
        assertEquals(3, taken0.elementAtOrDefault(2));
        assertEquals(null, taken0.elementAtOrDefault(-1));
        assertEquals(null, taken0.elementAtOrDefault(3));

        var taken1 = source.take(0..3);
        assertEquals(1, taken1.ElementAtOrDefault(0));
        assertEquals(3, taken1.ElementAtOrDefault(2));
        assertEquals(0, taken1.ElementAtOrDefault(-1));
        assertEquals(0, taken1.ElementAtOrDefault(3));

        var taken2 = source.take(^6..3);
        assertEquals(1, taken2.ElementAtOrDefault(0));
        assertEquals(3, taken2.ElementAtOrDefault(2));
        assertEquals(0, taken2.ElementAtOrDefault(-1));
        assertEquals(0, taken2.ElementAtOrDefault(3));

        var taken3 = source.take(0..^3);
        assertEquals(1, taken3.ElementAtOrDefault(0));
        assertEquals(3, taken3.ElementAtOrDefault(2));
        assertEquals(0, taken3.ElementAtOrDefault(-1));
        assertEquals(0, taken3.ElementAtOrDefault(3));

        var taken4 = source.take(^6..^3);
        assertEquals(1, taken4.ElementAtOrDefault(0));
        assertEquals(3, taken4.ElementAtOrDefault(2));
        assertEquals(0, taken4.ElementAtOrDefault(-1));
        assertEquals(0, taken4.ElementAtOrDefault(3));
    }

    @Test
    void ElementAtOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5, 6}));
        IEnumerable<Integer> taken0 = source.take(3);
        assertEquals(1, taken0.elementAtOrDefault(0));
        assertEquals(3, taken0.elementAtOrDefault(2));
        assertEquals(null, taken0.elementAtOrDefault(-1));
        assertEquals(null, taken0.elementAtOrDefault(3));

        var taken1 = source.take(0..3);
        assertEquals(1, taken1.ElementAtOrDefault(0));
        assertEquals(3, taken1.ElementAtOrDefault(2));
        assertEquals(0, taken1.ElementAtOrDefault(-1));
        assertEquals(0, taken1.ElementAtOrDefault(3));

        var taken2 = source.take(^6..3);
        assertEquals(1, taken2.ElementAtOrDefault(0));
        assertEquals(3, taken2.ElementAtOrDefault(2));
        assertEquals(0, taken2.ElementAtOrDefault(-1));
        assertEquals(0, taken2.ElementAtOrDefault(3));

        var taken3 = source.take(0..^3);
        assertEquals(1, taken3.ElementAtOrDefault(0));
        assertEquals(3, taken3.ElementAtOrDefault(2));
        assertEquals(0, taken3.ElementAtOrDefault(-1));
        assertEquals(0, taken3.ElementAtOrDefault(3));

        var taken4 = source.take(^6..^3);
        assertEquals(1, taken4.ElementAtOrDefault(0));
        assertEquals(3, taken4.ElementAtOrDefault(2));
        assertEquals(0, taken4.ElementAtOrDefault(-1));
        assertEquals(0, taken4.ElementAtOrDefault(3));
    }

    @Test
    void First() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).first());
        assertEquals(1, Linq.of(source).take(4).first());
        assertEquals(1, Linq.of(source).take(40).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(0).first());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).skip(5).take(10).first());

        assertEquals(1, source.take(0..1).First());
        assertEquals(1, source.take(0..4).First());
        assertEquals(1, source.take(0..40).First());
        Assert.Throws<InvalidOperationException>(() => source.take(0..0).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(0..10).First());

        assertEquals(1, source.take(^5..1).First());
        assertEquals(1, source.take(^5..4).First());
        assertEquals(1, source.take(^5..40).First());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..0).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(^5..10).First());

        assertEquals(1, source.take(0..^4).First());
        assertEquals(1, source.take(0..^1).First());
        assertEquals(1, source.take(0..^0).First());
        Assert.Throws<InvalidOperationException>(() => source.take(0..^5).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(0..^5).First());

        assertEquals(1, source.take(^5..^4).First());
        assertEquals(1, source.take(^5..^1).First());
        assertEquals(1, source.take(^5..^0).First());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..^5).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(^10..^0).First());
    }

    @Test
    void FirstNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).first());
        assertEquals(1, source.take(4).first());
        assertEquals(1, source.take(40).first());
        assertThrows(InvalidOperationException.class, () -> source.take(0).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(5).take(10).first());

        assertEquals(1, source.take(0..1).First());
        assertEquals(1, source.take(0..4).First());
        assertEquals(1, source.take(0..40).First());
        Assert.Throws<InvalidOperationException>(() => source.take(0..0).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(0..10).First());

        assertEquals(1, source.take(^5..1).First());
        assertEquals(1, source.take(^5..4).First());
        assertEquals(1, source.take(^5..40).First());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..0).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(^5..10).First());

        assertEquals(1, source.take(0..^4).First());
        assertEquals(1, source.take(0..^1).First());
        assertEquals(1, source.take(0..^0).First());
        Assert.Throws<InvalidOperationException>(() => source.take(0..^5).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(0..^5).First());

        assertEquals(1, source.take(^5..^4).First());
        assertEquals(1, source.take(^5..^1).First());
        assertEquals(1, source.take(^5..^0).First());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..^5).First());
        Assert.Throws<InvalidOperationException>(() => source.Skip(5).take(^10..^0).First());
    }

    @Test
    void FirstOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).firstOrDefault());
        assertEquals(1, Linq.of(source).take(4).firstOrDefault());
        assertEquals(1, Linq.of(source).take(40).firstOrDefault());
        assertEquals(null, Linq.of(source).take(0).firstOrDefault());
        assertEquals(null, Linq.of(source).skip(5).take(10).firstOrDefault());

        assertEquals(1, source.take(0..1).FirstOrDefault());
        assertEquals(1, source.take(0..4).FirstOrDefault());
        assertEquals(1, source.take(0..40).FirstOrDefault());
        assertEquals(0, source.take(0..0).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(0..10).FirstOrDefault());

        assertEquals(1, source.take(^5..1).FirstOrDefault());
        assertEquals(1, source.take(^5..4).FirstOrDefault());
        assertEquals(1, source.take(^5..40).FirstOrDefault());
        assertEquals(0, source.take(^5..0).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(^10..10).FirstOrDefault());

        assertEquals(1, source.take(0..^4).FirstOrDefault());
        assertEquals(1, source.take(0..^1).FirstOrDefault());
        assertEquals(1, source.take(0..^0).FirstOrDefault());
        assertEquals(0, source.take(0..^5).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(0..^10).FirstOrDefault());

        assertEquals(1, source.take(^5..^4).FirstOrDefault());
        assertEquals(1, source.take(^5..^1).FirstOrDefault());
        assertEquals(1, source.take(^5..^0).FirstOrDefault());
        assertEquals(0, source.take(^5..^5).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(^10..^0).FirstOrDefault());
    }

    @Test
    void FirstOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).firstOrDefault());
        assertEquals(1, source.take(4).firstOrDefault());
        assertEquals(1, source.take(40).firstOrDefault());
        assertEquals(null, source.take(0).firstOrDefault());
        assertEquals(null, source.skip(5).take(10).firstOrDefault());

        assertEquals(1, source.take(0..1).FirstOrDefault());
        assertEquals(1, source.take(0..4).FirstOrDefault());
        assertEquals(1, source.take(0..40).FirstOrDefault());
        assertEquals(0, source.take(0..0).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(0..10).FirstOrDefault());

        assertEquals(1, source.take(^5..1).FirstOrDefault());
        assertEquals(1, source.take(^5..4).FirstOrDefault());
        assertEquals(1, source.take(^5..40).FirstOrDefault());
        assertEquals(0, source.take(^5..0).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(^10..10).FirstOrDefault());

        assertEquals(1, source.take(0..^4).FirstOrDefault());
        assertEquals(1, source.take(0..^1).FirstOrDefault());
        assertEquals(1, source.take(0..^0).FirstOrDefault());
        assertEquals(0, source.take(0..^5).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(0..^10).FirstOrDefault());

        assertEquals(1, source.take(^5..^4).FirstOrDefault());
        assertEquals(1, source.take(^5..^1).FirstOrDefault());
        assertEquals(1, source.take(^5..^0).FirstOrDefault());
        assertEquals(0, source.take(^5..^5).FirstOrDefault());
        assertEquals(0, source.Skip(5).take(^10..^0).FirstOrDefault());
    }

    @Test
    void Last() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).last());
        assertEquals(5, Linq.of(source).take(5).last());
        assertEquals(5, Linq.of(source).take(40).last());
        assertThrows(InvalidOperationException.class, () -> Linq.of(source).take(0).last());
        assertThrows(InvalidOperationException.class, () -> Linq.empty().take(40).last());

        assertEquals(1, source.take(0..1).Last());
        assertEquals(5, source.take(0..5).Last());
        assertEquals(5, source.take(0..40).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(0..0).Last());
        Assert.Throws<InvalidOperationException>(() => Array.Empty<int>().take(0..40).Last());

        assertEquals(1, source.take(^5..1).Last());
        assertEquals(5, source.take(^5..5).Last());
        assertEquals(5, source.take(^5..40).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..0).Last());
        Assert.Throws<InvalidOperationException>(() => Array.Empty<int>().take(^5..40).Last());

        assertEquals(1, source.take(0..^4).Last());
        assertEquals(5, source.take(0..^0).Last());
        assertEquals(5, source.take(3..^0).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(0..^5).Last());
        Assert.Throws<InvalidOperationException>(() => Array.Empty<int>().take(0..^0).Last());

        assertEquals(1, source.take(^5..^4).Last());
        assertEquals(5, source.take(^5..^0).Last());
        assertEquals(5, source.take(^5..^0).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..^5).Last());
        Assert.Throws<InvalidOperationException>(() => Array.Empty<int>().take(^40..^0).Last());
    }

    @Test
    void LastNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).last());
        assertEquals(5, source.take(5).last());
        assertEquals(5, source.take(40).last());
        assertThrows(InvalidOperationException.class, () -> source.take(0).last());
        assertThrows(InvalidOperationException.class, () -> ForceNotCollection(Linq.empty()).take(40).last());

        assertEquals(1, source.take(0..1).Last());
        assertEquals(5, source.take(0..5).Last());
        assertEquals(5, source.take(0..40).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(0..0).Last());
        Assert.Throws<InvalidOperationException>(() => ForceNotCollection(Array.Empty<int>()).take(0..40).Last());

        assertEquals(1, source.take(^5..1).Last());
        assertEquals(5, source.take(^5..5).Last());
        assertEquals(5, source.take(^5..40).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..0).Last());
        Assert.Throws<InvalidOperationException>(() => ForceNotCollection(Array.Empty<int>()).take(^5..40).Last());

        assertEquals(1, source.take(0..^4).Last());
        assertEquals(5, source.take(0..^0).Last());
        assertEquals(5, source.take(3..^0).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(0..^5).Last());
        Assert.Throws<InvalidOperationException>(() => ForceNotCollection(Array.Empty<int>()).take(0..^0).Last());

        assertEquals(1, source.take(^5..^4).Last());
        assertEquals(5, source.take(^5..^0).Last());
        assertEquals(5, source.take(^5..^0).Last());
        Assert.Throws<InvalidOperationException>(() => source.take(^5..^5).Last());
        Assert.Throws<InvalidOperationException>(() => ForceNotCollection(Array.Empty<int>()).take(^40..^0).Last());
    }

    @Test
    void LastOrDefault() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        assertEquals(1, Linq.of(source).take(1).lastOrDefault());
        assertEquals(5, Linq.of(source).take(5).lastOrDefault());
        assertEquals(5, Linq.of(source).take(40).lastOrDefault());
        assertEquals(null, Linq.of(source).take(0).lastOrDefault());
        assertEquals(null, Linq.empty().take(40).lastOrDefault());

        assertEquals(1, source.take(0..1).LastOrDefault());
        assertEquals(5, source.take(0..5).LastOrDefault());
        assertEquals(5, source.take(0..40).LastOrDefault());
        assertEquals(0, source.take(0..0).LastOrDefault());
        assertEquals(0, Array.Empty<int>().take(0..40).LastOrDefault());

        assertEquals(1, source.take(^5..1).LastOrDefault());
        assertEquals(5, source.take(^5..5).LastOrDefault());
        assertEquals(5, source.take(^5..40).LastOrDefault());
        assertEquals(0, source.take(^5..0).LastOrDefault());
        assertEquals(0, Array.Empty<int>().take(^5..40).LastOrDefault());

        assertEquals(1, source.take(0..^4).LastOrDefault());
        assertEquals(5, source.take(0..^0).LastOrDefault());
        assertEquals(5, source.take(3..^0).LastOrDefault());
        assertEquals(0, source.take(0..^5).LastOrDefault());
        assertEquals(0, Array.Empty<int>().take(0..^0).LastOrDefault());

        assertEquals(1, source.take(^5..^4).LastOrDefault());
        assertEquals(5, source.take(^5..^0).LastOrDefault());
        assertEquals(5, source.take(^40..^0).LastOrDefault());
        assertEquals(0, source.take(^5..^5).LastOrDefault());
        assertEquals(0, Array.Empty<int>().take(^40..^0).LastOrDefault());
    }

    @Test
    void LastOrDefaultNotIList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        assertEquals(1, source.take(1).lastOrDefault());
        assertEquals(5, source.take(5).lastOrDefault());
        assertEquals(5, source.take(40).lastOrDefault());
        assertEquals(null, source.take(0).lastOrDefault());
        assertEquals(null, ForceNotCollection(Linq.empty()).take(40).lastOrDefault());

        assertEquals(1, source.take(0..1).LastOrDefault());
        assertEquals(5, source.take(0..5).LastOrDefault());
        assertEquals(5, source.take(0..40).LastOrDefault());
        assertEquals(0, source.take(0..0).LastOrDefault());
        assertEquals(0, ForceNotCollection(Array.Empty<int>()).take(0..40).LastOrDefault());

        assertEquals(1, source.take(^5..1).LastOrDefault());
        assertEquals(5, source.take(^5..5).LastOrDefault());
        assertEquals(5, source.take(^5..40).LastOrDefault());
        assertEquals(0, source.take(^5..0).LastOrDefault());
        assertEquals(0, ForceNotCollection(Array.Empty<int>()).take(^5..40).LastOrDefault());

        assertEquals(1, source.take(0..^4).LastOrDefault());
        assertEquals(5, source.take(0..^0).LastOrDefault());
        assertEquals(5, source.take(3..^0).LastOrDefault());
        assertEquals(0, source.take(0..^5).LastOrDefault());
        assertEquals(0, ForceNotCollection(Array.Empty<int>()).take(0..^0).LastOrDefault());

        assertEquals(1, source.take(^5..^4).LastOrDefault());
        assertEquals(5, source.take(^5..^0).LastOrDefault());
        assertEquals(5, source.take(^40..^0).LastOrDefault());
        assertEquals(0, source.take(^5..^5).LastOrDefault());
        assertEquals(0, ForceNotCollection(Array.Empty<int>()).take(^40..^0).LastOrDefault());
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

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..5).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..6).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..40).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..4).ToArray());
        assertEquals(1, source.take(0..1).ToArray().Single());
        Assert.Empty(source.take(0..0).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..5).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..6).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..40).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..4).ToArray());
        assertEquals(1, source.take(^5..1).ToArray().Single());
        Assert.Empty(source.take(^5..0).ToArray());
        Assert.Empty(source.take(^15..0).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..^1).ToArray());
        assertEquals(1, source.take(0..^4).ToArray().Single());
        Assert.Empty(source.take(0..^5).ToArray());
        Assert.Empty(source.take(0..^15).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^6..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^45..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..^1).ToArray());
        assertEquals(1, source.take(^5..^4).ToArray().Single());
        Assert.Empty(source.take(^5..^5).ToArray());
        Assert.Empty(source.take(^15..^5).ToArray());
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

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..5).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..6).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..40).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..4).ToArray());
        assertEquals(1, source.take(0..1).ToArray().Single());
        Assert.Empty(source.take(0..0).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..5).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..6).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..40).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..4).ToArray());
        assertEquals(1, source.take(^5..1).ToArray().Single());
        Assert.Empty(source.take(^5..0).ToArray());
        Assert.Empty(source.take(^15..0).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..^1).ToArray());
        assertEquals(1, source.take(0..^4).ToArray().Single());
        Assert.Empty(source.take(0..^5).ToArray());
        Assert.Empty(source.take(0..^15).ToArray());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^6..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^45..^0).ToArray());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..^1).ToArray());
        assertEquals(1, source.take(^5..^4).ToArray().Single());
        Assert.Empty(source.take(^5..^5).ToArray());
        Assert.Empty(source.take(^15..^5).ToArray());
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

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..5).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..6).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..40).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..4).ToList());
        assertEquals(1, source.take(0..1).ToList().Single());
        Assert.Empty(source.take(0..0).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..5).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..6).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..40).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..4).ToList());
        assertEquals(1, source.take(^5..1).ToList().Single());
        Assert.Empty(source.take(^5..0).ToList());
        Assert.Empty(source.take(^15..0).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..^1).ToList());
        assertEquals(1, source.take(0..^4).ToList().Single());
        Assert.Empty(source.take(0..^5).ToList());
        Assert.Empty(source.take(0..^15).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^6..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^45..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..^1).ToList());
        assertEquals(1, source.take(^5..^4).ToList().Single());
        Assert.Empty(source.take(^5..^5).ToList());
        Assert.Empty(source.take(^15..^5).ToList());
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

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..5).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..6).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..40).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..4).ToList());
        assertEquals(1, source.take(0..1).ToList().Single());
        Assert.Empty(source.take(0..0).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..5).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..6).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..40).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..4).ToList());
        assertEquals(1, source.take(^5..1).ToList().Single());
        Assert.Empty(source.take(^5..0).ToList());
        Assert.Empty(source.take(^15..0).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(0..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(0..^1).ToList());
        assertEquals(1, source.take(0..^4).ToList().Single());
        Assert.Empty(source.take(0..^5).ToList());
        Assert.Empty(source.take(0..^15).ToList());

        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^5..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^6..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4, 5 }, source.take(^45..^0).ToList());
        assertEquals(new[] { 1, 2, 3, 4 }, source.take(^5..^1).ToList());
        assertEquals(1, source.take(^5..^4).ToList().Single());
        Assert.Empty(source.take(^5..^5).ToList());
        Assert.Empty(source.take(^15..^5).ToList());
    }

    @Test
    void TakeCanOnlyBeOneList() {
        int[] source = new int[]{2, 4, 6, 8, 10};
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(1));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(1));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(3).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(3).take(1));

        assertEquals(new[] { 2 }, source.take(0..1));
        assertEquals(new[] { 4 }, source.Skip(1).take(0..1));
        assertEquals(new[] { 6 }, source.take(0..3).Skip(2));
        assertEquals(new[] { 2 }, source.take(0..3).take(0..1));

        assertEquals(new[] { 2 }, source.take(^5..1));
        assertEquals(new[] { 4 }, source.Skip(1).take(^4..1));
        assertEquals(new[] { 6 }, source.take(^5..3).Skip(2));
        assertEquals(new[] { 2 }, source.take(^5..3).take(^4..1));

        assertEquals(new[] { 2 }, source.take(0..^4));
        assertEquals(new[] { 4 }, source.Skip(1).take(0..^3));
        assertEquals(new[] { 6 }, source.take(0..^2).Skip(2));
        assertEquals(new[] { 2 }, source.take(0..^2).take(0..^2));

        assertEquals(new[] { 2 }, source.take(^5..^4));
        assertEquals(new[] { 4 }, source.Skip(1).take(^4..^3));
        assertEquals(new[] { 6 }, source.take(^5..^2).Skip(2));
        assertEquals(new[] { 2 }, source.take(^5..^2).take(^4..^2));
    }

    @Test
    void TakeCanOnlyBeOneNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{2, 4, 6, 8, 10}));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(1));
        assertEquals(Linq.of(new int[]{4}), Linq.of(source).skip(1).take(1));
        assertEquals(Linq.of(new int[]{6}), Linq.of(source).take(3).skip(2));
        assertEquals(Linq.of(new int[]{2}), Linq.of(source).take(3).take(1));

        assertEquals(new[] { 2 }, source.take(0..1));
        assertEquals(new[] { 4 }, source.Skip(1).take(0..1));
        assertEquals(new[] { 6 }, source.take(0..3).Skip(2));
        assertEquals(new[] { 2 }, source.take(0..3).take(0..1));

        assertEquals(new[] { 2 }, source.take(^5..1));
        assertEquals(new[] { 4 }, source.Skip(1).take(^4..1));
        assertEquals(new[] { 6 }, source.take(^5..3).Skip(2));
        assertEquals(new[] { 2 }, source.take(^5..3).take(^4..1));

        assertEquals(new[] { 2 }, source.take(0..^4));
        assertEquals(new[] { 4 }, source.Skip(1).take(0..^3));
        assertEquals(new[] { 6 }, source.take(0..^2).Skip(2));
        assertEquals(new[] { 2 }, source.take(0..^2).take(0..^2));

        assertEquals(new[] { 2 }, source.take(^5..^4));
        assertEquals(new[] { 4 }, source.Skip(1).take(^4..^3));
        assertEquals(new[] { 6 }, source.take(^5..^2).Skip(2));
        assertEquals(new[] { 2 }, source.take(^5..^2).take(^4..^2));
    }

    @Test
    void RepeatEnumerating() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerable<Integer> taken1 = Linq.of(source).take(3);
        assertEquals(taken1, taken1);

        var taken2 = source.take(0..3);
        assertEquals(taken2, taken2);

        var taken3 = source.take(^5..3);
        assertEquals(taken3, taken3);

        var taken4 = source.take(0..^2);
        assertEquals(taken4, taken4);

        var taken5 = source.take(^5..^2);
        assertEquals(taken5, taken5);
    }

    @Test
    void RepeatEnumeratingNotList() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.of(new int[]{1, 2, 3, 4, 5}));
        IEnumerable<Integer> taken1 = source.take(3);
        assertEquals(taken1, taken1);

        var taken2 = source.take(0..3);
        assertEquals(taken2, taken2);

        var taken3 = source.take(^5..3);
        assertEquals(taken3, taken3);

        var taken4 = source.take(0..^2);
        assertEquals(taken4, taken4);

        var taken5 = source.take(^5..^2);
        assertEquals(taken5, taken5);
    }

    @ParameterizedTest
    @MethodSource("LazySkipAllTakenForLargeNumbers_TestData")
    void LazySkipAllTakenForLargeNumbers(int largeNumber) {
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber).skip(42));
        assertEmpty(new FastInfiniteEnumerator<Integer>().take(largeNumber).skip(largeNumber / 2).skip(largeNumber / 2 + 1));

        Assert.Empty(new FastInfiniteEnumerator<int>().take(0..largeNumber).Skip(largeNumber));
        Assert.Empty(new FastInfiniteEnumerator<int>().take(0..largeNumber).Skip(largeNumber).Skip(42));
        Assert.Empty(new FastInfiniteEnumerator<int>().take(0..largeNumber).Skip(largeNumber / 2).Skip(largeNumber / 2 + 1));
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

        var taken2 = NumberRangeGuaranteedNotCollectionType(1, 100).take(42..int.MaxValue);
        assertEquals(Enumerable.Range(43, 100 - 42), taken2);
        assertEquals(100 - 42, taken2.Count());
        assertEquals(Enumerable.Range(43, 100 - 42), taken2.ToArray());
        assertEquals(Enumerable.Range(43, 100 - 42), taken2.ToList());

        var taken3 = NumberRangeGuaranteedNotCollectionType(1, 100).take(^(100 - 42)..int.MaxValue);
        assertEquals(Enumerable.Range(43, 100 - 42), taken3);
        assertEquals(100 - 42, taken3.Count());
        assertEquals(Enumerable.Range(43, 100 - 42), taken3.ToArray());
        assertEquals(Enumerable.Range(43, 100 - 42), taken3.ToList());

        var taken4 = NumberRangeGuaranteedNotCollectionType(1, 100).take(42..^0);
        assertEquals(Enumerable.Range(43, 100 - 42), taken4);
        assertEquals(100 - 42, taken4.Count());
        assertEquals(Enumerable.Range(43, 100 - 42), taken4.ToArray());
        assertEquals(Enumerable.Range(43, 100 - 42), taken4.ToList());

        var taken5 = NumberRangeGuaranteedNotCollectionType(1, 100).take(^(100 - 42)..^0);
        assertEquals(Enumerable.Range(43, 100 - 42), taken5);
        assertEquals(100 - 42, taken5.Count());
        assertEquals(Enumerable.Range(43, 100 - 42), taken5.ToArray());
        assertEquals(Enumerable.Range(43, 100 - 42), taken5.ToList());
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
        try
        {
            end = checked(skip + take);
        }
        catch (OverflowException)
        {
            end = int.MaxValue;
        }

        var partition2 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(skip..end);
        assertEquals(expected, partition2.Count());
        assertEquals(expected, partition2.Select(i => i).Count());
        assertEquals(expected, partition2.Select(i => i).ToArray().Length);

        var partition3 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(^Math.Max(totalCount - skip, 0)..end);
        assertEquals(expected, partition3.Count());
        assertEquals(expected, partition3.Select(i => i).Count());
        assertEquals(expected, partition3.Select(i => i).ToArray().Length);

        var partition4 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(skip..^Math.Max(totalCount - end, 0));
        assertEquals(expected, partition4.Count());
        assertEquals(expected, partition4.Select(i => i).Count());
        assertEquals(expected, partition4.Select(i => i).ToArray().Length);

        var partition5 = NumberRangeGuaranteedNotCollectionType(1, totalCount).take(^Math.Max(totalCount - skip, 0)..^Math.Max(totalCount - end, 0));
        assertEquals(expected, partition5.Count());
        assertEquals(expected, partition5.Select(i => i).Count());
        assertEquals(expected, partition5.Select(i => i).ToArray().Length);
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
        try
        {
            end = checked(skip + take);
        }
        catch (OverflowException)
        {
            end = int.MaxValue;
        }

        var partition2 = ForceNotCollection(source).take(skip..end);

        assertEquals(first, partition2.FirstOrDefault());
        assertEquals(first, partition2.ElementAtOrDefault(0));
        assertEquals(last, partition2.LastOrDefault());
        assertEquals(last, partition2.ElementAtOrDefault(partition2.Count() - 1));

        var partition3 = ForceNotCollection(source).take(^Math.Max(source.Length - skip, 0)..end);

        assertEquals(first, partition3.FirstOrDefault());
        assertEquals(first, partition3.ElementAtOrDefault(0));
        assertEquals(last, partition3.LastOrDefault());
        assertEquals(last, partition3.ElementAtOrDefault(partition3.Count() - 1));

        var partition4 = ForceNotCollection(source).take(skip..^Math.Max(source.Length - end, 0));

        assertEquals(first, partition4.FirstOrDefault());
        assertEquals(first, partition4.ElementAtOrDefault(0));
        assertEquals(last, partition4.LastOrDefault());
        assertEquals(last, partition4.ElementAtOrDefault(partition4.Count() - 1));

        var partition5 = ForceNotCollection(source).take(^Math.Max(source.Length - skip, 0)..^Math.Max(source.Length - end, 0));

        assertEquals(first, partition5.FirstOrDefault());
        assertEquals(first, partition5.ElementAtOrDefault(0));
        assertEquals(last, partition5.LastOrDefault());
        assertEquals(last, partition5.ElementAtOrDefault(partition5.Count() - 1));
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
        try
        {
            end = checked(skip + take);
        }
        catch (OverflowException)
        {
            end = int.MaxValue;
        }

        var partition2 = ForceNotCollection(source).take(skip..end);
        for (int i = 0; i < indices.Length; i++)
        {
            assertEquals(expectedValues[i], partition2.ElementAtOrDefault(indices[i]));
        }

        var partition3 = ForceNotCollection(source).take(^Math.Max(source.Length - skip, 0)..end);
        for (int i = 0; i < indices.Length; i++)
        {
            assertEquals(expectedValues[i], partition3.ElementAtOrDefault(indices[i]));
        }

        var partition4 = ForceNotCollection(source).take(skip..^Math.Max(source.Length - end, 0));
        for (int i = 0; i < indices.Length; i++)
        {
            assertEquals(expectedValues[i], partition4.ElementAtOrDefault(indices[i]));
        }

        var partition5 = ForceNotCollection(source).take(^Math.Max(source.Length - skip, 0)..^Math.Max(source.Length - end, 0));
        for (int i = 0; i < indices.Length; i++)
        {
            assertEquals(expectedValues[i], partition5.ElementAtOrDefault(indices[i]));
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
        boolean isItertorNotEmpty0 = count>0;
        assertEquals(isItertorNotEmpty0, isIteratorDisposed[0]);

        int end = Math.Max(0, count);
        IEnumerator<int> iterator1 = source[1].take(0..end).GetEnumerator();
        Assert.All(Enumerable.Range(0, Math.Min(sourceCount, Math.Max(0, count))), _ => Assert.True(iterator1.MoveNext()));
        Assert.False(iterator1.MoveNext());
        // When startIndex end and endIndex are both not from end and startIndex >= endIndex, Take(Range) returns an empty array.
        bool isItertorNotEmpty1 = end != 0;
        assertEquals(isItertorNotEmpty1, isIteratorDisposed[1]);

        int startIndexFromEnd = Math.Max(sourceCount, end);
        int endIndexFromEnd = Math.Max(0, sourceCount - end);

        IEnumerator<int> iterator2 = source[2].take(^startIndexFromEnd..end).GetEnumerator();
        Assert.All(Enumerable.Range(0, Math.Min(sourceCount, Math.Max(0, count))), _ => Assert.True(iterator2.MoveNext()));
        Assert.False(iterator2.MoveNext());
        // When startIndex is ^0, Take(Range) returns an empty array.
        bool isIteratorNotEmpty2 = startIndexFromEnd != 0;
        assertEquals(isIteratorNotEmpty2, isIteratorDisposed[2]);

        IEnumerator<int> iterator3 = source[3].take(0..^endIndexFromEnd).GetEnumerator();
        Assert.All(Enumerable.Range(0, Math.Min(sourceCount, Math.Max(0, count))), _ => Assert.True(iterator3.MoveNext()));
        Assert.False(iterator3.MoveNext());
        Assert.True(isIteratorDisposed[3]);

        IEnumerator<int> iterator4 = source[4].take(^startIndexFromEnd..^endIndexFromEnd).GetEnumerator();
        Assert.All(Enumerable.Range(0, Math.Min(sourceCount, Math.Max(0, count))), _ => Assert.True(iterator4.MoveNext()));
        Assert.False(iterator4.MoveNext());
        // When startIndex is ^0,
        // or when startIndex and endIndex are both from end and startIndex <= endIndexFromEnd, Take(Range) returns an empty array.
        bool isIteratorNotEmpty4 = startIndexFromEnd != 0 && startIndexFromEnd > endIndexFromEnd;
        assertEquals(isIteratorNotEmpty4, isIteratorDisposed[4]);
    }


        [Fact]
    public void DisposeSource_StartIndexFromEnd_ShouldDisposeOnFirstElement()
    {
            const int count = 5;
        int state = 0;
        var source = new DelegateIterator<int>(
                moveNext: () => ++state <= count,
            current: () => state,
            dispose: () => state = -1);

        using var e = source.take(^3..).GetEnumerator();
        Assert.True(e.MoveNext());
        assertEquals(3, e.Current);

        assertEquals(-1, state);
        Assert.True(e.MoveNext());
    }

        [Fact]
    public void DisposeSource_EndIndexFromEnd_ShouldDisposeOnCompletedEnumeration()
    {
            const int count = 5;
        int state = 0;
        var source = new DelegateIterator<int>(
                moveNext: () => ++state <= count,
            current: () => state,
            dispose: () => state = -1);

        using var e = source.take(..^3).GetEnumerator();

        Assert.True(e.MoveNext());
        assertEquals(4, state);
        assertEquals(1, e.Current);

        Assert.True(e.MoveNext());
        assertEquals(5, state);
        assertEquals(2, e.Current);

        Assert.False(e.MoveNext());
        assertEquals(-1, state);
    }

        [Fact]
    public void OutOfBoundNoException()
    {
        Func<int[]> source = () => new[] { 1, 2, 3, 4, 5 };

        assertEquals(source(), source().take(0..6));
        assertEquals(source(), source().take(0..int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, source().take(^10..4));
        assertEquals(new int[] { 1, 2, 3, 4 }, source().take(^int.MaxValue..4));
        assertEquals(source(), source().take(^10..6));
        assertEquals(source(), source().take(^int.MaxValue..6));
        assertEquals(source(), source().take(^10..int.MaxValue));
        assertEquals(source(), source().take(^int.MaxValue..int.MaxValue));

        Assert.Empty(source().take(0..^6));
        Assert.Empty(source().take(0..^int.MaxValue));
        Assert.Empty(source().take(4..^6));
        Assert.Empty(source().take(4..^int.MaxValue));
        Assert.Empty(source().take(6..^6));
        Assert.Empty(source().take(6..^int.MaxValue));
        Assert.Empty(source().take(int.MaxValue..^6));
        Assert.Empty(source().take(int.MaxValue..^int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, source().take(^10..^1));
        assertEquals(new int[] { 1, 2, 3, 4 }, source().take(^int.MaxValue..^1));
        Assert.Empty(source().take(^0..^6));
        Assert.Empty(source().take(^1..^6));
        Assert.Empty(source().take(^6..^6));
        Assert.Empty(source().take(^10..^6));
        Assert.Empty(source().take(^int.MaxValue..^6));
        Assert.Empty(source().take(^0..^int.MaxValue));
        Assert.Empty(source().take(^1..^int.MaxValue));
        Assert.Empty(source().take(^6..^int.MaxValue));
        Assert.Empty(source().take(^int.MaxValue..^int.MaxValue));
    }

        [Fact]
    public void OutOfBoundNoExceptionNotList()
    {
        var source = new[] { 1, 2, 3, 4, 5 };

        assertEquals(source, ForceNotCollection(source).take(0..6));
        assertEquals(source, ForceNotCollection(source).take(0..int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, ForceNotCollection(source).take(^10..4));
        assertEquals(new int[] { 1, 2, 3, 4 }, ForceNotCollection(source).take(^int.MaxValue..4));
        assertEquals(source, ForceNotCollection(source).take(^10..6));
        assertEquals(source, ForceNotCollection(source).take(^int.MaxValue..6));
        assertEquals(source, ForceNotCollection(source).take(^10..int.MaxValue));
        assertEquals(source, ForceNotCollection(source).take(^int.MaxValue..int.MaxValue));

        Assert.Empty(ForceNotCollection(source).take(0..^6));
        Assert.Empty(ForceNotCollection(source).take(0..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(4..^6));
        Assert.Empty(ForceNotCollection(source).take(4..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(6..^6));
        Assert.Empty(ForceNotCollection(source).take(6..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(int.MaxValue..^6));
        Assert.Empty(ForceNotCollection(source).take(int.MaxValue..^int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, ForceNotCollection(source).take(^10..^1));
        assertEquals(new int[] { 1, 2, 3, 4 }, ForceNotCollection(source).take(^int.MaxValue..^1));
        Assert.Empty(ForceNotCollection(source).take(^0..^6));
        Assert.Empty(ForceNotCollection(source).take(^1..^6));
        Assert.Empty(ForceNotCollection(source).take(^6..^6));
        Assert.Empty(ForceNotCollection(source).take(^10..^6));
        Assert.Empty(ForceNotCollection(source).take(^int.MaxValue..^6));
        Assert.Empty(ForceNotCollection(source).take(^0..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(^1..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(^6..^int.MaxValue));
        Assert.Empty(ForceNotCollection(source).take(^int.MaxValue..^int.MaxValue));
    }

        [Fact]
    public void OutOfBoundNoExceptionListPartition()
    {
        var source = new[] { 1, 2, 3, 4, 5 };

        assertEquals(source, ListPartitionOrEmpty(source).take(0..6));
        assertEquals(source, ListPartitionOrEmpty(source).take(0..int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, ListPartitionOrEmpty(source).take(^10..4));
        assertEquals(new int[] { 1, 2, 3, 4 }, ListPartitionOrEmpty(source).take(^int.MaxValue..4));
        assertEquals(source, ListPartitionOrEmpty(source).take(^10..6));
        assertEquals(source, ListPartitionOrEmpty(source).take(^int.MaxValue..6));
        assertEquals(source, ListPartitionOrEmpty(source).take(^10..int.MaxValue));
        assertEquals(source, ListPartitionOrEmpty(source).take(^int.MaxValue..int.MaxValue));

        Assert.Empty(ListPartitionOrEmpty(source).take(0..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(0..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(4..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(4..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(6..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(6..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(int.MaxValue..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(int.MaxValue..^int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, ListPartitionOrEmpty(source).take(^10..^1));
        assertEquals(new int[] { 1, 2, 3, 4 }, ListPartitionOrEmpty(source).take(^int.MaxValue..^1));
        Assert.Empty(ListPartitionOrEmpty(source).take(^0..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(^1..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(^6..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(^10..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(^int.MaxValue..^6));
        Assert.Empty(ListPartitionOrEmpty(source).take(^0..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(^1..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(^6..^int.MaxValue));
        Assert.Empty(ListPartitionOrEmpty(source).take(^int.MaxValue..^int.MaxValue));
    }

        [Fact]
    public void OutOfBoundNoExceptionEnumerablePartition()
    {
        var source = new[] { 1, 2, 3, 4, 5 };

        assertEquals(source, EnumerablePartitionOrEmpty(source).take(0..6));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(0..int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, EnumerablePartitionOrEmpty(source).take(^10..4));
        assertEquals(new int[] { 1, 2, 3, 4 }, EnumerablePartitionOrEmpty(source).take(^int.MaxValue..4));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(^10..6));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(^int.MaxValue..6));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(^10..int.MaxValue));
        assertEquals(source, EnumerablePartitionOrEmpty(source).take(^int.MaxValue..int.MaxValue));

        Assert.Empty(EnumerablePartitionOrEmpty(source).take(0..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(0..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(4..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(4..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(6..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(6..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(int.MaxValue..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(int.MaxValue..^int.MaxValue));

        assertEquals(new int[] { 1, 2, 3, 4 }, EnumerablePartitionOrEmpty(source).take(^10..^1));
        assertEquals(new int[] { 1, 2, 3, 4 }, EnumerablePartitionOrEmpty(source).take(^int.MaxValue..^1));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^0..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^1..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^6..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^10..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^int.MaxValue..^6));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^0..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^1..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^6..^int.MaxValue));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^int.MaxValue..^int.MaxValue));
    }

        [Fact]
    public void MutableSource()
    {
        var source1 = new List<int>() { 0, 1, 2, 3, 4 };
        var query1 = source1.take(3);
        source1.RemoveAt(0);
        source1.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query1);

        var source2 = new List<int>() { 0, 1, 2, 3, 4 };
        var query2 = source2.take(0..3);
        source2.RemoveAt(0);
        source2.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query2);

        var source3 = new List<int>() { 0, 1, 2, 3, 4 };
        var query3 = source3.take(^6..3);
        source3.RemoveAt(0);
        source3.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query3);

        var source4 = new List<int>() { 0, 1, 2, 3, 4 };
        var query4 = source4.take(^6..^3);
        source4.RemoveAt(0);
        source4.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query4);
    }

        [Fact]
    public void MutableSourceNotList()
    {
        var source1 = new List<int>() { 0, 1, 2, 3, 4 };
        var query1 = ForceNotCollection(source1).Select(i => i).take(3);
        source1.RemoveAt(0);
        source1.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query1);

        var source2 = new List<int>() { 0, 1, 2, 3, 4 };
        var query2 = ForceNotCollection(source2).Select(i => i).take(0..3);
        source2.RemoveAt(0);
        source2.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query2);

        var source3 = new List<int>() { 0, 1, 2, 3, 4 };
        var query3 = ForceNotCollection(source3).Select(i => i).take(^6..3);
        source3.RemoveAt(0);
        source3.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query3);

        var source4 = new List<int>() { 0, 1, 2, 3, 4 };
        var query4 = ForceNotCollection(source4).Select(i => i).take(^6..^3);
        source4.RemoveAt(0);
        source4.InsertRange(2, new[] { -1, -2 });
        assertEquals(new[] { 1, 2, -1 }, query4);
    }

        [Fact]
    public void NonEmptySource_ConsistencyWithCountable()
    {
        Func<int[]> source = () => new[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        // Multiple elements in the middle.
        assertEquals(source()[^9..5], source().take(^9..5));
        assertEquals(source()[2..7], source().take(2..7));
        assertEquals(source()[2..^4], source().take(2..^4));
        assertEquals(source()[^7..^4], source().take(^7..^4));

        // Range with default index.
        assertEquals(source()[^9..], source().take(^9..));
        assertEquals(source()[2..], source().take(2..));
        assertEquals(source()[..^4], source().take(..^4));
        assertEquals(source()[..6], source().take(..6));

        // All.
        assertEquals(source()[..], source().take(..));

        // Single element in the middle.
        assertEquals(source()[^9..2], source().take(^9..2));
        assertEquals(source()[2..3], source().take(2..3));
        assertEquals(source()[2..^7], source().take(2..^7));
        assertEquals(source()[^5..^4], source().take(^5..^4));

        // Single element at start.
        assertEquals(source()[^10..1], source().take(^10..1));
        assertEquals(source()[0..1], source().take(0..1));
        assertEquals(source()[0..^9], source().take(0..^9));
        assertEquals(source()[^10..^9], source().take(^10..^9));

        // Single element at end.
        assertEquals(source()[^1..10], source().take(^1..10));
        assertEquals(source()[9..10], source().take(9..10));
        assertEquals(source()[9..^0], source().take(9..^0));
        assertEquals(source()[^1..^0], source().take(^1..^0));

        // No element.
        assertEquals(source()[3..3], source().take(3..3));
        assertEquals(source()[6..^4], source().take(6..^4));
        assertEquals(source()[3..^7], source().take(3..^7));
        assertEquals(source()[^3..7], source().take(^3..7));
        assertEquals(source()[^6..^6], source().take(^6..^6));
    }

        [Fact]
    public void NonEmptySource_ConsistencyWithCountable_NotList()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        // Multiple elements in the middle.
        assertEquals(source[^9..5], ForceNotCollection(source).take(^9..5));
        assertEquals(source[2..7], ForceNotCollection(source).take(2..7));
        assertEquals(source[2..^4], ForceNotCollection(source).take(2..^4));
        assertEquals(source[^7..^4], ForceNotCollection(source).take(^7..^4));

        // Range with default index.
        assertEquals(source[^9..], ForceNotCollection(source).take(^9..));
        assertEquals(source[2..], ForceNotCollection(source).take(2..));
        assertEquals(source[..^4], ForceNotCollection(source).take(..^4));
        assertEquals(source[..6], ForceNotCollection(source).take(..6));

        // All.
        assertEquals(source[..], ForceNotCollection(source).take(..));

        // Single element in the middle.
        assertEquals(source[^9..2], ForceNotCollection(source).take(^9..2));
        assertEquals(source[2..3], ForceNotCollection(source).take(2..3));
        assertEquals(source[2..^7], ForceNotCollection(source).take(2..^7));
        assertEquals(source[^5..^4], ForceNotCollection(source).take(^5..^4));

        // Single element at start.
        assertEquals(source[^10..1], ForceNotCollection(source).take(^10..1));
        assertEquals(source[0..1], ForceNotCollection(source).take(0..1));
        assertEquals(source[0..^9], ForceNotCollection(source).take(0..^9));
        assertEquals(source[^10..^9], ForceNotCollection(source).take(^10..^9));

        // Single element at end.
        assertEquals(source[^1..10], ForceNotCollection(source).take(^1..10));
        assertEquals(source[9..10], ForceNotCollection(source).take(9..10));
        assertEquals(source[9..^0], ForceNotCollection(source).take(9..^0));
        assertEquals(source[^1..^0], ForceNotCollection(source).take(^1..^0));

        // No element.
        assertEquals(source[3..3], ForceNotCollection(source).take(3..3));
        assertEquals(source[6..^4], ForceNotCollection(source).take(6..^4));
        assertEquals(source[3..^7], ForceNotCollection(source).take(3..^7));
        assertEquals(source[^3..7], ForceNotCollection(source).take(^3..7));
        assertEquals(source[^6..^6], ForceNotCollection(source).take(^6..^6));
    }

        [Fact]
    public void NonEmptySource_ConsistencyWithCountable_ListPartition()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        // Multiple elements in the middle.
        assertEquals(source[^9..5], ListPartitionOrEmpty(source).take(^9..5));
        assertEquals(source[2..7], ListPartitionOrEmpty(source).take(2..7));
        assertEquals(source[2..^4], ListPartitionOrEmpty(source).take(2..^4));
        assertEquals(source[^7..^4], ListPartitionOrEmpty(source).take(^7..^4));

        // Range with default index.
        assertEquals(source[^9..], ListPartitionOrEmpty(source).take(^9..));
        assertEquals(source[2..], ListPartitionOrEmpty(source).take(2..));
        assertEquals(source[..^4], ListPartitionOrEmpty(source).take(..^4));
        assertEquals(source[..6], ListPartitionOrEmpty(source).take(..6));

        // All.
        assertEquals(source[..], ListPartitionOrEmpty(source).take(..));

        // Single element in the middle.
        assertEquals(source[^9..2], ListPartitionOrEmpty(source).take(^9..2));
        assertEquals(source[2..3], ListPartitionOrEmpty(source).take(2..3));
        assertEquals(source[2..^7], ListPartitionOrEmpty(source).take(2..^7));
        assertEquals(source[^5..^4], ListPartitionOrEmpty(source).take(^5..^4));

        // Single element at start.
        assertEquals(source[^10..1], ListPartitionOrEmpty(source).take(^10..1));
        assertEquals(source[0..1], ListPartitionOrEmpty(source).take(0..1));
        assertEquals(source[0..^9], ListPartitionOrEmpty(source).take(0..^9));
        assertEquals(source[^10..^9], ListPartitionOrEmpty(source).take(^10..^9));

        // Single element at end.
        assertEquals(source[^1..10], ListPartitionOrEmpty(source).take(^1..10));
        assertEquals(source[9..10], ListPartitionOrEmpty(source).take(9..10));
        assertEquals(source[9..^0], ListPartitionOrEmpty(source).take(9..^0));
        assertEquals(source[^1..^0], ListPartitionOrEmpty(source).take(^1..^0));

        // No element.
        assertEquals(source[3..3], ListPartitionOrEmpty(source).take(3..3));
        assertEquals(source[6..^4], ListPartitionOrEmpty(source).take(6..^4));
        assertEquals(source[3..^7], ListPartitionOrEmpty(source).take(3..^7));
        assertEquals(source[^3..7], ListPartitionOrEmpty(source).take(^3..7));
        assertEquals(source[^6..^6], ListPartitionOrEmpty(source).take(^6..^6));
    }

        [Fact]
    public void NonEmptySource_ConsistencyWithCountable_EnumerablePartition()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        // Multiple elements in the middle.
        assertEquals(source[^9..5], EnumerablePartitionOrEmpty(source).take(^9..5));
        assertEquals(source[2..7], EnumerablePartitionOrEmpty(source).take(2..7));
        assertEquals(source[2..^4], EnumerablePartitionOrEmpty(source).take(2..^4));
        assertEquals(source[^7..^4], EnumerablePartitionOrEmpty(source).take(^7..^4));

        // Range with default index.
        assertEquals(source[^9..], EnumerablePartitionOrEmpty(source).take(^9..));
        assertEquals(source[2..], EnumerablePartitionOrEmpty(source).take(2..));
        assertEquals(source[..^4], EnumerablePartitionOrEmpty(source).take(..^4));
        assertEquals(source[..6], EnumerablePartitionOrEmpty(source).take(..6));

        // All.
        assertEquals(source[..], EnumerablePartitionOrEmpty(source).take(..));

        // Single element in the middle.
        assertEquals(source[^9..2], EnumerablePartitionOrEmpty(source).take(^9..2));
        assertEquals(source[2..3], EnumerablePartitionOrEmpty(source).take(2..3));
        assertEquals(source[2..^7], EnumerablePartitionOrEmpty(source).take(2..^7));
        assertEquals(source[^5..^4], EnumerablePartitionOrEmpty(source).take(^5..^4));

        // Single element at start.
        assertEquals(source[^10..1], EnumerablePartitionOrEmpty(source).take(^10..1));
        assertEquals(source[0..1], EnumerablePartitionOrEmpty(source).take(0..1));
        assertEquals(source[0..^9], EnumerablePartitionOrEmpty(source).take(0..^9));
        assertEquals(source[^10..^9], EnumerablePartitionOrEmpty(source).take(^10..^9));

        // Single element at end.
        assertEquals(source[^1..10], EnumerablePartitionOrEmpty(source).take(^1..10));
        assertEquals(source[9..10], EnumerablePartitionOrEmpty(source).take(9..10));
        assertEquals(source[9..^0], EnumerablePartitionOrEmpty(source).take(9..^0));
        assertEquals(source[^1..^0], EnumerablePartitionOrEmpty(source).take(^1..^0));

        // No element.
        assertEquals(source[3..3], EnumerablePartitionOrEmpty(source).take(3..3));
        assertEquals(source[6..^4], EnumerablePartitionOrEmpty(source).take(6..^4));
        assertEquals(source[3..^7], EnumerablePartitionOrEmpty(source).take(3..^7));
        assertEquals(source[^3..7], EnumerablePartitionOrEmpty(source).take(^3..7));
        assertEquals(source[^6..^6], EnumerablePartitionOrEmpty(source).take(^6..^6));
    }

        [Fact]
    public void NonEmptySource_DoNotThrowException()
    {
        Func<int[]> source = () => new[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Assert.Empty(source().take(3..2));
        Assert.Empty(source().take(6..^5));
        Assert.Empty(source().take(3..^8));
        Assert.Empty(source().take(^6..^7));
    }

        [Fact]
    public void NonEmptySource_DoNotThrowException_NotList()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Assert.Empty(ForceNotCollection(source).take(3..2));
        Assert.Empty(ForceNotCollection(source).take(6..^5));
        Assert.Empty(ForceNotCollection(source).take(3..^8));
        Assert.Empty(ForceNotCollection(source).take(^6..^7));
    }

        [Fact]
    public void NonEmptySource_DoNotThrowException_ListPartition()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Assert.Empty(ListPartitionOrEmpty(source).take(3..2));
        Assert.Empty(ListPartitionOrEmpty(source).take(6..^5));
        Assert.Empty(ListPartitionOrEmpty(source).take(3..^8));
        Assert.Empty(ListPartitionOrEmpty(source).take(^6..^7));
    }

        [Fact]
    public void NonEmptySource_DoNotThrowException_EnumerablePartition()
    {
        int[] source = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..2));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(6..^5));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..^8));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^6..^7));
    }

        [Fact]
    public void EmptySource_DoNotThrowException()
    {
        Func<int[]> source = () => new int[] { };

        // Multiple elements in the middle.
        Assert.Empty(source().take(^9..5));
        Assert.Empty(source().take(2..7));
        Assert.Empty(source().take(2..^4));
        Assert.Empty(source().take(^7..^4));

        // Range with default index.
        Assert.Empty(source().take(^9..));
        Assert.Empty(source().take(2..));
        Assert.Empty(source().take(..^4));
        Assert.Empty(source().take(..6));

        // All.
        assertEquals(source()[..], source().take(..));

        // Single element in the middle.
        Assert.Empty(source().take(^9..2));
        Assert.Empty(source().take(2..3));
        Assert.Empty(source().take(2..^7));
        Assert.Empty(source().take(^5..^4));

        // Single element at start.
        Assert.Empty(source().take(^10..1));
        Assert.Empty(source().take(0..1));
        Assert.Empty(source().take(0..^9));
        Assert.Empty(source().take(^10..^9));

        // Single element at end.
        Assert.Empty(source().take(^1..^10));
        Assert.Empty(source().take(9..10));
        Assert.Empty(source().take(9..^9));
        Assert.Empty(source().take(^1..^9));

        // No element.
        Assert.Empty(source().take(3..3));
        Assert.Empty(source().take(6..^4));
        Assert.Empty(source().take(3..^7));
        Assert.Empty(source().take(^3..7));
        Assert.Empty(source().take(^6..^6));

        // Invalid range.
        Assert.Empty(source().take(3..2));
        Assert.Empty(source().take(6..^5));
        Assert.Empty(source().take(3..^8));
        Assert.Empty(source().take(^6..^7));
    }

        [Fact]
    public void EmptySource_DoNotThrowException_NotList()
    {
        int[] source = { };

        // Multiple elements in the middle.
        Assert.Empty(ForceNotCollection(source).take(^9..5));
        Assert.Empty(ForceNotCollection(source).take(2..7));
        Assert.Empty(ForceNotCollection(source).take(2..^4));
        Assert.Empty(ForceNotCollection(source).take(^7..^4));

        // Range with default index.
        Assert.Empty(ForceNotCollection(source).take(^9..));
        Assert.Empty(ForceNotCollection(source).take(2..));
        Assert.Empty(ForceNotCollection(source).take(..^4));
        Assert.Empty(ForceNotCollection(source).take(..6));

        // All.
        assertEquals(source[..], ForceNotCollection(source).take(..));

        // Single element in the middle.
        Assert.Empty(ForceNotCollection(source).take(^9..2));
        Assert.Empty(ForceNotCollection(source).take(2..3));
        Assert.Empty(ForceNotCollection(source).take(2..^7));
        Assert.Empty(ForceNotCollection(source).take(^5..^4));

        // Single element at start.
        Assert.Empty(ForceNotCollection(source).take(^10..1));
        Assert.Empty(ForceNotCollection(source).take(0..1));
        Assert.Empty(ForceNotCollection(source).take(0..^9));
        Assert.Empty(ForceNotCollection(source).take(^10..^9));

        // Single element at end.
        Assert.Empty(ForceNotCollection(source).take(^1..^10));
        Assert.Empty(ForceNotCollection(source).take(9..10));
        Assert.Empty(ForceNotCollection(source).take(9..^9));
        Assert.Empty(ForceNotCollection(source).take(^1..^9));

        // No element.
        Assert.Empty(ForceNotCollection(source).take(3..3));
        Assert.Empty(ForceNotCollection(source).take(6..^4));
        Assert.Empty(ForceNotCollection(source).take(3..^7));
        Assert.Empty(ForceNotCollection(source).take(^3..7));
        Assert.Empty(ForceNotCollection(source).take(^6..^6));

        // Invalid range.
        Assert.Empty(ForceNotCollection(source).take(3..2));
        Assert.Empty(ForceNotCollection(source).take(6..^5));
        Assert.Empty(ForceNotCollection(source).take(3..^8));
        Assert.Empty(ForceNotCollection(source).take(^6..^7));
    }

        [Fact]
    public void EmptySource_DoNotThrowException_ListPartition()
    {
        int[] source = { };

        // Multiple elements in the middle.
        Assert.Empty(ListPartitionOrEmpty(source).take(^9..5));
        Assert.Empty(ListPartitionOrEmpty(source).take(2..7));
        Assert.Empty(ListPartitionOrEmpty(source).take(2..^4));
        Assert.Empty(ListPartitionOrEmpty(source).take(^7..^4));

        // Range with default index.
        Assert.Empty(ListPartitionOrEmpty(source).take(^9..));
        Assert.Empty(ListPartitionOrEmpty(source).take(2..));
        Assert.Empty(ListPartitionOrEmpty(source).take(..^4));
        Assert.Empty(ListPartitionOrEmpty(source).take(..6));

        // All.
        assertEquals(source[..], ListPartitionOrEmpty(source).take(..));

        // Single element in the middle.
        Assert.Empty(ListPartitionOrEmpty(source).take(^9..2));
        Assert.Empty(ListPartitionOrEmpty(source).take(2..3));
        Assert.Empty(ListPartitionOrEmpty(source).take(2..^7));
        Assert.Empty(ListPartitionOrEmpty(source).take(^5..^4));

        // Single element at start.
        Assert.Empty(ListPartitionOrEmpty(source).take(^10..1));
        Assert.Empty(ListPartitionOrEmpty(source).take(0..1));
        Assert.Empty(ListPartitionOrEmpty(source).take(0..^9));
        Assert.Empty(ListPartitionOrEmpty(source).take(^10..^9));

        // Single element at end.
        Assert.Empty(ListPartitionOrEmpty(source).take(^1..^10));
        Assert.Empty(ListPartitionOrEmpty(source).take(9..10));
        Assert.Empty(ListPartitionOrEmpty(source).take(9..^9));
        Assert.Empty(ListPartitionOrEmpty(source).take(^1..^9));

        // No element.
        Assert.Empty(ListPartitionOrEmpty(source).take(3..3));
        Assert.Empty(ListPartitionOrEmpty(source).take(6..^4));
        Assert.Empty(ListPartitionOrEmpty(source).take(3..^7));
        Assert.Empty(ListPartitionOrEmpty(source).take(^3..7));
        Assert.Empty(ListPartitionOrEmpty(source).take(^6..^6));

        // Invalid range.
        Assert.Empty(ListPartitionOrEmpty(source).take(3..2));
        Assert.Empty(ListPartitionOrEmpty(source).take(6..^5));
        Assert.Empty(ListPartitionOrEmpty(source).take(3..^8));
        Assert.Empty(ListPartitionOrEmpty(source).take(^6..^7));
    }

        [Fact]
    public void EmptySource_DoNotThrowException_EnumerablePartition()
    {
        int[] source = { };

        // Multiple elements in the middle.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^9..5));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(2..7));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(2..^4));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^7..^4));

        // Range with default index.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^9..));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(2..));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(..^4));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(..6));

        // All.
        assertEquals(source[..], EnumerablePartitionOrEmpty(source).take(..));

        // Single element in the middle.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^9..2));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(2..3));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(2..^7));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^5..^4));

        // Single element at start.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^10..1));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(0..1));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(0..^9));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^10..^9));

        // Single element at end.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^1..^10));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(9..10));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(9..^9));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^1..^9));

        // No element.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..3));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(6..^4));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..^7));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^3..7));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^6..^6));

        // Invalid range.
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..2));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(6..^5));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(3..^8));
        Assert.Empty(EnumerablePartitionOrEmpty(source).take(^6..^7));
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
        assertIsType(Linq.empty().getClass(), emptySource2.skip(2));
        try (IEnumerator<Integer> e = emptySource2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }
}
