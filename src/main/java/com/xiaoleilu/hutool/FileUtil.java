package com.xiaoleilu.hutool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 文件工具类
 * @author xiaoleilu
 *
 */
public class FileUtil {
	
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
	 * @param path 目录绝对路径或者相对路径
	 * @return 文件列表（包含目录）
	 */
	public static File[] ls(String path) {
		if(path == null) {
			return null;
		}
		path = getAbsolutePath(path);
		
		File file = file(path);
		if(file.isDirectory()) {
			return file.listFiles();
		}
		throw new UtilException(StrUtil.format("Path [{}] is not directory!", path));
	}
	
	/**
	 * 目录是否为空
	 * @param file 目录
	 * @return 是否为空，当提供非目录时，返回false
	 */
	public static boolean isEmpty(File file) {
		if(null == file){
			return true;
		}
		
		if(file.isDirectory()){
			String[] subFiles = file.list();
			if(CollectionUtil.isEmpty(subFiles)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param file 当前遍历文件
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件
	 */
	public static List<File> loopFiles(File file, FileFilter fileFilter) {
		List<File> fileList = new ArrayList<File>();
		if(file == null){
			return fileList;
		}else if(file.exists() == false){
			return fileList;
		}
		
		if (file.isDirectory()) {
			for (File tmp : file.listFiles()) {
				fileList.addAll(loopFiles(tmp, fileFilter));
			}
		}else{
			if(fileFilter != null && fileFilter.accept(file)){
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
	 * @param path 相对ClassPath的目录或者绝对路径目录
	 * @return 文件路径列表（如果是jar中的文件，则给定类似.jar!/xxx/xxx的路径）
	 * @throws IOException
	 */
	public static List<String> listFileNames(String path) {
		if(path == null) {
			return null;
		}
		path = getAbsolutePath(path);
		if(path.endsWith(String.valueOf(UNIX_SEPARATOR)) == false) {
			path = path + UNIX_SEPARATOR;
		}
		
		List<String> paths = new ArrayList<String>();
		int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
		try {
			if(index == -1) {
				//普通目录路径
				File[] files = ls(path);
				for (File file : files) {
					if(file.isFile()) {
						paths.add(file.getName());
					}
				}
			}else {
				//jar文件中的路径
				index = index + FileUtil.JAR_FILE_EXT.length();
				final String jarPath = path.substring(0, index);
				final String subPath = path.substring(index + 2);
				for (JarEntry entry : Collections.list(new JarFile(jarPath).entries())) {
					final String name = entry.getName();
					if(name.startsWith(subPath)) {
						String nameSuffix = StrUtil.removePrefix(name, subPath);
						if(nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
							paths.add(nameSuffix);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Can not read file path of [{}]", path), e);
		}
		return paths;
	}
	
	/**
	 * 创建File对象
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(String path) {
		if(StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(path);
	}
	
	/**
	 * 创建File对象
	 * @param parent 父目录
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(String parent, String path) {
		if(StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}
	
	/**
	 * 创建File对象
	 * @param parent 父文件对象
	 * @param path 文件路径
	 * @return File
	 */
	public static File file(File parent, String path) {
		if(StrUtil.isBlank(path)) {
			throw new NullPointerException("File path is blank!");
		}
		return new File(parent, path);
	}
	
	/**
	 * 创建File对象
	 * @param uri 文件URI
	 * @return File
	 */
	public static File file(URI uri) {
		if(uri == null) {
			throw new NullPointerException("File uri is null!");
		}
		return new File(uri);
	}
	
	/**
	 * 创建文件，如果这个文件存在，直接返回这个文件
	 * @param fullFilePath 文件的全路径，使用POSIX风格
	 * @return 文件，若路径为null，返回null
	 * @throws IOException
	 */
	public static File touch(String fullFilePath) throws IOException {
		if(fullFilePath == null) {
			return null;
		}
		return touch(file(fullFilePath));
	}
	
	/**
	 * 创建文件，如果这个文件存在，直接返回这个文件
	 * @param file 文件对象
	 * @return 文件，若路径为null，返回null
	 * @throws IOException
	 */
	public static File touch(File file) throws IOException {
		if(null == file){
			return null;
		}
		
		file.getParentFile().mkdirs();
		if(false == file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	/**
	 * 删除文件或者文件夹
	 * @param fullFileOrDirPath 文件或者目录的路径
	 * @return 成功与否
	 * @throws IOException
	 */
	public static boolean del(String fullFileOrDirPath) throws IOException {
		return del(file(fullFileOrDirPath));
	}
	
	/**
	 * 删除文件或者文件夹
	 * @param file 文件对象
	 * @return 成功与否
	 * @throws IOException
	 */
	public static boolean del(File file) throws IOException {
		if(file == null || file.exists() == false) {
			return true;
		}
		
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (File childFile : files) {
				boolean isOk = del(childFile);
				if(isOk == false) {
					//删除一个出错则本次删除任务失败
					return false;
				}
			}
		}
		return file.delete();
	}
	
	/**
	 * 创建文件夹，如果存在直接返回此文件夹
	 * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
	 * @return 创建的目录
	 */
	public static File mkdir(String dirPath){
		if(dirPath == null) {
			return null;
		}
		File dir = file(dirPath);
		if(false == dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].tmp
	 * @param dir 临时文件创建的所在目录
	 * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
	 * @return 临时文件
	 * @throws IOException
	 */
	public static File createTempFile(File dir) throws IOException {
		return createTempFile("hutool", null, dir, true);
	}
	
	/**
	 * 创建临时文件<br>
	 * 创建后的文件名为 prefix[Randon].tmp
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
	 * 创建后的文件名为 prefix[Randon].suffix
	 * From com.jodd.io.FileUtil
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
				if(isReCreat) {
					file.delete();
					file.createNewFile();
				}
				return file;
			} catch (IOException ioex) {	// fixes java.io.WinNTFileSystem.createFileExclusively access denied
				if (++exceptionsCount >= 50) {
					throw ioex;
				}
			}
		}
	}
	
	/**
	 * 复制文件<br>
	 * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
	 * @param src 源文件
	 * @param dest 目标文件或目录
	 * @param isOverride 是否覆盖目标文件
	 * @throws IOException
	 */
	public static void copy(File src, File dest, boolean isOverride) throws IOException {
		//check
		if (! src.exists()) {
			throw new FileNotFoundException("File not exist: " + src);
		}
		if (! src.isFile()) {
			throw new IOException("Not a file:" + src);
		}
		if (equals(src, dest)) {
			throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
		}
		
		if (dest.exists()) {
			if (dest.isDirectory()) {
				dest = new File(dest, src.getName());
			}
			if (dest.exists() && ! isOverride) {
				throw new IOException("File already exist: " + dest);
			}
		}

		// do copy file
		FileInputStream input = new FileInputStream(src);
		FileOutputStream output = new FileOutputStream(dest);
		try {
			IoUtil.copy(input, output);
		} finally {
			close(output);
			close(input);
		}

		if (src.length() != dest.length()) {
			throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
		}
	}
	
	/**
	 * 移动文件或者目录
	 * @param src 源文件或者目录
	 * @param dest 目标文件或者目录
	 * @param isOverride 是否覆盖目标
	 * @throws IOException
	 */
	public static void move(File src, File dest, boolean isOverride) throws IOException {
		//check
		if (! src.exists()) {
			throw new FileNotFoundException("File already exist: " + src);
		}
		if (dest.exists()) {
			if (! isOverride) {
				throw new IOException("File already exist: " + dest);
			}
			dest.delete();
		}
		
		//来源为文件夹，目标为文件
		if(src.isDirectory() && dest.isFile()) {
			throw new IOException(StrUtil.format("Can not move directory [{}] to file [{}]", src, dest));
		}
		
		//来源为文件，目标为文件夹
		if(src.isFile() && dest.isDirectory()) {
			dest = new File(dest, src.getName());
		}

		if (src.renameTo(dest) == false) {
			//在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
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
	 * @param path 相对路径
	 * @param baseClass 相对路径所相对的类
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path, Class<?> baseClass){
		if(path == null) {
			path = StrUtil.EMPTY;
		}
		if(baseClass == null) {
			return getAbsolutePath(path);
		}
//		return baseClass.getResource(path).getPath();
		return StrUtil.removePrefix(PATH_FILE_PRE, baseClass.getResource(path).getPath());
	}
	
	/**
	 * 获取绝对路径，相对于classes的根目录<br>
	 * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/
	 * @param path 相对路径
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path){
		if(path == null) {
			path = StrUtil.EMPTY;
		}else {
			path = normalize(path);
			
			if(path.startsWith("/") || path.matches("^[a-zA-Z]:/.*")){
				//给定的路径已经是绝对路径了
				return path;
			}
			
		}
		
		ClassLoader classLoader = ClassUtil.getClassLoader();
		URL url = classLoader.getResource(path);
		String reultPath= url != null ? url.getPath() : classLoader.getResource(StrUtil.EMPTY).getPath() + path;
		return StrUtil.removePrefix(reultPath, PATH_FILE_PRE);
	}
	
	/**
	 * 获取标准的绝对路径
	 * @param file 文件
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(File file){
		if(file == null){
			return null;
		}
		
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

	/**
	 * 文件是否存在
	 * @param path 文件路径
	 * @return 是否存在
	 */
	public static boolean isExist(String path){
		return  file(path).exists();
	}
	
	/**
	 * 关闭
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable){
		if(closeable == null) return;
		try {
			closeable.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * 检查两个文件是否是同一个文件
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
	 * 获得输入流
	 * @param file 文件
	 * @return 输入流
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getInputStream(File file) throws FileNotFoundException{
		return new BufferedInputStream(new FileInputStream(file));
	}
	
	/**
	 * 获得输入流
	 * @param path 文件路径
	 * @return 输入流
	 * @throws FileNotFoundException
	 */
	public static BufferedInputStream getInputStream(String path) throws FileNotFoundException{
		return getInputStream(file(path));
	}
	
	/**
	 * 获得一个带缓存的写入对象
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getBufferedWriter(String path, String charset, boolean isAppend) throws IOException {
		return getBufferedWriter(touch(path), charset, isAppend);
	}
	
	/**
	 * 获得一个带缓存的写入对象
	 * @param file 输出文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedWriter getBufferedWriter(File file, String charset, boolean isAppend) throws IOException {
		if(false == file.exists()){
			file.createNewFile();
		}
		return new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(file, isAppend), charset)
		);
	}
	
	/**
	 * 获得一个打印写入对象，可以有print
	 * @param path 输出路径，绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IOException
	 */
	public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getBufferedWriter(path, charset, isAppend));
	}
	
	/**
	 * 获得一个打印写入对象，可以有print
	 * @param file 文件
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @return 打印对象
	 * @throws IOException
	 */
	public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IOException {
		return new PrintWriter(getBufferedWriter(file, charset, isAppend));
	}
	
	/**
	 * 获得一个输出流对象
	 * @param file 文件
	 * @return 输出流对象
	 * @throws IOException
	 */
	public static BufferedOutputStream getOutputStream(File file) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}

	/**
	 * 获得一个输出流对象
	 * @param path 输出到的文件路径，绝对路径
	 * @return 输出流对象
	 * @throws IOException
	 */
	public static BufferedOutputStream getOutputStream(String path) throws IOException {
		return getOutputStream(touch(path));
	}
	
	/**
	 * 获得一个文件读取器
	 * @param file 文件
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(File file, String charset) throws IOException{
		return IoUtil.getReader(getInputStream(file), charset);
	}
	
	/**
	 * 获得一个文件读取器
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IOException
	 */
	public static BufferedReader getReader(String path, String charset) throws IOException{
		return getReader(file(path), charset);
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param path	文件路径
	 * @param charset	字符集
	 * @param collection	集合
	 * @return	文件中的每行内容的集合
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IOException{
		return readLines(file(path), charset, collection);
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param file	文件路径
	 * @param charset	字符集
	 * @param collection	集合
	 * @return	文件中的每行内容的集合
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IOException{
		BufferedReader reader = null;
		try {
			reader = getReader(file, charset);
			String line;
			while(true){
				line = reader.readLine();
				if(line == null) break;
				collection.add(line);
			}
			return collection;
		}finally {
			close(reader);
		}
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param url	文件的URL
	 * @param charset	字符集
	 * @param collection	集合
	 * @return	文件中的每行内容的集合
	 * @throws IOException
	 */
	public static <T extends Collection<String>> T readLines(URL url, String charset, T collection) throws IOException{
		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.getLines(in, charset, collection);
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param url	文件的URL
	 * @param charset	字符集
	 * @return	文件中的每行内容的集合List
	 * @throws IOException
	 */
	public static List<String> readLines(URL url, String charset) throws IOException {
		return readLines(url, charset, new ArrayList<String>());
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param path	文件路径
	 * @param charset	字符集
	 * @return	文件中的每行内容的集合List
	 * @throws IOException
	 */
	public static List<String> readLines(String path, String charset) throws IOException {
		return readLines(path, charset, new ArrayList<String>());
	}
	
	/**
	 * 从文件中读取每一行数据
	 * @param file	文件
	 * @param charset	字符集
	 * @return	文件中的每行内容的集合List
	 * @throws IOException
	 */
	public static List<String> readLines(File file, String charset) throws IOException {
		return readLines(file, charset, new ArrayList<String>());
	}
	
	/**
	 * 按照给定的readerHandler读取文件中的数据
	 * @param readerHandler Reader处理类
	 * @param path 文件的绝对路径
	 * @param charset 字符集
	 * @return 从文件中load出的数据
	 * @throws IOException
	 */
	public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IOException {
		BufferedReader reader = null;
		T result = null;
		try {
			reader = getReader(path, charset);
			result = readerHandler.handle(reader);
		} catch (IOException e) {
			throw new IOException(e);
		}finally {
			close(reader);
		}
		return result;
	}
	
	/**
	 * 获得文件的扩展名
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String getExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		int index = fileName.lastIndexOf(StrUtil.DOT);
		if (index == -1) {
			return StrUtil.EMPTY;
		} else {
			String ext = fileName.substring(index + 1);
			//扩展名中不能包含路径相关的符号
			return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StrUtil.EMPTY : ext;
		}
	}
	
	/**
	 * 获得最后一个文件路径分隔符的位置
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
	 * 将String写入文件，覆盖模式
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IOException
	 */
	public static File writeString(String content, String path, String charset) throws IOException {
		return writeString(content, touch(path), charset);
	}
	
	/**
	 * 将String写入文件，覆盖模式
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @throws IOException
	 */
	public static File writeString(String content, File file, String charset) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(file, charset, false);
			writer.print(content);
		}finally {
			close(writer);
		}
		return file;
	}
	
	/**
	 * 将String写入文件，追加模式
	 * @param content 写入的内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IOException
	 */
	public static File appendString(String content, String path, String charset) throws IOException {
		return appendString(content, touch(path), charset);
	}
	
	/**
	 * 将String写入文件，追加模式
	 * @param content 写入的内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 写入的文件
	 * @throws IOException
	 */
	public static File appendString(String content, File file, String charset) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(file, charset, true);
			writer.print(content);
		}finally {
			close(writer);
		}
		return file;
	}
	
	/**
	 * 读取文件内容
	 * @param file 文件
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String readString(File file, String charset) throws IOException {
		return new String(readBytes(file), charset);
	}
	
	/**
	 * 读取文件内容
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String readString(String path, String charset) throws IOException {
		return readString(file(path), charset);
	}
	
	/**
	 * 读取文件内容
	 * @param url 文件URL
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String readString(URL url, String charset) throws IOException {
		if(url == null) {
			throw new RuntimeException("Empty url provided!");
		}
		
		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.getString(in, charset);
		} finally {
			close(in);
		}
	}
	
	/**
	 * 将列表写入文件，覆盖模式
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @throws IOException
	 */
	public static <T> void writeLines(Collection<T> list, String path, String charset) throws IOException {
		writeLines(list, path, charset, false);
	}
	
	/**
	 * 将列表写入文件，追加模式
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @throws IOException
	 */
	public static <T> void appendLines(Collection<T> list, String path, String charset) throws IOException {
		writeLines(list, path, charset, true);
	}
	
	/**
	 * 将列表写入文件
	 * @param list 列表
	 * @param path 绝对路径
	 * @param charset 字符集
	 * @param isAppend 是否追加
	 * @throws IOException
	 */
	public static <T> void writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IOException {
		PrintWriter writer = null;
		try {
			writer = getPrintWriter(path, charset, isAppend);
			for (T t : list) {
				if(t != null) {
					writer.println(t.toString());
				}
			}
		}finally {
			close(writer);
		}
	}
	
	//-------------------------------------------------------------------------- Write and read bytes
	/**
	 * 写数据到文件中
	 * @param data 数据
	 * @param path 目标文件
	 * @throws IOException
	 */
	public static void writeBytes(byte[] data, String path) throws IOException {
		writeBytes(touch(path), data);
	}
	
	/**
	 * 写数据到文件中
	 * @param dest 目标文件
	 * @param data 数据
	 * @throws IOException
	 */
	public static void writeBytes(File dest, byte[] data) throws IOException {
		writeBytes(dest, data, 0, data.length, false);
	}
	
	/**
	 * 写入数据到文件
	 * @param dest 目标文件
	 * @param data 数据
	 * @param off 
	 * @param len
	 * @param append
	 * @throws IOException
	 */
	public static void writeBytes(File dest, byte[] data, int off, int len, boolean append) throws IOException {
		if (dest.exists() == true) {
			if (dest.isFile() == false) {
				throw new IOException("Not a file: " + dest);
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest, append);
			out.write(data, off, len);
		} finally {
			close(out);
		}
	}
	
	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过Integer.MAX_VALUE
	 * @param file 文件
	 * @return 字节码
	 * @throws IOException
	 */
	public static byte[] readBytes(File file) throws IOException {
		//check
		if (! file.exists()) {
			throw new FileNotFoundException("File not exist: " + file);
		}
		if (! file.isFile()) {
			throw new IOException("Not a file:" + file);
		}
		
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IOException("File is larger then max array size");
		}

		byte[] bytes = new byte[(int) len];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(bytes);
		}finally {
			close(in);
		}

		return bytes;
	}
	
	// ---------------------------------------------------------------- stream
	/**
	 * 将流的内容写入文件<br>
	 * @param dest 目标文件
	 * @param in 输入流
	 * @throws IOException
	 */
	public static void writeStream(File dest, InputStream in) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest);
			IoUtil.copy(in, out);
		} finally {
			close(out);
		}
	}
	
	/**
	 * 将流的内容写入文件<br>
	 * @param fullFilePath 文件绝对路径
	 * @param in 输入流
	 * @throws IOException
	 */
	public static void writeStream(String fullFilePath, InputStream in) throws IOException {
		writeStream(touch(fullFilePath), in);
	}
	
	/**
	 * 判断文件是否被改动<br>
	 * 如果文件对象为 null 或者文件不存在，被视为改动
	 * @param file 文件对象
	 * @param lastModifyTime 上次的改动时间
	 * @return 是否被改动
	 */
	public static boolean isModifed(File file, long lastModifyTime) {
		if(null == file || false == file.exists()) {
			return true;
		}
		return file.lastModified() != lastModifyTime;
	}
	
	/**
	 * 修复路径<br>
	 * 1. 统一用 / <br>
	 * 2. 多个 / 转换为一个
	 * @param path 原路径
	 * @return 修复后的路径
	 */
	public static String normalize(String path){
		return path.replaceAll("[/\\\\]{1,}", "/");
	}
	
	/**
	 * 获得相对子路径
	 * @param rootDir 绝对父路径
	 * @param filePath 文件路径
	 * @return 相对子路径
	 */
	public static String subPath(String rootDir, String filePath){
		return subPath(rootDir, file(filePath));
	}
	
	/**
	 * 获得相对子路径
	 * @param rootDir 绝对父路径
	 * @param file 文件
	 * @return 相对子路径
	 */
	public static String subPath(String rootDir, File file){
		if(StrUtil.isEmpty(rootDir)) {
		}
		
		String subPath = null;
		try {
			subPath = file.getCanonicalPath();
		} catch (IOException e) {
			throw new UtilException(e);
		}
		
		if(StrUtil.isNotEmpty(rootDir) && StrUtil.isNotEmpty(subPath)) {
			rootDir = normalize(rootDir);
			subPath = normalize(subPath);
			
			if (subPath != null && subPath.toLowerCase().startsWith(subPath.toLowerCase())) {
				subPath = subPath.substring(rootDir.length() + 1);
			}
		}
		return subPath;
	}
	
	//-------------------------------------------------------------------------- Interface
	/**
	 * Reader处理接口
	 * @author Luxiaolei
	 *
	 * @param <T>
	 */
	public interface ReaderHandler<T> {
		public T handle(BufferedReader reader) throws IOException;
	}
	
	//---------------------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------------------- Private method end	
}
