package com.bestvike.linq.exception;

import java.util.NoSuchElementException;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
public final class Errors {
    private Errors() {
    }

    public static RuntimeException argumentArrayHasTooManyElements(Object p0) {
        return new ArgumentException(String.format("Parameter %s has too many elements", p0));
    }

    public static RuntimeException argumentNotIEnumerableGeneric(Object p0) {
        return new ArgumentException(String.format("%s is not IEnumerable", p0));
    }

    public static RuntimeException argumentNotSequence(Object p0) {
        return new ArgumentException(String.format("%s is not a sequence", p0));
    }

    public static RuntimeException argumentNotValid(Object p0) {
        return new ArgumentException(String.format("Argument %s is not valid", p0));
    }

    public static RuntimeException incompatibleElementTypes() {
        return new ArgumentException("The two sequences have incompatible element types");
    }

    public static RuntimeException argumentNotLambda(Object p0) {
        return new ArgumentException(String.format("Argument %s is not a LambdaExpression", p0));
    }

    public static RuntimeException moreThanOneElement() {
        return new InvalidOperationException("Sequence contains more than one element");
    }

    public static RuntimeException moreThanOneMatch() {
        return new InvalidOperationException("Sequence contains more than one matching element");
    }

    public static RuntimeException noArgumentMatchingMethodsInQueryable(Object p0) {
        return new InvalidOperationException(String.format("There is no method '%s' on class System.Linq.WhereIterator that matches the specified arguments", p0));
    }

    public static RuntimeException noElements() {
        return new InvalidOperationException("Sequence contains no elements");
    }

    public static RuntimeException noMatch() {
        return new InvalidOperationException("Sequence contains no matching element");
    }

    public static RuntimeException noMethodOnType(Object p0, Object p1) {
        return new InvalidOperationException(String.format("There is no method '%s' on type '%s'", p0, p1));
    }

    public static RuntimeException noMethodOnTypeMatchingArguments(Object p0, Object p1) {
        return new InvalidOperationException(String.format("There is no method '%s' on type '%s' that matches the specified arguments", p0, p1));
    }

    public static RuntimeException noNameMatchingMethodsInQueryable(Object p0) {
        return new InvalidOperationException(String.format("There is no method '%s' on class System.Linq.Queryable that matches the specified arguments", p0));
    }

    public static RuntimeException argumentNull(String paramName) {
        return new ArgumentNullException(paramName);
    }

    public static RuntimeException argumentOutOfRange(String paramName) {
        return new ArgumentOutOfRangeException(paramName);
    }

    public static RuntimeException notImplemented() {
        return new NotImplementedException();
    }

    public static RuntimeException notSupported() {
        return new NotSupportedException();
    }

    public static RuntimeException noSuchElement() {
        return new NoSuchElementException("There is no such element.");
    }

    public static RuntimeException implementComparable() {
        return new ArgumentException("At least one object must implement Comparable.");
    }

    public static RuntimeException tupleIncorrectType(String className, String paramName) {
        return new ArgumentException(String.format("Argument must be of type %s.", className), paramName);
    }
}
