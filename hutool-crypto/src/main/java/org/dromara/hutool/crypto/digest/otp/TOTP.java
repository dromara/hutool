/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.digest.otp;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.digest.mac.HmacAlgorithm;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>time-based one-time passwords (TOTP) 基于时间戳算法的一次性密码生成器，
 * 规范见：<a href="https://tools.ietf.org/html/rfc6238">RFC&nbsp;6238</a>.</p>
 *
 * <p>时间同步，基于客户端的动态口令和动态口令验证服务器的时间比对，一般每30秒产生一个新口令，
 * 要求客户端和服务器能够十分精确的保持正确的时钟，客户端和服务端基于时间计算的动态口令才能一致。</p>
 *
 * <p>参考：https://github.com/jchambers/java-otp</p>
 *
 * @author Looly
 */
public class TOTP extends HOTP {

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
	public TOTP(final byte[] key) {
		this(DEFAULT_TIME_STEP, key);
	}

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param timeStep 日期步进，用于生成移动因子（moving factor）
	 * @param key      共享密码，RFC 4226要求最少128位
	 */
	public TOTP(final Duration timeStep, final byte[] key) {
		this(timeStep, DEFAULT_PASSWORD_LENGTH, key);
	}

	/**
	 * 构造，使用默认HMAC算法(HmacSHA1)
	 *
	 * @param timeStep       日期步进，用于生成移动因子（moving factor）
	 * @param passwordLength 密码长度，可以是6,7,8
	 * @param key            共享密码，RFC 4226要求最少128位
	 */
	public TOTP(final Duration timeStep, final int passwordLength, final byte[] key) {
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
	public TOTP(final Duration timeStep, final int passwordLength, final HmacAlgorithm algorithm, final byte[] key) {
		super(passwordLength, algorithm, key);
		this.timeStep = timeStep;
	}

	/**
	 * 使用给定的时间戳生成一次性密码.
	 *
	 * @param timestamp 用于生成密码的时间戳
	 * @return 一次性密码的int形式
	 */
	public int generate(final Instant timestamp) {
		return this.generate(timestamp.toEpochMilli() / this.timeStep.toMillis());
	}

	/**
	 * 用于验证code是否正确
	 *
	 * @param timestamp  验证时间戳
	 * @param offsetSize 误差范围
	 * @param code       code
	 * @return 是否通过
	 * @since 5.7.4
	 */
	public boolean validate(final Instant timestamp, final int offsetSize, final int code) {
		if (offsetSize == 0) {
			return generate(timestamp) == code;
		}
		for (int i = -offsetSize; i <= offsetSize; i++) {
			if (generate(timestamp.plus(getTimeStep().multipliedBy(i))) == code) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 生成谷歌认证器的字符串（扫码字符串）
	 * 基于时间的，计数器不适合
	 *
	 * @param account  账户名。
	 * @param numBytes 将生成的种子字节数量。
	 * @return 共享密钥
	 * @since 5.7.4
	 */
	public static String generateGoogleSecretKey(final String account, final int numBytes) {
		return StrUtil.format("otpauth://totp/{}?secret={}", account, generateSecretKey(numBytes));
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
