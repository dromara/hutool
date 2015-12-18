package com.xiaoleilu.hutool.db.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.Direction;

/**
 * SQL排序对象
 * @author Looly
 *
 */
public class Order {
	/** 排序的字段 */
	private Collection<String> orderFields;
	/** 排序方式（正序还是反序） */
	private Direction direction;
	
	//---------------------------------------------------------- Constructor start
	public Order() {
	}
	
	/**
	 * 构造
	 * @param orderFields 排序字段
	 */
	public Order(Collection<String> orderFields) {
		this.orderFields = orderFields;
	}
	
	/**
	 * 构造
	 * @param orderField 排序字段
	 */
	public Order(String orderField) {
		if(null == this.orderFields){
			this.orderFields = new ArrayList<String>();
		}
		this.orderFields.add(orderField);
	}
	
	/**
	 * 构造
	 * @param orderFields 排序字段
	 * @param direction 排序方式
	 */
	public Order(Collection<String> orderFields, Direction direction) {
		this.orderFields = orderFields;
		this.direction = direction;
	}
	
	/**
	 * 构造
	 * @param orderFields 排序字段
	 * @param direction 排序方式
	 */
	public Order(Direction direction, String... orderFields) {
		this.orderFields = Arrays.asList(orderFields);
		this.direction = direction;
	}
	//---------------------------------------------------------- Constructor end

	//---------------------------------------------------------- Getters and Setters start
	/**
	 * @return 排序字段
	 */
	public Collection<String> getOrderFields() {
		return orderFields;
	}
	/**
	 * 设置排序字段
	 * @param orderFields 排序字段
	 */
	public void setOrderFields(Collection<String> orderFields) {
		this.orderFields = orderFields;
	}

	/**
	 * @return 排序方向
	 */
	public Direction getDirection() {
		return direction;
	}
	/**
	 * 设置排序方向
	 * @param direction 排序方向
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	//---------------------------------------------------------- Getters and Setters end
	
	//---------------------------------------------------------- Function start
	/**
	 * 增加排序字段
	 * @param filelds 排序字段
	 * @return 自身
	 */
	public Order addOrderFields(String... filelds){
		if(null == this.orderFields){
			this.orderFields = new ArrayList<String>();
		}
		for (String field : filelds) {
			if(StrUtil.isNotBlank(field)){
				this.orderFields.add(field);
			}
		}
		return this;
	}
	//---------------------------------------------------------- Function end
	
	@Override
	public String toString() {
		return StrUtil.builder().append(" ORDER BY ").append(CollectionUtil.join(orderFields, StrUtil.COMMA)).append(StrUtil.SPACE)
		.append(null == direction ? StrUtil.EMPTY : direction).toString();
	}
}
