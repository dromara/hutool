/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.digest;

import org.dromara.hutool.core.codec.binary.Base32;
import org.dromara.hutool.crypto.digest.mac.HmacAlgorithm;
import org.dromara.hutool.crypto.digest.otp.HOTP;
import org.dromara.hutool.crypto.digest.otp.TOTP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

/**
 * author: xlgogo@outlook.com
 * date: 2021-07-01 18:14
 */
public class OTPTest {

	@Test
	public void genKeyTest() {
		final String key = TOTP.generateSecretKey(8);
		Assertions.assertEquals(8, Base32.decode(key).length);
	}

	@Test
	public void validTest() {
		final String key = "VYCFSW2QZ3WZO";
		// 2021/7/1下午6:29:54 显示code为 106659
		//Assertions.assertEquals(new TOTP(Base32.decode(key)).generate(Instant.ofEpochSecond(1625135394L)),106659);
		final TOTP totp = new TOTP(Base32.decode(key));
		final Instant instant = Instant.ofEpochSecond(1625135394L);
		Assertions.assertTrue(totp.validate(instant, 0, 106659));
		Assertions.assertTrue(totp.validate(instant.plusSeconds(30), 1, 106659));
		Assertions.assertTrue(totp.validate(instant.plusSeconds(60), 2, 106659));

		Assertions.assertFalse(totp.validate(instant.plusSeconds(60), 1, 106659));
		Assertions.assertFalse(totp.validate(instant.plusSeconds(90), 2, 106659));
	}

	@Test
	public void googleAuthTest() {
		final String str = TOTP.generateGoogleSecretKey("xl7@qq.com", 10);
		Assertions.assertTrue(str.startsWith("otpauth://totp/xl7@qq.com?secret="));
	}

	@Test
	public void longPasswordLengthTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new HOTP(9, "123".getBytes()));
	}

	@Test
	public void generateHOPTTest(){
		final byte[] key = "12345678901234567890".getBytes();
		final HOTP hotp = new HOTP(key);
		Assertions.assertEquals(755224, hotp.generate(0));
		Assertions.assertEquals(287082, hotp.generate(1));
		Assertions.assertEquals(359152, hotp.generate(2));
		Assertions.assertEquals(969429, hotp.generate(3));
		Assertions.assertEquals(338314, hotp.generate(4));
		Assertions.assertEquals(254676, hotp.generate(5));
		Assertions.assertEquals(287922, hotp.generate(6));
		Assertions.assertEquals(162583, hotp.generate(7));
		Assertions.assertEquals(399871, hotp.generate(8));
		Assertions.assertEquals(520489, hotp.generate(9));
	}

	@Test
	public void getTimeStepTest() {
		final Duration timeStep = Duration.ofSeconds(97);

		final TOTP totp = new TOTP(timeStep, "123".getBytes());

		Assertions.assertEquals(timeStep, totp.getTimeStep());
	}

	@Test
	public void generateHmacSHA1TOPTTest(){
		final HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA1;
		final byte[] key = "12345678901234567890".getBytes();
		final TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assertions.assertEquals(94287082, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assertions.assertEquals(7081804, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assertions.assertEquals(14050471, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assertions.assertEquals(89005924, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assertions.assertEquals(69279037, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assertions.assertEquals(65353130, generate);
	}

	@Test
	public void generateHmacSHA256TOPTTest(){
		final HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA256;
		final byte[] key = "12345678901234567890123456789012".getBytes();
		final TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assertions.assertEquals(46119246, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assertions.assertEquals(68084774, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assertions.assertEquals(67062674, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assertions.assertEquals(91819424, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assertions.assertEquals(90698825, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assertions.assertEquals(77737706, generate);
	}

	@Test
	public void generateHmacSHA512TOPTTest(){
		final HmacAlgorithm algorithm = HmacAlgorithm.HmacSHA512;
		final byte[] key = "1234567890123456789012345678901234567890123456789012345678901234".getBytes();
		final TOTP totp = new TOTP(Duration.ofSeconds(30), 8, algorithm, key);

		int generate = totp.generate(Instant.ofEpochSecond(59L));
		Assertions.assertEquals(90693936, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111109L));
		Assertions.assertEquals(25091201, generate);
		generate = totp.generate(Instant.ofEpochSecond(1111111111L));
		Assertions.assertEquals(99943326, generate);
		generate = totp.generate(Instant.ofEpochSecond(1234567890L));
		Assertions.assertEquals(93441116, generate);
		generate = totp.generate(Instant.ofEpochSecond(2000000000L));
		Assertions.assertEquals(38618901, generate);
		generate = totp.generate(Instant.ofEpochSecond(20000000000L));
		Assertions.assertEquals(47863826, generate);
	}
}
