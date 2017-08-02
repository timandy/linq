package com.bestvike.linq.exception;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class InvalidOperationException extends RuntimeException {
    public InvalidOperationException() {
        super("Operation is not valid due to the current state of the object.");
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
