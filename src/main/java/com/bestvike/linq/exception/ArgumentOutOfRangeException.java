package com.bestvike.linq.exception;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public final class ArgumentOutOfRangeException extends ArgumentException {
    private Object actualValue;

    public ArgumentOutOfRangeException() {
        super("Specified argument was out of the range of valid values.");
    }

    public ArgumentOutOfRangeException(String paramName) {
        super("Specified argument was out of the range of valid values.", paramName);
    }

    public ArgumentOutOfRangeException(String paramName, String message) {
        super(message, paramName);
    }

    public ArgumentOutOfRangeException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentOutOfRangeException(String paramName, Object actualValue, String message) {
        super(message, paramName);
        this.actualValue = actualValue;
    }

    public Object getActualValue() {
        return this.actualValue;
    }

    @Override
    public String getMessage() {
        String s = super.getMessage();
        if (this.actualValue != null) {
            String valueMessage = String.format("Actual value was %s.", this.actualValue.toString());
            if (s == null)
                return valueMessage;
            return s + Environment.NewLine + valueMessage;
        }
        return s;
    }
}
