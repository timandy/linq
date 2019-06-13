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
import com.bestvike.linq.util.HashSet;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class UnionByTest extends TestCase {
    private static final Func1<Integer, Integer> IntKeySelector = x -> x;
    private static final Func1<String, String> StringKeySelector = x -> x;

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q1 = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.asEnumerable(1, 9, null, 4);

        assertEquals(q1.unionBy(q2, IntKeySelector), q1.unionBy(q2, IntKeySelector));
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q1 = Linq.asEnumerable("AAA", Empty, "q", "C", "#", "!@#$%^", "0987654321", "Calling Twice");
        IEnumerable<String> q2 = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS");

        assertEquals(q1.unionBy(q2, StringKeySelector), q1.unionBy(q2, StringKeySelector));
    }

    @Test
    public void SameResultsRepeatCallsMultipleUnions() {
        IEnumerable<Integer> q1 = Linq.asEnumerable(2, 3, null, 2, null, 4, 5);
        IEnumerable<Integer> q2 = Linq.asEnumerable(1, 9, null, 4);
        IEnumerable<Integer> q3 = Linq.asEnumerable(null, 8, 2, 2, 3);

        assertEquals(q1.unionBy(q2, IntKeySelector).unionBy(q3, IntKeySelector), q1.unionBy(q2, IntKeySelector).unionBy(q3, IntKeySelector));
    }

    @Test
    public void BothEmpty() {
        int[] first = {};
        int[] second = {};
        assertEmpty(Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void ManyEmpty() {
        int[] first = {};
        int[] second = {};
        int[] third = {};
        int[] fourth = {};
        assertEmpty(Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector).unionBy(Linq.asEnumerable(third), IntKeySelector).unionBy(Linq.asEnumerable(fourth), IntKeySelector));
    }

    @Test
    public void CustomComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "Charlie"};

        AnagramEqualityComparer comparer = new AnagramEqualityComparer();
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, comparer), comparer);
    }

    @Test
    public void RunOnce() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "Charlie"};

        AnagramEqualityComparer comparer = new AnagramEqualityComparer();
        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).runOnce().unionBy(Linq.asEnumerable(second).runOnce(), StringKeySelector, comparer), comparer);
    }

    @Test
    public void FirstNullCustomComparer() {
        String[] first = null;
        String[] second = {"ttaM", "Charlie", "Bbo"};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, new AnagramEqualityComparer()));
    }

    @Test
    public void SecondNullCustomComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, new AnagramEqualityComparer()));
    }

    @Test
    public void FirstNullNoComparer() {
        String[] first = null;
        String[] second = {"ttaM", "Charlie", "Bbo"};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector));
    }

    @Test
    public void SecondNullNoComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector));
    }

    @Test
    public void SingleNullWithEmpty() {
        String[] first = {null};
        String[] second = new String[0];
        String[] expected = {null};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    public void NullEmptyStringMix() {
        String[] first = {null, null, Empty};
        String[] second = {null, null};
        String[] expected = {null, Empty};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    public void DoubleNullWithEmpty() {
        String[] first = {null, null};
        String[] second = new String[0];
        String[] expected = {null};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, EqualityComparer.Default()));
    }

    @Test
    public void EmptyWithNonEmpty() {
        int[] first = {};
        int[] second = {2, 4, 5, 3, 2, 3, 9};
        int[] expected = {2, 4, 5, 3, 9};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void NonEmptyWithEmpty() {
        int[] first = {2, 4, 5, 3, 2, 3, 9};
        int[] second = {};
        int[] expected = {2, 4, 5, 3, 9};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void CommonElementsShared() {
        int[] first = {1, 2, 3, 4, 5, 6};
        int[] second = {6, 7, 7, 7, 8, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void SameElementRepeated() {
        int[] first = {1, 1, 1, 1, 1, 1};
        int[] second = {1, 1, 1, 1, 1, 1};
        int[] expected = {1};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void RepeatedElementsWithSingleElement() {
        int[] first = {1, 2, 3, 5, 3, 6};
        int[] second = {7};
        int[] expected = {1, 2, 3, 5, 6, 7};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void SingleWithAllUnique() {
        Integer[] first = {2};
        Integer[] second = {3, null, 4, 5};
        Integer[] expected = {2, 3, null, 4, 5};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void EachHasRepeatsBetweenAndAmongstThemselves() {
        Integer[] first = {1, 2, 3, 4, null, 5, 1};
        Integer[] second = {6, 2, 3, 4, 5, 6};
        Integer[] expected = {1, 2, 3, 4, null, 5, 6};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector));
    }

    @Test
    public void EachHasRepeatsBetweenAndAmongstThemselvesMultipleUnions() {
        Integer[] first = {1, 2, 3, 4, null, 5, 1};
        Integer[] second = {6, 2, 3, 4, 5, 6};
        Integer[] third = {2, 8, 2, 3, 2, 8};
        Integer[] fourth = {null, 1, 7, 2, 7};
        Integer[] expected = {1, 2, 3, 4, null, 5, 6, 8, 7};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector).unionBy(Linq.asEnumerable(third), IntKeySelector).unionBy(Linq.asEnumerable(fourth), IntKeySelector));
    }

    @Test
    public void MultipleUnionsCustomComparer() {
        Integer[] first = {1, 102, 903, 204, null, 5, 601};
        Integer[] second = {6, 202, 903, 204, 5, 106};
        Integer[] third = {2, 308, 2, 103, 802, 308};
        Integer[] fourth = {null, 101, 207, 202, 207};
        Integer[] expected = {1, 102, 903, 204, null, 5, 6, 308, 207};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), IntKeySelector, new Modulo100EqualityComparer()).unionBy(Linq.asEnumerable(third), IntKeySelector, new Modulo100EqualityComparer()).unionBy(Linq.asEnumerable(fourth), IntKeySelector, new Modulo100EqualityComparer()));
    }

    @Test
    public void MultipleUnionsDifferentComparers() {
        String[] first = {"Alpha", "Bravo", "Charlie", "Bravo", "Delta", "atleD", "ovarB"};
        String[] second = {"Charlie", "Delta", "Echo", "Foxtrot", "Foxtrot", "choE"};
        String[] third = {"trotFox", "Golf", "Alpha", "choE", "Tango"};

        String[] plainThenAnagram = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Tango"};
        String[] anagramThenPlain = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "trotFox", "Golf", "choE", "Tango"};

        assertEquals(Linq.asEnumerable(plainThenAnagram), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).unionBy(Linq.asEnumerable(third), StringKeySelector, new AnagramEqualityComparer()));
        assertEquals(Linq.asEnumerable(anagramThenPlain), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, new AnagramEqualityComparer()).unionBy(Linq.asEnumerable(third), StringKeySelector));
    }

    @Test
    public void NullEqualityComparer() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector, null));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).unionBy(Linq.range(0, 3), IntKeySelector);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerateMultipleUnions() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).unionBy(Linq.range(0, 3), IntKeySelector).unionBy(Linq.range(2, 4), IntKeySelector).unionBy(Linq.asEnumerable(new int[]{
                9, 2, 4
        }), IntKeySelector);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    public void ToArray() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).toArray());
    }

    @Test
    public void ToArrayMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo", "Albert"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).unionBy(Linq.asEnumerable(third), StringKeySelector).toArray());
    }

    @Test
    public void ToList() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).toList()));
    }

    @Test
    public void ToListMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};
        String[] expected = {"Bob", "Robert", "Tim", "Matt", "miT", "ttaM", "Charlie", "Bbo", "Albert"};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).unionBy(Linq.asEnumerable(third), StringKeySelector).toList()));
    }

    @Test
    public void Count() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};

        assertEquals(8, Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).count());
    }

    @Test
    public void CountMultipleUnion() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Bob", "Albert", "Tim"};

        assertEquals(9, Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).unionBy(Linq.asEnumerable(third), StringKeySelector).count());
    }

    @Test
    public void RepeatEnumerating() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};

        IEnumerable<String> result = Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector);

        assertEquals(result, result);
    }

    @Test
    public void RepeatEnumeratingMultipleUnions() {
        String[] first = {"Bob", "Robert", "Tim", "Matt", "miT"};
        String[] second = {"ttaM", "Charlie", "Bbo"};
        String[] third = {"Matt", "Albert", "Ichabod"};

        IEnumerable<String> result = Linq.asEnumerable(first).unionBy(Linq.asEnumerable(second), StringKeySelector).unionBy(Linq.asEnumerable(third), StringKeySelector);
        assertEquals(result, result);
    }

    @Test
    public void HashSetWithBuiltInComparer_HashSetContainsNotUsed() {
        HashSet<String> set1 = new HashSet<>(StringComparer.OrdinalIgnoreCase);
        set1.add("a");
        IEnumerable<String> input1 = Linq.asEnumerable(set1);
        IEnumerable<String> input2 = Linq.asEnumerable(new String[]{"A"});

        assertEquals(Linq.asEnumerable("a", "A"), input1.unionBy(input2, StringKeySelector));
        assertEquals(Linq.asEnumerable("a", "A"), input1.unionBy(input2, StringKeySelector, null));
        assertEquals(Linq.asEnumerable("a", "A"), input1.unionBy(input2, StringKeySelector, EqualityComparer.Default()));
        assertEquals(Linq.asEnumerable(new String[]{"a"}), input1.unionBy(input2, StringKeySelector, StringComparer.OrdinalIgnoreCase));

        assertEquals(Linq.asEnumerable("A", "a"), input2.unionBy(input1, StringKeySelector));
        assertEquals(Linq.asEnumerable("A", "a"), input2.unionBy(input1, StringKeySelector, null));
        assertEquals(Linq.asEnumerable("A", "a"), input2.unionBy(input1, StringKeySelector, EqualityComparer.Default()));
        assertEquals(Linq.asEnumerable(new String[]{"A"}), input2.unionBy(input1, StringKeySelector, StringComparer.OrdinalIgnoreCase));
    }

    @Test
    public void testMultiUnionBySameSelector() {
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

        IEnumerable<People> peoples = Linq.asEnumerable(source1).unionBy(Linq.asEnumerable(source2), x -> x.groupId).unionBy(Linq.asEnumerable(source3), x -> x.groupId);
        assertSame(UnionByIterator2.class, peoples.getClass());
        assertEquals(Linq.asEnumerable(expect), peoples);

        Func1<People, Integer> groupSelector = x -> x.groupId;
        IEnumerable<People> peoples2 = Linq.asEnumerable(source1).unionBy(Linq.asEnumerable(source2), groupSelector).unionBy(Linq.asEnumerable(source3), groupSelector);
        assertSame(UnionByIteratorN.class, peoples2.getClass());
        assertEquals(Linq.asEnumerable(expect), peoples2);
    }

    @Test
    public void testMultiUnionByDiffSelector() {
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

        IEnumerable<People> peoples = Linq.asEnumerable(source1).unionBy(Linq.asEnumerable(source2), x -> x.groupId).unionBy(Linq.asEnumerable(source3), x -> x.id);
        assertSame(UnionByIterator2.class, peoples.getClass());
        assertEquals(Linq.asEnumerable(expect), peoples);
    }

    @Test
    public void testUnionBy() {
        IEnumerable<Employee> enumerable = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno);
        assertEquals(4, enumerable.count());
        UnionByIterator2<Employee, Integer> unionByIterator2 = (UnionByIterator2<Employee, Integer>) enumerable;
        assertEquals(4, unionByIterator2._toArray(Employee.class).length);
        assertEquals(4, unionByIterator2._toArray().length);
        assertEquals(4, unionByIterator2._toList().size());
        assertEquals(-1, unionByIterator2._getCount(true));
        assertEquals(4, unionByIterator2._getCount(false));


        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), selector)
                .unionBy(Linq.asEnumerable(emps), selector);
        assertEquals(4, enumerable2.count());
        UnionByIteratorN<Employee, Integer> unionByIterator22 = (UnionByIteratorN<Employee, Integer>) enumerable2;
        assertEquals(4, unionByIterator22._toArray(Employee.class).length);
        assertEquals(4, unionByIterator22._toArray().length);
        assertEquals(4, unionByIterator22._toList().size());
        assertEquals(-1, unionByIterator22._getCount(true));
        assertEquals(4, unionByIterator22._getCount(false));
    }

    @Test
    public void testUnionByWithComparer() {
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


        IEnumerable<Employee> enumerable = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno, comparer)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno, comparer);
        assertEquals(1, enumerable.count());
        UnionByIterator2<Employee, Integer> unionByIterator2 = (UnionByIterator2<Employee, Integer>) enumerable;
        assertEquals(1, unionByIterator2._toArray(Employee.class).length);
        assertEquals(1, unionByIterator2._toArray().length);
        assertEquals(1, unionByIterator2._toList().size());
        assertEquals(-1, unionByIterator2._getCount(true));
        assertEquals(1, unionByIterator2._getCount(false));


        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), selector, comparer)
                .unionBy(Linq.asEnumerable(emps), selector, comparer);
        assertEquals(1, enumerable2.count());
        UnionByIteratorN<Employee, Integer> unionByIterator22 = (UnionByIteratorN<Employee, Integer>) enumerable2;
        assertEquals(1, unionByIterator22._toArray(Employee.class).length);
        assertEquals(1, unionByIterator22._toArray().length);
        assertEquals(1, unionByIterator22._toList().size());
        assertEquals(-1, unionByIterator22._getCount(true));
        assertEquals(1, unionByIterator22._getCount(false));
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
        final int groupId;
        final int id;
        final String name;

        private People(int groupId, int id, String name) {
            this.groupId = groupId;
            this.id = id;
            this.name = name;
        }
    }
}
