package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;

/**
 * 文件资源访问对象，支持{@link Path} 和 {@link File} 访问
 *
 * @author looly
 */
public class FileResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final File file;

	// ----------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 *
	 * @param path 文件
	 * @since 4.4.1
	 */
	public FileResource(Path path) {
		this(path.toFile());
	}

	/**
	 * 构造
	 *
	 * @param file 文件
	 */
	public FileResource(File file) {
		this(file, file.getName());
	}

	/**
	 * 构造
	 *
	 * @param file 文件
	 * @param fileName 文件名，如果为null获取文件本身的文件名
	 */
	public FileResource(File file, String fileName) {
		this.file = file;
	}

	/**
	 * 构造
	 *
	 * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
	 */
	public FileResource(String path) {
		this(FileUtil.file(path));
	}
	// ----------------------------------------------------------------------- Constructor end

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public URL getUrl(){
		return URLUtil.getURL(this.file);
	}

	@Override
	public InputStream getStream() throws NoResourceException {
		return FileUtil.getInputStream(this.file);
	}

	/**
	 * 获取文件
	 *
	 * @return 文件
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * 返回路径
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return (null == this.file) ? "null" : this.file.toString();
	}
}
