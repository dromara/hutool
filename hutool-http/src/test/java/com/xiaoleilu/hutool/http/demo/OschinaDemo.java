package com.xiaoleilu.hutool.http.demo;

import java.util.List;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.ReUtil;

public class OschinaDemo {
	public static void main(String[] args) {
		//请求列表页
		String listContent = HttpUtil.get("http://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=2");
		//使用正则获取所有标题
		List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
		for (String title : titles) {
			//打印标题
			Console.log(title);
		}
	}
}
