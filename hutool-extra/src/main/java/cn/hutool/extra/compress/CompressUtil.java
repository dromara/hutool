package cn.hutool.extra.compress;

import cn.hutool.extra.compress.archiver.Archiver;
import cn.hutool.extra.compress.archiver.SevenZArchiver;
import cn.hutool.extra.compress.archiver.StreamArchiver;
import cn.hutool.extra.compress.extractor.Extractor;
import cn.hutool.extra.compress.extractor.SenvenZExtractor;
import cn.hutool.extra.compress.extractor.StreamExtractor;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;

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
		try{
			return new StreamExtractor(charset, archiverName, file);
		} catch (CompressException e){
			final Throwable cause = e.getCause();
			if(cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")){
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
		try{
			return new StreamExtractor(charset, archiverName, in);
		} catch (CompressException e){
			final Throwable cause = e.getCause();
			if(cause instanceof StreamingNotSupportedException && cause.getMessage().contains("7z")){
				return new SenvenZExtractor(in);
			}
			throw e;
		}
	}
}
