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
