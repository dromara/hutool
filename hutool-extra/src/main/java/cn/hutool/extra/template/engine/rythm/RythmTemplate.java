package cn.hutool.extra.template.engine.rythm;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.extra.template.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Rythm模板包装
 *
 * @author looly
 *
 */
public class RythmTemplate implements Template, Serializable {
	private static final long serialVersionUID = -132774960373894911L;

	private final org.rythmengine.template.ITemplate rawTemplate;

	/**
	 * 包装Rythm模板
	 *
	 * @param template Rythm的模板对象 {@link org.rythmengine.template.ITemplate}
	 * @return {@code RythmTemplate}
	 */
	public static RythmTemplate wrap(final org.rythmengine.template.ITemplate template) {
		return (null == template) ? null : new RythmTemplate(template);
	}

	/**
	 * 构造
	 *
	 * @param rawTemplate Velocity模板对象
	 */
	public RythmTemplate(final org.rythmengine.template.ITemplate rawTemplate) {
		this.rawTemplate = rawTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.__setRenderArgs(map);
		rawTemplate.render(writer);
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		rawTemplate.__setRenderArgs(bindingMap);
		rawTemplate.render(out);
	}
}
