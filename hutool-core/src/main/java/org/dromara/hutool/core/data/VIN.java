/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.regex.ReUtil;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

/**
 * VIN是Vehicle Identification Number的缩写，即车辆识别号码。VIN码是全球通行的车辆唯一标识符，由17位数字和字母组成。
 * <p>
 * 不同位数代表着不同意义，具体解释如下：
 * <ul>
 *     <li>1-3位：WMI制造商标示符，代表车辆制造商信息</li>
 *     <li>4-8位：VDS车型识别代码，代表车辆品牌、车系、车型及其排量等信息</li>
 *     <li>9位：校验位，通过公式计算出来，用于验证VIN码的正确性</li>
 *     <li>10位：年份代号，代表车辆生产的年份</li>
 *     <li>11位：工厂代码，代表车辆生产工厂信息</li>
 *     <li>12-17位：流水号，代表车辆的生产顺序号</li>
 * </ul>
 * VIN码可以找到汽车详细的个人、工程、制造方面的信息，是判定一个汽车合法性及其历史的重要依据。
 * <p>
 * 本实现参考以下标准：
 * <ul>
 *     <li><a href="https://www.iso.org/standard/52200.html">ISO 3779</a></li>
 *     <li><a href="http://www.catarc.org.cn/upload/202004/24/202004241005284241.pdf">车辆识别代号管理办法</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Vehicle_identification_number">Wikipedia</a></li>
 *     <li><a href="https://openstd.samr.gov.cn/bzgk/gb/newGbInfo?hcno=E2EBF667F8C032B1EDFD6DF9C1114E02">GB 16735-2019</a></li>
 * </ul>
 *
 * @author dax
 * @since 6.0.0
 */
public class VIN {
	/**
	 * 加权系数，见附录A，表A.3
	 */
	private static final int[] WEIGHT = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
	private static final int YEAR_LOOP = 30;
	private static final char[] YEAR_ID = {
		'A', 'B', 'C', 'D', 'E',
		'F', 'G', 'H', 'J', 'K',
		'L', 'M', 'N', 'P', 'R',
		'S', 'T', 'V', 'W', 'X',
		'Y', '1', '2', '3', '4',
		'5', '6', '7', '8', '9'
	};
	private static final Map<Character, Integer> YEAR_MAP;

	static {
		final Map<Character, Integer> yearMap = new HashMap<>(YEAR_ID.length, 1);
		for (int i = 0; i < YEAR_ID.length; i++) {
			yearMap.put(YEAR_ID[i], i);
		}
		YEAR_MAP = MapUtil.view(yearMap);
	}

	/**
	 * 创建VIN
	 *
	 * @param vinCode VIN码
	 * @return VIN对象
	 */
	public static VIN of(final String vinCode) {
		return new VIN(vinCode);
	}

	private final String code;

	/**
	 * 构造
	 *
	 * @param vinCode VIN码
	 */
	public VIN(final String vinCode) {
		Assert.isTrue(verify(vinCode), "Invalid VIN code!");
		this.code = vinCode;
	}

	/**
	 * 获取VIN码
	 *
	 * @return VIN码
	 */
	public String getCode(){
		return this.code;
	}

	/**
	 * 获取国家或地区代码
	 *
	 * @return 国家或地区代码
	 */
	public String getCountryCode() {
		return this.code.substring(0, 2);
	}

	/**
	 * 获取世界制造厂识别代号WMI（World Manufacturer Identifier）<br>
	 * 对年产量大于或等于1000辆的完整车辆或非完整车辆制造,车辆识别代号的第一部分为世界制造)厂识别代号(WMI)<br>
	 * 对年产量小于1000辆的完整车辆和/或非完整车辆制造厂，第三部分的三、四、五位与第一部分的三位字码一起构成世界制造厂识别代号(WMI)
	 *
	 * @return WMI
	 */
	public String getWMI() {
		final String wmi = this.code.substring(0, 3);
		return isLessThan1000() ? wmi + this.code.substring(11, 14) : wmi;
	}

	/**
	 * 是否是年产量小于1000的车辆制造厂
	 *
	 * @return 是否年产量小于1000
	 */
	public boolean isLessThan1000() {
		return '9' == this.code.charAt(2);
	}

