package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.util.FileUtil;

/**
 * 文件工具类样例
 * @author Looly
 *
 */
public class FileUtilDemo {
	public static void main(String[] args) throws IOException {
		String absolutePath = FileUtil.getAbsolutePath("config/demo.set");
		System.out.println(absolutePath);
		
		//复制文件
//		FileUtil.copy("D:\\Java\\maven\\README.txt", "D:\\maven", true);
		
		//复制目录
//		FileUtil.copy("D:\\Java\\maven", "D:\\aaa.txt", true);
		
		
		System.out.println("OK");
	}
}
