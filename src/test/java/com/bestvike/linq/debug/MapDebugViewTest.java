package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 许崇雷 on 2019-12-09.
 */
class MapDebugViewTest extends TestCase {
    @Test
    void MapDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplayText(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerProxyObject(null));
    }

    @Test
    void MapDebugView_EmptySource() {
        Map<Integer, String> source = new HashMap<>();
        assertEquals("Count = 0", DebugView.getDebuggerDisplayText(source));
        //
        Object proxyObject = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEmpty(Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(source));
    }

    @Test
    void MapDebugView_NonEmptySource() {
        Map<Integer, String> source = new HashMap<>();
        source.put(1, "1");
        source.put(2, "2");
        source.put(3, "3");
        assertEquals("Count = 3", DebugView.getDebuggerDisplayText(source));
        //
        Object proxyObject = DebugView.getDebuggerProxyObject(source);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEquals(Linq.of(source).cast(Object.class).toArray(), Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(source));
    }
}
