package cn.hutool.db.ds.dbcp;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.dialect.DriverUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.setting.Setting;

/**
 * DBCP2数据源工厂类
 * 
 * @author Looly
 *
 */
public class DbcpDSFactory extends DSFactory {

	public static final String DS_NAME = "Common-DBCP2";

	/** 数据源池 */
	private Map<String, BasicDataSource> dsMap;

	public DbcpDSFactory() {
		this(null);
	}

	public DbcpDSFactory(Setting setting) {
		super(DS_NAME, BasicDataSource.class, setting);
		this.dsMap = new ConcurrentHashMap<>();
	}

	@Override
	synchronized public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		final BasicDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		final BasicDataSource ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		BasicDataSource ds = dsMap.get(group);
		if (ds != null) {
			IoUtil.close(ds);
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if (CollectionUtil.isNotEmpty(dsMap)) {
			Collection<BasicDataSource> values = dsMap.values();
			for (BasicDataSource ds : values) {
				IoUtil.close(ds);
			}
			dsMap.clear();
		}
	}

	/**
	 * 创建数据源
	 * 
	 * @param group 分组
	 * @return Dbcp数据源 {@link BasicDataSource}
	 */
	private BasicDataSource createDataSource(String group) {
		final Setting config = setting.getSetting(group);
		if (CollectionUtil.isEmpty(config)) {
			throw new DbRuntimeException("No DBCP config for group: [{}]", group);
		}

		final BasicDataSource ds = new BasicDataSource();

		// 基本信息
		String url = config.getAndRemoveStr(KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}
		ds.setUrl(url);
		ds.setUsername(config.getAndRemoveStr(KEY_ALIAS_USER));
		ds.setPassword(config.getAndRemoveStr(KEY_ALIAS_PASSWORD));
		final String driver = config.getAndRemoveStr(KEY_ALIAS_DRIVER);
		if (StrUtil.isNotBlank(driver)) {
			ds.setDriverClassName(driver);
		} else {
			ds.setDriverClassName(DriverUtil.identifyDriver(ds.getUrl()));
		}

		config.toBean(ds);// 注入属性
		return ds;
	}
}
