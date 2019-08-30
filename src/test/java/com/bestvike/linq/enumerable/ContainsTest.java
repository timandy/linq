package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ContainsTest extends TestCase {
    private static IEnumerable<Object[]> Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), 6, false);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), 6, false);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), 8, true);
        argsList.add(Linq.of(new int[]{8, 10, 3, 0, -8}), -8, true);
        argsList.add(Linq.of(new int[]{8, 0, 10, 3, 0, -8, 0}), 0, true);

        argsList.add(NumberRangeGuaranteedNotCollectionType(0, 0), 0, false);
        argsList.add(NumberRangeGuaranteedNotCollectionType(4, 5), 3, false);
        argsList.add(NumberRangeGuaranteedNotCollectionType(3, 5), 3, true);
        argsList.add(NumberRangeGuaranteedNotCollectionType(3, 5), 7, true);
        argsList.add(RepeatedNumberGuaranteedNotCollectionType(10, 3), 10, true);
        return argsList;
    }

    private static IEnumerable<Object[]> String_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new String[]{null}), StringComparer.Ordinal, null, true);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), null, "trboeR", false);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), null, "Tim", true);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "trboeR", true);
        argsList.add(Linq.of("Bob", "Robert", "Tim"), new AnagramEqualityComparer(), "nevar", false);
        return argsList;
    }

    private static IEnumerable<Object[]> NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(8, 0, 10, 3, 0, -8, 0), null, false);
        argsList.add(Linq.of(8, 0, 10, null, 3, 0, -8, 0), null, true);

        argsList.add(NullableNumberRangeGuaranteedNotCollectionType(3, 4), null, false);
        argsList.add(RepeatedNullableNumberGuaranteedNotCollectionType(null, 5), null, true);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.contains(-1), q.contains(-1));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.contains("X"), q.contains("X"));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void Int(IEnumerable<Integer> source, int value, boolean expected) {
        assertEquals(expected, source.contains(value));
        assertEquals(expected, source.contains(value, null));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void IntRunOnce(IEnumerable<Integer> source, int value, boolean expected) {
        assertEquals(expected, source.runOnce().contains(value));
        assertEquals(expected, source.runOnce().contains(value, null));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void String(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, boolean expected) {
        if (comparer == null) {
            assertEquals(expected, source.contains(value));
        }
        assertEquals(expected, source.contains(value, comparer));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void StringRunOnce(IEnumerable<String> source, IEqualityComparer<String> comparer, String value, boolean expected) {
        if (comparer == null) {
            assertEquals(expected, source.runOnce().contains(value));
        }
        assertEquals(expected, source.runOnce().contains(value, comparer));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableInt(IEnumerable<Integer> source, Integer value, boolean expected) {
        assertEquals(expected, source.contains(value));
        assertEquals(expected, source.contains(value, null));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        IEnumerable<Integer> source = null;
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.contains(42));
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> source.contains(42, EqualityComparer.Default()));
    }

    @Test
    void ExplicitNullComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertFalse(source.contains("BAC", null));
    }

    @Test
    void ExplicitComparerDoesNotDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertTrue(source.contains("abc", StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void ExplicitComparerDoestNotDeferToCollectionWithComparer() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertTrue(source.contains("BAC", new AnagramEqualityComparer()));
    }

    @Test
    void NoComparerDoesDeferToCollection() {
        HashSet<String> set = new HashSet<>();
        set.add("ABC");
        IEnumerable<String> source = Linq.of(set);
        assertTrue(source.contains("ABC"));
    }

    @Test
    void testContains() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        assertEquals(e, employeeClone);
        assertTrue(Linq.of(emps).contains(e));
        assertTrue(Linq.of(emps).contains(employeeClone));
        assertFalse(Linq.of(emps).contains(employeeOther));

        assertTrue(Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).contains('h'));

        assertTrue(Linq.of(new LinkedList<>(Arrays.asList('h', 'e', 'l', 'l', 'o'))).contains('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        assertTrue(Linq.of(arrChar).contains('h'));

        assertTrue(Linq.chars("hello").contains('h'));

        assertTrue(Linq.singleton('h').contains('h'));
        assertFalse(Linq.singleton('h').contains('o'));

        assertFalse(Linq.empty().contains(1));

        assertTrue(Linq.of(new boolean[]{true, true}).contains(true));
        assertFalse(Linq.of(new boolean[]{true, true}).contains(false));

        assertTrue(Linq.of(new byte[]{1, 1}).contains((byte) 1));
        assertFalse(Linq.of(new byte[]{1, 1}).contains((byte) 2));

        assertTrue(Linq.of(new short[]{1, 1}).contains((short) 1));
        assertFalse(Linq.of(new short[]{1, 1}).contains((short) 2));

        assertTrue(Linq.of(new int[]{1, 1}).contains(1));
        assertFalse(Linq.of(new int[]{1, 1}).contains(2));

        assertTrue(Linq.of(new long[]{1L, 1L}).contains(1L));
        assertFalse(Linq.of(new long[]{1L, 1L}).contains(2L));

        assertTrue(Linq.of(new char[]{'a', 'a'}).contains('a'));
        assertFalse(Linq.of(new char[]{'a', 'a'}).contains('b'));

        assertTrue(Linq.of(new float[]{1f, 1f}).contains(1f));
        assertFalse(Linq.of(new float[]{1f, 1f}).contains(2f));

        assertTrue(Linq.of(new double[]{1d, 1d}).contains(1d));
        assertFalse(Linq.of(new double[]{1d, 1d}).contains(2d));

        assertTrue(Linq.chars("abc").contains('a'));
        assertFalse(Linq.chars("abc").contains('d'));
    }

    @Test
    void testContainsWithEqualityComparer() {
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
        assertTrue(Linq.of(emps).contains(e, comparer));
        assertTrue(Linq.of(emps).contains(employeeClone, comparer));
        assertFalse(Linq.of(emps).contains(employeeOther, comparer));
    }
}
