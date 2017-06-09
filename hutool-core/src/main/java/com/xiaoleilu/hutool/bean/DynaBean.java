package com.xiaoleilu.hutool.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 动态Bean，通过反射对Bean的相关方法做操作<br>
 * 支持Map和普通Bean
 * @author Looly
 *
 * @param <T> 动态Bean持有的Bean类型
 */
public class DynaBean {
	
	private Class<?> beanClass;
	private Object bean;
	
	/**
	 * 构造
	 * @param bean 原始Bean
	 */
	public DynaBean(Object bean){
		Assert.notNull(bean);
		if(bean instanceof DynaBean){
			bean = ((DynaBean)bean).getBean();
		}
		this.bean = bean;
		this.beanClass = ClassUtil.getClass(bean);
	}
	
	/**
	 * 获得字段对应值
	 * @param <T> 属性值类型
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String fieldName){
		if(Map.class.isAssignableFrom(beanClass)){
			return (T) ((Map<?, ?>)bean).get(fieldName);
		}else{
			try {
				final PropertyDescriptor descriptor = BeanUtil.getPropertyDescriptor(beanClass, fieldName);
				if(null == descriptor){
					throw new BeanException("No PropertyDescriptor for {}", fieldName);
				}
				final Method method = descriptor.getReadMethod();
				if(null == method){
					throw new BeanException("No set method for {}", fieldName);
				}
				return (T) method.invoke(this.bean);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
	}
	
	/**
	 * 设置字段值
	 * @param fieldName 字段名
	 * @param value 字段值
	 * @throws BeanException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(String fieldName, Object value) throws BeanException{
		if(Map.class.isAssignableFrom(beanClass)){
			((Map)bean).put(fieldName, value);
			return;
		}else{
			try {
				final PropertyDescriptor descriptor = BeanUtil.getPropertyDescriptor(beanClass, fieldName);
				if(null == descriptor){
					throw new BeanException("No PropertyDescriptor for {}", fieldName);
				}
				final Method method = descriptor.getWriteMethod();
				if(null == method){
					throw new BeanException("No set method for {}", fieldName);
				}
				method.invoke(this.bean, value);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
	}
	
	/**
	 * 获得原始Bean
	 * @param <T> Bean类型
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(){
		return (T)this.bean;
	}
	
	/**
	 * 获得Bean的类型
	 * @param <T> Bean类型
	 * @return Bean类型
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getBeanClass(){
		return (Class<T>) this.beanClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DynaBean other = (DynaBean) obj;
		if (bean == null) {
			if (other.bean != null) {
				return false;
			}
		} else if (!bean.equals(other.bean)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.bean.toString();
	}
}
