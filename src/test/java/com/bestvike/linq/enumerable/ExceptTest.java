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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ExceptTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.asEnumerable(1, 9, null, 4);

        assertEquals(q1.except(q2), q1.except(q2));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> q2 = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        q1.except(q2);
        q1.except(q2);

        assertEquals(q1.except(q2), q1.except(q2));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(new int[0]), Linq.asEnumerable(new int[0]), null, Linq.asEnumerable(new int[0])});
        lst.add(new Object[]{Linq.asEnumerable(new int[0]), Linq.asEnumerable(new int[]{-6, -8, -6, 2, 0, 0, 5, 6}), null, Linq.asEnumerable(new int[0])});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{1, 1, 1, 1, 1}), Linq.asEnumerable(new int[]{2, 3, 4}), null, Linq.asEnumerable(new int[]{1})});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Init() {
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
        lst.add(new Object[]{Linq.asEnumerable(new String[1]), Linq.asEnumerable(), defaultComparer, Linq.asEnumerable(new String[1])});
        lst.add(new Object[]{Linq.asEnumerable(null, null, Empty), Linq.asEnumerable(new String[1]), defaultComparer, Linq.asEnumerable(new String[]{Empty})});
        lst.add(new Object[]{Linq.asEnumerable(new String[2]), Linq.asEnumerable(), defaultComparer, Linq.asEnumerable(new String[1])});
        lst.add(new Object[]{Linq.asEnumerable("Bob", "Tim", "Robert", "Chris"), Linq.asEnumerable("bBo", "shriC"), null, Linq.asEnumerable("Bob", "Tim", "Robert", "Chris")});
        lst.add(new Object[]{Linq.asEnumerable("Bob", "Tim", "Robert", "Chris"), Linq.asEnumerable("bBo", "shriC"), new AnagramEqualityComparer(), Linq.asEnumerable("Tim", "Robert")});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void String() {
        for (Object[] objects : this.String_TestData()) {
            this.String((IEnumerable<String>) objects[0], (IEnumerable<String>) objects[1], (IEqualityComparer<String>) objects[2], (IEnumerable<String>) objects[3]);
        }
    }

    public void String(IEnumerable<String> first, IEnumerable<String> second, IEqualityComparer<String> comparer, IEnumerable<String> expected) {
        if (comparer == null) {
            assertEquals(expected, first.except(second));
        }
        assertEquals(expected, first.except(second, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(-6, -8, -6, 2, 0, 0, 5, 6, null, null), Linq.asEnumerable(), Linq.asEnumerable(-6, -8, 2, 0, 5, 6, null)});
        lst.add(new Object[]{Linq.asEnumerable(1, 2, 2, 3, 4, 5), Linq.asEnumerable(5, 3, 2, 6, 6, 3, 1, null, null), Linq.asEnumerable(4)});
        lst.add(new Object[]{Linq.asEnumerable(2, 3, null, 2, null, 4, 5), Linq.asEnumerable(1, 9, null, 4), Linq.asEnumerable(2, 3, 5)});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableInt((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (IEnumerable<Integer>) objects[2]);
        }
    }

    private void NullableInt(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.except(second));
    }

    @Test
    public void NullableIntRunOnce() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableIntRunOnce((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (IEnumerable<Integer>) objects[2]);
        }
    }

    private void NullableIntRunOnce(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.runOnce().except(second.runOnce()));
    }

    @Test
    public void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.asEnumerable("bBo", "shriC");

        assertThrows(NullPointerException.class, () -> first.except(second));
        assertThrows(NullPointerException.class, () -> first.except(second, new AnagramEqualityComparer()));
    }

    @Test
    public void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.asEnumerable("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.except(second));
        assertThrows(ArgumentNullException.class, () -> first.except(second, new AnagramEqualityComparer()));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).except(Linq.range(0, 3));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        IEnumerable<String> input1 = Linq.asEnumerable(new String[]{"a"});
        IEnumerable<String> input2 = Linq.asEnumerable(new String[]{"A"});

        assertEquals(Linq.asEnumerable(new String[]{"a"}), input1.except(input2));
        assertEquals(Linq.asEnumerable(new String[]{"a"}), input1.except(input2, null));
        assertEquals(Linq.asEnumerable(new String[]{"a"}), input1.except(input2, EqualityComparer.Default()));
        assertEquals(Linq.empty(), input1.except(input2, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.asEnumerable(new String[]{"A"}), input2.except(input1));
        assertEquals(Linq.asEnumerable(new String[]{"A"}), input2.except(input1, null));
        assertEquals(Linq.asEnumerable(new String[]{"A"}), input2.except(input1, EqualityComparer.Default()));
        assertEquals(Enumerable.empty(), input2.except(input1, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void testExcept() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        assertEquals(3, Linq.asEnumerable(emps)
                .except(Linq.asEnumerable(emps2))
                .count());

        IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        assertTrue(oneToHundred.except(oneToFifty).sequenceEqual(fiftyOneToHundred));
    }

    @Test
    public void testExceptWithComparer() {
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
        assertEquals(1, Linq.asEnumerable(emps)
                .except(Linq.asEnumerable(emps2), comparer)
                .count());
    }
}
