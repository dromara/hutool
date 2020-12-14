package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.Convert;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * {@link AtomicIntegerArray}转换器
 * 
 * @author Looly
 * @since 5.4.5
 */
public class AtomicIntegerArrayConverter extends AbstractConverter<AtomicIntegerArray> {
	private static final long serialVersionUID = 1L;

	@Override
	protected AtomicIntegerArray convertInternal(Object value) {
		return new AtomicIntegerArray(Convert.convert(int[].class, value));
	}

}
