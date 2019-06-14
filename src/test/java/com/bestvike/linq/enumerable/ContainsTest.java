package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ContainsTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.contains(-1), q.contains(-1));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.contains("X"), q.contains("X"));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> results = new ArrayList<>();

        results.add(new Object[]{Linq.asEnumerable(new int[0]), 6, false});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 6, false});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 8, true});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), -8, true});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, true});

        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(0, 0), 0, false});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(4, 5), 3, false});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 3, true});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 7, true});
        results.add(new Object[]{RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, true});

        return Linq.asEnumerable(results);
    }

    @Test
    public void Int() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.Int((IEnumerable<Integer>) objects[0], (int) objects[1], (boolean) objects[2]);
    }

    private void Int(IEnumerable<Integer> source, int value, boolean expected) {
        assertEquals(expected, source.contains(value));
        assertEquals(expected, source.contains(value, null));
    }

    @Test
    public void IntRunOnce() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.IntRunOnce((IEnumerable<Integer>) objects[0], (int) objects[1], (boolean) objects[2]);
    }

    private void IntRunOnce(IEnumerable<Integer> source, int value, boolean expected) {
        assertEquals(expected, source.runOnce().contains(value));
        assertEquals(expected, source.runOnce().contains(value, null));
    }

    private IEnumerable<Object[]> String_TestData() {
        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(new String[]{null}), StringComparer.Ordinal, null, true},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), null, "trboeR", false},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), null, "Tim", true},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "trboeR", true},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "nevar", false}
        );
    }

    @Test
    public void String() {
        for (Object[] objects : this.String_TestData())
            //noinspection unchecked
            this.String((IEnumerable<String>) objects[0], (IEqualityComparer<String>) objects[1], (String) objects[2], (boolean) objects[3]);
    }

    public void String(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, boolean expected) {
        if (comparer == null) {
            assertEquals(expected, source.contains(value));
        }
        assertEquals(expected, source.contains(value, comparer));
    }

    @Test
    public void StringRunOnce() {
        for (Object[] objects : this.String_TestData())
            //noinspection unchecked
            this.StringRunOnce((IEnumerable<String>) objects[0], (IEqualityComparer<String>) objects[1], (String) objects[2], (boolean) objects[3]);
    }

    private void StringRunOnce(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, boolean expected) {
        if (comparer == null) {
            assertEquals(expected, source.runOnce().contains(value));
        }
        assertEquals(expected, source.runOnce().contains(value, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        return Linq.asEnumerable(

                new Object[]{Linq.asEnumerable(8, 0, 10, 3, 0, -8, 0), null, false},
                new Object[]{Linq.asEnumerable(8, 0, 10, null, 3, 0, -8, 0), null, true},

                new Object[]{NullableNumberRangeGuaranteedNotCollectionType(3, 4), null, false},
                new Object[]{RepeatedNullableNumberGuaranteedNotCollectionType(null, 5), null, true});
    }

    @Test
    public void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData())
            //noinspection unchecked
            this.NullableInt((IEnumerable<Integer>) objects[0], (Integer) objects[1], (boolean) objects[2]);
    }

    private void NullableInt(IEnumerable<Integer> source, Integer value, boolean expected) {
        assertEquals(expected, source.contains(value));
        assertEquals(expected, source.contains(value, null));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.contains(42));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.contains(42, EqualityComparer.Default()));
    }

    @Test
    public void ExplicitNullComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertFalse(source.contains("BAC", null));
    }

    @Test
    public void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertTrue(source.contains("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertTrue(source.contains("BAC", new AnagramEqualityComparer()));
    }

    @Test
    public void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertTrue(source.contains("ABC"));
    }

    @Test
    public void testContains() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertTrue(Linq.asEnumerable(emps).contains(e));
        assertTrue(Linq.asEnumerable(emps).contains(employeeClone));
        assertFalse(Linq.asEnumerable(emps).contains(employeeOther));

        assertTrue(Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).contains('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertTrue(Linq.asEnumerable(arrChar).contains('h'));

        assertTrue(Linq.asEnumerable("hello").contains('h'));

        assertTrue(Linq.singleton('h').contains('h'));
        assertFalse(Linq.singleton('h').contains('o'));

        assertFalse(Linq.empty().contains(1));
    }

    @Test
    public void testContainsWithEqualityComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return x != null && y != null
                        && x.empno == y.empno;
            }

            @Override
            public int hashCode(Employee obj) {
                return obj == null ? 0x789d : obj.hashCode();
            }
        };

        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertTrue(Linq.asEnumerable(emps).contains(e, comparer));
        assertTrue(Linq.asEnumerable(emps).contains(employeeClone, comparer));
        assertFalse(Linq.asEnumerable(emps).contains(employeeOther, comparer));
    }
}
