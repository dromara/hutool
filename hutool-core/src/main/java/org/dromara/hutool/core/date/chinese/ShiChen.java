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
 * <p>本转换器提供以下功能：
 * <ul>
 * <li>处理包含“时”、“初”或“正”后缀的时辰描述，并自动返回相应的现代时间段。
 * “初”和“正”分别对应每个时辰的前半段和后半段，而不带后缀的“时”描述则涵盖该时辰的完整时间段。</li>
 * <li>根据小时数转换为相应的时辰描述，通过{@code isAbs}参数控制是否包含“初”或“正”。</li>
 * </ul>
 * <p>
 * 异常情况：
 * <ul>
 * <li>如果输入的时辰描述无效或不被识别，{@code toModernTime} 方法将抛出 {@code IllegalArgumentException}。</li>
 * <li>同样，如果{@code toShiChen}方法接收到无效的小时数，将返回“未知”。</li>
 * </ul>
 * 示例：
 * <ul>
 * <li>{@code toModernTime("子时")} 返回的时间段从23点开始到1点结束。</li>
 * <li>{@code toModernTime("子初")} 返回的时间段从23点开始到0点结束。</li>
 * <li>{@code toModernTime("子正")} 返回的时间段从0点开始到1点结束。</li>
 * <li>{@code toShiChen(0, false)} 返回“子正”。</li>
 * <li>{@code toShiChen(0, true)} 返回“子时”。</li>
 * </ul>
 *
 * @author achao@hutool.cn
 */
public class ShiChen {

	private static final Map<String, Integer> timeMap = new HashMap<>();
	private static final Map<String, Integer[]> fullTimeMap = new HashMap<>();
	private static final Map<Integer, String> hourToShiChenMap = new HashMap<>();
	private static final Map<Integer, String> hourToShiChenAbsMap = new HashMap<>();

	static {
		// 初始化时辰对应的小时范围
		final String[] times = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		int hour = 23;
		for (final String time : times) {
			timeMap.put(time + "初", hour % 24);
			timeMap.put(time + "正", (hour + 1) % 24);
			fullTimeMap.put(time, new Integer[]{hour % 24, (hour + 2) % 24});
			hour += 2;
		}

		// 初始化小时到时辰的映射
		hour = 23;
		for (final String time : times) {
			hourToShiChenMap.put(hour % 24, time + "初");
			hourToShiChenMap.put((hour + 1) % 24, time + "正");
			hourToShiChenAbsMap.put(hour % 24, time + "时");
			hourToShiChenAbsMap.put((hour + 1) % 24, time + "时");
			hour += 2;
		}
	}

	/**
	 * 将时辰描述转换为现代时间段。示例：
	 * <ul>
	 * <li>{@code toModernTime("子时")} 返回的时间段从23点开始到1点结束。</li>
	 * <li>{@code toModernTime("子初")} 返回的时间段从23点开始到0点结束。</li>
	 * <li>{@code toModernTime("子正")} 返回的时间段从0点开始到1点结束。</li>
	 * </ul>
	 *
	 * @param shiChen 时辰描述，可以是“时”、“初”或“正”结尾。
	 * @return {@link DateBetween} 对象，表示起始和结束时间。
	 * @throws IllegalArgumentException 如果输入的时辰描述无效。
	 */
	public static DateBetween toModernTime(final String shiChen) {
		if (StrUtil.isEmpty(shiChen)) {
			throw new IllegalArgumentException("Invalid shiChen");
		}
		final Integer startHour;
		final Integer endHour;
		final LocalDateTime start;
		final LocalDateTime end;

		if (shiChen.endsWith("初") || shiChen.endsWith("正")) {
			startHour = timeMap.get(shiChen);
			if (startHour == null) {
				throw new IllegalArgumentException("Invalid ShiChen time");
			}
			endHour = (startHour + 1) % 24;
		} else {
			final String baseTime = shiChen.replace("时", "");
			final Integer[] hours = fullTimeMap.get(baseTime);
			if (hours == null) {
				throw new IllegalArgumentException("Invalid ShiChen time");
			}
			startHour = hours[0];
			endHour = hours[1];
		}

		start = LocalDateTime.now().withHour(startHour).withMinute(0).withSecond(0).withNano(0);
		end = (startHour > endHour) ? start.plusDays(1).withHour(endHour) : start.withHour(endHour);

		final Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
		final Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());

		return DateBetween.of(startDate, endDate);
	}

	/**
	 * 根据给定的小时数转换为对应的时辰描述。示例：
	 * <ul>
	 *   <li>{@code toShiChen(0, false)} 返回“子正”。</li>
	 *   <li>{@code toShiChen(0, true)} 返回“子时”。</li>
	 * </ul>
	 *
	 * @param hour  小时数，应在0到23之间。
	 * @param isAbs 是否返回绝对时辰描述（即包含“时”后缀），而不是“初”或“正”。
	 * @return 时辰描述，如果小时数无效，则返回“未知”。
	 */
	public static String toShiChen(final int hour, final boolean isAbs) {
		String result = hourToShiChenAbsMap.getOrDefault(hour, "未知");
		if (!isAbs && !result.equals("未知")) {
			result = hourToShiChenMap.get(hour);
		}
		return result;
	}

}
