package com.bestvike.linq.enumerable;

import com.bestvike.Index;
import com.bestvike.TestCase;
import com.bestvike.function.Func0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.ref;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ElementAtTest extends TestCase {
    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 1), 0, 1, 9);
        argsList.add(NumberRangeGuaranteedNotCollectionType(9, 10), 9, 1, 18);
        argsList.add(NumberRangeGuaranteedNotCollectionType(-4, 10), 3, 7, -1);

        argsList.add(Linq.of(new int[]{-4}), 0, 1, -4);
        argsList.add(Linq.of(new int[]{9, 8, 0, -5, 10}), 4, 1, 10);
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        List<IEnumerable<Integer>> q = Repeat(ii -> Linq.of(new int[]{0, 9999, 0, 888, -1, 66, -1, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE), 3);

        assertEquals(q.get(0).elementAt(3), q.get(1).elementAt(3));
        assertEquals(q.get(1).elementAt(Index.fromStart(3)), q.get(1).elementAt(Index.fromStart(3)));
        assertEquals(q.get(2).elementAt(Index.fromEnd(6)), q.get(2).elementAt(Index.fromEnd(6)));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        List<IEnumerable<String>> q = Repeat(ii -> Linq.of(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty})
                .where(x -> !IsNullOrEmpty(x)), 3);

        assertEquals(q.get(0).elementAt(4), q.get(1).elementAt(4));
        assertEquals(q.get(1).elementAt(Index.fromStart(4)), q.get(1).elementAt(Index.fromStart(4)));
        assertEquals(q.get(2).elementAt(Index.fromEnd(2)), q.get(2).elementAt(Index.fromEnd(2)));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAt(IEnumerable<Integer> source, int index, int indexFromEnd, Integer expected) {
        assertEquals(expected, source.elementAt(index));
        assertEquals(expected, source.elementAt(Index.fromStart(index)));
        assertEquals(expected, source.elementAt(Index.fromEnd(indexFromEnd)));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    void ElementAtRunOnce(IEnumerable<Integer> source, int index, int indexFromEnd, Integer expected) {
        assertEquals(expected, source.runOnce().elementAt(index));
        assertEquals(expected, source.runOnce().elementAt(Index.fromStart(index)));
        assertEquals(expected, source.runOnce().elementAt(Index.fromEnd(indexFromEnd)));
    }

    @Test
    void InvalidIndex_ThrowsArgumentOutOfRangeException() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(Index.fromEnd(3)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(Integer.MAX_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(Integer.MIN_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(Index.fromStart(Integer.MAX_VALUE)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new Integer[]{9, 8}).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[]{1, 2, 3, 4}).elementAt(4));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[]{1, 2, 3, 4}).elementAt(Index.fromStart(4)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[]{1, 2, 3, 4}).elementAt(Index.fromEnd(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[]{1, 2, 3, 4}).elementAt(Index.fromEnd(5)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[0]).elementAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[0]).elementAt(Index.fromStart(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[0]).elementAt(Index.fromEnd(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(new int[0]).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(Index.fromEnd(6)));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(Index.fromStart(Integer.MAX_VALUE)));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(-4, 5).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(5, 5).elementAt(5));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(5, 5).elementAt(Index.fromStart(5)));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(5, 5).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(0, 0).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(0, 0).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(0, 0).elementAt(Index.fromEnd(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> NumberRangeGuaranteedNotCollectionType(0, 0).elementAt(Index.fromEnd(1)));
    }

    @Test
    void NullableArray_ValidIndex_ReturnsCorrectObject() {
        Integer[] source = {9, 8, null, -5, 10};

        assertNull(Linq.of(source).elementAt(2));
        assertEquals(-5, Linq.of(source).elementAt(3));

        assertNull(Linq.of(source).elementAt(Index.fromStart(2)));
        assertEquals(-5, Linq.of(source).elementAt(Index.fromStart(3)));

        assertNull(Linq.of(source).elementAt(Index.fromEnd(3)));
        assertEquals(-5, Linq.of(source).elementAt(Index.fromEnd(2)));
    }

    @Test
    void NullSource_ThrowsArgumentNullException() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAt(2));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAt(Index.fromStart(2)));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).elementAt(Index.fromEnd(2)));
    }

    @Test
    void MutableSource() {
        List<Integer> source = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        assertEquals(2, Linq.of(source).elementAt(2));
        assertEquals(2, Linq.of(source).elementAt(Index.fromStart(2)));
        assertEquals(2, Linq.of(source).elementAt(Index.fromEnd(3)));

        source.addAll(3, Arrays.asList(-1, -2));
        source.remove(0);
        assertEquals(-1, Linq.of(source).elementAt(2));
        assertEquals(-1, Linq.of(source).elementAt(Index.fromStart(2)));
        assertEquals(-1, Linq.of(source).elementAt(Index.fromEnd(4)));
    }

    @Test
    void MutableSourceNotList() {
        List<Integer> source = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        List<IEnumerable<Integer>> query1 = Repeat(ii -> ForceNotCollection(Linq.of(source)).select(i -> i), 3);
        assertEquals(2, query1.get(0).elementAt(2));
        assertEquals(2, query1.get(1).elementAt(Index.fromStart(2)));
        assertEquals(2, query1.get(2).elementAt(Index.fromEnd(3)));

        List<IEnumerable<Integer>> query2 = Repeat(ii -> ForceNotCollection(Linq.of(source)).select(i -> i), 3);
        source.addAll(3, Arrays.asList(-1, -2));
        source.remove(0);
        assertEquals(-1, query2.get(0).elementAt(2));
        assertEquals(-1, query2.get(1).elementAt(Index.fromStart(2)));
        assertEquals(-1, query2.get(2).elementAt(Index.fromEnd(4)));
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

        assertEquals(0, source.apply().elementAt(0));
        assertEquals(1, moveNextCallCount.value);
        assertEquals(0, source.apply().elementAt(Index.fromStart(0)));
        assertEquals(1, moveNextCallCount.value);

        assertEquals(5, source.apply().elementAt(5));
        assertEquals(6, moveNextCallCount.value);
        assertEquals(5, source.apply().elementAt(Index.fromStart(5)));
        assertEquals(6, moveNextCallCount.value);

        assertEquals(0, source.apply().elementAt(Index.fromEnd(ElementCount)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertEquals(5, source.apply().elementAt(Index.fromEnd(5)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);

        assertThrows(ArgumentOutOfRangeException.class, () -> source.apply().elementAt(ElementCount));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertThrows(ArgumentOutOfRangeException.class, () -> source.apply().elementAt(Index.fromStart(ElementCount)));
        assertEquals(ElementCount + 1, moveNextCallCount.value);
        assertThrows(ArgumentOutOfRangeException.class, () -> source.apply().elementAt(Index.fromEnd(0)));
        assertEquals(0, moveNextCallCount.value);
    }

    @Test
    void NonEmptySource_Consistency() {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, Linq.of(source).elementAt(5));
        assertEquals(5, Linq.of(source).elementAt(Index.fromStart(5)));
        assertEquals(5, Linq.of(source).elementAt(Index.fromEnd(5)));

        assertEquals(0, Linq.of(source).elementAt(0));
        assertEquals(0, Linq.of(source).elementAt(Index.fromStart(0)));
        assertEquals(0, Linq.of(source).elementAt(Index.fromEnd(10)));

        assertEquals(9, Linq.of(source).elementAt(9));
        assertEquals(9, Linq.of(source).elementAt(Index.fromStart(9)));
        assertEquals(9, Linq.of(source).elementAt(Index.fromEnd(1)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(11)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(10)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(0)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Integer.MIN_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Integer.MAX_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_ThrowsIListIndexerException() {
        Integer[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(11)));
        // ImmutableArray<T> implements IList<T>. ElementAt calls ImmutableArray<T>'s indexer, which throws IndexOutOfRangeException instead of ArgumentOutOfRangeException.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Linq.of(Arrays.asList(source))).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromEnd(11)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(10)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(10));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromStart(10)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromEnd(0)));
    }

    @Test
    void NonEmptySource_Consistency_NotList() {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAt(5));
        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(5)));
        assertEquals(5, ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(5)));

        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAt(0));
        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertEquals(0, ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(10)));

        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAt(9));
        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(9)));
        assertEquals(9, ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(11)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(10));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(10)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_ListPartition() {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAt(5));
        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(5)));
        assertEquals(5, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(5)));

        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAt(0));
        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertEquals(0, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(10)));

        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAt(9));
        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(9)));
        assertEquals(9, ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(11)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(10));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(10)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_EnumerablePartition() {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(5));
        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(5)));
        assertEquals(5, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(5)));

        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(0));
        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertEquals(0, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(10)));

        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(9));
        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(9)));
        assertEquals(9, EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(11)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(10));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(10)));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_Collection() {
        Integer[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, new TestCollection<>(source).elementAt(5));
        assertEquals(5, new TestCollection<>(source).elementAt(Index.fromStart(5)));
        assertEquals(5, new TestCollection<>(source).elementAt(Index.fromEnd(5)));

        assertEquals(0, new TestCollection<>(source).elementAt(0));
        assertEquals(0, new TestCollection<>(source).elementAt(Index.fromStart(0)));
        assertEquals(0, new TestCollection<>(source).elementAt(Index.fromEnd(10)));

        assertEquals(9, new TestCollection<>(source).elementAt(9));
        assertEquals(9, new TestCollection<>(source).elementAt(Index.fromStart(9)));
        assertEquals(9, new TestCollection<>(source).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(11)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(10));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromStart(10)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void NonEmptySource_Consistency_NonGenericCollection() {
        int[] source = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        assertEquals(5, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(5));
        assertEquals(5, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(5)));
        assertEquals(5, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(5)));

        assertEquals(0, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(0));
        assertEquals(0, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(0)));
        assertEquals(0, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(10)));

        assertEquals(9, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(9));
        assertEquals(9, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(9)));
        assertEquals(9, new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(11)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(10));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(10)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency() {
        int[] source = {};

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(1)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(1)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(0)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Integer.MIN_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Integer.MAX_VALUE));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_ThrowsIListIndexerException() {
        Integer[] source = {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(1)));
        // ImmutableArray<T> implements IList<T>. ElementAt calls ImmutableArray<T>'s indexer, which throws IndexOutOfRangeException instead of ArgumentOutOfRangeException.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromEnd(0)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(source).elementAt(Index.fromStart(1)));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Linq.of(Arrays.asList(source)).elementAt(Index.fromStart(1)));
    }

    @Test
    void EmptySource_Consistency_NotList() {
        int[] source = {};

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(1)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ForceNotCollection(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_ListPartition() {
        int[] source = {};

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(1)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ListPartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_EnumerablePartition() {
        int[] source = {};

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(1)));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> EnumerablePartitionOrEmpty(Linq.of(source)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_Collection() {
        Integer[] source = {};

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromStart(1)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(source).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void EmptySource_Consistency_NonGenericCollection() {
        int[] source = {};

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(1)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(1)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(0)));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(0)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromEnd(Integer.MAX_VALUE)));

        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Integer.MAX_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> new TestCollection<>(Linq.of(source).toArray(Integer.class)).elementAt(Index.fromStart(Integer.MAX_VALUE)));
    }

    @Test
    void testElementAt() {
        IEnumerable<String> enumerable = Linq.of(Arrays.asList("jimi", "mitch"));
        assertEquals("jimi", enumerable.elementAt(0));
        try {
            enumerable.elementAt(2);
            fail("should not be here");
        } catch (IndexOutOfBoundsException ignored) {
            // ok
        }
        try {
            enumerable.elementAt(-1);
            fail("should not be here");
        } catch (IndexOutOfBoundsException ignored) {
        }

        IEnumerable<Long> enumerable2 = Linq.of(new CountIterable(2));
        assertEquals(1L, enumerable2.elementAt(0));
        try {
            enumerable2.elementAt(2);
            fail("should not be here");
        } catch (ArgumentOutOfRangeException ignored) {
        }
        try {
            enumerable2.elementAt(-1);
            fail("should not be here");
        } catch (ArgumentOutOfRangeException ignored) {
        }

        IEnumerable<Integer> one = Linq.singleton(1);
        assertEquals(1, one.elementAt(0));

        IEnumerable<Integer> empty = Linq.empty();
        try {
            Integer num = empty.elementAt(0);
            fail("expect error,but got " + num);
        } catch (ArgumentOutOfRangeException ignored) {
        }

        IEnumerable<Integer> enumerable3 = Linq.infinite(0);
        assertEquals(0, enumerable3.elementAt(10));
    }
}
