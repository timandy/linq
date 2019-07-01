package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
public class ToListTest extends TestCase {
    @Test
    public void ToList_AlwaysCreateACopy() {
        List<Integer> sourceList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> resultList = Linq.of(sourceList).toList();

        assertNotSame(sourceList, resultList);
        assertEquals(sourceList, resultList);
    }

    private <T> void RunToListOnAllCollectionTypes(T[] items, Action1<List<T>> validation) {
        validation.apply(Linq.of(items).toList());
        validation.apply(Linq.of(Arrays.asList(items)).toList());
        validation.apply(new TestEnumerable<>(items).toList());
        validation.apply(new TestReadOnlyCollection<>(items).toList());
        validation.apply(new TestCollection<>(items).toList());
    }

    @Test
    public void ToList_WorkWithEmptyCollection() {
        this.RunToListOnAllCollectionTypes(new Integer[0], resultList -> {
            assertNotNull(resultList);
            assertEquals(0, resultList.size());
        });
    }

    @Test
    public void ToList_ProduceCorrectList() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        this.RunToListOnAllCollectionTypes(sourceArray, resultList -> {
            assertEquals(sourceArray.length, resultList.size());
            assertEquals(Linq.of(sourceArray), Linq.of(resultList));
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        this.RunToListOnAllCollectionTypes(sourceStringArray, resultStringList -> {
            assertEquals(sourceStringArray.length, resultStringList.size());
            for (int i = 0; i < sourceStringArray.length; i++)
                assertSame(sourceStringArray[i], resultStringList.get(i));
        });
    }

    @Test
    public void RunOnce() {
        assertEquals(Linq.range(3, 9), Linq.of(Linq.range(3, 9).runOnce().toList()));
    }

    @Test
    public void ToList_TouchCountWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        List<Integer> resultList = source.toList();

        assertEquals(source, Linq.of(resultList));
        assertEquals(1, source.CountTouched);
    }

