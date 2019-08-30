package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.function.IndexFunc2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.ref;
import com.bestvike.tuple.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class SelectManyTest extends TestCase {
    private static IEnumerable<Object[]> ParameterizedTestsData() {
        ArgsList argsList = new ArgsList();
        for (int i = 1; i <= 20; i++) {
            int ii = i;
            Func1<Integer, IEnumerable<Integer>> selector = n -> Linq.range(ii, n);
            argsList.add(Linq.range(1, i), selector);
        }
        return argsList;
    }

    private static IEnumerable<Object[]> DisposeAfterEnumerationData() {
        int[] lengths = {1, 2, 3, 5, 8, 13, 21, 34};

        return Linq.of(lengths).selectMany(l -> Linq.of(lengths), (l1, l2) -> new Object[]{l1, l2});
    }

    private static IEnumerable<Object[]> ThrowOverflowExceptionOnConstituentLargeCounts_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new int[]{Integer.MAX_VALUE, 1});
        argsList.add(new int[]{2, Integer.MAX_VALUE - 1});
        argsList.add(new int[]{123, 456, Integer.MAX_VALUE - 100000, 123456});
        return argsList;
    }

    private static IEnumerable<Object[]> GetToArrayDataSources() {
        ArgsList argsList = new ArgsList();
        // Marker at the end
        argsList.add(new Object[]{
                new IEnumerable[]{
                        new TestEnumerable<>(new Integer[]{0}),
                        new TestEnumerable<>(new Integer[]{1}),
                        new TestEnumerable<>(new Integer[]{2}),
                        Linq.of(new int[]{3}),
                }
        });

        // Marker at beginning
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        new TestEnumerable<>(new Integer[]{1}),
                        new TestEnumerable<>(new Integer[]{2}),
                        new TestEnumerable<>(new Integer[]{3}),
                }
        });

        // Marker in middle
        argsList.add(new Object[]{
                new IEnumerable[]{
                        new TestEnumerable<>(new Integer[]{0}),
                        Linq.of(new int[]{1}),
                        new TestEnumerable<>(new Integer[]{2}),
                }
        });

        // Non-marker in middle
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        new TestEnumerable<>(new Integer[]{1}),
                        Linq.of(new int[]{2})
                }
        });

        // Big arrays (marker in middle)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        new TestEnumerable<>(Linq.range(0, 100).toArray(Integer.class)),
                        Linq.range(100, 100).toArray(),
                        new TestEnumerable<>(Linq.range(200, 100).toArray(Integer.class)),
                }
        });

        // Big arrays (non-marker in middle)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.range(0, 100).toArray(),
                        new TestEnumerable<>(Linq.range(100, 100).toArray(Integer.class)),
                        Linq.range(200, 100).toArray(),
                }
        });

        // Interleaved (first marker)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        new TestEnumerable<>(new Integer[]{1}),
                        Linq.of(new int[]{2}),
                        new TestEnumerable<>(new Integer[]{3}),
                        Linq.of(new int[]{4}),
                }
        });

        // Interleaved (first non-marker)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        new TestEnumerable<>(new Integer[]{0}),
                        Linq.of(new int[]{1}),
                        new TestEnumerable<>(new Integer[]{2}),
                        Linq.of(new int[]{3}),
                        new TestEnumerable<>(new Integer[]{4}),
                }
        });
        return argsList;
    }

    private static IEnumerable<Object[]> EvaluateSelectorOncePerItem_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(10);
        return argsList;
    }

    @Test
    void EmptySource() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany(e -> Linq.of(Linq.of(e.total))));
    }

    @Test
    void EmptySourceIndexedSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.of(e.total)));
    }

    @Test
    void EmptySourceResultSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany(e -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void EmptySourceResultSelectorIndexedSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void SingleElement() {
        Integer[] expected = {90, 55, null, 43, 89};
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", expected)
        };
        assertEquals(Linq.of(expected), Linq.of(source).selectMany(e -> Linq.of(e.total)));
    }

    @Test
    void NonEmptySelectingEmpty() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[0]),
                new StringWithIntArray("Bob", new Integer[0]),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[0]),
                new StringWithIntArray("Prakash", new Integer[0])
        };

        assertEmpty(Linq.of(source).selectMany(e -> Linq.of(e.total)));
    }

    @Test
    void NonEmptySelectingEmptyIndexedSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[0]),
                new StringWithIntArray("Bob", new Integer[0]),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[0]),
                new StringWithIntArray("Prakash", new Integer[0])
        };

        assertEmpty(Linq.of(source).selectMany((e, i) -> Linq.of(e.total)));
    }

    @Test
    void NonEmptySelectingEmptyWithResultSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[0]),
                new StringWithIntArray("Bob", new Integer[0]),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[0]),
                new StringWithIntArray("Prakash", new Integer[0])
        };

        assertEmpty(Linq.of(source).selectMany(e -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void NonEmptySelectingEmptyIndexedSelectorWithResultSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[0]),
                new StringWithIntArray("Bob", new Integer[0]),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[0]),
                new StringWithIntArray("Prakash", new Integer[0])
        };

        assertEmpty(Linq.of(source).selectMany((e, i) -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void ResultsSelected() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.of(expected), Linq.of(source).selectMany(e -> Linq.of(e.total)));
    }

    @Test
    void RunOnce() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.of(expected), Linq.of(source).runOnce().selectMany(e -> Linq.of(e.total).runOnce()));
    }

    @Test
    void SourceEmptyIndexUsed() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, index) -> Linq.of(e.total)));
    }

    @Test
    void SingleElementIndexUsed() {
        Integer[] expected = {90, 55, null, 43, 89};
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", expected)
        };
        assertEquals(Linq.of(expected), Linq.of(source).selectMany((e, index) -> Linq.of(e.total)));
    }

    @Test
    void NonEmptySelectingEmptyIndexUsed() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[0]),
                new StringWithIntArray("Bob", new Integer[0]),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[0]),
                new StringWithIntArray("Prakash", new Integer[0])
        };
        assertEmpty(Linq.of(source).selectMany((e, index) -> Linq.of(e.total)));
    }

    @Test
    void ResultsSelectedIndexUsed() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.of(expected), Linq.of(source).selectMany((e, index) -> Linq.of(e.total)));
    }

    @Test
    void IndexCausingFirstToBeSelected() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };

        assertEquals(Linq.of(Linq.of(source).first().total), Linq.of(source).selectMany((e, i) -> i == 0 ? Linq.of(e.total) : Linq.empty()));
    }

    @Test
    void IndexCausingLastToBeSelected() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Robert", new Integer[]{-10, 100})
        };

        assertEquals(Linq.of(Linq.of(source).last().total), Linq.of(source).selectMany((e, i) -> i == 4 ? Linq.of(e.total) : Linq.empty()));
    }

    @Test
    void IndexOverflow() {
        IEnumerable<?> selected = new FastInfiniteEnumerator<Integer>().selectMany((e, i) -> Linq.<Integer>empty());
        try (IEnumerator<?> en = selected.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    void ResultSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };
        String[] expected = {"1", "2", "3", "4", "5", "6", "8", "9", "-10", "100"};

        assertEquals(Linq.of(expected), Linq.of(source).selectMany(e -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void NullResultSelector() {
        Func2<StringWithIntArray, Integer, String> resultSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(e -> Linq.of(e.total), resultSelector));
    }

    @Test
    void NullResultSelectorIndexedSelector() {
        Func2<StringWithIntArray, Integer, String> resultSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.of(e.total), resultSelector));
    }

    @Test
    void NullSourceWithResultSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).selectMany(e -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void NullCollectionSelector() {
        Func1<StringWithIntArray, IEnumerable<Integer>> collectionSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(collectionSelector, (e, f) -> f.toString()));
    }

    @Test
    void NullIndexedCollectionSelector() {
        IndexFunc2<StringWithIntArray, IEnumerable<Integer>> collectionSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(collectionSelector, (e, f) -> f.toString()));
    }

    @Test
    void NullSource() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).selectMany(e -> Linq.of(e.total)));
    }

    @Test
    void NullSourceIndexedSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).selectMany((e, i) -> Linq.of(e.total)));
    }

    @Test
    void NullSourceIndexedSelectorWithResultSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).selectMany((e, i) -> Linq.of(e.total), (e, f) -> f.toString()));
    }

    @Test
    void NullSelector() {
        Func1<StringWithIntArray, IEnumerable<Integer>> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(new StringWithIntArray[0]).selectMany(selector));
    }

    @Test
    void NullIndexedSelector() {
        IndexFunc2<StringWithIntArray, IEnumerable<Integer>> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(new StringWithIntArray[0]).selectMany(selector));
    }

    @Test
    void IndexCausingFirstToBeSelectedWithResultSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Prakash", new Integer[]{-10, 100})
        };
        String[] expected = {"1", "2", "3", "4"};
        assertEquals(Linq.of(expected), Linq.of(source).selectMany((e, i) -> i == 0 ? Linq.of(e.total) : Linq.empty(), (e, f) -> f.toString()));
    }

    @Test
    void IndexCausingLastToBeSelectedWithResultSelector() {
        StringWithIntArray[] source = {
                new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                new StringWithIntArray("Bob", new Integer[]{5, 6}),
                new StringWithIntArray("Chris", new Integer[0]),
                new StringWithIntArray(null, new Integer[]{8, 9}),
                new StringWithIntArray("Robert", new Integer[]{-10, 100})
        };

        String[] expected = {"-10", "100"};
        assertEquals(Linq.of(expected), Linq.of(source).selectMany((e, i) -> i == 4 ? Linq.of(e.total) : Linq.empty(), (e, f) -> f.toString()));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany(i -> Linq.of(new int[0]));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany((e, i) -> Linq.of(new int[0]));
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateResultSel() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany(i -> Linq.of(new int[0]), (e, i) -> e);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateIndexedResultSel() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany((e, i) -> Linq.of(new int[0]), (e, i) -> e);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @ParameterizedTest
    @MethodSource("ParameterizedTestsData")
    void ParameterizedTests(IEnumerable<Integer> source, Func1<Integer, IEnumerable<Integer>> selector) {
        IEnumerable<Integer> expected = source.select(i -> selector.apply(i)).aggregate((l, r) -> l.concat(r));
        IEnumerable<Integer> actual = source.selectMany(selector);

        assertEquals(expected, actual);
        assertEquals(expected.count(), actual.count()); // SelectMany may employ an optimized Count implementation.
        assertEquals(expected.toArray(), actual.toArray());
        assertEquals(expected.toList(), actual.toList());
    }

    @ParameterizedTest
    @MethodSource("DisposeAfterEnumerationData")
    void DisposeAfterEnumeration(int sourceLength, int subLength) {
        ref<Integer> sourceState = ref.init(0);
        ref<Integer> subIndex = ref.init(0); // Index within the arrays the sub-collection is supposed to be at.
        int[] subState = new int[sourceLength];

        ref<Boolean> sourceDisposed = ref.init(false);
        boolean[] subCollectionDisposed = new boolean[sourceLength];

        DelegateIterator<Integer> source = new DelegateIterator<>(
                () -> ++sourceState.value <= sourceLength,
                () -> 0,
                () -> sourceDisposed.value = true);

        DelegateIterator<Integer> subCollection = new DelegateIterator<>(
                () -> ++subState[subIndex.value] <= subLength, // Return true `subLength` times.
                () -> subState[subIndex.value],
                () -> subCollectionDisposed[subIndex.value++] = true); // Record that Dispose was called, and move on to the next index.

        IEnumerable<Integer> iterator = source.selectMany(a -> subCollection);

        int index = 0; // How much have we gone into the iterator?
        IEnumerator<Integer> ee = iterator.enumerator();
        try (IEnumerator<Integer> e = ee) {
            while (e.moveNext()) {
                int item = e.current();

                assertEquals(subState[subIndex.value], item); // Verify Current.
                assertEquals(index / subLength, subIndex.value);

                assertFalse(sourceDisposed.value); // Not yet.

                // This represents whehter the sub-collection we're iterating thru right now
                // has been disposed. Also not yet.
                assertFalse(subCollectionDisposed[subIndex.value]);

                // However, all of the sub-collections before us should have been disposed.
                // Their indices should also be maxed out.
                assertAll(Linq.of(subState).take(subIndex.value), s -> assertEquals(subLength + 1, s));
                assertAll(Linq.of(subCollectionDisposed).take(subIndex.value), t -> assertTrue(t));

                index++;
            }
        }

        assertTrue(sourceDisposed.value);
        assertEquals(sourceLength, subIndex.value);
        assertAll(Linq.of(subState), s -> assertEquals(subLength + 1, s));
        assertAll(Linq.of(subCollectionDisposed), t -> assertTrue(t));

        // .NET Core fixes an oversight where we wouldn't properly dispose
        // the SelectMany iterator. See https://github.com/dotnet/corefx/pull/13942.
        Integer expectedCurrent = null;
        assertEquals(expectedCurrent, ee.current());
        assertFalse(ee.moveNext());
        assertEquals(expectedCurrent, ee.current());
    }

    @ParameterizedTest
    @MethodSource("ThrowOverflowExceptionOnConstituentLargeCounts_TestData")
    void ThrowOverflowExceptionOnConstituentLargeCounts(int[] counts) {
        IEnumerable<Integer> iterator = Linq.of(counts).selectMany(c -> Linq.range(1, c));
        assertThrows(ArithmeticException.class, () -> iterator.count());
    }

    @ParameterizedTest
    @MethodSource("GetToArrayDataSources")
    void CollectionInterleavedWithLazyEnumerables_ToArray(IEnumerable<Integer>[] arrays) {
        // See https://github.com/dotnet/corefx/issues/23680

        Array<Integer> results = Linq.of(arrays).selectMany(ar -> ar).toArray();

        for (int i = 0; i < results._getCount(); i++) {
            assertEquals(i, results.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("EvaluateSelectorOncePerItem_TestData")
    void EvaluateSelectorOncePerItem(int count) {
        int[] timesCalledMap = new int[count];

        IEnumerable<Integer> source = Linq.range(0, 10);
        IEnumerable<Integer> iterator = source.selectMany(index -> {
            timesCalledMap[index]++;
            return Linq.of(index);
        });

        // Iteration
        for (int index : iterator) {
            assertEquals(Linq.repeat(1, index + 1), Linq.of(timesCalledMap).take(index + 1));
            assertEquals(Linq.repeat(0, timesCalledMap.length - index - 1), Linq.of(timesCalledMap).skip(index + 1));
        }

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToArray
        iterator.toArray();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.of(timesCalledMap));

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToList
        iterator.toList();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.of(timesCalledMap));

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToHashSet
        iterator.toSet();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.of(timesCalledMap));
    }

    @Test
    void testSelectMany() {
        List<String> nameSeqs = Linq.of(depts)
                .selectMany(dept -> Linq.of(dept.employees))
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        assertEquals("[#0: Fred, #1: Eric, #2: Janet, #3: Bill]", nameSeqs.toString());
    }

    @Test
    void testSelectManyIndexed() {
        List<String> nameSeqs = Linq.of(depts)
                .selectMany((dept, index) -> Linq.of(dept.employees).select(emp -> String.format("#%d: %s", index, emp.name)))
                .toList();
        assertEquals("[#0: Fred, #0: Eric, #0: Janet, #2: Bill]", nameSeqs.toString());
    }

    @Test
    void testSelectManySelect() {
        List<String> nameSeqs = Linq.of(depts)
                .selectMany(dept -> Linq.of(dept.employees), (dept, emp) -> String.format("#%s: %s", dept.name, emp.name))
                .toList();
        assertEquals("[#Sales: Fred, #Sales: Eric, #Sales: Janet, #Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    void testSelectManyIndexedSelect() {
        List<String> nameSeqs = Linq.of(depts)
                .selectMany((dept, index) -> Linq.of(dept.employees).select(emp -> Tuple.create(index, emp)), (dept, empInfo) -> String.format("#%s: %s: %s", empInfo.getItem1(), dept.name, empInfo.getItem2().name))
                .toList();
        assertEquals("[#0: Sales: Fred, #0: Sales: Eric, #0: Sales: Janet, #2: Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    void testSelectManyCrossJoin() {
        //selectMany 实现简单的 cross join
        String cross = Linq.of(emps).concat(Linq.of(badEmps))
                .selectMany(emp -> Linq.of(depts).concat(Linq.of(badDepts)).select(dept -> String.format("%s works in %s", emp.name, dept.name)))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", cross);
    }

    @Test
    void testSelectManyIndexed2() {
        IEnumerable<Integer> source = Linq.of(Linq.of(0, 1), Linq.of(2, 3), Linq.of(4, 5))
                .selectMany((nums, index) -> nums);
        assertEquals(source.runOnce(), source.runOnce());
        assertEquals(Linq.of(0, 0, 1, 1, 2, 2), source.select(x -> x / 2));
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), source.toArray());
        assertEquals(Linq.of(0, 1, 2, 3, 4, 5), Linq.of(source.toArray(Integer.class)));
        assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5), source.toList());
        assertEquals(6, source.count());
        assertEquals(Linq.of(1, 2, 3, 4, 5), source.skip(1));
        assertEquals(Linq.of(0), source.take(1));
        assertEquals(0, source.elementAt(0));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertEquals(0, source.first());
        assertEquals(5, source.last());

        IEnumerable<Integer> emptySource = Linq.<IEnumerable<Integer>>of()
                .selectMany((nums, index) -> nums);
        assertSame(ArrayUtils.empty(), emptySource.toArray().getArray());
        assertEquals(Linq.empty(), Linq.of(emptySource.toArray(Integer.class)));
        assertEquals(Collections.emptyList(), emptySource.toList());
        assertThrows(InvalidOperationException.class, () -> emptySource.first());
        assertThrows(InvalidOperationException.class, () -> emptySource.last());
    }
}
