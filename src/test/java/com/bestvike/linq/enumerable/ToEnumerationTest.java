package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.NotSupportedException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public class ToEnumerationTest extends TestCase {
    @Test
    public void ToEnumeration_AlwaysCreateACopy() {
        Enumeration<Integer> sourceList = new Vector<>(Arrays.asList(1, 2, 3, 4, 5)).elements();
        Enumeration<Integer> resultList = Linq.asEnumerable(sourceList).toEnumeration();

        assertNotSame(sourceList, resultList);
        assertEquals(Linq.range(1, 5), Linq.asEnumerable(resultList));
        assertFalse(sourceList.hasMoreElements());
        assertFalse(resultList.hasMoreElements());
    }

    private <T> void RunToEnumerationOnAllCollectionTypes(T[] items, Action1<Enumeration<T>> validation) {
        validation.apply(Linq.asEnumerable(items).toEnumeration());
        validation.apply(Linq.asEnumerable(Arrays.asList(items)).toEnumeration());
        validation.apply(new TestEnumerable<>(items).toEnumeration());
        validation.apply(new TestReadOnlyCollection<>(items).toEnumeration());
        validation.apply(new TestCollection<>(items).toEnumeration());
    }

    @Test
    public void ToEnumeration_WorkWithEmptyCollection() {
        this.RunToEnumerationOnAllCollectionTypes(new Integer[0], resultList -> {
            assertNotNull(resultList);
            assertFalse(resultList.hasMoreElements());
        });
    }

    @Test
    public void ToEnumeration_ProduceCorrectList() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        this.RunToEnumerationOnAllCollectionTypes(sourceArray, resultList -> {
            assertEquals(Linq.asEnumerable(sourceArray), Linq.asEnumerable(resultList));
            assertFalse(resultList.hasMoreElements());
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        this.RunToEnumerationOnAllCollectionTypes(sourceStringArray, resultStringList -> {
            IEnumerable<String> enumerationEnumerable = Linq.asEnumerable(resultStringList);
            assertEquals(Linq.asEnumerable(sourceStringArray), enumerationEnumerable);
            assertThrows(NotSupportedException.class, () -> enumerationEnumerable.enumerator());
        });
    }

    @Test
    public void RunOnce() {
        assertEquals(Linq.range(3, 9), Linq.asEnumerable(Linq.range(3, 9).runOnce().toEnumeration()));
    }

    @Test
    public void ToEnumeration_TouchCountWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Enumeration<Integer> resultList = source.toEnumeration();

        assertEquals(source, Linq.asEnumerable(resultList));
        assertEquals(0, source.CountTouched);
    }

    @Test
    public void ToEnumeration_ThrowArgumentNullExceptionWhenSourceIsNull() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toEnumeration());
    }

    // Generally the optimal approach. Anything that breaks this should be confirmed as not harming performance.
    @Test
    public void ToEnumeration_UseCopyToWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Enumeration<Integer> resultList = source.toEnumeration();

        assertEquals(source, Linq.asEnumerable(resultList));
        // This is different from C#. C# call ICollection.copy in List.ctor(), but java call Collection.toArray() then copy the array in ArrayList.ctor().
        assertEquals(0, source.CopyToTouched);
        assertEquals(0, source.ToListTouched);
    }

    @Test
    public void ToEnumeration_ArrayWhereSelect() {
        this.ToEnumeration_ArrayWhereSelect(new int[]{}, new String[]{});
        this.ToEnumeration_ArrayWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToEnumeration_ArrayWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToEnumeration_ArrayWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        IEnumerable<Integer> sourceList = Linq.asEnumerable(sourceIntegers);
        IEnumerable<String> convertedList = Linq.asEnumerable(convertedStrings);

        IEnumerable<Integer> emptyIntegersList = Linq.empty();
        IEnumerable<String> emptyStringsList = Linq.empty();

        assertEquals(convertedList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).select(i -> i.toString()).toEnumeration()));

        assertEquals(sourceList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).where(i -> true).toEnumeration()));
        assertEquals(emptyIntegersList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).where(i -> false).toEnumeration()));

        assertEquals(convertedList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).where(i -> true).select(i -> i.toString()).toEnumeration()));
        assertEquals(emptyStringsList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).where(i -> false).select(i -> i.toString()).toEnumeration()));

        assertEquals(convertedList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).select(i -> i.toString()).where(s -> s != null).toEnumeration()));
        assertEquals(emptyStringsList, Linq.asEnumerable(Linq.asEnumerable(sourceIntegers).select(i -> i.toString()).where(s -> s == null).toEnumeration()));
    }

    @Test
    public void ToEnumeration_ListWhereSelect() {
        this.ToEnumeration_ListWhereSelect(new int[]{}, new String[]{});
        this.ToEnumeration_ListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToEnumeration_ListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});

    }

    private void ToEnumeration_ListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = new ArrayList<>(Linq.asEnumerable(sourceIntegers).toList());
        List<String> convertedList = new ArrayList<>(Linq.asEnumerable(convertedStrings).toList());

        List<Integer> emptyIntegersList = new ArrayList<>();
        List<String> emptyStringsList = new ArrayList<>();

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).toEnumeration()));

        assertEquals(Linq.asEnumerable(sourceList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> true).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyIntegersList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> false).toEnumeration()));

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> true).select(i -> i.toString()).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyStringsList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> false).select(i -> i.toString()).toEnumeration()));

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).where(s -> s != null).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyStringsList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).where(s -> s == null).toEnumeration()));
    }

    @Test
    public void ToEnumeration_IListWhereSelect() {
        this.ToEnumeration_IListWhereSelect(new int[]{}, new String[]{});
        this.ToEnumeration_IListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToEnumeration_IListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToEnumeration_IListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = Collections.unmodifiableList(Linq.asEnumerable(sourceIntegers).toList());
        List<String> convertedList = Collections.unmodifiableList(Linq.asEnumerable(convertedStrings).toList());

        List<Integer> emptyIntegersList = Collections.unmodifiableList(Linq.<Integer>empty().toList());
        List<String> emptyStringsList = Collections.unmodifiableList(Linq.<String>empty().toList());

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).toEnumeration()));

        assertEquals(Linq.asEnumerable(sourceList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> true).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyIntegersList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> false).toEnumeration()));

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> true).select(i -> i.toString()).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyStringsList), Linq.asEnumerable(Linq.asEnumerable(sourceList).where(i -> false).select(i -> i.toString()).toEnumeration()));

        assertEquals(Linq.asEnumerable(convertedList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).where(s -> s != null).toEnumeration()));
        assertEquals(Linq.asEnumerable(emptyStringsList), Linq.asEnumerable(Linq.asEnumerable(sourceList).select(i -> i.toString()).where(s -> s == null).toEnumeration()));
    }

    @Test
    public void SameResultsRepeatCallsFromWhereOnIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(Linq.asEnumerable(q.toEnumeration()), Linq.asEnumerable(q.toEnumeration()));
    }

    @Test
    public void SameResultsRepeatCallsFromWhereOnStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(Linq.asEnumerable(q.toEnumeration()), Linq.asEnumerable(q.toEnumeration()));
    }

    @Test
    public void SourceIsEmptyICollectionT() {
        int[] source = {};

        ICollection<Integer> collection = Linq.asEnumerable(source).toArray();

        assertEmpty(Linq.asEnumerable(Linq.asEnumerable(source).toEnumeration()));
        assertEmpty(Linq.asEnumerable(collection.toEnumeration()));
    }

    @Test
    public void SourceIsICollectionTWithFewElements() {
        Integer[] source = {-5, null, 0, 10, 3, -1, null, 4, 9};
        Integer[] expected = {-5, null, 0, 10, 3, -1, null, 4, 9};

        ICollection<Integer> collection = Linq.asEnumerable(source).toArray();

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(Linq.asEnumerable(source).toEnumeration()));
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(collection.toEnumeration()));
    }

    @Test
    public void SourceNotICollectionAndIsEmpty() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 0);
        assertEmpty(Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void SourceNotICollectionAndHasElements() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 10);
        int[] expected = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void SourceNotICollectionAndAllNull() {
        IEnumerable<Integer> source = RepeatedNullableNumberGuaranteedNotCollectionType(null, 5);
        Integer[] expected = {null, null, null, null, null};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void ConstantTimeCountPartitionSelectSameTypeToEnumeration() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8, 10}), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void ConstantTimeCountPartitionSelectDiffTypeToEnumeration() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.asEnumerable("1", "2", "3", "4", "5"), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void ConstantTimeCountEmptyPartitionSelectSameTypeToEnumeration() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void ConstantTimeCountEmptyPartitionSelectDiffTypeToEnumeration() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void NonConstantTimeCountPartitionSelectSameTypeToEnumeration() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.asEnumerable(new int[]{2, 4, 6, 8, 10}), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void NonConstantTimeCountPartitionSelectDiffTypeToEnumeration() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.asEnumerable("1", "2", "3", "4", "5"), Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void NonConstantTimeCountEmptyPartitionSelectSameTypeToEnumeration() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void NonConstantTimeCountEmptyPartitionSelectDiffTypeToEnumeration() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.asEnumerable(source.toEnumeration()));
    }

    @Test
    public void testToEnumeration() {
        Object[] source = {1, 2, 3};
        Enumeration<Integer> target = Linq.asEnumerable(source).cast(Integer.class).toEnumeration();
        assertTrue(Linq.asEnumerable(source).cast(Integer.class).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Enumeration<Integer> target2 = Linq.asEnumerable(source2).toEnumeration();
        assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));
        assertFalse(target2.hasMoreElements());

        Enumeration<Character> lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toEnumeration();
        assertEquals("o", Linq.asEnumerable(lst).elementAt(4).toString());
        assertFalse(lst.hasMoreElements());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Enumeration<Character> arr = Linq.asEnumerable(arrChar).toEnumeration();
        assertEquals("o", Linq.asEnumerable(arr).elementAt(4).toString());
        assertFalse(arr.hasMoreElements());

        Enumeration<Character> hello = Linq.asEnumerable("hello").toEnumeration();
        assertEquals("o", Linq.asEnumerable(hello).elementAt(4).toString());
        assertFalse(hello.hasMoreElements());

        Enumeration<Character> h = Linq.singleton('h').toEnumeration();
        assertEquals("h", Linq.asEnumerable(h).elementAt(0).toString());
        assertFalse(h.hasMoreElements());

        Enumeration<Character> empty = Linq.<Character>empty().toEnumeration();
        assertEquals(0, Linq.asEnumerable(empty).count());
        assertEquals(0, Linq.asEnumerable(empty).count());
    }
}
