package cn.hutool.extra.ssh;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.AbstractFtp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * SFTP是Secure File Transfer Protocol的缩写，安全文件传送协议。可以为传输文件提供一种安全的加密方法。<br>
 * SFTP 为 SSH的一部份，是一种传输文件到服务器的安全方式。SFTP是使用加密传输认证信息和传输的数据，所以，使用SFTP是非常安全的。<br>
 * 但是，由于这种传输方式使用了加密/解密技术，所以传输效率比普通的FTP要低得多，如果您对网络安全性要求更高时，可以使用SFTP代替FTP。<br>
 *
 * <p>
 * 此类为基于jsch的SFTP实现<br>
 * 参考：https://www.cnblogs.com/longyg/archive/2012/06/25/2556576.html
 * </p>
 *
 * @author looly
 * @since 4.0.2
 */
public class Sftp extends AbstractFtp {

	private Session session;
	private ChannelSftp channel;

	// ---------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 *
	 * @param sshHost 远程主机
	 * @param sshPort 远程主机端口
	 * @param sshUser 远程主机用户名
	 * @param sshPass 远程主机密码
	 */
	public Sftp(String sshHost, int sshPort, String sshUser, String sshPass) {
		this(sshHost, sshPort, sshUser, sshPass, DEFAULT_CHARSET);
	}

	/**
	 * 构造
	 *
	 * @param sshHost 远程主机
	 * @param sshPort 远程主机端口
	 * @param sshUser 远程主机用户名
	 * @param sshPass 远程主机密码
	 * @param charset 编码
	 * @since 4.1.14
	 */
	public Sftp(String sshHost, int sshPort, String sshUser, String sshPass, Charset charset) {
		this(new FtpConfig(sshHost, sshPort, sshUser, sshPass, charset));
	}

	/**
	 * 构造
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	public Sftp(FtpConfig config) {
		super(config);
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param session {@link Session}
	 */
	public Sftp(Session session) {
		this(session, DEFAULT_CHARSET);
	}

	/**
	 * 构造
	 *
	 * @param session {@link Session}
	 * @param charset 编码
	 * @since 4.1.14
	 */
	public Sftp(Session session, Charset charset) {
		super(FtpConfig.create().setCharset(charset));
		init(session, charset);
	}

	/**
	 * 构造
	 *
	 * @param channel {@link ChannelSftp}
	 * @param charset 编码
	 */
	public Sftp(ChannelSftp channel, Charset charset) {
		super(FtpConfig.create().setCharset(charset));
		init(channel, charset);
	}
	// ---------------------------------------------------------------------------------------- Constructor end

	/**
	 * 构造
	 *
	 * @param sshHost 远程主机
	 * @param sshPort 远程主机端口
	 * @param sshUser 远程主机用户名
	 * @param sshPass 远程主机密码
	 * @param charset 编码
	 */
	public void init(String sshHost, int sshPort, String sshUser, String sshPass, Charset charset) {
		init(JschUtil.getSession(sshHost, sshPort, sshUser, sshPass), charset);
	}

	/**
	 * 初始化
	 *
	 * @since 5.3.3
	 */
	public void init() {
		init(this.ftpConfig);
	}

	/**
	 * 初始化
	 *
	 * @param config FTP配置
	 * @since 5.3.3
	 */
	public void init(FtpConfig config) {
		init(config.getHost(), config.getPort(), config.getUser(), config.getPassword(), config.getCharset());
	}

	/**
	 * 初始化
	 *
	 * @param session {@link Session}
	 * @param charset 编码
	 */
	public void init(Session session, Charset charset) {
		this.session = session;
		init(JschUtil.openSftp(session, (int) this.ftpConfig.getConnectionTimeout()), charset);
	}

