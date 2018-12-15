package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class ZipTest extends EnumerableTest {
    @Test
    public void testZip() {
        final IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b", "c"));
        final IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));

        final IEnumerable<String> zipped = e1.zip(e2, (v0, v1) -> v0 + v1);
        Assert.assertEquals(3, zipped.count());
        for (int i = 0; i < 3; i++)
            Assert.assertEquals("" + (char) ('a' + i) + (char) ('1' + i), zipped.elementAt(i));
    }

    @Test
    public void testZipLengthNotMatch() {
        final IEnumerable<String> e1 = Linq.asEnumerable(Arrays.asList("a", "b"));
        final IEnumerable<String> e2 = Linq.asEnumerable(Arrays.asList("1", "2", "3"));

        final IEnumerable<String> zipped1 = e1.zip(e2, (v0, v1) -> v0 + v1);
        Assert.assertEquals(2, zipped1.count());
        for (int i = 0; i < 2; i++)
            Assert.assertEquals("" + (char) ('a' + i) + (char) ('1' + i), zipped1.elementAt(i));

        final IEnumerable<String> zipped2 = e2.zip(e1, (v0, v1) -> v0 + v1);
        Assert.assertEquals(2, zipped2.count());
        for (int i = 0; i < 2; i++)
            Assert.assertEquals("" + (char) ('1' + i) + (char) ('a' + i), zipped2.elementAt(i));
    }
}
