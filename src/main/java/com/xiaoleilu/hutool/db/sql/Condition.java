package com.xiaoleilu.hutool.db.sql;

import java.util.Arrays;
import java.util.List;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 条件对象<br>
 * @author Looly
 *
 */
public class Condition implements Cloneable{
	
	private static final List<String> OPERATORS = Arrays.asList("<>", "<=", "< ", ">=", "> ", "= ", "!=", "IN");
	
	private static final String OPERATOR_LIKE = "LIKE";
	
	/** 字段 */
	private String field;
	/** 运算符（大于号，小于号，等于号 like 等） */
	private String operator;
	/** 值 */
	private Object value;
	/** 是否使用条件值占位符 */
	private boolean isPlaceHolder = true;
	
	/**
	 * 解析为Condition
	 * @param field 字段名
	 * @param expression 表达式或普通值
	 * @return
	 */
	public static Condition parse(String field, Object expression){
		return new Condition(field, expression);
	}
	
	//--------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Condition() {
	}
	
	/**
	 * 构造
	 * @param isPlaceHolder 是否使用条件值占位符
	 */
	public Condition(boolean isPlaceHolder) {
		this.isPlaceHolder = isPlaceHolder;
	}
	
	/**
	 * 构造，使用等于表达式（运算符是=）
	 * @param field 字段
	 * @param value 值
	 */
	public Condition(String field, Object value) {
		this(field, "=", value);
		parseValue();
	}
	
	/**
	 * 构造
	 * @param field 字段
	 * @param operator 运算符（大于号，小于号，等于号 like 等）
	 * @param value 值
	 */
	public Condition(String field, String operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	//--------------------------------------------------------------- Constructor start
	
	//--------------------------------------------------------------- Getters and Setters start
	/**
	 * @return 字段
	 */
	public String getField() {
		return field;
	}
	/**
	 * 设置字段名
	 * @param field 字段名
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	/**
	 * 获得运算符<br>
	 * 大于号，小于号，等于号 等
	 * @return 运算符
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * 设置运算符<br>
	 * 大于号，小于号，等于号 等
	 * @param operator 运算符
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * 获得值
	 * @return 值
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * 设置值，不解析表达式
	 * @param value 值
	 */
	public void setValue(Object value) {
		setValue(value, false);
	}
	/**
	 * 设置值
	 * @param value 值
	 * @param isParse 是否解析值表达式
	 */
	public void setValue(Object value, boolean isParse) {
		this.value = value;
		if(isParse){
			parseValue();
		}
	}
	
	/**
	 * 是否使用条件占位符
	 * @return 是否使用条件占位符
	 */
	public boolean isPlaceHolder() {
		return isPlaceHolder;
	}
	/**
	 * 设置是否使用条件占位符
	 * @param isPlaceHolder 是否使用条件占位符
	 */
	public void setPlaceHolder(boolean isPlaceHolder) {
		this.isPlaceHolder = isPlaceHolder;
	}
	//--------------------------------------------------------------- Getters and Setters end
	
	@Override
	public Condition clone() {
		try {
			return (Condition) super.clone();
		} catch (CloneNotSupportedException e) {
			//不会发生
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
		//当值无时，视为空判定
		if(null == this.value){
			this.operator = "IS";
			this.value = "NULL";
			return;
		}
		
		//其他类型值，跳过
		if(false == (this.value instanceof String)){
			return;
		}
		
		String valueStr = ((String)value);
		if(StrUtil.isBlank(valueStr)){
			return;
		}
		
		valueStr = valueStr.trim();
		List<String> strs = StrUtil.split(valueStr, ' ', 2);
		if(strs.size() < 2){
			return;
		}
		
		//处理常用符号
		final String firstPart = strs.get(0).trim().toUpperCase();
		if(OPERATORS.contains(firstPart)){
			this.operator = firstPart;
			this.value = strs.get(1);
			return;
		}
		
		//处理LIKE
		if(valueStr.toUpperCase().startsWith(OPERATOR_LIKE)){
			this.operator = OPERATOR_LIKE;
			this.value = StrUtil.removePrefix(valueStr, OPERATOR_LIKE).trim();
		}
	}
}
