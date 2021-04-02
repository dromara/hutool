package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

import java.time.LocalDate;
import java.util.Date;

/**
 * 24节气相关信息
 *
 * @author looly, zak
 * @since 5.4.1
 */
public class SolarTerms {

	/**
	 * 24节气
	 */
	public static final String[] TERMS = {
			"小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
			"清明", "谷雨", "立夏", "小满", "芒种", "夏至",
			"小暑", "大暑", "立秋", "处暑", "白露", "秋分",
			"寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
	};

	/**
	 * 小寒
	 */
	public static final String XIAO_HAN     = TERMS[0];
	/**
	 * 大寒
	 */
	public static final String DA_HAN       = TERMS[1];
	/**
	 * 立春
	 */
	public static final String LI_CHUN      = TERMS[2];
	/**
	 * 雨水
	 */
	public static final String YU_SHUI      = TERMS[3];
	/**
	 * 惊蛰
	 */
	public static final String JING_ZHE     = TERMS[4];
	/**
	 * 春分
	 */
	public static final String CHUN_FEN     = TERMS[5];
	/**
	 * 清明
	 */
	public static final String QING_MING    = TERMS[6];
	/**
	 * 谷雨
	 */
	public static final String GU_YU        = TERMS[7];
	/**
	 * 立夏
	 */
	public static final String LI_XIA       = TERMS[8];
	/**
	 * 小满
	 */
	public static final String XIAO_MAN     = TERMS[9];
	/**
	 * 芒种
	 */
	public static final String MANG_ZHONG   = TERMS[10];
	/**
	 * 夏至
	 */
	public static final String XIA_ZHI      = TERMS[11];
	/**
	 * 小暑
	 */
	public static final String XIAO_SHU     = TERMS[12];
	/**
	 * 大暑
	 */
	public static final String DA_SHU       = TERMS[13];
	/**
	 * 立秋
	 */
	public static final String LI_QIU       = TERMS[14];
	/**
	 * 处暑
	 */
	public static final String CHU_SHU      = TERMS[15];
	/**
	 * 白露
	 */
	public static final String BAI_LU       = TERMS[16];
	/**
	 * 秋分
	 */
	public static final String QIU_FEN      = TERMS[17];
	/**
	 * 寒露
	 */
	public static final String HAN_LU       = TERMS[18];
	/**
	 * 霜降
	 */
	public static final String SHUANG_JIANG = TERMS[19];
	/**
	 * 立冬
	 */
	public static final String LI_DONG      = TERMS[20];
	/**
	 * 小雪
	 */
	public static final String XIAO_XUE     = TERMS[21];
	/**
	 * 大雪
	 */
	public static final String DA_XUE       = TERMS[22];
	/**
	 * 冬至
	 */
	public static final String DONG_ZHI     = TERMS[23];





	/**
	 * 根据节气修正干支月
	 *
	 * @param y 月
	 * @param n 节气
	 * @return 干支月
	 */
	public static int getTerm(int y, int n) {
		if (y < 1900 || y > 2100) {
			return -1;
		}
		if (n < 1 || n > 24) {
			return -1;
		}

		String _table = S_TERM_INFO[y - 1900];
		Integer[] _info = new Integer[6];
		for (int i = 0; i < 6; i++) {
			_info[i] = NumberUtil.parseInt("0x" + _table.substring(i * 5, 5 * (i + 1)));
		}
		String[] _calday = new String[24];
		for (int i = 0; i < 6; i++) {
			_calday[4 * i] = _info[i].toString().substring(0, 1);
			_calday[4 * i + 1] = _info[i].toString().substring(1, 3);
			_calday[4 * i + 2] = _info[i].toString().substring(3, 4);
			_calday[4 * i + 3] = _info[i].toString().substring(4, 6);
		}
		return NumberUtil.parseInt(_calday[n - 1]);
	}



	/**
	 * 根据日期获取节气
	 * @param date 日期
	 * @return 返回指定日期所处的节气
	 */
	public static String getTerm(Date date) {
		final DateTime dt = DateUtil.date(date);
		return getTerm0(dt.year(), dt.month() + 1, dt.dayOfMonth());
	}


	/**
	 * 根据农历日期获取节气
	 * @param chineseDate 农历日期
	 * @return 返回指定日期所处的节气
	 */
	public static String getTerm(ChineseDate chineseDate) {
		return chineseDate.getTerm();
	}

