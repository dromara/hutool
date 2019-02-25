package cn.hutool.crypto.digest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BouncyCastleSupport;
import cn.hutool.crypto.CryptoException;

/**
 * 摘要算法<br>
 * 注意：此对象实例化后为非线程安全！
 * 
 * @author Looly
 *
 */
public class Digester extends BouncyCastleSupport {

	private MessageDigest digest;
	/** 盐值 */
	protected byte[] salt;
	/** 散列次数 */
	protected int digestCount;

	// ------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param algorithm 算法枚举
	 */
	public Digester(DigestAlgorithm algorithm) {
		this(algorithm.getValue());
	}

	/**
	 * 构造
	 * 
	 * @param algorithm 算法
	 * @since 3.3.0
	 */
	public Digester(String algorithm) {
		init(algorithm);
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 初始化
	 * 
	 * @param algorithm 算法
	 * @return {@link Digester}
	 * @throws CryptoException Cause by IOException
	 */
	public Digester init(String algorithm) {
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
		return this;
	}

	/**
	 * 设置加盐内容
	 * 
	 * @param salt 盐值
	 * @return this
	 * @since 4.4.3
	 */
	public Digester setSalt(byte[] salt) {
		this.salt = salt;
		return this;
	}

	/**
	 * 设置重复计算摘要值次数
	 * 
	 * @param digestCount 摘要值次数
	 * @return this
	 */
	public Digester setDigestCount(int digestCount) {
		this.digestCount = digestCount;
		return this;
	}

	// ------------------------------------------------------------------------------------------- Digest
	/**
	 * 生成文件摘要
	 * 
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return 摘要
	 */
	public byte[] digest(String data, String charset) {
		return digest(StrUtil.bytes(data, charset));
	}

	/**
	 * 生成文件摘要
	 * 
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public byte[] digest(String data) {
		return digest(data, CharsetUtil.UTF_8);
	}

	/**
	 * 生成文件摘要，并转为16进制字符串
	 * 
	 * @param data 被摘要数据
	 * @param charset 编码
	 * @return 摘要
	 */
	public String digestHex(String data, String charset) {
		return HexUtil.encodeHexStr(digest(data, charset));
	}

	/**
	 * 生成文件摘要
	 * 
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(String data) {
		return digestHex(data, CharsetUtil.UTF_8);
	}

	/**
	 * 生成文件摘要<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param file 被摘要文件
	 * @return 摘要bytes
	 * @throws CryptoException Cause by IOException
	 */
	public byte[] digest(File file) throws CryptoException {
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return digest(in);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 生成文件摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param file 被摘要文件
	 * @return 摘要
	 */
	public String digestHex(File file) {
		return HexUtil.encodeHexStr(digest(file));
	}

	/**
	 * 生成摘要，考虑加盐和重复摘要次数
	 * 
	 * @param data 数据bytes
	 * @return 摘要bytes
	 */
	public byte[] digest(byte[] data) {
		if (null != this.salt) {
			digest.update(this.salt);
		}
		int digestCount = Math.max(1, this.digestCount);
		byte[] result = data;
		for (int i = 0; i < digestCount; i++) {
			result = doDigest(result);
		}
		return result;
	}

	/**
	 * 生成摘要
	 * 
	 * @param data 数据bytes
	 * @return 摘要bytes
	 * @since 4.4.3
	 */
	private byte[] doDigest(byte[] data) {
		byte[] result;
		try {
			result = digest.digest(data);
		} finally {
			digest.reset();
		}
		return result;
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 * 
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(byte[] data) {
		return HexUtil.encodeHexStr(digest(data));
	}

	/**
	 * 生成摘要，使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param data {@link InputStream} 数据流
	 * @return 摘要bytes
	 */
	public byte[] digest(InputStream data) {
		return digest(data, IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param data 被摘要数据
	 * @return 摘要
	 */
	public String digestHex(InputStream data) {
		return HexUtil.encodeHexStr(digest(data));
	}

	/**
	 * 生成摘要
	 * 
	 * @param data {@link InputStream} 数据流
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要bytes
	 * @throws CryptoException IO异常
	 */
	public byte[] digest(InputStream data, int bufferLength) throws CryptoException {
		if (bufferLength < 1) {
			bufferLength = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		byte[] buffer = new byte[bufferLength];

		byte[] result = null;
		try {
			int read = data.read(buffer, 0, bufferLength);

			while (read > -1) {
				digest.update(buffer, 0, read);
				read = data.read(buffer, 0, bufferLength);
			}
			result = digest.digest();
		} catch (IOException e) {
			throw new CryptoException(e);
		} finally {
			digest.reset();
		}
		return result;
	}

	/**
	 * 生成摘要，并转为16进制字符串<br>
	 * 使用默认缓存大小，见 {@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * 
	 * @param data 被摘要数据
	 * @param bufferLength 缓存长度，不足1使用 {@link IoUtil#DEFAULT_BUFFER_SIZE} 做为默认值
	 * @return 摘要
	 */
	public String digestHex(InputStream data, int bufferLength) {
		return HexUtil.encodeHexStr(digest(data, bufferLength));
	}

	/**
	 * 获得 {@link MessageDigest}
	 * 
	 * @return {@link MessageDigest}
	 */
	public MessageDigest getDigest() {
		return digest;
	}

	/**
	 * 获取散列长度，0表示不支持此方法
	 * 
	 * @return 散列长度，0表示不支持此方法
	 * @since 4.5.0
	 */
	public int getDigestLength() {
		return this.digest.getDigestLength();
	}
}
