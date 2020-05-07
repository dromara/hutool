package cn.hutool.crypto.digest.opt;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

/**
 * <p>HMAC-based one-time passwords (HOTP) 一次性密码生成器，
 * 规范见：<a href="https://tools.ietf.org/html/rfc4226">RFC&nbsp;4226</a>.</p>
 *
 * <p>参考：https://github.com/jchambers/java-otp</p>
 *
 * @author Looly
 */
public class HOTP {

	/**
	 * 数子量级
	 */
	private static final int[] MOD_DIVISORS = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
	/**
	 * 默认密码长度.
	 */
	public static final int DEFAULT_PASSWORD_LENGTH = 6;

	/**
	 * 默认HMAC算法.
	 */
	public static final HmacAlgorithm HOTP_HMAC_ALGORITHM = HmacAlgorithm.HmacSHA1;

	private final HMac mac;
	private final int passwordLength;
	private final int modDivisor;

	private final byte[] buffer;

	/**
	 * 构造，使用默认密码长度和默认HMAC算法(HmacSHA1)
	 *
	 * @param key 共享密码，RFC 4226要求最少128位
	 */
	public HOTP(byte[] key) {
		this(DEFAULT_PASSWORD_LENGTH, key);
	}

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param passwordLength 密码长度，可以是6,7,8
	 * @param key            共享密码，RFC 4226要求最少128位
	 */
	public HOTP(int passwordLength, byte[] key) {
		this(passwordLength, HOTP_HMAC_ALGORITHM, key);
	}

	/**
	 * 构造
	 *
	 * @param passwordLength 密码长度，可以是6,7,8
	 * @param algorithm      HMAC算法枚举
	 * @param key            共享密码，RFC 4226要求最少128位
	 */
	public HOTP(int passwordLength, HmacAlgorithm algorithm, byte[] key) {
		this.mac = new HMac(algorithm, key);

		this.modDivisor = MOD_DIVISORS[passwordLength];
		this.passwordLength = passwordLength;
		this.buffer = new byte[this.mac.getMacLength()];
	}

	/**
	 * 生成一次性密码
	 *
	 * @param counter 事件计数的值，8 字节的整数，称为移动因子（moving factor），
	 *                可以是基于计次的动移动因子，也可以是计时移动因子
	 * @return 一次性密码的int值
	 */
	public synchronized int generate(final long counter) {
		// C 的整数值需要用二进制的字符串表达，比如某个事件计数为 3，
		// 则C是 "11"（此处省略了前面的二进制的数字0）
		this.buffer[0] = (byte) ((counter & 0xff00000000000000L) >>> 56);
		this.buffer[1] = (byte) ((counter & 0x00ff000000000000L) >>> 48);
		this.buffer[2] = (byte) ((counter & 0x0000ff0000000000L) >>> 40);
		this.buffer[3] = (byte) ((counter & 0x000000ff00000000L) >>> 32);
		this.buffer[4] = (byte) ((counter & 0x00000000ff000000L) >>> 24);
		this.buffer[5] = (byte) ((counter & 0x0000000000ff0000L) >>> 16);
		this.buffer[6] = (byte) ((counter & 0x000000000000ff00L) >>> 8);
		this.buffer[7] = (byte) (counter & 0x00000000000000ffL);

		final byte[] digest = this.mac.digest(this.buffer);

		return truncate(digest);
	}

	/**
	 * 截断
	 *
	 * @param digest HMAC的hash值
	 * @return 截断值
	 */
	private int truncate(byte[] digest) {
		final int offset = digest[digest.length - 1] & 0x0f;
		return ((digest[offset] & 0x7f) << 24 |
				(digest[offset + 1] & 0xff) << 16 |
				(digest[offset + 2] & 0xff) << 8 |
				(digest[offset + 3] & 0xff)) %
				this.modDivisor;
	}

	/**
	 * 获取密码长度，可以是6,7,8
	 *
	 * @return 密码长度，可以是6,7,8
	 */
	public int getPasswordLength() {
		return this.passwordLength;
	}

	/**
	 * 获取HMAC算法
	 *
	 * @return HMAC算法
	 */
	public String getAlgorithm() {
		return this.mac.getAlgorithm();
	}
}