package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class DistinctTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345, 66, 66, -1, -1})
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.distinct(), q.distinct());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of(new String[]{"!@#$%^", "C", "AAA", "Calling Twice", "SoS"})
                .where(x -> IsNullOrEmpty(x));

        assertEquals(q.distinct(), q.distinct());
    }

    @Test
    void EmptySource() {
        int[] source = {};
        assertEmpty(Linq.of(source).distinct());
    }

    @Test
    void EmptySourceRunOnce() {
        int[] source = {};
        assertEmpty(Linq.of(source).runOnce().distinct());
    }

    @Test
    void SingleNullElementExplicitlyUseDefaultComparer() {
        String[] source = {null};
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(source).distinct(EqualityComparer.Default()));
    }

    @Test
    void EmptyStringDistinctFromNull() {
        String[] source = {null, null, Empty};
        String[] expected = {null, Empty};

        assertEquals(Linq.of(expected), Linq.of(source).distinct(EqualityComparer.Default()));
    }

    @Test
    void CollapsDuplicateNulls() {
        String[] source = {null, null};
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(source).distinct(EqualityComparer.Default()));
    }

    @Test
    void SourceAllDuplicates() {
        int[] source = {5, 5, 5, 5, 5, 5};
        int[] expected = {5};

        assertEquals(Linq.of(expected), Linq.of(source).distinct());
    }

    @Test
    void AllUnique() {
        int[] source = {2, -5, 0, 6, 10, 9};

        assertEquals(Linq.of(source), Linq.of(source).distinct());
    }

    @Test
    void SomeDuplicatesIncludingNulls() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).distinct());
    }

    @Test
    void SomeDuplicatesIncludingNullsRunOnce() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinct());
    }

    @Test
    void LastSameAsFirst() {
        int[] source = {1, 2, 3, 4, 5, 1};
        int[] expected = {1, 2, 3, 4, 5};

        assertEquals(Linq.of(expected), Linq.of(source).distinct());
    }

    // Multiple elements repeat non-consecutively
    @Test
    void RepeatsNonConsecutive() {
        int[] source = {1, 1, 2, 2, 4, 3, 1, 3, 2};
        int[] expected = {1, 2, 4, 3};

        assertEquals(Linq.of(expected), Linq.of(source).distinct());
    }

    @Test
    void RepeatsNonConsecutiveRunOnce() {
        int[] source = {1, 1, 2, 2, 4, 3, 1, 3, 2};
        int[] expected = {1, 2, 4, 3};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinct());
    }

    @Test
    void NullComparer() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};

        assertEquals(Linq.of(expected), Linq.of(source).distinct());
    }

    @Test
    void NullSource() {
        IEnumerable<String> source = null;

        assertThrows(NullPointerException.class, () -> source.distinct());
    }

    @Test
    void NullSourceCustomComparer() {
        IEnumerable<String> source = null;

        assertThrows(NullPointerException.class, () -> source.distinct(StringComparer.Ordinal));
    }

    @Test
    void CustomEqualityComparer() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "Robert"};

        assertEquals(Linq.of(expected), Linq.of(source).distinct(new AnagramEqualityComparer()), new AnagramEqualityComparer());
    }

    @Test
    void CustomEqualityComparerRunOnce() {
        String[] source = {"Bob", "Tim", "bBo", "miT", "Robert", "iTm"};
        String[] expected = {"Bob", "Tim", "Robert"};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().distinct(new AnagramEqualityComparer()), new AnagramEqualityComparer());
    }

    private IEnumerable<Object[]> SequencesWithDuplicates() {
        List<Object[]> lst = new ArrayList<>();
        // Validate an array of different numeric data types.
        lst.add(new Object[]{Linq.of(new int[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10})});
        lst.add(new Object[]{Linq.of(new long[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10})});
        lst.add(new Object[]{Linq.of(new float[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10})});
        lst.add(new Object[]{Linq.of(new double[]{1, 1, 1, 2, 3, 5, 5, 6, 6, 10})});
        lst.add(new Object[]{Linq.of(m(1), m(1), m(1), m(2), m(3), m(5), m(5), m(6), m(6), m(10))});
        // Try strings
        lst.add(new Object[]{Linq.of("add",
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
                "namespace")
        });
        return Linq.of(lst);
    }

    @Test
    void FindDistinctAndValidate() {
        for (Object[] objects : this.SequencesWithDuplicates()) {
            this.FindDistinctAndValidate((IEnumerable<?>) objects[0]);
        }
    }

    private <T> void FindDistinctAndValidate(IEnumerable<T> original) {
        // Convert to list to avoid repeated enumerations of the enumerables.
        List<T> originalList = original.toList();
        List<T> distinctList = Linq.of(originalList).distinct().toList();

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
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).distinct();
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void toArray() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(expected), Linq.of(source).distinct().toArray());
    }

    @Test
    void toList() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        Integer[] expected = {1, 2, null};

        assertEquals(Linq.of(Linq.of(expected).toList()), Linq.of(Linq.of(source).distinct().toList()));
    }

    @Test
    void Count() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};
        assertEquals(3, Linq.of(source).distinct().count());
    }

    @Test
    void RepeatEnumerating() {
        Integer[] source = {1, 1, 1, 2, 2, 2, null, null};

        IEnumerable<Integer> result = Linq.of(source).distinct();

        assertEquals(result, result);
    }

    @Test
    void testDistinct() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[0],
                emps[3],
        };
        assertEquals(3, Linq.of(emps2).distinct().count());
    }

    @Test
    void testDistinctWithEqualityComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno.hashCode();
            }
        };

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[1],
                emps[3]
        };
        assertEquals(2, Linq.of(emps2).distinct(comparer).count());
    }
}
