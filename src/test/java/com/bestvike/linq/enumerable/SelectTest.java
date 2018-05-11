package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SelectTest extends EnumerableTest {

    @Test
    public void testSelectIndexed() {
        List<String> names = Linq.asEnumerable(emps)
                .select((emp, index) -> emp.name)
                .toList();
        Assert.assertEquals("[Fred, Bill, Eric, Janet]", names.toString());

        List<String> indexes = Linq.asEnumerable(emps)
                .select((emp, index) -> String.format("#%d: %s", index, emp.name))
                .toList();
        Assert.assertEquals("[#0: Fred, #1: Bill, #2: Eric, #3: Janet]", indexes.toString());
    }

}