package cn.hutool.db.ds.bee;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import cn.hutool.core.text.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.db.ds.DSKeys;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;

/**
 * BeeCP数据源工厂类
 *
 * @author Looly
 */
public class BeeDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1L;

	/**
	 * 连接池名称：BeeCP
	 */
	public static final String DS_NAME = "BeeCP";

	/**
	 * 构造，使用默认配置文件
	 */
	public BeeDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置文件
	 */
	public BeeDSFactory(final Setting setting) {
		super(DS_NAME, BeeDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {

		final BeeDataSourceConfig beeConfig = new BeeDataSourceConfig(driver, jdbcUrl, user, pass);
		poolSetting.toBean(beeConfig);

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if (StrUtil.isNotBlank(connValue)) {
				beeConfig.addConnectProperty(key, connValue);
			}
		}

		return new BeeDataSource(beeConfig);
	}
}
