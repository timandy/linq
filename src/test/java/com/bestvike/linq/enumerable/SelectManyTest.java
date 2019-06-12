package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.ref;
import com.bestvike.tuple.Tuple;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SelectManyTest extends TestCase {
    @Test
    public void EmptySource() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany(e -> Linq.asEnumerable(Linq.asEnumerable(e.total))));
    }

    @Test
    public void EmptySourceIndexedSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void EmptySourceResultSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany(e -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void EmptySourceResultSelectorIndexedSelector() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void SingleElement() {
        Integer[] expected = {90, 55, null, 43, 89};
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", expected)
                };
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NonEmptySelectingEmpty() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[0]),
                        new StringWithIntArray("Bob", new Integer[0]),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[0]),
                        new StringWithIntArray("Prakash", new Integer[0])
                };

        assertEmpty(Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NonEmptySelectingEmptyIndexedSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[0]),
                        new StringWithIntArray("Bob", new Integer[0]),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[0]),
                        new StringWithIntArray("Prakash", new Integer[0])
                };

        assertEmpty(Linq.asEnumerable(source).selectMany((e, i) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NonEmptySelectingEmptyWithResultSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[0]),
                        new StringWithIntArray("Bob", new Integer[0]),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[0]),
                        new StringWithIntArray("Prakash", new Integer[0])
                };

        assertEmpty(Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void NonEmptySelectingEmptyIndexedSelectorWithResultSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[0]),
                        new StringWithIntArray("Bob", new Integer[0]),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[0]),
                        new StringWithIntArray("Prakash", new Integer[0])
                };

        assertEmpty(Linq.asEnumerable(source).selectMany((e, i) -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void ResultsSelected() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void RunOnce() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).runOnce().selectMany(e -> Linq.asEnumerable(e.total).runOnce()));
    }

    @Test
    public void SourceEmptyIndexUsed() {
        assertEmpty(Linq.<StringWithIntArray>empty().selectMany((e, index) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void SingleElementIndexUsed() {
        Integer[] expected = {90, 55, null, 43, 89};
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", expected)
                };
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany((e, index) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NonEmptySelectingEmptyIndexUsed() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[0]),
                        new StringWithIntArray("Bob", new Integer[0]),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[0]),
                        new StringWithIntArray("Prakash", new Integer[0])
                };
        assertEmpty(Linq.asEnumerable(source).selectMany((e, index) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void ResultsSelectedIndexUsed() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };
        Integer[] expected = {1, 2, 3, 4, 5, 6, 8, 9, -10, 100};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany((e, index) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void IndexCausingFirstToBeSelected() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };

        assertEquals(Linq.asEnumerable(Linq.asEnumerable(source).first().total), Linq.asEnumerable(source).selectMany((e, i) -> i == 0 ? Linq.asEnumerable(e.total) : Linq.empty()));
    }

    @Test
    public void IndexCausingLastToBeSelected() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Robert", new Integer[]{-10, 100})
                };

        assertEquals(Linq.asEnumerable(Linq.asEnumerable(source).last().total), Linq.asEnumerable(source).selectMany((e, i) -> i == 4 ? Linq.asEnumerable(e.total) : Linq.empty()));
    }

    @Test
    public void IndexOverflow() {
        IEnumerable<?> selected = new FastInfiniteEnumerator<Integer>().selectMany((e, i) -> Linq.<Integer>empty());
        try (IEnumerator<?> en = selected.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    public void ResultSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };
        String[] expected = {"1", "2", "3", "4", "5", "6", "8", "9", "-10", "100"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void NullResultSelector() {
        Func2<StringWithIntArray, Integer, String> resultSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(e -> Linq.asEnumerable(e.total), resultSelector));
    }

    @Test
    public void NullResultSelectorIndexedSelector() {
        Func2<StringWithIntArray, Integer, String> resultSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany((e, i) -> Linq.asEnumerable(e.total), resultSelector));
    }

    @Test
    public void NullSourceWithResultSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void NullCollectionSelector() {
        Func1<StringWithIntArray, IEnumerable<Integer>> collectionSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(collectionSelector, (e, f) -> f.toString()));
    }

    @Test
    public void NullIndexedCollectionSelector() {
        Func2<StringWithIntArray, Integer, IEnumerable<Integer>> collectionSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<StringWithIntArray>empty().selectMany(collectionSelector, (e, f) -> f.toString()));
    }

    @Test
    public void NullSource() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(source).selectMany(e -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NullSourceIndexedSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(source).selectMany((e, i) -> Linq.asEnumerable(e.total)));
    }

    @Test
    public void NullSourceIndexedSelectorWithResultSelector() {
        StringWithIntArray[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(source).selectMany((e, i) -> Linq.asEnumerable(e.total), (e, f) -> f.toString()));
    }

    @Test
    public void NullSelector() {
        Func1<StringWithIntArray, IEnumerable<Integer>> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(new StringWithIntArray[0]).selectMany(selector));
    }

    @Test
    public void NullIndexedSelector() {
        Func2<StringWithIntArray, Integer, IEnumerable<Integer>> selector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(new StringWithIntArray[0]).selectMany(selector));
    }

    @Test
    public void IndexCausingFirstToBeSelectedWithResultSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Prakash", new Integer[]{-10, 100})
                };
        String[] expected = {"1", "2", "3", "4"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany((e, i) -> i == 0 ? Linq.asEnumerable(e.total) : Linq.empty(), (e, f) -> f.toString()));
    }

    @Test
    public void IndexCausingLastToBeSelectedWithResultSelector() {
        StringWithIntArray[] source =
                {
                        new StringWithIntArray("Prakash", new Integer[]{1, 2, 3, 4}),
                        new StringWithIntArray("Bob", new Integer[]{5, 6}),
                        new StringWithIntArray("Chris", new Integer[0]),
                        new StringWithIntArray(null, new Integer[]{8, 9}),
                        new StringWithIntArray("Robert", new Integer[]{-10, 100})
                };

        String[] expected = {"-10", "100"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).selectMany((e, i) -> i == 4 ? Linq.asEnumerable(e.total) : Linq.empty(), (e, f) -> f.toString()));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany(i -> Linq.asEnumerable(new int[0]));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany((e, i) -> Linq.asEnumerable(new int[0]));
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateResultSel() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany(i -> Linq.asEnumerable(new int[0]), (e, i) -> e);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexedResultSel() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).selectMany((e, i) -> Linq.asEnumerable(new int[0]), (e, i) -> e);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ParameterizedTests() {
        for (Object[] objects : this.ParameterizedTestsData()) {
            this.ParameterizedTests((IEnumerable<Integer>) objects[0], (Func1<Integer, IEnumerable<Integer>>) objects[1]);
        }
    }

    private void ParameterizedTests(IEnumerable<Integer> source, Func1<Integer, IEnumerable<Integer>> selector) {
        IEnumerable<Integer> expected = source.select(i -> selector.apply(i)).aggregate((l, r) -> l.concat(r));
        IEnumerable<Integer> actual = source.selectMany(selector);

        assertEquals(expected, actual);
        assertEquals(expected.count(), actual.count()); // SelectMany may employ an optimized Count implementation.
        assertEquals(expected.toArray(), actual.toArray());
        assertEquals(expected.toList(), actual.toList());
    }

    private IEnumerable<Object[]> ParameterizedTestsData() {
        List<Object[]> lst = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            int ii = i;
            Func1<Integer, IEnumerable<Integer>> selector = n -> Linq.range(ii, n);
            lst.add(new Object[]{Linq.range(1, i), selector});
        }
        return Linq.asEnumerable(lst);
    }

    @Test
    public void DisposeAfterEnumeration() {
        for (Object[] objects : this.DisposeAfterEnumerationData()) {
            this.DisposeAfterEnumeration((int) objects[0], (int) objects[1]);
        }
    }

    private void DisposeAfterEnumeration(int sourceLength, int subLength) {
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
                assertAll(Linq.asEnumerable(subState).take(subIndex.value), s -> assertEquals(subLength + 1, s));
                assertAll(Linq.asEnumerable(subCollectionDisposed).take(subIndex.value), t -> assertTrue(t));

                index++;
            }
        }

        assertTrue(sourceDisposed.value);
        assertEquals(sourceLength, subIndex.value);
        assertAll(Linq.asEnumerable(subState), s -> assertEquals(subLength + 1, s));
        assertAll(Linq.asEnumerable(subCollectionDisposed), t -> assertTrue(t));

        // .NET Core fixes an oversight where we wouldn't properly dispose
        // the SelectMany iterator. See https://github.com/dotnet/corefx/pull/13942.
        Integer expectedCurrent = null;
        assertEquals(expectedCurrent, ee.current());
        assertFalse(ee.moveNext());
        assertEquals(expectedCurrent, ee.current());
    }

    private IEnumerable<Object[]> DisposeAfterEnumerationData() {
        int[] lengths = {1, 2, 3, 5, 8, 13, 21, 34};

        return Linq.asEnumerable(lengths).selectMany(l -> Linq.asEnumerable(lengths), (l1, l2) -> new Object[]{l1, l2});
    }

    @Test
    public void ThrowOverflowExceptionOnConstituentLargeCounts() {
        this.ThrowOverflowExceptionOnConstituentLargeCounts(new int[]{Integer.MAX_VALUE, 1});
        this.ThrowOverflowExceptionOnConstituentLargeCounts(new int[]{2, Integer.MAX_VALUE - 1});
        this.ThrowOverflowExceptionOnConstituentLargeCounts(new int[]{123, 456, Integer.MAX_VALUE - 100000, 123456});
    }

    private void ThrowOverflowExceptionOnConstituentLargeCounts(int[] counts) {
        IEnumerable<Integer> iterator = Linq.asEnumerable(counts).selectMany(c -> Linq.range(1, c));
        assertThrows(ArithmeticException.class, () -> iterator.count());
    }

    @Test
    public void CollectionInterleavedWithLazyEnumerables_ToArray() {
        for (Object[] objects : this.GetToArrayDataSources()) {
            this.CollectionInterleavedWithLazyEnumerables_ToArray((IEnumerable<Integer>[]) objects[0]);
        }
    }

    private void CollectionInterleavedWithLazyEnumerables_ToArray(IEnumerable<Integer>[] arrays) {
        // See https://github.com/dotnet/corefx/issues/23680

        Array<Integer> results = Linq.asEnumerable(arrays).selectMany(ar -> ar).toArray();

        for (int i = 0; i < results._getCount(); i++) {
            assertEquals(i, results.get(i));
        }
    }

    private IEnumerable<Object[]> GetToArrayDataSources() {
        List<Object[]> lst = new ArrayList<>();
        // Marker at the end
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        new TestEnumerable<>(new Integer[]{0}),
                                        new TestEnumerable<>(new Integer[]{1}),
                                        new TestEnumerable<>(new Integer[]{2}),
                                        Linq.asEnumerable(new int[]{3}),
                                }
                });

        // Marker at beginning
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        Linq.asEnumerable(new int[]{0}),
                                        new TestEnumerable<>(new Integer[]{1}),
                                        new TestEnumerable<>(new Integer[]{2}),
                                        new TestEnumerable<>(new Integer[]{3}),
                                }
                });

        // Marker in middle
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        new TestEnumerable<>(new Integer[]{0}),
                                        Linq.asEnumerable(new int[]{1}),
                                        new TestEnumerable<>(new Integer[]{2}),
                                }
                });

        // Non-marker in middle
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        Linq.asEnumerable(new int[]{0}),
                                        new TestEnumerable<>(new Integer[]{1}),
                                        Linq.asEnumerable(new int[]{2})
                                }
                });

        // Big arrays (marker in middle)
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        new TestEnumerable<>(Linq.range(0, 100).toArray(Integer.class)),
                                        Linq.range(100, 100).toArray(),
                                        new TestEnumerable<>(Linq.range(200, 100).toArray(Integer.class)),
                                }
                });

        // Big arrays (non-marker in middle)
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        Linq.range(0, 100).toArray(),
                                        new TestEnumerable<>(Linq.range(100, 100).toArray(Integer.class)),
                                        Linq.range(200, 100).toArray(),
                                }
                });

        // Interleaved (first marker)
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        Linq.asEnumerable(new int[]{0}),
                                        new TestEnumerable<>(new Integer[]{1}),
                                        Linq.asEnumerable(new int[]{2}),
                                        new TestEnumerable<>(new Integer[]{3}),
                                        Linq.asEnumerable(new int[]{4}),
                                }
                });

        // Interleaved (first non-marker)
        lst.add(new Object[]
                {
                        new IEnumerable[]
                                {
                                        new TestEnumerable<>(new Integer[]{0}),
                                        Linq.asEnumerable(new int[]{1}),
                                        new TestEnumerable<>(new Integer[]{2}),
                                        Linq.asEnumerable(new int[]{3}),
                                        new TestEnumerable<>(new Integer[]{4}),
                                }
                });
        return Linq.asEnumerable(lst);
    }

    @Test
    public void EvaluateSelectorOncePerItem() {
        this.EvaluateSelectorOncePerItem(10);
    }

    private void EvaluateSelectorOncePerItem(int count) {
        int[] timesCalledMap = new int[count];

        IEnumerable<Integer> source = Linq.range(0, 10);
        IEnumerable<Integer> iterator = source.selectMany(index ->
        {
            timesCalledMap[index]++;
            return Linq.asEnumerable(index);
        });

        // Iteration
        for (int index : iterator) {
            assertEquals(Linq.repeat(1, index + 1), Linq.asEnumerable(timesCalledMap).take(index + 1));
            assertEquals(Linq.repeat(0, timesCalledMap.length - index - 1), Linq.asEnumerable(timesCalledMap).skip(index + 1));
        }

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToArray
        iterator.toArray();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.asEnumerable(timesCalledMap));

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToList
        iterator.toList();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.asEnumerable(timesCalledMap));

        Arrays.fill(timesCalledMap, 0, timesCalledMap.length, 0);

        // ToHashSet
        iterator.toSet();
        assertEquals(Linq.repeat(1, timesCalledMap.length), Linq.asEnumerable(timesCalledMap));
    }

    @Test
    public void testSelectMany() {
        List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees))
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        assertEquals("[#0: Fred, #1: Eric, #2: Janet, #3: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexed() {
        List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((dept, index) -> Linq.asEnumerable(dept.employees).select(emp -> String.format("#%d: %s", index, emp.name)))
                .toList();
        assertEquals("[#0: Fred, #0: Eric, #0: Janet, #2: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManySelect() {
        List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees), (dept, emp) -> String.format("#%s: %s", dept.name, emp.name))
                .toList();
        assertEquals("[#Sales: Fred, #Sales: Eric, #Sales: Janet, #Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexedSelect() {
        List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((Department dept, Integer index) -> Linq.asEnumerable(dept.employees).select(emp -> Tuple.create(index, emp)), (dept, empInfo) -> String.format("#%s: %s: %s", empInfo.getItem1(), dept.name, empInfo.getItem2().name))
                .toList();
        assertEquals("[#0: Sales: Fred, #0: Sales: Eric, #0: Sales: Janet, #2: Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyCrossJoin() {
        //selectMany 实现简单的 cross join
        String cross = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .selectMany(emp -> Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)).select(dept -> String.format("%s works in %s", emp.name, dept.name)))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", cross);
    }
}
