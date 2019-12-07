package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.Linq;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.tuple.Tuple;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

/**
 * Created by 许崇雷 on 2019-06-19.
 */
class LookupDebugViewTest extends TestCase {
    private static IEnumerable<Object[]> DebuggerAttributesValid_Data() {
        ArgsList argsList = new ArgsList();
        IEnumerable<Integer> source = Linq.of(new int[]{1});
        argsList.add(source.toLookup(i -> i));
        argsList.add(source.toLookup(i -> i.toString(), i -> i));
        argsList.add(source.toLookup(i -> Duration.ofSeconds(i), i -> i));
        argsList.add(Linq.of(new String[]{null}).toLookup(x -> x));
        argsList.add(Linq.of(new Integer[]{null}).toLookup(x -> x));
        return argsList;
    }

    @ParameterizedTest
    @MethodSource("DebuggerAttributesValid_Data")
    <TKey, TElement> void DebuggerAttributesValid(ILookup<TKey, TElement> lookup) {
        //count
        assertEquals("Count = " + lookup.getCount(), DebugView.getDebuggerDisplay(lookup));
        //groupings
        Object proxyObject = DebugView.getDebuggerProxyObject(lookup);
        assertIsType(Object[].class, proxyObject);
        Object[] groupings = (Object[]) proxyObject;
        assertEquals(1, groupings.length);
        assertAll(Linq.of(groupings).zip(lookup, (l, r) -> Tuple.create(l, r)), tuple -> assertSame(tuple.getItem1(), tuple.getItem2()));
        assertSame(groupings, DebugView.getDebuggerProxyObject(lookup)); // The result should be cached, as Lookup is immutable.
    }
}
