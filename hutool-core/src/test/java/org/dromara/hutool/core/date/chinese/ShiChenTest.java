package org.dromara.hutool.core.date.chinese;

import org.dromara.hutool.core.date.DateBetween;
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
	void testToModernTimeForAllTimes() {
		// 测试每个时辰的转换
		String[] times = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		int[][] expectedHours = {{23, 1}, {1, 3}, {3, 5}, {5, 7}, {7, 9}, {9, 11}, {11, 13}, {13, 15}, {15, 17}, {17, 19}, {19, 21}, {21, 23}};
		for (int i = 0; i < times.length; i++) {
			DateBetween dateBetween = ShiChen.toModernTime(times[i] + "时");
			Assertions.assertEquals(2, dateBetween.between(DateUnit.HOUR));
			Assertions.assertEquals(expectedHours[i][0], dateBetween.getBegin().getHours());
			Assertions.assertEquals(expectedHours[i][1], dateBetween.getEnd().getHours());
		}
	}

	@Test
	void testToChangAnTimeForAllHours() {
		// 从23时开始测试，因为子时开始于23时
		String[] expectedTimes = {"子时", "丑时", "丑时", "寅时", "寅时", "卯时", "卯时", "辰时", "辰时", "巳时", "巳时", "午时", "午时", "未时", "未时", "申时", "申时", "酉时", "酉时", "戌时", "戌时", "亥时", "亥时", "子时", "未知"};
		for (int hour = 0; hour <= 24; hour++) {
			String expectedTime = expectedTimes[hour];
			String actualTime = ShiChen.toChangAnTime(hour);
			Assertions.assertEquals(expectedTime, actualTime);
		}
	}

}
