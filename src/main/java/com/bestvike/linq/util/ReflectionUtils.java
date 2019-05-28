package com.bestvike.linq.util;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public final class ReflectionUtils {
    private static final Map<Class<?>, Field[]> CLASS_FIELDS_MAP = new ConcurrentHashMap<>();

    private ReflectionUtils() {
    }

    // get all instance fields include super class.
    public static Field[] getFields(Class<?> clazz) {
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);

        return CLASS_FIELDS_MAP.computeIfAbsent(clazz, cls -> {
            List<Field> fields = new ArrayList<>();
            while (cls != Object.class) {
                Field[] declaredFields = cls.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    field.setAccessible(true);
                    fields.add(field);
                }
                cls = cls.getSuperclass();
            }
            return fields.toArray(new Field[fields.size()]);
        });
    }
}
