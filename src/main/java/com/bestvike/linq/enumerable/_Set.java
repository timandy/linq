package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class Set<TElement> {
    private final IEqualityComparer<TElement> comparer;
    private int[] buckets;
    private Slot[] slots;
    private int count;

    // Constructs a set that compares items with the specified comparer.
    Set(IEqualityComparer<TElement> comparer) {
        this.comparer = comparer == null ? EqualityComparer.Default() : comparer;
        this.buckets = new int[7];
        //noinspection unchecked
        this.slots = new Set.Slot[7];
        for (int i = 0; i < 7; i++)
            this.slots[i] = new Slot();
    }

    // If value is not in set, add it and return true; otherwise return false
    public boolean add(TElement value) {
        int hashCode = this.internalGetHashCode(value);
        for (int i = this.buckets[hashCode % this.buckets.length] - 1; i >= 0; i = this.slots[i].next) {
            if (this.slots[i].hashCode == hashCode && this.comparer.equals(this.slots[i].value, value))
                return false;
        }

        if (this.count == this.slots.length)
            this.resize();

        int index = this.count;
        this.count++;
        int bucket = hashCode % this.buckets.length;
        this.slots[index].hashCode = hashCode;
        this.slots[index].value = value;
        this.slots[index].next = this.buckets[bucket] - 1;
        this.buckets[bucket] = index + 1;
        return true;
    }

    // Attempts to remove an item from this set.
    public boolean remove(TElement value) {
        int hashCode = this.internalGetHashCode(value);
        int bucket = hashCode % this.buckets.length;
        int last = -1;
        for (int i = this.buckets[bucket] - 1; i >= 0; last = i, i = this.slots[i].next) {
            if (this.slots[i].hashCode == hashCode && this.comparer.equals(this.slots[i].value, value)) {
                if (last < 0)
                    this.buckets[bucket] = this.slots[i].next + 1;
                else
                    this.slots[last].next = this.slots[i].next;

                this.slots[i].hashCode = -1;
                this.slots[i].value = null;
                this.slots[i].next = -1;
                return true;
            }
        }

        return false;
    }

    // Expands the capacity of this set to double the current capacity, plus one.
    private void resize() {
        int newSize = Math.addExact(Math.multiplyExact(this.count, 2), 1);
        int[] newBuckets = new int[newSize];
        //noinspection unchecked
        Slot[] newSlots = new Set.Slot[newSize];
        System.arraycopy(this.slots, 0, newSlots, 0, this.count);
        for (int i = this.count; i < newSize; i++)
            newSlots[i] = new Slot();
        for (int i = 0; i < this.count; i++) {
            int bucket = newSlots[i].hashCode % newSize;
            newSlots[i].next = newBuckets[bucket] - 1;
            newBuckets[bucket] = i + 1;
        }
        this.buckets = newBuckets;
        this.slots = newSlots;
    }

    // Creates an array from the items in this set.
    public TElement[] toArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, this.count);
        for (int i = 0; i != array.length; ++i)
            array[i] = this.slots[i].value;
        return array;
    }

    // Creates an array from the items in this set.
    public Object[] toArray() {
        Object[] array = new Object[this.count];
        for (int i = 0; i != array.length; ++i)
            array[i] = this.slots[i].value;
        return array;
    }

    // Creates a list from the items in this set.
    public List<TElement> toList() {
        int count = this.count;
        List<TElement> list = new ArrayList<>(count);
        for (int i = 0; i != count; ++i)
            list.add(this.slots[i].value);
        return list;
    }

    // The number of items in this set.
    public int getCount() {
        return this.count;
    }

    // Unions this set with an enumerable.
    public void unionWith(IEnumerable<TElement> other) {
        assert other != null;

        try (IEnumerator<TElement> e = other.enumerator()) {
            while (e.moveNext())
                this.add(e.current());
        }
    }

    // Unions this set with an enumerable and selector.
    public <TSource> void unionWith(IEnumerable<TSource> other, Func1<TSource, TElement> selector) {
        assert other != null;
        assert selector != null;

        try (IEnumerator<TSource> e = other.enumerator()) {
            while (e.moveNext())
                this.add(selector.apply(e.current()));
        }
    }

    // Gets the hash code of the provided value with its sign bit zeroed out, so that modulo has a positive result.
    private int internalGetHashCode(TElement value) {
        return value == null ? 0 : this.comparer.hashCode(value) & 0x7FFFFFFF;
    }


    // An entry in the hash set.
    private final class Slot {//struct
        private int hashCode;
        private int next;
        private TElement value;
    }
}
