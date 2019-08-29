package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.jupiter.api.Test;

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
        assertEquals(2, Linq.of(emps)
                .intersectBy(Linq.of(emps2), emp -> emp.deptno)
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
        assertEquals(1, Linq.of(emps)
                .intersectBy(Linq.of(emps2), emp -> emp.deptno, comparer)
                .count());
    }
}
