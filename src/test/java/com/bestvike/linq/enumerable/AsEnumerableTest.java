package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
class AsEnumerableTest extends TestCase {
    @Test
    void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.of(9999, 0, 888, -1, 66, -777, 1, 2, -12345)
                .where(x -> x > Integer.MIN_VALUE);

        assertEquals(q.asEnumerable(), q.asEnumerable());
    }

    @Test
    void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.of("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        assertEquals(q.asEnumerable(), q.asEnumerable());
    }

    @Test
    void NullSourceAllowed() {
        IEnumerable<Integer> source = null;

        //not same to C#
        assertThrows(NullPointerException.class, () -> source.asEnumerable());
    }

    @Test
    void OneElement() {
        IEnumerable<Integer> source = Linq.singleton(2);

        assertEquals(source, source.asEnumerable());
    }

    @Test
    void SomeElements() {
        IEnumerable<Integer> source = Linq.of(-5, 0, 1, -4, 3, null, 10);

        assertEquals(source, source.asEnumerable());
    }

    @Test
    void SomeElementsRunOnce() {
        IEnumerable<Integer> source = Linq.of(-5, 0, 1, -4, 3, null, 10);

        assertEquals(source, source.runOnce().asEnumerable());
    }

    @Test
    void testAsEnumerable() {
        IEnumerable<String> as = Linq.as(new String[]{"!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty});
        IEnumerable<String> q = as.where(x -> !IsNullOrEmpty(x));

        assertEquals(q.asEnumerable(), q.asEnumerable());

        assertIsType(CharSequence[].class, q.<CharSequence>asEnumerable().toArray(CharSequence.class));
        assertEquals(q.asEnumerable(), Linq.of(q.<CharSequence>asEnumerable().toArray(CharSequence.class)));

        assertThrows(ArrayStoreException.class, () -> q.<Integer>asEnumerable().toArray(Integer.class));
    }
}
