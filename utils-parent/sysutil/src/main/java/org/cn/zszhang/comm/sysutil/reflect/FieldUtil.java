package org.cn.zszhang.comm.sysutil.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldUtil {
	private final static Logger logger = LoggerFactory.getLogger(FieldUtil.class);

	/**
	 * 获取指定类的指定字段名的Field, 如果找不到就返回空。
	 * 查找范围包括父、子类的所有字段
	 */
	public static  Field getField(Class<?> clazz, String fieldName) {
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
	public static  Field getField(List<Field> fields, String fieldName) {
		if( null == fields || null == fieldName )	return null;
		
		for( Field f : fields ) {
			if( f.getName().equals(fieldName) )	return f;
		}
		return null;
	}
	/**
	 * 获取一个类的所有字段信息，包括父子类的所有可见不可见字段。
	 */
	public static  List<Field> getAllFields(Class<?> clazz) {
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
	public static void setFieldValue(Object bean, Field field, Object value) {
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
				logger.warn("set field [" + field.getName() + "] fail....");
			}
		} catch (IllegalAccessException e) {
			logger.warn("设置字段值时出现非法访问异常:" + field.getName());
			logger.warn("Cause:" + e.getCause());
		}
	}

	/**
	 *  得到bean的field字段的值。
	 */
	public static <T> Object getFieldValue(Field field, T bean) {
		field.setAccessible(true);
		try {
			return field.get(bean);
		} catch (IllegalAccessException e) {
			logger.warn("取字段值时出现非法访问异常:" + field.getName());
			logger.warn("错误信息:" + e.getMessage());
			return null;
		}
	}

}
