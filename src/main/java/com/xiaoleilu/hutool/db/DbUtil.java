package com.xiaoleilu.hutool.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.db.meta.Column;
import com.xiaoleilu.hutool.db.meta.Table;
import com.xiaoleilu.hutool.db.sql.Condition;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数据库操作工具类
 * 
 * @author Luxiaolei
 * 
 */
public class DbUtil {
	private final static Log log = StaticLog.get();

	private DbUtil() {
		// 非可实例化类
	}
	
	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param ds 数据源
	 * @return SQL执行类
	 */
	public static SqlRunner newSqlRunner(DataSource ds) {
		return new SqlRunner(ds);
	}
	
	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param ds 数据源
	 * @param dialect SQL方言
	 * @return SQL执行类
	 */
	public static SqlRunner newSqlRunner(DataSource ds, Dialect dialect) {
		return new SqlRunner(ds, dialect);
	}
	
	/**
	 * 连续关闭一系列的SQL相关对象<br/>
	 * 这些对象必须按照顺序关闭，否则会出错。
	 * 
	 * @param objsToClose 需要关闭的对象
	 */
	public static void close(Object... objsToClose) {
		for (Object obj : objsToClose) {
			try {
				if (obj != null) {
					if (obj instanceof ResultSet) {
						((ResultSet) obj).close();
					} else if (obj instanceof Statement) {
						((Statement) obj).close();
					} else if (obj instanceof PreparedStatement) {
						((PreparedStatement) obj).close();
					} else if (obj instanceof Connection) {
						((Connection) obj).close();
					} else {
						log.warn("Object " + obj.getClass().getName() + " not a ResultSet or Statement or PreparedStatement or Connection!");
					}
				}
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * 获得JNDI数据源
	 * @param jndiName JNDI名称
	 * @return 数据源
	 */
	public static DataSource getJndiDs(String jndiName) {
		try {
			return (DataSource) new InitialContext().lookup(jndiName);
		} catch (NamingException e) {
			log.error("Find JNDI datasource error!", e);
		}
		return null;
	}
	
	/**
	 * 获得所有表名
	 */
	public static List<String> getTables(DataSource ds) {
		final List<String> tables = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLES"});
			if(rs == null) {
				return null;
			}
			while(rs.next()) {
				final String table = rs.getString("TABLE_NAME");
				if(StrUtil.isBlank(table) == false) {
					tables.add(table);
				}
			}
		} catch (Exception e) {
			throw new UtilException("Get tables error!", e);
		}finally {
			close(rs, conn);
		}
		return tables;
	}
	
	/**
	 * 获得结果集的所有列名
	 * @param rs 结果集
	 * @return 列名数组
	 */
	public static String[] getColumnNames(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] labelNames = new String[columnCount];
			for (int i=0; i<labelNames.length; i++) {
				labelNames[i] = rsmd.getColumnLabel(i +1);
			}
			return labelNames;
		} catch (Exception e) {
			throw new UtilException("Get colunms error!", e);
		}
	}
	
