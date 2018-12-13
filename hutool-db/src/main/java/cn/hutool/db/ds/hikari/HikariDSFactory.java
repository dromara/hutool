package cn.hutool.db.ds.hikari;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.dialect.DriverUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;

/**
 * HikariCP数据源工厂类
 * 
 * @author Looly
 *
 */
public class HikariDSFactory extends DSFactory {

	public static final String DS_NAME = "HikariCP";

	/** 数据源池 */
	private Map<String, HikariDataSource> dsMap;

	public HikariDSFactory() {
		this(null);
	}

	public HikariDSFactory(Setting setting) {
		super(DS_NAME, HikariDataSource.class, setting);
		this.dsMap = new ConcurrentHashMap<>();
	}

	@Override
	synchronized public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		final HikariDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		final HikariDataSource ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		HikariDataSource ds = dsMap.get(group);
		if (ds != null) {
			IoUtil.close(ds);
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if (CollectionUtil.isNotEmpty(dsMap)) {
			Collection<HikariDataSource> values = dsMap.values();
			for (HikariDataSource ds : values) {
				IoUtil.close(ds);
			}
			dsMap.clear();
		}
	}

	/**
	 * 创建数据源
	 * 
	 * @param group 分组
	 * @return Hikari数据源 {@link HikariDataSource}
	 */
	private HikariDataSource createDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final Props config = setting.getProps(group);
		if (MapUtil.isEmpty(config)) {
			throw new DbRuntimeException("No HikariCP config for group: [{}]", group);
		}
		
		// 规范化属性名
		final String url = config.getAndRemoveStr(KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}
		config.put("jdbcUrl", url);
		//自动识别Driver
		String driver = config.getAndRemoveStr(KEY_ALIAS_DRIVER);
		if(StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}
		if(null != driver) {
			config.put("driverClassName", driver);
		}
		final String user = config.getAndRemoveStr(KEY_ALIAS_USER);
		if(null != user) {
			config.put("username", user);
		}
		final String pass = config.getAndRemoveStr(KEY_ALIAS_PASSWORD);
		if(null != pass) {
			config.put("password", pass);
		}
		
		final HikariDataSource ds = new HikariDataSource(new HikariConfig(config));
		return ds;
	}
}
