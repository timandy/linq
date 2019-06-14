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
public class IndexOfTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.indexOf(-1), q.indexOf(-1));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.indexOf("X"), q.indexOf("X"));
    }

    private IEnumerable<Object[]> Int_TestData() {
        List<Object[]> results = new ArrayList<>();

        results.add(new Object[]{Linq.asEnumerable(new int[0]), 6, -1});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 6, -1});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), 8, 0});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 10, 3, 0, -8}), -8, 4});
        results.add(new Object[]{Linq.asEnumerable(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, 1});

        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(0, 0), 0, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(4, 5), 3, -1});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 3, 0});
        results.add(new Object[]{NumberRangeGuaranteedNotCollectionType(3, 5), 7, 4});
        results.add(new Object[]{RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, 0});

        return Linq.asEnumerable(results);
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
        return Linq.asEnumerable(
                new Object[]{Linq.asEnumerable(8, 0, 10, 3, 0, -8, 0), null, -1},
                new Object[]{Linq.asEnumerable(8, 0, 10, null, 3, 0, -8, 0), null, 3},

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
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(-1, source.indexOf("BAC", null));
    }

    @Test
    public void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.indexOf("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.indexOf("BAC", new AnagramEqualityComparer()));
    }

    @Test
    public void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.asEnumerable(set);
        assertEquals(0, source.indexOf("ABC"));
    }

    @Test
    public void SameResultsRepeatCallsIntQuery2() {
        IEnumerable<Integer> q = Linq.asEnumerable(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Func1<Integer, Boolean> predicate = TestCase::IsEven;
        assertEquals(q.indexOf(predicate), q.indexOf(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery2() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Func1<String, Boolean> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.indexOf(predicate), q.indexOf(predicate));
    }

    @Test
    public void IndexOf() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.IndexOf(Linq.empty(), isEvenFunc, -1);
        this.IndexOf(Linq.singleton(4), isEvenFunc, 0);
        this.IndexOf(Linq.singleton(5), isEvenFunc, -1);
        this.IndexOf(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.IndexOf(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.IndexOf(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.IndexOf(range, i -> i > k, j);
        }
    }

    private void IndexOf(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.indexOf(predicate));
    }

    @Test
    public void IndexOfRunOnce() {
        Func1<Integer, Boolean> isEvenFunc = TestCase::IsEven;
        this.IndexOfRunOnce(Linq.empty(), isEvenFunc, -1);
        this.IndexOfRunOnce(Linq.singleton(4), isEvenFunc, 0);
        this.IndexOfRunOnce(Linq.singleton(5), isEvenFunc, -1);
        this.IndexOfRunOnce(Linq.asEnumerable(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.IndexOfRunOnce(Linq.asEnumerable(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.IndexOfRunOnce(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.IndexOfRunOnce(range, i -> i > k, j);
        }
    }

    private void IndexOfRunOnce(IEnumerable<Integer> source, Func1<Integer, Boolean> predicate, int expected) {
        assertEquals(expected, source.runOnce().indexOf(predicate));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException2() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).indexOf(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).indexOf(predicate));
    }

    @Test
    public void testIList() {
        assertEquals(1, new Array<String>(new String[]{"a", "b", "c"}).indexOf("b"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "c"}).indexOf("d"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "c"}).indexOf((String) null));
        //
        assertEquals(1, Linq.asEnumerable(new boolean[]{true, false, true}).indexOf(false));
        assertEquals(-1, Linq.asEnumerable(new boolean[]{true, true, true}).indexOf(false));
        assertEquals(-1, Linq.asEnumerable(new boolean[]{true, false, true}).indexOf((Boolean) null));
        //
        assertEquals(1, Linq.asEnumerable(new byte[]{0, 1, 2}).indexOf((byte) 1));
        assertEquals(-1, Linq.asEnumerable(new byte[]{0, 1, 2}).indexOf((byte) 3));
        assertEquals(-1, Linq.asEnumerable(new byte[]{0, 1, 2}).indexOf((Byte) null));
        //
        assertEquals(1, Linq.asEnumerable(new short[]{0, 1, 2}).indexOf((short) 1));
        assertEquals(-1, Linq.asEnumerable(new short[]{0, 1, 2}).indexOf((short) 3));
        assertEquals(-1, Linq.asEnumerable(new short[]{0, 1, 2}).indexOf((Short) null));
        //
        assertEquals(1, Linq.asEnumerable(new int[]{0, 1, 2}).indexOf(1));
        assertEquals(-1, Linq.asEnumerable(new int[]{0, 1, 2}).indexOf(3));
        assertEquals(-1, Linq.asEnumerable(new int[]{0, 1, 2}).indexOf((Integer) null));
        //
        assertEquals(1, Linq.asEnumerable(new long[]{0, 1, 2}).indexOf(1L));
        assertEquals(-1, Linq.asEnumerable(new long[]{0, 1, 2}).indexOf(3L));
        assertEquals(-1, Linq.asEnumerable(new long[]{0, 1, 2}).indexOf((Long) null));
        //
        assertEquals(1, Linq.asEnumerable(new char[]{'a', 'b', 'c'}).indexOf('b'));
        assertEquals(-1, Linq.asEnumerable(new char[]{'a', 'b', 'c'}).indexOf('d'));
        assertEquals(-1, Linq.asEnumerable(new char[]{'a', 'b', 'c'}).indexOf((Character) null));
        //
        assertEquals(1, Linq.asEnumerable(new float[]{0f, 1f, 2f}).indexOf(1f));
        assertEquals(-1, Linq.asEnumerable(new float[]{0f, 1f, 2f}).indexOf(3f));
        assertEquals(-1, Linq.asEnumerable(new float[]{0f, 1f, 2f}).indexOf((Float) null));
        //
        assertEquals(1, Linq.asEnumerable(new double[]{0d, 1d, 2d}).indexOf(1d));
        assertEquals(-1, Linq.asEnumerable(new double[]{0d, 1d, 2d}).indexOf(3d));
        assertEquals(-1, Linq.asEnumerable(new double[]{0d, 1d, 2d}).indexOf((Double) null));
        //
        assertEquals(2, Linq.asEnumerable("hello").indexOf('l'));
        assertEquals(-1, Linq.asEnumerable("hello").indexOf('z'));
        assertEquals(-1, Linq.asEnumerable("hello").indexOf((Character) null));
        //
        assertEquals(1, Linq.asEnumerable("hello", "world", "bye").indexOf("world"));
        assertEquals(-1, Linq.asEnumerable("hello", "world", "bye").indexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable("hello", "world", "bye").indexOf((String) null));
        //
        assertEquals(1, Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).indexOf("world"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).indexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).indexOf((String) null));
        //
        assertEquals(0, Linq.singleton("Tim").indexOf("Tim"));
        assertEquals(-1, Linq.singleton("Tim").indexOf("Jim"));
        assertEquals(-1, Linq.singleton("Tim").indexOf((String) null));
        //
        assertEquals(0, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).indexOf(emps[0]));
        assertEquals(-1, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).indexOf(emps[1]));
        assertEquals(-1, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).toLookup(x -> x.deptno).get(10).indexOf((Employee) null));
        //
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).runOnce();
        assertEquals(1, enumerable.indexOf("world"));
        assertThrows(NotSupportedException.class, () -> enumerable.indexOf("world"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).runOnce().indexOf("thanks"));
        assertEquals(-1, Linq.asEnumerable(Arrays.asList("hello", "world", "bye")).runOnce().indexOf((String) null));
        //
        assertEquals(0, Linq.range(0, 30).skip(10).take(5).indexOf(10));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).indexOf(0));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).indexOf((Integer) null));
        //
        assertEquals(0, Linq.repeat(0, 30).skip(10).take(5).indexOf(0));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).indexOf(1));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).indexOf((Integer) null));
    }

    @Test
    public void testIndexOf() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertEquals(1, Linq.asEnumerable(emps).indexOf(e));
        assertEquals(1, Linq.asEnumerable(emps).indexOf(employeeClone));
        assertEquals(-1, Linq.asEnumerable(emps).indexOf(employeeOther));

        assertEquals(0, Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).indexOf('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, Linq.asEnumerable(arrChar).indexOf('h'));

        assertEquals(0, Linq.asEnumerable("hello").indexOf('h'));

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
        assertEquals(1, Linq.asEnumerable(emps).indexOf(e, comparer));
        assertEquals(1, Linq.asEnumerable(emps).indexOf(employeeClone, comparer));
        assertEquals(-1, Linq.asEnumerable(emps).indexOf(employeeOther, comparer));
    }

    @Test
    public void testIndexOfPredicate() {
        assertEquals(-1, Linq.asEnumerable(depts).indexOf(dept -> dept.name != null && dept.name.equals("IT")));
        assertEquals(0, Linq.asEnumerable(depts).indexOf(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
