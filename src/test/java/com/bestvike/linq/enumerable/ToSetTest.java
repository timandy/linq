package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-17.
 */
public class ToSetTest extends TestCase {
    @Test
    public void NoExplicitComparer() {
        Set<Integer> hs = Linq.range(0, 50).toSet();
        assertIsType(HashSet.class, hs);
        assertEquals(50, hs.size());
    }

    @Test
    public void RunOnce() {
        Linq.range(0, 50).runOnce().toSet();
    }

    @Test
    public void TolerateNullElements() {
        // Unlike the keys of a dictionary, HashSet tolerates null items.
        assertFalse(Linq.of(new HashSet<String>()).contains(null));
        Set<String> hs = Linq.of(new String[]{"abc", null, "def"}).toSet();
        assertTrue(hs.contains(null));
    }

    @Test
    public void TolerateDuplicates() {
        // ToDictionary throws on duplicates, because that is the normal behaviour
        // of Dictionary<TKey, TValue>.Add().
        // By the same token, since the normal behaviour of HashSet<T>.Add()
        // is to signal duplicates without an exception ToHashSet should
        // tolerate duplicates.
        Set<Integer> hs = Linq.range(0, 50).select(i -> i / 5).toSet();

        // The OrderBy isn't strictly necessary, but that depends upon an
        // implementation detail of HashSet, so explicitly force ordering.
        assertEquals(Linq.range(0, 10), Linq.of(hs).orderBy(i -> i));
    }

    @Test
    public void ThrowOnNullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).toSet());
    }
}
