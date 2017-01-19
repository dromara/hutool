package com.xiaoleilu.hutool.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 文件工具类样例
 * @author Looly
 *
 */
public class FileUtilDemo {
	public static void main(String[] args) throws IOException {
		
		File file = FileUtil.file("config/demo.set");
		Console.log(file.getAbsolutePath());
		
		String absolutePath = FileUtil.getAbsolutePath("config/demo.set");
		Console.log(absolutePath);
		
		//复制文件或者目录
		FileUtil.copy("D:/a/a.txt", "d:/b", false);
		Console.log("Copy OK");
		
		//复制文件
		FileUtil.copyFile("D:/a/a.txt", "d:/b", StandardCopyOption.REPLACE_EXISTING);
		Console.log("Copy OK");
		
		//复制目录
//		FileUtil.copy("D:\\Java\\maven", "D:\\aaa.txt", true);
		
		//标准化路径
		String result = FileUtil.normalize("file:/aaaaa/../bbbb/./cccc.txt");
		Console.log(result);
		
		Long a = 44356645566006L;
		Console.log(FileUtil.readableFileSize(a));
		
		Console.log("OK");
	}
}
