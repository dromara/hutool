package cn.hutool.db.ds.tomcat;

import cn.hutool.core.text.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.db.ds.DSKeys;
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

	/**
	 * 数据源名称：Tomcat-Jdbc-Pool
	 */
	public static final String DS_NAME = "Tomcat-Jdbc-Pool";

	/**
	 * 构造
	 */
	public TomcatDSFactory() {
		this(null);
	}

	/**
	 * 构造，自定义配置
	 *
	 * @param setting Setting数据库配置
	 */
	public TomcatDSFactory(final Setting setting) {
		super(DS_NAME, DataSource.class, setting);
	}

	@Override
	protected javax.sql.DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final PoolProperties poolProps = new PoolProperties();
		poolProps.setUrl(jdbcUrl);
		poolProps.setDriverClassName(driver);
		poolProps.setUsername(user);
		poolProps.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
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
