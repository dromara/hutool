package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.ZipUtil;

/**
 * ZipUtil测试类
 * @author Looly
 *
 */
public class ZipUtilTest {
	
	public static void main(String[] args) {
		String dir = "D:\\Java\\maven";
		String zipPath = "d:/aaa/test.zip";
		try {
			ZipUtil.zip(dir, zipPath,true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String zipFilePath = "d:/aaa/test.zip";
		String unzipFilePath = "d:/aaa/";
		try {
			ZipUtil.unzip(zipFilePath, unzipFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("OK");
	}
}
