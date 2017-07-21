package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class ArgumentOutOfRangeException extends ArgumentException {
    public ArgumentOutOfRangeException(String paramName) {
        super(paramName, String.format("argument '%s' out of range.", paramName));
    }

    public ArgumentOutOfRangeException(String paramName, String message) {
        super(paramName, message);
    }
}
