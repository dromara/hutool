package cn.hutool.core.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ZipUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Zip文件或流读取器，一般用于Zip文件解压
 *
 * @author looly
 * @since 5.7.8
 */
public class ZipReader implements Closeable {

	private ZipFile zipFile;
	private ZipInputStream in;

	/**
	 * 创建{@link ZipReader}
	 *
	 * @param zipFile 生成的Zip文件
	 * @param charset 编码
	 * @return {@link ZipReader}
	 */
	public static ZipReader of(File zipFile, Charset charset) {
		return new ZipReader(zipFile, charset);
	}

	/**
	 * 创建{@link ZipReader}
	 *
	 * @param in      Zip输入的流，一般为输入文件流
	 * @param charset 编码
	 * @return {@link ZipReader}
	 */
	public static ZipReader of(InputStream in, Charset charset) {
		return new ZipReader(in, charset);
	}

	/**
	 * 构造
	 *
	 * @param zipFile 读取的的Zip文件
	 * @param charset 编码
	 */
	public ZipReader(File zipFile, Charset charset) {
		this.zipFile = ZipUtil.toZipFile(zipFile, charset);
	}

	/**
	 * 构造
	 *
	 * @param zipFile 读取的的Zip文件
	 */
	public ZipReader(ZipFile zipFile) {
		this.zipFile = zipFile;
	}

	/**
	 * 构造
	 *
	 * @param in      读取的的Zip文件流
	 * @param charset 编码
	 */
	public ZipReader(InputStream in, Charset charset) {
		this.in = new ZipInputStream(in, charset);
	}

	/**
	 * 构造
	 *
	 * @param zin 读取的的Zip文件流
	 */
	public ZipReader(ZipInputStream zin) {
		this.in = zin;
	}

	/**
	 * 获取指定路径的文件流<br>
	 * 如果是文件模式，则直接获取Entry对应的流，如果是流模式，则遍历entry后，找到对应流返回
	 *
	 * @param path 路径
	 * @return 文件流
	 */
	public InputStream get(String path) {
		if (null != this.zipFile) {
			final ZipFile zipFile = this.zipFile;
			final ZipEntry entry = zipFile.getEntry(path);
			if (null != entry) {
				return ZipUtil.getStream(zipFile, entry);
			}
		} else {
			try {
				this.in.reset();
				ZipEntry zipEntry;
				while (null != (zipEntry = in.getNextEntry())) {
					if(zipEntry.getName().equals(path)){
						return this.in;
					}
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}

		return null;
	}

	/**
	 * 解压到指定目录中
	 *
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 */
	public File readTo(File outFile) throws IORuntimeException {
		return readTo(outFile, null);
	}

	/**
	 * 解压到指定目录中
	 *
	 * @param outFile     解压到的目录
	 * @param entryFilter 过滤器，排除不需要的文件
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * @since 5.7.12
	 */
	public File readTo(File outFile, Filter<ZipEntry> entryFilter) throws IORuntimeException {
		read((zipEntry) -> {
			if (null == entryFilter || entryFilter.accept(zipEntry)) {
				// FileUtil.file会检查slip漏洞，漏洞说明见http://blog.nsfocus.net/zip-slip-2/
				final File outItemFile = FileUtil.file(outFile, zipEntry.getName());
				if (zipEntry.isDirectory()) {
					// 目录
					//noinspection ResultOfMethodCallIgnored
					outItemFile.mkdirs();
				} else {
					InputStream in;
					if (null != this.zipFile) {
						in = ZipUtil.getStream(this.zipFile, zipEntry);
					} else {
						in = this.in;
					}
					// 文件
					FileUtil.writeFromStream(in, outItemFile, false);
				}
			}
		});
		return outFile;
	}

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param consumer {@link ZipEntry}处理器
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ZipReader read(Consumer<ZipEntry> consumer) throws IORuntimeException {
		if (null != this.zipFile) {
			readFromZipFile(consumer);
		} else {
			readFromStream(consumer);
		}
		return this;
	}

	@Override
	public void close() throws IORuntimeException {
		if (null != this.zipFile) {
			IoUtil.close(this.zipFile);
		} else {
			IoUtil.close(this.in);
		}
	}

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param consumer {@link ZipEntry}处理器
	 */
	private void readFromZipFile(Consumer<ZipEntry> consumer) {
		final Enumeration<? extends ZipEntry> em = zipFile.entries();
		while (em.hasMoreElements()) {
			consumer.accept(em.nextElement());
		}
	}

	/**
	 * 读取并处理Zip流中的每一个{@link ZipEntry}
	 *
	 * @param consumer {@link ZipEntry}处理器
	 * @throws IORuntimeException IO异常
	 */
	private void readFromStream(Consumer<ZipEntry> consumer) throws IORuntimeException {
		try {
			ZipEntry zipEntry;
			while (null != (zipEntry = in.getNextEntry())) {
				consumer.accept(zipEntry);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
