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
		ds.setUrl(getAndRemoveProperty(config, "url", "jdbcUrl"));
		ds.setUsername(getAndRemoveProperty(config, "username", "user"));
		ds.setPassword(getAndRemoveProperty(config, "password", "pass"));
		final String driver = getAndRemoveProperty(config, "driver", "driverClassName");
		if(StrUtil.isNotBlank(driver)){
			ds.setDriverClassName(driver);
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
		
		//检查关联配置，在用户未设置某项配置时，
		if(null == ds.getValidationQuery()){
			//在validationQuery未设置的情况下，以下三项设置都将无效
			ds.setTestOnBorrow(false);
			ds.setTestOnReturn(false);
			ds.setTestWhileIdle(false);
		}
		
		return ds;
	}
	
	/**
	 * 获得指定KEY对应的值，key1和key2为属性的两个名字，可以互作别名
	 * @param properties 属性
	 * @param key1 属性名
	 * @param key2 备用属性名
	 * @return 值
	 */
	private String getAndRemoveProperty(Properties properties, String key1, String key2){
		String value = (String) properties.remove(key1);
		if(StrUtil.isBlank(value)){
			value = (String) properties.remove(key2);
		}
		return value;
	}
}
