package com.xiaoleilu.hutool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.exceptions.UtilException;

public class ZipUtil {
	private static final Logger log = Log.get();
	
	/**
	 * 对文件或文件目录进行压缩<br>
	 * 不包含被打包目录
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath) throws Exception {
		zip(srcPath, zipPath, false);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath, boolean withSrcDir) throws Exception {
		File srcFile = FileUtil.file(srcPath);
		File zipFile = FileUtil.touch(zipPath);
		validateFile(srcFile, zipFile);
		
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream( new CheckedOutputStream(FileUtil.getOutputStream(zipFile), new CRC32()));

			// 如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile() || withSrcDir) {
				srcRootDir = srcFile.getParent();
			}
			// 调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, out);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			FileUtil.close(out);
		}
	}
	
	/**
	 * 解压
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir 解压到的目录
	 * @throws IOException
	 */
	public static void unzip(String zipFilePath,String outFileDir) throws IOException{
		unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir));
	}

	/**
	 * 解压
	 * @param zipFile zip文件
	 * @param outFile 解压到的文件或目录
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void unzip(File zipFile,File outFile) throws IOException{
		final ZipFile zipFileObj = new ZipFile(zipFile);
		final Enumeration<ZipEntry> em = (Enumeration<ZipEntry>) zipFileObj.entries();
		ZipEntry zipEntry = null;
		File outItemFile = null;
		while(em.hasMoreElements()){
			zipEntry = em.nextElement();
			outItemFile = new File(outFile,zipEntry.getName());
			log.debug("UNZIP {}", outItemFile.getPath());
			if(zipEntry.isDirectory()){
				outItemFile.mkdirs();
			}else{
				FileUtil.touch(outItemFile);
				copy(zipFileObj, zipEntry, outItemFile);
			}
		}
		FileUtil.close(zipFileObj);
	}
	
	//---------------------------------------------------------------------------------------------- Private method start
	/**
	 * 递归压缩文件夹
	 * 
	 * @param srcRootDir 压缩文件夹根目录的子路径
	 * @param file 当前递归压缩的文件或目录对象
	 * @param out 压缩文件存储对象
	 * @throws Exception
	 */
	private static void zip(String srcRootDir, File file, ZipOutputStream out) {
		if (file == null) {
			return;
		}

		if (file.isFile()) {// 如果是文件，则直接压缩该文件
			final String subPath = FileUtil.subPath(srcRootDir, file); // 获取文件相对于压缩文件夹根目录的子路径
			log.debug("ZIP {}", subPath);
			BufferedInputStream in = null;
			try {
				out.putNextEntry(new ZipEntry(subPath));
				in = FileUtil.getInputStream(file);
				IoUtil.copy(in, out);
			} catch (IOException e) {
				throw new UtilException(e);
			}finally{
				FileUtil.close(in);
				closeEntry(out);
			}
		}else {// 如果是目录，则压缩压缩目录中的文件或子目录
			for (File childFile : file.listFiles()) {
				zip(srcRootDir, childFile, out);
			}
		}
	}
	
	/**
	 * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
	 * @param srcFile 被压缩的文件或目录
	 * @param zipFile 压缩后的产生的文件路径
	 */
	private static void  validateFile(File srcFile, File zipFile) {
		if(false == srcFile.exists()) {
			throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
		}
		
		try {
			if (srcFile.isDirectory() && zipFile.getCanonicalPath().contains(srcFile.getCanonicalPath())) {
				throw new UtilException("[zipPath] must not be the child directory of [srcPath]!");
			}
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 关闭当前Entry，继续下一个Entry
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
	 * @param zipFile Zip文件
	 * @param zipEntry zip文件中的子文件
	 * @param outItemFile 输出到的文件
	 * @throws IOException
	 */
	private static void copy(ZipFile zipFile, ZipEntry zipEntry, File outItemFile) throws IOException{
		InputStream in = null;
		OutputStream out = null;
		try {
			in = zipFile.getInputStream(zipEntry);
			out = FileUtil.getOutputStream(outItemFile);
			IoUtil.copy(in, out);
		}finally{
			FileUtil.close(out);
			FileUtil.close(in);
		}
	}
	//---------------------------------------------------------------------------------------------- Private method end

}