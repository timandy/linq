package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Action0;
import com.bestvike.function.Action1;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ConcatTest extends TestCase {
    @Test
    public void SameResultsWithQueryAndRepeatCallsInt() {
        this.SameResultsWithQueryAndRepeatCallsInt(Linq.asEnumerable(2, 3, 2, 4, 5), Linq.asEnumerable(1, 9, 4));
    }

    private void SameResultsWithQueryAndRepeatCallsInt(IEnumerable<Integer> first, IEnumerable<Integer> second) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        this.SameResultsWithQueryAndRepeatCallsWorker(first, second);
    }

    @Test
    public void SameResultsWithQueryAndRepeatCallsString() {
        this.SameResultsWithQueryAndRepeatCallsString(Linq.asEnumerable("AAA", "", "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice"), Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS"));
    }

    private void SameResultsWithQueryAndRepeatCallsString(IEnumerable<String> first, IEnumerable<String> second) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        this.SameResultsWithQueryAndRepeatCallsWorker(first, second);
    }

    private <T> void SameResultsWithQueryAndRepeatCallsWorker(IEnumerable<T> first, IEnumerable<T> second) {
        first = first.select(a -> a);
        second = second.select(a -> a);

        this.VerifyEqualsWorker(first.concat(second), first.concat(second));
        this.VerifyEqualsWorker(second.concat(first), second.concat(first));
    }

    @Test
    public void PossiblyEmptyInputs() {
        this.PossiblyEmptyInputs(Linq.empty(), Linq.empty(), Linq.empty());
        this.PossiblyEmptyInputs(Linq.empty(), Linq.asEnumerable(2, 6, 4, 6, 2), Linq.asEnumerable(2, 6, 4, 6, 2));
        this.PossiblyEmptyInputs(Linq.asEnumerable(2, 3, 5, 9), Linq.asEnumerable(8, 10), Linq.asEnumerable(2, 3, 5, 9, 8, 10));
    }

    private void PossiblyEmptyInputs(IEnumerable<Integer> first, IEnumerable<Integer> second, IEnumerable<Integer> expected) {
        this.VerifyEqualsWorker(expected, first.concat(second));
        this.VerifyEqualsWorker(expected.skip(first.count()).concat(expected.take(first.count())), second.concat(first)); // Swap the inputs around
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).concat(Linq.range(0, 3));
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator en = (IEnumerator) iterator;
        Assert.assertFalse(en != null && en.moveNext());
    }

    @Test
    public void FirstNull() {
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((int[]) null).concat(Linq.range(0, 0)));
        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable((int[]) null).concat(null)); // If both inputs are null, throw for "first" first
    }

    @Test
    public void SecondNull() {
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 0).concat(null));
    }

    @Test
    public void VerifyEquals() {
        for (Object[] objects : this.ArraySourcesData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.SelectArraySourcesData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.EnumerableSourcesData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.NonCollectionSourcesData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.ListSourcesData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.ConcatOfConcatsData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.ConcatWithSelfData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.ChainedCollectionConcatData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);

        for (Object[] objects : this.AppendedPrependedConcatAlternationsData())
            //noinspection unchecked
            this.VerifyEquals((IEnumerable<Integer>) objects[0], (IEnumerable<Integer>) objects[1]);
    }

    private void VerifyEquals(IEnumerable<Integer> expected, IEnumerable<Integer> actual) {
        // workaround: xUnit type inference doesn't work if the input type is not T (like IEnumerable<T>)
        this.VerifyEqualsWorker(expected, actual);
    }

    private <T> void VerifyEqualsWorker(IEnumerable<T> expected, IEnumerable<T> actual) {
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

    private IEnumerable<Object[]> ArraySourcesData() {
        return this.GenerateSourcesData(e -> e.toArray(), null);
    }

    private IEnumerable<Object[]> SelectArraySourcesData() {
        return this.GenerateSourcesData(e -> e.select(i -> i).toArray(), null);
    }

    private IEnumerable<Object[]> EnumerableSourcesData() {
        return this.GenerateSourcesData(null, null);
    }

    private IEnumerable<Object[]> NonCollectionSourcesData() {
        return this.GenerateSourcesData(e -> ForceNotCollection(e), null);
    }

    private IEnumerable<Object[]> ListSourcesData() {
        return this.GenerateSourcesData(e -> Linq.asEnumerable(e.toList()), null);
    }

    private IEnumerable<Object[]> ConcatOfConcatsData() {
        return Linq.singleton(new Object[]
                {
                        Linq.range(0, 20),
                        Linq.range(0, 4).concat(Linq.range(4, 6)).concat(Linq.range(10, 3).concat(Linq.range(13, 7)))
                });
    }

    private IEnumerable<Object[]> ConcatWithSelfData() {
        IEnumerable<Integer> source = Linq.repeat(1, 4).concat(Linq.repeat(1, 5));
        source = source.concat(source);

        return Linq.singleton(new Object[]{Linq.repeat(1, 18), source});
    }

    private IEnumerable<Object[]> ChainedCollectionConcatData() {
        return this.GenerateSourcesData(null, e -> Linq.asEnumerable(e.toList()));
    }

    private IEnumerable<Object[]> AppendedPrependedConcatAlternationsData() {
        final int EnumerableCount = 4; // How many enumerables to concat together per test case.

        IEnumerable<Integer> foundation = Linq.empty();
        ArrayList<Integer> expected = new ArrayList<>();
        IEnumerable<Integer> actual = foundation;

        List<Object[]> result = new ArrayList<>();

        // each bit in the last EnumerableCount bits of i represent whether we want to prepend/append a sequence for this iteration.
        // if it's set, we'll prepend. otherwise, we'll append.
        for (int i = 0; i < (1 << EnumerableCount); i++) {
            // each bit in last EnumerableCount bits of j is set if we want to ensure the nth enumerable
            // concat'd is an ICollection.
            // Note: It is important we run over the all-bits-set case, since currently
            // Concat is specialized for when all inputs are ICollection.
            for (int j = 0; j < (1 << EnumerableCount); j++) {
                for (int k = 0; k < EnumerableCount; k++) // k is how much bits we shift by, and also the item that gets appended/prepended.
                {
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

                result.add(new Object[]{Linq.asEnumerable(expected.toArray()), actual.toArray()});

                actual = foundation;
                expected.clear();
            }
        }
        return Linq.asEnumerable(result);
    }

    private IEnumerable<Object[]> GenerateSourcesData(Func1<IEnumerable<Integer>, IEnumerable<Integer>> outerTransform, Func1<IEnumerable<Integer>, IEnumerable<Integer>> innerTransform) {
        Func1<IEnumerable<Integer>, IEnumerable<Integer>> outer = outerTransform == null ? e -> e : outerTransform;
        Func1<IEnumerable<Integer>, IEnumerable<Integer>> inner = innerTransform == null ? e -> e : innerTransform;

        return Linq.range(0, 7).select(i -> {

            IEnumerable<Integer> expected = Linq.range(0, i * 3);
            IEnumerable<Integer> actual = Linq.empty();
            for (int j = 0; j < i; j++) {
                actual = outer.apply(actual.concat(inner.apply(Linq.range(j * 3, 3))));
            }

            return new Object[]{expected, actual};
        });
    }

    private IEnumerable<Object[]> ManyConcatsData() {
        List<Object[]> result = new ArrayList<>();
        result.add(new Object[]{Linq.repeat(Linq.empty(), 256), Linq.empty()});
        result.add(new Object[]{Linq.repeat(Linq.repeat(6, 1), 256), Linq.repeat(6, 256)});
        result.add(new Object[]{Linq.range(0, 500).select(i -> Linq.repeat(i, 1)).reverse(), Linq.range(0, 500).reverse()});
        return Linq.asEnumerable(result);
    }

    @Test
    public void ManyConcats() {
        for (Object[] objects : this.ManyConcatsData())
            //noinspection unchecked
            this.ManyConcats((IEnumerable<IEnumerable<Integer>>) objects[0], (IEnumerable<Integer>) objects[1]);
    }

    private void ManyConcats(IEnumerable<IEnumerable<Integer>> sources, IEnumerable<Integer> expected) {
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> identityTransforms = IdentityTransforms();
        for (Func1<IEnumerable<Integer>, IEnumerable<Integer>> transform : identityTransforms) {
            IEnumerable<Integer> concatee = Linq.empty();
            for (IEnumerable<Integer> source : sources) {
                concatee = concatee.concat(transform.apply(source));
            }

            Assert.assertEquals(sources.sumInt(s -> s.count()), concatee.count());
            this.VerifyEqualsWorker(sources.selectMany(s -> s), concatee);
        }
    }

    @Test
    public void ManyConcatsRunOnce() {
        for (Object[] objects : this.ManyConcatsData())
            //noinspection unchecked
            this.ManyConcatsRunOnce((IEnumerable<IEnumerable<Integer>>) objects[0], (IEnumerable<Integer>) objects[1]);
    }

    private void ManyConcatsRunOnce(IEnumerable<IEnumerable<Integer>> sources, IEnumerable<Integer> expected) {
        List<Func1<IEnumerable<Integer>, IEnumerable<Integer>>> identityTransforms = IdentityTransforms();
        for (Func1<IEnumerable<Integer>, IEnumerable<Integer>> transform : identityTransforms) {
            IEnumerable<Integer> concatee = Linq.empty();
            for (IEnumerable<Integer> source : sources) {
                concatee = concatee.runOnce().concat(transform.apply(source));
            }

            Assert.assertEquals(sources.sumInt(s -> s.count()), concatee.count());
        }
    }

    @Test
    public void CountOfConcatIteratorShouldThrowExceptionOnIntegerOverflow() {
        DelegateBasedCollection<Integer> supposedlyLargeCollection = new DelegateBasedCollection<Integer>() {
            {
                this.CountWorker = () -> Integer.MAX_VALUE;
            }
        };
        DelegateBasedCollection<Integer> tinyCollection = new DelegateBasedCollection<Integer>() {{
            this.CountWorker = () -> 1;
        }};

        Action1<Action0> assertThrow = (testCode) -> assertThrows(ArithmeticException.class, testCode);

        // We need to use checked arithmetic summing up the collections' counts.
        assertThrow.apply(() -> supposedlyLargeCollection.concat(tinyCollection).count());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).count());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).count());

        // This applies to ToArray() and ToList() as well, which try to preallocate the exact size
        // needed if all inputs are ICollections.
        assertThrow.apply(() -> supposedlyLargeCollection.concat(tinyCollection).toArray());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).toArray());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).toArray());

        assertThrow.apply(() -> supposedlyLargeCollection.concat(tinyCollection).toList());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(supposedlyLargeCollection).toList());
        assertThrow.apply(() -> tinyCollection.concat(tinyCollection).concat(tinyCollection).concat(supposedlyLargeCollection).toList());
    }

    @Test
    public void CountOfConcatCollectionChainShouldBeResilientToStackOverflow() {
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

        Assert.assertEquals(0, concatChain.count()); // should not throw a StackOverflowException
        // ToArray needs the count as well, and the process of copying all of the collections
        // to the array should also not be recursive.
        assertEquals(Linq.empty(), concatChain.toArray());
        assertEquals(Linq.empty(), Linq.asEnumerable(concatChain.toList())); // ToList also gets the count beforehand
    }

    @Test
    public void CountOfConcatEnumerableChainShouldBeResilientToStackOverflow() {
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

        Assert.assertEquals(0, concatChain.count());
        // ToArray/ToList do not attempt to preallocate a result of the correct
        // size- if there's just 1 lazy enumerable in the chain, it's impossible
        // to get the count to preallocate without iterating through that, and then
        // when the data is being copied you'd need to iterate through it again
        // (not ideal). So we do not need to worry about those methods having a
        // stack overflow through getting the Count() of the iterator.
    }

    @Test
    public void GettingFirstEnumerableShouldBeResilientToStackOverflow() {
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
            Assert.assertTrue(en.moveNext()); // should not SO
            Assert.assertEquals(0xf00, (long) en.current());
        }
    }

    @Test
    public void GetEnumerableOfConcatCollectionChainFollowedByEnumerableNodeShouldBeResilientToStackOverflow() {
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
            Assert.assertTrue(en.moveNext());
            Assert.assertEquals(0xf00, (long) en.current());
        }
    }

    private IEnumerable<Object[]> GetToArrayDataSources() {
        ArrayList<Object[]> result = new ArrayList<>();

        // Marker at the end
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.asEnumerable(new int[]{0}),
                        Linq.asEnumerable(new int[]{1}),
                        Linq.asEnumerable(new int[]{2}),
                        Linq.singleton(3)
                }
        });

        // Marker at beginning
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.asEnumerable(new int[]{1}),
                        Linq.asEnumerable(new int[]{2}),
                        Linq.asEnumerable(new int[]{3})
                }
        });

        // Marker in middle
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.asEnumerable(new int[]{0}),
                        Linq.singleton(1),
                        Linq.asEnumerable(new int[]{2})
                }});

        // Non-marker in middle
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.asEnumerable(new int[]{1}),
                        Linq.singleton(2)
                }
        });

        // Big arrays (marker in middle)
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.asEnumerable(Linq.range(0, 100).toArray()),
                        Linq.range(100, 100).toArray(),
                        Linq.asEnumerable(Linq.range(200, 100).toArray())
                }
        });

        // Big arrays (non-marker in middle)
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.range(0, 100).toArray(),
                        Linq.asEnumerable(Linq.range(100, 100).toArray()),
                        Linq.range(200, 100).toArray()
                }
        });

        // Interleaved (first marker)
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.singleton(0),
                        Linq.asEnumerable(new int[]{1}),
                        Linq.singleton(2),
                        Linq.asEnumerable(new int[]{3}),
                        Linq.singleton(4)
                }
        });

        // Interleaved (first non-marker)
        result.add(new Object[]{
                new IEnumerable[]{
                        Linq.asEnumerable(new int[]{0}),
                        Linq.singleton(1),
                        Linq.asEnumerable(new int[]{2}),
                        Linq.singleton(3),
                        Linq.asEnumerable(new int[]{4})
                }
        });
        return Linq.asEnumerable(result);
    }

    @Test
    public void CollectionInterleavedWithLazyEnumerables_ToArray() {
        for (Object[] objects : this.GetToArrayDataSources())
            //noinspection unchecked
            this.CollectionInterleavedWithLazyEnumerables_ToArray((IEnumerable<Integer>[]) objects[0]);
    }

    private void CollectionInterleavedWithLazyEnumerables_ToArray(IEnumerable<Integer>[] arrays) {
        // See https://github.com/dotnet/corefx/issues/23680

        IEnumerable<Integer> concats = arrays[0];

        for (int i = 1; i < arrays.length; i++) {
            concats = concats.concat(arrays[i]);
        }

        Integer[] results = concats.toArray(Integer.class);

        for (int i = 0; i < results.length; i++) {
            Assert.assertEquals((long) i, (long) results[i]);
        }
    }

    @Test
    public void testConcat() {
        Assert.assertEquals(6, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).count());
    }
}
