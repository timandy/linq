package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;

/**
 * Created by 许崇雷 on 2017/7/16.
 */
final class SelectManyIterator3<TSource, TCollection, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, IEnumerable<TCollection>> collectionSelector;
    private final Func2<TSource, TCollection, TResult> resultSelector;
    private IEnumerator<TSource> enumerator;
    private IEnumerator<TCollection> selectorEnumerator;
    private TSource cursor;

    public SelectManyIterator3(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
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
                        this.cursor = this.enumerator.current();
                        this.selectorEnumerator = this.collectionSelector.apply(this.cursor).enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.selectorEnumerator.moveNext()) {
                        TCollection item = this.selectorEnumerator.current();
                        this.current = this.resultSelector.apply(this.cursor, item);
                        return true;
                    }
                    this.selectorEnumerator.close();
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
        if (this.selectorEnumerator != null) {
            this.selectorEnumerator.close();
            this.selectorEnumerator = null;
        }
        this.cursor = null;
        super.close();
    }
}
