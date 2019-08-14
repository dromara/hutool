package cn.hutool.crypto;

/**
 * 补码方式
 * 
 * @author Looly
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher"> Cipher章节</a>
 * @since 3.0.8
 */
public enum Padding {
	/** 无补码 */
	NoPadding, 
	ISO10126Padding,
	OAEPPadding,
	PKCS1Padding,
	PKCS5Padding, 
	SSL3Padding
}
