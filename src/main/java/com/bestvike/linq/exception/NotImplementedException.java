package com.bestvike.linq.exception;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("The method or operation is not implemented.");
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(String message, Exception inner) {
        super(message, inner);
    }
}
