package cn.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Bean对象处理器，只处理第一条数据
 * 
 * @param <E> 处理对象类型
 * @author loolly
 *@since 3.1.0
 */
public class BeanHandler<E> implements RsHandler<E>{
	private static final long serialVersionUID = -5491214744966544475L;

	private final Class<E> elementBeanType;
	
	/**
	 * 创建一个 BeanHandler对象
	 * 
	 * @param <E> 处理对象类型
	 * @param beanType Bean类型
	 * @return BeanHandler对象
	 */
	public static <E> BeanHandler<E> create(Class<E> beanType) {
		return new BeanHandler<>(beanType);
	}

	public BeanHandler(Class<E> beanType) {
		this.elementBeanType = beanType;
	}

	@Override
	public E handle(ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		return rs.next() ? HandleHelper.handleRow(columnCount, meta, rs, this.elementBeanType) : null;
	}
}
