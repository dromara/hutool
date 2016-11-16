package com.xiaoleilu.hutool.db.ds;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.ds.c3p0.C3p0DSFactory;
import com.xiaoleilu.hutool.db.ds.dbcp.DbcpDSFactory;
import com.xiaoleilu.hutool.db.ds.druid.DruidDSFactory;
import com.xiaoleilu.hutool.db.ds.hikari.HikariDSFactory;
import com.xiaoleilu.hutool.db.ds.pooled.PooledDSFactory;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数据源工厂类
 * 
 * @author Looly
 *
 */
public abstract class DSFactory {
	private static final Log log = LogFactory.get();
	
	protected static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	
	private String dataSourceName;
	
	public DSFactory(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	
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
	 * 获得数据源<br>
	 * 使用默认配置文件的无分组配置
	 * 
	 * @return 数据源
	 */
	public static DataSource get(){
		return get(null);
	}
	
	/**
	 * 获得数据源
	 * 
	 * @param group 配置文件中对应的分组
	 * @return 数据源
	 */
	public static DataSource get(String group){
		return get(null, group);
	}
	
	/**
	 * 获得数据源
	 * 
	 * @param dbSetting 数据库配置文件，如果为<code>null</code>，查找默认的配置文件
	 * @param group 配置文件中对应的分组
	 * @return 数据源
	 */
	public static DataSource get(Setting dbSetting, String group){
		return getCurrentDSFactory(dbSetting).getDataSource(group);
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
		log.debug("Custom use [{}] datasource.", dsFactory.dataSourceName);
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
			log.debug("Use [{}] datasource as default", dsFactory.dataSourceName);
		} catch (Throwable e) {
			try {
				dsFactory = new DruidDSFactory(setting);
				log.debug("Use [{}] datasource as default", dsFactory.dataSourceName);
			} catch (Throwable e2) {
				try {
					dsFactory = new DbcpDSFactory(setting);
					log.debug("Use [{}] datasource as default", dsFactory.dataSourceName);
				} catch (Throwable e3) {
					try {
						dsFactory = new C3p0DSFactory(setting);
						log.debug("Use [{}] datasource as default", dsFactory.dataSourceName);
					} catch (Throwable e4) {
						dsFactory = new PooledDSFactory(setting);
						log.debug("Use [{}] datasource as default", dsFactory.dataSourceName);
					}
				}
			}
		}
		return dsFactory;
	}
	//------------------------------------------------------------------------- Static end
}
