package cn.hutool.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.util.StrUtil;

/**
 * 文件类型判断工具类<br>
 * 此工具根据文件的前几位bytes猜测文件类型，对于文本、zip判断不准确，对于视频、图片类型判断准确
 * 
 * @author Looly
 *
 */
public final class FileTypeUtil {

	private FileTypeUtil() {
	};

	private static final Map<String, String> fileTypeMap;

	static {
		fileTypeMap = new ConcurrentHashMap<>();

//		fileTypeMap.put("ffd8ffe000104a464946", "jpg"); // JPEG (jpg)
		fileTypeMap.put("ffd8ffe", "jpg"); // JPEG (jpg)
		fileTypeMap.put("89504e470d0a1a0a0000", "png"); // PNG (png)
		fileTypeMap.put("47494638396126026f01", "gif"); // GIF (gif)
		fileTypeMap.put("49492a00227105008037", "tif"); // TIFF (tif)
		fileTypeMap.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
		fileTypeMap.put("424d8240090000000000", "bmp"); // 24位位图(bmp)
		fileTypeMap.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
		fileTypeMap.put("41433130313500000000", "dwg"); // CAD (dwg)
		fileTypeMap.put("3c21444f435459504520", "html"); // HTML (html)
		fileTypeMap.put("3c21646f637479706520", "htm"); // HTM (htm)
		fileTypeMap.put("48544d4c207b0d0a0942", "css"); // css
		fileTypeMap.put("696b2e71623d696b2e71", "js"); // js
		fileTypeMap.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
		fileTypeMap.put("38425053000100000000", "psd"); // Photoshop (psd)
		fileTypeMap.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
		fileTypeMap.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel 注意：word、msi 和 excel的文件头一样
		fileTypeMap.put("d0cf11e0a1b11ae10000", "vsd"); // Visio 绘图
		fileTypeMap.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
		fileTypeMap.put("252150532D41646F6265", "ps");
		fileTypeMap.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
		fileTypeMap.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
		fileTypeMap.put("464c5601050000000900", "flv"); // flv与f4v相同
		fileTypeMap.put("00000020667479706d70", "mp4");
		fileTypeMap.put("49443303000000002176", "mp3");
		fileTypeMap.put("000001ba210001000180", "mpg"); //
		fileTypeMap.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
		fileTypeMap.put("52494646e27807005741", "wav"); // Wave (wav)
		fileTypeMap.put("52494646d07d60074156", "avi");
		fileTypeMap.put("4d546864000000060001", "mid"); // MIDI (mid)
		fileTypeMap.put("526172211a0700cf9073", "rar");// WinRAR
		fileTypeMap.put("235468697320636f6e66", "ini");
		fileTypeMap.put("504B03040a0000000000", "jar");
		fileTypeMap.put("504B0304140008000800", "jar");
		fileTypeMap.put("504B0304140006000800", "docx");// docx文件
		fileTypeMap.put("504B0304140006000800", "xlsx");// docx文件
		fileTypeMap.put("D0CF11E0A1B11AE10", "xls");// xls文件
		fileTypeMap.put("504B0304", "zip");
		fileTypeMap.put("4d5a9000030000000400", "exe");// 可执行文件
		fileTypeMap.put("3c25402070616765206c", "jsp");// jsp文件
		fileTypeMap.put("4d616e69666573742d56", "mf");// MF文件
		fileTypeMap.put("3c3f786d6c2076657273", "xml");// xml文件
		fileTypeMap.put("494e5345525420494e54", "sql");// xml文件
		fileTypeMap.put("7061636b616765207765", "java");// java文件
		fileTypeMap.put("406563686f206f66660d", "bat");// bat文件
		fileTypeMap.put("1f8b0800000000000000", "gz");// gz文件
		fileTypeMap.put("6c6f67346a2e726f6f74", "properties");// bat文件
		fileTypeMap.put("cafebabe0000002e0041", "class");// bat文件
		fileTypeMap.put("49545346030000006000", "chm");// bat文件
		fileTypeMap.put("04000000010000001300", "mxp");// bat文件
		fileTypeMap.put("d0cf11e0a1b11ae10000", "wps");// WPS文字wps、表格et、演示dps都是一样的
		fileTypeMap.put("6431303a637265617465", "torrent");
		fileTypeMap.put("6D6F6F76", "mov"); // Quicktime (mov)
		fileTypeMap.put("FF575043", "wpd"); // WordPerfect (wpd)
		fileTypeMap.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
		fileTypeMap.put("2142444E", "pst"); // Outlook (pst)
		fileTypeMap.put("AC9EBD8F", "qdf"); // Quicken (qdf)
		fileTypeMap.put("E3828596", "pwl"); // Windows Password (pwl)
		fileTypeMap.put("2E7261FD", "ram"); // Real Audio (ram)
	}

	/**
	 * 增加文件类型映射<br>
	 * 如果已经存在将覆盖之前的映射
	 * 
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @param extName 文件扩展名
	 * @return 之前已经存在的文件扩展名
	 */
	public static String putFileType(String fileStreamHexHead, String extName) {
		return fileTypeMap.put(fileStreamHexHead.toLowerCase(), extName);
	}

	/**
	 * 移除文件类型映射
	 * 
	 * @param fileStreamHexHead 文件流头部Hex信息
	 * @return 移除的文件扩展名
	 */
	public static String removeFileType(String fileStreamHexHead) {
		return fileTypeMap.remove(fileStreamHexHead.toLowerCase());
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 
	 * @param fileStreamHexHead 文件流头部16进制字符串
	 * @return 文件类型，未找到为<code>null</code>
	 */
	public static String getType(String fileStreamHexHead) {
		for (Entry<String, String> fileTypeEntry : fileTypeMap.entrySet()) {
			if(StrUtil.startWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
				return fileTypeEntry.getValue();
			}
		}
		return null;
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 
	 * @param in {@link InputStream}
	 * @return 类型，文件的扩展名，未找到为<code>null</code>
	 * @throws IORuntimeException 读取流引起的异常
	 */
	public static String getType(InputStream in) throws IORuntimeException {
		return getType(IoUtil.readHex28Upper(in));
	}

	/**
	 * 根据文件流的头部信息获得文件类型
	 * 
	 * @param file 文件 {@link File}
	 * @return 类型，文件的扩展名，未找到为<code>null</code>
	 * @throws IORuntimeException 读取文件引起的异常
	 */
	public static String getType(File file) throws IORuntimeException {
		FileInputStream in = null;
		try {
			in = IoUtil.toStream(file);
			return getType(in);
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
