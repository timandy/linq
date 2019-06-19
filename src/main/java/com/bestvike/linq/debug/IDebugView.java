package com.bestvike.linq.debug;

/**
 * Created by 许崇雷 on 2019-06-18.
 */
interface IDebugView {
    String getDebuggerDisplay();

    Object getDebuggerTypeProxy();

    default String getDebuggerMemberString(Object member) {
        return this.getDebuggerMemberString(member, false);
    }

    default String getDebuggerMemberString(Object member, boolean noQuotes) {
        if (member == null)
            return "null";
        if (member instanceof String)
            return noQuotes ? (String) member : '"' + (String) member + '"';
        Class<?> clazz = member.getClass();
        if (clazz.isPrimitive()
                || clazz == Boolean.class || clazz == Byte.class || clazz == Short.class || clazz == Integer.class
                || clazz == Long.class || clazz == Character.class || clazz == Float.class || clazz == Double.class)
            return member.toString();
        return '{' + member.toString() + '}';
    }
}
