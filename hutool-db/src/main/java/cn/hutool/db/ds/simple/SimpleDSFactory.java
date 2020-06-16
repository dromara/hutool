package cn.hutool.db.ds.simple;

import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;

/**
 * 简单数据源工厂类
 * 
 * @author Looly
 *
 */
public class SimpleDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4738029988261034743L;
	
	public static final String DS_NAME = "Hutool-Simple-DataSource";

	public SimpleDSFactory() {
		this(null);
	}

	public SimpleDSFactory(Setting setting) {
		super(DS_NAME, SimpleDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		SimpleDataSource ds = new SimpleDataSource(//
				jdbcUrl, //
				user, //
				pass, //
				driver//
		);
		ds.setConnProps(poolSetting.getProps(Setting.DEFAULT_GROUP));
		return ds;
	}
}
