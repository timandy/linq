package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.Department;
import com.bestvike.linq.entity.Employee;
import com.bestvike.linq.entity.IterableDemo;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class FirstTest extends IteratorTest {

    @Test
    public void testFirst() {
        Employee e = emps[0];
        Assert.assertEquals(e, emps[0]);
        Assert.assertEquals(e, Linq.asEnumerable(emps).first());

        Department d = depts[0];
        Assert.assertEquals(d, depts[0]);
        Assert.assertEquals(d, Linq.asEnumerable(depts).first());

        try {
            String s = Linq.<String>empty().first();
            Assert.fail("expected exception, got " + s);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first throws
        try {
            Long num = Linq.asEnumerable(new IterableDemo(0)).first();
            Assert.fail("expected exception, got " + num);
        } catch (InvalidOperationException ignored) {
        }

        // close occurs if first does not throw
        final Long num = Linq.asEnumerable(new IterableDemo(1)).first();
        Assert.assertEquals((Long) 1L, num);
    }

    @Test
    public void testFirstPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).first(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).first(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testFirstOrDefault() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] empty = {};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[0], Linq.asEnumerable(people).firstOrDefault());
        Assert.assertEquals(numbers[0], Linq.asEnumerable(numbers).firstOrDefault());

        Assert.assertNull(Linq.asEnumerable(empty).firstOrDefault());
    }

    @Test
    public void testFirstOrDefaultPredicate() {
        String[] people = {"Brill", "Smith", "Simpsom"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).firstOrDefault(i -> i > 15));
        Assert.assertNull(Linq.asEnumerable(peopleWithoutCharS).firstOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
    }

}