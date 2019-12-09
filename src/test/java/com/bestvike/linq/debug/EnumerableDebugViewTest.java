package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.resources.SR;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
class EnumerableDebugViewTest extends TestCase {
    @Test
    void GenericEnumerableDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplayText(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerProxyObject(null));
    }

    @Test
    void GenericEnumerableDebugView_ThrowsForEmptySource() {
        IEnumerable<Integer> source = Linq.range(10, 0);
        assertEquals("Count = 0", DebugView.getDebuggerDisplayText(source));
        assertEquals(SR.EmptyEnumerable, DebugView.getDebuggerProxyObject(source));
    }

    @Test
    void GenericEnumerableDebugView_NonEmptySource() {
        IEnumerable<Integer> source = Linq.range(10, 5);
        assertEquals("Count = 5", DebugView.getDebuggerDisplayText(source));
        //
        Object proxyObject = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEquals(source.cast(Object.class).toArray(), Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(source));
    }
}
