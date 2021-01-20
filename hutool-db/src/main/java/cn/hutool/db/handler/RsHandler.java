package cn.hutool.db.handler;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理接口<br>
 * 此接口用于实现{@link ResultSet} 转换或映射为用户指定的pojo对象
 * 
 * 默认实现有：
 * @see EntityHandler
 * @see EntityListHandler
 * @see EntitySetHandler
 * @see EntitySetHandler
 * @see NumberHandler
 * @see PageResultHandler
 * 
 * @author Luxiaolei
 *
 */
@FunctionalInterface
public interface RsHandler<T> extends Serializable{
	
	/**
	 * 处理结果集<br>
	 * 结果集处理后不需要关闭
	 * @param rs 结果集
	 * @return 处理后生成的对象
	 * @throws SQLException SQL异常
	 */
	T handle(ResultSet rs) throws SQLException;
}
