package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.IterableDemo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class CountTest extends EnumerableTest {
    @Test
    public void testCount() {
        final int count = Linq.asEnumerable(depts).count();
        Assert.assertEquals(3, count);
    }

    @Test
    public void testCountPredicate() {
        final int count = Linq.asEnumerable(depts).count(dept -> dept.employees.size() > 0);
        Assert.assertEquals(2, count);
    }

    @Test
    public void testLongCount() {
        final long count = Linq.asEnumerable(depts).longCount();
        Assert.assertEquals(3, count);

        final long count2 = Linq.asEnumerable(new IterableDemo(10)).longCount();
        Assert.assertEquals(10, count2);
    }

    @Test
    public void testLongCountPredicate() {
        final long count = Linq.asEnumerable(depts).longCount(dept -> dept.employees.size() > 0);
        Assert.assertEquals(2, count);

        final long count2 = Linq.asEnumerable(new IterableDemo(10L)).longCount(s -> s > 9);
        Assert.assertEquals(1, count2);
    }
}
