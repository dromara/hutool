package cn.hutool.core.net.multipart;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传的文件对象
 *
 * @author xiaoleilu
 */
public class UploadFile {

	private static final String TMP_FILE_PREFIX = "hutool-";
	private static final String TMP_FILE_SUFFIX = ".upload.tmp";

	private final UploadFileHeader header;
	private final UploadSetting setting;

	private int size = -1;

	// 文件流（小文件位于内存中）
	private byte[] data;
	// 临时文件（大文件位于临时文件夹中）
	private File tempFile;

	/**
	 * 构造
	 *
	 * @param header  头部信息
	 * @param setting 上传设置
	 */
	public UploadFile(UploadFileHeader header, UploadSetting setting) {
		this.header = header;
		this.setting = setting;
	}

	// ---------------------------------------------------------------- operations

	/**
	 * 从磁盘或者内存中删除这个文件
	 */
	public void delete() {
		if (tempFile != null) {
			//noinspection ResultOfMethodCallIgnored
			tempFile.delete();
		}
		if (data != null) {
			data = null;
		}
	}

	/**
	 * 将上传的文件写入指定的目标文件路径，自动创建文件<br>
	 * 写入后原临时文件会被删除
	 *
	 * @param destPath 目标文件路径
	 * @return 目标文件
	 * @throws IOException IO异常
	 */
	public File write(String destPath) throws IOException {
		if (data != null || tempFile != null) {
			return write(FileUtil.file(destPath));
		}
		return null;
	}

	/**
	 * 将上传的文件写入目标文件<br>
	 * 写入后原临时文件会被删除
	 *
	 * @param destination 目标文件
	 * @return 目标文件
	 * @throws IOException IO异常
	 */
	public File write(File destination) throws IOException {
		assertValid();

		if (destination.isDirectory() == true) {
			destination = new File(destination, this.header.getFileName());
		}
		if (data != null) {
			FileUtil.writeBytes(data, destination);
			data = null;
		} else {
			if (tempFile != null) {
				FileUtil.move(tempFile, destination, true);
			}
		}
		return destination;
	}

	/**
	 * @return 获得文件字节流
	 * @throws IOException IO异常
	 */
	public byte[] getFileContent() throws IOException {
		assertValid();

		if (data != null) {
			return data;
		}
		if (tempFile != null) {
			return FileUtil.readBytes(tempFile);
		}
		return null;
	}

	/**
	 * @return 获得文件流
	 * @throws IOException IO异常
	 */
	public InputStream getFileInputStream() throws IOException {
		assertValid();

		if (data != null) {
			return IoUtil.toBuffered(IoUtil.toStream(this.data));
		}
		if (tempFile != null) {
			return IoUtil.toBuffered(IoUtil.toStream(this.tempFile));
		}
		return null;
	}

	// ---------------------------------------------------------------- header

	/**
	 * @return 上传文件头部信息
	 */
	public UploadFileHeader getHeader() {
		return header;
	}

	/**
	 * @return 文件名
	 */
	public String getFileName() {
		return header == null ? null : header.getFileName();
	}

	// ---------------------------------------------------------------- properties

	/**
	 * @return 上传文件的大小，&gt; 0 表示未上传
	 */
	public int size() {
		return size;
	}

	/**
	 * @return 是否上传成功
	 */
	public boolean isUploaded() {
		return size > 0;
	}

	/**
	 * @return 文件是否在内存中
	 */
	public boolean isInMemory() {
		return data != null;
	}

	// ---------------------------------------------------------------- process

	/**
	 * 处理上传表单流，提取出文件
	 *
	 * @param input 上传表单的流
	 * @return 是否成功
	 * @throws IOException IO异常
	 */
	protected boolean processStream(MultipartRequestInputStream input) throws IOException {
		if (!isAllowedExtension()) {
			// 非允许的扩展名
			size = input.skipToBoundary();
			return false;
		}
		size = 0;

		// 处理内存文件
		int memoryThreshold = setting.memoryThreshold;
		if (memoryThreshold > 0) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(memoryThreshold);
			int written = input.copy(baos, memoryThreshold);
			data = baos.toByteArray();
			if (written <= memoryThreshold) {
				// 文件存放于内存
				size = data.length;
				return true;
			}
		}

		// 处理硬盘文件
		tempFile = FileUtil.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX, FileUtil.touch(setting.tmpUploadPath), false);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
		if (data != null) {
			size = data.length;
			out.write(data);
			data = null; // not needed anymore
		}
		int maxFileSize = setting.maxFileSize;
		try {
			if (maxFileSize == -1) {
				size += input.copy(out);
				return true;
			}
			size += input.copy(out, maxFileSize - size + 1); // one more byte to detect larger files
			if (size > maxFileSize) {
				// 超出上传大小限制
				//noinspection ResultOfMethodCallIgnored
				tempFile.delete();
				tempFile = null;
				input.skipToBoundary();
				return false;
			}
		} finally {
			IoUtil.close(out);
		}
		// if (getFileName().length() == 0 && size == 0) {
		// size = -1;
		// }
		return true;
	}

	// ---------------------------------------------------------------------------- Private method start

	/**
	 * @return 是否为允许的扩展名
	 */
	private boolean isAllowedExtension() {
		String[] exts = setting.fileExts;
		boolean isAllow = setting.isAllowFileExts;
		if (exts == null || exts.length == 0) {
			// 如果给定扩展名列表为空，当允许扩展名时全部允许，否则全部禁止
			return isAllow;
		}

		String fileNameExt = FileUtil.extName(this.getFileName());
		for (String fileExtension : setting.fileExts) {
			if (fileNameExt.equalsIgnoreCase(fileExtension)) {
				return isAllow;
			}
		}

		// 未匹配到扩展名，如果为允许列表，返回false， 否则true
		return !isAllow;
	}

	/**
	 * 断言是否文件流可用
	 *
	 * @throws IOException IO异常
	 */
	private void assertValid() throws IOException {
		if (false == isUploaded()) {
			throw new IOException(StrUtil.format("File [{}] upload fail", getFileName()));
		}
	}
	// ---------------------------------------------------------------------------- Private method end
}
