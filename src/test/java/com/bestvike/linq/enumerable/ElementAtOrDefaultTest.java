package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.TestCase;
import com.bestvike.function.Func0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-05-07.
 */
class ElementAtOrDefaultTest extends TestCase {
    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 1), 0, 1, 9);
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 10), 9, 1, 18);
        argsList.add(NumberRangeGuaranteedNotCollectionType(-4, 10), 3, 7, -1);

        argsList.add(Linq.of(new int[]{1, 2, 3, 4}), 4, 0, null);
        argsList.add(Linq.of(new int[0]), 0, 0, null);
        argsList.add(Linq.of(new int[]{-4}), 0, 1, -4);
        argsList.add(Linq.of(new int[]{9, 8, 0, -5, 10}), 4, 1, 10);

        argsList.add(NumberRangeGuaranteedNotCollectionType(-4, 5), -1, 6, null);
        argsList.add(NumberRangeGuaranteedNotCollectionType(5, 5), 5, 0, null);
        argsList.add(NumberRangeGuaranteedNotCollectionType(0, 0), 0, 0, null);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        List<IEnumerable<Integer>> q = Repeat(ii -> Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE), 3);

        assertEquals(q.get(0).elementAtOrDefault(3), q.get(0).elementAtOrDefault(3));
        assertEquals(q.get(1).elementAtOrDefault(Index.fromStart(3)), q.get(1).elementAtOrDefault(Index.fromStart(3)));
        assertEquals(q.get(2).elementAtOrDefault(Index.fromEnd(6)), q.get(2).elementAtOrDefault(Index.fromEnd(6)));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        List<IEnumerable<String>> q = Repeat(ii -> Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x)), 3);

        assertEquals(q.get(0).elementAtOrDefault(4), q.get(0).elementAtOrDefault(4));
        assertEquals(q.get(0).elementAtOrDefault(Index.fromStart(4)), q.get(0).elementAtOrDefault(Index.fromStart(4)));
        assertEquals(q.get(0).elementAtOrDefault(Index.fromEnd(2)), q.get(0).elementAtOrDefault(Index.fromEnd(2)));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAtOrDefault(IEnumerable<Integer> source, int index, int indexFromEnd, Integer expected) {
        assertEquals(expected, source.elementAtOrDefault(index));
        if (index >= 0)
            assertEquals(expected, source.elementAtOrDefault(Index.fromStart(index)));
        assertEquals(expected, source.elementAtOrDefault(Index.fromEnd(indexFromEnd)));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAtOrDefaultRunOnce(IEnumerable<Integer> source, int index, int indexFromEnd, Integer expected) {
        assertEquals(expected, source.runOnce().elementAtOrDefault(index));
        if (index >= 0)
            assertEquals(expected, source.runOnce().elementAtOrDefault(Index.fromStart(index)));
        assertEquals(expected, source.runOnce().elementAtOrDefault(Index.fromEnd(indexFromEnd)));
    }

    @Test
    void NullableArray_InvalidIndex_ReturnsNull() {
        Integer[] source = {9, 8};
        assertNull(Linq.of(source).elementAtOrDefault(-1));
        assertNull(Linq.of(source).elementAtOrDefault(3));
        assertNull(Linq.of(source).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(Linq.of(source).elementAtOrDefault(Integer.MIN_VALUE));

        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(3)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(3)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));
    }

    @Test
    void NullableArray_ValidIndex_ReturnsCorrectObject() {
        Integer[] source = {9, 8, null, -5, 10};

        assertNull(Linq.of(source).elementAtOrDefault(2));
        assertEquals(-5, Linq.of(source).elementAtOrDefault(3));

        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(2)));
        assertEquals(-5, Linq.of(source).elementAtOrDefault(Index.fromStart(3)));

        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(3)));
        assertEquals(-5, Linq.of(source).elementAtOrDefault(Index.fromEnd(2)));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAtOrDefault(2));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAtOrDefault(Index.fromStart(2)));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAtOrDefault(Index.fromEnd(2)));
    }

    @Test
    void MutableSource() {
        List<Integer> source = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(2, Linq.of(source).elementAtOrDefault(2));
        assertEquals(2, Linq.of(source).elementAtOrDefault(Index.fromStart(2)));
        assertEquals(2, Linq.of(source).elementAtOrDefault(Index.fromEnd(3)));

        source.addAll(3, Arrays.asList(-1, -2));
        source.remove(0);
        assertEquals(-1, Linq.of(source).elementAtOrDefault(2));
        assertEquals(-1, Linq.of(source).elementAtOrDefault(Index.fromStart(2)));
        assertEquals(-1, Linq.of(source).elementAtOrDefault(Index.fromEnd(4)));
    }

    @Test
    void MutableSourceNotList() {
        List<Integer> source = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        List<IEnumerable<Integer>> query1 = Repeat(ii -> ForceNotCollection(Linq.of(source)).select(i -> i), 3);
        assertEquals(2, query1.get(0).elementAtOrDefault(2));
        assertEquals(2, query1.get(1).elementAtOrDefault(Index.fromStart(2)));
        assertEquals(2, query1.get(2).elementAtOrDefault(Index.fromEnd(3)));

        List<IEnumerable<Integer>> query2 = Repeat(ii -> ForceNotCollection(Linq.of(source)).select(i -> i), 3);
        source.addAll(3, Arrays.asList(-1, -2));
        source.remove(0);
        assertEquals(-1, query2.get(0).elementAtOrDefault(2));
        assertEquals(-1, query2.get(1).elementAtOrDefault(Index.fromStart(2)));
        assertEquals(-1, query2.get(2).elementAtOrDefault(Index.fromEnd(4)));
    }

    @Test
    void EnumerateElements() {
        final int ElementCount = 10;
        ref<Integer> state = ref.init(-1);
        ref<Integer> moveNextCallCount = ref.init(0);
        Func0<DelegateIterator<Integer>> source = () ->
        {
            state.value = -1;
            moveNextCallCount.value = 0;
            return new DelegateIterator<>(
                    () -> {
                        moveNextCallCount.value++;
                        return ++state.value < ElementCount;
                    },
                    () -> state.value,
                    () -> state.value = -1);
        };

        assertEquals(0, source.apply().elementAtOrDefault(0));
        assertEquals(1, moveNextCallCount.value);
        assertEquals(0, source.apply().elementAtOrDefault(Index.fromStart(0)));
        assertEquals(1, moveNextCallCount.value);

        assertEquals(5, source.apply().elementAtOrDefault(5));
        assertEquals(6, moveNextCallCount.value);
        assertEquals(5, source.apply().elementAtOrDefault(Index.fromStart(5)));
        assertEquals(6, moveNextCallCount.value);

        assertEquals(0, source.apply().elementAtOrDefault(Index.fromEnd(ElementCount)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertEquals(5, source.apply().elementAtOrDefault(Index.fromEnd(5)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);

        assertNull(source.apply().elementAtOrDefault(ElementCount));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertNull(source.apply().elementAtOrDefault(Index.fromStart(ElementCount)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertNull(source.apply().elementAtOrDefault(Index.fromEnd(0)));
        assertEquals(0, moveNextCallCount.value);
    }

    @Test
    void NonEmptySource_Consistency() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, Linq.of(source).elementAtOrDefault(5));
        assertEquals(5, Linq.of(source).elementAtOrDefault(Index.fromStart(5)));
        assertEquals(5, Linq.of(source).elementAtOrDefault(Index.fromEnd(5)));

        assertEquals(0, Linq.of(source).elementAtOrDefault(0));
        assertEquals(0, Linq.of(source).elementAtOrDefault(Index.fromStart(0)));
        assertEquals(0, Linq.of(source).elementAtOrDefault(Index.fromEnd(10)));

        assertEquals(9, Linq.of(source).elementAtOrDefault(9));
        assertEquals(9, Linq.of(source).elementAtOrDefault(Index.fromStart(9)));
        assertEquals(9, Linq.of(source).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(Linq.of(source).elementAtOrDefault(-1));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(11)));

        assertNull(Linq.of(source).elementAtOrDefault(10));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(10)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(Linq.of(source).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(Linq.of(source).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_NotList() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAtOrDefault(5));
        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(5)));
        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(5)));

        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAtOrDefault(0));
        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(10)));

        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAtOrDefault(9));
        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(9)));
        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(11)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(10));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(10)));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        final int ElementCount = 10;
        ref<Integer> state = ref.init(-1);
        ref<Integer> moveNextCallCount = ref.init(0);
        Func0<DelegateIterator<Integer>> getSource = () ->
        {
            state.value = -1;
            moveNextCallCount.value = 0;
            return new DelegateIterator<>(
                    () -> {
                        moveNextCallCount.value++;
                        return ++state.value < ElementCount;
                    },
                    () -> state.value,
                    () -> state.value = -1);
        };

        assertNull(getSource.apply().elementAtOrDefault(10));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertNull(getSource.apply().elementAtOrDefault(Index.fromStart(10)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertNull(getSource.apply().elementAtOrDefault(Index.fromEnd(0)));
        assertEquals(0, moveNextCallCount.value);

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_ListPartition() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(5));
        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(5)));
        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(5)));

        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(0));
        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(10)));

        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(9));
        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(9)));
        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(11)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(10));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(10)));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_EnumerablePartition() {
        Integer[] source = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(5));
        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(5)));
        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(5)));

        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(0));
        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(10)));

        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(9));
        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(9)));
        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(11)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(10));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(10)));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency() {
        Integer[] source = new Integer[]{};

        assertNull(Linq.of(source).elementAtOrDefault(1));
        assertNull(Linq.of(source).elementAtOrDefault(-1));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(1)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(Linq.of(source).elementAtOrDefault(0));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(0)));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(Linq.of(source).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(Linq.of(source).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(Linq.of(source).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_NotList() {
        Integer[] source = new Integer[]{};

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(1));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(1)));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(0));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(ForceNotCollection(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_ListPartition() {
        Integer[] source = new Integer[]{};

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(1));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(1)));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(0));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(ListPartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_EnumerablePartition() {
        Integer[] source = new Integer[]{};

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(1));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(-1));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(1)));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(1)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(0));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(0)));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(0)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MIN_VALUE));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromEnd(Integer.MAX_VALUE)));

        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Integer.MAX_VALUE));
        assertNull(EnumerablePartitionOrEmpty(Linq.of(source)).elementAtOrDefault(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void testElementAtOrDefault() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("jimi", enumerable.elementAtOrDefault(0));
        assertNull(enumerable.elementAtOrDefault(2));
        assertNull(enumerable.elementAtOrDefault(-1));

        IEnumerable<Long> enumerable2 = Linq.of(new CountIterable(2));
        assertEquals(1L, enumerable2.elementAtOrDefault(0));
        assertNull(enumerable2.elementAtOrDefault(2));
        assertNull(enumerable2.elementAtOrDefault(-1));
    }
}
