package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;

import java.util.NoSuchElementException;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
public final class Errors {
    private Errors() {
    }

    public static RuntimeException argumentNull(String paramName) {
        return new ArgumentNullException(paramName);
    }

    public static RuntimeException argumentOutOfRange(String paramName) {
        return new ArgumentOutOfRangeException(paramName);
    }

    public static RuntimeException moreThanOneElement() {
        return new InvalidOperationException(SR.MoreThanOneElement);
    }

    public static RuntimeException moreThanOneMatch() {
        return new InvalidOperationException(SR.MoreThanOneMatch);
    }

    public static RuntimeException noElements() {
        return new InvalidOperationException(SR.NoElements);
    }

    public static RuntimeException noMatch() {
        return new InvalidOperationException(SR.NoMatch);
    }

    public static RuntimeException notSupported() {
        return new NotSupportedException();
    }

    public static RuntimeException noSuchElement() {
        return new NoSuchElementException(SR.NoSuchElement);
    }

    public static RuntimeException indexOutOfRange() {
        return new IndexOutOfBoundsException(SR.Arg_IndexOutOfRangeException);
    }

    public static RuntimeException implementComparable() {
        return new ArgumentException(SR.Argument_ImplementComparable);
    }

    public static RuntimeException tupleIncorrectType(Class type, String paramName) {
        return new ArgumentException(String.format(SR.ArgumentException_TupleIncorrectType, type), paramName);
    }

    public static RuntimeException tupleLastArgumentNotATuple() {
        return new ArgumentException(SR.ArgumentException_TupleLastArgumentNotATuple);
    }
}
