package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.util.FileUtil;
import com.xiaoleilu.hutool.util.SecureUtil;

/**
 * 安全工具类Demo
 * @author Looly
 *
 */
public class SecureUtilDemo {
	public static void main(String[] args) throws IOException {
		byte[] bytes = FileUtil.readBytes(FileUtil.file("d:\\aaa.png"));
		byte[] base64 = SecureUtil.base64(bytes, true);
		byte[] decodeBase64 = SecureUtil.decodeBase64(base64);
		FileUtil.writeBytes(decodeBase64, "d:\\bbb.png");
		System.out.println("OK");
	}
}
