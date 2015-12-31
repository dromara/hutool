package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.FileUtil;

public class FileUtilDemo {
	public static void main(String[] args) throws IOException {
		FileUtil.writeString("测试测试s", "e:/a.txt", "utf-8");
	}
}
