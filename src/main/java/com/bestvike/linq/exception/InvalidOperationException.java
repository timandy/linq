package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
        super("invalid operation.");
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
