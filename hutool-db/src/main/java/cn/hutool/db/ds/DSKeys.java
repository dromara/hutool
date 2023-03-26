package cn.hutool.db.ds;

/**
 * 数据源配置的字段名
 *
 * @since 6.0.0
 * @author Looly
 */
public interface DSKeys {

	/** 某些数据库需要的特殊配置项需要的配置项 */
	String[] KEY_CONN_PROPS = {"remarks", "useInformationSchema"};

	/** 别名字段名：URL */
	String[] KEY_ALIAS_URL = { "url", "jdbcUrl" };
	/** 别名字段名：驱动名 */
	String[] KEY_ALIAS_DRIVER = { "driver", "driverClassName" };
	/** 别名字段名：用户名 */
	String[] KEY_ALIAS_USER = { "user", "username" };
	/** 别名字段名：密码 */
	String[] KEY_ALIAS_PASSWORD = { "pass", "password" };
}
