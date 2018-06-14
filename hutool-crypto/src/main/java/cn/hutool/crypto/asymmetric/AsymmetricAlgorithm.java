package cn.hutool.crypto.asymmetric;

/**
 * 非对称算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
 * 
 * @author Looly
 *
 */
public enum AsymmetricAlgorithm {
	RSA("RSA"), EC("EC");

	private String value;

	/**
	 * 构造
	 * @param value 算法字符表示，区分大小写
	 */
	private AsymmetricAlgorithm(String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示，区分大小写
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}
