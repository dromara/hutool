package cn.hutool.extra.template.engine.beetl;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import cn.hutool.extra.template.Template;

/**
 * Beetl模板实现
 * 
 * @author looly
 */
public class BeetlTemplate implements Template{
	
	org.beetl.core.Template beetlTemplate;
	
	/**
	 * 包装Beetl模板
	 * 
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 * @return {@link BeetlTemplate}
	 */
	public static BeetlTemplate wrap(org.beetl.core.Template beetlTemplate) {
		return (null == beetlTemplate) ? null : new BeetlTemplate(beetlTemplate);
	}
	
	/**
	 * 构造
	 * 
	 * @param beetlTemplate Beetl的模板对象 {@link org.beetl.core.Template}
	 */
	public BeetlTemplate(org.beetl.core.Template beetlTemplate) {
		this.beetlTemplate = beetlTemplate;
	}

	@Override
	public void render(Map<String, Object> bindingMap, Writer writer) {
		beetlTemplate.binding(bindingMap);
		beetlTemplate.renderTo(writer);
	}

	@Override
	public void render(Map<String, Object> bindingMap, OutputStream out) {
		beetlTemplate.binding(bindingMap);
		beetlTemplate.renderTo(out);
	}

}