	/**
	 * 初始化
	 *
	 * @param channel {@link ChannelSftp}
	 * @param charset 编码
	 */
	public void init(ChannelSftp channel, Charset charset) {
		this.ftpConfig.setCharset(charset);
		try {
			channel.setFilenameEncoding(charset.toString());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		this.channel = channel;
	}

	@Override
	public Sftp reconnectIfTimeout() {
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

	/**
	 * 获取SFTP通道客户端
	 *
	 * @return 通道客户端
	 * @since 4.1.14
	 */
	public ChannelSftp getClient() {
		return this.channel;
	}

	/**
	 * 远程当前目录
	 *
	 * @return 远程当前目录
	 */
	@Override
	public String pwd() {
		try {
			return channel.pwd();
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
	}

	/**
	 * 获取HOME路径
	 *
	 * @return HOME路径
	 * @since 4.0.5
	 */
	public String home() {
		try {
			return channel.getHome();
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
	}

	/**
	 * 遍历某个目录下所有文件或目录，不会递归遍历
	 *
	 * @param path 遍历某个目录下所有文件或目录
	 * @return 目录或文件名列表
	 * @since 4.0.5
	 */
	@Override
	public List<String> ls(String path) {
		return ls(path, null);
	}

	/**
	 * 遍历某个目录下所有目录，不会递归遍历
	 *
	 * @param path 遍历某个目录下所有目录
	 * @return 目录名列表
	 * @since 4.0.5
	 */
	public List<String> lsDirs(String path) {
		return ls(path, t -> t.getAttrs().isDir());
	}

	/**
	 * 遍历某个目录下所有文件，不会递归遍历
	 *
	 * @param path 遍历某个目录下所有文件
	 * @return 文件名列表
	 * @since 4.0.5
	 */
	public List<String> lsFiles(String path) {
		return ls(path, t -> false == t.getAttrs().isDir());
	}

	/**
	 * 遍历某个目录下所有文件或目录，不会递归遍历<br>
	 * 此方法自动过滤"."和".."两种目录
	 *
	 * @param path   遍历某个目录下所有文件或目录
	 * @param filter 文件或目录过滤器，可以实现过滤器返回自己需要的文件或目录名列表
	 * @return 目录或文件名列表
	 * @since 4.0.5
	 */
	public List<String> ls(String path, final Filter<LsEntry> filter) {
		final List<LsEntry> entries = lsEntries(path, filter);
		if (CollUtil.isEmpty(entries)) {
			return ListUtil.empty();
		}
		return CollUtil.map(entries, LsEntry::getFilename, true);
	}

	/**
	 * 遍历某个目录下所有文件或目录，生成LsEntry列表，不会递归遍历<br>
	 * 此方法自动过滤"."和".."两种目录
	 *
	 * @param path 遍历某个目录下所有文件或目录
	 * @return 目录或文件名列表
	 * @since 5.3.5
	 */
	public List<LsEntry> lsEntries(String path) {
		return lsEntries(path, null);
	}

	/**
	 * 遍历某个目录下所有文件或目录，生成LsEntry列表，不会递归遍历<br>
	 * 此方法自动过滤"."和".."两种目录
	 *
	 * @param path   遍历某个目录下所有文件或目录
	 * @param filter 文件或目录过滤器，可以实现过滤器返回自己需要的文件或目录名列表
	 * @return 目录或文件名列表
	 * @since 5.3.5
	 */
	public List<LsEntry> lsEntries(String path, Filter<LsEntry> filter) {
		final List<LsEntry> entryList = new ArrayList<>();
		try {
			channel.ls(path, entry -> {
				final String fileName = entry.getFilename();
				if (false == StrUtil.equals(".", fileName) && false == StrUtil.equals("..", fileName)) {
					if (null == filter || filter.accept(entry)) {
						entryList.add(entry);
					}
				}
				return LsEntrySelector.CONTINUE;
			});
		} catch (SftpException e) {
			if (false == StrUtil.startWithIgnoreCase(e.getMessage(), "No such file")) {
				throw new JschRuntimeException(e);
			}
			// 文件不存在忽略
		}
		return entryList;
	}

	@Override
	public boolean mkdir(String dir) {
		if (isDir(dir)) {
			// 目录已经存在，创建直接返回
			return true;
		}
		try {
			this.channel.mkdir(dir);
			return true;
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
	}

	@Override
	public boolean isDir(String dir) {
		final SftpATTRS sftpATTRS;
		try {
			sftpATTRS = this.channel.stat(dir);
		} catch (SftpException e) {
			final String msg = e.getMessage();
			// issue#I4P9ED@Gitee
			if (msg.contains("No such file") || msg.contains("does not exist")) {
				// 文件不存在直接返回false
				// pr#378@Gitee
				return false;
			}
			throw new FtpException(e);
		}
		return sftpATTRS.isDir();
	}

	/**
	 * 打开指定目录，如果指定路径非目录或不存在返回false
	 *
	 * @param directory directory
	 * @return 是否打开目录
	 * @throws FtpException 进入目录失败异常
	 */
	@Override
	synchronized public boolean cd(String directory) throws FtpException {
		if (StrUtil.isBlank(directory)) {
			// 当前目录
			return true;
		}
		try {
			channel.cd(directory.replace('\\', '/'));
			return true;
		} catch (SftpException e) {
			throw new FtpException(e);
		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath 要删除的文件绝对路径
	 */
	@Override
	public boolean delFile(String filePath) {
		try {
			channel.rm(filePath);
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return true;
	}

	/**
	 * 删除文件夹及其文件夹下的所有文件
	 *
	 * @param dirPath 文件夹路径
	 * @return boolean 是否删除成功
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean delDir(String dirPath) {
		if (false == cd(dirPath)) {
			return false;
		}

		Vector<LsEntry> list;
		try {
			list = channel.ls(channel.pwd());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}

		String fileName;
		for (LsEntry entry : list) {
			fileName = entry.getFilename();
			if (false == ".".equals(fileName) && false == "..".equals(fileName)) {
				if (entry.getAttrs().isDir()) {
					delDir(fileName);
				} else {
					delFile(fileName);
				}
			}
		}

		if (false == cd("..")) {
			return false;
		}

		// 删除空目录
		try {
			channel.rmdir(dirPath);
			return true;
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
	}

	/**
	 * 将本地文件或者文件夹同步（覆盖）上传到远程路径
	 *
	 * @param file       文件或者文件夹
	 * @param remotePath 远程路径
	 * @since 5.7.6
	 */
	public void syncUpload(File file, String remotePath) {
		if (false == FileUtil.exist(file)) {
			return;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files == null) {
				return;
			}
			for (File fileItem : files) {
				if (fileItem.isDirectory()) {
					String mkdir = FileUtil.normalize(remotePath + "/" + fileItem.getName());
					this.syncUpload(fileItem, mkdir);
				} else {
					this.syncUpload(fileItem, remotePath);
				}
			}
		} else {
			this.mkDirs(remotePath);
			this.upload(remotePath, file);
		}
	}

	@Override
	public boolean upload(String destPath, File file) {
		put(FileUtil.getAbsolutePath(file), destPath);
		return true;
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
	 * @param destPath   服务端路径，可以为{@code null} 或者相对路径或绝对路径
	 * @param fileName   文件名
	 * @param fileStream 文件流
	 * @return 是否上传成功
	 * @since 5.7.16
	 */
	public boolean upload(String destPath, String fileName, InputStream fileStream) {
		destPath = StrUtil.addSuffixIfNot(destPath, StrUtil.SLASH) + StrUtil.removePrefix(fileName, StrUtil.SLASH);
		put(fileStream, destPath, null, Mode.OVERWRITE);
		return true;
	}

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与srcFilePath文件名相同。覆盖模式
	 *
	 * @param srcFilePath 本地文件路径
	 * @param destPath    目标路径，
	 * @return this
	 */
	public Sftp put(String srcFilePath, String destPath) {
		return put(srcFilePath, destPath, Mode.OVERWRITE);
	}

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与srcFilePath文件名相同。
	 *
	 * @param srcFilePath 本地文件路径
	 * @param destPath    目标路径，
	 * @param mode        {@link Mode} 模式
	 * @return this
	 */
	public Sftp put(String srcFilePath, String destPath, Mode mode) {
		return put(srcFilePath, destPath, null, mode);
	}

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与srcFilePath文件名相同。
	 *
	 * @param srcFilePath 本地文件路径
	 * @param destPath    目标路径，
	 * @param monitor     上传进度监控，通过实现此接口完成进度显示
	 * @param mode        {@link Mode} 模式
	 * @return this
	 * @since 4.6.5
	 */
	public Sftp put(String srcFilePath, String destPath, SftpProgressMonitor monitor, Mode mode) {
		try {
			channel.put(srcFilePath, destPath, monitor, mode.ordinal());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	/**
	 * 将本地数据流上传到目标服务器，目标文件名为destPath，目标必须为文件
	 *
	 * @param srcStream 本地的数据流
	 * @param destPath  目标路径，
	 * @param monitor   上传进度监控，通过实现此接口完成进度显示
	 * @param mode      {@link Mode} 模式
	 * @return this
	 * @since 5.7.16
	 */
	public Sftp put(InputStream srcStream, String destPath, SftpProgressMonitor monitor, Mode mode) {
		try {
			channel.put(srcStream, destPath, monitor, mode.ordinal());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	@Override
	public void download(String src, File destFile) {
		get(src, FileUtil.getAbsolutePath(destFile));
	}

	/**
	 * 下载文件到{@link OutputStream}中
	 *
	 * @param src 源文件路径，包括文件名
	 * @param out 目标流
	 * @see #get(String, OutputStream)
	 */
	public void download(String src, OutputStream out) {
		get(src, out);
	}

	/**
	 * 递归下载FTP服务器上文件到本地(文件目录和服务器同步)
	 *
	 * @param sourcePath ftp服务器目录，必须为目录
	 * @param destDir    本地目录
	 */
	@Override
	public void recursiveDownloadFolder(String sourcePath, File destDir) throws JschRuntimeException {
		String fileName;
		String srcFile;
		File destFile;
		for (LsEntry item : lsEntries(sourcePath)) {
			fileName = item.getFilename();
			srcFile = StrUtil.format("{}/{}", sourcePath, fileName);
			destFile = FileUtil.file(destDir, fileName);

			if (false == item.getAttrs().isDir()) {
				// 本地不存在文件或者ftp上文件有修改则下载
				if (false == FileUtil.exist(destFile)
						|| (item.getAttrs().getMTime() > (destFile.lastModified() / 1000))) {
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
	 * 获取远程文件
	 *
	 * @param src  远程文件路径
	 * @param dest 目标文件路径
	 * @return this
	 */
	public Sftp get(String src, String dest) {
		try {
			channel.get(src, dest);
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	/**
	 * 获取远程文件
	 *
	 * @param src 远程文件路径
	 * @param out 目标流
	 * @return this
	 * @since 5.7.0
	 */
	public Sftp get(String src, OutputStream out) {
		try {
			channel.get(src, out);
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		JschUtil.close(this.channel);
		JschUtil.close(this.session);
	}

	@Override
	public String toString() {
		return "Sftp{" +
				"host='" + this.ftpConfig.getHost() + '\'' +
				", port=" + this.ftpConfig.getPort() +
				", user='" + this.ftpConfig.getUser() + '\'' +
				'}';
	}

	/**
	 * JSch支持的三种文件传输模式
	 *
	 * @author looly
	 */
	public enum Mode {
		/**
		 * 完全覆盖模式，这是JSch的默认文件传输模式，即如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件。
		 */
		OVERWRITE,
		/**
		 * 恢复模式，如果文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件，则会从上一次中断的地方续传。
		 */
		RESUME,
		/**
		 * 追加模式，如果目标文件已存在，传输的文件将在目标文件后追加。
		 */
		APPEND
	}
}