    @Test
    public void ToList_ThrowArgumentNullExceptionWhenSourceIsNull() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toList());
    }

    // Generally the optimal approach. Anything that breaks this should be confirmed as not harming performance.
    @Test
    public void ToList_UseCopyToWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        List<Integer> resultList = source.toList();

        assertEquals(source, Linq.of(resultList));
        // This is different from C#. C# call ICollection.copy in List.ctor(), but java call Collection.toArray() then copy the array in ArrayList.ctor().
        assertEquals(0, source.CopyToTouched);
        assertEquals(1, source.ToListTouched);
    }

    @Test
    public void ToList_ArrayWhereSelect() {
        this.ToList_ArrayWhereSelect(new int[]{}, new String[]{});
        this.ToList_ArrayWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToList_ArrayWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToList_ArrayWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = new ArrayList<>(Linq.of(sourceIntegers).toList());
        List<String> convertedList = new ArrayList<>(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = new ArrayList<>();
        List<String> emptyStringsList = new ArrayList<>();

        assertEquals(convertedList, Linq.of(sourceIntegers).select(i -> i.toString()).toList());

        assertEquals(sourceList, Linq.of(sourceIntegers).where(i -> true).toList());
        assertEquals(emptyIntegersList, Linq.of(sourceIntegers).where(i -> false).toList());

        assertEquals(convertedList, Linq.of(sourceIntegers).where(i -> true).select(i -> i.toString()).toList());
        assertEquals(emptyStringsList, Linq.of(sourceIntegers).where(i -> false).select(i -> i.toString()).toList());

        assertEquals(convertedList, Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s != null).toList());
        assertEquals(emptyStringsList, Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s == null).toList());
    }

    @Test
    public void ToList_ListWhereSelect() {
        this.ToList_ListWhereSelect(new int[]{}, new String[]{});
        this.ToList_ListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToList_ListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});

    }

    private void ToList_ListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = new ArrayList<>(Linq.of(sourceIntegers).toList());
        List<String> convertedList = new ArrayList<>(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = new ArrayList<>();
        List<String> emptyStringsList = new ArrayList<>();

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).toList());

        assertEquals(sourceList, Linq.of(sourceList).where(i -> true).toList());
        assertEquals(emptyIntegersList, Linq.of(sourceList).where(i -> false).toList());

        assertEquals(convertedList, Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toList());
        assertEquals(emptyStringsList, Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toList());

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toList());
        assertEquals(emptyStringsList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toList());
    }

    @Test
    public void ToList_IListWhereSelect() {
        this.ToList_IListWhereSelect(new int[]{}, new String[]{});
        this.ToList_IListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToList_IListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToList_IListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = Collections.unmodifiableList(Linq.of(sourceIntegers).toList());
        List<String> convertedList = Collections.unmodifiableList(Linq.of(convertedStrings).toList());

        List<Integer> emptyIntegersList = Collections.unmodifiableList(Linq.<Integer>empty().toList());
        List<String> emptyStringsList = Collections.unmodifiableList(Linq.<String>empty().toList());

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).toList());

        assertEquals(sourceList, Linq.of(sourceList).where(i -> true).toList());
        assertEquals(emptyIntegersList, Linq.of(sourceList).where(i -> false).toList());

        assertEquals(convertedList, Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toList());
        assertEquals(emptyStringsList, Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toList());

        assertEquals(convertedList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toList());
        assertEquals(emptyStringsList, Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toList());
    }

    @Test
    public void SameResultsRepeatCallsFromWhereOnIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.toList(), q.toList());
    }

    @Test
    public void SameResultsRepeatCallsFromWhereOnStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.toList(), q.toList());
    }

    @Test
    public void SourceIsEmptyICollectionT() {
        int[] source = {};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEmpty(Linq.of(Linq.of(source).toList()));
        assertEmpty(Linq.of(collection.toList()));
    }

    @Test
    public void SourceIsICollectionTWithFewElements() {
        Integer[] source = {-5, null, 0, 10, 3, -1, null, 4, 9};
        Integer[] expected = {-5, null, 0, 10, 3, -1, null, 4, 9};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEquals(Linq.of(expected), Linq.of(Linq.of(source).toList()));
        assertEquals(Linq.of(expected), Linq.of(collection.toList()));
    }

    @Test
    public void SourceNotICollectionAndIsEmpty() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 0);
        assertEmpty(Linq.of(source.toList()));
    }

    @Test
    public void SourceNotICollectionAndHasElements() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 10);
        int[] expected = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toList()));
    }

    @Test
    public void SourceNotICollectionAndAllNull() {
        IEnumerable<Integer> source = RepeatedNullableNumberGuaranteedNotCollectionType(null, 5);
        Integer[] expected = {null, null, null, null, null};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toList()));
    }

    @Test
    public void ConstantTimeCountPartitionSelectSameTypeToList() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toList()));
    }

    @Test
    public void ConstantTimeCountPartitionSelectDiffTypeToList() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toList()));
    }

    @Test
    public void ConstantTimeCountEmptyPartitionSelectSameTypeToList() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toList()));
    }

    @Test
    public void ConstantTimeCountEmptyPartitionSelectDiffTypeToList() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toList()));
    }

    @Test
    public void NonConstantTimeCountPartitionSelectSameTypeToList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toList()));
    }

    @Test
    public void NonConstantTimeCountPartitionSelectDiffTypeToList() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toList()));
    }

    @Test
    public void NonConstantTimeCountEmptyPartitionSelectSameTypeToList() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toList()));
    }

    @Test
    public void NonConstantTimeCountEmptyPartitionSelectDiffTypeToList() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toList()));
    }

    @Test
    public void testToList() {
        Object[] source = {1, 2, 3};
        List<Integer> target = Linq.of(source).cast(Integer.class).toList();
        assertEquals(3, target.size());
        assertTrue(Linq.of(source).cast(Integer.class).sequenceEqual(Linq.of(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        List<Integer> target2 = Linq.of(source2).toList();
        assertEquals(3, target2.size());
        assertTrue(Linq.of(target2).sequenceEqual(Linq.of(source2)));

        List<Character> lst = Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).toList();
        assertEquals(5, lst.size());
        assertEquals("o", lst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        List<Character> arr = Linq.of(arrChar).toList();
        assertEquals(5, arr.size());
        assertEquals("o", arr.get(4).toString());

        List<Character> hello = Linq.of("hello").toList();
        assertEquals(5, hello.size());
        assertEquals("o", hello.get(4).toString());

        List<Character> h = Linq.singleton('h').toList();
        assertEquals(1, h.size());
        assertEquals("h", h.get(0).toString());

        List<Character> empty = Linq.<Character>empty().toList();
        assertEquals(0, empty.size());
    }
}
