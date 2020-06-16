package cn.hutool.db.ds.c3p0;

import cn.hutool.core.util.StrUtil;
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
	public C3p0DSFactory(Setting setting) {
		super(DS_NAME, ComboPooledDataSource.class, setting);
	}
	
	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		final ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl(jdbcUrl);
		try {
			ds.setDriverClass(driver);
		} catch (PropertyVetoException e) {
			throw new DbRuntimeException(e);
		}
		ds.setUser(user);
		ds.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}
		ds.setProperties(connProps);

		// 注入属性
		poolSetting.toBean(ds);

		return ds;
	}
}
