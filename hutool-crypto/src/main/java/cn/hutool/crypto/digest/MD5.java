package cn.hutool.crypto.digest;

/**
 * MD5算法
 * 
 * @author looly
 * @since 4.4.3
 */
public class MD5 extends Digester {
	
	public MD5() {
		super(DigestAlgorithm.MD5);
	}
	
	/**
	 * 构造
	 * @param salt 盐值
	 * @param digestCount 摘要次数，当此值小于等于1,默认为1。
	 */
	public MD5(byte[] salt, int digestCount) {
		this();
		this.salt = salt;
		this.digestCount = digestCount;
	}
}
