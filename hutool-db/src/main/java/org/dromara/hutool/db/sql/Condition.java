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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exceptions.CloneRuntimeException;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.CharUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 条件对象<br>
 *
 * @author Looly
 */
public class Condition implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SQL中 LIKE 语句查询方式<br>
	 *
	 * @author Looly
	 */
	public enum LikeType {
		/**
		 * 以给定值开头，拼接后的SQL "value%"
		 */
		StartWith,
		/**
		 * 以给定值开头，拼接后的SQL "%value"
		 */
		EndWith,
		/**
		 * 包含给定值，拼接后的SQL "%value%"
		 */
		Contains
	}

	private static final String OPERATOR_LIKE = "LIKE";
	private static final String OPERATOR_IN = "IN";
	private static final String OPERATOR_IS = "IS";
	private static final String OPERATOR_IS_NOT = "IS NOT";
	private static final String OPERATOR_BETWEEN = "BETWEEN";
	private static final List<String> OPERATORS = Arrays.asList("<>", "<=", "<", ">=", ">", "=", "!=", OPERATOR_IN);

	private static final String VALUE_NULL = "NULL";

	/**
	 * 字段
	 */
	private String field;
	/**
	 * 运算符（大于号，小于号，等于号 like 等）
	 */
	private String operator;
	/**
	 * 值
	 */
	private Object value;
	/**
	 * 是否使用条件值占位符
	 */
	private boolean isPlaceHolder = true;
	/**
	 * between firstValue and secondValue
	 */
	private Object secondValue;

	/**
	 * 与前一个Condition连接的逻辑运算符，可以是and或or
	 */
	private LogicalOperator linkOperator = LogicalOperator.AND;

	/**
	 * 解析为Condition
	 *
	 * @param field      字段名
	 * @param expression 表达式或普通值
	 * @return Condition
	 */
	public static Condition parse(final String field, final Object expression) {
		return new Condition(field, expression);
	}

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public Condition() {
	}

	/**
	 * 构造
	 *
	 * @param isPlaceHolder 是否使用条件值占位符
	 */
	public Condition(final boolean isPlaceHolder) {
		this.isPlaceHolder = isPlaceHolder;
	}

	/**
	 * 构造，使用等于表达式（运算符是=）
	 *
	 * @param field 字段
	 * @param value 值
	 */
	public Condition(final String field, final Object value) {
		this(field, "=", value);
		parseValue();
	}

	/**
	 * 构造
	 *
	 * @param field    字段
	 * @param operator 运算符（大于号，小于号，等于号 like 等）
	 * @param value    值
	 */
	public Condition(final String field, final String operator, final Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * 构造
	 *
	 * @param field    字段
	 * @param value    值
	 * @param likeType {@link LikeType}
	 */
	public Condition(final String field, final String value, final LikeType likeType) {
		this.field = field;
		this.operator = OPERATOR_LIKE;
		this.value = SqlUtil.buildLikeValue(value, likeType, false);
	}
	// --------------------------------------------------------------- Constructor end

	// --------------------------------------------------------------- Getters and Setters start

	/**
	 * @return 字段
	 */
	public String getField() {
		return field;
	}

	/**
	 * 设置字段名
	 *
	 * @param field 字段名
	 */
	public void setField(final String field) {
		this.field = field;
	}

	/**
	 * 获得运算符<br>
	 * 大于号，小于号，等于号 等
	 *
	 * @return 运算符
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置运算符<br>
	 * 大于号，小于号，等于号 等
	 *
	 * @param operator 运算符
	 */
	public void setOperator(final String operator) {
		this.operator = operator;
	}

	/**
	 * 获得值
	 *
	 * @return 值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置值，不解析表达式
	 *
	 * @param value 值
	 */
	public void setValue(final Object value) {
		setValue(value, false);
	}

	/**
	 * 设置值
	 *
	 * @param value   值
	 * @param isParse 是否解析值表达式
	 */
	public void setValue(final Object value, final boolean isParse) {
		this.value = value;
		if (isParse) {
			parseValue();
		}
	}

	/**
	 * 是否使用条件占位符
	 *
	 * @return 是否使用条件占位符
	 */
	public boolean isPlaceHolder() {
		return isPlaceHolder;
	}

	/**
	 * 设置是否使用条件占位符
	 *
	 * @param isPlaceHolder 是否使用条件占位符
	 */
	public void setPlaceHolder(final boolean isPlaceHolder) {
		this.isPlaceHolder = isPlaceHolder;
	}

	/**
	 * 是否 between x and y 类型
	 *
	 * @return 是否 between x and y 类型
	 * @since 4.0.1
	 */
	public boolean isOperatorBetween() {
		return OPERATOR_BETWEEN.equalsIgnoreCase(this.operator);
	}

	/**
	 * 是否IN条件
	 *
	 * @return 是否IN条件
	 * @since 4.0.1
	 */
	public boolean isOperatorIn() {
		return OPERATOR_IN.equalsIgnoreCase(this.operator);
	}

	/**
	 * 是否IS条件
	 *
	 * @return 是否IS条件
	 * @since 4.0.1
	 */
	public boolean isOperatorIs() {
		return OPERATOR_IS.equalsIgnoreCase(this.operator);
	}

	/**
	 * 是否LIKE条件
	 *
	 * @return 是否LIKE条件
	 * @since 5.7.14
	 */
	public boolean isOperatorLike() {
		return OPERATOR_LIKE.equalsIgnoreCase(this.operator);
	}

	/**
	 * 检查值是否为null，如果为null转换为 "IS NULL"形式
	 *
	 * @return this
	 */
	public Condition checkValueNull() {
		if (null == this.value) {
			this.operator = OPERATOR_IS;
			this.value = VALUE_NULL;
		}
		return this;
	}

	/**
	 * 获得between 类型中第二个值
	 *
	 * @return 值
	 */
	public Object getSecondValue() {
		return secondValue;
	}

	/**
	 * 设置between 类型中第二个值
	 *
	 * @param secondValue 第二个值
	 */
	public void setSecondValue(final Object secondValue) {
		this.secondValue = secondValue;
	}

	/**
	 * 获取与前一个Condition连接的逻辑运算符，可以是and或or
	 *
	 * @return 与前一个Condition连接的逻辑运算符，可以是and或or
	 * @since 5.4.3
	 */
	public LogicalOperator getLinkOperator() {
		return linkOperator;
	}

	/**
	 * 设置与前一个Condition连接的逻辑运算符，可以是and或or
	 *
	 * @param linkOperator 与前一个Condition连接的逻辑运算符，可以是and或or
	 * @since 5.4.3
	 */
	public void setLinkOperator(final LogicalOperator linkOperator) {
		this.linkOperator = linkOperator;
	}

	// --------------------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return toString(null);
	}

	/**
	 * 转换为条件字符串，并回填占位符对应的参数值
	 *
	 * @param paramValues 参数列表，用于回填占位符对应参数值
	 * @return 条件字符串
	 */
	public String toString(final List<Object> paramValues) {
		final StringBuilder conditionStrBuilder = StrUtil.builder();
		// 判空值
		checkValueNull();

		// 固定前置，例如："name ="、"name IN"、"name BETWEEN"、"name LIKE"
		conditionStrBuilder.append(this.field).append(StrUtil.SPACE).append(this.operator);

		if (isOperatorBetween()) {
			buildValuePartForBETWEEN(conditionStrBuilder, paramValues);
		} else if (isOperatorIn()) {
			// 类似：" (?,?,?)" 或者 " (1,2,3,4)"
			buildValuePartForIN(conditionStrBuilder, paramValues);
		} else {
			if (isPlaceHolder() && false == isOperatorIs()) {
				// 使用条件表达式占位符，条件表达式并不适用于 IS NULL
				conditionStrBuilder.append(" ?");
				if (null != paramValues) {
					paramValues.add(this.value);
				}
			} else {
				// 直接使用条件值
				final String valueStr = String.valueOf(this.value);
				conditionStrBuilder.append(" ").append(isOperatorLike() ?
						StrUtil.wrap(valueStr, "'") : valueStr);
			}
		}

		return conditionStrBuilder.toString();
	}

	@Override
	public Condition clone() {
		try {
			return (Condition) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneRuntimeException(e);
		}
	}

	// ----------------------------------------------------------------------------------------------- Private method start

	/**
	 * 构建BETWEEN语句中的值部分<br>
	 * 开头必须加空格，类似：" ? AND ?" 或者 " 1 AND 2"
	 *
	 * @param conditionStrBuilder 条件语句构建器
	 * @param paramValues         参数集合，用于参数占位符对应参数回填
	 */
	private void buildValuePartForBETWEEN(final StringBuilder conditionStrBuilder, final List<Object> paramValues) {
		// BETWEEN x AND y 的情况，两个参数
		if (isPlaceHolder()) {
			// 使用条件表达式占位符
			conditionStrBuilder.append(" ?");
			if (null != paramValues) {
				paramValues.add(this.value);
			}
		} else {
			// 直接使用条件值
			conditionStrBuilder.append(CharUtil.SPACE).append(this.value);
		}

		// 处理 AND y
		conditionStrBuilder.append(StrUtil.SPACE).append(LogicalOperator.AND);
		if (isPlaceHolder()) {
			// 使用条件表达式占位符
			conditionStrBuilder.append(" ?");
			if (null != paramValues) {
				paramValues.add(this.secondValue);
			}
		} else {
			// 直接使用条件值
			conditionStrBuilder.append(CharUtil.SPACE).append(this.secondValue);
		}
	}

	/**
	 * 构建IN语句中的值部分<br>
	 * 开头必须加空格，类似：" (?,?,?)" 或者 " (1,2,3,4)"
	 *
	 * @param conditionStrBuilder 条件语句构建器
	 * @param paramValues         参数集合，用于参数占位符对应参数回填
	 */
	private void buildValuePartForIN(final StringBuilder conditionStrBuilder, final List<Object> paramValues) {
		conditionStrBuilder.append(" (");
		final Object value = this.value;
		if (isPlaceHolder()) {
			final Collection<?> valuesForIn;
			// 占位符对应值列表
			if (value instanceof Collection) {
				// pr#2046@Github
				valuesForIn = (Collection<?>) value;
			} else if (value instanceof CharSequence) {
				valuesForIn = SplitUtil.split((CharSequence) value, StrUtil.COMMA);
			} else {
				valuesForIn = Arrays.asList(Convert.convert(Object[].class, value));
			}
			conditionStrBuilder.append(StrUtil.repeatAndJoin("?", valuesForIn.size(), StrUtil.COMMA));
			if (null != paramValues) {
				paramValues.addAll(valuesForIn);
			}
		} else {
			conditionStrBuilder.append(StrUtil.join(",", value));
		}
		conditionStrBuilder.append(')');
	}

	/**
	 * 解析值表达式<br>
	 * 支持"<>", "<=", "< ", ">=", "> ", "= ", "!=", "IN", "LIKE", "IS", "IS NOT"表达式<br>
	 * 如果无法识别表达式，则表达式为"="，表达式与值用空格隔开<br>
	 * 例如字段为name，那value可以为："> 1"或者 "LIKE %Tom"此类
	 */
	private void parseValue() {
		// 当值无时，视为空判定
		if (null == this.value) {
			this.operator = OPERATOR_IS;
			this.value = VALUE_NULL;
			return;
		}

		// 对数组和集合值按照 IN 处理
		if (this.value instanceof Collection || ArrayUtil.isArray(this.value)) {
			this.operator = OPERATOR_IN;
			return;
		}

		// 其他类型值，跳过
		if (false == (this.value instanceof String)) {
			return;
		}

		String valueStr = ((String) value);
		if (StrUtil.isBlank(valueStr)) {
			// 空字段不做处理
			return;
		}

		valueStr = StrUtil.trim(valueStr);

		// 处理null
		if (StrUtil.endWithIgnoreCase(valueStr, "null")) {
			if (StrUtil.equalsIgnoreCase("= null", valueStr) || StrUtil.equalsIgnoreCase("is null", valueStr)) {
				// 处理"= null"和"is null"转换为"IS NULL"
				this.operator = OPERATOR_IS;
				this.value = VALUE_NULL;
				this.isPlaceHolder = false;
				return;
			} else if (StrUtil.equalsIgnoreCase("!= null", valueStr) || StrUtil.equalsIgnoreCase("is not null", valueStr)) {
				// 处理"!= null"和"is not null"转换为"IS NOT NULL"
				this.operator = OPERATOR_IS_NOT;
				this.value = VALUE_NULL;
				this.isPlaceHolder = false;
				return;
			}
		}

		final List<String> strs = SplitUtil.split(valueStr, StrUtil.SPACE, 2, true, false);
		if (strs.size() < 2) {
			return;
		}

		// 处理常用符号和IN
		final String firstPart = strs.get(0).toUpperCase();
		if (OPERATORS.contains(firstPart)) {
			this.operator = firstPart;
			// 比较符号后跟大部分为数字，此处做转换（IN不做转换）
			final String valuePart = strs.get(1);
			this.value = isOperatorIn() ? valuePart : tryToNumber(valuePart);
			return;
		}

		// 处理LIKE
		if (OPERATOR_LIKE.equals(firstPart)) {
			this.operator = OPERATOR_LIKE;
			this.value = unwrapQuote(strs.get(1));
			return;
		}

		// 处理BETWEEN x AND y
		if (OPERATOR_BETWEEN.equals(firstPart)) {
			final List<String> betweenValueStrs = SplitUtil.split(strs.get(1), LogicalOperator.AND.toString(),
					2, true, true, true);
			if (betweenValueStrs.size() < 2) {
				// 必须满足a AND b格式，不满足被当作普通值
				return;
			}

			this.operator = OPERATOR_BETWEEN;
			this.value = unwrapQuote(betweenValueStrs.get(0));
			this.secondValue = unwrapQuote(betweenValueStrs.get(1));
		}
	}

	/**
	 * 去掉包围在字符串两端的单引号或双引号
	 *
	 * @param value 值
	 * @return 去掉引号后的值
	 */
	private static String unwrapQuote(final String value) {
		if (null == value) {
			return null;
		}

		int from = 0;
		int to = value.length();
		final char startChar = value.charAt(0);
		final char endChar = value.charAt(value.length() - 1);
		if (startChar == endChar) {
			if ('\'' == startChar || '"' == startChar) {
				from = 1;
				to--;
			}
		}

		if (from == 0) {
			// 并不包含，返回原值
			return value;
		}
		return value.substring(from, to);
	}

	/**
	 * 尝试转换为数字，转换失败返回字符串
	 *
	 * @param value 被转换的字符串值
	 * @return 转换后的值
	 */
	private static Object tryToNumber(final String value) {
		if (false == NumberUtil.isNumber(value)) {
			return value;
		}
		try {
			return NumberUtil.parseNumber(value);
		} catch (final Exception ignore) {
			return value;
		}
	}
	// ----------------------------------------------------------------------------------------------- Private method end
}
