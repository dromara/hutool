package cn.hutool.db.ds.tomcat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Tomcat-Jdbc-Pool数据源工厂类
 * 
 * @author Looly
 *
 */
public class TomcatDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4925514193275150156L;
	
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

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}
		poolProps.setDbProperties(connProps);

		// 连接池相关参数
		poolSetting.toBean(poolProps);
		
		return new DataSource(poolProps);
	}
}