	/**
	 * 根据日期获取节气
	 * @param date 日期
	 * @return 返回指定日期所处的节气
	 */
	public static String getTerm(LocalDate date) {
		return getTerm0(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

	/**
	 * 根据年月日获取节气
	 * @param year 年
	 * @param mouth 月
	 * @param day 日
	 * @return 返回指定年月日所处的节气
	 */
	public static String getTerm(int year, int mouth, int day) {
		return getTerm(LocalDate.of(year, mouth, day));
	}

	/**
	 * 根据年月日获取节气, 内部方法，不对月和日做有效校验
	 * @param year 年
	 * @param mouth 月
	 * @param day 日
	 * @return 返回指定年月日所处的节气
	 */
	static String getTerm0(int year, int mouth, int day) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("只支持1900-2100之间的日期获取节气");
		}

		String termTable = S_TERM_INFO[year - 1900];

		// 节气速查表中每5个字符含有4个节气，通过月份直接计算偏移
		int segment = (mouth + 1) / 2 - 1;
		int termInfo = NumberUtil.parseInt("0x" + termTable.substring(segment * 5, (segment + 1) * 5));

		String[] segmentTable = new String[24];
		segmentTable[0] = String.valueOf(termInfo).substring(0, 1);
		segmentTable[1] = String.valueOf(termInfo).substring(1, 3);
		segmentTable[2] = String.valueOf(termInfo).substring(3, 4);
		segmentTable[3] = String.valueOf(termInfo).substring(4, 6);

		// 奇数月份的节气在前2个，偶数月份的节气在后两个
		int segmentOffset = (mouth & 1) == 1 ? 0 : 2;
		if (day < NumberUtil.parseInt(segmentTable[segmentOffset])) {
			int idx = segment * 4 + segmentOffset - 1;
			return TERMS[idx < 0 ? 23 : idx];
		}
		if(day >= NumberUtil.parseInt(segmentTable[segmentOffset + 1])) {
			return TERMS[segment * 4 + segmentOffset + 1];
		}
		return TERMS[segment * 4 + segmentOffset];
	}


	/**
	 * 1900-2100各年的24节气日期速查表
	 * 此表来自：https://github.com/jjonline/calendar.js/blob/master/calendar.js
	 */
	private static final String[] S_TERM_INFO = new String[]{
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f",
			"97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f",
			"b027097bd097c36b0b6fc9274c91aa", "9778397bd19801ec9210c965cc920e", "97b6b97bd19801ec95f8c965cc920f",
			"97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2", "9778397bd197c36c9210c9274c91aa",
			"97b6b97bd19801ec95f8c965cc920e", "97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec95f8c965cc920e", "97bcf97c3598082c95f8e1cfcc920f",
			"97bd097bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f",
			"97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c359801ec95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd097bd07f595b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9210c8dc2", "9778397bd19801ec9210c9274c920e", "97b6b97bd19801ec95f8c965cc920f",
			"97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
			"97b6b97bd19801ec95f8c965cc920f", "97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bd07f1487f595b0b0bc920fb0722",
			"7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f531b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf7f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"9778397bd097c36b0b6fc9210c91aa", "97b6b97bd197c36c9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
			"97b6b7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36b0b70c9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b7f0e47f531b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"9778397bd097c36b0b6fc9210c91aa", "97b6b7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "977837f0e37f149b0723b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c35b0b6fc9210c8dc2",
			"977837f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc9210c8dc2", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "977837f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd",
			"7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"977837f0e37f14998082b0723b06bd", "7f07e7f0e37f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f595b0b0bb0b6fb0722", "7f0e37f0e37f14898082b0723b02d5",
			"7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f531b0b0bb0b6fb0722",
			"7f0e37f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e37f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35",
			"7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f149b0723b0787b0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0723b06bd",
			"7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722", "7f0e37f0e366aa89801eb072297c35",
			"7ec967f0e37f14998082b0723b06bd", "7f07e7f0e37f14998083b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14898082b0723b02d5", "7f07e7f0e37f14998082b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66aa89801e9808297c35", "665f67f0e37f14898082b0723b02d5",
			"7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66a449801e9808297c35",
			"665f67f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e36665b66a449801e9808297c35", "665f67f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e26665b66a449801e9808297c35", "665f67f0e37f1489801eb072297c35",
			"7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722"};
}
