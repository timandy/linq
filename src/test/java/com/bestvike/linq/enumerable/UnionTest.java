package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class UnionTest extends EnumerableTest {

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

}