package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.resources.SR;
import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
public class EnumerableDebugViewTest extends TestCase {
    @Test
    public void GenericEnumerableDebugView_ThrowsForNullSource() {
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerDisplay(null));
        assertThrows(ArgumentNullException.class, () -> DebugView.getDebuggerTypeProxy(null));
    }

    @Test
    public void GenericEnumerableDebugView_ThrowsForEmptySource() {
        IEnumerable<Integer> source = Linq.range(10, 0);
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplay(source));
        assertEquals(SR.EmptyEnumerable, DebugView.getDebuggerTypeProxy(source));
    }

    @Test
    public void GenericEnumerableDebugView_NonEmptySource() {
        IEnumerable<Integer> source = Linq.range(10, 5);
        assertEquals(source.getClass().getName(), DebugView.getDebuggerDisplay(source));
        //
        Object debuggerTypeProxy = DebugView.getDebuggerTypeProxy(source);
        assertIsType(Object[].class, debuggerTypeProxy);
        Object[] values = (Object[]) debuggerTypeProxy;
        assertEquals(source.cast(Object.class).toArray(), Linq.asEnumerable(values));
        assertSame(debuggerTypeProxy, DebugView.getDebuggerTypeProxy(source));
    }
}
