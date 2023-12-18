package com.bestvike;

import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 许崇雷 on 2023-05-31.
 */
class IndexTest extends TestCase {
    @Test
    void CreationTest() {
        Index index = new Index(1, false);
        assertEquals(1, index.getValue());
        assertFalse(index.isFromEnd());

        index = new Index(11, true);
        assertEquals(11, index.getValue());
        assertTrue(index.isFromEnd());

        index = Index.Start;
        assertEquals(0, index.getValue());
        assertFalse(index.isFromEnd());

        index = Index.End;
        assertEquals(0, index.getValue());
        assertTrue(index.isFromEnd());

        index = Index.fromStart(3);
        assertEquals(3, index.getValue());
        assertFalse(index.isFromEnd());

        index = Index.fromEnd(10);
        assertEquals(10, index.getValue());
        assertTrue(index.isFromEnd());

        assertThrows(ArgumentOutOfRangeException.class, () -> {
            new Index(-1, false);
        });
        assertThrows(ArgumentOutOfRangeException.class, () -> {
            Index.fromStart(-3);
        });
        assertThrows(ArgumentOutOfRangeException.class, () -> {
            Index.fromEnd(-1);
        });
    }

    @Test
    void GetOffsetTest() {
        Index index = Index.fromStart(3);
        assertEquals(3, index.getOffset(3));
        assertEquals(3, index.getOffset(10));
        assertEquals(3, index.getOffset(20));

        // we don't validate the length in the getOffset so passing short length will just return the regular calculation according to the length value.
        assertEquals(3, index.getOffset(2));

        index = Index.fromEnd(3);
        assertEquals(0, index.getOffset(3));
        assertEquals(7, index.getOffset(10));
        assertEquals(17, index.getOffset(20));

        // we don't validate the length in the getOffset so passing short length will just return the regular calculation according to the length value.
        assertEquals(-1, index.getOffset(2));
    }

    @Test
    void EqualityTest() {
        Index index1 = Index.fromStart(10);
        Index index2 = Index.fromStart(10);
        assertTrue(index1.equals(index2));

        index2 = new Index(10, true);
        assertFalse(index1.equals(index2));

        index2 = new Index(9, false);
        assertFalse(index1.equals(index2));
    }

    @Test
    void HashCodeTest() {
        Index index1 = Index.fromStart(10);
        Index index2 = Index.fromStart(10);
        assertEquals(index1.hashCode(), index2.hashCode());

        index2 = new Index(10, true);
        assertNotEquals(index1.hashCode(), index2.hashCode());

        index2 = new Index(99999, false);
        assertNotEquals(index1.hashCode(), index2.hashCode());
    }

    @Test
    void CloneTest() {
        Index index1 = Index.fromStart(10);
        assertEquals(index1, index1.clone());
    }

    @Test
    void ToStringTest() {
        Index index1 = Index.fromStart(100);
        assertEquals(new Index(100, false).toString(), index1.toString());

        index1 = new Index(50, true);
        assertEquals("^50", index1.toString());
    }

    @Test
    void CollectionTest() {
        Integer[] array = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> list = Arrays.asList(array);

        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, list.get(Index.fromStart(i).getValue()));
            //
            int offset = Index.fromEnd(i + 1).getOffset(list.size());
            assertEquals(list.size() - i - 1, list.get(offset));
            //
            assertEquals(i, list.get(Index.fromStart(i).getValue()));
            assertEquals(list.size() - i - 1, array[offset]);
        }
    }
}
