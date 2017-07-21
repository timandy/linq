package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func2;

/**
 * @author 许崇雷
 * @date 2017/7/16
 */
public class SelectManyIterator4<TSource, TCollection, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector;
    private final Func2<TSource, TCollection, TResult> resultSelector;
    private IEnumerator<TSource> enumerator;
    private IEnumerator<TCollection> enumerator2;
    private TSource cursor;
    private int index;


    public SelectManyIterator4(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        this.source = source;
        this.collectionSelector = collectionSelector;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectManyIterator4<>(this.source, this.collectionSelector, this.resultSelector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.index = -1;
                    this.enumerator = this.source.enumerator();
                    this.state = 2;
                case 2:
                    if (this.enumerator.moveNext()) {
                        this.cursor = this.enumerator.current();
                        this.index = Math.addExact(this.index, 1);
                        this.enumerator2 = this.collectionSelector.apply(this.cursor, this.index).enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.enumerator2.moveNext()) {
                        TCollection item = this.enumerator2.current();
                        this.current = this.resultSelector.apply(this.cursor, item);
                        return true;
                    }
                    this.enumerator2.close();
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
        if (this.enumerator2 != null) {
            this.enumerator2.close();
            this.enumerator2 = null;
        }
        this.cursor = null;
        super.close();
    }
}
