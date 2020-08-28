package cn.hutool.core.date.chinese;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 节假日（农历）封装
 *
 * @author looly
 * @since 5.4.1
 */
public class LunarFestival {
	//农历节日  *表示放假日
	private static final String[] lFtv = new String[]{
			"0101 春节", "0102 大年初二", "0103 大年初三", "0104 大年初四",
			"0105 大年初五", "0106 大年初六", "0107 大年初七", "0105 路神生日",
			"0115 元宵节", "0202 龙抬头", "0219 观世音圣诞", "0404 寒食节",
			"0408 佛诞节 ", "0505 端午节", "0606 天贶节 姑姑节", "0624 彝族火把节",
			"0707 七夕情人节", "0714 鬼节(南方)", "0715 盂兰节", "0730 地藏节",
			"0815 中秋节", "0909 重阳节", "1001 祭祖节", "1117 阿弥陀佛圣诞",
			"1208 腊八节 释迦如来成道日", "1223 过小年", "1229 腊月二十九", "1230 除夕"
	};

	/**
	 * 获得节日列表
	 *
	 * @param month 月
	 * @param day   日
	 * @return 获得农历节日
	 */
	public static List<String> getFestivals(int month, int day) {
		final StringBuilder currentChineseDate = new StringBuilder();
		if (month < 10) {
			currentChineseDate.append('0');
		}
		currentChineseDate.append(month);

		if (day < 10) {
			currentChineseDate.append('0');
		}
		currentChineseDate.append(day);

		final List<String> result = new ArrayList<>();
		for (String fv : lFtv) {
			final List<String> split = StrUtil.split(fv, ' ');
			if (split.get(0).contentEquals(currentChineseDate)) {
				result.add(split.get(1));
			}
		}
		return result;
	}
}
