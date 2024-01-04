package cn.hutool.crypto.symmetric;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 国密对称堆成加密算法SM4实现
 *
 * <p>
 * 国密算法包括：
 * <ol>
 *     <li>非对称加密和签名：SM2</li>
 *     <li>摘要签名算法：SM3</li>
 *     <li>对称加密：SM4</li>
 * </ol>
 *
 * @author Looly
 * @since 4.6.8
 */
public class SM4 extends BCCipher {
	private static final long serialVersionUID = 1L;

	public static final String ALGORITHM_NAME = "SM4/ECB/PKCS7Padding";

	//------------------------------------------------------------------------- Constrctor start
	/**
	 * 构造，使用随机密钥
	 */
	public SM4() {
		super(ALGORITHM_NAME, new KeyParameter(RandomUtil.randomBytes(16)));
	}

	/**
	 * 构造
	 *
	 * @param key 密钥
	 */
	public SM4(byte[] key) {
		super(ALGORITHM_NAME, new KeyParameter(key));
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public SM4(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持密钥长度：128位
	 */
	public SM4(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持密钥长度：128位
	 * @param iv      偏移向量，加盐
	 */
	public SM4(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持密钥长度：128位
	 */
	public SM4(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, (IvParameterSpec) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持密钥长度：128位
	 * @param iv      偏移向量，加盐
	 */
	public SM4(Mode mode, Padding padding, SecretKey key, byte[] iv) {
		this(mode, padding, key, ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持密钥长度：128位
	 * @param iv      偏移向量，加盐
	 */
	public SM4(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 */
	public SM4(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持密钥长度：128位
	 */
	public SM4(String mode, String padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持密钥长度：128位
	 * @param iv      加盐
	 */
	public SM4(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding,//
				SecureUtil.generateKey(ALGORITHM_NAME, key),//
				ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持密钥长度：128位
	 */
	public SM4(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持密钥长度：128位
	 * @param iv      加盐
	 */
	public SM4(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("SM4/{}/{}", mode, padding),
			Mode.ECB.name().equalsIgnoreCase(mode) ?
				new KeyParameter(key.getEncoded()) :
				new ParametersWithIV(new KeyParameter(key.getEncoded()),
					iv == null ?
						new byte[16] :
						iv.getIV()));
	}

	/**
	 * 构造 AEAD 参数
	 *
	 * @param parameters {@link AEADParameters}
	 */
	public SM4(AEADParameters parameters) {
		super("SM4/GCM/NoPadding", parameters);
	}
	//------------------------------------------------------------------------- Constrctor end
}
