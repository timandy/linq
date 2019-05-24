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
import com.bestvike.linq.util.HashSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class IntersectTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> first = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> second = Linq.asEnumerable(1, 9, null, 4);

        assertEquals(first.intersect(second), first.intersect(second));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> first = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> second = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        assertEquals(first.intersect(second), first.intersect(second));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(new int[0]), Linq.asEnumerable(new int[0]), new int[0]});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{-5, 3, -2, 6, 9}), Linq.asEnumerable(new int[]{0, 5, 2, 10, 20}), new int[0]});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{1, 2, 2, 3, 4, 3, 5}), Linq.asEnumerable(new int[]{1, 4, 4, 2, 2, 2}), new int[]{1, 2, 4}});
        lst.add(new Object[]{Linq.asEnumerable(new int[]{1, 1, 1, 1, 1, 1}), Linq.asEnumerable(new int[]{1, 1, 1, 1, 1}), new int[]{1}});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void Int() {
        for (Object[] objects : this.Int_TestData()) {
            this.Int((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (int[]) objects[2]);
        }
    }

    private void Int(IEnumerable<Integer> first, IEnumerable<Integer> second, int[] expected) {
        assertEquals(Linq.asEnumerable(expected), first.intersect(second));
        assertEquals(Linq.asEnumerable(expected), first.intersect(second, null));
    }

    private IEnumerable<Object[]> String_TestData() {
        List<Object[]> lst = new ArrayList<>();
        IEqualityComparer<String> defaultComparer = EqualityComparer.Default();
        lst.add(new Object[]{Linq.asEnumerable(new String[1]), Linq.asEnumerable(), defaultComparer, new String[0]});
        lst.add(new Object[]{Linq.asEnumerable(null, null, Empty), Linq.asEnumerable(new String[2]), defaultComparer, new String[]{null}});
        lst.add(new Object[]{Linq.asEnumerable(new String[2]), Linq.asEnumerable(), defaultComparer, new String[0]});

        lst.add(new Object[]{Linq.asEnumerable("Tim", "Bob", "Mike", "Robert"), Linq.asEnumerable("ekiM", "bBo"), null, new String[0]});
        lst.add(new Object[]{Linq.asEnumerable("Tim", "Bob", "Mike", "Robert"), Linq.asEnumerable("ekiM", "bBo"), new AnagramEqualityComparer(), new String[]{"Bob", "Mike"}});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void String() {
        for (Object[] objects : this.String_TestData()) {
            this.String((IEnumerable<String>) objects[0], (IEnumerable<String>) objects[1], (IEqualityComparer<String>) objects[2], (String[]) objects[3]);
        }
    }

    public void String(IEnumerable<String> first, IEnumerable<String> second, IEqualityComparer<String> comparer, String[] expected) {
        if (comparer == null) {
            assertEquals(Linq.asEnumerable(expected), first.intersect(second));
        }
        assertEquals(Linq.asEnumerable(expected), first.intersect(second, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(), Linq.asEnumerable(-5, 0, null, 1, 2, 9, 2), new Integer[0]});
        lst.add(new Object[]{Linq.asEnumerable(-5, 0, 1, 2, null, 9, 2), Linq.asEnumerable(), new Integer[0]});
        lst.add(new Object[]{Linq.asEnumerable(1, 2, null, 3, 4, 5, 6), Linq.asEnumerable(6, 7, 7, 7, null, 8, 1), new Integer[]{1, null, 6}});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableInt((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (Integer[]) objects[2]);
        }
    }

    private void NullableInt(IEnumerable<Integer> first, IEnumerable<Integer> second, Integer[] expected) {
        assertEquals(Linq.asEnumerable(expected), first.intersect(second));
        assertEquals(Linq.asEnumerable(expected), first.intersect(second, null));
    }

    @Test
    public void NullableIntRunOnce() {
        for (Object[] objects : this.NullableInt_TestData()) {
            this.NullableIntRunOnce((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1], (Integer[]) objects[2]);
        }
    }

    private void NullableIntRunOnce(IEnumerable<Integer> first, IEnumerable<Integer> second, Integer[] expected) {
        assertEquals(Linq.asEnumerable(expected), first.runOnce().intersect(second.runOnce()));
        assertEquals(Linq.asEnumerable(expected), first.runOnce().intersect(second.runOnce(), null));
    }

    @Test
    public void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.asEnumerable("ekiM", "bBo");

        assertThrows(NullPointerException.class, () -> first.intersect(second));
        assertThrows(NullPointerException.class, () -> first.intersect(second, new AnagramEqualityComparer()));
    }

    @Test
    public void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.asEnumerable("Tim", "Bob", "Mike", "Robert");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.intersect(second));
        assertThrows(ArgumentNullException.class, () -> first.intersect(second, new AnagramEqualityComparer()));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).intersect(Linq.range(0, 3));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        HashSet<String> set = new HashSet<>(StringComparer.OrdinalIgnoreCase);
        set.add("a");

        IEnumerable<String> input1 = Linq.asEnumerable(set);
        IEnumerable<String> input2 = Linq.asEnumerable(new String[]{"A"});

        assertEquals(Linq.empty(), input1.intersect(input2));
        assertEquals(Linq.empty(), input1.intersect(input2, null));
        assertEquals(Linq.empty(), input1.intersect(input2, EqualityComparer.Default()));
        assertEquals(Linq.asEnumerable(new String[]{"a"}), input1.intersect(input2, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.empty(), input2.intersect(input1));
        assertEquals(Linq.empty(), input2.intersect(input1, null));
        assertEquals(Linq.empty(), input2.intersect(input1, EqualityComparer.Default()));
        assertEquals(Linq.asEnumerable(new String[]{"A"}), input2.intersect(input1, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void testIntersect() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        assertEquals(1, Linq.asEnumerable(emps)
                .intersect(Linq.asEnumerable(emps2))
                .count());
    }

    @Test
    public void testIntersectWithComparer() {
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

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10)
        };
        assertEquals(1, Linq.asEnumerable(emps)
                .intersect(Linq.asEnumerable(emps2), comparer)
                .count());
    }
}
