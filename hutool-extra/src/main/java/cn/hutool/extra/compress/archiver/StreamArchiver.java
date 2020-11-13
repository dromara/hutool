package cn.hutool.extra.compress.archiver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.compress.CompressException;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 数据归档封装，归档即将几个文件或目录打成一个压缩包<br>
 * 支持的归档文件格式为：
 * <ul>
 *     <li>{@link ArchiveStreamFactory#AR}</li>
 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
 *     <li>{@link ArchiveStreamFactory#JAR}</li>
 *     <li>{@link ArchiveStreamFactory#TAR}</li>
 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
 * </ul>
 *
 * @author looly
 */
public class StreamArchiver implements Archiver {

	/**
	 * 创建归档器
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param file         归档输出的文件
	 * @return StreamArchiver
	 */
	public static StreamArchiver create(Charset charset, String archiverName, File file) {
		return new StreamArchiver(charset, archiverName, file);
	}

	/**
	 * 创建归档器
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param out          归档输出的流
	 * @return StreamArchiver
	 */
	public static StreamArchiver create(Charset charset, String archiverName, OutputStream out) {
		return new StreamArchiver(charset, archiverName, out);
	}

	private final ArchiveOutputStream out;

	/**
	 * 构造
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param file         归档输出的文件
	 */
	public StreamArchiver(Charset charset, String archiverName, File file) {
		this(charset, archiverName, FileUtil.getOutputStream(file));
	}

	/**
	 * 构造
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param targetStream 归档输出的流
	 */
	public StreamArchiver(Charset charset, String archiverName, OutputStream targetStream) {
		final ArchiveStreamFactory factory = new ArchiveStreamFactory(charset.name());
		try {
			this.out = factory.createArchiveOutputStream(archiverName, targetStream);
		} catch (ArchiveException e) {
			throw new CompressException(e);
		}

		//特殊设置
		if(this.out instanceof TarArchiveOutputStream){
			((TarArchiveOutputStream)out).setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		} else if(this.out instanceof ArArchiveOutputStream){
			((ArArchiveOutputStream)out).setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
		}
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Filter#accept(Object)}为true时加入。
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	@Override
	public StreamArchiver add(File file, String path, Filter<File> filter) throws IORuntimeException {
		try {
			addInternal(file, path, filter);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 结束已经增加的文件归档，此方法不会关闭归档流，可以继续添加文件
	 *
	 * @return this
	 */
	@Override
	public StreamArchiver finish() {
		try {
			this.out.finish();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			finish();
		} catch (Exception ignore) {
			//ignore
		}
		IoUtil.close(this.out);
	}

	/**
	 * 将文件或目录加入归档包，目录采取递归读取方式按照层级加入
	 *
	 * @param file   文件或目录
	 * @param path   文件或目录的初始路径，null表示位于根路径
	 * @param filter 文件过滤器，指定哪些文件或目录可以加入，当{@link Filter#accept(Object)}为true时加入。
	 */
	private void addInternal(File file, String path, Filter<File> filter) throws IOException {
		if (null != filter && false == filter.accept(file)) {
			return;
		}
		final ArchiveOutputStream out = this.out;

		final String entryName = StrUtil.addSuffixIfNot(StrUtil.nullToEmpty(path), StrUtil.SLASH) + file.getName();
		out.putArchiveEntry(out.createArchiveEntry(file, entryName));

		if (file.isDirectory()) {
			// 目录遍历写入
			final File[] files = file.listFiles();
			for (File childFile : files) {
				addInternal(childFile, entryName, filter);
			}
		} else {
			if (file.isFile()) {
				// 文件直接写入
				FileUtil.writeToStream(file, out);
			}
			out.closeArchiveEntry();
		}
	}
}
