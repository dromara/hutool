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

package org.dromara.hutool.math;

import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.comparator.CompareUtil;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.CharUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Objects;
import java.util.Locale;

/**
 * 数字工具类<br>
 * 对于精确值计算应该使用 {@link BigDecimal}<br>
 * JDK7中<strong>BigDecimal(double val)</strong>构造方法的结果有一定的不可预知性，例如：
 *
 * <pre>
 * new BigDecimal(0.1)
 * </pre>
 * <p>
 * 表示的不是<strong>0.1</strong>而是<strong>0.1000000000000000055511151231257827021181583404541015625</strong>
 *
 * <p>
 * 这是因为0.1无法准确的表示为double。因此应该使用<strong>new BigDecimal(String)</strong>。
 * </p>
 * 相关介绍：
 * <ul>
 * <li><a href="https://github.com/venusdrogon/feilong-core/wiki/one-jdk7-bug-thinking">one-jdk7-bug-thinking</a></li>
 * </ul>
 * <p>
 *
 * @author Looly
 */
public class NumberUtil {

	/**
	 * 默认除法运算精度
	 */
	private static final int DEFAULT_DIV_SCALE = 10;

	// region ----- add

	/**
	 * 提供精确的加法运算<br>
	 * 如果传入多个值为null或者空，则返回0
	 *
	 * @param values 多个被加值
	 * @return 和
	 * @since 4.0.0
	 */
	public static BigDecimal add(final Number... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.add(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 提供精确的加法运算<br>
	 * 如果传入多个值为null或者空，则返回
	 *
	 * <p>
	 *     需要注意的是，在不同Locale下，数字的表示形式也是不同的，例如：<br>
	 *     德国、荷兰、比利时、丹麦、意大利、罗马尼亚和欧洲大多地区使用`,`区分小数<br>
	 *     也就是说，在这些国家地区，1.20表示120，而非1.2。
	 * </p>
	 *
	 * @param values 多个被加值
	 * @return 和
	 * @since 4.0.0
	 */
	public static BigDecimal add(final String... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		String value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (StrUtil.isNotBlank(value)) {
				result = result.add(toBigDecimal(value));
			}
		}
		return result;
	}
	// endregion

	// region ----- sub

	/**
	 * 提供精确的减法运算<br>
	 * 如果传入多个值为null或者空，则返回0
	 *
	 * @param values 多个被减值
	 * @return 差
	 * @since 4.0.0
	 */
	public static BigDecimal sub(final Number... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (null != value) {
				result = result.subtract(toBigDecimal(value));
			}
		}
		return result;
	}

