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
class TakeLastTest extends TestCase {
    @ParameterizedTest
    @MethodSource("com.bestvike.TestCase$SkipTakeData#EnumerableData")
    void TakeLast(IEnumerable<Integer> source, int count) {
        assertAll(Linq.of(TestCase.<Integer>IdentityTransforms()), transform -> {
            IEnumerable<Integer> equivalent = transform.apply(source);

            IEnumerable<Integer> expected = equivalent.reverse().take(count).reverse();
            IEnumerable<Integer> actual = equivalent.takeLast(count);

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
        ref<Integer> index = ref.init(0);
        int limit = count * 2;

        IEnumerable<Integer> source = new DelegateIterator<>(
                () -> index.value++ != limit, // Stop once we go past the limit.
                () -> index.value, // Yield from 1 up to the limit, inclusive.
                () -> index.value ^= Integer.MIN_VALUE);

        IEnumerator<Integer> iterator = source.takeLast(count).enumerator();
        assertEquals(0, index.value); // Nothing should be done before MoveNext is called.

        for (int i = 1; i <= count; i++) {
            assertTrue(iterator.moveNext());
            assertEquals(count + i, iterator.current());

            // After the first MoveNext call to the enumerator, everything should be evaluated and the enumerator
            // should be disposed.
            assertEquals(Integer.MIN_VALUE, index.value & Integer.MIN_VALUE);
            assertEquals(limit + 1, index.value & Integer.MAX_VALUE);
        }

        assertFalse(iterator.moveNext());

        // Unlike SkipLast, TakeLast can tell straightaway that it can return a sequence with no elements if count <= 0.
        // The enumerable it returns is a specialized empty iterator that has no connections to the source. Hence,
        // after MoveNext returns false under those circumstances, it won't invoke Dispose on our enumerator.
        int expected = count <= 0 ? 0 : Integer.MIN_VALUE;
        assertEquals(expected, index.value & Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @MethodSource("com.bestvike.TestCase$SkipTakeData#EnumerableData")
    void RunOnce(IEnumerable<Integer> source, int count) {
        IEnumerable<Integer> expected = source.takeLast(count);
        assertEquals(expected, source.takeLast(count).runOnce());
    }

    @Test
    void List_ChangesAfterTakeLast_ChangesReflectedInResults() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IEnumerable<Integer> e = Linq.of(list).takeLast(3);
        list.remove(0);
        list.remove(0);
        assertEquals(Linq.of(3, 4, 5), Linq.of(e.toArray()));
    }

    @Test
    void List_Skip_ChangesAfterTakeLast_ChangesReflectedInResults() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IEnumerable<Integer> e = Linq.of(list).skip(1).takeLast(3);
        list.remove(0);
        assertEquals(Linq.of(3, 4, 5), Linq.of(e.toArray()));
    }

    @Test
    void testTakeLast() {
        assertEquals(1, Linq.of(depts).takeLast(1).count());
        assertEquals(3, Linq.of(depts).takeLast(5).count());
    }

    @Test
    void runOnce() {
        IEnumerable<Department> expected = Linq.of(depts).skip(2);
        assertEquals(expected, Linq.of(depts).takeLast(1).runOnce());
    }
}
