package cn.hutool.core.io.resource;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import javax.tools.FileObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * {@link FileObject} 资源包装
 *
 * @author looly
 * @since 5.5.2
 */
public class FileObjectResource implements Resource {

	private final FileObject fileObject;

	/**
	 * 构造
	 *
	 * @param fileObject {@link FileObject}
	 */
	public FileObjectResource(FileObject fileObject) {
		this.fileObject = fileObject;
	}

	/**
	 * 获取原始的{@link FileObject}
	 *
	 * @return {@link FileObject}
	 */
	public FileObject getFileObject() {
		return this.fileObject;
	}

	@Override
	public String getName() {
		return this.fileObject.getName();
	}

	@Override
	public URL getUrl() {
		try {
			return this.fileObject.toUri().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public InputStream getStream() {
		try {
			return this.fileObject.openInputStream();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public BufferedReader getReader(Charset charset) {
		try {
			return IoUtil.getReader(this.fileObject.openReader(false));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
