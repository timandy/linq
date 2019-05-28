package com.bestvike;

import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public abstract class ValueType {
    protected ValueType() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj == this)
            return true;

        Class<?> clazz = this.getClass();
        if (obj.getClass() != clazz)
            return false;

        Field[] fields = ReflectionUtils.getFields(clazz);
        try {
            for (Field field : fields) {
                if (Objects.equals(field.get(this), field.get(obj)))
                    continue;
                return false;
            }
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Class<?> clazz = this.getClass();
        Field[] fields = ReflectionUtils.getFields(clazz);
        try {
            for (Field field : fields)
                result = prime * result + Objects.hashCode(field.get(this));
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        return result;
    }

    @Override
    public String toString() {
        Class<?> clazz = this.getClass();
        Field[] fields = ReflectionUtils.getFields(clazz);
        if (fields.length <= 0)
            return clazz.getSimpleName() + "{}";

        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append("{");
        try {
            Field head = fields[0];
            sb.append(head.getName()).append("=");
            if (head.getType() == String.class)
                sb.append("'").append(head.get(this)).append("'");
            else
                sb.append(head.get(this));
            for (int i = 1; i < fields.length; i++) {
                Field field = fields[i];
                sb.append(", ").append(field.getName()).append("=");
                if (field.getType() == String.class)
                    sb.append("'").append(field.get(this)).append("'");
                else
                    sb.append(field.get(this));
            }
        } catch (IllegalAccessException e) {
            ThrowHelper.throwRuntimeException(e);
        }
        sb.append("}");
        return sb.toString();
    }
}
