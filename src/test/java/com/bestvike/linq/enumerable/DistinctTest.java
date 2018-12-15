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
public class DistinctTest extends EnumerableTest {
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
}
