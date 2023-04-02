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

package org.dromara.hutool.handler;

import org.dromara.hutool.bean.BeanUtil;
import org.dromara.hutool.bean.PropDesc;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.reflect.ConstructorUtil;
import org.dromara.hutool.reflect.MethodUtil;
import org.dromara.hutool.reflect.TypeUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.DbRuntimeException;
import org.dromara.hutool.Entity;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据结果集处理辅助类
 *
 * @author looly
 */
public class ResultSetUtil {

	/**
	 * 处理单条数据
	 *
	 * @param <T>         Bean类型
	 * @param columnCount 列数
	 * @param meta        ResultSetMetaData
	 * @param rs          数据集
	 * @param bean        目标Bean
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	public static <T> T toBean(final int columnCount, final ResultSetMetaData meta, final ResultSet rs, final T bean) throws SQLException {
		return toEntity(columnCount, meta, rs).toBeanIgnoreCase(bean);
	}

	/**
	 * 处理单条数据
	 *
	 * @param <T>         Bean类型
	 * @param columnCount 列数
	 * @param meta        ResultSetMetaData
	 * @param rs          数据集
	 * @param beanClass   目标Bean类型
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(final int columnCount, final ResultSetMetaData meta, final ResultSet rs, final Class<T> beanClass) throws SQLException {
		Assert.notNull(beanClass, "Bean Class must be not null !");

		if (beanClass.isArray()) {
			//返回数组
			final Class<?> componentType = beanClass.getComponentType();
			final Object[] result = ArrayUtil.newArray(componentType, columnCount);
			for (int i = 0, j = 1; i < columnCount; i++, j++) {
				result[i] = getColumnValue(rs, j, meta.getColumnType(j), componentType);
			}
			return (T) result;
		} else if (Iterable.class.isAssignableFrom(beanClass)) {
			//集合
			final Object[] objRow = toBean(columnCount, meta, rs, Object[].class);
			return Convert.convert(beanClass, objRow);
		} else if (beanClass.isAssignableFrom(Entity.class)) {
			//Entity的父类都可按照Entity返回
			return (T) toEntity(columnCount, meta, rs);
		} else if (String.class == beanClass) {
			//字符串
			final Object[] objRow = toBean(columnCount, meta, rs, Object[].class);
			return (T) StrUtil.join(", ", objRow);
		}

		//普通bean
		final T bean = ConstructorUtil.newInstanceIfPossible(beanClass);
		//忽略字段大小写
		final Map<String, PropDesc> propMap = BeanUtil.getBeanDesc(beanClass).getPropMap(true);
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
				value = getColumnValue(rs, i, meta.getColumnType(i), TypeUtil.getFirstParamType(setter));
				MethodUtil.invokeWithCheck(bean, setter, value);
			}
		}
		return bean;
	}

	/**
	 * 处理单条数据
	 *
	 * @param columnCount 列数
	 * @param meta        ResultSetMetaData
	 * @param rs          数据集
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 */
	public static Entity toEntity(final int columnCount, final ResultSetMetaData meta, final ResultSet rs) throws SQLException {
		return toEntity(columnCount, meta, rs, false);
	}

	/**
	 * 处理单条数据
	 *
	 * @param columnCount     列数
	 * @param meta            ResultSetMetaData
	 * @param rs              数据集
	 * @param caseInsensitive 是否大小写不敏感
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 4.5.16
	 */
	public static Entity toEntity(final int columnCount, final ResultSetMetaData meta, final ResultSet rs, final boolean caseInsensitive) throws SQLException {
		return toEntity(new Entity(null, caseInsensitive), columnCount, meta, rs, true);
	}

