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
	ISO10126Padding,
	OAEPPadding,
	PKCS1Padding,
	PKCS5Padding,
	SSL3Padding
}
