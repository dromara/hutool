package cn.hutool.extra.ssh;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;

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
public class Sftp implements Closeable {

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
		this.session = JschUtil.getSession(sshHost, sshPort, sshUser, sshPass);
		this.channel = JschUtil.openSftp(session);
	}

	/**
	 * 构造
	 * 
	 * @param session {@link Session}
	 */
	public Sftp(Session session) {
		this.session = session;
		this.channel = JschUtil.openSftp(session);
	}

	/**
	 * 构造
	 * 
	 * @param channel {@link ChannelSftp}
	 */
	public Sftp(ChannelSftp channel) {
		this.channel = channel;
	}
	// ---------------------------------------------------------------------------------------- Constructor end

	/**
	 * 获取SFTP通道
	 * 
	 * @return 通道
	 */
	public ChannelSftp getChannel() {
		return this.channel;
	}

	/**
	 * 远程当前目录
	 * 
	 * @return 远程当前目录
	 */
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
	 * 文件或目录在指定目录下是否存在
	 * 
	 * @param path 目录
	 * @param fileOrDirName 文件或目录名
	 * @return 是否存在
	 * @since 4.0.5
	 */
	public boolean exist(String path, String fileOrDirName) {
		List<String> ls = ls(path);
		return containsIgnoreCase(ls, fileOrDirName);
	}

	/**
	 * 遍历某个目录下所有文件或目录，不会递归遍历
	 * 
	 * @param path 遍历某个目录下所有文件或目录
	 * @return 目录或文件名列表
	 * @since 4.0.5
	 */
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
		return ls(path, new Filter<LsEntry>() {
			@Override
			public boolean accept(LsEntry t) {
				return t.getAttrs().isDir();
			}
		});
	}

	/**
	 * 遍历某个目录下所有文件，不会递归遍历
	 * 
	 * @param path 遍历某个目录下所有文件
	 * @return 文件名列表
	 * @since 4.0.5
	 */
	public List<String> lsFiles(String path) {
		return ls(path, new Filter<LsEntry>() {
			@Override
			public boolean accept(LsEntry t) {
				return false == t.getAttrs().isDir();
			}
		});
	}

	/**
	 * 遍历某个目录下所有文件或目录，不会递归遍历
	 * 
	 * @param path 遍历某个目录下所有文件或目录
	 * @param filter 文件或目录过滤器，可以实现过滤器返回自己需要的文件或目录名列表
	 * @return 目录或文件名列表
	 * @since 4.0.5
	 */
	public List<String> ls(String path, final Filter<LsEntry> filter) {
		final List<String> fileNames = new ArrayList<>();
		try {
			channel.ls(path, new LsEntrySelector() {
				@Override
				public int select(LsEntry entry) {
					String fileName = entry.getFilename();
					if (false == StrUtil.equals(".", fileName) && false == StrUtil.equals("..", fileName)) {
						if (null == filter || filter.accept(entry)) {
							fileNames.add(entry.getFilename());
						}
					}
					return CONTINUE;
				}
			});
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return fileNames;
	}

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
	 * 打开指定目录
	 * 
	 * @param directory directory
	 * @return 是否打开目录
	 */
	public boolean cd(String directory) {
		if (StrUtil.isBlank(directory)) {
			return false;
		}
		try {
			channel.cd(directory.replaceAll("\\\\", "/"));
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	/**
	 * 创建指定文件夹及其父目录
	 * 
	 * @param dir 文件夹路径，绝对路径
	 */
	public void mkDirs(String dir) {
		final String[] dirs = dir.split("[\\\\/]");

		try {
			final String now = channel.pwd();
			channel.cd("/");
			for (int i = 0; i < dirs.length; i++) {
				if (StrUtil.isNotEmpty(dirs[i])) {
					boolean dirExists = cd(dirs[i]);
					if (false == dirExists) {
						channel.mkdir(dirs[i]);
						channel.cd(dirs[i]);
					}
				}
			}
			channel.cd(now);
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath 要删除的文件绝对路径
	 */
	public Sftp delFile(String filePath) {
		try {
			channel.rm(filePath);
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	/**
	 * 删除文件夹及其文件夹下的所有文件
	 * 
	 * @param dirPath 文件夹路径
	 * @return boolean 是否删除成功
	 */
	@SuppressWarnings("unchecked")
	public boolean delDir(String dirPath) {
		if (false == cd(dirPath)) {
			return false;
		}

		Vector<LsEntry> list = null;
		try {
			list = channel.ls(channel.pwd());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}

		String fileName;
		for (LsEntry entry : list) {
			fileName = entry.getFilename();
			if (false == fileName.equals(".") && false == fileName.equals("..")) {
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
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与srcFilePath文件名相同。覆盖模式
	 * 
	 * @param srcFilePath 本地文件路径
	 * @param destPath 目标路径，
	 * @return this
	 */
	public Sftp put(String srcFilePath, String destPath) {
		return put(srcFilePath, destPath, Mode.OVERWRITE);
	}

	/**
	 * 将本地文件上传到目标服务器，目标文件名为destPath，若destPath为目录，则目标文件名将与srcFilePath文件名相同。
	 * 
	 * @param srcFilePath 本地文件路径
	 * @param destPath 目标路径，
	 * @param mode {@link Mode} 模式
	 * @return this
	 */
	public Sftp put(String srcFilePath, String destPath, Mode mode) {
		try {
			channel.put(srcFilePath, destPath, mode.ordinal());
		} catch (SftpException e) {
			throw new JschRuntimeException(e);
		}
		return this;
	}

	/**
	 * 获取远程文件
	 * 
	 * @param src 远程文件路径
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

	@Override
	public void close() throws IOException {
		JschUtil.close(this.channel);
		JschUtil.close(this.session);
	}

	/**
	 * JSch支持的三种文件传输模式
	 * 
	 * @author looly
	 *
	 */
	public static enum Mode {
		/** 完全覆盖模式，这是JSch的默认文件传输模式，即如果目标文件已经存在，传输的文件将完全覆盖目标文件，产生新的文件。 */
		OVERWRITE,
		/** 恢复模式，如果文件已经传输一部分，这时由于网络或其他任何原因导致文件传输中断，如果下一次传输相同的文件，则会从上一次中断的地方续传。 */
		RESUME,
		/** 追加模式，如果目标文件已存在，传输的文件将在目标文件后追加。 */
		APPEND;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 是否包含指定字符串，忽略大小写
	 * 
	 * @param names 文件或目录名列表
	 * @param nameToFind 要查找的文件或目录名
	 * @return 是否包含
	 * @since 4.0.5
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
