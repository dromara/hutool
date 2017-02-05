package com.xiaoleilu.hutool.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FastByteArrayOutputStream;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * 压缩工具类
 * 
 * @author Looly
 *
 */
public class ZipUtil {
	
	private ZipUtil() {}

	/**
	 * 打包到当前目录
	 * 
	 * @param srcPath 源文件路径
	 * @return 打包好的压缩文件
	 * @throws IOException
	 */
	public static File zip(String srcPath) throws IOException {
		return zip(FileUtil.file(srcPath));
	}

	/**
	 * 打包到当前目录
	 * 
	 * @param srcFile 源文件或目录
	 * @return 打包好的压缩文件
	 * @throws IOException
	 */
	public static File zip(File srcFile) throws IOException {
		File zipFile = FileUtil.file(srcFile.getParentFile(), FileUtil.mainName(srcFile) + ".zip");
		zip(zipFile, false, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 不包含被打包目录
	 * 
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @return 压缩好的Zip文件
	 * @throws IOException
	 */
	public static File zip(String srcPath, String zipPath) throws IOException {
		return zip(srcPath, zipPath, false);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @throws IOException
	 * @throws Exception
	 */
	public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws IOException {
		File srcFile = FileUtil.file(srcPath);
		File zipFile = FileUtil.file(zipPath);
		zip(zipFile, withSrcDir, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @param srcFiles 要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @throws IOException
	 */
	public static void zip(File zipFile, boolean withSrcDir, File... srcFiles) throws IOException {
		validateFiles(zipFile, srcFiles);

		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new CheckedOutputStream(FileUtil.getOutputStream(zipFile), new CRC32()));
			for (File srcFile : srcFiles) {
				// 如果只是压缩一个文件，则需要截取该文件的父目录
				String srcRootDir = srcFile.getCanonicalPath();
				if (srcFile.isFile() || withSrcDir) {
					srcRootDir = srcFile.getParent();
				}
				// 调用递归压缩方法进行目录或文件压缩
				zip(out, srcRootDir, srcFile);
				out.flush();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			IoUtil.close(out);
		}
	}

	/**
	 * 解压到文件名相同的目录中
	 * 
	 * @param zipFile 压缩文件
	 * @return 解压的目录
	 * @throws IOException
	 */
	public static File unzip(File zipFile) throws IOException {
		return unzip(zipFile, FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile)));
	}

	/**
	 * 解压到文件名相同的目录中
	 * 
	 * @param zipFilePath 压缩文件路径
	 * @return 解压的目录
	 * @throws IOException
	 */
	public static File unzip(String zipFilePath) throws IOException {
		return unzip(FileUtil.file(zipFilePath));
	}

	/**
	 * 解压
	 * 
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir 解压到的目录
	 * @return 解压的目录
	 * @throws IOException
	 */
	public static File unzip(String zipFilePath, String outFileDir) throws IOException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir));
	}

	/**
	 * 解压
	 * 
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static File unzip(File zipFile, File outFile) throws IOException {
		final ZipFile zipFileObj = new ZipFile(zipFile);
		final Enumeration<ZipEntry> em = (Enumeration<ZipEntry>) zipFileObj.entries();
		ZipEntry zipEntry = null;
		File outItemFile = null;
		while (em.hasMoreElements()) {
			zipEntry = em.nextElement();
			outItemFile = new File(outFile, zipEntry.getName());
			if (zipEntry.isDirectory()) {
				outItemFile.mkdirs();
			} else {
				FileUtil.touch(outItemFile);
				copy(zipFileObj, zipEntry, outItemFile);
			}
		}
		IoUtil.close(zipFileObj);
		return outFile;
	}
	
	//----------------------------------------------------------------------------- Gzip
	/**
	 * Gzip压缩处理
	 * 
	 * @param content 被压缩的字符串
	 * @param charset 编码
	 * @return 压缩后的字节流
	 * @throws IOException
	 */
	public static byte[] gzip(String content, String charset) throws IOException {
		return gzip(StrUtil.bytes(content, charset));
	}
	
	/**
	 * Gzip压缩处理
	 * 
	 * @param val 被压缩的字节流
	 * @return 压缩后的字节流
	 * @throws IOException
	 */
	public static byte[] gzip(byte[] val) throws IOException {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(val.length);
		GZIPOutputStream gos = null;
		try {
			gos = new GZIPOutputStream(bos);
			gos.write(val, 0, val.length);
			gos.finish();
			gos.flush();
			val = bos.toByteArray();
		} finally {
			IoUtil.close(gos);
		}
		return val;
	}
	
	/**
	 * Gzip压缩文件
	 * 
	 * @param file 被压缩的文件
	 * @return 压缩后的字节流
	 * @throws IOException
	 */
	public static byte[] gzip(File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
		GZIPOutputStream gos = null;
		BufferedInputStream in;
		try {
			gos = new GZIPOutputStream(bos);
			in = FileUtil.getInputStream(file);
			IoUtil.copy(in, gos);
			return bos.toByteArray();
		} finally {
			IoUtil.close(gos);
		}
	}
	
	/**
	 * Gzip解压缩处理
	 * 
	 * @param buf 压缩过的字节流
	 * @param charset 编码
	 * @return 解压后的字符串
	 * @throws IOException
	 */
	public static String unGzip(byte[] buf, String charset) throws IOException {
		return StrUtil.str(unGzip(buf), charset);
	}
	
	/**
	 * Gzip解压处理
	 * 
	 * @param buf buf
	 * @return bytes
	 * @throws IOException
	 */
	public static byte[] unGzip(byte[] buf) throws IOException {
		GZIPInputStream gzi = null;
		ByteArrayOutputStream bos = null;
		try {
			gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
			bos = new ByteArrayOutputStream(buf.length);
			IoUtil.copy(gzi, bos);
			buf = bos.toByteArray();
		} finally {
			IoUtil.close(gzi);
		}
		return buf;
	}

	// ---------------------------------------------------------------------------------------------- Private method start
	/**
	 * 递归压缩文件夹
	 * 
	 * @param out 压缩文件存储对象
	 * @param srcRootDir 压缩文件夹根目录的子路径
	 * @param file 当前递归压缩的文件或目录对象
	 * @throws Exception
	 */
	private static void zip(ZipOutputStream out, String srcRootDir, File file) {
		if (file == null) {
			return;
		}

		if (file.isFile()) {// 如果是文件，则直接压缩该文件
			final String subPath = FileUtil.subPath(srcRootDir, file); // 获取文件相对于压缩文件夹根目录的子路径
			BufferedInputStream in = null;
			try {
				out.putNextEntry(new ZipEntry(subPath));
				in = FileUtil.getInputStream(file);
				IoUtil.copy(in, out);
			} catch (IOException e) {
				throw new UtilException(e);
			} finally {
				IoUtil.close(in);
				closeEntry(out);
			}
		} else {// 如果是目录，则压缩压缩目录中的文件或子目录
			for (File childFile : file.listFiles()) {
				zip(out, srcRootDir, childFile);
			}
		}
	}

	/**
	 * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
	 * 
	 * @param srcFile 被压缩的文件或目录
	 * @param zipFile 压缩后的产生的文件路径
	 */
	private static void validateFiles(File zipFile, File... srcFiles) throws UtilException {
		for (File srcFile : srcFiles) {
			if (false == srcFile.exists()) {
				throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			}
			
			try {
				// 压缩文件不能位于被压缩的目录内
				if (srcFile.isDirectory() && zipFile.getParent().contains(srcFile.getCanonicalPath())) {
					throw new UtilException("[zipPath] must not be the child directory of [srcPath]!");
				}
				
				if (false == zipFile.exists()) {
					FileUtil.touch(zipFile);
				}
			} catch (IOException e) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * 关闭当前Entry，继续下一个Entry
	 * 
	 * @param out ZipOutputStream
	 */
	private static void closeEntry(ZipOutputStream out) {
		try {
			out.closeEntry();
		} catch (IOException e) {
		}
	}

	/**
	 * 从Zip文件流中拷贝文件出来
	 * 
	 * @param zipFile Zip文件
	 * @param zipEntry zip文件中的子文件
	 * @param outItemFile 输出到的文件
	 * @throws IOException
	 */
	private static void copy(ZipFile zipFile, ZipEntry zipEntry, File outItemFile) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = zipFile.getInputStream(zipEntry);
			out = FileUtil.getOutputStream(outItemFile);
			IoUtil.copy(in, out);
		} finally {
			IoUtil.close(out);
			IoUtil.close(in);
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end

}