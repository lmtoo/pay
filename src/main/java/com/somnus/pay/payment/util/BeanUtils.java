package com.somnus.pay.payment.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    /**
     * <pre>
     * 两个javabean之间的相同类型、相同属性名之间的值的复制。
     * </pre>
     *
     * @param target
     * @param source
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void copyProperties(Object target, Object source) throws Exception {

        Class sourceClz = source.getClass();

        Class targetClz = target.getClass();

        Field[] fields = sourceClz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            String fieldName = fields[i].getName();

            Field targetField = null;

            try {

                targetField = targetClz.getDeclaredField(fieldName);

            } catch (SecurityException e) {

                e.printStackTrace();

                break;

            } catch (NoSuchFieldException e) {

                continue;

            }

            if (fields[i].getType() == targetField.getType()) {

                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
                                       + fieldName.substring(1);

                String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase()
                                       + fieldName.substring(1);

                Method getMethod;

                try {
                    getMethod = sourceClz.getDeclaredMethod(getMethodName, new Class[] {});
                    Method setMethod = targetClz.getDeclaredMethod(setMethodName,
                        fields[i].getType());
                    Object result = getMethod.invoke(source, new Object[] {});
                    setMethod.invoke(target, result);

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new Exception("同名属性类型不匹配！");

            }

        }

    }
}
