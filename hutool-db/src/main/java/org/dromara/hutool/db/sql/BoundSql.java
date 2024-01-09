/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import java.util.ArrayList;
import java.util.List;

/**
 * 参数绑定的SQL封装，用于表示SQL语句模板（'?'表示参数占位符）和参数值的封装<br>
 * SQL中的'?'占位符必须和params列表中的参数值一一对应
 *
 * @author Looly
 */
public class BoundSql {

	private String sql;
	private List<Object> params;

	/**
	 * 构造
	 */
	public BoundSql() {}

	/**
	 * 构造
	 *
	 * @param sql    SQL语句，参数占位符使用'?'表示
	 * @param params 参数列表，每个参数对应一个'?'
	 */
	public BoundSql(final String sql, final List<Object> params) {
		this.sql = sql;
		this.params = params;
	}

	/**
	 * 获取SQL
	 *
	 * @return SQL
	 */
	public String getSql() {
		return this.sql;
	}

	/**
	 * 设置SQL语句
	 *
	 * @param sql SQL语句
	 * @return this
	 */
	public BoundSql setSql(final String sql) {
		this.sql = sql;
		return this;
	}

	/**
	 * 获取参数列表，按照占位符顺序
	 *
	 * @return 参数列表
	 */
	public List<Object> getParams() {
		return this.params;
	}

	/**
	 * 获取参数列表，按照占位符顺序
	 *
	 * @return 参数数组
	 */
	public Object[] getParamArray() {
		return this.params.toArray(new Object[0]);
	}

	/**
	 * 设置参数列表
	 *
	 * @param params 参数列表
	 * @return this
	 */
	public BoundSql setParams(final List<Object> params) {
		this.params = params;
		return this;
	}

	/**
	 * 增加参数
	 *
	 * @param paramValue 参数值
	 * @return this
	 */
	public BoundSql addParam(final Object paramValue){
		if(null == this.params){
			this.params = new ArrayList<>();
		}
		this.params.add(paramValue);
		return this;
	}
}
