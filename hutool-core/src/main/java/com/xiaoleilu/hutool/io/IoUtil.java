package com.xiaoleilu.hutool.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Collection;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 * 
 * @author xiaoleilu
 *
 */
public final class IoUtil {

	private IoUtil() {
	}

	/** 默认缓存大小 */
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	/** 默认缓存大小 */
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 4096;
	/** 数据流末尾 */
	public static final int EOF = -1;

	// -------------------------------------------------------------------------------------- Copy start
	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IOException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中
	 * 
	 * @param reader Reader
	 * @param writer Writer
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IOException {
		char[] buffer = new char[bufferSize];
		long size = 0;
		int readSize;
		if (null != streamProgress) {
			streamProgress.start();
		}
		while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
			writer.write(buffer, 0, readSize);
			size += readSize;
			writer.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝流，使用默认Buffer大小
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IOException {
		if (null == in) {
			throw new NullPointerException("InputStream is null!");
		}
		if (null == out) {
			throw new NullPointerException("OutputStream is null!");
		}
		if (bufferSize <= 0) {
			bufferSize = DEFAULT_BUFFER_SIZE;
		}

		byte[] buffer = new byte[bufferSize];
		long size = 0;
		if (null != streamProgress) {
			streamProgress.start();
		}
		for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
			out.write(buffer, 0, readSize);
			size += readSize;
			out.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}

	/**
	 * 拷贝流 thanks to: https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java
	 * 
	 * @param in 输入流
	 * @param out 输出流
	 * @param bufferSize 缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IOException IO异常
	 */
	public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IOException {
		return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, streamProgress);
	}

