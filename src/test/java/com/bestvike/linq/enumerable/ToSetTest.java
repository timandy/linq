package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-17.
 */
class ToSetTest extends TestCase {
    @Test
    void NoExplicitComparer() {
        Set<Integer> hs = Linq.range(0, 50).toSet();
        assertIsType(HashSet.class, hs);
        assertEquals(50, hs.size());
    }

    @Test
    void RunOnce() {
        Linq.range(0, 50).runOnce().toSet();
    }

    @Test
    void TolerateNullElements() {
        // Unlike the keys of a dictionary, HashSet tolerates null items.
        assertFalse(Linq.of(new HashSet<String>()).contains(null));
        Set<String> hs = Linq.of(new String[]{"abc", null, "def"}).toSet();
        assertTrue(hs.contains(null));
    }

    @Test
    void TolerateDuplicates() {
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
    void ThrowOnNullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Object>) null).toSet());
    }

    @Test
    void testToSet() {
        assertEmpty(Linq.of(Linq.of().toSet()));

        Set<Integer> intSource = Linq.of(new LinkedList<>(Arrays.asList(1, 1, 2, 2, 3, 3))).toSet();
        assertEquals(3, intSource.size());

        //元素重复,保留第一个
        NameScore[] source = new NameScore[]{
                new NameScore("Chris", 50),
                new NameScore("Bob", 95),
                new NameScore("null", 55),
                new NameScore("Chris", 100)
        };
        Set<NameScore> set = Linq.of(source).toSet();
        assertSame(source[0], Linq.of(set).firstOrDefault(x -> x.Name.equals("Chris")));
        assertEquals(3, set.size());
    }


    private static class NameScore {
        private final String Name;
        private final int Score;

        private NameScore(String name, int score) {
            this.Name = name;
            this.Score = score;
        }

        @Override
        public int hashCode() {
            return Values.hashCode(this.Name);
        }

        @Override
        public boolean equals(Object that) {
            if (that == null)
                return false;
            if (that.getClass() != this.getClass())
                return false;
            return Values.equals(this.Name, ((NameScore) that).Name);
        }
    }
}
