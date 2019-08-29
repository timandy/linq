package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-05-08.
 */
class EmptyEnumerableTest extends TestCase {
    private <T> void TestEmptyCached() {
        IEnumerable<T> enumerable1 = Linq.empty();
        IEnumerable<T> enumerable2 = Linq.empty();

        assertSame(enumerable1, enumerable2); // Enumerable.empty is not cached if not the same.
    }

    @Test
    void EmptyEnumerableCachedTest() {
        this.<Integer>TestEmptyCached();
        this.<String>TestEmptyCached();
        this.TestEmptyCached();
        this.<EmptyEnumerableTest>TestEmptyCached();
    }

    private <T> void TestEmptyEmpty() {
        assertEquals(Linq.of(), Linq.empty());
        assertEquals(0, Linq.<T>empty().count());
        assertSame(Linq.<T>empty().enumerator(), Linq.<T>empty().enumerator());
    }

    @Test
    void EmptyEnumerableIsIndeedEmpty() {
        this.<Integer>TestEmptyEmpty();
        this.<String>TestEmptyEmpty();
        this.TestEmptyEmpty();
        this.<EmptyEnumerableTest>TestEmptyEmpty();
    }
}
