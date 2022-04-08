package cn.hutool.extra.ftp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Closeable;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 抽象FTP类，用于定义通用的FTP方法
 *
 * @author looly
 * @since 4.1.14
 */
public abstract class AbstractFtp implements Closeable {

	public static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

	protected FtpConfig ftpConfig;

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	protected AbstractFtp(FtpConfig config) {
		this.ftpConfig = config;
	}

	/**
	 * 如果连接超时的话，重新进行连接
	 *
	 * @return this
	 * @since 4.5.2
	 */
	public abstract AbstractFtp reconnectIfTimeout();

	/**
	 * 打开指定目录，具体逻辑取决于实现，例如在FTP中，进入失败返回{@code false}， SFTP中则抛出异常
	 *
	 * @param directory directory
	 * @return 是否打开目录
	 */
	public abstract boolean cd(String directory);

	/**
	 * 打开上级目录
	 *
	 * @return 是否打开目录
	 * @since 4.0.5
	 */
	public boolean toParent() {
		return cd("..");
	}

	/**
	 * 远程当前目录（工作目录）
	 *
	 * @return 远程当前目录
	 */
	public abstract String pwd();

	/**
	 * 判断给定路径是否为目录
	 *
	 * @param dir 被判断的路径
	 * @return 是否为目录
	 * @since 5.7.5
	 */
	public boolean isDir(String dir) {
		return cd(dir);
	}

	/**
	 * 在当前远程目录（工作目录）下创建新的目录
	 *
	 * @param dir 目录名
	 * @return 是否创建成功
	 */
	public abstract boolean mkdir(String dir);

	/**
	 * 文件或目录是否存在
	 *
	 * @param path 目录
	 * @return 是否存在
	 */
	public boolean exist(String path) {
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		final List<String> names;
		try {
			names = ls(dir);
		} catch (FtpException ignore) {
			return false;
		}
		return containsIgnoreCase(names, fileName);
	}

	/**
	 * 遍历某个目录下所有文件和目录，不会递归遍历
	 *
	 * @param path 需要遍历的目录
	 * @return 文件和目录列表
	 */
	public abstract List<String> ls(String path);

	/**
	 * 删除指定目录下的指定文件
	 *
	 * @param path 目录路径
	 * @return 是否存在
	 */
	public abstract boolean delFile(String path);

	/**
	 * 删除文件夹及其文件夹下的所有文件
	 *
	 * @param dirPath 文件夹路径
	 * @return boolean 是否删除成功
	 */
	public abstract boolean delDir(String dirPath);

	/**
	 * 创建指定文件夹及其父目录，从根目录开始创建，创建完成后回到默认的工作目录
	 *
	 * @param dir 文件夹路径，绝对路径
	 */
	public void mkDirs(String dir) {
		final String[] dirs = StrUtil.trim(dir).split("[\\\\/]+");

		final String now = pwd();
		if (dirs.length > 0 && StrUtil.isEmpty(dirs[0])) {
			//首位为空，表示以/开头
			this.cd(StrUtil.SLASH);
		}
		for (String s : dirs) {
			if (StrUtil.isNotEmpty(s)) {
				boolean exist = true;
				try {
					if (false == cd(s)) {
						exist = false;
					}
				} catch (FtpException e) {
					exist = false;
				}
				if (false == exist) {
					//目录不存在时创建
					mkdir(s);
					cd(s);
				}
			}
		}
		// 切换回工作目录
		cd(now);
	}

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与file文件名相同。
	 * 覆盖模式
	 *
	 * @param destPath 服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param file     需要上传的文件
	 * @return 是否成功
	 */
	public abstract boolean upload(String destPath, File file);

	/**
	 * 下载文件
	 *
	 * @param path    文件路径
	 * @param outFile 输出文件或目录
	 */
	public abstract void download(String path, File outFile);

	/**
	 * 下载文件-避免未完成的文件<br>
	 * 来自：https://gitee.com/dromara/hutool/pulls/407<br>
	 * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
	 *
	 * @param path     文件路径
	 * @param outFile  输出文件或目录
	 * @param tempFileSuffix 临时文件后缀，默认".temp"
	 * @since 5.7.12
	 */
	public void download(String path, File outFile, String tempFileSuffix) {
		if(StrUtil.isBlank(tempFileSuffix)){
			tempFileSuffix = ".temp";
		} else {
			tempFileSuffix = StrUtil.addPrefixIfNot(tempFileSuffix, StrUtil.DOT);
		}

		// 目标文件真实名称
		final String fileName = outFile.isDirectory() ? FileUtil.getName(path) : outFile.getName();
		// 临时文件名称
		final String tempFileName = fileName + tempFileSuffix;

		// 临时文件
		outFile = new File(outFile.isDirectory() ? outFile : outFile.getParentFile(), tempFileName);
		try {
			download(path, outFile);
			// 重命名下载好的临时文件
			FileUtil.rename(outFile, fileName, true);
		} catch (Throwable e) {
			// 异常则删除临时文件
			FileUtil.del(outFile);
			throw new FtpException(e);
		}
	}

	/**
	 * 递归下载FTP服务器上文件到本地(文件目录和服务器同步), 服务器上有新文件会覆盖本地文件
	 *
	 * @param sourcePath ftp服务器目录
	 * @param destDir    本地目录
	 * @since 5.3.5
	 */
	public abstract void recursiveDownloadFolder(String sourcePath, File destDir);

	// ---------------------------------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 是否包含指定字符串，忽略大小写
	 *
	 * @param names      文件或目录名列表
	 * @param nameToFind 要查找的文件或目录名
	 * @return 是否包含
	 */
	private static boolean containsIgnoreCase(List<String> names, String nameToFind) {
		if (CollUtil.isEmpty(names)) {
			return false;
		}
		if (StrUtil.isEmpty(nameToFind)) {
			return false;
		}
		for (String name : names) {
			if (nameToFind.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	// ---------------------------------------------------------------------------------------------------------------------------------------- Private method end
}
