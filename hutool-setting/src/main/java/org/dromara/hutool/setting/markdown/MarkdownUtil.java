/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.setting.markdown;

import org.commonmark.Extension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IORuntimeException;

import java.io.IOException;
import java.io.Reader;

/**
 * Markdown工具类，基于commonmark-java(https://github.com/commonmark/commonmark-java)封装
 *
 * @author Looly
 * @since 6.0.0
 */
public class MarkdownUtil {

	// region ----- ofXXX

	/**
	 * 创建并返回一个默认配置的Parser对象
	 * 该方法使用Parser的构造器模式来创建对象，便于外部进行进一步的配置和使用
	 *
	 * @param extensions 可选扩展，如{@code TablesExtension.create()}
	 * @return 默认配置的Parser对象
	 */
	public static Parser ofParser(final Extension... extensions) {
		return Parser.builder().extensions(ListUtil.of(extensions)).build();
	}

	/**
	 * 创建并返回一个HtmlRenderer实例
	 *
	 * @param extensions 可选扩展，如{@code TablesExtension.create()}
	 * @return HtmlRenderer实例，用于将Markdown文本渲染为HTML格式
	 */
	public static HtmlRenderer ofHtmlRender(final Extension... extensions) {
		return HtmlRenderer.builder().extensions(ListUtil.of(extensions)).build();
	}

	/**
	 * 创建并返回一个Markdown渲染器实例
	 *
	 * @return MarkdownRenderer 实例，用于处理Markdown渲染
	 */
	public static MarkdownRenderer ofMarkdownRender() {
		return MarkdownRenderer.builder().build();
	}
	// endregion

	// region ----- to

	/**
	 * 将Markdown文本转换为HTML格式
	 *
	 * @param markdown Markdown文本
	 * @return HTML字符串
	 */
	public static String markdownToHtml(final String markdown) {
		return renderHtml(parse(markdown));
	}

	/**
	 * 将HTML转换为Markdown
	 *
	 * @param html HTML文本
	 * @return Markdown字符串
	 */
	public static String htmlToMarkdown(final String html) {
		return renderMarkdown(parse(html));
	}

	// endregion

	// region ----- parse

	/**
	 * 解析Markdown字符串内容为树对象
	 *
	 * @param content 待解析的字符串内容
	 * @return 解析后的节点对象
	 */
	public static Node parse(final String content) {
		return ofParser().parse(content);
	}

	/**
	 * 从给定的Reader中读取Markdown并解析为树结构对象
	 *
	 * @param reader 要解析的字符输入流
	 * @return 返回解析后的Node对象，Node的具体类型依赖于解析器的实现
	 * @throws IORuntimeException 如果在解析过程中发生IO错误，将包装原始的IOException并抛出运行时异常
	 */
	public static Node parse(final Reader reader) throws IORuntimeException {
		try {
			return ofParser().parseReader(reader);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
	// endregion

	// region ----- render

	/**
	 * 将Markdown树渲染为HTML字符串并追加到指定的Appendable对象中
	 *
	 * @param node       Markdown树
	 * @param appendable 要追加渲染结果的字符串接收器，可以是StringBuilder、StringBuffer或者Writer等
	 */
	public static void renderHtml(final Node node, final Appendable appendable) {
		// 调用render方法，使用htmlRender渲染器对node进行渲染，并将结果追加到appendable中
		render(ofHtmlRender(), node, appendable);
	}

	/**
	 * 将Markdown树渲染为HTML字符串
	 *
	 * @param node Markdown树
	 * @return HTML字符串
	 */
	public static String renderHtml(final Node node) {
		return render(ofHtmlRender(), node);
	}

	/**
	 * 将Markdown树渲染为Markdown并追加到指定的Appendable对象中
	 *
	 * @param node       Markdown树
	 * @param appendable 要追加渲染结果的字符串接收器，可以是StringBuilder、StringBuffer或者Writer等
	 */
	public static void renderMarkdown(final Node node, final Appendable appendable) {
		render(ofMarkdownRender(), node, appendable);
	}

	/**
	 * 将Markdown树渲染为Markdown文本
	 *
	 * @param node Markdown树
	 * @return Markdown字符串
	 */
	public static String renderMarkdown(final Node node) {
		return render(ofMarkdownRender(), node);
	}

	/**
	 * 通过指定的渲染器和节点，执行渲染操作并将结果追加到指定的文本容器中。
	 *
	 * @param renderer   实现渲染逻辑的渲染器对象
	 * @param node       待渲染的Markdown树节点
	 * @param appendable 用于接收渲染结果的文本容器，支持追加操作。
	 */
	public static void render(final Renderer renderer, final Node node, final Appendable appendable) {
		renderer.render(node, appendable);
	}

	/**
	 * 通过指定的渲染器和节点，执行渲染并返回String。
	 *
	 * @param renderer 实现渲染逻辑的渲染器对象
	 * @param node     待渲染的Markdown树节点
	 * @return 渲染后的String
	 */
	public static String render(final Renderer renderer, final Node node) {
		return renderer.render(node);
	}

	// endregion
}
