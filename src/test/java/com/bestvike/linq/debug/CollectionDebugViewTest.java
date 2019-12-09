package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by 许崇雷 on 2019-12-09.
 */
class CollectionDebugViewTest extends TestCase {
    @Test
    void CollectionDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplayText(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerProxyObject(null));
    }

    @Test
    void CollectionDebugView_EmptySource() {
        Collection<Integer> source = new LinkedList<>();
        assertEquals("Count = 0", DebugView.getDebuggerDisplayText(source));
        //
        Object proxyObject = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEmpty(Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(source));
    }

    @Test
    void CollectionDebugView_NonEmptySource() {
        Collection<Integer> source = new LinkedList<>(Arrays.asList(1, 2, 3));
        assertEquals("Count = 3", DebugView.getDebuggerDisplayText(source));
        //
        Object proxyObject = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEquals(Linq.of(source).cast(Object.class).toArray(), Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(source));
    }
}
