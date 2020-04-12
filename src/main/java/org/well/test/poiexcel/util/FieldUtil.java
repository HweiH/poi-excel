package org.well.test.poiexcel.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @ClassName:FieldUtil
 * @Description:类字段相关工具类
 * @author well
 * @date:2020年4月10日
 *
 */
public class FieldUtil {

    /**
     * 利用反射通过get方法获取bean字段localFieldName的值
     *
     * @param bean
     *            实体
     * @param localFieldName
     *            实体类中包含的字段名
     * @return
     */
    public static <T> Object getBeanLocalFieldValue(T bean, String localFieldName) {
        return getBeanLocalFieldValue(bean, localFieldName, Object.class);
    }

    /**
     * 利用反射通过get方法获取bean字段localFieldName的值
     *
     * @param bean
     *            实体
     * @param localFieldName
     *            实体类中包含的字段名
     * @param valueType
     *            需要获取的值的类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, V> V getBeanLocalFieldValue(T bean, String localFieldName, Class<V> valueType) {
        if (bean == null || StringUtils.isBlank(localFieldName)) {
            return null;
        }
        Field field = ReflectionUtils.findField(bean.getClass(), localFieldName);
        if (field == null) {
            return null;
        }
        boolean oldFlag = field.isAccessible();
        field.setAccessible(true); // 开启访问
        Object obj = ReflectionUtils.getField(field, bean);
        field.setAccessible(oldFlag); // 重置
        if (valueType != null && valueType.isAssignableFrom(obj.getClass())) {
            return (V) obj;
        }
        return null;
    }

    /**
     * 利用反射调用bean.set方法将value设置到字段
     *
     * @param bean
     *            实体
     * @param localFieldName
     *            实体类中包含的字段名
     * @param value
     *            需要设置的值
     */
    public static <T, V> void setBeanLocalFieldValue(T bean, String localFieldName, V value) {
        if (bean == null || StringUtils.isBlank(localFieldName)) {
            return;
        }
        Field field = ReflectionUtils.findField(bean.getClass(), localFieldName);
        if (field == null) {
            return;
        }
        boolean oldFlag = field.isAccessible();
        field.setAccessible(true); // 开启访问
        if (value == null || field.getType().isAssignableFrom(value.getClass())) {
            ReflectionUtils.setField(field, bean, value);
        }
        field.setAccessible(oldFlag); // 重置
    }

}
