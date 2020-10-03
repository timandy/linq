package com.bestvike.linq.util.lang;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
public interface IStringSpan extends CharSequence {
    CharSequence raw();

    int offset();

    IStringSpan trim();

    IStringSpan subSequence(int start);

    @Override
    IStringSpan subSequence(int start, int end);
}
