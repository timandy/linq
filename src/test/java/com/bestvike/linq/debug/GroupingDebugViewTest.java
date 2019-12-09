package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.Linq;
import com.bestvike.linq.util.ArgsList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
class GroupingDebugViewTest extends TestCase {
    private static IEnumerable<Object[]> DebuggerAttributesValid_Data() {
        ArgsList argsList = new ArgsList();
        IEnumerable<Integer> source = Linq.of(new int[]{1});
        argsList.add(source.groupBy(i -> i).single(), "1");
        argsList.add(source.groupBy(i -> i.toString(), i -> i).single(), "\"1\"");
        argsList.add(source.groupBy(i -> Duration.ofSeconds(i), i -> i).single(), "{PT1S}");
        argsList.add(Linq.of(new String[]{null}).groupBy(x -> x).single(), "null");
        argsList.add(Linq.of(new Integer[]{null}).groupBy(x -> x).single(), "null");
        return argsList;
    }

    @ParameterizedTest
    @MethodSource("DebuggerAttributesValid_Data")
    <TKey, TElement> void DebuggerAttributesValid(IGrouping<TKey, TElement> grouping, String keyString) {
        //key
        assertEquals("Key = " + keyString, DebugView.getDebuggerDisplayText(grouping));
        //values
        Object proxyObject = DebugView.getDebuggerProxyObject(grouping);
        assertIsType(Object[].class, proxyObject);
        Object[] values = (Object[]) proxyObject;
        assertEquals(grouping.toArray(), Linq.of(values));
        assertSame(proxyObject, DebugView.getDebuggerProxyObject(grouping));
    }
}
