package looly.github.hutool.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import looly.github.hutool.Log;
import looly.github.hutool.StrUtil;
import looly.github.hutool.exceptions.UtilException;

import org.slf4j.Logger;

/**
 * 数据库操作工具类
 * 
 * @author Luxiaolei
 * 
 */
public class DbUtil {
	private static Logger log = Log.get();

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
			Context ctx = new InitialContext();
			return (DataSource) ctx.lookup(jndiName);
		} catch (NamingException e) {
			log.error("Find JNDI datasource error!", e);
		}
		return null;
	}
	
	/**
	 * 获得所有表名
	 */
	public static List<String> getTables(DataSource ds) {
		List<String> tables = new ArrayList<String>();
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
	 * @return
	 */
	public static String[] getColumns(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] labelNames = new String[columnCount + 1];
			for (int i=1; i<labelNames.length; i++) {
				labelNames[i] = rsmd.getColumnLabel(i);
			}
			return labelNames;
		} catch (Exception e) {
			throw new UtilException("Get colunms error!", e);
		}
	}
	
	//---------------------------------------------------------------------------- Private method start
	//---------------------------------------------------------------------------- Private method end
}
