package cn.hutool.extra.template;

import cn.hutool.extra.template.engine.TemplateFactory;

/**
 * 模板工具类
 * 
 * @author looly
 * @since 4.1.0
 */
public class TemplateUtil {
	
	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象，使用默认配置<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 * 
	 * @return {@link TemplateEngine}
	 * @since 4.1.11
	 */
	public static TemplateEngine createEngine() {
		return TemplateFactory.create();
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 * 
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	public static TemplateEngine createEngine(TemplateConfig config) {
		return TemplateFactory.create(config);
	}
}