	/**
	 * 处理单条数据
	 *
	 * @param <T>          Entity及其子对象
	 * @param row          Entity对象
	 * @param columnCount  列数
	 * @param meta         ResultSetMetaData
	 * @param rs           数据集
	 * @param withMetaInfo 是否包含表名、字段名等元信息
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	public static <T extends Entity> T toEntity(final T row, final int columnCount, final ResultSetMetaData meta, final ResultSet rs, final boolean withMetaInfo) throws SQLException {
		int type;
		String columnLabel;
		for (int i = 1; i <= columnCount; i++) {
			type = meta.getColumnType(i);
			columnLabel = meta.getColumnLabel(i);
			if("rownum_".equalsIgnoreCase(columnLabel)){
				// issue#2618@Github
				// 分页时会查出rownum字段，此处忽略掉读取
				continue;
			}
			row.put(columnLabel, getColumnValue(rs, i, type, null));
		}
		if (withMetaInfo) {
			try {
				row.setTableName(meta.getTableName(1));
			} catch (final SQLException ignore) {
				//issue#I2AGLU@Gitee
				// Hive等NoSQL中无表的概念，此处报错，跳过。
			}
			row.setFieldNames(row.keySet());
		}
		return row;
	}

	/**
	 * 处理单条数据
	 *
	 * @param rs 数据集
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 */
	public static Entity toEntity(final ResultSet rs) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		return toEntity(columnCount, meta, rs);
	}

	/**
	 * 处理单行数据
	 *
	 * @param rs 数据集（行）
	 * @return 每一行的List
	 * @throws SQLException SQL执行异常
	 * @since 5.1.6
	 */
	public static List<Object> handleRowToList(final ResultSet rs) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		final List<Object> row = new ArrayList<>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			row.add(getColumnValue(rs, i, meta.getColumnType(i), null));
		}

		return row;
	}

	/**
	 * 处理多条数据
	 *
	 * @param <T>        集合类型
	 * @param rs         数据集
	 * @param collection 数据集
	 * @return Entity列表
	 * @throws SQLException SQL执行异常
	 */
	public static <T extends Collection<Entity>> T toEntityList(final ResultSet rs, final T collection) throws SQLException {
		return toEntityList(rs, collection, false);
	}

	/**
	 * 处理多条数据
	 *
	 * @param <T>             集合类型
	 * @param rs              数据集
	 * @param collection      数据集
	 * @param caseInsensitive 是否大小写不敏感
	 * @return Entity列表
	 * @throws SQLException SQL执行异常
	 * @since 4.5.16
	 */
	public static <T extends Collection<Entity>> T toEntityList(final ResultSet rs, final T collection, final boolean caseInsensitive) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();

		while (rs.next()) {
			collection.add(toEntity(columnCount, meta, rs, caseInsensitive));
		}

		return collection;
	}

	/**
	 * 处理多条数据并返回一个Bean列表
	 *
	 * @param <E>             集合元素类型
	 * @param <T>             集合类型
	 * @param rs              数据集
	 * @param collection      数据集
	 * @param elementBeanType Bean类型
	 * @return Entity列表
	 * @throws SQLException SQL执行异常
	 * @since 3.1.0
	 */
	public static <E, T extends Collection<E>> T toBeanList(final ResultSet rs, final T collection, final Class<E> elementBeanType) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();

		while (rs.next()) {
			collection.add(toBean(columnCount, meta, rs, elementBeanType));
		}

		return collection;
	}

	/**
	 * 结果集读取为一个Long值，一版用于插入时返回主键等<br>
	 * 当有多个值返回时，只取第一个
	 *
	 * @param rs 数据集
	 * @return long值
	 * @throws SQLException SQL执行异常
	 */
	public static Long toLong(final ResultSet rs) throws SQLException {
		Long generatedKey = null;
		if (rs != null && rs.next()) {
			try {
				generatedKey = rs.getLong(1);
			} catch (final DbRuntimeException e) {
				// 自增主键不为数字或者为Oracle的rowid，跳过
			}
		}
		return generatedKey;
	}

	// -------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 获取字段值<br>
	 * 针对日期时间等做单独处理判断
	 *
	 * @param rs               {@link ResultSet}
	 * @param columnIndex      字段索引
	 * @param type             字段类型，默认Object
	 * @param targetColumnType 结果要求的类型，需进行二次转换（null或者Object不转换）
	 * @return 字段值
	 * @throws SQLException SQL异常
	 */
	private static Object getColumnValue(final ResultSet rs, final int columnIndex, final int type, final Type targetColumnType) throws SQLException {
		Object rawValue = null;
		switch (type) {
			case Types.TIMESTAMP:
				try {
					rawValue = rs.getTimestamp(columnIndex);
				} catch (final SQLException ignore) {
					// issue#776@Github
					// 当数据库中日期为0000-00-00 00:00:00报错，转为null
				}
				break;
			case Types.TIME:
				rawValue = rs.getTime(columnIndex);
				break;
			default:
				rawValue = rs.getObject(columnIndex);
		}
		if (null == targetColumnType || Object.class == targetColumnType) {
			// 无需转换
			return rawValue;
		} else {
			// 按照返回值要求转换
			return Convert.convert(targetColumnType, rawValue);
		}
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
