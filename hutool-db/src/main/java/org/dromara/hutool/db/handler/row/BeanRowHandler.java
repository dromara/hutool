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

package org.dromara.hutool.db.handler.row;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.handler.ResultSetUtil;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * 将{@link ResultSet}结果中的某行处理为Bean对象
 *
 * @param <T> Bean类型
 * @author looly
 */
public class BeanRowHandler<T> extends AbsRowHandler<T> {

	private final Class<T> beanType;
	private final boolean caseInsensitive;

	/**
	 * 构造
	 *
	 * @param meta            {@link ResultSetMetaData}
	 * @param beanType        Bean类型
	 * @param caseInsensitive 是否大小写不敏感
	 * @throws SQLException SQL异常
	 */
	public BeanRowHandler(final ResultSetMetaData meta, final Class<T> beanType, final boolean caseInsensitive) throws SQLException {
		super(meta);
		this.beanType = beanType;
		this.caseInsensitive = caseInsensitive;
	}


	@Override
	public T handle(final ResultSet rs) throws SQLException {
		//普通bean
		final T bean = ConstructorUtil.newInstanceIfPossible(beanType);
		final Map<String, PropDesc> propMap = BeanUtil.getBeanDesc(beanType).getPropMap(this.caseInsensitive);
		String columnLabel;
		PropDesc pd;
		Method setter;
		Object value;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			pd = propMap.get(columnLabel);
			if (null == pd) {
				// 尝试驼峰命名风格
				pd = propMap.get(StrUtil.toCamelCase(columnLabel));
			}
			setter = (null == pd) ? null : pd.getSetter();
			if (null != setter) {
				value = ResultSetUtil.getColumnValue(rs, i, meta.getColumnType(i), TypeUtil.getFirstParamType(setter));
				MethodUtil.invokeWithCheck(bean, setter, value);
			}
		}
		return bean;
	}
}
