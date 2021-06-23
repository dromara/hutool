package cn.hutool.crypto;

/**
 * 补码方式
 *
 * <p>
 * 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 *
 * @author Looly
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher"> Cipher章节</a>
 * @since 3.0.8
 */
public enum Padding {
	/**
	 * 无补码
	 */
	NoPadding,
	/**
	 * 0补码，即不满block长度时使用0填充
	 */
	ZeroPadding,
	/**
	 * This padding for block ciphers is described in 5.2 Block Encryption Algorithms in the W3C's "XML Encryption Syntax and Processing" document.
	 */
	ISO10126Padding,
	/**
	 * Optimal Asymmetric Encryption Padding scheme defined in PKCS1
	 */
	OAEPPadding,
	/**
	 * The padding scheme described in PKCS #1, used with the RSA algorithm
	 */
	PKCS1Padding,
	/**
	 * The padding scheme described in RSA Laboratories, "PKCS #5: Password-Based Encryption Standard," version 1.5, November 1993.
	 */
	PKCS5Padding,
	/**
	 * The padding scheme defined in the SSL Protocol Version 3.0, November 18, 1996, section 5.2.3.2 (CBC block cipher)
	 */
	SSL3Padding
}
