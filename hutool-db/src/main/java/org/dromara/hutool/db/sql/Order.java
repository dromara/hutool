/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 *
	 * @param direction 排序方向
	 * @return this
	 */
	public Order setDirection(final Direction direction) {
		this.direction = direction;
		return this;
	}
	//---------------------------------------------------------- Getters and Setters end

	@Override
	public String toString() {
		return StrUtil.builder().append(this.field).append(StrUtil.SPACE).append(null == direction ? StrUtil.EMPTY : direction).toString();
	}
}
