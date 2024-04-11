package org.dromara.hutool.extra.template.engine.jte;

import gg.jte.CodeResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link CodeResolver} 字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author cdy
 * @since 6.0.0
 */
public class SimpleStringCodeResolver implements CodeResolver {

	private final Map<String, String> templates;

	/**
	 * 构造
	 * @param templates 参数
	 */
	public SimpleStringCodeResolver(final Map<String, String> templates) {
		this.templates = templates;
	}

	@Override
	public String resolve(final String name) {
		return templates.get(name);
	}

	@Override
	public long getLastModified(final String name) {
		return 0L;
	}

	@Override
	public List<String> resolveAllTemplateNames() {
		return new ArrayList<>(templates.keySet());
	}

}
