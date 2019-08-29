package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.Linq;
import com.bestvike.tuple.Tuple;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
class LookupDebugViewTest extends TestCase {
    private static IEnumerable<Object[]> DebuggerAttributesValid_Data() {
        List<Object[]> lst = new ArrayList<>();
        IEnumerable<Integer> source = Linq.of(new int[]{1});
        lst.add(new Object[]{source.toLookup(i -> i)});
        lst.add(new Object[]{source.toLookup(i -> i.toString(), i -> i)});
        lst.add(new Object[]{source.toLookup(i -> Duration.ofSeconds(i), i -> i)});
        lst.add(new Object[]{Linq.of(new String[]{null}).toLookup(x -> x)});
        lst.add(new Object[]{Linq.of(new Integer[]{null}).toLookup(x -> x)});
        return Linq.of(lst);
    }

    @Test
    void DebuggerAttributesValid() {
        for (Object[] objects : DebuggerAttributesValid_Data()) {
            this.DebuggerAttributesValid((ILookup<?, ?>) objects[0]);
        }
    }

    private <TKey, TElement> void DebuggerAttributesValid(ILookup<TKey, TElement> lookup) {
        //count
        assertEquals("count = " + lookup.getCount(), DebugView.getDebuggerDisplay(lookup));
        //groupings
        Object debuggerTypeProxy = DebugView.getDebuggerTypeProxy(lookup);
        assertIsType(Object[].class, debuggerTypeProxy);
        Object[] groupings = (Object[]) debuggerTypeProxy;
        assertEquals(1, groupings.length);
        assertAll(Linq.of(groupings).zip(lookup, (l, r) -> Tuple.create(l, r)), tuple -> assertSame(tuple.getItem1(), tuple.getItem2()));
        assertSame(groupings, DebugView.getDebuggerTypeProxy(lookup)); // The result should be cached, as Lookup is immutable.
    }
}
