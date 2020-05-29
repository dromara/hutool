package cn.hutool.crypto.symmetric;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * AES加密算法实现<br>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称Rijndael加密法<br>
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7
 *
 * <p>
 * 相关概念说明：
 * <pre>
 * mode:    加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码，）在加密时对明文分组的模式，它代表了不同的分组方式
 * padding: 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 * iv:      在对明文分组加密时，会将明文分组与前一个密文分组进行XOR运算（即异或运算），但是加密第一个明文分组时不存在“前一个密文分组”，
 *          因此需要事先准备一个与分组长度相等的比特序列来代替，这个比特序列就是偏移量。
 * </pre>
 * <p>
 * 相关概念见：https://blog.csdn.net/OrangeJack/article/details/82913804
 *
 * @author Looly
 * @since 3.0.8
 */
public class AES extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	//------------------------------------------------------------------------- Constrctor start

	/**
	 * 构造，默认AES/ECB/PKCS5Padding，使用随机密钥
	 */
	public AES() {
		super(SymmetricAlgorithm.AES);
	}

	/**
	 * 构造，使用默认的AES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public AES(byte[] key) {
		super(SymmetricAlgorithm.AES, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public AES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, (IvParameterSpec) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * @since 4.6.7
	 */
	public AES(Mode mode, Padding padding, SecretKey key, byte[] iv) {
		this(mode, padding, key, ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      偏移向量，加盐
	 * @since 3.3.0
	 */
	public AES(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 */
	public AES(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      加盐
	 */
	public AES(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding,//
				SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key),//
				ArrayUtil.isEmpty(iv) ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 */
	public AES(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode    模式
	 * @param padding 补码方式
	 * @param key     密钥，支持三种密钥长度：128、192、256位
	 * @param iv      加盐
	 */
	public AES(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("AES/{}/{}", mode, padding), key, iv);
	}
	//------------------------------------------------------------------------- Constrctor end
}
