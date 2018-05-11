package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ExceptTest extends EnumerableTest {

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

}