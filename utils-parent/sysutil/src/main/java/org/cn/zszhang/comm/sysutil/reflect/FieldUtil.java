package org.cn.zszhang.comm.sysutil.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(FieldUtil.class);

	/**
	 * 获取指定类的指定字段名的Field, 如果找不到就返回空。
	 * 查找范围包括父、子类的所有字段
	 */
	public static  Field getField(final Class<?> clazz, final String fieldName) {
		if( null == clazz || null == fieldName || fieldName.isEmpty() ) return null;

		for(Class<?> c = clazz; c != Object.class ; c = c.getSuperclass() ) {
			Field[] sf = c.getDeclaredFields();
			for( Field e : sf ) {
				if( e.getName().equals(fieldName) )		return e;
			}
		}

		return null;
	}
	/**
	 * 从字段列表中，获取指定字段名的Field, 如果找不到就返回空。
	 */
	public static  Field getField(final List<Field> fields, final String fieldName) {
		if( null == fields || null == fieldName )	return null;

		for( Field f : fields ) {
			if( f.getName().equals(fieldName) )	return f;
		}
		return null;
	}
	/**
	 * 获取一个类的所有字段信息，包括父子类的所有可见不可见字段。
	 */
	public static  List<Field> getAllFields(final Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();

		for(Class<?> c = clazz; c != Object.class ; c = c.getSuperclass() ) {
			Field[] sf = c.getDeclaredFields();
			for( Field e : sf ) {
				fields.add(e);
			}
		}

		return fields;
	}

	/**
	 *  给bean的field字段这只值为value。
	 */
	public static void setFieldValue(Object bean, Field field, final Object value) {
		if( null == value || null == field || null == bean) return;
		field.setAccessible(true);
		try {
			field.set(bean, value);
		} catch (IllegalArgumentException e) {
            LOGGER.warn("设置字段值时出现数据类型不一致问题:" + field.getName(), e);
        } catch (IllegalAccessException e) {
			LOGGER.warn("设置字段值时出现非法访问异常:" + field.getName(), e);
		}
	}

    public static void setFieldValueFromExcel(Object bean, Field field, final Object value) {
        if( null == value ) return;
        Class<?> type = field.getType();
        String typeName = type.getSimpleName();
        field.setAccessible(true);
        try {
            if( value instanceof String)
                field.set(bean, value);
            else if( !type.isPrimitive() )
                field.set(bean, value.toString());
            else if( "int".equals(typeName) )
                field.set(bean,((Double)value).intValue());
            else if( "short".equals(typeName) )
                field.set(bean,((Double)value).shortValue());
            else if( "long".equals(typeName) )
                field.set(bean,((Double)value).longValue());
            else if( "float".equals(typeName) )
                field.set(bean,((Double)value).floatValue());
            else if( "double".equals(typeName) )
                field.set(bean,((Double)value).doubleValue());
            else if( "boolean".equals(typeName) )
                field.set(bean,((Boolean)value));
            else {
                LOGGER.warn("set field [" + field.getName() + "] fail....");
            }
        } catch (IllegalAccessException e) {
            LOGGER.warn("设置字段值时出现非法访问异常:" + field.getName(), e);
        }
    }

    /**
     *  得到bean的field字段的值。
     */
	public static <T> Object getFieldValue(final Field field, final T bean) {
		field.setAccessible(true);
		try {
			return field.get(bean);
		} catch (IllegalAccessException e) {
			LOGGER.warn("取字段值时出现非法访问异常:" + field.getName(), e);
			return null;
		}
	}

	public static Object getFieldValue(final Object bean, final String attrName) {
		Field filed = getField(bean.getClass(), attrName);
		return getFieldValue(filed, bean);
	}

	public static void setFieldValue(Object bean, String attrName, final Object value) {
		Field filed = getField(bean.getClass(), attrName);
		FieldUtil.setFieldValue(bean, filed, value);
	}


	// 得到当前类所有为空字段的字段名列表
	public static <T> List<String> getAllNullFieldNames(final T bean) {
		List<String> nullFiledNames = new ArrayList<String>();
		if(null == bean)	return nullFiledNames;
		List<Field> fields = getAllFields(bean.getClass());
		for(Field field : fields) {
			Object v = getFieldValue(field, bean);
			if(null == v)	nullFiledNames.add(field.getName());
		}
		return nullFiledNames;
	}
	// 得到当前类所有为空字段的字段名列表
	public static <T> List<String> getAllEmptyFieldNames(final T bean) {
		List<String> nullFiledNames = new ArrayList<String>();
		if(null == bean)	return nullFiledNames;
		List<Field> fields = getAllFields(bean.getClass());
		for(Field field : fields) {
			Object v = getFieldValue(field, bean);
			if(isEmptyObject(v))	nullFiledNames.add(field.getName());
		}
		return nullFiledNames;
	}
	// 得到所有指定字段中为空字段的列表
	public static <T> List<String> getAllNullFieldNames(final T bean, final String[] fieldNames) {
		List<String> nullFiledNames = new ArrayList<String>();
		if(null == fieldNames) {
			LOGGER.warn("字段列表为空，直接返回空list");
			return nullFiledNames;
		}
		for(String field : fieldNames) {
			if(null == field) break;
			Object v = getFieldValue(bean, field);
			if(null == v)	nullFiledNames.add(field);
		}

		return nullFiledNames;
	}
	// 得到所有指定字段中为空字段的列表
	public static <T> List<String> getAllNullFieldNames(final T bean, final List<String> fieldNames) {
		if(null == fieldNames || fieldNames.isEmpty()) {
			LOGGER.warn("字段列表为空，直接返回空list");
			return new ArrayList<String>();
		}
		return getAllNullFieldNames(bean, (String[])fieldNames.toArray());
	}
	// 得到所有指定字段中为空字段的列表
	public static <T> List<String> getAllEmptyFieldNames(final T bean, final String[] fieldNames) {
		List<String> nullFiledNames = new ArrayList<String>();
		if(null == fieldNames) {
			LOGGER.warn("字段列表为空，直接返回空list");
			return nullFiledNames;
		}
		for(String field : fieldNames) {
			if(null == field) break;
			Object v = getFieldValue(bean, field);
			if(isEmptyObject(v))	nullFiledNames.add(field);
		}
		return nullFiledNames;
	}
	// 得到所有指定字段中为空字段的列表
	public static <T> List<String> getAllEmptyFieldNames(final T bean, final List<String> fieldNames) {
		if(null == fieldNames || fieldNames.isEmpty()) {
			LOGGER.warn("字段列表为空，直接返回null");
			return new ArrayList<String>();
		}
		return getAllEmptyFieldNames(bean, (String[])fieldNames.toArray());
	}

	public static <T> String getFirstNullFieldName(final T bean, final String[] fieldNames) {
		if(null == fieldNames) {
			LOGGER.warn("字段列表为空，直接返回null");
			return null;
		}
		for(String fname : fieldNames) {
			if(null == fname)	break;
			Object v = getFieldValue(bean, fname);
			if(null == v)	return fname;
		}

		return null;
	}

	public static <T> String getFirstNullFieldName(final T bean, final List<String> fieldNames) {
		if(null == fieldNames || fieldNames.isEmpty()) {
			LOGGER.warn("字段列表为空，直接返回null");
			return null;
		}
		return getFirstNullFieldName(bean, (String[]) fieldNames.toArray());
	}

	// 检查fieldNames中所列字段的值不为NULL或empty,返回null表示全部不为null，否则返回第一个为null的字段名
	public static <T> String getFirstEmptyFieldName(final T bean, final String[] fieldNames) {
		if(null == fieldNames) {
			LOGGER.warn("字段列表为空，直接返回null");
			return null;
		}

		for(String fname : fieldNames) {
			if(null == fname)	break;
			Object v = getFieldValue(bean, fname);
			if(isEmptyObject(v))	return fname;
		}

		return null;
	}

	// 检查fieldNames中所列字段的值不为NULL或empty,返回null表示全部不为null，否则返回第一个为null的字段名
	public static <T> String getFirstEmptyFieldName(final T bean, final List<String> fieldNames) {
		if(null == fieldNames || fieldNames.isEmpty()) {
			LOGGER.warn("字段列表为空，直接返回null");
			return null;
		}
		return getFirstEmptyFieldName(bean, (String[]) fieldNames.toArray());
	}

	private static boolean isEmptyObject(Object o) {
		if(null == o)	return true;
		if(o instanceof Collection)
			return ((Collection)o).isEmpty();
		if(o instanceof String)
			return ((String)o).isEmpty();
		return false;
	}
}
