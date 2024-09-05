/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ZoneUtilTest {

	@Test
	public void test() {
		Assertions.assertEquals(ZoneId.systemDefault(), ZoneUtil.toZoneId(null));
		Assertions.assertEquals(TimeZone.getDefault(), ZoneUtil.toTimeZone(null));
	}

	@Test
	public void testGetTimeZoneByOffsetReturnsNonNull() {
		// Arrange
		final int rawOffset = 8; // Example offset, you may adjust for different cases
		final TimeUnit timeUnit = TimeUnit.HOURS;

		// Act
		final TimeZone result = ZoneUtil.getTimeZoneByOffset(rawOffset, timeUnit);

		// Assert
		assertNotNull(result, "Expected non-null TimeZone for valid offset and unit");
	}

	@Test
	public void testGetTimeZoneByOffsetWithInvalidOffsetReturnsNull() {
		// Arrange
		final int rawOffset = 999; // Unlikely valid offset to test edge case
		final TimeUnit timeUnit = TimeUnit.HOURS;

		// Act
		final TimeZone result = ZoneUtil.getTimeZoneByOffset(rawOffset, timeUnit);

		// Assert
		assertNull(result, "Expected null TimeZone for invalid offset");
	}

	@Test
	public void testGetAvailableIDWithInvalidOffset() {
		// Test with an invalid offset that should result in null or an exception.
		// Assuming that an offset of 25 hours is invalid and should return null.
		final String result = ZoneUtil.getAvailableID(25, TimeUnit.HOURS);
		assertNull(result, "Expected null for invalid offset of 25 hours");
	}
}
