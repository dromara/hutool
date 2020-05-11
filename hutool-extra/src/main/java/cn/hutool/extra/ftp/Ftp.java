package cn.hutool.extra.ftp;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * FTP客户端封装<br>
 * 此客户端基于Apache-Commons-Net
 *
 * @author looly
 * @since 4.1.8
 */
public class Ftp extends AbstractFtp {

	/**
	 * 默认端口
	 */
	public static final int DEFAULT_PORT = 21;

	private FTPClient client;
	private FtpMode mode;
	/**
	 * 执行完操作是否返回当前目录
	 */
	private boolean backToPwd;

	/**
	 * 构造，匿名登录
	 *
	 * @param host 域名或IP
	 */
	public Ftp(String host) {
		this(host, DEFAULT_PORT);
	}

	/**
	 * 构造，匿名登录
	 *
	 * @param host 域名或IP
	 * @param port 端口
	 */
	public Ftp(String host, int port) {
		this(host, port, "anonymous", "");
	}

	/**
	 * 构造
	 *
	 * @param host     域名或IP
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 */
	public Ftp(String host, int port, String user, String password) {
		this(host, port, user, password, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param host     域名或IP
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @param charset  编码
	 */
	public Ftp(String host, int port, String user, String password, Charset charset) {
		this(host, port, user, password, charset, null);
	}

	/**
	 * 构造
	 *
	 * @param host     域名或IP
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @param charset  编码
	 * @param mode     模式
	 */
	public Ftp(String host, int port, String user, String password, Charset charset, FtpMode mode) {
		this(new FtpConfig(host, port, user, password, charset), mode);
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @param mode   模式
	 */
	public Ftp(FtpConfig config, FtpMode mode) {
		super(config);
		this.mode = mode;
		this.init();
	}

	/**
	 * 初始化连接
	 *
	 * @return this
	 */
	public Ftp init() {
		return this.init(this.ftpConfig, this.mode);
	}

	/**
	 * 初始化连接
	 *
	 * @param host     域名或IP
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @return this
	 */
	public Ftp init(String host, int port, String user, String password) {
		return this.init(host, port, user, password, null);
	}

	/**
	 * 初始化连接
	 *
	 * @param host     域名或IP
	 * @param port     端口
	 * @param user     用户名
	 * @param password 密码
	 * @param mode     模式
	 * @return this
	 */
	public Ftp init(String host, int port, String user, String password, FtpMode mode) {
		return init(new FtpConfig(host, port, user, password, this.ftpConfig.getCharset()), mode);
	}

	/**
	 * 初始化连接
	 *
	 * @param config FTP配置
	 * @param mode   模式
	 * @return this
	 */
	public Ftp init(FtpConfig config, FtpMode mode) {
		final FTPClient client = new FTPClient();
		client.setControlEncoding(config.getCharset().toString());
		client.setConnectTimeout((int) config.getConnectionTimeout());
		try {
			// 连接ftp服务器
			client.connect(config.getHost(), config.getPort());
			client.setSoTimeout((int) config.getSoTimeout());
			// 登录ftp服务器
			client.login(config.getUser(), config.getPassword());
		} catch (IOException e) {
			throw new FtpException(e);
		}
		final int replyCode = client.getReplyCode(); // 是否成功登录服务器
		if (false == FTPReply.isPositiveCompletion(replyCode)) {
			try {
				client.disconnect();
			} catch (IOException e) {
				// ignore
			}
			throw new FtpException("Login failed for user [{}], reply code is: [{}]", config.getUser(), replyCode);
		}
		this.client = client;
		if (mode != null) {
			setMode(mode);
		}
		return this;
	}

	/**
	 * 设置FTP连接模式，可选主动和被动模式
	 *
	 * @param mode 模式枚举
	 * @return this
	 * @since 4.1.19
	 */
	public Ftp setMode(FtpMode mode) {
		this.mode = mode;
		switch (mode) {
			case Active:
				this.client.enterLocalActiveMode();
				break;
			case Passive:
				this.client.enterLocalPassiveMode();
				break;
		}
		return this;
	}

	/**
	 * 设置执行完操作是否返回当前目录
	 *
	 * @param backToPwd 执行完操作是否返回当前目录
	 * @return this
	 * @since 4.6.0
	 */
	public Ftp setBackToPwd(boolean backToPwd) {
		this.backToPwd = backToPwd;
		return this;
	}

	/**
	 * 如果连接超时的话，重新进行连接 经测试，当连接超时时，client.isConnected()仍然返回ture，无法判断是否连接超时 因此，通过发送pwd命令的方式，检查连接是否超时
	 *
	 * @return this
	 */
	@Override
	public Ftp reconnectIfTimeout() {
		String pwd = null;
		try {
			pwd = pwd();
		} catch (FtpException fex) {
			// ignore
		}

		if (pwd == null) {
			return this.init();
		}
		return this;
	}

	/**
	 * 改变目录
	 *
	 * @param directory 目录
	 * @return 是否成功
	 */
	@Override
	public boolean cd(String directory) {
		if (StrUtil.isBlank(directory)) {
			return false;
		}

		try {
			return client.changeWorkingDirectory(directory);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 远程当前目录
	 *
	 * @return 远程当前目录
	 * @since 4.1.14
	 */
	@Override
	public String pwd() {
		try {
			return client.printWorkingDirectory();
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public List<String> ls(String path) {
		final FTPFile[] ftpFiles = lsFiles(path);

		final List<String> fileNames = new ArrayList<>();
		for (FTPFile ftpFile : ftpFiles) {
			fileNames.add(ftpFile.getName());
		}
		return fileNames;
	}

	/**
	 * 遍历某个目录下所有文件和目录，不会递归遍历<br>
	 * 此方法自动过滤"."和".."两种目录
	 *
	 * @param path   目录
	 * @param filter 过滤器，null表示不过滤，默认去掉"."和".."两种目录
	 * @return 文件或目录列表
	 * @since 5.3.5
	 */
	public List<FTPFile> lsFiles(String path, Filter<FTPFile> filter) {
		final FTPFile[] ftpFiles = lsFiles(path);
		if (ArrayUtil.isEmpty(ftpFiles)) {
			return ListUtil.empty();
		}

		final List<FTPFile> result = new ArrayList<>(ftpFiles.length - 2);
		String fileName;
		for (FTPFile ftpFile : ftpFiles) {
			fileName = ftpFile.getName();
			if (false == StrUtil.equals(".", fileName) && false == StrUtil.equals("..", fileName)) {
				if (null == filter || filter.accept(ftpFile)) {
					result.add(ftpFile);
				}
			}
		}
		return result;
	}

	/**
	 * 遍历某个目录下所有文件和目录，不会递归遍历
	 *
	 * @param path 目录
	 * @return 文件或目录列表
	 */
	public FTPFile[] lsFiles(String path) {
		String pwd = null;
		if (StrUtil.isNotBlank(path)) {
			pwd = pwd();
			cd(path);
		}

		FTPFile[] ftpFiles;
		try {
			ftpFiles = this.client.listFiles();
		} catch (IOException e) {
			throw new FtpException(e);
		} finally {
			// 回到原目录
			cd(pwd);
		}

		return ftpFiles;
	}

	@Override
	public boolean mkdir(String dir) {
		try {
			return this.client.makeDirectory(dir);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 判断ftp服务器文件是否存在
	 *
	 * @param path 文件路径
	 * @return 是否存在
	 */
	public boolean existFile(String path) {
		FTPFile[] ftpFileArr;
		try {
			ftpFileArr = client.listFiles(path);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return ArrayUtil.isNotEmpty(ftpFileArr);
	}

	@Override
	public boolean delFile(String path) {
		final String pwd = pwd();
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		cd(dir);
		boolean isSuccess;
		try {
			isSuccess = client.deleteFile(fileName);
		} catch (IOException e) {
			throw new FtpException(e);
		} finally {
			// 回到原目录
			cd(pwd);
		}
		return isSuccess;
	}

	@Override
	public boolean delDir(String dirPath) {
		FTPFile[] dirs;
		try {
			dirs = client.listFiles(dirPath);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		String name;
		String childPath;
		for (FTPFile ftpFile : dirs) {
			name = ftpFile.getName();
			childPath = StrUtil.format("{}/{}", dirPath, name);
			if (ftpFile.isDirectory()) {
				// 上级和本级目录除外
				if (false == ".".equals(name) && false == "..".equals(name)) {
					delDir(childPath);
				}
			} else {
				delFile(childPath);
			}
		}

		// 删除空目录
		try {
			return this.client.removeDirectory(dirPath);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 上传文件到指定目录，可选：
	 *
	 * <pre>
	 * 1. path为null或""上传到当前路径
	 * 2. path为相对路径则相对于当前路径的子路径
	 * 3. path为绝对路径则上传到此路径
	 * </pre>
	 *
	 * @param destPath 服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param file     文件
	 * @return 是否上传成功
	 */
	@Override
	public boolean upload(String destPath, File file) {
		Assert.notNull(file, "file to upload is null !");
		return upload(destPath, file.getName(), file);
	}

	/**
	 * 上传文件到指定目录，可选：
	 *
	 * <pre>
	 * 1. path为null或""上传到当前路径
	 * 2. path为相对路径则相对于当前路径的子路径
	 * 3. path为绝对路径则上传到此路径
	 * </pre>
	 *
	 * @param file     文件
	 * @param path     服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param fileName 自定义在服务端保存的文件名
	 * @return 是否上传成功
	 */
	public boolean upload(String path, String fileName, File file) {
		try (InputStream in = FileUtil.getInputStream(file)) {
			return upload(path, fileName, in);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 上传文件到指定目录，可选：
	 *
	 * <pre>
	 * 1. path为null或""上传到当前路径
	 * 2. path为相对路径则相对于当前路径的子路径
	 * 3. path为绝对路径则上传到此路径
	 * </pre>
	 *
	 * @param path       服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param fileName   文件名
	 * @param fileStream 文件流
	 * @return 是否上传成功
	 */
	public boolean upload(String path, String fileName, InputStream fileStream) {
		try {
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (IOException e) {
			throw new FtpException(e);
		}

		String pwd = null;
		if (this.backToPwd) {
			pwd = pwd();
		}

		if (StrUtil.isNotBlank(path)) {
			mkDirs(path);
			boolean isOk = cd(path);
			if (false == isOk) {
				return false;
			}
		}

		try {
			return client.storeFile(fileName, fileStream);
		} catch (IOException e) {
			throw new FtpException(e);
		} finally {
			if (this.backToPwd) {
				cd(pwd);
			}
		}
	}

	/**
	 * 下载文件
	 *
	 * @param path    文件路径
	 * @param outFile 输出文件或目录
	 */
	@Override
	public void download(String path, File outFile) {
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		download(dir, fileName, outFile);
	}

	/**
	 * 递归下载FTP服务器上文件到本地(文件目录和服务器同步)
	 *
	 * @param sourcePath ftp服务器目录
	 * @param destDir    本地目录
	 */
	@Override
	public void recursiveDownloadFolder(String sourcePath, File destDir) {
		String fileName;
		String srcFile;
		File destFile;
		for (FTPFile ftpFile : lsFiles(sourcePath, null)) {
			fileName = ftpFile.getName();
			srcFile = StrUtil.format("{}/{}", sourcePath, fileName);
			destFile = FileUtil.file(destDir, fileName);

			if (false == ftpFile.isDirectory()) {
				// 本地不存在文件或者ftp上文件有修改则下载
				if (false == FileUtil.exist(destFile)
						|| (ftpFile.getTimestamp().getTimeInMillis() > destFile.lastModified())) {
					download(srcFile, destFile);
				}
			} else {
				// 服务端依旧是目录，继续递归
				FileUtil.mkdir(destFile);
				recursiveDownloadFolder(srcFile, destFile);
			}
		}
	}

	/**
	 * 下载文件
	 *
	 * @param path     文件路径
	 * @param fileName 文件名
	 * @param outFile  输出文件或目录
	 */
	public void download(String path, String fileName, File outFile) {
		if (outFile.isDirectory()) {
			outFile = new File(outFile, fileName);
		}
		if (false == outFile.exists()) {
			FileUtil.touch(outFile);
		}
		try (OutputStream out = FileUtil.getOutputStream(outFile)) {
			download(path, fileName, out);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 下载文件到输出流
	 *
	 * @param path     文件路径
	 * @param fileName 文件名
	 * @param out      输出位置
	 */
	public void download(String path, String fileName, OutputStream out) {
		String pwd = null;
		if (this.backToPwd) {
			pwd = pwd();
		}

		cd(path);
		try {
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.retrieveFile(fileName, out);
		} catch (IOException e) {
			throw new FtpException(e);
		} finally {
			if (backToPwd) {
				cd(pwd);
			}
		}
	}

	/**
	 * 获取FTPClient客户端对象
	 *
	 * @return {@link FTPClient}
	 */
	public FTPClient getClient() {
		return this.client;
	}

	@Override
	public void close() throws IOException {
		if (null != this.client) {
			this.client.logout();
			if (this.client.isConnected()) {
				this.client.disconnect();
			}
			this.client = null;
		}
	}
}
