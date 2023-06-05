package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2023-06-02.
 */
class ChunkTest extends TestCase {
    private static IEnumerable<Object[]> ThrowsWhenSizeIsNonPositive_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(0);
        argsList.add(-1);
        return argsList;
    }

    private static <T> IEnumerable<T> ConvertToType(T[] array, Class<?> type) {
        if (type == TestReadOnlyCollection.class)
            return new TestReadOnlyCollection<>(array);
        if (type == TestCollection.class)
            return new TestCollection<>(array);
        if (type == TestEnumerable.class)
            return new TestEnumerable<>(array);
        throw new RuntimeException();
    }

    private static IEnumerable<Object[]> ChunkSourceRepeatCalls_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestReadOnlyCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestEnumerable.class);
        return argsList;
    }

    private static IEnumerable<Object[]> ChunkSourceEvenly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestReadOnlyCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}, TestEnumerable.class);
        return argsList;
    }

    private static IEnumerable<Object[]> ChunkSourceUnevenly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2}, TestReadOnlyCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2}, TestCollection.class);
        argsList.add(new Integer[]{9999, 0, 888, -1, 66, -777, 1, 2}, TestEnumerable.class);
        return argsList;
    }

    private static IEnumerable<Object[]> ChunkSourceSmallerThanMaxSize_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new Integer[]{9999, 0}, TestReadOnlyCollection.class);
        argsList.add(new Integer[]{9999, 0}, TestCollection.class);
        argsList.add(new Integer[]{9999, 0}, TestEnumerable.class);
        return argsList;
    }

    private static IEnumerable<Object[]> EmptySourceYieldsNoChunks_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new Integer[]{}, TestReadOnlyCollection.class);
        argsList.add(new Integer[]{}, TestCollection.class);
        argsList.add(new Integer[]{}, TestEnumerable.class);
        return argsList;
    }

    @Test
    void ThrowsOnNullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.chunk(5, Integer.class));
    }

    @ParameterizedTest
    @MethodSource("ThrowsWhenSizeIsNonPositive_TestData")
    void ThrowsWhenSizeIsNonPositive(int size) {
        IEnumerable<Integer> source = Linq.singleton(1);
        assertThrows(ArgumentOutOfRangeException.class, () -> source.chunk(size, Integer.class));
    }

    @Test
    void ChunkSourceLazily() {
        try (IEnumerator<Integer[]> chunks = new FastInfiniteEnumerator<Integer>().chunk(5, Integer.class).enumerator()) {
            chunks.moveNext();
            assertEquals(Linq.of(null, null, null, null, null), Linq.of(chunks.current()));
            assertTrue(chunks.moveNext());
        }
    }

    @ParameterizedTest
    @MethodSource("ChunkSourceRepeatCalls_TestData")
    void ChunkSourceRepeatCalls(Integer[] array, Class<?> type) {
        IEnumerable<Integer> source = ConvertToType(array, type);
        assertEquals(source.chunk(3, Integer.class), source.chunk(3, Integer.class));
    }

    @ParameterizedTest
    @MethodSource("ChunkSourceEvenly_TestData")
    void ChunkSourceEvenly(Integer[] array, Class<?> type) {
        IEnumerable<Integer> source = ConvertToType(array, type);

        try (IEnumerator<Integer[]> chunks = source.chunk(3, Integer.class).enumerator()) {
            chunks.moveNext();
            assertEquals(Linq.of(9999, 0, 888), Linq.of(chunks.current()));

            chunks.moveNext();
            assertEquals(Linq.of(-1, 66, -777), Linq.of(chunks.current()));

            chunks.moveNext();
            assertEquals(Linq.of(1, 2, -12345), Linq.of(chunks.current()));

            assertFalse(chunks.moveNext());
        }
    }

    @ParameterizedTest
    @MethodSource("ChunkSourceUnevenly_TestData")
    void ChunkSourceUnevenly(Integer[] array, Class<?> type) {
        IEnumerable<Integer> source = ConvertToType(array, type);

        try (IEnumerator<Integer[]> chunks = source.chunk(3, Integer.class).enumerator()) {
            chunks.moveNext();
            assertEquals(Linq.of(9999, 0, 888), Linq.of(chunks.current()));

            chunks.moveNext();
            assertEquals(Linq.of(-1, 66, -777), Linq.of(chunks.current()));

            chunks.moveNext();
            assertEquals(Linq.of(1, 2), Linq.of(chunks.current()));

            assertFalse(chunks.moveNext());
        }
    }

    @ParameterizedTest
    @MethodSource("ChunkSourceSmallerThanMaxSize_TestData")
    void ChunkSourceSmallerThanMaxSize(Integer[] array, Class<?> type) {
        IEnumerable<Integer> source = ConvertToType(array, type);
        try (IEnumerator<Integer[]> chunks = source.chunk(3, Integer.class).enumerator()) {
            chunks.moveNext();
            assertEquals(Linq.of(9999, 0), Linq.of(chunks.current()));

            assertFalse(chunks.moveNext());
        }
    }

    @ParameterizedTest
    @MethodSource("EmptySourceYieldsNoChunks_TestData")
    void EmptySourceYieldsNoChunks(Integer[] array, Class<?> type) {
        IEnumerable<Integer> source = ConvertToType(array, type);
        try (IEnumerator<Integer[]> chunks = source.chunk(3, Integer.class).enumerator()) {
            assertFalse(chunks.moveNext());
        }
    }

    @Test
    void RemovingFromSourceBeforeIterating() {
        List<Integer> list = new ArrayList<>(Arrays.asList(9999, 0, 888, -1, 66, -777, 1, 2, -12345));

        IEnumerable<Integer[]> chunks = Linq.of(list).chunk(3, Integer.class);
        list.remove((Object) 66);

        IEnumerable<IEnumerable<Integer>> expect = Linq.of(Linq.of(9999, 0, 888), Linq.of(-1, -777, 1), Linq.of(2, -12345));
        assertEquals(expect, chunks);
    }

    @Test
    void AddingToSourceBeforeIterating() {
        List<Integer> list = new ArrayList<>(Arrays.asList(9999, 0, 888, -1, 66, -777, 1, 2, -12345));

        IEnumerable<Integer[]> chunks = Linq.of(list).chunk(3, Integer.class);
        list.add(10);

        IEnumerable<IEnumerable<Integer>> expect = Linq.of(Linq.of(9999, 0, 888), Linq.of(-1, 66, -777), Linq.of(1, 2, -12345), Linq.singleton(10));
        assertEquals(expect, chunks);
    }
}
