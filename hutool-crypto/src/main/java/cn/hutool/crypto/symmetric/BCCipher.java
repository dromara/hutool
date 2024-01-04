package cn.hutool.crypto.symmetric;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.CipherMode;
import cn.hutool.crypto.CryptoException;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.*;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 绕过 JCE 完全基于 BC 库的对称加/解密算法实现，可以在不加载 BC Provider 的情况下执行加解密运算
 * 这可以解决重打包和 graalvm 的签名校验问题
 * <p>
 * 支持
 * AES/SM4/Blowfish/DESede 算法
 * ECB/CBC/CFB/OFB/CTR/GCM 模式
 * NoPadding/PKCS5Padding/PKCS7Padding 填充
 *
 * @author changhr2013
 */
public class BCCipher implements SymmetricEncryptor, SymmetricDecryptor, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 表示当前的算法
	 */
	private String algorithm;

	/**
	 * 算法的参数
	 */
	private CipherParameters parameters;

	private BufferedBlockCipher blockCipher;

	private final Lock lock = new ReentrantLock();

	/**
	 * 获取当前的算法
	 *
	 * @return 算法/模式/填充
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * 获取当前的加/解密密钥
	 *
	 * @return {@link SecretKeySpec}
	 */
	public SecretKey getSecretKey() {
		String keyAlgo = algorithm.split("/")[0];

		if (parameters instanceof KeyParameter) {
			return new SecretKeySpec(((KeyParameter) parameters).getKey(), keyAlgo);
		} else if (parameters instanceof ParametersWithIV) {
			return new SecretKeySpec(((KeyParameter) ((ParametersWithIV) parameters).getParameters()).getKey(), keyAlgo);
		} else if (parameters instanceof AEADParameters) {
			return new SecretKeySpec(((AEADParameters) parameters).getKey().getKey(), keyAlgo);
		} else {
			throw new CryptoException("unsupported algorithm parameters " + parameters.getClass().getName());
		}
	}

	/**
	 * 设置 {@link CipherParameters}，通常用于加盐或偏移向量
	 *
	 * @param params {@link CipherParameters}
	 * @return 自身
	 */
	public BCCipher setParams(CipherParameters params) {
		this.parameters = params;
		return this;
	}

	/**
	 * 设置偏移向量
	 *
	 * @param iv {@link IvParameterSpec}偏移向量
	 * @return 自身
	 */
	public BCCipher setIv(IvParameterSpec iv) {
		return setIv(iv.getIV());
	}

	/**
	 * 动态设置当前 BlockCipher 参数的 IV 值
	 *
	 * @param iv IV
	 */
	public BCCipher setIv(byte[] iv) {
		if (parameters instanceof ParametersWithIV) {
			parameters = new ParametersWithIV(((ParametersWithIV) parameters).getParameters(), iv);
		} else if (parameters instanceof AEADParameters) {
			AEADParameters aeadParameters = (AEADParameters) parameters;
			parameters = new AEADParameters(aeadParameters.getKey(), aeadParameters.getMacSize(), iv, aeadParameters.getAssociatedText());
		} else {
			// ignore KeyParameter
		}
		return this;
	}

	/**
	 * 初始化模式并清空数据
	 *
	 * @param mode 模式枚举
	 * @return this
	 */
	public BCCipher setMode(CipherMode mode) {
		lock.lock();
		try {
			if (CipherMode.encrypt == mode) {
				this.blockCipher.init(true, this.parameters);
			} else if (CipherMode.decrypt == mode) {
				this.blockCipher.init(false, this.parameters);
			} else {
				throw new CryptoException("unsupported CipherMode " + mode.name());
			}
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
		}
		return this;
	}

	/**
	 * 构造
	 *
	 * @param algorithm  算法
	 * @param paramsSpec 算法参数，例如加盐等
	 */
	public BCCipher(String algorithm, CipherParameters paramsSpec) {
		Assert.notBlank(algorithm, "'algorithm' must be not blank !");

		this.blockCipher = getInstance(algorithm);
		this.algorithm = algorithm;
		this.parameters = paramsSpec;
	}

	@Override
	public byte[] decrypt(byte[] bytes) {
		lock.lock();
		try {
			// 初始化
			this.blockCipher.init(false, this.parameters);
			// 执行解密运算
			return process(bytes);
		} catch (Exception e) {
			throw new CryptoException("decrypt exception.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void decrypt(InputStream data, OutputStream out, boolean isClose) {
		lock.lock();
		CipherInputStream cipherInputStream = null;
		try {
			// 初始化
			this.blockCipher.init(false, this.parameters);
			// 构建输入流
			cipherInputStream = new CipherInputStream(data, this.blockCipher);
			IoUtil.copy(cipherInputStream, out);
		} catch (IORuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
			// issue#I4EMST@Gitee
			// CipherOutputStream必须关闭，才能完全写出
			IoUtil.close(cipherInputStream);
			if (isClose) {
				IoUtil.close(data);
			}
		}
	}

	@Override
	public byte[] encrypt(byte[] data) {
		lock.lock();
		try {
			// 初始化
			this.blockCipher.init(true, this.parameters);
			// 执行加密运算
			return process(data);
		} catch (Exception e) {
			throw new CryptoException("encrypt exception.", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void encrypt(InputStream data, OutputStream out, boolean isClose) {
		lock.lock();
		CipherOutputStream cipherOutputStream = null;
		try {
			// 初始化
			this.blockCipher.init(true, this.parameters);
			// 构建输出流
			cipherOutputStream = new CipherOutputStream(out, this.blockCipher);
			IoUtil.copy(data, cipherOutputStream);
		} catch (IORuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new CryptoException(e);
		} finally {
			lock.unlock();
			// issue#I4EMST@Gitee
			// CipherOutputStream必须关闭，才能完全写出
			IoUtil.close(cipherOutputStream);
			if (isClose) {
				IoUtil.close(data);
			}
		}
	}

	/**
	 * 使用当前的 BlockCipher 做运算
	 *
	 * @param data 待运算的数据
	 * @return 运算结果
	 */
	private byte[] process(byte[] data) {
		byte[] out;
		try {
			BufferedBlockCipher cipher = this.blockCipher;
			int updateOutputSize = cipher.getOutputSize(data.length);
			byte[] buf = new byte[updateOutputSize];
			int len = cipher.processBytes(data, 0, data.length, buf, 0);
			len += cipher.doFinal(buf, len);
			out = new byte[len];
			System.arraycopy(buf, 0, out, 0, len);
		} catch (Exception e) {
			throw new CryptoException("encrypt/decrypt process exception.", e);
		}
		return out;
	}

	/**
	 * 获取 BlockCipher
	 *
	 * @param cipherAlgorithm 算法
	 * @return BufferedBlockCipher
	 */
	private static BufferedBlockCipher getInstance(String cipherAlgorithm) {

		String[] transform = cipherAlgorithm.split("/");
		if (transform.length < 3) {
			throw new RuntimeException("unsupported cipherAlgorithm format");
		}

		final String algorithm = transform[0];
		BlockCipher cipherEngine;
		if ("AES".equalsIgnoreCase(algorithm)) {
			cipherEngine = new AESEngine();
		} else if ("SM4".equalsIgnoreCase(algorithm)) {
			cipherEngine = new SM4Engine();
		} else if ("Blowfish".equalsIgnoreCase(algorithm)) {
			cipherEngine = new BlowfishEngine();
		} else if ("DESede".equalsIgnoreCase(algorithm)) {
			cipherEngine = new DESedeEngine();
		} else {
			throw new CryptoException("unsupported algorithm" + cipherAlgorithm);
		}

		final String mode = transform[1];
		BlockCipher blockCipher;
		if ("CBC".equalsIgnoreCase(mode)) {
			blockCipher = new CBCBlockCipher(cipherEngine);
		} else if ("CFB".equalsIgnoreCase(mode)) {
			blockCipher = new CFBBlockCipher(cipherEngine, cipherEngine.getBlockSize() * 8);
		} else if ("OFB".equalsIgnoreCase(mode)) {
			blockCipher = new OFBBlockCipher(cipherEngine, cipherEngine.getBlockSize() * 8);
		} else if ("CTR".equalsIgnoreCase(mode)) {
			blockCipher = new SICBlockCipher(cipherEngine);
		} else if ("ECB".equalsIgnoreCase(mode)) {
			blockCipher = cipherEngine;
		} else if ("GCM".equalsIgnoreCase(mode)) {
			blockCipher = new GCMBlockCipher(cipherEngine).getUnderlyingCipher();
		} else {
			throw new CryptoException("unsupported cipher algorithm");
		}

		final String padding = transform[2];
		if ("NoPadding".equalsIgnoreCase(padding)) {
			return new BufferedBlockCipher(blockCipher);
		} else if ("PKCS7Padding".equalsIgnoreCase(padding) || "PKCS5Padding".equalsIgnoreCase(padding)) {
			return new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
		} else {
			throw new CryptoException("unsupported padding" + cipherAlgorithm);
		}
	}

}
