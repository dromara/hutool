package cn.hutool.db.ds.dbcp;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * DBCP2数据源工厂类
 * 
 * @author Looly
 *
 */
public class DbcpDSFactory extends AbstractDSFactory {

	public static final String DS_NAME = "commons-dbcp2";

	public DbcpDSFactory() {
		this(null);
	}

	public DbcpDSFactory(Setting setting) {
		super(DS_NAME, BasicDataSource.class, setting);
	}
	
	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		final BasicDataSource ds = new BasicDataSource();
		
		ds.setUrl(jdbcUrl);
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pass);
		poolSetting.toBean(ds);// 注入属性
		
		return ds;
	}
}
