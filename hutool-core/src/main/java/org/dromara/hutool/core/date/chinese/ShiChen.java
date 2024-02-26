package org.dromara.hutool.core.date.chinese;

import org.dromara.hutool.core.date.DateBetween;
import org.dromara.hutool.core.text.StrUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时辰转换器，支持宋以后的二十四时辰制度。
 * <p>
 * 该转换器能够处理包含“时”、“初”或“正”后缀的长安时辰描述，并根据这些描述自动返回相应的现代时间段。
 * 对于“初”和“正”的描述，分别对应每个时辰的前半段和后半段，而不带后缀的时辰描述则表示该时辰的整个时间段。
 * </p>
 * <p>
 * 此外，该转换器还提供了根据小时数转换为对应的长安时辰描述的功能，通过isAbs参数可以控制返回的描述是否包含“初”或“正”。
 * </p>
 * <p>
 * 示例：
 * <ul>
 * <li>toModernTime("子时") 返回的时间段从23点开始到1点结束。</li>
 * <li>toModernTime("子初") 返回的时间段从23点开始到0点结束。</li>
 * <li>toModernTime("子正") 返回的时间段从0点开始到1点结束。</li>
 * <li>toShiChen(0, false) 返回“子正”。</li>
 * <li>toShiChen(0, true) 返回“子时”。</li>
 * </ul>
 * </p>
 * @author achao@hutool.cn
 */
public class ShiChen {

	private static final Map<String, Integer> timeMap = new HashMap<>();
	private static final Map<String, Integer[]> fullTimeMap = new HashMap<>();
	private static final Map<Integer, String> hourToShiChenMap = new HashMap<>();
	private static final Map<Integer, String> hourToShiChenAbsMap = new HashMap<>();

	static {
		// 初始化时辰对应的小时范围
		String[] times = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		int hour = 23;
		for (String time : times) {
			timeMap.put(time + "初", hour % 24);
			timeMap.put(time + "正", (hour + 1) % 24);
			fullTimeMap.put(time, new Integer[]{hour % 24, (hour + 2) % 24});
			hour += 2;
		}

		// 初始化小时到时辰的映射
		hour = 23;
		for (String time : times) {
			hourToShiChenMap.put(hour % 24, time + "初");
			hourToShiChenMap.put((hour + 1) % 24, time + "正");
			hourToShiChenAbsMap.put(hour % 24, time + "时");
			hourToShiChenAbsMap.put((hour + 1) % 24, time + "时");
			hour += 2;
		}
	}

	public static DateBetween toModernTime(String shiChen) {
		if (StrUtil.isEmpty(shiChen)) {
			throw new IllegalArgumentException("Invalid shiChen");
		}
		Integer startHour, endHour;
		LocalDateTime start, end;

		if (shiChen.endsWith("初") || shiChen.endsWith("正")) {
			startHour = timeMap.get(shiChen);
			if (startHour == null) {
				throw new IllegalArgumentException("Invalid ChangAn time");
			}
			endHour = (startHour + 1) % 24;
		} else {
			String baseTime = shiChen.replace("时", "");
			Integer[] hours = fullTimeMap.get(baseTime);
			if (hours == null) {
				throw new IllegalArgumentException("Invalid ChangAn time");
			}
			startHour = hours[0];
			endHour = hours[1];
		}

		start = LocalDateTime.now().withHour(startHour).withMinute(0).withSecond(0).withNano(0);
		end = (startHour > endHour) ? start.plusDays(1).withHour(endHour) : start.withHour(endHour);

		Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

		return DateBetween.of(startDate, endDate);
	}

	public static String toShiChen(int hour, boolean isAbs) {
		String result = hourToShiChenAbsMap.getOrDefault(hour, "未知");
		if (!isAbs && !result.equals("未知")) {
			result = hourToShiChenMap.get(hour);
		}
		return result;
	}

}
