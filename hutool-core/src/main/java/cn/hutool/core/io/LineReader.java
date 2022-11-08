package cn.hutool.core.io;

import cn.hutool.core.util.ArrayUtil;

import java.io.IOException;
import java.io.Reader;

/**
 * 行读取器，类似于BufferedInputStream，支持注释和多行转义
 * TODO 待实现
 *
 * @author looly
 */
public class LineReader extends ReaderWrapper {

	/**
	 * 注释标识符
	 */
	private char[] commentFlags;

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}
	 */
	public LineReader(final Reader reader) {
		super(reader);
	}

	/**
	 * 设置注释行标识符
	 *
	 * @param commentFlags 注释行标识符
	 * @return this
	 */
	public LineReader setCommentFlags(final char... commentFlags) {
		if (ArrayUtil.isEmpty(commentFlags)) {
			// 无注释行
			this.commentFlags = null;
		}
		this.commentFlags = ArrayUtil.copy(commentFlags, new char[commentFlags.length]);
		return this;
	}

	/**
	 * 读取一行
	 *
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public String readLine() throws IOException {
		return null;
	}
}
