package org.dromara.hutool.extra.ftp;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * FTP的统一规范接口
 *
 * @author Looly
 */
public interface Ftp extends Closeable {

	/**
	 * 默认编码
	 */
	Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * 获取FTP配置
	 *
	 * @return FTP配置
	 */
	FtpConfig getConfig();

	/**
	 * 如果连接超时的话，重新进行连接
	 *
	 * @return this
	 */
	Ftp reconnectIfTimeout();

	/**
	 * 远程当前目录（工作目录）
	 *
	 * @return 远程当前目录
	 */
	String pwd();

	/**
	 * 打开指定目录，具体逻辑取决于实现，例如在FTP中，进入失败返回{@code false}， SFTP中则抛出异常
	 *
	 * @param directory directory
	 * @return 是否打开目录
	 */
	boolean cd(String directory);

	/**
	 * 打开上级目录
	 *
	 * @return 是否打开目录
	 */
	default boolean toParent() {
		return cd(StrUtil.DOUBLE_DOT);
	}

	/**
	 * 文件或目录是否存在<br>
	 * <ul>
	 *     <li>提供路径为空则返回{@code false}</li>
	 *     <li>提供路径非目录但是以'/'或'\'结尾返回{@code false}</li>
	 *     <li>文件名是'.'或者'..'返回{@code false}</li>
	 * </ul>
	 *
	 * @param path 目录
	 * @return 是否存在
	 */
	boolean exist(final String path);

	/**
	 * 判断给定路径是否为目录
	 *
	 * @param dir 被判断的路径
	 * @return 是否为目录
	 * @since 5.7.5
	 */
	default boolean isDir(final String dir) {
		final String workDir = pwd();
		try {
			return cd(dir);
		} finally {
			cd(workDir);
		}
	}

	/**
	 * 在当前远程目录（工作目录）下创建新的目录
	 *
	 * @param dir 目录名
	 * @return 是否创建成功
	 */
	boolean mkdir(String dir);

	/**
	 * 创建指定文件夹及其父目录，从根目录开始创建，创建完成后回到默认的工作目录
	 *
	 * @param dir 文件夹路径，绝对路径
	 */
	void mkDirs(final String dir);

	/**
	 * 遍历某个目录下所有文件和目录，不会递归遍历
	 *
	 * @param path 需要遍历的目录
	 * @return 文件和目录列表
	 */
	List<String> ls(String path);

	/**
	 * 删除指定目录下的指定文件
	 *
	 * @param path 目录路径
	 * @return 是否存在
	 */
	boolean delFile(String path);

	/**
	 * 删除文件夹及其文件夹下的所有文件
	 *
	 * @param dirPath 文件夹路径
	 * @return boolean 是否删除成功
	 */
	boolean delDir(String dirPath);

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与file文件名相同。
	 * 覆盖模式
	 *
	 * @param destPath 服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param file     需要上传的文件
	 * @return 是否成功
	 */
	boolean uploadFile(String destPath, File file);

	/**
	 * 下载文件
	 *
	 * @param path    文件路径
	 * @param outFile 输出文件或目录
	 */
	void download(String path, File outFile);

	/**
	 * 递归下载FTP服务器上文件到本地(文件目录和服务器同步), 服务器上有新文件会覆盖本地文件
	 *
	 * @param sourcePath ftp服务器目录
	 * @param destDir    本地目录
	 * @since 5.3.5
	 */
	void recursiveDownloadFolder(String sourcePath, File destDir);

	/**
	 * 读取FTP服务器上的文件为输入流
	 *
	 * @param path 文件路径
	 * @return {@link InputStream}
	 */
	InputStream getFileStream(String path);
}
