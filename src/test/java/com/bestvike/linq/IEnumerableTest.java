package com.bestvike.linq;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.entity.IterableDemo;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple1;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by 许崇雷 on 2017/7/24.
 */
public class IEnumerableTest {
    private static final Employee[] badEmps = {
            new Employee(140, "Cedric", 40),
            new Employee(150, "Gates", null)};
    private static final Department[] badDepts = {
            new Department("Manager", null, Collections.emptyList())};
    private static final Employee[] emps = {
            new Employee(100, "Fred", 10),
            new Employee(110, "Bill", 30),
            new Employee(120, "Eric", 10),
            new Employee(130, "Janet", 10)};
    private static final Department[] depts = {
            new Department("Sales", 10, Arrays.asList(emps[0], emps[2], emps[3])),
            new Department("HR", 20, Collections.emptyList()),
            new Department("Marketing", 30, Collections.singletonList(emps[1]))};

    private static String stringJoin(Iterable<String> group) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<String> iterator = group.iterator();
        if (iterator.hasNext()) {
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append("+").append(iterator.next());
            }
        }
        return builder.toString();
    }

    @Test
    public void testWhere() {
        List<String> names = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric, Janet]", names.toString());

        List<String> names2 = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .where(employee -> employee.name.length() == 4)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric]", names2.toString());

        List<Integer> even = Linq.range(1, 10)
                .where(n -> n % 2 == 0)
                .toList();
        Assert.assertEquals("[2, 4, 6, 8, 10]", even.toString());

        Integer[] numbs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> even2 = Linq.asEnumerable(numbs)
                .where(n -> n % 2 == 0)
                .toList();
        Assert.assertEquals("[2, 4, 6, 8, 10]", even2.toString());
    }

    @Test
    public void testWhereIndexed() {
        // Returns every other employee.
        List<String> names = Linq.asEnumerable(emps)
                .where((employee, n) -> n % 2 == 0)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric]", names.toString());
    }

    @Test
    public void testSelect() {
        List<String> names = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .toList();
        Assert.assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<Character> names2 = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .select(name -> name.charAt(0))
                .toList();
        Assert.assertEquals("[F, B, E, J]", names2.toString());
    }

    @Test
    public void testSelectIndexed() {
        List<String> names = Linq.asEnumerable(emps)
                .select((emp, index) -> emp.name)
                .toList();
        Assert.assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<String> indexes = Linq.asEnumerable(emps)
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        Assert.assertEquals("[#0: Fred, #1: Bill, #2: Eric, #3: Janet]", indexes.toString());
    }

    @Test
    public void testSelectMany() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees))
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        Assert.assertEquals("[#0: Fred, #1: Eric, #2: Janet, #3: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexed() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((dept, index) -> Linq.asEnumerable(dept.employees).select(emp -> String.format("#%d: %s", index, emp.name)))
                .toList();
        Assert.assertEquals("[#0: Fred, #0: Eric, #0: Janet, #2: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManySelect() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany(dept -> Linq.asEnumerable(dept.employees), (dept, emp) -> String.format("#%s: %s", dept.name, emp.name))
                .toList();
        Assert.assertEquals("[#Sales: Fred, #Sales: Eric, #Sales: Janet, #Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyIndexedSelect() {
        final List<String> nameSeqs = Linq.asEnumerable(depts)
                .selectMany((dept, index) -> Linq.asEnumerable(dept.employees).select(emp -> Tuple.create(index, emp)), (dept, empInfo) -> String.format("#%s: %s: %s", empInfo.getItem1(), dept.name, empInfo.getItem2().name))
                .toList();
        Assert.assertEquals("[#0: Sales: Fred, #0: Sales: Eric, #0: Sales: Janet, #2: Marketing: Bill]", nameSeqs.toString());
    }

    @Test
    public void testSelectManyCrossJoin() {
        //selectMany 实现简单的 cross join
        String cross = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .selectMany(emp -> Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)).select(dept -> String.format("%s works in %s", emp.name, dept.name)))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", cross);
    }

    @Test
    public void testTake() {
        final List<Department> enumerableDeptsResult = Linq.asEnumerable(depts).take(2).toList();
        Assert.assertEquals(2, enumerableDeptsResult.size());
        Assert.assertEquals(depts[0], enumerableDeptsResult.get(0));
        Assert.assertEquals(depts[1], enumerableDeptsResult.get(1));

        final List<Department> enumerableDeptsResult5 = Linq.asEnumerable(depts).take(5).toList();
        Assert.assertEquals(3, enumerableDeptsResult5.size());
    }

    @Test
    public void testTakeWhile() {
        final List<Department> deptList = Linq.asEnumerable(depts).takeWhile(dept -> dept.name.contains("e")).toList();
        // Only one department:
        // 0: Sales --> true
        // 1: HR --> false
        // 2: Marketing --> never get to it (we stop after false)
        Assert.assertEquals(1, deptList.size());
        Assert.assertEquals(depts[0], deptList.get(0));
    }

    @Test
    public void testTakeWhileIndexed() {
        final List<Department> deptList = Linq.asEnumerable(depts).takeWhile((dept, index) -> index < 2).toList();
        Assert.assertEquals(2, deptList.size());
        Assert.assertEquals(depts[0], deptList.get(0));
        Assert.assertEquals(depts[1], deptList.get(1));
    }

    @Test
    public void testSkip() {
        Assert.assertEquals(2, Linq.asEnumerable(depts).skip(1).count());
        Assert.assertEquals(0, Linq.asEnumerable(depts).skip(5).count());
    }

    @Test
    public void testSkipWhile() {
        Assert.assertEquals(2, Linq.asEnumerable(depts).skipWhile(dept -> dept.name.equals("Sales")).count());
        Assert.assertEquals(3, Linq.asEnumerable(depts).skipWhile(dept -> !dept.name.equals("Sales")).count());
    }

    @Test
    public void testSkipWhileIndexed() {
        int count = Linq.asEnumerable(depts)
                .skipWhile((dept, index) -> dept.name.equals("Sales") || index == 1)
                .count();
        Assert.assertEquals(1, count);
    }

    @Test
    public void testJoin() {
        //null key 被排除
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .join(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .join(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, Bill works in Marketing]", ss);
    }

    @Test
    public void testJoinWithComparer() {
        //null key 被排除
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

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .join(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .join(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing]", ss);
    }

    @Test
    public void testGroupJoin() {
        //左连接,empty dept 保留,bad Emp 被滤掉
        String s = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)).groupJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                dept -> dept.deptno,
                emp -> emp.deptno,
                (dept, emps) -> {
                    final StringBuilder buf = new StringBuilder("[");
                    int n = 0;
                    for (Employee employee : emps) {
                        if (n++ > 0) {
                            buf.append(", ");
                        }
                        buf.append(employee.name);
                    }
                    return buf.append("] work(s) in ").append(dept.name).toString();
                })
                .toList()
                .toString();
        Assert.assertEquals("[[Fred, Eric, Janet] work(s) in Sales, [] work(s) in HR, [Bill] work(s) in Marketing, [] work(s) in Manager]", s);
    }

    @Test
    public void testGroupJoinWithComparer() {
        //交叉连接,但 null key 不参与交叉
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

        String s = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .groupJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emps) -> {
                            final StringBuilder buf = new StringBuilder("[");
                            int n = 0;
                            for (Employee employee : emps) {
                                if (n++ > 0) {
                                    buf.append(", ");
                                }
                                buf.append(employee.name);
                            }
                            return buf.append("] work(s) in ").append(dept.name)
                                    .toString();
                        },
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[[Fred, Bill, Eric, Janet, Cedric] work(s) in Sales, [Fred, Bill, Eric, Janet, Cedric] work(s) in HR, [Fred, Bill, Eric, Janet, Cedric] work(s) in Marketing, [] work(s) in Manager]", s);
    }

    @Test
    public void testOrderBy() {
        //null 在前,值相等的按原始顺序
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", sss);

        Set<Integer> set = new HashSet<>();
        set.add(3);
        set.add(1);
        set.add(2);
        IEnumerable<Integer> ordered = Linq.asEnumerable(set).orderBy(a -> a);
        Integer[] orderedArr = {1, 2, 3};
        Assert.assertTrue(ordered.sequenceEqual(Linq.asEnumerable(orderedArr)));
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
        Assert.assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", sss);
    }

    @Test
    public void testOrderByDesc() {
        //null 在后,值相等的按原始顺序,同 testOrderByWithComparer()
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", sss);
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
        Assert.assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", sss);
    }

    @Test
    public void testGroupByWithKeySelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndComparer() {
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

        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group)))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndComparer() {
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
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, emp -> emp.name, comparer)
                .select(group -> String.format("%s: %s", group.getKey(), stringJoin(group)))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelector() {
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno, (key, group) -> String.format("%s: %s", key, stringJoin(group.select(element -> element.name))))
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndResultSelectorAndComparer() {
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
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno,
                        (key, group) -> String.format("%s: %s", key, stringJoin(group.select(element -> element.name))),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelector() {
        String s =
                Linq.asEnumerable(emps)
                        .groupBy(emp -> emp.deptno, emp -> emp.name, (key, group) -> String.format("%s: %s", key, stringJoin(group)))
                        .toList()
                        .toString();
        Assert.assertEquals("[10: Fred+Eric+Janet, 30: Bill]", s);
    }

    @Test
    public void testGroupByWithKeySelectorAndElementSelectorAndResultSelectorAndComparer() {
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
        String s = Linq.asEnumerable(emps)
                .groupBy(emp -> emp.deptno,
                        emp -> emp.name,
                        (key, group) -> String.format("%s: %s", key, stringJoin(group)),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[10: Fred+Bill+Eric+Janet]", s);
    }

    @Test
    public void testConcat() {
        Assert.assertEquals(6, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).count());
    }

    @Test
    public void testZip() {
        final IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b", "c"));
        final IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));

        final IEnumerable<String> zipped = e1.zip(e2, (v0, v1) -> v0 + v1);
        Assert.assertEquals(3, zipped.count());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals("" + (char) ('a' + i) + (char) ('1' + i), zipped.elementAt(i));
        }
    }

    @Test
    public void testZipLengthNotMatch() {
        final IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b"));
        final IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));


        final IEnumerable<String> zipped1 = e1.zip(e2, (v0, v1) -> v0 + v1);
        Assert.assertEquals(2, zipped1.count());
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals("" + (char) ('a' + i) + (char) ('1' + i), zipped1.elementAt(i));
        }

        final IEnumerable<String> zipped2 = e2.zip(e1, (v0, v1) -> v0 + v1);
        Assert.assertEquals(2, zipped2.count());
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals("" + (char) ('1' + i) + (char) ('a' + i), zipped2.elementAt(i));
        }
    }

    @Test
    public void testDistinct() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[0],
                emps[3],
        };
        Assert.assertEquals(3, Linq.asEnumerable(emps2).distinct().count());
    }

    @Test
    public void testDistinctWithEqualityComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno.hashCode();
            }
        };

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[1],
                emps[3]
        };
        Assert.assertEquals(2, Linq.asEnumerable(emps2).distinct(comparer).count());
    }

    @Test
    public void testUnion() {
        Assert.assertEquals(6, Linq.asEnumerable(emps)
                .union(Linq.asEnumerable(badEmps))
                .union(Linq.asEnumerable(emps))
                .count());
    }

    @Test
    public void testUnionWithComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno == null ? 0 : obj.deptno;
            }
        };

        Assert.assertEquals(4, Linq.asEnumerable(emps)
                .union(Linq.asEnumerable(badEmps), comparer)
                .union(Linq.asEnumerable(emps), comparer)
                .count());
    }

    @Test
    public void testIntersect() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .intersect(Linq.asEnumerable(emps2))
                .count());
    }

    @Test
    public void testIntersectWithComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno == null ? 0 : obj.deptno;
            }
        };

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10)
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .intersect(Linq.asEnumerable(emps2), comparer)
                .count());
    }

    @Test
    public void testExcept() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        Assert.assertEquals(3, Linq.asEnumerable(emps)
                .except(Linq.asEnumerable(emps2))
                .count());

        final IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        final IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        final IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        Assert.assertTrue(oneToHundred.except(oneToFifty).sequenceEqual(fiftyOneToHundred));
    }

    @Test
    public void testExceptWithComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return Objects.equals(x.deptno, y.deptno);
            }

            @Override
            public int hashCode(Employee obj) {
                return obj.deptno == null ? 0 : obj.deptno;
            }
        };

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .except(Linq.asEnumerable(emps2), comparer)
                .count());
    }

    @Test
    public void testReverse() {
        //null 在前,值相等的按原始顺序
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        //reverse 与 orderByDescending 不同.reverse 是完全反序,orderByDescending  如果相等保持原始顺序
        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .reverse()
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Janet, Eric, Fred, Gates]", ss);

        Character[] lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).reverse().toArray(Character.class);
        Assert.assertEquals(5, lst.length);
        Assert.assertEquals("h", lst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.asEnumerable(arrChar).reverse().toArray(Character.class);
        Assert.assertEquals(5, arr.length);
        Assert.assertEquals("h", arr[4].toString());

        Character[] hello = Linq.asEnumerable("hello").reverse().toArray(Character.class);
        Assert.assertEquals(5, hello.length);
        Assert.assertEquals("h", hello[4].toString());

        Character[] h = Linq.singletonEnumerable('h').reverse().toArray(Character.class);
        Assert.assertEquals(1, h.length);
        Assert.assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().reverse().toArray(Character.class);
        Assert.assertEquals(0, empty.length);
    }

    @Test
    public void testSequenceEqual() {
        final List<Employee> list = Linq.asEnumerable(emps).toList();

        Assert.assertTrue(Linq.asEnumerable(list).sequenceEqual(Linq.asEnumerable(emps)));
    }

    @Test
    public void testSequenceEqualWithComparer() {
        final int[] array1 = {1, 2, 3};
        final int[] array2 = {11, 12, 13};

        IEqualityComparer<Integer> comparer = new IEqualityComparer<Integer>() {
            @Override
            public boolean equals(Integer x, Integer y) {
                return x % 10 == y % 10;
            }

            @Override
            public int hashCode(Integer obj) {
                return (obj % 10);
            }
        };

        Assert.assertTrue(Linq.asEnumerable(array1).sequenceEqual(Linq.asEnumerable(array2), comparer));
    }

    @Test
    public void testToArray() {
        Object[] source = {1, 2, 3};
        Integer[] target = Linq.asEnumerable(source).cast(Integer.class).toArray(Integer.class);
        Assert.assertEquals(3, target.length);
        Assert.assertTrue(Linq.asEnumerable(source).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        Integer[] target2 = Linq.asEnumerable(source2).toArray(Integer.class);
        Assert.assertEquals(3, target2.length);
        Assert.assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        Character[] lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toArray(Character.class);
        Assert.assertEquals(5, lst.length);
        Assert.assertEquals("o", lst[4].toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Character[] arr = Linq.asEnumerable(arrChar).toArray(Character.class);
        Assert.assertEquals(5, arr.length);
        Assert.assertEquals("o", arr[4].toString());

        Character[] hello = Linq.asEnumerable("hello").toArray(Character.class);
        Assert.assertEquals(5, hello.length);
        Assert.assertEquals("o", hello[4].toString());

        Character[] h = Linq.singletonEnumerable('h').toArray(Character.class);
        Assert.assertEquals(1, h.length);
        Assert.assertEquals("h", h[0].toString());

        Character[] empty = Linq.<Character>empty().toArray(Character.class);
        Assert.assertEquals(0, empty.length);
    }

    @Test
    public void testToList() {
        Object[] source = {1, 2, 3};
        List<Integer> target = Linq.asEnumerable(source).cast(Integer.class).toList();
        Assert.assertEquals(3, target.size());
        Assert.assertTrue(Linq.asEnumerable(source).cast(Integer.class).sequenceEqual(Linq.asEnumerable(target)));

        Set<Integer> source2 = new HashSet<>();
        source2.add(1);
        source2.add(2);
        source2.add(3);
        List<Integer> target2 = Linq.asEnumerable(source2).toList();
        Assert.assertEquals(3, target2.size());
        Assert.assertTrue(Linq.asEnumerable(target2).sequenceEqual(Linq.asEnumerable(source2)));

        List<Character> lst = Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).toList();
        Assert.assertEquals(5, lst.size());
        Assert.assertEquals("o", lst.get(4).toString());

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        List<Character> arr = Linq.asEnumerable(arrChar).toList();
        Assert.assertEquals(5, arr.size());
        Assert.assertEquals("o", arr.get(4).toString());

        List<Character> hello = Linq.asEnumerable("hello").toList();
        Assert.assertEquals(5, hello.size());
        Assert.assertEquals("o", hello.get(4).toString());

        List<Character> h = Linq.singletonEnumerable('h').toList();
        Assert.assertEquals(1, h.size());
        Assert.assertEquals("h", h.get(0).toString());

        List<Character> empty = Linq.<Character>empty().toList();
        Assert.assertEquals(0, empty.size());
    }

    @Test
    public void testToMap() {
        Map<Integer, Employee> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno);
        Assert.assertTrue(map.get(110).name.equals("Bill"));
        Assert.assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, Employee> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno);
        Assert.assertEquals(emps[3], map2.get(10));
        Assert.assertEquals(emps[1], map2.get(30));
        Assert.assertEquals(2, map2.size());
    }

    @Test
    public void testToMapWithSelector() {
        Map<Integer, String> map = Linq.asEnumerable(emps).toMap(emp -> emp.empno, emp -> emp.name);
        Assert.assertTrue(map.get(110).equals("Bill"));
        Assert.assertEquals(4, map.size());

        //key 重复,保留最后一个
        Map<Integer, String> map2 = Linq.asEnumerable(emps).toMap(emp -> emp.deptno, emp -> emp.name);
        Assert.assertEquals(emps[3].name, map2.get(10));
        Assert.assertEquals(emps[1].name, map2.get(30));
        Assert.assertEquals(2, map2.size());
    }

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

    @Test
    public void testDefaultIfEmpty() {
        final List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        final IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty();
        final IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty();
        final IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertNull(emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }

    @Test
    public void testDefaultIfEmptyWithDefaultValue() {
        final List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        final IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty("dummy");
        final IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty("N/A");
        final IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertEquals("N/A", emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }

    @Test
    public void testOfType() {
        final List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        final IEnumerator<Integer> enumerator = Linq.asEnumerable(numbers)
                .ofType(Integer.class)
                .enumerator();
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(Integer.valueOf(2), enumerator.current());
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(Integer.valueOf(5), enumerator.current());
        Assert.assertFalse(enumerator.moveNext());
    }

    @Test
    public void testCast() {
        final List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        final IEnumerator<Integer> enumerator = Linq.asEnumerable(numbers)
                .cast(Integer.class)
                .enumerator();

        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals((Integer) 2, enumerator.current());
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(null, enumerator.current());
        try {
            enumerator.moveNext();
            Assert.fail("cast() fail");
        } catch (ClassCastException ignored) {
        }
    }

    @Test
    public void testFirst() {
        Employee e = emps[0];
        Assert.assertEquals(e, emps[0]);
        Assert.assertEquals(e, Linq.asEnumerable(emps).first());

        Department d = depts[0];
        Assert.assertEquals(d, depts[0]);
        Assert.assertEquals(d, Linq.asEnumerable(depts).first());

        try {
            String s = Linq.<String>empty().first();
            Assert.fail("expected exception, got " + s);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first throws
        try {
            Long num = Linq.asEnumerable(new IterableDemo(0)).first();
            Assert.fail("expected exception, got " + num);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first does not throw
        final Long num = Linq.asEnumerable(new IterableDemo(1)).first();
        Assert.assertEquals((Long) 1L, num);
    }

    @Test
    public void testFirstPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).first(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testFirstOrDefault() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] empty = {};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[0], Linq.asEnumerable(people).firstOrDefault());
        Assert.assertEquals(numbers[0], Linq.asEnumerable(numbers).firstOrDefault());

        Assert.assertNull(Linq.asEnumerable(empty).firstOrDefault());
    }

    @Test
    public void testFirstOrDefaultPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).firstOrDefault(i -> i > 15));
        Assert.assertNull(Linq.asEnumerable(peopleWithoutCharS).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
    }

    @Test
    public void testLast() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("mitch", enumerable.last());

        final IEnumerable emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        try {
            emptyEnumerable.last();
            Assert.fail("should not run at here");
        } catch (InvalidOperationException ignored) {
        }

        final IEnumerable<String> enumerable2 = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList("jimi", "noel", "mitch")));
        Assert.assertEquals("mitch", enumerable2.last());
    }

    @Test
    public void testLastWithPredicate() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch", "ming"));
        Assert.assertEquals("mitch", enumerable.last(x -> x.startsWith("mit")));
        try {
            enumerable.last(x -> false);
            Assert.fail();
        } catch (InvalidOperationException ignored) {
        }

        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        try {
            emptyEnumerable.last(x -> {
                Assert.fail("should not run at here");
                return false;
            });
            Assert.fail();
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testLastOrDefault() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("mitch", enumerable.lastOrDefault());

        final IEnumerable emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        Assert.assertNull(emptyEnumerable.lastOrDefault());
    }

    @Test
    public void testLastOrDefaultWithPredicate() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch", "ming"));
        Assert.assertEquals("mitch", enumerable.lastOrDefault(x -> x.startsWith("mit")));
        Assert.assertNull(enumerable.lastOrDefault(x -> false));
        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        Assert.assertNull(emptyEnumerable.lastOrDefault(x -> {
            Assert.fail("should not run at here");
            return false;
        }));
    }

    @Test
    public void testSingle() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        Assert.assertEquals(person[0], Linq.asEnumerable(person).single());
        Assert.assertEquals(number[0], Linq.asEnumerable(number).single());
        try {
            String s = Linq.asEnumerable(people).single();
            Assert.fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            int i = Linq.asEnumerable(numbers).single();
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSinglePredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithoutGT15 = {5, 10, 15};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).single(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithTwoGT15).single(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithoutGT15).single(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSingleOrDefault() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        Assert.assertEquals(person[0], Linq.asEnumerable(person).singleOrDefault());
        Assert.assertEquals(number[0], Linq.asEnumerable(number).singleOrDefault());
        try {
            String s = Linq.asEnumerable(people).singleOrDefault();
            Assert.fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            Integer i = Linq.asEnumerable(numbers).singleOrDefault();
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSingleOrDefaultPredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};
        Integer[] numbersWithoutGT15 = {5, 10, 15};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).singleOrDefault(n -> n > 15));
        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
        try {

            Integer i = Linq.asEnumerable(numbersWithTwoGT15).singleOrDefault(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
        Assert.assertNull(Linq.asEnumerable(peopleWithoutCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertNull(Linq.asEnumerable(numbersWithoutGT15).singleOrDefault(n -> n > 15));
    }

    @Test
    public void testElementAt() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("jimi", enumerable.elementAt(0));
        try {
            enumerable.elementAt(2);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
            // ok
        }
        try {
            enumerable.elementAt(-1);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
        }

        final IEnumerable<Long> enumerable2 = Linq.asEnumerable(new IterableDemo(2));
        Assert.assertEquals((Long) 1L, enumerable2.elementAt(0));
        try {
            enumerable2.elementAt(2);
            Assert.fail();
        } catch (ArgumentOutOfRangeException ignored) {
        }
        try {
            enumerable2.elementAt(-1);
            Assert.fail();
        } catch (ArgumentOutOfRangeException ignored) {
        }

        IEnumerable<Integer> one = Linq.singletonEnumerable(1);
        Assert.assertEquals((Integer) 1, one.elementAt(0));

        IEnumerable<Integer> empty = Linq.empty();
        try {
            Integer num = empty.elementAt(0);
            Assert.fail("expect error,but got " + num);
        } catch (ArgumentOutOfRangeException ignored) {
        }
    }

    @Test
    public void testElementAtOrDefault() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("jimi", enumerable.elementAtOrDefault(0));
        Assert.assertNull(enumerable.elementAtOrDefault(2));
        Assert.assertNull(enumerable.elementAtOrDefault(-1));

        final IEnumerable<Long> enumerable2 = Linq.asEnumerable(new IterableDemo(2));
        Assert.assertEquals((Long) 1L, enumerable2.elementAtOrDefault(0));
        Assert.assertNull(enumerable2.elementAtOrDefault(2));
        Assert.assertNull(enumerable2.elementAtOrDefault(-1));
    }

    @Test
    public void testAny() {
        Assert.assertFalse(Linq.asEnumerable(Collections.emptyList()).any());
        Assert.assertTrue(Linq.asEnumerable(emps).any());
    }

    @Test
    public void testAnyPredicate() {
        Assert.assertFalse(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("IT")));
        Assert.assertTrue(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("Sales")));
    }

    @Test
    public void testAllPredicate() {
        Assert.assertTrue(Linq.asEnumerable(emps).all(emp -> emp.empno >= 100));
        Assert.assertFalse(Linq.asEnumerable(emps).all(emp -> emp.empno > 100));
    }

    @Test
    public void testCount() {
        final int count = Linq.asEnumerable(depts).count();
        Assert.assertEquals(3, count);
    }

    @Test
    public void testCountPredicate() {
        final int count = Linq.asEnumerable(depts).count(dept -> dept.employees.size() > 0);
        Assert.assertEquals(2, count);
    }

    @Test
    public void testLongCount() {
        final long count = Linq.asEnumerable(depts).longCount();
        Assert.assertEquals(3, count);

        final long count2 = Linq.asEnumerable(new IterableDemo(10)).longCount();
        Assert.assertEquals(10, count2);
    }

    @Test
    public void testLongCountPredicate() {
        final long count = Linq.asEnumerable(depts).longCount(dept -> dept.employees.size() > 0);
        Assert.assertEquals(2, count);

        final long count2 = Linq.asEnumerable(new IterableDemo(10L)).longCount(s -> s > 9);
        Assert.assertEquals(1, count2);
    }

    @Test
    public void testContains() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        Assert.assertEquals(e, employeeClone);
        Assert.assertTrue(Linq.asEnumerable(emps).contains(e));
        Assert.assertTrue(Linq.asEnumerable(emps).contains(employeeClone));
        Assert.assertFalse(Linq.asEnumerable(emps).contains(employeeOther));

        Assert.assertTrue(Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).contains('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(Linq.asEnumerable(arrChar).contains('h'));

        Assert.assertTrue(Linq.asEnumerable("hello").contains('h'));

        Assert.assertTrue(Linq.singletonEnumerable('h').contains('h'));
        Assert.assertFalse(Linq.singletonEnumerable('h').contains('o'));

        Assert.assertFalse(Linq.empty().contains(1));
    }

    @Test
    public void testContainsWithEqualityComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return x != null && y != null
                        && x.empno == y.empno;
            }

            @Override
            public int hashCode(Employee obj) {
                return obj == null ? 0x789d : obj.hashCode();
            }
        };

        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        Assert.assertEquals(e, employeeClone);
        Assert.assertTrue(Linq.asEnumerable(emps).contains(e, comparer));
        Assert.assertTrue(Linq.asEnumerable(emps).contains(employeeClone, comparer));
        Assert.assertFalse(Linq.asEnumerable(emps).contains(employeeOther, comparer));
    }

    @Test
    public void testAggregate() {
        Assert.assertEquals("Sales,HR,Marketing", Linq.asEnumerable(depts)
                .select(dept -> dept.name)
                .aggregate((res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    public void testAggregateWithSeed() {
        Assert.assertEquals("A,Sales,HR,Marketing",
                Linq.asEnumerable(depts)
                        .select(dept -> dept.name)
                        .aggregate("A", (res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    public void testAggregateWithSeedWithResultSelector() {
        String s = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .aggregate(null,
                        (res, name) -> res == null ? name : res + "+" + name,
                        res -> "<no key>: " + res);
        Assert.assertEquals("<no key>: Fred+Bill+Eric+Janet", s);
    }

    @Test
    public void testSumInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumInt());

        final Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        Assert.assertEquals(Integer.MAX_VALUE, Linq.asEnumerable(numbers2).sumInt());

        final Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        try {
            int num = Linq.asEnumerable(numbers3).sumInt();
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumLong());

        final Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        Assert.assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong());

        final Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong();
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloat() {
        final Float[] numbers = {null, 0f, 2f, 3f};
        Assert.assertEquals(5f, Linq.asEnumerable(numbers).sumFloat(), 0f);

        final Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        Assert.assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat(), 0f);
    }

    @Test
    public void testSumDouble() {
        final Double[] numbers = {null, 0d, 2d, 3d};
        Assert.assertEquals(5d, Linq.asEnumerable(numbers).sumDouble(), 0d);

        final Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        Assert.assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble(), 0f);
    }

    @Test
    public void testSumDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal());

        final BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal());
    }

    @Test
    public void testSumIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumInt(n -> n));

        final Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        Assert.assertEquals(Integer.MAX_VALUE, Linq.asEnumerable(numbers2).sumInt(n -> n));

        final Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        try {
            int num = Linq.asEnumerable(numbers3).sumInt(n -> n);
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumLong(n -> n));

        final Long[] numbers2 = {null, Long.MAX_VALUE - 1, 1L};
        Assert.assertEquals(Long.MAX_VALUE, Linq.asEnumerable(numbers2).sumLong(n -> n));

        final Long[] numbers3 = {null, Long.MAX_VALUE, 1L};
        try {
            long num = Linq.asEnumerable(numbers3).sumLong(n -> n);
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

    @Test
    public void testSumFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, 3f};
        Assert.assertEquals(5f, Linq.asEnumerable(numbers).sumFloat(n -> n), 0f);

        final Float[] numbers2 = {null, Float.MAX_VALUE - 1, 1F};
        Assert.assertEquals(Float.MAX_VALUE, Linq.asEnumerable(numbers2).sumFloat(n -> n), 0f);
    }

    @Test
    public void testSumDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, 3d};
        Assert.assertEquals(5d, Linq.asEnumerable(numbers).sumDouble(n -> n), 0d);

        final Double[] numbers2 = {null, Double.MAX_VALUE - 1, 1d};
        Assert.assertEquals(Double.MAX_VALUE, Linq.asEnumerable(numbers2).sumDouble(n -> n), 0f);
    }

    @Test
    public void testSumDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers).sumDecimal(n -> n));

        final BigDecimal[] numbers2 = {null, BigDecimal.ZERO, new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("5"), Linq.asEnumerable(numbers2).sumDecimal(n -> n));
    }

    @Test
    public void testMinInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt());

        final Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNull() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull());
    }

    @Test
    public void testMinLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong());

        final Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNull() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull());

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull());
    }

    @Test
    public void testMinFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull());
    }

    @Test
    public void testMinDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull());
    }

    @Test
    public void testMinDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull());
    }

    @Test
    public void testMin() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).min(), 0d);

        final Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min();
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(0f, Linq.asEnumerable(numbers).minNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minNull());
    }

    @Test
    public void testMinIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(0, Linq.asEnumerable(numbers).minInt(n -> n));

        final Integer[] numbers2 = {null};
        try {
            int min = Linq.asEnumerable(numbers2).minInt(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 0, Linq.asEnumerable(numbers).minIntNull(n -> n));

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minIntNull(n -> n));
    }

    @Test
    public void testMinLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(0L, Linq.asEnumerable(numbers).minLong(n -> n));

        final Long[] numbers2 = {null};
        try {
            long min = Linq.asEnumerable(numbers2).minLong(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 0L, Linq.asEnumerable(numbers).minLongNull(n -> n));

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minLongNull(n -> n));
    }

    @Test
    public void testMinFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float min = Linq.asEnumerable(numbers2).minFloat(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).minFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minFloatNull(n -> n));
    }

    @Test
    public void testMinDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double min = Linq.asEnumerable(numbers2).minDouble(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).minDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDoubleNull(n -> n));
    }

    @Test
    public void testMinDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal min = Linq.asEnumerable(numbers2).minDecimal(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("0"), Linq.asEnumerable(numbers).minDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).minDecimalNull(n -> n));
    }

    @Test
    public void testMinWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).min(n -> n);
        Assert.assertEquals(0f, f, 0d);

        final Float[] numbers2 = {null};
        try {
            Float min = Linq.asEnumerable(numbers2).min(n -> n);
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).minNull(n -> n);
        Assert.assertEquals(0f, f, 0d);

        final Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).minNull(n -> n);
        Assert.assertEquals(null, f2);
    }

    @Test
    public void testMaxInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(3, Linq.asEnumerable(numbers).maxInt());

        final Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNull() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 3, Linq.asEnumerable(numbers).maxIntNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull());
    }

    @Test
    public void testMaxLong() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(3L, Linq.asEnumerable(numbers).maxLong());

        final Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNull() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 3L, Linq.asEnumerable(numbers).maxLongNull());

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull());
    }

    @Test
    public void testMaxFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull());
    }

    @Test
    public void testMaxDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull());
    }

    @Test
    public void testMaxDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull());
    }

    @Test
    public void testMax() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).max(), 0d);

        final Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max();
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).maxNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxNull());
    }

    @Test
    public void testMaxIntWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(3, Linq.asEnumerable(numbers).maxInt(n -> n));

        final Integer[] numbers2 = {null};
        try {
            int max = Linq.asEnumerable(numbers2).maxInt(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals((Integer) 3, Linq.asEnumerable(numbers).maxIntNull(n -> n));

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxIntNull(n -> n));
    }

    @Test
    public void testMaxLongWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals(3L, Linq.asEnumerable(numbers).maxLong(n -> n));

        final Long[] numbers2 = {null};
        try {
            long max = Linq.asEnumerable(numbers2).maxLong(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 2L, 3L};
        Assert.assertEquals((Long) 3L, Linq.asEnumerable(numbers).maxLongNull(n -> n));

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxLongNull(n -> n));
    }

    @Test
    public void testMaxFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float max = Linq.asEnumerable(numbers2).maxFloat(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(2f, Linq.asEnumerable(numbers).maxFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxFloatNull(n -> n));
    }

    @Test
    public void testMaxDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double max = Linq.asEnumerable(numbers2).maxDouble(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).maxDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDoubleNull(n -> n));
    }

    @Test
    public void testMaxDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal max = Linq.asEnumerable(numbers2).maxDecimal(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("2"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("3"), Linq.asEnumerable(numbers).maxDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).maxDecimalNull(n -> n));
    }

    @Test
    public void testMaxWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).max(n -> n);
        Assert.assertEquals(Float.NaN, f, 0d);

        final Float[] numbers2 = {null};
        try {
            Float max = Linq.asEnumerable(numbers2).max(n -> n);
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Float f = Linq.asEnumerable(numbers).maxNull(n -> n);
        Assert.assertEquals(Float.NaN, f, 0d);

        final Float[] numbers2 = {null};
        Float f2 = Linq.asEnumerable(numbers2).maxNull(n -> n);
        Assert.assertEquals(null, f2);
    }

    @Test
    public void testAverageInt() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNull() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull());
    }

    @Test
    public void testAverageLong() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNull() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull());
    }

    @Test
    public void testAverageFloat() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNull() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull());
    }

    @Test
    public void testAverageDouble() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNull() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull());
    }

    @Test
    public void testAverageDecimal() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal());

        final Integer[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal();
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNull() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull());

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull());
    }

    @Test
    public void testAverageIntWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageInt(n -> n), 0d);

        final Integer[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageInt(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageIntNullWithSelector() {
        final Integer[] numbers = {null, 0, 3, 3};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageIntNull(n -> n), 0d);

        final Integer[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageIntNull(n -> n));
    }

    @Test
    public void testAverageLongWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLong(n -> n), 0d);

        final Long[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageLong(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageLongNullWithSelector() {
        final Long[] numbers = {null, 0L, 3L, 3L};
        Assert.assertEquals(2d, Linq.asEnumerable(numbers).averageLongNull(n -> n), 0d);

        final Long[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageLongNull(n -> n));
    }

    @Test
    public void testAverageFloatWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloat(n -> n), 0d);

        final Float[] numbers2 = {null};
        try {
            float average = Linq.asEnumerable(numbers2).averageFloat(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageFloatNullWithSelector() {
        final Float[] numbers = {null, 0f, 2f, Float.NaN};
        Assert.assertEquals(Float.NaN, Linq.asEnumerable(numbers).averageFloatNull(n -> n), 0d);

        final Float[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageFloatNull(n -> n));
    }

    @Test
    public void testAverageDoubleWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDouble(n -> n), 0d);

        final Double[] numbers2 = {null};
        try {
            double average = Linq.asEnumerable(numbers2).averageDouble(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDoubleNullWithSelector() {
        final Double[] numbers = {null, 0d, 2d, Double.NaN};
        Assert.assertEquals(Double.NaN, Linq.asEnumerable(numbers).averageDoubleNull(n -> n), 0d);

        final Double[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDoubleNull(n -> n));
    }

    @Test
    public void testAverageDecimalWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimal(n -> n));

        final BigDecimal[] numbers2 = {null};
        try {
            BigDecimal average = Linq.asEnumerable(numbers2).averageDecimal(n -> n);
            Assert.fail("expect error,but got " + average);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testAverageDecimalNullWithSelector() {
        final BigDecimal[] numbers = {null, new BigDecimal("0"), new BigDecimal("3"), new BigDecimal("3")};
        Assert.assertEquals(new BigDecimal("2"), Linq.asEnumerable(numbers).averageDecimalNull(n -> n));

        final BigDecimal[] numbers2 = {null};
        Assert.assertEquals(null, Linq.asEnumerable(numbers2).averageDecimalNull(n -> n));
    }

    @Test
    public void testLeftJoin() {
        //包含左侧数据,另外 null key 不能用来关联
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .leftJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", ss);
    }

    @Test
    public void testLeftJoinWithDefaultValue() {
        //包含左侧数据,另外 null key 不能用来关联,使用指定默认值
        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .leftJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", s);

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", ss);
    }

    @Test
    public void testLeftJoinWithComparer() {
        //包含左侧数据,另外 null key 不能用来关联,使用指定关联规则
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

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .leftJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", ss);
    }

    @Test
    public void testLeftJoinWithDefaultValueAndComparer() {
        //包含左侧数据,另外 null key 不能用来关联,使用指定默认值和关联规则
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

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .leftJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept]", s);

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager]", ss);
    }

    @Test
    public void testRightJoin() {
        //包含右侧数据,另外 null key 不能用来关联
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .rightJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", ss);
    }

    @Test
    public void testRightJoinWithDefaultValue() {
        //包含右侧数据,另外 null key 不能用来关联,使用指定默认值
        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .rightJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", s);

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", ss);
    }

    @Test
    public void testRightJoinWithComparer() {
        //包含右侧数据,另外 null key 不能用来关联,使用指定关联规则
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

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .rightJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", ss);
    }

    @Test
    public void testRightJoinWithDefaultValueAndComparer() {
        //包含右侧数据,另外 null key 不能用来关联,使用指定默认值和关联规则
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

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .rightJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager]", s);

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept]", ss);
    }

    @Test
    public void testFullJoin() {
        //包含两侧数据,另外 null key 不能用来关联
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .fullJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null, null works in HR, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager, Cedric works in null, Gates works in null]", ss);
    }

    @Test
    public void testFullJoinWithDefaultValue() {
        //包含两侧数据,另外 null key 不能用来关联,使用指定默认值
        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .fullJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept, defaultEmp works in HR, defaultEmp works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager, Cedric works in defaultDept, Gates works in defaultDept]", ss);
    }

    @Test
    public void testFullJoinWithComparer() {
        //包含两侧数据,另外 null key 不能用来关联,使用指定关联规则
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

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .fullJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager, Gates works in null]", ss);
    }

    @Test
    public void testFullJoinWithDefaultValueAndComparer() {
        //包含两侧数据,另外 null key 不能用来关联,使用指定默认值和关联规则
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

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .fullJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept, defaultEmp works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager, Gates works in defaultDept]", ss);
    }

    @Test
    public void testCrossJoin() {
        //交叉关联,不理会 key 是否为 null
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .crossJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .crossJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        Assert.assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Gates works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Gates works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, Gates works in Marketing, Fred works in Manager, Bill works in Manager, Eric works in Manager, Janet works in Manager, Cedric works in Manager, Gates works in Manager]", ss);
    }

    @Test
    public void testDistinctBy() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[0],
                emps[3],
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps2).distinctBy(emp -> emp.deptno).count());
    }

    @Test
    public void testDistinctByWithEqualityComparer() {
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

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[1],
                emps[3]
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps2).distinctBy(emp -> emp.empno, comparer).count());
    }

    @Test
    public void testUnionBy() {
        Assert.assertEquals(4, Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno)
                .count());
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

        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno, comparer)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno, comparer)
                .count());
    }

    @Test
    public void testIntersectBy() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 30),
                emps[3],
        };
        Assert.assertEquals(2, Linq.asEnumerable(emps)
                .intersectBy(Linq.asEnumerable(emps2), emp -> emp.deptno)
                .count());
    }

    @Test
    public void testIntersectByWithComparer() {
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

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10)
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .intersectBy(Linq.asEnumerable(emps2), emp -> emp.deptno, comparer)
                .count());
    }

    @Test
    public void testExceptBy() {
        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .exceptBy(Linq.asEnumerable(emps2), emp -> emp.deptno)
                .count());

        final IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        final IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        final IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        Assert.assertTrue(oneToHundred.exceptBy(oneToFifty, a -> a).sequenceEqual(fiftyOneToHundred));
    }

    @Test
    public void testExceptByWithComparer() {
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

        final Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
        };
        Assert.assertEquals(0, Linq.asEnumerable(emps)
                .exceptBy(Linq.asEnumerable(emps2), emp -> emp.deptno, comparer)
                .count());
    }

    @Test
    public void testMinByInt() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByInt(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByInt(tuple -> (Integer) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByIntNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(0), Linq.asEnumerable(tuple1s).minByIntNull(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMinByLong() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLong(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByLong(tuple -> (Long) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByLongNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(0L), Linq.asEnumerable(tuple1s).minByLongNull(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMinByFloat() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloat(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByFloat(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByFloatNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).minByFloatNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMinByDouble() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDouble(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDouble(tuple -> (Double) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByDoubleNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(Double.NaN), Linq.asEnumerable(tuple1s).minByDoubleNull(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMinByDecimal() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minByDecimal(tuple -> (BigDecimal) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByDecimalNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("0")), Linq.asEnumerable(tuple1s).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMinBy() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minBy(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 min = Linq.asEnumerable(tuple1s2).minBy(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + min);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMinByNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(0f), Linq.asEnumerable(tuple1s).minByNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).minByNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByInt() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByInt(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByInt(tuple -> (Integer) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByIntNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0), Tuple.create(2), Tuple.create(3)};
        Assert.assertEquals(Tuple.create(3), Linq.asEnumerable(tuple1s).maxByIntNull(tuple -> (Integer) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByIntNull(tuple -> (Integer) tuple.getItem1()));
    }

    @Test
    public void testMaxByLong() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLong(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByLong(tuple -> (Long) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByLongNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0L), Tuple.create(2L), Tuple.create(3L)};
        Assert.assertEquals(Tuple.create(3L), Linq.asEnumerable(tuple1s).maxByLongNull(tuple -> (Long) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByLongNull(tuple -> (Long) tuple.getItem1()));
    }

    @Test
    public void testMaxByFloat() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloat(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByFloat(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByFloatNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(2f), Linq.asEnumerable(tuple1s).maxByFloatNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByFloatNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testMaxByDouble() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDouble(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByDouble(tuple -> (Double) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByDoubleNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0d), Tuple.create(2d), Tuple.create(Double.NaN)};
        Assert.assertEquals(Tuple.create(2d), Linq.asEnumerable(tuple1s).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDoubleNull(tuple -> (Double) tuple.getItem1()));
    }

    @Test
    public void testMaxByDecimal() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("3")), Linq.asEnumerable(tuple1s).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxByDecimal(tuple -> (BigDecimal) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByDecimalNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(new BigDecimal("0")), Tuple.create(new BigDecimal("2")), Tuple.create(new BigDecimal("3"))};
        Assert.assertEquals(Tuple.create(new BigDecimal("3")), Linq.asEnumerable(tuple1s).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByDecimalNull(tuple -> (BigDecimal) tuple.getItem1()));
    }

    @Test
    public void testMaxBy() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxBy(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        try {
            Tuple1 max = Linq.asEnumerable(tuple1s2).maxBy(tuple -> (Float) tuple.getItem1());
            Assert.fail("expect error,but got " + max);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testMaxByNull() {
        final Tuple1[] tuple1s = {Tuple.create(null), Tuple.create(0f), Tuple.create(2f), Tuple.create(Float.NaN)};
        Assert.assertEquals(Tuple.create(Float.NaN), Linq.asEnumerable(tuple1s).maxByNull(tuple -> (Float) tuple.getItem1()));

        final Tuple1[] tuple1s2 = {Tuple.create(null)};
        Assert.assertEquals(null, Linq.asEnumerable(tuple1s2).maxByNull(tuple -> (Float) tuple.getItem1()));
    }

    @Test
    public void testAppend() {
        final String s = Linq.asEnumerable(emps).append(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Fred, Bill, Eric, Janet, Cedric]", s);
    }
}
