package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by 许崇雷 on 2019-06-06.
 */
class ToArrayTest extends TestCase {
    private static IEnumerable<Object[]> JustBelowPowersOfTwoLengths() {
        return SmallPowersOfTwo().select(p -> new Object[]{p - 1});
    }

    private static IEnumerable<Object[]> PowersOfTwoLengths() {
        return SmallPowersOfTwo().select(p -> new Object[]{p});
    }

    private static IEnumerable<Object[]> JustAbovePowersOfTwoLengths() {
        return SmallPowersOfTwo().select(p -> new Object[]{p + 1});
    }

    private static IEnumerable<Integer> SmallPowersOfTwo() {
        // By N being "small" we mean that allocating an array of
        // size N doesn't come close to the risk of causing an OOME

        final int MaxPower = 19;
        return Linq.range(0, MaxPower)
                .select(i -> 1 << i);// equivalent to pow(2, i)
    }

    @Test
    void ToArray_CreateACopyWhenNotEmpty() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5};
        Array<Integer> resultArray = Linq.of(sourceArray).toArray();

        assertNotSame(sourceArray, resultArray);
        assertEquals(Linq.of(sourceArray), resultArray);
    }

    @Test
    void ToArray_UseArrayEmptyWhenEmpty() {
        IEnumerable<Integer> emptySourceArray = new Array<>(new Object[0]);

        assertSame(emptySourceArray.toArray(), emptySourceArray.toArray());

        assertSame(emptySourceArray.select(i -> i).toArray(), emptySourceArray.select(i -> i).toArray());
        assertSame(Linq.of(emptySourceArray.toList()).select(i -> i).toArray(), Linq.of(emptySourceArray.toList()).select(i -> i).toArray());
        assertSame(Linq.of(new ArrayList<>(emptySourceArray.toList())).select(i -> i).toArray(), Linq.of(new ArrayList<>(emptySourceArray.toList())).select(i -> i).toArray());
        assertSame(emptySourceArray.orderBy(i -> i).toArray(), emptySourceArray.orderBy(i -> i).toArray());

        assertSame(Linq.range(5, 0).toArray(), Linq.range(3, 0).toArray());
        assertSame(Linq.range(5, 3).take(0).toArray(), Linq.range(3, 0).toArray());
        assertSame(Linq.range(5, 3).skip(3).toArray(), Linq.range(3, 0).toArray());

        assertSame(Linq.repeat(42, 0).toArray(), Linq.range(84, 0).toArray());
        assertSame(Linq.repeat(42, 3).take(0).toArray(), Linq.range(84, 3).take(0).toArray());
        assertSame(Linq.repeat(42, 3).skip(3).toArray(), Linq.range(84, 3).skip(3).toArray());
    }

    private <T> void RunToArrayOnAllCollectionTypes(T[] items, Action1<Array<T>> validation) {
        validation.apply(Linq.of(items).toArray());
        validation.apply(Linq.of(Arrays.asList(items)).toArray());
        validation.apply(new TestEnumerable<>(items).toArray());
        validation.apply(new TestReadOnlyCollection<>(items).toArray());
        validation.apply(new TestCollection<>(items).toArray());
    }

    @Test
    void ToArray_WorkWithEmptyCollection() {
        this.RunToArrayOnAllCollectionTypes(new Integer[0], resultArray -> {
            assertNotNull(resultArray);
            assertEquals(0, resultArray._getCount());
        });
    }

    @Test
    void ToArray_ProduceCorrectArray() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        this.RunToArrayOnAllCollectionTypes(sourceArray, resultArray -> {
            assertEquals(sourceArray.length, resultArray._getCount());
            assertEquals(Linq.of(sourceArray), resultArray);
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        this.RunToArrayOnAllCollectionTypes(sourceStringArray, resultStringArray -> {
            assertEquals(sourceStringArray.length, resultStringArray._getCount());
            for (int i = 0; i < sourceStringArray.length; i++)
                assertSame(sourceStringArray[i], resultStringArray.get(i));
        });
    }

    @Test
    void RunOnce() {
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5, 6, 7}), Linq.range(1, 7).runOnce().toArray());
        assertEquals(Linq.of("1", "2", "3", "4", "5", "6", "7", "8"), Linq.range(1, 8).select(i -> i.toString()).runOnce().toArray());
    }

    @Test
    void ToArray_TouchCountWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Array<Integer> resultArray = source.toArray();

        assertEquals(source, resultArray);
        assertEquals(1, source.CountTouched);
    }

    @Test
    void ToArray_ThrowArgumentNullExceptionWhenSourceIsNull() {
        int[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toArray());
    }

    // Generally the optimal approach. Anything that breaks this should be confirmed as not harming performance.
    @Test
    void ToArray_UseCopyToWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Array<Integer> resultArray = source.toArray();

        assertEquals(source, resultArray);
        // assertEquals(1, source.CopyToTouched);
        assertEquals(1, source.ToArrayTouched); // This is different from C#
    }

    @Test
    void ToArray_FailOnExtremelyLargeCollection() {
        IEnumerable<Byte> largeSeq = new FastInfiniteEnumerator<>();
        assertThrows(OutOfMemoryError.class, () -> largeSeq.toArray());
    }

    @Test
    void ToArray_ArrayWhereSelect() {
        this.ToArray_ArrayWhereSelect(new int[]{}, new String[]{});
        this.ToArray_ArrayWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToArray_ArrayWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToArray_ArrayWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        assertEquals(Linq.of(convertedStrings), Linq.of(sourceIntegers).select(i -> i.toString()).toArray());

        assertEquals(Linq.of(sourceIntegers), Linq.of(sourceIntegers).where(i -> true).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceIntegers).where(i -> false).toArray());

        assertEquals(Linq.of(convertedStrings), Linq.of(sourceIntegers).where(i -> true).select(i -> i.toString()).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceIntegers).where(i -> false).select(i -> i.toString()).toArray());

        assertEquals(Linq.of(convertedStrings), Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s != null).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s == null).toArray());
    }

    @Test
    void ToArray_ListWhereSelect() {
        this.ToArray_ListWhereSelect(new int[]{}, new String[]{});
        this.ToArray_ListWhereSelect(new int[]{1}, new String[]{"1"});
        this.ToArray_ListWhereSelect(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
    }

    private void ToArray_ListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = Linq.of(sourceIntegers).toList();

        assertEquals(Linq.of(convertedStrings), Linq.of(sourceList).select(i -> i.toString()).toArray());

        assertEquals(Linq.of(sourceList), Linq.of(sourceList).where(i -> true).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceList).where(i -> false).toArray());

        assertEquals(Linq.of(convertedStrings), Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toArray());

        assertEquals(Linq.of(convertedStrings), Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toArray());
        assertEquals(Linq.empty(), Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toArray());
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.toArray(), q.toArray());
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(q.toArray(), q.toArray());
    }

    @Test
    void SameResultsButNotSameObject() {
        IEnumerable<Integer> qInt = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);
        IEnumerable<String> qString = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertNotSame(qInt.toArray(), qInt.toArray());
        assertNotSame(qString.toArray(), qString.toArray());
        assertNotSame(qInt.toArray().getArray(), qInt.toArray().getArray());
        assertNotSame(qString.toArray().getArray(), qString.toArray().getArray());
    }

    @Test
    void EmptyArraysSameObject() {
        // .NET Core returns the instance as an optimization.
        // see https://github.com/dotnet/corefx/pull/2401.
        assertSame(Linq.<Integer>empty().toArray(), Linq.<Integer>empty().toArray());

        Integer[] array = new Integer[0];
        assertNotSame(array, Linq.of(array).toArray().getArray());
    }

    @Test
    void SourceIsEmptyICollectionT() {
        int[] source = {};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEmpty(Linq.of(source).toArray());
        assertEmpty(collection.toArray());
    }

    @Test
    void SourceIsICollectionTWithFewElements() {
        Integer[] source = {-5, null, 0, 10, 3, -1, null, 4, 9};
        Integer[] expected = {-5, null, 0, 10, 3, -1, null, 4, 9};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEquals(Linq.of(expected), Linq.of(source).toArray());
        assertEquals(Linq.of(expected), collection.toArray());
    }

    @Test
    void SourceNotICollectionAndIsEmpty() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 0);

        assertNull(as(source, ICollection.class));

        assertEmpty(source.toArray());
    }

    @Test
    void SourceNotICollectionAndHasElements() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 10);
        int[] expected = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), source.toArray());
    }

    @Test
    void SourceNotICollectionAndAllNull() {
        IEnumerable<Integer> source = RepeatedNullableNumberGuaranteedNotCollectionType(null, 5);
        Integer[] expected = {null, null, null, null, null};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), source.toArray());
    }

    @Test
    void ConstantTimeCountPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), source.toArray());
    }

    @Test
    void ConstantTimeCountPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), source.toArray());
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1000);
        assertEmpty(source.toArray());
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1000);
        assertEmpty(source.toArray());
    }

    @Test
    void NonConstantTimeCountPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), source.toArray());
    }

    @Test
    void NonConstantTimeCountPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), source.toArray());
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1000);
        assertEmpty(source.toArray());
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1000);
        assertEmpty(source.toArray());
    }

    @Test
    void ToArrayShouldWorkWithSpecialLengthLazyEnumerables() {
        for (Object[] objects : JustBelowPowersOfTwoLengths()) {
            this.ToArrayShouldWorkWithSpecialLengthLazyEnumerables((int) objects[0]);
        }
        for (Object[] objects : PowersOfTwoLengths()) {
            this.ToArrayShouldWorkWithSpecialLengthLazyEnumerables((int) objects[0]);
        }
        for (Object[] objects : JustAbovePowersOfTwoLengths()) {
            this.ToArrayShouldWorkWithSpecialLengthLazyEnumerables((int) objects[0]);
        }
    }

    private void ToArrayShouldWorkWithSpecialLengthLazyEnumerables(int length) {
        assertTrue(length >= 0);

        IEnumerable<Integer> range = Linq.range(0, length);
        IEnumerable<Integer> lazyEnumerable = ForceNotCollection(range); // We won't go down the IIListProvider path
        assertEquals(range, lazyEnumerable.toArray());
    }

    @Test
    void ToArray_Cast() {
        Object[] source = {Enum0.First, Enum0.Second, Enum0.Third};
        IEnumerable<Enum0> cast = Linq.of(source).cast(Enum0.class);
        assertIsType(CastIterator.class, cast);
        Array<Enum0> castArray = cast.toArray();
        assertIsType(Array.class, castArray);
        assertEquals(Linq.of(Enum0.First, Enum0.Second, Enum0.Third), castArray);
    }

    @Test
    void testToArray() {
        Object[] source = {1, 2, 3};
        Array<Integer> target = Linq.of(source).cast(Integer.class).toArray();
        assertEquals(3, target._getCount());
        assertTrue(Linq.of(source).sequenceEqual(target));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Array<Integer> target2 = Linq.of(source2).toArray();
        assertEquals(3, target2._getCount());
        assertTrue(Linq.of(source2).sequenceEqual(target2));

        Array<Character> lst = Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).toArray();
        assertEquals(5, lst._getCount());
        assertEquals("o", lst.get(4).toString());

        Array<Character> linkedLst = Linq.of(new LinkedList<>(Arrays.asList('h', 'e', 'l', 'l', 'o'))).toArray();
        assertEquals(5, linkedLst._getCount());
        assertEquals("o", linkedLst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Array<Character> arr = Linq.of(arrChar).toArray();
        assertEquals(5, arr._getCount());
        assertEquals("o", arr.get(4).toString());

        Array<Character> hello = Linq.chars("hello").toArray();
        assertEquals(5, hello._getCount());
        assertEquals("o", hello.get(4).toString());

        Array<Character> h = Linq.singleton('h').toArray();
        assertEquals(1, h._getCount());
        assertEquals("h", h.get(0).toString());

        Array<Character> empty = Linq.<Character>empty().toArray();
        assertEquals(0, empty._getCount());

        String[] array = {"a", "b"};
        Array<String> stringArray = Linq.of(array).toArray();
        stringArray.set(0, "c");
        assertEquals("a", array[0]);
        assertEquals("c", stringArray.get(0));

        assertEquals(Linq.of(true, false, false), Linq.of(new boolean[]{true, false, false}).toArray());
        assertEquals(Linq.of((byte) 1, (byte) 2, (byte) 3), Linq.of(new byte[]{1, 2, 3}).toArray());
        assertEquals(Linq.of((short) 1, (short) 2, (short) 3), Linq.of(new short[]{1, 2, 3}).toArray());
        assertEquals(Linq.of(1, 2, 3), Linq.of(new int[]{1, 2, 3}).toArray());
        assertEquals(Linq.of(1L, 2L, 3L), Linq.of(new long[]{1, 2, 3}).toArray());
        assertEquals(Linq.of('a', 'b', 'c'), Linq.of(new char[]{'a', 'b', 'c'}).toArray());
        assertEquals(Linq.of(1f, 2f, 3f), Linq.of(new float[]{1f, 2f, 3f}).toArray());
        assertEquals(Linq.of(1d, 2d, 3d), Linq.of(new double[]{1d, 2d, 3d}).toArray());
    }


    // Consider that two very similar enums is not unheard of, if e.g. two assemblies map the
    // same external source of numbers (codes, response codes, colour codes, etc.) to values.
    private enum Enum0 {
        First,
        Second,
        Third
    }
}
