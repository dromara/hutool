package cn.hutool.extra.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * FTP客户端封装<br>
 * 此客户端基于Apache-Commons-Net
 * 
 * @author looly
 * @since 4.1.8
 */
public class Ftp extends AbstractFtp {

	/** 默认端口 */
	public static final int DEFAULT_PORT = 21;

	private FTPClient client;
	private Charset charset;

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
	 * @param host 域名或IP
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 */
	public Ftp(String host, int port, String user, String password) {
		this(host, port, user, password, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 构造
	 * 
	 * @param host 域名或IP
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 * @param charset 编码
	 */
	public Ftp(String host, int port, String user, String password, Charset charset) {
		this.charset = charset;
		init(host, port, user, password);
	}

	/**
	 * 初始化连接
	 * 
	 * @param host 域名或IP
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 * @return this
	 */
	public Ftp init(String host, int port, String user, String password) {
		final FTPClient client = new FTPClient();
		client.setControlEncoding(this.charset.toString());
		try {
			// 连接ftp服务器
			client.connect(host, port);
			// 登录ftp服务器
			client.login(user, password);
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
			throw new FtpException("Login failed for user [{}], reply code is: [{}]", user, replyCode);
		}
		this.client = client;
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
	 * 改变目录
	 * 
	 * @param directory 目录
	 * @return 是否成功
	 */
	@Override
	public boolean cd(String directory) {
		boolean flag = true;
		try {
			flag = client.changeWorkingDirectory(directory);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return flag;
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
		try {
			return CollUtil.toList(this.client.listNames(path));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean mkdir(String dir) {
		boolean flag = true;
		try {
			flag = this.client.makeDirectory(dir);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return flag;
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
		if (ArrayUtil.isNotEmpty(ftpFileArr)) {
			return true;
		}
		return false;
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
		}
		cd(pwd);
		return isSuccess;
	}

	@Override
	public boolean delDir(String dirPath) {
		FTPFile[] dirs;
		try {
			dirs = client.listDirectories(dirPath);
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
				if (false == name.equals(".") && false == name.equals("..")) {
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
	 * 上传文件
	 * 
	 * @param path 服务端路径（目录）
	 * @param file 文件
	 * @return 是否上传成功
	 */
	@Override
	public boolean upload(String path, File file) {
		Assert.notNull(file, "file to upload is null !");
		return upload(path, file.getName(), file);
	}

	/**
	 * 上传文件
	 * 
	 * @param file 文件
	 * @param path 服务端路径
	 * @param fileName 文件名
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
	 * 上传文件
	 * 
	 * @param path 服务端路径
	 * @param fileName 文件名
	 * @param fileStream 文件流
	 * @return 是否上传成功
	 */
	public boolean upload(String path, String fileName, InputStream fileStream) {
		try {
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		mkDirs(path);
		cd(path);
		try {
			return client.storeFile(fileName, fileStream);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param path 文件路径
	 * @param outFile 输出文件或目录
	 */
	@Override
	public void download(String path, File outFile) {
		final String fileName = FileUtil.getName(path);
		final String dir = StrUtil.removeSuffix(path, fileName);
		download(dir, fileName, outFile);
	}

	/**
	 * 下载文件
	 * 
	 * @param path 文件路径
	 * @param fileName 文件名
	 * @param outFile 输出文件或目录
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
	 * @param path 文件路径
	 * @param fileName 文件名
	 * @param out 输出位置
	 */
	public void download(String path, String fileName, OutputStream out) {
		cd(path);
		try {
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.retrieveFile(fileName, out);
		} catch (IOException e) {
			throw new FtpException(e);
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
		this.client.logout();
		if (this.client.isConnected()) {
			this.client.disconnect();
		}
	}
}
