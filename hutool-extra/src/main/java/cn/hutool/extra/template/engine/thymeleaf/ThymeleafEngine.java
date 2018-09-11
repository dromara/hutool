package cn.hutool.extra.template.engine.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;

/**
 * Thymeleaf模板引擎实现
 * 
 * @author looly
 * @since 4.1.11
 */
public class ThymeleafEngine implements Engine {

	TemplateEngine engine;
	TemplateConfig config;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public ThymeleafEngine() {
		this(new TemplateConfig());
	}

	/**
	 * 构造
	 * 
	 * @param config 模板配置
	 */
	public ThymeleafEngine(TemplateConfig config) {
		this(createEngine(config));
		this.config = config;
	}

	/**
	 * 构造
	 * 
	 * @param engine {@link TemplateEngine}
	 */
	public ThymeleafEngine(TemplateEngine engine) {
		this.engine = engine;
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public Template getTemplate(String resource) {
		return ThymeleafTemplate.wrap(this.engine, resource, (null == this.config) ? null : this.config.getCharset());
	}

	/**
	 * 创建引擎
	 * 
	 * @param config 模板配置
	 * @return {@link TemplateEngine}
	 */
	private static TemplateEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}
		
		ITemplateResolver resolver = null;
		switch (config.getResourceMode()) {
		case CLASSPATH:
			final ClassLoaderTemplateResolver classLoaderResolver = new ClassLoaderTemplateResolver();
			classLoaderResolver.setCharacterEncoding(config.getCharsetStr());
			classLoaderResolver.setTemplateMode(TemplateMode.HTML);
			resolver = classLoaderResolver;
			break;
		case FILE:
			final FileTemplateResolver fileResolver = new FileTemplateResolver();
			fileResolver.setCharacterEncoding(config.getCharsetStr());
			fileResolver.setTemplateMode(TemplateMode.HTML);
			resolver = fileResolver;
			break;
		case WEB_ROOT:
			final FileTemplateResolver webRootResolver = new FileTemplateResolver();
			webRootResolver.setCharacterEncoding(config.getCharsetStr());
			webRootResolver.setTemplateMode(TemplateMode.HTML);
			webRootResolver.setPrefix(FileUtil.getWebRoot().getAbsolutePath());
			resolver = webRootResolver;
			break;
		case STRING:
			resolver = new StringTemplateResolver();
			break;
		case COMPOSITE:
			resolver = new DefaultTemplateResolver();
			break;
		default:
			resolver = new DefaultTemplateResolver();
			break;
		}
		
		final TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		return engine;
	}
}
