package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * URL对象转换器
 * @author Looly
 *
 */
public class URLConverter extends AbstractConverter{
	private static final long serialVersionUID = 1L;

	@Override
	protected URL convertInternal(final Class<?> targetClass, final Object value) {
		try {
			if(value instanceof File){
				return ((File)value).toURI().toURL();
			}

			if(value instanceof URI){
				return ((URI)value).toURL();
			}
			return new URL(convertToStr(value));
		} catch (final Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
