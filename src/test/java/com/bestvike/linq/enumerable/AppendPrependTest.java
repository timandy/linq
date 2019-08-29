package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class AppendPrependTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQueryAppend() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);

        assertEquals(q1.append(42), q1.append(42));
        assertEquals(q1.append(42), q1.concat(Linq.singleton(42)));
    }

    @Test
    void SameResultsRepeatCallsIntQueryPrepend() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);

        assertEquals(q1.prepend(42), q1.prepend(42));
        assertEquals(q1.prepend(42), Linq.singleton(42).concat(q1));
    }

    @Test
    void SameResultsRepeatCallsStringQueryAppend() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");

        assertEquals(q1.append("hi"), q1.append("hi"));
        assertEquals(q1.append("hi"), q1.concat(Linq.singleton("hi")));
    }

    @Test
    void SameResultsRepeatCallsStringQueryPrepend() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");

        assertEquals(q1.prepend("hi"), q1.prepend("hi"));
        assertEquals(q1.prepend("hi"), Linq.singleton("hi").concat(q1));
    }

    @Test
    void RepeatIteration() {
        IEnumerable<Integer> q = Linq.range(3, 4).append(12);
        assertEquals(q, q);
        q = q.append(14);
        assertEquals(q, q);
    }

    @Test
    void EmptyAppend() {
        IEnumerable<Integer> first = Linq.empty();
        assertEquals(first.append(42), Linq.singleton(42));
    }

    @Test
    void EmptyPrepend() {
        IEnumerable<String> first = Linq.empty();
        assertEquals(first.prepend("aa"), Linq.singleton("aa"));
    }

    @Test
    void PrependNoIteratingSourceBeforeFirstItem() {
        List<Integer> ie = new ArrayList<>();
        IEnumerable<Integer> prepended = Linq.of(ie).prepend(4);
        ie.add(42);
        assertEquals(prepended, Linq.of(ie).prepend(4));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumeratePrepend() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).prepend(4);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateAppend() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).append(4);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateMultipleAppendsAndPrepends() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).append(4).append(5).prepend(-1).prepend(-2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = as(iterator, IEnumerator.class);
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void SourceNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).append(1));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).prepend(1));
    }

    @Test
    void Combined() {
        IEnumerable<Character> v = Linq.chars("foo").append('1').append('2').prepend('3').concat(Linq.chars("qq").append('Q').prepend('W'));

        assertEquals(v.toArray(), Linq.chars("3foo12WqqQ").toArray());

        IEnumerable<Character> v1 = Linq.chars("a").append('b').append('c').append('d');

        assertEquals(v1.toArray(), Linq.chars("abcd").toArray());

        IEnumerable<Character> v2 = Linq.chars("a").prepend('b').prepend('c').prepend('d');

        assertEquals(v2.toArray(), Linq.chars("dcba").toArray());
    }

    @Test
    void AppendCombinations() {
        IEnumerable<Integer> source = Linq.range(0, 3).append(3).append(4);
        IEnumerable<Integer> app0a = source.append(5);
        IEnumerable<Integer> app0b = source.append(6);
        IEnumerable<Integer> app1aa = app0a.append(7);
        IEnumerable<Integer> app1ab = app0a.append(8);
        IEnumerable<Integer> app1ba = app0b.append(9);
        IEnumerable<Integer> app1bb = app0b.append(10);

        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), app0a);
        assertEquals(Linq.of(0, 1, 2, 3, 4, 6), app0b);
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 7), app1aa);
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5, 8), app1ab);
        assertEquals(Linq.of(0, 1, 2, 3, 4, 6, 9), app1ba);
        assertEquals(Linq.of(0, 1, 2, 3, 4, 6, 10), app1bb);
    }

    @Test
    void PrependCombinations() {
        IEnumerable<Integer> source = Linq.range(2, 2).prepend(1).prepend(0);
        IEnumerable<Integer> pre0a = source.prepend(5);
        IEnumerable<Integer> pre0b = source.prepend(6);
        IEnumerable<Integer> pre1aa = pre0a.prepend(7);
        IEnumerable<Integer> pre1ab = pre0a.prepend(8);
        IEnumerable<Integer> pre1ba = pre0b.prepend(9);
        IEnumerable<Integer> pre1bb = pre0b.prepend(10);

        assertEquals(Linq.of(5, 0, 1, 2, 3), pre0a);
        assertEquals(Linq.of(6, 0, 1, 2, 3), pre0b);
        assertEquals(Linq.of(7, 5, 0, 1, 2, 3), pre1aa);
        assertEquals(Linq.of(8, 5, 0, 1, 2, 3), pre1ab);
        assertEquals(Linq.of(9, 6, 0, 1, 2, 3), pre1ba);
        assertEquals(Linq.of(10, 6, 0, 1, 2, 3), pre1bb);
    }

    @Test
    void Append1ToArrayToList() {
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
    void Prepend1ToArrayToList() {
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
    void AppendNToArrayToList() {
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
    void PrependNToArrayToList() {
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
    void AppendPrependToArrayToList() {
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
    void AppendPrependRunOnce() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(2, 2).runOnce().prepend(1).runOnce().prepend(0).runOnce().append(4).runOnce().append(5).runOnce();
        assertEquals(Linq.range(0, 6), source.toArray());
        source = NumberRangeGuaranteedNotCollectionType(2, 2).prepend(1).prepend(0).append(4).append(5).runOnce();
        assertEquals(Linq.range(0, 6), source.toArray());
    }

    @Test
    void testAppend() {
        String s = Linq.of(emps).append(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Fred, Bill, Eric, Janet, Cedric]", s);

        List<Employee> result = new ArrayList<>(Arrays.asList(emps));
        result.add(badEmps[0]);
        IEnumerable<Employee> expected = Linq.of(result);

        IEnumerable<Employee> source = Linq.of(emps).append(badEmps[0]);
        assertEquals(source.runOnce(), source.runOnce());

        assertEquals(expected, Linq.of(Linq.of(emps).append(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(emps).append(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(emps).append(badEmps[0]).toList()));

        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).append(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).append(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).append(badEmps[0]).toList()));

        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).append(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).append(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).append(badEmps[0]).toList()));
    }

    @Test
    void testPrepend() {
        String s = Linq.of(emps).prepend(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Fred, Bill, Eric, Janet]", s);

        List<Employee> result = new ArrayList<>(Collections.singletonList(badEmps[0]));
        result.addAll(Arrays.asList(emps));
        IEnumerable<Employee> expected = Linq.of(result);

        IEnumerable<Employee> source = Linq.of(emps).prepend(badEmps[0]);
        assertEquals(source.runOnce(), source.runOnce());

        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).toList()));

        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).toList()));

        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).toList()));
    }

    @Test
    void testAppendPrepend() {
        List<Employee> result = new ArrayList<>(Collections.singletonList(badEmps[0]));
        result.addAll(Arrays.asList(emps));
        result.add(badEmps[1]);
        IEnumerable<Employee> expected = Linq.of(result);

        IEnumerable<Employee> source = Linq.of(emps).prepend(badEmps[0]).append(badEmps[1]);
        assertEquals(source.runOnce(), source.runOnce());

        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).append(badEmps[1]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).append(badEmps[1]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(emps).prepend(badEmps[0]).append(badEmps[1]).toList()));

        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).append(badEmps[1]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).append(badEmps[1]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(Arrays.asList(emps)).prepend(badEmps[0]).append(badEmps[1]).toList()));

        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).append(badEmps[1]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).append(badEmps[1]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).prepend(badEmps[0]).append(badEmps[1]).toList()));

        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).take(99).prepend(badEmps[0]).append(badEmps[1]).toArray()));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).take(99).prepend(badEmps[0]).append(badEmps[1]).toArray(Employee.class)));
        assertEquals(expected, Linq.of(Linq.of(new LinkedList<>(Arrays.asList(emps))).take(99).prepend(badEmps[0]).append(badEmps[1]).toList()));
    }
}
