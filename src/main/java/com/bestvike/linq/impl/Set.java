package com.bestvike.linq.impl;

import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.EqualityComparer;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class Set<TElement> {
    private final IEqualityComparer<TElement> comparer;
    private int[] buckets;
    private Array<Slot> slots;
    private int count;
    private int freeList;

    public Set() {
        this(null);
    }

    public Set(IEqualityComparer<TElement> comparer) {
        if (comparer == null) comparer = EqualityComparer.Default();
        this.comparer = comparer;
        this.buckets = new int[7];
        this.slots = Array.create(7);
        for (int i = 0; i < 7; i++)
            this.slots.set(i, new Slot());
        this.freeList = -1;
    }

    // If value is not in set, add it and return true; otherwise return false
    public boolean add(TElement value) {
        return !this.find(value, true);
    }

    // Check whether value is in set
    public boolean contains(TElement value) {
        return this.find(value, false);
    }

    // If value is in set, remove it and return true; otherwise return false
    public boolean remove(TElement value) {
        int hashCode = this.hashCode(value);
        int bucket = hashCode % this.buckets.length;
        int last = -1;
        for (int i = this.buckets[bucket] - 1; i >= 0; last = i, i = this.slots.get(i).next) {
            if (this.slots.get(i).hashCode == hashCode && this.comparer.equals(this.slots.get(i).value, value)) {
                if (last < 0) {
                    this.buckets[bucket] = this.slots.get(i).next + 1;
                } else {
                    this.slots.get(last).next = this.slots.get(i).next;
                }
                this.slots.get(i).hashCode = -1;
                this.slots.get(i).value = null;
                this.slots.get(i).next = this.freeList;
                this.freeList = i;
                return true;
            }
        }
        return false;
    }

    private boolean find(TElement value, boolean add) {
        int hashCode = this.hashCode(value);
        for (int i = this.buckets[hashCode % this.buckets.length] - 1; i >= 0; i = this.slots.get(i).next) {
            if (this.slots.get(i).hashCode == hashCode && this.comparer.equals(this.slots.get(i).value, value))
                return true;
        }
        if (add) {
            int index;
            if (this.freeList >= 0) {
                index = this.freeList;
                this.freeList = this.slots.get(index).next;
            } else {
                if (this.count == this.slots.length())
                    this.resize();
                index = this.count;
                this.count++;
            }
            int bucket = hashCode % this.buckets.length;
            this.slots.get(index).hashCode = hashCode;
            this.slots.get(index).value = value;
            this.slots.get(index).next = this.buckets[bucket] - 1;
            this.buckets[bucket] = index + 1;
        }
        return false;
    }

    private void resize() {
        int newSize = Math.addExact(Math.multiplyExact(this.count, 2), 1);
        int[] newBuckets = new int[newSize];
        Array<Slot> newSlots = Array.create(newSize);
        Array.copy(this.slots, 0, newSlots, 0, this.count);
        for (int i = this.count; i < newSize; i++)
            newSlots.set(i, new Slot());
        for (int i = 0; i < this.count; i++) {
            int bucket = newSlots.get(i).hashCode % newSize;
            newSlots.get(i).next = newBuckets[bucket] - 1;
            newBuckets[bucket] = i + 1;
        }
        this.buckets = newBuckets;
        this.slots = newSlots;
    }

    private int hashCode(TElement value) {
        //Microsoft DevDivBugs 171937. work around comparer implementations that throw when passed null
        return (value == null) ? 0 : this.comparer.hashCode(value) & 0x7FFFFFFF;
    }

    private final class Slot {
        private int hashCode;
        private TElement value;
        private int next;
    }
}
