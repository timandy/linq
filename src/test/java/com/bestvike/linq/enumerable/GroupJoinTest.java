package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class GroupJoinTest extends IteratorTest {


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

}