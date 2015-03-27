package com.xiaoleilu.hutool.db;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 条件对象<br>
 * @author Looly
 *
 */
public class Condition {
	
	/** 字段 */
	private String field;
	/** 运算符（大于号，小于号，等于号 like 等） */
	private String operator;
	/** 值 */
	private Object value;
	
	//--------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Condition() {
	}
	
	/**
	 * 构造，使用等于表达式（运算符是=）
	 * @param field 字段
	 * @param value 值
	 */
	public Condition(String field, Object value) {
		super();
		this.field = field;
		this.operator = "=";
		this.value = value;
	}
	
	/**
	 * 构造
	 * @param field 字段
	 * @param operator 运算符（大于号，小于号，等于号 like 等）
	 * @param value 值
	 */
	public Condition(String field, String operator, Object value) {
		super();
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
	 * 设置值
	 * @param value 值
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	//--------------------------------------------------------------- Getters and Setters end
	
	@Override
	public String toString() {
		return StrUtil.format("`{}` {} {}", this.field, this.operator, this.value);
	}
}
