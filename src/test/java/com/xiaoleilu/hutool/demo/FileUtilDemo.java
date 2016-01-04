package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.FileUtil;

public class FileUtilDemo {
	public static void main(String[] args) throws IOException {
		//复制文件
//		FileUtil.copy("D:\\Java\\maven\\README.txt", "D:\\maven", true);
		
		//复制目录
		FileUtil.copy("D:\\Java\\maven", "D:\\aaa.txt", true);
		
		
		System.out.println("OK");
	}
}
