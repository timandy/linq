package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ExceptTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.of(1, 9, null, 4);

        assertEquals(q1.except(q2), q1.except(q2));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> q2 = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        q1.except(q2);
        q1.except(q2);

        assertEquals(q1.except(q2), q1.except(q2));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.of(new int[0]), Linq.of(new int[0]), null, Linq.of(new int[0])});
        lst.add(new Object[]{Linq.of(new int[0]), Linq.of(new int[]{-6, -8, -6, 2, 0, 0, 5, 6}), null, Linq.of(new int[0])});
        lst.add(new Object[]{Linq.of(new int[]{1, 1, 1, 1, 1}), Linq.of(new int[]{2, 3, 4}), null, Linq.of(new int[]{1})});
        return Linq.of(lst);
    }

    @Test
    void Init() {
        for (Object[] objects : this.Int_TestData()) {
            this.Int((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (IEqualityComparer<Integer>) objects[2], (IEnumerable<Integer>) objects[3]);
        }
    }

    private void Int(IEnumerable<Integer> first, IEnumerable<Integer> second, IEqualityComparer<Integer> comparer, IEnumerable<Integer> expected) {
        if (comparer == null) {
            assertEquals(expected, first.except(second));
        }
        assertEquals(expected, first.except(second, comparer));
    }

    private IEnumerable<Object[]> String_TestData() {
        IEqualityComparer<String> defaultComparer = EqualityComparer.Default();

        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.of(new String[1]), Linq.of(), defaultComparer, Linq.of(new String[1])});
        lst.add(new Object[]{Linq.of(null, null, Empty), Linq.of(new String[1]), defaultComparer, Linq.of(Empty)});
        lst.add(new Object[]{Linq.of(new String[2]), Linq.of(), defaultComparer, Linq.of(new String[1])});
        lst.add(new Object[]{Linq.of("Bob", "Tim", "Robert", "Chris"), Linq.of("bBo", "shriC"), null, Linq.of("Bob", "Tim", "Robert", "Chris")});
        lst.add(new Object[]{Linq.of("Bob", "Tim", "Robert", "Chris"), Linq.of("bBo", "shriC"), new AnagramEqualityComparer(), Linq.of("Tim", "Robert")});
        return Linq.of(lst);
    }

    @Test
    void String() {
        for (Object[] objects : this.String_TestData()) {
            this.String((IEnumerable<String>) objects[0], (IEnumerable<String>) objects[1], (IEqualityComparer<String>) objects[2], (IEnumerable<String>) objects[3]);
        }
    }

    void String(IEnumerable<String> first, IEnumerable<String> second, IEqualityComparer<String> comparer, IEnumerable<String> expected) {
        if (comparer == null) {
            assertEquals(expected, first.except(second));
        }
        assertEquals(expected, first.except(second, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.of(-6, -8, -6, 2, 0, 0, 5, 6, null, null), Linq.of(), Linq.of(-6, -8, 2, 0, 5, 6, null)});
        lst.add(new Object[]{Linq.of(1, 2, 2, 3, 4, 5), Linq.of(5, 3, 2, 6, 6, 3, 1, null, null), Linq.of(4)});
        lst.add(new Object[]{Linq.of(2, 3, null, 2, null, 4, 5), Linq.of(1, 9, null, 4), Linq.of(2, 3, 5)});
        return Linq.of(lst);
    }

    @Test
    void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableInt((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (IEnumerable<Integer>) objects[2]);
        }
    }

    private void NullableInt(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.except(second));
    }

    @Test
    void NullableIntRunOnce() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableIntRunOnce((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (IEnumerable<Integer>) objects[2]);
        }
    }

    private void NullableIntRunOnce(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.runOnce().except(second.runOnce()));
    }

    @Test
    void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.of("bBo", "shriC");

        assertThrows(NullPointerException.class, () -> first.except(second));
        assertThrows(NullPointerException.class, () -> first.except(second, new AnagramEqualityComparer()));
    }

    @Test
    void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.except(second));
        assertThrows(ArgumentNullException.class, () -> first.except(second, new AnagramEqualityComparer()));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).except(Linq.range(0, 3));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        IEnumerable<String> input1 = Linq.of("a");
        IEnumerable<String> input2 = Linq.of("A");

        assertEquals(Linq.of("a"), input1.except(input2));
        assertEquals(Linq.of("a"), input1.except(input2, null));
        assertEquals(Linq.of("a"), input1.except(input2, EqualityComparer.Default()));
        assertEquals(Linq.empty(), input1.except(input2, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.of("A"), input2.except(input1));
        assertEquals(Linq.of("A"), input2.except(input1, null));
        assertEquals(Linq.of("A"), input2.except(input1, EqualityComparer.Default()));
        assertEquals(Enumerable.empty(), input2.except(input1, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void testExcept() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        assertEquals(3, Linq.of(emps)
                .except(Linq.of(emps2))
                .count());

        IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        assertTrue(oneToHundred.except(oneToFifty).sequenceEqual(fiftyOneToHundred));
    }

    @Test
    void testExceptWithComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno == null ? 0 : obj.deptno;
            }
        };

        Employee[] emps2 = {new Employee(150, "Theodore", 10)};
        assertEquals(1, Linq.of(emps)
                .except(Linq.of(emps2), comparer)
                .count());
    }
}
