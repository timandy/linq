package com.bestvike.collections.generic;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.out;
import org.junit.jupiter.api.Test;

/**
 * Created by 许崇雷 on 2023-10-10.
 */
class QueueTest extends TestCase {
    @Test
    void size() {
        Queue<Integer> queue = new Queue<>();
        assertEquals(0, queue.size());
        queue.enqueue(null);
        assertEquals(1, queue.size());
        queue.enqueue(5);
        assertEquals(2, queue.size());
    }

    @Test
    void isEmpty() {
        Queue<Integer> queue = new Queue<>(5);
        assertTrue(queue.isEmpty());
        queue.enqueue(null);
        assertFalse(queue.isEmpty());
    }

    @Test
    void clear() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(null);
        assertFalse(queue.isEmpty());
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test
    void enqueue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(null);
        assertEquals(3, queue.size());
    }

    @Test
    void dequeue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(null);
        assertEquals(1, queue.dequeue());
        assertNull(queue.dequeue());
        assertThrows(InvalidOperationException.class, queue::dequeue);
    }

    @Test
    void tryDequeue() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(null);
        //
        out<Integer> result = out.init();
        assertTrue(queue.tryDequeue(result));
        assertEquals(1, result.value);
        assertTrue(queue.tryDequeue(result));
        assertNull(result.value);
        assertFalse(queue.tryDequeue(result));
        assertNull(result.value);
    }

    @Test
    void peek() {
        Queue<Integer> queue = new Queue<>();
        assertThrows(InvalidOperationException.class, queue::peek);
        queue.enqueue(1);
        queue.enqueue(null);
        assertEquals(1, queue.peek());
        assertEquals(2, queue.size());
        queue.clear();
        assertTrue(queue.isEmpty());
        queue.enqueue(null);
        queue.enqueue(1);
        assertEquals(null, queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    void tryPeek() {
        Queue<Integer> queue = new Queue<>();
        out<Integer> result = out.init();
        assertFalse(queue.tryPeek(result));
        assertNull(result.value);
        queue.enqueue(1);
        queue.enqueue(null);
        assertTrue(queue.tryPeek(result));
        assertEquals(1, result.value);
        assertEquals(2, queue.size());
        queue.clear();
        assertTrue(queue.isEmpty());
        queue.enqueue(null);
        queue.enqueue(1);
        assertTrue(queue.tryPeek(result));
        assertNull(result.value);
        assertEquals(2, queue.size());
    }

    @Test
    void contains() {
        Queue<Integer> queue = new Queue<>();
        assertFalse(queue.contains(null));
        queue.enqueue(null);
        assertTrue(queue.contains(null));
    }
}