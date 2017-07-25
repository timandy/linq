package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class ArgumentNullException extends ArgumentException {
    public ArgumentNullException() {
        super("Value cannot be null.");
    }

    public ArgumentNullException(String paramName) {
        super("Value cannot be null.", paramName);
    }

    public ArgumentNullException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentNullException(String paramName, String message) {
        super(message, paramName);
    }
}
