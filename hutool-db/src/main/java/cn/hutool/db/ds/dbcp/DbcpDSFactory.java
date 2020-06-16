package cn.hutool.db.ds.dbcp;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * DBCP2数据源工厂类
 * 
 * @author Looly
 *
 */
public class DbcpDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = -9133501414334104548L;
	
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

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				ds.addConnectionProperty(key, connValue);
			}
		}

		// 注入属性
		poolSetting.toBean(ds);

		return ds;
	}
}
