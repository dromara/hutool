package cn.hutool.poi.word;

import java.io.File;

/**
 * Word工具类
 * 
 * @author Looly
 * @since 4.5.16
 */
public class WordUtil {
	/**
	 * 创建Word 07格式的生成器
	 * 
	 * @return {@link Word07Writer}
	 */
	public static Word07Writer getWriter() {
		return new Word07Writer();
	}

	/**
	 * 创建Word 07格式的生成器
	 * 
	 * @param destFile 目标文件
	 * @return {@link Word07Writer}
	 */
	public static Word07Writer getWriter(File destFile) {
		return new Word07Writer(destFile);
	}
}
