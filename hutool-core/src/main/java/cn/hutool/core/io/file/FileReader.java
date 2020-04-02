package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件读取器
 * 
 * @author Looly
 *
 */
public class FileReader extends FileWrapper {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 创建 FileReader
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 * @return {@link FileReader}
	 */
	public static FileReader create(File file, Charset charset){
		return new FileReader(file, charset);
	}
	
	/**
	 * 创建 FileReader, 编码：{@link FileWrapper#DEFAULT_CHARSET}
	 * @param file 文件
	 * @return {@link FileReader}
	 */
	public static FileReader create(File file){
		return new FileReader(file);
	}
	
	// ------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil}
	 */
	public FileReader(File file, Charset charset) {
		super(file, charset);
		checkFile();
	}

	/**
	 * 构造
	 * @param file 文件
	 * @param charset 编码，使用 {@link CharsetUtil#charset(String)}
	 */
	public FileReader(File file, String charset) {
		this(file, CharsetUtil.charset(charset));
	}

	/**
	 * 构造
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 * @param charset 编码，使用 {@link CharsetUtil}
	 */
	public FileReader(String filePath, Charset charset) {
		this(FileUtil.file(filePath), charset);
	}

	/**
	 * 构造
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 * @param charset 编码，使用 {@link CharsetUtil#charset(String)}
	 */
	public FileReader(String filePath, String charset) {
		this(FileUtil.file(filePath), CharsetUtil.charset(charset));
	}

	/**
	 * 构造<br>
	 * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
	 * @param file 文件
	 */
	public FileReader(File file) {
		this(file, DEFAULT_CHARSET);
	}

	/**
	 * 构造<br>
	 * 编码使用 {@link FileWrapper#DEFAULT_CHARSET}
	 * @param filePath 文件路径，相对路径会被转换为相对于ClassPath的路径
	 */
	public FileReader(String filePath) {
		this(filePath, DEFAULT_CHARSET);
	}
	// ------------------------------------------------------- Constructor end

	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过 {@link Integer#MAX_VALUE}
	 * 
	 * @return 字节码
	 * @throws IORuntimeException IO异常
	 */
	public byte[] readBytes() throws IORuntimeException {
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IORuntimeException("File is larger then max array size");
		}

		byte[] bytes = new byte[(int) len];
		FileInputStream in = null;
		int readLength;
		try {
			in = new FileInputStream(file);
			readLength = in.read(bytes);
			if(readLength < len){
				throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", len, readLength));
			}
		} catch (Exception e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}

		return bytes;
	}

	/**
	 * 读取文件内容
	 * 
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public String readString() throws IORuntimeException{
		return new String(readBytes(), this.charset);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public <T extends Collection<String>> T readLines(T collection) throws IORuntimeException {
		BufferedReader reader = null;
		try {
			reader = FileUtil.getReader(file, charset);
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				collection.add(line);
			}
			return collection;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(reader);
		}
	}
	
	/**
	 * 按照行处理文件内容
	 * 
	 * @param lineHandler 行处理器
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public void readLines(LineHandler lineHandler) throws IORuntimeException{
		BufferedReader reader = null;
		try {
			reader = FileUtil.getReader(file, charset);
			IoUtil.readLines(reader, lineHandler);
		} finally {
			IoUtil.close(reader);
		}
	}
	
	/**
	 * 从文件中读取每一行数据
	 * 
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public List<String> readLines() throws IORuntimeException {
		return readLines(new ArrayList<>());
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 读取的结果对象类型
	 * @param readerHandler Reader处理类
	 * @return 从文件中read出的数据
	 * @throws IORuntimeException IO异常
	 */
	public <T> T read(ReaderHandler<T> readerHandler) throws IORuntimeException {
		BufferedReader reader = null;
		T result;
		try {
			reader = FileUtil.getReader(this.file, charset);
			result = readerHandler.handle(reader);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(reader);
		}
		return result;
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public BufferedReader getReader() throws IORuntimeException {
		return IoUtil.getReader(getInputStream(), this.charset);
	}

	/**
	 * 获得输入流
	 * 
	 * @return 输入流
	 * @throws IORuntimeException IO异常
	 */
	public BufferedInputStream getInputStream() throws IORuntimeException {
		try {
			return new BufferedInputStream(new FileInputStream(this.file));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 将文件写入流中
	 * 
	 * @param out 流
	 * @return 写出的流byte数
	 * @throws IORuntimeException IO异常
	 */
	public long writeToStream(OutputStream out) throws IORuntimeException {
		try (FileInputStream in = new FileInputStream(this.file)){
			return IoUtil.copy(in, out);
		}catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------- Interface start
	/**
	 * Reader处理接口
	 * 
	 * @author Luxiaolei
	 *
	 * @param <T> Reader处理返回结果类型
	 */
	public interface ReaderHandler<T> {
		T handle(BufferedReader reader) throws IOException;
	}
	// -------------------------------------------------------------------------- Interface end
	
	/**
	 * 检查文件
	 * 
	 * @throws IORuntimeException IO异常
	 */
	private void checkFile() throws IORuntimeException {
		if (false == file.exists()) {
			throw new IORuntimeException("File not exist: " + file);
		}
		if (false == file.isFile()) {
			throw new IORuntimeException("Not a file:" + file);
		}
	}
}
