package cn.hutool.extra.template.engine.enjoy;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import cn.hutool.extra.template.AbstractTemplate;

/**
 * Engoy模板实现
 * 
 * @author looly
 * @since 4.1.9
 */
public class EnjoyTemplate extends AbstractTemplate implements Serializable{
	private static final long serialVersionUID = -8157926902932567280L;

	com.jfinal.template.Template rawTemplate;
	
	/**
	 * 包装Enjoy模板
	 * 
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 * @return {@link EnjoyTemplate}
	 */
	public static EnjoyTemplate wrap(com.jfinal.template.Template EnjoyTemplate) {
		return (null == EnjoyTemplate) ? null : new EnjoyTemplate(EnjoyTemplate);
	}
	
	/**
	 * 构造
	 * 
	 * @param EnjoyTemplate Enjoy的模板对象 {@link com.jfinal.template.Template}
	 */
	public EnjoyTemplate(com.jfinal.template.Template EnjoyTemplate) {
		this.rawTemplate = EnjoyTemplate;
	}

	@Override
	public void render(Map<String, Object> bindingMap, Writer writer) {
		rawTemplate.render(bindingMap, writer);
	}

	@Override
	public void render(Map<String, Object> bindingMap, OutputStream out) {
		rawTemplate.render(bindingMap, out);
	}

}
