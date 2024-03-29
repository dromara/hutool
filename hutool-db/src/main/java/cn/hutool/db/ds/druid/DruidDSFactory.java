package cn.hutool.db.ds.druid;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * Druid数据源工厂类
 *
 * @author Looly
 *
 */
public class DruidDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4680621702534433222L;

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

		// 基本信息
		ds.setUrl(jdbcUrl);
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		// Druid中也可以通过 druid.connectProperties 属性设置
		String connValue;
		for (String key : KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemoveStr(key);
			if(StrUtil.isNotBlank(connValue)){
				ds.addConnectionProperty(key, connValue);
			}
		}

		// Druid连接池配置信息，规范化属性名
		final Props druidProps = new Props();
		poolSetting.forEach((key, value)-> druidProps.put(StrUtil.addPrefixIfNot(key, "druid."), value));
		ds.configFromPropety(druidProps);

		//issue#I4ZKCW 某些非属性设置单独设置
		// connectionErrorRetryAttempts
		final String connectionErrorRetryAttemptsKey = "druid.connectionErrorRetryAttempts";
		if(druidProps.containsKey(connectionErrorRetryAttemptsKey)){
			ds.setConnectionErrorRetryAttempts(druidProps.getInt(connectionErrorRetryAttemptsKey));
		}
		// timeBetweenConnectErrorMillis
		final String timeBetweenConnectErrorMillisKey = "druid.timeBetweenConnectErrorMillis";
		if(druidProps.containsKey(timeBetweenConnectErrorMillisKey)){
			ds.setTimeBetweenConnectErrorMillis(druidProps.getInt(timeBetweenConnectErrorMillisKey));
		}
		// breakAfterAcquireFailure
		final String breakAfterAcquireFailureKey = "druid.breakAfterAcquireFailure";
		if(druidProps.containsKey(breakAfterAcquireFailureKey)){
			ds.setBreakAfterAcquireFailure(druidProps.getBool(breakAfterAcquireFailureKey));
		}

		// issue#I8STFC 补充
		// validationQueryTimeout
		final String validationQueryTimeout = "druid.validationQueryTimeout";
		if(druidProps.containsKey(validationQueryTimeout)){
			ds.setValidationQueryTimeout(druidProps.getInt(validationQueryTimeout));
		}
		// queryTimeout
		final String queryTimeout = "druid.queryTimeout";
		if(druidProps.containsKey(queryTimeout)){
			ds.setQueryTimeout(druidProps.getInt(queryTimeout));
		}
		// connectTimeout
		final String connectTimeout = "druid.connectTimeout";
		if(druidProps.containsKey(connectTimeout)){
			ds.setConnectTimeout(druidProps.getInt(connectTimeout));
		}
		// socketTimeout
		final String socketTimeout = "druid.socketTimeout";
		if(druidProps.containsKey(socketTimeout)){
			ds.setSocketTimeout(druidProps.getInt(socketTimeout));
		}
		// transactionQueryTimeout
		final String transactionQueryTimeout = "druid.transactionQueryTimeout";
		if(druidProps.containsKey(transactionQueryTimeout)){
			ds.setTransactionQueryTimeout(druidProps.getInt(transactionQueryTimeout));
		}
		// loginTimeout
		final String loginTimeout = "druid.loginTimeout";
		if(druidProps.containsKey(loginTimeout)){
			ds.setLoginTimeout(druidProps.getInt(loginTimeout));
		}

		// 检查关联配置，在用户未设置某项配置时，设置默认值
		if (null == ds.getValidationQuery()) {
			// 在validationQuery未设置的情况下，以下三项设置都将无效
			ds.setTestOnBorrow(false);
			ds.setTestOnReturn(false);
			ds.setTestWhileIdle(false);
		}

		return ds;
	}
}
