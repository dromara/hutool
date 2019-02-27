package cn.hutool.db.ds.druid;

import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * Druid数据源工厂类
 * 
 * @author Looly
 *
 */
public class DruidDSFactory extends AbstractDSFactory {

	public static final String DS_NAME = "Druid";

	/**
	 * 构造，使用默认配置文件
	 */
	public DruidDSFactory() {
		this(null);
	}

	/**
	 * 构造
	 * 
	 * @param setting 数据库配置
	 */
	public DruidDSFactory(Setting setting) {
		super(DS_NAME, DruidDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		final DruidDataSource ds = new DruidDataSource();

		ds.setUrl(jdbcUrl);
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pass);

		// 规范化属性名
		Properties druidProps = new Properties();
		String keyStr;
		for (Entry<String, String> entry : poolSetting.entrySet()) {
			keyStr = StrUtil.addPrefixIfNot(entry.getKey(), "druid.");
			druidProps.put(keyStr, entry.getValue());
		}
		// 连接池信息
		ds.configFromPropety(druidProps);

		// 检查关联配置，在用户未设置某项配置时，
		if (null == ds.getValidationQuery()) {
			// 在validationQuery未设置的情况下，以下三项设置都将无效
			ds.setTestOnBorrow(false);
			ds.setTestOnReturn(false);
			ds.setTestWhileIdle(false);
		}

		return ds;
	}
}
