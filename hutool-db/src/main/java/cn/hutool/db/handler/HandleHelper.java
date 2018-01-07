package cn.hutool.db.handler;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.convert.Convert;
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
		if(Map.class.isAssignableFrom(Entity.class)) {
			return (T) handleRow(columnCount, meta, rs);
		}
		
		final T bean = ReflectUtil.newInstanceIfPossible(beanClass);
		//忽略字段大小写
		final Map<String, PropDesc> propMap = BeanUtil.getBeanDesc(beanClass).getPropMap(true);
		String columnLabel;
		int type;
		PropDesc pd;
		Method setter = null;
		Object value = null;
		for (int i = 1; i <= columnCount; i++) {
			//驼峰命名风格
			columnLabel = StrUtil.toCamelCase(meta.getColumnLabel(i));
			type = meta.getColumnType(i);
			pd = propMap.get(columnLabel);
			if(null != pd) {
				setter = pd.getSetter();
			}
			if(null != setter) {
				value = getColumnValue(rs, columnLabel, type);
				value = Convert.convert(TypeUtil.getFirstParamType(setter), value);
				ReflectUtil.invoke(bean, setter, new Object[] {value});
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
		if(withTableName) {
			row.setTableName(meta.getTableName(1));
		}
		String columnLabel;
		int type;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			type = meta.getColumnType(i);
			row.put(columnLabel, getColumnValue(rs, columnLabel, type));
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
	 * @param rs {@link ResultSet}
	 * @param label 字段名
	 * @param type 字段类型，默认Object
	 * @return 字段值
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	private static Object getColumnValue(ResultSet rs, String label, int type) throws SQLException {
		switch (type) {
		case Types.TIMESTAMP:
			return rs.getTimestamp(label);
		case Types.TIME:
			return rs.getTime(label);
		default:
			return rs.getObject(label);
		}
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
