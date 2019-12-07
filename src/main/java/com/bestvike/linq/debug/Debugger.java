package com.bestvike.linq.debug;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.InvalidOperationException;
import com.bestvike.out;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;

/**
 * Created by 许崇雷 on 2019-12-06.
 */
final class Debugger {
    private Debugger() {
    }

    static String getDebuggerDisplay(Object obj) {
        // Get the DebuggerDisplay for obj
        Class<?> objType = obj.getClass();
        DebuggerDisplay debuggerDisplay = objType.getAnnotation(DebuggerDisplay.class);
        if (debuggerDisplay == null) {
            if (obj instanceof ICollection)
                return "Count = " + ((ICollection<?>) obj)._getCount();
            if (obj instanceof Collection)
                return "Count = " + ((Collection<?>) obj).size();
            return objType.getName();
        }

        // Get the text of the DebuggerDisplay
        String attrText = debuggerDisplay.value();
        String[] segments = Linq.split(attrText, '{', '}').toArray(String.class);
        if (segments.length % 2 == 0)
            throw new InvalidOperationException(String.format("The DebuggerDisplay for %s lacks a closing brace.", objType));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.length; i += 2) {
            String literal = segments[i];
            sb.append(literal);

            if (i + 1 < segments.length) {
                String reference = segments[i + 1];
                boolean noQuotes = reference.endsWith(",nq");
                if (noQuotes)
                    reference = reference.substring(0, reference.length() - 3);

                // Evaluate the reference.
                out<Object> memberRef = out.init();
                if (!tryEvaluateReference(obj, reference, memberRef))
                    throw new InvalidOperationException(String.format("The DebuggerDisplay for %s contains the expression \"%s\".", objType, reference));
                String memberString = getDebuggerMemberString(memberRef.value, noQuotes);
                sb.append(memberString);
            }
        }

        return sb.toString();
    }

    private static String getDebuggerMemberString(Object member, boolean noQuotes) {
        if (member == null)
            return "null";
        if (member instanceof String)
            return noQuotes ? (String) member : '"' + (String) member + '"';
        if (isPrimitiveType(member))
            return member.toString();
        return '{' + member.toString() + '}';
    }

    private static boolean isPrimitiveType(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive()
                || clazz == Boolean.class || clazz == Byte.class || clazz == Short.class || clazz == Integer.class
                || clazz == Long.class || clazz == Character.class || clazz == Float.class || clazz == Double.class;
    }

    private static boolean tryEvaluateReference(Object obj, String reference, out<Object> memberRef) {
        if (reference.endsWith("()")) {
            Method method = getMethod(obj, reference.substring(0, reference.length() - 2));
            if (method != null) {
                try {
                    memberRef.value = Modifier.isStatic(method.getModifiers()) ? method.invoke(null) : method.invoke(obj);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Field field = getField(obj, reference);
            if (field != null) {
                try {
                    memberRef.value = Modifier.isStatic(field.getModifiers()) ? field.get(null) : field.get(obj);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        memberRef.value = null;
        return false;
    }

    private static Field getField(Object obj, String fieldName) {
        for (Class<?> t = obj.getClass(); t != null; t = t.getSuperclass()) {
            try {
                Field field = t.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    private static Method getMethod(Object obj, String propertyName) {
        for (Class<?> t = obj.getClass(); t != null; t = t.getSuperclass()) {
            try {
                Method method = t.getDeclaredMethod(propertyName);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    static IDebugView getDebugView(Object obj) {
        Class<?> objType = obj.getClass();
        Class<? extends IDebugView> proxyType = getProxyType(objType);
        for (Constructor<?> constructor : proxyType.getDeclaredConstructors()) {
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length == 1 && parameters[0].getType().isAssignableFrom(objType)) {
                try {
                    constructor.setAccessible(true);
                    return (IDebugView) constructor.newInstance(obj);
                } catch (Exception ignored) {
                }
            }
        }

        throw new InvalidOperationException(String.format("The DebuggerTypeProxy for %s reference invalid proxyType %s", objType, proxyType));
    }

    private static Class<? extends IDebugView> getProxyType(Class<?> type) {
        // Get the DebuggerTypeProxy for obj
        DebuggerTypeProxy debuggerTypeProxy = type.getAnnotation(DebuggerTypeProxy.class);
        if (debuggerTypeProxy != null)
            return debuggerTypeProxy.value();
        if (IEnumerable.class.isAssignableFrom(type))
            return EnumerableDebugView.class;
        if (Iterable.class.isAssignableFrom(type))
            return IterableDebugView.class;
        return ObjectDebugView.class;
    }
}