	/**
	 * 获取车辆说明部分 VDS（Vehicle Descriptor section）
	 *
	 * @return VDS值
	 */
	public String getVDS() {
		return this.code.substring(3, 9);
	}

	/**
	 * 获取车辆特征代码（Vehicle Descriptor Code），相对于VDS，不包含校验位。
	 *
	 * @return 车辆特征代码
	 */
	public String getVehicleDescriptorCode() {
		return this.code.substring(3, 8);
	}

	/**
	 * 获取车辆指示部分 VIS（Vehicle Indicator Section）
	 *
	 * @return VIS
	 */
	public String getVIS() {
		return this.code.substring(9);
	}

	/**
	 * Get year.
	 *
	 * @return the year
	 */
	public Year getYear() {
		return getYear(1);
	}

	/**
	 * 获取装配厂字码
	 *
	 * @return 由厂家自行定义的装配厂字码 string
	 */
	public char getOemCode() {
		return this.code.charAt(10);
	}

	/**
	 * Gets year.
	 *
	 * @param multiple 1 代表从 1980年开始的第一个30年
	 * @return the year
	 */
	public Year getYear(final int multiple) {
		final int year = 1980 + YEAR_LOOP * multiple + YEAR_MAP.get(this.code.charAt(9)) % YEAR_LOOP;
		return Year.of(year);
	}

	/**
	 * 生产序号
	 * <p>
	 * 年产量大于1000为6位，年产量小于1000的为3位
	 *
	 * @return 生产序号 string
	 */
	public String getProdNo() {
		return this.code.substring(isLessThan1000() ? 14 : 11, 17);
	}

	/**
	 * 校验VIN码有效性，要求：
	 * <ul>
	 *     <li>满足正则：{@link PatternPool#CAR_VIN}</li>
	 *     <li>满足第9位校验位和计算的检验值一致</li>
	 * </ul>
	 *
	 * @param vinCode VIN码
	 * @return 是否有效
	 */
	public static boolean verify(final String vinCode) {
		Assert.notBlank(vinCode, "VIN code must be not blank!");
		if (!ReUtil.isMatch(PatternPool.CAR_VIN, vinCode)) {
			return false;
		}

		return vinCode.charAt(8) == calculateVerifyCode(vinCode);
	}

	/**
	 * 计算校验值，见附录A
	 *
	 * @param vinCode VIN码
	 * @return 校验值
	 */
	private static char calculateVerifyCode(final String vinCode) {
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += getWeightFactor(vinCode, i);
		}

		final int factor = sum % 11;
		return factor != 10 ? (char) (factor + '0') : 'X';
	}

	/**
	 * 获取对应位置字符的权重因子
	 *
	 * @param vinCode VIN码
	 * @param i       位置
	 * @return 权重因子
	 */
	private static int getWeightFactor(final String vinCode, final int i) {
		final char c = vinCode.charAt(i);
		return getVinValue(c) * WEIGHT[i];
	}

	/**
	 * 获取字母对应值（见：附录A，表A.2）
	 *
	 * @param vinCodeChar VIN编码中的字符
	 * @return 对应值
	 */
	private static int getVinValue(final char vinCodeChar) {
		switch (vinCodeChar) {
			case '0':
				return 0;
			case '1':
			case 'J':
			case 'A':
				return 1;
			case '2':
			case 'S':
			case 'K':
			case 'B':
				return 2;
			case '3':
			case 'T':
			case 'L':
			case 'C':
				return 3;
			case '4':
			case 'U':
			case 'M':
			case 'D':
				return 4;
			case '5':
			case 'V':
			case 'N':
			case 'E':
				return 5;
			case '6':
			case 'W':
			case 'F':
				return 6;
			case '7':
			case 'P':
			case 'X':
			case 'G':
				return 7;
			case '8':
			case 'Y':
			case 'H':
				return 8;
			case '9':
			case 'Z':
			case 'R':
				return 9;
		}
		throw new IllegalArgumentException("Invalid VIN char: " + vinCodeChar);
	}
}
