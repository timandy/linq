package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public class ArgumentException extends RuntimeException {
    private String paramName;

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String paramName, String message) {
        super(message);
        this.paramName = paramName;
    }

    public String getParamName() {
        return this.paramName;
    }
}