	/**
	 * 获得表的所有列名
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws SQLException
	 */
	public static String[] getColumnNames(DataSource ds, String tableName) {
		List<String> columnNames = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while(rs.next()) {
				columnNames.add(rs.getString("COLUMN_NAME"));
			}
			return columnNames.toArray(new String[columnNames.size()]);
		} catch (Exception e) {
			throw new UtilException("Get columns error!", e);
		}finally {
			close(rs, conn);
		}
	}
	
	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return Entity对象
	 */
	public static Entity createLimitedEntity(DataSource ds, String tableName){
		String[] columnNames = getColumnNames(ds, tableName);
		return Entity.create(tableName).setFieldNames(columnNames);
	}
	
	/**
	 * 获得表的元信息
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	@SuppressWarnings("resource")
	public static Table getTableMeta(DataSource ds, String tableName) {
		final Table table = Table.create(tableName);
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			//获得主键
			rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
			while(rs.next()) {
				table.addPk("COLUMN_NAME");
			}
			
			//获得列
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while(rs.next()) {
				table.setColumn(Column.create(tableName, rs));
			}
		} catch (Exception e) {
			throw new UtilException("Get columns error!", e);
		}finally {
			close(rs, conn);
		}
		
		return table;
	}
	
	/**
	 * 填充SQL的参数。
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @throws SQLException
	 */
	public static void fillParams(PreparedStatement ps, Collection<Object> params) throws SQLException {
		fillParams(ps, params.toArray(new Object[params.size()]));
	}
	
	/**
	 * 填充SQL的参数。
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @throws SQLException
	 */
	public static void fillParams(PreparedStatement ps, Object... params) throws SQLException {
		if (params == null) {
			return;
		}
		ParameterMetaData pmd = ps.getParameterMetaData();
		for (int i = 0; i < params.length; i++) {
			int paramIndex = i + 1;
			if (params[i] != null) {
				ps.setObject(paramIndex, params[i]);
			} else {
				int sqlType = Types.VARCHAR;
				try {
					sqlType = pmd.getParameterType(paramIndex);
				} catch (SQLException e) {
					log.warn("Param get type fail, by: " + e.getMessage());
				}
				ps.setNull(paramIndex, sqlType);
			}
		}
	}
	
	/**
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效
	 * @param ps PreparedStatement
	 * @return 自增键的值
	 * @throws SQLException
	 */
	public static Long getGeneratedKeyOfLong(PreparedStatement ps) throws SQLException {
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys(); 
			Long generatedKey = null;
			if(rs != null && rs.next()) {
				try{
					generatedKey = rs.getLong(1);
				}catch (SQLException e){
					//自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		} catch (SQLException e) {
			throw e;
		}finally {
			close(rs);
		}
	}
	
	/**
	 * 获得所有主键<br>
	 * @param ps PreparedStatement
	 * @return 所有主键
	 * @throws SQLException
	 */
	public static List<Object> getGeneratedKeys(PreparedStatement ps) throws SQLException {
		List<Object> keys = new ArrayList<Object>();
		ResultSet rs = null;
		int i=1;
		try {
			rs = ps.getGeneratedKeys(); 
			if(rs != null && rs.next()) {
				keys.add(rs.getObject(i++));
			}
			return keys;
		} catch (SQLException e) {
			throw e;
		}finally {
			close(rs);
		}
	}
	
	/**
	 * 构件相等条件的where语句<br>
	 * 如果没有条件语句，泽返回空串，表示没有条件
	 * @param entity 条件实体
	 * @param paramValues 条件值得存放List
	 * @return 带where关键字的SQL部分
	 */
	public static String buildEqualsWhere(Entity entity, List<Object> paramValues) {
		if(null == entity || entity.isEmpty()) {
			return StrUtil.EMPTY;
		}
		
		final StringBuilder sb = new StringBuilder(" WHERE ");
		boolean isNotFirst = false;
		for (Entry<String, Object> entry : entity.entrySet()) {
			if(isNotFirst) {
				sb.append(" and ");
			}else {
				isNotFirst = true;
			}
			sb.append("`").append(entry.getKey()).append("`").append(" = ?");
			paramValues.add(entry.getValue());
		}
		
		return sb.toString();
	}
	
	/**
	 * 通过实体对象构建条件对象
	 * @param entity 实体对象
	 * @return 条件对象
	 */
	public static Condition[] buildConditions(Entity entity){
		if(null == entity || entity.isEmpty()) {
			return null;
		}
		
		final Condition[] conditions = new Condition[entity.size()];
		int i = 0;
		for (Entry<String, Object> entry : entity.entrySet()) {
			conditions[i++] = new Condition(entry.getKey(), entry.getValue());
		}
		
		return conditions;
	}
	
	/**
	 * 识别JDBC驱动名
	 * @param nameContainsProductInfo 包含数据库标识的字符串
	 * @return 驱动
	 */
	public static String identifyDriver(String nameContainsProductInfo) {
		if(StrUtil.isBlank(nameContainsProductInfo)) {
			return null;
		}
		nameContainsProductInfo = nameContainsProductInfo.toLowerCase();
		
		String driver = null;
		if(nameContainsProductInfo.contains("mysql")) {
			driver = DialectFactory.DRIVER_MYSQL;
		}else if(nameContainsProductInfo.contains("oracle")) {
			driver = DialectFactory.DRIVER_ORACLE;
		}else if(nameContainsProductInfo.contains("postgresql")) {
			driver = DialectFactory.DRIVER_POSTGRESQL;
		}else if(nameContainsProductInfo.contains("sqlite")) {
			driver = DialectFactory.DRIVER_SQLLITE3;
		}
		
		return driver;
	}
	
	/**
	 * 识别JDBC驱动名
	 * @param ds 数据源
	 * @return 驱动
	 */
	public static String identifyDriver(DataSource ds) {
		Connection conn = null;
		String driver = null;
		try {
			conn = ds.getConnection();
			driver = identifyDriver(conn);
		} catch (Exception e) {
			throw new DbRuntimeException("Identify driver error!", e);
		}finally {
			close(conn);
		}
		
		return driver;
	}
	
	/**
	 * 识别JDBC驱动名
	 * @param conn 数据库连接对象
	 * @return 驱动
	 */
	public static String identifyDriver(Connection conn) {
		String driver = null;
		try {
			DatabaseMetaData meta = conn.getMetaData();
			driver =  identifyDriver(meta.getDatabaseProductName());
			if(StrUtil.isBlank(driver)) {
				driver =  identifyDriver(meta.getDriverName());
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Identify driver error!", e);
		}
		
		return driver;
	}
	
	/**
	 * 验证实体类对象的有效性
	 * @param entity 实体类对象
	 */
	public static void validateEntity(Entity entity){
		if(null == entity) {
			throw new DbRuntimeException("Entity is null !");
		}
		if(StrUtil.isBlank(entity.getTableName())) {
			throw new DbRuntimeException("Entity`s table name is null !");
		}
		if(entity.isEmpty()) {
			throw new DbRuntimeException("No filed and value in this entity !");
		}
	}
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method end
}
