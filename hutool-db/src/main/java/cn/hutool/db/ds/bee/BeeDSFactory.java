package cn.hutool.db.ds.bee;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import cn.beecp.TransactionIsolationLevel;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * BeeCP数据源工厂类
 *
 * @author Looly
 *
 */
public class BeeDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	public static final String DS_NAME = "BeeCP";

	public BeeDSFactory() {
		this(null);
	}

	public BeeDSFactory(Setting setting) {
		super(DS_NAME, BeeDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {

		final BeeDataSourceConfig beeConfig = new BeeDataSourceConfig(driver, jdbcUrl, user, pass);
		poolSetting.toBean(beeConfig);

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				beeConfig.addConnectProperty(key, connValue);
			}
		}

		// since BeepCP 3.2.1 bug，Sqlite下默认Transaction Isolation不支持，在此判断修正
		if(StrUtil.containsIgnoreCase(jdbcUrl, "sqlite")){
			final int isolationCode = beeConfig.getDefaultTransactionIsolationCode();
			if(Connection.TRANSACTION_READ_UNCOMMITTED != isolationCode
					&& Connection.TRANSACTION_SERIALIZABLE != isolationCode){
				// SQLite只支持这两种事务
				beeConfig.setDefaultTransactionIsolation(TransactionIsolationLevel.LEVEL_READ_UNCOMMITTED);
				beeConfig.setDefaultTransactionIsolationCode(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
		}

		return new BeeDataSource(beeConfig);
	}
}
