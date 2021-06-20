package cn.hutool.extra.template.engine.wit;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.template.AbstractTemplate;
import org.febit.wit.Template;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Wit模板实现
 *
 * @author looly
 */
public class WitTemplate extends AbstractTemplate implements Serializable{
	private static final long serialVersionUID = 1L;

	private final Template rawTemplate;

	/**
	 * 包装Wit模板
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 * @return WitTemplate
	 */
	public static WitTemplate wrap(Template witTemplate) {
		return (null == witTemplate) ? null : new WitTemplate(witTemplate);
	}

	/**
	 * 构造
	 *
	 * @param witTemplate Wit的模板对象 {@link Template}
	 */
	public WitTemplate(Template witTemplate) {
		this.rawTemplate = witTemplate;
	}

	@Override
	public void render(Map<?, ?> bindingMap, Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, writer);
	}

	@Override
	public void render(Map<?, ?> bindingMap, OutputStream out) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.merge(map, out);
	}

}
