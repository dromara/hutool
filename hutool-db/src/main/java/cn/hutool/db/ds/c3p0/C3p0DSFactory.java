package cn.hutool.db.ds.c3p0;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Druid数据源工厂类
 *
 * @author Looly
 *
 */
public class C3p0DSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = -6090788225842047281L;

	public static final String DS_NAME = "C3P0";

	/**
	 * 构造，使用默认配置
	 */
	public C3p0DSFactory() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param setting 配置
	 */
	public C3p0DSFactory(final Setting setting) {
		super(DS_NAME, ComboPooledDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final ComboPooledDataSource ds = new ComboPooledDataSource();

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (final String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}
		if(MapUtil.isNotEmpty(connProps)){
			ds.setProperties(connProps);
		}

		ds.setJdbcUrl(jdbcUrl);
		try {
			ds.setDriverClass(driver);
		} catch (final PropertyVetoException e) {
			throw new DbRuntimeException(e);
		}
		ds.setUser(user);
		ds.setPassword(pass);

		// 注入属性
		poolSetting.toBean(ds);

		return ds;
	}
}
