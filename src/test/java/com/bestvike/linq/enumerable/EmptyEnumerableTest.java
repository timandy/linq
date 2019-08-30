package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-05-08.
 */
class EmptyEnumerableTest extends TestCase {
    private static <T> void TestEmptyCached() {
        IEnumerable<T> enumerable1 = Linq.empty();
        IEnumerable<T> enumerable2 = Linq.empty();

        assertSame(enumerable1, enumerable2); // Enumerable.empty is not cached if not the same.
    }

    private static <T> void TestEmptyEmpty() {
        assertEquals(Linq.of(), Linq.empty());
        assertEquals(0, Linq.<T>empty().count());
        assertSame(Linq.<T>empty().enumerator(), Linq.<T>empty().enumerator());
    }

    @Test
    void EmptyEnumerableCachedTest() {
        EmptyEnumerableTest.<Integer>TestEmptyCached();
        EmptyEnumerableTest.<String>TestEmptyCached();
        EmptyEnumerableTest.TestEmptyCached();
        EmptyEnumerableTest.<EmptyEnumerableTest>TestEmptyCached();
    }

    @Test
    void EmptyEnumerableIsIndeedEmpty() {
        EmptyEnumerableTest.<Integer>TestEmptyEmpty();
        EmptyEnumerableTest.<String>TestEmptyEmpty();
        EmptyEnumerableTest.TestEmptyEmpty();
        EmptyEnumerableTest.<EmptyEnumerableTest>TestEmptyEmpty();
    }
}
