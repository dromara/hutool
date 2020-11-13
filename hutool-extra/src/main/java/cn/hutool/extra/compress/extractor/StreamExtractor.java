package cn.hutool.extra.compress.extractor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.compress.CompressException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StreamExtractor {

	private final ArchiveInputStream in;

	public StreamExtractor(Charset charset, InputStream in) {
		final ArchiveStreamFactory factory = new ArchiveStreamFactory(charset.name());
		try {
			this.in = factory.createArchiveInputStream(in);
		} catch (ArchiveException e) {
			throw new CompressException(e);
		}
	}

	/**
	 * 释放（解压）到指定目录
	 *
	 * @param targetDir 目标目录
	 */
	public void extract(File targetDir) {
		try {
			extractInternal(targetDir);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 释放（解压）到指定目录
	 *
	 * @param targetDir 目标目录
	 * @throws IOException IO异常
	 */
	private void extractInternal(File targetDir) throws IOException {
		Assert.isTrue(null != targetDir && targetDir.isDirectory(), "target must be dir.");
		final ArchiveInputStream in = this.in;
		ArchiveEntry entry;
		File outItemFile;
		while(null != (entry = this.in.getNextEntry())){
			if(false == in.canReadEntryData(entry)){
				// 无法读取的文件直接跳过
				continue;
			}
			outItemFile = FileUtil.file(targetDir, entry.getName());
			if(entry.isDirectory()){
				// 创建对应目录
				//noinspection ResultOfMethodCallIgnored
				outItemFile.mkdirs();
			} else {
				FileUtil.writeFromStream(in, outItemFile);
			}
		}
	}
}
