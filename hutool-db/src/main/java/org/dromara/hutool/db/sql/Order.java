/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.text.StrUtil;

import java.io.Serializable;

/**
 * SQL排序对象
 * @author Looly
 *
 */
public class Order implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 排序的字段 */
	private String field;
	/** 排序方式（正序还是反序） */
	private Direction direction;

	//---------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public Order() {
	}

	/**
	 * 构造
	 * @param field 排序字段
	 */
	public Order(final String field) {
		this.field = field;
	}

	/**
	 * 构造
	 * @param field 排序字段
	 * @param direction 排序方式
	 */
	public Order(final String field, final Direction direction) {
		this(field);
		this.direction = direction;
	}

	//---------------------------------------------------------- Constructor end

	//---------------------------------------------------------- Getters and Setters start
	/**
	 * @return 排序字段
	 */
	public String getField() {
		return this.field;
	}
	/**
	 * 设置排序字段
	 * @param field 排序字段
	 */
	public void setField(final String field) {
		this.field = field;
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
	public void setDirection(final Direction direction) {
		this.direction = direction;
	}
	//---------------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return StrUtil.builder().append(this.field).append(StrUtil.SPACE).append(null == direction ? StrUtil.EMPTY : direction).toString();
	}
}
