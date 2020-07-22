package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ConcatTest extends TestCase {
    private static <T> void SameResultsWithQueryAndRepeatCallsWorker(IEnumerable<T> first, IEnumerable<T> second) {
        first = first.select(a -> a);
        second = second.select(a -> a);

        VerifyEqualsWorker(first.concat(second), first.concat(second));
        VerifyEqualsWorker(second.concat(first), second.concat(first));
    }

    private static IEnumerable<Object[]> SameResultsWithQueryAndRepeatCalls_Int_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(2, 3, 2, 4, 5), Linq.of(1, 9, 4));
        return argsList;
    }

    private static IEnumerable<Object[]> SameResultsWithQueryAndRepeatCalls_String_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of("AAA", "", "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice"), Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS"));
        return argsList;
    }

    private static IEnumerable<Object[]> PossiblyEmptyInputs_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.empty(), Linq.empty(), Linq.empty());
        argsList.add(Linq.empty(), Linq.of(2, 6, 4, 6, 2), Linq.of(2, 6, 4, 6, 2));
        argsList.add(Linq.of(2, 3, 5, 9), Linq.of(8, 10), Linq.of(2, 3, 5, 9, 8, 10));
        return argsList;
    }

    private static <T> void VerifyEqualsWorker(IEnumerable<T> expected, IEnumerable<T> actual) {
        // Returns a list of functions that, when applied to enumerable, should return
        // another one that has equivalent contents.
        List<Func1<IEnumerable<T>, IEnumerable<T>>> identityTransforms = IdentityTransforms();

        // We run the transforms N^2 times, by testing all transforms
        // of expected against all transforms of actual.
        for (Func1<IEnumerable<T>, IEnumerable<T>> outTransform : identityTransforms) {
            for (Func1<IEnumerable<T>, IEnumerable<T>> inTransform : identityTransforms) {
                assertEquals(outTransform.apply(expected), inTransform.apply(actual));
            }
        }
    }

    private static IEnumerable<Object[]> ArraySourcesData() {
        return GenerateSourcesData(e -> e.toArray(), null);
    }

    private static IEnumerable<Object[]> SelectArraySourcesData() {
        return GenerateSourcesData(e -> e.select(i -> i).toArray(), null);
    }

    private static IEnumerable<Object[]> EnumerableSourcesData() {
        return GenerateSourcesData(null, null);
    }

    private static IEnumerable<Object[]> NonCollectionSourcesData() {
        return GenerateSourcesData(e -> ForceNotCollection(e), null);
    }

    private static IEnumerable<Object[]> ListSourcesData() {
        return GenerateSourcesData(e -> Linq.of(e.toList()), null);
    }

    private static IEnumerable<Object[]> ConcatOfConcatsData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.range(0, 20),
                Linq.range(0, 4).concat(Linq.range(4, 6)).concat(Linq.range(10, 3).concat(Linq.range(13, 7))));
        return argsList;
    }

    private static IEnumerable<Object[]> ConcatWithSelfData() {
        IEnumerable<Integer> source = Linq.repeat(1, 4).concat(Linq.repeat(1, 5));
        source = source.concat(source);

        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(1, 18), source);
        return argsList;
    }

    private static IEnumerable<Object[]> ChainedCollectionConcatData() {
        return GenerateSourcesData(null, e -> Linq.of(e.toList()));
    }

    private static IEnumerable<Object[]> AppendedPrependedConcatAlternationsData() {
        final int EnumerableCount = 4; // How many enumerables to concat together per test case.

        IEnumerable<Integer> foundation = Linq.empty();
        ArrayList<Integer> expected = new ArrayList<>();
        IEnumerable<Integer> actual = foundation;

        ArgsList argsList = new ArgsList();

        // each bit in the last EnumerableCount bits of i represent whether we want to prepend/append a sequence for this iteration.
        // if it's set, we'll prepend. otherwise, we'll append.
        for (int i = 0; i < (1 << EnumerableCount); i++) {
            // each bit in last EnumerableCount bits of j is set if we want to ensure the nth enumerable
            // concat'd is an ICollection.
            // Note: It is important we run over the all-bits-set case, since currently
            // Concat is specialized for when all inputs are ICollection.
            for (int j = 0; j < (1 << EnumerableCount); j++) {
                for (int k = 0; k < EnumerableCount; k++) {// k is how much bits we shift by, and also the item that gets appended/prepended.
                    IEnumerable<Integer> nextRange = Linq.range(k, 1);
                    boolean prepend = ((i >> k) & 1) != 0;
                    boolean forceCollection = ((j >> k) & 1) != 0;

                    if (forceCollection) {
                        nextRange = nextRange.toArray();
                    }

                    actual = prepend ? nextRange.concat(actual) : actual.concat(nextRange);
                    if (prepend) {
                        expected.add(0, k);
                    } else {
                        expected.add(k);
                    }
                }

                argsList.add(Linq.of(expected.toArray()), actual.toArray());

                actual = foundation;
                expected.clear();
            }
        }
        return argsList;
    }

    private static IEnumerable<Object[]> GenerateSourcesData(Func1<IEnumerable<Integer>, IEnumerable<Integer>> outerTransform, Func1<IEnumerable<Integer>, IEnumerable<Integer>> innerTransform) {
        Func1<IEnumerable<Integer>, IEnumerable<Integer>> outer = outerTransform == null ? e -> e : outerTransform;
        Func1<IEnumerable<Integer>, IEnumerable<Integer>> inner = innerTransform == null ? e -> e : innerTransform;

        ArgsList argsList = new ArgsList();
        for (int i = 0; i <= 6; i++) {
            IEnumerable<Integer> expected = Linq.range(0, i * 3);
            IEnumerable<Integer> actual = Linq.empty();
            for (int j = 0; j < i; j++) {
                actual = outer.apply(actual.concat(inner.apply(Linq.range(j * 3, 3))));
            }
            argsList.add(expected, actual);
        }
        return argsList;
    }

    private static IEnumerable<Object[]> ManyConcatsData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.repeat(Linq.empty(), 256));
        argsList.add(Linq.repeat(Linq.repeat(6, 1), 256));
        argsList.add(Linq.range(0, 500).select(i -> Linq.repeat(i, 1)).reverse());
        return argsList;
    }

    private static IEnumerable<Object[]> GetToArrayDataSources() {
        ArgsList argsList = new ArgsList();

        // Marker at the end
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        Linq.of(new int[]{1}),
                        Linq.of(new int[]{2}),
                        Linq.singleton(3)
                }
        });

        // Marker at beginning
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.of(new int[]{1}),
                        Linq.of(new int[]{2}),
                        Linq.of(new int[]{3})
                }
        });

        // Marker in middle
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        Linq.singleton(1),
                        Linq.of(new int[]{2})
                }});

        // Non-marker in middle
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.of(new int[]{1}),
                        Linq.singleton(2)
                }
        });

        // Big arrays (marker in middle)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(Linq.range(0, 100).toArray()),
                        Linq.range(100, 100).toArray(),
                        Linq.of(Linq.range(200, 100).toArray())
                }
        });

        // Big arrays (non-marker in middle)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.range(0, 100).toArray(),
                        Linq.of(Linq.range(100, 100).toArray()),
                        Linq.range(200, 100).toArray()
                }
        });

        // Interleaved (first marker)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.of(new int[]{1}),
                        Linq.singleton(2),
                        Linq.of(new int[]{3}),
                        Linq.singleton(4)
                }
        });

        // Interleaved (first non-marker)
        argsList.add(new Object[]{
                new IEnumerable[]{
                        Linq.of(new int[]{0}),
                        Linq.singleton(1),
                        Linq.of(new int[]{2}),
                        Linq.singleton(3),
                        Linq.of(new int[]{4})
                }
        });
        return argsList;
    }

    @ParameterizedTest
    @MethodSource("SameResultsWithQueryAndRepeatCalls_Int_TestData")
    void SameResultsWithQueryAndRepeatCalls_Int(IEnumerable<Integer> first, IEnumerable<Integer> second) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        SameResultsWithQueryAndRepeatCallsWorker(first, second);
    }

    @ParameterizedTest
    @MethodSource("SameResultsWithQueryAndRepeatCalls_String_TestData")
    void SameResultsWithQueryAndRepeatCalls_String(IEnumerable<String> first, IEnumerable<String> second) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        SameResultsWithQueryAndRepeatCallsWorker(first, second);
    }

    @ParameterizedTest
    @MethodSource("PossiblyEmptyInputs_TestData")
    void PossiblyEmptyInputs(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        VerifyEqualsWorker(expected, first.concat(second));
        VerifyEqualsWorker(expected.skip(first.count()).concat(expected.take(first.count())), second.concat(first)); // Swap the inputs around
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).concat(Linq.range(0, 3));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = (IEnumerator) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void FirstNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).concat(Linq.range(0, 0)));
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).concat(null)); // If both inputs are null, throw for "first" first
    }

    @Test
    void SecondNull() {
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 0).concat(null));
    }

    @ParameterizedTest
    @MethodSource({"ArraySourcesData",
            "SelectArraySourcesData",
            "EnumerableSourcesData",
            "NonCollectionSourcesData",
            "ListSourcesData",
            "ConcatOfConcatsData",
            "ConcatWithSelfData",
            "ChainedCollectionConcatData",
            "AppendedPrependedConcatAlternationsData"})
    void VerifyEquals(IEnumerable<Integer> expected, IEnumerable<Integer> actual) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        VerifyEqualsWorker(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("ManyConcatsData")
    void ManyConcats(IEnumerable<IEnumerable<Integer>> sources) {
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> identityTransforms = IdentityTransforms();
        for (Func1<IEnumerable<Integer>, IEnumerable<Integer>> transform : identityTransforms) {
            IEnumerable<Integer> concatee = Linq.empty();
            for (IEnumerable<Integer> source : sources) {
                concatee = concatee.concat(transform.apply(source));
            }

            assertEquals(sources.sumInt(s -> s.count()), concatee.count());
            VerifyEqualsWorker(sources.selectMany(s -> s), concatee);
        }
    }

    @ParameterizedTest
    @MethodSource("ManyConcatsData")
    void ManyConcatsRunOnce(IEnumerable<IEnumerable<Integer>> sources) {
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> identityTransforms = IdentityTransforms();
        for (Func1<IEnumerable<Integer>, IEnumerable<Integer>> transform : identityTransforms) {
            IEnumerable<Integer> concatee = Linq.empty();
            for (IEnumerable<Integer> source : sources) {
                concatee = concatee.runOnce().concat(transform.apply(source));
            }

            assertEquals(sources.sumInt(s -> s.count()), concatee.count());
        }
    }

    @Test
    void CountOfConcatIteratorShouldThrowExceptionOnIntegerOverflow() {
        DelegateBasedCollection<Integer> supposedlyLargeCollection = new DelegateBasedCollection<Integer>() {{
            this.CountWorker = () -> Integer.MAX_VALUE;
        }};
        DelegateBasedCollection<Integer> tinyCollection = new DelegateBasedCollection<Integer>() {{
            this.CountWorker = () -> 1;
        }};

        // We need to use checked arithmetic summing up the collections' counts.
        assertThrows(ArithmeticException.class, () -> supposedlyLargeCollection.concat(tinyCollection).count());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).count());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).count());

        // This applies to ToArray() and ToList() as well, which try to preallocate the exact size
        // needed if all inputs are ICollections.
        assertThrows(ArithmeticException.class, () -> supposedlyLargeCollection.concat(tinyCollection).toArray());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).toArray());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).toArray());

        assertThrows(ArithmeticException.class, () -> supposedlyLargeCollection.concat(tinyCollection).toList());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).toList());
        assertThrows(ArithmeticException.class, () -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).toList());
    }

    @Test
    void CountOfConcatCollectionChainShouldBeResilientToStackOverflow() {
        // Currently, .Concat chains of 3+ ICollections are represented as a
        // singly-linked list of iterators, each holding 1 collection. When
        // we call Count() on the last iterator, it needs to sum up the count
        // of its collection, plus the count of all the previous collections.

        // It is tempting to use recursion to solve this problem, by simply
        // returning [this collection].Count + [previous iterator].Count(),
        // since it is so much simpler than the iterative solution.
        // However, this can lead to a stack overflow for long chains of
        // .Concats. This is a test to guard against that.

        // Something big enough to cause a SO when this many method calls
        // are made, but not too large.
        final int NumberOfConcats = 30000;

        // For perf reasons (this test can take a long time to run)
        // we use a for-loop manually rather than .Repeat and .Aggregate
        IEnumerable<Integer> concatChain = Linq.empty(); // note: all items in this chain must implement ICollection<T>
        for (int i = 0; i < NumberOfConcats; i++) {
            concatChain = concatChain.concat(Linq.empty());
        }

        assertEquals(0, concatChain.count()); // should not throw a StackOverflowException
        // ToArray needs the count as well, and the process of copying all of the collections
        // to the array should also not be recursive.
        assertEquals(Linq.empty(), concatChain.toArray());
        assertEquals(Linq.empty(), Linq.of(concatChain.toList())); // ToList also gets the count beforehand
    }

    @Test
    void CountOfConcatEnumerableChainShouldBeResilientToStackOverflow() {
        // Concat chains where one or more of the inputs is not an ICollection
        // (so we cannot figure out the total count w/o iterating through some
        // of the enumerables) are represented much the same, as singly-linked lists.
        // However, the underlying iterator type/implementation of Count()
        // will be different (it treats all of the concatenated enumerables as lazy),
        // so we have to make sure that implementation isn't recursive either.

        final int NumberOfConcats = 30000;

        // Start with a non-ICollection seed, which means all subsequent
        // Concats will be treated as lazy enumerables.
        IEnumerable<Integer> concatChain = ForceNotCollection(Linq.empty());

        // Then, concatenate a bunch of ICollections to it.
        // Since Count() is optimized for when its input is an ICollection,
        // this will reduce the time it takes to run the test, which otherwise
        // would take quite long.
        for (int i = 0; i < NumberOfConcats; i++) {
            concatChain = concatChain.concat(Linq.empty());
        }

        assertEquals(0, concatChain.count());
        // ToArray/ToList do not attempt to preallocate a result of the correct
        // size- if there's just 1 lazy enumerable in the chain, it's impossible
        // to get the count to preallocate without iterating through that, and then
        // when the data is being copied you'd need to iterate through it again
        // (not ideal). So we do not need to worry about those methods having a
        // stack overflow through getting the Count() of the iterator.
    }

    @Test
    void GettingFirstEnumerableShouldBeResilientToStackOverflow() {
        // When MoveNext() is first called on a chain of 3+ Concats, we have to
        // walk all the way to the tail of the linked list to reach the first
        // concatee. Likewise as before, make sure this isn't accomplished with
        // a recursive implementation.

        final int NumberOfConcats = 30000;

        // Start with a lazy seed.
        // The seed holds 1 item, so during the first MoveNext we won't have to
        // backtrack through the linked list 30000 times. This is for test perf.
        IEnumerable<Integer> concatChain = ForceNotCollection(Linq.singleton(0xf00));

        for (int i = 0; i < NumberOfConcats; i++) {
            concatChain = concatChain.concat(Linq.empty());
        }

        try (IEnumerator<Integer> en = concatChain.enumerator()) {
            assertTrue(en.moveNext()); // should not SO
            assertEquals(0xf00, (long) en.current());
        }
    }

    @Test
    void GetEnumerableOfConcatCollectionChainFollowedByEnumerableNodeShouldBeResilientToStackOverflow() {
        // Since concatenating even 1 lazy enumerable ruins the ability for ToArray/ToList
        // optimizations, if one is Concat'd to a collection-based iterator then we will
        // fall back to treating all subsequent concatenations as lazy.
        // Since it is possible for an enumerable iterator to be preceded by a collection
        // iterator, but not the other way around, an assumption is made. If an enumerable
        // iterator encounters a collection iterator during GetEnumerable(), it calls into
        // the collection iterator's GetEnumerable() under the assumption that the callee
        // will never call into another enumerable iterator's GetEnumerable(). This is
        // because collection iterators can only be preceded by other collection iterators.

        // Violation of this assumption means that the GetEnumerable() implementations could
        // become mutually recursive, which may lead to stack overflow for enumerable iterators
        // preceded by a long chain of collection iterators.

        final int NumberOfConcats = 30000;

        // This time, start with an ICollection seed. We want the subsequent Concats in
        // the loop to produce collection iterators.
        IEnumerable<Integer> concatChain = Linq.singleton(0xf00);

        for (int i = 0; i < NumberOfConcats - 1; i++) {
            concatChain = concatChain.concat(Linq.empty());
        }

        // Finally, link an enumerable iterator at the head of the list.
        concatChain = concatChain.concat(ForceNotCollection(Linq.empty()));

        try (IEnumerator<Integer> en = concatChain.enumerator()) {
            assertTrue(en.moveNext());
            assertEquals(0xf00, (long) en.current());
        }
    }

    @ParameterizedTest
    @MethodSource("GetToArrayDataSources")
    void CollectionInterleavedWithLazyEnumerables_ToArray(IEnumerable<Integer>[] arrays) {
        // See https://github.com/dotnet/runtime/issues/23389

        IEnumerable<Integer> concats = arrays[0];

        for (int i = 1; i < arrays.length; i++) {
            concats = concats.concat(arrays[i]);
        }

        Array<Integer> results = concats.toArray();
        Integer[] results2 = concats.toArray(Integer.class);

        for (int i = 0; i < results2.length; i++) {
            assertEquals(i, results.get(i));
            assertEquals(i, results2[i]);
        }
    }

    @Test
    void testConcat() {
        assertEquals(6, Linq.of(emps).concat(Linq.of(badEmps)).count());

        assertEquals(Linq.of(true, false, false), Linq.singleton(true).concat(Linq.of(new boolean[]{false, false})).toArray());
        assertEquals(Arrays.asList(true, false, false), Linq.singleton(true).concat(Linq.of(new boolean[]{false, false})).toList());

        assertEquals(Linq.of((byte) 1, (byte) 2, (byte) 3), Linq.singleton((byte) 1).concat(Linq.of(new byte[]{2, 3})).toArray());
        assertEquals(Arrays.asList((byte) 1, (byte) 2, (byte) 3), Linq.singleton((byte) 1).concat(Linq.of(new byte[]{2, 3})).toList());

        assertEquals(Linq.of((short) 1, (short) 2, (short) 3), Linq.singleton((short) 1).concat(Linq.of(new short[]{2, 3})).toArray());
        assertEquals(Arrays.asList((short) 1, (short) 2, (short) 3), Linq.singleton((short) 1).concat(Linq.of(new short[]{2, 3})).toList());

        assertEquals(Linq.of(1, 2, 3), Linq.singleton(1).concat(Linq.of(new int[]{2, 3})).toArray());
        assertEquals(Arrays.asList(1, 2, 3), Linq.singleton(1).concat(Linq.of(new int[]{2, 3})).toList());

        assertEquals(Linq.of(1L, 2L, 3L), Linq.singleton(1L).concat(Linq.of(new long[]{2, 3})).toArray());
        assertEquals(Arrays.asList(1L, 2L, 3L), Linq.singleton(1L).concat(Linq.of(new long[]{2, 3})).toList());

        assertEquals(Linq.of('a', 'b', 'c'), Linq.singleton('a').concat(Linq.of(new char[]{'b', 'c'})).toArray());
        assertEquals(Arrays.asList('a', 'b', 'c'), Linq.singleton('a').concat(Linq.of(new char[]{'b', 'c'})).toList());

        assertEquals(Linq.of(1f, 2f, 3f), Linq.singleton(1f).concat(Linq.of(new float[]{2f, 3f})).toArray());
        assertEquals(Arrays.asList(1f, 2f, 3f), Linq.singleton(1f).concat(Linq.of(new float[]{2f, 3f})).toList());

        assertEquals(Linq.of(1d, 2d, 3d), Linq.singleton(1d).concat(Linq.of(new double[]{2d, 3d})).toArray());
        assertEquals(Arrays.asList(1d, 2d, 3d), Linq.singleton(1d).concat(Linq.of(new double[]{2d, 3d})).toList());

        assertEquals(Linq.of(1, 2, 3), Linq.singleton(1).concat(Linq.of(Collections.unmodifiableCollection(Arrays.asList(2, 3)))).toArray());
        assertEquals(Arrays.asList(1, 2, 3), Linq.singleton(1).concat(Linq.of(Collections.unmodifiableCollection(Arrays.asList(2, 3)))).toList());
    }
}
