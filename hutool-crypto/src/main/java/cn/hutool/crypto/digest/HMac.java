package cn.hutool.crypto.digest;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.digest.mac.MacEngine;
import cn.hutool.crypto.digest.mac.MacEngineFactory;

import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Key;

/**
 * HMAC摘要算法<br>
 * HMAC，全称为“Hash Message Authentication Code”，中文名“散列消息鉴别码”<br>
 * 主要是利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。<br>
 * 一般的，消息鉴别码用于验证传输于两个共 同享有一个密钥的单位之间的消息。<br>
 * HMAC 可以与任何迭代散列函数捆绑使用。MD5 和 SHA-1 就是这种散列函数。HMAC 还可以使用一个用于计算和确认消息鉴别值的密钥。<br>
 * 注意：此对象实例化后为非线程安全！
 * @author Looly
 *
 */
public class HMac implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final MacEngine engine;
	
	// ------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，自动生成密钥
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 */
	public HMac(HmacAlgorithm algorithm) {
		this(algorithm, (Key)null);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key 密钥
	 */
	public HMac(HmacAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法 {@link HmacAlgorithm}
	 * @param key 密钥
	 */
	public HMac(HmacAlgorithm algorithm, Key key) {
		this(algorithm.getValue(), key);
	}
	
	/**
	 * 构造
	 * @param algorithm 算法
	 * @param key 密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, byte[] key) {
		this(algorithm, new SecretKeySpec(key, algorithm));
	}
	
	/**
	 * 构造
	 * @param algorithm 算法
	 * @param key 密钥
	 * @since 4.5.13
	 */
	public HMac(String algorithm, Key key) {
		this(MacEngineFactory.createEngine(algorithm, key));
	}
	
	/**
	 * 构造
	 * @param engine MAC算法实现引擎
	 * @since 4.5.13
	 */
	public HMac(MacEngine engine) {
		this.engine = engine;
	}
	// ------------------------------------------------------------------------------------------- Constructor end
	
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
	public byte[] digest(File file) throws CryptoException{
		InputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return digest(in);
		} finally{
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
	 * 生成摘要
	 * 
	 * @param data 数据bytes
	 * @return 摘要bytes
	 */
	public byte[] digest(byte[] data) {
		return digest(new ByteArrayInputStream(data), -1);
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
	 */
	public byte[] digest(InputStream data, int bufferLength) {
		return this.engine.digest(data, bufferLength);
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
	 * 获取MAC算法块长度
	 * @return MAC算法块长度
	 * @since 5.3.3
	 */
	public int getMacLength(){
		return this.engine.getMacLength();
	}

	/**
	 * 获取算法
	 *
	 * @return 算法
	 * @since 5.3.3
	 */
	public String getAlgorithm() {
		return this.engine.getAlgorithm();
	}
}
