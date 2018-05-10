package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class CastTest extends IteratorTest {

    @Test
    public void testOfType() {
        final List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        final IEnumerator<Integer> enumerator = Linq.asEnumerable(numbers)
                .ofType(Integer.class)
                .enumerator();
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(Integer.valueOf(2), enumerator.current());
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(Integer.valueOf(5), enumerator.current());
        Assert.assertFalse(enumerator.moveNext());
    }

    @Test
    public void testCast() {
        final List<Number> numbers = Arrays.asList(2, null, 3.14, 5);
        final IEnumerator<Integer> enumerator = Linq.asEnumerable(numbers)
                .cast(Integer.class)
                .enumerator();

        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals((Integer) 2, enumerator.current());
        Assert.assertTrue(enumerator.moveNext());
        Assert.assertEquals(null, enumerator.current());
        try {
            enumerator.moveNext();
            Assert.fail("cast() fail");
        } catch (ClassCastException ignored) {
        }
    }

}