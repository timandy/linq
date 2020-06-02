package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.function.Action1;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
class ToLinkedMapTest extends TestCase {
    private static <K, E> void AssertMatches(IEnumerable<K> keys, IEnumerable<E> values, Map<K, E> dict) {
        assertNotNull(dict);
        assertNotNull(keys);
        assertNotNull(values);
        assertSame(LinkedHashMap.class, dict.getClass());
        try (IEnumerator<K> ke = keys.enumerator()) {
            for (E value : values) {
                assertTrue(ke.moveNext());
                K key = ke.current();
                assertTrue(dict.containsKey(key));
                E dictValue = dict.get(key);
                assertEquals(value, dictValue);
                dict.remove(key);
            }
            assertFalse(ke.moveNext());
            assertEquals(0, dict.size());
        }
    }

    private static <T> void RunToDictionaryOnAllCollectionTypes(T[] items, Action1<Map<T, T>> validation) {
        validation.apply(Linq.of(items).toLinkedMap(key -> key));
        validation.apply(Linq.of(items).toLinkedMap(key -> key, value -> value));
        validation.apply(Linq.of(items).toArray().toLinkedMap(key -> key));
        validation.apply(Linq.of(items).toArray().toLinkedMap(key -> key, value -> value));
        validation.apply(new TestEnumerable<>(items).toLinkedMap(key -> key));
        validation.apply(new TestEnumerable<>(items).toLinkedMap(key -> key, value -> value));
        validation.apply(new TestReadOnlyCollection<>(items).toLinkedMap(key -> key));
        validation.apply(new TestReadOnlyCollection<>(items).toLinkedMap(key -> key, value -> value));
        validation.apply(new TestCollection<>(items).toLinkedMap(key -> key));
        validation.apply(new TestCollection<>(items).toLinkedMap(key -> key, value -> value));
    }

    @Test
    void ToDictionary_AlwaysCreateACopy() {
        Map<Integer, Integer> source = new LinkedHashMap<>();
        source.put(1, 1);
        source.put(2, 2);
        source.put(3, 3);
        Map<Integer, Integer> result = Linq.of(source).toLinkedMap(key -> key.getKey(), val -> val.getValue());

        assertIsType(LinkedHashMap.class, result);
        assertNotSame(source, result);
        assertEquals(source, result);
    }

    @Test
    void ToDictionary_WorkWithEmptyCollection() {
        RunToDictionaryOnAllCollectionTypes(new Integer[0], resultDictionary -> {
            assertNotNull(resultDictionary);
            assertEquals(0, resultDictionary.size());
        });
    }

