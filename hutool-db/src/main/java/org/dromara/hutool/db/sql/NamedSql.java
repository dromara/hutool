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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Collection;
import java.util.Map;

/**
 * 使用命名占位符的SQL，例如：select * from table where field1=:name1<br>
 * 支持的占位符格式为：
 * <pre>
 * 1、:name
 * 2、@name
 * 3、?name
 * </pre>
 *
 * @author looly
 * @since 4.0.10
 */
public class NamedSql extends BoundSql {

	private static final char[] NAME_START_CHARS = {':', '@', '?'};

	private final String namedSql;
	private final Map<String, Object> paramMap;

	/**
	 * 构造
	 *
	 * @param namedSql 命名占位符的SQL
	 * @param paramMap 名和参数的对应Map
	 */
	public NamedSql(final String namedSql, final Map<String, Object> paramMap) {
		this.namedSql = namedSql;
		this.paramMap = paramMap;
		parse(namedSql, paramMap);
	}

	/**
	 * 获取原始地带名称占位符的SQL语句
	 *
	 * @return 名称占位符的SQL
	 */
	public String getNamedSql() {
		return namedSql;
	}

	/**
	 * 获取原始参数名和参数值对应关系参数表
	 *
	 * @return 参数名和参数值对应关系参数表
	 */
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	/**
	 * 解析命名占位符的SQL
	 *
	 * @param namedSql 命名占位符的SQL
	 * @param paramMap 名和参数的对应Map
	 */
	private void parse(final String namedSql, final Map<String, Object> paramMap) {
		if (MapUtil.isEmpty(paramMap)) {
			setSql(namedSql);
			return;
		}

		final int len = namedSql.length();

		final StringBuilder name = new StringBuilder();
		final StringBuilder sqlBuilder = new StringBuilder();
		char c;
		Character nameStartChar = null;
		for (int i = 0; i < len; i++) {
			c = namedSql.charAt(i);
			if (ArrayUtil.contains(NAME_START_CHARS, c)) {
				// 新的变量开始符出现，要处理之前的变量
				replaceVar(nameStartChar, name, sqlBuilder, paramMap);
				nameStartChar = c;
			} else if (null != nameStartChar) {
				// 变量状态
				if (isGenerateChar(c)) {
					// 变量名
					name.append(c);
				} else {
					// 非标准字符也非变量开始的字符出现表示变量名结束，开始替换
					replaceVar(nameStartChar, name, sqlBuilder, paramMap);
					nameStartChar = null;
					sqlBuilder.append(c);
				}
			} else {
				// 变量以外的字符原样输出
				sqlBuilder.append(c);
			}
		}

		// 收尾，如果SQL末尾存在变量，处理之
		if (name.length() > 0) {
			replaceVar(nameStartChar, name, sqlBuilder, paramMap);
		}

		setSql(sqlBuilder.toString());
	}

	/**
	 * 替换变量，如果无变量，原样输出到SQL中去
	 *
	 * @param nameStartChar 变量开始字符
	 * @param name          变量名
	 * @param sqlBuilder    结果SQL缓存
	 * @param paramMap      变量map（非空）
	 */
	private void replaceVar(final Character nameStartChar, final StringBuilder name, final StringBuilder sqlBuilder, final Map<String, Object> paramMap) {
		if (name.length() == 0) {
			if (null != nameStartChar) {
				// 类似于:的情况，需要补上:
				sqlBuilder.append(nameStartChar);
			}
			// 无变量，按照普通字符处理
			return;
		}

		// 变量结束
		final String nameStr = name.toString();
		if (paramMap.containsKey(nameStr)) {
			// 有变量对应值（值可以为null），替换占位符为?，变量值放入相应index位置
			Object paramValue = paramMap.get(nameStr);
			if ((paramValue instanceof Collection || ArrayUtil.isArray(paramValue)) && StrUtil.containsIgnoreCase(sqlBuilder, "in")) {
				if (paramValue instanceof Collection) {
					// 转为数组
					paramValue = ((Collection<?>) paramValue).toArray();
				}

				// 可能为select in (xxx)语句，则拆分参数为多个参数，变成in (?,?,?)
				final int length = ArrayUtil.length(paramValue);
				for (int i = 0; i < length; i++) {
					if (0 != i) {
						sqlBuilder.append(',');
					}
					sqlBuilder.append('?');
					addParam(ArrayUtil.get(paramValue, i));
				}
			} else {
				sqlBuilder.append('?');
				addParam(paramValue);
			}
		} else {
			// 无变量对应值，原样输出
			sqlBuilder.append(nameStartChar).append(name);
		}

		//清空变量，表示此变量处理结束
		name.setLength(0);
	}

	/**
	 * 是否为标准的字符，包括大小写字母、下划线和数字
	 *
	 * @param c 字符
	 * @return 是否标准字符
	 */
	private static boolean isGenerateChar(final char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || (c >= '0' && c <= '9');
	}
}
