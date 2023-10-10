package com.bestvike.collections.generic;

import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.resources.SR;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2023-10-10.
 */
public final class Queue<T> {
    private final List<T> list;

    public Queue() {
        this.list = new ArrayList<>();
    }

    public Queue(int capacity) {
        this.list = new ArrayList<>(capacity);
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public void clear() {
        this.list.clear();
    }

    public void enqueue(T item) {
        this.list.add(item);
    }

    public T dequeue() {
        if (this.list.isEmpty())
            this.throwForEmptyQueue();
        return this.list.remove(0);
    }

    public boolean tryDequeue(out<T> result) {
        if (this.list.isEmpty()) {
            result.value = null;
            return false;
        }
        result.value = this.list.remove(0);
        return true;
    }

    public T peek() {
        if (this.list.isEmpty())
            this.throwForEmptyQueue();
        return this.list.get(0);
    }

    public boolean tryPeek(out<T> result) {
        if (this.list.isEmpty()) {
            result.value = null;
            return false;
        }
        result.value = this.list.get(0);
        return true;
    }

    public boolean contains(T item) {
        return this.list.contains(item);
    }

    private void throwForEmptyQueue() {
        assert this.list.isEmpty();
        throw new InvalidOperationException(SR.InvalidOperation_EmptyQueue);
    }
}
