package cn.hutool.extra.template.engine.beetl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.CharsetUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.Matcher;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Beetl模板引擎工具类<br>
 * http://git.oschina.net/xiandafu/beetl2.0 <br>
 * 文档：http://ibeetl.com/guide/beetl.html
 * 
 * @author Looly
 * @deprecated 使用TemplateUtil替代
 */
@Deprecated
public final class BeetlUtil {

	/**
	 * 创建默认模板组{@link GroupTemplate}，默认的模板组从ClassPath中读取
	 * 
	 * @return {@link GroupTemplate}
	 */
	public static GroupTemplate createGroupTemplate() {
		return new GroupTemplate();
	}

	/**
	 * 创建字符串的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createStrGroupTemplate() {
		return createGroupTemplate(new StringTemplateResourceLoader());
	}

	/**
	 * 创建WebApp的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createWebAppGroupTemplate() {
		return createGroupTemplate(new WebAppResourceLoader());
	}

	/**
	 * 创建字符串的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @param path 相对ClassPath的路径
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createClassPathGroupTemplate(String path) {
		return createGroupTemplate(new ClasspathResourceLoader(path));
	}

	/**
	 * 创建文件目录的模板组 {@link GroupTemplate}，配置文件使用全局默认，使用UTF-8编码<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @param dir 目录路径（绝对路径）
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createFileGroupTemplate(String dir) {
		return createFileGroupTemplate(dir, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 创建文件目录的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @param dir 目录路径（绝对路径）
	 * @param charset 读取模板文件的编码
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createFileGroupTemplate(String dir, Charset charset) {
		return createGroupTemplate(new FileResourceLoader(dir, charset.name()));
	}

	/**
	 * 创建自定义的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 * 
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	public static GroupTemplate createGroupTemplate(ResourceLoader<?> loader) {
		try {
			return createGroupTemplate(loader, Configuration.defaultConfiguration());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建自定义的 {@link GroupTemplate}
	 * 
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @param conf {@link Configuration} 配置文件
	 * @return {@link GroupTemplate}
	 */
	public static GroupTemplate createGroupTemplate(ResourceLoader<?> loader, Configuration conf) {
		return new GroupTemplate(loader, conf);
	}

	/**
	 * 获得模板
	 * 
	 * @param groupTemplate {@link GroupTemplate}
	 * @param source 模板资源，根据不同的 {@link ResourceLoader} 加载不同的模板资源
	 * @return {@link Template}
	 */
	public static Template getTemplate(GroupTemplate groupTemplate, String source) {
		return groupTemplate.getTemplate(source);
	}

	/**
	 * 获得字符串模板
	 * 
	 * @param source 模板内容
	 * @return 模板
	 * @since 3.2.0
	 */
	public static Template getStrTemplate(String source) {
		return getTemplate(createStrGroupTemplate(), source);
	}

	/**
	 * 获得ClassPath模板
	 * 
	 * @param path ClassPath路径
	 * @param templateFileName 模板内容
	 * @return 模板
	 * @since 3.2.0
	 */
	public static Template getClassPathTemplate(String path, String templateFileName) {
		return getTemplate(createClassPathGroupTemplate(path), templateFileName);
	}

	/**
	 * 获得本地文件模板
	 * 
	 * @param dir 目录绝对路径
	 * @param templateFileName 模板内容
	 * @return 模板
	 * @since 3.2.0
	 */
	public static Template getFileTemplate(String dir, String templateFileName) {
		return getTemplate(createFileGroupTemplate(dir), templateFileName);
	}

	// ------------------------------------------------------------------------------------- Render
	/**
	 * 渲染模板
	 * 
	 * @param template {@link Template}
	 * @param bindingMap 绑定参数
	 * @return 渲染后的内容
	 */
	public static String render(Template template, Map<String, Object> bindingMap) {
		template.binding(bindingMap);
		return template.render();
	}

	/**
	 * 渲染模板，如果为相对路径，则渲染ClassPath模板，否则渲染本地文件模板
	 * 
	 * @param path 路径
	 * @param templateFileName 模板文件名
	 * @param bindingMap 绑定参数
	 * @return 渲染后的内容
	 * @since 3.2.0
	 */
	public static String render(String path, String templateFileName, Map<String, Object> bindingMap) {
		if (FileUtil.isAbsolutePath(path)) {
			return render(getFileTemplate(path, templateFileName), bindingMap);
		} else {
			return render(getClassPathTemplate(path, templateFileName), bindingMap);
		}
	}

	/**
	 * 渲染模板
	 * 
	 * @param templateContent 模板内容
	 * @param bindingMap 绑定参数
	 * @return 渲染后的内容
	 * @since 3.2.0
	 */
	public static String renderFromStr(String templateContent, Map<String, Object> bindingMap) {
		return render(getStrTemplate(templateContent), bindingMap);
	}

	/**
	 * 渲染模板
	 * 
	 * @param templateContent {@link Template}
	 * @param bindingMap 绑定参数
	 * @param writer {@link Writer} 渲染后写入的目标Writer
	 * @return {@link Writer}
	 */
	public static Writer render(Template templateContent, Map<String, Object> bindingMap, Writer writer) {
		templateContent.binding(bindingMap);
		templateContent.renderTo(writer);
		return writer;
	}

	/**
	 * 渲染模板
	 * 
	 * @param templateContent 模板内容
	 * @param bindingMap 绑定参数
	 * @param writer {@link Writer} 渲染后写入的目标Writer
	 * @return {@link Writer}
	 */
	public static Writer renderFromStr(String templateContent, Map<String, Object> bindingMap, Writer writer) {
		return render(getStrTemplate(templateContent), bindingMap, writer);
	}

	/**
	 * 开始构建 {@link ResourceLoaderBuilder}，调用{@link ResourceLoaderBuilder#build()}完成构建
	 * 
	 * @return {@link ResourceLoaderBuilder}
	 */
	public static ResourceLoaderBuilder resourceLoaderBuilder() {
		return new ResourceLoaderBuilder();
	}

	/**
	 * ResourceLoader构建器
	 * 
	 * @author Looly
	 *
	 */
	public static class ResourceLoaderBuilder {
		private final CompositeResourceLoader compositeResourceLoader = new CompositeResourceLoader();

		/**
		 * 创建
		 * 
		 * @return {@link ResourceLoaderBuilder}
		 */
		public static ResourceLoaderBuilder create() {
			return new ResourceLoaderBuilder();
		}

		/**
		 * 添加一个资源加载器
		 * 
		 * @param matcher {@link Matcher} 匹配器
		 * @param resourceLoader {@link ResourceLoader} 匹配时对应的资源加载器
		 * @return {@link ResourceLoaderBuilder}
		 */
		public ResourceLoaderBuilder add(Matcher matcher, ResourceLoader<?> resourceLoader) {
			compositeResourceLoader.addResourceLoader(matcher, resourceLoader);
			return this;
		}

		/**
		 * 构建
		 * 
		 * @return {@link ResourceLoader} 资源加载器
		 */
		public ResourceLoader<?> build() {
			return compositeResourceLoader;
		}
	}
}
