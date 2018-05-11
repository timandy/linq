package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.entity.IterableDemo;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ElementAtTest extends IteratorTest {

    @Test
    public void testElementAt() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("jimi", enumerable.elementAt(0));
        try {
            enumerable.elementAt(2);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
            // ok
        }
        try {
            enumerable.elementAt(-1);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignored) {
        }

        final IEnumerable<Long> enumerable2 = Linq.asEnumerable(new IterableDemo(2));
        Assert.assertEquals((Long) 1L, enumerable2.elementAt(0));
        try {
            enumerable2.elementAt(2);
            Assert.fail();
        } catch (ArgumentOutOfRangeException ignored) {
        }
        try {
            enumerable2.elementAt(-1);
            Assert.fail();
        } catch (ArgumentOutOfRangeException ignored) {
        }

        IEnumerable<Integer> one = Linq.singleton(1);
        Assert.assertEquals((Integer) 1, one.elementAt(0));

        IEnumerable<Integer> empty = Linq.empty();
        try {
            Integer num = empty.elementAt(0);
            Assert.fail("expect error,but got " + num);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void testElementAtOrDefault() {
        final IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("jimi", enumerable.elementAtOrDefault(0));
        Assert.assertNull(enumerable.elementAtOrDefault(2));
        Assert.assertNull(enumerable.elementAtOrDefault(-1));

        final IEnumerable<Long> enumerable2 = Linq.asEnumerable(new IterableDemo(2));
        Assert.assertEquals((Long) 1L, enumerable2.elementAtOrDefault(0));
        Assert.assertNull(enumerable2.elementAtOrDefault(2));
        Assert.assertNull(enumerable2.elementAtOrDefault(-1));
    }

}