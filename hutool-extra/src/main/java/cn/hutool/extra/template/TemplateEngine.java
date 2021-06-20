package cn.hutool.extra.template;

/**
 * 引擎接口，通过实现此接口从而使用对应的模板引擎
 *
 * @author looly
 */
public interface TemplateEngine {

	/**
	 * 使用指定配置文件初始化模板引擎
	 *
	 * @param config 配置文件
	 * @return this
	 */
	TemplateEngine init(TemplateConfig config);

	/**
	 * 获取模板
	 *
	 * @param resource 资源，根据实现不同，此资源可以是模板本身，也可以是模板的相对路径
	 * @return 模板实现
	 */
	Template getTemplate(String resource);

}
