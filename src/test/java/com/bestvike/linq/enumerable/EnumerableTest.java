package com.bestvike.linq.enumerable;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class EnumerableTest extends IteratorTest {
    protected static Boolean IsEven(int num) {
        return num % 2 == 0;
    }

    static Boolean IsNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
