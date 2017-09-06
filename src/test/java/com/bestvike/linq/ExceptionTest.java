package com.bestvike.linq;

import com.bestvike.linq.exception.ArgumentException;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Environment;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by 许崇雷 on 2017/8/4.
 */
public class ExceptionTest {
    @Test
    public void testArgument() {
        try {
            throw new ArgumentException();
        } catch (ArgumentException e) {
            Assert.assertEquals(SR.Arg_ArgumentException, e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error");
        } catch (ArgumentException e) {
            Assert.assertEquals("arg error", e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", new NullPointerException());
        } catch (ArgumentException e) {
            Assert.assertEquals("arg error", e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "id", new NullPointerException());
        } catch (ArgumentException e) {
            Assert.assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "id");
        } catch (ArgumentException e) {
            Assert.assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.implementComparable();
        } catch (ArgumentException e) {
            Assert.assertEquals(SR.Argument_ImplementComparable, e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw Errors.tupleIncorrectType(String.class, "id");
        } catch (ArgumentException e) {
            Assert.assertEquals(String.format(SR.ArgumentException_TupleIncorrectType, String.class) + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.tupleLastArgumentNotATuple();
        } catch (ArgumentException e) {
            Assert.assertEquals(SR.ArgumentException_TupleLastArgumentNotATuple, e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }
    }

    @Test
    public void testArgumentNullException() {
        try {
            throw new ArgumentNullException();
        } catch (ArgumentNullException e) {
            Assert.assertEquals(SR.ArgumentNull_Generic, e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("id");
        } catch (ArgumentNullException e) {
            Assert.assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }

        try {
            throw new ArgumentNullException("null error", new NullPointerException());
        } catch (ArgumentNullException e) {
            Assert.assertEquals("null error", e.getMessage());
            Assert.assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("id", "null error");
        } catch (ArgumentNullException e) {
            Assert.assertEquals("null error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.argumentNull("id");
        } catch (ArgumentNullException e) {
            Assert.assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            Assert.assertEquals("id", e.getParamName());
        }
    }

    @Test
    public void testArgumentOutOfRangeException() {
        try {
            throw new ArgumentOutOfRangeException();
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals(SR.Arg_ArgumentOutOfRangeException, e.getMessage());
            Assert.assertEquals(null, e.getParamName());
            Assert.assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index");
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals(SR.Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            Assert.assertEquals("index", e.getParamName());
            Assert.assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", "out of range");
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals("out of range" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            Assert.assertEquals("index", e.getParamName());
            Assert.assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("out of range", new NullPointerException());
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals("out of range", e.getMessage());
            Assert.assertEquals(null, e.getParamName());
            Assert.assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", -1, "out of range");
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals("out of range" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index") + Environment.NewLine + String.format(SR.ArgumentOutOfRange_ActualValue, "-1"), e.getMessage());
            Assert.assertEquals("index", e.getParamName());
            Assert.assertEquals(-1, e.getActualValue());
        }

        try {
            throw Errors.argumentOutOfRange("index");
        } catch (ArgumentOutOfRangeException e) {
            Assert.assertEquals(SR.Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            Assert.assertEquals("index", e.getParamName());
            Assert.assertEquals(null, e.getActualValue());
        }
    }

    @Test
    public void testInvalidOperationException() {
        try {
            throw new InvalidOperationException();
        } catch (InvalidOperationException e) {
            Assert.assertEquals(SR.Arg_InvalidOperationException, e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation");
        } catch (InvalidOperationException e) {
            Assert.assertEquals("invalid operation", e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation", new RuntimeException());
        } catch (InvalidOperationException e) {
            Assert.assertEquals("invalid operation", e.getMessage());
        }

        try {
            throw Errors.moreThanOneElement();
        } catch (InvalidOperationException e) {
            Assert.assertEquals(SR.MoreThanOneElement, e.getMessage());
        }

        try {
            throw Errors.moreThanOneMatch();
        } catch (InvalidOperationException e) {
            Assert.assertEquals(SR.MoreThanOneMatch, e.getMessage());
        }

        try {
            throw Errors.noElements();
        } catch (InvalidOperationException e) {
            Assert.assertEquals(SR.NoElements, e.getMessage());
        }
        try {
            throw Errors.noMatch();
        } catch (InvalidOperationException e) {
            Assert.assertEquals(SR.NoMatch, e.getMessage());
        }
    }

    @Test
    public void testNotSupportedException() {
        try {
            throw new NotSupportedException();
        } catch (NotSupportedException e) {
            Assert.assertEquals(SR.Arg_NotSupportedException, e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported");
        } catch (NotSupportedException e) {
            Assert.assertEquals("not supported", e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported", new NullPointerException());
        } catch (NotSupportedException e) {
            Assert.assertEquals("not supported", e.getMessage());
        }

        try {
            throw Errors.notSupported();
        } catch (NotSupportedException e) {
            Assert.assertEquals(SR.Arg_NotSupportedException, e.getMessage());
        }
    }

    @Test
    public void testOther() {
        try {
            throw Errors.noSuchElement();
        } catch (NoSuchElementException e) {
            Assert.assertEquals(SR.NoSuchElement, e.getMessage());
        }

        try {
            throw Errors.indexOutOfRange();
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals(SR.Arg_IndexOutOfRangeException, e.getMessage());
        }
    }
}
