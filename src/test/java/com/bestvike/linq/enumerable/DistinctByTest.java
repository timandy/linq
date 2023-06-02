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
import com.bestvike.out;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class DistinctByTest extends TestCase {
    private static IEnumerable<Object[]> SequencesWithDuplicates() {
        ArgsList argsList = new ArgsList();
        // Validate an array of different numeric data types.
        argsList.add(Linq.of(new int[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10}));
        argsList.add(Linq.of(new long[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10}));
        argsList.add(Linq.of(new float[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10}));
        argsList.add(Linq.of(new double[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10}));
        argsList.add(Linq.of(m(1), m(1), m(1), m(2), m(3), m(5), m(5), m(6), m(6), m(10)));
        // Try strings
        argsList.add(Linq.of("add",
                "add",
                "subtract",
                "multiply",
                "divide",
                "divide2",
                "subtract",
                "add",
                "power",
                "exponent",
                "hello",
                "class",
                "namespace",
                "namespace",
                "namespace"));
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345, 66, 66, -1, -1})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.distinctBy(x -> x), q.distinctBy(x -> x));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "Calling Twice", "SoS"})
                .where(x -> IsNullOrEmpty(x));

        assertEquals(q.distinctBy(x -> x), q.distinctBy(x -> x));
    }

    @Test
    void EmptySource() {
        int[] source = {};
        assertEmpty(Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void EmptySourceRunOnce() {
        int[] source = {};
        assertEmpty(Linq.of(source).runOnce().distinctBy(x -> x));
    }

    @Test
    void SingleNullElementExplicitlyUseDefaultComparer() {
        String[] source = {null};
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x, EqualityComparer.Default()));
    }

    @Test
    void EmptyStringDistinctFromNull() {
        String[] source = {null, null, Empty};
        String[] expected = {null, Empty};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x, EqualityComparer.Default()));
    }

    @Test
    void CollapsDuplicateNulls() {
        String[] source = {null, null};
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x, EqualityComparer.Default()));
    }

    @Test
    void SourceAllDuplicates() {
        int[] source = {5, 5, 5, 5, 5, 5};
        int[] expected = {5};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void AllUnique() {
        int[] source = {2, -5, 0, 6, 10, 9};

        assertEquals(Linq.of(source), Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void SomeDuplicatesIncludingNulls() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void SomeDuplicatesIncludingNullsRunOnce() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinctBy(x -> x));
    }

    @Test
    void LastSameAsFirst() {
        int[] source = {1, 2, 3, 4, 5, 1};
        int[] expected = {1, 2, 3, 4, 5};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x));
    }

    // Multiple elements repeat non-consecutively
    @Test
    void RepeatsNonConsecutive() {
        int[] source = {1, 1, 2, 2, 4, 3, 1, 3, 2};
        int[] expected = {1, 2, 4, 3};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void RepeatsNonConsecutiveRunOnce() {
        int[] source = {1, 1, 2, 2, 4, 3, 1, 3, 2};
        int[] expected = {1, 2, 4, 3};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinctBy(x -> x));
    }

    @Test
    void NullComparer() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x));
    }

    @Test
    void NullSource() {
        IEnumerable<String> source = null;

        assertThrows(NullPointerException.class, () -> source.distinctBy(x -> x));
    }

    @Test
    void NullSourceCustomComparer() {
        IEnumerable<String> source = null;

        assertThrows(NullPointerException.class, () -> source.distinctBy(x -> x, StringComparer.Ordinal));
    }

    @Test
    void CustomEqualityComparer() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "Robert"};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x, new AnagramEqualityComparer()), new AnagramEqualityComparer());
    }

    @Test
    void CustomEqualityComparerRunOnce() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "Robert"};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinctBy(x -> x, new AnagramEqualityComparer()), new AnagramEqualityComparer());
    }

    @ParameterizedTest
    @MethodSource("SequencesWithDuplicates")
    <T> void FindDistinctAndValidate(IEnumerable<T> original) {
        // Convert to list to avoid repeated enumerations of the enumerables.
        List<T> originalList = original.toList();
        List<T> distinctList = Linq.of(originalList).distinctBy(x -> x).toList();

        // Ensure the result doesn't contain duplicates.
        HashSet<T> hashSet = new HashSet<>();
        for (T i : distinctList)
            assertTrue(hashSet.add(i));

        HashSet<T> originalSet = new HashSet<>(originalList);
        assertSuperset(originalSet, hashSet);
        assertSubset(originalSet, hashSet);
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).distinctBy(x -> x);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void toArray() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).distinctBy(x -> x).toArray());
        assertEquals(Linq.of(expected), Linq.of(Linq.of(source).distinctBy(x -> x).toArray(Integer.class)));
    }

    @Test
    void toList() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(Linq.of(expected).toList()), Linq.of(Linq.of(source).distinctBy(x -> x).toList()));
    }

    @Test
    void Count() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        assertEquals(3, Linq.of(source).distinctBy(x -> x).count());
    }

    @Test
    void RepeatEnumerating() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};

        IEnumerable<Integer> result = Linq.of(source).distinctBy(x -> x);

        assertEquals(result, result);
    }

    @Test
    void testDistinctBy() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[0],
                emps[3],
        };
        IEnumerable<Employee> enumerable = Linq.of(emps2).distinctBy(emp -> emp.deptno);
        DistinctByIterator<Employee, Integer> distinctIterator = (DistinctByIterator<Employee, Integer>) enumerable;
        assertEquals(1, distinctIterator.toArray(Employee.class).length);
        assertEquals(1, distinctIterator.toArray().size());
        assertEquals(1, distinctIterator.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(distinctIterator, out.init()));
        assertEquals(1, distinctIterator.count());
        assertEquals(1, enumerable.count());

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).distinctBy(x -> x));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2, 3).distinctBy(null));

        IEnumerable<Employee> source = Linq.of(emps2).distinctBy(x -> x);
        IEnumerator<Employee> e1 = source.enumerator();
        IEnumerator<Employee> e2 = source.enumerator();
        assertNotSame(e1, e2);
    }

    @Test
    void testDistinctByWithEqualityComparer() {
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

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[1],
                emps[3]
        };

        IEnumerable<Employee> enumerable = Linq.of(emps2).distinctBy(emp -> emp.empno, comparer);
        DistinctByIterator<Employee, Integer> distinctIterator = (DistinctByIterator<Employee, Integer>) enumerable;
        assertEquals(1, distinctIterator.toArray(Employee.class).length);
        assertEquals(1, distinctIterator.toArray().size());
        assertEquals(1, distinctIterator.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(distinctIterator, out.init()));
        assertEquals(1, distinctIterator.count());
        assertEquals(1, enumerable.count());
    }
}
