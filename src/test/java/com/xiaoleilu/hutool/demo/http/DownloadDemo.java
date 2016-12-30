package com.xiaoleilu.hutool.demo.http;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.StreamProgress;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 下载样例
 * @author Looly
 *
 */
public class DownloadDemo {
	public static void main(String[] args) {
		// 下载文件
		long size = HttpUtil.downloadFile("https://www.baidu.com/", FileUtil.file("e:/"));
		System.out.println("Download size: " + size);
		
		//带进度显示的文件下载
		HttpUtil.downloadFile("http://mirrors.sohu.com/centos/7.3.1611/isos/x86_64/CentOS-7-x86_64-DVD-1611.iso", 
				FileUtil.file("e:/"), new StreamProgress(){
			
			@Override
			public void start() {
				Console.log("开始下载。。。。");
			}
			
			@Override
			public void progress(long progressSize) {
				Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
			}
			
			@Override
			public void finish() {
				Console.log("下载完成！");
			}
		});
	}
}
