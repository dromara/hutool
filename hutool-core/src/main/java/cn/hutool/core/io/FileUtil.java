package cn.hutool.core.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileCopier;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileReader.ReaderHandler;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.file.LineSeparator;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

/**
 * 文件工具类
 * 
 * @author xiaoleilu
 *
 */
public class FileUtil {

	/** 类Unix路径分隔符 */
	private static final char UNIX_SEPARATOR = CharUtil.SLASH;
	/** Windows路径分隔符 */
	private static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;
	/** Windows下文件名中的无效字符 */
	private static Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");

	/** Class文件扩展名 */
	public static final String CLASS_EXT = ".class";
	/** Jar文件扩展名 */
	public static final String JAR_FILE_EXT = ".jar";
	/** 在Jar中的路径jar的扩展名形式 */
	public static final String JAR_PATH_EXT = ".jar!";
	/** 当Path为文件形式时, path会加入一个表示文件的前缀 */
	public static final String PATH_FILE_PRE = URLUtil.FILE_URL_PREFIX;

	/**
	 * 是否为Windows环境
	 * 
	 * @return 是否为Windows环境
	 * @since 3.0.9
	 */
	public static boolean isWindows() {
		return WINDOWS_SEPARATOR == File.separatorChar;
	}

	/**
	 * 列出目录文件<br>
	 * 给定的绝对路径不能是压缩包中的路径
	 * 
	 * @param path 目录绝对路径或者相对路径
	 * @return 文件列表（包含目录）
	 */
	public static File[] ls(String path) {
		if (path == null) {
			return null;
		}

		path = getAbsolutePath(path);

		File file = file(path);
		if (file.isDirectory()) {
			return file.listFiles();
		}
		throw new IORuntimeException(StrUtil.format("Path [{}] is not directory!", path));
	}

	/**
	 * 文件是否为空<br>
	 * 目录：里面没有文件时为空 文件：文件大小为0时为空
	 * 
	 * @param file 文件
	 * @return 是否为空，当提供非目录时，返回false
	 */
	public static boolean isEmpty(File file) {
		if (null == file) {
			return true;
		}

		if (file.isDirectory()) {
			String[] subFiles = file.list();
			if (ArrayUtil.isEmpty(subFiles)) {
				return true;
			}
		} else if (file.isFile()) {
			return file.length() <= 0;
		}

		return false;
	}

	/**
	 * 目录是否为空
	 * 
	 * @param file 目录
	 * @return 是否为空，当提供非目录时，返回false
	 */
	public static boolean isNotEmpty(File file) {
		return false == isEmpty(file);
	}

