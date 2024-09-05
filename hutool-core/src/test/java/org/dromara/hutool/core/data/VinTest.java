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

package org.dromara.hutool.core.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;

/**
 * @author VampireAchao
 */
public class VinTest {

	@Test
	public void parseVinTest() {
		final String vinStr = "HE9XR1C48PS083871";
		final VIN vin = VIN.of(vinStr);
		// VIN
		Assertions.assertEquals("HE9XR1C48PS083871", vin.getCode());
		// 年产量<1000
		Assertions.assertTrue(vin.isLessThan1000());
		// 地理区域码
		Assertions.assertEquals("HE", vin.getCountryCode());
		// WMI主机厂代码
		Assertions.assertEquals("HE9083", vin.getWMI());
		// VDS
		Assertions.assertEquals("XR1C48", vin.getVDS());
		// 车型年份
		Assertions.assertEquals(Year.of(2023), vin.getYear());
		// OEM厂商
		Assertions.assertEquals('S', vin.getOemCode());
		// 生产序号
		Assertions.assertEquals("871", vin.getProdNo());
	}

}
