package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class IntersectByTest extends TestCase {
    @Test
    public void testIntersectBy() {
        Employee[] emps2 = {
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

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10)
        };
        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .intersectBy(Linq.asEnumerable(emps2), emp -> emp.deptno, comparer)
                .count());
    }
}
