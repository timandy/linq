package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ExceptByTest extends EnumerableTest {

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

}