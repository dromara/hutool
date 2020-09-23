package cn.hutool.crypto.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * RC4加密解密算法实现<br>
 * 来自：https://github.com/xSAVIKx/RC4-cipher/blob/master/src/main/java/com/github/xsavikx/rc4/RC4.java
 *
 * @author Iurii Sergiichuk，Looly
 */
public class RC4 implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int SBOX_LENGTH = 256;
	/** 密钥最小长度 */
	private static final int KEY_MIN_LENGTH = 5;

	/** Sbox */
	private int[] sbox;
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 构造
	 * 
	 * @param key 密钥
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public RC4(String key) throws CryptoException {
		setKey(key);
	}

	/**
	 * 加密
	 *
	 * @param message 消息
	 * @param charset 编码
	 * @return 密文
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public byte[] encrypt(String message, Charset charset) throws CryptoException {
		return crypt(StrUtil.bytes(message, charset));
	}

	/**
	 * 加密，使用默认编码：UTF-8
	 *
	 * @param message 消息
	 * @return 密文
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public byte[] encrypt(String message) throws CryptoException {
		return encrypt(message, CharsetUtil.CHARSET_UTF_8);
	}
	
	/**
	 * 加密
	 * 
	 * @param data 数据
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	public String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(crypt(data));
	}

	/**
	 * 加密
	 * 
	 * @param data 数据
	 * @return 加密后的Base64
	 * @since 4.5.12
	 */
	public String encryptBase64(byte[] data) {
		return Base64.encode(crypt(data));
	}
	
	/**
	 * 加密
	 * 
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	public String encryptHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 * @since 5.4.4
	 */
	public String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 * 
	 * @param data 被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 * @since 4.5.12
	 */
	public String encryptBase64(String data, Charset charset) {
		return Base64.encode(encrypt(data, charset));
	}


	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 * @since 5.4.4
	 */
	public String encryptBase64(String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 解密
	 *
	 * @param message 消息
	 * @param charset 编码
	 * @return 明文
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public String decrypt(byte[] message, Charset charset) throws CryptoException {
		return StrUtil.str(crypt(message), charset);
	}

	/**
	 * 解密，使用默认编码UTF-8
	 *
	 * @param message 消息
	 * @return 明文
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public String decrypt(byte[] message) throws CryptoException {
		return decrypt(message, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串，使用默认编码UTF-8
	 *
	 * @param message 消息
	 * @return 明文
	 * @since 5.4.4
	 */
	public String decrypt(String message) {
		return decrypt(SecureUtil.decode(message));
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param message    明文
	 * @param charset 解密后的charset
	 * @return 明文
	 * @since 5.4.4
	 */
	public String decrypt(String message, Charset charset) {
		return StrUtil.str(decrypt(message), charset);
	}


	/**
	 * 加密或解密指定值，调用此方法前需初始化密钥
	 *
	 * @param msg 要加密或解密的消息
	 * @return 加密或解密后的值
	 */
	public byte[] crypt(final byte[] msg) {
		final ReadLock readLock = this.lock.readLock();
		byte[] code;
		readLock.lock();
		try {
			final int[] sbox = this.sbox.clone();
			code = new byte[msg.length];
			int i = 0;
			int j = 0;
			for (int n = 0; n < msg.length; n++) {
				i = (i + 1) % SBOX_LENGTH;
				j = (j + sbox[i]) % SBOX_LENGTH;
				swap(i, j, sbox);
				int rand = sbox[(sbox[i] + sbox[j]) % SBOX_LENGTH];
				code[n] = (byte) (rand ^ msg[n]);
			}
		} finally {
			readLock.unlock();
		}
		return code;
	}

	/**
	 * 设置密钥
	 *
	 * @param key 密钥
	 * @throws CryptoException key长度小于5或者大于255抛出此异常
	 */
	public void setKey(String key) throws CryptoException {
		final int length = key.length();
		if (length < KEY_MIN_LENGTH || length >= SBOX_LENGTH) {
			throw new CryptoException("Key length has to be between {} and {}", KEY_MIN_LENGTH, (SBOX_LENGTH - 1));
		}

		final WriteLock writeLock = this.lock.writeLock();
		writeLock.lock();
		try {
			this.sbox = initSBox(StrUtil.utf8Bytes(key));
		} finally {
			writeLock.unlock();
		}
	}

	//----------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 初始化Sbox
	 *
	 * @param key 密钥
	 * @return sbox
	 */
	private int[] initSBox(byte[] key) {
		int[] sbox = new int[SBOX_LENGTH];
		int j = 0;

		for (int i = 0; i < SBOX_LENGTH; i++) {
			sbox[i] = i;
		}

		for (int i = 0; i < SBOX_LENGTH; i++) {
			j = (j + sbox[i] + (key[i % key.length]) & 0xFF) % SBOX_LENGTH;
			swap(i, j, sbox);
		}
		return sbox;
	}

	/**
	 * 交换指定两个位置的值
	 * 
	 * @param i 位置1
	 * @param j 位置2
	 * @param sbox 数组
	 */
	private void swap(int i, int j, int[] sbox) {
		int temp = sbox[i];
		sbox[i] = sbox[j];
		sbox[j] = temp;
	}
	//----------------------------------------------------------------------------------------------------------------------- Private method end
}
