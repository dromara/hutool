package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.FileUtil;

/**
 * Setting演示样例类
 * @author Looly
 *
 */
public class SettingDemo {
	public static void main(String[] args) throws IOException {
		//--------------------------------------------- 初始化
		//读取classpath下的XXX.setting，不使用变量
		Setting setting = new Setting("XXX.setting");
		
		//读取classpath下的config目录下的XXX.setting，不使用变量
		setting = new Setting("config/XXX.setting");
		
		//读取绝对路径文件/home/looly/XXX.setting（没有就创建，关于touch请查阅FileUtil）
		//第二个参数为自定义的编码，请保持与Setting文件的编码一致
		//第三个参数为是否使用变量，如果为true，则配置文件中的每个key都可以被之后的条目中的value引用形式为 ${key}
		setting = new Setting(FileUtil.touch("/home/looly/XXX.setting"), CharsetUtil.UTF_8, true);
		
		//读取与SettingDemo.class文件同包下的XXX.setting
		setting = new Setting("XXX.setting", SettingDemo.class, CharsetUtil.UTF_8, true);
		
		//--------------------------------------------- 使用
		//获取key为name的值
		setting.getString("name");
		//获取分组为group下key为name的值
		setting.getString("name", "group1");
		//当获取的值为空（null或者空白字符时，包括多个空格），返回默认值
		setting.getStringWithDefault("name", "默认值");
		//完整的带有key、分组和默认值的获得值得方法
		setting.getStringWithDefault("name", "group1", "默认值");
		
		//如果想获得其它类型的值，可以调用相应的getXXX方法，参数相似
		
		//有时候需要在key对应value不存在的时候（没有这项设置的时候）告知用户，故有此方法打印一个debug日志
		setting.getWithLog("name");
		setting.getWithLog("name", "group1");
		
		//重新读取配置文件，可以启用一个定时器调用此方法来定时更新配置
		setting.reload();
		
		//当通过代码加入新的键值对的时候，调用store会保存到文件，但是会覆盖原来的文件，并丢失注释
		setting.setSetting("name1", "value");
		setting.store("/home/looly/XXX.setting");

		//获得所有分组名
		setting.getGroups();
		
		//将key-value映射为对象，原理是原理是调用对象对应的setXX方法
		//setting.toObject();
		
		//设定变量名的正则表达式。
		//Setting的变量替换是通过正则查找替换的，如果Setting中的变量名和其他冲突，可以改变变量的定义方式
		//整个正则匹配变量名，分组1匹配key的名字
		setting.setVarRegex("\\$\\{(.*?)\\}");
	}
}
