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
import com.bestvike.linq.util.ArgsList;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class OrderByTest extends TestCase {
    private static IEnumerable<Object[]> SortsRandomizedEnumerableCorrectly_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(0);
        argsList.add(1);
        argsList.add(2);
        argsList.add(3);
        argsList.add(8);
        argsList.add(16);
        argsList.add(1024);
        argsList.add(4096);
        argsList.add(1_000_000);
        return argsList;
    }

    private static IEnumerable<Object[]> TakeOne_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(Linq.of(new int[]{1}));
        argsList.add(Linq.of(new int[]{1, 2}));
        argsList.add(Linq.of(new int[]{2, 1}));
        argsList.add(Linq.of(new int[]{1, 2, 3, 4, 5}));
        argsList.add(Linq.of(new int[]{5, 4, 3, 2, 1}));
        argsList.add(Linq.of(new int[]{4, 3, 2, 1, 5, 9, 8, 7, 6}));
        argsList.add(Linq.of(new int[]{2, 4, 6, 8, 10, 5, 3, 7, 1, 9}));
        return argsList;
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Tuple2<Integer, Integer>> q = Linq.of(new int[]{1, 6, 0, -1, 3})
                .selectMany(x1 -> Linq.of(new int[]{55, 49, 9, -100, 24, 25}), (x1, x2) -> Tuple.create(x1, x2));


        assertEquals(q.orderBy(e -> e.getItem1()).thenBy(f -> f.getItem2()), q.orderBy(e -> e.getItem1()).thenBy(f -> f.getItem2()));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<Tuple2<Integer, String>> q = Linq.of(new int[]{55, 49, 9, -100, 24, 25, -1, 0})
                .selectMany(x1 -> Linq.of("!@#$%^", "C", "AAA", "", null, "Calling Twice", "SoS", Empty), (x1, x2) -> Tuple.create(x1, x2))
                .where(t -> !IsNullOrEmpty(t.getItem2()));

        assertEquals(q.orderBy(e -> e.getItem1()), q.orderBy(e -> e.getItem1()));
    }

    @Test
    void SourceEmpty() {
        int[] source = {};
        assertEmpty(Linq.of(source).orderBy(e -> e));
    }

    @Test
    void OrderedCount() {
        IEnumerable<Integer> source = Linq.range(0, 20).shuffle();
        assertEquals(20, source.orderBy(i -> i).count());
    }

    @Test
    void SurviveBadComparerAlwaysReturnsNegative() {
        int[] source = {1};
        int[] expected = {1};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e, new BadComparer2()));
    }

    @Test
    void KeySelectorReturnsNull() {
        Integer[] source = {null, null, null};
        Integer[] expected = {null, null, null};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e));
    }

    @Test
    void ElementsAllSameKey() {
        Integer[] source = {9, 9, 9, 9, 9, 9};
        Integer[] expected = {9, 9, 9, 9, 9, 9};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e));
    }

    @Test
    void KeySelectorCalled() {
        NameScore[] source = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 45),
                new NameScore("Prakash", 99)
        };
        NameScore[] expected = new NameScore[]{
                new NameScore("Prakash", 99),
                new NameScore("Robert", 45),
                new NameScore("Tim", 90)
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Name, null));
    }

    @Test
    void FirstAndLastAreDuplicatesCustomComparer() {
        String[] source = {"Prakash", "Alpha", "dan", "DAN", "Prakash"};
        String[] expected = {"Alpha", "dan", "DAN", "Prakash", "Prakash"};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void RunOnce() {
        String[] source = {"Prakash", "Alpha", "dan", "DAN", "Prakash"};
        String[] expected = {"Alpha", "dan", "DAN", "Prakash", "Prakash"};

        assertEquals(Linq.of(expected), Linq.of(source).runOnce().orderBy(e -> e, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void FirstAndLastAreDuplicatesNullPassedAsComparer() {
        int[] source = {5, 1, 3, 2, 5};
        int[] expected = {1, 2, 3, 5, 5};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e, null));
    }

    @Test
    void SourceReverseOfResultNullPassedAsComparer() {
        Integer[] source = {100, 30, 9, 5, 0, -50, -75, null};
        Integer[] expected = {null, -75, -50, 0, 5, 9, 30, 100};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e, null));
    }

    @Test
    void SameKeysVerifySortStable() {
        NameScore[] source = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };
        NameScore[] expected = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Score));
    }

    @Test
    void OrderedToArray() {
        NameScore[] source = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };
        NameScore[] expected = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e.Score).toArray());
    }

    @Test
    void EmptyOrderedToArray() {
        assertEmpty(Linq.<Integer>empty().orderBy(e -> e).toArray());
    }

    @Test
    void OrderedToList() {
        NameScore[] source = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };
        NameScore[] expected = new NameScore[]{
                new NameScore("Tim", 90),
                new NameScore("Robert", 90),
                new NameScore("Prakash", 90),
                new NameScore("Jim", 90),
                new NameScore("John", 90),
                new NameScore("Albert", 90),
        };

        assertEquals(Linq.of(expected), Linq.of(Linq.of(source).orderBy(e -> e.Score).toList()));
    }

    @Test
    void EmptyOrderedToList() {
        assertEmpty(Linq.of(Linq.<Integer>empty().orderBy(e -> e).toList()));
    }

    @Test
    void SurviveBadComparerAlwaysReturnsPositive() {
        int[] source = {1};
        int[] expected = {1};

        assertEquals(Linq.of(expected), Linq.of(source).orderBy(e -> e, new BadComparer1()));
    }

    @Test
    void OrderByExtremeComparer() {
        int[] outOfOrder = new int[]{7, 1, 0, 9, 3, 5, 4, 2, 8, 6};
        assertEquals(Linq.range(0, 10), Linq.of(outOfOrder).orderBy(i -> i, new ExtremeComparer()));
    }

    @Test
    void NullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.orderBy(i -> i));
    }

    @Test
    void NullKeySelector() {
        Func1<Date, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.<Date>empty().orderBy(keySelector));
    }

    @Test
    void FirstOnOrdered() {
        assertEquals(0, Linq.range(0, 10).shuffle().orderBy(i -> i).first());
        assertEquals(9, Linq.range(0, 10).shuffle().orderByDescending(i -> i).first());
        assertEquals(10, Linq.range(0, 100).shuffle().orderByDescending(i -> i.toString().length()).thenBy(i -> i).first());
    }

    @Test
    void FirstOnEmptyOrderedThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).first());
    }

    @Test
    void FirstOrDefaultOnOrdered() {
        assertEquals(0, Linq.range(0, 10).shuffle().orderBy(i -> i).firstOrDefault());
        assertEquals(9, Linq.range(0, 10).shuffle().orderByDescending(i -> i).firstOrDefault());
        assertEquals(10, Linq.range(0, 100).shuffle().orderByDescending(i -> i.toString().length()).thenBy(i -> i).firstOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).firstOrDefault());
    }

    @Test
    void LastOnOrdered() {
        assertEquals(9, Linq.range(0, 10).shuffle().orderBy(i -> i).last());
        assertEquals(0, Linq.range(0, 10).shuffle().orderByDescending(i -> i).last());
        assertEquals(10, Linq.range(0, 100).shuffle().orderBy(i -> i.toString().length()).thenByDescending(i -> i).last());
    }

    @Test
    void LastOnOrderedMatchingCases() {
        Object[] boxedInts = new Object[]{0, 1, 2, 9, 1, 2, 3, 9, 4, 5, 7, 8, 9, 0, 1};
        assertSame(boxedInts[12], Linq.of(boxedInts).orderBy(o -> (int) o).last());
        assertSame(boxedInts[12], Linq.of(boxedInts).orderBy(o -> (int) o).lastOrDefault());
        assertSame(boxedInts[12], Linq.of(boxedInts).orderBy(o -> (int) o).last(o -> (int) o % 2 == 1));
        assertSame(boxedInts[12], Linq.of(boxedInts).orderBy(o -> (int) o).lastOrDefault(o -> (int) o % 2 == 1));
    }

    @Test
    void LastOnEmptyOrderedThrows() {
        assertThrows(InvalidOperationException.class, () -> Linq.<Integer>empty().orderBy(i -> i).last());
    }

    @Test
    void LastOrDefaultOnOrdered() {
        assertEquals(9, Linq.range(0, 10).shuffle().orderBy(i -> i).lastOrDefault());
        assertEquals(0, Linq.range(0, 10).shuffle().orderByDescending(i -> i).lastOrDefault());
        assertEquals(10, Linq.range(0, 100).shuffle().orderBy(i -> i.toString().length()).thenByDescending(i -> i).lastOrDefault());
        assertEquals(null, Linq.<Integer>empty().orderBy(i -> i).lastOrDefault());
    }

    @Test
    void EnumeratorDoesntContinue() {
        IEnumerator<Integer> enumerator = NumberRangeGuaranteedNotCollectionType(0, 3).shuffle().orderBy(i -> i).enumerator();
        while (enumerator.moveNext()) {
        }
        assertFalse(enumerator.moveNext());
    }

    @Test
    void OrderByIsCovariantTestWithCast() {
        IOrderedEnumerable<String> ordered = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length());
        IOrderedEnumerable<Comparable> covariantOrdered = (IOrderedEnumerable) ordered;
        covariantOrdered = covariantOrdered.thenBy(i -> i);
        Array<String> expected = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length()).thenBy(i -> i).toArray();
        assertEquals(expected, covariantOrdered);
    }

    @Test
    void OrderByIsCovariantTestWithAssignToArgument() {
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
    void CanObtainFromCovariantIOrderedQueryable() {
        // If an ordered queryable is cast covariantly and then has ThenBy() called on it,
        // it depends on IOrderedEnumerable<TElement> also being covariant to allow for
        // that ThenBy() to be processed within Linq-to-objects, as otherwise there is no
        // equivalent ThenBy() overload to translate the call to.

        IOrderedEnumerable<String> ordered = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length());
        ordered = ordered.thenBy(i -> i);
        Array<String> expected = Linq.range(0, 100).select(i -> i.toString()).orderBy(i -> i.length()).thenBy(i -> i).toArray();
        assertEquals(Linq.of(expected), ordered);
    }

    @Test
    void SortsLargeAscendingEnumerableCorrectly() {
        final int Items = 1_000_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> i);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(i -> i);

        assertEquals(expected, ordered);
    }

    @Test
    void SortsLargeDescendingEnumerableCorrectly() {
        final int Items = 1_000_000;
        IEnumerable<Integer> expected = NumberRangeGuaranteedNotCollectionType(0, Items);

        IEnumerable<Integer> unordered = expected.select(i -> Items - i - 1);
        IOrderedEnumerable<Integer> ordered = unordered.orderBy(i -> i);

        assertEquals(expected, ordered);
    }

    @ParameterizedTest
    @MethodSource("SortsRandomizedEnumerableCorrectly_TestData")
    void SortsRandomizedEnumerableCorrectly(int items) {
        Random r = new Random(42);

        Integer[] randomized = Linq.range(0, items).select(i -> r.nextInt()).toArray(Integer.class);
        Integer[] ordered = ForceNotCollection(Linq.of(randomized)).orderBy(i -> i).toArray(Integer.class);

        Arrays.sort(randomized, Comparator.comparingInt(a -> a));
        assertEquals(Linq.of(randomized), Linq.of(ordered));
    }

    @ParameterizedTest
    @MethodSource("TakeOne_TestData")
    void TakeOne(IEnumerable<Integer> source) {
        int count = 0;
        for (int x : source.orderBy(i -> i).take(1)) {
            count++;
            assertEquals(source.min(), x);
        }
        assertEquals(1, count);
    }

    @Test
    void testOrderBy() {
        //null 在前,值相等的按原始顺序
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", ss);

        String sss = Linq.of(emps).concat(Linq.of(badEmps))
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
        IEnumerable<Integer> ordered = Linq.of(set).orderBy(a -> a);
        Integer[] orderedArr = {1, 2, 3};
        assertTrue(ordered.sequenceEqual(Linq.of(orderedArr)));
    }

    @Test
    void testOrderByWithComparer() {
        //null 在后,值相等的按原始顺序
        Comparator<Object> reverse = Comparer.Default().reversed();

        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", ss);

        String sss = Linq.of(emps).concat(Linq.of(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", sss);
    }


    private static class NameScore extends ValueType {
        private final String Name;
        private final int Score;

        private NameScore(String name, int score) {
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
