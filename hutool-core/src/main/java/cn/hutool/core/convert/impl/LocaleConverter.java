package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.text.StrUtil;

import java.util.Locale;

/**
 *
 * {@link Locale}对象转换器<br>
 * 只提供String转换支持
 *
 * @author Looly
 * @since 4.5.2
 */
public class LocaleConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected Locale convertInternal(final Class<?> targetClass, final Object value) {
		try {
			final String str = convertToStr(value);
			if (StrUtil.isEmpty(str)) {
				return null;
			}

			final String[] items = str.split("_");
			if (items.length == 1) {
				return new Locale(items[0]);
			}
			if (items.length == 2) {
				return new Locale(items[0], items[1]);
			}
			return new Locale(items[0], items[1], items[2]);
		} catch (final Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
