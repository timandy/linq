//package com.bestvike.linq.iterator;
//
//import com.bestvike.linq.IEnumerable;
//import com.bestvike.linq.function.Func1;
//
///**
// * @author 许崇雷
// * @date 2017/7/10
// */
//public class ArrayIterator<TSource> extends WhereIterator<TSource> {
//    protected TSource[] source;
//    protected int index;
//    protected Func1<TSource, Boolean> predicate;
//
//    public ArrayIterator(TSource[] source, Func1<TSource, Boolean> predicate) {
//        this.source = source;
//        this.predicate = predicate;
//    }
//
//    @Override
//    public WhereIterator<TSource> clone() {
//        return new ArrayIterator<>(this.source, this.predicate);
//    }
//
//    @Override
//    public <TResult> IEnumerable<TResult> select(Func1<TSource, TResult> selector) {
//        return null;
//    }
//
//    @Override
//    public IEnumerable<TSource> where(Func1<TSource, Boolean> predicate) {
//        return null;
//    }
//
//    @Override
//    public boolean moveNext() {
//        if (this.state == 1) {
//            while (this.index < this.source.length) {
//                TSource item = this.source[this.index++];
//                if (this.predicate.apply(item)) {
//                    this.current = item;
//                    return true;
//                }
//            }
//            this.close();
//        }
//        return false;
//    }
//}
