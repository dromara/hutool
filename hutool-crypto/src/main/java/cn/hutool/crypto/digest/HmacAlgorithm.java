package cn.hutool.crypto.digest;

/**
 * HMAC算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Mac
 * 
 * @author Looly
 */
public enum HmacAlgorithm {
	HmacMD5("HmacMD5"), 
	HmacSHA1("HmacSHA1"), 
	HmacSHA256("HmacSHA256"), 
	HmacSHA384("HmacSHA384"), 
	HmacSHA512("HmacSHA512"),
	/** HmacSM3算法实现，需要BouncyCastle库支持 */
	HmacSM3("HmacSM3");

	private final String value;

	HmacAlgorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}