package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.ZipUtil;

/**
 * ZipUtil测试类
 * @author Looly
 *
 */
public class ZipUtilTest {
	
	public static void main(String[] args) throws IOException {
		ZipUtil.zip("d:/java/maven/README.txt");

		System.out.println("OK");
	}
}
