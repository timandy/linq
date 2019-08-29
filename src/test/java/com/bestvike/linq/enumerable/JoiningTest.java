package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-08-07.
 */
class JoiningTest extends TestCase {
    @Test
    void testJoining() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining());
        assertEquals(Empty, Linq.empty().joining());
        assertEquals("0", Linq.of(0).joining());
        assertEquals("01", Linq.of(0, 1).joining());
        assertEquals("0123null", Linq.of(0, 1, 2, 3, null).joining());
    }

    @Test
    void testJoiningWithSeparator() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(null));
        assertEquals(Empty, Linq.empty().joining(null));
        assertEquals("0", Linq.of(0).joining(null));
        assertEquals("01", Linq.of(0, 1).joining(null));
        assertEquals("0123null", Linq.of(0, 1, 2, 3, null).joining(null));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(", "));
        assertEquals(Empty, Linq.empty().joining(", "));
        assertEquals("0", Linq.of(0).joining(", "));
        assertEquals("0, 1", Linq.of(0, 1).joining(", "));
        assertEquals("0, 1, 2, 3, null", Linq.of(0, 1, 2, 3, null).joining(", "));
    }

    @Test
    void testJoiningWithSeparatorAndPrefixAndSuffix_NoneValidArg() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(null, null, null));
        assertEquals(Empty, Linq.empty().joining(null, null, null));
        assertEquals("0", Linq.of(0).joining(null, null, null));
        assertEquals("01", Linq.of(0, 1).joining(null, null, null));
        assertEquals("0123null", Linq.of(0, 1, 2, 3, null).joining(null, null, null));
    }

    @Test
    void testJoiningWithSeparatorAndPrefixAndSuffix_OneValidNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(", ", null, null));
        assertEquals(Empty, Linq.empty().joining(", ", null, null));
        assertEquals("0", Linq.of(0).joining(", ", null, null));
        assertEquals("0, 1", Linq.of(0, 1).joining(", ", null, null));
        assertEquals("0, 1, 2, 3, null", Linq.of(0, 1, 2, 3, null).joining(", ", null, null));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(null, "[", null));
        assertEquals("[", Linq.empty().joining(null, "[", null));
        assertEquals("[0", Linq.of(0).joining(null, "[", null));
        assertEquals("[01", Linq.of(0, 1).joining(null, "[", null));
        assertEquals("[0123null", Linq.of(0, 1, 2, 3, null).joining(null, "[", null));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(null, null, "]"));
        assertEquals("]", Linq.empty().joining(null, null, "]"));
        assertEquals("0]", Linq.of(0).joining(null, null, "]"));
        assertEquals("01]", Linq.of(0, 1).joining(null, null, "]"));
        assertEquals("0123null]", Linq.of(0, 1, 2, 3, null).joining(null, null, "]"));
    }

    @Test
    void testJoiningWithSeparatorAndPrefixAndSuffix_TwoValidNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(", ", "[", null));
        assertEquals("[", Linq.empty().joining(", ", "[", null));
        assertEquals("[0", Linq.of(0).joining(", ", "[", null));
        assertEquals("[0, 1", Linq.of(0, 1).joining(", ", "[", null));
        assertEquals("[0, 1, 2, 3, null", Linq.of(0, 1, 2, 3, null).joining(", ", "[", null));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(", ", null, "]"));
        assertEquals("]", Linq.empty().joining(", ", null, "]"));
        assertEquals("0]", Linq.of(0).joining(", ", null, "]"));
        assertEquals("0, 1]", Linq.of(0, 1).joining(", ", null, "]"));
        assertEquals("0, 1, 2, 3, null]", Linq.of(0, 1, 2, 3, null).joining(", ", null, "]"));

        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(null, "[", "]"));
        assertEquals("[]", Linq.empty().joining(null, "[", "]"));
        assertEquals("[0]", Linq.of(0).joining(null, "[", "]"));
        assertEquals("[01]", Linq.of(0, 1).joining(null, "[", "]"));
        assertEquals("[0123null]", Linq.of(0, 1, 2, 3, null).joining(null, "[", "]"));
    }

    @Test
    void testJoiningWithSeparatorAndPrefixAndSuffix_AllValidNull() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<String>) null).joining(", ", "[", "]"));
        assertEquals("[]", Linq.empty().joining(", ", "[", "]"));
        assertEquals("[0]", Linq.of(0).joining(", ", "[", "]"));
        assertEquals("[0, 1]", Linq.of(0, 1).joining(", ", "[", "]"));
        assertEquals("[0, 1, 2, 3, null]", Linq.of(0, 1, 2, 3, null).joining(", ", "[", "]"));
    }
}