	/**
	 * 提供精确的减法运算<br>
	 * 如果传入多个值为null或者空，则返回0
	 *
	 * @param values 多个被减值
	 * @return 差
	 * @since 4.0.0
	 */
	public static BigDecimal sub(final String... values) {
		if (ArrayUtil.isEmpty(values)) {
			return BigDecimal.ZERO;
		}

		String value = values[0];
		BigDecimal result = toBigDecimal(value);
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			if (StrUtil.isNotBlank(value)) {
				result = result.subtract(toBigDecimal(value));
			}
		}
		return result;
	}
	// endregion

	// region ----- mul

	/**
	 * 提供精确的乘法运算<br>
	 * 如果传入多个值为null或者空，则返回0
	 *
	 * @param values 多个被乘值
	 * @return 积
	 * @since 4.0.0
	 */
	public static BigDecimal mul(final Number... values) {
		if (ArrayUtil.isEmpty(values) || ArrayUtil.hasNull(values)) {
			return BigDecimal.ZERO;
		}

		Number value = values[0];
		BigDecimal result = new BigDecimal(value.toString());
		for (int i = 1; i < values.length; i++) {
			value = values[i];
			result = result.multiply(new BigDecimal(value.toString()));
		}
		return result;
	}

	/**
	 * 提供精确的乘法运算<br>
	 * 如果传入多个值为null或者空，则返回0
	 *
	 * @param values 多个被乘值
	 * @return 积
	 * @since 4.0.0
	 */
	public static BigDecimal mul(final String... values) {
		if (ArrayUtil.isEmpty(values) || ArrayUtil.hasNull(values)) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = new BigDecimal(values[0]);
		for (int i = 1; i < values.length; i++) {
			result = result.multiply(new BigDecimal(values[i]));
		}

		return result;
	}
	// endregion

	// region ----- div

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 * @since 3.1.0
	 */
	public static BigDecimal div(final Number v1, final Number v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static BigDecimal div(final String v1, final String v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
	 *
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 精确度，如果为负值，取绝对值
	 * @return 两个参数的商
	 * @since 3.1.0
	 */
	public static BigDecimal div(final Number v1, final Number v2, final int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
	 *
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 精确度，如果为负值，取绝对值
	 * @return 两个参数的商
	 */
	public static BigDecimal div(final String v1, final String v2, final int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 *
	 * @param v1           被除数
	 * @param v2           除数
	 * @param scale        精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 */
	public static BigDecimal div(final String v1, final String v2, final int scale, final RoundingMode roundingMode) {
		return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 *
	 * @param v1           被除数
	 * @param v2           除数
	 * @param scale        精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 * @since 3.1.0
	 */
	public static BigDecimal div(Number v1, final Number v2, int scale, final RoundingMode roundingMode) {
		Assert.notNull(v2, "Divisor must be not null !");
		if (null == v1) {
			v1 = BigDecimal.ZERO;
		}
		if (v1 instanceof BigDecimal && v2 instanceof BigDecimal) {
			if (scale < 0) {
				scale = -scale;
			}
			return ((BigDecimal) v1).divide((BigDecimal) v2, scale, roundingMode);
		}
		return div(StrUtil.toStringOrNull(v1), StrUtil.toStringOrNull(v2), scale, roundingMode);
	}

	/**
	 * 补充Math.ceilDiv() JDK8中添加了和 {@link Math#floorDiv(int, int)} 但却没有ceilDiv()
	 *
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 * @since 5.3.3
	 */
	public static int ceilDiv(final int v1, final int v2) {
		return (int) Math.ceil((double) v1 / v2);
	}
	// endregion

	// region ----- round

	/**
	 * 保留固定位数小数<br>
	 * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
	 * 例如保留2位小数：123.456789 =》 123.46
	 *
	 * @param v     值
	 * @param scale 保留小数位数
	 * @return 新值
	 */
	public static BigDecimal round(final double v, final int scale) {
		return round(v, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 保留固定位数小数<br>
	 * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
	 * 例如保留2位小数：123.456789 =》 123.46
	 *
	 * @param v     值
	 * @param scale 保留小数位数
	 * @return 新值
	 */
	public static String roundStr(final double v, final int scale) {
		return round(v, scale).toPlainString();
	}

	/**
	 * 保留固定位数小数<br>
	 * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
	 * 例如保留2位小数：123.456789 =》 123.46
	 *
	 * @param numberStr 数字值的字符串表现形式
	 * @param scale     保留小数位数
	 * @return 新值
	 */
	public static BigDecimal round(final String numberStr, final int scale) {
		return round(numberStr, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 保留固定位数小数<br>
	 * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
	 * 例如保留2位小数：123.456789 =》 123.46
	 *
	 * @param number 数字值
	 * @param scale  保留小数位数
	 * @return 新值
	 * @since 4.1.0
	 */
	public static BigDecimal round(final BigDecimal number, final int scale) {
		return round(number, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 保留固定位数小数<br>
	 * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
	 * 例如保留2位小数：123.456789 =》 123.46
	 *
	 * @param numberStr 数字值的字符串表现形式
	 * @param scale     保留小数位数
	 * @return 新值
	 * @since 3.2.2
	 */
	public static String roundStr(final String numberStr, final int scale) {
		return round(numberStr, scale).toPlainString();
	}

	/**
	 * 保留固定位数小数<br>
	 * 例如保留四位小数：123.456789 =》 123.4567
	 *
	 * @param v            值
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 新值
	 */
	public static BigDecimal round(final double v, final int scale, final RoundingMode roundingMode) {
		return round(Double.toString(v), scale, roundingMode);
	}

	/**
	 * 保留固定位数小数<br>
	 * 例如保留四位小数：123.456789 =》 123.4567
	 *
	 * @param v            值
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 新值
	 * @since 3.2.2
	 */
	public static String roundStr(final double v, final int scale, final RoundingMode roundingMode) {
		return round(v, scale, roundingMode).toPlainString();
	}

	/**
	 * 保留固定位数小数<br>
	 * 例如保留四位小数：123.456789 =》 123.4567
	 *
	 * @param numberStr    数字值的字符串表现形式
	 * @param scale        保留小数位数，如果传入小于0，则默认0
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
	 * @return 新值
	 */
	public static BigDecimal round(final String numberStr, int scale, final RoundingMode roundingMode) {
		Assert.notBlank(numberStr);
		if (scale < 0) {
			scale = 0;
		}
		return round(toBigDecimal(numberStr), scale, roundingMode);
	}

	/**
	 * 保留固定位数小数<br>
	 * 例如保留四位小数：123.456789 =》 123.4567
	 *
	 * @param number       数字值
	 * @param scale        保留小数位数，如果传入小于0，则默认0
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
	 * @return 新值
	 */
	public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
		if (null == number) {
			number = BigDecimal.ZERO;
		}
		if (scale < 0) {
			scale = 0;
		}
		if (null == roundingMode) {
			roundingMode = RoundingMode.HALF_UP;
		}

		return number.setScale(scale, roundingMode);
	}

	/**
	 * 保留固定位数小数<br>
	 * 例如保留四位小数：123.456789 =》 123.4567
	 *
	 * @param numberStr    数字值的字符串表现形式
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 新值
	 * @since 3.2.2
	 */
	public static String roundStr(final String numberStr, final int scale, final RoundingMode roundingMode) {
		return round(numberStr, scale, roundingMode).toPlainString();
	}

	/**
	 * 四舍六入五成双计算法
	 * <p>
	 * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
	 * </p>
	 *
	 * <pre>
	 * 算法规则:
	 * 四舍六入五考虑，
	 * 五后非零就进一，
	 * 五后皆零看奇偶，
	 * 五前为偶应舍去，
	 * 五前为奇要进一。
	 * </pre>
	 *
	 * @param number 需要科学计算的数据
	 * @param scale  保留的小数位
	 * @return 结果
	 * @since 4.1.0
	 */
	public static BigDecimal roundHalfEven(final Number number, final int scale) {
		return roundHalfEven(toBigDecimal(number), scale);
	}

	/**
	 * 四舍六入五成双计算法
	 * <p>
	 * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
	 * </p>
	 *
	 * <pre>
	 * 算法规则:
	 * 四舍六入五考虑，
	 * 五后非零就进一，
	 * 五后皆零看奇偶，
	 * 五前为偶应舍去，
	 * 五前为奇要进一。
	 * </pre>
	 *
	 * @param value 需要科学计算的数据
	 * @param scale 保留的小数位
	 * @return 结果
	 * @since 4.1.0
	 */
	public static BigDecimal roundHalfEven(final BigDecimal value, final int scale) {
		return round(value, scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * 保留固定小数位数，舍去多余位数
	 *
	 * @param number 需要科学计算的数据
	 * @param scale  保留的小数位
	 * @return 结果
	 * @since 4.1.0
	 */
	public static BigDecimal roundDown(final Number number, final int scale) {
		return roundDown(toBigDecimal(number), scale);
	}

	/**
	 * 保留固定小数位数，舍去多余位数
	 *
	 * @param value 需要科学计算的数据
	 * @param scale 保留的小数位
	 * @return 结果
	 * @since 4.1.0
	 */
	public static BigDecimal roundDown(final BigDecimal value, final int scale) {
		return round(value, scale, RoundingMode.DOWN);
	}
	// endregion

	// region ----- decimalFormat

	/**
	 * 格式化double<br>
	 * 对 {@link DecimalFormat} 做封装<br>
	 *
	 * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
	 *                <ul>
	 *                <li>0 =》 取一位整数</li>
	 *                <li>0.00 =》 取一位整数和两位小数</li>
	 *                <li>00.000 =》 取两位整数和三位小数</li>
	 *                <li># =》 取所有整数部分</li>
	 *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
	 *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
	 *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
	 *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
	 *                </ul>
	 * @param value   值
	 * @return 格式化后的值
	 */
	public static String format(final String pattern, final double value) {
		Assert.isTrue(isValid(value), "value is NaN or Infinite!");
		return new DecimalFormat(pattern).format(value);
	}

	/**
	 * 格式化double<br>
	 * 对 {@link DecimalFormat} 做封装<br>
	 *
	 * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
	 *                <ul>
	 *                <li>0 =》 取一位整数</li>
	 *                <li>0.00 =》 取一位整数和两位小数</li>
	 *                <li>00.000 =》 取两位整数和三位小数</li>
	 *                <li># =》 取所有整数部分</li>
	 *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
	 *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
	 *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
	 *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
	 *                </ul>
	 * @param value   值
	 * @return 格式化后的值
	 * @since 3.0.5
	 */
	public static String format(final String pattern, final long value) {
		return new DecimalFormat(pattern).format(value);
	}

	/**
	 * 格式化double<br>
	 * 对 {@link DecimalFormat} 做封装<br>
	 *
	 * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
	 *                <ul>
	 *                <li>0 =》 取一位整数</li>
	 *                <li>0.00 =》 取一位整数和两位小数</li>
	 *                <li>00.000 =》 取两位整数和三位小数</li>
	 *                <li># =》 取所有整数部分</li>
	 *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
	 *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
	 *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
	 *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
	 *                </ul>
	 * @param value   值，支持BigDecimal、BigInteger、Number等类型
	 * @return 格式化后的值
	 * @since 5.1.6
	 */
	public static String format(final String pattern, final Object value) {
		return format(pattern, value, null);
	}

	/**
	 * 格式化double<br>
	 * 对 {@link DecimalFormat} 做封装<br>
	 *
	 * @param pattern      格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
	 *                     <ul>
	 *                     <li>0 =》 取一位整数</li>
	 *                     <li>0.00 =》 取一位整数和两位小数</li>
	 *                     <li>00.000 =》 取两位整数和三位小数</li>
	 *                     <li># =》 取所有整数部分</li>
	 *                     <li>#.##% =》 以百分比方式计数，并取两位小数</li>
	 *                     <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
	 *                     <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
	 *                     <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
	 *                     </ul>
	 * @param value        值，支持BigDecimal、BigInteger、Number等类型
	 * @param roundingMode 保留小数的方式枚举
	 * @return 格式化后的值
	 * @since 5.6.5
	 */
	public static String format(final String pattern, final Object value, final RoundingMode roundingMode) {
		if (value instanceof Number) {
			Assert.isTrue(isValidNumber((Number) value), "value is NaN or Infinite!");
		}
		final DecimalFormat decimalFormat = new DecimalFormat(pattern);
		if (null != roundingMode) {
			decimalFormat.setRoundingMode(roundingMode);
		}
		return decimalFormat.format(value);
	}

	/**
	 * 格式化金额输出，每三位用逗号分隔
	 *
	 * @param value 金额
	 * @return 格式化后的值
	 * @since 3.0.9
	 */
	public static String formatMoney(final double value) {
		return format(",##0.00", value);
	}

	/**
	 * 格式化百分比，小数采用四舍五入方式
	 *
	 * @param number 值
	 * @param scale  保留小数位数
	 * @return 百分比
	 * @since 3.2.3
	 */
	public static String formatPercent(final double number, final int scale) {
		final NumberFormat format = NumberFormat.getPercentInstance();
		format.setMaximumFractionDigits(scale);
		return format.format(number);
	}
	// endregion

	// region ----- isXXX

	/**
	 * 是否为数字，支持包括：
	 *
	 * <pre>
	 * 1、10进制
	 * 2、16进制数字（0x开头）
	 * 3、科学计数法形式（1234E3）
	 * 4、类型标识形式（123D）
	 * 5、正负数标识形式（+123、-234）
	 * 6、八进制数字(0开头)
	 * </pre>
	 *
	 * @param str 字符串值, 不可以含有任何空白字符
	 * @return 是否为数字
	 */
	public static boolean isNumber(final CharSequence str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		final char[] chars = str.toString().toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		final int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1 && chars[start] == '0' && !StrUtil.contains(str, CharUtil.DOT)) { // leading 0, skip if is a decimal number
			if (chars[start + 1] == 'x' || chars[start + 1] == 'X') { // leading 0x/0X
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
			if (Character.isDigit(chars[start + 1])) {
				// leading 0, but not hex, must be octal
				int i = start + 1;
				for (; i < chars.length; i++) {
					if (chars[i] < '0' || chars[i] > '7') {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (false == foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent or decimal point
				return foundDigit && !hasExp && !hasDecPoint;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return false == allowSigns && foundDigit;
	}

	/**
	 * 判断字符串是否是整数
	 *
	 * <p>支持格式:
	 * <ol>
	 * 		<li>10进制, 不能包含前导零</li>
	 * 		<li>8进制(以0开头)</li>
	 * 		<li>16进制(以0x或者0X开头)</li>
	 * </ol>
	 *
	 * @param s 校验的字符串, 只能含有 正负号、数字字符 和 {@literal X/x}
	 * @return 是否为 {@link Integer}类型
	 * @apiNote 6.0.0 支持8进制和16进制
	 * @see Integer#decode(String)
	 */
	public static boolean isInteger(final String s) {
		if (false == isNumber(s)) {
			return false;
		}
		try {
			//noinspection ResultOfMethodCallIgnored
			Integer.decode(s);
		} catch (final NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否是Long类型<br>
	 *
	 * <p>支持格式:
	 * <ol>
	 * 		<li>10进制, 不能包含前导零</li>
	 * 		<li>8进制(以0开头)</li>
	 * 		<li>16进制(以0x或者0X开头)</li>
	 * </ol>
	 *
	 * @param s 校验的字符串, 只能含有 正负号、数字字符、{@literal X/x} 和 后缀{@literal L/l}
	 * @return 是否为 {@link Long}类型
	 * @apiNote 6.0.0 支持8进制和16进制数字
	 * @since 4.0.0
	 */
	public static boolean isLong(final String s) {
		if (false == isNumber(s)) {
			return false;
		}
		final char lastChar = s.charAt(s.length() - 1);
		if (lastChar == 'l' || lastChar == 'L') {
			return true;
		}
		try {
			//noinspection ResultOfMethodCallIgnored
			Long.decode(s);
		} catch (final NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否是浮点数
	 *
	 * @param s String
	 * @return 是否为 {@link Double}类型
	 */
	public static boolean isDouble(final String s) {
		if (StrUtil.isBlank(s)) {
			return false;
		}
		try {
			Double.parseDouble(s);
		} catch (final NumberFormatException ignore) {
			return false;
		}
		return s.contains(".");
	}

	/**
	 * 是否是质数（素数）<br>
	 * 质数表的质数又称素数。指整数在一个大于1的自然数中,除了1和此整数自身外,没法被其他自然数整除的数。
	 *
	 * @param n 数字
	 * @return 是否是质数
	 */
	public static boolean isPrime(final int n) {
		Assert.isTrue(n > 1, "The number must be > 1");
		if (n <= 3) {
			return true;
		} else if ((n & 1) == 0) {
			// 快速排除偶数
			return false;
		}
		final int end = (int) Math.sqrt(n);
		for (int i = 3; i <= end; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
	// endregion

	// region ----- range

	/**
	 * 生成一个从0开始的数字列表<br>
	 *
	 * @param stopIncluded 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(final int stopIncluded) {
		return range(0, stopIncluded, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 *
	 * @param startInclude 开始的数字（包含）
	 * @param stopIncluded 结束的数字（包含）
	 * @return 数字列表
	 */
	public static int[] range(final int startInclude, final int stopIncluded) {
		return range(startInclude, stopIncluded, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 *
	 * @param startInclude 开始的数字（包含）
	 * @param stopIncluded 结束的数字（不包含）
	 * @param step         步进
	 * @return 数字列表
	 */
	public static int[] range(int startInclude, int stopIncluded, int step) {
		if (startInclude > stopIncluded) {
			final int tmp = startInclude;
			startInclude = stopIncluded;
			stopIncluded = tmp;
		}

		if (step <= 0) {
			step = 1;
		}

		final int deviation = stopIncluded + 1 - startInclude;
		int length = deviation / step;
		if (deviation % step != 0) {
			length += 1;
		}
		final int[] range = new int[length];
		for (int i = 0; i < length; i++) {
			range[i] = startInclude;
			startInclude += step;
		}
		return range;
	}

	/**
	 * 将给定范围内的整数添加到已有集合中，步进为1
	 *
	 * @param start  开始（包含）
	 * @param stop   结束（包含）
	 * @param values 集合
	 * @return 集合
	 */
	public static Collection<Integer> appendRange(final int start, final int stop, final Collection<Integer> values) {
		return appendRange(start, stop, 1, values);
	}

	/**
	 * 将给定范围内的整数添加到已有集合中
	 *
	 * @param startInclude 开始（包含）
	 * @param stopInclude  结束（包含）
	 * @param step         步进
	 * @param values       集合
	 * @return 集合
	 */
	public static Collection<Integer> appendRange(final int startInclude, final int stopInclude, int step, final Collection<Integer> values) {
		if (startInclude < stopInclude) {
			step = Math.abs(step);
		} else if (startInclude > stopInclude) {
			step = -Math.abs(step);
		} else {// start == end
			values.add(startInclude);
			return values;
		}

		for (int i = startInclude; (step > 0) ? i <= stopInclude : i >= stopInclude; i += step) {
			values.add(i);
		}
		return values;
	}
	// endregion

	// ------------------------------------------------------------------------------------------- others

	/**
	 * 获得数字对应的二进制字符串
	 *
	 * @param number 数字
	 * @return 二进制字符串
	 */
	public static String getBinaryStr(final Number number) {
		if (number instanceof Long) {
			return Long.toBinaryString((Long) number);
		} else if (number instanceof Integer) {
			return Integer.toBinaryString((Integer) number);
		} else {
			return Long.toBinaryString(number.longValue());
		}
	}

	/**
	 * 二进制转int
	 *
	 * @param binaryStr 二进制字符串
	 * @return int
	 */
	public static int binaryToInt(final String binaryStr) {
		return Integer.parseInt(binaryStr, 2);
	}

	/**
	 * 二进制转long
	 *
	 * @param binaryStr 二进制字符串
	 * @return long
	 */
	public static long binaryToLong(final String binaryStr) {
		return Long.parseLong(binaryStr, 2);
	}

	// region ----- equals

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法通过调用{@link Double#doubleToLongBits(double)}方法来判断是否相等<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * @param num1 数字1
	 * @param num2 数字2
	 * @return 是否相等
	 * @since 5.4.2
	 */
	public static boolean equals(final double num1, final double num2) {
		return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
	}

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法通过调用{@link Float#floatToIntBits(float)}方法来判断是否相等<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * @param num1 数字1
	 * @param num2 数字2
	 * @return 是否相等
	 * @since 5.4.5
	 */
	public static boolean equals(final float num1, final float num2) {
		return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
	}

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法修复传入long型数据由于没有本类型重载方法,导致数据精度丢失
	 *
	 * @param num1 数字1
	 * @param num2 数字2
	 * @return 是否相等
	 * @since 5.7.19
	 */
	public static boolean equals(final long num1, final long num2) {
		return num1 == num2;
	}

	/**
	 * 比较数字值是否相等，相等返回{@code true}<br>
	 * 需要注意的是{@link BigDecimal}需要特殊处理<br>
	 * BigDecimal使用compareTo方式判断，因为使用equals方法也判断小数位数，如2.0和2.00就不相等，<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * <ul>
	 *     <li>如果用户提供两个Number都是{@link BigDecimal}，则通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等</li>
	 *     <li>其他情况调用{@link Number#equals(Object)}比较</li>
	 * </ul>
	 *
	 * @param number1 数字1
	 * @param number2 数字2
	 * @return 是否相等
	 * @see CompareUtil#equals(Comparable, Comparable)
	 * @see Objects#equals(Object, Object)
	 */
	public static boolean equals(final Number number1, final Number number2) {
		if (number1 instanceof BigDecimal && number2 instanceof BigDecimal) {
			// BigDecimal使用compareTo方式判断，因为使用equals方法也判断小数位数，如2.0和2.00就不相等
			return CompareUtil.equals((BigDecimal) number1, (BigDecimal) number2);
		}
		return Objects.equals(number1, number2);
	}

	/**
	 * 比较两个字符是否相同
	 *
	 * @param c1         字符1
	 * @param c2         字符2
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否相同
	 * @see CharUtil#equals(char, char, boolean)
	 * @since 3.2.1
	 */
	public static boolean equals(final char c1, final char c2, final boolean ignoreCase) {
		return CharUtil.equals(c1, c2, ignoreCase);
	}
	// endregion

	// region ----- toStr

	/**
	 * 数字转字符串<br>
	 * 调用{@link Number#toString()}，并去除尾小数点儿后多余的0
	 *
	 * @param number       A Number
	 * @param defaultValue 如果number参数为{@code null}，返回此默认值
	 * @return A String.
	 * @since 3.0.9
	 */
	public static String toStr(final Number number, final String defaultValue) {
		return (null == number) ? defaultValue : toStr(number);
	}

	/**
	 * 数字转字符串<br>
	 * 调用{@link Number#toString()}或 {@link BigDecimal#toPlainString()}，并去除尾小数点儿后多余的0
	 *
	 * @param number A Number
	 * @return A String.
	 */
	public static String toStr(final Number number) {
		return toStr(number, true);
	}

	/**
	 * 数字转字符串<br>
	 * 调用{@link Number#toString()}或 {@link BigDecimal#toPlainString()}，并去除尾小数点儿后多余的0
	 *
	 * @param number               A Number
	 * @param isStripTrailingZeros 是否去除末尾多余0，例如5.0返回5
	 * @return A String.
	 */
	public static String toStr(final Number number, final boolean isStripTrailingZeros) {
		Assert.notNull(number, "Number is null !");

		// BigDecimal单独处理，使用非科学计数法
		if (number instanceof BigDecimal) {
			return toStr((BigDecimal) number, isStripTrailingZeros);
		}

		Assert.isTrue(isValidNumber(number), "Number is non-finite!");
		// 去掉小数点儿后多余的0
		String string = number.toString();
		if (isStripTrailingZeros) {
			if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string.indexOf('E') < 0) {
				while (string.endsWith("0")) {
					string = string.substring(0, string.length() - 1);
				}
				if (string.endsWith(".")) {
					string = string.substring(0, string.length() - 1);
				}
			}
		}
		return string;
	}

	/**
	 * {@link BigDecimal}数字转字符串<br>
	 * 调用{@link BigDecimal#toPlainString()}，并去除尾小数点儿后多余的0
	 *
	 * @param bigDecimal A {@link BigDecimal}
	 * @return A String.
	 * @since 5.4.6
	 */
	public static String toStr(final BigDecimal bigDecimal) {
		return toStr(bigDecimal, true);
	}

	/**
	 * {@link BigDecimal}数字转字符串<br>
	 * 调用{@link BigDecimal#toPlainString()}，可选去除尾小数点儿后多余的0
	 *
	 * @param bigDecimal           A {@link BigDecimal}
	 * @param isStripTrailingZeros 是否去除末尾多余0，例如5.0返回5
	 * @return A String.
	 * @since 5.4.6
	 */
	public static String toStr(BigDecimal bigDecimal, final boolean isStripTrailingZeros) {
		Assert.notNull(bigDecimal, "BigDecimal is null !");
		if (isStripTrailingZeros) {
			bigDecimal = bigDecimal.stripTrailingZeros();
		}
		return bigDecimal.toPlainString();
	}
	// endregion

	/**
	 * 数字转{@link BigDecimal}<br>
	 * Float、Double等有精度问题，转换为字符串后再转换<br>
	 * null转换为0
	 *
	 * @param number 数字
	 * @return {@link BigDecimal}
	 * @since 4.0.9
	 */
	public static BigDecimal toBigDecimal(final Number number) {
		if (null == number) {
			return BigDecimal.ZERO;
		}

		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof Long) {
			return new BigDecimal((Long) number);
		} else if (number instanceof Integer) {
			return new BigDecimal((Integer) number);
		} else if (number instanceof BigInteger) {
			return new BigDecimal((BigInteger) number);
		}

		// Float、Double等有精度问题，转换为字符串后再转换
		return toBigDecimal(number.toString());
	}

	/**
	 * 数字转{@link BigDecimal}<br>
	 * null或""或空白符转换为0
	 *
	 * @param numberStr 数字字符串
	 * @return {@link BigDecimal}
	 * @since 4.0.9
	 */
	public static BigDecimal toBigDecimal(final String numberStr) {
		if (StrUtil.isBlank(numberStr)) {
			return BigDecimal.ZERO;
		}

		try {
			// 支持类似于 1,234.55 格式的数字
			final Number number = parseNumber(numberStr);
			if (number instanceof BigDecimal) {
				return (BigDecimal) number;
			} else {
				return new BigDecimal(number.toString());
			}
		} catch (final Exception ignore) {
			// 忽略解析错误
		}

		return new BigDecimal(numberStr);
	}

	/**
	 * 数字转{@link BigInteger}<br>
	 * null转换为0
	 *
	 * @param number 数字
	 * @return {@link BigInteger}
	 * @since 5.4.5
	 */
	public static BigInteger toBigInteger(final Number number) {
		if (null == number) {
			return BigInteger.ZERO;
		}

		if (number instanceof BigInteger) {
			return (BigInteger) number;
		} else if (number instanceof Long) {
			return BigInteger.valueOf((Long) number);
		}

		return toBigInteger(number.longValue());
	}

	/**
	 * 数字转{@link BigInteger}<br>
	 * null或""或空白符转换为0
	 *
	 * @param number 数字字符串
	 * @return {@link BigInteger}
	 * @since 5.4.5
	 */
	public static BigInteger toBigInteger(final String number) {
		return StrUtil.isBlank(number) ? BigInteger.ZERO : new BigInteger(number);
	}

	/**
	 * 计算等份个数
	 *
	 * @param total 总数
	 * @param part  每份的个数
	 * @return 分成了几份
	 * @since 3.0.6
	 */
	public static int count(final int total, final int part) {
		return (total % part == 0) ? (total / part) : (total / part + 1);
	}

	/**
	 * 空转0
	 *
	 * @param decimal {@link BigDecimal}，可以为{@code null}
	 * @return {@link BigDecimal}参数为空时返回0的值
	 * @since 3.0.9
	 */
	public static BigDecimal null2Zero(final BigDecimal decimal) {

		return decimal == null ? BigDecimal.ZERO : decimal;
	}

	/**
	 * 如果给定值为0，返回1，否则返回原值
	 *
	 * @param value 值
	 * @return 1或非0值
	 * @since 3.1.2
	 */
	public static int zero2One(final int value) {
		return 0 == value ? 1 : value;
	}

	/**
	 * 创建{@link BigInteger}，支持16进制、10进制和8进制，如果传入空白串返回null<br>
	 * from Apache Common Lang
	 *
	 * @param str 数字字符串
	 * @return {@link BigInteger}
	 * @since 3.2.1
	 */
	public static BigInteger newBigInteger(String str) {
		str = StrUtil.trimToNull(str);
		if (null == str) {
			return null;
		}

		int pos = 0; // 数字字符串位置
		int radix = 10;
		boolean negate = false; // 负数与否
		if (str.startsWith("-")) {
			negate = true;
			pos = 1;
		}
		if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
			// hex
			radix = 16;
			pos += 2;
		} else if (str.startsWith("#", pos)) {
			// alternative hex (allowed by Long/Integer)
			radix = 16;
			pos++;
		} else if (str.startsWith("0", pos) && str.length() > pos + 1) {
			// octal; so long as there are additional digits
			radix = 8;
			pos++;
		} // default is to treat as decimal

		if (pos > 0) {
			str = str.substring(pos);
		}
		final BigInteger value = new BigInteger(str, radix);
		return negate ? value.negate() : value;
	}

	/**
	 * 判断两个数字是否相邻，例如1和2相邻，1和3不相邻<br>
	 * 判断方法为做差取绝对值判断是否为1
	 *
	 * @param number1 数字1
	 * @param number2 数字2
	 * @return 是否相邻
	 * @since 4.0.7
	 */
	public static boolean isBeside(final long number1, final long number2) {
		return Math.abs(number1 - number2) == 1;
	}

	/**
	 * 判断两个数字是否相邻，例如1和2相邻，1和3不相邻<br>
	 * 判断方法为做差取绝对值判断是否为1
	 *
	 * @param number1 数字1
	 * @param number2 数字2
	 * @return 是否相邻
	 * @since 4.0.7
	 */
	public static boolean isBeside(final int number1, final int number2) {
		return Math.abs(number1 - number2) == 1;
	}

	/**
	 * 把给定的总数平均分成N份，返回每份的个数<br>
	 * 当除以分数有余数时每份+1
	 *
	 * @param total     总数
	 * @param partCount 份数
	 * @return 每份的个数
	 * @since 4.0.7
	 */
	public static int partValue(final int total, final int partCount) {
		return partValue(total, partCount, true);
	}

	/**
	 * 把给定的总数平均分成N份，返回每份的个数<br>
	 * 如果isPlusOneWhenHasRem为true，则当除以分数有余数时每份+1，否则丢弃余数部分
	 *
	 * @param total               总数
	 * @param partCount           份数
	 * @param isPlusOneWhenHasRem 在有余数时是否每份+1
	 * @return 每份的个数
	 * @since 4.0.7
	 */
	public static int partValue(final int total, final int partCount, final boolean isPlusOneWhenHasRem) {
		int partValue = total / partCount;
		if (isPlusOneWhenHasRem && total % partCount > 0) {
			partValue++;
		}
		return partValue;
	}

	/**
	 * 提供精确的幂运算
	 *
	 * @param number 底数
	 * @param n      指数
	 * @return 幂的积
	 * @since 4.1.0
	 */
	public static BigDecimal pow(final Number number, final int n) {
		return pow(toBigDecimal(number), n);
	}

	/**
	 * 提供精确的幂运算
	 *
	 * @param number 底数
	 * @param n      指数
	 * @return 幂的积
	 * @since 4.1.0
	 */
	public static BigDecimal pow(final BigDecimal number, final int n) {
		return number.pow(n);
	}


	/**
	 * 判断一个整数是否是2的幂
	 *
	 * @param n 待验证的整数
	 * @return 如果n是2的幂返回true, 反之返回false
	 */
	public static boolean isPowerOfTwo(final long n) {
		return (n > 0) && ((n & (n - 1)) == 0);
	}

	// region ----- parse

	/**
	 * 解析转换数字字符串为 {@link java.lang.Integer } 规则如下：
	 *
	 * <pre>
	 * 1、0x开头的视为16进制数字
	 * 2、0开头的忽略开头的0
	 * 3、其它情况按照10进制转换
	 * 4、空串返回0
	 * 5、.123形式返回0（按照小于0的小数对待）
	 * 6、123.56截取小数点之前的数字，忽略小数部分
	 * 7、解析失败返回默认值
	 * </pre>
	 *
	 * @param numberStr    数字字符串，支持0x开头、0开头和普通十进制
	 * @param defaultValue 如果解析失败, 将返回defaultValue, 允许null
	 * @return Integer
	 */
	public static Integer parseInt(final String numberStr, final Integer defaultValue) {
		if (StrUtil.isNotBlank(numberStr)) {
			try {
				return parseInt(numberStr);
			} catch (final NumberFormatException ignore) {
				// ignore
			}
		}
		return defaultValue;
	}

	/**
	 * 解析转换数字字符串为int型数字，规则如下：
	 *
	 * <pre>
	 * 1、0x开头的视为16进制数字
	 * 2、0开头的忽略开头的0
	 * 3、其它情况按照10进制转换
	 * 4、空串返回0
	 * 5、.123形式返回0（按照小于0的小数对待）
	 * 6、123.56截取小数点之前的数字，忽略小数部分
	 * 7、科学计数法抛出NumberFormatException异常
	 * </pre>
	 *
	 * @param number 数字，支持0x开头、0开头和普通十进制
	 * @return int
	 * @throws NumberFormatException 数字格式异常
	 * @since 4.1.4
	 */
	public static int parseInt(final String number) throws NumberFormatException {
		if (StrUtil.isBlank(number)) {
			return 0;
		}

		if (StrUtil.containsIgnoreCase(number, "E")) {
			// 科学计数法忽略支持，科学计数法一般用于表示非常小和非常大的数字，这类数字转换为int后精度丢失，没有意义。
			throw new NumberFormatException(StrUtil.format("Unsupported int format: [{}]", number));
		}

		if (StrUtil.startWithIgnoreCase(number, "0x")) {
			// 0x04表示16进制数
			return Integer.parseInt(number.substring(2), 16);
		}

		try {
			return Integer.parseInt(number);
		} catch (final NumberFormatException e) {
			return parseNumber(number).intValue();
		}
	}

	/**
	 * 解析转换数字字符串为 {@link java.lang.Long } 规则如下：
	 *
	 * <pre>
	 * 1、0x开头的视为16进制数字
	 * 2、0开头的忽略开头的0
	 * 3、其它情况按照10进制转换
	 * 4、空串返回0
	 * 5、.123形式返回0（按照小于0的小数对待）
	 * 6、123.56截取小数点之前的数字，忽略小数部分
	 * 7、解析失败返回默认值
	 * </pre>
	 *
	 * @param numberStr    数字字符串，支持0x开头、0开头和普通十进制
	 * @param defaultValue 如果解析失败, 将返回defaultValue, 允许null
	 * @return Long
	 */
	public static Long parseLong(final String numberStr, final Long defaultValue) {
		if (StrUtil.isNotBlank(numberStr)) {
			try {
				return parseLong(numberStr);
			} catch (final NumberFormatException ignore) {
				// ignore
			}
		}

		return defaultValue;
	}

	/**
	 * 解析转换数字字符串为long型数字，规则如下：
	 *
	 * <pre>
	 * 1、0x开头的视为16进制数字
	 * 2、0开头的忽略开头的0
	 * 3、空串返回0
	 * 4、其它情况按照10进制转换
	 * 5、.123形式返回0（按照小于0的小数对待）
	 * 6、123.56截取小数点之前的数字，忽略小数部分
	 * </pre>
	 *
	 * @param number 数字，支持0x开头、0开头和普通十进制
	 * @return long
	 * @since 4.1.4
	 */
	public static long parseLong(final String number) {
		if (StrUtil.isBlank(number)) {
			return 0L;
		}

		if (StrUtil.startWithIgnoreCase(number, "0x")) {
			// 0x04表示16进制数
			return Long.parseLong(number.substring(2), 16);
		}

		try {
			return Long.parseLong(number);
		} catch (final NumberFormatException e) {
			return parseNumber(number).longValue();
		}
	}

	/**
	 * 解析转换数字字符串为 {@link java.lang.Float } 规则如下：
	 *
	 * <pre>
	 * 1、0开头的忽略开头的0
	 * 2、空串返回0
	 * 3、其它情况按照10进制转换
	 * 4、.123形式返回0.123（按照小于0的小数对待）
	 * </pre>
	 *
	 * @param numberStr    数字字符串，支持0x开头、0开头和普通十进制
	 * @param defaultValue 如果解析失败, 将返回defaultValue, 允许null
	 * @return Float
	 */
	public static Float parseFloat(final String numberStr, final Float defaultValue) {
		if (StrUtil.isNotBlank(numberStr)) {
			try {
				return parseFloat(numberStr);
			} catch (final NumberFormatException ignore) {
				// ignore
			}
		}

		return defaultValue;
	}

	/**
	 * 解析转换数字字符串为long型数字，规则如下：
	 *
	 * <pre>
	 * 1、0开头的忽略开头的0
	 * 2、空串返回0
	 * 3、其它情况按照10进制转换
	 * 4、.123形式返回0.123（按照小于0的小数对待）
	 * </pre>
	 *
	 * @param number 数字，支持0x开头、0开头和普通十进制
	 * @return long
	 * @since 5.5.5
	 */
	public static float parseFloat(final String number) {
		if (StrUtil.isBlank(number)) {
			return 0f;
		}

		try {
			return Float.parseFloat(number);
		} catch (final NumberFormatException e) {
			return parseNumber(number).floatValue();
		}
	}

	/**
	 * 解析转换数字字符串为 {@link java.lang.Double } 规则如下：
	 *
	 * <pre>
	 * 1、0开头的忽略开头的0
	 * 2、空串返回0
	 * 3、其它情况按照10进制转换
	 * 4、.123形式返回0.123（按照小于0的小数对待）
	 * </pre>
	 *
	 * @param numberStr    数字字符串，支持0x开头、0开头和普通十进制
	 * @param defaultValue 如果解析失败, 将返回defaultValue, 允许null
	 * @return Double
	 */
	public static Double parseDouble(final String numberStr, final Double defaultValue) {
		if (StrUtil.isNotBlank(numberStr)) {
			try {
				return parseDouble(numberStr);
			} catch (final NumberFormatException ignore) {
				// ignore
			}
		}
		return defaultValue;
	}

	/**
	 * 解析转换数字字符串为long型数字，规则如下：
	 *
	 * <pre>
	 * 1、0开头的忽略开头的0
	 * 2、空串返回0
	 * 3、其它情况按照10进制转换
	 * 4、.123形式返回0.123（按照小于0的小数对待）
	 * </pre>
	 *
	 * @param number 数字，支持0x开头、0开头和普通十进制
	 * @return long
	 * @since 5.5.5
	 */
	public static double parseDouble(final String number) {
		if (StrUtil.isBlank(number)) {
			return 0D;
		}

		try {
			return Double.parseDouble(number);
		} catch (final NumberFormatException e) {
			return parseNumber(number).doubleValue();
		}
	}

	/**
	 * 将指定字符串转换为{@link Number }
	 * 此方法不支持科学计数法
	 *
	 * @param numberStr    Number字符串
	 * @param defaultValue 如果解析失败, 将返回defaultValue, 允许null
	 * @return Number对象
	 */
	public static Number parseNumber(final String numberStr, final Number defaultValue) {
		if (StrUtil.isNotBlank(numberStr)) {
			try {
				return parseNumber(numberStr);
			} catch (final NumberFormatException ignore) {
				// ignore
			}
		}
		return defaultValue;
	}

	/**
	 * 将指定字符串转换为{@link Number} 对象<br>
	 * 此方法不支持科学计数法
	 *
	 * <p>
	 *     需要注意的是，在不同Locale下，数字的表示形式也是不同的，例如：<br>
	 *     德国、荷兰、比利时、丹麦、意大利、罗马尼亚和欧洲大多地区使用`,`区分小数<br>
	 *     也就是说，在这些国家地区，1.20表示120，而非1.2。
	 * </p>
	 *
	 * @param numberStr Number字符串
	 * @return Number对象
	 * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
	 * @since 4.1.15
	 */
	public static Number parseNumber(final String numberStr) throws NumberFormatException {
		return parseNumber(numberStr, Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 将指定字符串转换为{@link Number} 对象<br>
	 * 此方法不支持科学计数法
	 *
	 * <p>
	 *     需要注意的是，在不同Locale下，数字的表示形式也是不同的，例如：<br>
	 *     德国、荷兰、比利时、丹麦、意大利、罗马尼亚和欧洲大多地区使用`,`区分小数<br>
	 *     也就是说，在这些国家地区，1.20表示120，而非1.2。
	 * </p>
	 *
	 * @param numberStr Number字符串
	 * @param locale 地区，不同地区数字表示方式不同
	 * @return Number对象
	 * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
	 */
	public static Number parseNumber(final String numberStr, final Locale locale) throws NumberFormatException {
		if (StrUtil.startWithIgnoreCase(numberStr, "0x")) {
			// 0x04表示16进制数
			return Long.parseLong(numberStr.substring(2), 16);
		}

		try {
			final NumberFormat format = NumberFormat.getInstance(locale);
			if (format instanceof DecimalFormat) {
				// issue#1818@Github
				// 当字符串数字超出double的长度时，会导致截断，此处使用BigDecimal接收
				((DecimalFormat) format).setParseBigDecimal(true);
			}
			return format.parse(numberStr);
		} catch (final ParseException e) {
			final NumberFormatException nfe = new NumberFormatException(e.getMessage());
			nfe.initCause(e);
			throw nfe;
		}
	}
	// endregion

	/**
	 * 检查是否为有效的数字<br>
	 * 检查Double和Float是否为无限大，或者Not a Number<br>
	 * 非数字类型和Null将返回true
	 *
	 * @param number 被检查类型
	 * @return 检查结果，非数字类型和Null将返回true
	 * @since 4.6.7
	 */
	public static boolean isValidNumber(final Number number) {
		if (null == number) {
			return false;
		}
		if (number instanceof Double) {
			return (false == ((Double) number).isInfinite()) && (false == ((Double) number).isNaN());
		} else if (number instanceof Float) {
			return (false == ((Float) number).isInfinite()) && (false == ((Float) number).isNaN());
		}
		return true;
	}

	/**
	 * 检查是否为有效的数字<br>
	 * 检查double否为无限大，或者Not a Number（NaN）<br>
	 *
	 * @param number 被检查double
	 * @return 检查结果
	 * @since 5.7.0
	 */
	public static boolean isValid(final double number) {
		return false == (Double.isNaN(number) || Double.isInfinite(number));
	}

	/**
	 * 检查是否为有效的数字<br>
	 * 检查double否为无限大，或者Not a Number（NaN）<br>
	 *
	 * @param number 被检查double
	 * @return 检查结果
	 * @since 5.7.0
	 */
	public static boolean isValid(final float number) {
		return false == (Float.isNaN(number) || Float.isInfinite(number));
	}

	/**
	 * 计算数学表达式的值，只支持加减乘除和取余<br>
	 * 如：
	 * <pre class="code">
	 *   calculate("(0*1--3)-5/-4-(3*(-2.13))") -》 10.64
	 * </pre>
	 *
	 * @param expression 数学表达式
	 * @return 结果
	 * @since 5.7.6
	 */
	public static double calculate(final String expression) {
		return Calculator.conversion(expression);
	}

	/**
	 * Number值转换为double<br>
	 * float强制转换存在精度问题，此方法避免精度丢失
	 *
	 * @param value 被转换的float值
	 * @return double值
	 * @since 5.7.8
	 */
	public static double toDouble(final Number value) {
		if (value instanceof Float) {
			return Double.parseDouble(value.toString());
		} else {
			return value.doubleValue();
		}
	}

	/**
	 * 检查是否为奇数<br>
	 *
	 * @param num 被判断的数值
	 * @return 是否是奇数
	 * @author GuoZG
	 * @since 5.7.17
	 */
	public static boolean isOdd(final int num) {
		return (num & 1) == 1;
	}

	/**
	 * 检查是否为偶数<br>
	 *
	 * @param num 被判断的数值
	 * @return 是否是偶数
	 * @author GuoZG
	 * @since 5.7.17
	 */
	public static boolean isEven(final int num) {
		return false == isOdd(num);
	}
}
