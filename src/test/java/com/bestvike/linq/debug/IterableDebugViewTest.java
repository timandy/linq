package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.resources.SR;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
class IterableDebugViewTest extends TestCase {
    @Test
    void GenericIterableDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplay(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerProxyObject(null));
    }

    @Test
    void GenericIterableDebugView_ThrowsForEmptySource() {
        Iterable<Long> source = new CountIterable(0);
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplay(source));
        assertEquals(SR.EmptyIterable, DebugView.getDebuggerProxyObject(source));
    }

    @Test
    void GenericIterableDebugView_NonEmptySource() {
        Iterable<Long> source = new CountIterable(5);
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplay(source));
        //
        Object debuggerTypeProxy = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, debuggerTypeProxy);
        Object[] values = (Object[]) debuggerTypeProxy;
        assertEquals(Linq.of(source).cast(Object.class).toArray(), Linq.of(values));
        assertSame(debuggerTypeProxy, DebugView.getDebuggerProxyObject(source));
    }
}
