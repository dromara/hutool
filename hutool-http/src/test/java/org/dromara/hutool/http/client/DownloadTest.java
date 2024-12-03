/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.io.StreamProgress;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.http.HttpGlobalConfig;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

/**
 * 下载单元测试
 *
 * @author looly
 */
public class DownloadTest {

	@Test
	@Disabled
	public void downloadPicTest() {
		final String url = "http://wx.qlogo.cn/mmopen/vKhlFcibVUtNBVDjcIowlg0X8aJfHXrTNCEFBukWVH9ta99pfEN88lU39MKspCUCOP3yrFBH3y2NbV7sYtIIlon8XxLwAEqv2/0";
		HttpDownloader.of(url).downloadFile(new File("d:/test/download/t3.jpg"));
		Console.log("ok");
	}

	@Test
	@Disabled
	public void downloadTest1() {
		final File size = HttpDownloader.of("http://explorer.bbfriend.com/crossdomain.xml").downloadFile(new File("d:test/download/temp/"));
		Console.log("Download size: " + size);
	}

	@Test
	void downloadWithHeaderTest() {
		HttpDownloader.of("https://hutool.cn/")
			.header(MapUtil.of("Authorization", "token"))
			.downloadFile(FileUtil.file("d:/test/"));
	}

	@Test
	@Disabled
	public void downloadTest() {
		// 带进度显示的文件下载
		HttpDownloader.of("http://mirrors.sohu.com/centos/7/isos/x86_64/CentOS-7-x86_64-DVD-2009.iso").setStreamProgress(new StreamProgress() {

			final long time = System.currentTimeMillis();

			@Override
			public void start() {
				Console.log("开始下载。。。。");
			}

			@Override
			public void progress(final long contentLength, final long progressSize) {
				final long speed = progressSize / (System.currentTimeMillis() - time) * 1000;
				Console.log("总大小:{}, 已下载：{}, 速度：{}/s", FileUtil.readableFileSize(contentLength), FileUtil.readableFileSize(progressSize), FileUtil.readableFileSize(speed));
			}

			@Override
			public void finish() {
				Console.log("下载完成！");
			}
		}).downloadFile(FileUtil.file("d:/test/"));
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest1() {
		final File file = HttpDownloader.of("http://groovy-lang.org/changelogs/changelog-3.0.5.html")
			.downloadFile(new File("d:/download/temp"));
		Assertions.assertNotNull(file);
		Assertions.assertTrue(file.isFile());
		Assertions.assertTrue(file.length() > 0);
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest2() {
		File file = null;
		try {
			file = HttpDownloader.of("https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.4.0/hutool-all-5.4.0-sources.jar")
				.setStreamProgress(new StreamProgress() {
				@Override
				public void start() {
					System.out.println("start");
				}

				@Override
				public void progress(final long contentLength, final long progressSize) {
					System.out.println("download size:" + progressSize);
				}

				@Override
				public void finish() {
					System.out.println("end");
				}
			}).downloadFile(FileUtil.file("d:/download/temp"));

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertFalse(file.getName().isEmpty());
		} finally {
			FileUtil.del(file);
		}
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest3() {
		File file = null;
		try {
			file = HttpDownloader.of("https://repo1.maven.org/maven2/cn/hutool/hutool-all/5.4.0/hutool-all-5.4.0-sources.jar").setStreamProgress(new StreamProgress() {
				@Override
				public void start() {
					System.out.println("start");
				}

				@Override
				public void progress(final long contentLength, final long progressSize) {
					System.out.println("contentLength:" + contentLength + "download size:" + progressSize);
				}

				@Override
				public void finish() {
					System.out.println("end");
				}
			}).downloadFile(FileUtil.file("d:/download/temp"));

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertFalse(file.getName().isEmpty());
		} finally {
			FileUtil.del(file);
		}
	}

	@Test
	@Disabled
	public void downloadFileFromUrlTest4() {
		File file = null;
		try {
			file = HttpDownloader.of("http://groovy-lang.org/changelogs/changelog-3.0.5.html")
				.downloadFile(FileUtil.file("d:/download/temp"));

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
			Assertions.assertFalse(file.getName().isEmpty());
		} finally {
			FileUtil.del(file);
		}
	}


	@Test
	@Disabled
	public void downloadFileFromUrlTest5() {
		File file = null;
		try {
			file = HttpDownloader.of("http://groovy-lang.org/changelogs/changelog-3.0.5.html")
				.downloadFile(FileUtil.file("d:/download/temp", UUID.randomUUID().toString()));

			Assertions.assertNotNull(file);
			Assertions.assertTrue(file.exists());
			Assertions.assertTrue(file.isFile());
			Assertions.assertTrue(file.length() > 0);
		} finally {
			FileUtil.del(file);
		}

		File file1 = null;
		try {
			file1 = HttpDownloader.of("http://groovy-lang.org/changelogs/changelog-3.0.5.html")
				.downloadFile(FileUtil.file("d:/download/temp"));

			Assertions.assertNotNull(file1);
			Assertions.assertTrue(file1.exists());
			Assertions.assertTrue(file1.isFile());
			Assertions.assertTrue(file1.length() > 0);
		} finally {
			FileUtil.del(file1);
		}
	}

	@Test
	@Disabled
	public void downloadTeamViewerByOkHttpTest() {
		// 此URL有3次重定向, 需要请求4次
		final String url = "https://download.teamviewer.com/download/TeamViewer_Setup_x64.exe";
		HttpGlobalConfig.setMaxRedirects(2);

		final Response send = Request.of(url).send(ClientEngineFactory.createEngine("okhttp"));
		Console.log(send.getStatus());
		Console.log(send.headers());
	}

	@Test
	@Disabled
	public void downloadTeamViewerByJdkTest() {
		// 此URL有3次重定向, 需要请求4次
		final String url = "https://download.teamviewer.com/download/TeamViewer_Setup_x64.exe";
		HttpGlobalConfig.setMaxRedirects(2);

		final Response send = Request.of(url).send(ClientEngineFactory.createEngine("jdkClient"));
		Console.log(send.getStatus());
		Console.log(send.headers());
	}

	@Test
	@Disabled
	public void downloadTeamViewerByHttpClient5Test() {
		// 此URL有3次重定向, 需要请求4次
		final String url = "https://download.teamviewer.com/download/TeamViewer_Setup_x64.exe";
		HttpGlobalConfig.setMaxRedirects(2);

		final Response send = Request.of(url).send(ClientEngineFactory.createEngine("HttpClient5"));
		Console.log(send.getStatus());
		Console.log(send.headers());
	}
}
