package com.bestvike.linq;

import com.bestvike.function.Func0;
import com.bestvike.function.Func1;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.enumerable.Enumerable;
import com.bestvike.linq.enumerable.Enumerate;
import com.bestvike.linq.enumerable.Generate;
import com.bestvike.linq.enumerable.Range;
import com.bestvike.linq.enumerable.Repeat;
import com.bestvike.linq.enumerable.Split;
import com.bestvike.linq.util.StringSplitOptions;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2017-07-18.
 */
@SuppressWarnings("unchecked")
public final class Linq {
    private Linq() {
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Enumerable.empty();
    }

    public static <TSource> IEnumerable<TSource> singleton(TSource item) {
        return Enumerable.singleton(item);
    }

    public static <TSource> IEnumerable<TSource> ofNullable(TSource item) {
        return Enumerable.ofNullable(item);
    }

    public static IEnumerable<Boolean> of(boolean[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Byte> of(byte[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Short> of(short[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Integer> of(int[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Long> of(long[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Float> of(float[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Double> of(double[] source) {
        return Enumerable.of(source);
    }

    public static IEnumerable<Character> of(char[] source) {
        return Enumerable.of(source);
    }

    public static <TSource> IEnumerable<TSource> of(TSource... source) {
        return Enumerable.of(source);
    }

    public static <TSource> IEnumerable<TSource> of(IEnumerable<? extends TSource> source) {
        return Enumerable.of((IEnumerable<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(List<? extends TSource> source) {
        return Enumerable.of((List<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Collection<? extends TSource> source) {
        return Enumerable.of((Collection<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterable<? extends TSource> source) {
        return Enumerable.of((Iterable<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Iterator<? extends TSource> source) {
        return Enumerable.of((Iterator<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Enumeration<? extends TSource> source) {
        return Enumerable.of((Enumeration<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Stream<? extends TSource> source) {
        return Enumerable.of((Stream<TSource>) source);
    }

    public static <TSource> IEnumerable<TSource> of(Spliterator<? extends TSource> source) {
        return Enumerable.of((Spliterator<TSource>) source);
    }

    public static <TKey, TValue> IEnumerable<Map.Entry<TKey, TValue>> of(Map<? extends TKey, ? extends TValue> source) {
        return Enumerable.of((Map<TKey, TValue>) source);
    }

    public static <TSource> IEnumerable<TSource> as(Object source) {
        return Enumerable.as(source);
    }

    public static IEnumerable<Character> chars(CharSequence source) {
        return Enumerable.chars(source);
    }

    public static IEnumerable<String> words(String source) {
        return Enumerable.words(source);
    }

    public static IEnumerable<String> lines(String source) {
        return Enumerable.lines(source);
    }

    public static IEnumerable<String> split(CharSequence source, char separator) {
        return Split.split(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, char separator, StringSplitOptions options) {
        return Split.split(source, separator, options);
    }

    public static IEnumerable<String> split(CharSequence source, char... separator) {
        return Split.split(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, char[] separator, StringSplitOptions options) {
        return Split.split(source, separator, options);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence separator) {
        return Split.split(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence separator, StringSplitOptions options) {
        return Split.split(source, separator, options);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence[] separator) {
        return Split.split(source, separator);
    }

    public static IEnumerable<String> split(CharSequence source, CharSequence[] separator, StringSplitOptions options) {
        return Split.split(source, separator, options);
    }

    public static <TSource> IEnumerable<TSource> enumerate(TSource seed, Func1<? super TSource, ? extends TSource> next) {
        return Enumerate.enumerate(seed, (Func1<TSource, TSource>) next);
    }

    public static <TSource> IEnumerable<TSource> enumerate(TSource seed, Predicate1<? super TSource> condition, Func1<? super TSource, ? extends TSource> next) {
        return Enumerate.enumerate(seed, (Predicate1<TSource>) condition, (Func1<TSource, TSource>) next);
    }

    public static <TSource> IEnumerable<TSource> generate(TSource item) {
        return Generate.generate(item);
    }

    public static <TSource> IEnumerable<TSource> generate(Func0<? extends TSource> func) {
        return Generate.generate((Func0<TSource>) func);
    }

    public static IEnumerable<Integer> range(int start, int count) {
        return Range.range(start, count);
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        return Repeat.repeat(element, count);
    }
}
