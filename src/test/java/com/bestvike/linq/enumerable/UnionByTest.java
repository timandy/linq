package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.StringComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.linq.util.HashSet;
import com.bestvike.out;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class UnionByTest extends TestCase {
    private static final Func1<Integer, Integer> IntKeySelector = x -> x;
    private static final Func1<String, String> StringKeySelector = x -> x;

    private static <TSource, TKey> Object[] WrapArgs(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        return new Object[]{first, second, keySelector, comparer, expected};
    }

    private static IEnumerable<Object[]> TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(WrapArgs(
                Linq.range(0, 7),
                Linq.range(3, 7),
                x -> x,
                null,
                Linq.range(0, 10)));

        argsList.add(WrapArgs(
                Linq.range(0, 10),
                Linq.range(10, 10),
                x -> x,
                null,
                Linq.range(0, 20)));

        argsList.add(WrapArgs(
                Linq.empty(),
                Linq.range(0, 5),
                x -> x,
                null,
                Linq.range(0, 5)));

        argsList.add(WrapArgs(
                Linq.repeat(5, 20),
                Linq.empty(),
                x -> x,
                null,
                Linq.repeat(5, 1)));

        argsList.add(WrapArgs(
                Linq.repeat(5, 20),
                Linq.repeat(5, 3),
                x -> x,
                null,
                Linq.repeat(5, 1)));

        argsList.add(WrapArgs(
                Linq.of("Bob", "Tim", "Robert", "Chris"),
                Linq.of("bBo", "shriC"),
                x -> x,
                null,
                Linq.of("Bob", "Tim", "Robert", "Chris", "bBo", "shriC")));

        argsList.add(WrapArgs(
                Linq.of("Bob", "Tim", "Robert", "Chris"),
                Linq.of("bBo", "shriC"),
                x -> x,
                new AnagramEqualityComparer(),
                Linq.of("Bob", "Tim", "Robert", "Chris")));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Martin", 20)),
                Linq.of(Tuple.create("Peter", 21), Tuple.create("John", 30), Tuple.create("Toby", 33)),
                Tuple2::getItem2,
                null,
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Peter", 21), Tuple.create("Toby", 33))));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Martin", 20)),
                Linq.of(Tuple.create("Toby", 33), Tuple.create("Harry", 35), Tuple.create("tom", 67)),
                Tuple2::getItem1,
                null,
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Martin", 20), Tuple.create("Toby", 33), Tuple.create("tom", 67))));

        argsList.add(WrapArgs(
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Martin", 20)),
                Linq.of(Tuple.create("Toby", 33), Tuple.create("Harry", 35), Tuple.create("tom", 67)),
                Tuple2::getItem1,
                StringComparer.OrdinalIgnoreCase,
                Linq.of(Tuple.create("Tom", 20), Tuple.create("Dick", 30), Tuple.create("Harry", 40), Tuple.create("Martin", 20), Tuple.create("Toby", 33))));

        return argsList;
    }

    @Test
    void FirstNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = null;
        IEnumerable<String> second = Linq.of("bBo", "shriC");

        assertThrows(NullPointerException.class, () -> first.unionBy(second, x -> x));
        assertThrows(NullPointerException.class, () -> first.unionBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void SecondNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = null;

        assertThrows(ArgumentNullException.class, () -> first.unionBy(second, x -> x));
        assertThrows(ArgumentNullException.class, () -> first.unionBy(second, x -> x, new AnagramEqualityComparer()));
    }

    @Test
    void KeySelectorNull_ThrowsArgumentNullException() {
        IEnumerable<String> first = Linq.of("Bob", "Tim", "Robert", "Chris");
        IEnumerable<String> second = Linq.of("bBo", "shriC");
        Func1<String, String> keySelector = null;

        assertThrows(ArgumentNullException.class, () -> first.unionBy(second, keySelector));
        assertThrows(ArgumentNullException.class, () -> first.unionBy(second, keySelector, new AnagramEqualityComparer()));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    <TSource, TKey> void HasExpectedOutput(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        assertEquals(expected, first.unionBy(second, keySelector, comparer));
    }

    @ParameterizedTest
    @MethodSource("TestData")
    <TSource, TKey> void RunOnce_HasExpectedOutput(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer, IEnumerable<TSource> expected) {
        assertEquals(expected, first.runOnce().unionBy(second.runOnce(), keySelector, comparer));
    }

    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.of(1, 9, null, 4);

        assertEquals(q1.unionBy(q2, IntKeySelector), q1.unionBy(q2, IntKeySelector));
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.of("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> q2 = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        assertEquals(q1.unionBy(q2, StringKeySelector), q1.unionBy(q2, StringKeySelector));
    }

    @Test
    void SameResultsRepeatCallsMultipleUnions() {
        IEnumerable<Integer> q1 = Linq.of(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.of(1, 9, null, 4);
        IEnumerable<Integer> q3 = Linq.of(null, 8, 2, 2, 3);

        assertEquals(q1.unionBy(q2, IntKeySelector).unionBy(q3, IntKeySelector), q1.unionBy(q2, IntKeySelector).unionBy(q3, IntKeySelector));
    }

    @Test
    void BothEmpty() {
        int[] first = {};
        int[] second = {};
        assertEmpty(Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void ManyEmpty() {
        int[] first = {};
        int[] second = {};
        int[] third = {};
        int[] fourth = {};
        assertEmpty(Linq.of(first).unionBy(Linq.of(second), IntKeySelector).unionBy(Linq.of(third), IntKeySelector).unionBy(Linq.of(fourth), IntKeySelector));
    }

    @Test
    void CustomComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "Charlie"};

        AnagramEqualityComparer comparer = new AnagramEqualityComparer();
        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, comparer), comparer);
    }

    @Test
    void RunOnce() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "Charlie"};

        AnagramEqualityComparer comparer = new AnagramEqualityComparer();
        assertEquals(Linq.of(expected), Linq.of(first).runOnce().unionBy(Linq.of(second).runOnce(), StringKeySelector, comparer), comparer);
    }

    @Test
    void SingleNullWithEmpty() {
        String[] first = {null};
        String[] second = new String[0];
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    void NullEmptyStringMix() {
        String[] first = {null, null, Empty};
        String[] second = {null, null};
        String[] expected = {null, Empty};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    void DoubleNullWithEmpty() {
        String[] first = {null, null};
        String[] second = new String[0];
        String[] expected = {null};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    void EmptyWithNonEmpty() {
        int[] first = {};
        int[] second = {2, 4, 5, 3, 2, 3, 9};
        int[] expected = {2, 4, 5, 3, 9};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void NonEmptyWithEmpty() {
        int[] first = {2, 4, 5, 3, 2, 3, 9};
        int[] second = {};
        int[] expected = {2, 4, 5, 3, 9};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void CommonElementsShared() {
        int[] first = {1, 2, 3, 4, 5, 6};
        int[] second = {6, 7, 7, 7, 8, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void SameElementRepeated() {
        int[] first = {1, 1, 1, 1, 1, 1};
        int[] second = {1, 1, 1, 1, 1, 1};
        int[] expected = {1};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void RepeatedElementsWithSingleElement() {
        int[] first = {1, 2, 3, 5, 3, 6};
        int[] second = {7};
        int[] expected = {1, 2, 3, 5, 6, 7};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void SingleWithAllUnique() {
        Integer[] first = {2};
        Integer[] second = {3, null, 4, 5};
        Integer[] expected = {2, 3, null, 4, 5};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void EachHasRepeatsBetweenAndAmongstThemselves() {
        Integer[] first = {1, 2, 3, 4, null, 5, 1};
        Integer[] second = {6, 2, 3, 4, 5, 6};
        Integer[] expected = {1, 2, 3, 4, null, 5, 6};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector));
    }

    @Test
    void EachHasRepeatsBetweenAndAmongstThemselvesMultipleUnions() {
        Integer[] first = {1, 2, 3, 4, null, 5, 1};
        Integer[] second = {6, 2, 3, 4, 5, 6};
        Integer[] third = {2, 8, 2, 3, 2, 8};
        Integer[] fourth = {null, 1, 7, 2, 7};
        Integer[] expected = {1, 2, 3, 4, null, 5, 6, 8, 7};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector).unionBy(Linq.of(third), IntKeySelector).unionBy(Linq.of(fourth), IntKeySelector));
    }

    @Test
    void MultipleUnionsCustomComparer() {
        Integer[] first = {1, 102, 903, 204, null, 5, 601};
        Integer[] second = {6, 202, 903, 204, 5, 106};
        Integer[] third = {2, 308, 2, 103, 802, 308};
        Integer[] fourth = {null, 101, 207, 202, 207};
        Integer[] expected = {1, 102, 903, 204, null, 5, 6, 308, 207};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), IntKeySelector, new Modulo100EqualityComparer()).unionBy(Linq.of(third), IntKeySelector, new Modulo100EqualityComparer()).unionBy(Linq.of(fourth), IntKeySelector, new Modulo100EqualityComparer()));
    }

    @Test
    void MultipleUnionsDifferentComparers() {
        String[] first = {"Alpha", "Bravo", "Charlie", "Bravo", "Delta", "atleD", "ovarB"};
        String[] second = {"Charlie", "Delta", "Echo", "Foxtrot", "Foxtrot", "choE"};
        String[] third = {"trotFox", "Golf", "Alpha", "choE", "Tango"};

        String[] plainThenAnagram = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Tango"};
        String[] anagramThenPlain = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "trotFox", "Golf", "choE", "Tango"};

        assertEquals(Linq.of(plainThenAnagram), Linq.of(first).unionBy(Linq.of(second), StringKeySelector).unionBy(Linq.of(third), StringKeySelector, new AnagramEqualityComparer()));
        assertEquals(Linq.of(anagramThenPlain), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, new AnagramEqualityComparer()).unionBy(Linq.of(third), StringKeySelector));
    }

    @Test
    void NullEqualityComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector, null));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).unionBy(Linq.range(0, 3), IntKeySelector);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerateMultipleUnions() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).unionBy(Linq.range(0, 3), IntKeySelector).unionBy(Linq.range(2, 4), IntKeySelector).unionBy(Linq.of(new int[]{
                9, 2, 4
        }), IntKeySelector);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void ToArray() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector).toArray());
    }

    @Test
    void ToArrayMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo", "Albert"};

        assertEquals(Linq.of(expected), Linq.of(first).unionBy(Linq.of(second), StringKeySelector).unionBy(Linq.of(third), StringKeySelector).toArray());
    }

    @Test
    void ToList() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.of(expected), Linq.of(Linq.of(first).unionBy(Linq.of(second), StringKeySelector).toList()));
    }

    @Test
    void ToListMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo", "Albert"};

        assertEquals(Linq.of(expected), Linq.of(Linq.of(first).unionBy(Linq.of(second), StringKeySelector).unionBy(Linq.of(third), StringKeySelector).toList()));
    }

    @Test
    void Count() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};

        assertEquals(8, Linq.of(first).unionBy(Linq.of(second), StringKeySelector).count());
    }

    @Test
    void CountMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};

        assertEquals(9, Linq.of(first).unionBy(Linq.of(second), StringKeySelector).unionBy(Linq.of(third), StringKeySelector).count());
    }

    @Test
    void RepeatEnumerating() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};

        IEnumerable<String> result = Linq.of(first).unionBy(Linq.of(second), StringKeySelector);

        assertEquals(result, result);
    }

    @Test
    void RepeatEnumeratingMultipleUnions() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Matt", "Albert", "Ichabod"};

        IEnumerable<String> result = Linq.of(first).unionBy(Linq.of(second), StringKeySelector).unionBy(Linq.of(third), StringKeySelector);
        assertEquals(result, result);
    }

    @Test
    void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        HashSet<String> set1 = new HashSet<>(StringComparer.OrdinalIgnoreCase);
        set1.add("a");
        IEnumerable<String> input1 = Linq.of(set1);
        IEnumerable<String> input2 = Linq.of("A");

        assertEquals(Linq.of("a", "A"), input1.unionBy(input2, StringKeySelector));
        assertEquals(Linq.of("a", "A"), input1.unionBy(input2, StringKeySelector, null));
        assertEquals(Linq.of("a", "A"), input1.unionBy(input2, StringKeySelector, EqualityComparer.Default()));
        assertEquals(Linq.of("a"), input1.unionBy(input2, StringKeySelector, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.of("A", "a"), input2.unionBy(input1, StringKeySelector));
        assertEquals(Linq.of("A", "a"), input2.unionBy(input1, StringKeySelector, null));
        assertEquals(Linq.of("A", "a"), input2.unionBy(input1, StringKeySelector, EqualityComparer.Default()));
        assertEquals(Linq.of("A"), input2.unionBy(input1, StringKeySelector, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    void testMultiUnionBySameSelector() {
        People[] source1 = new People[]{
                new People(1, 1, "Tim"),
                new People(1, 2, "Jack"),
                new People(2, 3, "John"),
                new People(2, 4, "Cherry")
        };
        People[] source2 = new People[]{
                new People(1, 1, "Jany"),
                new People(2, 2, "Tony"),
                new People(3, 3, "Jane"),
                new People(4, 4, "Cook"),
        };
        People[] source3 = new People[]{
                new People(1, 1, "Jim"),
                new People(2, 2, "Linda"),
                new People(5, 3, "Candy"),
                new People(6, 4, "Peter")
        };
        People[] expect = new People[]{
                new People(1, 1, "Tim"),
                new People(2, 3, "John"),
                new People(3, 3, "Jane"),
                new People(4, 4, "Cook"),
                new People(5, 3, "Candy"),
                new People(6, 4, "Peter")
        };

        IEnumerable<People> peoples = Linq.of(source1).unionBy(Linq.of(source2), x -> x.groupId).unionBy(Linq.of(source3), x -> x.groupId);
        assertSame(UnionByIterator.class, peoples.getClass());
        assertEquals(Linq.of(expect), peoples);

        Func1<People, Integer> groupSelector = x -> x.groupId;
        IEnumerable<People> peoples2 = Linq.of(source1).unionBy(Linq.of(source2), groupSelector).unionBy(Linq.of(source3), groupSelector);
        assertSame(UnionByIterator.class, peoples2.getClass());
        assertEquals(Linq.of(expect), peoples2);
    }

    @Test
    void testMultiUnionByDiffSelector() {
        People[] source1 = new People[]{
                new People(1, 1, "Tim"),
                new People(1, 2, "Jack"),
                new People(2, 3, "John"),
                new People(2, 4, "Cherry")
        };
        People[] source2 = new People[]{
                new People(1, 1, "Jany"),
                new People(2, 2, "Tony"),
                new People(3, 3, "Jane"),
                new People(4, 4, "Cook"),
        };
        People[] source3 = new People[]{
                new People(1, 1, "Jim"),
                new People(2, 2, "Linda"),
                new People(5, 3, "Candy"),
                new People(6, 4, "Peter")
        };
        People[] expect = new People[]{
                new People(1, 1, "Tim"),
                new People(2, 3, "John"),
                new People(4, 4, "Cook"),
                new People(2, 2, "Linda")
        };

        IEnumerable<People> peoples = Linq.of(source1).unionBy(Linq.of(source2), x -> x.groupId).unionBy(Linq.of(source3), x -> x.id);
        assertSame(UnionByIterator.class, peoples.getClass());
        assertEquals(Linq.of(expect), peoples);
    }

    @Test
    void testUnionBy() {
        IEnumerable<Employee> enumerable = Linq.of(emps)
                .unionBy(Linq.of(badEmps), emp -> emp.deptno)
                .unionBy(Linq.of(emps), emp -> emp.deptno);
        assertEquals(4, enumerable.count());
        UnionByIterator<Employee, Integer> unionByIterator2 = (UnionByIterator<Employee, Integer>) enumerable;
        assertEquals(4, unionByIterator2.toArray(Employee.class).length);
        assertEquals(4, unionByIterator2.toArray().size());
        assertEquals(4, unionByIterator2.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(unionByIterator2, out.init()));
        assertEquals(4, unionByIterator2.count());

        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.of(emps)
                .unionBy(Linq.of(badEmps), selector)
                .unionBy(Linq.of(emps), selector);
        assertEquals(4, enumerable2.count());
        UnionByIterator<Employee, Integer> unionByIterator22 = (UnionByIterator<Employee, Integer>) enumerable2;
        assertEquals(4, unionByIterator22.toArray(Employee.class).length);
        assertEquals(4, unionByIterator22.toArray().size());
        assertEquals(4, unionByIterator22.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(unionByIterator22, out.init()));
        assertEquals(4, unionByIterator22.count());

        IEnumerable<Employee> source = Linq.of(emps)
                .unionBy(Linq.of(badEmps), selector)
                .unionBy(Linq.of(emps), selector);

        IEnumerator<Employee> e1 = source.enumerator();
        assertTrue(e1.moveNext());
        IEnumerator<Employee> e2 = source.enumerator();
        assertTrue(e2.moveNext());
        assertNotSame(e1, e2);
    }

    @Test
    void testUnionByWithComparer() {
        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };

        IEnumerable<Employee> enumerable = Linq.of(emps)
                .unionBy(Linq.of(badEmps), emp -> emp.deptno, comparer)
                .unionBy(Linq.of(emps), emp -> emp.deptno, comparer);
        assertEquals(1, enumerable.count());
        UnionByIterator<Employee, Integer> unionByIterator2 = (UnionByIterator<Employee, Integer>) enumerable;
        assertEquals(1, unionByIterator2.toArray(Employee.class).length);
        assertEquals(1, unionByIterator2.toArray().size());
        assertEquals(1, unionByIterator2.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(unionByIterator2, out.init()));
        assertEquals(1, unionByIterator2.count());

        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.of(emps)
                .unionBy(Linq.of(badEmps), selector, comparer)
                .unionBy(Linq.of(emps), selector, comparer);
        assertEquals(1, enumerable2.count());
        UnionByIterator<Employee, Integer> unionByIterator22 = (UnionByIterator<Employee, Integer>) enumerable2;
        assertEquals(1, unionByIterator22.toArray(Employee.class).length);
        assertEquals(1, unionByIterator22.toArray().size());
        assertEquals(1, unionByIterator22.toList().size());
        assertFalse(Count.tryGetNonEnumeratedCount(unionByIterator22, out.init()));
        assertEquals(1, unionByIterator22.count());
    }


    private static final class Modulo100EqualityComparer implements IEqualityComparer<Integer> {
        @Override
        public boolean equals(Integer x, Integer y) {
            if (x == null)
                return y == null;
            if (y == null)
                return false;
            return x % 100 == y % 100;
        }

        @Override
        public int hashCode(Integer obj) {
            return obj != null ? obj % 100 + 1 : 0;
        }

        @Override
        public boolean equals(Object obj) {
            // Equal to all other instances.
            return obj instanceof Modulo100EqualityComparer;
        }

        @Override
        public int hashCode() {
            return 0xAFFAB1E; // Any number as long as it's constant.
        }
    }

    private static class People extends ValueType {
        private final int groupId;
        private final int id;
        private final String name;

        private People(int groupId, int id, String name) {
            this.groupId = groupId;
            this.id = id;
            this.name = name;
        }
    }
}
