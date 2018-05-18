package cn.hutool.extra.template;

import java.nio.charset.Charset;

import cn.hutool.core.util.CharsetUtil;

/**
 * 模板配置
 * 
 * @author looly
 *
 */
public class TemplateConfig {
	
	/** 编码 */
	private Charset charset;
	/** 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径 */
	private String path;
	/** 模板资源加载方式 */
	private ResourceMode resourceMode;
	
	/**
	 * 默认构造，使用UTF8编码，默认从ClassPath获取模板
	 */
	public TemplateConfig() {
		this(CharsetUtil.CHARSET_UTF_8, null, ResourceMode.CLASSPATH);
	}
	
	/**
	 * 构造
	 * 
	 * @param charset 编码
	 * @param path 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 * @param resourceMode 模板资源加载方式
	 */
	public TemplateConfig(Charset charset, String path, ResourceMode resourceMode) {
		this.charset = charset;
		this.path = path;
		this.resourceMode = resourceMode;
	}

	/**
	 * 获取编码
	 * 
	 * @return 编码
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置编码
	 * 
	 * @param charset 编码
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * 获取模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 * 
	 * @return 模板路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 * 
	 * @param path 模板路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取模板资源加载方式
	 * 
	 * @return 模板资源加载方式
	 */
	public ResourceMode getResourceMode() {
		return resourceMode;
	}

	/**
	 * 设置模板资源加载方式
	 * 
	 * @param resourceMode 模板资源加载方式
	 */
	public void setResourceMode(ResourceMode resourceMode) {
		this.resourceMode = resourceMode;
	}

	/**
	 * 资源加载方式枚举
	 * 
	 * @author looly
	 */
	public static enum ResourceMode {
		/** 从ClassPath加载模板 */
		CLASSPATH,
		/** 从File目录加载模板 */
		FILE,
		/** 从WebRoot目录加载模板 */
		WEB_ROOT,
		/** 从模板文本加载模板 */
		STRING;
	};
}
