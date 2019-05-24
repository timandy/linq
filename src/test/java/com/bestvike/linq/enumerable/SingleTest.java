package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class SingleTest extends TestCase {
    @Test
    public void testSingle() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        assertEquals(person[0], Linq.asEnumerable(person).single());
        assertEquals(number[0], Linq.asEnumerable(number).single());
        try {
            String s = Linq.asEnumerable(people).single();
            fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            int i = Linq.asEnumerable(numbers).single();
            fail("expected exception, but got" + i);
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

        assertEquals(people[1], Linq.asEnumerable(people).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.asEnumerable(numbers).single(i -> i > 15));

        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithTwoGT15).single(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }

        try {
            String ss = Linq.asEnumerable(peopleWithoutCharS).single(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }

        try {
            int i = Linq.asEnumerable(numbersWithoutGT15).single(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
    }

    @Test
    public void testSingleOrDefault() {
        String[] person = {"Smith"};
        String[] people = {"Brill", "Smith", "Simpson"};
        Integer[] number = {20};
        Integer[] numbers = {5, 10, 15, 20};

        assertEquals(person[0], Linq.asEnumerable(person).singleOrDefault());
        assertEquals(number[0], Linq.asEnumerable(number).singleOrDefault());
        try {
            String s = Linq.asEnumerable(people).singleOrDefault();
            fail("expected exception, but got" + s);
        } catch (InvalidOperationException ignored) {
        }
        try {
            Integer i = Linq.asEnumerable(numbers).singleOrDefault();
            fail("expected exception, but got" + i);
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

        assertEquals(people[1], Linq.asEnumerable(people).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertEquals(numbers[3], Linq.asEnumerable(numbers).singleOrDefault(n -> n > 15));
        try {
            String ss = Linq.asEnumerable(twoPeopleWithCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S');
            fail("expected exception, but got" + ss);
        } catch (InvalidOperationException ignored) {
        }
        try {

            Integer i = Linq.asEnumerable(numbersWithTwoGT15).singleOrDefault(n -> n > 15);
            fail("expected exception, but got" + i);
        } catch (InvalidOperationException ignored) {
        }
        assertNull(Linq.asEnumerable(peopleWithoutCharS).singleOrDefault(s -> s != null && s.length() > 0 && s.charAt(0) == 'S'));
        assertNull(Linq.asEnumerable(numbersWithoutGT15).singleOrDefault(n -> n > 15));
    }
}
