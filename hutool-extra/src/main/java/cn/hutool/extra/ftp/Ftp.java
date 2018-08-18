package cn.hutool.extra.ftp;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import cn.hutool.core.util.CharsetUtil;

/**
 * FTP客户端封装<br>
 * 此客户端基于Apache-Commons-Net
 * 
 * @author looly
 * @since 4.1.8
 */
public class Ftp {

	private FTPClient client;
	private Charset charset = CharsetUtil.CHARSET_UTF_8;

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
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	public boolean mkdir(String remote) {
		boolean success = true;
		String directory = remote + "/";
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase("/") && false == cd(new String(directory))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = remote.substring(start, end);
				path = path + "/" + subDirectory;
				if (false == existFile(path)) {
					if (makeDirectory(subDirectory)) {
						cd(subDirectory);
					} else {
						System.out.println("创建目录[" + subDirectory + "]失败");
						cd(subDirectory);
					}
				} else {
					cd(subDirectory);
				}

				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
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
		boolean flag = false;
		FTPFile[] ftpFileArr;
		try {
			ftpFileArr = client.listFiles(path);
		} catch (IOException e) {
			throw new FtpException(e);
		}
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}

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

}
