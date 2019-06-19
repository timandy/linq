package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-17.
 */
public class ToLinkedSetTest extends TestCase {
    @Test
    public void NoExplicitComparer() {
        Set<Integer> hs = Linq.range(0, 50).toLinkedSet();
        assertIsType(LinkedHashSet.class, hs);
        assertEquals(50, hs.size());
    }

    @Test
    public void RunOnce() {
        Linq.range(0, 50).runOnce().toLinkedSet();
    }

    @Test
    public void TolerateNullElements() {
        // Unlike the keys of a dictionary, HashSet tolerates null items.
        assertFalse(Linq.asEnumerable(new LinkedHashSet<String>()).contains(null));
        Set<String> hs = Linq.asEnumerable(new String[]{"abc", null, "def"}).toLinkedSet();
        assertTrue(hs.contains(null));
    }

    @Test
    public void TolerateDuplicates() {
        // ToDictionary throws on duplicates, because that is the normal behaviour
        // of Dictionary<TKey, TValue>.Add().
        // By the same token, since the normal behaviour of HashSet<T>.Add()
        // is to signal duplicates without an exception ToHashSet should
        // tolerate duplicates.
        Set<Integer> hs = Linq.range(0, 50).select(i -> i / 5).toLinkedSet();

        // The OrderBy isn't strictly necessary, but that depends upon an
        // implementation detail of HashSet, so explicitly force ordering.
        assertEquals(Linq.range(0, 10), Linq.asEnumerable(hs));
    }

    @Test
    public void ThrowOnNullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).toLinkedSet());
    }
}
