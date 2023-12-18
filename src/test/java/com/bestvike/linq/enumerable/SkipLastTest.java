package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-17.
 */
class SkipLastTest extends TestCase {
    @ParameterizedTest
    @MethodSource("com.bestvike.TestCase$SkipTakeData#EnumerableData")
    void SkipLast(IEnumerable<Integer> source, int count) {
        assertAll(Linq.of(TestCase.<Integer>IdentityTransforms()), transform -> {
            IEnumerable<Integer> equivalent = transform.apply(source);

            IEnumerable<Integer> expected = equivalent.reverse().skip(count).reverse();
            IEnumerable<Integer> actual = equivalent.skipLast(count);

            assertEquals(expected, actual);
            assertEquals(expected.count(), actual.count());
            assertEquals(expected, actual.toArray());
            assertEquals(expected, Linq.of(actual.toArray(Integer.class)));
            assertEquals(expected, Linq.of(actual.toList()));

            assertEquals(expected.firstOrDefault(), actual.firstOrDefault());
            assertEquals(expected.lastOrDefault(), actual.lastOrDefault());

            assertAll(Linq.range(0, expected.count()), index -> {
                assertEquals(expected.elementAt(index), actual.elementAt(index));
            });

            assertEquals(null, actual.elementAtOrDefault(-1));
            assertEquals(null, actual.elementAtOrDefault(actual.count()));
        });
    }

    @ParameterizedTest
    @MethodSource("com.bestvike.TestCase$SkipTakeData#EvaluationBehaviorData")
    void EvaluationBehavior(int count) {
        // We want to make sure no more than `count` items are ever evaluated ahead of the current position.
        // As an example, if Enumerable.Range(1, 6).SkipLast(2) is called, then we should read in the first 3 items,
        // yield 1, read in 4, yield 2, and so on.
        ref<Integer> index = ref.init(0);
        int limit = Math.max(0, count * 2);

        IEnumerable<Integer> source = new DelegateIterator<>(
                () -> index.value++ != limit, // Stop once we go past the limit.
                () -> index.value, // Yield from 1 up to the limit, inclusive.
                () -> index.value ^= Integer.MIN_VALUE);

        IEnumerator<Integer> iterator = source.skipLast(count).enumerator();
        assertEquals(0, index.value); // Nothing should be done before MoveNext is called.

        for (int i = 1; i <= count; i++) {
            assertTrue(iterator.moveNext());
            assertEquals(i, iterator.current());
            assertEquals(count + i, index.value);
        }

        assertFalse(iterator.moveNext());
        assertEquals(Integer.MIN_VALUE, index.value & Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @MethodSource("com.bestvike.TestCase$SkipTakeData#EnumerableData")
    void RunOnce(IEnumerable<Integer> source, int count) {
        IEnumerable<Integer> expected = source.skipLast(count);
        assertEquals(expected, source.skipLast(count).runOnce());
    }

    @Test
    void List_ChangesAfterSkipLast_ChangesReflectedInResults() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IEnumerable<Integer> e = Linq.of(list).skipLast(2);
        list.remove(4);
        list.remove(3);
        assertEquals(Linq.of(1), Linq.of(e.toArray()));
    }

    @Test
    void List_Skip_ChangesAfterSkipLast_ChangesReflectedInResults() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IEnumerable<Integer> e = Linq.of(list).skip(1).skipLast(2);
        list.remove(4);
        assertEquals(Linq.of(2), Linq.of(e.toArray()));
    }

    @Test
    void testSkipLast() {
        assertEquals(2, Linq.of(depts).skipLast(1).count());
        assertEquals(0, Linq.of(depts).skipLast(5).count());
    }

    @Test
    void runOnce() {
        IEnumerable<Department> expected = Linq.of(depts).take(2);
        assertEquals(expected, Linq.of(depts).skipLast(1).runOnce());
    }
}
