package cn.hutool.core.io.copy;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * {@link ReadableByteChannel} 向 {@link WritableByteChannel} 拷贝
 *
 * @author looly
 * @since 5.7.8
 */
public class ChannelCopier extends IoCopier<ReadableByteChannel, WritableByteChannel> {

	/**
	 * 构造
	 */
	public ChannelCopier() {
		this(IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 */
	public ChannelCopier(int bufferSize) {
		this(bufferSize, -1);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 * @param count      拷贝总数
	 */
	public ChannelCopier(int bufferSize, long count) {
		this(bufferSize, count, null);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 * @param count      拷贝总数
	 * @param progress   进度条
	 */
	public ChannelCopier(int bufferSize, long count, StreamProgress progress) {
		super(bufferSize, count, progress);
	}

	@Override
	public long copy(ReadableByteChannel source, WritableByteChannel target) {
		Assert.notNull(source, "InputStream is null !");
		Assert.notNull(target, "OutputStream is null !");

		final StreamProgress progress = this.progress;
		if (null != progress) {
			progress.start();
		}
		final long size;
		try {
			size = doCopy(source, target, ByteBuffer.allocate(bufferSize(this.count)), progress);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		if (null != progress) {
			progress.finish();
		}
		return size;
	}

	/**
	 * 执行拷贝，如果限制最大长度，则按照最大长度读取，否则一直读取直到遇到-1
	 *
	 * @param source   {@link InputStream}
	 * @param target   {@link OutputStream}
	 * @param buffer   缓存
	 * @param progress 进度条
	 * @return 拷贝总长度
	 * @throws IOException IO异常
	 */
	private long doCopy(ReadableByteChannel source, WritableByteChannel target, ByteBuffer buffer, StreamProgress progress) throws IOException {
		long numToRead = this.count > 0 ? this.count : Long.MAX_VALUE;
		long total = 0;

		int read;
		while (numToRead > 0) {
			read = source.read(buffer);
			if (read < 0) {
				// 提前读取到末尾
				break;
			}
			buffer.flip();// 写转读
			target.write(buffer);
			buffer.clear();

			numToRead -= read;
			total += read;
			if (null != progress) {
				progress.progress(total);
			}
		}

		return total;
	}
}
