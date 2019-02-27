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
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象，使用默认配置
	 * 
	 * @return {@link TemplateEngine}
	 * @since 4.1.11
	 */
	public static TemplateEngine createEngine() {
		return createEngine(new TemplateConfig());
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
	 * 
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	public static TemplateEngine createEngine(TemplateConfig config) {
		return TemplateFactory.create(config);
	}
}
