package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class JoinTest extends EnumerableTest {
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
}
