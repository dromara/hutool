package cn.hutool.db.ds.bee;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import cn.hutool.core.text.StrUtil;
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

	public BeeDSFactory(final Setting setting) {
		super(DS_NAME, BeeDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {

		final BeeDataSourceConfig beeConfig = new BeeDataSourceConfig(driver, jdbcUrl, user, pass);
		poolSetting.toBean(beeConfig);

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				beeConfig.addConnectProperty(key, connValue);
			}
		}

		return new BeeDataSource(beeConfig);
	}
}
