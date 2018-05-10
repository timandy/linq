package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ToLookupTest extends IteratorTest {

    @Test
    public void testToLookup() {
        final ILookup<Integer, Employee> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.deptno);
        Assert.assertTrue(lookup.containsKey(10));
        Assert.assertEquals(3, lookup.get(10).count());
        int n = 0;
        for (IGrouping<Integer, Employee> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case 10:
                    Assert.assertEquals(3, grouping.count());
                    break;
                case 30:
                    Assert.assertEquals(1, grouping.count());
                    break;
                default:
                    Assert.fail("toLookup() fail " + grouping);
            }
        }
        Assert.assertEquals(2, n);
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
        final ILookup<String, Employee> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.name, comparer);
        Assert.assertTrue(lookup.containsKey("Fred"));
        Assert.assertEquals(3, lookup.get("Fred").count());
        int n = 0;
        for (IGrouping<String, Employee> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case "Fred":
                    Assert.assertEquals(3, grouping.count());
                    break;
                case "Janet":
                    Assert.assertEquals(1, grouping.count());
                    break;
                default:
                    Assert.fail("toLookup() with comparer fail " + grouping);
            }
        }
        Assert.assertEquals(2, n);

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
        final Employee badEmp = new Employee(160, "Tim", null);
        final ILookup<Integer, Employee> lookup2 = Linq.singletonEnumerable(badEmp).concat(Linq.asEnumerable(emps)).toLookup(emp -> emp.deptno, comparer2);
        Assert.assertTrue(lookup2.containsKey(null));
        Assert.assertEquals(5, lookup2.get(null).count());
        int n2 = 0;
        for (IGrouping<Integer, Employee> grouping : lookup2) {
            ++n2;
            if (grouping.getKey() == null)
                Assert.assertEquals(5, grouping.count());
            else
                Assert.fail("toLookup() with comparer fail " + grouping);

        }
        Assert.assertEquals(1, n2);
    }

    @Test
    public void testToLookupSelector() {
        final ILookup<Integer, String> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.deptno, emp -> emp.name);
        Assert.assertTrue(lookup.containsKey(10));
        Assert.assertEquals(3, lookup.get(10).count());
        int n = 0;
        for (IGrouping<Integer, String> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case 10:
                    Assert.assertEquals(3, grouping.count());
                    Assert.assertTrue(grouping.contains("Fred"));
                    Assert.assertTrue(grouping.contains("Eric"));
                    Assert.assertTrue(grouping.contains("Janet"));
                    Assert.assertFalse(grouping.contains("Bill"));
                    break;
                case 30:
                    Assert.assertEquals(1, grouping.count());
                    Assert.assertTrue(grouping.contains("Bill"));
                    Assert.assertFalse(grouping.contains("Fred"));
                    break;
                default:
                    Assert.fail("toLookup() with elementSelector error " + grouping);
            }
        }
        Assert.assertEquals(2, n);
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

        final ILookup<String, Integer> lookup = Linq.asEnumerable(emps).toLookup(emp -> emp.name, emp -> emp.empno, comparer);
        int n = 0;
        for (IGrouping<String, Integer> grouping : lookup) {
            ++n;
            switch (grouping.getKey()) {
                case "Fred":
                    Assert.assertEquals(3, grouping.count());
                    Assert.assertTrue(grouping.contains(100));
                    Assert.assertTrue(grouping.contains(110));
                    Assert.assertTrue(grouping.contains(120));
                    Assert.assertFalse(grouping.contains(130));
                    break;
                case "Janet":
                    Assert.assertEquals(1, grouping.count());
                    Assert.assertTrue(grouping.contains(130));
                    Assert.assertFalse(grouping.contains(110));
                    break;
                default:
                    Assert.fail("toLookup() with elementSelector and comparer error " + grouping);
            }
        }
        Assert.assertEquals(2, n);


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
        final Employee badEmp = new Employee(160, "Tim", null);
        final ILookup<Integer, String> lookup2 = Linq.singletonEnumerable(badEmp).concat(Linq.asEnumerable(emps)).toLookup(emp -> emp.deptno, emp -> emp.name, comparer2);
        Assert.assertTrue(lookup2.containsKey(null));
        Assert.assertEquals(5, lookup2.get(null).count());
        int n2 = 0;
        for (IGrouping<Integer, String> grouping : lookup2) {
            ++n2;
            if (grouping.getKey() == null) {
                Assert.assertEquals(5, grouping.count());
                Assert.assertTrue(grouping.contains("Tim"));
                Assert.assertTrue(grouping.contains("Fred"));
                Assert.assertTrue(grouping.contains("Bill"));
                Assert.assertTrue(grouping.contains("Eric"));
                Assert.assertTrue(grouping.contains("Janet"));
                Assert.assertFalse(grouping.contains("Cedric"));
            } else {
                Assert.fail("toLookup() with comparer fail " + grouping);
            }
        }
        Assert.assertEquals(1, n2);
    }

}