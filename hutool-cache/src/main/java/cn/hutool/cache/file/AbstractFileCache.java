package cn.hutool.cache.file;

import java.io.File;
import java.io.Serializable;

import cn.hutool.cache.Cache;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;

/**
 * 文件缓存，以解决频繁读取文件引起的性能问题
 * @author Looly
 *
 */
public abstract class AbstractFileCache implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 容量 */
	protected final int capacity;
	/** 缓存的最大文件大小，文件大于此大小时将不被缓存 */
	protected final int maxFileSize;
	/** 默认超时时间，0表示无默认超时 */
	protected final long timeout;
	/** 缓存实现 */
	protected final Cache<File, byte[]> cache;

	/** 已使用缓存空间 */
	protected int usedSize;

	/**
	 * 构造
	 * @param capacity 缓存容量
	 * @param maxFileSize 文件最大大小
	 * @param timeout 默认超时时间，0表示无默认超时
	 */
	public AbstractFileCache(int capacity, int maxFileSize, long timeout) {
		this.capacity = capacity;
		this.maxFileSize = maxFileSize;
		this.timeout = timeout;
		this.cache = initCache();
	}

	/**
	 * @return 缓存容量（byte数）
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * @return 已使用空间大小（byte数）
	 */
	public int getUsedSize() {
		return usedSize;
	}

	/**
	 * @return 允许被缓存文件的最大byte数
	 */
	public int maxFileSize() {
		return maxFileSize;
	}

	/**
	 * @return 缓存的文件数
	 */
	public int getCachedFilesCount() {
		return cache.size();
	}

	/**
	 * @return 超时时间
	 */
	public long timeout() {
		return this.timeout;
	}

	/**
	 * 清空缓存
	 */
	public void clear() {
		cache.clear();
		usedSize = 0;
	}

	// ---------------------------------------------------------------- get

	/**
	 * 获得缓存过的文件bytes
	 * @param path 文件路径
	 * @return 缓存过的文件bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] getFileBytes(String path) throws IORuntimeException {
		return getFileBytes(new File(path));
	}

	/**
	 * 获得缓存过的文件bytes
	 * @param file 文件
	 * @return 缓存过的文件bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] getFileBytes(File file) throws IORuntimeException {
		byte[] bytes = cache.get(file);
		if (bytes != null) {
			return bytes;
		}

		// add file
		bytes = FileUtil.readBytes(file);

		if ((maxFileSize != 0) && (file.length() > maxFileSize)) {
			//大于缓存空间，不缓存，直接返回
			return bytes;
		}

		usedSize += bytes.length;

		//文件放入缓存，如果usedSize > capacity，purge()方法将被调用
		cache.put(file, bytes);

		return bytes;
	}

	// ---------------------------------------------------------------- protected method start
	/**
	 * 初始化实现文件缓存的缓存对象
	 * @return {@link Cache}
	 */
	protected abstract Cache<File, byte[]> initCache();
	// ---------------------------------------------------------------- protected method end

}
