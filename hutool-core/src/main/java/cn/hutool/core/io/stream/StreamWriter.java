package cn.hutool.core.io.stream;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * {@link OutputStream}写出器
 *
 * @author looly
 */
public class StreamWriter {

	/**
	 * 创建写出器
	 *
	 * @param out             {@link OutputStream}
	 * @param closeAfterWrite 写出结束后是否关闭流
	 * @return StreamReader
	 */
	public static StreamWriter of(final OutputStream out, final boolean closeAfterWrite) {
		return new StreamWriter(out, closeAfterWrite);
	}

	private final OutputStream out;
	private final boolean closeAfterWrite;

	/**
	 * 构造
	 *
	 * @param out             {@link OutputStream}
	 * @param closeAfterWrite 写出结束后是否关闭流
	 */
	public StreamWriter(final OutputStream out, final boolean closeAfterWrite) {
		this.out = out;
		this.closeAfterWrite = closeAfterWrite;
	}

	/**
	 * 将byte[]写到流中
	 *
	 * @param content 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public void write(final byte[] content) throws IORuntimeException {
		final OutputStream out = this.out;
		try {
			out.write(content);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(out);
			}
		}
	}

	/**
	 * 将多部分对象写到流中，使用{@link ObjectOutputStream}，对象必须实现序列化接口
	 *
	 * @param contents 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public void writeObj(final Object... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (final Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
				}
			}
			osw.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(osw);
			}
		}
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param charset  写出的内容的字符集
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 */
	public void writeStr(final Charset charset, final Object... contents) throws IORuntimeException {
		OutputStreamWriter osw = null;
		try {
			osw = IoUtil.toWriter(out, charset);
			for (final Object content : contents) {
				if (content != null) {
					osw.write(Convert.toStr(content, StrUtil.EMPTY));
				}
			}
			osw.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(osw);
			}
		}
	}
}
