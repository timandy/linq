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
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
class JoinTest extends TestCase {
    private static JoinRec createJoinRec(CustomerRec cr, OrderRec or) {
        return new JoinRec(cr.name, or.orderID, or.total);
    }

    private static JoinRec createJoinRec(CustomerRec cr, AnagramRec or) {
        return new JoinRec(cr.name, or.orderID, or.total);
    }

    @Test
    void OuterEmptyInnerNonEmpty() {
        CustomerRec[] outer = {};
        OrderRec[] inner = {
                new OrderRec(45321, 98022, 50),
                new OrderRec(97865, 32103, 25)};

        assertEmpty(Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void FirstOuterMatchesLastInnerLastOuterMatchesFirstInnerSameNumberElements() {
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

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void NullComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};
        JoinRec[] expected = {new JoinRec("Prakash", 323232, 9)};

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, null));
    }

    @Test
    void CustomComparer() {
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

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    void OuterNull() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(NullPointerException.class, () -> outer.join(Linq.of(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    void InnerNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(inner, e -> e.name, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    void OuterKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), null, e -> e.name, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    void InnerKeySelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), e -> e.name, null, JoinTest::createJoinRec, new AnagramEqualityComparer()));
    }

    @Test
    void ResultSelectorNull() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, AnagramRec, JoinRec>) null, new AnagramEqualityComparer()));
    }

    @Test
    void OuterNullNoComparer() {
        IEnumerable<CustomerRec> outer = null;
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(NullPointerException.class, () -> outer.join(Linq.of(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    void InnerNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        IEnumerable<AnagramRec> inner = null;

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), e -> e.name, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    void OuterKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), null, e -> e.name, JoinTest::createJoinRec));
    }

    @Test
    void InnerKeySelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), e -> e.name, null, JoinTest::createJoinRec));
    }

    @Test
    void ResultSelectorNullNoComparer() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        AnagramRec[] inner = {
                new AnagramRec("miT", 43455, 10),
                new AnagramRec("Prakash", 323232, 9)};

        assertThrows(ArgumentNullException.class, () -> Linq.of(outer).join(Linq.of(inner), e -> e.name, e -> e.name, (Func2<CustomerRec, AnagramRec, JoinRec>) null));
    }

    @Test
    void SkipsNullElements() {
        String[] outer = {null, Empty};
        String[] inner = {null, Empty};
        String[] expected = {Empty};

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e, e -> e, (x, y) -> y, EqualityComparer.Default()));
    }

    @Test
    void OuterNonEmptyInnerEmpty() {
        CustomerRec[] outer = {
                new CustomerRec("Tim", 43434),
                new CustomerRec("Bob", 34093)};
        OrderRec[] inner = {};
        assertEmpty(Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void SingleElementEachAndMatches() {
        CustomerRec[] outer = {new CustomerRec("Prakash", 98022)};
        OrderRec[] inner = {new OrderRec(45321, 98022, 50)};
        JoinRec[] expected = {new JoinRec("Prakash", 45321, 50)};

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void SingleElementEachAndDoesntMatch() {
        CustomerRec[] outer = {new CustomerRec("Prakash", 98922)};
        OrderRec[] inner = {new OrderRec(45321, 98022, 50)};
        assertEmpty(Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void SelectorsReturnNull() {
        Integer[] inner = {null, null, null};
        Integer[] outer = {null, null};
        assertEmpty(Linq.of(outer).join(Linq.of(inner), e -> e, e -> e, (x, y) -> x));
    }

    @Test
    void InnerSameKeyMoreThanOneElementAndMatches() {
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

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void OuterSameKeyMoreThanOneElementAndMatches() {
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

        assertEquals(Linq.of(expected), Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void NoMatches() {
        CustomerRec[] outer = {
                new CustomerRec("Prakash", 98022),
                new CustomerRec("Bob", 99022),
                new CustomerRec("Tim", 99021),
                new CustomerRec("Robert", 99022)};
        OrderRec[] inner = {
                new OrderRec(45321, 18022, 50),
                new OrderRec(43421, 29022, 20),
                new OrderRec(95421, 39021, 9)};
        assertEmpty(Linq.of(outer).join(Linq.of(inner), e -> e.custID, e -> e.custID, JoinTest::createJoinRec));
    }

    @Test
    void ForcedToEnumeratorDoesntEnumerate() {
        IEnumerable<Integer> iterator = NumberRangeGuaranteedNotCollectionType(0, 3).join(Linq.<Integer>empty(), i -> i, i -> i, (o, i) -> i);
        // Don't insist on this behaviour, but check it's correct if it happens
        IEnumerator<Integer> en = (IEnumerator<Integer>) iterator;
        assertFalse(en != null && en.moveNext());
    }

    @Test
    void testJoin() {
        //null key 被排除
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .join(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .join(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, Bill works in Marketing]", ss);

        IEnumerable<Tuple2<Integer, Integer>> source = Linq.of(1, 2, null).join(Linq.of(1, 3, null), x -> x, y -> y, Tuple::create);
        assertEquals(1, source.count());
        assertEquals(1, source.count());
    }

    @Test
    void testJoinWithComparer() {
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

        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .join(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .join(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing]", ss);
    }

    @Test
    void testLeftJoin() {
        //包含左侧数据,另外 null key 不能用来关联
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .leftJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .leftJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", ss);

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).leftJoin(Linq.of(1, 2), x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).leftJoin(null, x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).leftJoin(Linq.of(1, 2), null, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).leftJoin(Linq.of(1, 2), x -> x, null, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).leftJoin(Linq.of(1, 2), x -> x, y -> y, null));

        IEnumerable<Tuple2<Integer, Integer>> source = Linq.of(1, 2, null).leftJoin(Linq.of(1, 3, null), x -> x, y -> y, Tuple::create);
        assertEquals(3, source.count());
        assertEquals(3, source.count());
        try (IEnumerator<Tuple2<Integer, Integer>> e = source.enumerator()) {
            for (int i = 0; i < 3; i++) {
                assertTrue(e.moveNext());
            }
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        IEnumerable<Tuple2<Integer, Integer>> source2 = Linq.<Integer>empty().leftJoin(Linq.of(1, 2), x -> x, y -> y, Tuple::create);
        try (IEnumerator<Tuple2<Integer, Integer>> e = source2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    void testLeftJoinWithDefaultValue() {
        //包含左侧数据,另外 null key 不能用来关联,使用指定默认值
        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .leftJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", s);

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .leftJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultEmp,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", ss);
    }

    @Test
    void testLeftJoinWithComparer() {
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

        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .leftJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .leftJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", ss);
    }

    @Test
    void testLeftJoinWithDefaultValueAndComparer() {
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
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .leftJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept]", s);

        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .leftJoin(Linq.of(emps).concat(Linq.of(badEmps)),
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
    void testRightJoin() {
        //包含右侧数据,另外 null key 不能用来关联
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .rightJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .rightJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null]", ss);

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).rightJoin(Linq.of(1, 2), x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).rightJoin(null, x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).rightJoin(Linq.of(1, 2), null, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).rightJoin(Linq.of(1, 2), x -> x, null, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).rightJoin(Linq.of(1, 2), x -> x, y -> y, null));

        IEnumerable<Tuple2<Integer, Integer>> source = Linq.of(1, 2, null).rightJoin(Linq.of(1, 3, null), x -> x, y -> y, Tuple::create);
        assertEquals(3, source.count());
        assertEquals(3, source.count());
        try (IEnumerator<Tuple2<Integer, Integer>> e = source.enumerator()) {
            for (int i = 0; i < 3; i++) {
                assertTrue(e.moveNext());
            }
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        IEnumerable<Tuple2<Integer, Integer>> source2 = Linq.of(1, 2).rightJoin(Linq.<Integer>empty(), x -> x, y -> y, Tuple::create);
        try (IEnumerator<Tuple2<Integer, Integer>> e = source2.enumerator()) {
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    void testRightJoinWithDefaultValue() {
        //包含右侧数据,另外 null key 不能用来关联,使用指定默认值
        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .rightJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, defaultEmp works in HR, Bill works in Marketing, defaultEmp works in Manager]", s);

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .rightJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        defaultDept,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept]", ss);
    }

    @Test
    void testRightJoinWithComparer() {
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

        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .rightJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .rightJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null]", ss);
    }

    @Test
    void testRightJoinWithDefaultValueAndComparer() {
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
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .rightJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, defaultEmp works in Manager]", s);

        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .rightJoin(Linq.of(emps).concat(Linq.of(badEmps)),
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
    void testFullJoin() {
        //包含两侧数据,另外 null key 不能用来关联
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .fullJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in null, Gates works in null, null works in HR, null works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .fullJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Eric works in Sales, Janet works in Sales, null works in HR, Bill works in Marketing, null works in Manager, Cedric works in null, Gates works in null]", ss);

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).fullJoin(Linq.of(1, 2), x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).fullJoin(null, x -> x, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).fullJoin(Linq.of(1, 2), null, y -> y, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).fullJoin(Linq.of(1, 2), x -> x, null, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).fullJoin(Linq.of(1, 2), x -> x, y -> y, null));

        IEnumerable<Tuple2<Integer, Integer>> source = Linq.of(1, 2, null).fullJoin(Linq.of(1, 3, null), x -> x, y -> y, Tuple::create);
        assertEquals(5, source.count());
        assertEquals(5, source.count());

        try (IEnumerator<Tuple2<Integer, Integer>> e = source.enumerator()) {
            for (int i = 0; i < 5; i++) {
                assertTrue(e.moveNext());
            }
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }
    }

    @Test
    void testFullJoinWithDefaultValue() {
        //包含两侧数据,另外 null key 不能用来关联,使用指定默认值
        Employee defaultEmp = new Employee(0, "defaultEmp", null);
        Department defaultDept = new Department("defaultDept", null, Collections.emptyList());
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .fullJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name))
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Marketing, Eric works in Sales, Janet works in Sales, Cedric works in defaultDept, Gates works in defaultDept, defaultEmp works in HR, defaultEmp works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .fullJoin(Linq.of(emps).concat(Linq.of(badEmps)),
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
    void testFullJoinWithComparer() {
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

        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .fullJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        (emp, dept) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in null, null works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .fullJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        dept -> dept.deptno,
                        emp -> emp.deptno,
                        (dept, emp) -> String.format("%s works in %s", emp == null ? null : emp.name, dept == null ? null : dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, null works in Manager, Gates works in null]", ss);
    }

    @Test
    void testFullJoinWithDefaultValueAndComparer() {
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
        String s = Linq.of(emps).concat(Linq.of(badEmps))
                .fullJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        emp -> emp.deptno,
                        dept -> dept.deptno,
                        defaultEmp,
                        defaultDept,
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name),
                        comparer)
                .toList()
                .toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Bill works in Sales, Bill works in HR, Bill works in Marketing, Eric works in Sales, Eric works in HR, Eric works in Marketing, Janet works in Sales, Janet works in HR, Janet works in Marketing, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Gates works in defaultDept, defaultEmp works in Manager]", s);

        String ss = Linq.of(depts).concat(Linq.of(badDepts))
                .fullJoin(Linq.of(emps).concat(Linq.of(badEmps)),
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
    void testCrossJoin() {
        //交叉关联,不理会 key 是否为 null
        IEnumerable<String> enumerable = Linq.of(emps).concat(Linq.of(badEmps))
                .crossJoin(Linq.of(depts).concat(Linq.of(badDepts)),
                        (emp, dept) -> String.format("%s works in %s", emp.name, dept.name));
        CrossJoinIterator<Employee, Department, String> crossJoinIterator = (CrossJoinIterator<Employee, Department, String>) enumerable;
        assertEquals(24, crossJoinIterator._toArray(String.class).length);
        assertEquals(24, crossJoinIterator._toArray().length);
        assertEquals(24, crossJoinIterator._toList().size());
        assertEquals(-1, crossJoinIterator._getCount(true));
        assertEquals(24, crossJoinIterator._getCount(false));
        String s = enumerable.toList().toString();
        assertEquals("[Fred works in Sales, Fred works in HR, Fred works in Marketing, Fred works in Manager, Bill works in Sales, Bill works in HR, Bill works in Marketing, Bill works in Manager, Eric works in Sales, Eric works in HR, Eric works in Marketing, Eric works in Manager, Janet works in Sales, Janet works in HR, Janet works in Marketing, Janet works in Manager, Cedric works in Sales, Cedric works in HR, Cedric works in Marketing, Cedric works in Manager, Gates works in Sales, Gates works in HR, Gates works in Marketing, Gates works in Manager]", s);

        IEnumerable<String> enumerable2 = Linq.of(depts).concat(Linq.of(badDepts))
                .crossJoin(Linq.of(emps).concat(Linq.of(badEmps)),
                        (dept, emp) -> String.format("%s works in %s", emp.name, dept.name));
        CrossJoinIterator<Department, Employee, String> crossJoinIterator2 = (CrossJoinIterator<Department, Employee, String>) enumerable2;
        assertEquals(24, crossJoinIterator2._toArray(String.class).length);
        assertEquals(24, crossJoinIterator2._toArray().length);
        assertEquals(24, crossJoinIterator2._toList().size());
        assertEquals(-1, crossJoinIterator2._getCount(true));
        assertEquals(24, crossJoinIterator2._getCount(false));
        String ss = enumerable2.toList().toString();
        assertEquals("[Fred works in Sales, Bill works in Sales, Eric works in Sales, Janet works in Sales, Cedric works in Sales, Gates works in Sales, Fred works in HR, Bill works in HR, Eric works in HR, Janet works in HR, Cedric works in HR, Gates works in HR, Fred works in Marketing, Bill works in Marketing, Eric works in Marketing, Janet works in Marketing, Cedric works in Marketing, Gates works in Marketing, Fred works in Manager, Bill works in Manager, Eric works in Manager, Janet works in Manager, Cedric works in Manager, Gates works in Manager]", ss);

        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).crossJoin(Linq.of(1, 2), Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).crossJoin(null, Tuple::create));
        assertThrows(ArgumentNullException.class, () -> Linq.of(1, 2).crossJoin(Linq.of(1, 2), null));

        IEnumerable<Integer> source = Linq.of(1, 2).crossJoin(Linq.of(0, 2, 3), (l, r) -> l * r);
        assertEquals(6, source.count());
        assertEquals(15, source.sumInt());
        assertEquals(15, source.sumInt());

        IEnumerable<Integer> source2 = Linq.of(1, 2).crossJoin(Linq.of(3), (x, y) -> x * y);
        try (IEnumerator<Integer> e = source2.enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(3, e.current());
            assertTrue(e.moveNext());
            assertEquals(6, e.current());
            assertFalse(e.moveNext());
            assertFalse(e.moveNext());
        }

        IEnumerable<Integer> source3 = Linq.of(1).crossJoin(Linq.of(2, 3), (x, y) -> x * y);
        try (IEnumerator<Integer> e = source3.enumerator()) {
            assertTrue(e.moveNext());
            assertEquals(2, e.current());
        }
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
        private final String name;
        private final int orderID;
        private final int total;

        private JoinRec(String name, int orderID, int total) {
            this.name = name;
            this.orderID = orderID;
            this.total = total;
        }
    }
}
