package com.xiaoleilu.hutool.db.ds.c3p0;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xiaoleilu.hutool.db.DbRuntimeException;
import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Druid数据源工厂类
 * @author Looly
 *
 */
public class C3p0DSFactory extends DSFactory {
	
	private Setting setting;
	/** 数据源池 */
	private Map<String, ComboPooledDataSource> dsMap;
	
	public C3p0DSFactory() {
		this(null);
	}
	
	public C3p0DSFactory(Setting setting) {
		super("C3P0");
		checkCPExist(ComboPooledDataSource.class);
		if(null == setting){
			setting = new Setting(DEFAULT_DB_SETTING_PATH, true);
		}
		this.setting = setting;
		this.dsMap = new ConcurrentHashMap<>();
	}

	@Override
	public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}
		
		// 如果已经存在已有数据源（连接池）直接返回
		final ComboPooledDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		ComboPooledDataSource ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		ComboPooledDataSource ds = dsMap.get(group);
		if (ds != null) {
			ds.close();
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if(CollectionUtil.isNotEmpty(dsMap)){
			Collection<ComboPooledDataSource> values = dsMap.values();
			for (ComboPooledDataSource ds : values) {
				ds.close();
			}
			dsMap.clear();
			dsMap = null;
		}
	}

	/**
	 * 创建数据源
	 * @param group 分组
	 * @return C3P0数据源 {@link ComboPooledDataSource}
	 */
	private ComboPooledDataSource createDataSource(String group){
		final Properties config = setting.getProperties(group);
		if(CollectionUtil.isEmpty(config)){
			throw new DbRuntimeException("No Druid config for group: [{}]", group);
		}
		
		final ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setProperties(config);
		return ds;
	}
}
