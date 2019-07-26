package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Array;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.adapter.enumerable.ArrayListEnumerable;
import com.bestvike.linq.adapter.enumerable.BooleanArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ByteArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.adapter.enumerable.CharacterArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.DoubleArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.FloatArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.GenericArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.IntegerArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.LinkedListEnumerable;
import com.bestvike.linq.adapter.enumerable.LongArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.ShortArrayEnumerable;
import com.bestvike.linq.adapter.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by 许崇雷 on 2019-06-17.
 */
public class FindLastIndexTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        Predicate1<Integer> predicate = TestCase::IsEven;
        assertEquals(q.findLastIndex(predicate), q.findLastIndex(predicate));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty);

        Predicate1<String> predicate = TestCase::IsNullOrEmpty;
        assertEquals(q.findLastIndex(predicate), q.findLastIndex(predicate));
    }

    @Test
    public void FindLastIndex() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        this.FindLastIndex(Linq.empty(), isEvenFunc, -1);
        this.FindLastIndex(Linq.singleton(4), isEvenFunc, 0);
        this.FindLastIndex(Linq.singleton(5), isEvenFunc, -1);
        this.FindLastIndex(Linq.of(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.FindLastIndex(Linq.of(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.FindLastIndex(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.FindLastIndex(range, i -> i > k, 9);
        }
    }

    private void FindLastIndex(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        assertEquals(expected, source.findLastIndex(predicate));
    }

    @Test
    public void FindLastIndexRunOnce() {
        Predicate1<Integer> isEvenFunc = TestCase::IsEven;
        this.FindLastIndexRunOnce(Linq.empty(), isEvenFunc, -1);
        this.FindLastIndexRunOnce(Linq.singleton(4), isEvenFunc, 0);
        this.FindLastIndexRunOnce(Linq.singleton(5), isEvenFunc, -1);
        this.FindLastIndexRunOnce(Linq.of(5, 9, 3, 7, 4), isEvenFunc, 4);
        this.FindLastIndexRunOnce(Linq.of(5, 8, 9, 3, 7, 11), isEvenFunc, 1);

        Array<Integer> range = Linq.range(1, 10).toArray();
        this.FindLastIndexRunOnce(range, i -> i > 10, -1);
        for (int j = 0; j <= 9; j++) {
            int k = j; // Local copy for iterator
            this.FindLastIndexRunOnce(range, i -> i > k, 9);
        }
    }

    private void FindLastIndexRunOnce(IEnumerable<Integer> source, Predicate1<Integer> predicate, int expected) {
        assertEquals(expected, source.runOnce().findLastIndex(predicate));
    }

    @Test
    public void NullSource_ThrowsArgumentNullException2() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).findLastIndex(i -> i != 0));
    }

    @Test
    public void NullPredicate_ThrowsArgumentNullException() {
        Predicate1<Integer> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).findLastIndex(predicate));
    }

    @Test
    public void testFindLastIndex() {
        assertEquals(-1, Linq.<Integer>empty().findLastIndex(x -> x == 0));

        assertEquals(0, Linq.singleton(1).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.singleton(1).findLastIndex(x -> x == 0));

        assertEquals(2, Linq.of(new boolean[]{true, true, true}).findLastIndex(x -> x));
        assertEquals(-1, Linq.of(new boolean[]{true, true, true}).findLastIndex(x -> !x));

        assertEquals(4, Linq.of(new byte[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(new byte[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(new short[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(new short[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(new int[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(new int[]{1, 2, 3, 2, 1}).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(new long[]{1L, 2L, 3L, 2L, 1L}).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(new long[]{1L, 2L, 3L, 2L, 1L}).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(new float[]{1f, 2f, 3f, 2f, 1f}).findLastIndex(x -> x == 1f));
        assertEquals(-1, Linq.of(new float[]{1f, 2f, 3f, 2f, 1f}).findLastIndex(x -> x == 4f));

        assertEquals(4, Linq.of(new double[]{1d, 2d, 3d, 2d, 1d}).findLastIndex(x -> x == 1d));
        assertEquals(-1, Linq.of(new double[]{1d, 2d, 3d, 2d, 1d}).findLastIndex(x -> x == 4d));

        assertEquals(4, Linq.of(new char[]{'1', '2', '3', '2', '1'}).findLastIndex(x -> x == '1'));
        assertEquals(-1, Linq.of(new char[]{'1', '2', '3', '2', '1'}).findLastIndex(x -> x == '4'));

        assertEquals(4, Linq.chars("12321").findLastIndex(x -> x == '1'));
        assertEquals(-1, Linq.chars("12321").findLastIndex(x -> x == '4'));

        assertEquals(4, Linq.of(1, 2, 3, 2, 1).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(1, 2, 3, 2, 1).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(Arrays.asList(1, 2, 3, 2, 1)).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(Arrays.asList(1, 2, 3, 2, 1)).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1))).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1))).findLastIndex(x -> x == 4));

        assertEquals(4, Linq.of(Collections.unmodifiableList(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1)))).findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(Collections.unmodifiableList(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1)))).findLastIndex(x -> x == 4));

        assertEquals(2, Linq.of(Tuple.create(1, "1"), Tuple.create(2, "2"), Tuple.create(2, "3"), Tuple.create(2, "3")).groupBy(Tuple2::getItem1).elementAt(1).findLastIndex(a -> a.getItem2().equals("3")));
        assertEquals(-1, Linq.of(Tuple.create(1, "1"), Tuple.create(2, "2"), Tuple.create(2, "3"), Tuple.create(2, "3")).groupBy(Tuple2::getItem1).elementAt(1).findLastIndex(a -> a.getItem2().equals("4")));

        assertEquals(4, Linq.of(1, 2, 3, 2, 1).runOnce().findLastIndex(x -> x == 1));
        assertEquals(-1, Linq.of(1, 2, 3, 2, 1).runOnce().findLastIndex(x -> x == 4));
    }

    @Test
    public void testFindLastIndexNullPredicate() {
        assertThrows(ArgumentNullException.class, () -> ((SingletonEnumerable<Integer>) Linq.singleton(1))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((SingletonEnumerable<Integer>) Linq.singleton(1))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((BooleanArrayEnumerable) Linq.of(new boolean[]{true, true, true}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((BooleanArrayEnumerable) Linq.of(new boolean[]{true, true, true}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((ByteArrayEnumerable) Linq.of(new byte[]{1, 2, 3, 2, 1}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((ByteArrayEnumerable) Linq.of(new byte[]{1, 2, 3, 2, 1}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((ShortArrayEnumerable) Linq.of(new short[]{1, 2, 3, 2, 1}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((ShortArrayEnumerable) Linq.of(new short[]{1, 2, 3, 2, 1}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((IntegerArrayEnumerable) Linq.of(new int[]{1, 2, 3, 2, 1}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((IntegerArrayEnumerable) Linq.of(new int[]{1, 2, 3, 2, 1}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((LongArrayEnumerable) Linq.of(new long[]{1L, 2L, 3L, 2L, 1L}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((LongArrayEnumerable) Linq.of(new long[]{1L, 2L, 3L, 2L, 1L}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((FloatArrayEnumerable) Linq.of(new float[]{1f, 2f, 3f, 2f, 1f}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((FloatArrayEnumerable) Linq.of(new float[]{1f, 2f, 3f, 2f, 1f}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((DoubleArrayEnumerable) Linq.of(new double[]{1d, 2d, 3d, 2d, 1d}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((DoubleArrayEnumerable) Linq.of(new double[]{1d, 2d, 3d, 2d, 1d}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((CharacterArrayEnumerable) Linq.of(new char[]{'1', '2', '3', '2', '1'}))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((CharacterArrayEnumerable) Linq.of(new char[]{'1', '2', '3', '2', '1'}))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((CharSequenceEnumerable) Linq.chars("12321"))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((CharSequenceEnumerable) Linq.chars("12321"))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((GenericArrayEnumerable<Integer>) Linq.of(1, 2, 3, 2, 1))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((GenericArrayEnumerable<Integer>) Linq.of(1, 2, 3, 2, 1))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((ArrayListEnumerable<Integer>) Linq.of(Arrays.asList(1, 2, 3, 2, 1)))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((ArrayListEnumerable<Integer>) Linq.of(Arrays.asList(1, 2, 3, 2, 1)))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((LinkedListEnumerable<Integer>) Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1))))._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((LinkedListEnumerable<Integer>) Linq.of(new LinkedList<>(Arrays.asList(1, 2, 3, 2, 1))))._findLastIndex(null));

        assertThrows(ArgumentNullException.class, () -> ((RunOnceLinkedList<Integer>) Linq.of(1, 2, 3, 2, 1).runOnce())._findLastIndex(null));
        assertThrows(ArgumentNullException.class, () -> ((RunOnceLinkedList<Integer>) Linq.of(1, 2, 3, 2, 1).runOnce())._findLastIndex(null));
    }

    @Test
    public void testFindLastIndexPredicate() {
        assertEquals(-1, Linq.of(depts).findLastIndex(dept -> dept.name != null && dept.name.equals("IT")));
        assertEquals(0, Linq.of(depts).findLastIndex(dept -> dept.name != null && dept.name.equals("Sales")));
    }
}
