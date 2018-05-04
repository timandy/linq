package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * Created by 许崇雷 on 2017/7/16.
 */
final class SelectManyIterator3<TSource, TCollection, TResult> extends Iterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, IEnumerable<TCollection>> collectionSelector;
    private final Func2<TSource, TCollection, TResult> resultSelector;
    private IEnumerator<TSource> enumerator;
    private IEnumerator<TCollection> subEnumerator;
    private TSource element;

    SelectManyIterator3(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        assert source != null;
        assert collectionSelector != null;
        assert resultSelector != null;

        this.source = source;
        this.collectionSelector = collectionSelector;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectManyIterator3<>(this.source, this.collectionSelector, this.resultSelector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.enumerator = this.source.enumerator();
                    this.state = 2;
                case 2:
                    if (this.enumerator.moveNext()) {
                        this.element = this.enumerator.current();
                        this.subEnumerator = this.collectionSelector.apply(this.element).enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.subEnumerator.moveNext()) {
                        TCollection item = this.subEnumerator.current();
                        this.current = this.resultSelector.apply(this.element, item);
                        return true;
                    }
                    this.subEnumerator.close();
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        if (this.subEnumerator != null) {
            this.subEnumerator.close();
            this.subEnumerator = null;
        }
        this.element = null;
        super.close();
    }
}
