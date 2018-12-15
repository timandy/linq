package com.bestvike.linq.enumerable;

import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SingleTest extends EnumerableTest {
    @Test
    public void testSingle() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        Assert.assertEquals(person[0], Linq.asEnumerable(person).single());
        Assert.assertEquals(number[0], Linq.asEnumerable(number).single());
        try {
            String s = Linq.asEnumerable(people).single();
            Assert.fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            int i = Linq.asEnumerable(numbers).single();
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSinglePredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithoutGT15 = {5, 10, 15};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).single(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithTwoGT15).single(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithoutGT15).single(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSingleOrDefault() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        Assert.assertEquals(person[0], Linq.asEnumerable(person).singleOrDefault());
        Assert.assertEquals(number[0], Linq.asEnumerable(number).singleOrDefault());
        try {
            String s = Linq.asEnumerable(people).singleOrDefault();
            Assert.fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            Integer i = Linq.asEnumerable(numbers).singleOrDefault();
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSingleOrDefaultPredicate() {
        String[] people = {"Brill", "Smith"};
        String[] twoPeopleWithCharS = {"Brill", "Smith", "Simpson"};
        String[] peopleWithoutCharS = {"Brill", "Andrew", "Alice"};
        Integer[] numbers = {5, 10, 15, 20};
        Integer[] numbersWithTwoGT15 = {5, 10, 15, 20, 25};
        Integer[] numbersWithoutGT15 = {5, 10, 15};

        Assert.assertEquals(people[1], Linq.asEnumerable(people).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertEquals(numbers[3], Linq.asEnumerable(numbers).singleOrDefault(n -> n > 15));
        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            Assert.fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
        try {

            Integer i = Linq.asEnumerable(numbersWithTwoGT15).singleOrDefault(n -> n > 15);
            Assert.fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
        Assert.assertNull(Linq.asEnumerable(peopleWithoutCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        Assert.assertNull(Linq.asEnumerable(numbersWithoutGT15).singleOrDefault(n -> n > 15));
    }
}
