package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-17.
 */
public class TakeLastTest extends TestCase {
    @Test
    public void testTakeLast() {
        Assert.assertEquals(1, Linq.asEnumerable(depts).takeLast(1).count());
        Assert.assertEquals(3, Linq.asEnumerable(depts).takeLast(5).count());
    }

    @Test
    public void runOnce() {
        IEnumerable<Department> expected = Linq.asEnumerable(depts).skip(2);
        assertEquals(expected, Linq.asEnumerable(depts).takeLast(1).runOnce());
    }
}
