package cn.hutool.poi.word;

import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.util.Map;

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

	/**
	 * 根据模版导出报表。
	 * 模版语法支持：变量表达式、foreach循环(支持无限级嵌套)、if、Merge(垂直方向上合并单元格)
	 * 具体用法，参考测试用例
	 * 注意：模版和报表都是.docx格式
	 *
	 * @param templateFile 模版
	 * @param context      模版上下文数据
	 * @param reportFile   报表
	 */
	public static void exportDocx(String templateFile, Map<String, Object> context, String reportFile) {
		try (
				BufferedInputStream inputStream = FileUtil.getInputStream(templateFile);
				BufferedOutputStream outputStream = FileUtil.getOutputStream(reportFile)
		) {
			exportDocx(inputStream, context, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("io exception");
		}


	}

	/**
	 * 根据模版导出报表
	 * 注意：模版和报表都是.docx格式
	 *
	 * @param templateFileInput 模版输入流
	 * @param context           模版上下文数据
	 * @param reportFileOutput  报表输出流
	 */
	public static void exportDocx(InputStream templateFileInput, Map<String, Object> context, OutputStream reportFileOutput) {
		try {
			new WordXParser(templateFileInput, context).getParsedDoc().write(reportFileOutput);
		} catch (IOException e) {
			throw new RuntimeException("io exception");
		}

	}
}
