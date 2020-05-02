package cn.hutool.crypto.digest.opt;

import cn.hutool.crypto.digest.HmacAlgorithm;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>time-based one-time passwords (TOTP) 一次性密码生成器，
 * 规范见：<a href="https://tools.ietf.org/html/rfc6238">RFC&nbsp;6238</a>.</p>
 *
 * <p>参考：https://github.com/jchambers/java-otp</p>
 *
 * @author Looly
 */
public class TOPT extends HOTP {

	/**
	 * 默认步进 (30秒).
	 */
	public static final Duration DEFAULT_TIME_STEP = Duration.ofSeconds(30);

	private final Duration timeStep;

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param key 共享密码，RFC 4226要求最少128位
	 */
	public TOPT(byte[] key) {
		this(DEFAULT_TIME_STEP, key);
	}

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param timeStep 日期步进，用于生成移动因子（moving factor）
	 * @param key      共享密码，RFC 4226要求最少128位
	 */
	public TOPT(Duration timeStep, byte[] key) {
		this(timeStep, DEFAULT_PASSWORD_LENGTH, key);
	}

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param timeStep       日期步进，用于生成移动因子（moving factor）
	 * @param passwordLength 密码长度，可以是6,7,8
	 * @param key            共享密码，RFC 4226要求最少128位
	 */
	public TOPT(Duration timeStep, int passwordLength, byte[] key) {
		this(timeStep, passwordLength, HOTP_HMAC_ALGORITHM, key);
	}

	/**
	 * 构造
	 *
	 * @param timeStep       日期步进，用于生成移动因子（moving factor）
	 * @param passwordLength 密码长度，可以是6,7,8
	 * @param algorithm      HMAC算法枚举
	 * @param key            共享密码，RFC 4226要求最少128位
	 */
	public TOPT(Duration timeStep, int passwordLength, HmacAlgorithm algorithm, byte[] key) {
		super(passwordLength, algorithm, key);
		this.timeStep = timeStep;
	}

	/**
	 * 使用给定的时间戳生成一次性密码.
	 *
	 * @param timestamp 用于生成密码的时间戳
	 * @return 一次性密码的int形式
	 */
	public int generate(Instant timestamp) {
		return this.generate(timestamp.toEpochMilli() / this.timeStep.toMillis());
	}

	/**
	 * 获取步进
	 *
	 * @return 步进
	 */
	public Duration getTimeStep() {
		return this.timeStep;
	}
}
