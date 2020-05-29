package cn.hutool.core.bean;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 动态Bean，通过反射对Bean的相关方法做操作<br>
 * 支持Map和普通Bean
 * 
 * @author Looly
 * @since 3.0.7
 */
public class DynaBean extends CloneSupport<DynaBean> implements Serializable{
	private static final long serialVersionUID = 1L;

	private final Class<?> beanClass;
	private final Object bean;
	
	/**
	 * 创建一个{@link DynaBean}
	 * @param bean 普通Bean
	 * @return {@link DynaBean}
	 */
	public static DynaBean create(Object bean){
		return new DynaBean(bean);
	}
	/**
	 * 创建一个{@link DynaBean}
	 * @param beanClass Bean类
	 * @param params 构造Bean所需要的参数
	 * @return {@link DynaBean}
	 */
	public static DynaBean create(Class<?> beanClass, Object... params){
		return new DynaBean(beanClass, params);
	}
	
	//------------------------------------------------------------------------ Constructor start
	/**
	 * 构造
	 * @param beanClass Bean类
	 * @param params 构造Bean所需要的参数
	 */
	public DynaBean(Class<?> beanClass, Object... params){
		this(ReflectUtil.newInstance(beanClass, params));
	}
	
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
	//------------------------------------------------------------------------ Constructor end
	
	/**
	 * 获得字段对应值
	 * @param <T> 属性值类型
	 * @param fieldName 字段名
	 * @return 字段值
	 * @throws BeanException 反射获取属性值或字段值导致的异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String fieldName) throws BeanException{
		if(Map.class.isAssignableFrom(beanClass)){
			return (T) ((Map<?, ?>)bean).get(fieldName);
		}else{
			try {
				final Method method = BeanUtil.getBeanDesc(beanClass).getGetter(fieldName);
				if(null == method){
					throw new BeanException("No get method for {}", fieldName);
				}
				return (T) method.invoke(this.bean);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
	}
	
	/**
	 * 获得字段对应值，获取异常返回{@code null}
	 * 
	 * @param <T> 属性值类型
	 * @param fieldName 字段名
	 * @return 字段值
	 * @since 3.1.1
	 */
	public <T> T safeGet(String fieldName){
		try {
			return get(fieldName);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 设置字段值
	 * @param fieldName 字段名
	 * @param value 字段值
	 * @throws BeanException 反射获取属性值或字段值导致的异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(String fieldName, Object value) throws BeanException{
		if(Map.class.isAssignableFrom(beanClass)){
			((Map)bean).put(fieldName, value);
		}else{
			try {
				final Method setter = BeanUtil.getBeanDesc(beanClass).getSetter(fieldName);
				if(null == setter){
					throw new BeanException("No set method for {}", fieldName);
				}
				setter.invoke(this.bean, value);
			} catch (Exception e) {
				throw new BeanException(e);
			}
		}
	}
	
	/**
	 * 执行原始Bean中的方法
	 * @param methodName 方法名
	 * @param params 参数
	 * @return 执行结果，可能为null
	 */
	public Object invoke(String methodName, Object... params){
		return ReflectUtil.invoke(this.bean, methodName, params);
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
			return other.bean == null;
		} else return bean.equals(other.bean);
	}

	@Override
	public String toString() {
		return this.bean.toString();
	}
}
