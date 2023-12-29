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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * NIO中Path对象操作封装
 *
 * @author looly
 * @since 5.4.1
 */
public class PathUtil {
	/**
	 * 拼接多个路径
	 *
	 * @param firstPath 第一个路径
	 * @param paths     其它路径
	 * @return 拼接后的路径
	 * @see Paths#get(String, String...)
	 */
	public static Path of(final String firstPath, final String... paths) {
		return Paths.get(firstPath, paths);
	}

	/**
	 * 拼接多个路径，
	 *
	 * @param firstPath 第一个路径
	 * @param paths     其它路径
	 * @return 拼接后的路径
	 */
	public static Path of(Path firstPath, final Path... paths) {
		if (ArrayUtil.isEmpty(paths)) {
			return firstPath;
		}

		for (final Path path : paths) {
			if (null == path) {
				continue;
			}
			if (null == firstPath) {
				firstPath = path;
			} else {
				firstPath = firstPath.resolve(path);
			}
		}
		return firstPath;
	}

	/**
	 * 目录是否为空
	 *
	 * @param dirPath 目录
	 * @return 是否为空
	 * @throws IORuntimeException IOException
	 */
	public static boolean isDirEmpty(final Path dirPath) {
		try (final DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
			return !dirStream.iterator().hasNext();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供path为文件，直接返回过滤结果
	 *
	 * @param path       当前遍历文件或目录
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录，null表示接收全部文件
	 * @return 文件列表
	 * @since 5.4.1
	 */
	public static List<File> loopFiles(final Path path, final FileFilter fileFilter) {
		return loopFiles(path, -1, fileFilter);
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供path为文件，直接返回过滤结果
	 *
	 * @param path       当前遍历文件或目录
	 * @param maxDepth   遍历最大深度，-1表示遍历到没有目录为止
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录，null表示接收全部文件
	 * @return 文件列表
	 * @since 5.4.1
	 */
	public static List<File> loopFiles(final Path path, final int maxDepth, final FileFilter fileFilter) {
		return loopFiles(path, maxDepth, false, fileFilter);
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供path为文件，直接返回过滤结果
	 *
	 * @param path          当前遍历文件或目录
	 * @param maxDepth      遍历最大深度，-1表示遍历到没有目录为止
	 * @param isFollowLinks 是否跟踪软链（快捷方式）
	 * @param fileFilter    文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录，null表示接收全部文件
	 * @return 文件列表
	 * @since 5.4.1
	 */
	public static List<File> loopFiles(final Path path, final int maxDepth, final boolean isFollowLinks, final FileFilter fileFilter) {
		final List<File> fileList = new ArrayList<>();

		if (!exists(path, isFollowLinks)) {
			return fileList;
		} else if (!isDirectory(path, isFollowLinks)) {
			final File file = path.toFile();
			if (null == fileFilter || fileFilter.accept(file)) {
				fileList.add(file);
			}
			return fileList;
		}

		walkFiles(path, maxDepth, isFollowLinks, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) {
				final File file = path.toFile();
				if (null == fileFilter || fileFilter.accept(file)) {
					fileList.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});

		return fileList;
	}

	// region ----- walkFiles

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start   起始路径，必须为目录
	 * @param visitor {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 * @since 5.5.2
	 */
	public static void walkFiles(final Path start, final FileVisitor<? super Path> visitor) {
		walkFiles(start, -1, visitor);
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start    起始路径，必须为目录
	 * @param maxDepth 最大遍历深度，-1表示不限制深度
	 * @param visitor  {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 * @since 4.6.3
	 */
	public static void walkFiles(final Path start, final int maxDepth, final FileVisitor<? super Path> visitor) {
		walkFiles(start, maxDepth, false, visitor);
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start         起始路径，必须为目录
	 * @param maxDepth      最大遍历深度，-1表示不限制深度
	 * @param visitor       {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
	 * @param isFollowLinks 是否追踪到软链对应的真实地址
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 * @since 5.8.23
	 */
	public static void walkFiles(final Path start, int maxDepth, final boolean isFollowLinks, final FileVisitor<? super Path> visitor) {
		if (maxDepth < 0) {
			// < 0 表示遍历到最底层
			maxDepth = Integer.MAX_VALUE;
		}

		try {
			Files.walkFileTree(start, getFileVisitOption(isFollowLinks), maxDepth, visitor);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
	// endregion

	/**
	 * 删除文件或者文件夹，不追踪软链<br>
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param path 文件对象
	 * @throws IORuntimeException IO异常
	 * @since 4.4.2
	 */
	public static void del(final Path path) throws IORuntimeException {
		PathDeleter.of(path).del();
	}

	/**
	 * 清空目录
	 *
	 * @param path 目录路径
	 */
	public static void clean(final Path path) {
		PathDeleter.of(path).clean();
	}

	/**
	 * 复制src到target中
	 * <ul>
	 *     <li>src路径和target路径相同时，不执行操作</li>
	 *     <li>src为文件，target为已存在目录，则拷贝到目录下，文件名不变。</li>
	 *     <li>src为文件，target为不存在路径，则目标以文件对待（自动创建父级目录），相当于拷贝后重命名，比如：/dest/aaa，如果aaa不存在，则aaa被当作文件名</li>
	 *     <li>src为文件，target是一个已存在的文件，则当{@link CopyOption}设为覆盖时会被覆盖，默认不覆盖，抛出{@link FileAlreadyExistsException}</li>
	 *     <li>src为目录，target为已存在目录，整个src目录连同其目录拷贝到目标目录中</li>
	 *     <li>src为目录，target为不存在路径，则自动创建目标为新目录，并只拷贝src内容到目标目录中，相当于重命名目录。</li>
	 *     <li>src为目录，target为文件，抛出{@link IllegalArgumentException}</li>
	 * </ul>
	 *
	 * @param src     源文件路径，如果为目录会在目标中创建新目录
	 * @param target  目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 */
	public static Path copy(final Path src, final Path target, final CopyOption... options) throws IORuntimeException {
		return PathCopier.of(src, target, options).copy();
	}

	/**
	 * 复制src的内容到target中
	 * <ul>
	 *     <li>src路径和target路径相同时，不执行操作</li>
	 *     <li>src为文件，target为已存在目录，则拷贝到目录下，文件名不变。</li>
	 *     <li>src为文件，target为不存在路径，则目标以文件对待（自动创建父级目录），相当于拷贝后重命名，比如：/dest/aaa，如果aaa不存在，则aaa被当作文件名</li>
	 *     <li>src为文件，target是一个已存在的文件，则当{@link CopyOption}设为覆盖时会被覆盖，默认不覆盖，抛出{@link FileAlreadyExistsException}</li>
	 *     <li>src为目录，target为已存在目录，整个src目录下的内容拷贝到目标目录中</li>
	 *     <li>src为目录，target为不存在路径，则自动创建目标为新目录，整个src目录下的内容拷贝到目标目录中，相当于重命名目录。</li>
	 *     <li>src为目录，target为文件，抛出IO异常</li>
	 * </ul>
	 *
	 * @param src     源文件路径，如果为目录只在目标中创建新目录
	 * @param target  目标目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 */
	public static Path copyContent(final Path src, final Path target, final CopyOption... options) throws IORuntimeException {
		return PathCopier.of(src, target, options).copyContent();
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false<br>
	 * 此方法不会追踪到软链对应的真实地址，即软链被当作文件
	 *
	 * @param path {@link Path}
	 * @return 如果为目录true
	 * @since 5.5.1
	 */
	public static boolean isDirectory(final Path path) {
		return isDirectory(path, false);
	}

	/**
	 * 判断是否存在且为非目录
	 * <ul>
	 *     <li>如果path为{@code null}，返回{@code false}</li>
	 *     <li>如果path不存在，返回{@code false}</li>
	 * </ul>
	 *
	 * @param path          {@link Path}
	 * @param isFollowLinks 是否追踪到软链对应的真实地址
	 * @return 如果为目录true
	 * @since 3.1.0
	 */
	public static boolean isExistsAndNotDirectory(final Path path, final boolean isFollowLinks) {
		return exists(path, isFollowLinks) && !isDirectory(path, isFollowLinks);
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false
	 *
	 * @param path          {@link Path}
	 * @param isFollowLinks 是否追踪到软链对应的真实地址
	 * @return 如果为目录true
	 * @since 3.1.0
	 */
	public static boolean isDirectory(final Path path, final boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		return Files.isDirectory(path, getLinkOptions(isFollowLinks));
	}

	/**
	 * 获取指定位置的子路径部分，支持负数，例如index为-1表示从后数第一个节点位置
	 *
	 * @param path  路径
	 * @param index 路径节点位置，支持负数（负数从后向前计数）
	 * @return 获取的子路径
	 * @since 3.1.2
	 */
	public static Path getPathEle(final Path path, final int index) {
		return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
	}

	/**
	 * 获取指定位置的最后一个子路径部分
	 *
	 * @param path 路径
	 * @return 获取的最后一个子路径
	 * @since 3.1.2
	 */
	public static Path getLastPathEle(final Path path) {
		return getPathEle(path, path.getNameCount() - 1);
	}

	/**
	 * 获取指定位置的子路径部分，支持负数，例如起始为-1表示从后数第一个节点位置
	 *
	 * @param path      路径
	 * @param fromIndex 起始路径节点（包括）
	 * @param toIndex   结束路径节点（不包括）
	 * @return 获取的子路径
	 * @since 3.1.2
	 */
	public static Path subPath(final Path path, int fromIndex, int toIndex) {
		if (null == path) {
			return null;
		}
		final int len = path.getNameCount();

		if (fromIndex < 0) {
			fromIndex = len + fromIndex;
			if (fromIndex < 0) {
				fromIndex = 0;
			}
		} else if (fromIndex > len) {
			fromIndex = len;
		}

		if (toIndex < 0) {
			toIndex = len + toIndex;
			if (toIndex < 0) {
				toIndex = len;
			}
		} else if (toIndex > len) {
			toIndex = len;
		}

		if (toIndex < fromIndex) {
			final int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}

		if (fromIndex == toIndex) {
			return null;
		}
		return path.subpath(fromIndex, toIndex);
	}

	/**
	 * 获取文件属性
	 *
	 * @param path          文件路径{@link Path}
	 * @param isFollowLinks 是否跟踪到软链对应的真实路径
	 * @return {@link BasicFileAttributes}
	 * @throws IORuntimeException IO异常
	 */
	public static BasicFileAttributes getAttributes(final Path path, final boolean isFollowLinks) throws IORuntimeException {
		if (null == path) {
			return null;
		}

		try {
			return Files.readAttributes(path, BasicFileAttributes.class, getLinkOptions(isFollowLinks));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得输入流
	 *
	 * @param path Path
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 * @since 4.0.0
	 */
	public static BufferedInputStream getInputStream(final Path path) throws IORuntimeException {
		final InputStream in;
		try {
			in = Files.newInputStream(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return IoUtil.toBuffered(in);
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param path 文件Path
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 * @since 4.0.0
	 */
	public static BufferedReader getUtf8Reader(final Path path) throws IORuntimeException {
		return getReader(path, CharsetUtil.UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param path    文件Path
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 * @since 4.0.0
	 */
	public static BufferedReader getReader(final Path path, final Charset charset) throws IORuntimeException {
		return IoUtil.toReader(getInputStream(path), charset);
	}

	/**
	 * 读取文件的所有内容为byte数组
	 *
	 * @param path 文件
	 * @return byte数组
	 * @since 5.5.4
	 */
	public static byte[] readBytes(final Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得输出流
	 *
	 * @param path Path
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 * @since 5.4.1
	 */
	public static BufferedOutputStream getOutputStream(final Path path) throws IORuntimeException {
		final OutputStream in;
		try {
			in = Files.newOutputStream(path);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return IoUtil.toBuffered(in);
	}

	/**
	 * 修改文件或目录的文件名，不变更路径，只是简单修改文件名<br>
	 *
	 * <pre>
	 * FileUtil.rename(file, "aaa.jpg", false) xx/xx.png =》xx/aaa.jpg
	 * FileUtil.rename(dir, "dir2", false) xx/xx/ =》xx/dir2/
	 * </pre>
	 *
	 * @param path       被修改的文件
	 * @param newName    新的文件名，包括扩展名
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 * @since 5.4.1
	 */
	public static Path rename(final Path path, final String newName, final boolean isOverride) {
		return move(path, path.resolveSibling(newName), isOverride);
	}

	/**
	 * 移动文件或目录到目标中，例如：
	 * <ul>
	 *     <li>如果src和target为同一文件或目录，直接返回target。</li>
	 *     <li>如果src为文件，target为目录，则移动到目标目录下，存在同名文件则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为文件，则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为不存在的路径，则重命名源文件到目标指定的文件，如moveContent("/a/b", "/c/d"), d不存在，则b变成d。</li>
	 *     <li>如果src为目录，target为文件，抛出{@link IllegalArgumentException}</li>
	 *     <li>如果src为目录，target为目录，则将源目录及其内容移动到目标路径目录中，如move("/a/b", "/c/d")，结果为"/c/d/b"</li>
	 *     <li>如果src为目录，target为不存在的路径，则重命名src到target，如move("/a/b", "/c/d")，结果为"/c/d/"，相当于b重命名为d</li>
	 * </ul>
	 *
	 * @param src        源文件或目录路径
	 * @param target     目标路径，如果为目录，则移动到此目录下
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 */
	public static Path move(final Path src, final Path target, final boolean isOverride) {
		return PathMover.of(src, target, isOverride).move();
	}

	/**
	 * 移动文件或目录内容到目标中，例如：
	 * <ul>
	 *     <li>如果src为文件，target为目录，则移动到目标目录下，存在同名文件则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为文件，则按照是否覆盖参数执行。</li>
	 *     <li>如果src为文件，target为不存在的路径，则重命名源文件到目标指定的文件，如moveContent("/a/b", "/c/d"), d不存在，则b变成d。</li>
	 *     <li>如果src为目录，target为文件，抛出{@link IllegalArgumentException}</li>
	 *     <li>如果src为目录，target为目录，则将源目录下的内容移动到目标路径目录中，源目录不删除。</li>
	 *     <li>如果src为目录，target为不存在的路径，则创建目标路径为目录，将源目录下的内容移动到目标路径目录中，源目录不删除。</li>
	 * </ul>
	 *
	 * @param src        源文件或目录路径
	 * @param target     目标路径，如果为目录，则移动到此目录下
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 */
	public static Path moveContent(final Path src, final Path target, final boolean isOverride) {
		return PathMover.of(src, target, isOverride).moveContent();
	}

	/**
	 * 检查两个文件是否是同一个文件<br>
	 * 所谓文件相同，是指Path对象是否指向同一个文件或文件夹
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 是否相同
	 * @throws IORuntimeException IO异常
	 * @see Files#isSameFile(Path, Path)
	 * @since 5.4.1
	 */
	public static boolean equals(final Path file1, final Path file2) throws IORuntimeException {
		try {
			return Files.isSameFile(file1, file2);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 判断是否为文件，如果file为null，则返回false
	 *
	 * @param path          文件
	 * @param isFollowLinks 是否跟踪软链（快捷方式）
	 * @return 如果为文件true
	 * @see Files#isRegularFile(Path, LinkOption...)
	 */
	public static boolean isFile(final Path path, final boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		return Files.isRegularFile(path, getLinkOptions(isFollowLinks));
	}

	/**
	 * 判断是否为符号链接文件
	 *
	 * @param path 被检查的文件
	 * @return 是否为符号链接文件
	 * @since 4.4.2
	 */
	public static boolean isSymlink(final Path path) {
		return Files.isSymbolicLink(path);
	}

	/**
	 * 判断是否为其它类型文件，即非文件、非目录、非链接。
	 *
	 * @param path 被检查的文件（非空）
	 * @return 是否为其它类型文件
	 * @since 6.0.0
	 */
	public static boolean isOther(final Path path) {
		return getAttributes(path, false).isOther();
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param path          文件，{@code null}返回{@code false}
	 * @param isFollowLinks 是否跟踪软链（快捷方式）
	 * @return 是否存在
	 * @since 5.5.3
	 */
	public static boolean exists(final Path path, final boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		return Files.exists(path, getLinkOptions(isFollowLinks));
	}

	/**
	 * 判断给定的目录是否为给定文件或文件夹的子目录
	 *
	 * @param parent 父目录
	 * @param sub    子目录
	 * @return 子目录是否为父目录的子目录
	 * @since 5.5.5
	 */
	public static boolean isSub(final Path parent, final Path sub) {
		return toAbsNormal(sub).startsWith(toAbsNormal(parent));
	}

	/**
	 * 将Path路径转换为标准的绝对路径<br>
	 * 如果{@link Path#isAbsolute()}为{@code true}，表示已经是绝对路径，返回本身<br>
	 * 如果是相对路径，则返回相对于系统默认目录的路径（一般为项目路径）
	 *
	 * @param path 文件或目录Path
	 * @return 转换后的Path
	 * @since 5.5.5
	 */
	public static Path toAbsNormal(final Path path) {
		if (null == path) {
			return null;
		}
		return path.toAbsolutePath().normalize();
	}

	/**
	 * 获取实际路径，路径文件必须存在<br>
	 * 如果给定Path是软链接（符号链接），则返回指向的实际链接<br>
	 * 如果路径不存在，会直接抛出NoSuchFileException异常<br>
	 * 无论给定是何种类型的路径，返回都是唯一的路径（总是equals）
	 *
	 * @param path 路径
	 * @return 实际路径
	 * @throws IORuntimeException IO异常，如文件不存在等
	 */
	public static Path toRealPath(Path path) throws IORuntimeException {
		if (null != path) {
			try {
				path = path.toRealPath();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return path;
	}

	/**
	 * 获得文件的MimeType
	 *
	 * @param file 文件
	 * @return MimeType
	 * @see Files#probeContentType(Path)
	 * @since 5.5.5
	 */
	public static String getMimeType(final Path file) {
		try {
			return Files.probeContentType(file);
		} catch (final IOException ignore) {
			// issue#3179，使用OpenJDK可能抛出NoSuchFileException，此处返回null
			return null;
		}
	}

	/**
	 * 创建所给目录及其父目录
	 *
	 * @param dir 目录
	 * @return 目录
	 * @since 5.5.7
	 */
	public static Path mkdir(final Path dir) {
		if (null != dir && !exists(dir, false)) {
			try {
				Files.createDirectories(dir);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return dir;
	}

	/**
	 * 创建所给文件或目录的父目录
	 *
	 * @param path 文件或目录
	 * @return 父目录
	 * @since 5.5.7
	 */
	public static Path mkParentDirs(final Path path) {
		return mkdir(path.getParent());
	}

	/**
	 * 获取{@link Path}文件名
	 *
	 * @param path {@link Path}
	 * @return 文件名
	 * @since 5.7.15
	 */
	public static String getName(final Path path) {
		if (null == path) {
			return null;
		}
		return path.getFileName().toString();
	}

	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Random].suffix From com.jodd.io.FileUtil
	 *
	 * @param prefix 前缀，至少3个字符
	 * @param suffix 后缀，如果null则使用默认.tmp
	 * @param dir    临时文件创建的所在目录
	 * @return 临时文件
	 * @throws IORuntimeException IO异常
	 * @since 6.0.0
	 */
	public static Path createTempFile(final String prefix, final String suffix, final Path dir) throws IORuntimeException {
		int exceptionsCount = 0;
		while (true) {
			try {
				if (null == dir) {
					return Files.createTempFile(prefix, suffix);
				} else {
					return Files.createTempFile(mkdir(dir), prefix, suffix);
				}
			} catch (final IOException ioex) {
				// fixes java.io.WinNTFileSystem.createFileExclusively access denied
				if (++exceptionsCount >= 50) {
					throw new IORuntimeException(ioex);
				}
			}
		}
	}

	/**
	 * 构建是否追踪软链的选项
	 *
	 * @param isFollowLinks 是否追踪软链
	 * @return 选项
	 * @since 5.8.23
	 */
	public static LinkOption[] getLinkOptions(final boolean isFollowLinks) {
		return isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
	}

	/**
	 * 构建是否追踪软链的选项
	 *
	 * @param isFollowLinks 是否追踪软链
	 * @return 选项
	 * @since 5.8.23
	 */
	public static Set<FileVisitOption> getFileVisitOption(final boolean isFollowLinks) {
		return isFollowLinks ? EnumSet.of(FileVisitOption.FOLLOW_LINKS) :
			EnumSet.noneOf(FileVisitOption.class);
	}
}
