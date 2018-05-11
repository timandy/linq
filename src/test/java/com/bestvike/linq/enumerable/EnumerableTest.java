package com.bestvike.linq.enumerable;

import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class EnumerableTest extends IteratorTest {
    static <T> T as(Object value, Class<T> clazz) {
        if (clazz == null)
            throw Errors.argumentNull("clazz");
        return clazz.isInstance(value) ? (T) value : null;
    }

    static Boolean IsEven(int num) {
        return num % 2 == 0;
    }

    static Boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
