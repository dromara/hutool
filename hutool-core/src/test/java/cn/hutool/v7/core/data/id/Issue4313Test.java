/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hutool.v7.core.data.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Issue #4313: uuid7的生成不符合标准
 * <a href="https://github.com/chinabugotech/hutool/issues/4313">https://github.com/chinabugotech/hutool/issues/4313</a>
 */
public class Issue4313Test {

	/**
	 * 验证UUIDv7使用系统真实时间戳，符合RFC 9562标准
	 */
	@Test
	public void testUUIDv7TimestampSource() {
		// 验证基本属性
		final UUID uuid = UUID.randomUUID7();

		// 版本号必须是7
		assertEquals(7, uuid.version());
		// 变体必须是IETF variant (RFC 9562)
		assertEquals(2, uuid.variant());

		// 验证格式符合标准
		final String uuidStr = uuid.toString();
		assertTrue(uuidStr.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));

		// 验证时间戳部分是基于当前真实时间
		// 获取当前时间的毫秒数
		final long currentTimeMs = System.currentTimeMillis();

		// 从UUIDv7中提取时间戳（前48位）
		final long mostSignificantBits = uuid.getMostSignificantBits();
		final long uuidTimestamp = (mostSignificantBits >> 16) & 0xFFFFFFFFFFFFL;

		// 时间戳应该接近当前时间（误差在5秒内都算合理）
		final long diff = Math.abs(uuidTimestamp - currentTimeMs);
		assertTrue(diff < 5000,
			"UUIDv7 timestamp (" + uuidTimestamp + ") should be close to current time (" + currentTimeMs + "), diff: " + diff);
	}

	/**
	 * 验证连续生成的UUIDv7保持单调递增
	 */
	@Test
	public void testUUIDv7Monotonicity() {
		UUID prev = UUID.randomUUID7();

		// 验证连续生成的100个UUIDv7保持单调递增
		for (int i = 0; i < 100; i++) {
			final UUID next = UUID.randomUUID7();
			assertTrue(next.compareTo(prev) > 0,
				"Next UUID should be greater than previous UUID at iteration " + i);
			prev = next;
		}
	}
}
