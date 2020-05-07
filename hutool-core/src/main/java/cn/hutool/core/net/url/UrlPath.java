package cn.hutool.core.net.url;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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
	 * @param pathStr 初始化的路径字符串
	 * @param charset decode用的编码，null表示不做decode
	 * @return {@link UrlPath}
	 */
	public static UrlPath of(String pathStr, Charset charset) {
		final UrlPath urlPath = new UrlPath();
		urlPath.parse(pathStr, charset);
		return urlPath;
	}

	/**
	 * 是否path的末尾加 /
	 *
	 * @param withEngTag 是否path的末尾加 /
	 * @return this
	 */
	public UrlPath setWithEndTag(boolean withEngTag) {
		this.withEngTag = withEngTag;
		return this;
	}

	/**
	 * 获取path的节点列表
	 *
	 * @return 节点列表
	 */
	public List<String> getSegments() {
		return this.segments;
	}

	/**
	 * 获得指定节点
	 *
	 * @param index 节点位置
	 * @return 节点，无节点或者越界返回null
	 */
	public String getSegment(int index) {
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
	public UrlPath add(CharSequence segment) {
		add(segment, false);
		return this;
	}

	/**
	 * 添加到path最前面
	 *
	 * @param segment Path节点
	 * @return this
	 */
	public UrlPath addBefore(CharSequence segment) {
		add(segment, true);
		return this;
	}

	/**
	 * 解析path
	 *
	 * @param path    路径，类似于aaa/bb/ccc
	 * @param charset decode编码，null表示不解码
	 * @return this
	 */
	public UrlPath parse(String path, Charset charset) {
		UrlPath urlPath = new UrlPath();

		if (StrUtil.isNotEmpty(path)) {
			path = path.trim();

			// 原URL中以/结尾，则这个规则需保留，issue#I1G44J@Gitee
			if(StrUtil.endWith(path, CharUtil.SLASH)){
				this.withEngTag = true;
			}

			final StringTokenizer tokenizer = new StringTokenizer(path, "/");
			while (tokenizer.hasMoreTokens()) {
				add(URLUtil.decode(tokenizer.nextToken(), charset));
			}
		}

		return urlPath;
	}

	/**
	 * 构建path，前面带'/'
	 *
	 * @param charset encode编码，null表示不做encode
	 * @return 如果没有任何内容，则返回空字符串""
	 */
	public String build(Charset charset) {
		if (CollUtil.isEmpty(this.segments)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder builder = new StringBuilder();
		for (String segment : segments) {
			builder.append(CharUtil.SLASH).append(URLUtil.encodeAll(segment, charset));
		}
		if (withEngTag || StrUtil.isEmpty(builder)) {
			builder.append(CharUtil.SLASH);
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
	private void add(CharSequence segment, boolean before) {
		final String seg = fixSegment(segment);
		if (null == seg) {
			return;
		}


		if (this.segments == null) {
			this.segments = new LinkedList<>();
		}
		if (before) {
			this.segments.add(0, seg);
		} else {
			this.segments.add(seg);
		}
	}

	/**
	 * 修正节点，包括去掉前后的/，去掉空白符
	 *
	 * @param segment 节点
	 * @return 修正后的节点
	 */
	private static String fixSegment(CharSequence segment) {
		if (StrUtil.isEmpty(segment) || "/".contentEquals(segment)) {
			return null;
		}

		String segmentStr = StrUtil.str(segment);
		segmentStr = StrUtil.trim(segmentStr);
		segmentStr = StrUtil.removePrefix(segmentStr, "/");
		segmentStr = StrUtil.removeSuffix(segmentStr, "/");
		segmentStr = StrUtil.trim(segmentStr);
		return segmentStr;
	}
}
