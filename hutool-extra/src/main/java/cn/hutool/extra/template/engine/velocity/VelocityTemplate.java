package cn.hutool.extra.template.engine.velocity;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.AbstractTemplate;

/**
 * Velocity模板包装
 * 
 * @author looly
 *
 */
public class VelocityTemplate extends AbstractTemplate implements Serializable {
	private static final long serialVersionUID = -132774960373894911L;

	private org.apache.velocity.Template rawTemplate;
	private String charset;
	
	/**
	 * 包装Velocity模板
	 * 
	 * @param template Velocity的模板对象 {@link org.apache.velocity.Template}
	 * @return {@link VelocityTemplate}
	 */
	public static VelocityTemplate wrap(org.apache.velocity.Template template) {
		return (null == template) ? null : new VelocityTemplate(template);
	}
	
	/**
	 * 构造
	 * 
	 * @param rawTemplate Velocity模板对象
	 */
	public VelocityTemplate(org.apache.velocity.Template rawTemplate) {
		this.rawTemplate = rawTemplate;
	}

	@Override
	public void render(Map<String, Object> bindingMap, Writer writer) {
		rawTemplate.merge(toContext(bindingMap), writer);
	}

	@Override
	public void render(Map<String, Object> bindingMap, OutputStream out) {
		if(null == charset) {
			loadEncoding();
		}
		render(bindingMap, IoUtil.getWriter(out, this.charset));
	}

	/**
	 * 将Map转为VelocityContext
	 * 
	 * @param bindingMap 参数绑定的Map
	 * @return {@link VelocityContext}
	 */
	private VelocityContext toContext(Map<String, Object> bindingMap) {
		return new VelocityContext(bindingMap);
	}
	
	/**
	 * 加载可用的Velocity中预定义的编码
	 */
	private void loadEncoding() {
		String charset = (String) Velocity.getProperty(Velocity.OUTPUT_ENCODING);
		if(StrUtil.isEmpty(charset)) {
			charset = (String) Velocity.getProperty(Velocity.INPUT_ENCODING);
		}
		this.charset = StrUtil.isEmpty(charset) ? CharsetUtil.UTF_8 : charset;
	}
}
