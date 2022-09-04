package cn.hutool.json.convert;

import cn.hutool.core.convert.CompositeConverter;
import cn.hutool.core.convert.ConvertException;

import java.lang.reflect.Type;

public class JSONCompositeConverter extends CompositeConverter {

	public static final JSONCompositeConverter INSTANCE = new JSONCompositeConverter();

	@Override
	public <T> T convert(final Type type, final Object value, final T defaultValue, final boolean isCustomFirst) throws ConvertException {
		return super.convert(type, value, defaultValue, isCustomFirst);
	}
}
