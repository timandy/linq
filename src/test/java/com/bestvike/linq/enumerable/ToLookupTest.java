package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class ToLookupTest extends TestCase {
    private static <K, T> void AssertMatches(IEnumerable<K> keys, IEnumerable<T> elements, ILookup<K, T> lookup) {
        assertNotNull(lookup);
        assertNotNull(keys);
        assertNotNull(elements);

        int num = 0;
        try (IEnumerator<K> keyEnumerator = keys.enumerator();
             IEnumerator<T> elEnumerator = elements.enumerator()) {
            while (keyEnumerator.moveNext()) {
                assertTrue(lookup.containsKey(keyEnumerator.current()));

                for (T e : lookup.get(keyEnumerator.current())) {
                    assertTrue(elEnumerator.moveNext());
                    assertEquals(e, elEnumerator.current());
                }
                ++num;
            }
            assertFalse(elEnumerator.moveNext());
        }
        assertEquals(num, lookup.getCount());
    }

    private static IEnumerable<Object[]> ApplyResultSelectorForGroup_TestData() {
        ArgsList argsList = new ArgsList();
        argsList.add(1);
        argsList.add(2);
        argsList.add(3);
        argsList.add(4);
        return argsList;
    }

    @Test
    void SameResultsRepeatCall() {
        IEnumerable<String> q1 = Linq.of("Alen", "Felix", null, null, "X", "Have Space", "Clinton", "");
        IEnumerable<Integer> q2 = Linq.of(new int[]{55, 49, 9, -100, 24, 25, -1, 0});
        IEnumerable<Tuple2<String, Integer>> q = q1.selectMany(a -> q2, (x3, x4) -> Tuple.create(x3, x4));


        assertEquals(q.toLookup(e -> e.getItem1()), q.toLookup(e -> e.getItem1()));
    }

    @Test
    void NullKeyIncluded() {
        String[] key = {"Chris", "Bob", null, "Tim"};
        int[] element = {50, 95, 55, 90};
        IEnumerable<NameScore> source = Linq.of(key).zip(Linq.of(element), (k, e) -> new NameScore(k, e));

        AssertMatches(Linq.of(key), source, source.toLookup(e -> e.Name));
    }

    @Test
    void OneElementCustomComparer() {
        String[] key = {"Chris"};
        int[] element = {50};
        NameScore[] source = new NameScore[]{new NameScore("risCh", 50)};

        AssertMatches(Linq.of(key), Linq.of(source), Linq.of(source).toLookup(e -> e.Name, new AnagramEqualityComparer()));
    }

    @Test
    void UniqueElementsElementSelector() {
        String[] key = {"Chris", "Prakash", "Tim", "Robert", "Brian"};
        int[] element = {50, 100, 95, 60, 80};
        NameScore[] source = new NameScore[]{
                new NameScore(key[0], element[0]),
                new NameScore(key[1], element[1]),
                new NameScore(key[2], element[2]),
                new NameScore(key[3], element[3]),
                new NameScore(key[4], element[4])
        };

        AssertMatches(Linq.of(key), Linq.of(element), Linq.of(source).toLookup(e -> e.Name, e -> e.Score));
    }

    @Test
    void DuplicateKeys() {
        String[] key = {"Chris", "Prakash", "Robert"};
        int[] element = {50, 80, 100, 95, 99, 56};
        NameScore[] source = new NameScore[]{
                new NameScore(key[0], element[0]),
                new NameScore(key[1], element[2]),
                new NameScore(key[2], element[5]),
                new NameScore(key[1], element[3]),
                new NameScore(key[0], element[1]),
                new NameScore(key[1], element[4])
        };

        AssertMatches(Linq.of(key), Linq.of(element), Linq.of(source).toLookup(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
    }

    @Test
    void RunOnce() {
        String[] key = {"Chris", "Prakash", "Robert"};
        int[] element = {50, 80, 100, 95, 99, 56};
        NameScore[] source = new NameScore[]{
                new NameScore(key[0], element[0]),
                new NameScore(key[1], element[2]),
                new NameScore(key[2], element[5]),
                new NameScore(key[1], element[3]),
                new NameScore(key[0], element[1]),
                new NameScore(key[1], element[4])
        };

        AssertMatches(Linq.of(key), Linq.of(element), Linq.of(source).runOnce().toLookup(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
    }

    @Test
    void Count() {
        String[] key = {"Chris", "Prakash", "Robert"};
        int[] element = {50, 80, 100, 95, 99, 56};
        NameScore[] source = new NameScore[]{
                new NameScore(key[0], element[0]),
                new NameScore(key[1], element[2]),
                new NameScore(key[2], element[5]),
                new NameScore(key[1], element[3]),
                new NameScore(key[0], element[1]),
                new NameScore(key[1], element[4])
        };

        assertEquals(3, Linq.of(source).toLookup(e -> e.Name, e -> e.Score).count());
    }

    @Test
    void EmptySource() {
        String[] key = {};
        int[] element = {};
        IEnumerable<NameScore> source = Linq.of(key).zip(Linq.of(element), (k, e) -> new NameScore(k, e));

        AssertMatches(Linq.of(key), Linq.of(element), Linq.of(source).toLookup(e -> e.Name, e -> e.Score, new AnagramEqualityComparer()));
    }

    @Test
    void SingleNullKeyAndElement() {
        String[] key = {null};
        String[] element = {null};
        String[] source = new String[]{null};

        AssertMatches(Linq.of(key), Linq.of(element), Linq.of(source).toLookup(e -> e, e -> e, EqualityComparer.Default()));
    }

    @Test
    void NullSource() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toLookup(i -> i / 10));
    }

    @Test
    void NullSourceExplicitComparer() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toLookup(i -> i / 10, EqualityComparer.Default()));
    }

    @Test
    void NullSourceElementSelector() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toLookup(i -> i / 10, i -> i + 2));
    }

    @Test
    void NullSourceElementSelectorExplicitComparer() {
        IEnumerable<Integer> source = null;
        assertThrows(NullPointerException.class, () -> source.toLookup(i -> i / 10, i -> i + 2, EqualityComparer.Default()));
    }

    @Test
    void NullKeySelector() {
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(keySelector));
    }

    @Test
    void NullKeySelectorExplicitComparer() {
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(keySelector, EqualityComparer.Default()));
    }

    @Test
    void NullKeySelectorElementSelector() {
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(keySelector, i -> i + 2));
    }

    @Test
    void NullKeySelectorElementSelectorExplicitComparer() {
        Func1<Integer, Integer> keySelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(keySelector, i -> i + 2, EqualityComparer.Default()));
    }

    @Test
    void NullElementSelector() {
        Func1<Integer, Integer> elementSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(i -> i / 10, elementSelector));
    }

    @Test
    void NullElementSelectorExplicitComparer() {
        Func1<Integer, Integer> elementSelector = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 1000).toLookup(i -> i / 10, elementSelector, EqualityComparer.Default()));
    }

    @ParameterizedTest
    @MethodSource("ApplyResultSelectorForGroup_TestData")
    void ApplyResultSelectorForGroup(int enumType) {
        //Create test data
        List<Role> roles = Arrays.asList(
                new Role(1),
                new Role(2),
                new Role(3)
        );

        IEnumerable<Membership> memberships = Linq.range(0, 50).select(i -> new Membership(i,
                roles.get(i % 3),
                i % 3 == 0));

        //Run actual test
        IEnumerable<RoleMetadata> grouping = memberships.groupBy(
                m -> m.Role,
                (role, mems) -> new RoleMetadata(
                        role,
                        mems.count(m -> m.CountMe),
                        mems.count(m -> !m.CountMe)));

        IEnumerable<RoleMetadata> result;
        switch (enumType) {
            case 1:
                result = Linq.of(grouping.toList());
                break;
            case 2:
                result = grouping.toArray();
                break;
            case 3:
                result = Linq.of(grouping.toArray(RoleMetadata.class));
                break;
            default:
                result = grouping;
                break;
        }

        RoleMetadata[] expected = new RoleMetadata[]{
                new RoleMetadata(new Role(1), 17, 0),
                new RoleMetadata(new Role(2), 0, 17),
                new RoleMetadata(new Role(3), 0, 16)
        };

        assertEquals(Linq.of(expected), result);
    }

    @Test
    void testToLookup() {
        ILookup<Integer, Employee> lookup = Linq.of(emps).toLookup(emp -> emp.deptno);
        assertTrue(lookup.containsKey(10));
        assertEquals(3, lookup.get(10).count());
        int n = 0;
        for (IGrouping<Integer, Employee> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case 10:
                    assertEquals(3, grouping.count());
                    break;
                case 30:
                    assertEquals(1, grouping.count());
                    break;
                default:
                    fail("toLookup() fail " + grouping);
            }
        }
        assertEquals(2, n);

        ILookup<Integer, Integer> lookup2 = Linq.range(0, 10).toLookup(x -> x);
        assertEquals(10, lookup2.getCount());
    }

    @Test
    void testToLookupComparer() {
        IEqualityComparer<String> comparer = new IEqualityComparer<String>() {
            @Override
            public boolean equals(String x, String y) {
                return Objects.equals(x, y) || x.length() == y.length();
            }

            @Override
            public int hashCode(String obj) {
                return obj == null ? 0 : obj.length();
            }
        };
        ILookup<String, Employee> lookup = Linq.of(emps).toLookup(emp -> emp.name, comparer);
        assertTrue(lookup.containsKey("Fred"));
        assertEquals(3, lookup.get("Fred").count());
        int n = 0;
        for (IGrouping<String, Employee> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case "Fred":
                    assertEquals(3, grouping.count());
                    break;
                case "Janet":
                    assertEquals(1, grouping.count());
                    break;
                default:
                    fail("toLookup() with comparer fail " + grouping);
            }
        }
        assertEquals(2, n);

        IEqualityComparer<Integer> comparer2 = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        Employee badEmp = new Employee(160, "Tim", null);
        ILookup<Integer, Employee> lookup2 = Linq.singleton(badEmp).concat(Linq.of(emps)).toLookup(emp -> emp.deptno, comparer2);
        assertTrue(lookup2.containsKey(null));
        assertEquals(5, lookup2.get(null).count());
        int n2 = 0;
        for (IGrouping<Integer, Employee> grouping : lookup2) {
            ++n2;
            if (grouping.getKey() == null)
                assertEquals(5, grouping.count());
            else
                fail("toLookup() with comparer fail " + grouping);

        }
        assertEquals(1, n2);
    }

    @Test
    void testToLookupSelector() {
        ILookup<Integer, String> lookup = Linq.of(emps).toLookup(emp -> emp.deptno, emp -> emp.name);
        assertTrue(lookup.containsKey(10));
        assertEquals(3, lookup.get(10).count());
        int n = 0;
        for (IGrouping<Integer, String> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case 10:
                    assertEquals(3, grouping.count());
                    assertTrue(grouping.contains("Fred"));
                    assertTrue(grouping.contains("Eric"));
                    assertTrue(grouping.contains("Janet"));
                    assertFalse(grouping.contains("Bill"));
                    break;
                case 30:
                    assertEquals(1, grouping.count());
                    assertTrue(grouping.contains("Bill"));
                    assertFalse(grouping.contains("Fred"));
                    break;
                default:
                    fail("toLookup() with elementSelector error " + grouping);
            }
        }
        assertEquals(2, n);
    }

    @Test
    void testToLookupSelectorComparer() {
        IEqualityComparer<String> comparer = new IEqualityComparer<String>() {
            @Override
            public boolean equals(String x, String y) {
                return Objects.equals(x, y) || x.length() == y.length();
            }

            @Override
            public int hashCode(String obj) {
                return obj == null ? 0 : obj.length();
            }
        };

        ILookup<String, Integer> lookup = Linq.of(emps).toLookup(emp -> emp.name, emp -> emp.empno, comparer);
        int n = 0;
        for (IGrouping<String, Integer> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case "Fred":
                    assertEquals(3, grouping.count());
                    assertTrue(grouping.contains(100));
                    assertTrue(grouping.contains(110));
                    assertTrue(grouping.contains(120));
                    assertFalse(grouping.contains(130));
                    break;
                case "Janet":
                    assertEquals(1, grouping.count());
                    assertTrue(grouping.contains(130));
                    assertFalse(grouping.contains(110));
                    break;
                default:
                    fail("toLookup() with elementSelector and comparer error " + grouping);
            }
        }
        assertEquals(2, n);


        IEqualityComparer<Integer> comparer2 = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return true;
            }

            @Override
            public int hashCode(Integer obj) {
                return 0;
            }
        };
        Employee badEmp = new Employee(160, "Tim", null);
        ILookup<Integer, String> lookup2 = Linq.singleton(badEmp).concat(Linq.of(emps)).toLookup(emp -> emp.deptno, emp -> emp.name, comparer2);
        assertTrue(lookup2.containsKey(null));
        assertEquals(5, lookup2.get(null).count());
        int n2 = 0;
        for (IGrouping<Integer, String> grouping : lookup2) {
            ++n2;
            if (grouping.getKey() == null) {
                assertEquals(5, grouping.count());
                assertTrue(grouping.contains("Tim"));
                assertTrue(grouping.contains("Fred"));
                assertTrue(grouping.contains("Bill"));
                assertTrue(grouping.contains("Eric"));
                assertTrue(grouping.contains("Janet"));
                assertFalse(grouping.contains("Cedric"));
            } else {
                fail("toLookup() with comparer fail " + grouping);
            }
        }
        assertEquals(1, n2);
    }


    private static class NameScore extends ValueType {
        private final String Name;
        private final int Score;

        private NameScore(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    private static class Membership extends ValueType {
        private final int Id;
        private final Role Role;
        private final boolean CountMe;

        private Membership(int id, Role role, boolean countMe) {
            this.Id = id;
            this.Role = role;
            this.CountMe = countMe;
        }
    }

    private static class Role extends ValueType {
        private final int Id;

        private Role(int id) {
            this.Id = id;
        }
    }

    private static class RoleMetadata extends ValueType {
        private final Role Role;
        private final int CountA;
        private final int CountrB;

        private RoleMetadata(Role role, int countA, int countrB) {
            this.Role = role;
            this.CountA = countA;
            this.CountrB = countrB;
        }
    }
}
