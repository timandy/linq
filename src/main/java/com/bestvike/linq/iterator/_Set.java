package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class _Set {
    private _Set() {
    }
}

final class Set<TElement> {
    private final IEqualityComparer<TElement> comparer;
    private int[] buckets;
    private Array<Slot> slots;
    private int count;

    // Constructs a set that compares items with the specified comparer.
    public Set(IEqualityComparer<TElement> comparer) {
        this.comparer = comparer == null ? EqualityComparer.Default() : comparer;
        this.buckets = new int[7];
        this.slots = Array.create(7);
        for (int i = 0; i < 7; i++)
            this.slots.set(i, new Slot());
    }

    // If value is not in set, add it and return true; otherwise return false
    public boolean add(TElement value) {
        int hashCode = this.internalGetHashCode(value);
        for (int i = this.buckets[hashCode % this.buckets.length] - 1; i >= 0; i = this.slots.get(i).next) {
            if (this.slots.get(i).hashCode == hashCode && this.comparer.equals(this.slots.get(i).value, value)) {
                return false;
            }
        }

        if (this.count == this.slots.length()) {
            this.resize();
        }

        int index = this.count;
        this.count++;
        int bucket = hashCode % this.buckets.length;
        this.slots.get(index).hashCode = hashCode;
        this.slots.get(index).value = value;
        this.slots.get(index).next = this.buckets[bucket] - 1;
        this.buckets[bucket] = index + 1;
        return true;
    }

    // Attempts to remove an item from this set.
    public boolean remove(TElement value) {
        int hashCode = this.internalGetHashCode(value);
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
                this.slots.get(i).next = -1;
                return true;
            }
        }

        return false;
    }

    // Expands the capacity of this set to double the current capacity, plus one.
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

    // Creates an array from the items in this set.
    public TElement[] toArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, this.count);
        for (int i = 0; i != array.length; ++i)
            array[i] = this.slots.get(i).value;
        return array;
    }

    // Creates an array from the items in this set.
    public Array<TElement> toArray() {
        Array<TElement> array = Array.create(this.count);
        for (int i = 0; i != array.length(); ++i)
            array.set(i, this.slots.get(i).value);
        return array;
    }

    // Creates a list from the items in this set.
    public List<TElement> toList() {
        int count = this.count;
        List<TElement> list = new ArrayList<TElement>(count);
        for (int i = 0; i != count; ++i)
            list.add(this.slots.get(i).value);
        return list;
    }

    // The number of items in this set.
    public int getCount() {
        return this.count;
    }

    // Unions this set with an enumerable.
    public void unionWith(IEnumerable<TElement> other) {
        assert other != null;

        for (TElement item : other)
            this.add(item);
    }

    // Gets the hash code of the provided value with its sign bit zeroed out, so that modulo has a positive result.
    private int internalGetHashCode(TElement value) {
        return (value == null) ? 0 : this.comparer.hashCode(value) & 0x7FFFFFFF;
    }

    // An entry in the hash set.
    private final class Slot {//struct
        private int hashCode;
        private int next;
        private TElement value;
    }
}
