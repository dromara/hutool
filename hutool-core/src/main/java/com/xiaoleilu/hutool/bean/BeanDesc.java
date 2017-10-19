package com.xiaoleilu.hutool.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.collection.CaseInsensitiveMap;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.TypeUtil;

/**
 * Bean信息描述做为BeanInfo替代方案，此对象持有JavaBean中的setters和getters等相关信息描述
 * 
 * @author looly
 * @since 3.1.2
 */
public class BeanDesc {
	
	/** Bean类 */
	private Class<?> beanClass;
	/** 属性Map */
	private Map<String, PropDesc> propMap = new HashMap<>();
	
	/**
	 * 构造
	 * @param beanClass Bean类
	 */
	public BeanDesc(Class<?> beanClass) {
		Assert.notNull(beanClass);
		this.beanClass = beanClass;
		init();
	}
	
	/**
	 * 获取Bean的全类名
	 * @return Bean的类名
	 */
	public String getName() {
		return this.beanClass.getName();
	}
	
	/**
	 * 获取Bean的简单类名
	 * @return Bean的类名
	 */
	public String getSimpleName() {
		return this.beanClass.getSimpleName();
	}
	
	/**
	 * 获取字段名-字段属性Map
	 * @return 字段名-字段属性Map
	 */
	public Map<String, PropDesc> getPropMap(boolean ignoreCase) {
		return ignoreCase ? new CaseInsensitiveMap<>(1, this.propMap) : this.propMap;
	}
	
	/**
	 * 获取字段名-字段属性Map
	 * @return {@link PropDesc} 列表
	 */
	public Collection<PropDesc> getProps() {
		return this.propMap.values();
	}
	
	/**
	 * 获取属性，如果不存在返回null
	 * @param fieldName 字段名
	 * @return {@link PropDesc}
	 */
	public PropDesc getProp(String fieldName) {
		return this.propMap.get(fieldName);
	}
	
	/**
	 * 获得字段名对应的字段对象，如果不存在返回null
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	public Field getField(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getField();
	}
	
	/**
	 * 获取Getter方法，如果不存在返回null
	 * @param fieldName 字段名
	 * @return Getter方法
	 */
	public Method getGetter(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getGetter();
	}
	
	/**
	 * 获取Setter方法，如果不存在返回null
	 * @param fieldName 字段名
	 * @return Setter方法
	 */
	public Method getSetter(String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getSetter();
	}
	
	/**
	 * 初始化
	 * @return this
	 */
	private BeanDesc init() {
		final Field[] fields = ReflectUtil.getFields(this.beanClass);
		
		String fieldName;
		Method getter;
		Method setter;
		for (Field field : fields) {
			fieldName = field.getName();
			getter = ReflectUtil.getMethod(this.beanClass, StrUtil.genGetter(fieldName));
			setter = ReflectUtil.getMethod(this.beanClass, StrUtil.genSetter(fieldName), field.getType());
			this.propMap.put(fieldName, new PropDesc(field, getter, setter));
		}
		return this;
	}

	/**
	 * 属性描述
	 * @author looly
	 *
	 */
	public static class PropDesc {
		
		/** 字段 */
		private Field field;
		/** Getter方法 */
		private Method getter;
		/** Setter方法 */
		private Method setter;
		
		/**
		 * 构造<br>
		 * Getter和Setter方法设置为默认可访问
		 * 
		 * @param field 字段
		 * @param getter get方法
		 * @param setter set方法
		 */
		public PropDesc(Field field, Method getter, Method setter) {
			this.field = field;
			this.getter = ClassUtil.setAccessible(getter);
			this.setter = ClassUtil.setAccessible(setter);
		}

		/**
		 * 获取字段名
		 * @return 字段名
		 */
		public String getFieldName() {
			return null == this.field ? null : this.field.getName();
		}
		
		/**
		 * 获取字段
		 * @return 字段
		 */
		public Field getField() {
			return this.field;
		}
		
		/**
		 * 获得字段类型<br>
		 * 先获取字段的类型，如果字段不存在，则获取Getter方法的返回类型，否则获取Setter的第一个参数类型
		 * 
		 * @return 字段类型
		 */
		public Type getFieldType() {
			if(null != this.field) {
				return TypeUtil.getType(this.field);
			}
			return findPropType(getter, setter);
		}
		
		/**
		 * 获得字段类型<br>
		 * 先获取字段的类型，如果字段不存在，则获取Getter方法的返回类型，否则获取Setter的第一个参数类型
		 * 
		 * @return 字段类型
		 */
		public Class<?> getFieldClass() {
			if(null != this.field) {
				return TypeUtil.getClass(this.field);
			}
			return findPropClass(getter, setter);
		}

		/**
		 * 获取Getter方法
		 * @return Getter方法
		 */
		public Method getGetter() {
			return this.getter;
		}

		/**
		 * 获取Setter方法
		 */
		public Method getSetter() {
			return this.setter;
		}
		
		/**
		 * 通过Getter和Setter方法中找到属性类型
		 * @param getter Getter方法
		 * @param setter Setter方法
		 * @return {@link Type}
		 */
		private Type findPropType(Method getter, Method setter) {
			Type type = null;
			if(null != getter) {
				type = TypeUtil.getReturnType(getter);
			}
			if(null == type && null != setter) {
				type = TypeUtil.getParamType(setter, 0);
			}
			return type;
		}
		
		/**
		 * 通过Getter和Setter方法中找到属性类型
		 * @param getter Getter方法
		 * @param setter Setter方法
		 * @return {@link Type}
		 */
		private Class<?> findPropClass(Method getter, Method setter) {
			Class<?> type = null;
			if(null != getter) {
				type = TypeUtil.getReturnClass(getter);
			}
			if(null == type && null != setter) {
				type = TypeUtil.getFirstParamClass(setter);
			}
			return type;
		}
	}
}