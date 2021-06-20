package cn.hutool.db.ds.jndi;

import javax.sql.DataSource;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * JNDI数据源工厂类<br>
 * Setting配置样例：<br>
 * ---------------------<br>
 * [group]<br>
 * jndi = jdbc/TestDB<br>
 * ---------------------<br>
 * 
 * @author Looly
 *
 */
public class JndiDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1573625812927370432L;
	
	public static final String DS_NAME = "JNDI DataSource";

	public JndiDSFactory() {
		this(null);
	}

	public JndiDSFactory(Setting setting) {
		super(DS_NAME, null, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		String jndiName = poolSetting.getStr("jndi");
		if (StrUtil.isEmpty(jndiName)) {
			throw new DbRuntimeException("No setting name [jndi] for this group.");
		}
		return DbUtil.getJndiDs(jndiName);
	}
}
