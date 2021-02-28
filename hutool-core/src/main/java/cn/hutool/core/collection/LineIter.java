package cn.hutool.core.collection;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;

/**
 * 将Reader包装为一个按照行读取的Iterator<br>
 * 此对象遍历结束后，应关闭之，推荐使用方式:
 *
 * <pre>
 * LineIterator it = null;
 * try {
 * 	it = new LineIterator(reader);
 * 	while (it.hasNext()) {
 * 		String line = it.nextLine();
 * 		// do something with line
 * 	}
 * } finally {
 * 		it.close();
 * }
 * </pre>
 *
 * 此类来自于Apache Commons io
 *
 * @author looly
 * @since 4.1.1
 */
public class LineIter implements Iterator<String>, Iterable<String>, Closeable, Serializable {
	private static final long serialVersionUID = 1L;

	/** The reader that is being read. */
	private final BufferedReader bufferedReader;
	/** The current line. */
	private String cachedLine;
	/** A flag indicating if the iterator has been fully read. */
	private boolean finished = false;

	/**
	 * 构造
	 *
	 * @param in {@link InputStream}
	 * @param charset 编码
	 * @throws IllegalArgumentException reader为null抛出此异常
	 */
	public LineIter(InputStream in, Charset charset) throws IllegalArgumentException {
		this(IoUtil.getReader(in, charset));
	}

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}对象，不能为null
	 * @throws IllegalArgumentException reader为null抛出此异常
	 */
	public LineIter(Reader reader) throws IllegalArgumentException {
		Assert.notNull(reader, "Reader must not be null");
		this.bufferedReader = IoUtil.getReader(reader);
	}

	// -----------------------------------------------------------------------
	/**
	 * 判断{@link Reader}是否可以存在下一行。 If there is an <code>IOException</code> then {@link #close()} will be called on this instance.
	 *
	 * @return {@code true} 表示有更多行
	 * @throws IORuntimeException IO异常
	 */
	@Override
	public boolean hasNext() throws IORuntimeException {
		if (cachedLine != null) {
			return true;
		} else if (finished) {
			return false;
		} else {
			try {
				while (true) {
					String line = bufferedReader.readLine();
					if (line == null) {
						finished = true;
						return false;
					} else if (isValidLine(line)) {
						cachedLine = line;
						return true;
					}
				}
			} catch (IOException ioe) {
				close();
				throw new IORuntimeException(ioe);
			}
		}
	}

	/**
	 * 返回下一行内容
	 *
	 * @return 下一行内容
	 * @throws NoSuchElementException 没有新行
	 */
	@Override
	public String next() throws NoSuchElementException {
		return nextLine();
	}

	/**
	 * 返回下一行
	 *
	 * @return 下一行
	 * @throws NoSuchElementException 没有更多行
	 */
	public String nextLine() throws NoSuchElementException {
		if (false == hasNext()) {
			throw new NoSuchElementException("No more lines");
		}
		String currentLine = this.cachedLine;
		this.cachedLine = null;
		return currentLine;
	}

	/**
	 * 关闭Reader
	 */
	@Override
	public void close() {
		finished = true;
		IoUtil.close(bufferedReader);
		cachedLine = null;
	}

	/**
	 * 不支持移除
	 *
	 * @throws UnsupportedOperationException 始终抛出此异常
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported on LineIterator");
	}

	/**
	 * 重写此方法来判断是否每一行都被返回，默认全部为true
	 *
	 * @param line 需要验证的行
	 * @return 是否通过验证
	 */
	protected boolean isValidLine(String line) {
		return true;
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}
}
