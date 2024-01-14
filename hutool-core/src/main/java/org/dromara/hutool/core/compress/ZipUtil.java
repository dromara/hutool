/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.collection.iter.EnumerationIter;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileSystemUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.file.PathUtil;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.io.stream.LimitedInputStream;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 *
 * @author Looly
 * @see ZipWriter
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
	 * @param charset 解析zip文件的编码，null表示{@link CharsetUtil#UTF_8}
	 * @return {@link ZipFile}
	 */
	public static ZipFile toZipFile(final File file, final Charset charset) {
		try {
			return new ZipFile(file, ObjUtil.defaultIfNull(charset, CharsetUtil.UTF_8));
		} catch (final IOException e) {
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
	public static InputStream getStream(final ZipFile zipFile, final ZipEntry zipEntry) {
		try {
			return new LimitedInputStream(zipFile.getInputStream(zipEntry), zipEntry.getSize());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得 {@link ZipOutputStream}
	 *
	 * @param out     压缩文件流
	 * @param charset 编码
	 * @return {@link ZipOutputStream}
	 * @since 5.8.0
	 */
	public static ZipOutputStream getZipOutputStream(final OutputStream out, final Charset charset) {
		if (out instanceof ZipOutputStream) {
			return (ZipOutputStream) out;
		}
		return new ZipOutputStream(out, charset);
	}

	/**
	 * 在zip文件中添加新文件或目录<br>
	 * 新文件添加在zip根目录，文件夹包括其本身和内容<br>
	 * 如果待添加文件夹是系统根路径（如/或c:/），则只复制文件夹下的内容
	 *
	 * @param zipPath        zip文件的Path
	 * @param appendFilePath 待添加文件Path(可以是文件夹)
	 * @param options        拷贝选项，可选是否覆盖等
	 * @throws IORuntimeException IO异常
	 * @since 5.7.15
	 */
	public static void append(final Path zipPath, final Path appendFilePath, final CopyOption... options) throws IORuntimeException {
		try (final FileSystem zipFileSystem = FileSystemUtil.createZip(zipPath.toString())) {
			if (Files.isDirectory(appendFilePath)) {
				Path source = appendFilePath.getParent();
				if (null == source) {
					// 如果用户提供的是根路径，则不复制目录，直接复制目录下的内容
					source = appendFilePath;
				}
				Files.walkFileTree(appendFilePath, new ZipCopyVisitor(source, zipFileSystem, options));
			} else {
				Files.copy(appendFilePath, zipFileSystem.getPath(PathUtil.getName(appendFilePath)), options);
			}
		} catch (final FileAlreadyExistsException ignored) {
			// 不覆盖情况下，文件已存在, 跳过
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcPath 源文件路径
	 * @return 打包好的压缩文件
	 * @throws HutoolException IO异常
	 */
	public static File zip(final String srcPath) throws HutoolException {
		return zip(srcPath, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcPath 源文件路径
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws HutoolException IO异常
	 */
	public static File zip(final String srcPath, final Charset charset) throws HutoolException {
		return zip(FileUtil.file(srcPath), charset);
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcFile 源文件或目录
	 * @return 打包好的压缩文件
	 * @throws HutoolException IO异常
	 */
	public static File zip(final File srcFile) throws HutoolException {
		return zip(srcFile, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcFile 源文件或目录
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws HutoolException IO异常
	 */
	public static File zip(final File srcFile, final Charset charset) throws HutoolException {
		final File zipFile = FileUtil.file(srcFile.getParentFile(), FileNameUtil.mainName(srcFile) + ".zip");
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
	 * @throws HutoolException IO异常
	 */
	public static File zip(final String srcPath, final String zipPath) throws HutoolException {
		return zip(srcPath, zipPath, false);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 *
	 * @param srcPath    要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath    压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @return 压缩文件
	 * @throws HutoolException IO异常
	 */
	public static File zip(final String srcPath, final String zipPath, final boolean withSrcDir) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 */
	public static File zip(final String srcPath, final String zipPath, final Charset charset, final boolean withSrcDir) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 */
	public static File zip(final File zipFile, final boolean withSrcDir, final File... srcFiles) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 */
	public static File zip(final File zipFile, final Charset charset, final boolean withSrcDir, final File... srcFiles) throws HutoolException {
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
	public static File zip(final File zipFile, final Charset charset, final boolean withSrcDir, final FileFilter filter, final File... srcFiles) throws IORuntimeException {
		validateFiles(zipFile, srcFiles);
		//noinspection resource
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
	public static void zip(final OutputStream out, final Charset charset, final boolean withSrcDir, final FileFilter filter, final File... srcFiles) throws IORuntimeException {
		ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
	}

	/**
	 * 对流中的数据加入到压缩文件，使用默认UTF-8编码
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param data    要压缩的数据
	 * @return 压缩文件
	 * @throws HutoolException IO异常
	 * @since 3.0.6
	 */
	public static File zip(final File zipFile, final String path, final String data) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 3.2.2
	 */
	public static File zip(final File zipFile, final String path, final String data, final Charset charset) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 3.0.6
	 */
	public static File zip(final File zipFile, final String path, final InputStream in) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 3.2.2
	 */
	public static File zip(final File zipFile, final String path, final InputStream in, final Charset charset) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 3.0.9
	 */
	public static File zip(final File zipFile, final String[] paths, final InputStream[] ins) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 3.0.9
	 */
	public static File zip(final File zipFile, final String[] paths, final InputStream[] ins, final Charset charset) throws HutoolException {
		try (final ZipWriter zipWriter = ZipWriter.of(zipFile, charset)) {
			zipWriter.add(paths, ins);
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
	public static void zip(final OutputStream out, final String[] paths, final InputStream[] ins) {
		zip(getZipOutputStream(out, DEFAULT_CHARSET), paths, ins);
	}

	/**
	 * 将文件流压缩到目标流中
	 *
	 * @param zipOutputStream 目标流，压缩完成自动关闭
	 * @param paths           流数据在压缩文件中的路径或文件名
	 * @param ins             要压缩的源，添加完成后自动关闭流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.2
	 */
	public static void zip(final ZipOutputStream zipOutputStream, final String[] paths, final InputStream[] ins) throws IORuntimeException {
		try (final ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(paths, ins);
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
	 * @throws HutoolException IO异常
	 * @since 5.5.2
	 */
	public static File zip(final File zipFile, final Charset charset, final Resource... resources) throws HutoolException {
		//noinspection resource
		ZipWriter.of(zipFile, charset).add(resources).close();
		return zipFile;
	}

	// ---------------------------------------------------------------------------------------------- Unzip

	/**
	 * 解压到文件名相同的目录中，默认编码UTF-8
	 *
	 * @param zipFilePath 压缩文件路径
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 */
	public static File unzip(final String zipFilePath) throws HutoolException {
		return unzip(zipFilePath, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFilePath 压缩文件路径
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(final String zipFilePath, final Charset charset) throws HutoolException {
		return unzip(FileUtil.file(zipFilePath), charset);
	}

	/**
	 * 解压到文件名相同的目录中，使用UTF-8编码
	 *
	 * @param zipFile 压缩文件
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(final File zipFile) throws HutoolException {
		return unzip(zipFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFile 压缩文件
	 * @param charset 编码
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 * @since 3.2.2
	 */
	public static File unzip(final File zipFile, final Charset charset) throws HutoolException {
		final File destDir = FileUtil.file(zipFile.getParentFile(), FileNameUtil.mainName(zipFile));
		return unzip(zipFile, destDir, charset);
	}

	/**
	 * 解压，默认UTF-8编码
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 */
	public static File unzip(final String zipFilePath, final String outFileDir) throws HutoolException {
		return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
	}

	/**
	 * 解压
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 */
	public static File unzip(final String zipFilePath, final String outFileDir, final Charset charset) throws HutoolException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
	}

	/**
	 * 解压，默认使用UTF-8编码
	 *
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws HutoolException IO异常
	 */
	public static File unzip(final File zipFile, final File outFile) throws HutoolException {
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
	public static File unzip(final File zipFile, final File outFile, final Charset charset) {
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
	public static File unzip(final ZipFile zipFile, final File outFile) throws IORuntimeException {
		return unzip(zipFile, outFile, -1);
	}

	/**
	 * 限制解压后文件大小
	 *
	 * @param zipFile zip文件，附带编码信息，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @param limit   限制解压文件大小(单位B)
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * @since 5.8.5
	 */
	public static File unzip(final ZipFile zipFile, final File outFile, final long limit) throws IORuntimeException {
		if (outFile.exists() && outFile.isFile()) {
			throw new IllegalArgumentException(
					StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
		}

		// pr#726@Gitee
		if (limit > 0) {
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			long zipFileSize = 0L;
			ZipEntry zipEntry;
			while (zipEntries.hasMoreElements()) {
				zipEntry = zipEntries.nextElement();
				zipFileSize += zipEntry.getSize();
				if (zipFileSize > limit) {
					throw new IllegalArgumentException("The file size exceeds the limit");
				}
			}
		}

		try (final ZipReader reader = new ZipReader(zipFile)) {
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
	public static InputStream get(final File zipFile, final Charset charset, final String path) {
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
	public static InputStream get(final ZipFile zipFile, final String path) {
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
	public static void read(final ZipFile zipFile, final Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipFile)) {
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
	 * @throws HutoolException IO异常
	 * @since 4.5.8
	 */
	public static File unzip(final InputStream in, final File outFile, Charset charset) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 * @since 4.5.8
	 */
	public static File unzip(final ZipInputStream zipStream, final File outFile) throws HutoolException {
		try (final ZipReader reader = new ZipReader(zipStream)) {
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
	public static void read(final ZipInputStream zipStream, final Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipStream)) {
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
	public static byte[] unzipFileBytes(final String zipFilePath, final String name) {
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
	public static byte[] unzipFileBytes(final String zipFilePath, final Charset charset, final String name) {
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
	public static byte[] unzipFileBytes(final File zipFile, final String name) {
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
	public static byte[] unzipFileBytes(final File zipFile, final Charset charset, final String name) {
		try (final ZipReader reader = ZipReader.of(zipFile, charset)) {
			return IoUtil.readBytes(reader.get(name));
		}
	}

	// ----------------------------------------------------------------------------- Gzip

	/**
	 * Gzip压缩处理
	 *
	 * @param content 被压缩的字符串
	 * @param charset 编码 {@link StandardCharsets#UTF_8}、 {@link CharsetUtil#UTF_8}
	 * @return 压缩后的字节流
	 * @throws HutoolException IO异常
	 */
	public static byte[] gzip(final String content, final Charset charset) throws HutoolException {
		return gzip(ByteUtil.toBytes(content, charset));
	}

	/**
	 * Gzip压缩处理
	 *
	 * @param buf 被压缩的字节流
	 * @return 压缩后的字节流
	 * @throws HutoolException IO异常
	 */
	public static byte[] gzip(final byte[] buf) throws HutoolException {
		return gzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param file 被压缩的文件
	 * @return 压缩后的字节流
	 * @throws HutoolException IO异常
	 */
	public static byte[] gzip(final File file) throws HutoolException {
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return gzip(in, (int) file.length());
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param in 被压缩的流
	 * @return 压缩后的字节流
	 * @throws HutoolException IO异常
	 * @since 4.1.18
	 */
	public static byte[] gzip(final InputStream in) throws HutoolException {
		return gzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Gzip压缩文件
	 *
	 * @param in     被压缩的流
	 * @param length 预估长度
	 * @return 压缩后的字节流
	 * @throws HutoolException IO异常
	 * @since 4.1.18
	 */
	public static byte[] gzip(final InputStream in, final int length) throws HutoolException {
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
	 * @throws HutoolException IO异常
	 */
	public static String unGzip(final byte[] buf, final Charset charset) throws HutoolException {
		return StrUtil.str(unGzip(buf), charset);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param buf buf
	 * @return bytes
	 * @throws HutoolException IO异常
	 */
	public static byte[] unGzip(final byte[] buf) throws HutoolException {
		return unGzip(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param in Gzip数据
	 * @return 解压后的数据
	 * @throws HutoolException IO异常
	 */
	public static byte[] unGzip(final InputStream in) throws HutoolException {
		return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
	}

	/**
	 * Gzip解压处理
	 *
	 * @param in     Gzip数据
	 * @param length 估算长度，如果无法确定请传入{@link #DEFAULT_BYTE_ARRAY_LENGTH}
	 * @return 解压后的数据
	 * @throws HutoolException IO异常
	 * @since 4.1.18
	 */
	public static byte[] unGzip(final InputStream in, final int length) throws HutoolException {
		final FastByteArrayOutputStream bos = new FastByteArrayOutputStream(length);
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
	public static byte[] zlib(final String content, final Charset charset, final int level) {
		return zlib(ByteUtil.toBytes(content, charset), level);
	}

	/**
	 * Zlib压缩文件
	 *
	 * @param file  被压缩的文件
	 * @param level 压缩级别
	 * @return 压缩后的字节流
	 * @since 4.1.4
	 */
	public static byte[] zlib(final File file, final int level) {
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return zlib(in, level, (int) file.length());
		} finally {
			IoUtil.closeQuietly(in);
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
	public static byte[] zlib(final byte[] buf, final int level) {
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
	public static byte[] zlib(final InputStream in, final int level) {
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
	public static byte[] zlib(final InputStream in, final int level, final int length) {
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
	public static String unZlib(final byte[] buf, final Charset charset) {
		return StrUtil.str(unZlib(buf), charset);
	}

	/**
	 * 解压缩zlib
	 *
	 * @param buf 数据
	 * @return 解压后的bytes
	 * @since 4.1.4
	 */
	public static byte[] unZlib(final byte[] buf) {
		return unZlib(new ByteArrayInputStream(buf), buf.length);
	}

	/**
	 * 解压缩zlib
	 *
	 * @param in 数据流
	 * @return 解压后的bytes
	 * @since 4.1.19
	 */
	public static byte[] unZlib(final InputStream in) {
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
	public static byte[] unZlib(final InputStream in, final int length) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		Deflate.of(in, out, false).inflater();
		return out.toByteArray();
	}

	/**
	 * 获取Zip文件中指定目录下的所有文件，只显示文件，不显示目录<br>
	 * 此方法并不会关闭{@link ZipFile}。
	 *
	 * @param zipFile Zip文件
	 * @param dir     目录前缀（目录前缀不包含开头的/）
	 * @return 文件列表
	 * @since 4.6.6
	 */
	public static List<String> listFileNames(final ZipFile zipFile, String dir) {
		if (StrUtil.isNotBlank(dir)) {
			// 目录尾部添加"/"
			dir = StrUtil.addSuffixIfNot(dir, StrUtil.SLASH);
		}

		final List<String> fileNames = new ArrayList<>();
		String name;
		for (final ZipEntry entry : new EnumerationIter<>(zipFile.entries())) {
			name = entry.getName();
			if (StrUtil.isEmpty(dir) || name.startsWith(dir)) {
				final String nameSuffix = StrUtil.removePrefix(name, dir);
				if (StrUtil.isNotEmpty(nameSuffix) && !StrUtil.contains(nameSuffix, CharUtil.SLASH)) {
					fileNames.add(nameSuffix);
				}
			}
		}

		return fileNames;
	}

	/**
	 * 获取对应URL路径的jar文件，支持包括file://xxx这类路径<br>
	 * 来自：org.springframework.core.io.support.PathMatchingResourcePatternResolver#getJarFile
	 *
	 * @param jarFileUrl jar文件路径
	 * @return {@link JarFile}
	 * @throws IORuntimeException IO异常
	 * @since 6.0.0
	 */
	public static JarFile ofJar(String jarFileUrl) throws IORuntimeException{
		Assert.notBlank(jarFileUrl, "Jar file url is blank!");

		if(jarFileUrl.startsWith(UrlUtil.FILE_URL_PREFIX)){
			try{
				jarFileUrl = UrlUtil.toURI(jarFileUrl).getSchemeSpecificPart();
			} catch (final HutoolException e){
				jarFileUrl = jarFileUrl.substring(UrlUtil.FILE_URL_PREFIX.length());
			}
		}
		try {
			return new JarFile(jarFileUrl);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// ---------------------------------------------------------------------------------------------- Private method start

	/**
	 * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
	 *
	 * @param zipFile  压缩后的产生的文件路径
	 * @param srcFiles 被压缩的文件或目录
	 */
	private static void validateFiles(final File zipFile, final File... srcFiles) throws HutoolException {
		if (zipFile.isDirectory()) {
			throw new HutoolException("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile());
		}

		for (final File srcFile : srcFiles) {
			if (null == srcFile) {
				continue;
			}
			if (!srcFile.exists()) {
				throw new HutoolException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			}

			// issue#1961@Github
			// 当 zipFile =  new File("temp.zip") 时, zipFile.getParentFile() == null
			File parentFile;
			try {
				parentFile = zipFile.getCanonicalFile().getParentFile();
			} catch (final IOException e) {
				parentFile = zipFile.getParentFile();
			}

			// 压缩文件不能位于被压缩的目录内
			if (srcFile.isDirectory() && FileUtil.isSub(srcFile, parentFile)) {
				throw new HutoolException("Zip file path [{}] must not be the child directory of [{}] !", zipFile.getPath(), srcFile.getPath());
			}
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end

}
