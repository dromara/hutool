package com.xiaoleilu.hutool.io;

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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.xiaoleilu.hutool.io.file.FileReader;
import com.xiaoleilu.hutool.io.file.FileReader.ReaderHandler;
import com.xiaoleilu.hutool.io.file.FileWriter;
import com.xiaoleilu.hutool.json.JSON;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.xiaoleilu.hutool.util.URLUtil;

/**
 * 文件工具类
 * 
 * @author xiaoleilu
 *
 */
public final class FileUtil {
	
	private FileUtil() {}

	/** The Unix separator character. */
	private static final char UNIX_SEPARATOR = '/';
	/** The Windows separator character. */
	private static final char WINDOWS_SEPARATOR = '\\';

	/** Class文件扩展名 */
	public static final String CLASS_EXT = ".class";
	/** Jar文件扩展名 */
	public static final String JAR_FILE_EXT = ".jar";
	/** 在Jar中的路径jar的扩展名形式 */
	public static final String JAR_PATH_EXT = ".jar!";
	/** 当Path为文件形式时, path会加入一个表示文件的前缀 */
	public static final String PATH_FILE_PRE = "file:";

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
	 * 目录：里面没有文件时为空
	 * 文件：文件大小为0时为空
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
		}else if(file.isFile()){
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
	 * @param dirPath 目录
	 * @return 是否为空
	 * @exception IoRuntimeException IOException
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
	 * @param dir 目录
	 * @return 是否为空
	 */
	public static boolean isDirEmpty(File dir) {
		return isDirEmpty(dir.toPath());
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param file 当前遍历文件
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件
	 */
	public static List<File> loopFiles(File file, FileFilter fileFilter) {
		List<File> fileList = new ArrayList<File>();
		if (file == null) {
			return fileList;
		} else if (file.exists() == false) {
			return fileList;
		}

		if (file.isDirectory()) {
			for (File tmp : file.listFiles()) {
				fileList.addAll(loopFiles(tmp, fileFilter));
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
	 * @param file 当前遍历文件
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
	 * @throws IOException
	 */
	public static List<String> listFileNames(String path) {
		if (path == null) {
			return null;
		}
		path = getAbsolutePath(path);
		if (path.endsWith(String.valueOf(UNIX_SEPARATOR)) == false) {
			path = path + UNIX_SEPARATOR;
		}

		List<String> paths = new ArrayList<String>();
		int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
		try {
			if (index == -1) {
				// 普通目录路径
				File[] files = ls(path);
				for (File file : files) {
					if (file.isFile()) {
						paths.add(file.getName());
					}
				}
			} else {
				// jar文件中的路径
				index = index + FileUtil.JAR_FILE_EXT.length();
				final String jarPath = path.substring(0, index);
				final String subPath = path.substring(index + 2);
				for (JarEntry entry : Collections.list(new JarFile(jarPath).entries())) {
					final String name = entry.getName();
					if (name.startsWith(subPath)) {
						String nameSuffix = StrUtil.removePrefix(name, subPath);
						if (nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
							paths.add(nameSuffix);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IORuntimeException(StrUtil.format("Can not read file path of [{}]", path), e);
		}
		return paths;
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
		File file = new File(directory);
		if (!file.exists()) {
			return false;
		}

		String[] fileList = file.list();
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
	 * @param file 目录或文件
	 * @return 总大小
	 */
	public static long size(File file){
		Assert.notNull(file, "file argument is null !");
		if(false == file.exists()){
			throw new IllegalArgumentException(StrUtil.format("File [{}] not exist !", file.getAbsolutePath()));
		}
		
		if(file.isDirectory()){
			long size = 0L;
			File[] subFiles = file.listFiles();
			if(ArrayUtil.isEmpty(subFiles)){
				return 0L;//empty directory
			}
			for (int i = 0; i < subFiles.length; i++) {
				size += size(subFiles[i]);
			}
			return size;
		}else{
			return file.length();
		}
	}
	
	/**
	 * 给定文件或目录的最后修改时间是否晚于给定时间
	 * @param file 文件或目录
	 * @param reference 参照文件
	 * @return 是否晚于给定时间
	 */
	public static boolean newerThan(File file, File reference){
		if(null == file || false == reference.exists()){
			return true;//文件一定比一个不存在的文件新
		}
		return newerThan(file, reference.lastModified());
	}
	
	/**
	 * 给定文件或目录的最后修改时间是否晚于给定时间
	 * @param file 文件或目录
	 * @param timeMillis 做为对比的时间
	 * @return 是否晚于给定时间
	 */
	public static boolean newerThan(File file, long timeMillis){
		if(null == file || false == file.exists()){
			return false;//不存在的文件一定比任何时间旧
		}
		return file.lastModified() > timeMillis;
	}

	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param fullFilePath 文件的全路径，使用POSIX风格
	 * @return 文件，若路径为null，返回null
	 * @throws IOException
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
	 * @throws IOException
	 */
	public static File touch(File file) {
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
	 * @throws IOException 
	 */
	public static File touch(File parent, String path) throws IOException {
		return touch(file(parent, path));
	}
	
	/**
	 * 创建文件及其父目录，如果这个文件存在，直接返回这个文件<br>
	 * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
	 * 
	 * @param parent 父文件对象
	 * @param path 文件路径
	 * @return File
	 * @throws IOException 
	 */
	public static File touch(String parent, String path) throws IOException {
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
	 * 路径如果为相对路径，会转换为ClassPath路径！
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param fullFileOrDirPath 文件或者目录的路径
	 * @return 成功与否
	 * @throws IOException
	 */
	public static boolean del(String fullFileOrDirPath) throws IOException {
		return del(file(fullFileOrDirPath));
	}

	/**
	 * 删除文件或者文件夹<br>
	 * 注意：删除文件夹时不会判断文件夹是否为空，如果不空则递归删除子文件或文件夹<br>
	 * 某个文件删除失败会终止删除操作
	 * 
	 * @param file 文件对象
	 * @return 成功与否
	 * @throws IOException
	 */
	public static boolean del(File file) throws IOException {
		if (file == null || file.exists() == false) {
			return true;
		}

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File childFile : files) {
				boolean isOk = del(childFile);
				if (isOk == false) {
					// 删除一个出错则本次删除任务失败
					return false;
				}
			}
		}
		return file.delete();
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
	 * @throws IOException
	 */
	public static File createTempFile(File dir) throws IOException {
		return createTempFile("hutool", null, dir, true);
	}

	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].tmp
	 * 
	 * @param dir 临时文件创建的所在目录
	 * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
	 * @return 临时文件
	 * @throws IOException
	 */
	public static File createTempFile(File dir, boolean isReCreat) throws IOException {
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
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
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
					throw ioex;
				}
			}
		}
	}
	
	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * @param src 源文件路径
	 * @param dest 目标文件或目录路径，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return File
	 * @throws IOException
	 */
	public static File copyFile(String src, String dest, StandardCopyOption... options) throws IOException{
		Assert.notBlank(src, "Source File path is blank !");
		Assert.notNull(src, "Destination File path is null !");
		return copyFile(Paths.get(src), Paths.get(dest), options).toFile();
	}
	
	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * @param src 源文件
	 * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return File
	 * @throws IOException
	 */
	public static File copyFile(File src, File dest, StandardCopyOption... options) throws IOException{
		// check
		Assert.notNull(src, "Source File is null !");
		if (false == src.exists()) {
			throw new FileNotFoundException("File not exist: " + src);
		}
		Assert.notNull(dest, "Destination File or directiory is null !");
		if (equals(src, dest)) {
			throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
		}
		
		Path srcPath = src.toPath();
		Path destPath = dest.isDirectory() ? dest.toPath().resolve(srcPath.getFileName()) : dest.toPath();
		return Files.copy(srcPath, destPath, options).toFile();
	}
	
	/**
	 * 通过JDK7+的 {@link Files#copy(Path, Path, CopyOption...)} 方法拷贝文件
	 * @param src 源文件路径
	 * @param dest 目标文件或目录，如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IOException
	 */
	public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IOException{
		Assert.notNull(src, "Source File is null !");
		Assert.notNull(dest, "Destination File or directiory is null !");
		
		Path destPath = dest.toFile().isDirectory() ? dest.resolve(src.getFileName()) : dest;
		return Files.copy(src, destPath, options);
	}

	/**
	 * 复制文件或目录<br>
	 * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
	 * 
	 * @param srcPath 源文件或目录
	 * @param destPath 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IOException
	 */
	public static File copy(String srcPath, String destPath, boolean isOverride) throws IOException {
		return copy(file(srcPath), file(destPath), isOverride);
	}

	/**
	 * 复制文件或目录<br>
	 * 情况如下：<br>
	 * 1、src和dest都为目录，则讲src下所有文件目录拷贝到dest下<br>
	 * 2、src和dest都为文件，直接复制，名字为dest<br>
	 * 3、src为文件，dest为目录，将src拷贝到dest目录下<br>
	 * 
	 * @param src 源文件
	 * @param dest 目标文件或目录，目标不存在会自动创建（目录、文件都创建）
	 * @param isOverride 是否覆盖目标文件
	 * @return 目标目录或文件
	 * @throws IOException
	 */
	public static File copy(File src, File dest, boolean isOverride) throws IOException {
		// check
		Assert.notNull(src, "Source File is null !");
		if (false == src.exists()) {
			throw new FileNotFoundException("File not exist: " + src);
		}
		Assert.notNull(dest, "Destination File or directiory is null !");
		if (equals(src, dest)) {
			throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
		}

		if (src.isDirectory()) {//复制目录
			internalCopyDir(src, dest, isOverride);
		}else{//复制文件
			internalCopyFile(src, dest, isOverride);
		}
		return dest;
	}
	
	/**
	 * 拷贝目录，只用于内部，不做任何安全检查
	 * @param src 源目录
	 * @param dest 目标目录
	 * @param isOverride 是否覆盖
	 * @throws IOException
	 */
	private static void internalCopyDir(File src, File dest, boolean isOverride) throws IOException{
		if (false == dest.exists()) {
			dest.mkdirs();
		}else if(dest.isFile()) {
			throw new IOException(StrUtil.format("Src [{}] is a directory but dest [{}] is a file!", src.getPath(), dest.getPath()));
		}

		final String files[] = src.list();
		for (String file : files) {
			File srcFile = new File(src, file);
			File destFile = new File(dest, file);
			// 递归复制
			if(src.isDirectory()){
				internalCopyDir(srcFile, destFile, isOverride);
			}else{
				internalCopyFile(srcFile, destFile, isOverride);
			}
		}
	}
	
	/**
	 * 拷贝文件，只用于内部，不做任何安全检查
	 * @param src 源文件，必须为文件
	 * @param dest 目标文件，必须为文件
	 * @param isOverride 是否覆盖已有文件
	 * @throws IOException
	 */
	private static void internalCopyFile(File src, File dest, boolean isOverride) throws IOException{
		//copy
		if(false == dest.exists()){//目标不存在，默认做为文件创建
			touch(dest);
		}else if(dest.isDirectory()){//目标为目录，则在这个目录下创建同名文件
			dest = new File(dest, src.getName());
		}else if(false == isOverride){//如果已经存在目标文件，切为不覆盖模式，跳过之
//			StaticLog.debug("File [{}] already exist, ignore it.", dest);
			return;
		}
		
		// do copy file
		FileInputStream input = new FileInputStream(src);
		FileOutputStream output = new FileOutputStream(dest);
		try {
			IoUtil.copy(input, output);
		} finally {
			IoUtil.close(output);
			IoUtil.close(input);
		}

		//验证
		if (src.length() != dest.length()) {
			throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
		}
	}

	/**
	 * 移动文件或者目录
	 * 
	 * @param src 源文件或者目录
	 * @param dest 目标文件或者目录
	 * @param isOverride 是否覆盖目标，只有目标为文件才覆盖
	 * @throws IOException
	 */
	public static void move(File src, File dest, boolean isOverride) throws IOException {
		// check
		if (!src.exists()) {
			throw new FileNotFoundException("File already exist: " + src);
		}

		// 来源为文件夹，目标为文件
		if (src.isDirectory() && dest.isFile()) {
			throw new IOException(StrUtil.format("Can not move directory [{}] to file [{}]", src, dest));
		}
		
		if (isOverride && dest.isFile()) {//只有目标为文件的情况下覆盖之
			dest.delete();
		}

		// 来源为文件，目标为文件夹
		if (src.isFile() && dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}

		if (src.renameTo(dest) == false) {
			// 在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
			try {
				copy(src, dest, isOverride);
				src.delete();
			} catch (Exception e) {
				throw new IOException(StrUtil.format("Move [{}] to [{}] failed!", src, dest), e);
			}

		}
	}
	
	/**
	 * 获取绝对路径<br/>
	 * 此方法不会判定给定路径是否有效（文件或目录存在）
	 * 
	 * @param path 相对路径
	 * @param baseClass 相对路径所相对的类
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path, Class<?> baseClass) {
		if (path == null) {
			path = StrUtil.EMPTY;
		}
		if (baseClass == null) {
			return getAbsolutePath(path);
		}
		// return baseClass.getResource(path).getPath();
		return StrUtil.removePrefix(PATH_FILE_PRE, baseClass.getResource(path).getPath());
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
		if (path == null) {
			path = StrUtil.EMPTY;
		} else {
			path = normalize(path);

			if (StrUtil.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:/.*")) {
				// 给定的路径已经是绝对路径了
				return path;
			}
		}
		
		//兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
		path = StrUtil.removePrefixIgnoreCase(path, "classpath:");
		path = StrUtil.removePrefix(path, StrUtil.SLASH);

		// 相对于ClassPath路径
		ClassLoader classLoader = ClassUtil.getClassLoader();
		URL url = classLoader.getResource(path);
		String reultPath = url != null ? url.getPath() : ClassUtil.getClassPath() + path;
		// return StrUtil.removePrefix(reultPath, PATH_FILE_PRE);
		return reultPath;
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
	 * 检查两个文件是否是同一个文件
	 * 
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 是否相同
	 */
	public static boolean equals(File file1, File file2) {
		try {
			file1 = file1.getCanonicalFile();
			file2 = file2.getCanonicalFile();
		} catch (IOException ignore) {
			return false;
		}
		return file1.equals(file2);
	}

	/**
	 * 获得最后一个文件路径分隔符的位置
	 * 
	 * @param filePath 文件路径
	 * @return 最后一个文件路径分隔符的位置
	 */
	public static int indexOfLastSeparator(String filePath) {
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
	 * 1. 统一用 / <br>
	 * 2. 多个 / 转换为一个
	 * 3. 去除两边空格
	 * 4. .. 和 . 转换为绝对路径
	 * 5. 去掉前缀，例如file:
	 * 
	 * @param path 原路径
	 * @return 修复后的路径
	 */
	public static String normalize(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = path.replaceAll("[/\\\\]{1,}", "/").trim();

		int prefixIndex = pathToUse.indexOf(StrUtil.COLON);
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains("/")) {
				prefix = "";
			}else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(StrUtil.SLASH)) {
			prefix = prefix + StrUtil.SLASH;
			pathToUse = pathToUse.substring(1);
		}

		List<String> pathList = StrUtil.split(pathToUse, StrUtil.C_SLASH);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathList.size() - 1; i >= 0; i--) {
			String element = pathList.get(i);
			if (StrUtil.DOT.equals(element)) {
				//当前目录，丢弃
			}else if (StrUtil.DOUBLE_DOT.equals(element)) {
				tops++;
			}else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				}else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, StrUtil.DOUBLE_DOT);
		}

		return prefix + CollectionUtil.join(pathElements, StrUtil.SLASH);
	}
	
	/**
	 * 获得相对子路径
	 * 
	 * @param rootDir 绝对父路径
	 * @param filePath 文件路径
	 * @return 相对子路径
	 */
	public static String subPath(String rootDir, String filePath) {
		return subPath(rootDir, file(filePath));
	}

	/**
	 * 获得相对子路径
	 * 
	 * @param rootDir 绝对父路径
	 * @param file 文件
	 * @return 相对子路径
	 */
	public static String subPath(String rootDir, File file) {
		if (StrUtil.isEmpty(rootDir)) {
		}

		String subPath = null;
		try {
			subPath = file.getCanonicalPath();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		if (StrUtil.isNotEmpty(rootDir) && StrUtil.isNotEmpty(subPath)) {
			rootDir = normalize(rootDir);
			subPath = normalize(subPath);

			if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
				subPath = subPath.substring(rootDir.length() + 1);
			}
		}
		return subPath;
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
	
	/**
	 * 根据文件流的头部信息获得文件类型
	 * @see FileTypeUtil#getType(File)
	 * 
	 * @param file 文件 {@link File}
	 * @return 类型，文件的扩展名，未找到为<code>null</code>
	 * @throws IORuntimeException
	 */
	public static String getType(File file){
		try {
			return FileTypeUtil.getType(file);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	// -------------------------------------------------------------------------------------------- name end

	// -------------------------------------------------------------------------------------------- in start
	/**
	 * 获得输入流
	 * 
	 * @param file 文件
	 * @return 输入流
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}

	/**
	 * 获得输入流
	 * 
	 * @param path 文件路径
	 * @return 输入流
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getInputStream(String path) throws FileNotFoundException {
		return getInputStream(file(path));
	}
	
	/**
	 * 获得BOM输入流，用于处理带BOM头的文件
	 * 
	 * @param file 文件
	 * @return 输入流
	 * @throws FileNotFoundException
	 */
	public static BOMInputStream getBOMInputStream(File file) throws FileNotFoundException {
		return new BOMInputStream(new FileInputStream(file));
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param file 文件
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getUtf8Reader(File file) throws IOException {
		return getReader(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 文件路径
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getUtf8Reader(String path) throws IOException {
		return getReader(path, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param file 文件
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(File file, String charsetName) throws IOException {
		return IoUtil.getReader(getInputStream(file), charsetName);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(File file, Charset charset) throws IOException {
		return IoUtil.getReader(getInputStream(file), charset);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 绝对路径
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(String path, String charsetName) throws IOException {
		return getReader(file(path), charsetName);
	}

	/**
	 * 获得一个文件读取器
	 * 
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(String path, Charset charset) throws IOException {
		return getReader(file(path), charset);
	}

	// -------------------------------------------------------------------------------------------- in end

	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过Integer.MAX_VALUE
	 * 
	 * @param file 文件
	 * @return 字节码
	 * @throws IORuntimeException
	 */
	public static byte[] readBytes(File file) throws IORuntimeException {
		return FileReader.create(file).readBytes();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file 文件
	 * @return 内容
	 * @throws IOException
	 */
	public static String readUtf8String(File file) throws IOException {
		return readString(file, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path 文件路径
	 * @return 内容
	 * @throws IORuntimeException
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
	 * @throws IORuntimeException
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
	 * @throws IORuntimeException
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
	 * @throws IORuntimeException
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
	 * @throws IORuntimeException
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
	 * @throws IOException
	 */
	public static String readString(URL url, String charset) throws IORuntimeException {
		if (url == null) {
			throw new RuntimeException("Empty url provided!");
		}

		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.read(in, charset);
		}catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException
	 */
	public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IORuntimeException {
		return readLines(file(path), charset, collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param file 文件路径
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException
	 */
	public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IORuntimeException {
		return FileReader.create(file, CharsetUtil.charset(charset)).readLines(collection);
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param url 文件的URL
	 * @param charset 字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(URL url, String charset, T collection) throws IORuntimeException {
		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.readLines(in, charset, collection);
		}catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param url 文件的URL
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException
	 */
	public static List<String> readLines(URL url, String charset) throws IORuntimeException {
		return readLines(url, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException
	 */
	public static List<String> readLines(String path, String charset) throws IORuntimeException {
		return readLines(path, charset, new ArrayList<String>());
	}

	/**
	 * 从文件中读取每一行数据
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @return 文件中的每行内容的集合List
	 * @throws IORuntimeException
	 */
	public static List<String> readLines(File file, String charset) throws IORuntimeException {
		return readLines(file, charset, new ArrayList<String>());
	}

	/**
	 * 读取JSON
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSON（包括JSONObject和JSONArray）
	 * @throws IORuntimeException
	 */
	public static JSON readJSON(File file, Charset charset) throws IORuntimeException {
		return FileReader.create(file, charset).readJSON();
	}

	/**
	 * 读取JSON
	 * 
	 * @param file JSON文件
	 * @param charsetName 编码
	 * @return JSON（包括JSONObject和JSONArray）
	 * @throws IORuntimeException
	 */
	public static JSON readJSON(File file, String charsetName) throws IORuntimeException {
		return FileReader.create(file, CharsetUtil.charset(charsetName)).readJSON();
	}

	/**
	 * 读取JSONObject
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSONObject
	 * @throws IORuntimeException
	 */
	public static JSONObject readJSONObject(File file, Charset charset) throws IORuntimeException {
		return FileReader.create(file, charset).readJSONObject();
	}

	/**
	 * 读取JSONObject
	 * 
	 * @param file JSON文件
	 * @param charsetName 编码
	 * @return JSONObject
	 * @throws IORuntimeException
	 */
	public static JSONObject readJSONObject(File file, String charsetName) throws IORuntimeException {
		return FileReader.create(file, CharsetUtil.charset(charsetName)).readJSONObject();
	}

	/**
	 * 读取JSONArray
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSONArray
	 * @throws IORuntimeException
	 */
	public static JSONArray readJSONArray(File file, Charset charset) throws IORuntimeException {
		return FileReader.create(file, charset).readJSONArray();
	}

	/**
	 * 读取JSONArray
	 * 
	 * @param file JSON文件
	 * @param charsetName 编码
	 * @return JSONArray
	 * @throws IORuntimeException
	 */
	public static JSONArray readJSONArray(File file, String charsetName) throws IORuntimeException {
		return FileReader.create(file, CharsetUtil.charset(charsetName)).readJSONArray();
	}

	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * 
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IORuntimeException
	 */
	public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IORuntimeException {
		return FileReader.create(file(path), CharsetUtil.charset(charset)).read(readerHandler);
	}

	// -------------------------------------------------------------------------------------------- out start
	/**
	 * 获得一个输出流对象
	 * 
	 * @param file 文件
	 * @return 输出流对象
	 * @throws IOException
	 */
	public static BufferedOutputStream getOutputStream(File file) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(touch(file)));
	}

	/**
	 * 获得一个输出流对象
	 * 
	 * @param path 输出到的文件路径，绝对路径
	 * @return 输出流对象
	 * @throws IOException
	 */
	public static BufferedOutputStream getOutputStream(String path) throws IOException {
		return getOutputStream(touch(path));
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charsetName 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IOException {
		return getWriter(touch(path), Charset.forName(charsetName), isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IOException {
		return getWriter(touch(path), charset, isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param file 输出文件
	 * @param charsetName 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IOException {
		return getWriter(file, Charset.forName(charsetName), isAppend);
	}

	/**
	 * 获得一个带缓存的写入对象
	 * 
	 * @param file 输出文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IOException {
		if (false == file.exists()) {
			file.createNewFile();
		}
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), charset));
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 * 
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IOException
	 */
	public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getWriter(path, charset, isAppend));
	}

	/**
	 * 获得一个打印写入对象，可以有print
	 * 
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IOException
	 */
	public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getWriter(file, charset, isAppend));
	}

	// -------------------------------------------------------------------------------------------- out end

	/**
	 * 将String写入文件，覆盖模式，字符集为UTF-8
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @return 写入的文件
	 * @throws IORuntimeException
	 */
	public static File writeUtf8String(String content, String path) throws IORuntimeException {
		return writeString(content, path, CharsetUtil.UTF_8);
	}

	/**
	 * 将String写入文件，覆盖模式，字符集为UTF-8
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @return 写入的文件
	 * @throws IORuntimeException
	 */
	public static File writeUtf8String(String content, File file) throws IORuntimeException {
		return writeString(content, file, CharsetUtil.UTF_8);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException
	 */
	public static File writeString(String content, String path, String charset) throws IORuntimeException {
		return writeString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，覆盖模式
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @throws IORuntimeException
	 */
	public static File writeString(String content, File file, String charset) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).write(content);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException
	 */
	public static File appendString(String content, String path, String charset) throws IORuntimeException {
		return appendString(content, touch(path), charset);
	}

	/**
	 * 将String写入文件，追加模式
	 * 
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IORuntimeException
	 */
	public static File appendString(String content, File file, String charset) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).append(content);
	}

	/**
	 * 将列表写入文件，覆盖模式
	 * 
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @throws IORuntimeException
	 */
	public static <T> void writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
		writeLines(list, path, charset, false);
	}

	/**
	 * 将列表写入文件，追加模式
	 * 
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @throws IORuntimeException
	 */
	public static <T> void appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
		writeLines(list, path, charset, true);
	}
	
	/**
	 * 将列表写入文件
	 * 
	 * @param list 列表
	 * @param path 文件路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @throws IORuntimeException
	 */
	public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IORuntimeException {
		return writeLines(list, file(path), charset, isAppend);
	}

	/**
	 * 将列表写入文件
	 * 
	 * @param list 列表
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @throws IORuntimeException
	 */
	public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend) throws IORuntimeException {
		return FileWriter.create(file, CharsetUtil.charset(charset)).writeLines(list);
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param data 数据
	 * @param path 目标文件
	 * @return File
	 * @throws IORuntimeException
	 */
	public static File writeBytes(byte[] data, String path) throws IORuntimeException {
		return writeBytes(data, touch(path));
	}

	/**
	 * 写数据到文件中
	 * 
	 * @param dest 目标文件
	 * @param data 数据
	 * @return dest
	 * @throws IORuntimeException
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
	 * @param append 是否追加模式
	 * @return File
	 * @throws IORuntimeException
	 */
	public static File writeBytes(byte[] data, File dest, int off, int len, boolean append) throws IORuntimeException {
		return FileWriter.create(dest).write(data, off, len);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param dest 目标文件
	 * @param in 输入流
	 * @return dest
	 * @throws IORuntimeException
	 */
	public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
		return FileWriter.create(dest).writeFromStream(in);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param in 输入流
	 * @param fullFilePath 文件绝对路径
	 * @return dest
	 * @throws IORuntimeException
	 */
	public static File writeFromStream(InputStream in, String fullFilePath) throws IORuntimeException {
		return writeFromStream(in, touch(fullFilePath));
	}

	/**
	 * 将文件写入流中
	 * 
	 * @param file 文件
	 * @param out 流
	 * @return File
	 * @throws IORuntimeException
	 */
	public static File writeToStream(File file, OutputStream out) throws IORuntimeException {
		return FileReader.create(file).writeToStream(out);
	}

	/**
	 * 将流的内容写入文件<br>
	 * 
	 * @param fullFilePath 文件绝对路径
	 * @param out 输出流
	 * @throws IORuntimeException
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
		if (size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}
