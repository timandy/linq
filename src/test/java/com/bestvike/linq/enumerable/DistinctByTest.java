package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class DistinctByTest extends TestCase {
    @Test
    public void testDistinctBy() {
        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[0],
                emps[3],
        };
        IEnumerable<Employee> enumerable = Linq.of(emps2).distinctBy(emp -> emp.deptno);
        DistinctByIterator<Employee, Integer> distinctIterator = (DistinctByIterator<Employee, Integer>) enumerable;
        assertEquals(1, distinctIterator._toArray(Employee.class).length);
        assertEquals(1, distinctIterator._toArray().length);
        assertEquals(1, distinctIterator._toList().size());
        assertEquals(-1, distinctIterator._getCount(true));
        assertEquals(1, distinctIterator._getCount(false));
        assertEquals(1, enumerable.count());
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

        Employee[] emps2 = {
                new Employee(150, "Theodore", 10),
                emps[3],
                emps[1],
                emps[3]
        };

        IEnumerable<Employee> enumerable = Linq.of(emps2).distinctBy(emp -> emp.empno, comparer);
        DistinctByIterator<Employee, Integer> distinctIterator = (DistinctByIterator<Employee, Integer>) enumerable;
        assertEquals(1, distinctIterator._toArray(Employee.class).length);
        assertEquals(1, distinctIterator._toArray().length);
        assertEquals(1, distinctIterator._toList().size());
        assertEquals(-1, distinctIterator._getCount(true));
        assertEquals(1, distinctIterator._getCount(false));
        assertEquals(1, enumerable.count());
    }
}
