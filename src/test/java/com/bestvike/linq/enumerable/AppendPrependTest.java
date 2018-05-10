package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AppendPrependTest extends IteratorTest {

    @Test
    public void testAppend() {
        final String s = Linq.asEnumerable(emps).append(badEmps[0])
                .select(emp -> emp.name)
                .toList()
                .toString();
        Assert.assertEquals("[Fred, Bill, Eric, Janet, Cedric]", s);
    }
}