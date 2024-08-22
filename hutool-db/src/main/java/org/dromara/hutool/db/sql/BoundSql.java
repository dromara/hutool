/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