	/**
	 * 拷贝文件流，使用NIO
	 * 
	 * @param in 输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IOException IO异常
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
		if (null == in) {
			throw new NullPointerException("FileInputStream is null!");
		}
		if (null == out) {
			throw new NullPointerException("FileOutputStream is null!");
		}

		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();

		return inChannel.transferTo(0, inChannel.size(), outChannel);
	}

	/**
	 * 拷贝流，使用NIO，不会关闭流
	 * 
	 * @param in {@link ReadableByteChannel}
	 * @param out {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小，如果小于等于0，使用默认
	 * @param streamProgress {@link StreamProgress}进度处理器
	 * @return 拷贝的字节数
	 * @throws IOException IO异常
	 */
	public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize, StreamProgress streamProgress) throws IOException {
		if (null == in) {
			throw new NullPointerException("In is null!");
		}
		if (null == out) {
			throw new NullPointerException("Out is null!");
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
		long size = 0;
		if (null != streamProgress) {
			streamProgress.start();
		}
		while (in.read(byteBuffer) != EOF) {
			byteBuffer.flip();// 写转读
			size += out.write(byteBuffer);
			byteBuffer.clear();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}

		return size;
	}
	// -------------------------------------------------------------------------------------- Copy end

	// -------------------------------------------------------------------------------------- getReader and getWriter start
	/**
	 * 获得一个文件读取器
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集名称
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, String charsetName) {
		return getReader(in, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Reader
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader = null;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 获得{@link BufferedReader}
	 * 
	 * @param reader 普通Reader
	 * @return {@link BufferedReader}
	 * @since 3.0.9
	 */
	public static BufferedReader getReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 获得一个Writer
	 * 
	 * @param out 输入流
	 * @param charsetName 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
		return getWriter(out, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Writer
	 * 
	 * @param out 输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
		if (null == out) {
			return null;
		}

		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
	// -------------------------------------------------------------------------------------- getReader and getWriter end

	// -------------------------------------------------------------------------------------- read start
	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String read(InputStream in, String charsetName) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容，读取完毕后并不关闭流
	 * 
	 * @param in 输入流，读取完毕后并不关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String read(InputStream in, Charset charset) throws IOException {
		FastByteArrayOutputStream out = read(in);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param in 输入流
	 * @return 输出流
	 * @throws IOException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out;
	}

	/**
	 * 从Reader中读取String，读取完毕后并不关闭Reader
	 * 
	 * @param reader Reader
	 * @return String
	 * @throws IOException IO异常
	 */
	public static String read(Reader reader) throws IOException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		while (-1 != reader.read(buffer)) {
			builder.append(buffer.flip().toString());
		}
		return builder.toString();
	}

	/**
	 * 从FileChannel中读取内容，读取完毕后并不关闭Channel
	 * 
	 * @param fileChannel 文件管道
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String read(FileChannel fileChannel, String charsetName) throws IOException {
		return read(fileChannel, CharsetUtil.charset(charsetName));
	}

	/**
	 * 从FileChannel中读取内容
	 * 
	 * @param fileChannel 文件管道
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String read(FileChannel fileChannel, Charset charset) throws IOException {
		final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		return StrUtil.str(buffer, charset);
	}

	/**
	 * 从流中读取bytes
	 * 
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IOException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	/**
	 * 读取指定长度的byte数组
	 * 
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @return bytes
	 * @throws IOException IO异常
	 */
	public static byte[] readBytes(InputStream in, int length) throws IOException {
		byte[] b = new byte[length];
		int readLength = in.read(b);
		if (readLength < length) {
			byte[] b2 = new byte[length];
			System.arraycopy(b, 0, b2, 0, readLength);
			return b2;
		} else {
			return b;
		}
	}

	/**
	 * 读取16进制字符串
	 * 
	 * @param in {@link InputStream}
	 * @param length 长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IOException IO异常
	 */
	public static String readHex(InputStream in, int length, boolean toLowerCase) throws IOException {
		return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用大写
	 * 
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IOException IO异常
	 */
	public static String readHex28Upper(InputStream in) throws IOException {
		return readHex(in, 28, false);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用小写
	 * 
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IOException IO异常
	 */
	public static String readHex28Lower(InputStream in) throws IOException {
		return readHex(in, 28, true);
	}

	/**
	 * 从流中读取内容，读到输出流中
	 * 
	 * @param <T> 读取对象的类型
	 * @param in 输入流
	 * @return 输出流
	 * @throws IOException IO异常
	 */
	public static <T> T readObj(InputStream in) throws IOException {
		if (in == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			@SuppressWarnings("unchecked") // may fail with CCE if serialised form is incorrect
			final T obj = (T) ois.readObject();
			return obj;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param <T> 集合类型
	 * @param in 输入流
	 * @param charsetName 字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IOException {
		return readLines(in, CharsetUtil.charset(charsetName), collection);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param <T> 集合类型
	 * @param in 输入流
	 * @param charset 字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, final T collection) throws IOException {
		return readLines(getReader(in, charset), collection);
	}
	
	/**
	 * 从Reader中读取内容
	 * 
	 * @param <T> 集合类型
	 * @param reader {@link Reader}
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static <T extends Collection<String>> T readLines(Reader reader, final T collection) throws IOException {
		readLines(reader, new LineHandler(){
			@Override
			public void handle(String line) {
				collection.add(line);
			}
		});
		return collection;
	}
	
	/**
	 * 按行读取数据，针对每行的数据做处理
	 * 
	 * @param in {@link InputStream}
	 * @param charset {@link Charset}编码
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IOException
	 * @since 3.0.9
	 */
	public static void readLines(InputStream in, Charset charset, LineHandler lineHandler) throws IOException {
		readLines(getReader(in, charset), lineHandler);
	}

	/**
	 * 按行读取数据，针对每行的数据做处理
	 * 
	 * @param reader {@link Reader}
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IOException
	 */
	public static void readLines(Reader reader, LineHandler lineHandler) throws IOException {
		// 从返回的内容中读取所需内容
		BufferedReader bReader = getReader(reader);
		String line = null;
		while ((line = bReader.readLine()) != null) {
			lineHandler.handle(line);
		}
	}

	// -------------------------------------------------------------------------------------- read end

	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charsetName 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, String charsetName) {
		return toStream(content, CharsetUtil.charset(charsetName));
	}

	/**
	 * String 转为流
	 * 
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, Charset charset) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(StrUtil.bytes(content, charset));
	}

	/**
	 * 文件转为流
	 * 
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 将byte[]写到流中
	 * 
	 * @param out 输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content 写入的内容
	 * @throws IOException IO异常
	 */
	public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IOException {
		try {
			out.write(content);
		} finally {
			if (isCloseOut) {
				close(out);
			}
		}
	}
	
	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 * 
	 * @param out 输出流
	 * @param charsetName 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IOException IO异常
	 */
	public static void write(OutputStream out, String charsetName, boolean isCloseOut, Object... contents) throws IOException {
		Assert.notNull(charsetName, "[charsetName] is null !");
		write(out, Charset.forName(charsetName), isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 * 
	 * @param out 输出流
	 * @param charset 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IOException IO异常
	 * @since 3.0.9
	 */
	public static void write(OutputStream out, Charset charset, boolean isCloseOut, Object... contents) throws IOException {
		OutputStreamWriter osw = null;
		try {
			osw = getWriter(out, charset);
			for (Object content : contents) {
				if (content != null) {
					osw.write(Convert.toStr(content, StrUtil.EMPTY));
					osw.flush();
				}
			}
		} catch (Exception e) {
			throw new IOException("Write content to OutputStream error!", e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}
	
	/**
	 * 将多部分内容写到流中
	 * 
	 * @param out 输出流
	 * @param charsetName 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容
	 * @throws IOException IO异常
	 */
	public static void writeObjects(OutputStream out, String charsetName, boolean isCloseOut, Serializable... contents) throws IOException {
		Assert.notNull(charsetName, "[charsetName] is null !");
		writeObjects(out, Charset.forName(charsetName), isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中
	 * 
	 * @param out 输出流
	 * @param charset 写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents 写入的内容
	 * @throws IOException IO异常
	 */
	public static void writeObjects(OutputStream out, Charset charset, boolean isCloseOut, Serializable... contents) throws IOException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
					osw.flush();
				}
			}
		} catch (Exception e) {
			throw new IOException("Write content to OutputStream error!", e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 * 
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 * 
	 * @param closeable 被关闭的对象
	 */
	public static void close(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}
}
