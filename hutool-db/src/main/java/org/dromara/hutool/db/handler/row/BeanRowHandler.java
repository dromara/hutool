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

package org.dromara.hutool.db.handler.row;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.Invoker;
import org.dromara.hutool.core.reflect.method.MethodInvoker;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.handler.ResultSetUtil;

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
		Invoker setter;
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
				value = ResultSetUtil.getColumnValue(rs, i, meta.getColumnType(i), setter.getType());
				if(setter instanceof MethodInvoker){
					MethodInvoker.of(((MethodInvoker) setter).getMethod()).setCheckArgs(true).invoke(bean, value);
				}else {
					setter.invoke(bean, value);
				}
			}
		}
		return bean;
	}
}
