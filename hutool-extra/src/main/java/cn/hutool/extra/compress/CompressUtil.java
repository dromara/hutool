package cn.hutool.extra.compress;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.compress.archiver.Archiver;
import cn.hutool.extra.compress.archiver.SevenZArchiver;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import cn.hutool.extra.compress.extractor.Extractor;
import cn.hutool.extra.compress.extractor.SenvenZExtractor;
import cn.hutool.extra.compress.extractor.StreamExtractor;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 压缩工具类<br>
 * 基于commons-compress的压缩解压封装
 *
 * @author looly
 * @since 5.5.0
 */
public class CompressUtil {

	/**
	 * 获取压缩输出流，用于压缩指定内容，支持的格式例如：
	 * <ul>
	 *     <li>{@value CompressorStreamFactory#GZIP}</li>
	 *     <li>{@value CompressorStreamFactory#BZIP2}</li>
	 *     <li>{@value CompressorStreamFactory#XZ}</li>
	 *     <li>{@value CompressorStreamFactory#PACK200}</li>
	 *     <li>{@value CompressorStreamFactory#SNAPPY_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_BLOCK}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#ZSTANDARD}</li>
	 *     <li>{@value CompressorStreamFactory#DEFLATE}</li>
	 * </ul>
	 *
	 * @param compressorName 压缩名称，见：{@link CompressorStreamFactory}
	 * @param out            输出流，可以输出到内存、网络或文件
	 * @return {@link CompressorOutputStream}
	 */
	public CompressorOutputStream getOut(String compressorName, OutputStream out) {
		try {
			return new CompressorStreamFactory().createCompressorOutputStream(compressorName, out);
		} catch (CompressorException e) {
			throw new CompressException(e);
		}
	}

	/**
	 * 获取压缩输入流，用于解压缩指定内容，支持的格式例如：
	 * <ul>
	 *     <li>{@value CompressorStreamFactory#GZIP}</li>
	 *     <li>{@value CompressorStreamFactory#BZIP2}</li>
	 *     <li>{@value CompressorStreamFactory#XZ}</li>
	 *     <li>{@value CompressorStreamFactory#PACK200}</li>
	 *     <li>{@value CompressorStreamFactory#SNAPPY_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_BLOCK}</li>
	 *     <li>{@value CompressorStreamFactory#LZ4_FRAMED}</li>
	 *     <li>{@value CompressorStreamFactory#ZSTANDARD}</li>
	 *     <li>{@value CompressorStreamFactory#DEFLATE}</li>
	 * </ul>
	 *
	 * @param compressorName 压缩名称，见：{@link CompressorStreamFactory}，null表示自动检测
	 * @param in            输出流，可以输出到内存、网络或文件
	 * @return {@link CompressorOutputStream}
	 */
	public CompressorInputStream getIn(String compressorName, InputStream in) {
		in = IoUtil.toMarkSupportStream(in);
		try {
			if(StrUtil.isBlank(compressorName)){
				compressorName = CompressorStreamFactory.detect(in);
			}
			return new CompressorStreamFactory().createCompressorInputStream(compressorName, in);
		} catch (CompressorException e) {
			throw new CompressException(e);
		}
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
	 * @param out          归档输出的流
	 * @return Archiver
	 */
	public static Archiver createArchiver(Charset charset, String archiverName, OutputStream out) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SevenZArchiver(out);
		}
		return StreamArchiver.create(charset, archiverName, out);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset 编码，7z格式此参数无效
	 * @param file    归档文件
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(Charset charset, File file) {
		return createExtractor(charset, null, file);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码，7z格式此参数无效
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}，null表示自动识别
	 * @param file         归档文件
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(Charset charset, String archiverName, File file) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SenvenZExtractor(file);
		}
		try {
			return new StreamExtractor(charset, archiverName, file);
		} catch (CompressException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")) {
				return new SenvenZExtractor(file);
			}
			throw e;
		}
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset 编码，7z格式此参数无效
	 * @param in      归档输入的流
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(Charset charset, InputStream in) {
		return createExtractor(charset, null, in);
	}

	/**
	 * 创建归档解包器，支持：
	 * <ul>
	 *     <li>{@link ArchiveStreamFactory#AR}</li>
	 *     <li>{@link ArchiveStreamFactory#CPIO}</li>
	 *     <li>{@link ArchiveStreamFactory#JAR}</li>
	 *     <li>{@link ArchiveStreamFactory#TAR}</li>
	 *     <li>{@link ArchiveStreamFactory#ZIP}</li>
	 *     <li>{@link ArchiveStreamFactory#SEVEN_Z}</li>
	 * </ul>
	 *
	 * @param charset      编码，7z格式此参数无效
	 * @param archiverName 归档类型名称，见{@link ArchiveStreamFactory}，null表示自动识别
	 * @param in           归档输入的流
	 * @return {@link Extractor}
	 */
	public static Extractor createExtractor(Charset charset, String archiverName, InputStream in) {
		if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
			return new SenvenZExtractor(in);
		}
		try {
			return new StreamExtractor(charset, archiverName, in);
		} catch (CompressException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")) {
				return new SenvenZExtractor(in);
			}
			throw e;
		}
	}
}
