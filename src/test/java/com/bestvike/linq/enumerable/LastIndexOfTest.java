package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-14.
 */
public class LastIndexOfTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.lastIndexOf(-1), q.lastIndexOf(-1));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.lastIndexOf("X"), q.lastIndexOf("X"));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> results = new ArrayList<>();

        results.add(new Object[]{Linq.asEnumerable(new int[0]), 6, -1});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 6, -1});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 8, 0});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), -8, 4});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, 6});

        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(0, 0), 0, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(4, 5), 3, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 3, 0});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 7, 4});
        results.add(new Object[]{RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, 2});

        return Linq.asEnumerable(results);
    }

    @Test
    public void Int() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.Int((IEnumerable<Integer>) objects[0], (int) objects[1], (int) objects[2]);
    }

    private void Int(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.lastIndexOf(value));
        assertEquals(expected, source.lastIndexOf(value, null));
    }

    @Test
    public void IntRunOnce() {
        for (Object[] objects : this.Int_TestData())
            //noinspection unchecked
            this.IntRunOnce((IEnumerable<Integer>) objects[0], (int) objects[1], (int) objects[2]);
    }

    private void IntRunOnce(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.runOnce().lastIndexOf(value));
        assertEquals(expected, source.runOnce().lastIndexOf(value, null));
    }

    private IEnumerable<Object[]> String_TestData() {
        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(new String[]{null}), StringComparer.Ordinal, null, 0},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), null, "trboeR", -1},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), null, "Tim", 2},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "trboeR", 1},
                new Object[]{Linq.asEnumerable("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "nevar", -1}
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
            assertEquals(expected, source.lastIndexOf(value));
        }
        assertEquals(expected, source.lastIndexOf(value, comparer));
    }

    @Test
    public void StringRunOnce() {
        for (Object[] objects : this.String_TestData())
            //noinspection unchecked
            this.StringRunOnce((IEnumerable<String>) objects[0], (IEqualityComparer<String>) objects[1], (String) objects[2], (int) objects[3]);
    }

    private void StringRunOnce(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, int expected) {
        if (comparer == null) {
            assertEquals(expected, source.runOnce().lastIndexOf(value));
        }
        assertEquals(expected, source.runOnce().lastIndexOf(value, comparer));
    }

    private IEnumerable<Object[]> NullableInt_TestData() {
        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(8, 0, 10, 3, 0, -8, 0), null, -1},
                new Object[]{Linq.asEnumerable(8, 0, 10, null, 3, 0, -8, 0), null, 3},

                new Object[]{NullableNumberRangeGuaranteedNotCollectionType(3, 4), null, -1},
                new Object[]{RepeatedNullableNumberGuaranteedNotCollectionType(null, 5), null, 4});
    }

    @Test
    public void NullableInt() {
        for (Object[] objects : this.NullableInt_TestData())
            //noinspection unchecked
            this.NullableInt((IEnumerable<Integer>) objects[0], (Integer) objects[1], (int) objects[2]);
    }

    private void NullableInt(IEnumerable<Integer> source, Integer value, int expected) {
        assertEquals(expected, source.lastIndexOf(value));
        assertEquals(expected, source.lastIndexOf(value, null));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.lastIndexOf(42));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.lastIndexOf(42, EqualityComparer.Default()));
    }

    @Test
    public void ExplicitNullComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(-1, source.lastIndexOf("BAC", null));
    }

    @Test
    public void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.lastIndexOf("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.lastIndexOf("BAC", new AnagramEqualityComparer()));
    }

    @Test
    public void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.lastIndexOf("ABC"));
    }

    @Test
    public void SameResultsRepeatCallsIntQuery2() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        assertEquals(q.lastIndexOf(predicate), q.lastIndexOf(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery2() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Func1<String, Boolean> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.lastIndexOf(predicate), q.lastIndexOf(predicate));
    }

    @Test
    public void LastIndexOf() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.LastIndexOf(Linq.empty(), isEvenFunc, -1);
        this.LastIndexOf(Linq.singleton(4), isEvenFunc, 0);
        this.LastIndexOf(Linq.singleton(5), isEvenFunc, -1);
        this.LastIndexOf(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.LastIndexOf(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.LastIndexOf(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.LastIndexOf(range, i -> i > k, 9);
        }
    }

    private void LastIndexOf(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.lastIndexOf(predicate));
    }

    @Test
    public void LastIndexOfRunOnce() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.LastIndexOfRunOnce(Linq.empty(), isEvenFunc, -1);
        this.LastIndexOfRunOnce(Linq.singleton(4), isEvenFunc, 0);
        this.LastIndexOfRunOnce(Linq.singleton(5), isEvenFunc, -1);
        this.LastIndexOfRunOnce(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.LastIndexOfRunOnce(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.LastIndexOfRunOnce(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.LastIndexOfRunOnce(range, i -> i > k, 9);
        }
    }

    private void LastIndexOfRunOnce(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.runOnce().lastIndexOf(predicate));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException2() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).lastIndexOf(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).lastIndexOf(predicate));
    }

    @Test
    public void testIList() {
        assertEquals(2, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf("b"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf("d"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf((String) null));
        //
        assertEquals(2, Linq.asEnumerable(new boolean[]{true, false, false, true}).lastIndexOf(false));
        assertEquals(-1, Linq.asEnumerable(new boolean[]{true, true, true, true}).lastIndexOf(false));
        assertEquals(-1, Linq.asEnumerable(new boolean[]{true, false, false, true}).lastIndexOf((Boolean) null));
        //
        assertEquals(2, Linq.asEnumerable(new byte[]{0, 1, 1, 2}).lastIndexOf((byte) 1));
        assertEquals(-1, Linq.asEnumerable(new byte[]{0, 1, 1, 2}).lastIndexOf((byte) 3));
        assertEquals(-1, Linq.asEnumerable(new byte[]{0, 1, 1, 2}).lastIndexOf((Byte) null));
        //
        assertEquals(2, Linq.asEnumerable(new short[]{0, 1, 1, 2}).lastIndexOf((short) 1));
        assertEquals(-1, Linq.asEnumerable(new short[]{0, 1, 1, 2}).lastIndexOf((short) 3));
        assertEquals(-1, Linq.asEnumerable(new short[]{0, 1, 1, 2}).lastIndexOf((Short) null));
        //
        assertEquals(2, Linq.asEnumerable(new int[]{0, 1, 1, 2}).lastIndexOf(1));
        assertEquals(-1, Linq.asEnumerable(new int[]{0, 1, 1, 2}).lastIndexOf(3));
        assertEquals(-1, Linq.asEnumerable(new int[]{0, 1, 1, 2}).lastIndexOf((Integer) null));
        //
        assertEquals(2, Linq.asEnumerable(new long[]{0, 1, 1, 2}).lastIndexOf(1L));
        assertEquals(-1, Linq.asEnumerable(new long[]{0, 1, 1, 2}).lastIndexOf(3L));
        assertEquals(-1, Linq.asEnumerable(new long[]{0, 1, 1, 2}).lastIndexOf((Long) null));
        //
        assertEquals(2, Linq.asEnumerable(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf('b'));
        assertEquals(-1, Linq.asEnumerable(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf('d'));
        assertEquals(-1, Linq.asEnumerable(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf((Character) null));
        //
        assertEquals(2, Linq.asEnumerable(new float[]{0f, 1f, 1f, 2f}).lastIndexOf(1f));
        assertEquals(-1, Linq.asEnumerable(new float[]{0f, 1f, 1f, 2f}).lastIndexOf(3f));
        assertEquals(-1, Linq.asEnumerable(new float[]{0f, 1f, 1f, 2f}).lastIndexOf((Float) null));
        //
        assertEquals(2, Linq.asEnumerable(new double[]{0d, 1d, 1d, 2d}).lastIndexOf(1d));
        assertEquals(-1, Linq.asEnumerable(new double[]{0d, 1d, 1d, 2d}).lastIndexOf(3d));
        assertEquals(-1, Linq.asEnumerable(new double[]{0d, 1d, 1d, 2d}).lastIndexOf((Double) null));
        //
        assertEquals(3, Linq.asEnumerable("hello").lastIndexOf('l'));
        assertEquals(-1, Linq.asEnumerable("hello").lastIndexOf('z'));
        assertEquals(-1, Linq.asEnumerable("hello").lastIndexOf((Character) null));
        //
        assertEquals(2, Linq.asEnumerable("hello", "world", "world", "bye").lastIndexOf("world"));
        assertEquals(-1, Linq.asEnumerable("hello", "world", "world", "bye").lastIndexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable("hello", "world", "world", "bye").lastIndexOf((String) null));
        //
        assertEquals(2, Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf("world"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf((String) null));
        //
        assertEquals(0, Linq.singleton("Tim").lastIndexOf("Tim"));
        assertEquals(-1, Linq.singleton("Tim").lastIndexOf("Jim"));
        assertEquals(-1, Linq.singleton("Tim").lastIndexOf((String) null));
        //
        assertEquals(0, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf(emps[0]));
        assertEquals(-1, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf(emps[1]));
        assertEquals(-1, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf((Employee) null));
        //
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).runOnce();
        assertEquals(2, enumerable.lastIndexOf("world"));
        assertThrows(NotSupportedException.class, () -> enumerable.lastIndexOf("world"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).runOnce().lastIndexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "world", "bye")).runOnce().lastIndexOf((String) null));
        //
        assertEquals(0, Linq.range(0, 30).skip(10).take(5).lastIndexOf(10));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).lastIndexOf(0));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).lastIndexOf((Integer) null));
        //
        assertEquals(4, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf(0));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf(1));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf((Integer) null));
    }

    @Test
    public void testLastIndexOf() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertEquals(1, Linq.asEnumerable(emps).lastIndexOf(e));
        assertEquals(1, Linq.asEnumerable(emps).lastIndexOf(employeeClone));
        assertEquals(-1, Linq.asEnumerable(emps).lastIndexOf(employeeOther));

        assertEquals(0, Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).lastIndexOf('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, Linq.asEnumerable(arrChar).lastIndexOf('h'));

        assertEquals(0, Linq.asEnumerable("hello").lastIndexOf('h'));

        assertEquals(0, Linq.singleton('h').lastIndexOf('h'));
        assertEquals(-1, Linq.singleton('h').lastIndexOf('o'));

        assertEquals(-1, Linq.empty().lastIndexOf(1));
    }

    @Test
    public void testLastIndexOfWithEqualityComparer() {
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
        assertEquals(1, Linq.asEnumerable(emps).lastIndexOf(e, comparer));
        assertEquals(1, Linq.asEnumerable(emps).lastIndexOf(employeeClone, comparer));
        assertEquals(-1, Linq.asEnumerable(emps).lastIndexOf(employeeOther, comparer));
    }

    @Test
    public void testLastIndexOfPredicate() {
        assertEquals(-1, Linq.asEnumerable(depts).lastIndexOf(dept -> dept.name != null && dept.name.equals("IT")));
        assertEquals(0, Linq.asEnumerable(depts).lastIndexOf(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
