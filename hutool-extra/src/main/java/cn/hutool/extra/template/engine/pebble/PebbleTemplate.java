package cn.hutool.extra.template.engine.pebble;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateException;
import cn.hutool.extra.template.engine.velocity.VelocityTemplate;

import java.io.*;
import java.util.Map;

/**
 * @author：zooooooooy
 */
public class PebbleTemplate implements Template {

	private final io.pebbletemplates.pebble.template.PebbleTemplate template;

	public PebbleTemplate(io.pebbletemplates.pebble.template.PebbleTemplate template) {
		this.template = template;
	}

	/**
	 * 包装pebbleTemplate模板
	 * @param template
	 * @return
	 */
	public static PebbleTemplate wrap(final io.pebbletemplates.pebble.template.PebbleTemplate template) {
		return (null == template) ? null : new PebbleTemplate(template);
	}

	/**
	 * 渲染对象
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param writer 输出
	 */
	@Override
	public void render(Map<?, ?> bindingMap, Writer writer) {

		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		try {
			this.template.evaluate(writer, map);
		} catch (Exception e) {
			throw new TemplateException("pebble template parse failed, cause by: ", e);
		}
	}

	/**
	 * 渲染对象
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param out 输出
	 */
	@Override
	public void render(Map<?, ?> bindingMap, OutputStream out) {

		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		try {
			this.template.evaluate(new OutputStreamWriter(out), map);
		} catch (Exception e) {
			throw new TemplateException("pebble template parse failed, cause by: ", e);
		}

	}

}
