package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.RepeatInvokeException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by 许崇雷 on 2019-06-14.
 */
class LastIndexOfTest extends TestCase {
    private static IEnumerable<Object[]> Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), 6, -1);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), 6, -1);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), 8, 0);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), -8, 4);
        argsList.add(Linq.of(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, 6);

        argsList.add(NumberRangeGuaranteedNotCollectionType(0, 0), 0, -1);
        argsList.add(NumberRangeGuaranteedNotCollectionType(4, 5), 3, -1);
        argsList.add(NumberRangeGuaranteedNotCollectionType(3, 5), 3, 0);
        argsList.add(NumberRangeGuaranteedNotCollectionType(3, 5), 7, 4);
        argsList.add(RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, 2);
        return argsList;
    }

    private static IEnumerable<Object[]> String_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new String[]{null}), StringComparer.Ordinal, null, 0);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), null, "trboeR", -1);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), null, "Tim", 2);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "trboeR", 1);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "nevar", -1);
        return argsList;
    }

    private static IEnumerable<Object[]> NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(8, 0, 10, 3, 0, -8, 0), null, -1);
        argsList.add(Linq.of(8, 0, 10, null, 3, 0, -8, 0), null, 3);

        argsList.add(NullableNumberRangeGuaranteedNotCollectionType(3, 4), null, -1);
        argsList.add(RepeatedNullableNumberGuaranteedNotCollectionType(null, 5), null, 4);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.lastIndexOf(-1), q.lastIndexOf(-1));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.lastIndexOf("X"), q.lastIndexOf("X"));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void Int(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.lastIndexOf(value));
        assertEquals(expected, source.lastIndexOf(value, null));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void IntRunOnce(IEnumerable<Integer> source, int value, int expected) {
        assertEquals(expected, source.runOnce().lastIndexOf(value));
        assertEquals(expected, source.runOnce().lastIndexOf(value, null));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void String(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, int expected) {
        if (comparer == null) {
            assertEquals(expected, source.lastIndexOf(value));
        }
        assertEquals(expected, source.lastIndexOf(value, comparer));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void StringRunOnce(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, int expected) {
        if (comparer == null) {
            assertEquals(expected, source.runOnce().lastIndexOf(value));
        }
        assertEquals(expected, source.runOnce().lastIndexOf(value, comparer));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableInt(IEnumerable<Integer> source, Integer value, int expected) {
        assertEquals(expected, source.lastIndexOf(value));
        assertEquals(expected, source.lastIndexOf(value, null));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.lastIndexOf(42));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.lastIndexOf(42, EqualityComparer.Default()));
    }

    @Test
    void ExplicitNullComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(-1, source.lastIndexOf("BAC", null));
    }

    @Test
    void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.lastIndexOf("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.lastIndexOf("BAC", new AnagramEqualityComparer()));
    }

    @Test
    void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertEquals(0, source.lastIndexOf("ABC"));
    }

    @Test
    void testIList() {
        assertEquals(2, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf("b"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf("d"));
        assertEquals(-1, new Array<String>(new String[]{"a", "b", "b", "c"}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new boolean[]{true, false, false, true}).lastIndexOf(false));
        assertEquals(-1, Linq.of(new boolean[]{true, true, true, true}).lastIndexOf(false));
        assertEquals(-1, Linq.of(new boolean[]{true, false, false, true}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new byte[]{0, 1, 1, 2}).lastIndexOf((byte) 1));
        assertEquals(-1, Linq.of(new byte[]{0, 1, 1, 2}).lastIndexOf((byte) 3));
        assertEquals(-1, Linq.of(new byte[]{0, 1, 1, 2}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new short[]{0, 1, 1, 2}).lastIndexOf((short) 1));
        assertEquals(-1, Linq.of(new short[]{0, 1, 1, 2}).lastIndexOf((short) 3));
        assertEquals(-1, Linq.of(new short[]{0, 1, 1, 2}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new int[]{0, 1, 1, 2}).lastIndexOf(1));
        assertEquals(-1, Linq.of(new int[]{0, 1, 1, 2}).lastIndexOf(3));
        assertEquals(-1, Linq.of(new int[]{0, 1, 1, 2}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new long[]{0, 1, 1, 2}).lastIndexOf(1L));
        assertEquals(-1, Linq.of(new long[]{0, 1, 1, 2}).lastIndexOf(3L));
        assertEquals(-1, Linq.of(new long[]{0, 1, 1, 2}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf('b'));
        assertEquals(-1, Linq.of(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf('d'));
        assertEquals(-1, Linq.of(new char[]{'a', 'b', 'b', 'c'}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new float[]{0f, 1f, 1f, 2f}).lastIndexOf(1f));
        assertEquals(-1, Linq.of(new float[]{0f, 1f, 1f, 2f}).lastIndexOf(3f));
        assertEquals(-1, Linq.of(new float[]{0f, 1f, 1f, 2f}).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new double[]{0d, 1d, 1d, 2d}).lastIndexOf(1d));
        assertEquals(-1, Linq.of(new double[]{0d, 1d, 1d, 2d}).lastIndexOf(3d));
        assertEquals(-1, Linq.of(new double[]{0d, 1d, 1d, 2d}).lastIndexOf(null));
        //
        assertEquals(3, Linq.chars("hello").lastIndexOf('l'));
        assertEquals(-1, Linq.chars("hello").lastIndexOf('z'));
        assertEquals(-1, Linq.chars("hello").lastIndexOf(null));
        //
        assertEquals(2, Linq.of("hello", "world", "world", "bye").lastIndexOf("world"));
        assertEquals(-1, Linq.of("hello", "world", "world", "bye").lastIndexOf("thanks"));
        assertEquals(-1, Linq.of("hello", "world", "world", "bye").lastIndexOf(null));
        //
        assertEquals(2, Linq.of(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf("world"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf("thanks"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "world", "bye")).lastIndexOf(null));
        //
        assertEquals(2, Linq.of(new LinkedList<>(Arrays.asList("hello", "world", "world", "bye"))).lastIndexOf("world"));
        assertEquals(-1, Linq.of(new LinkedList<>(Arrays.asList("hello", "world", "world", "bye"))).lastIndexOf("thanks"));
        assertEquals(-1, Linq.of(new LinkedList<>(Arrays.asList("hello", "world", "world", "bye"))).lastIndexOf(null));
        //
        assertEquals(0, Linq.singleton("Tim").lastIndexOf("Tim"));
        assertEquals(-1, Linq.singleton("Tim").lastIndexOf("Jim"));
        assertEquals(-1, Linq.singleton("Tim").lastIndexOf(null));
        //
        assertEquals(0, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf(emps[0]));
        assertEquals(-1, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf(emps[1]));
        assertEquals(-1, Linq.of(emps).concat(Linq.of(badEmps)).toLookup(x -> x.deptno).get(10).lastIndexOf(null));
        //
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("hello", "world", "world", "bye")).runOnce();
        assertEquals(2, enumerable.lastIndexOf("world"));
        assertThrows(RepeatInvokeException.class, () -> enumerable.lastIndexOf("world"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "world", "bye")).runOnce().lastIndexOf("thanks"));
        assertEquals(-1, Linq.of(Arrays.asList("hello", "world", "world", "bye")).runOnce().lastIndexOf(null));
        //
        assertEquals(0, Linq.range(0, 30).skip(10).take(5).lastIndexOf(10));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).lastIndexOf(0));
        assertEquals(-1, Linq.range(0, 30).skip(10).take(5).lastIndexOf(null));
        //
        assertEquals(4, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf(0));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf(1));
        assertEquals(-1, Linq.repeat(0, 30).skip(10).take(5).lastIndexOf(null));
    }

    @Test
    void testLastIndexOf() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertEquals(1, Linq.of(emps).lastIndexOf(e));
        assertEquals(1, Linq.of(emps).lastIndexOf(employeeClone));
        assertEquals(-1, Linq.of(emps).lastIndexOf(employeeOther));

        assertEquals(0, Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).lastIndexOf('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(0, Linq.of(arrChar).lastIndexOf('h'));

        assertEquals(0, Linq.chars("hello").lastIndexOf('h'));

        assertEquals(0, Linq.singleton('h').lastIndexOf('h'));
        assertEquals(-1, Linq.singleton('h').lastIndexOf('o'));

        assertEquals(-1, Linq.empty().lastIndexOf(1));
    }

    @Test
    void testLastIndexOfWithEqualityComparer() {
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
        assertEquals(1, Linq.of(emps).lastIndexOf(e, comparer));
        assertEquals(1, Linq.of(emps).lastIndexOf(employeeClone, comparer));
        assertEquals(-1, Linq.of(emps).lastIndexOf(employeeOther, comparer));
    }
}
