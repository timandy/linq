package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-05-24.
 */
class OrderedSubsettingTest extends TestCase {
    @Test
    void FirstMultipleTruePredicateResult() {
        assertEquals(10, Linq.range(1, 99).orderBy(i -> i).first(x -> x % 10 == 0));
        assertEquals(100, Linq.range(1, 999).concat(Linq.range(1001, 3)).orderByDescending(i -> i.toString().length()).thenBy(i -> i).first(x -> x % 10 == 0));
    }

    @Test
    void FirstOrDefaultMultipleTruePredicateResult() {
        assertEquals(10, Linq.range(1, 99).orderBy(i -> i).firstOrDefault(x -> x % 10 == 0));
        assertEquals(100, Linq.range(1, 999).concat(Linq.range(1001, 3)).orderByDescending(i -> i.toString().length()).thenBy(i -> i).firstOrDefault(x -> x % 10 == 0));
    }

    @Test
    void FirstNoTruePredicateResult() {
        assertThrows(InvalidOperationException.class, () -> Linq.range(1, 99).orderBy(i -> i).first(x -> x > 1000));
    }

    @Test
    void FirstEmptyOrderedEnumerable() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).first());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).first(x -> true));
    }

    @Test
    void FirstOrDefaultNoTruePredicateResult() {
        assertEquals(null, Linq.range(1, 99).orderBy(i -> i).firstOrDefault(x -> x > 1000));
    }

    @Test
    void FirstOrDefaultEmptyOrderedEnumerable() {
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).firstOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).firstOrDefault(x -> true));
    }

    @Test
    void Last() {
        assertEquals(10, Linq.range(1, 99).reverse().orderByDescending(i -> i).last(x -> x % 10 == 0));
        assertEquals(100, Linq.range(1, 999).concat(Linq.range(1001, 3)).reverse().orderBy(i -> i.toString().length()).thenByDescending(i -> i).last(x -> x % 10 == 0));
        assertEquals(10, Linq.range(1, 10).orderBy(i -> 1).last());
    }

    @Test
    void LastMultipleTruePredicateResult() {
        assertEquals(90, Linq.range(1, 99).orderBy(i -> i).last(x -> x % 10 == 0));
        assertEquals(90, Linq.range(1, 999).concat(Linq.range(1001, 3)).orderByDescending(i -> i.toString().length()).thenBy(i -> i).last(x -> x % 10 == 0));
    }

    @Test
    void LastOrDefaultMultipleTruePredicateResult() {
        assertEquals(90, Linq.range(1, 99).orderBy(i -> i).lastOrDefault(x -> x % 10 == 0));
        assertEquals(90, Linq.range(1, 999).concat(Linq.range(1001, 3)).orderByDescending(i -> i.toString().length()).thenBy(i -> i).lastOrDefault(x -> x % 10 == 0));
    }

    @Test
    void LastNoTruePredicateResult() {
        assertThrows(InvalidOperationException.class, () -> Linq.range(1, 99).orderBy(i -> i).last(x -> x > 1000));
    }

    @Test
    void LastEmptyOrderedEnumerable() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).last());
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).last(x -> true));
    }

    @Test
    void LastOrDefaultNoTruePredicateResult() {
        assertEquals(null, Linq.range(1, 99).orderBy(i -> i).lastOrDefault(x -> x > 1000));
    }

    @Test
    void LastOrDefaultEmptyOrderedEnumerable() {
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).lastOrDefault(x -> true));
    }

    @Test
    void Take() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(Linq.range(0, 20), ordered.take(20));
        assertEquals(Linq.range(0, 30), ordered.take(50).take(30));
        assertEmpty(ordered.take(0));
        assertEmpty(ordered.take(-1));
        assertEmpty(ordered.take(Integer.MIN_VALUE));
        assertEquals(Linq.of(new int[]{0}), ordered.take(1));
        assertEquals(Linq.range(0, 100), ordered.take(101));
        assertEquals(Linq.range(0, 100), ordered.take(Integer.MAX_VALUE));
        assertEquals(Linq.range(0, 100), ordered.take(100));
        assertEquals(Linq.range(0, 99), ordered.take(99));
        assertEquals(Linq.range(0, 100), ordered);
    }

    @Test
    void TakeThenFirst() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(0, ordered.take(20).first());
        assertEquals(0, ordered.take(20).firstOrDefault());
    }

    @Test
    void TakeThenLast() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(19, ordered.take(20).last());
        assertEquals(19, ordered.take(20).lastOrDefault());
    }

    @Test
    void Skip() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(Linq.range(20, 80), ordered.skip(20));
        assertEquals(Linq.range(80, 20), ordered.skip(50).skip(30));
        assertEquals(20, ordered.skip(20).first());
        assertEquals(20, ordered.skip(20).firstOrDefault());
        assertEquals(Linq.range(0, 100), ordered.skip(0));
        assertEquals(Linq.range(0, 100), ordered.skip(-1));
        assertEquals(Linq.range(0, 100), ordered.skip(Integer.MIN_VALUE));
        assertEquals(Linq.of(new int[]{99}), ordered.skip(99));
        assertEmpty(ordered.skip(101));
        assertEmpty(ordered.skip(Integer.MAX_VALUE));
        assertEmpty(ordered.skip(100));
        assertEquals(Linq.range(1, 99), ordered.skip(1));
        assertEquals(Linq.range(0, 100), ordered);
    }

    @Test
    void SkipThenFirst() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(20, ordered.skip(20).first());
        assertEquals(20, ordered.skip(20).firstOrDefault());
    }

    @Test
    void SkipExcessiveThenFirstThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.range(2, 10).shuffle().orderBy(i -> i).skip(20).first());
    }

    @Test
    void SkipExcessiveThenFirstOrDefault() {
        assertEquals(null, Linq.range(2, 10).shuffle().orderBy(i -> i).skip(20).firstOrDefault());
    }

    @Test
    void SkipExcessiveEmpty() {
        assertEmpty(Linq.range(0, 10).shuffle().orderBy(i -> i).skip(42));
    }

    @Test
    void SkipThenLast() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(99, ordered.skip(20).last());
        assertEquals(99, ordered.skip(20).lastOrDefault());
    }

    @Test
    void SkipExcessiveThenLastThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.range(2, 10).shuffle().orderBy(i -> i).skip(20).last());
    }

    @Test
    void SkipExcessiveThenLastOrDefault() {
        assertEquals(null, Linq.range(2, 10).shuffle().orderBy(i -> i).skip(20).lastOrDefault());
    }

    @Test
    void SkipAndTake() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(Linq.range(20, 60), ordered.skip(20).take(60));
        assertEquals(Linq.range(30, 20), ordered.skip(20).skip(10).take(50).take(20));
        assertEquals(Linq.range(30, 20), ordered.skip(20).skip(10).take(20).take(Integer.MAX_VALUE));
        assertEmpty(ordered.skip(10).take(9).take(0));
        assertEmpty(ordered.skip(200).take(10));
        assertEmpty(ordered.skip(3).take(0));
    }

    @Test
    void TakeAndSkip() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEmpty(ordered.skip(100).take(20));
        assertEquals(Linq.range(10, 20), ordered.take(30).skip(10));
        assertEquals(Linq.range(10, 1), ordered.take(11).skip(10));
    }

    @Test
    void TakeAndSkip_DoesntIterateRangeUnlessNecessary() {
        assertEmpty(Linq.range(0, Integer.MAX_VALUE).take(Integer.MAX_VALUE).orderBy(i -> i).skip(Integer.MAX_VALUE - 4).skip(15));
    }

    @Test
    void TakeThenTakeExcessive() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(ordered.take(20), ordered.take(20).take(100));
    }

    @Test
    void TakeThenSkipAll() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEmpty(ordered.take(20).skip(30));
    }

    @Test
    void SkipAndTakeThenFirst() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(20, ordered.skip(20).take(60).first());
        assertEquals(20, ordered.skip(20).take(60).firstOrDefault());
    }

    @Test
    void SkipAndTakeThenLast() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(79, ordered.skip(20).take(60).last());
        assertEquals(79, ordered.skip(20).take(60).lastOrDefault());
    }

    @Test
    void ElementAt() {
        Array<Integer> source = Linq.range(0, 100).shuffle().toArray();
        IEnumerable<Integer> ordered = source.orderBy(i -> i);
        assertEquals(42, ordered.elementAt(42));
        assertEquals(93, ordered.elementAt(93));
        assertEquals(99, ordered.elementAt(99));
        assertEquals(42, ordered.elementAtOrDefault(42));
        assertEquals(93, ordered.elementAtOrDefault(93));
        assertEquals(99, ordered.elementAtOrDefault(99));
        assertThrows(ArgumentOutOfRangeException.class, () -> ordered.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> ordered.elementAt(100));
        assertThrows(ArgumentOutOfRangeException.class, () -> ordered.elementAt(1000));
        assertThrows(ArgumentOutOfRangeException.class, () -> ordered.elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> ordered.elementAt(Integer.MAX_VALUE));
        assertEquals(null, ordered.elementAtOrDefault(-1));
        assertEquals(null, ordered.elementAtOrDefault(100));
        assertEquals(null, ordered.elementAtOrDefault(1000));
        assertEquals(null, ordered.elementAtOrDefault(Integer.MIN_VALUE));
        assertEquals(null, ordered.elementAtOrDefault(Integer.MAX_VALUE));
        IEnumerable<Integer> skipped = ordered.skip(10).take(80);
        assertEquals(52, skipped.elementAt(42));
        assertEquals(83, skipped.elementAt(73));
        assertEquals(89, skipped.elementAt(79));
        assertEquals(52, skipped.elementAtOrDefault(42));
        assertEquals(83, skipped.elementAtOrDefault(73));
        assertEquals(89, skipped.elementAtOrDefault(79));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped.elementAt(80));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped.elementAt(1000));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped.elementAt(Integer.MIN_VALUE));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped.elementAt(Integer.MAX_VALUE));
        assertEquals(null, skipped.elementAtOrDefault(-1));
        assertEquals(null, skipped.elementAtOrDefault(80));
        assertEquals(null, skipped.elementAtOrDefault(1000));
        assertEquals(null, skipped.elementAtOrDefault(Integer.MIN_VALUE));
        assertEquals(null, skipped.elementAtOrDefault(Integer.MAX_VALUE));
        IEnumerable<Integer> skipped2 = ordered.skip(1000).take(20);
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped2.elementAt(-1));
        assertThrows(ArgumentOutOfRangeException.class, () -> skipped2.elementAt(0));
        assertThrows(InvalidOperationException.class, () -> skipped2.first());
        assertThrows(InvalidOperationException.class, () -> skipped2.last());
        assertEquals(null, skipped2.elementAtOrDefault(-1));
        assertEquals(null, skipped2.elementAtOrDefault(0));
        assertEquals(null, skipped2.firstOrDefault());
        assertEquals(null, skipped2.lastOrDefault());
    }

    @Test
    void ToArray() {
        assertEquals(Linq.range(10, 20), Linq.range(0, 100).shuffle().orderBy(i -> i).skip(10).take(20).toArray());
    }

    @Test
    void ToList() {
        assertEquals(Linq.range(10, 20), Linq.of(Linq.range(0, 100).shuffle().orderBy(i -> i).skip(10).take(20).toList()));
    }

    @Test
    void Count() {
        assertEquals(20, Linq.range(0, 100).shuffle().orderBy(i -> i).skip(10).take(20).count());
        assertEquals(1, Linq.range(0, 100).shuffle().orderBy(i -> i).take(2).skip(1).count());
    }

    @Test
    void SkipTakesOnlyOne() {
        assertEquals(Linq.of(new int[]{1}), Linq.range(1, 10).shuffle().orderBy(i -> i).take(1));
        assertEquals(Linq.of(new int[]{2}), Linq.range(1, 10).shuffle().orderBy(i -> i).skip(1).take(1));
        assertEquals(Linq.of(new int[]{3}), Linq.range(1, 10).shuffle().orderBy(i -> i).take(3).skip(2));
        assertEquals(Linq.of(new int[]{1}), Linq.range(1, 10).shuffle().orderBy(i -> i).take(3).take(1));
    }

    @Test
    void EmptyToArray() {
        assertEmpty(Linq.range(0, 100).shuffle().orderBy(i -> i).skip(100).toArray());
        assertTrue(Linq.range(0, 100).shuffle().orderBy(i -> i).skip(100).toArray(Integer.class).length == 0);
    }

    @Test
    void EmptyToList() {
        assertEmpty(Linq.of(Linq.range(0, 100).shuffle().orderBy(i -> i).skip(100).toList()));
    }

    @Test
    void EmptyCount() {
        assertEquals(0, Linq.range(0, 100).shuffle().orderBy(i -> i).skip(100).count());
        assertEquals(0, Linq.range(0, 100).shuffle().orderBy(i -> i).take(0).count());
    }

    @Test
    void AttemptedMoreArray() {
        assertEquals(Linq.range(0, 20), Linq.range(0, 20).shuffle().orderBy(i -> i).take(30).toArray());
    }

    @Test
    void AttemptedMoreList() {
        assertEquals(Linq.range(0, 20), Linq.of(Linq.range(0, 20).shuffle().orderBy(i -> i).take(30).toList()));
    }

    @Test
    void AttemptedMoreCount() {
        assertEquals(20, Linq.range(0, 20).shuffle().orderBy(i -> i).take(30).count());
    }

    @Test
    void SingleElementToArray() {
        assertEquals(Linq.repeat(10, 1), Linq.range(0, 20).shuffle().orderBy(i -> i).skip(10).take(1).toArray());
    }

    @Test
    void SingleElementToList() {
        assertEquals(Linq.repeat(10, 1), Linq.of(Linq.range(0, 20).shuffle().orderBy(i -> i).skip(10).take(1).toList()));
    }

    @Test
    void SingleElementCount() {
        assertEquals(1, Linq.range(0, 20).shuffle().orderBy(i -> i).skip(10).take(1).count());
    }

    @Test
    void EnumeratorDoesntContinue() {
        IEnumerator<Integer> enumerator = NumberRangeGuaranteedNotCollectionType(0, 3).shuffle().orderBy(i -> i).skip(1).enumerator();
        while (enumerator.moveNext()) {
        }
        assertFalse(enumerator.moveNext());
    }

    @Test
    void Select() {
        assertEquals(Linq.of(new int[]{0, 2, 4, 6, 8}), Linq.range(-1, 8).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2));
    }

    @Test
    void SelectForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = Linq.range(-1, 8).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void SelectElementAt() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(6, source.elementAt(2));
        assertEquals(8, source.elementAtOrDefault(3));
        assertEquals(null, source.elementAtOrDefault(8));
        assertThrows(ArgumentOutOfRangeException.class, () -> source.elementAt(-2));
    }

    @Test
    void SelectFirst() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(2, source.first());
        assertEquals(2, source.firstOrDefault());
        IEnumerable<Integer> source2 = source.skip(20);
        assertEquals(null, source2.firstOrDefault());
        assertThrows(InvalidOperationException.class, () -> source2.first());
    }

    @Test
    void SelectLast() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(10, source.last());
        assertEquals(10, source.lastOrDefault());
        IEnumerable<Integer> source2 = source.skip(20);
        assertEquals(null, source2.lastOrDefault());
        assertThrows(InvalidOperationException.class, () -> source2.last());
    }

    @Test
    void SelectArray() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), source.toArray());
    }

    @Test
    void SelectList() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(Linq.of(new int[]{2, 4, 6, 8, 10}), Linq.of(source.toList()));
    }

    @Test
    void SelectCount() {
        IEnumerable<Integer> source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(5).select(i -> i * 2);
        assertEquals(5, source.count());
        source = Linq.range(0, 9).shuffle().orderBy(i -> i).skip(1).take(1000).select(i -> i * 2);
        assertEquals(8, source.count());
    }

    @Test
    void RunOnce() {
        IEnumerable<Integer> source = Linq.range(0, 100).shuffle().toArray();
        assertEquals(Linq.range(30, 20), source.runOnce().orderBy(i -> i).skip(20).skip(10).take(50).take(20));
        assertEmpty(source.runOnce().orderBy(i -> i).skip(10).take(9).take(0));
        assertEquals(20, source.runOnce().orderBy(i -> i).skip(20).take(60).first());
        assertEquals(79, source.runOnce().orderBy(i -> i).skip(20).take(60).last());
        assertEquals(93, source.runOnce().orderBy(i -> i).elementAt(93));
        assertEquals(42, source.runOnce().orderBy(i -> i).elementAtOrDefault(42));
        assertEquals(20, source.runOnce().orderBy(i -> i).skip(10).take(20).count());
        assertEquals(1, source.runOnce().orderBy(i -> i).take(2).skip(1).count());
    }
}
