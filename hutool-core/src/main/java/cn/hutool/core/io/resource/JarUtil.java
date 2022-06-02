package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Jar文件资源工具类
 *
 * @author Selier
 */
public class JarUtil {


	/**
	 * 获取Jar包内类所在的目录路径
	 *
	 * @param clazz Jar包所在类(任意一个类)
	 * @return Jar包内类所在的目录路径
	 */
	public static String getDir(Class clazz) {
		return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
	}

	/**
	 * 获取Jar包内文件所在的路径
	 *
	 * @param clazz    Jar包所在类(任意一个类)
	 * @param fileName Jar内的文件路径
	 * @return Jar包内类所在的路径
	 */
	public static String getFileDir(Class clazz, String fileName) {
		return clazz.getClassLoader().getResource(fileName).getFile();
	}

	/**
	 * 获取Jar包内文件的资源对象
	 *
	 * @param clazz    类的任意
	 * @param fileName Jar内的文件路径
	 * @return Jar包内文件的资源对象
	 * @throws IOException
	 */
	public static URL getJarUrl(Class clazz, String fileName) throws IOException {
		return clazz.getClassLoader().getResource(fileName);
	}

	/**
	 * 获取Jar包内文件的数据
	 *
	 * @param clazz    Jar包所在类(任意一个类)
	 * @param fileName Jar内的文件路径
	 * @return Jar包内文件的数据
	 */
	public static List<String> readUtf8Lines(Class clazz, String fileName) {
		return readLines(clazz, fileName, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获取Jar包内文件的数据
	 *
	 * @param clazz    Jar包所在类(任意一个类)
	 * @param fileName Jar内的文件路径
	 * @param charset  字符集
	 * @return Jar包内文件的数据
	 */
	public static List<String> readLines(Class clazz, String fileName, Charset charset) {
		URL jarFile;
		try {
			jarFile = getJarUrl(clazz, fileName);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		Assert.notNull(jarFile);
		return FileUtil.readLines(jarFile, charset);
	}

	/**
	 * 获取Jar包内文件的数据
	 *
	 * @param clazz    Jar包所在类(任意一个类)
	 * @param fileName Jar内的文件路径
	 * @return Jar包内文件的数据
	 */
	public static String readString(Class clazz, String fileName) {
		return readString(clazz, fileName, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获取Jar包内文件的数据
	 *
	 * @param clazz    Jar包所在类(任意一个类)
	 * @param fileName Jar内的文件路径
	 * @param charset  字符集
	 * @return Jar包内文件的数据
	 */
	public static String readString(Class clazz, String fileName, Charset charset) {
		URL jarFile;
		try {
			jarFile = getJarUrl(clazz, fileName);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		Assert.notNull(jarFile);
		System.out.println(jarFile.getFile());
		return FileUtil.readString(jarFile, charset);
	}

}
