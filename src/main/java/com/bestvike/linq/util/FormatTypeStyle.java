package com.bestvike.linq.util;

/**
 * Created by 许崇雷 on 2019-06-21.
 */
public enum FormatTypeStyle {
    None,
    SimpleName,
    FullName;

    void appendType(Class<?> type, StringBuilder sb) {
        switch (this) {
            case SimpleName:
                sb.append(type.getSimpleName());
                break;
            case FullName:
                sb.append(type.getName());
                break;
            default:
                break;
        }
    }
}
