package cn.hutool.db.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Map;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.db.Entity;

/**
 * 数据结果集处理辅助类
 * 
 * @author loolly
 *
 */
public class HandleHelper {

	/**
	 * 处理单条数据
	 * 
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @param bean 目标Bean
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	public static <T> T handleRow(int columnCount, ResultSetMetaData meta, ResultSet rs, T bean) throws SQLException {
		return handleRow(columnCount, meta, rs).toBeanIgnoreCase(bean);
	}

	/**
	 * 处理单条数据
	 * 
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @param beanClass 目标Bean类型
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> T handleRow(int columnCount, ResultSetMetaData meta, ResultSet rs, Class<T> beanClass) throws SQLException {
		Assert.notNull(beanClass, "Bean Class must be not null !");
		
		if(beanClass.isArray()) {
			//返回数组
			final Class<?> componentType = beanClass.getComponentType();
			final Object[] result = ArrayUtil.newArray(componentType, columnCount);
			for(int i = 0,j = 1; i < columnCount; i++, j++) {
				result[i] = getColumnValue(rs, j, meta.getColumnType(j), componentType);
			}
			return (T) result;
		} else if(Iterable.class.isAssignableFrom(beanClass)) {
			//集合
			final Object[] objRow = handleRow(columnCount, meta, rs, Object[].class);
			return Convert.convert(beanClass, objRow);
		} else if(beanClass.isAssignableFrom(Entity.class)) {
			//Entity的父类都可按照Entity返回
			return (T) handleRow(columnCount, meta, rs);
		} else if(String.class == beanClass) {
			//字符串
			final Object[] objRow = handleRow(columnCount, meta, rs, Object[].class);
			return (T) StrUtil.join(", ", objRow);
		}
		
		//普通bean
		final T bean = ReflectUtil.newInstanceIfPossible(beanClass);
		//忽略字段大小写
		final Map<String, PropDesc> propMap = BeanUtil.getBeanDesc(beanClass).getPropMap(true);
		String columnLabel;
		PropDesc pd;
		Method setter = null;
		Object value = null;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			//驼峰命名风格
			pd = propMap.get(StrUtil.toCamelCase(columnLabel));
			setter = (null == pd) ? null : pd.getSetter();
			if(null != setter) {
				value = getColumnValue(rs, columnLabel,  meta.getColumnType(i), TypeUtil.getFirstParamType(setter));
				ReflectUtil.invokeWithCheck(bean, setter, new Object[] {value});
			}
		}
		return bean;
	}

	/**
	 * 处理单条数据
	 * 
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 */
	public static Entity handleRow(int columnCount, ResultSetMetaData meta, ResultSet rs) throws SQLException {
		return handleRow(Entity.create(), columnCount, meta, rs, false);
	}

	/**
	 * 处理单条数据
	 * 
	 * @param <T> Entity及其子对象
	 * @param row Entity对象
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @param withTableName 是否包含表名
	 * @return 每一行的Entity
	 * @throws SQLException SQL执行异常
	 * @since 3.3.1
	 */
	public static <T extends Entity> T handleRow(T row, int columnCount, ResultSetMetaData meta, ResultSet rs, boolean withTableName) throws SQLException {
		if (withTableName) {
			row.setTableName(meta.getTableName(1));
		}
		String columnLabel;
		int type;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			type = meta.getColumnType(i);
			row.put(columnLabel, getColumnValue(rs, columnLabel, type, null));
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
	public static Entity handleRow(ResultSet rs) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		return handleRow(columnCount, meta, rs);
	}

	/**
	 * 处理多条数据
	 * 
	 * @param <T> 集合类型
	 * @param rs 数据集
	 * @param collection 数据集
	 * @return Entity列表
	 * @throws SQLException SQL执行异常
	 */
	public static <T extends Collection<Entity>> T handleRs(ResultSet rs, T collection) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();

		while (rs.next()) {
			collection.add(HandleHelper.handleRow(columnCount, meta, rs));
		}

		return collection;
	}

	/**
	 * 处理多条数据并返回一个Bean列表
	 * 
	 * @param <E> 集合元素类型
	 * @param <T> 集合类型
	 * @param rs 数据集
	 * @param collection 数据集
	 * @param elementBeanType Bean类型
	 * @return Entity列表
	 * @throws SQLException SQL执行异常
	 * @since 3.1.0
	 */
	public static <E, T extends Collection<E>> T handleRsToBeanList(ResultSet rs, T collection, Class<E> elementBeanType) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();

		while (rs.next()) {
			collection.add(handleRow(columnCount, meta, rs, elementBeanType));
		}

		return collection;
	}

	// -------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 获取字段值<br>
	 * 针对日期时间等做单独处理判断
	 * 
	 * @param <T> 返回类型
	 * @param rs {@link ResultSet}
	 * @param label 字段标签或者字段名
	 * @param type 字段类型，默认Object
	 * @param targetColumnType 结果要求的类型，需进行二次转换（null或者Object不转换）
	 * @return 字段值
	 * @throws SQLException SQL异常
	 */
	private static <T> Object getColumnValue(ResultSet rs, String label, int type, Type targetColumnType) throws SQLException {
		Object rawValue;
		switch (type) {
		case Types.TIMESTAMP:
			rawValue = rs.getTimestamp(label);
			break;
		case Types.TIME:
			rawValue = rs.getTime(label);
			break;
		default:
			rawValue = rs.getObject(label);
		}
		if (null == targetColumnType || Object.class == targetColumnType) {
			// 无需转换
			return rawValue;
		} else {
			// 按照返回值要求转换
			return Convert.convert(targetColumnType, rawValue);
		}
	}

	/**
	 * 获取字段值<br>
	 * 针对日期时间等做单独处理判断
	 * 
	 * @param <T> 返回类型
	 * @param rs {@link ResultSet}
	 * @param columnIndex 字段索引
	 * @param type 字段类型，默认Object
	 * @param targetColumnType 结果要求的类型，需进行二次转换（null或者Object不转换）
	 * @return 字段值
	 * @throws SQLException SQL异常
	 */
	private static <T> Object getColumnValue(ResultSet rs, int columnIndex, int type, Type targetColumnType) throws SQLException {
		Object rawValue;
		switch (type) {
		case Types.TIMESTAMP:
			rawValue = rs.getTimestamp(columnIndex);
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
