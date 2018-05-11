package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ConcatTest extends EnumerableTest {

    @Test
    public void testConcat() {
        Assert.assertEquals(6, Linq.asEnumerable(emps).concat(Linq.asEnumerable(badEmps)).count());
    }

}