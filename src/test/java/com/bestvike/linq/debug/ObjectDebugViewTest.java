package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.ArgumentNullException;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2019-12-09.
 */
class ObjectDebugViewTest extends TestCase {
    @Test
    void ObjectDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplayText(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerProxyObject(null));
    }

    @Test
    void ObjectDebugView_NonNull() {
        Object source = new Object();
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplayText(source));
        //
        assertSame(source, DebugView.getDebuggerProxyObject(source));
    }
}
