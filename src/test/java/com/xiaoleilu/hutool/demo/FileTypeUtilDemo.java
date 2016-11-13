package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.io.FileTypeUtil;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.system.SystemUtil;

/**
 * 文件类型判断Demo
 * @author Looly
 *
 */
public class FileTypeUtilDemo {
	public static void main(String[] args) {
		String javaHome = SystemUtil.get(SystemUtil.HOME);
		Console.log("Java home: {}", javaHome);
		Console.log("-----------------------------------------------------------");
		try {
			Console.log(FileTypeUtil.getTypeByPath(javaHome + "/lib/alt-rt.jar"));
			Console.log(FileTypeUtil.getTypeByPath(javaHome + "/lib/plugin.jar"));
		} catch (IOException e) {
			Console.error(e);
		}
	}
}
