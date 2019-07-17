package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IArrayList;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.util.Dictionary;
import com.bestvike.out;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class GroupByTest extends TestCase {
    private static <TKey, TElement> void AssertGroupingCorrect(IEnumerable<TKey> keys, IEnumerable<TElement> elements, IEnumerable<IGrouping<TKey, TElement>> grouping) {
        AssertGroupingCorrect(keys, elements, grouping, EqualityComparer.Default());
    }

    private static <TKey, TElement> void AssertGroupingCorrect(IEnumerable<TKey> keys, IEnumerable<TElement> elements, IEnumerable<IGrouping<TKey, TElement>> grouping, IEqualityComparer<TKey> keyComparer) {
        if (grouping == null) {
            assertNull(elements);
            assertNull(keys);
            return;
        }

        assertNotNull(elements);
        assertNotNull(keys);

        Dictionary<TKey, List<TElement>> dict = new Dictionary<>(keyComparer);
        List<TElement> groupingForNullKeys = new ArrayList<>();
        try (IEnumerator<TElement> elEn = elements.enumerator();
             IEnumerator<TKey> keyEn = keys.enumerator()) {
            while (keyEn.moveNext()) {
                assertTrue(elEn.moveNext());

                TKey key = keyEn.current();

                if (key == null) {
                    groupingForNullKeys.add(elEn.current());
                } else {
                    out<List<TElement>> list = out.init();
                    if (!dict.tryGetValue(key, list))
                        dict.put(key, list.value = new ArrayList<>());
                    list.value.add(elEn.current());
                }
            }
            assertFalse(elEn.moveNext());
        }
        for (IGrouping<TKey, TElement> group : grouping) {
            assertNotEmpty(group);
            TKey key = group.getKey();
            out<List<TElement>> list = out.init();

            if (key == null) {
                assertEquals(Linq.of(groupingForNullKeys), group);
                groupingForNullKeys.clear();
            } else {
                assertTrue(dict.tryGetValue(key, list));
                assertEquals(Linq.of(list.value), group);
                dict.remove(key);
            }
        }
        assertEmpty(Linq.of(dict));
        assertEmpty(Linq.of(groupingForNullKeys));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.of("Alen", "Felix", null, null, "X", "Have Space", "Clinton", "");
        IEnumerable<Integer> q2 = Linq.of(new int[]{55, 49, 9, -100, 24, 25, -1, 0});


        IEnumerable<Tuple2<String, Integer>> q = q1.zip(q2);

        assertNotNull(q.groupBy(Tuple2::getItem1, Tuple2::getItem2));
        assertEquals(q.groupBy(Tuple2::getItem1, Tuple2::getItem2), q.groupBy(Tuple2::getItem1, Tuple2::getItem2));
    }

    @Test
    public void Grouping_IList_IsReadOnly() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        for (IGrouping grouping : oddsEvens) {
            assertTrue(grouping instanceof IArrayList);
        }
    }

    @Test
    public void Grouping_IList_NotSupported() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        for (IGrouping grouping : oddsEvens) {
            assertTrue(grouping instanceof IArrayList);
        }
    }

    @Test
    public void Grouping_IList_IndexerGetter() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        IEnumerator<IGrouping<Boolean, Integer>> e = oddsEvens.enumerator();

        assertTrue(e.moveNext());
        IArrayList<Integer> odds = (IArrayList<Integer>) e.current();
        assertEquals(1, odds.get(0));
        assertEquals(3, odds.get(1));

        assertTrue(e.moveNext());
        IArrayList<Integer> evens = (IArrayList<Integer>) e.current();
        assertEquals(2, evens.get(0));
        assertEquals(4, evens.get(1));
    }

    @Test
    public void Grouping_IList_IndexGetterOutOfRange() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        IEnumerator<IGrouping<Boolean, Integer>> e = oddsEvens.enumerator();

        assertTrue(e.moveNext());
        IArrayList<Integer> odds = (IArrayList<Integer>) e.current();
        assertThrows(ArgumentOutOfRangeException.class, () -> odds.get(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> odds.get(23));
    }

    @Test
    public void Grouping_ICollection_Contains() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        IEnumerator<IGrouping<Boolean, Integer>> e = oddsEvens.enumerator();

        assertTrue(e.moveNext());
        ICollection<Integer> odds = (IArrayList<Integer>) e.current();
        assertTrue(odds.contains(1));
        assertTrue(odds.contains(3));
        assertFalse(odds.contains(2));
        assertFalse(odds.contains(4));

        assertTrue(e.moveNext());
        ICollection<Integer> evens = (IArrayList<Integer>) e.current();
        assertTrue(evens.contains(2));
        assertTrue(evens.contains(4));
        assertFalse(evens.contains(1));
        assertFalse(evens.contains(3));
    }

    @Test
    public void Grouping_IList_IndexOf() {
        IEnumerable<IGrouping<Boolean, Integer>> oddsEvens = Linq.of(new int[]{1, 2, 3, 4}).groupBy(i -> i % 2 == 0);
        IEnumerator<IGrouping<Boolean, Integer>> e = oddsEvens.enumerator();

        assertTrue(e.moveNext());
        IArrayList<Integer> odds = (IArrayList<Integer>) e.current();
        assertEquals(0, odds.toList().indexOf(1));
        assertEquals(1, odds.toList().indexOf(3));
        assertEquals(-1, odds.toList().indexOf(2));
        assertEquals(-1, odds.toList().indexOf(4));

        assertTrue(e.moveNext());
        IArrayList<Integer> evens = (IArrayList<Integer>) e.current();
        assertEquals(0, evens.toList().indexOf(2));
        assertEquals(1, evens.toList().indexOf(4));
        assertEquals(-1, evens.toList().indexOf(1));
        assertEquals(-1, evens.toList().indexOf(3));
    }

    @Test
    public void SingleNullKeySingleNullElement() {
        String[] key = {null};
        String[] element = {null};

        AssertGroupingCorrect(Linq.of(key), Linq.of(element), Linq.of(new String[]{null}).groupBy(e -> e, e -> e, EqualityComparer.Default()), EqualityComparer.Default());
    }

    @Test
    public void EmptySource() {
        assertEmpty(Linq.<Record>of().groupBy(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
    }

    @Test
    public void EmptySourceRunOnce() {
        assertEmpty(Linq.<Record>of().runOnce().groupBy(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
    }

    @Test
    public void SourceIsNull() {
        IEnumerable<Record> source = null;
        assertThrows(NullPointerException.class, () -> source.groupBy(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
        assertThrows(NullPointerException.class, () -> source.groupBy(e -> e.Name, new AnagramEqualityComparer()));
    }

    @Test
    public void SourceIsNullResultSelectorUsed() {
        IEnumerable<Record> source = null;
        assertThrows(NullPointerException.class, () -> source.groupBy(e -> e.Name, e -> e.Score, (k, es) -> es.sumInt(), new AnagramEqualityComparer()));
    }

    @Test
    public void SourceIsNullResultSelectorUsedNoComparer() {
        IEnumerable<Record> source = null;
        assertThrows(NullPointerException.class, () -> source.groupBy(e -> e.Name, e -> e.Score, (k, es) -> es.sumInt()));
    }

    @Test
    public void SourceIsNullResultSelectorUsedNoComparerOrElementSelector() {
        IEnumerable<Record> source = null;
        assertThrows(NullPointerException.class, () -> source.groupBy(e -> e.Name, (k, es) -> es.sumInt(e -> e.Score)));
    }

    @Test
    public void KeySelectorNull() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(null, e -> e.Score, new AnagramEqualityComparer()));
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(null, new AnagramEqualityComparer()));
    }

    @Test
    public void KeySelectorNullResultSelectorUsed() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(null, e -> e.Score, (k, es) -> es.sumInt(), new AnagramEqualityComparer()));
    }

    @Test
    public void KeySelectorNullResultSelectorUsedNoComparer() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func1<Record, String> keySelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(keySelector, e -> e.Score, (k, es) -> es.sumInt()));
    }

    @Test
    public void KeySelectorNullResultSelectorUsedNoElementSelector() {
        String[] key = {"Tim", "Tim", "Tim", "Tim"};
        int[] element = {60, -10, 40, 100};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(null, (k, es) -> es.sumInt(e -> e.Score), new AnagramEqualityComparer()));
    }

    @Test
    public void ElementSelectorNull() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func1<Record, Integer> elementSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, elementSelector, new AnagramEqualityComparer()));
    }

    @Test
    public void ElementSelectorNullResultSelectorUsedNoComparer() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func1<Record, Integer> elementSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, elementSelector, (k, es) -> es.sumInt()));
    }

    @Test
    public void ResultSelectorNull() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func2<String, IEnumerable<Integer>, Long> resultSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, e -> e.Score, resultSelector, new AnagramEqualityComparer()));
    }

    @Test
    public void ResultSelectorNullNoComparer() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func2<String, IEnumerable<Integer>, Long> resultSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, e -> e.Score, resultSelector));
    }

    @Test
    public void ResultSelectorNullNoElementSelector() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Func2<String, IEnumerable<Record>, Long> resultSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, resultSelector));
    }

    @Test
    public void ResultSelectorNullNoElementSelectorCustomComparer() {
        String[] key = {"Tim", "Tim", "Tim", "Tim"};
        int[] element = {60, -10, 40, 100};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        Func2<String, IEnumerable<Record>, Long> resultSelector = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(source).groupBy(e -> e.Name, resultSelector, new AnagramEqualityComparer()));
    }

    @Test
    public void EmptySourceWithResultSelector() {
        assertEmpty(Linq.<Record>of().groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(), new AnagramEqualityComparer()));
    }

    @Test
    public void DuplicateKeysCustomComparer() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("miT", 25)};

        long[] expected = {240, 365, -600, 63};

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(), new AnagramEqualityComparer()));
    }

    @Test
    public void DuplicateKeysCustomComparerRunOnce() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("miT", 25)};
        long[] expected = {240, 365, -600, 63};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(), new AnagramEqualityComparer()));
    }

    @Test
    public void NullComparer() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record(null, 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record(null, 9),
                new Record("miT", 25)};
        long[] expected = {165, 58, -600, 120, 75};

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(), null));
    }

    @Test
    public void NullComparerRunOnce() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record(null, 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record(null, 9),
                new Record("miT", 25)};
        long[] expected = {165, 58, -600, 120, 75};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(), null));
    }

    @Test
    public void SingleNonNullElement() {
        String[] key = {"Tim"};
        Record[] source = {new Record(key[0], 60)};

        AssertGroupingCorrect(Linq.of(key), Linq.of(source), Linq.of(source).groupBy(e -> e.Name));
    }

    @Test
    public void AllElementsSameKey() {
        String[] key = {"Tim", "Tim", "Tim", "Tim"};
        int[] scores = {60, -10, 40, 100};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(scores), Record::new);

        AssertGroupingCorrect(Linq.of(key), Linq.of(source), Linq.of(source).groupBy(e -> e.Name, new AnagramEqualityComparer()), new AnagramEqualityComparer());
    }

    @Test
    public void AllElementsDifferentKeyElementSelectorUsed() {
        String[] key = {"Tim", "Chris", "Robert", "Prakash"};
        int[] element = {60, -10, 40, 100};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        AssertGroupingCorrect(Linq.of(key), Linq.of(element), Linq.of(source).groupBy(e -> e.Name, e -> e.Score));
    }

    @Test
    public void SomeDuplicateKeys() {
        String[] key = {"Tim", "Tim", "Chris", "Chris", "Robert", "Prakash"};
        int[] element = {55, 25, 49, 24, -100, 9};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        AssertGroupingCorrect(Linq.of(key), Linq.of(element), Linq.of(source).groupBy(e -> e.Name, e -> e.Score));
    }

    @Test
    public void SomeDuplicateKeysIncludingNulls() {
        String[] key = {null, null, "Chris", "Chris", "Prakash", "Prakash"};
        int[] element = {55, 25, 49, 24, 9, 9};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        AssertGroupingCorrect(Linq.of(key), Linq.of(element), Linq.of(source).groupBy(e -> e.Name, e -> e.Score));
    }

    @Test
    public void SingleElementResultSelectorUsed() {
        String[] key = {"Tim"};
        int[] element = {60};
        long[] expected = {180};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt(e -> e.Score)));
    }

    @Test
    public void GroupedResultCorrectSize() {
        IEnumerable<Character> elements = Linq.repeat('q', 5);

        IEnumerable<Tuple2<Character, IEnumerable<Character>>> result = elements.groupBy(e -> e, (Func2<Character, IEnumerable<Character>, Tuple2<Character, IEnumerable<Character>>>) Tuple::create);

        assertEquals(1, result.count());

        Tuple2<Character, IEnumerable<Character>> grouping = result.first();

        assertEquals(5, grouping.getItem2().count());
        assertEquals('q', grouping.getItem1());
        assertTrue(grouping.getItem2().all(e -> e == 'q'));
    }

    @Test
    public void AllElementsDifferentKeyElementSelectorUsedResultSelector() {
        String[] key = {"Tim", "Chris", "Robert", "Prakash"};
        int[] element = {60, -10, 40, 100};
        IEnumerable<Record> source = Linq.of(key).zip(Linq.of(element), Record::new);
        long[] expected = {180, -50, 240, 700};

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, e -> e.Score, (k, es) -> (long) (k == null ? " " : k).length() * es.sumInt()));
    }

    @Test
    public void AllElementsSameKeyResultSelectorUsed() {
        int[] element = {60, -10, 40, 100};
        long[] expected = {570};
        Record[] source = new Record[]{
                new Record("Tim", element[0]),
                new Record("Tim", element[1]),
                new Record("miT", element[2]),
                new Record("miT", element[3])
        };

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, (k, es) -> k.length() * es.sumLong(e -> (long) e.Score), new AnagramEqualityComparer()));
    }

    @Test
    public void NullComparerResultSelectorUsed() {
        int[] element = {60, -10, 40, 100};
        Record[] source = {
                new Record("Tim", element[0]),
                new Record("Tim", element[1]),
                new Record("miT", element[2]),
                new Record("miT", element[3]),
        };

        long[] expected = {150, 420};

        assertEquals(Linq.of(expected), Linq.of(source).groupBy(e -> e.Name, (k, es) -> k.length() * es.sumLong(e -> (long) e.Score), null));
    }

    @Test
    public void GroupingToArray() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Array<IGrouping<String, Record>> groupedArray = Linq.of(source).groupBy(r -> r.Name).toArray();
        assertEquals(4, groupedArray._getCount());
        assertEquals(Linq.of(source).groupBy(r -> r.Name), groupedArray);
    }

    @Test
    public void GroupingWithElementSelectorToArray() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Array<IGrouping<String, Integer>> groupedArray = Linq.of(source).groupBy(r -> r.Name, e -> e.Score).toArray();
        assertEquals(4, groupedArray._getCount());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, e -> e.Score), groupedArray);
    }

    @Test
    public void GroupingWithResultsToArray() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Array<IEnumerable<Record>> groupedArray = Linq.of(source).groupBy(r -> r.Name, (r, e) -> e).toArray();
        assertEquals(4, groupedArray._getCount());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, (r, e) -> e), groupedArray);
    }

    @Test
    public void GroupingWithElementSelectorAndResultsToArray() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        Array<IEnumerable<Record>> groupedArray = Linq.of(source).groupBy(r -> r.Name, e -> e, (r, e) -> e).toArray();
        assertEquals(4, groupedArray._getCount());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, e -> e, (r, e) -> e), groupedArray);
    }

    @Test
    public void GroupingToList() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        List<IGrouping<String, Record>> groupedList = Linq.of(source).groupBy(r -> r.Name).toList();
        assertEquals(4, groupedList.size());
        assertEquals(Linq.of(source).groupBy(r -> r.Name), Linq.of(groupedList));
    }

    @Test
    public void GroupingWithElementSelectorToList() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        List<IGrouping<String, Integer>> groupedList = Linq.of(source).groupBy(r -> r.Name, e -> e.Score).toList();
        assertEquals(4, groupedList.size());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, e -> e.Score), Linq.of(groupedList));
    }

    @Test
    public void GroupingWithResultsToList() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        List<IEnumerable<Record>> groupedList = Linq.of(source).groupBy(r -> r.Name, (r, e) -> e).toList();
        assertEquals(4, groupedList.size());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, (r, e) -> e), Linq.of(groupedList));
    }

    @Test
    public void GroupingWithElementSelectorAndResultsToList() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        List<IEnumerable<Record>> groupedList = Linq.of(source).groupBy(r -> r.Name, e -> e, (r, e) -> e).toList();
        assertEquals(4, groupedList.size());
        assertEquals(Linq.of(source).groupBy(r -> r.Name, e -> e, (r, e) -> e), Linq.of(groupedList));
    }

    @Test
    public void GroupingCount() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertEquals(4, Linq.of(source).groupBy(r -> r.Name).count());
    }

    @Test
    public void GroupingWithElementSelectorCount() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertEquals(4, Linq.of(source).groupBy(r -> r.Name, e -> e.Score).count());
    }

    @Test
    public void GroupingWithResultsCount() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertEquals(4, Linq.of(source).groupBy(r -> r.Name, (r, e) -> e).count());
    }

    @Test
    public void GroupingWithElementSelectorAndResultsCount() {
        Record[] source = new Record[]{
                new Record("Tim", 55),
                new Record("Chris", 49),
                new Record("Robert", -100),
                new Record("Chris", 24),
                new Record("Prakash", 9),
                new Record("Tim", 25)};

        assertEquals(4, Linq.of(source).groupBy(r -> r.Name, e -> e, (r, e) -> e).count());
    }

    @Test
    public void EmptyGroupingToArray() {
        assertEmpty(Linq.<Integer>empty().groupBy(i -> i).toArray());
    }

    @Test
    public void EmptyGroupingToList() {
        assertEmpty(Linq.of(Linq.<Integer>empty().groupBy(i -> i).toList()));
    }

    @Test
    public void EmptyGroupingCount() {
        assertEquals(0, Linq.<Integer>empty().groupBy(i -> i).count());
    }

    @Test
    public void EmptyGroupingWithResultToArray() {
        assertEmpty(Linq.<Integer>empty().groupBy(i -> i, (x, y) -> x + y.count()).toArray());
    }

    @Test
    public void EmptyGroupingWithResultToList() {
        assertEmpty(Linq.of(Linq.<Integer>empty().groupBy(i -> i, (x, y) -> x + y.count()).toList()));
    }

    @Test
    public void EmptyGroupingWithResultCount() {
        assertEquals(0, Linq.<Integer>empty().groupBy(i -> i, (x, y) -> x + y.count()).count());
    }

    @Test
    public void GroupingKeyIsPublic() throws NoSuchMethodException {
        // Grouping.Key needs to be public (not explicitly implemented) for the sake of WPF.

        Object[] objs = {"Foo", BigDecimal.valueOf(1), "Bar", new Tmp("X"), BigDecimal.valueOf(2)};
        Object group = Linq.of(objs).groupBy(Object::getClass).first();

        Class<?> grouptype = group.getClass();
        Method key = grouptype.getMethod("getKey");
        assertNotNull(key);
    }

    @Test
    public void testGroupByWithKeySelector() {
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno)
                .select(group -> String.format("%s: %s", group.getKey(), join(group.select(element -> element.name))))
                .toList()
                .toString();
        assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };

        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), join(group.select(element -> element.name))))
                .toList()
                .toString();
        assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelector() {
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name)
                .select(group -> String.format("%s: %s", group.getKey(), join(group)))
                .toList()
                .toString();
        assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), join(group)))
                .toList()
                .toString();
        assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelector() {
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno, (key, group) -> String.format("%s: %s", key, join(group.select(element -> element.name))))
                .toList()
                .toString();
        assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno,
                        (key, group) -> String.format("%s: %s", key, join(group.select(element -> element.name))),
                        comparer)
                .toList()
                .toString();
        assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelector() {
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name, (key, group) -> String.format("%s: %s", key, join(group)))
                .toList()
                .toString();
        assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelectorAndComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        String s = Linq.of(emps)
                .groupBy(emp -> emp.deptno,
                        emp -> emp.name,
                        (key, group) -> String.format("%s: %s", key, join(group)),
                        comparer)
                .toList()
                .toString();
        assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    //struct
    private static final class Record extends ValueType {
        final String Name;
        final int Score;

        Record(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    //struct
    private static final class Tmp extends ValueType {
        final String X;

        Tmp(String x) {
            this.X = x;
        }
    }
}
