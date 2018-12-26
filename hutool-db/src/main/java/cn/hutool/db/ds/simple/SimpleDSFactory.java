package cn.hutool.db.ds.simple;

import javax.sql.DataSource;

import cn.hutool.db.ds.AbstractDSFactory;
import cn.hutool.setting.Setting;

/**
 * 简单数据源工厂类
 * 
 * @author Looly
 *
 */
public class SimpleDSFactory extends AbstractDSFactory {

	public static final String DS_NAME = "Hutool-Simple-DataSource";

	public SimpleDSFactory() {
		this(null);
	}

	public SimpleDSFactory(Setting setting) {
		super(DS_NAME, SimpleDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting) {
		return new SimpleDataSource(//
				jdbcUrl, //
				user, //
				pass, //
				driver//
		);
	}

}
