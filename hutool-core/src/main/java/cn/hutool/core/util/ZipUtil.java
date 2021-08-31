package cn.hutool.core.util;

import cn.hutool.core.compress.Deflate;
import cn.hutool.core.compress.Gzip;
import cn.hutool.core.compress.ZipReader;
import cn.hutool.core.compress.ZipWriter;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.Resource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 *
 * @see cn.hutool.core.compress.ZipWriter
 * @author Looly
 */
public class ZipUtil {

	private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

	/**
	 * 默认编码，使用平台相关编码
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

	/**
	 * 将Zip文件转换为{@link ZipFile}
	 *
	 * @param file    zip文件
	 * @param charset 解析zip文件的编码，null表示{@link CharsetUtil#CHARSET_UTF_8}
	 * @return {@link ZipFile}
	 */
	public static ZipFile toZipFile(File file, Charset charset) {
		try {
			return new ZipFile(file, ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取指定{@link ZipEntry}的流，用于读取这个entry的内容
	 *
	 * @param zipFile  {@link ZipFile}
	 * @param zipEntry {@link ZipEntry}
	 * @return 流
	 * @since 5.5.2
	 */
	public static InputStream getStream(ZipFile zipFile, ZipEntry zipEntry) {
		try {
			return zipFile.getInputStream(zipEntry);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcPath 源文件路径
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath) throws UtilException {
		return zip(srcPath, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcPath 源文件路径
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, Charset charset) throws UtilException {
		return zip(FileUtil.file(srcPath), charset);
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcFile 源文件或目录
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File srcFile) throws UtilException {
		return zip(srcFile, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcFile 源文件或目录
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File srcFile, Charset charset) throws UtilException {
		final File zipFile = FileUtil.file(srcFile.getParentFile(), FileUtil.mainName(srcFile) + ".zip");
		zip(zipFile, charset, false, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 不包含被打包目录
	 *
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @return 压缩好的Zip文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath) throws UtilException {
		return zip(srcPath, zipPath, false);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 *
	 * @param srcPath    要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath    压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws UtilException {
		return zip(srcPath, zipPath, DEFAULT_CHARSET, withSrcDir);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 *
	 * @param srcPath    要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath    压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath, Charset charset, boolean withSrcDir) throws UtilException {
		final File srcFile = FileUtil.file(srcPath);
		final File zipFile = FileUtil.file(zipPath);
		zip(zipFile, charset, withSrcDir, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 使用默认UTF-8编码
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param srcFiles   要压缩的源文件或目录。
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File zipFile, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, DEFAULT_CHARSET, withSrcDir, srcFiles);
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File zipFile, Charset charset, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, charset, withSrcDir, null, srcFiles);
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter     文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @return 压缩文件
	 * @throws IORuntimeException IO异常
	 * @since 4.6.5
	 */
	public static File zip(File zipFile, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		validateFiles(zipFile, srcFiles);
		ZipWriter.of(zipFile, charset).add(withSrcDir, filter, srcFiles).close();
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param out        生成的Zip到的目标流，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter     文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @throws IORuntimeException IO异常
	 * @since 5.1.1
	 */
	public static void zip(OutputStream out, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipOutputStream 生成的Zip到的目标流，不关闭此流
	 * @param withSrcDir      是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter          文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles        要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @throws IORuntimeException IO异常
	 * @since 5.1.1
	 * @deprecated 请使用 {@link #zip(OutputStream, Charset, boolean, FileFilter, File...)}
	 */
	@Deprecated
	public static void zip(ZipOutputStream zipOutputStream, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		try(final ZipWriter zipWriter = new ZipWriter(zipOutputStream)){
			zipWriter.add(withSrcDir, filter, srcFiles);
		}
	}

	/**
	 * 对流中的数据加入到压缩文件，使用默认UTF-8编码
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param data    要压缩的数据
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.0.6
	 */
	public static File zip(File zipFile, String path, String data) throws UtilException {
		return zip(zipFile, path, data, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param data    要压缩的数据
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.2.2
	 */
	public static File zip(File zipFile, String path, String data, Charset charset) throws UtilException {
		return zip(zipFile, path, IoUtil.toStream(data, charset), charset);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 使用默认编码UTF-8
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param in      要压缩的源
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.0.6
	 */
	public static File zip(File zipFile, String path, InputStream in) throws UtilException {
		return zip(zipFile, path, in, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param in      要压缩的源，默认关闭
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.2.2
	 */
	public static File zip(File zipFile, String path, InputStream in, Charset charset) throws UtilException {
		return zip(zipFile, new String[]{path}, new InputStream[]{in}, charset);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param paths   流数据在压缩文件中的路径或文件名
	 * @param ins     要压缩的源，添加完成后自动关闭流
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.0.9
	 */
	public static File zip(File zipFile, String[] paths, InputStream[] ins) throws UtilException {
		return zip(zipFile, paths, ins, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param paths   流数据在压缩文件中的路径或文件名
	 * @param ins     要压缩的源，添加完成后自动关闭流
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 3.0.9
	 */
	public static File zip(File zipFile, String[] paths, InputStream[] ins, Charset charset) throws UtilException {
		if (ArrayUtil.isEmpty(paths) || ArrayUtil.isEmpty(ins)) {
			throw new IllegalArgumentException("Paths or ins is empty !");
		}
		if (paths.length != ins.length) {
			throw new IllegalArgumentException("Paths length is not equals to ins length !");
		}

		try(final ZipWriter zipWriter = ZipWriter.of(zipFile, charset)){
			for (int i = 0; i < paths.length; i++) {
				zipWriter.add(paths[i], ins[i]);
			}
		}

		return zipFile;
	}

	/**
	 * 将文件流压缩到目标流中
	 *
	 * @param out   目标流，压缩完成自动关闭
	 * @param paths 流数据在压缩文件中的路径或文件名
	 * @param ins   要压缩的源，添加完成后自动关闭流
	 * @since 5.5.2
	 */
	public static void zip(OutputStream out, String[] paths, InputStream[] ins) {
		if (ArrayUtil.isEmpty(paths) || ArrayUtil.isEmpty(ins)) {
			throw new IllegalArgumentException("Paths or ins is empty !");
		}
		if (paths.length != ins.length) {
			throw new IllegalArgumentException("Paths length is not equals to ins length !");
		}

		try(final ZipWriter zipWriter = ZipWriter.of(out, DEFAULT_CHARSET)){
			for (int i = 0; i < paths.length; i++) {
				zipWriter.add(paths[i], ins[i]);
			}
		}
	}

	/**
	 * 将文件流压缩到目标流中
	 *
	 * @param zipOutputStream 目标流，压缩完成不关闭
	 * @param paths           流数据在压缩文件中的路径或文件名
	 * @param ins             要压缩的源，添加完成后自动关闭流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.2
	 */
	public static void zip(ZipOutputStream zipOutputStream, String[] paths, InputStream[] ins) throws IORuntimeException {
		if (ArrayUtil.isEmpty(paths) || ArrayUtil.isEmpty(ins)) {
			throw new IllegalArgumentException("Paths or ins is empty !");
		}
		if (paths.length != ins.length) {
			throw new IllegalArgumentException("Paths length is not equals to ins length !");
		}

		try(final ZipWriter zipWriter = new ZipWriter(zipOutputStream)){
			for (int i = 0; i < paths.length; i++) {
				zipWriter.add(paths[i], ins[i]);
			}
		}
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile   生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset   编码
	 * @param resources 需要压缩的资源，资源的路径为{@link Resource#getName()}
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * @since 5.5.2
	 */
	public static File zip(File zipFile, Charset charset, Resource... resources) throws UtilException {
		ZipWriter.of(zipFile, charset).add(resources).close();
		return zipFile;
	}

	// ---------------------------------------------------------------------------------------------- Unzip

	/**
	 * 解压到文件名相同的目录中，默认编码UTF-8
	 *
	 * @param zipFilePath 压缩文件路径
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath) throws UtilException {
		return unzip(zipFilePath, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFilePath 压缩文件路径
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(String zipFilePath, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), charset);
	}

	/**
	 * 解压到文件名相同的目录中，使用UTF-8编码
	 *
	 * @param zipFile 压缩文件
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(File zipFile) throws UtilException {
		return unzip(zipFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFile 压缩文件
	 * @param charset 编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(File zipFile, Charset charset) throws UtilException {
		final File destDir = FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile));
		return unzip(zipFile, destDir, charset);
	}

	/**
	 * 解压，默认UTF-8编码
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath, String outFileDir) throws UtilException {
		return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
	}

	/**
	 * 解压
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath, String outFileDir, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
	}

	/**
	 * 解压，默认使用UTF-8编码
	 *
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(File zipFile, File outFile) throws UtilException {
		return unzip(zipFile, outFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压
	 *
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @param charset 编码
	 * @return 解压的目录
	 * @since 3.2.2
	 */
	public static File unzip(File zipFile, File outFile, Charset charset) {
		return unzip(toZipFile(zipFile, charset), outFile);
	}

	/**
	 * 解压
	 *
	 * @param zipFile zip文件，附带编码信息，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * @since 4.5.8
	 */
	public static File unzip(ZipFile zipFile, File outFile) throws IORuntimeException {
		if (outFile.exists() && outFile.isFile()) {
			throw new IllegalArgumentException(
					StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
		}

		try(final ZipReader reader = new ZipReader(zipFile)){
			reader.readTo(outFile);
		}
		return outFile;
	}

	/**
	 * 获取压缩包中的指定文件流
	 *
	 * @param zipFile 压缩文件
	 * @param charset 编码
	 * @param path    需要提取文件的文件名或路径
	 * @return 压缩文件流，如果未找到返回{@code null}
	 * @since 5.5.2
	 */
	public static InputStream get(File zipFile, Charset charset, String path) {
		return get(toZipFile(zipFile, charset), path);
	}

	/**
	 * 获取压缩包中的指定文件流
	 *
	 * @param zipFile 压缩文件
	 * @param path    需要提取文件的文件名或路径
	 * @return 压缩文件流，如果未找到返回{@code null}
	 * @since 5.5.2
	 */
	public static InputStream get(ZipFile zipFile, String path) {
		final ZipEntry entry = zipFile.getEntry(path);
		if (null != entry) {
			return getStream(zipFile, entry);
		}
		return null;
	}

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param zipFile  Zip文件
	 * @param consumer {@link ZipEntry}处理器
	 * @since 5.5.2
	 */
	public static void read(ZipFile zipFile, Consumer<ZipEntry> consumer) {
		try(final ZipReader reader = new ZipReader(zipFile)){
			reader.read(consumer);
		}
	}

	/**
	 * 解压<br>
	 * ZIP条目不使用高速缓冲。
	 *
	 * @param in      zip文件流，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @param charset 编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * @since 4.5.8
	 */
	public static File unzip(InputStream in, File outFile, Charset charset) throws UtilException {
		if (null == charset) {
			charset = DEFAULT_CHARSET;
		}
		return unzip(new ZipInputStream(in, charset), outFile);
	}

	/**
	 * 解压<br>
	 * ZIP条目不使用高速缓冲。
	 *
	 * @param zipStream zip文件流，包含编码信息
	 * @param outFile   解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * @since 4.5.8
	 */
	public static File unzip(ZipInputStream zipStream, File outFile) throws UtilException {
		try(final ZipReader reader = new ZipReader(zipStream)){
			reader.readTo(outFile);
		}
		return outFile;
	}

	/**
	 * 读取并处理Zip流中的每一个{@link ZipEntry}
	 *
	 * @param zipStream zip文件流，包含编码信息
	 * @param consumer  {@link ZipEntry}处理器
	 * @since 5.5.2
	 */
	public static void read(ZipInputStream zipStream, Consumer<ZipEntry> consumer) {
		try(final ZipReader reader = new ZipReader(zipStream)){
			reader.read(consumer);
		}
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFilePath Zip文件
	 * @param name        文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * @since 4.1.8
	 */
	public static byte[] unzipFileBytes(String zipFilePath, String name) {
		return unzipFileBytes(zipFilePath, DEFAULT_CHARSET, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFilePath Zip文件
	 * @param charset     编码
	 * @param name        文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * @since 4.1.8
	 */
	public static byte[] unzipFileBytes(String zipFilePath, Charset charset, String name) {
		return unzipFileBytes(FileUtil.file(zipFilePath), charset, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFile Zip文件
	 * @param name    文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * @since 4.1.8
	 */
	public static byte[] unzipFileBytes(File zipFile, String name) {
		return unzipFileBytes(zipFile, DEFAULT_CHARSET, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFile Zip文件
	 * @param charset 编码
	 * @param name    文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * @since 4.1.8
	 */
	public static byte[] unzipFileBytes(File zipFile, Charset charset, String name) {
		try(final ZipReader reader = ZipReader.of(zipFile, charset)){
			return IoUtil.readBytes(reader.get(name));
		}
	}

	// ----------------------------------------------------------------------------- Gzip

	/**
	 * Gzip压缩处理
	 *
	 * @param content 被压缩的字符串
	 * @param charset 编码
	 * @return 压缩后的字节流
	 * @throws UtilException IO异常
	 */
	public static byte[] gzip(String content, String charset) throws UtilException {
		return gzip(StrUtil.bytes(content, charset));
	}

	/**
	 * Gzip压缩处理
	 *
	 * @param buf 被压缩的字节流
	 * @return 压缩后的字节流
	 * @throws UtilException IO异常
	 */
	public static byte[] gzip(byte[] buf) throws UtilException {
		return gzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param file 被压缩的文件
	 * @return 压缩后的字节流
	 * @throws UtilException IO异常
	 */
	public static byte[] gzip(File file) throws UtilException {
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return gzip(in, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param in 被压缩的流
	 * @return 压缩后的字节流
	 * @throws UtilException IO异常
	 * @since 4.1.18
	 */
	public static byte[] gzip(InputStream in) throws UtilException {
		return gzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param in     被压缩的流
	 * @param length 预估长度
	 * @return 压缩后的字节流
	 * @throws UtilException IO异常
	 * @since 4.1.18
	 */
	public static byte[] gzip(InputStream in, int length) throws UtilException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
		Gzip.of(in, bos).gzip().close();
		return bos.toByteArray();
	}

	/**
	 * Gzip解压缩处理
	 *
	 * @param buf     压缩过的字节流
	 * @param charset 编码
	 * @return 解压后的字符串
	 * @throws UtilException IO异常
	 */
	public static String unGzip(byte[] buf, String charset) throws UtilException {
		return StrUtil.str(unGzip(buf), charset);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param buf buf
	 * @return bytes
	 * @throws UtilException IO异常
	 */
	public static byte[] unGzip(byte[] buf) throws UtilException {
		return unGzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param in Gzip数据
	 * @return 解压后的数据
	 * @throws UtilException IO异常
	 */
	public static byte[] unGzip(InputStream in) throws UtilException {
		return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param in     Gzip数据
	 * @param length 估算长度，如果无法确定请传入{@link #DEFAULT_BYTE_ARRAY_LENGTH}
	 * @return 解压后的数据
	 * @throws UtilException IO异常
	 * @since 4.1.18
	 */
	public static byte[] unGzip(InputStream in, int length) throws UtilException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(length);
		Gzip.of(in, bos).unGzip().close();
		return bos.toByteArray();
	}

	// ----------------------------------------------------------------------------- Zlib

	/**
	 * Zlib压缩处理
	 *
	 * @param content 被压缩的字符串
	 * @param charset 编码
	 * @param level   压缩级别，1~9
	 * @return 压缩后的字节流
	 * @since 4.1.4
	 */
	public static byte[] zlib(String content, String charset, int level) {
		return zlib(StrUtil.bytes(content, charset), level);
	}

	/**
	 * Zlib压缩文件
	 *
	 * @param file  被压缩的文件
	 * @param level 压缩级别
	 * @return 压缩后的字节流
	 * @since 4.1.4
	 */
	public static byte[] zlib(File file, int level) {
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return zlib(in, level, (int) file.length());
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 打成Zlib压缩包
	 *
	 * @param buf   数据
	 * @param level 压缩级别，0~9
	 * @return 压缩后的bytes
	 * @since 4.1.4
	 */
	public static byte[] zlib(byte[] buf, int level) {
		return zlib(new ByteArrayInputStream(buf), level, buf.length);
	}

	/**
	 * 打成Zlib压缩包
	 *
	 * @param in    数据流
	 * @param level 压缩级别，0~9
	 * @return 压缩后的bytes
	 * @since 4.1.19
	 */
	public static byte[] zlib(InputStream in, int level) {
		return zlib(in, level, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * 打成Zlib压缩包
	 *
	 * @param in     数据流
	 * @param level  压缩级别，0~9
	 * @param length 预估大小
	 * @return 压缩后的bytes
	 * @since 4.1.19
	 */
	public static byte[] zlib(InputStream in, int level, int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).deflater(level);
		return out.toByteArray();
	}

	/**
	 * Zlib解压缩处理
	 *
	 * @param buf     压缩过的字节流
	 * @param charset 编码
	 * @return 解压后的字符串
	 * @since 4.1.4
	 */
	public static String unZlib(byte[] buf, String charset) {
		return StrUtil.str(unZlib(buf), charset);
	}

	/**
	 * 解压缩zlib
	 *
	 * @param buf 数据
	 * @return 解压后的bytes
	 * @since 4.1.4
	 */
	public static byte[] unZlib(byte[] buf) {
		return unZlib(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * 解压缩zlib
	 *
	 * @param in 数据流
	 * @return 解压后的bytes
	 * @since 4.1.19
	 */
	public static byte[] unZlib(InputStream in) {
		return unZlib(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * 解压缩zlib
	 *
	 * @param in     数据流
	 * @param length 预估长度
	 * @return 解压后的bytes
	 * @since 4.1.19
	 */
	public static byte[] unZlib(InputStream in, int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).inflater();
		return out.toByteArray();
	}

	/**
	 * 获取Zip文件中指定目录下的所有文件，只显示文件，不显示目录
	 *
	 * @param zipFile Zip文件
	 * @param dir     目录前缀（目录前缀不包含开头的/）
	 * @return 文件列表
	 * @since 4.6.6
	 */
	public static List<String> listFileNames(ZipFile zipFile, String dir) {
		if (StrUtil.isNotBlank(dir)) {
			// 目录尾部添加"/"
			dir = StrUtil.addSuffixIfNot(dir, StrUtil.SLASH);
		}

		final List<String> fileNames = new ArrayList<>();
		String name;
		for (ZipEntry entry : Collections.list(zipFile.entries())) {
			name = entry.getName();
			if (StrUtil.isEmpty(dir) || name.startsWith(dir)) {
				final String nameSuffix = StrUtil.removePrefix(name, dir);
				if (StrUtil.isNotEmpty(nameSuffix) && false == StrUtil.contains(nameSuffix, CharUtil.SLASH)) {
					fileNames.add(nameSuffix);
				}
			}
		}

		return fileNames;
	}

	// ---------------------------------------------------------------------------------------------- Private method start

	/**
	 * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
	 *
	 * @param zipFile  压缩后的产生的文件路径
	 * @param srcFiles 被压缩的文件或目录
	 */
	private static void validateFiles(File zipFile, File... srcFiles) throws UtilException {
		if (zipFile.isDirectory()) {
			throw new UtilException("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile());
		}

		for (File srcFile : srcFiles) {
			if (null == srcFile) {
				continue;
			}
			if (false == srcFile.exists()) {
				throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			}

			// 压缩文件不能位于被压缩的目录内
			if (srcFile.isDirectory() && FileUtil.isSub(srcFile, zipFile.getParentFile())) {
				throw new UtilException("Zip file path [{}] must not be the child directory of [{}] !", zipFile.getPath(), srcFile.getPath());
			}
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end

}
