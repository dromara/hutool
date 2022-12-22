package cn.hutool.core.io;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 文件类型判断工具类
 *
 * <p>此工具根据文件的前几位bytes猜测文件类型，对于文本、zip判断不准确，对于视频、图片类型判断准确</p>
 *
 * <p>需要注意的是，xlsx、docx等Office2007格式，全部识别为zip，因为新版采用了OpenXML格式，这些格式本质上是XML文件打包为zip</p>
 *
 * @author Looly
 */
public class FileTypeUtil {

	private static final Map<String, String> FILE_TYPE_MAP;

	static {
		FILE_TYPE_MAP = new ConcurrentSkipListMap<>((s1, s2) -> {
			int len1 = s1.length();
			int len2 = s2.length();
			if (len1 == len2) {
				return s1.compareTo(s2);
			} else {
				return len2 - len1;
			}
		});

		FILE_TYPE_MAP.put("ffd8ff", "jpg"); // JPEG (jpg)
		FILE_TYPE_MAP.put("89504e47", "png"); // PNG (png)
		FILE_TYPE_MAP.put("4749463837", "gif"); // GIF (gif)
		FILE_TYPE_MAP.put("4749463839", "gif"); // GIF (gif)
		FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
		// https://github.com/sindresorhus/file-type/blob/main/core.js#L90
		FILE_TYPE_MAP.put("424d", "bmp"); // 位图(bmp)
		FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
		FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
		FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
		FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
		FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
		FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
		FILE_TYPE_MAP.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
		FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
		FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
		FILE_TYPE_MAP.put("0000001C66747970", "mp4");
		FILE_TYPE_MAP.put("00000020667479706", "mp4");
		FILE_TYPE_MAP.put("00000018667479706D70", "mp4");
		FILE_TYPE_MAP.put("49443303000000002176", "mp3");
		FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
		FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
		FILE_TYPE_MAP.put("52494646e27807005741", "wav"); // Wave (wav)
		FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
		FILE_TYPE_MAP.put("4d546864000000060001", "mid"); // MIDI (mid)
		FILE_TYPE_MAP.put("526172211a0700cf9073", "rar"); // WinRAR
		FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
		FILE_TYPE_MAP.put("504B03040a0000000000", "jar");
		FILE_TYPE_MAP.put("504B0304140008000800", "jar");
		// MS Excel 注意：word、msi 和 excel的文件头一样
		FILE_TYPE_MAP.put("d0cf11e0a1b11ae10", "xls");
		FILE_TYPE_MAP.put("504B0304", "zip");
		FILE_TYPE_MAP.put("4d5a9000030000000400", "exe"); // 可执行文件
		FILE_TYPE_MAP.put("3c25402070616765206c", "jsp"); // jsp文件
		FILE_TYPE_MAP.put("4d616e69666573742d56", "mf"); // MF文件
		FILE_TYPE_MAP.put("7061636b616765207765", "java"); // java文件
		FILE_TYPE_MAP.put("406563686f206f66660d", "bat"); // bat文件
		FILE_TYPE_MAP.put("1f8b0800000000000000", "gz"); // gz文件
		FILE_TYPE_MAP.put("cafebabe0000002e0041", "class"); // class文件
		FILE_TYPE_MAP.put("49545346030000006000", "chm"); // chm文件
		FILE_TYPE_MAP.put("04000000010000001300", "mxp"); // mxp文件
		FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
		FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
		FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
		FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
		FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
		FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
		FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
		FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
		// https://stackoverflow.com/questions/45321665/magic-number-for-google-image-format
		FILE_TYPE_MAP.put("52494646", "webp");
	}

	/**
	 * 增加文件类型映射<br>
	 * 如果已经存在将覆盖之前的映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @param extName           文件扩展名
	 * @return 之前已经存在的文件扩展名
	 */
	public static String putFileType(String fileStreamHexHead, String extName) {
		return FILE_TYPE_MAP.put(fileStreamHexHead, extName);
	}

