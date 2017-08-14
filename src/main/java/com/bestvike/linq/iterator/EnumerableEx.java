package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.enumerable.ArrayEnumerable;
import com.bestvike.linq.enumerable.CharSequenceEnumerable;
import com.bestvike.linq.enumerable.CollectionEnumerable;
import com.bestvike.linq.enumerable.IterableEnumerable;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.enumerable.SingletonEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.util.Array;
import com.bestvike.linq.util.Comparer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by 许崇雷 on 2017/7/21.
 */
@SuppressWarnings("Duplicates")
public final class EnumerableEx {
    private EnumerableEx() {
    }

    public static IEnumerable<Boolean> asEnumerable(boolean[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Byte> asEnumerable(byte[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Short> asEnumerable(short[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Integer> asEnumerable(int[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Long> asEnumerable(long[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Float> asEnumerable(float[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Double> asEnumerable(double[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static IEnumerable<Character> asEnumerable(char[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(TSource[] source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ArrayEnumerable<>(Array.create(source));
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(List<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ListEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Collection<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CollectionEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(Iterable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new IterableEnumerable<>(source);
    }

    public static IEnumerable<Character> asEnumerable(CharSequence source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CharSequenceEnumerable(source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> asEnumerable(Map<TKey, TValue> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new CollectionEnumerable<>(source.entrySet());
    }

    public static <TSource> IEnumerable<TSource> singletonEnumerable(TSource item) {
        return new SingletonEnumerable<>(item);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new LeftJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new RightJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, null, null, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TOuter defaultOuter, TInner defaultInner, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new FullJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, comparer);
    }

    public static <TOuter, TInner, TResult> IEnumerable<TResult> crossJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new CrossJoinIterator<>(outer, inner, resultSelector);
    }

    public static <TSource, TKey> IEnumerable<TSource> distinctBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new DistinctByIterator<>(source, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> distinctBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new DistinctByIterator<>(source, keySelector, comparer);
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new UnionByIterator<>(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new UnionByIterator<>(first, second, keySelector, comparer);
    }

    public static <TSource, TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new IntersectByIterator<>(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new IntersectByIterator<>(first, second, keySelector, comparer);
    }

    public static <TSource, TKey> IEnumerable<TSource> exceptBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new ExceptByIterator<>(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> exceptBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        return new ExceptByIterator<>(first, second, keySelector, comparer);
    }

    public static <TSource> TSource minByInt(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        int key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Integer curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey < key) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minByIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Integer key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Integer curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey < key) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource minByLong(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        long key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Long curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey < key) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minByLongNull(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Long key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Long curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey < key) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource minByFloat(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        float key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Float curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                // Normally NaN < anything is false, as is anything < NaN
                // However,  leads to some irksome outcomes : Min and Max.
                // If we use those semantics then Min(NaN, 5.0) is NaN, but
                // Min(5.0, NaN) is 5.0!  To fix , we impose a total
                // ordering where NaN is smaller than every value, including
                // negative infinity.
                if (curKey < key || Float.isNaN(curKey)) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minByFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Float key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Float curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey < key || Float.isNaN(curKey)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource minByDouble(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        double key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Double curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey < key || Double.isNaN(curKey)) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minByDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Double key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Double curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey < key || Double.isNaN(curKey)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource minByDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        BigDecimal key = BigDecimal.ZERO;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            BigDecimal curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey.compareTo(key) < 0) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minByDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        BigDecimal key = null;
        TSource value = null;
        for (TSource curValue : source) {
            BigDecimal curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey.compareTo(key) < 0) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource, TKey> TSource minBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Comparator<TKey> comparer = Comparer.Default();
        TKey key = null;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            TKey curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (comparer.compare(curKey, key) < 0) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource, TKey> TSource minByNull(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Comparator<TKey> comparer = Comparer.Default();
        TKey key = null;
        TSource value = null;
        for (TSource curValue : source) {
            TKey curKey = keySelector.apply(curValue);
            if (curKey != null && (key == null || comparer.compare(curKey, key) < 0)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource maxByInt(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        int key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Integer curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey > key) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxByIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Integer key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Integer curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey > key) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource maxByLong(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        long key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Long curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey > key) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxByLongNull(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Long key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Long curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey > key) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource maxByFloat(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        float key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Float curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey > key || Float.isNaN(key)) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxByFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Float key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Float curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey > key || Float.isNaN(key)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource maxByDouble(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        double key = 0;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            Double curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey > key || Double.isNaN(key)) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxByDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Double key = null;
        TSource value = null;
        for (TSource curValue : source) {
            Double curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey > key || Double.isNaN(key)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> TSource maxByDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        BigDecimal key = BigDecimal.ZERO;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            BigDecimal curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (curKey.compareTo(key) > 0) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxByDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        BigDecimal key = null;
        TSource value = null;
        for (TSource curValue : source) {
            BigDecimal curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (key == null || curKey.compareTo(key) > 0) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource, TKey> TSource maxBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Comparator<TKey> comparer = Comparer.Default();
        TKey key = null;
        TSource value = null;
        boolean hasValue = false;
        for (TSource curValue : source) {
            TKey curKey = keySelector.apply(curValue);
            if (curKey == null) continue;
            if (hasValue) {
                if (comparer.compare(curKey, key) > 0) {
                    key = curKey;
                    value = curValue;
                }
            } else {
                key = curKey;
                value = curValue;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource, TKey> TSource maxByNull(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        Comparator<TKey> comparer = Comparer.Default();
        TKey key = null;
        TSource value = null;
        for (TSource curValue : source) {
            TKey curKey = keySelector.apply(curValue);
            if (curKey != null && (key == null || comparer.compare(curKey, key) > 0)) {
                key = curKey;
                value = curValue;
            }
        }
        return value;
    }

    public static <TSource> IEnumerable<TSource> append(IEnumerable<TSource> source, TSource item) {
        if (source == null) throw Errors.argumentNull("source");
        return new AppendIterator<>(source, item);
    }
}
