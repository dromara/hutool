package cn.hutool.db.ds.jndi;

import cn.hutool.core.text.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.db.ds.DSUtil;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;

/**
 * JNDI数据源工厂类<br>
 * Setting配置样例：
 * <pre>
 *     [group]
 *     jndi = jdbc/TestDB
 * </pre>
 *
 * @author Looly
 *
 */
public class JndiDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1573625812927370432L;

	/**
	 * 数据源名称：JNDI DataSource
	 */
	public static final String DS_NAME = "JNDI DataSource";

	/**
	 * 构造，使用默认配置文件
	 */
	public JndiDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public JndiDSFactory(final Setting setting) {
		super(DS_NAME, null, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final String jndiName = poolSetting.getStr("jndi");
		if (StrUtil.isEmpty(jndiName)) {
			throw new DbRuntimeException("No setting name [jndi] for this group.");
		}
		return DSUtil.getJndiDS(jndiName);
	}
}