	/**
	 * 移除文件类型映射
	 *
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @return 移除的文件扩展名
	 */
	public static String removeFileType(String fileStreamHexHead) {
		return FILE_TYPE_MAP.remove(fileStreamHexHead);
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * @param fileStreamHexHead 文件流头部16进制字符串
	 * @return 文件类型，未找到为{@code null}
	 */
	public static String getType(String fileStreamHexHead) {
		for (Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
			if (StrUtil.startWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
				return fileTypeEntry.getValue();
			}
		}
		return null;
	}

	/**
	 * 根据文件流的头部信息获得文件类型<br>
	 * 注意此方法会读取头部28个bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想服用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * @param in {@link InputStream}
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(InputStream in) throws IORuntimeException {
		return getType(IoUtil.readHex28Upper(in));
	}


	/**
	 * 根据文件流的头部信息获得文件类型
	 * 注意此方法会读取头部28个bytes，造成此流接下来读取时缺少部分bytes<br>
	 * 因此如果想服用此流，流需支持{@link InputStream#reset()}方法。
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为docx、xlsx、pptx、jar、war、ofd头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param in       {@link InputStream}
	 * @param filename 文件名
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(InputStream in, String filename) {
		String typeName = getType(in);

		if (null == typeName) {
			// 未成功识别类型，扩展名辅助识别
			typeName = FileUtil.extName(filename);
		} else if ("xls".equals(typeName)) {
			// xls、doc、msi的头一样，使用扩展名辅助判断
			final String extName = FileUtil.extName(filename);
			if ("doc".equalsIgnoreCase(extName)) {
				typeName = "doc";
			} else if ("msi".equalsIgnoreCase(extName)) {
				typeName = "msi";
			} else if ("ppt".equalsIgnoreCase(extName)) {
				typeName = "ppt";
			}
		} else if ("zip".equals(typeName)) {
			// zip可能为docx、xlsx、pptx、jar、war、ofd等格式，扩展名辅助判断
			final String extName = FileUtil.extName(filename);
			if ("docx".equalsIgnoreCase(extName)) {
				typeName = "docx";
			} else if ("xlsx".equalsIgnoreCase(extName)) {
				typeName = "xlsx";
			} else if ("pptx".equalsIgnoreCase(extName)) {
				typeName = "pptx";
			} else if ("jar".equalsIgnoreCase(extName)) {
				typeName = "jar";
			} else if ("war".equalsIgnoreCase(extName)) {
				typeName = "war";
			} else if ("ofd".equalsIgnoreCase(extName)) {
				typeName = "ofd";
			} else if ("apk".equalsIgnoreCase(extName)) {
				typeName = "apk";
			}
		} else if ("jar".equals(typeName)) {
			// wps编辑过的.xlsx文件与.jar的开头相同,通过扩展名判断
			final String extName = FileUtil.extName(filename);
			if ("xlsx".equalsIgnoreCase(extName)) {
				typeName = "xlsx";
			} else if ("docx".equalsIgnoreCase(extName)) {
				// issue#I47JGH
				typeName = "docx";
			} else if ("pptx".equalsIgnoreCase(extName)) {
				// issue#I5A0GO
				typeName = "pptx";
			} else if ("zip".equalsIgnoreCase(extName)) {
				typeName = "zip";
			} else if ("apk".equalsIgnoreCase(extName)) {
				typeName = "apk";
			}
		}
		return typeName;
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 *
	 * <pre>
	 *     1、无法识别类型默认按照扩展名识别
	 *     2、xls、doc、msi头信息无法区分，按照扩展名区分
	 *     3、zip可能为docx、xlsx、pptx、jar、war头信息无法区分，按照扩展名区分
	 * </pre>
	 *
	 * @param file 文件 {@link File}
	 * @return 类型，文件的扩展名，未找到为{@code null}
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getType(File file) throws IORuntimeException {
		FileInputStream in = null;
		try {
			in = IoUtil.toStream(file);
			return getType(in, file.getName());
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 通过路径获得文件类型
	 *
	 * @param path 路径，绝对路径或相对ClassPath的路径
	 * @return 类型
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getTypeByPath(String path) throws IORuntimeException {
		return getType(FileUtil.file(path));
	}
}
