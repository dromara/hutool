package cn.hutool.extra.compress;

import cn.hutool.extra.compress.archiver.Archiver;
import cn.hutool.extra.compress.archiver.SevenZArchiver;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 压缩工具类<br>
 * 基于commons-compress的压缩解压封装
 *
 * @since 5.5.0
 * @author looly
 */
public class CompressUtil {

	/**
	 * 创建归档器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param file         归档输出的文件
	 * @return Archiver
	 */
	public static Archiver createArchiver(Charset charset, String archiverName, File file) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZArchiver(file);
		}
		return StreamArchiver.create(charset, archiverName, file);
	}

	/**
	 * 创建归档器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}
	 * @param out         归档输出的流
	 * @return Archiver
	 */
	public static Archiver createArchiver(Charset charset, String archiverName, OutputStream out) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZArchiver(out);
		}
		return StreamArchiver.create(charset, archiverName, out);
	}
}
