package com.xiaoleilu.hutool.extra;

import java.io.Writer;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.Matcher;

/**
 * Beetl模板引擎工具类<br>
 * http://git.oschina.net/xiandafu/beetl2.0
 * 文档：http://ibeetl.com/guide/beetl.html
 * 
 * @author Looly
 */
public final class BeetlUtil {
	
	/**
	 * 创建默认 {@link GroupTemplate}
	 * @return {@link GroupTemplate}
	 */
	public static GroupTemplate createGroupTemplate(){
		return new GroupTemplate();
	}
	
	/**
	 * 创建自定义的 {@link GroupTemplate}
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @param conf {@link Configuration} 配置文件
	 * @return {@link GroupTemplate}
	 */
	public static GroupTemplate createGroupTemplate(ResourceLoader loader, Configuration conf){
		return new GroupTemplate(loader, conf);
	}
	
	/**
	 * 获得模板
	 * @param groupTemplate {@link GroupTemplate}
	 * @param source 模板资源，根据不同的 {@link ResourceLoader} 加载不同的模板资源
	 * @return {@link Template}
	 */
	public static Template getTemplate(GroupTemplate groupTemplate, String source){
		return groupTemplate.getTemplate(source);
	}
	
	/**
	 * 渲染模板
	 * @param template {@link Template}
	 * @param bindingMap 绑定参数
	 * @return 渲染后的内容
	 */
	public static String render(Template template, Map<String, Object> bindingMap){
		template.binding(bindingMap);
		return template.render();
	}
	
	/**
	 * 渲染模板
	 * @param template {@link Template}
	 * @param bindingMap 绑定参数
	 * @param writer {@link Writer} 渲染后写入的目标Writer
	 * @return {@link Writer}
	 */
	public static Writer render(Template template, Map<String, Object> bindingMap, Writer writer){
		template.binding(bindingMap);
		template.renderTo(writer);
		return writer;
	}
	
	/**
	 * 开始构建 {@link ResourceLoaderBuilder}，调用{@link ResourceLoaderBuilder#build()}完成构建
	 * @return {@link ResourceLoaderBuilder}
	 */
	public static ResourceLoaderBuilder resourceLoaderBuilder(){
		return new ResourceLoaderBuilder();
	}
	
	/**
	 * ResourceLoader构建器
	 * @author Looly
	 *
	 */
	public static class ResourceLoaderBuilder{
		private CompositeResourceLoader compositeResourceLoader = new CompositeResourceLoader();
		
		/**
		 * 创建
		 * @return {@link ResourceLoaderBuilder}
		 */
		public static ResourceLoaderBuilder create(){
			return new ResourceLoaderBuilder();
		}
		
		/**
		 * 添加一个资源加载器
		 * @param matcher {@link Matcher} 匹配器
		 * @param resourceLoader {@link ResourceLoader} 匹配时对应的资源加载器
		 * @return {@link ResourceLoaderBuilder}
		 */
		public ResourceLoaderBuilder add(Matcher matcher, ResourceLoader resourceLoader){
			compositeResourceLoader.addResourceLoader(matcher, resourceLoader);
			return this;
		}
		
		/**
		 * 构建
		 * @return {@link ResourceLoader} 资源加载器
		 */
		public ResourceLoader build(){
			return compositeResourceLoader;
		}
	}
}
