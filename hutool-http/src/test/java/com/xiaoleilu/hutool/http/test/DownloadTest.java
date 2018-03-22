package com.xiaoleilu.hutool.http.test;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.StreamProgress;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 下载单元测试
 * 
 * @author looly
 */
@Ignore
public class DownloadTest {

	@Test
	public void downloadSizeTest() {
		long size = HttpUtil.downloadFile("https://www.baidu.com/", FileUtil.file("d:/"));
		System.out.println("Download size: " + size);
	}

	@Test
	public void downloadTest() {
		// 带进度显示的文件下载
		HttpUtil.downloadFile("http://mirrors.sohu.com/centos/7/isos/x86_64/CentOS-7-x86_64-NetInstall-1708.iso", FileUtil.file("d:/"), new StreamProgress() {

			long time = System.currentTimeMillis();

			@Override
			public void start() {
				Console.log("开始下载。。。。");
			}

			@Override
			public void progress(long progressSize) {
				long speed = progressSize / (System.currentTimeMillis() - time) * 1000;
				Console.log("已下载：{}, 速度：{}/s", FileUtil.readableFileSize(progressSize), FileUtil.readableFileSize(speed));
			}

			@Override
			public void finish() {
				Console.log("下载完成！");
			}
		});
	}
}
