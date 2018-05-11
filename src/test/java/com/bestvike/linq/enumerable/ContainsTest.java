package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ContainsTest extends EnumerableTest {

    @Test
    public void testContains() {
        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        Assert.assertEquals(e, employeeClone);
        Assert.assertTrue(Linq.asEnumerable(emps).contains(e));
        Assert.assertTrue(Linq.asEnumerable(emps).contains(employeeClone));
        Assert.assertFalse(Linq.asEnumerable(emps).contains(employeeOther));

        Assert.assertTrue(Linq.asEnumerable(Arrays.asList('h', 'e', 'l', 'l', 'o')).contains('h'));

        Character[] arrChar = {'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(Linq.asEnumerable(arrChar).contains('h'));

        Assert.assertTrue(Linq.asEnumerable("hello").contains('h'));

        Assert.assertTrue(Linq.singleton('h').contains('h'));
        Assert.assertFalse(Linq.singleton('h').contains('o'));

        Assert.assertFalse(Linq.empty().contains(1));
    }

    @Test
    public void testContainsWithEqualityComparer() {
        IEqualityComparer<Employee> comparer = new IEqualityComparer<Employee>() {
            @Override
            public boolean equals(Employee x, Employee y) {
                return x != null && y != null
                        && x.empno == y.empno;
            }

            @Override
            public int hashCode(Employee obj) {
                return obj == null ? 0x789d : obj.hashCode();
            }
        };

        Employee e = emps[1];
        Employee employeeClone = new Employee(e.empno, e.name, e.deptno);
        Employee employeeOther = badEmps[0];

        Assert.assertEquals(e, employeeClone);
        Assert.assertTrue(Linq.asEnumerable(emps).contains(e, comparer));
        Assert.assertTrue(Linq.asEnumerable(emps).contains(employeeClone, comparer));
        Assert.assertFalse(Linq.asEnumerable(emps).contains(employeeOther, comparer));
    }

    @Test
    public void testSumInt() {
        final Integer[] numbers = {null, 0, 2, 3};
        Assert.assertEquals(5, Linq.asEnumerable(numbers).sumInt());

        final Integer[] numbers2 = {null, Integer.MAX_VALUE - 1, 1};
        Assert.assertEquals(Integer.MAX_VALUE, Linq.asEnumerable(numbers2).sumInt());

        final Integer[] numbers3 = {null, Integer.MAX_VALUE, 1};
        try {
            int num = Linq.asEnumerable(numbers3).sumInt();
            Assert.fail("expect error,but got " + num);
        } catch (ArithmeticException ignored) {
        }
    }

}