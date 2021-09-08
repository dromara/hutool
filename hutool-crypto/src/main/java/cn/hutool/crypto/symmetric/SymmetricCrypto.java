package cn.hutool.crypto.symmetric;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CipherMode;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对称加密算法<br>
 * 在对称加密算法中，数据发信方将明文（原始数据）和加密密钥一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。<br>
 * 收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。<br>
 * 在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，这就要求解密方事先必须知道加密密钥。<br>
 *
 * @author Looly
 */
public class SymmetricCrypto implements SymmetricEncryptor, SymmetricDecryptor, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SecretKey 负责保存对称密钥
	 */
	private SecretKey secretKey;
	/**
	 * Cipher负责完成加密或解密工作
	 */
	private Cipher cipher;
	/**
	 * 加密解密参数
	 */
	private AlgorithmParameterSpec params;
	/**
	 * 是否0填充
	 */
	private boolean isZeroPadding;
	private final Lock lock = new ReentrantLock();

	// ------------------------------------------------------------------ Constructor start

	/**
	 * 构造，使用随机密钥
	 *
	 * @param algorithm {@link SymmetricAlgorithm}
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm) {
		this(algorithm, (byte[]) null);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param algorithm 算法，可以是"algorithm/mode/padding"或者"algorithm"
	 */
	public SymmetricCrypto(String algorithm) {
		this(algorithm, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link SymmetricAlgorithm}
	 * @param key       自定义KEY
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm, byte[] key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法 {@link SymmetricAlgorithm}
	 * @param key       自定义KEY
	 * @since 3.1.2
	 */
	public SymmetricCrypto(SymmetricAlgorithm algorithm, SecretKey key) {
		this(algorithm.getValue(), key);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 */
	public SymmetricCrypto(String algorithm, byte[] key) {
		this(algorithm, KeyUtil.generateKey(algorithm, key));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 3.1.2
	 */
	public SymmetricCrypto(String algorithm, SecretKey key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm  算法
	 * @param key        密钥
	 * @param paramsSpec 算法参数，例如加盐等
	 * @since 3.3.0
	 */
	public SymmetricCrypto(String algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
		init(algorithm, key);
		initParams(algorithm, paramsSpec);
	}

	// ------------------------------------------------------------------ Constructor end

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥，如果为{@code null}自动生成一个key
	 * @return SymmetricCrypto的子对象，即子对象自身
	 */
	public SymmetricCrypto init(String algorithm, SecretKey key) {
		Assert.notBlank(algorithm, "'algorithm' must be not blank !");
		this.secretKey = key;

		// 检查是否为ZeroPadding，是则替换为NoPadding，并标记以便单独处理
		if (algorithm.contains(Padding.ZeroPadding.name())) {
			algorithm = StrUtil.replace(algorithm, Padding.ZeroPadding.name(), Padding.NoPadding.name());
			this.isZeroPadding = true;
		}

		this.cipher = SecureUtil.createCipher(algorithm);
		return this;
	}

	/**
	 * 获得对称密钥
	 *
	 * @return 获得对称密钥
	 */
	public SecretKey getSecretKey() {
		return secretKey;
	}

	/**
	 * 获得加密或解密器
	 *
	 * @return 加密或解密
	 */
	public Cipher getCipher() {
		return cipher;
	}

	/**
	 * 设置 {@link AlgorithmParameterSpec}，通常用于加盐或偏移向量
	 *
	 * @param params {@link AlgorithmParameterSpec}
	 * @return 自身
	 */
	public SymmetricCrypto setParams(AlgorithmParameterSpec params) {
		this.params = params;
		return this;
	}

	/**
	 * 设置偏移向量
	 *
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public SymmetricCrypto setIv(IvParameterSpec iv) {
		setParams(iv);
		return this;
	}

	/**
	 * 设置偏移向量
	 *
	 * @param iv 偏移向量，加盐
	 * @return 自身
	 */
	public SymmetricCrypto setIv(byte[] iv) {
		setIv(new IvParameterSpec(iv));
		return this;
	}

	// --------------------------------------------------------------------------------- Update

	/**
	 * 初始化模式并清空数据
	 *
	 * @param mode 模式枚举
	 * @return this
	 * @since 5.7.12
	 */
	public SymmetricCrypto setMode(CipherMode mode){
		lock.lock();
		try {
			initMode(mode.getValue());
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
		return this;
	}

	/**
	 * 更新数据，分组加密中间结果可以当作随机数<br>
	 * 第一次更新数据前需要调用{@link #setMode(CipherMode)}初始化加密或解密模式，然后每次更新数据都是累加模式
	 *
	 * @param data 被加密的bytes
	 * @return update之后的bytes
	 * @since 5.6.8
	 */
	public byte[] update(byte[] data) {
		lock.lock();
		try {
			return cipher.update(paddingDataWithZero(data, cipher.getBlockSize()));
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 更新数据，分组加密中间结果可以当作随机数<br>
	 * 第一次更新数据前需要调用{@link #setMode(CipherMode)}初始化加密或解密模式，然后每次更新数据都是累加模式
	 *
	 * @param data 被加密的bytes
	 * @return update之后的hex数据
	 * @since 5.6.8
	 */
	public String updateHex(byte[] data) {
		return HexUtil.encodeHexStr(update(data));
	}

	// --------------------------------------------------------------------------------- Encrypt

	@Override
	public byte[] encrypt(byte[] data) {
		lock.lock();
		try {
			final Cipher cipher = initMode(Cipher.ENCRYPT_MODE);
			return cipher.doFinal(paddingDataWithZero(data, cipher.getBlockSize()));
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		lock.lock();
		CipherOutputStream cipherOutputStream = null;
		try {
			final Cipher cipher = initMode(Cipher.ENCRYPT_MODE);
			cipherOutputStream = new CipherOutputStream(out, cipher);
			long length = IoUtil.copy(data, cipherOutputStream);
			if (this.isZeroPadding) {
				final int blockSize = cipher.getBlockSize();
				if (blockSize > 0) {
					// 按照块拆分后的数据中多余的数据
					final int remainLength = (int) (length % blockSize);
					if (remainLength > 0) {
						// 补充0
						cipherOutputStream.write(new byte[blockSize - remainLength]);
						cipherOutputStream.flush();
					}
				}
			}
		} catch (IORuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
			if (isClose) {
				IoUtil.close(data);
				IoUtil.close(cipherOutputStream);
			}
		}
	}

	// --------------------------------------------------------------------------------- Decrypt

	@Override
	public byte[] decrypt(byte[] bytes) {
		final int blockSize;
		final byte[] decryptData;

		lock.lock();
		try {
			final Cipher cipher = initMode(Cipher.DECRYPT_MODE);
			blockSize = cipher.getBlockSize();
			decryptData = cipher.doFinal(bytes);
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}

		return removePadding(decryptData, blockSize);
	}

	@Override
	public void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		lock.lock();
		CipherInputStream cipherInputStream = null;
		try {
			final Cipher cipher = initMode(Cipher.DECRYPT_MODE);
			cipherInputStream = new CipherInputStream(data, cipher);
			if (this.isZeroPadding) {
				final int blockSize = cipher.getBlockSize();
				if (blockSize > 0) {
					copyForZeroPadding(cipherInputStream, out, blockSize);
					return;
				}
			}
			IoUtil.copy(cipherInputStream, out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (IORuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
			if (isClose) {
				IoUtil.close(data);
				IoUtil.close(cipherInputStream);
			}
		}
	}

	// --------------------------------------------------------------------------------- Getters

	// --------------------------------------------------------------------------------- Private method start

	/**
	 * 初始化加密解密参数，如IV等
	 *
	 * @param algorithm  算法
	 * @param paramsSpec 用户定义的{@link AlgorithmParameterSpec}
	 * @return this
	 * @since 5.7.11
	 */
	private SymmetricCrypto initParams(String algorithm, AlgorithmParameterSpec paramsSpec) {
		if (null == paramsSpec) {
			byte[] iv = null;
			final Cipher cipher = this.cipher;
			if (null != cipher) {
				iv = cipher.getIV();
			}

			// 随机IV
			if (StrUtil.startWithIgnoreCase(algorithm, "PBE")) {
				// 对于PBE算法使用随机数加盐
				if (null == iv) {
					iv = RandomUtil.randomBytes(8);
				}
				paramsSpec = new PBEParameterSpec(iv, 100);
			} else if (StrUtil.startWithIgnoreCase(algorithm, "AES")) {
				if (null != iv) {
					//AES使用Cipher默认的随机盐
					paramsSpec = new IvParameterSpec(iv);
				}
			}
		}

		return setParams(paramsSpec);
	}

	/**
	 * 初始化{@link Cipher}为加密或者解密模式
	 *
	 * @param mode 模式，见{@link Cipher#ENCRYPT_MODE} 或 {@link Cipher#DECRYPT_MODE}
	 * @return {@link Cipher}
	 * @throws InvalidKeyException                无效key
	 * @throws InvalidAlgorithmParameterException 无效算法
	 */
	private Cipher initMode(int mode) throws InvalidKeyException, InvalidAlgorithmParameterException {
		final Cipher cipher = this.cipher;
		if (null == this.params) {
			cipher.init(mode, secretKey);
		} else {
			cipher.init(mode, secretKey, params);
		}
		return cipher;
	}

	/**
	 * 数据按照blockSize的整数倍长度填充填充0
	 *
	 * <p>
	 * 在{@link Padding#ZeroPadding} 模式下，且数据长度不是blockSize的整数倍才有效，否则返回原数据
	 *
	 * <p>
	 * 见：https://blog.csdn.net/OrangeJack/article/details/82913804
	 *
	 * @param data      数据
	 * @param blockSize 块大小
	 * @return 填充后的数据，如果isZeroPadding为false或长度刚好，返回原数据
	 * @since 4.6.7
	 */
	private byte[] paddingDataWithZero(byte[] data, int blockSize) {
		if (this.isZeroPadding) {
			final int length = data.length;
			// 按照块拆分后的数据中多余的数据
			final int remainLength = length % blockSize;
			if (remainLength > 0) {
				// 新长度为blockSize的整数倍，多余部分填充0
				return ArrayUtil.resize(data, length + blockSize - remainLength);
			}
		}
		return data;
	}

	/**
	 * 数据按照blockSize去除填充部分，用于解密
	 *
	 * <p>
	 * 在{@link Padding#ZeroPadding} 模式下，且数据长度不是blockSize的整数倍才有效，否则返回原数据
	 *
	 * @param data      数据
	 * @param blockSize 块大小，必须大于0
	 * @return 去除填充后的数据，如果isZeroPadding为false或长度刚好，返回原数据
	 * @since 4.6.7
	 */
	private byte[] removePadding(byte[] data, int blockSize) {
		if (this.isZeroPadding && blockSize > 0) {
			final int length = data.length;
			final int remainLength = length % blockSize;
			if (remainLength == 0) {
				// 解码后的数据正好是块大小的整数倍，说明可能存在补0的情况，去掉末尾所有的0
				int i = length - 1;
				while (i >= 0 && 0 == data[i]) {
					i--;
				}
				return ArrayUtil.resize(data, i + 1);
			}
		}
		return data;
	}

	/**
	 * 拷贝解密后的流
	 *
	 * @param in        {@link CipherInputStream}
	 * @param out       输出流
	 * @param blockSize 块大小
	 * @throws IOException IO异常
	 */
	private static void copyForZeroPadding(CipherInputStream in, OutputStream out, int blockSize) throws IOException {
		int n = 1;
		if (IoUtil.DEFAULT_BUFFER_SIZE > blockSize) {
			n = Math.max(n, IoUtil.DEFAULT_BUFFER_SIZE / blockSize);
		}
		// 此处缓存buffer使用blockSize的整数倍，方便读取时可以正好将补位的0读在一个buffer中
		final int bufSize = blockSize * n;
		final byte[] preBuffer = new byte[bufSize];
		final byte[] buffer = new byte[bufSize];

		boolean isFirst = true;
		int preReadSize = 0;
		for (int readSize; (readSize = in.read(buffer)) != IoUtil.EOF; ) {
			if (isFirst) {
				isFirst = false;
			} else {
				// 将前一批数据写出
				out.write(preBuffer, 0, preReadSize);
			}
			ArrayUtil.copy(buffer, preBuffer, readSize);
			preReadSize = readSize;
		}
		// 去掉末尾所有的补位0
		int i = preReadSize - 1;
		while (i >= 0 && 0 == preBuffer[i]) {
			i--;
		}
		out.write(preBuffer, 0, i + 1);
		out.flush();
	}
	// --------------------------------------------------------------------------------- Private method end
}
