package cn.hutool.db.ds.tomcat;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * Tomcat-Jdbc-Pool数据源工厂类
 * 
 * @author Looly
 *
 */
public class TomcatDSFactory extends AbstractDSFactory {

	public static final String DS_NAME = "Tomcat-Jdbc-Pool";

	/**
	 * 构造
	 */
	public TomcatDSFactory() {
		this(null);
	}

	/**
	 * 构造
	 * 
	 * @param setting Setting数据库配置
	 */
	public TomcatDSFactory(Setting setting) {
		super(DS_NAME, DataSource.class, setting);
	}
	
	@Override
	protected javax.sql.DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		final PoolProperties poolProps = new PoolProperties();
		poolProps.setUrl(jdbcUrl);
		poolProps.setDriverClassName(driver);
		poolProps.setUsername(user);
		poolProps.setPassword(pass);
		
		return new DataSource(poolProps);
	}
}
