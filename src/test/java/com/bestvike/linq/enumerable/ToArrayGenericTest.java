package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action1;
import com.bestvike.function.Action2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by 许崇雷 on 2019-07-19.
 */
class ToArrayGenericTest extends TestCase {
    private static <T> void RunToArrayOnAllCollectionTypes(Class<T> clazz, T[] items, Action1<T[]> validation) {
        validation.apply(Linq.of(items).toArray(clazz));
        validation.apply(Linq.of(Arrays.asList(items)).toArray(clazz));
        validation.apply(new TestEnumerable<>(items).toArray(clazz));
        validation.apply(new TestReadOnlyCollection<>(items).toArray(clazz));
        validation.apply(new TestCollection<>(items).toArray(clazz));
    }

    private static IEnumerable<Object[]> ToArray_ArrayWhereSelect_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new int[]{}, new String[]{});
        argsList.add(new int[]{1}, new String[]{"1"});
        argsList.add(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
        return argsList;
    }

    private static IEnumerable<Object[]> ToArray_ListWhereSelect_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(new int[]{}, new String[]{});
        argsList.add(new int[]{1}, new String[]{"1"});
        argsList.add(new int[]{1, 2, 3}, new String[]{"1", "2", "3"});
        return argsList;
    }

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
        Integer[] resultArray = Linq.of(sourceArray).toArray(Integer.class);

        assertNotSame(sourceArray, resultArray);
        assertEquals(Linq.of(sourceArray), Linq.of(resultArray));
    }

    @Test
    void ToArray_UseArrayEmptyWhenEmpty() {
        IEnumerable<Integer> emptySourceArray = new Array<>(new Object[0]);

        Action2<Object, Object> assertEquals = (objA, objB) -> {
            assertNotSame(objA, objB);
            assertTrue(Values.equals(objA, objB));
        };

        assertEquals.apply(emptySourceArray.toArray(Integer.class), emptySourceArray.toArray(Integer.class));

        assertEquals.apply(emptySourceArray.select(i -> i).toArray(Integer.class), emptySourceArray.select(i -> i).toArray(Integer.class));
        assertEquals.apply(Linq.of(emptySourceArray.toList()).select(i -> i).toArray(Integer.class), Linq.of(emptySourceArray.toList()).select(i -> i).toArray(Integer.class));
        assertEquals.apply(Linq.of(new ArrayList<>(emptySourceArray.toList())).select(i -> i).toArray(Integer.class), Linq.of(new ArrayList<>(emptySourceArray.toList())).select(i -> i).toArray(Integer.class));
        assertEquals.apply(emptySourceArray.orderBy(i -> i).toArray(Integer.class), emptySourceArray.orderBy(i -> i).toArray(Integer.class));

        assertEquals.apply(Linq.range(5, 0).toArray(Integer.class), Linq.range(3, 0).toArray(Integer.class));
        assertEquals.apply(Linq.range(5, 3).take(0).toArray(Integer.class), Linq.range(3, 0).toArray(Integer.class));
        assertEquals.apply(Linq.range(5, 3).skip(3).toArray(Integer.class), Linq.range(3, 0).toArray(Integer.class));

        assertEquals.apply(Linq.repeat(42, 0).toArray(Integer.class), Linq.range(84, 0).toArray(Integer.class));
        assertEquals.apply(Linq.repeat(42, 3).take(0).toArray(Integer.class), Linq.range(84, 3).take(0).toArray(Integer.class));
        assertEquals.apply(Linq.repeat(42, 3).skip(3).toArray(Integer.class), Linq.range(84, 3).skip(3).toArray(Integer.class));
    }

    @Test
    void ToArray_WorkWithEmptyCollection() {
        RunToArrayOnAllCollectionTypes(Integer.class, new Integer[0], resultArray -> {
            assertNotNull(resultArray);
            assertEquals(0, resultArray.length);
        });
    }

    @Test
    void ToArray_ProduceCorrectArray() {
        Integer[] sourceArray = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        RunToArrayOnAllCollectionTypes(Integer.class, sourceArray, resultArray -> {
            assertEquals(sourceArray.length, resultArray.length);
            assertEquals(Linq.of(sourceArray), Linq.of(resultArray));
        });

        String[] sourceStringArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        RunToArrayOnAllCollectionTypes(String.class, sourceStringArray, resultStringArray -> {
            assertEquals(sourceStringArray.length, resultStringArray.length);
            for (int i = 0; i < sourceStringArray.length; i++)
                assertSame(sourceStringArray[i], resultStringArray[i]);
        });
    }

    @Test
    void RunOnce() {
        assertEquals(Linq.of(new int[]{1, 2, 3, 4, 5, 6, 7}), Linq.of(Linq.range(1, 7).runOnce().toArray(Integer.class)));
        assertEquals(Linq.of("1", "2", "3", "4", "5", "6", "7", "8"), Linq.of(Linq.range(1, 8).select(i -> i.toString()).runOnce().toArray(String.class)));
    }

    @Test
    void ToArray_TouchCountWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Integer[] resultArray = source.toArray(Integer.class);

        assertEquals(source, Linq.of(resultArray));
        assertEquals(1, source.CountTouched);
    }

    @Test
    void ToArray_ThrowArgumentNullExceptionWhenSourceIsNull() {
        int[] source = null;
        assertThrows(ArgumentNullException.class, () -> Linq.of(source).toArray(Integer.class));
    }

    // Generally the optimal approach. Anything that breaks this should be confirmed as not harming performance.
    @Test
    void ToArray_UseCopyToWithICollection() {
        TestCollection<Integer> source = new TestCollection<>(new Integer[]{1, 2, 3, 4});
        Integer[] resultArray = source.toArray(Integer.class);

        assertEquals(source, Linq.of(resultArray));
        // assertEquals(1, source.CopyToTouched);
        assertEquals(1, source.ToArrayTouched); // This is different from C#
    }

    @Test
    void ToArray_FailOnExtremelyLargeCollection() {
        IEnumerable<Byte> largeSeq = new FastInfiniteEnumerator<>();
        assertThrows(OutOfMemoryError.class, () -> largeSeq.toArray(Byte.class));
    }

    @ParameterizedTest
    @MethodSource("ToArray_ArrayWhereSelect_TestData")
    void ToArray_ArrayWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceIntegers).select(i -> i.toString()).toArray(String.class)));

        assertEquals(Linq.of(sourceIntegers), Linq.of(Linq.of(sourceIntegers).where(i -> true).toArray(Integer.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceIntegers).where(i -> false).toArray(Integer.class)));

        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceIntegers).where(i -> true).select(i -> i.toString()).toArray(String.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceIntegers).where(i -> false).select(i -> i.toString()).toArray(String.class)));

        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s != null).toArray(String.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceIntegers).select(i -> i.toString()).where(s -> s == null).toArray(String.class)));
    }

    @ParameterizedTest
    @MethodSource("ToArray_ListWhereSelect_TestData")
    void ToArray_ListWhereSelect(int[] sourceIntegers, String[] convertedStrings) {
        List<Integer> sourceList = Linq.of(sourceIntegers).toList();

        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceList).select(i -> i.toString()).toArray(String.class)));

        assertEquals(Linq.of(sourceList), Linq.of(Linq.of(sourceList).where(i -> true).toArray(Integer.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceList).where(i -> false).toArray(Integer.class)));

        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceList).where(i -> true).select(i -> i.toString()).toArray(String.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceList).where(i -> false).select(i -> i.toString()).toArray(String.class)));

        assertEquals(Linq.of(convertedStrings), Linq.of(Linq.of(sourceList).select(i -> i.toString()).where(s -> s != null).toArray(String.class)));
        assertEquals(Linq.empty(), Linq.of(Linq.of(sourceList).select(i -> i.toString()).where(s -> s == null).toArray(String.class)));
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnIntQuery() {
        IEnumerable<Integer> q = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);

        assertEquals(Linq.of(q.toArray(Integer.class)), Linq.of(q.toArray(Integer.class)));
    }

    @Test
    void SameResultsRepeatCallsFromWhereOnStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertEquals(Linq.of(q.toArray(String.class)), Linq.of(q.toArray(String.class)));
    }

    @Test
    void SameResultsButNotSameObject() {
        IEnumerable<Integer> qInt = Linq.of(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345}).where(x -> x > Integer.MIN_VALUE);
        IEnumerable<String> qString = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty).where(x -> !IsNullOrEmpty(x));

        assertNotSame(qInt.toArray(Integer.class), qInt.toArray(Integer.class));
        assertNotSame(qString.toArray(String.class), qString.toArray(String.class));
        assertEquals(Linq.of(qInt.toArray(Integer.class)), Linq.of(qInt.toArray(Integer.class)));
        assertEquals(Linq.of(qString.toArray(String.class)), Linq.of(qString.toArray(String.class)));
    }

    @Test
    void EmptyArraysSameObject() {
        // .NET Core returns the instance as an optimization.
        // see https://github.com/dotnet/corefx/pull/2401.
        assertNotSame(Linq.<Integer>empty().toArray(Integer.class), Linq.<Integer>empty().toArray(Integer.class));

        Integer[] array = new Integer[0];
        assertNotSame(array, Linq.of(array).toArray(Integer.class));
    }

    @Test
    void SourceIsEmptyICollectionT() {
        int[] source = {};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEmpty(Linq.of(Linq.of(source).toArray(Integer.class)));
        assertEmpty(Linq.of(collection.toArray(Integer.class)));
    }

    @Test
    void SourceIsICollectionTWithFewElements() {
        Integer[] source = {-5, null, 0, 10, 3, -1, null, 4, 9};
        Integer[] expected = {-5, null, 0, 10, 3, -1, null, 4, 9};

        ICollection<Integer> collection = Linq.of(source).toArray();

        assertEquals(Linq.of(expected), Linq.of(Linq.of(source).toArray(Integer.class)));
        assertEquals(Linq.of(expected), Linq.of(collection.toArray(Integer.class)));
    }

    @Test
    void SourceNotICollectionAndIsEmpty() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 0);

        assertNull(as(source, ICollection.class));

        assertEmpty(Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void SourceNotICollectionAndHasElements() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-4, 10);
        int[] expected = {-4, -3, -2, -1, 0, 1, 2, 3, 4, 5};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void SourceNotICollectionAndAllNull() {
        IEnumerable<Integer> source = RepeatedNullableNumberGuaranteedNotCollectionType(null, 5);
        Integer[] expected = {null, null, null, null, null};

        assertNull(as(source, ICollection.class));

        assertEquals(Linq.of(expected), Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void ConstantTimeCountPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void ConstantTimeCountPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toArray(String.class)));
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = Linq.range(0, 100).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void ConstantTimeCountEmptyPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = Linq.range(0, 100).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toArray(String.class)));
    }

    @Test
    void NonConstantTimeCountPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1).take(5);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void NonConstantTimeCountPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1).take(5);
        assertEquals(Linq.of("1", "2", "3", "4", "5"), Linq.of(source.toArray(String.class)));
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectSameTypeToArray() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i * 2).skip(1000);
        assertEmpty(Linq.of(source.toArray(Integer.class)));
    }

    @Test
    void NonConstantTimeCountEmptyPartitionSelectDiffTypeToArray() {
        IEnumerable<String> source = NumberRangeGuaranteedNotCollectionType(0, 100).orderBy(i -> i).select(i -> i.toString()).skip(1000);
        assertEmpty(Linq.of(source.toArray(String.class)));
    }

    @ParameterizedTest
    @MethodSource({"JustBelowPowersOfTwoLengths",
            "PowersOfTwoLengths",
            "JustAbovePowersOfTwoLengths"})
    void ToArrayShouldWorkWithSpecialLengthLazyEnumerables(int length) {
        assertTrue(length >= 0);

        IEnumerable<Integer> range = Linq.range(0, length);
        IEnumerable<Integer> lazyEnumerable = ForceNotCollection(range); // We won't go down the IIListProvider path
        assertEquals(range, Linq.of(lazyEnumerable.toArray(Integer.class)));
    }

    @Test
    void ToArray_Cast() {
        Object[] source = {Enum0.First, Enum0.Second, Enum0.Third};
        IEnumerable<Enum0> cast = Linq.of(source).cast(Enum0.class);
        assertIsType(CastIterator.class, cast);
        Enum0[] castArray = cast.toArray(Enum0.class);
        assertIsType(Enum0[].class, castArray);
        assertEquals(Linq.of(Enum0.First, Enum0.Second, Enum0.Third), Linq.of(castArray));
    }

    @Test
    void testToArray() {
        assertThrows(ArgumentNullException.class, () -> Linq.empty().toArray(null));

        Object[] source = {1, 2, 3};
        Object[] target = Linq.of(source).cast(Integer.class).toArray(Integer.class);
        assertEquals(3, target.length);
        assertTrue(Linq.of(source).sequenceEqual(Linq.of(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Integer[] target2 = Linq.of(source2).toArray(Integer.class);
        assertEquals(3, target2.length);
        assertTrue(Linq.of(source2).sequenceEqual(Linq.of(target2)));

        Character[] lst = Linq.of(Arrays.asList('h', 'e', 'l', 'l', 'o')).toArray(Character.class);
        assertEquals(5, lst.length);
        assertEquals("o", lst[4].toString());

        Character[] linkedLst = Linq.of(new LinkedList<>(Arrays.asList('h', 'e', 'l', 'l', 'o'))).toArray(Character.class);
        assertEquals(5, linkedLst.length);
        assertEquals("o", linkedLst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.of(arrChar).toArray(Character.class);
        assertEquals(5, arr.length);
        assertEquals("o", arr[4].toString());

        Character[] hello = Linq.chars("hello").toArray(Character.class);
        assertEquals(5, hello.length);
        assertEquals("o", hello[4].toString());

        Character[] h = Linq.singleton('h').toArray(Character.class);
        assertEquals(1, h.length);
        assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().toArray(Character.class);
        assertEquals(0, empty.length);

        String[] array = {"a", "b"};
        String[] stringArray = Linq.of(array).toArray(String.class);
        stringArray[0] = "c";
        assertEquals("a", array[0]);
        assertEquals("c", stringArray[0]);

        assertEquals(Linq.of(true, false, false), Linq.of(Linq.of(new boolean[]{true, false, false}).toArray(Boolean.class)));
        assertEquals(Linq.of((byte) 1, (byte) 2, (byte) 3), Linq.of(Linq.of(new byte[]{1, 2, 3}).toArray(Byte.class)));
        assertEquals(Linq.of((short) 1, (short) 2, (short) 3), Linq.of(Linq.of(new short[]{1, 2, 3}).toArray(Short.class)));
        assertEquals(Linq.of(1, 2, 3), Linq.of(Linq.of(new int[]{1, 2, 3}).toArray(Integer.class)));
        assertEquals(Linq.of(1L, 2L, 3L), Linq.of(Linq.of(new long[]{1, 2, 3}).toArray(Long.class)));
        assertEquals(Linq.of('a', 'b', 'c'), Linq.of(Linq.of(new char[]{'a', 'b', 'c'}).toArray(Character.class)));
        assertEquals(Linq.of(1f, 2f, 3f), Linq.of(Linq.of(new float[]{1f, 2f, 3f}).toArray(Float.class)));
        assertEquals(Linq.of(1d, 2d, 3d), Linq.of(Linq.of(new double[]{1d, 2d, 3d}).toArray(Double.class)));
    }


    // Consider that two very similar enums is not unheard of, if e.g. two assemblies map the
    // same external source of numbers (codes, response codes, colour codes, etc.) to values.
    private enum Enum0 {
        First,
        Second,
        Third
    }
}
