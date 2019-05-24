package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Test;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ToLookupTest extends TestCase {
    @Test
    public void testToLookup() {
        ILookup<Integer, Employee> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.deptno);
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
    }

    @Test
    public void testToLookupComparer() {
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
        ILookup<String, Employee> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.name, comparer);
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
        ILookup<Integer, Employee> lookup2 = Linq.singleton(badEmp).concat(Linq.asEnumerable(emps)).toLookup(emp -> emp.deptno, comparer2);
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
    public void testToLookupSelector() {
        ILookup<Integer, String> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.deptno, emp -> emp.name);
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
    public void testToLookupSelectorComparer() {
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

        ILookup<String, Integer> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.name, emp -> emp.empno, comparer);
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
        ILookup<Integer, String> lookup2 = Linq.singleton(badEmp).concat(Linq.asEnumerable(emps)).toLookup(emp -> emp.deptno, emp -> emp.name, comparer2);
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
}
