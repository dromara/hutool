package cn.hutool.db.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 条件对象<br>
 * 
 * @author Looly
 *
 */
public class Condition implements Cloneable {

	/**
	 * SQL中 WHERE 语句查询方式<br>
	 * 
	 * @author Looly
	 *
	 */
	public static enum LikeType {
		/** 以给定值开头，拼接后的SQL "value%" */
		StartWith,
		/** 以给定值开头，拼接后的SQL "%value" */
		EndWith,
		/** 包含给定值，拼接后的SQL "%value%" */
		Contains
	}

	private static final String OPERATOR_LIKE = "LIKE";
	private static final String OPERATOR_IN = "IN";
	private static final String OPERATOR_IS = "IS";
	private static final String OPERATOR_BETWEEN = "BETWEEN";
	private static final List<String> OPERATORS = Arrays.asList("<>", "<=", "<", ">=", ">", "=", "!=", OPERATOR_IN);

	private static final String VALUE_NULL = "NULL";

	/** 字段 */
	private String field;
	/** 运算符（大于号，小于号，等于号 like 等） */
	private String operator;
	/** 值 */
	private Object value;
	/** 是否使用条件值占位符 */
	private boolean isPlaceHolder = true;
	/** between firstValue and secondValue */
	private Object secondValue;

	/**
	 * 解析为Condition
	 * 
	 * @param field 字段名
	 * @param expression 表达式或普通值
	 * @return Condition
	 */
	public static Condition parse(String field, Object expression) {
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
	public Condition(boolean isPlaceHolder) {
		this.isPlaceHolder = isPlaceHolder;
	}

	/**
	 * 构造，使用等于表达式（运算符是=）
	 * 
	 * @param field 字段
	 * @param value 值
	 */
	public Condition(String field, Object value) {
		this(field, "=", value);
		parseValue();
	}

	/**
	 * 构造
	 * 
	 * @param field 字段
	 * @param operator 运算符（大于号，小于号，等于号 like 等）
	 * @param value 值
	 */
	public Condition(String field, String operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * 构造
	 * 
	 * @param field 字段
	 * @param value 值
	 * @param likeType {@link LikeType}
	 */
	public Condition(String field, String value, LikeType likeType) {
		this.field = field;
		this.operator = OPERATOR_LIKE;
		this.value = SqlUtil.buildLikeValue(value, likeType, false);
	}
	// --------------------------------------------------------------- Constructor start

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
	public void setField(String field) {
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
	public void setOperator(String operator) {
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
	public void setValue(Object value) {
		setValue(value, false);
	}

	/**
	 * 设置值
	 * 
	 * @param value 值
	 * @param isParse 是否解析值表达式
	 */
	public void setValue(Object value, boolean isParse) {
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
	public void setPlaceHolder(boolean isPlaceHolder) {
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
	public void setSecondValue(Object secondValue) {
		this.secondValue = secondValue;
	}

	// --------------------------------------------------------------- Getters and Setters end

	@Override
	public Condition clone() {
		try {
			return (Condition) super.clone();
		} catch (CloneNotSupportedException e) {
			// 不会发生
			return null;
		}
	}

	@Override
	public String toString() {
		return StrUtil.format("`{}` {} {}", this.field, this.operator, this.value);
	}

	/**
	 * 解析值表达式<br>
	 * 支持"<>", "<=", "< ", ">=", "> ", "= ", "!=", "IN","LIKE"表达式<br>
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

		valueStr = valueStr.trim();

		// 处理"= null"和"is null"转换为"IS NULL"
		if (StrUtil.equalsIgnoreCase("= null", valueStr) || StrUtil.equalsIgnoreCase("is null", valueStr)) {
			this.operator = OPERATOR_IS;
			this.value = VALUE_NULL;
			return;
		}

		List<String> strs = StrUtil.split(valueStr, StrUtil.C_SPACE, 2);
		if (strs.size() < 2) {
			return;
		}

		// 处理常用符号和IN
		final String firstPart = strs.get(0).trim().toUpperCase();
		if (OPERATORS.contains(firstPart)) {
			this.operator = firstPart;
			this.value = strs.get(1).trim();
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
			final List<String> betweenValueStrs = StrSpliter.splitTrimIgnoreCase(strs.get(1), LogicalOperator.AND.toString(), 2, true);
			if (betweenValueStrs.size() < 2) {
				// 必须满足a AND b格式，不满足被当作普通值
				return;
			}

			this.operator = OPERATOR_BETWEEN;
			this.value = unwrapQuote(betweenValueStrs.get(0));
			this.secondValue = unwrapQuote(betweenValueStrs.get(1));
			return;
		}
	}

	// ----------------------------------------------------------------------------------------------- Private method start
	/**
	 * 去掉包围在字符串两端的单引号或双引号
	 * 
	 * @param value 值
	 * @return 去掉引号后的值
	 */
	private static String unwrapQuote(String value) {
		if (null == value) {
			return null;
		}
		value = value.trim();

		int from = 0;
		int to = value.length();
		char startChar = value.charAt(0);
		char endChar = value.charAt(to - 1);
		if (startChar == endChar) {
			if ('\'' == startChar || '"' == startChar) {
				from = 1;
				to = to - 1;
			}
		}

		if (from == 0 && to == value.length()) {
			// 并不包含，返回原值
			return value;
		}
		return value.substring(from, to);
	}
	// ----------------------------------------------------------------------------------------------- Private method end
}
