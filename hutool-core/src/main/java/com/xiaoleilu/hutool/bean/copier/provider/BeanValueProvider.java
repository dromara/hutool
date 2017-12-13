package com.xiaoleilu.hutool.bean.copier.provider;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import com.xiaoleilu.hutool.bean.BeanDesc.PropDesc;
import com.xiaoleilu.hutool.bean.BeanUtil;
import com.xiaoleilu.hutool.bean.copier.ValueProvider;
import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * Bean的值提供者
 * 
 * @author looly
 *
 */
public class BeanValueProvider implements ValueProvider<String> {

	private Object source;
	private boolean ignoreError;
	final Map<String, PropDesc> sourcePdMap;

	public BeanValueProvider(Object bean, boolean ignoreCase, boolean ignoreError) {
		this.source = bean;
		this.ignoreError = ignoreError;
		sourcePdMap = BeanUtil.getBeanDesc(source.getClass()).getPropMap(ignoreCase);
	}

	@Override
	public Object value(String key, Type valueType) {
		final PropDesc sourcePd = sourcePdMap.get(key);
		if (null != sourcePd) {
			final Method getter = sourcePd.getGetter();
			if (null != getter) {
				try {
					return getter.invoke(source);
				} catch (Exception e) {
					if (false == ignoreError) {
						throw new UtilException(e, "Inject [{}] error!", key);
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean containsKey(String key) {
		return sourcePdMap.containsKey(key);
	}

}
