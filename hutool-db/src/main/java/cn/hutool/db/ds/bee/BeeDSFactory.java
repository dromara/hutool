package cn.hutool.db.ds.bee;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;

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

		// 修复BeeCP默认参数无效问题
		if(beeConfig.getBorrowConcurrentSize() > beeConfig.getMaxActive()){
			beeConfig.setMaxActive(beeConfig.getBorrowConcurrentSize() + 1);
		}

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				beeConfig.addConnectProperty(key, connValue);
			}
		}

		return new BeeDataSource(beeConfig);
	}
}
