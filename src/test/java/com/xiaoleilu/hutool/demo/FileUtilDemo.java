package com.xiaoleilu.hutool.demo;

import java.io.File;
import java.io.IOException;

import com.xiaoleilu.hutool.util.FileUtil;

/**
 * 文件工具类样例
 * @author Looly
 *
 */
public class FileUtilDemo {
	public static void main(String[] args) throws IOException {
		
		File file = FileUtil.file("config/demo.set");
		System.out.println(file.getAbsolutePath());
		
		String absolutePath = FileUtil.getAbsolutePath("config/demo.set");
		System.out.println(absolutePath);
		
		//复制文件
//		FileUtil.copy("D:\\Java\\maven\\README.txt", "D:\\maven", true);
		
		//复制目录
//		FileUtil.copy("D:\\Java\\maven", "D:\\aaa.txt", true);
		
		Long a = 44356645566006L;
		
		System.out.println(FileUtil.readableFileSize(a));
		
		System.out.println("OK");
	}
}
