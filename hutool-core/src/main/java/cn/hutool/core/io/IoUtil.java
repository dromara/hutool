package cn.hutool.core.io;

import cn.hutool.core.collection.LineIter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.copy.ReaderWriterCopier;
import cn.hutool.core.io.copy.StreamCopier;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 *
 * @author xiaoleilu
 */
public class IoUtil extends NioUtil {

	// -------------------------------------------------------------------------------------- Copy start

	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
	 *
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader     Reader
	 * @param writer     Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(reader, writer, bufferSize, -1, streamProgress);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param count          最大长度
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, int count, StreamProgress streamProgress) throws IORuntimeException {
		return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
	}

	/**
	 * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
	 *
	 * @param in  输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in         输入流
	 * @param out        输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param count          总拷贝长度
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, int count, StreamProgress streamProgress) throws IORuntimeException {
		return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	/**
	 * 拷贝文件流，使用NIO
	 *
	 * @param in  输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
		Assert.notNull(in, "FileInputStream is null!");
		Assert.notNull(out, "FileOutputStream is null!");

		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = in.getChannel();
			outChannel = out.getChannel();
			return copy(inChannel, outChannel);
		} finally {
			close(outChannel);
			close(inChannel);
		}
	}

	// -------------------------------------------------------------------------------------- Copy end

	// -------------------------------------------------------------------------------------- getReader and getWriter start

	/**
	 * 获得一个文件读取器，默认使用UTF-8编码
	 *
	 * @param in 输入流
	 * @return BufferedReader对象
	 * @since 5.1.6
	 */
	public static BufferedReader getUtf8Reader(InputStream in) {
		return getReader(in, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param in          输入流
	 * @param charsetName 字符集名称
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, String charsetName) {
		return getReader(in, Charset.forName(charsetName));
	}

	/**
	 * 从{@link BOMInputStream}中获取Reader
	 *
	 * @param in {@link BOMInputStream}
	 * @return {@link BufferedReader}
	 * @since 5.5.8
	 */
	public static BufferedReader getReader(BOMInputStream in) {
		return getReader(in, in.getCharset());
	}

	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 获得{@link BufferedReader}<br>
	 * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
	 *
	 * @param reader 普通Reader，如果为null返回null
	 * @return {@link BufferedReader} or null
	 * @since 3.0.9
	 */
	public static BufferedReader getReader(Reader reader) {
		if (null == reader) {
			return null;
		}

		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 获得{@link PushbackReader}<br>
	 * 如果是{@link PushbackReader}强转返回，否则新建
	 *
	 * @param reader       普通Reader
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackReader}
	 * @since 3.1.0
	 */
	public static PushbackReader getPushBackReader(Reader reader, int pushBackSize) {
		return (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
	}

	/**
	 * 获得一个Writer，默认编码UTF-8
	 *
	 * @param out 输入流
	 * @return OutputStreamWriter对象
	 * @since 5.1.6
	 */
	public static OutputStreamWriter getUtf8Writer(OutputStream out) {
		return getWriter(out, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out         输入流
	 * @param charsetName 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
		return getWriter(out, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out     输入流
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
	 * 从流中读取UTF8编码的内容
	 *
	 * @param in 输入流
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 5.4.4
	 */
	public static String readUtf8(InputStream in) throws IORuntimeException {
		return read(in, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从流中读取内容，读取完成后关闭流
	 *
	 * @param in          输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, String charsetName) throws IORuntimeException {
		final FastByteArrayOutputStream out = read(in);
		return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容，读取完毕后关闭流
	 *
	 * @param in      输入流，读取完毕后并不关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, Charset charset) throws IORuntimeException {
		return StrUtil.str(readBytes(in), charset);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后关闭流
	 *
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
		return read(in, true);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param in      输入流
	 * @param isClose 读取完毕后是否关闭流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.3
	 */
	public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			try {
				out = new FastByteArrayOutputStream(in.available());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			copy(in, out);
		} finally {
			if (isClose) {
				close(in);
			}
		}
		return out;
	}

	/**
	 * 从Reader中读取String，读取完毕后关闭Reader
	 *
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader) throws IORuntimeException {
		return read(reader, true);
	}

	/**
	 * 从{@link Reader}中读取String
	 *
	 * @param reader  {@link Reader}
	 * @param isClose 是否关闭{@link Reader}
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader, boolean isClose) throws IORuntimeException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				IoUtil.close(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * 从流中读取bytes，读取完毕后关闭流
	 *
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IORuntimeException {
		return readBytes(in, true);
	}

	/**
	 * 从流中读取bytes
	 *
	 * @param in      {@link InputStream}
	 * @param isClose 是否关闭输入流
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			final byte[] result;
			try {
				final int available = in.available();
				result = new byte[available];
				final int readLength = in.read(result);
				if (readLength != available) {
					throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", available, readLength));
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (isClose) {
					close(in);
				}
			}
			return result;
		}

		// 未知bytes总量的流
		return read(in, isClose).toByteArray();
	}

	/**
	 * 读取指定长度的byte数组，不关闭流
	 *
	 * @param in     {@link InputStream}，为null返回null
	 * @param length 长度，小于等于0返回空byte数组
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in, int length) throws IORuntimeException {
		if (null == in) {
			return null;
		}
		if (length <= 0) {
			return new byte[0];
		}

		byte[] b = new byte[length];
		int readLength;
		try {
			readLength = in.read(b);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		if (readLength > 0 && readLength < length) {
			byte[] b2 = new byte[readLength];
			System.arraycopy(b, 0, b2, 0, readLength);
			return b2;
		} else {
			return b;
		}
	}

	/**
	 * 读取16进制字符串
	 *
	 * @param in          {@link InputStream}
	 * @param length      长度
	 * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex(InputStream in, int length, boolean toLowerCase) throws IORuntimeException {
		return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用大写
	 *
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex28Upper(InputStream in) throws IORuntimeException {
		return readHex(in, 28, false);
	}

	/**
	 * 从流中读取前28个byte并转换为16进制，字母部分使用小写
	 *
	 * @param in {@link InputStream}
	 * @return 16进制字符串
	 * @throws IORuntimeException IO异常
	 */
	public static String readHex28Lower(InputStream in) throws IORuntimeException {
		return readHex(in, 28, true);
	}

	/**
	 * 从流中读取对象，即对象的反序列化
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T> 读取对象的类型
	 * @param in  输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(InputStream in) throws IORuntimeException, UtilException {
		return readObj(in, null);
	}

	/**
	 * 从流中读取对象，即对象的反序列化，读取后不关闭流
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T>   读取对象的类型
	 * @param in    输入流
	 * @param clazz 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(InputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
		try {
			return readObj((in instanceof ValidateObjectInputStream) ?
							(ValidateObjectInputStream) in : new ValidateObjectInputStream(in),
					clazz);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 从流中读取对象，即对象的反序列化，读取后不关闭流
	 *
	 * <p>
	 * 此方法使用了{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞<br>
	 * 通过构造{@link ValidateObjectInputStream}，调用{@link ValidateObjectInputStream#accept(Class[])}
	 * 或者{@link ValidateObjectInputStream#refuse(Class[])}方法添加可以被序列化的类或者禁止序列化的类。
	 * </p>
	 *
	 * @param <T>   读取对象的类型
	 * @param in    输入流，使用{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞
	 * @param clazz 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(ValidateObjectInputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
		if (in == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		try {
			//noinspection unchecked
			return (T) in.readObject();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 从流中读取内容，使用UTF-8编码
	 *
	 * @param <T>        集合类型
	 * @param in         输入流
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readUtf8Lines(InputStream in, T collection) throws IORuntimeException {
		return readLines(in, CharsetUtil.CHARSET_UTF_8, collection);
	}

	/**
	 * 从流中读取内容
	 *
	 * @param <T>         集合类型
	 * @param in          输入流
	 * @param charsetName 字符集
	 * @param collection  返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IORuntimeException {
		return readLines(in, CharsetUtil.charset(charsetName), collection);
	}

	/**
	 * 从流中读取内容
	 *
	 * @param <T>        集合类型
	 * @param in         输入流
	 * @param charset    字符集
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection) throws IORuntimeException {
		return readLines(getReader(in, charset), collection);
	}

	/**
	 * 从Reader中读取内容
	 *
	 * @param <T>        集合类型
	 * @param reader     {@link Reader}
	 * @param collection 返回集合
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(Reader reader, final T collection) throws IORuntimeException {
		readLines(reader, (LineHandler) collection::add);
		return collection;
	}

	/**
	 * 按行读取UTF-8编码数据，针对每行的数据做处理
	 *
	 * @param in          {@link InputStream}
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static void readUtf8Lines(InputStream in, LineHandler lineHandler) throws IORuntimeException {
		readLines(in, CharsetUtil.CHARSET_UTF_8, lineHandler);
	}

	/**
	 * 按行读取数据，针对每行的数据做处理
	 *
	 * @param in          {@link InputStream}
	 * @param charset     {@link Charset}编码
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void readLines(InputStream in, Charset charset, LineHandler lineHandler) throws IORuntimeException {
		readLines(getReader(in, charset), lineHandler);
	}

	/**
	 * 按行读取数据，针对每行的数据做处理<br>
	 * {@link Reader}自带编码定义，因此读取数据的编码跟随其编码。
	 *
	 * @param reader      {@link Reader}
	 * @param lineHandler 行处理接口，实现handle方法用于编辑一行的数据后入到指定地方
	 * @throws IORuntimeException IO异常
	 */
	public static void readLines(Reader reader, LineHandler lineHandler) throws IORuntimeException {
		Assert.notNull(reader);
		Assert.notNull(lineHandler);

		// 从返回的内容中读取所需内容
		final BufferedReader bReader = getReader(reader);
		String line;
		try {
			while ((line = bReader.readLine()) != null) {
				lineHandler.handle(line);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------------------- read end

	/**
	 * String 转为流
	 *
	 * @param content     内容
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
		return toStream(StrUtil.bytes(content, charset));
	}

	/**
	 * String 转为UTF-8编码的字节流流
	 *
	 * @param content 内容
	 * @return 字节流
	 * @since 4.5.1
	 */
	public static ByteArrayInputStream toUtf8Stream(String content) {
		return toStream(content, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 文件转为{@link FileInputStream}
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
	 * byte[] 转为{@link ByteArrayInputStream}
	 *
	 * @param content 内容bytes
	 * @return 字节流
	 * @since 4.1.8
	 */
	public static ByteArrayInputStream toStream(byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@link ByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link ByteArrayOutputStream}
	 * @return 字节流
	 * @since 5.3.6
	 */
	public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in         {@link InputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedInputStream}
	 * @since 5.6.1
	 */
	public static BufferedInputStream toBuffered(InputStream in, int bufferSize) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @return {@link BufferedOutputStream}
	 * @since 4.0.10
	 */
	public static BufferedOutputStream toBuffered(OutputStream out) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out        {@link OutputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedOutputStream}
	 * @since 5.6.1
	 */
	public static BufferedOutputStream toBuffered(OutputStream out, int bufferSize) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader {@link Reader}
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(Reader reader) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader     {@link Reader}
	 * @param bufferSize buffer size
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(Reader reader, int bufferSize) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer {@link Writer}
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(Writer writer) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer     {@link Writer}
	 * @param bufferSize buffer size
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(Writer writer, int bufferSize) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
	}

	/**
	 * 将{@link InputStream}转换为支持mark标记的流<br>
	 * 若原流支持mark标记，则返回原流，否则使用{@link BufferedInputStream} 包装之
	 *
	 * @param in 流
	 * @return {@link InputStream}
	 * @since 4.0.9
	 */
	public static InputStream toMarkSupportStream(InputStream in) {
		if (null == in) {
			return null;
		}
		if (false == in.markSupported()) {
			return new BufferedInputStream(in);
		}
		return in;
	}

	/**
	 * 转换为{@link PushbackInputStream}<br>
	 * 如果传入的输入流已经是{@link PushbackInputStream}，强转返回，否则新建一个
	 *
	 * @param in           {@link InputStream}
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackInputStream}
	 * @since 3.1.0
	 */
	public static PushbackInputStream toPushbackStream(InputStream in, int pushBackSize) {
		return (in instanceof PushbackInputStream) ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
	}

	/**
	 * 将指定{@link InputStream} 转换为{@link InputStream#available()}方法可用的流。<br>
	 * 在Socket通信流中，服务端未返回数据情况下{@link InputStream#available()}方法始终为{@code 0}<br>
	 * 因此，在读取前需要调用{@link InputStream#read()}读取一个字节（未返回会阻塞），一旦读取到了，{@link InputStream#available()}方法就正常了。<br>
	 * 需要注意的是，在网络流中，是按照块来传输的，所以 {@link InputStream#available()} 读取到的并非最终长度，而是此次块的长度。<br>
	 * 此方法返回对象的规则为：
	 *
	 * <ul>
	 *     <li>FileInputStream 返回原对象，因为文件流的available方法本身可用</li>
	 *     <li>其它InputStream 返回PushbackInputStream</li>
	 * </ul>
	 *
	 * @param in 被转换的流
	 * @return 转换后的流，可能为{@link PushbackInputStream}
	 * @since 5.5.3
	 */
	public static InputStream toAvailableStream(InputStream in) {
		if (in instanceof FileInputStream) {
			// FileInputStream本身支持available方法。
			return in;
		}

		final PushbackInputStream pushbackInputStream = toPushbackStream(in, 1);
		try {
			final int available = pushbackInputStream.available();
			if (available <= 0) {
				//此操作会阻塞，直到有数据被读到
				int b = pushbackInputStream.read();
				pushbackInputStream.unread(b);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return pushbackInputStream;
	}

	/**
	 * 将byte[]写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content    写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IORuntimeException {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(out);
			}
		}
	}

	/**
	 * 将多部分内容写到流中，自动转换为UTF-8字符串
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static void writeUtf8(OutputStream out, boolean isCloseOut, Object... contents) throws IORuntimeException {
		write(out, CharsetUtil.CHARSET_UTF_8, isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param out         输出流
	 * @param charsetName 写出的内容的字符集
	 * @param isCloseOut  写入完毕是否关闭输出流
	 * @param contents    写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, String charsetName, boolean isCloseOut, Object... contents) throws IORuntimeException {
		write(out, CharsetUtil.charset(charsetName), isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param out        输出流
	 * @param charset    写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void write(OutputStream out, Charset charset, boolean isCloseOut, Object... contents) throws IORuntimeException {
		OutputStreamWriter osw = null;
		try {
			osw = getWriter(out, charset);
			for (Object content : contents) {
				if (content != null) {
					osw.write(Convert.toStr(content, StrUtil.EMPTY));
				}
			}
			osw.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param obj        写入的对象内容
	 * @throws IORuntimeException IO异常
	 * @since 5.3.3
	 */
	public static void writeObj(OutputStream out, boolean isCloseOut, Serializable obj) throws IORuntimeException {
		writeObjects(out, isCloseOut, obj);
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
				}
			}
			osw.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 * @since 4.2.2
	 */
	public static void flush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (Exception e) {
				// 静默刷出
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
	 * 尝试关闭指定对象<br>
	 * 判断对象如果实现了{@link AutoCloseable}，则调用之
	 *
	 * @param obj 可关闭对象
	 * @since 4.3.2
	 */
	public static void closeIfPosible(Object obj) {
		if (obj instanceof AutoCloseable) {
			close((AutoCloseable) obj);
		}
	}

	/**
	 * 对比两个流内容是否相同<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个流
	 * @param input2 第二个流
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEquals(InputStream input1, InputStream input2) throws IORuntimeException {
		if (false == (input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (false == (input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		try {
			int ch = input1.read();
			while (EOF != ch) {
				int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}

			int ch2 = input2.read();
			return ch2 == EOF;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 对比两个Reader的内容是否一致<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个reader
	 * @param input2 第二个reader
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEquals(Reader input1, Reader input2) throws IORuntimeException {
		input1 = getReader(input1);
		input2 = getReader(input2);

		try {
			int ch = input1.read();
			while (EOF != ch) {
				int ch2 = input2.read();
				if (ch != ch2) {
					return false;
				}
				ch = input1.read();
			}

			int ch2 = input2.read();
			return ch2 == EOF;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 对比两个流内容是否相同，忽略EOL字符<br>
	 * 内部会转换流为 {@link BufferedInputStream}
	 *
	 * @param input1 第一个流
	 * @param input2 第二个流
	 * @return 两个流的内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IORuntimeException {
		final BufferedReader br1 = getReader(input1);
		final BufferedReader br2 = getReader(input2);

		try {
			String line1 = br1.readLine();
			String line2 = br2.readLine();
			while (line1 != null && line1.equals(line2)) {
				line1 = br1.readLine();
				line2 = br2.readLine();
			}
			return Objects.equals(line1, line2);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 计算流CRC32校验码，计算后关闭流
	 *
	 * @param in 文件，不能为目录
	 * @return CRC32值
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static long checksumCRC32(InputStream in) throws IORuntimeException {
		return checksum(in, new CRC32()).getValue();
	}

	/**
	 * 计算流的校验码，计算后关闭流
	 *
	 * @param in       流
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 * @since 4.0.10
	 */
	public static Checksum checksum(InputStream in, Checksum checksum) throws IORuntimeException {
		Assert.notNull(in, "InputStream is null !");
		if (null == checksum) {
			checksum = new CRC32();
		}
		try {
			in = new CheckedInputStream(in, checksum);
			IoUtil.copy(in, new NullOutputStream());
		} finally {
			IoUtil.close(in);
		}
		return checksum;
	}

	/**
	 * 计算流的校验码，计算后关闭流
	 *
	 * @param in       流
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 * @since 5.4.0
	 */
	public static long checksumValue(InputStream in, Checksum checksum) {
		return checksum(in, checksum).getValue();
	}

	/**
	 * 返回行遍历器
	 * <pre>
	 * LineIterator it = null;
	 * try {
	 * 	it = IoUtil.lineIter(reader);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// do something with line
	 *    }
	 * } finally {
	 * 		it.close();
	 * }
	 * </pre>
	 *
	 * @param reader {@link Reader}
	 * @return {@link LineIter}
	 * @since 5.6.1
	 */
	public static LineIter lineIter(Reader reader) {
		return new LineIter(reader);
	}

	/**
	 * 返回行遍历器
	 * <pre>
	 * LineIterator it = null;
	 * try {
	 * 	it = IoUtil.lineIter(in, CharsetUtil.CHARSET_UTF_8);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// do something with line
	 *    }
	 * } finally {
	 * 		it.close();
	 * }
	 * </pre>
	 *
	 * @param in      {@link InputStream}
	 * @param charset 编码
	 * @return {@link LineIter}
	 * @since 5.6.1
	 */
	public static LineIter lineIter(InputStream in, Charset charset) {
		return new LineIter(in, charset);
	}
}
