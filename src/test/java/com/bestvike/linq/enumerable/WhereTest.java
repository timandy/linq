package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class WhereTest extends EnumerableTest {
    @Test
    public void testWhere() {
        List<String> names = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric, Janet]", names.toString());

        List<String> names2 = Linq.asEnumerable(emps)
                .where(employee -> employee.deptno < 15)
                .where(employee -> employee.name.length() == 4)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric]", names2.toString());

        List<Integer> even = Linq.range(1, 10)
                .where(n -> n % 2 == 0)
                .toList();
        Assert.assertEquals("[2, 4, 6, 8, 10]", even.toString());

        Integer[] numbs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> even2 = Linq.asEnumerable(numbs)
                .where(n -> n % 2 == 0)
                .toList();
        Assert.assertEquals("[2, 4, 6, 8, 10]", even2.toString());
    }

    @Test
    public void testWhereIndexed() {
        // Returns every other employee.
        List<String> names = Linq.asEnumerable(emps)
                .where((employee, n) -> n % 2 == 0)
                .select(a -> a.name)
                .toList();
        Assert.assertEquals("[Fred, Eric]", names.toString());
    }

    @Test
    public void testSelect() {
        List<String> names = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .toList();
        Assert.assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<Character> names2 = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .select(name -> name.charAt(0))
                .toList();
        Assert.assertEquals("[F, B, E, J]", names2.toString());
    }
}
