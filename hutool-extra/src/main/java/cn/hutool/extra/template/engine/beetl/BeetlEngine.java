package cn.hutool.extra.template.engine.beetl;

import org.beetl.core.GroupTemplate;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.engine.Engine;

/**
 * Beetl模板引擎封装
 * 
 * @author looly
 */
public class BeetlEngine implements Engine{
	
	private GroupTemplate engine;
	
	/**
	 * 构造
	 * 
	 * @param engine {@link GroupTemplate}
	 */
	public BeetlEngine(GroupTemplate engine) {
		this.engine = engine;
	}
	
	@Override
	public Template getTemplate(String resource) {
		return BeetlTemplate.wrap(engine.getTemplate(resource));
	}
}
