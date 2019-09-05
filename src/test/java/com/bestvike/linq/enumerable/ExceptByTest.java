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
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ExceptByTest extends TestCase {
    private static IEnumerable<Object[]> Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[0]), Linq.of(new int[0]), null, Linq.of(new int[0]));
        argsList.add(Linq.of(new int[0]), Linq.of(new int[]{-6, -8, -6, 2, 0, 0, 5, 6}), null, Linq.of(new int[0]));
        argsList.add(Linq.of(new int[]{1, 1, 1, 1, 1}), Linq.of(new int[]{2, 3, 4}), null, Linq.of(new int[]{1}));
        return argsList;
    }

    private static IEnumerable<Object[]> String_TestData() {
        IEqualityComparer<String> defaultComparer = EqualityComparer.Default();

        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new String[1]), Linq.of(), defaultComparer, Linq.of(new String[1]));
        argsList.add(Linq.of(null, null, Empty), Linq.of(new String[1]), defaultComparer, Linq.of(Empty));
        argsList.add(Linq.of(new String[2]), Linq.of(), defaultComparer, Linq.of(new String[1]));
        argsList.add(Linq.of("Bob", "Tim", "Robert", "Chris"), Linq.of("bBo", "shriC"), null, Linq.of("Bob", "Tim", "Robert", "Chris"));
        argsList.add(Linq.of("Bob", "Tim", "Robert", "Chris"), Linq.of("bBo", "shriC"), new AnagramEqualityComparer(), Linq.of("Tim", "Robert"));
        return argsList;
    }

    private static IEnumerable<Object[]> NullableInt_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(-6, -8, -6, 2, 0, 0, 5, 6, null, null), Linq.of(), Linq.of(-6, -8, 2, 0, 5, 6, null));
        argsList.add(Linq.of(1, 2, 2, 3, 4, 5), Linq.of(5, 3, 2, 6, 6, 3, 1, null, null), Linq.of(4));
        argsList.add(Linq.of(2, 3, null, 2, null, 4, 5), Linq.of(1, 9, null, 4), Linq.of(2, 3, 5));
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.of(1, 9, null, 4);

        assertEquals(q1.exceptBy(q2, x -> x), q1.exceptBy(q2, x -> x));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> q2 = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        q1.exceptBy(q2, x -> x);
        q1.exceptBy(q2, x -> x);

        assertEquals(q1.exceptBy(q2, x -> x), q1.exceptBy(q2, x -> x));
    }

    @ParameterizedTest
    @MethodSource("Int_TestData")
    void Int(IEnumerable<Integer> first, IEnumerable<Integer> second, IEqualityComparer<Integer> comparer, IEnumerable<Integer> expected) {
        if (comparer == null) {
            assertEquals(expected, first.exceptBy(second, x -> x));
        }
        assertEquals(expected, first.exceptBy(second, x -> x, comparer));
    }

    @ParameterizedTest
    @MethodSource("String_TestData")
    void String(IEnumerable<String> first, IEnumerable<String> second, IEqualityComparer<String> comparer, IEnumerable<String> expected) {
        if (comparer == null) {
            assertEquals(expected, first.exceptBy(second, x -> x));
        }
        assertEquals(expected, first.exceptBy(second, x -> x, comparer));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableInt(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.exceptBy(second, x -> x));
    }

    @ParameterizedTest
    @MethodSource("NullableInt_TestData")
    void NullableIntRunOnce(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        assertEquals(expected, first.runOnce().exceptBy(second.runOnce(), x -> x));
    }

    @Test
    void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.of("bBo", "shriC");

        assertThrows(NullPointerException.class, () -> first.exceptBy(second, x -> x));
        assertThrows(NullPointerException.class, () -> first.exceptBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.exceptBy(second, x -> x));
        assertThrows(ArgumentNullException.class, () -> first.exceptBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).exceptBy(Linq.range(0, 3), x -> x);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        IEnumerable<String> input1 = Linq.of("a");
        IEnumerable<String> input2 = Linq.of("A");

        assertEquals(Linq.of("a"), input1.exceptBy(input2, x -> x));
        assertEquals(Linq.of("a"), input1.exceptBy(input2, x -> x, null));
        assertEquals(Linq.of("a"), input1.exceptBy(input2, x -> x, EqualityComparer.Default()));
        assertEquals(Linq.empty(), input1.exceptBy(input2, x -> x, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.of("A"), input2.exceptBy(input1, x -> x));
        assertEquals(Linq.of("A"), input2.exceptBy(input1, x -> x, null));
        assertEquals(Linq.of("A"), input2.exceptBy(input1, x -> x, EqualityComparer.Default()));
        assertEquals(Enumerable.empty(), input2.exceptBy(input1, x -> x, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void testExceptBy() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        assertEquals(1, Linq.of(emps)
                .exceptBy(Linq.of(emps2), emp -> emp.deptno)
                .count());

        IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        assertTrue(oneToHundred.exceptBy(oneToFifty, a -> a).sequenceEqual(fiftyOneToHundred));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).exceptBy(Linq.empty(), x -> x));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2, 3).exceptBy(null, x -> x));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2, 3).exceptBy(Linq.empty(), null));

        IEnumerable<Integer> source = Linq.of(1, 2, 3).exceptBy(Linq.singleton(1), x -> x);
        IEnumerator<Integer> e1 = source.enumerator();
        assertTrue(e1.moveNext());
        IEnumerator<Integer> e2 = source.enumerator();
        assertTrue(e2.moveNext());
        assertNotSame(e1, e2);
    }

    @Test
    void testExceptByWithComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };

        Employee[] emps2 = {new Employee(150, "Theodore", 10)};
        assertEquals(0, Linq.of(emps)
                .exceptBy(Linq.of(emps2), emp -> emp.deptno, comparer)
                .count());
    }
}
