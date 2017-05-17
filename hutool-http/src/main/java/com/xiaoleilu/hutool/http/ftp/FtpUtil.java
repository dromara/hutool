package com.xiaoleilu.hutool.http.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.http.HttpException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

/**
 * FTP工具类
 * @author Looly
 *
 */
@SuppressWarnings("restriction")
public class FtpUtil {

	// ----------------------------------------------------------------------------------------------- connect
	/**
	 * 连接FTP服务器
	 * 
	 * @param server 服务器IP或域名
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 * @param path 初始路径
	 * @return {@link FtpClient}
	 * @throws HttpException FtpProtocolException和IOException包装
	 */
	public static FtpClient connect(String server, int port, String user, String password, String path) {
		FtpClient client = FtpClient.create();

		try {
			client.connect(new InetSocketAddress(server, port));
			if (StrUtil.isNotBlank(user)) {
				client.login(user, null == password ? null : password.toCharArray());
			}

			if (StrUtil.isNotBlank(path)) {
				client.changeDirectory(path);
			}
			client.setBinaryType();
		} catch (FtpProtocolException | IOException e) {
			throw new HttpException(e);
		}

		return client;
	}

	/**
	 * 连接FTP服务器
	 * 
	 * @param server 服务器IP或域名
	 * @param port 端口
	 * @param path 初始路径
	 * @return {@link FtpClient}
	 * @throws FtpProtocolException
	 * @throws HttpException FtpProtocolException和IOException包装
	 */
	public static FtpClient connect(String server, int port, String path) {
		return connect(server, port, null, null, path);
	}

	/**
	 * 连接FTP服务器
	 * 
	 * @param server 服务器IP或域名
	 * @param port 端口
	 * @return {@link FtpClient}
	 * @throws FtpProtocolException
	 * @throws HttpException FtpProtocolException和IOException包装
	 */
	public static FtpClient connect(String server, int port) {
		return connect(server, port, null, null, null);
	}

	/**
	 * 连接FTP服务器
	 * 
	 * @param server 服务器IP或域名
	 * @param port 端口
	 * @return {@link FtpClient}
	 * @throws FtpProtocolException
	 * @throws HttpException FtpProtocolException和IOException包装
	 */
	public static FtpClient connect(String server) {
		return connect(server, 20, null, null, null);
	}

	// ----------------------------------------------------------------------------------------------- upload
	/**
	 * 上传文件
	 * 
	 * @param client {@link FtpClient}
	 * @param file 被上传的文件
	 * @param newName 传后的新文件名
	 * @return {@link FtpClient}
	 */
	public FtpClient upload(FtpClient client, File file, String newName) {
		OutputStream out = null;
		try {
			out = client.putFileStream(newName, true);
			FileUtil.writeToStream(file, out);
		} catch (FtpProtocolException | IOException e) {
			throw new HttpException(e);
		} finally {
			IoUtil.close(out);
		}
		return client;
	}

	/**
	 * 上传文件
	 * 
	 * @param client {@link FtpClient}
	 * @param file 上传的文件
	 * @return {@link FtpClient}
	 */
	public FtpClient upload(FtpClient client, File file) {
		return upload(client, file, file.getName());
	}

	// ----------------------------------------------------------------------------------------------- download
	/**
	 * 下载文件
	 * 
	 * @param client {@link FtpClient}
	 * @param fileName 要下载的文件名
	 * @param destFile 下载的目标文件。当为目录时在此目录下生成新的文件，文件名与FTP服务器一致
	 * @return {@link FtpClient}
	 */
	public FtpClient download(FtpClient client, String fileName, File destFile) {
		if (destFile.isDirectory()) {
			destFile = new File(destFile, fileName);
		}

		InputStream in = null;
		try {
			in = client.getFileStream(fileName);
			FileUtil.writeFromStream(in, destFile);
		} catch (FtpProtocolException | IOException e) {
			throw new HttpException(e);
		} finally {
			IoUtil.close(in);
		}
		return client;
	}

	/**
	 * 重命名
	 * 
	 * @param client {@link FtpClient}
	 * @param oldName 原文件名
	 * @param newName 新文件名
	 * @return {@link FtpClient}
	 */
	public FtpClient rename(FtpClient client, String oldName, String newName) {
		try {
			client.rename(oldName, newName);
		} catch (FtpProtocolException | IOException e) {
			throw new HttpException(e);
		}

		return client;
	}

	/**
	 * 文件名列表
	 * 
	 * @param client {@link FtpClient}
	 * @param path 路径
	 * @return 文件名列表
	 */
	public List<String> fileList(FtpClient client, String path, Charset charset) {
		InputStream in = null;
		try {
			in = client.nameList(path);
			return IoUtil.readLines(in, charset, new ArrayList<String>());
		} catch (FtpProtocolException | IOException e) {
			throw new HttpException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 指定文件名文件是否存在
	 * @param client {@link FtpClient}
	 * @param path 路径
	 * @param fileName 文件名
	 * @return 是否存在
	 */
	public boolean exist(FtpClient client, String path, String fileName) {
		List<String> fileList = fileList(client, path, null);
		if(fileList.contains(fileName)){
			return true;
		}
		return false;
	}
}
