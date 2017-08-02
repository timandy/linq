package com.bestvike.linq.impl;

import com.bestvike.linq.util.Array;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public abstract class AbstractEnumerableSorter<TElement> {
    protected abstract void computeKeys(Array<TElement> elements, int count);

    protected abstract int compareKeys(int index1, int index2);

    protected int[] sort(Array<TElement> elements, int count) {
        this.computeKeys(elements, count);
        int[] map = new int[count];
        for (int i = 0; i < count; i++)
            map[i] = i;
        this.quickSort(map, 0, count - 1);
        return map;
    }

    protected void quickSort(int[] map, int left, int right) {
        do {
            int i = left;
            int j = right;
            int x = map[i + ((j - i) >> 1)];
            do {
                while (i < map.length && this.compareKeys(x, map[i]) > 0)
                    i++;
                while (j >= 0 && this.compareKeys(x, map[j]) < 0)
                    j--;
                if (i > j)
                    break;
                if (i < j) {
                    int temp = map[i];
                    map[i] = map[j];
                    map[j] = temp;
                }
                i++;
                j--;
            } while (i <= j);
            if (j - left <= right - i) {
                if (left < j)
                    this.quickSort(map, left, j);
                left = i;
            } else {
                if (i < right)
                    this.quickSort(map, i, right);
                right = j;
            }
        } while (left < right);
    }
}