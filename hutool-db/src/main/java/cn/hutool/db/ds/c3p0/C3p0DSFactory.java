package cn.hutool.db.ds.c3p0;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * Druid数据源工厂类
 * 
 * @author Looly
 *
 */
public class C3p0DSFactory extends AbstractDSFactory {

	public static final String DS_NAME = "C3P0";

	/**
	 * 构造，使用默认配置
	 */
	public C3p0DSFactory() {
		this(null);
	}

	/**
	 * 构造
	 * 
	 * @param setting 配置
	 */
	public C3p0DSFactory(Setting setting) {
		super(DS_NAME, ComboPooledDataSource.class, setting);
	}
	
	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		final ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl(jdbcUrl);
		try {
			ds.setDriverClass(driver);
		} catch (PropertyVetoException e) {
			throw new DbRuntimeException(e);
		}
		ds.setUser(user);
		ds.setPassword(pass);
		poolSetting.toBean(ds);// 注入属性
		
		return ds;
	}
}
