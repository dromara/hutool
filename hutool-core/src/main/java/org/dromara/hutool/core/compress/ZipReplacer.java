package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipReplacer {

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFile      源文件
	 * @param tarFile      输出文件
	 * @param innerFiles   替换文件在zip中的相对路径
	 * @param replaceFiles 替换文件到系统中到存储路径
	 * @param charset      读取编码格式
	 * @param charsetOut   输出编码格式
	 */
	public static void replace(File srcFile, File tarFile, String[] innerFiles, File[] replaceFiles, Charset charset, Charset charsetOut) {
//		记录zip中是否存在相同路径的文件， 是 更新文件 否，添加文件
		boolean[] updates = new boolean[replaceFiles.length];
		try (ZipFile zipFile = new ZipFile(srcFile, charset);
			 FileOutputStream fos = new FileOutputStream(tarFile);
			 ZipOutputStream zos = new ZipOutputStream(fos);
			 ZipWriter zipWriter = new ZipWriter(zos, charsetOut)) {
			HashMap<String, InputStream> data = new HashMap<>();
			for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
				ZipEntry zipEntryIn = entries.nextElement();
				String zipEntryInName = zipEntryIn.getName();
//				false 未被替换
				boolean update = false;
				for (int i = 0; i < innerFiles.length; i++) {
					update = samePath(zipEntryInName, innerFiles[i]);
//					存在同路径文件，替换
					if (update) {
						updates[i] = true;
						data.put(zipEntryInName, FileUtil.getInputStream(replaceFiles[i]));
						break;
					}
				}
//				不存在同路径文件，直接添加原zipEntry
				if (!update) {
					data.put(zipEntryInName, ZipUtil.getStream(zipFile, zipEntryIn));
				}
			}
//			确认replaceFiles是否替换原zip中文件，没有替换直接添加
			for (int i = 0; i < updates.length; i++) {
				if (!updates[i]) {
//					原zip中不存在同路径文件，添加到制定目录
					data.put(innerFiles[i], FileUtil.getInputStream(replaceFiles[i]));
				}
			}
			for (String key : data.keySet()) {
				zipWriter.add(key, data.get(key));
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, List<File> replaceFiles, Charset charset, Charset charsetOut) {
		File[] files = replaceFiles.toArray(new File[0]);
		replace(srcFile, tarFile, innerFiles, files, charset, charsetOut);
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, String[] replaceFiles, Charset charset, Charset charsetOut) {
		List<File> files = Arrays.stream(replaceFiles).map(FileUtil::file).collect(Collectors.toList());
		replace(srcFile, tarFile, innerFiles, files, charset, charsetOut);
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFile      源文件
	 * @param tarFile      输出文件
	 * @param innerFiles   替换文件在zip中的相对路径
	 * @param replaceFiles 替换文件到系统中到存储路径
	 * @param charset      读取输出编码格式
	 */
	public static void replace(File srcFile, File tarFile, String[] innerFiles, String[] replaceFiles, Charset charset) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, charset, charset);
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, File[] replaceFiles, Charset charset) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, charset, charset);
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, List<File> replaceFiles, Charset charset) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, charset, charset);
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFile      源文件
	 * @param tarFile      输出文件
	 * @param innerFiles   要替换文件在zip中的相对路径
	 * @param replaceFiles 替换文件到系统中到存储路径
	 */
	public static void replace(File srcFile, File tarFile, String[] innerFiles, String[] replaceFiles) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, File[] replaceFiles) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(File srcFile, File tarFile, String[] innerFiles, List<File> replaceFiles) {
		replace(srcFile, tarFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFile      源文件，也是输出路径
	 * @param innerFiles   替换文件在zip中的相对路径
	 * @param replaceFiles 替换文件到系统中到存储路径
	 * @param charset      读取输出编码格式
	 */
	public static void replace(File srcFile, String[] innerFiles, String[] replaceFiles, Charset charset) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, charset);
	}

	public static void replace(File srcFile, String[] innerFiles, File[] replaceFiles, Charset charset) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, charset);
	}

	public static void replace(File srcFile, String[] innerFiles, List<File> replaceFiles, Charset charset) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, charset);
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFile      源文件
	 * @param innerFiles   要替换文件在zip中的相对路径
	 * @param replaceFiles 要替换的文件绝对路径
	 */
	public static void replace(File srcFile, String[] innerFiles, String[] replaceFiles) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(File srcFile, String[] innerFiles, File[] replaceFiles) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(File srcFile, String[] innerFiles, List<File> replaceFiles) {
		replace(srcFile, srcFile, innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFilePath  源文件路径
	 * @param tarFilePath  输出文件路径
	 * @param innerFiles   要替换文件在zip中的相对路径
	 * @param replaceFiles 要替换的文件绝对路径
	 * @param charset      读取编码格式
	 * @param charsetOut   输出编码格式
	 */
	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, String[] replaceFiles, Charset charset, Charset charsetOut) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charsetOut);
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, File[] replaceFiles, Charset charset, Charset charsetOut) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charsetOut);
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, List<File> replaceFiles, Charset charset, Charset charsetOut) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charsetOut);
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFilePath  zip源文件路径
	 * @param tarFilePath  zip输出文件路径
	 * @param innerFiles   要替换文件在zip中的相对路径
	 * @param replaceFiles 要替换的文件绝对路径
	 * @param charset      读取输出编码格式
	 */
	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, String[] replaceFiles, Charset charset) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charset);
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, File[] replaceFiles, Charset charset) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charset);
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, List<File> replaceFiles, Charset charset) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, charset, charset);
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFilePath  源文件路径
	 * @param tarFilePath  输出文件路径
	 * @param innerFiles   替换文件在zip中的相对路径
	 * @param replaceFiles 替换文件到系统中到存储路径
	 */
	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, String[] replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, File[] replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(String srcFilePath, String tarFilePath, String[] innerFiles, List<File> replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(tarFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	/**
	 * 不解压zip，替换文件内容，也可用于转换zip编码格式
	 *
	 * @param srcFilePath  源文件路径，也是输出路径
	 * @param innerFiles   要替换文件在zip中的相对路径
	 * @param replaceFiles 要替换的文件绝对路径
	 */
	public static void replace(String srcFilePath, String[] innerFiles, String[] replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(srcFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(String srcFilePath, String[] innerFiles, File[] replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(srcFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	public static void replace(String srcFilePath, String[] innerFiles, List<File> replaceFiles) {
		replace(FileUtil.file(srcFilePath), FileUtil.file(srcFilePath), innerFiles, replaceFiles, CharsetUtil.defaultCharset());
	}

	/**
	 * 判断路径是否相等
	 *
	 * @param entryPath  路径A
	 * @param targetPath 路径B
	 * @param ignoreCase 是否忽略大小写
	 * @return ture 路径相等
	 */
	public static boolean samePath(String entryPath, String targetPath, boolean ignoreCase) {

		entryPath = entryPath.replaceAll("[/\\\\]", Matcher.quoteReplacement(File.separator));
		targetPath = targetPath.replaceAll("[/\\\\]", Matcher.quoteReplacement(File.separator));
		if (entryPath.startsWith(File.separator)) {
			entryPath = entryPath.substring(1);
		}
		if (targetPath.startsWith(File.separator)) {
			targetPath = targetPath.substring(1);
		}

		if (ignoreCase) {
			return StrUtil.equalsIgnoreCase(entryPath, targetPath);
		} else {
			return StrUtil.equals(entryPath, targetPath);
		}
	}

	public static boolean samePath(String entryPath, String targetPath) {
		return samePath(entryPath, targetPath, true);
	}
}
