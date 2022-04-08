package cn.hutool.crypto.symmetric.fpe;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.bouncycastle.crypto.AlphabetMapper;
import org.bouncycastle.jcajce.spec.FPEParameterSpec;

import java.io.Serializable;

/**
 * FPE(Format Preserving Encryption)实现，支持FF1和FF3-1模式。<br>
 * 相关介绍见：https://anquan.baidu.com/article/193
 *
 * <p>
 * FPE是一种格式保持与明文相同的加密方式，通常用于数据脱敏中，因为它需要保持明密文的格式相同，
 * 例如社保号经过加密之后并不是固定长度的杂文，而是相同格式、打乱的号码，依然是社保号的格式。
 * </p>
 * <p>
 * FPE算法可以保证：
 *
 * <ul>
 *     <li>数据长度不变。加密前长度是N，加密后长度仍然是N</li>
 *     <li>数据类型不变，加密前是数字类型，加密后仍然是数字类型</li>
 *     <li>加密过程可逆，加密后的数据可以通过密钥解密还原原始数据</li>
 * </ul>
 *
 * 此类基于BouncyCastle实现。
 *
 * @author looly
 * @since 5.7.12
 */
public class FPE implements Serializable {
	private static final long serialVersionUID = 1L;

	// 映射字符表，规定了明文和密文的字符范围
	private final AES aes;
	private final AlphabetMapper mapper;

	/**
	 * 构造，使用空的Tweak
	 *
	 * @param mode   FPE模式枚举，可选FF1或FF3-1
	 * @param key    密钥，{@code null}表示随机密钥，长度必须是16bit、24bit或32bit
	 * @param mapper Alphabet字典映射，被加密的字符范围和这个映射必须一致，例如手机号、银行卡号等字段可以采用数字字母字典表
	 */
	public FPE(FPEMode mode, byte[] key, AlphabetMapper mapper) {
		this(mode, key, mapper, null);
	}

	/**
	 * 构造
	 *
	 * @param mode   FPE模式枚举，可选FF1或FF3-1
	 * @param key    密钥，{@code null}表示随机密钥，长度必须是16bit、24bit或32bit
	 * @param mapper Alphabet字典映射，被加密的字符范围和这个映射必须一致，例如手机号、银行卡号等字段可以采用数字字母字典表
	 * @param tweak  Tweak是为了解决因局部加密而导致结果冲突问题，通常情况下将数据的不可变部分作为Tweak，{@code null}使用默认长度全是0的bytes
	 */
	public FPE(FPEMode mode, byte[] key, AlphabetMapper mapper, byte[] tweak) {
		if (null == mode) {
			mode = FPEMode.FF1;
		}

		if (null == tweak) {
			switch (mode) {
				case FF1:
					tweak = new byte[0];
					break;
				case FF3_1:
					// FF3-1要求必须为56 bits
					tweak = new byte[7];
			}
		}
		this.aes = new AES(mode.value, Padding.NoPadding.name(),
				KeyUtil.generateKey(mode.value, key),
				new FPEParameterSpec(mapper.getRadix(), tweak));
		this.mapper = mapper;
	}

	/**
	 * 加密
	 *
	 * @param data 数据，数据必须在构造传入的{@link AlphabetMapper}中定义的范围
	 * @return 密文结果
	 */
	public String encrypt(String data) {
		if (null == data) {
			return null;
		}
		return new String(encrypt(data.toCharArray()));
	}

	/**
	 * 加密
	 *
	 * @param data 数据，数据必须在构造传入的{@link AlphabetMapper}中定义的范围
	 * @return 密文结果
	 */
	public char[] encrypt(char[] data) {
		if (null == data) {
			return null;
		}
		// 通过 mapper 将密文输出处理为原始格式
		return mapper.convertToChars(aes.encrypt(mapper.convertToIndexes(data)));
	}

	/**
	 * 解密
	 *
	 * @param data 密文数据，数据必须在构造传入的{@link AlphabetMapper}中定义的范围
	 * @return 明文结果
	 */
	public String decrypt(String data) {
		if (null == data) {
			return null;
		}
		return new String(decrypt(data.toCharArray()));
	}

	/**
	 * 加密
	 *
	 * @param data 密文数据，数据必须在构造传入的{@link AlphabetMapper}中定义的范围
	 * @return 明文结果
	 */
	public char[] decrypt(char[] data) {
		if (null == data) {
			return null;
		}
		// 通过 mapper 将密文输出处理为原始格式
		return mapper.convertToChars(aes.decrypt(mapper.convertToIndexes(data)));
	}

	/**
	 * FPE模式<br>
	 * FPE包括两种模式：FF1和FF3（FF2弃用），核心均为Feistel网络结构。
	 *
	 * @author looly
	 */
	public enum FPEMode {
		/**
		 * FF1模式
		 */
		FF1("FF1"),
		/**
		 * FF3-1 模式
		 */
		FF3_1("FF3-1");

		private final String value;

		FPEMode(String name) {
			this.value = name;
		}

		/**
		 * 获取模式名
		 *
		 * @return 模式名
		 */
		public String getValue() {
			return value;
		}
	}
}
