package cn.hutool.core.io.file;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.visitor.CopyVisitor;
import cn.hutool.core.io.file.visitor.DelVisitor;
import cn.hutool.core.io.file.visitor.MoveVisitor;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * NIO中Path对象操作封装
 *
 * @author looly
 * @since 5.4.1
 */
public class PathUtil {
	/**
	 * 目录是否为空
	 *
	 * @param dirPath 目录
	 * @return 是否为空
	 * @throws IORuntimeException IOException
	 */
	public static boolean isDirEmpty(Path dirPath) {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
			return false == dirStream.iterator().hasNext();
		} catch (IOException e) {
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
	public static List<File> loopFiles(Path path, FileFilter fileFilter) {
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
	public static List<File> loopFiles(Path path, int maxDepth, FileFilter fileFilter) {
		final List<File> fileList = new ArrayList<>();

		if (null == path || false == Files.exists(path)) {
			return fileList;
		} else if (false == isDirectory(path)) {
			final File file = path.toFile();
			if (null == fileFilter || fileFilter.accept(file)) {
				fileList.add(file);
			}
			return fileList;
		}

		walkFiles(path, maxDepth, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				final File file = path.toFile();
				if (null == fileFilter || fileFilter.accept(file)) {
					fileList.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});

		return fileList;
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start   起始路径，必须为目录
	 * @param visitor {@link FileVisitor} 接口，用于自定义在访问文件时，访问目录前后等节点做的操作
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 * @since 5.5.2
	 */
	public static void walkFiles(Path start, FileVisitor<? super Path> visitor) {
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
	public static void walkFiles(Path start, int maxDepth, FileVisitor<? super Path> visitor) {
		if (maxDepth < 0) {
			// < 0 表示遍历到最底层
			maxDepth = Integer.MAX_VALUE;
		}

		try {
			Files.walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), maxDepth, visitor);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 删除文件或者文件夹，不追踪软链<br>
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 *
	 * @param path 文件对象
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 * @since 4.4.2
	 */
	public static boolean del(Path path) throws IORuntimeException {
		if (Files.notExists(path)) {
			return true;
		}

		try {
			if (isDirectory(path)) {
				Files.walkFileTree(path, DelVisitor.INSTANCE);
			} else {
				delFile(path);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return true;
	}

	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件<br>
	 * 此方法不支持递归拷贝目录，如果src传入是目录，只会在目标目录中创建空目录
	 *
	 * @param src     源文件路径，如果为目录只在目标中创建新目录
	 * @param dest    目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 */
	public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IORuntimeException {
		return copyFile(src, dest, (CopyOption[]) options);
	}

	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件<br>
	 * 此方法不支持递归拷贝目录，如果src传入是目录，只会在目标目录中创建空目录
	 *
	 * @param src     源文件路径，如果为目录只在目标中创建新目录
	 * @param target  目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 * @since 5.4.1
	 */
	public static Path copyFile(Path src, Path target, CopyOption... options) throws IORuntimeException {
		Assert.notNull(src, "Source File is null !");
		Assert.notNull(target, "Destination File or directory is null !");

		final Path targetPath = isDirectory(target) ? target.resolve(src.getFileName()) : target;
		// 创建级联父目录
		mkParentDirs(targetPath);
		try {
			return Files.copy(src, targetPath, options);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 拷贝文件或目录，拷贝规则为：
	 *
	 * <ul>
	 *     <li>源文件为目录，目标也为目录或不存在，则拷贝整个目录到目标目录下</li>
	 *     <li>源文件为文件，目标为目录或不存在，则拷贝文件到目标目录下</li>
	 *     <li>源文件为文件，目标也为文件，则在{@link StandardCopyOption#REPLACE_EXISTING}情况下覆盖之</li>
	 * </ul>
	 *
	 * @param src     源文件路径，如果为目录会在目标中创建新目录
	 * @param target  目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 * @since 5.5.1
	 */
	public static Path copy(Path src, Path target, CopyOption... options) throws IORuntimeException {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");

		if (isDirectory(src)) {
			return copyContent(src, target.resolve(src.getFileName()), options);
		}
		return copyFile(src, target, options);
	}

	/**
	 * 拷贝目录下的所有文件或目录到目标目录中，此方法不支持文件对文件的拷贝。
	 * <ul>
	 *     <li>源文件为目录，目标也为目录或不存在，则拷贝目录下所有文件和目录到目标目录下</li>
	 *     <li>源文件为文件，目标为目录或不存在，则拷贝文件到目标目录下</li>
	 * </ul>
	 *
	 * @param src     源文件路径，如果为目录只在目标中创建新目录
	 * @param target  目标目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 * @since 5.5.1
	 */
	public static Path copyContent(Path src, Path target, CopyOption... options) throws IORuntimeException {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");

		try {
			Files.walkFileTree(src, new CopyVisitor(src, target, options));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return target;
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false<br>
	 * 此方法不会追踪到软链对应的真实地址，即软链被当作文件
	 *
	 * @param path {@link Path}
	 * @return 如果为目录true
	 * @since 5.5.1
	 */
	public static boolean isDirectory(Path path) {
		return isDirectory(path, false);
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false
	 *
	 * @param path          {@link Path}
	 * @param isFollowLinks 是否追踪到软链对应的真实地址
	 * @return 如果为目录true
	 * @since 3.1.0
	 */
	public static boolean isDirectory(Path path, boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		return Files.isDirectory(path, options);
	}

	/**
	 * 获取指定位置的子路径部分，支持负数，例如index为-1表示从后数第一个节点位置
	 *
	 * @param path  路径
	 * @param index 路径节点位置，支持负数（负数从后向前计数）
	 * @return 获取的子路径
	 * @since 3.1.2
	 */
	public static Path getPathEle(Path path, int index) {
		return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
	}

	/**
	 * 获取指定位置的最后一个子路径部分
	 *
	 * @param path 路径
	 * @return 获取的最后一个子路径
	 * @since 3.1.2
	 */
	public static Path getLastPathEle(Path path) {
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
	public static Path subPath(Path path, int fromIndex, int toIndex) {
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
			int tmp = fromIndex;
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
	public static BasicFileAttributes getAttributes(Path path, boolean isFollowLinks) throws IORuntimeException {
		if (null == path) {
			return null;
		}

		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		try {
			return Files.readAttributes(path, BasicFileAttributes.class, options);
		} catch (IOException e) {
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
	public static BufferedInputStream getInputStream(Path path) throws IORuntimeException {
		final InputStream in;
		try {
			in = Files.newInputStream(path);
		} catch (IOException e) {
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
	public static BufferedReader getUtf8Reader(Path path) throws IORuntimeException {
		return getReader(path, CharsetUtil.CHARSET_UTF_8);
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
	public static BufferedReader getReader(Path path, Charset charset) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(path), charset);
	}

	/**
	 * 读取文件的所有内容为byte数组
	 *
	 * @param path 文件
	 * @return byte数组
	 * @since 5.5.4
	 */
	public static byte[] readBytes(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
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
	public static BufferedOutputStream getOutputStream(Path path) throws IORuntimeException {
		final OutputStream in;
		try {
			in = Files.newOutputStream(path);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return IoUtil.toBuffered(in);
	}

	/**
	 * 修改文件或目录的文件名，不变更路径，只是简单修改文件名<br>
	 *
	 * <pre>
	 * FileUtil.rename(file, "aaa.jpg", false) xx/xx.png =》xx/aaa.jpg
	 * </pre>
	 *
	 * @param path       被修改的文件
	 * @param newName    新的文件名，包括扩展名
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 * @since 5.4.1
	 */
	public static Path rename(Path path, String newName, boolean isOverride) {
		return move(path, path.resolveSibling(newName), isOverride);
	}

	/**
	 * 移动文件或目录<br>
	 * 当目标是目录时，会将源文件或文件夹整体移动至目标目录下<br>
	 * 例如：
	 * <ul>
	 *     <li>move("/usr/aaa/abc.txt", "/usr/bbb")结果为："/usr/bbb/abc.txt"</li>
	 *     <li>move("/usr/aaa", "/usr/bbb")结果为："/usr/bbb/aaa"</li>
	 * </ul>
	 *
	 * @param src        源文件或目录路径
	 * @param target     目标路径，如果为目录，则移动到此目录下
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 * @since 5.5.1
	 */
	public static Path move(Path src, Path target, boolean isOverride) {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");

		if (isDirectory(target)) {
			target = target.resolve(src.getFileName());
		}
		return moveContent(src, target, isOverride);
	}

	/**
	 * 移动文件或目录内容到目标目录中，例如：
	 * <ul>
	 *     <li>moveContent("/usr/aaa/abc.txt", "/usr/bbb")结果为："/usr/bbb/abc.txt"</li>
	 *     <li>moveContent("/usr/aaa", "/usr/bbb")结果为："/usr/bbb"</li>
	 * </ul>
	 *
	 * @param src        源文件或目录路径
	 * @param target     目标路径，如果为目录，则移动到此目录下
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件Path
	 * @since 5.7.9
	 */
	public static Path moveContent(Path src, Path target, boolean isOverride) {
		Assert.notNull(src, "Src path must be not null !");
		Assert.notNull(target, "Target path must be not null !");
		final CopyOption[] options = isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{};

		// 自动创建目标的父目录
		mkParentDirs(target);
		try {
			return Files.move(src, target, options);
		} catch (IOException e) {
			if(e instanceof FileAlreadyExistsException){
				// 目标文件已存在，直接抛出异常
				// issue#I4QV0L@Gitee
				throw new IORuntimeException(e);
			}
			// 移动失败，可能是跨分区移动导致的，采用递归移动方式
			try {
				Files.walkFileTree(src, new MoveVisitor(src, target, options));
				// 移动后空目录没有删除，
				del(src);
			} catch (IOException e2) {
				throw new IORuntimeException(e2);
			}
			return target;
		}
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
	public static boolean equals(Path file1, Path file2) throws IORuntimeException {
		try {
			return Files.isSameFile(file1, file2);
		} catch (IOException e) {
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
	public static boolean isFile(Path path, boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		return Files.isRegularFile(path, options);
	}

	/**
	 * 判断是否为符号链接文件
	 *
	 * @param path 被检查的文件
	 * @return 是否为符号链接文件
	 * @since 4.4.2
	 */
	public static boolean isSymlink(Path path) {
		return Files.isSymbolicLink(path);
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param path          文件
	 * @param isFollowLinks 是否跟踪软链（快捷方式）
	 * @return 是否存在
	 * @since 5.5.3
	 */
	public static boolean exists(Path path, boolean isFollowLinks) {
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
		return Files.exists(path, options);
	}

	/**
	 * 判断给定的目录是否为给定文件或文件夹的子目录
	 *
	 * @param parent 父目录
	 * @param sub    子目录
	 * @return 子目录是否为父目录的子目录
	 * @since 5.5.5
	 */
	public static boolean isSub(Path parent, Path sub) {
		return toAbsNormal(sub).startsWith(toAbsNormal(parent));
	}

	/**
	 * 将Path路径转换为标准的绝对路径
	 *
	 * @param path 文件或目录Path
	 * @return 转换后的Path
	 * @since 5.5.5
	 */
	public static Path toAbsNormal(Path path) {
		Assert.notNull(path);
		return path.toAbsolutePath().normalize();
	}

	/**
	 * 获得文件的MimeType
	 *
	 * @param file 文件
	 * @return MimeType
	 * @see Files#probeContentType(Path)
	 * @since 5.5.5
	 */
	public static String getMimeType(Path file) {
		try {
			return Files.probeContentType(file);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建所给目录及其父目录
	 *
	 * @param dir 目录
	 * @return 目录
	 * @since 5.5.7
	 */
	public static Path mkdir(Path dir) {
		if (null != dir && false == exists(dir, false)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
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
	public static Path mkParentDirs(Path path) {
		return mkdir(path.getParent());
	}

	/**
	 * 获取{@link Path}文件名
	 *
	 * @param path {@link Path}
	 * @return 文件名
	 * @since 5.7.15
	 */
	public static String getName(Path path) {
		if (null == path) {
			return null;
		}
		return path.getFileName().toString();
	}

	/**
	 * 删除文件或空目录，不追踪软链
	 *
	 * @param path 文件对象
	 * @throws IOException IO异常
	 * @since 5.7.7
	 */
	protected static void delFile(Path path) throws IOException {
		try {
			Files.delete(path);
		} catch (AccessDeniedException e) {
			// 可能遇到只读文件，无法删除.使用 file 方法删除
			if (false == path.toFile().delete()) {
				throw e;
			}
		}
	}
}
