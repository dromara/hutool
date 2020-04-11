package cn.hutool.crypto.digest;

/**
 * 摘要算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
 * 
 * @author Looly
 */
public enum DigestAlgorithm {
	MD2("MD2"), 
	MD5("MD5"), 
	SHA1("SHA-1"), 
	SHA256("SHA-256"), 
	SHA384("SHA-384"), 
	SHA512("SHA-512");

	private final String value;

	/**
	 * 构造
	 * 
	 * @param value 算法字符串表示
	 */
	DigestAlgorithm(String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}