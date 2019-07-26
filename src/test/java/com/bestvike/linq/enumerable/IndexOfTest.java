package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-14.
 */
public class IndexOfTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.indexOf(-1), q.indexOf(-1));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.indexOf("X"), q.indexOf("X"));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> results = new ArrayList<>();

        results.add(new Object[]{Linq.of(new int[0]), 6, -1});
        results.add(new Object[]{Linq.of(new int[]{8, 10, 3, 0, -8}), 6, -1});
        results.add(new Object[]{Linq.of(new int[]{8, 10, 3, 0, -8}), 8, 0});
        results.add(new Object[]{Linq.of(new int[]{8, 10, 3, 0, -8}), -8, 4});
        results.add(new Object[]{Linq.of(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, 1});

        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(0, 0), 0, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(4, 5), 3, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 3, 0});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 7, 4});
        results.add(new Object[]{RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, 0});

        return Linq.of(results);
    }

    @Test
    public void Int() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.Int((IEnumerable<Integer>) objects[0], (int) objects[1], (int) objects[2]);
    }

    private void Int(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.indexOf(value));
        assertEquals(expected, source.indexOf(value, null));
    }

    @Test
    public void IntRunOnce() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.IntRunOnce((IEnumerable<Integer>) objects[0], (int) objects[1], (int) objects[2]);
    }

    private void IntRunOnce(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.runOnce().indexOf(value));
        assertEquals(expected, source.runOnce().indexOf(value, null));
    }

    private IEnumerable<Object[]> String_TestData() {
        return Linq.of(
                new Object[]{Linq.of(new String[]{null}), StringComparer.Ordinal, null, 0},
                new Object[]{Linq.of("Bob", "Robert", "Tim"), null, "trboeR", -1},
                new Object[]{Linq.of("Bob", "Robert", "Tim"), null, "Tim", 2},
                new Object[]{Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "trboeR", 1},
                new Object[]{Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "nevar", -1}
        );
    }

    @Test
    public void String() {
        for (Object[] objects : this.String_TestData())
            //noinspection unchecked
            this.String((IEnumerable<String>) objects[0], (IEqualityComparer<String>) objects[1], (String) objects[2], (int) objects[3]);
    }

    public void String(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, int expected) {
        if (comparer == null) {
            assertEquals(expected, source.indexOf(value));
        }
        assertEquals(expected, source.indexOf(value, comparer));
    }

    @Test
    public void StringRunOnce() {
        for (Object[] objects : this.String_TestData())
            //noinspection unchecked
            this.StringRunOnce((IEnumerable<String>) objects[0], (IEqualityComparer<String>) objects[1], (String) objects[2], (int) objects[3]);
    }

    private void StringRunOnce(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, int expected) {
        if (comparer == null) {
            assertEquals(expected, source.runOnce().indexOf(value));
        }
        assertEquals(expected, source.runOnce().indexOf(value, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        return Linq.of(
                new Object[]{Linq.of(8, 0, 10, 3, 0, -8, 0), null, -1},
                new Object[]{Linq.of(8, 0, 10, null, 3, 0, -8, 0), null, 3},

                new Object[]{NullableNumberRangeGuaranteedNotCollectionType(3, 4), null, -1},
                new Object[]{RepeatedNullableNumberGuaranteedNotCollectionType(null, 5), null, 0});
    }

    @Test
    public void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData())
            //noinspection unchecked
            this.NullableInt((IEnumerable<Integer>) objects[0], (Integer) objects[1], (int) objects[2]);
    }

    private void NullableInt(IEnumerable<Integer> source, Integer value, int expected) {
        assertEquals(expected, source.indexOf(value));
        assertEquals(expected, source.indexOf(value, null));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.indexOf(42));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.indexOf(42, EqualityComparer.Default()));
    }

    @Test
    public void ExplicitNullComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(-1, source.indexOf("BAC", null));
    }

    @Test
    public void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.indexOf("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.indexOf("BAC", new AnagramEqualityComparer()));
    }

    @Test
    public void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.indexOf("ABC"));
    }

    @Test
    public void testIList() {
        assertEquals(1, new Array<String>(new String[]{"a", "b", "c"}).indexOf("b"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "c"}).indexOf("d"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "c"}).indexOf(null));
        //
        assertEquals(1, Linq.of(new boolean[]{true, false, true}).indexOf(false));
        assertEquals(-1, Linq.of(new boolean[]{true, true, true}).indexOf(false));
        assertEquals(-1, Linq.of(new boolean[]{true, false, true}).indexOf(null));
        //
        assertEquals(1, Linq.of(new byte[]{0, 1, 2}).indexOf((byte) 1));
        assertEquals(-1, Linq.of(new byte[]{0, 1, 2}).indexOf((byte) 3));
        assertEquals(-1, Linq.of(new byte[]{0, 1, 2}).indexOf(null));
        //
        assertEquals(1, Linq.of(new short[]{0, 1, 2}).indexOf((short) 1));
        assertEquals(-1, Linq.of(new short[]{0, 1, 2}).indexOf((short) 3));
        assertEquals(-1, Linq.of(new short[]{0, 1, 2}).indexOf(null));
        //
        assertEquals(1, Linq.of(new int[]{0, 1, 2}).indexOf(1));
        assertEquals(-1, Linq.of(new int[]{0, 1, 2}).indexOf(3));
        assertEquals(-1, Linq.of(new int[]{0, 1, 2}).indexOf(null));
        //
        assertEquals(1, Linq.of(new long[]{0, 1, 2}).indexOf(1L));
        assertEquals(-1, Linq.of(new long[]{0, 1, 2}).indexOf(3L));
        assertEquals(-1, Linq.of(new long[]{0, 1, 2}).indexOf(null));
        //
        assertEquals(1, Linq.of(new char[]{'a', 'b', 'c'}).indexOf('b'));
        assertEquals(-1, Linq.of(new char[]{'a', 'b', 'c'}).indexOf('d'));
        assertEquals(-1, Linq.of(new char[]{'a', 'b', 'c'}).indexOf(null));
        //
        assertEquals(1, Linq.of(new float[]{0f, 1f, 2f}).indexOf(1f));
        assertEquals(-1, Linq.of(new float[]{0f, 1f, 2f}).indexOf(3f));
        assertEquals(-1, Linq.of(new float[]{0f, 1f, 2f}).indexOf(null));
        //
        assertEquals(1, Linq.of(new double[]{0d, 1d, 2d}).indexOf(1d));
        assertEquals(-1, Linq.of(new double[]{0d, 1d, 2d}).indexOf(3d));
        assertEquals(-1, Linq.of(new double[]{0d, 1d, 2d}).indexOf(null));
        //
        assertEquals(2, Linq.chars("hello").indexOf('l'));
        assertEquals(-1, Linq.chars("hello").indexOf('z'));
        assertEquals(-1, Linq.chars("hello").indexOf(null));
        //
        assertEquals(1, Linq.of("hello", "world", "bye").indexOf("world"));
        assertEquals(-1, Linq.of("hello", "world", "bye").indexOf("thanks"));
        assertEquals(-1, Linq.of("hello", "world", "bye").indexOf(null));
        //
        assertEquals(1, Linq.of(Arrays.asList("hello", "world", "bye")).indexOf("world"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "bye")).indexOf("thanks"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "bye")).indexOf(null));
        //
        assertEquals(0, Linq.singleton("Tim").indexOf("Tim"));
        assertEquals(-1, Linq.singleton("Tim").indexOf("Jim"));
        assertEquals(-1, Linq.singleton("Tim").indexOf(null));
        //
        assertEquals(0, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).indexOf(emps[0]));
        assertEquals(-1, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).indexOf(emps[1]));
        assertEquals(-1, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).indexOf(null));
        //
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("hello", "world", "bye")).runOnce();
        assertEquals(1, enumerable.indexOf("world"));
        assertThrows(NotSupportedException.class, () -> enumerable.indexOf("world"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "bye")).runOnce().indexOf("thanks"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "bye")).runOnce().indexOf(null));
        //
        assertEquals(0, Linq.range(0, 30).skip(10).take(5).indexOf(10));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).indexOf(0));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).indexOf(null));
        //
        assertEquals(0, Linq.repeat(0, 30).skip(10).take(5).indexOf(0));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).indexOf(1));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).indexOf(null));
    }

    @Test
    public void testIndexOf() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertEquals(1, Linq.of(emps).indexOf(e));
        assertEquals(1, Linq.of(emps).indexOf(employeeClone));
        assertEquals(-1, Linq.of(emps).indexOf(employeeOther));

        assertEquals(0, Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).indexOf('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, Linq.of(arrChar).indexOf('h'));

        assertEquals(0, Linq.chars("hello").indexOf('h'));

        assertEquals(0, Linq.singleton('h').indexOf('h'));
        assertEquals(-1, Linq.singleton('h').indexOf('o'));

        assertEquals(-1, Linq.empty().indexOf(1));
    }

    @Test
    public void testIndexOfWithEqualityComparer() {
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
        assertEquals(1, Linq.of(emps).indexOf(e, comparer));
        assertEquals(1, Linq.of(emps).indexOf(employeeClone, comparer));
        assertEquals(-1, Linq.of(emps).indexOf(employeeOther, comparer));
    }
}
