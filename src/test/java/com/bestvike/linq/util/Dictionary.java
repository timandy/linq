package com.bestvike.linq.util;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by 许崇雷 on 2019-05-09.
 */
public final class Dictionary<K, V> extends AbstractMap<K, V> {
    private final IEqualityComparer<K> comparer;
    private final Set<Map.Entry<K, V>> entrySet = new LinkedHashSet<>();

    public Dictionary() {
        this(null);
    }

    public Dictionary(IEqualityComparer<K> comparer) {
        if (comparer == null)
            comparer = EqualityComparer.Default();
        this.comparer = comparer;
    }

    private Map.Entry<K, V> findEntry(Object key) {
        for (Map.Entry<K, V> entry : this.entrySet) {
            if (this.comparer.equals(entry.getKey(), (K) key))
                return entry;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        final Map.Entry<K, V> entry = this.findEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V put(K key, V value) {
        Map.Entry<K, V> entry = this.findEntry(key);
        if (entry == null) {
            entry = new Entry<>(key, value);
            this.entrySet.add(entry);
            return null;
        }
        final V oldValue = entry.getValue();
        entry.setValue(value);
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        final Map.Entry<K, V> entry = this.findEntry(key);
        if (entry == null)
            return null;
        this.entrySet.remove(entry);
        return entry.getValue();
    }

    public boolean tryGetValue(Object key, out<V> value) {
        final Map.Entry<K, V> entry = this.findEntry(key);
        if (entry == null) {
            value.value = null;
            return false;
        }
        value.value = entry.getValue();
        return true;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.entrySet;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        final Map.Entry<K, V> entry = this.findEntry(key);
        return entry == null ? defaultValue : entry.getValue();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : this.entrySet) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        ThrowHelper.throwNotSupportedException();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }

    @Override
    public boolean remove(Object key, Object value) {
        ThrowHelper.throwNotSupportedException();
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        ThrowHelper.throwNotSupportedException();
        return false;
    }

    @Override
    public V replace(K key, V value) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        ThrowHelper.throwNotSupportedException();
        return null;
    }


    private static final class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }
    }
}
