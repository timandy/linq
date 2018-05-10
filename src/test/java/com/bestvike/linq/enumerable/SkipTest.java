package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SkipTest extends IteratorTest {

    @Test
    public void testSkip() {
        Assert.assertEquals(2, Linq.asEnumerable(depts).skip(1).count());
        Assert.assertEquals(0, Linq.asEnumerable(depts).skip(5).count());
    }

    @Test
    public void testSkipWhile() {
        Assert.assertEquals(2, Linq.asEnumerable(depts).skipWhile(dept -> dept.name.equals("Sales")).count());
        Assert.assertEquals(3, Linq.asEnumerable(depts).skipWhile(dept -> !dept.name.equals("Sales")).count());
    }

    @Test
    public void testSkipWhileIndexed() {
        int count = Linq.asEnumerable(depts)
                .skipWhile((dept, index) -> dept.name.equals("Sales") || index == 1)
                .count();
        Assert.assertEquals(1, count);
    }

}