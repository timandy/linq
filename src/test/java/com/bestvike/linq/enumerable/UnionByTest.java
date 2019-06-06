package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class UnionByTest extends TestCase {
    @Test
    public void testUnionBy() {
        IEnumerable<Employee> enumerable = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno);
        assertEquals(4, enumerable.count());
        UnionByIterator2<Employee, Integer> unionByIterator2 = (UnionByIterator2<Employee, Integer>) enumerable;
        assertEquals(4, unionByIterator2._toArray(Employee.class).length);
        assertEquals(4, unionByIterator2._toArray().length);
        assertEquals(4, unionByIterator2._toList().size());
        assertEquals(-1, unionByIterator2._getCount(true));
        assertEquals(4, unionByIterator2._getCount(false));


        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), selector)
                .unionBy(Linq.asEnumerable(emps), selector);
        assertEquals(4, enumerable2.count());
        UnionByIteratorN<Employee, Integer> unionByIterator22 = (UnionByIteratorN<Employee, Integer>) enumerable2;
        assertEquals(4, unionByIterator22._toArray(Employee.class).length);
        assertEquals(4, unionByIterator22._toArray().length);
        assertEquals(4, unionByIterator22._toList().size());
        assertEquals(-1, unionByIterator22._getCount(true));
        assertEquals(4, unionByIterator22._getCount(false));
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


        IEnumerable<Employee> enumerable = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), emp -> emp.deptno, comparer)
                .unionBy(Linq.asEnumerable(emps), emp -> emp.deptno, comparer);
        assertEquals(1, enumerable.count());
        UnionByIterator2<Employee, Integer> unionByIterator2 = (UnionByIterator2<Employee, Integer>) enumerable;
        assertEquals(1, unionByIterator2._toArray(Employee.class).length);
        assertEquals(1, unionByIterator2._toArray().length);
        assertEquals(1, unionByIterator2._toList().size());
        assertEquals(-1, unionByIterator2._getCount(true));
        assertEquals(1, unionByIterator2._getCount(false));


        Func1<Employee, Integer> selector = emp -> emp.deptno;
        IEnumerable<Employee> enumerable2 = Linq.asEnumerable(emps)
                .unionBy(Linq.asEnumerable(badEmps), selector, comparer)
                .unionBy(Linq.asEnumerable(emps), selector, comparer);
        assertEquals(1, enumerable2.count());
        UnionByIteratorN<Employee, Integer> unionByIterator22 = (UnionByIteratorN<Employee, Integer>) enumerable2;
        assertEquals(1, unionByIterator22._toArray(Employee.class).length);
        assertEquals(1, unionByIterator22._toArray().length);
        assertEquals(1, unionByIterator22._toList().size());
        assertEquals(-1, unionByIterator22._getCount(true));
        assertEquals(1, unionByIterator22._getCount(false));
    }
}
