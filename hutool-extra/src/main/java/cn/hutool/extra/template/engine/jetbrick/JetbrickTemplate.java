package cn.hutool.extra.template.engine.jetbrick;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.template.AbstractTemplate;
import jetbrick.template.JetTemplate;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

/**
 * Jetbrick模板实现<br>
 * 见：https://github.com/subchen/jetbrick-template-2x
 *
 * @author looly
 * @since 5.7.21
 */
public class JetbrickTemplate extends AbstractTemplate implements Serializable{
	private static final long serialVersionUID = 1L;

	private final JetTemplate rawTemplate;

	/**
	 * 包装Jetbrick模板
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 * @return JetbrickTemplate
	 */
	public static JetbrickTemplate wrap(JetTemplate jetTemplate) {
		return (null == jetTemplate) ? null : new JetbrickTemplate(jetTemplate);
	}

	/**
	 * 构造
	 *
	 * @param jetTemplate Jetbrick的模板对象 {@link JetTemplate }
	 */
	public JetbrickTemplate(JetTemplate jetTemplate) {
		this.rawTemplate = jetTemplate;
	}

	@Override
	public void render(Map<?, ?> bindingMap, Writer writer) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.render(map, writer);
	}

	@Override
	public void render(Map<?, ?> bindingMap, OutputStream out) {
		final Map<String, Object> map = Convert.convert(new TypeReference<Map<String, Object>>() {}, bindingMap);
		rawTemplate.render(map, out);
	}

}
