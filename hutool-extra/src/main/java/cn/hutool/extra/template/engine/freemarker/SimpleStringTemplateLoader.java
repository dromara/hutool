package cn.hutool.extra.template.engine.freemarker;

import freemarker.cache.TemplateLoader;

import java.io.Reader;
import java.io.StringReader;

/**
 * {@link TemplateLoader} 字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author looly
 * @since 4.3.3
 */
public class SimpleStringTemplateLoader implements TemplateLoader {

	@Override
	public Object findTemplateSource(final String name) {
		return name;
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return System.currentTimeMillis();
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) {
		return new StringReader((String) templateSource);
	}

	@Override
	public void closeTemplateSource(final Object templateSource) {
		// ignore
	}

}
