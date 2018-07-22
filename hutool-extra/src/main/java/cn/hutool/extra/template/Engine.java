package cn.hutool.extra.template;

/**
 * 引擎接口，通过实现此接口从而使用对应的模板引擎
 * 
 * @author looly
 */
public interface Engine {
	
	/**
	 * 获取模板引擎名
	 * @return 模板引擎名
	 * @since 4.1.3
	 */
	String getName();

	/**
	 * 获取模板
	 * 
	 * @param resource 资源，根据事先不同，此资源可以是模板本身，也可以是模板的相对路径
	 * @return 模板实现
	 */
	Template getTemplate(String resource);
	
}
