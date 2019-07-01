package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SequenceEqualTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.of(new Integer[]{2, 3, null, 2, null, 4, 5}).select(x1 -> x1);
        IEnumerable<Integer> q2 = Linq.of(new Integer[]{1, 9, null, 4}).select(x2 -> x2);

        assertEquals(q1.sequenceEqual(q2), q1.sequenceEqual(q2));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice").select(x1 -> x1);
        IEnumerable<String> q2 = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS").select(x2 -> x2);

        assertEquals(q1.sequenceEqual(q2), q1.sequenceEqual(q2));
    }

    @Test
    public void BothEmpty() {
        int[] first = {};
        int[] second = {};

        assertTrue(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertTrue(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void MismatchInMiddle() {
        Integer[] first = {1, 2, 3, 4};
        Integer[] second = {1, 2, 6, 4};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void NullComparer() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second), null));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second), null));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second)), null));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second)), null));
    }

    @Test
    public void CustomComparer() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertTrue(Linq.of(first).sequenceEqual(Linq.of(second), new AnagramEqualityComparer()));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second), new AnagramEqualityComparer()));
        assertTrue(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second)), new AnagramEqualityComparer()));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second)), new AnagramEqualityComparer()));
    }

    @Test
    public void RunOnce() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertTrue(Linq.of(first).runOnce().sequenceEqual(Linq.of(second).runOnce(), new AnagramEqualityComparer()));
    }

    @Test
    public void BothSingleNullExplicitComparer() {
        String[] first = {null};
        String[] second = {null};

        assertTrue(Linq.of(first).sequenceEqual(Linq.of(second), StringComparer.Ordinal));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second), StringComparer.Ordinal));
        assertTrue(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second)), StringComparer.Ordinal));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second)), StringComparer.Ordinal));
    }

    @Test
    public void BothMatchIncludingNullElements() {
        Integer[] first = {-6, null, 0, -4, 9, 10, 20};
        Integer[] second = {-6, null, 0, -4, 9, 10, 20};

        assertTrue(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertTrue(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertTrue(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void EmptyWithNonEmpty() {
        Integer[] first = {};
        Integer[] second = {2, 3, 4};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void NonEmptyWithEmpty() {
        Integer[] first = {2, 3, 4};
        Integer[] second = {};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void MismatchingSingletons() {
        Integer[] first = {2};
        Integer[] second = {4};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void MismatchOnFirst() {
        Integer[] first = {1, 2, 3, 4, 5};
        Integer[] second = {2, 2, 3, 4, 5};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void MismatchOnLast() {
        Integer[] first = {1, 2, 3, 4, 4};
        Integer[] second = {1, 2, 3, 4, 5};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void SecondLargerThanFirst() {
        Integer[] first = {1, 2, 3, 4};
        Integer[] second = {1, 2, 3, 4, 4};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void FirstLargerThanSecond() {
        Integer[] first = {1, 2, 3, 4, 4};
        Integer[] second = {1, 2, 3, 4};

        assertFalse(Linq.of(first).sequenceEqual(Linq.of(second)));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(Linq.of(second)));
        assertFalse(Linq.of(first).sequenceEqual(FlipIsCollection(Linq.of(second))));
        assertFalse(FlipIsCollection(Linq.of(first)).sequenceEqual(FlipIsCollection(Linq.of(second))));
    }

    @Test
    public void FirstSourceNull() {
        IEnumerable<Integer> first = null;
        IEnumerable<Integer> second = Linq.empty();

        assertThrows(NullPointerException.class, () -> first.sequenceEqual(second));
    }

    @Test
    public void SecondSourceNull() {
        IEnumerable<Integer> first = Linq.empty();
        IEnumerable<Integer> second = null;

        assertThrows(ArgumentNullException.class, () -> first.sequenceEqual(second));
    }

    @Test
    public void testSequenceEqual() {
        List<Employee> list = Linq.of(emps).toList();

        assertTrue(Linq.of(list).sequenceEqual(Linq.of(emps)));
    }

    @Test
    public void testSequenceEqualWithComparer() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {11, 12, 13};

        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return x % 10 == y % 10;
            }

            @Override
            public int hashCode(Integer obj) {
                return obj % 10;
            }
        };

        assertTrue(Linq.of(array1).sequenceEqual(Linq.of(array2), comparer));
    }
}
