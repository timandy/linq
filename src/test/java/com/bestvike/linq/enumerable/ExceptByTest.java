package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ExceptByTest extends TestCase {
    @Test
    public void testExceptBy() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
        };
        assertEquals(1, Linq.of(emps)
                .exceptBy(Linq.of(emps2), emp -> emp.deptno)
                .count());

        IEnumerable<Integer> oneToHundred = Linq.range(1, 100);
        IEnumerable<Integer> oneToFifty = Linq.range(1, 50);
        IEnumerable<Integer> fiftyOneToHundred = Linq.range(51, 50);
        assertTrue(oneToHundred.exceptBy(oneToFifty, a -> a).sequenceEqual(fiftyOneToHundred));
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

        Employee[] emps2 = {new Employee(150, "Theodore", 10)};
        assertEquals(0, Linq.of(emps)
                .exceptBy(Linq.of(emps2), emp -> emp.deptno, comparer)
                .count());
    }
}
