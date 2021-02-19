package cn.hutool.db.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.DataSourceWrapper;

/**
 * 驱动相关工具类，包括自动获取驱动类名
 * 
 * @author looly
 * @since 4.0.10
 */
public class DriverUtil {
	/**
	 * 通过JDBC URL等信息识别JDBC驱动名
	 * 
	 * @param nameContainsProductInfo 包含数据库标识的字符串
	 * @return 驱动
	 * @see DialectFactory#identifyDriver(String)
	 */
	public static String identifyDriver(String nameContainsProductInfo) {
		return DialectFactory.identifyDriver(nameContainsProductInfo);
	}

	/**
	 * 识别JDBC驱动名
	 * 
	 * @param ds 数据源
	 * @return 驱动
	 */
	public static String identifyDriver(DataSource ds) {
		if(ds instanceof DataSourceWrapper) {
			final String driver = ((DataSourceWrapper)ds).getDriver();
			if(StrUtil.isNotBlank(driver)) {
				return driver;
			}
		}
		
		Connection conn = null;
		String driver;
		try {
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new DbRuntimeException("Get Connection error !", e);
			} catch (NullPointerException e) {
				throw new DbRuntimeException("Unexpected NullPointException, maybe [jdbcUrl] or [url] is empty!", e);
			}
			driver = identifyDriver(conn);
		} finally {
			DbUtil.close(conn);
		}

		return driver;
	}

	/**
	 * 识别JDBC驱动名
	 * 
	 * @param conn 数据库连接对象
	 * @return 驱动
	 * @throws DbRuntimeException SQL异常包装，获取元数据信息失败
	 */
	public static String identifyDriver(Connection conn) throws DbRuntimeException {
		String driver;
		DatabaseMetaData meta;
		try {
			meta = conn.getMetaData();
			driver = identifyDriver(meta.getDatabaseProductName());
			if (StrUtil.isBlank(driver)) {
				driver = identifyDriver(meta.getDriverName());
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Identify driver error!", e);
		}

		return driver;
	}
}
