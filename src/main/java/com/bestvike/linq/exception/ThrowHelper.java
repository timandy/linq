package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Strings;

import java.util.NoSuchElementException;

/**
 * Created by 许崇雷 on 2019-04-23.
 */
public final class ThrowHelper {
    private ThrowHelper() {
    }

    public static void throwArgumentNullException(ExceptionArgument argument) {
        throw new ArgumentNullException(getArgumentString(argument));
    }

    public static void throwArgumentOutOfRangeException(ExceptionArgument argument) {
        throw new ArgumentOutOfRangeException(getArgumentString(argument));
    }

    public static void throwMoreThanOneElementException() {
        throw new InvalidOperationException(SR.MoreThanOneElement);
    }

    public static void throwMoreThanOneMatchException() {
        throw new InvalidOperationException(SR.MoreThanOneMatch);
    }

    public static void throwNoElementsException() {
        throw new InvalidOperationException(SR.NoElements);
    }

    public static void throwNoMatchException() {
        throw new InvalidOperationException(SR.NoMatch);
    }

    public static void throwNotSupportedException() {
        throw new NotSupportedException();
    }

    public static void throwRepeatInvokeException() {
        throw new RepeatInvokeException();
    }

    public static void throwNoSuchElementException() {
        throw new NoSuchElementException(SR.NoSuchElement);
    }

    public static void throwIndexOutOfRangeException() {
        throw new IndexOutOfBoundsException(SR.Arg_IndexOutOfRangeException);
    }

    public static void throwImplementComparableException() {
        throw new ArgumentException(SR.Argument_ImplementComparable);
    }

    public static void throwTupleIncorrectTypeException(Class<?> type, ExceptionArgument argument) {
        throw new ArgumentException(String.format(SR.ArgumentException_TupleIncorrectType, type), getArgumentString(argument));
    }

    public static void throwTupleLastArgumentNotATupleException() {
        throw new ArgumentException(SR.ArgumentException_TupleLastArgumentNotATuple);
    }

    public static void throwRuntimeException(Throwable cause) {
        throw new RuntimeException(cause);
    }

    public static void throwNullPointerException() {
        throw new NullPointerException();
    }

    private static String getArgumentString(ExceptionArgument argument) {
        if (argument == null) {
            assert false : "The ExceptionArgument value is not defined.";
            return Strings.Empty;
        }
        return argument.name();
    }
}
