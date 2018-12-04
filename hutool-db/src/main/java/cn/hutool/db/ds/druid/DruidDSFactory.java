package cn.hutool.db.ds.druid;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.dialect.DriverUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.setting.Setting;

/**
 * Druid数据源工厂类
 * 
 * @author Looly
 *
 */
public class DruidDSFactory extends DSFactory {

	public static final String DS_NAME = "Druid";

	/** 数据源池 */
	private Map<String, DruidDataSource> dsMap;

	public DruidDSFactory() {
		this(null);
	}

	public DruidDSFactory(Setting setting) {
		super(DS_NAME, DruidDataSource.class, setting);
		this.dsMap = new ConcurrentHashMap<>();
	}

	@Override
	synchronized public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		final DruidDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		DruidDataSource ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		DruidDataSource dds = dsMap.get(group);
		if (dds != null) {
			IoUtil.close(dds);
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if (CollectionUtil.isNotEmpty(dsMap)) {
			Collection<DruidDataSource> values = dsMap.values();
			for (DruidDataSource dds : values) {
				IoUtil.close(dds);
			}
			dsMap.clear();
		}
	}

	/**
	 * 创建数据源
	 * 
	 * @param group 分组
	 * @return Druid数据源 {@link DruidDataSource}
	 */
	private DruidDataSource createDataSource(String group) {
		final Setting config = setting.getSetting(group);
		if (CollectionUtil.isEmpty(config)) {
			throw new DbRuntimeException("No Druid config for group: [{}]", group);
		}

		final DruidDataSource ds = new DruidDataSource();

		// 基本信息
		final String url = config.getAndRemoveStr(KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}
		ds.setUrl(url);
		// 自动识别Driver
		final String driver = config.getAndRemoveStr(KEY_ALIAS_DRIVER);
		ds.setDriverClassName(StrUtil.isNotBlank(driver) ? driver : DriverUtil.identifyDriver(url));
		ds.setUsername(config.getAndRemoveStr(KEY_ALIAS_USER));
		ds.setPassword(config.getAndRemoveStr(KEY_ALIAS_PASSWORD));

		// 规范化属性名
		Properties druidProps = new Properties();
		String keyStr;
		for (Entry<String, String> entry : config.entrySet()) {
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