	/**
	 * 目录是否为空
	 * 
	 * @param dirPath 目录
	 * @return 是否为空
	 * @exception IORuntimeException IOException
	 */
	public static boolean isDirEmpty(Path dirPath) {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath)) {
			return false == dirStream.iterator().hasNext();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 目录是否为空
	 * 
	 * @param dir 目录
	 * @return 是否为空
	 */
	public static boolean isDirEmpty(File dir) {
		return isDirEmpty(dir.toPath());
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供file为文件，直接返回过滤结果
	 * 
	 * @param path 当前遍历文件或目录的路径
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
	 * @return 文件列表
	 * @since 3.2.0
	 */
	public static List<File> loopFiles(String path, FileFilter fileFilter) {
		return loopFiles(file(path), fileFilter);
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供file为文件，直接返回过滤结果
	 * 
	 * @param file 当前遍历文件或目录
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
	 * @return 文件列表
	 */
	public static List<File> loopFiles(File file, FileFilter fileFilter) {
		List<File> fileList = new ArrayList<File>();
		if (null == file) {
			return fileList;
		} else if (false == file.exists()) {
			return fileList;
		}

		if (file.isDirectory()) {
			final File[] subFiles = file.listFiles();
			if (ArrayUtil.isNotEmpty(subFiles)) {
				for (File tmp : subFiles) {
					fileList.addAll(loopFiles(tmp, fileFilter));
				}
			}
		} else {
			if (null == fileFilter || fileFilter.accept(file)) {
				fileList.add(file);
			}
		}

		return fileList;
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param path 当前遍历文件或目录的路径
	 * @return 文件列表
	 * @since 3.2.0
	 */
	public static List<File> loopFiles(String path) {
		return loopFiles(file(path));
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param file 当前遍历文件
	 * @return 文件列表
	 */
	public static List<File> loopFiles(File file) {
		return loopFiles(file, null);
	}

	/**
	 * 获得指定目录下所有文件<br>
	 * 不会扫描子目录
	 * 
	 * @param path 相对ClassPath的目录或者绝对路径目录
	 * @return 文件路径列表（如果是jar中的文件，则给定类似.jar!/xxx/xxx的路径）
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> listFileNames(String path) throws IORuntimeException {
		if (path == null) {
			return null;
		}
		List<String> paths = new ArrayList<String>();

		int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
		if (index == -1) {
			// 普通目录路径
			File[] files = ls(path);
			for (File file : files) {
				if (file.isFile()) {
					paths.add(file.getName());
				}
			}
		} else {
			// jar文件
			path = getAbsolutePath(path);
			if (false == path.endsWith(String.valueOf(UNIX_SEPARATOR))) {
				path = path + UNIX_SEPARATOR;
			}
			// jar文件中的路径
			index = index + FileUtil.JAR_FILE_EXT.length();
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(path.substring(0, index));
				final String subPath = path.substring(index + 2);
				for (JarEntry entry : Collections.list(jarFile.entries())) {
					final String name = entry.getName();
					if (name.startsWith(subPath)) {
						final String nameSuffix = StrUtil.removePrefix(name, subPath);
						if (nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
							paths.add(nameSuffix);
						}
					}
				}
			} catch (IOException e) {
				throw new IORuntimeException(StrUtil.format("Can not read file path of [{}]", path), e);
			} finally {
				IoUtil.close(jarFile);
			}
		}
		return paths;
	}
	
	/**
	 * 创建File对象，相当于调用new File()，不做任何处理
	 * 
	 * @param path 文件路径
	 * @return File
	 * @since 4.1.4
	 */
	public static File newFile(String path) {
		return new File(path);
	}

	/**
	 * 创建File对象，自动识别相对或绝对路径，相对路径将自动从ClassPath下寻找
	 * 
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(String path) {
		if (StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(getAbsolutePath(path));
	}

	/**
	 * 创建File对象
	 * 
	 * @param parent 父目录
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(String parent, String path) {
		if (StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}

	/**
	 * 创建File对象
	 * 
	 * @param parent 父文件对象
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(File parent, String path) {
		if (StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}

	/**
	 * 通过多层目录参数创建文件
	 * 
	 * @param directory 父目录
	 * @param names 元素名（多层目录名）
	 * @return the file 文件
	 * @since 4.0.6
	 */
	public static File file(File directory, String... names) {
		Assert.notNull(directory, "directorydirectory must not be null");
		if (ArrayUtil.isEmpty(names)) {
			return directory;
		}

		File file = directory;
		for (String name : names) {
			if (null != name) {
				file = new File(file, name);
			}
		}
		return file;
	}

	/**
	 * 通过多层目录创建文件
	 * 
	 * 元素名（多层目录名）
	 * 
	 * @return the file 文件
	 * @since 4.0.6
	 */
	public static File file(String... names) {
		if (ArrayUtil.isEmpty(names)) {
			return null;
		}

		File file = null;
		for (String name : names) {
			if (file == null) {
				file = new File(name);
			} else {
				file = new File(file, name);
			}
		}
		return file;
	}

	/**
	 * 创建File对象
	 * 
	 * @param uri 文件URI
	 * @return File
	 */
	public static File file(URI uri) {
		if (uri == null) {
			throw new NullPointerException("File uri is null!");
		}
		return new File(uri);
	}

	/**
	 * 创建File对象
	 * 
	 * @param url 文件URL
	 * @return File
	 */
	public static File file(URL url) {
		return new File(URLUtil.toURI(url));
	}

	/**
	 * 获取临时文件路径（绝对路径）
	 * 
	 * @return 临时文件路径
	 * @since 4.0.6
	 */
	public static String getTmpDirPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 获取临时文件目录
	 * 
	 * @return 临时文件目录
	 * @since 4.0.6
	 */
	public static File getTmpDir() {
		return file(getTmpDirPath());
	}

	/**
	 * 获取用户路径（绝对路径）
	 * 
	 * @return 用户路径
	 * @since 4.0.6
	 */
	public static String getUserHomePath() {
		return System.getProperty("user.home");
	}

	/**
	 * 获取用户目录
	 * 
	 * @return 用户目录
	 * @since 4.0.6
	 */
	public static File getUserHomeDir() {
		return file(getUserHomePath());
	}

	/**
	 * 判断文件是否存在，如果path为null，则返回false
	 * 
	 * @param path 文件路径
	 * @return 如果存在返回true
	 */
	public static boolean exist(String path) {
		return (path == null) ? false : file(path).exists();
	}

	/**
	 * 判断文件是否存在，如果file为null，则返回false
	 * 
	 * @param file 文件
	 * @return 如果存在返回true
	 */
	public static boolean exist(File file) {
		return (file == null) ? false : file.exists();
	}
	
	/**
	 * 是否存在匹配文件
	 * 
	 * @param directory 文件夹路径
	 * @param regexp 文件夹中所包含文件名的正则表达式
	 * @return 如果存在匹配文件返回true
	 */
	public static boolean exist(String directory, String regexp) {
		final File file = new File(directory);
		if (false == file.exists()) {
			return false;
		}

		final String[] fileList = file.list();
		if (fileList == null) {
			return false;
		}

		for (String fileName : fileList) {
			if (fileName.matches(regexp)) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 指定文件最后修改时间
	 * 
	 * @param file 文件
	 * @return 最后修改时间
	 */
	public static Date lastModifiedTime(File file) {
		if (!exist(file)) {
			return null;
		}

		return new Date(file.lastModified());
	}

	/**
	 * 指定路径文件最后修改时间
	 * 
	 * @param path 绝对路径
	 * @return 最后修改时间
	 */
	public static Date lastModifiedTime(String path) {
		return lastModifiedTime(new File(path));
	}

	/**
	 * 计算目录或文件的总大小<br>
	 * 当给定对象为文件时，直接调用 {@link File#length()}<br>
	 * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回
	 * 
	 * @param file 目录或文件
	 * @return 总大小，bytes长度
	 */
	public static long size(File file) {
		Assert.notNull(file, "file argument is null !");
		if (false == file.exists()) {
			throw new IllegalArgumentException(StrUtil.format("File [{}] not exist !", file.getAbsolutePath()));
		}

		if (file.isDirectory()) {
			long size = 0L;
			File[] subFiles = file.listFiles();
			if (ArrayUtil.isEmpty(subFiles)) {
				return 0L;// empty directory
			}
			for (int i = 0; i < subFiles.length; i++) {
				size += size(subFiles[i]);
			}
			return size;
		} else {
			return file.length();
		}
	}

	/**
	 * 给定文件或目录的最后修改时间是否晚于给定时间
	 * 
	 * @param file 文件或目录
	 * @param reference 参照文件
	 * @return 是否晚于给定时间
	 */
	public static boolean newerThan(File file, File reference) {
		if (null == file || false == reference.exists()) {
			return true;// 文件一定比一个不存在的文件新
		}
		return newerThan(file, reference.lastModified());
	}

	/**
	 * 给定文件或目录的最后修改时间是否晚于给定时间
	 * 
	 * @param file 文件或目录
	 * @param timeMillis 做为对比的时间
	 * @return 是否晚于给定时间
	 */
	public static boolean newerThan(File file, long timeMillis) {
		if (null == file || false == file.exists()) {
			return false;// 不存在的文件一定比任何时间旧
		}
		return file.lastModified() > timeMillis;
	}

	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param fullFilePath 文件的全路径，使用POSIX风格
	 * @return 文件，若路径为null，返回null
	 * @throws IORuntimeException IO异常
	 */
	public static File touch(String fullFilePath) throws IORuntimeException {
		if (fullFilePath == null) {
			return null;
		}
		return touch(file(fullFilePath));
	}

	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param file 文件对象
	 * @return 文件，若路径为null，返回null
	 * @throws IORuntimeException IO异常
	 */
	public static File touch(File file) throws IORuntimeException {
		if (null == file) {
			return null;
		}
		if (false == file.exists()) {
			mkParentDirs(file);
			try {
				file.createNewFile();
			} catch (Exception e) {
				throw new IORuntimeException(e);
			}
		}
		return file;
	}

	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param parent 父文件对象
	 * @param path 文件路径
	 * @return File
	 * @throws IORuntimeException IO异常
	 */
	public static File touch(File parent, String path) throws IORuntimeException {
		return touch(file(parent, path));
	}

	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param parent 父文件对象
	 * @param path 文件路径
	 * @return File
	 * @throws IORuntimeException IO异常
	 */
	public static File touch(String parent, String path) throws IORuntimeException {
		return touch(file(parent, path));
	}

	/**
	 * 创建所给文件或目录的父目录
	 * 
	 * @param file 文件或目录
	 * @return 父目录
	 */
	public static File mkParentDirs(File file) {
		final File parentFile = file.getParentFile();
		if (null != parentFile && false == parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFile;
	}

	/**
	 * 创建父文件夹，如果存在直接返回此文件夹
	 * 
	 * @param path 文件夹路径，使用POSIX格式，无论哪个平台
	 * @return 创建的目录
	 */
	public static File mkParentDirs(String path) {
		if (path == null) {
			return null;
		}
		return mkParentDirs(file(path));
	}

	/**
	 * 删除文件或者文件夹<br>
	 * 路径如果为相对路径，会转换为ClassPath路径！ 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param fullFileOrDirPath 文件或者目录的路径
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean del(String fullFileOrDirPath) throws IORuntimeException {
		return del(file(fullFileOrDirPath));
	}

	/**
	 * 删除文件或者文件夹<br>
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param file 文件对象
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 */
	public static boolean del(File file) throws IORuntimeException {
		if (file == null || false == file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			clean(file);
		}
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return true;
	}

	/**
	 * 清空文件夹<br>
	 * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param dirPath 文件夹路径
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 * @since 4.0.8
	 */
	public static boolean clean(String dirPath) throws IORuntimeException {
		return clean(file(dirPath));
	}

	/**
	 * 清空文件夹<br>
	 * 注意：清空文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param directory 文件夹
	 * @return 成功与否
	 * @throws IORuntimeException IO异常
	 * @since 3.0.6
	 */
	public static boolean clean(File directory) throws IORuntimeException {
		if (directory == null || directory.exists() == false || false == directory.isDirectory()) {
			return true;
		}

		final File[] files = directory.listFiles();
		for (File childFile : files) {
			boolean isOk = del(childFile);
			if (isOk == false) {
				// 删除一个出错则本次删除任务失败
				return false;
			}
		}
		return true;
	}

	/**
	 * 创建文件夹，如果存在直接返回此文件夹<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
	 * @return 创建的目录
	 */
	public static File mkdir(String dirPath) {
		if (dirPath == null) {
			return null;
		}
		final File dir = file(dirPath);
		return mkdir(dir);
	}

	/**
	 * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param dir 目录
	 * @return 创建的目录
	 */
	public static File mkdir(File dir) {
		if (dir == null) {
			return null;
		}
		if (false == dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].tmp
	 * 
	 * @param dir 临时文件创建的所在目录
	 * @return 临时文件
	 * @throws IORuntimeException IO异常
	 */
	public static File createTempFile(File dir) throws IORuntimeException {
		return createTempFile("hutool", null, dir, true);
	}

	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].tmp
	 * 
	 * @param dir 临时文件创建的所在目录
	 * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
	 * @return 临时文件
	 * @throws IORuntimeException IO异常
	 */
	public static File createTempFile(File dir, boolean isReCreat) throws IORuntimeException {
		return createTempFile("hutool", null, dir, isReCreat);
	}

	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].suffix From com.jodd.io.FileUtil
	 * 
	 * @param prefix 前缀，至少3个字符
	 * @param suffix 后缀，如果null则使用默认.tmp
	 * @param dir 临时文件创建的所在目录
	 * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
	 * @return 临时文件
	 * @throws IORuntimeException IO异常
	 */
	public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IORuntimeException {
		int exceptionsCount = 0;
		while (true) {
			try {
				File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
				if (isReCreat) {
					file.delete();
					file.createNewFile();
				}
				return file;
			} catch (IOException ioex) { // fixes java.io.WinNTFileSystem.createFileExclusively access denied
				if (++exceptionsCount >= 50) {
					throw new IORuntimeException(ioex);
				}
			}
		}
	}

	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * 
	 * @param src 源文件路径
	 * @param dest 目标文件或目录路径，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return File
	 * @throws IORuntimeException IO异常
	 */
	public static File copyFile(String src, String dest, StandardCopyOption... options) throws IORuntimeException {
		Assert.notBlank(src, "Source File path is blank !");
		Assert.notNull(src, "Destination File path is null !");
		return copyFile(Paths.get(src), Paths.get(dest), options).toFile();
	}

	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * 
	 * @param src 源文件
	 * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File copyFile(File src, File dest, StandardCopyOption... options) throws IORuntimeException {
		// check
		Assert.notNull(src, "Source File is null !");
		if (false == src.exists()) {
			throw new IORuntimeException("File not exist: " + src);
		}
		Assert.notNull(dest, "Destination File or directiory is null !");
		if (equals(src, dest)) {
			throw new IORuntimeException("Files '{}' and '{}' are equal", src, dest);
		}
		return copyFile(src.toPath(), dest.toPath(), options).toFile();
	}

	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * 
	 * @param src 源文件路径
	 * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IORuntimeException IO异常
	 */
	public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IORuntimeException {
		Assert.notNull(src, "Source File is null !");
		Assert.notNull(dest, "Destination File or directiory is null !");

		Path destPath = dest.toFile().isDirectory() ? dest.resolve(src.getFileName()) : dest;
		try {
			return Files.copy(src, destPath, options);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 复制文件或目录<br>
	 * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
	 * 
	 * @param srcPath 源文件或目录
	 * @param destPath 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IORuntimeException IO异常
	 */
	public static File copy(String srcPath, String destPath, boolean isOverride) throws IORuntimeException {
		return copy(file(srcPath), file(destPath), isOverride);
	}

	/**
	 * 复制文件或目录<br>
	 * 情况如下：
	 * 
	 * <pre>
	 * 1、src和dest都为目录，则将src目录及其目录下所有文件目录拷贝到dest下
	 * 2、src和dest都为文件，直接复制，名字为dest
	 * 3、src为文件，dest为目录，将src拷贝到dest目录下
	 * </pre>
	 * 
	 * @param src 源文件
	 * @param dest 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IORuntimeException IO异常
	 */
	public static File copy(File src, File dest, boolean isOverride) throws IORuntimeException {
		return FileCopier.create(src, dest).setOverride(isOverride).copy();
	}

	/**
	 * 复制文件或目录<br>
	 * 情况如下：
	 * 
	 * <pre>
	 * 1、src和dest都为目录，则讲src下所有文件目录拷贝到dest下
	 * 2、src和dest都为文件，直接复制，名字为dest
	 * 3、src为文件，dest为目录，将src拷贝到dest目录下
	 * </pre>
	 * 
	 * @param src 源文件
	 * @param dest 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IORuntimeException IO异常
	 */
	public static File copyContent(File src, File dest, boolean isOverride) throws IORuntimeException{
		return FileCopier.create(src, dest).setCopyContentIfDir(true).setOverride(isOverride).copy();
	}
	
	/**
	 * 复制文件或目录<br>
	 * 情况如下：
	 * 
	 * <pre>
	 * 1、src和dest都为目录，则讲src下所有文件（包括子目录）拷贝到dest下
	 * 2、src和dest都为文件，直接复制，名字为dest
	 * 3、src为文件，dest为目录，将src拷贝到dest目录下
	 * </pre>
	 * 
	 * @param src 源文件
	 * @param dest 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IORuntimeException IO异常
	 * @since 4.1.5
	 */
	public static File copyFilesFromDir(File src, File dest, boolean isOverride) throws IORuntimeException{
		return FileCopier.create(src, dest).setCopyContentIfDir(true).setOnlyCopyFile(true).setOverride(isOverride).copy();
	}

	/**
	 * 移动文件或者目录
	 * 
	 * @param src 源文件或者目录
	 * @param dest 目标文件或者目录
	 * @param isOverride 是否覆盖目标，只有目标为文件才覆盖
	 * @throws IORuntimeException IO异常
	 */
	public static void move(File src, File dest, boolean isOverride) throws IORuntimeException {
		// check
		if (false == src.exists()) {
			throw new IORuntimeException("File not found: " + src);
		}

		// 来源为文件夹，目标为文件
		if (src.isDirectory() && dest.isFile()) {
			throw new IORuntimeException(StrUtil.format("Can not move directory [{}] to file [{}]", src, dest));
		}

		if (isOverride && dest.isFile()) {// 只有目标为文件的情况下覆盖之
			dest.delete();
		}

		// 来源为文件，目标为文件夹
		if (src.isFile() && dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}

		if (false == src.renameTo(dest)) {
			// 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
			try {
				copy(src, dest, isOverride);
				src.delete();
			} catch (Exception e) {
				throw new IORuntimeException(StrUtil.format("Move [{}] to [{}] failed!", src, dest), e);
			}

		}
	}

	/**
	 * 修改文件或目录的文件名，不变更路径，只是简单修改文件名<br>
	 * 重命名有两种模式：<br>
	 * 1、isRetainExt为true时，保留原扩展名：
	 * 
	 * <pre>
	 * FileUtil.rename(file, "aaa", true) xx/xx.png =》xx/aaa.png
	 * </pre>
	 * 
	 * 2、isRetainExt为false时，不保留原扩展名，需要在newName中
	 * 
	 * <pre>
	 * FileUtil.rename(file, "aaa.jpg", false) xx/xx.png =》xx/aaa.jpg
	 * </pre>
	 * 
	 * @param file 被修改的文件
	 * @param newName 新的文件名，包括扩展名
	 * @param isRetainExt 是否保留原文件的扩展名，如果保留，则newName不需要加扩展名
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标文件
	 * @since 3.0.9
	 */
	public static File rename(File file, String newName, boolean isRetainExt, boolean isOverride) {
		if (isRetainExt) {
			newName = newName.concat(".").concat(FileUtil.extName(file));
		}
		final Path path = file.toPath();
		final CopyOption[] options = isOverride ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING } : new CopyOption[] {};
		try {
			return Files.move(path, path.resolveSibling(newName), options).toFile();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 获取规范的绝对路径
	 * 
	 * @param file 文件
	 * @return 规范绝对路径，如果传入file为null，返回null
	 * @since 4.1.4
	 */
	public static String getCanonicalPath(File file) {
		if(null == file) {
			return null;
		}
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取绝对路径<br>
	 * 此方法不会判定给定路径是否有效（文件或目录存在）
	 * 
	 * @param path 相对路径
	 * @param baseClass 相对路径所相对的类
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path, Class<?> baseClass) {
		String normalPath;
		if (path == null) {
			normalPath = StrUtil.EMPTY;
		} else {
			normalPath = normalize(path);
			if (isAbsolutePath(normalPath)) {
				// 给定的路径已经是绝对路径了
				return normalPath;
			}
		}

		// 相对于ClassPath路径
		final URL url = ResourceUtil.getResource(normalPath, baseClass);
		if (null != url) {
			// 对于jar中文件包含file:前缀，需要去掉此类前缀，在此做标准化，since 3.0.8 解决中文或空格路径被编码的问题
			return FileUtil.normalize(URLUtil.getDecodedPath(url));
		}

		// 如果资源不存在，则返回一个拼接的资源绝对路径
		final String classPath = ClassUtil.getClassPath();
		if (null == classPath) {
			throw new NullPointerException("ClassPath is null !");
		}

		// 资源不存在的情况下使用标准化路径有问题，使用原始路径拼接后标准化路径
		return normalize(classPath.concat(path));
	}

	/**
	 * 获取绝对路径，相对于ClassPath的目录<br>
	 * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/<br>
	 * 兼容Spring风格的路径表示，例如：classpath:config/example.setting也会被识别后转换
	 * 
	 * @param path 相对路径
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path) {
		return getAbsolutePath(path, null);
	}

	/**
	 * 获取标准的绝对路径
	 * 
	 * @param file 文件
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(File file) {
		if (file == null) {
			return null;
		}

		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

	/**
	 * 给定路径已经是绝对路径<br>
	 * 此方法并没有针对路径做标准化，建议先执行{@link #normalize(String)}方法标准化路径后判断
	 * 
	 * @param path 需要检查的Path
	 * @return 是否已经是绝对路径
	 */
	public static boolean isAbsolutePath(String path) {
		if (StrUtil.isEmpty(path)) {
			return false;
		}

		if (StrUtil.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:[/\\\\].*")) {
			// 给定的路径已经是绝对路径了
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为目录，如果path为null，则返回false
	 * 
	 * @param path 文件路径
	 * @return 如果为目录true
	 */
	public static boolean isDirectory(String path) {
		return (path == null) ? false : file(path).isDirectory();
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false
	 * 
	 * @param file 文件
	 * @return 如果为目录true
	 */
	public static boolean isDirectory(File file) {
		return (file == null) ? false : file.isDirectory();
	}

	/**
	 * 判断是否为目录，如果file为null，则返回false
	 * 
	 * @param path {@link Path}
	 * @param isFollowLinks 是否追踪到软链对应的真实地址
	 * @return 如果为目录true
	 * @since 3.1.0
	 */
	public static boolean isDirectory(Path path, boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.isDirectory(path, options);
	}

	/**
	 * 判断是否为文件，如果path为null，则返回false
	 * 
	 * @param path 文件路径
	 * @return 如果为文件true
	 */
	public static boolean isFile(String path) {
		return (path == null) ? false : file(path).isFile();
	}

	/**
	 * 判断是否为文件，如果file为null，则返回false
	 * 
	 * @param file 文件
	 * @return 如果为文件true
	 */
	public static boolean isFile(File file) {
		return (file == null) ? false : file.isFile();
	}

	/**
	 * 判断是否为文件，如果file为null，则返回false
	 * 
	 * @param path 文件
	 * @param isFollowLinks 是否跟踪软链（快捷方式）
	 * @return 如果为文件true
	 */
	public static boolean isFile(Path path, boolean isFollowLinks) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.isRegularFile(path, options);
	}

	/**
	 * 检查两个文件是否是同一个文件<br>
	 * 所谓文件相同，是指File对象是否指向同一个文件或文件夹
	 * 
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 是否相同
	 * @throws IORuntimeException IO异常
	 * @see Files#isSameFile(Path, Path)
	 */
	public static boolean equals(File file1, File file2) throws IORuntimeException {
		Assert.notNull(file1);
		Assert.notNull(file2);
		if (false == file1.exists() || false == file2.exists()) {
			// 两个文件都不存在判断其路径是否相同
			if (false == file1.exists() && false == file2.exists() && pathEquals(file1, file2)) {
				return true;
			}
			// 对于一个存在一个不存在的情况，一定不相同
			return false;
		}
		try {
			return Files.isSameFile(file1.toPath(), file2.toPath());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 比较两个文件内容是否相同<br>
	 * 首先比较长度，长度一致再比较内容<br>
	 * 此方法来自Apache Commons io
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 两个文件内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEquals(File file1, File file2) throws IORuntimeException {
		boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}

		if (false == file1Exists) {
			// 两个文件都不存在，返回true
			return true;
		}

		if (file1.isDirectory() || file2.isDirectory()) {
			// 不比较目录
			throw new IORuntimeException("Can't compare directories, only files");
		}

		if (file1.length() != file2.length()) {
			// 文件长度不同
			return false;
		}

		if (equals(file1, file2)) {
			// 同一个文件
			return true;
		}

		InputStream input1 = null;
		InputStream input2 = null;
		try {
			input1 = getInputStream(file1);
			input2 = getInputStream(file2);
			return IoUtil.contentEquals(input1, input2);

		} finally {
			IoUtil.close(input1);
			IoUtil.close(input2);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * 比较两个文件内容是否相同<br>
	 * 首先比较长度，长度一致再比较内容，比较内容采用按行读取，每行比较<br>
	 * 此方法来自Apache Commons io
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @param charset 编码，null表示使用平台默认编码 两个文件内容一致返回true，否则false
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static boolean contentEqualsIgnoreEOL(File file1, File file2, Charset charset) throws IORuntimeException {
		boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}

		if (!file1Exists) {
			// 两个文件都不存在，返回true
			return true;
		}

		if (file1.isDirectory() || file2.isDirectory()) {
			// 不比较目录
			throw new IORuntimeException("Can't compare directories, only files");
		}

		if (equals(file1, file2)) {
			// 同一个文件
			return true;
		}

		Reader input1 = null;
		Reader input2 = null;
		try {
			input1 = getReader(file1, charset);
			input2 = getReader(file2, charset);
			return IoUtil.contentEqualsIgnoreEOL(input1, input2);
		} finally {
			IoUtil.close(input1);
			IoUtil.close(input2);
		}
	}

	/**
	 * 文件路径是否相同<br>
	 * 取两个文件的绝对路径比较，在Windows下忽略大小写，在Linux下不忽略。
	 * 
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 文件路径是否相同
	 * @since 3.0.9
	 */
	public static boolean pathEquals(File file1, File file2) {
		if (isWindows()) {
			// Windows环境
			try {
				if (StrUtil.equalsIgnoreCase(file1.getCanonicalPath(), file2.getCanonicalPath())) {
					return true;
				}
			} catch (Exception e) {
				if (StrUtil.equalsIgnoreCase(file1.getAbsolutePath(), file2.getAbsolutePath())) {
					return true;
				}
			}
		} else {
			// 类Unix环境
			try {
				if (StrUtil.equals(file1.getCanonicalPath(), file2.getCanonicalPath())) {
					return true;
				}
			} catch (Exception e) {
				if (StrUtil.equals(file1.getAbsolutePath(), file2.getAbsolutePath())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得最后一个文件路径分隔符的位置
	 * 
	 * @param filePath 文件路径
	 * @return 最后一个文件路径分隔符的位置
	 */
	public static int lastIndexOfSeparator(String filePath) {
		if (filePath == null) {
			return -1;
		}
		int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
		return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
	}

	/**
	 * 判断文件是否被改动<br>
	 * 如果文件对象为 null 或者文件不存在，被视为改动
	 * 
	 * @param file 文件对象
	 * @param lastModifyTime 上次的改动时间
	 * @return 是否被改动
	 */
	public static boolean isModifed(File file, long lastModifyTime) {
		if (null == file || false == file.exists()) {
			return true;
		}
		return file.lastModified() != lastModifyTime;
	}

	/**
	 * 修复路径<br>
	 * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
	 * <ol>
	 * <li>1. 统一用 /</li>
	 * <li>2. 多个 / 转换为一个 /</li>
	 * <li>3. 去除两边空格</li>
	 * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
	 * </ol>
	 * 
	 * 栗子：
	 * 
	 * <pre>
	 * "/foo//" =》 "/foo/"
	 * "/foo/./" =》 "/foo/"
	 * "/foo/../bar" =》 "/bar"
	 * "/foo/../bar/" =》 "/bar/"
	 * "/foo/../bar/../baz" =》 "/baz"
	 * "/../" =》 "/"
	 * "foo/bar/.." =》 "foo"
	 * "foo/../bar" =》 "bar"
	 * "foo/../../bar" =》 "bar"
	 * "//server/foo/../bar" =》 "/server/bar"
	 * "//server/../bar" =》 "/bar"
	 * "C:\\foo\\..\\bar" =》 "C:/bar"
	 * "C:\\..\\bar" =》 "C:/bar"
	 * "~/foo/../bar/" =》 "~/bar/"
	 * "~/../bar" =》 "bar"
	 * </pre>
	 * 
	 * @param path 原路径
	 * @return 修复后的路径
	 */
	public static String normalize(String path) {
		if (path == null) {
			return null;
		}

		// 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
		String pathToUse = StrUtil.removePrefixIgnoreCase(path, "classpath:");
		// 去除file:前缀
		pathToUse = StrUtil.removePrefixIgnoreCase(pathToUse, "file:");
		// 统一使用斜杠
		pathToUse = pathToUse.replaceAll("[/\\\\]{1,}", "/").trim();

		int prefixIndex = pathToUse.indexOf(StrUtil.COLON);
		String prefix = "";
		if (prefixIndex > -1) {
			// 可能Windows风格路径
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (StrUtil.startWith(prefix, StrUtil.C_SLASH)) {
				// 去除类似于/C:这类路径开头的斜杠
				prefix = prefix.substring(1);
			}
			if (false == prefix.contains("/")) {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			} else {
				// 如果前缀中包含/,说明非Windows风格path
				prefix = StrUtil.EMPTY;
			}
		}
		if (pathToUse.startsWith(StrUtil.SLASH)) {
			prefix += StrUtil.SLASH;
			pathToUse = pathToUse.substring(1);
		}

		List<String> pathList = StrUtil.split(pathToUse, StrUtil.C_SLASH);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		String element;
		for (int i = pathList.size() - 1; i >= 0; i--) {
			element = pathList.get(i);
			if (StrUtil.DOT.equals(element)) {
				// 当前目录，丢弃
			} else if (StrUtil.DOUBLE_DOT.equals(element)) {
				tops++;
			} else {
				if (tops > 0) {
					// 有上级目录标记时按照个数依次跳过
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		return prefix + CollUtil.join(pathElements, StrUtil.SLASH);
	}

	/**
	 * 获得相对子路径
	 * 
	 * 栗子：
	 * 
	 * <pre>
	 * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
	 * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
	 * </pre>
	 * 
	 * @param rootDir 绝对父路径
	 * @param file 文件
	 * @return 相对子路径
	 */
	public static String subPath(String rootDir, File file) {
		try {
			return subPath(rootDir, file.getCanonicalPath());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得相对子路径，忽略大小写
	 * 
	 * 栗子：
	 * 
	 * <pre>
	 * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
	 * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
	 * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/     =》    ""
	 * </pre>
	 * 
	 * @param dirPath 父路径
	 * @param filePath 文件路径
	 * @return 相对子路径
	 */
	public static String subPath(String dirPath, String filePath) {
		if (StrUtil.isNotEmpty(dirPath) && StrUtil.isNotEmpty(filePath)) {

			dirPath = StrUtil.removeSuffix(normalize(dirPath), "/");
			filePath = normalize(filePath);

			final String result = StrUtil.removePrefixIgnoreCase(filePath, dirPath);
			return StrUtil.removePrefix(result, "/");
		}
		return filePath;
	}

	/**
	 * 获取指定位置的子路径部分，支持负数，例如index为-1表示从后数第一个节点位置
	 * 
	 * @param path 路径
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
	 * @param path 路径
	 * @param fromIndex 起始路径节点（包括）
	 * @param toIndex 结束路径节点（不包括）
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

	// -------------------------------------------------------------------------------------------- name start
	/**
	 * 返回主文件名
	 * 
	 * @param file 文件
	 * @return 主文件名
	 */
	public static String mainName(File file) {
		if (file.isDirectory()) {
			return file.getName();
		}
		return mainName(file.getName());
	}

	/**
	 * 返回主文件名
	 * 
	 * @param fileName 完整文件名
	 * @return 主文件名
	 */
	public static String mainName(String fileName) {
		if (StrUtil.isBlank(fileName) || false == fileName.contains(StrUtil.DOT)) {
			return fileName;
		}
		return StrUtil.subPre(fileName, fileName.lastIndexOf(StrUtil.DOT));
	}

	/**
	 * 获取文件扩展名，扩展名不带“.”
	 * 
	 * @param file 文件
	 * @return 扩展名
	 */
	public static String extName(File file) {
		if (null == file) {
			return null;
		}
		if (file.isDirectory()) {
			return null;
		}
		return extName(file.getName());
	}

	/**
	 * 获得文件的扩展名，扩展名不带“.”
	 * 
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String extName(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(StrUtil.DOT);
		if (index == -1) {
			return StrUtil.EMPTY;
		} else {
			String ext = fileName.substring(index + 1);
			// 扩展名中不能包含路径相关的符号
			return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StrUtil.EMPTY : ext;
		}
	}
	// -------------------------------------------------------------------------------------------- name end

	/**
	 * 判断文件路径是否有指定后缀，忽略大小写<br>
	 * 常用语判断扩展名
	 * 
	 * @param file 文件或目录
	 * @param suffix 后缀
	 * @return 是否有指定后缀
	 */
	public static boolean pathEndsWith(File file, String suffix) {
		return file.getPath().toLowerCase().endsWith(suffix);
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 
	 * @see FileTypeUtil#getType(File)
	 * 
	 * @param file 文件 {@link File}
	 * @return 类型，文件的扩展名，未找到为<code>null</code>
	 * @throws IORuntimeException IO异常
	 */
	public static String getType(File file) throws IORuntimeException {
		return FileTypeUtil.getType(file);
	}

	/**
	 * 获取文件属性
	 * 
	 * @param path 文件路径{@link Path}
	 * @param isFollowLinks 是否跟踪到软链对应的真实路径
	 * @return {@link BasicFileAttributes}
	 * @throws IORuntimeException IO异常
	 * @since 3.1.0
	 */
	public static BasicFileAttributes getAttributes(Path path, boolean isFollowLinks) throws IORuntimeException {
		if (null == path) {
			return null;
		}

		final LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		try {
			return Files.readAttributes(path, BasicFileAttributes.class, options);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------------------------- in start
	/**
	 * 获得输入流
	 * 
	 * @param path Path
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 * @since 4.0.0
	 */
	public static BufferedInputStream getInputStream(Path path) throws IORuntimeException {
		try {
			return new BufferedInputStream(Files.newInputStream(path));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得输入流
	 * 
	 * @param file 文件
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 */
	public static BufferedInputStream getInputStream(File file) throws IORuntimeException {
		return new BufferedInputStream(IoUtil.toStream(file));
	}

	/**
	 * 获得输入流
	 * 
	 * @param path 文件路径
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 */
	public static BufferedInputStream getInputStream(String path) throws IORuntimeException {
		return getInputStream(file(path));
	}

	/**
	 * 获得BOM输入流，用于处理带BOM头的文件
	 * 
	 * @param file 文件
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 */
	public static BOMInputStream getBOMInputStream(File file) throws IORuntimeException {
		try {
			return new BOMInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
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
	 * @param file 文件
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getUtf8Reader(File file) throws IORuntimeException {
		return getReader(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 文件路径
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getUtf8Reader(String path) throws IORuntimeException {
		return getReader(path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 文件Path
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 * @since 4.0.0
	 */
	public static BufferedReader getReader(Path path, Charset charset) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(path), charset);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param file 文件
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(File file, String charsetName) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(file), charsetName);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(File file, Charset charset) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(file), charset);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 绝对路径
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(String path, String charsetName) throws IORuntimeException {
		return getReader(file(path), charsetName);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(String path, Charset charset) throws IORuntimeException {
		return getReader(file(path), charset);
	}

	// -------------------------------------------------------------------------------------------- in end

	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过Integer.MAX_VALUE
	 * 
	 * @param file 文件
	 * @return 字节码
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(File file) throws IORuntimeException {
		return FileReader.create(file).readBytes();
	}

	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过Integer.MAX_VALUE
	 * 
	 * @param filePath 文件路径
	 * @return 字节码
	 * @throws IORuntimeException IO异常
	 * @since 3.2.0
	 */
	public static byte[] readBytes(String filePath) throws IORuntimeException {
		return readBytes(file(filePath));
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file 文件
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readUtf8String(File file) throws IORuntimeException {
		return readString(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path 文件路径
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readUtf8String(String path) throws IORuntimeException {
		return readString(path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file 文件
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(File file, String charsetName) throws IORuntimeException {
		return readString(file, CharsetUtil.charset(charsetName));
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(File file, Charset charset) throws IORuntimeException {
		return FileReader.create(file, charset).readString();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path 文件路径
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(String path, String charsetName) throws IORuntimeException {
		return readString(file(path), charsetName);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(String path, Charset charset) throws IORuntimeException {
		return readString(file(path), charset);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param url 文件URL
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(URL url, String charset) throws IORuntimeException {
		if (url == null) {
			throw new NullPointerException("Empty url provided!");
		}

		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.read(in, charset);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 从文件中读取每一行的UTF-8编码数据
	 * 
	 * @param <T> 集合类型
	 * @param path 文件路径
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T extends Collection<String>> T readUtf8Lines(String path, T collection) throws IORuntimeException {
		return readLines(path, CharsetUtil.CHARSET_UTF_8, collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IORuntimeException {
		return readLines(file(path), charset, collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(String path, Charset charset, T collection) throws IORuntimeException {
		return readLines(file(path), charset, collection);
	}

	/**
	 * 从文件中读取每一行数据，数据编码为UTF-8
	 * 
	 * @param <T> 集合类型
	 * @param file 文件路径
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T extends Collection<String>> T readUtf8Lines(File file, T collection) throws IORuntimeException {
		return readLines(file, CharsetUtil.CHARSET_UTF_8, collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param file 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IORuntimeException {
		return FileReader.create(file, CharsetUtil.charset(charset)).readLines(collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param file 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(File file, Charset charset, T collection) throws IORuntimeException {
		return FileReader.create(file, charset).readLines(collection);
	}

	/**
	 * 从文件中读取每一行数据，编码为UTF-8
	 * 
	 * @param <T> 集合类型
	 * @param url 文件的URL
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readUtf8Lines(URL url, T collection) throws IORuntimeException {
		return readLines(url, CharsetUtil.CHARSET_UTF_8, collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param url 文件的URL
	 * @param charsetName 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(URL url, String charsetName, T collection) throws IORuntimeException {
		return readLines(url, CharsetUtil.charset(charsetName), collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param <T> 集合类型
	 * @param url 文件的URL
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T extends Collection<String>> T readLines(URL url, Charset charset, T collection) throws IORuntimeException {
		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.readLines(in, charset, collection);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param url 文件的URL
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readUtf8Lines(URL url) throws IORuntimeException {
		return readLines(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param url 文件的URL
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readLines(URL url, String charset) throws IORuntimeException {
		return readLines(url, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param url 文件的URL
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readLines(URL url, Charset charset) throws IORuntimeException {
		return readLines(url, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据，编码为UTF-8
	 * 
	 * @param path 文件路径
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static List<String> readUtf8Lines(String path) throws IORuntimeException {
		return readLines(path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readLines(String path, String charset) throws IORuntimeException {
		return readLines(path, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static List<String> readLines(String path, Charset charset) throws IORuntimeException {
		return readLines(path, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param file 文件
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static List<String> readUtf8Lines(File file) throws IORuntimeException {
		return readLines(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readLines(File file, String charset) throws IORuntimeException {
		return readLines(file, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException IO异常
	 */
	public static List<String> readLines(File file, Charset charset) throws IORuntimeException {
		return readLines(file, charset, new ArrayList<String>());
	}

	/**
	 * 按行处理文件内容，编码为UTF-8
	 * 
	 * @param file 文件
	 * @param lineHandler {@link LineHandler}行处理器
	 * @throws IORuntimeException IO异常
	 */
	public static void readUtf8Lines(File file, LineHandler lineHandler) throws IORuntimeException {
		readLines(file, CharsetUtil.CHARSET_UTF_8, lineHandler);
	}

	/**
	 * 按行处理文件内容
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineHandler {@link LineHandler}行处理器
	 * @throws IORuntimeException IO异常
	 */
	public static void readLines(File file, Charset charset, LineHandler lineHandler) throws IORuntimeException {
		FileReader.create(file, charset).readLines(lineHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @deprecated 使用FileUtil#load(String, String, ReaderHandler) 代替
	 */
	@Deprecated
	public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IORuntimeException {
		return FileReader.create(file(path), CharsetUtil.charset(charset)).read(readerHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T> T loadUtf8(String path, ReaderHandler<T> readerHandler) throws IORuntimeException {
		return load(path, CharsetUtil.CHARSET_UTF_8, readerHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T> T load(String path, String charset, ReaderHandler<T> readerHandler) throws IORuntimeException {
		return FileReader.create(file(path), CharsetUtil.charset(charset)).read(readerHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T> T load(String path, Charset charset, ReaderHandler<T> readerHandler) throws IORuntimeException {
		return FileReader.create(file(path), charset).read(readerHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param file 文件
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T> T loadUtf8(File file, ReaderHandler<T> readerHandler) throws IORuntimeException {
		return load(file, CharsetUtil.CHARSET_UTF_8, readerHandler);
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param <T> 集合类型
	 * @param readerHandler Reader处理类
	 * @param file 文件
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException IO异常
	 * @since 3.1.1
	 */
	public static <T> T load(File file, Charset charset, ReaderHandler<T> readerHandler) throws IORuntimeException {
		return FileReader.create(file, charset).read(readerHandler);
	}

	// -------------------------------------------------------------------------------------------- out start
	/**
	 * 获得一个输出流对象
	 * 
	 * @param file 文件
	 * @return 输出流对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedOutputStream getOutputStream(File file) throws IORuntimeException {
		try {
			return new BufferedOutputStream(new FileOutputStream(touch(file)));
		} catch (Exception e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得一个输出流对象
	 * 
	 * @param path 输出到的文件路径，绝对路径
	 * @return 输出流对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedOutputStream getOutputStream(String path) throws IORuntimeException {
		return getOutputStream(touch(path));
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charsetName 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IORuntimeException {
		return getWriter(touch(path), Charset.forName(charsetName), isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IORuntimeException {
		return getWriter(touch(path), charset, isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param file 输出文件
	 * @param charsetName 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IORuntimeException {
		return getWriter(file, Charset.forName(charsetName), isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param file 输出文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, charset).getWriter(isAppend);
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IORuntimeException IO异常
	 */
	public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IORuntimeException {
		return new PrintWriter(getWriter(path, charset, isAppend));
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IORuntimeException IO异常
	 * @since 4.1.1
	 */
	public static PrintWriter getPrintWriter(String path, Charset charset, boolean isAppend) throws IORuntimeException {
		return new PrintWriter(getWriter(path, charset, isAppend));
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IORuntimeException IO异常
	 */
	public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IORuntimeException {
		return new PrintWriter(getWriter(file, charset, isAppend));
	}

	/**
	 * 获取当前系统的换行分隔符
	 * 
	 * <pre>
	 * Windows: \r\n
	 * Mac: \r
	 * Linux: \n
	 * </pre>
	 * 
	 * @return 换行符
	 * @since 4.0.5
	 */
	public static String getLineSeparator() {
		return System.lineSeparator();
		// return System.getProperty("line.separator");
	}

	// -------------------------------------------------------------------------------------------- out end

	/**
	 * 将String写入文件，覆盖模式，字符集为UTF-8
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeUtf8String(String content, String path) throws IORuntimeException {
		return writeString(content, path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将String写入文件，覆盖模式，字符集为UTF-8
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeUtf8String(String content, File file) throws IORuntimeException {
		return writeString(content, file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeString(String content, String path, String charset) throws IORuntimeException {
		return writeString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeString(String content, String path, Charset charset) throws IORuntimeException {
		return writeString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 被写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeString(String content, File file, String charset) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).write(content);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 被写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeString(String content, File file, Charset charset) throws IORuntimeException {
		return FileWriter.create(file, charset).write(content);
	}

	/**
	 * 将String写入文件，UTF-8编码追加模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static File appendUtf8String(String content, String path) throws IORuntimeException {
		return appendString(content, path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File appendString(String content, String path, String charset) throws IORuntimeException {
		return appendString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File appendString(String content, String path, Charset charset) throws IORuntimeException {
		return appendString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，UTF-8编码追加模式
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static File appendUtf8String(String content, File file) throws IORuntimeException {
		return appendString(content, file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File appendString(String content, File file, String charset) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).append(content);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException IO异常
	 */
	public static File appendString(String content, File file, Charset charset) throws IORuntimeException {
		return FileWriter.create(file, charset).append(content);
	}

	/**
	 * 将列表写入文件，覆盖模式，编码为UTF-8
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 绝对路径
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.0
	 */
	public static <T> File writeUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
		return writeLines(list, path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将列表写入文件，覆盖模式，编码为UTF-8
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 绝对路径
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.2.0
	 */
	public static <T> File writeUtf8Lines(Collection<T> list, File file) throws IORuntimeException {
		return writeLines(list, file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
		return writeLines(list, path, charset, false);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
		return writeLines(list, path, charset, false);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.2.0
	 */
	public static <T> File writeLines(Collection<T> list, File file, String charset) throws IORuntimeException {
		return writeLines(list, file, charset, false);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.2.0
	 */
	public static <T> File writeLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
		return writeLines(list, file, charset, false);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static <T> File appendUtf8Lines(Collection<T> list, File file) throws IORuntimeException {
		return appendLines(list, file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 文件路径
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static <T> File appendUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
		return appendLines(list, path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
		return writeLines(list, path, charset, true);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static <T> File appendLines(Collection<T> list, File file, String charset) throws IORuntimeException {
		return writeLines(list, file, charset, true);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File appendLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
		return writeLines(list, path, charset, true);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 3.1.2
	 */
	public static <T> File appendLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
		return writeLines(list, file, charset, true);
	}

	/**
	 * 将列表写入文件
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IORuntimeException {
		return writeLines(list, file(path), charset, isAppend);
	}

	/**
	 * 将列表写入文件
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, String path, Charset charset, boolean isAppend) throws IORuntimeException {
		return writeLines(list, file(path), charset, isAppend);
	}

	/**
	 * 将列表写入文件
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).writeLines(list, isAppend);
	}

	/**
	 * 将列表写入文件
	 * 
	 * @param <T> 集合元素类型
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static <T> File writeLines(Collection<T> list, File file, Charset charset, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, charset).writeLines(list, isAppend);
	}

	/**
	 * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
	 * 
	 * @param map Map
	 * @param file 文件
	 * @param kvSeparator 键和值之间的分隔符，如果传入null使用默认分隔符" = "
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.0.5
	 */
	public static File writeUtf8Map(Map<?, ?> map, File file, String kvSeparator, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.CHARSET_UTF_8).writeMap(map, kvSeparator, isAppend);
	}

	/**
	 * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
	 * 
	 * @param map Map
	 * @param file 文件
	 * @param charset 字符集编码
	 * @param kvSeparator 键和值之间的分隔符，如果传入null使用默认分隔符" = "
	 * @param isAppend 是否追加
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 * @since 4.0.5
	 */
	public static File writeMap(Map<?, ?> map, File file, Charset charset, String kvSeparator, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, charset).writeMap(map, kvSeparator, isAppend);
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param data 数据
	 * @param path 目标文件
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeBytes(byte[] data, String path) throws IORuntimeException {
		return writeBytes(data, touch(path));
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param dest 目标文件
	 * @param data 数据
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeBytes(byte[] data, File dest) throws IORuntimeException {
		return writeBytes(data, dest, 0, data.length, false);
	}

	/**
	 * 写入数据到文件
	 * 
	 * @param data 数据
	 * @param dest 目标文件
	 * @param off 数据开始位置
	 * @param len 数据长度
	 * @param isAppend 是否追加模式
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeBytes(byte[] data, File dest, int off, int len, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(dest).write(data, off, len, isAppend);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param dest 目标文件
	 * @param in 输入流
	 * @return dest
	 * @throws IORuntimeException IO异常
	 */
	public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
		return FileWriter.create(dest).writeFromStream(in);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param in 输入流
	 * @param fullFilePath 文件绝对路径
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeFromStream(InputStream in, String fullFilePath) throws IORuntimeException {
		return writeFromStream(in, touch(fullFilePath));
	}

	/**
	 * 将文件写入流中
	 * 
	 * @param file 文件
	 * @param out 流
	 * @return 目标文件
	 * @throws IORuntimeException IO异常
	 */
	public static File writeToStream(File file, OutputStream out) throws IORuntimeException {
		return FileReader.create(file).writeToStream(out);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param fullFilePath 文件绝对路径
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static void writeToStream(String fullFilePath, OutputStream out) throws IORuntimeException {
		writeToStream(touch(fullFilePath), out);
	}

	/**
	 * 可读的文件大小
	 * 
	 * @param file 文件
	 * @return 大小
	 */
	public static String readableFileSize(File file) {
		return readableFileSize(file.length());
	}

	/**
	 * 可读的文件大小<br>
	 * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	 * 
	 * @param size Long类型大小
	 * @return 大小
	 */
	public static String readableFileSize(long size) {
		if (size <= 0) {
			return "0";
		}
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * 转换文件编码<br>
	 * 此方法用于转换文件编码，读取的文件实际编码必须与指定的srcCharset编码一致，否则导致乱码
	 * 
	 * @param file 文件
	 * @param srcCharset 原文件的编码，必须与文件内容的编码保持一致
	 * @param destCharset 转码后的编码
	 * @return 被转换编码的文件
	 * @see CharsetUtil#convert(File, Charset, Charset)
	 * @since 3.1.0
	 */
	public static File convertCharset(File file, Charset srcCharset, Charset destCharset) {
		return CharsetUtil.convert(file, srcCharset, destCharset);
	}

	/**
	 * 转换换行符<br>
	 * 将给定文件的换行符转换为指定换行符
	 * 
	 * @param file 文件
	 * @param charset 编码
	 * @param lineSeparator 换行符枚举{@link LineSeparator}
	 * @return 被修改的文件
	 * @since 3.1.0
	 */
	public static File convertLineSeparator(File file, Charset charset, LineSeparator lineSeparator) {
		final List<String> lines = readLines(file, charset);
		return FileWriter.create(file, charset).writeLines(lines, lineSeparator, false);
	}

	/**
	 * 清除文件名中的在Windows下不支持的非法字符，包括： \ / : * ? " &lt; &gt; |
	 * 
	 * @param fileName 文件名（必须不包括路径，否则路径符将被替换）
	 * @return 清理后的文件名
	 * @since 3.3.1
	 */
	public static String cleanInvalid(String fileName) {
		return StrUtil.isBlank(fileName) ? fileName : ReUtil.delAll(FILE_NAME_INVALID_PATTERN_WIN, fileName);
	}

	/**
	 * 文件名中是否包含在Windows下不支持的非法字符，包括： \ / : * ? " &lt; &gt; |
	 * 
	 * @param fileName 文件名（必须不包括路径，否则路径符将被替换）
	 * @return 是否包含非法字符
	 * @since 3.3.1
	 */
	public static boolean containsInvalid(String fileName) {
		return StrUtil.isBlank(fileName) ? false : ReUtil.contains(FILE_NAME_INVALID_PATTERN_WIN, fileName);
	}

	/**
	 * 计算文件CRC32校验码
	 * 
	 * @param file 文件，不能为目录
	 * @return CRC32值
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static long checksumCRC32(File file) throws IORuntimeException {
		return checksum(file, new CRC32()).getValue();
	}

	/**
	 * 计算文件校验码
	 * 
	 * @param file 文件，不能为目录
	 * @param checksum {@link Checksum}
	 * @return Checksum
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public static Checksum checksum(File file, Checksum checksum) throws IORuntimeException {
		Assert.notNull(file, "File is null !");
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		try {
			return IoUtil.checksum(new FileInputStream(file), checksum);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取Web项目下的web root路径
	 * 
	 * @return web root路径
	 * @since 4.0.13
	 */
	public static File getWebRoot() {
		String classPath = ClassUtil.getClassPath();
		if (StrUtil.isNotBlank(classPath)) {
			return file(classPath).getParentFile().getParentFile();
		}
		return null;
	}
	
	/**
	 * 获取指定层级的父路径
	 * 
	 * <pre>
	 * getParent("d:/aaa/bbb/cc/ddd", 0) -> "d:/aaa/bbb/cc/ddd"
	 * getParent("d:/aaa/bbb/cc/ddd", 2) -> "d:/aaa/bbb"
	 * getParent("d:/aaa/bbb/cc/ddd", 4) -> "d:/"
	 * getParent("d:/aaa/bbb/cc/ddd", 5) -> null
	 * </pre>
	 * 
	 * @param filePath 目录或文件路径
	 * @param level 层级
	 * @return 路径File，如果不存在返回null
	 * @since 4.1.2
	 */
	public static String getParent(String filePath, int level) {
		final File parent = getParent(file(filePath), level);
		return null == parent ? null : parent.getAbsolutePath();
	}

	/**
	 * 获取指定层级的父路径
	 * 
	 * <pre>
	 * getParent(file("d:/aaa/bbb/cc/ddd", 0)) -> "d:/aaa/bbb/cc/ddd"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 2)) -> "d:/aaa/bbb"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 4)) -> "d:/"
	 * getParent(file("d:/aaa/bbb/cc/ddd", 5)) -> null
	 * </pre>
	 * 
	 * @param file 目录或文件
	 * @param level 层级
	 * @return 路径File，如果不存在返回null
	 * @since 4.1.2
	 */
	public static File getParent(File file, int level) {
		if (level < 1 || null == file) {
			return file;
		}

		final File parentFile = file.getParentFile();
		if (1 == level) {
			return parentFile;
		}
		return getParent(parentFile, level - 1);
	}
}
