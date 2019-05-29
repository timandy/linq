package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class JoinTest extends TestCase {
    private static JoinRec createJoinRec(CustomerRec cr, OrderRec or) {
        return new JoinRec(cr.name, or.orderID, or.total);
    }

    private static JoinRec createJoinRec(CustomerRec cr, AnagramRec or) {
        return new JoinRec(cr.name, or.orderID, or.total);
    }

    @Test
    public void OuterEmptyInnerNonEmpty() {
        CustomerRec[] outer = {};
        OrderRec[] inner = {
                new OrderRec(45321, 98022, 50),
                new OrderRec(97865, 32103, 25)};

        assertEmpty(Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void FirstOuterMatchesLastInnerLastOuterMatchesFirstInnerSameNumberElements() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        OrderRec[] inner = {
                new OrderRec(45321, 99022, 50),
                new OrderRec(43421, 29022, 20),
                new OrderRec(95421, 98022, 9)};
        JoinRec[] expected = {
                new JoinRec("Prakash", 95421, 9),
                new JoinRec("Robert", 45321, 50)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void NullComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};
        JoinRec[] expected = {new JoinRec("Prakash", 323232, 9)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, null));
    }

    @Test
    public void CustomComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};
        JoinRec[] expected = {
                new JoinRec("Prakash", 323232, 9),
                new JoinRec("Tim", 43455, 10)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterNull() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(NullPointerException.class, () -> outer.join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    public void InnerNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(inner, e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), null, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    public void InnerKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, null, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    public void ResultSelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, AnagramRec, JoinRec>) null, new AnagramEqualityComparer()));
    }

    @Test
    public void OuterNullNoComparer() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(NullPointerException.class, () -> outer.join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    public void InnerNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    public void OuterKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), null, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    public void InnerKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, null, JoinTest::createJoinRec));
    }

    @Test
    public void ResultSelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, AnagramRec, JoinRec>) null));
    }

    @Test
    public void SkipsNullElements() {
        String[] outer = {null, Empty};
        String[] inner = {null, Empty};
        String[] expected = {Empty};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e, e -> e, (x, y) -> y, EqualityComparer.Default()));
    }

    @Test
    public void OuterNonEmptyInnerEmpty() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 43434),
                new CustomerRec("Bob", 34093)};
        OrderRec[] inner = {};
        assertEmpty(Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void SingleElementEachAndMatches() {
        CustomerRec[] outer = {new CustomerRec("Prakash", 98022)};
        OrderRec[] inner = {new OrderRec(45321, 98022, 50)};
        JoinRec[] expected = {new JoinRec("Prakash", 45321, 50)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void SingleElementEachAndDoesntMatch() {
        CustomerRec[] outer = {new CustomerRec("Prakash", 98922)};
        OrderRec[] inner = {new OrderRec(45321, 98022, 50)};
        assertEmpty(Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void SelectorsReturnNull() {
        Integer[] inner = {null, null, null};
        Integer[] outer = {null, null};
        assertEmpty(Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e, e -> e, (x, y) -> x));
    }

    @Test
    public void InnerSameKeyMoreThanOneElementAndMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        OrderRec[] inner = {
                new OrderRec(45321, 98022, 50),
                new OrderRec(45421, 98022, 10),
                new OrderRec(43421, 99022, 20),
                new OrderRec(85421, 98022, 18),
                new OrderRec(95421, 99021, 9)};
        JoinRec[] expected = {
                new JoinRec("Prakash", 45321, 50),
                new JoinRec("Prakash", 45421, 10),
                new JoinRec("Prakash", 85421, 18),
                new JoinRec("Tim", 95421, 9),
                new JoinRec("Robert", 43421, 20)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void OuterSameKeyMoreThanOneElementAndMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Bob", 99022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        OrderRec[] inner = {
                new OrderRec(45321, 98022, 50),
                new OrderRec(43421, 99022, 20),
                new OrderRec(95421, 99021, 9)};
        JoinRec[] expected = {
                new JoinRec("Prakash", 45321, 50),
                new JoinRec("Bob", 43421, 20),
                new JoinRec("Tim", 95421, 9),
                new JoinRec("Robert", 43421, 20)};

        assertEquals(Linq.asEnumerable(expected), Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void NoMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Bob", 99022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        OrderRec[] inner = {
                new OrderRec(45321, 18022, 50),
                new OrderRec(43421, 29022, 20),
                new OrderRec(95421, 39021, 9)};
        assertEmpty(Linq.asEnumerable(outer).join(Linq.asEnumerable(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    public void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).join(Linq.<Integer>empty(), i -> i, i -> i, (o, i) -> i);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
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
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .join(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, Bill works in Marketing]", ss);
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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .join(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", s);

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", ss);
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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .leftJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", ss);
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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept]", s);

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
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager]", ss);
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
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", ss);
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
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", s);

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .rightJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager]", s);

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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null, null works in HR, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager, Cedric works in null, Gates works in null]", ss);
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
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept, defaultEmp works in HR, defaultEmp works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager, Cedric works in defaultDept, Gates works in defaultDept]", ss);
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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null, null works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .fullJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager, Gates works in null]", ss);
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
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept, defaultEmp works in Manager]", s);

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
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager, Gates works in defaultDept]", ss);
    }

    @Test
    public void testCrossJoin() {
        //交叉关联,不理会 key 是否为 null
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .crossJoin(Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts)),
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", s);

        String ss = Linq.asEnumerable(depts).concat(Linq.asEnumerable(badDepts))
                .crossJoin(Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)),
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Gates works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Gates works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, Gates works in Marketing, Fred works in Manager, Bill works in Manager, Eric works in Manager, Janet works in Manager, Cedric works in Manager, Gates works in Manager]", ss);
    }

    //struct
    private static final class CustomerRec extends ValueType {
        final String name;
        final int custID;

        CustomerRec(String name, int custID) {
            this.name = name;
            this.custID = custID;
        }
    }

    //struct
    private static final class OrderRec extends ValueType {
        final int orderID;
        final int custID;
        final int total;

        OrderRec(int orderID, int custID, int total) {
            this.orderID = orderID;
            this.custID = custID;
            this.total = total;
        }
    }

    //struct
    private static final class AnagramRec extends ValueType {
        final String name;
        final int orderID;
        final int total;

        AnagramRec(String name, int orderID, int total) {
            this.name = name;
            this.orderID = orderID;
            this.total = total;
        }
    }

    //struct
    private static final class JoinRec extends ValueType {
        final String name;
        final int orderID;
        final int total;

        JoinRec(String name, int orderID, int total) {
            this.name = name;
            this.orderID = orderID;
            this.total = total;
        }
    }
}
