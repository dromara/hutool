package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.lang.Base64;

/**
 * 安全工具类Demo
 * @author Looly
 *
 */
public class SecureUtilDemo {
	public static void main(String[] args) throws IOException {
		byte[] bytes = FileUtil.readBytes(FileUtil.file("d:\\aaa.png"));
		byte[] base64 = Base64.encode(bytes, true);
		byte[] decodeBase64 = Base64.decode(base64);
		FileUtil.writeBytes(decodeBase64, "d:\\bbb.png");
		System.out.println("OK");
	}
}
