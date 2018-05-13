package cn.hutool.extra.template;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * 抽象模板接口
 * 
 * @author looly
 *
 */
public interface Template {

	/**
	 * 将模板与绑定参数融合后输出到
	 * 
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param writer 输出
	 */
	void render(Map<String, Object> bindingMap, Writer writer);

	/**
	 * 将模板与绑定参数融合后输出到
	 * 
	 * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
	 * @param out 输出
	 */
	void render(Map<String, Object> bindingMap, OutputStream out);
}
