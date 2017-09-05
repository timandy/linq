package com.bestvike.linq;

import com.bestvike.linq.exception.ArgumentException;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ArgumentOutOfRangeException;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.exception.NotSupportedException;
import com.bestvike.linq.util.Environment;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by 许崇雷 on 2017/8/4.
 */
public class ExceptionTest {
    @Test
    public void testArgument() {
        try {
            throw Errors.argumentNull("id");
        } catch (ArgumentException e) {
            String message = e.getMessage();
            Assert.assertEquals("Value cannot be null." + Environment.NewLine + "Parameter name: id.", message);
        }
    }

    @Test
    public void testArgumentNullException() {
        try {
            List<Object> names = Linq.asEnumerable((Object[]) null).toList();
            Assert.fail("should throw exception,bug get " + names);
        } catch (ArgumentNullException ignored) {
        }
    }

    @Test
    public void testArgumentOutOfRangeException() {
        try {
            throw Errors.argumentOutOfRange("id");
        } catch (ArgumentOutOfRangeException e) {
            String message = e.getMessage();
            Assert.assertEquals("Specified argument was out of the range of valid values." + Environment.NewLine + "Parameter name: id.", message);
        }

        try {
            throw new ArgumentOutOfRangeException("id", -1, "id must greater than zero.");
        } catch (ArgumentOutOfRangeException e) {
            String message = e.getMessage();
            Assert.assertEquals("id must greater than zero." + Environment.NewLine + "Parameter name: id." + Environment.NewLine + "Actual value was -1.", message);
        }
    }

    @Test
    public void testNotSupportedException() {
        try {
            Linq.empty().enumerator().reset();
            Assert.fail("should throw exception");
        } catch (NotSupportedException ignored) {
        }

        try {
            Linq.asEnumerable("hello").enumerator().reset();
            Assert.fail("should throw exception");
        } catch (NotSupportedException ignored) {
        }
    }
}
