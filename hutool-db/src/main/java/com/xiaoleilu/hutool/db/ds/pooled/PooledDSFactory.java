package com.xiaoleilu.hutool.db.ds.pooled;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.DbRuntimeException;
import com.xiaoleilu.hutool.db.ds.DSFactory;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 池化数据源工厂类
 * @author Looly
 *
 */
public class PooledDSFactory extends DSFactory {
	
	private Setting setting;
	/** 数据源池 */
	private Map<String, PooledDataSource> dsMap;
	
	public PooledDSFactory() {
		this(null);
	}
	
	public PooledDSFactory(Setting setting) {
		super("Hutool-Pooled-Datasource");
		if(null == setting){
			setting = new Setting(DEFAULT_DB_SETTING_PATH, true);
		}
		this.setting = setting;
		this.dsMap = new ConcurrentHashMap<>();
	}

	@Override
	synchronized public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}
		
		// 如果已经存在已有数据源（连接池）直接返回
		final PooledDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		final PooledDataSource ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		PooledDataSource ds = dsMap.get(group);
		if (ds != null) {
			IoUtil.close(ds);
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if(CollectionUtil.isNotEmpty(dsMap)){
			Collection<PooledDataSource> values = dsMap.values();
			for (PooledDataSource ds : values) {
				IoUtil.close(ds);
			}
			dsMap.clear();
		}
	}

	/**
	 * 创建数据源
	 * @param group 分组
	 * @return 池化数据源 {@link PooledDataSource}
	 */
	private PooledDataSource createDataSource(String group){
		if (group == null) {
			group = StrUtil.EMPTY;
		}
		
		Setting config = setting.getSetting(group);
		if(null == config || config.isEmpty()){
			throw new DbRuntimeException("No PooledDataSource config for group: [{}]", group);
		}
		final PooledDataSource ds = new PooledDataSource(new DbSetting(config).getDbConfig(null));
		return ds;
	}
}
