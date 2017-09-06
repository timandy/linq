package com.bestvike.linq;

import com.bestvike.linq.exception.ArgumentException;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Environment;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.bestvike.linq.resources.SR.Arg_ArgumentOutOfRangeException;
import static com.bestvike.linq.resources.SR.Arg_IndexOutOfRangeException;
import static com.bestvike.linq.resources.SR.Arg_InvalidOperationException;
import static com.bestvike.linq.resources.SR.Arg_NotSupportedException;
import static com.bestvike.linq.resources.SR.Arg_ParamName_Name;
import static com.bestvike.linq.resources.SR.ArgumentOutOfRange_ActualValue;
import static com.bestvike.linq.resources.SR.MoreThanOneElement;
import static com.bestvike.linq.resources.SR.MoreThanOneMatch;
import static com.bestvike.linq.resources.SR.NoElements;
import static com.bestvike.linq.resources.SR.NoMatch;
import static com.bestvike.linq.resources.SR.NoSuchElement;
import static org.junit.Assert.assertEquals;

/**
 * Created by 许崇雷 on 2017/8/4.
 */
public class ExceptionTest {
    @Test
    public void testArgument() {
        try {
            throw new ArgumentException();
        } catch (ArgumentException e) {
            assertEquals(SR.Arg_ArgumentException, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error");
        } catch (ArgumentException e) {
            assertEquals("arg error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", new NullPointerException());
        } catch (ArgumentException e) {
            assertEquals("arg error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "id", new NullPointerException());
        } catch (ArgumentException e) {
            assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "id");
        } catch (ArgumentException e) {
            assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.implementComparable();
        } catch (ArgumentException e) {
            assertEquals(SR.Argument_ImplementComparable, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw Errors.tupleIncorrectType(String.class, "id");
        } catch (ArgumentException e) {
            assertEquals(String.format(SR.ArgumentException_TupleIncorrectType, String.class) + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.tupleLastArgumentNotATuple();
        } catch (ArgumentException e) {
            assertEquals(SR.ArgumentException_TupleLastArgumentNotATuple, e.getMessage());
            assertEquals(null, e.getParamName());
        }
    }

    @Test
    public void testArgumentNullException() {
        try {
            throw new ArgumentNullException();
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("id");
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }

        try {
            throw new ArgumentNullException("null error", new NullPointerException());
        } catch (ArgumentNullException e) {
            assertEquals("null error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("id", "null error");
        } catch (ArgumentNullException e) {
            assertEquals("null error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }

        try {
            throw Errors.argumentNull("id");
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "id"), e.getMessage());
            assertEquals("id", e.getParamName());
        }
    }

    @Test
    public void testArgumentOutOfRangeException() {
        try {
            throw new ArgumentOutOfRangeException();
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(Arg_ArgumentOutOfRangeException, e.getMessage());
            assertEquals(null, e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", "out of range");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range" + Environment.NewLine + String.format(Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("out of range", new NullPointerException());
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range", e.getMessage());
            assertEquals(null, e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", -1, "out of range");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range" + Environment.NewLine + String.format(Arg_ParamName_Name, "index") + Environment.NewLine + String.format(ArgumentOutOfRange_ActualValue, "-1"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(-1, e.getActualValue());
        }

        try {
            throw Errors.argumentOutOfRange("index");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }
    }

    @Test
    public void testInvalidOperationException() {
        try {
            throw new InvalidOperationException();
        } catch (InvalidOperationException e) {
            assertEquals(Arg_InvalidOperationException, e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation");
        } catch (InvalidOperationException e) {
            assertEquals("invalid operation", e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation", new RuntimeException());
        } catch (InvalidOperationException e) {
            assertEquals("invalid operation", e.getMessage());
        }

        try {
            throw Errors.moreThanOneElement();
        } catch (InvalidOperationException e) {
            assertEquals(MoreThanOneElement, e.getMessage());
        }

        try {
            throw Errors.moreThanOneMatch();
        } catch (InvalidOperationException e) {
            assertEquals(MoreThanOneMatch, e.getMessage());
        }

        try {
            throw Errors.noElements();
        } catch (InvalidOperationException e) {
            assertEquals(NoElements, e.getMessage());
        }
        try {
            throw Errors.noMatch();
        } catch (InvalidOperationException e) {
            assertEquals(NoMatch, e.getMessage());
        }
    }

    @Test
    public void testNotSupportedException() {
        try {
            throw new NotSupportedException();
        } catch (NotSupportedException e) {
            assertEquals(Arg_NotSupportedException, e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported");
        } catch (NotSupportedException e) {
            assertEquals("not supported", e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported", new NullPointerException());
        } catch (NotSupportedException e) {
            assertEquals("not supported", e.getMessage());
        }

        try {
            throw Errors.notSupported();
        } catch (NotSupportedException e) {
            assertEquals(Arg_NotSupportedException, e.getMessage());
        }
    }

    @Test
    public void testOther() {
        try {
            throw Errors.noSuchElement();
        } catch (NoSuchElementException e) {
            assertEquals(NoSuchElement, e.getMessage());
        }

        try {
            throw Errors.indexOutOfRange();
        } catch (IndexOutOfBoundsException e) {
            assertEquals(Arg_IndexOutOfRangeException, e.getMessage());
        }
    }
}
