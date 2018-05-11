package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class UnionByTest extends EnumerableTest {

    @Test
    public void testUnionBy() {
        Assert.assertEquals(4, Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno)
                .count());
    }

    @Test
    public void testUnionByWithComparer() {
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

        Assert.assertEquals(1, Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno, comparer)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno, comparer)
                .count());
    }

}