    @Test
    void ToDictionary_ProduceCorrectDictionary() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        RunToDictionaryOnAllCollectionTypes(sourceArray, resultDictionary -> {
            assertEquals(sourceArray.length, resultDictionary.size());
            assertEquals(Linq.of(sourceArray), Linq.of(resultDictionary.keySet()));
            assertEquals(Linq.of(sourceArray), Linq.of(resultDictionary.values()));
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        RunToDictionaryOnAllCollectionTypes(sourceStringArray, resultDictionary -> {
            assertEquals(sourceStringArray.length, resultDictionary.size());
            for (int i = 0; i < sourceStringArray.length; i++)
                assertSame(sourceStringArray[i], resultDictionary.get(sourceStringArray[i]));
        });
    }

    @Test
    void RunOnce() {
        Map<Integer, String> expect = new LinkedHashMap<>();
        expect.put(1, "0");
        expect.put(2, "1");
        expect.put(3, "2");
        expect.put(4, "3");

        assertEquals(expect,
                Linq.range(0, 4).runOnce().toLinkedMap(i -> i + 1, i -> i.toString()));
    }

    @Test
    void ToDictionary_PassCustomComparer() {
        TestCollection<Integer> collection = new TestCollection<>(new Integer[]{1, 2, 3, 4, 5, 6});
        Map<Integer, Integer> result1 = collection.toLinkedMap(key -> key);
        Map<Integer, Integer> result2 = collection.toLinkedMap(key -> key, val -> val);

        assertEquals(6, result1.size());
        assertTrue(Values.equals(result1, result2));
    }

    @Test
    void ToDictionary_KeyValueSelectorsWork() {
        TestCollection<Integer> collection = new TestCollection<>(new Integer[]{1, 2, 3, 4, 5, 6});

        Map<Integer, Integer> result = collection.toLinkedMap(key -> key + 10, val -> val + 100);

        assertEquals(Linq.of(collection.Items).cast(Integer.class).select(o -> o + 10), Linq.of(result.keySet()).orderBy(x -> x));
        assertEquals(Linq.of(collection.Items).cast(Integer.class).select(o -> o + 100), Linq.of(result.values()).orderBy(x -> x));
    }

    @Test
    void ToDictionary_ThrowArgumentNullExceptionWhenSourceIsNull() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toLinkedMap(key -> key));
    }

    @Test
    void ToDictionary_ThrowArgumentNullExceptionWhenKeySelectorIsNull() {
        int[] source = new int[0];
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toLinkedMap(keySelector));
    }

    @Test
    void ToDictionary_ThrowArgumentNullExceptionWhenValueSelectorIsNull() {
        int[] source = new int[0];
        Func1<Integer, Integer> keySelector = key -> key;
        Func1<Integer, Integer> valueSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toLinkedMap(keySelector, valueSelector));
    }

    @Test
    void ToDictionary_ThrowArgumentNullExceptionWhenSourceIsNullElementSelector() {
        int[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toLinkedMap(key -> key, e -> e));
    }

    @Test
    void ToDictionary_ThrowArgumentNullExceptionWhenKeySelectorIsNullElementSelector() {
        int[] source = new int[0];
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toLinkedMap(keySelector, e -> e));
    }

    @Test
    void ToDictionary_KeySelectorThrowException() {
        int[] source = new int[]{1, 2, 3};
        Func1<Integer, Integer> keySelector = key -> {
            if (key == 1)
                throw new InvalidOperationException();
            return key;
        };

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).toLinkedMap(keySelector));
    }

    @Test
    void ToDictionary_ThrowWhenKeySelectorReturnNull() {
        int[] source = new int[]{1, 2, 3};
        Func1<Integer, String> keySelector = key -> null;

        Map<String, Integer> map = Linq.of(source).toLinkedMap(keySelector);
        assertEquals(1, map.get(null));
    }

    @Test
    void ToDictionary_ThrowWhenKeySelectorReturnSameValueTwice() {
        int[] source = new int[]{1, 2, 3};
        Func1<Integer, Integer> keySelector = key -> 1;

        Map<Integer, Integer> map = Linq.of(source).toLinkedMap(keySelector);
        assertEquals(1, map.get(1));
    }

    @Test
    void ToDictionary_ValueSelectorThrowException() {
        int[] source = new int[]{1, 2, 3};
        Func1<Integer, Integer> keySelector = key -> key;
        Func1<Integer, Integer> valueSelector = value -> {
            if (value == 1)
                throw new InvalidOperationException();
            return value;
        };

        assertThrows(InvalidOperationException.class, () -> Linq.of(source).toLinkedMap(keySelector, valueSelector));
    }

    @Test
    void ThrowsOnNullKey() {
        NameScore[] source = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore("null", 55)
        };

        Linq.of(source).toLinkedMap(e -> e.Name); // Doesn't throw;

        NameScore[] sourceF = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore(null, 55)
        };

        Linq.of(sourceF).toLinkedMap(e -> e.Name);
    }

    @Test
    void ThrowsOnNullKeyValueSelector() {
        NameScore[] source = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore("null", 55)
        };

        Linq.of(source).toLinkedMap(e -> e.Name, e -> e); // Doesn't throw;

        NameScore[] sourceF = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore(null, 55)
        };

        Linq.of(sourceF).toLinkedMap(e -> e.Name, e -> e);
    }

    @Test
    void ThrowsOnDuplicateKeys() {
        NameScore[] source = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore("Bob", 55)
        };

        Map<String, NameScore> map = Linq.of(source).toLinkedMap(e -> e.Name, e -> e);
        assertEquals(2, map.size());
    }

    @Test
    void EmtpySource() {
        int[] elements = new int[]{};
        String[] keys = new String[]{};
        IEnumerable<NameScore> source = Linq.of(keys).zip(Linq.of(elements), (k, e) -> new NameScore(k, e));

        AssertMatches(Linq.of(keys), Linq.of(elements), Linq.of(source).toLinkedMap(e -> e.Name, e -> e.Score));
    }

    @Test
    void OneElementNullComparer() {
        int[] elements = new int[]{5};
        String[] keys = new String[]{"Bob"};
        NameScore[] source = new NameScore[]{new NameScore(keys[0], elements[0])};

        AssertMatches(Linq.of(keys), Linq.of(elements), Linq.of(source).toLinkedMap(e -> e.Name, e -> e.Score));
    }

    @Test
    void SeveralElementsCustomComparerer() {
        String[] keys = new String[]{"Bob", "Zen", "Prakash", "Chris", "Sachin"};
        NameScore[] source = new NameScore[]{
                new NameScore(keys[0], 95),
                new NameScore(keys[1], 45),
                new NameScore(keys[2], 100),
                new NameScore(keys[3], 90),
                new NameScore(keys[4], 45)
        };

        AssertMatches(Linq.of(keys), Linq.of(source), Linq.of(source).toLinkedMap(e -> e.Name));
    }

    @Test
    void NullCoalescedKeySelector() {
        String[] elements = new String[]{null};
        String[] keys = new String[]{Empty};
        String[] source = new String[]{null};

        AssertMatches(Linq.of(keys), Linq.of(elements), Linq.of(source).toLinkedMap(e -> e == null ? Empty : e, e -> e));
    }

    @Test
    void testToMap() {
        Map<Integer, Employee> map = Linq.of(emps).toLinkedMap(emp -> emp.empno);
        assertTrue(map.get(110).name.equals("Bill"));
        assertEquals(4, map.size());

        //key 重复,保留第一个
        Map<Integer, Employee> map2 = Linq.of(emps).toLinkedMap(emp -> emp.deptno);
        assertEquals(emps[0], map2.get(10));
        assertEquals(emps[1], map2.get(30));
        assertEquals(2, map2.size());
    }

    @Test
    void testToMapWithSelector() {
        Map<Integer, String> map = Linq.of(emps).toLinkedMap(emp -> emp.empno, emp -> emp.name);
        assertTrue(map.get(110).equals("Bill"));
        assertEquals(4, map.size());

        //key 重复,保留第一个
        Map<Integer, String> map2 = Linq.of(emps).toLinkedMap(emp -> emp.deptno, emp -> emp.name);
        assertEquals(emps[0].name, map2.get(10));
        assertEquals(emps[1].name, map2.get(30));
        assertEquals(2, map2.size());
    }


    private static class NameScore extends ValueType {
        private final String Name;
        private final int Score;

        private NameScore(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }
}
