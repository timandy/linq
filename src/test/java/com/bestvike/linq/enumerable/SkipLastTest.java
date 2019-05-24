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
public class SkipLastTest extends TestCase {
    @Test
    public void testSkipLast() {
        Assert.assertEquals(2, Linq.asEnumerable(depts).skipLast(1).count());
        Assert.assertEquals(0, Linq.asEnumerable(depts).skipLast(5).count());
    }

    @Test
    public void runOnce() {
        IEnumerable<Department> expected = Linq.asEnumerable(depts).take(2);
        assertEquals(expected, Linq.asEnumerable(depts).skipLast(1).runOnce());
    }
}
