package cn.hutool.extra.template.engine.freemarker;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Freemarker模板实现
 *
 * @author looly
 */
public class FreemarkerTemplate implements Template, Serializable{
	private static final long serialVersionUID = -8157926902932567280L;

	freemarker.template.Template rawTemplate;

	/**
	 * 包装Freemarker模板
	 *
	 * @param beetlTemplate Beetl的模板对象 {@link freemarker.template.Template}
	 * @return this
	 */
	public static FreemarkerTemplate wrap(final freemarker.template.Template beetlTemplate) {
		return (null == beetlTemplate) ? null : new FreemarkerTemplate(beetlTemplate);
	}

	/**
	 * 构造
	 *
	 * @param freemarkerTemplate Beetl的模板对象 {@link freemarker.template.Template}
	 */
	public FreemarkerTemplate(final freemarker.template.Template freemarkerTemplate) {
		this.rawTemplate = freemarkerTemplate;
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final Writer writer) {
		try {
			rawTemplate.process(bindingMap, writer);
		} catch (final freemarker.template.TemplateException e) {
			throw new TemplateException(e);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void render(final Map<?, ?> bindingMap, final OutputStream out) {
		render(bindingMap, IoUtil.toWriter(out, Charset.forName(this.rawTemplate.getEncoding())));
	}

}
