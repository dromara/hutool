package cn.hutool.db.ds.hikari;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * HikariCP数据源工厂类
 * 
 * @author Looly
 *
 */
public class HikariDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = -8834744983614749401L;
	
	public static final String DS_NAME = "HikariCP";

	public HikariDSFactory() {
		this(null);
	}

	public HikariDSFactory(Setting setting) {
		super(DS_NAME, HikariDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}

		final Props config = new Props();
		config.putAll(poolSetting);

		config.put("jdbcUrl", jdbcUrl);
		if (null != driver) {
			config.put("driverClassName", driver);
		}
		if (null != user) {
			config.put("username", user);
		}
		if (null != pass) {
			config.put("password", pass);
		}

		final HikariConfig hikariConfig = new HikariConfig(config);
		hikariConfig.setDataSourceProperties(connProps);

		return new HikariDataSource(hikariConfig);
	}
}
