package com.bestvike.linq.impl.order;

import com.bestvike.collections.generic.Array;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public abstract class AbstractEnumerableSorter<TElement> {
    protected abstract void computeKeys(Array<TElement> elements, int count);

    protected abstract int compareAnyKeys(int index1, int index2);

    private Integer[] computeMap(Array<TElement> elements, int count) {
        this.computeKeys(elements, count);
        Integer[] map = new Integer[count];
        for (int i = 0; i < map.length; i++)
            map[i] = i;
        return map;
    }

    protected Integer[] sort(Array<TElement> elements, int count) {
        Integer[] map = this.computeMap(elements, count);
        this.quickSort(map, 0, count - 1);
        return map;
    }

    protected Integer[] sort(Array<TElement> elements, int count, int minIdx, int maxIdx) {
        Integer[] map = this.computeMap(elements, count);
        this.partialQuickSort(map, 0, count - 1, minIdx, maxIdx);
        return map;
    }

    protected TElement elementAt(Array<TElement> elements, int count, int idx) {
        return elements.get(this.quickSelect(this.computeMap(elements, count), count - 1, idx));
    }

    protected abstract void quickSort(Integer[] map, int left, int right);

    // Sorts the k elements between minIdx and maxIdx without sorting all elements
    // Time complexity: O(n + k log k) best and average case. O(n^2) worse case.
    protected abstract void partialQuickSort(Integer[] map, int left, int right, int minIdx, int maxIdx);

    // Finds the element that would be at idx if the collection was sorted.
    // Time complexity: O(n) best and average case. O(n^2) worse case.
    protected abstract int quickSelect(Integer[] map, int right, int idx);
}
