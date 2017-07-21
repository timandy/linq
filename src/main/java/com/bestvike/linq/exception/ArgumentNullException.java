package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class ArgumentNullException extends ArgumentException {
    public ArgumentNullException(String paramName) {
        super(paramName, String.format("argument '%s' can not be null.", paramName));
    }

    public ArgumentNullException(String paramName, String message) {
        super(paramName, message);
    }
}
