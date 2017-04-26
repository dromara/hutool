package com.xiaoleilu.hutool.db.ds.druid;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.db.DbRuntimeException;
import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Druid数据源工厂类
 * @author Looly
 *
 */
public class DruidDSFactory extends DSFactory {
	
	public static final String DS_NAME = "Druid";
	
	private Setting setting;
	/** 数据源池 */
	private Map<String, DruidDataSource> dsMap;
	
	public DruidDSFactory() {
		this(null);
	}
	
	public DruidDSFactory(Setting setting) {
		super(DS_NAME, setting);
		checkCPExist(DruidDataSource.class);
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
		if(CollectionUtil.isNotEmpty(dsMap)){
			Collection<DruidDataSource> values = dsMap.values();
			for (DruidDataSource dds : values) {
				IoUtil.close(dds);
			}
			dsMap.clear();
		}
	}

	/**
	 * 创建数据源
	 * @param group 分组
	 * @return Druid数据源 {@link DruidDataSource}
	 */
	private DruidDataSource createDataSource(String group){
		final Properties config = setting.getProperties(group);
		if(CollectionUtil.isEmpty(config)){
			throw new DbRuntimeException("No Druid config for group: [{}]", group);
		}
		
		final DruidDataSource ds = new DruidDataSource();
		//基本信息
		ds.setUrl(config.getProperty("url"));
		config.remove("url");
		ds.setUsername(config.getProperty("username", config.getProperty("user")));
		config.remove("username");
		config.remove("user");
		ds.setPassword(config.getProperty("password", config.getProperty("pass")));
		config.remove("password");
		config.remove("pass");
		final String driver = config.getProperty("driver");
		if(StrUtil.isNotBlank(driver)){
			config.remove("driver");
			ds.setDriverClassName(config.getProperty("driver"));
		}
		
		//规范化属性名
		Properties config2 = new Properties();
		String keyStr;
		for (Entry<Object, Object> entry : config.entrySet()) {
			keyStr = StrUtil.addPrefixIfNot(Convert.toStr(entry.getKey()), "druid.");
			config2.put(keyStr, entry.getValue());
		}
		
		//连接池信息
		ds.configFromPropety(config2);
		return ds;
	}
}
