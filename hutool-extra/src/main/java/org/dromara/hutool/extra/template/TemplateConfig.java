/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.template;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;

import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;

/**
 * 模板配置
 *
 * @author looly
 * @since 4.1.0
 */
public class TemplateConfig implements Serializable {
	private static final long serialVersionUID = 2933113779920339523L;

	public static final TemplateConfig DEFAULT = new TemplateConfig();

	/**
	 * 编码
	 */
	private Charset charset;
	/**
	 * 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 */
	private String path;
	/**
	 * 模板资源加载方式
	 */
	private ResourceMode resourceMode;
	/**
	 * 自定义引擎，当多个jar包引入时，可以自定使用的默认引擎
	 */
	private Class<? extends TemplateEngine> customEngine;

	/**
	 * 默认构造，使用UTF8编码，默认从ClassPath获取模板
	 */
	public TemplateConfig() {
		this(null);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param path 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 */
	public TemplateConfig(final String path) {
		this(path, ResourceMode.STRING);
	}

	/**
	 * 构造，默认UTF-8编码
	 *
	 * @param path         模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 * @param resourceMode 模板资源加载方式
	 */
	public TemplateConfig(final String path, final ResourceMode resourceMode) {
		this(CharsetUtil.UTF_8, path, resourceMode);
	}

	/**
	 * 构造
	 *
	 * @param charset      编码
	 * @param path         模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
	 * @param resourceMode 模板资源加载方式
	 */
	public TemplateConfig(final Charset charset, final String path, final ResourceMode resourceMode) {
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
	 * 获取编码
	 *
	 * @return 编码
	 * @since 4.1.11
	 */
	public String getCharsetStr() {
		if (null == this.charset) {
			return null;
		}
		return this.charset.toString();
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 */
	public void setCharset(final Charset charset) {
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
	public void setPath(final String path) {
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
	public void setResourceMode(final ResourceMode resourceMode) {
		this.resourceMode = resourceMode;
	}

	/**
	 * 获取自定义引擎，null表示系统自动判断
	 *
	 * @return 自定义引擎，null表示系统自动判断
	 * @since 5.2.1
	 */
	public Class<? extends TemplateEngine> getCustomEngine() {
		return customEngine;
	}


	/**
	 * 设置自定义引擎，null表示系统自动判断
	 *
	 * @param customEngine 自定义引擎，null表示系统自动判断
	 * @return this
	 * @since 5.2.1
	 */
	public TemplateConfig setCustomEngine(final Class<? extends TemplateEngine> customEngine) {
		this.customEngine = customEngine;
		return this;
	}

	/**
	 * 资源加载方式枚举
	 *
	 * @author looly
	 */
	public enum ResourceMode {
		/**
		 * 从ClassPath加载模板
		 */
		CLASSPATH,
		/**
		 * 从File目录加载模板
		 */
		FILE,
		/**
		 * 从WebRoot目录加载模板
		 */
		WEB_ROOT,
		/**
		 * 从模板文本加载模板
		 */
		STRING,
		/**
		 * 复合加载模板（分别从File、ClassPath、Web-root、String方式尝试查找模板）
		 */
		COMPOSITE
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o){
			return true;
		}
		if (o == null || getClass() != o.getClass()){
			return false;
		}
		final TemplateConfig that = (TemplateConfig) o;
		return Objects.equals(charset, that.charset) &&
				Objects.equals(path, that.path) &&
				resourceMode == that.resourceMode &&
				Objects.equals(customEngine, that.customEngine);
	}

	@Override
	public int hashCode() {
		return Objects.hash(charset, path, resourceMode, customEngine);
	}
}
