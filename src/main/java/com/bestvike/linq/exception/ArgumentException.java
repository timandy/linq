package com.bestvike.linq.exception;

import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Environment;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public class ArgumentException extends RuntimeException {
    private String paramName;

    public ArgumentException() {
        super(SR.Arg_ArgumentException);
    }

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Exception innerException) {
        super(message, innerException);
    }

    public ArgumentException(String message, String paramName, Exception innerException) {
        super(message, innerException);
        this.paramName = paramName;
    }

    public ArgumentException(String message, String paramName) {
        super(message);
        this.paramName = paramName;
    }

    public String getParamName() {
        return this.paramName;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (this.paramName != null && this.paramName.length() > 0) {
            String resourceString = String.format(SR.Arg_ParamName_Name, this.paramName);
            return message + Environment.NewLine + resourceString;
        }
        return message;
    }
}
