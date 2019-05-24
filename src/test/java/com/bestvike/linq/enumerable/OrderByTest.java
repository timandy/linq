package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class OrderByTest extends TestCase {
    @Test
    public void testOrderBy() {
        //null 在前,值相等的按原始顺序
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", sss);

        Set<Integer> set = new HashSet<>();
        set.add(3);
        set.add(1);
        set.add(2);
        IEnumerable<Integer> ordered = Linq.asEnumerable(set).orderBy(a -> a);
        Integer[] orderedArr = {1, 2, 3};
        Assert.assertTrue(ordered.sequenceEqual(Linq.asEnumerable(orderedArr)));
    }

    @Test
    public void testOrderByWithComparer() {
        //null 在后,值相等的按原始顺序
        Comparator<Object> reverse = Comparer.Default().reversed();

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderBy(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", sss);
    }

    @Test
    public void testOrderByDesc() {
        //null 在后,值相等的按原始顺序,同 testOrderByWithComparer()
        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Fred, Eric, Janet, Gates]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenBy(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Eric, Fred, Janet, Gates]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno)
                .thenByDescending(emp -> emp.name)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Cedric, Bill, Janet, Fred, Eric, Gates]", sss);
    }

    @Test
    public void testOrderByDescWithComparer() {
        //null 在后,值相等的按原始顺序
        Comparator<Object> reverse = Comparer.Default().reversed();

        String s = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Fred, Eric, Janet, Bill, Cedric]", s);

        String ss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenBy(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Janet, Fred, Eric, Bill, Cedric]", ss);

        String sss = Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps))
                .orderByDescending(emp -> emp.deptno, reverse)
                .thenByDescending(emp -> emp.name, reverse)
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Gates, Eric, Fred, Janet, Bill, Cedric]", sss);
    }
}
