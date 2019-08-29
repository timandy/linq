package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
public class GroupingDebugViewTest extends TestCase {
    private static IEnumerable<Object[]> DebuggerAttributesValid_Data() {
        List<Object[]> lst = new ArrayList<>();
        IEnumerable<Integer> source = Linq.of(new int[]{1});
        lst.add(new Object[]{source.groupBy(i -> i).single(), "1"});
        lst.add(new Object[]{source.groupBy(i -> i.toString(), i -> i).single(), "\"1\""});
        lst.add(new Object[]{source.groupBy(i -> Duration.ofSeconds(i), i -> i).single(), "{PT1S}"});
        lst.add(new Object[]{Linq.of(new String[]{null}).groupBy(x -> x).single(), "null"});
        lst.add(new Object[]{Linq.of(new Integer[]{null}).groupBy(x -> x).single(), "null"});
        return Linq.of(lst);
    }

    @Test
    public void DebuggerAttributesValid() {
        for (Object[] objects : DebuggerAttributesValid_Data()) {
            this.DebuggerAttributesValid((IGrouping<?, ?>) objects[0], (String) objects[1]);
        }
    }

    private <TKey, TElement> void DebuggerAttributesValid(IGrouping<TKey, TElement> grouping, String keyString) {
        //key
        assertEquals("key = " + keyString, DebugView.getDebuggerDisplay(grouping));
        //values
        Object debuggerTypeProxy = DebugView.getDebuggerTypeProxy(grouping);
        assertIsType(Object[].class, debuggerTypeProxy);
        Object[] values = (Object[]) debuggerTypeProxy;
        assertEquals(grouping.toArray(), Linq.of(values));
        assertSame(debuggerTypeProxy, DebugView.getDebuggerTypeProxy(grouping));
    }
}
