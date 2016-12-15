package com.xiaoleilu.hutool.test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.setting.Setting;

/**
 * 仅用于临时测试
 * 
 * @author Looly
 *
 */
public class Test {
	public static void main(String[] args) {
		Setting setting = new Setting("config/db.setting");
		Console.log(setting);
	}
}
