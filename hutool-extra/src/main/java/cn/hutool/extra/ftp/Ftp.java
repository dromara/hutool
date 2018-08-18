package cn.hutool.extra.ftp;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

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
public class Ftp implements Closeable {
	
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
	 * 改变目录
	 * 
	 * @param directory 目录
	 * @return 是否成功
	 */
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
	 * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	 * 
	 * @param remote 目录
	 * @return 是否创建成功
	 */
	public boolean mkdir(String remote) {
		boolean success = true;
		String directory = StrUtil.addSuffixIfNot(remote, StrUtil.SLASH);
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (false == directory.equalsIgnoreCase(StrUtil.SLASH) && false == cd(directory)) {
			int start = directory.startsWith(directory) ? 1 : 0;
			int end = directory.indexOf(directory.startsWith(directory) ? 1 : 0, start);
			String path = "";
			String paths = "";
			String subDirectory;
			while (true) {
				subDirectory = remote.substring(start, end);
				path = StrUtil.format("{}/{}", path, subDirectory);
				if (false == existFile(path)) {
					makeDirectory(subDirectory);
				}
				cd(subDirectory);
				paths = StrUtil.format("{}/{}", paths, subDirectory);
				start = end + 1;
				end = directory.indexOf(StrUtil.SLASH, start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
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

	/**
	 * 删除指定目录下的指定文件
	 * 
	 * @param path 目录路径
	 * @param fileName 文件名
	 * @return 是否存在
	 */
	public boolean del(String path, String fileName) {
		cd(path);
		try {
			return client.deleteFile(fileName);
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
		mkdir(path);
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

	// ------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * @param dir 目录名
	 * @return 是否创建成功
	 */
	private boolean makeDirectory(String dir) {
		boolean flag = true;
		try {
			flag = this.client.makeDirectory(dir);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return flag;
	}
	// ------------------------------------------------------------------------------------------------------- Private method end
}
