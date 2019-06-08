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
        IEnumerable<Integer> q1 = Linq.asEnumerable(new Integer[]{2, 3, null, 2, null, 4, 5}).select(x1 -> x1);
        IEnumerable<Integer> q2 = Linq.asEnumerable(new Integer[]{1, 9, null, 4}).select(x2 -> x2);

        assertEquals(q1.sequenceEqual(q2), q1.sequenceEqual(q2));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice").select(x1 -> x1);
        IEnumerable<String> q2 = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS").select(x2 -> x2);

        assertEquals(q1.sequenceEqual(q2), q1.sequenceEqual(q2));
    }

    @Test
    public void BothEmpty() {
        int[] first = {};
        int[] second = {};

        assertTrue(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertTrue(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void MismatchInMiddle() {
        Integer[] first = {1, 2, 3, 4};
        Integer[] second = {1, 2, 6, 4};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void NullComparer() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second), null));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second), null));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), null));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), null));
    }

    @Test
    public void CustomComparer() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertTrue(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second), new AnagramEqualityComparer()));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second), new AnagramEqualityComparer()));
        assertTrue(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), new AnagramEqualityComparer()));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), new AnagramEqualityComparer()));
    }

    @Test
    public void RunOnce() {
        String[] first = {"Bob", "Tim", "Chris"};
        String[] second = {"Bbo", "mTi", "rishC"};

        assertTrue(Linq.asEnumerable(first).runOnce().sequenceEqual(Linq.asEnumerable(second).runOnce(), new AnagramEqualityComparer()));
    }

    @Test
    public void BothSingleNullExplicitComparer() {
        String[] first = {null};
        String[] second = {null};

        assertTrue(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second), StringComparer.Ordinal));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second), StringComparer.Ordinal));
        assertTrue(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), StringComparer.Ordinal));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second)), StringComparer.Ordinal));
    }

    @Test
    public void BothMatchIncludingNullElements() {
        Integer[] first = {-6, null, 0, -4, 9, 10, 20};
        Integer[] second = {-6, null, 0, -4, 9, 10, 20};

        assertTrue(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertTrue(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertTrue(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void EmptyWithNonEmpty() {
        Integer[] first = {};
        Integer[] second = {2, 3, 4};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void NonEmptyWithEmpty() {
        Integer[] first = {2, 3, 4};
        Integer[] second = {};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void MismatchingSingletons() {
        Integer[] first = {2};
        Integer[] second = {4};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void MismatchOnFirst() {
        Integer[] first = {1, 2, 3, 4, 5};
        Integer[] second = {2, 2, 3, 4, 5};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void MismatchOnLast() {
        Integer[] first = {1, 2, 3, 4, 4};
        Integer[] second = {1, 2, 3, 4, 5};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void SecondLargerThanFirst() {
        Integer[] first = {1, 2, 3, 4};
        Integer[] second = {1, 2, 3, 4, 4};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
    }

    @Test
    public void FirstLargerThanSecond() {
        Integer[] first = {1, 2, 3, 4, 4};
        Integer[] second = {1, 2, 3, 4};

        assertFalse(Linq.asEnumerable(first).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(Linq.asEnumerable(second)));
        assertFalse(Linq.asEnumerable(first).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
        assertFalse(FlipIsCollection(Linq.asEnumerable(first)).sequenceEqual(FlipIsCollection(Linq.asEnumerable(second))));
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
        List<Employee> list = Linq.asEnumerable(emps).toList();

        assertTrue(Linq.asEnumerable(list).sequenceEqual(Linq.asEnumerable(emps)));
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

        assertTrue(Linq.asEnumerable(array1).sequenceEqual(Linq.asEnumerable(array2), comparer));
    }
}
