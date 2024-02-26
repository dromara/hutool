package org.dromara.hutool.core.date.chinese;

import org.dromara.hutool.core.date.DateUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * ShiChenTest
 *
 * @author achao@apache.org
 */
public class ShiChenTest {

	@Test
	void testToModernTime() {
		// 测试“时”后缀的转换，表示整个时辰
		Assertions.assertEquals(2, ShiChen.toModernTime("子时").between(DateUnit.HOUR));

		// 测试“初”和“正”后缀的转换，表示时辰的前半段和后半段
		Assertions.assertEquals(1, ShiChen.toModernTime("子初").between(DateUnit.HOUR));
		Assertions.assertEquals(1, ShiChen.toModernTime("子正").between(DateUnit.HOUR));

		// 测试所有时辰
		String[] times = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		for (String time : times) {
			Assertions.assertEquals(2, ShiChen.toModernTime(time + "时").between(DateUnit.HOUR));
			Assertions.assertEquals(1, ShiChen.toModernTime(time + "初").between(DateUnit.HOUR));
			Assertions.assertEquals(1, ShiChen.toModernTime(time + "正").between(DateUnit.HOUR));
		}
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ShiChen.toModernTime("无效时");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ShiChen.toModernTime("无效正");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ShiChen.toModernTime("");
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ShiChen.toModernTime(null);
		});
	}

	@Test
	void testToShiChen() {
		// 测试小时转换为长安时辰，不包含“初”或“正”
		Assertions.assertEquals("子时", ShiChen.toShiChen(23, true));
		Assertions.assertEquals("子时", ShiChen.toShiChen(0, true));

		// 测试小时转换为长安时辰，包含“初”或“正”
		Assertions.assertEquals("子正", ShiChen.toShiChen(0, false));
		Assertions.assertEquals("丑初", ShiChen.toShiChen(1, false));

		// 测试边界条件
		Assertions.assertEquals("未知", ShiChen.toShiChen(24, true));
		Assertions.assertEquals("未知", ShiChen.toShiChen(-1, false));
	}

}
