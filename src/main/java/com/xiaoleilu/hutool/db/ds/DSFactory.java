package com.xiaoleilu.hutool.db.ds;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.ds.druid.DruidDSFactory;
import com.xiaoleilu.hutool.db.ds.hikari.HikariDSFactory;
import com.xiaoleilu.hutool.db.ds.pooled.PooledDSFactory;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数据源工厂类
 * 
 * @author Looly
 *
 */
public abstract class DSFactory {
	
	protected static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	
	/**
	 * 获得默认数据源
	 * 
	 * @return 数据源
	 */
	public DataSource getDataSource(){
		return getDataSource(StrUtil.EMPTY);
	}
	
	/**
	 * 获得分组对应数据源
	 * 
	 * @param group 分组名
	 * @return 数据源
	 */
	public abstract DataSource getDataSource(String group);
	
	/**
	 * 关闭默认数据源（空组）
	 */
	public void close(){
		close(StrUtil.EMPTY);
	}
	
	/**
	 * 关闭对应数据源
	 * @param group
	 */
	public abstract void close(String group);
	
	/**
	 * 销毁工厂类，关闭所有数据源
	 */
	public abstract void destroy();
	
	//------------------------------------------------------------------------- Static start
	private static DSFactory currentDSFactory;
	private static final Object lock = new Object();
	
	/**
	 * 获得数据源
	 * @return 数据源
	 */
	public static DataSource get(Setting setting, String group){
		return getCurrentDSFactory(setting).getDataSource(group);
	}
	
	/**
	 * @param setting 数据源配置文件
	 * @return 当前使用的数据源工厂
	 */
	public static DSFactory getCurrentDSFactory(Setting setting){
		if(null == currentDSFactory){
			synchronized (lock) {
				if(null == currentDSFactory){
					currentDSFactory = detectDSFactory(setting);
				}
			}
		}
		return currentDSFactory;
	}
	
	/**
	 * @param dsFactory 数据源工厂
	 * @return 自定义的数据源工厂
	 */
	public static DSFactory setCurrentDSFactory(DSFactory dsFactory){
		currentDSFactory = dsFactory;
		return currentDSFactory;
	}
	
	/**
	 * 决定数据源实现工厂
	 * @return 日志实现类
	 */
	private static DSFactory detectDSFactory(Setting setting){
		DSFactory dsFactory;
		try {
			dsFactory = new HikariDSFactory(setting);
		} catch (Throwable e) {
			try {
				dsFactory = new DruidDSFactory(setting);
			} catch (Throwable e2) {
				dsFactory = new PooledDSFactory(setting);
			}
		}
		return dsFactory;
	}
	//------------------------------------------------------------------------- Static end
}
