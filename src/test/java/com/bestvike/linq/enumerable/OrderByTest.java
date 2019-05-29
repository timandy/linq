package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class OrderByTest extends TestCase {
    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Tuple2<Integer, Integer>> q = Linq.asEnumerable(new int[]{1, 6, 0, -1, 3})
                .selectMany(x1 -> Linq.asEnumerable(new int[]{55, 49, 9, -100, 24, 25}), (x1, x2) -> Tuple.create(x1, x2));


        assertEquals(q.orderBy(e -> e.getItem1()).thenBy(f -> f.getItem2()), q.orderBy(e -> e.getItem1()).thenBy(f -> f.getItem2()));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<Tuple2<Integer, String>> q = Linq.asEnumerable(new int[]{55, 49, 9, -100, 24, 25, -1, 0})
                .selectMany(x1 -> Linq.asEnumerable("!@#$%^", "C", "AAA", "", null, "Calling Twice", "SoS", Empty), (x1, x2) -> Tuple.create(x1, x2))
                .where(t -> !IsNullOrEmpty(t.getItem2()));

        assertEquals(q.orderBy(e -> e.getItem1()), q.orderBy(e -> e.getItem1()));
    }

    @Test
    public void SourceEmpty() {
        int[] source = {};
        assertEmpty(Linq.asEnumerable(source).orderBy(e -> e));
    }

    @Test
    public void OrderedCount() {
        IEnumerable<Integer> source = Linq.range(0, 20).shuffle();
        assertEquals(20, source.orderBy(i -> i).count());
    }

    @Test
    public void SurviveBadComparerAlwaysReturnsNegative() {
        int[] source = {1};
        int[] expected = {1};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e, new BadComparer2()));
    }

    @Test
    public void KeySelectorReturnsNull() {
        Integer[] source = {null, null, null};
        Integer[] expected = {null, null, null};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e));
    }

    @Test
    public void ElementsAllSameKey() {
        Integer[] source = {9, 9, 9, 9, 9, 9};
        Integer[] expected = {9, 9, 9, 9, 9, 9};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e));
    }

    @Test
    public void KeySelectorCalled() {
        NameScore[] source = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 45),
                        new NameScore("Prakash", 99)
                };
        NameScore[] expected = new NameScore[]
                {
                        new NameScore("Prakash", 99),
                        new NameScore("Robert", 45),
                        new NameScore("Tim", 90)
                };

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e.Name, null));
    }

    @Test
    public void FirstAndLastAreDuplicatesCustomComparer() {
        String[] source = {"Prakash", "Alpha", "dan", "DAN", "Prakash"};
        String[] expected = {"Alpha", "dan", "DAN", "Prakash", "Prakash"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void RunOnce() {
        String[] source = {"Prakash", "Alpha", "dan", "DAN", "Prakash"};
        String[] expected = {"Alpha", "dan", "DAN", "Prakash", "Prakash"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).runOnce().orderBy(e -> e, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void FirstAndLastAreDuplicatesNullPassedAsComparer() {
        int[] source = {5, 1, 3, 2, 5};
        int[] expected = {1, 2, 3, 5, 5};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e, null));
    }

    @Test
    public void SourceReverseOfResultNullPassedAsComparer() {
        Integer[] source = {100, 30, 9, 5, 0, -50, -75, null};
        Integer[] expected = {null, -75, -50, 0, 5, 9, 30, 100};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e, null));
    }

    @Test
    public void SameKeysVerifySortStable() {
        NameScore[] source = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };
        NameScore[] expected = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e.Score));
    }

    @Test
    public void OrderedToArray() {
        NameScore[] source = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };
        NameScore[] expected = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e.Score).toArray());
    }

    @Test
    public void EmptyOrderedToArray() {
        assertEmpty(Linq.<Integer>empty().orderBy(e -> e).toArray());
    }

    @Test
    public void OrderedToList() {
        NameScore[] source = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };
        NameScore[] expected = new NameScore[]
                {
                        new NameScore("Tim", 90),
                        new NameScore("Robert", 90),
                        new NameScore("Prakash", 90),
                        new NameScore("Jim", 90),
                        new NameScore("John", 90),
                        new NameScore("Albert", 90),
                };

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(Linq.asEnumerable(source).orderBy(e -> e.Score).toList()));
    }

    @Test
    public void EmptyOrderedToList() {
        assertEmpty(Linq.asEnumerable(Linq.<Integer>empty().orderBy(e -> e).toList()));
    }

    @Test
    public void SurviveBadComparerAlwaysReturnsPositive() {
        int[] source = {1};
        int[] expected = {1};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(source).orderBy(e -> e, new BadComparer1()));
    }

    @Test
    public void OrderByExtremeComparer() {
        int[] outOfOrder = new int[]{7, 1, 0, 9, 3, 5, 4, 2, 8, 6};
        assertEquals(Linq.range(0, 10), Linq.asEnumerable(outOfOrder).orderBy(i -> i, new ExtremeComparer()));
    }

    @Test
    public void NullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.orderBy(i -> i));
    }

    @Test
    public void NullKeySelector() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(keySelector));
    }

    @Test
    public void FirstOnOrdered() {
        assertEquals(0, Linq.range(0, 10).shuffle().orderBy(i -> i).first());
        assertEquals(9, Linq.range(0, 10).shuffle().orderByDescending(i -> i).first());
        assertEquals(10, Linq.range(0, 100).shuffle().orderByDescending(i -> i.toString().length()).thenBy(i -> i).first());
    }

    @Test
    public void FirstOnEmptyOrderedThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).first());
    }

    @Test
    public void FirstOrDefaultOnOrdered() {
        assertEquals(0, Linq.range(0, 10).shuffle().orderBy(i -> i).firstOrDefault());
        assertEquals(9, Linq.range(0, 10).shuffle().orderByDescending(i -> i).firstOrDefault());
        assertEquals(10, Linq.range(0, 100).shuffle().orderByDescending(i -> i.toString().length()).thenBy(i -> i).firstOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).firstOrDefault());
    }

    @Test
    public void LastOnOrdered() {
        assertEquals(9, Linq.range(0, 10).shuffle().orderBy(i -> i).last());
        assertEquals(0, Linq.range(0, 10).shuffle().orderByDescending(i -> i).last());
        assertEquals(10, Linq.range(0, 100).shuffle().orderBy(i -> i.toString().length()).thenByDescending(i -> i).last());
    }

    @Test
    public void LastOnOrderedMatchingCases() {
        Object[] boxedInts = new Object[]{0, 1, 2, 9, 1, 2, 3, 9, 4, 5, 7, 8, 9, 0, 1};
        assertSame(boxedInts[12], Linq.asEnumerable(boxedInts).orderBy(o -> (int) o).last());
        assertSame(boxedInts[12], Linq.asEnumerable(boxedInts).orderBy(o -> (int) o).lastOrDefault());
        assertSame(boxedInts[12], Linq.asEnumerable(boxedInts).orderBy(o -> (int) o).last(o -> (int) o % 2 == 1));
        assertSame(boxedInts[12], Linq.asEnumerable(boxedInts).orderBy(o -> (int) o).lastOrDefault(o -> (int) o % 2 == 1));
    }

    @Test
    public void LastOnEmptyOrderedThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).last());
    }

    @Test
    public void LastOrDefaultOnOrdered() {
        assertEquals(9, Linq.range(0, 10).shuffle().orderBy(i -> i).lastOrDefault());
        assertEquals(0, Linq.range(0, 10).shuffle().orderByDescending(i -> i).lastOrDefault());
        assertEquals(10, Linq.range(0, 100).shuffle().orderBy(i -> i.toString().length()).thenByDescending(i -> i).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).lastOrDefault());
    }

    @Test
    public void EnumeratorDoesntContinue() {
        IEnumerator<Integer> enumerator = NumberRangeGuaranteedNotCollectionType(0, 3).shuffle().orderBy(i -> i).enumerator();
        while (enumerator.moveNext()) {
        }
        assertFalse(enumerator.moveNext());
    }

    @Test
    public void OrderByIsCovariantTestWithCast() {
        IOrderedEnumerable<String> ordered = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length());
        IOrderedEnumerable<Comparable> covariantOrdered = (IOrderedEnumerable) ordered;
        covariantOrdered = covariantOrdered.thenBy(i -> i);
        Array<String> expected = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length()).thenBy(i -> i).toArray();
        assertEquals(expected, covariantOrdered);
    }

    @Test
    public void OrderByIsCovariantTestWithAssignToArgument() {
        IOrderedEnumerable<String> ordered = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length());
        IOrderedEnumerable<Comparable> covariantOrdered = (IOrderedEnumerable) ordered.thenByDescending(i -> i);
        Array<String> expected = Linq.range(0, 100)
                .select(i -> i.toString())
                .orderBy(i -> i.length())
                .thenByDescending(i -> i)
                .toArray();
        assertEquals(expected, covariantOrdered);
    }

    @Test
    public void CanObtainFromCovariantIOrderedQueryable() {
        // If an ordered queryable is cast covariantly and then has ThenBy() called on it,
        // it depends on IOrderedEnumerable<TElement> also being covariant to allow for
        // that ThenBy() to be processed within Linq-to-objects, as otherwise there is no
        // equivalent ThenBy() overload to translate the call to.

        IOrderedEnumerable<String> ordered =
                Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length());
        ordered = ordered.thenBy(i -> i);
        Array<String> expected =
                Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length()).thenBy(i -> i).toArray();
        assertEquals(Linq.asEnumerable(expected), ordered);
    }

    @Test
    public void SortsLargeAscendingEnumerableCorrectly() {
        final int Items = 1_000_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> i);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(i -> i);

        assertEquals(expected, ordered);
    }

    @Test
    public void SortsLargeDescendingEnumerableCorrectly() {
        final int Items = 1_000_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> Items - i - 1);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(i -> i);

        assertEquals(expected, ordered);
    }

    @Test
    public void SortsRandomizedEnumerableCorrectly() {
        this.SortsRandomizedEnumerableCorrectly(0);
        this.SortsRandomizedEnumerableCorrectly(1);
        this.SortsRandomizedEnumerableCorrectly(2);
        this.SortsRandomizedEnumerableCorrectly(3);
        this.SortsRandomizedEnumerableCorrectly(8);
        this.SortsRandomizedEnumerableCorrectly(16);
        this.SortsRandomizedEnumerableCorrectly(1024);
        this.SortsRandomizedEnumerableCorrectly(4096);
        this.SortsRandomizedEnumerableCorrectly(1_000_000);
    }

    private void SortsRandomizedEnumerableCorrectly(int items) {
        Random r = new Random(42);

        Integer[] randomized = Linq.range(0, items).select(i -> r.nextInt()).toArray(Integer.class);
        Integer[] ordered = ForceNotCollection(Linq.asEnumerable(randomized)).orderBy(i -> i).toArray(Integer.class);

        Arrays.sort(randomized, Comparator.comparingInt(a -> a));
        assertEquals(Linq.asEnumerable(randomized), Linq.asEnumerable(ordered));
    }

    @Test
    public void TakeOne() {
        this.TakeOne(Linq.asEnumerable(new int[]{1}));
        this.TakeOne(Linq.asEnumerable(new int[]{1, 2}));
        this.TakeOne(Linq.asEnumerable(new int[]{2, 1}));
        this.TakeOne(Linq.asEnumerable(new int[]{1, 2, 3, 4, 5}));
        this.TakeOne(Linq.asEnumerable(new int[]{5, 4, 3, 2, 1}));
        this.TakeOne(Linq.asEnumerable(new int[]{4, 3, 2, 1, 5, 9, 8, 7, 6}));
        this.TakeOne(Linq.asEnumerable(new int[]{2, 4, 6, 8, 10, 5, 3, 7, 1, 9}));
    }

    private void TakeOne(IEnumerable<Integer> source) {
        int count = 0;
        for (int x : source.orderBy(i -> i).take(1)) {
            count++;
            assertEquals(source.min(), x);
        }
        assertEquals(1, count);
    }

    @Test
    public void testOrderBy() {
        //null 在前,值相等的按原始顺序
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", sss);

        Set<Integer> set = new HashSet<>();
        set.add(3);
        set.add(1);
        set.add(2);
        IEnumerable<Integer> ordered = Linq.asEnumerable(set).orderBy(a -> a);
        Integer[] orderedArr = {1, 2, 3};
        assertTrue(ordered.sequenceEqual(Linq.asEnumerable(orderedArr)));
    }

    @Test
    public void testOrderByWithComparer() {
        //null 在后,值相等的按原始顺序
        Comparator<Object> reverse = Comparer.Default().reversed();

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", sss);
    }

    @Test
    public void testOrderByDesc() {
        //null 在后,值相等的按原始顺序,同 testOrderByWithComparer()
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", sss);
    }

    @Test
    public void testOrderByDescWithComparer() {
        //null 在后,值相等的按原始顺序
        Comparator<Object> reverse = Comparer.Default().reversed();

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", sss);
    }

    private static class NameScore extends ValueType {
        final String Name;
        final int Score;

        NameScore(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    private static class ExtremeComparer implements Comparator<Integer> {
        @Override
        public int compare(Integer x, Integer y) {
            if (x == y)
                return 0;
            if (x < y)
                return Integer.MIN_VALUE;
            return Integer.MAX_VALUE;
        }
    }

    private static class BadComparer1 implements Comparator<Integer> {
        @Override
        public int compare(Integer x, Integer y) {
            return 1;
        }
    }

    private static class BadComparer2 implements Comparator<Integer> {
        @Override
        public int compare(Integer x, Integer y) {
            return -1;
        }
    }
}
