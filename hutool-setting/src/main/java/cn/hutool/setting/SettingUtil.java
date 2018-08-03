package cn.hutool.setting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SettingUtil {
	/** 配置文件缓存 */
	private static Map<String, Setting> settingMap = new ConcurrentHashMap<>();
	
	/**
	 * 获取当前环境下的配置文件
	 * @param name 文件名，如果没有扩展名，默认为.setting
	 * @return 当前环境下配置文件
	 */
	public static Setting getSetting(String name){
		Setting setting = settingMap.get(name);
		if(null == setting){
			String filePath = name;
			if(false == filePath.endsWith(Setting.EXT_NAME)) {
				filePath += Setting.EXT_NAME;
			}
			setting = new Setting(name, true);
			settingMap.put(name, setting);
		}
		return setting;
	}
}
