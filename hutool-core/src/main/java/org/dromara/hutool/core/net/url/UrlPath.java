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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * URL中Path部分的封装
 *
 * @author looly
 * @since 5.3.1
 */
public class UrlPath {

	private List<String> segments;
	private boolean withEngTag;

	/**
	 * 构建UrlPath
	 *
	 * @return UrlPath
	 * @since 6.0.0
	 */
	public static UrlPath of() {
		return new UrlPath();
	}

	/**
	 * 构建UrlPath
	 *
	 * @param pathStr 初始化的路径字符串
	 * @param charset decode用的编码，null表示不做decode
	 * @return UrlPath
	 */
	public static UrlPath of(final CharSequence pathStr, final Charset charset) {
		return of().parse(pathStr, charset);
	}

	/**
	 * 是否path的末尾加 /
	 *
	 * @param withEngTag 是否path的末尾加 /
	 * @return this
	 */
	public UrlPath setWithEndTag(final boolean withEngTag) {
		this.withEngTag = withEngTag;
		return this;
	}

	/**
	 * 获取path的节点列表，如果列表为空，返回{@link ListUtil#empty()}
	 *
	 * @return 节点列表
	 */
	public List<String> getSegments() {
		return ObjUtil.defaultIfNull(this.segments, ListUtil.empty());
	}

	/**
	 * 获得指定节点
	 *
	 * @param index 节点位置
	 * @return 节点，无节点或者越界返回null
	 */
	public String getSegment(final int index) {
		if (null == this.segments || index >= this.segments.size()) {
			return null;
		}
		return this.segments.get(index);
	}

	/**
	 * 添加到path最后面
	 *
	 * @param segment Path节点
	 * @return this
	 */
	public UrlPath add(final CharSequence segment) {
		addInternal(fixPath(segment), false);
		return this;
	}

	/**
	 * 添加到path最前面
	 *
	 * @param segment Path节点
	 * @return this
	 */
	public UrlPath addBefore(final CharSequence segment) {
		addInternal(fixPath(segment), true);
		return this;
	}

	/**
	 * 解析path
	 *
	 * @param path    路径，类似于aaa/bb/ccc或/aaa/bbb/ccc
	 * @param charset decode编码，null表示不解码
	 * @return this
	 */
	public UrlPath parse(CharSequence path, final Charset charset) {
		if (StrUtil.isNotEmpty(path)) {
			// 原URL中以/结尾，则这个规则需保留，issue#I1G44J@Gitee
			if (StrUtil.endWith(path, CharUtil.SLASH)) {
				this.withEngTag = true;
			}

			path = fixPath(path);
			if (StrUtil.isNotEmpty(path)) {
				final List<String> split = SplitUtil.split(path, StrUtil.SLASH);
				for (final String seg : split) {
					addInternal(URLDecoder.decodeForPath(seg, charset), false);
				}
			}
		}

		return this;
	}

	/**
	 * 构建path，前面带'/'<br>
	 * <pre>
	 *     path = path-abempty / path-absolute / path-noscheme / path-rootless / path-empty
	 * </pre>
	 *
	 * @param charset encode编码，null表示不做encode
	 * @return 如果没有任何内容，则返回空字符串""
	 */
	public String build(final Charset charset) {
		return build(charset, true);
	}

	/**
	 * 构建path，前面带'/'<br>
	 * <pre>
	 *     path = path-abempty / path-absolute / path-noscheme / path-rootless / path-empty
	 * </pre>
	 *
	 * @param charset       encode编码，null表示不做encode
	 * @param encodePercent 是否编码`%`
	 * @return 如果没有任何内容，则返回空字符串""
	 * @since 5.8.0
	 */
	public String build(final Charset charset, final boolean encodePercent) {
		if (CollUtil.isEmpty(this.segments)) {
			// 没有节点的path取决于是否末尾追加/，如果不追加返回空串，否则返回/
			return withEngTag ? StrUtil.SLASH : StrUtil.EMPTY;
		}

		final char[] safeChars = encodePercent ? null : new char[]{'%'};
		final StringBuilder builder = new StringBuilder();
		for (final String segment : segments) {
			if (builder.length() == 0) {
				// 根据https://www.ietf.org/rfc/rfc3986.html#section-3.3定义
				// path的第一部分不允许有":"，其余部分允许
				// 在此处的Path部分特指host之后的部分，即不包含第一部分
				builder.append(CharUtil.SLASH).append(RFC3986.SEGMENT_NZ_NC.encode(segment, charset, safeChars));
			} else {
				builder.append(CharUtil.SLASH).append(RFC3986.SEGMENT.encode(segment, charset, safeChars));
			}
		}

		if (withEngTag) {
			if (StrUtil.isEmpty(builder)) {
				// 空白追加是保证以/开头
				builder.append(CharUtil.SLASH);
			} else if (!StrUtil.endWith(builder, CharUtil.SLASH)) {
				// 尾部没有/则追加，否则不追加
				builder.append(CharUtil.SLASH);
			}
		}

		return builder.toString();
	}

	@Override
	public String toString() {
		return build(null);
	}

	/**
	 * 增加节点
	 *
	 * @param segment 节点
	 * @param before  是否在前面添加
	 */
	private void addInternal(final CharSequence segment, final boolean before) {
		if (this.segments == null) {
			this.segments = new LinkedList<>();
		}

		final String seg = StrUtil.str(segment);
		if (before) {
			this.segments.add(0, seg);
		} else {
			this.segments.add(seg);
		}
	}

	/**
	 * 修正路径，包括去掉前后的/，去掉空白符
	 *
	 * @param path 节点或路径path
	 * @return 修正后的路径
	 */
	private static String fixPath(final CharSequence path) {
		Assert.notNull(path, "Path segment must be not null!");
		if ("/".contentEquals(path)) {
			return StrUtil.EMPTY;
		}

		String segmentStr = StrUtil.trim(path);
		segmentStr = StrUtil.removePrefix(segmentStr, StrUtil.SLASH);
		segmentStr = StrUtil.removeSuffix(segmentStr, StrUtil.SLASH);
		segmentStr = StrUtil.trim(segmentStr);
		return segmentStr;
	}
}
