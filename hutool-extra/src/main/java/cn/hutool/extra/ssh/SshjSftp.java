package cn.hutool.extra.ssh;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.AbstractFtp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
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
import java.util.stream.Collectors;

/**
 * 在使用jsch 进行sftp协议下载文件时，总是中文乱码，而该框架源码又不允许设置编码。故：站在巨人的肩膀上，此类便孕育而出。
 *
 * <p>
 * 基于sshj 框架适配。<br>
 * 参考：https://github.com/hierynomus/sshj
 * </p>
 *
 * @author youyongkun
 * @date 2021/12/31 14:51
 * @since 5.7.18
 */
public class SshjSftp extends AbstractFtp {

	private SSHClient ssh;
	private SFTPClient sftp;

	public SshjSftp(String sshHost) {
		this(new FtpConfig(sshHost, 22, null, null, CharsetUtil.CHARSET_UTF_8));
	}

	public SshjSftp(String sshHost, String sshUser, String sshPass) {
		this(new FtpConfig(sshHost, 22, sshUser, sshPass, CharsetUtil.CHARSET_UTF_8));
	}

	public SshjSftp(String sshHost, int sshPort, String sshUser, String sshPass) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, CharsetUtil.CHARSET_UTF_8));
	}

	public SshjSftp(String sshHost, int sshPort, String sshUser, String sshPass, Charset charset) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, charset));
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	protected SshjSftp(FtpConfig config) {
		super(config);
		init();
	}

	/**
	 * SSH 初始化并创建一个sftp客户端.
	 *
	 * @author youyongkun
	 * @date 2021/12/31 14:59
	 * @since 5.7.18
	 */
	public void init() {
		this.ssh = new SSHClient();
		ssh.addHostKeyVerifier(new PromiscuousVerifier());
		try {
			ssh.connect(ftpConfig.getHost());
			ssh.authPassword(ftpConfig.getUser(), ftpConfig.getPassword());
			ssh.setRemoteCharset(ftpConfig.getCharset());
			this.sftp = ssh.newSFTPClient();
		} catch (IOException e) {
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
		} catch (FtpException e) {
			close();
			init();
		}
		return this;
	}

	@Override
	public boolean cd(String directory) {
		String exec = String.format("cd %s", directory);
		command(exec);
		String pwd = pwd();
		return pwd.equals(directory);
	}

	@Override
	public String pwd() {
		return command("pwd");
	}

	@Override
	public boolean mkdir(String dir) {
		try {
			sftp.mkdir(dir);
			return containsFile(dir);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public List<String> ls(String path) {
		try {
			List<RemoteResourceInfo> infoList = sftp.ls(path);
			if (infoList != null && infoList.size() > 0) {
				return infoList.stream().map(info -> info.getName()).collect(Collectors.toList());
			}
		} catch (IOException e) {
			throw new FtpException(e);
		}
		return null;
	}

	@Override
	public boolean delFile(String path) {
		try {
			sftp.rm(path);
			return !containsFile(path);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean delDir(String dirPath) {
		try {
			sftp.rmdir(dirPath);
			return !containsFile(dirPath);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public boolean upload(String destPath, File file) {
		try {
			sftp.put(new FileSystemFile(file), destPath);
			return containsFile(destPath);
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void download(String path, File outFile) {
		try {
			sftp.get(path, new FileSystemFile(outFile));
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	@Override
	public void recursiveDownloadFolder(String sourcePath, File destDir) {
		List<String> files = ls(sourcePath);
		if (files != null && !files.isEmpty()) {
			files.forEach(path -> {
				download(sourcePath + "/" + path, destDir);
			});
		}
	}

	@Override
	public void close() {
		try {
			sftp.close();
			ssh.disconnect();
		} catch (IOException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 是否包含该文件
	 *
	 * @param fileDir 文件绝对路径
	 * @return true:包含 false:不包含
	 * @author youyongkun
	 * @date 2021/12/31 15:36
	 * @since 5.7.18
	 */
	public boolean containsFile(String fileDir) {
		try {
			sftp.lstat(fileDir);
			return true;
		} catch (IOException e) {
			return false;
		}
	}


	/**
	 * 执行Linux 命令
	 *
	 * @param exec 命令
	 * @return 返回响应结果.
	 * @author youyongkun
	 * @date 2021/12/31 15:59
	 * @since 5.7.18
	 */
	public String command(String exec) {
		Session session = null;
		try {
			session = ssh.startSession();
			final Session.Command pwd = session.exec(exec);
			InputStream inputStream = pwd.getInputStream();
			return IoUtil.read(inputStream, DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new FtpException(e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					throw new FtpException(e);
				}
			}
		}
	}
}
