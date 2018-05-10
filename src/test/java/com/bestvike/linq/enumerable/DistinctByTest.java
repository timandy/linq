package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class DistinctByTest extends IteratorTest {

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

}