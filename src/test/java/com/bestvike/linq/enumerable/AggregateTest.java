package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AggregateTest extends IteratorTest {
    @Test
    public void testAggregate() {
        Assert.assertEquals("Sales,HR,Marketing", Linq.asEnumerable(depts)
                .select(dept -> dept.name)
                .aggregate((res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    public void testAggregateWithSeed() {
        Assert.assertEquals("A,Sales,HR,Marketing",
                Linq.asEnumerable(depts)
                        .select(dept -> dept.name)
                        .aggregate("A", (res, name) -> res == null ? name : res + "," + name));
    }

    @Test
    public void testAggregateWithSeedWithResultSelector() {
        String s = Linq.asEnumerable(emps)
                .select(emp -> emp.name)
                .aggregate(null,
                        (res, name) -> res == null ? name : res + "+" + name,
                        res -> "<no key>: " + res);
        Assert.assertEquals("<no key>: Fred+Bill+Eric+Janet", s);
    }
}