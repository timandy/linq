package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class AnyAllTest extends IteratorTest {

    @Test
    public void testAny() {
        Assert.assertFalse(Linq.asEnumerable(Collections.emptyList()).any());
        Assert.assertTrue(Linq.asEnumerable(emps).any());
    }

    @Test
    public void testAnyPredicate() {
        Assert.assertFalse(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("IT")));
        Assert.assertTrue(Linq.asEnumerable(depts).any(dept -> dept.name != null && dept.name.equals("Sales")));
    }

    @Test
    public void testAllPredicate() {
        Assert.assertTrue(Linq.asEnumerable(emps).all(emp -> emp.empno >= 100));
        Assert.assertFalse(Linq.asEnumerable(emps).all(emp -> emp.empno > 100));
    }

}