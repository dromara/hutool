/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.ssh;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.ftp.AbstractFtp;
import org.dromara.hutool.extra.ftp.FtpConfig;
import org.dromara.hutool.extra.ftp.FtpException;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 在使用jsch 进行sftp协议下载文件时，总是中文乱码，而该框架源码又不允许设置编码。故：站在巨人的肩膀上，此类便孕育而出。
 *
 * <p>
 * 基于sshj 框架适配。<br>
 * 参考：<a href="https://github.com/hierynomus/sshj">https://github.com/hierynomus/sshj</a>
 * </p>
 *
 * @author youyongkun
 * @since 5.7.19
 */
public class SshjSftp extends AbstractFtp {

	private SSHClient ssh;
	private SFTPClient sftp;

	/**
	 * 构造，使用默认端口
	 *
	 * @param sshHost 主机
	 */
	public SshjSftp(final String sshHost) {
		this(new FtpConfig(sshHost, 22, null, null, CharsetUtil.UTF_8));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 */
	public SshjSftp(final String sshHost, final String sshUser, final String sshPass) {
		this(new FtpConfig(sshHost, 22, sshUser, sshPass, CharsetUtil.UTF_8));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 */
	public SshjSftp(final String sshHost, final int sshPort, final String sshUser, final String sshPass) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, CharsetUtil.UTF_8));
	}

	/**
	 * 构造
	 *
	 * @param sshHost 主机
	 * @param sshPort 端口
	 * @param sshUser 用户名
	 * @param sshPass 密码
	 * @param charset 编码
	 */
	public SshjSftp(final String sshHost, final int sshPort, final String sshUser, final String sshPass, final Charset charset) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, charset));
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	protected SshjSftp(final FtpConfig config) {
		super(config);
		init();
	}

	/**
	 * SSH 初始化并创建一个sftp客户端.
	 *
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public void init() {
		this.ssh = new SSHClient();
		ssh.addHostKeyVerifier(new PromiscuousVerifier());
		try {
			ssh.connect(ftpConfig.getHost(), ftpConfig.getPort());
			ssh.authPassword(ftpConfig.getUser(), ftpConfig.getPassword());
			ssh.setRemoteCharset(ftpConfig.getCharset());
			this.sftp = ssh.newSFTPClient();
		} catch (final IOException e) {
			throw new FtpException("sftp 初始化失败.", e);
		}
	}

	@Override
	public AbstractFtp reconnectIfTimeout() {
		if (StrUtil.isBlank(this.ftpConfig.getHost())) {
			throw new FtpException("Host is blank!");
		}
		try {
			this.cd(StrUtil.SLASH);
		} catch (final FtpException e) {
			close();
			init();
		}
		return this;
	}

	@Override
	public boolean cd(final String directory) {
		final String exec = String.format("cd %s", directory);
		command(exec);
		final String pwd = pwd();
		return pwd.equals(directory);
	}

	@Override
	public String pwd() {
		return command("pwd");
	}

	@Override
	public boolean mkdir(final String dir) {
		try {
			sftp.mkdir(dir);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
		return containsFile(dir);
	}

	@Override
	public List<String> ls(final String path) {
		final List<RemoteResourceInfo> infoList;
		try {
			infoList = sftp.ls(path);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
		if (CollUtil.isNotEmpty(infoList)) {
			return CollUtil.map(infoList, RemoteResourceInfo::getName);
		}
		return null;
	}

	@Override
	public boolean delFile(final String path) {
		try {
			sftp.rm(path);
			return !containsFile(path);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean delDir(final String dirPath) {
		try {
			sftp.rmdir(dirPath);
			return !containsFile(dirPath);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean uploadFile(final String destPath, final File file) {
		try {
			sftp.put(new FileSystemFile(file), destPath);
			return containsFile(destPath);
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void download(final String path, final File outFile) {
		try {
			sftp.get(path, new FileSystemFile(outFile));
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void recursiveDownloadFolder(final String sourcePath, final File destDir) {
		final List<String> files = ls(sourcePath);
		if (files != null && !files.isEmpty()) {
			files.forEach(path -> download(sourcePath + "/" + path, destDir));
		}
	}

	@Override
	public void close() {
		try {
			sftp.close();
			ssh.disconnect();
		} catch (final IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 是否包含该文件
	 *
	 * @param fileDir 文件绝对路径
	 * @return true:包含 false:不包含
	 * @author youyongkun
	 * @since 5.7.18
	 */
	public boolean containsFile(final String fileDir) {
		try {
			sftp.lstat(fileDir);
			return true;
		} catch (final IOException e) {
			return false;
		}
	}


	/**
	 * 执行Linux 命令
	 *
	 * @param exec 命令
	 * @return 返回响应结果.
	 * @author youyongkun
	 * @since 5.7.19
	 */
	public String command(final String exec) {
		Session session = null;
		try {
			session = ssh.startSession();
			final Session.Command command = session.exec(exec);
			final InputStream inputStream = command.getInputStream();
			return IoUtil.read(inputStream, DEFAULT_CHARSET);
		} catch (final Exception e) {
			throw new FtpException(e);
		} finally {
			IoUtil.closeQuietly(session);
		}
	}
}
