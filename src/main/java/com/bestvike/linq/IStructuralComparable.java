package com.bestvike.linq;

import java.util.Comparator;

/**
 * @author 许崇雷
 * @date 2017/7/23
 */
public interface IStructuralComparable {
    int compareTo(Object other, Comparator comparer);
}
