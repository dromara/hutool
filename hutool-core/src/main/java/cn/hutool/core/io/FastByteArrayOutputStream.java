package cn.hutool.core.io;

import cn.hutool.core.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区
 * <p>
 * 可以通过{@link #toByteArray()}和 {@link #toString()}来获取数据
 * <p>
 * {@link #close()}方法无任何效果，当流被关闭后不会抛出IOException
 * <p>
 * 这种设计避免重新分配内存块而是分配新增的缓冲区，缓冲区不会被GC，数据也不会被拷贝到其他缓冲区。
 *
 * @author biezhi
 */
public class FastByteArrayOutputStream extends OutputStream {

	private final FastByteBuffer buffer;

	public FastByteArrayOutputStream() {
		this(1024);
	}

	/**
	 * 构造
	 * 
	 * @param size 预估大小
	 */
	public FastByteArrayOutputStream(int size) {
		buffer = new FastByteBuffer(size);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	@Override
	public void write(int b) {
		buffer.append((byte) b);
	}

	public int size() {
		return buffer.size();
	}

	/**
	 * 此方法无任何效果，当流被关闭后不会抛出IOException
	 */
	@Override
	public void close() {
		// nop
	}

	public void reset() {
		buffer.reset();
	}

	/**
	 * 写出
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 */
	public void writeTo(OutputStream out) throws IORuntimeException {
		final int index = buffer.index();
		byte[] buf;
		try {
			for (int i = 0; i < index; i++) {
				buf = buffer.array(i);
				out.write(buf);
			}
			out.write(buffer.array(index), 0, buffer.offset());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	
	/**
	 * 转为Byte数组
	 * @return Byte数组
	 */
	public byte[] toByteArray() {
		return buffer.toArray();
	}

	@Override
	public String toString() {
		return new String(toByteArray());
	}

	/**
	 * 转为字符串
	 * @param charsetName 编码
	 * @return 字符串
	 */
	public String toString(String charsetName) {
		return toString(CharsetUtil.charset(charsetName));
	}
	
	/**
	 * 转为字符串
	 * @param charset 编码
	 * @return 字符串
	 */
	public String toString(Charset charset) {
		return new String(toByteArray(), charset);
	}

}