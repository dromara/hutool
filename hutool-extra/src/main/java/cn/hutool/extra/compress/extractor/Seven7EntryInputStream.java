package cn.hutool.extra.compress.extractor;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 7z解压中文件流读取的封装
 *
 * @author looly
 * @since 5.5.0
 */
public class Seven7EntryInputStream extends InputStream {

	private final SevenZFile sevenZFile;
	private final long size;
	private long readSize = 0;

	/**
	 * 构造
	 * @param sevenZFile {@link SevenZFile}
	 * @param entry {@link SevenZArchiveEntry}
	 */
	public Seven7EntryInputStream(SevenZFile sevenZFile, SevenZArchiveEntry entry) {
		this.sevenZFile = sevenZFile;
		this.size = entry.getSize();
	}

	@Override
	public int available() throws IOException {
		try{
			return Math.toIntExact(this.size);
		} catch (ArithmeticException e){
			throw new IOException("Entry size is too large!(max than Integer.MAX)", e);
		}
	}

	@Override
	public int read() throws IOException {
		this.readSize++;
		return this.sevenZFile.read();
	}
}
