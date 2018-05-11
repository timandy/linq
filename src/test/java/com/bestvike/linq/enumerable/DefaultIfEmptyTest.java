package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class DefaultIfEmptyTest extends EnumerableTest {

    @Test
    public void testDefaultIfEmpty() {
        final List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        final IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty();
        final IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty();
        final IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertNull(emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }

    @Test
    public void testDefaultIfEmptyWithDefaultValue() {
        final List<String> experience = Arrays.asList("jimi", "mitch", "noel");
        final IEnumerable<String> notEmptyEnumerable = Linq.asEnumerable(experience).defaultIfEmpty("dummy");
        final IEnumerator<String> notEmptyEnumerator = notEmptyEnumerable.enumerator();
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("jimi", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("mitch", notEmptyEnumerator.current());
        notEmptyEnumerator.moveNext();
        Assert.assertEquals("noel", notEmptyEnumerator.current());

        final IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Linq.<String>empty()).defaultIfEmpty("N/A");
        final IEnumerator<String> emptyEnumerator = emptyEnumerable.enumerator();
        Assert.assertTrue(emptyEnumerator.moveNext());
        Assert.assertEquals("N/A", emptyEnumerator.current());
        Assert.assertFalse(emptyEnumerator.moveNext());
    }

}