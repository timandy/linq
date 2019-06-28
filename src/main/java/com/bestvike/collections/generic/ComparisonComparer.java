package com.bestvike.collections.generic;

import com.bestvike.IComparison;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-04-21.
 */
final class ComparisonComparer<T> implements Comparator<T> {
    private final IComparison<? super T> comparison;

    ComparisonComparer(IComparison<? super T> comparison) {
        this.comparison = comparison;
    }

    @Override
    public int compare(T x, T y) {
        return this.comparison.compare(x, y);
    }
}
