package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AppendPrependTest extends EnumerableTest {
    @Test
    public void SameResultsRepeatCallsIntQueryAppend() {
        IEnumerable<Integer> q1 = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);

        assertEquals(q1.append(42), q1.append(42));
        assertEquals(q1.append(42), q1.concat(Linq.singleton(42)));
    }

    @Test
    public void SameResultsRepeatCallsIntQueryPrepend() {
        IEnumerable<Integer> q1 = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);

        assertEquals(q1.prepend(42), q1.prepend(42));
        assertEquals(q1.prepend(42), Linq.singleton(42).concat(q1));
    }

    @Test
    public void SameResultsRepeatCallsStringQueryAppend() {
        IEnumerable<String> q1 = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");

        assertEquals(q1.append("hi"), q1.append("hi"));
        assertEquals(q1.append("hi"), q1.concat(Linq.singleton("hi")));
    }

    @Test
    public void SameResultsRepeatCallsStringQueryPrepend() {
        IEnumerable<String> q1 = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");

        assertEquals(q1.prepend("hi"), q1.prepend("hi"));
        assertEquals(q1.prepend("hi"), Linq.singleton("hi").concat(q1));
    }

    @Test
    public void RepeatIteration() {
        IEnumerable<Integer> q = Linq.range(3, 4).append(12);
        assertEquals(q, q);
        q = q.append(14);
        assertEquals(q, q);
    }

    @Test
    public void EmptyAppend() {
        IEnumerable<Integer> first = Linq.empty();
        assertEquals(first.append(42), Linq.singleton(42));
    }

    @Test
    public void EmptyPrepend() {
        IEnumerable<String> first = Linq.empty();
        assertEquals(first.prepend("aa"), Linq.singleton("aa"));
    }

    @Test
    public void PrependNoIteratingSourceBeforeFirstItem() {
        List<Integer> ie = new ArrayList<>();
        IEnumerable<Integer> prepended = Linq.asEnumerable(ie).prepend(4);
        ie.add(42);
        assertEquals(prepended, Linq.asEnumerable(ie).prepend(4));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumeratePrepend() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).prepend(4);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateAppend() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).append(4);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateMultipleAppendsAndPrepends() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).append(4).append(5).prepend(-1).prepend(-2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void SourceNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).append(1));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).prepend(1));
    }

    @Test
    public void Combined() {
        IEnumerable<Character> v = Linq.asEnumerable("foo").append('1').append('2').prepend('3').concat(Linq.asEnumerable("qq").append('Q').prepend('W'));

        assertEquals(v.toArray(), Linq.asEnumerable("3foo12WqqQ").toArray());

        IEnumerable<Character> v1 = Linq.asEnumerable("a").append('b').append('c').append('d');

        assertEquals(v1.toArray(), Linq.asEnumerable("abcd").toArray());

        IEnumerable<Character> v2 = Linq.asEnumerable("a").prepend('b').prepend('c').prepend('d');

        assertEquals(v2.toArray(), Linq.asEnumerable("dcba").toArray());
    }

    @Test
    public void AppendCombinations() {
        IEnumerable<Integer> source = Linq.range(0, 3).append(3).append(4);
        IEnumerable<Integer> app0a = source.append(5);
        IEnumerable<Integer> app0b = source.append(6);
        IEnumerable<Integer> app1aa = app0a.append(7);
        IEnumerable<Integer> app1ab = app0a.append(8);
        IEnumerable<Integer> app1ba = app0b.append(9);
        IEnumerable<Integer> app1bb = app0b.append(10);

        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 5), app0a);
        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 6), app0b);
        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 5, 7), app1aa);
        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 5, 8), app1ab);
        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 6, 9), app1ba);
        assertEquals(Linq.asEnumerable(0, 1, 2, 3, 4, 6, 10), app1bb);
    }

    @Test
    public void PrependCombinations() {
        IEnumerable<Integer> source = Linq.range(2, 2).prepend(1).prepend(0);
        IEnumerable<Integer> pre0a = source.prepend(5);
        IEnumerable<Integer> pre0b = source.prepend(6);
        IEnumerable<Integer> pre1aa = pre0a.prepend(7);
        IEnumerable<Integer> pre1ab = pre0a.prepend(8);
        IEnumerable<Integer> pre1ba = pre0b.prepend(9);
        IEnumerable<Integer> pre1bb = pre0b.prepend(10);

        assertEquals(Linq.asEnumerable(5, 0, 1, 2, 3), pre0a);
        assertEquals(Linq.asEnumerable(6, 0, 1, 2, 3), pre0b);
        assertEquals(Linq.asEnumerable(7, 5, 0, 1, 2, 3), pre1aa);
        assertEquals(Linq.asEnumerable(8, 5, 0, 1, 2, 3), pre1ab);
        assertEquals(Linq.asEnumerable(9, 6, 0, 1, 2, 3), pre1ba);
        assertEquals(Linq.asEnumerable(10, 6, 0, 1, 2, 3), pre1bb);
    }

    @Test
    public void Append1ToArrayToList() {
        IEnumerable<Integer> source = Linq.range(0, 2).append(2);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());

        source = Linq.range(0, 2).toArray().append(2);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(0, 2).append(2);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());
    }

    @Test
    public void Prepend1ToArrayToList() {
        IEnumerable<Integer> source = Linq.range(1, 2).prepend(0);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());

        source = Linq.range(1, 2).toArray().prepend(0);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(1, 2).prepend(0);
        assertEquals(Linq.range(0, 3), source.toArray());
        assertEquals(Linq.range(0, 3), source.toArray());
    }

    @Test
    public void AppendNToArrayToList() {
        IEnumerable<Integer> source = Linq.range(0, 2).append(2).append(3);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());

        source = Linq.range(0, 2).toArray().append(2).append(3);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(0, 2).append(2).append(3);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());
    }

    @Test
    public void PrependNToArrayToList() {
        IEnumerable<Integer> source = Linq.range(2, 2).prepend(1).prepend(0);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());

        source = Linq.range(2, 2).toArray().prepend(1).prepend(0);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(2, 2).prepend(1).prepend(0);
        assertEquals(Linq.range(0, 4), source.toArray());
        assertEquals(Linq.range(0, 4), source.toArray());
    }

    @Test
    public void AppendPrependToArrayToList() {
        IEnumerable<Integer> source = Linq.range(2, 2).prepend(1).append(4).prepend(0).append(5);
        assertEquals(Linq.range(0, 6), source.toArray());
        assertEquals(Linq.range(0, 6), source.toArray());

        source = Linq.range(2, 2).toArray().prepend(1).append(4).prepend(0).append(5);
        assertEquals(Linq.range(0, 6), source.toArray());
        assertEquals(Linq.range(0, 6), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(2, 2).append(4).prepend(1).append(5).prepend(0);
        assertEquals(Linq.range(0, 6), source.toArray());
        assertEquals(Linq.range(0, 6), source.toArray());

        source = NumberRangeGuaranteedNotCollectionType(2, 2).prepend(1).prepend(0).append(4).append(5);
        assertEquals(Linq.range(0, 6), source.toArray());
        assertEquals(Linq.range(0, 6), source.toArray());
    }

    @Test
    public void AppendPrependRunOnce() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(2, 2).runOnce().prepend(1).runOnce().prepend(0).runOnce().append(4).runOnce().append(5).runOnce();
        assertEquals(Linq.range(0, 6), source.toArray());
        source = NumberRangeGuaranteedNotCollectionType(2, 2).prepend(1).prepend(0).append(4).append(5).runOnce();
        assertEquals(Linq.range(0, 6), source.toArray());
    }

    @Test
    public void testAppend() {
        String s = Linq.asEnumerable(emps).append(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Fred, Bill, Eric, Janet, Cedric]", s);
    }

    @Test
    public void testPrepend() {
        String s = Linq.asEnumerable(emps).prepend(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Fred, Bill, Eric, Janet]", s);
    }
}
