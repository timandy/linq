package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.Func0;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.ref;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SelectTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.asEnumerable(new String[]{"Alen", "Felix", null, null, "X", "Have Space", "Clinton", ""})
                .select(x1 -> x1);

        IEnumerable<Integer> q2 = Linq.asEnumerable(new int[]{55, 49, 9, -100, 24, 25, -1, 0})
                .select(x2 -> x2);

        IEnumerable<Tuple2<String, Integer>> q = q1.selectMany(a -> q2, (x3, x4) -> Tuple.create(x3, x4));

        assertEquals(q.select(e -> e.getItem1()), q.select(e -> e.getItem1()));
    }

    @Test
    public void SingleElement() {
        CustInfo[] source = new CustInfo[]{new CustInfo("Prakash", 98088)};
        String[] expected = {"Prakash"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select(e -> e.name));
    }

    @Test
    public void SelectProperty() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088),
                new CustInfo("Bob", 29099),
                new CustInfo("Chris", 39033),
                new CustInfo(null, 30349),
                new CustInfo("Prakash", 39030)
        };
        String[] expected = {"Prakash", "Bob", "Chris", null, "Prakash"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select(e -> e.name));
    }

    @Test
    public void RunOnce() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088),
                new CustInfo("Bob", 29099),
                new CustInfo("Chris", 39033),
                new CustInfo(null, 30349),
                new CustInfo("Prakash", 39030)
        };
        String[] expected = {"Prakash", "Bob", "Chris", null, "Prakash"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).runOnce().select(e -> e.name));
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).toArray().runOnce().select(e -> e.name));
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(Linq.asEnumerable(source).toList()).runOnce().select(e -> e.name));
    }

    @Test
    public void EmptyWithIndexedSelector() {
        assertEquals(Linq.<Integer>empty(), Linq.<String>empty().select((s, i) -> s.length() + i));
    }

    @Test
    public void EnumerateFromDifferentThread() {
        IEnumerable<String> selected = Linq.range(0, 100).where(i -> i > 3).select(i -> i.toString());
        CompletableFuture[] tasks = new CompletableFuture[4];
        for (int i = 0; i != 4; ++i)
            tasks[i] = CompletableFuture.supplyAsync(() -> selected.toList());
        CompletableFuture.allOf(tasks).join();
    }

    @Test
    public void SingleElementIndexedSelector() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088)
        };
        String[] expected = {"Prakash"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select((e, index) -> e.name));
    }

    @Test
    public void SelectPropertyPassingIndex() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088),
                new CustInfo("Bob", 29099),
                new CustInfo("Chris", 39033),
                new CustInfo(null, 30349),
                new CustInfo("Prakash", 39030)
        };
        String[] expected = {"Prakash", "Bob", "Chris", null, "Prakash"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select((e, i) -> e.name));
    }

    @Test
    public void SelectPropertyUsingIndex() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088),
                new CustInfo("Bob", 29099),
                new CustInfo("Chris", 39033)
        };
        String[] expected = {"Prakash", null, null};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select((e, i) -> i == 0 ? e.name : null));
    }

    @Test
    public void SelectPropertyPassingIndexOnLast() {
        CustInfo[] source = new CustInfo[]{
                new CustInfo("Prakash", 98088),
                new CustInfo("Bob", 29099),
                new CustInfo("Chris", 39033),
                new CustInfo("Robert", 39033),
                new CustInfo("Allen", 39033),
                new CustInfo("Chuck", 39033)
        };
        String[] expected = {null, null, null, null, null, "Chuck"};
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).select((e, i) -> i == 5 ? e.name : null));
    }

    @Test
    public void Overflow() {
        IEnumerable<Integer> selected = new FastInfiniteEnumerator<Integer>().select((e, i) -> e);
        try (IEnumerator<Integer> en = selected.enumerator()) {
            assertThrows(ArithmeticException.class, () -> {
                while (en.moveNext()) {
                }
            });
        }
    }

    @Test
    public void Select_SourceIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = null;
        Func1<Integer, Integer> selector = i -> i + 1;

        assertThrows(NullPointerException.class, () -> source.select(selector));
    }

    @Test
    public void Select_SelectorIsNull_ArgumentNullExceptionThrown_Indexed() {
        IEnumerable<Integer> source = Linq.range(1, 10);
        Func2<Integer, Integer, Integer> selector = null;

        assertThrows(ArgumentNullException.class, () -> source.select(selector));
    }

    @Test
    public void Select_SourceIsNull_ArgumentNullExceptionThrown_Indexed() {
        IEnumerable<Integer> source = null;
        Func2<Integer, Integer, Integer> selector = (e, i) -> i + 1;

        assertThrows(NullPointerException.class, () -> source.select(selector));
    }

    @Test
    public void Select_SelectorIsNull_ArgumentNullExceptionThrown() {
        IEnumerable<Integer> source = Linq.range(1, 10);
        Func1<Integer, Integer> selector = null;

        assertThrows(ArgumentNullException.class, () -> source.select(selector));
    }

    @Test
    public void Select_SourceIsAnArray_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Func0<Integer>[] source = new Func0[]{() -> {
            funcCalled.value = true;
            return 1;
        }};

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Select_SourceIsAList_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Func0<Integer>> source = Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        });

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Select_SourceIsIReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Integer>> source = Collections.unmodifiableCollection(Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        }));

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Select_SourceIsICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Integer>> source = new LinkedList<>(Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        }));

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Select_SourceIsIEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Func0<Integer>> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return 1;
        }, 1);

        IEnumerable<Integer> query = source.select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void SelectSelect_SourceIsAnArray_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Func0<Integer>[] source = new Func0[]{() -> {
            funcCalled.value = true;
            return 1;
        }};

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void SelectSelect_SourceIsAList_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        List<Func0<Integer>> source = Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        });

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void SelectSelect_SourceIsIReadOnlyCollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Integer>> source = Collections.unmodifiableCollection(Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        }));

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void SelectSelect_SourceIsICollection_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        Collection<Func0<Integer>> source = new LinkedList<>(Arrays.asList(() -> {
            funcCalled.value = true;
            return 1;
        }));

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(d -> d).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void SelectSelect_SourceIsIEnumerable_ExecutionIsDeferred() {
        ref<Boolean> funcCalled = ref.init(false);
        IEnumerable<Func0<Integer>> source = Linq.repeat(() -> {
            funcCalled.value = true;
            return 1;
        }, 1);

        IEnumerable<Integer> query = source.select(d -> d).select(d -> d.apply());
        assertFalse(funcCalled.value);
    }

    @Test
    public void Select_SourceIsAnArray_ReturnsExpectedValues() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        int index = 0;
        for (int item : query) {
            int expected = selector.apply(source[index]);
            assertEquals(expected, item);
            index++;
        }

        assertEquals(source.length, index);
    }

    @Test
    public void Select_SourceIsAList_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        int index = 0;
        for (int item : query) {
            int expected = selector.apply(source.get(index));
            assertEquals(expected, item);
            index++;
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void Select_SourceIsIReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(index);
            assertEquals(expected, item);
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void Select_SourceIsICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(index);
            assertEquals(expected, item);
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void Select_SourceIsIEnumerable_ReturnsExpectedValues() {
        int nbOfItems = 5;
        IEnumerable<Integer> source = Linq.range(1, nbOfItems);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = source.select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(index);
            assertEquals(expected, item);
        }

        assertEquals(nbOfItems, index);
    }

    @Test
    public void Select_SourceIsAnArray_CurrentIsDefaultOfTAfterEnumeration() {
        int[] source = new int[]{1};
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        IEnumerator<Integer> enumerator = query.enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Select_SourceIsAList_CurrentIsDefaultOfTAfterEnumeration() {
        List<Integer> source = Arrays.asList(1);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        IEnumerator<Integer> enumerator = query.enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Select_SourceIsIReadOnlyCollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        IEnumerator<Integer> enumerator = query.enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Select_SourceIsICollection_CurrentIsDefaultOfTAfterEnumeration() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector);

        IEnumerator<Integer> enumerator = query.enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void Select_SourceIsIEnumerable_CurrentIsDefaultOfTAfterEnumeration() {
        IEnumerable<Integer> source = Linq.repeat(1, 1);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = source.select(selector);

        IEnumerator<Integer> enumerator = query.enumerator();
        while (enumerator.moveNext()) ;

        assertEquals(null, enumerator.current());
    }

    @Test
    public void SelectSelect_SourceIsAnArray_ReturnsExpectedValues() {
        Func1<Integer, Integer> selector = i -> i + 1;
        int[] source = new int[]{1, 2, 3, 4, 5};

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector).select(selector);

        int index = 0;
        for (int item : query) {
            int expected = selector.apply(selector.apply(source[index]));
            assertEquals(expected, item);
            index++;
        }

        assertEquals(source.length, index);
    }

    @Test
    public void SelectSelect_SourceIsAList_ReturnsExpectedValues() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector).select(selector);

        int index = 0;
        for (int item : query) {
            int expected = selector.apply(selector.apply(source.get(index)));
            assertEquals(expected, item);
            index++;
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void SelectSelect_SourceIsIReadOnlyCollection_ReturnsExpectedValues() {
        Collection<Integer> source = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector).select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(selector.apply(index));
            assertEquals(expected, item);
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void SelectSelect_SourceIsICollection_ReturnsExpectedValues() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = Linq.asEnumerable(source).select(selector).select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(selector.apply(index));
            assertEquals(expected, item);
        }

        assertEquals(source.size(), index);
    }

    @Test
    public void SelectSelect_SourceIsIEnumerable_ReturnsExpectedValues() {
        int nbOfItems = 5;
        IEnumerable<Integer> source = Linq.range(1, 5);
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> query = source.select(selector).select(selector);

        int index = 0;
        for (int item : query) {
            index++;
            int expected = selector.apply(selector.apply(index));
            assertEquals(expected, item);
        }

        assertEquals(nbOfItems, index);
    }

    @Test
    public void Select_SourceIsEmptyEnumerable_ReturnedCollectionHasNoElements() {
        IEnumerable<Integer> source = Linq.empty();
        ref<Boolean> wasSelectorCalled = ref.init(false);

        IEnumerable<Integer> result = source.select(i -> {
            wasSelectorCalled.value = true;
            return i + 1;
        });

        boolean hadItems = false;
        for (int item : result) {
            hadItems = true;
        }

        assertFalse(hadItems);
        assertFalse(wasSelectorCalled.value);
    }

    @Test
    public void Select_ExceptionThrownFromSelector_ExceptionPropagatedToTheCaller() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Integer> selector = i -> {
            throw new InvalidOperationException();
        };

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Select_ExceptionThrownFromSelector_IteratorCanBeUsedAfterExceptionIsCaught() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Integer> selector = i -> {
            if (i == 1)
                throw new InvalidOperationException();
            return i + 1;
        };

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
        enumerator.moveNext();
        assertEquals(3 /* 2 + 1 */, enumerator.current());
    }

    @Test
    public void Select_ExceptionThrownFromCurrentOfSourceIterator_ExceptionPropagatedToTheCaller() {
        IEnumerable<Integer> source = new ThrowsOnCurrent();
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = source.select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Select_ExceptionThrownFromCurrentOfSourceIterator_IteratorCanBeUsedAfterExceptionIsCaught() {
        IEnumerable<Integer> source = new ThrowsOnCurrent();
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = source.select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
        enumerator.moveNext();
        assertEquals(3 /* 2 + 1 */, enumerator.current());
    }

    @Test
    public void Select_ExceptionThrownFromMoveNextOfSourceIterator_ExceptionPropagatedToTheCaller() {
        IEnumerable<Integer> source = new ThrowsOnMoveNext();
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = source.select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Select_ExceptionThrownFromMoveNextOfSourceIterator_IteratorCanBeUsedAfterExceptionIsCaught() {
        IEnumerable<Integer> source = new ThrowsOnMoveNext();
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = source.select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
        enumerator.moveNext();
        assertEquals(3 /* 2 + 1 */, enumerator.current());
    }

    @Test
    public void Select_ExceptionThrownFromGetEnumeratorOnSource_ExceptionPropagatedToTheCaller() {
        IEnumerable<Integer> source = new ThrowsOnGetEnumerator();
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = source.select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Select_ExceptionThrownFromGetEnumeratorOnSource_CurrentIsSetToDefaultOfItemTypeAndIteratorCanBeUsedAfterExceptionIsCaught() {
        IEnumerable<Integer> source = new ThrowsOnGetEnumerator();
        Func1<Integer, String> selector = i -> i.toString();

        IEnumerable<String> result = source.select(selector);
        IEnumerator<String> enumerator = result.enumerator();

        assertThrows(InvalidOperationException.class, () -> enumerator.moveNext());
        String currentValue = enumerator.current();
        assertEquals(null, currentValue);

        assertTrue(enumerator.moveNext());
        assertEquals("1", enumerator.current());
    }

    @Test
    public void Select_SourceListGetsModifiedDuringIteration_ExceptionIsPropagated() {
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        assertTrue(enumerator.moveNext());
        assertEquals(2 /* 1 + 1 */, enumerator.current());

        source.add(6);
        assertThrows(ConcurrentModificationException.class, () -> enumerator.moveNext());
    }

    @Test
    public void Select_GetEnumeratorCalledTwice_DifferentInstancesReturned() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        IEnumerable<Integer> query = Linq.asEnumerable(source).select(i -> i + 1);

        IEnumerator<Integer> enumerator1 = query.enumerator();
        IEnumerator<Integer> enumerator2 = query.enumerator();

        assertSame(query, enumerator1);
        assertNotSame(enumerator1, enumerator2);

        enumerator1.close();
        enumerator2.close();
    }

    @Test
    public void Select_ResetCalledOnEnumerator_ThrowsException() {
        int[] source = new int[]{1, 2, 3, 4, 5};
        Func1<Integer, Integer> selector = i -> i + 1;

        IEnumerable<Integer> result = Linq.asEnumerable(source).select(selector);
        IEnumerator<Integer> enumerator = result.enumerator();

        // The.NET full framework throws a NotImplementedException.
        // See https://github.com/dotnet/corefx/pull/2959.
        assertThrows(NotSupportedException.class, () -> enumerator.reset());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).select(i -> i);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIndexed() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).select((e, i) -> i);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateArray() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).toArray().select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateList() {
        IEnumerable<Integer> iterator = Linq.asEnumerable(NumberRangeGuaranteedNotCollectionType(0, 3).toList()).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIList() {
        IEnumerable<Integer> iterator = Linq.asEnumerable(Collections.unmodifiableCollection(NumberRangeGuaranteedNotCollectionType(0, 3).toList())).select(i -> i);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateIPartition() {
        IEnumerable<Integer> iterator = Linq.asEnumerable(Collections.unmodifiableCollection(NumberRangeGuaranteedNotCollectionType(0, 3).toList())).select(i -> i).skip(1);
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void Select_SourceIsArray_Count() {
        int[] source = new int[]{1, 2, 3, 4};
        assertEquals(source.length, Linq.asEnumerable(source).select(i -> i * 2).count());
    }

    @Test
    public void Select_SourceIsAList_Count() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4);
        assertEquals(source.size(), Linq.asEnumerable(source).select(i -> i * 2).count());
    }

    @Test
    public void Select_SourceIsAnIList_Count() {
        Collection<Integer> souce = Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4));
        assertEquals(souce.size(), Linq.asEnumerable(souce).select(i -> i * 2).count());
    }

    @Test
    public void Select_SourceIsArray_Skip() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2).skip(-1));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(1).skip(1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.skip(-1));
        assertEmpty(source.skip(4));
        assertEmpty(source.skip(20));
    }

    @Test
    public void Select_SourceIsList_Skip() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2).skip(-1));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(1).skip(1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.skip(-1));
        assertEmpty(source.skip(4));
        assertEmpty(source.skip(20));
    }

    @Test
    public void Select_SourceIsIList_Skip() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(2).skip(-1));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), source.skip(1).skip(1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.skip(-1));
        assertEmpty(source.skip(4));
        assertEmpty(source.skip(20));
    }

    @Test
    public void Select_SourceIsArray_Take() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(2));
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(3).take(2));
        assertEmpty(source.take(-1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(4));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(40));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(1));
        assertEquals(Linq.asEnumerable(new int[]{4}), source.skip(1).take(1));
        assertEquals(Linq.asEnumerable(new int[]{6}), source.take(3).skip(2));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(3).take(1));
    }

    @Test
    public void Select_SourceIsList_Take() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(2));
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(3).take(2));
        assertEmpty(source.take(-1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(4));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(40));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(1));
        assertEquals(Linq.asEnumerable(new int[]{4}), source.skip(1).take(1));
        assertEquals(Linq.asEnumerable(new int[]{6}), source.take(3).skip(2));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(3).take(1));
    }

    @Test
    public void Select_SourceIsIList_Take() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(2));
        assertEquals(Linq.asEnumerable(new int[]{2, 4}), source.take(3).take(2));
        assertEmpty(source.take(-1));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(4));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), source.take(40));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(1));
        assertEquals(Linq.asEnumerable(new int[]{4}), source.skip(1).take(1));
        assertEquals(Linq.asEnumerable(new int[]{6}), source.take(3).skip(2));
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(3).take(1));
    }

    @Test
    public void Select_SourceIsArray_ElementAt() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAt(i));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(4));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(40));

        assertEquals(6, source.skip(1).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.skip(2).elementAt(9));
    }

    @Test
    public void Select_SourceIsList_ElementAt() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAt(i));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(4));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(40));

        assertEquals(6, source.skip(1).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.skip(2).elementAt(9));
    }

    @Test
    public void Select_SourceIsIList_ElementAt() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAt(i));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(4));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(40));

        assertEquals(6, source.skip(1).elementAt(1));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.skip(2).elementAt(9));
    }

    @Test
    public void Select_SourceIsArray_ElementAtOrDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAtOrDefault(i));
        assertEquals(null, source.elementAtOrDefault(-1));
        assertEquals(null, source.elementAtOrDefault(4));
        assertEquals(null, source.elementAtOrDefault(40));

        assertEquals(6, source.skip(1).elementAtOrDefault(1));
        assertEquals(null, source.skip(2).elementAtOrDefault(9));
    }

    @Test
    public void Select_SourceIsList_ElementAtOrDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAtOrDefault(i));
        assertEquals(null, source.elementAtOrDefault(-1));
        assertEquals(null, source.elementAtOrDefault(4));
        assertEquals(null, source.elementAtOrDefault(40));

        assertEquals(6, source.skip(1).elementAtOrDefault(1));
        assertEquals(null, source.skip(2).elementAtOrDefault(9));
    }

    @Test
    public void Select_SourceIsIList_ElementAtOrDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        for (int i = 0; i != 4; ++i)
            assertEquals(i * 2 + 2, source.elementAtOrDefault(i));
        assertEquals(null, source.elementAtOrDefault(-1));
        assertEquals(null, source.elementAtOrDefault(4));
        assertEquals(null, source.elementAtOrDefault(40));

        assertEquals(6, source.skip(1).elementAtOrDefault(1));
        assertEquals(null, source.skip(2).elementAtOrDefault(9));
    }

    @Test
    public void Select_SourceIsArray_First() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        assertEquals(2, source.first());
        assertEquals(2, source.firstOrDefault());

        assertEquals(6, source.skip(2).first());
        assertEquals(6, source.skip(2).firstOrDefault());
        assertThrows(InvalidOperationException.class, () -> source.skip(4).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(14).first());
        assertEquals(null, source.skip(4).firstOrDefault());
        assertEquals(null, source.skip(14).firstOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(new int[0]).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.first());
        assertEquals(null, empty.firstOrDefault());
    }

    @Test
    public void Select_SourceIsList_First() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        assertEquals(2, source.first());
        assertEquals(2, source.firstOrDefault());

        assertEquals(6, source.skip(2).first());
        assertEquals(6, source.skip(2).firstOrDefault());
        assertThrows(InvalidOperationException.class, () -> source.skip(4).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(14).first());
        assertEquals(null, source.skip(4).firstOrDefault());
        assertEquals(null, source.skip(14).firstOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(new ArrayList<Integer>()).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.first());
        assertEquals(null, empty.firstOrDefault());
    }

    @Test
    public void Select_SourceIsIList_First() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        assertEquals(2, source.first());
        assertEquals(2, source.firstOrDefault());

        assertEquals(6, source.skip(2).first());
        assertEquals(6, source.skip(2).firstOrDefault());
        assertThrows(InvalidOperationException.class, () -> source.skip(4).first());
        assertThrows(InvalidOperationException.class, () -> source.skip(14).first());
        assertEquals(null, source.skip(4).firstOrDefault());
        assertEquals(null, source.skip(14).firstOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(Collections.unmodifiableCollection(new ArrayList<Integer>())).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.first());
        assertEquals(null, empty.firstOrDefault());
    }

    @Test
    public void Select_SourceIsArray_Last() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2);
        assertEquals(8, source.last());
        assertEquals(8, source.lastOrDefault());

        assertEquals(6, source.take(3).last());
        assertEquals(6, source.take(3).lastOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(new int[0]).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.last());
        assertEquals(null, empty.lastOrDefault());
        assertThrows(InvalidOperationException.class, () -> empty.skip(1).last());
        assertEquals(null, empty.skip(1).lastOrDefault());
    }

    @Test
    public void Select_SourceIsList_Last() {
        IEnumerable<Integer> source = Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2);
        assertEquals(8, source.last());
        assertEquals(8, source.lastOrDefault());

        assertEquals(6, source.take(3).last());
        assertEquals(6, source.take(3).lastOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(new ArrayList<Integer>()).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.last());
        assertEquals(null, empty.lastOrDefault());
        assertThrows(InvalidOperationException.class, () -> empty.skip(1).last());
        assertEquals(null, empty.skip(1).lastOrDefault());
    }

    @Test
    public void Select_SourceIsIList_Last() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList(1, 2, 3, 4))).select(i -> i * 2);
        assertEquals(8, source.last());
        assertEquals(8, source.lastOrDefault());

        assertEquals(6, source.take(3).last());
        assertEquals(6, source.take(3).lastOrDefault());

        IEnumerable<Integer> empty = Linq.asEnumerable(Collections.unmodifiableCollection(new ArrayList<Integer>())).select(i -> i * 2);
        assertThrows(InvalidOperationException.class, () -> empty.last());
        assertEquals(null, empty.lastOrDefault());
        assertThrows(InvalidOperationException.class, () -> empty.skip(1).last());
        assertEquals(null, empty.skip(1).lastOrDefault());
    }

    @Test
    public void Select_SourceIsArray_SkipRepeatCalls() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2).skip(1);
        assertEquals(source, source);
    }

    @Test
    public void Select_SourceIsArraySkipSelect() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2).skip(1).select(i -> i + 1);
        assertEquals(Linq.asEnumerable(new int[]{5, 7, 9}), source);
    }

    @Test
    public void Select_SourceIsArrayTakeTake() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{1, 2, 3, 4}).select(i -> i * 2).take(2).take(1);
        assertEquals(Linq.asEnumerable(new int[]{2}), source);
        assertEquals(Linq.asEnumerable(new int[]{2}), source.take(10));
    }

    @Test
    public void Select_SourceIsListSkipTakeCount() {
        assertEquals(3, Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(3).count());
        assertEquals(4, Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(9).count());
        assertEquals(2, Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(2).count());
        assertEquals(0, Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(8).count());
    }

    @Test
    public void Select_SourceIsListSkipTakeToArray() {
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6}), Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(3).toArray());
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(9).toArray());
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(2).toArray());
        assertEmpty(Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(8).toArray());
    }

    @Test
    public void Select_SourceIsListSkipTakeToList() {
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6}), Linq.asEnumerable(Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(3).toList()));
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8}), Linq.asEnumerable(Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).take(9).toList()));
        assertEquals(Linq.asEnumerable(new int[]{6, 8}), Linq.asEnumerable(Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(2).toList()));
        assertEmpty(Linq.asEnumerable(Linq.asEnumerable(Arrays.asList(1, 2, 3, 4)).select(i -> i * 2).skip(8).toList()));
    }

    @Test
    public void MoveNextAfterDispose() {
        for (Object[] objects : this.MoveNextAfterDisposeData()) {
            this.MoveNextAfterDispose((IEnumerable<Integer>) objects[0]);
        }
    }

    private void MoveNextAfterDispose(IEnumerable<Integer> source) {
        // Select is specialized for a bunch of different types, so we want
        // to make sure this holds true for all of them.
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> identityTransforms = Arrays.asList(
                e -> e,
                e -> ForceNotCollection(e),
                e -> e.toArray(),
                e -> Linq.asEnumerable(e.toList()),
                e -> Linq.asEnumerable(new LinkedList<>(e.toList())), // IList<T> that's not a List
                e -> e.select(i -> i) // Multiple Select() chains are optimized
        );

        for (IEnumerable<Integer> equivalentSource : Linq.asEnumerable(identityTransforms).select(t -> t.apply(source))) {
            IEnumerable<Integer> result = equivalentSource.select(i -> i);
            try (IEnumerator<Integer> e = result.enumerator()) {
                while (e.moveNext()) ; // Loop until we reach the end of the iterator, @ which pt it gets disposed.
                assertFalse(e.moveNext()); // MoveNext should not throw an exception after Dispose.
            }
        }
    }

    private IEnumerable<Object[]> MoveNextAfterDisposeData() {
        List<Object[]> lst = new ArrayList<>();
        lst.add(new Object[]{Linq.asEnumerable(new int[0])});
        lst.add(new Object[]{Linq.asEnumerable(new int[1])});
        lst.add(new Object[]{Linq.range(1, 30)});
        return Linq.asEnumerable(lst);
    }

    @Test
    public void RunSelectorDuringCount() {
        for (Object[] objects : this.RunSelectorDuringCountData()) {
            this.RunSelectorDuringCount((IEnumerable<Integer>) objects[0]);
        }
    }

    private void RunSelectorDuringCount(IEnumerable<Integer> source) {
        ref<Integer> timesRun = ref.init(0);
        IEnumerable<Integer> selected = source.select(i -> timesRun.value++);
        selected.count();

        assertEquals(source.count(), timesRun.value);
    }

    //@Test
    public void RunSelectorDuringPartitionCount() {
        for (Object[] objects : this.RunSelectorDuringCountData()) {
            this.RunSelectorDuringPartitionCount((IEnumerable<Integer>) objects[0]);
        }
    }

    private void RunSelectorDuringPartitionCount(IEnumerable<Integer> source) {
        ref<Integer> timesRun = ref.init(0);

        IEnumerable<Integer> selected = source.select(i -> timesRun.value++);

        if (source.any()) {
            selected.skip(1).count();
            assertEquals(source.count() - 1, timesRun.value);

            selected.take(source.count() - 1).count();
            assertEquals(source.count() * 2 - 2, timesRun.value);
        }
    }

    private IEnumerable<Object[]> RunSelectorDuringCountData() {
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> transforms = Arrays.asList(
                e -> e,
                e -> ForceNotCollection(e),
                e -> ForceNotCollection(e).skip(1),
                e -> ForceNotCollection(e).where(i -> true),
                e -> e.toArray().where(i -> true),
                e -> Linq.asEnumerable(e.toList()).where(i -> true),
                e -> Linq.asEnumerable(new LinkedList<>(e.toList())).where(i -> true),
                e -> e.select(i -> i),
                e -> e.take(e.count()),
                e -> e.toArray(),
                e -> Linq.asEnumerable(e.toList()),
                e -> Linq.asEnumerable(new LinkedList<>(e.toList())) // Implements IList<T>.
        );

        Random r = new Random(0x984bf1a3);

        List<Object[]> lst = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            IEnumerable<Integer> enumerable = Linq.range(1, i).select(a -> r.nextInt());
            for (Func1<IEnumerable<Integer>, IEnumerable<Integer>> transform : transforms) {
                lst.add(new Object[]{transform.apply(enumerable)});
            }
        }
        return Linq.asEnumerable(lst);
    }

    @Test
    public void testSelectIndexed() {
        List<String> names = Linq.asEnumerable(emps)
                .select((emp, index) -> emp.name)
                .toList();
        assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<String> indexes = Linq.asEnumerable(emps)
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        assertEquals("[#0: Fred, #1: Bill, #2: Eric, #3: Janet]", indexes.toString());
    }

    /// <summary>
    /// Test enumerator - throws InvalidOperationException from Current after MoveNext called once.
    /// </summary>
    private static class ThrowsOnCurrent extends TestEnumerator {
        @Override
        public Integer current() {
            int current = super.current();
            if (current == 1)
                throw new InvalidOperationException();
            return current;
        }
    }

    private static class CustInfo extends ValueType {
        private final String name;
        private final int custId;

        CustInfo(String name, int custId) {
            this.name = name;
            this.custId = custId;
        }
    }
}
