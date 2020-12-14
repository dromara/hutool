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
	public Object findTemplateSource(String name) {
		return name;
	}

	@Override
	public long getLastModified(Object templateSource) {
		return System.currentTimeMillis();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) {
		return new StringReader((String) templateSource);
	}

	@Override
	public void closeTemplateSource(Object templateSource) {
		// ignore
	}

}
