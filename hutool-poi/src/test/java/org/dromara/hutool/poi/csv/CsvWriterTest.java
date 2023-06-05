package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CsvWriterTest {

	@Test
	@Disabled
	public void writeWithAliasTest(){
		final CsvWriteConfig csvWriteConfig = CsvWriteConfig.defaultConfig()
				.addHeaderAlias("name", "姓名")
				.addHeaderAlias("gender", "性别");

		final CsvWriter writer = CsvUtil.getWriter(
				FileUtil.file("d:/test/csvAliasTest.csv"),
				CharsetUtil.GBK, false, csvWriteConfig);

		writer.writeHeaderLine("name", "gender", "address");
		writer.writeLine("张三", "男", "XX市XX区");
		writer.writeLine("李四", "男", "XX市XX区,01号");
		writer.close();
	}

	@Test
	@Disabled
	public void issue2255Test(){
		final String fileName = "D:/test/" + new Random().nextInt(100) + "-a.csv";
		final CsvWriter writer = CsvUtil.getWriter(fileName, CharsetUtil.UTF_8);
		final List<String> list = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			list.add(String.valueOf(i));
		}
		Console.log("{} : {}", fileName, list.size());
		for (final String s : list) {
			writer.writeLine(s);
		}
		writer.close();
	}

	@Test
	@Disabled
	public void issue3014Test(){
		// https://blog.csdn.net/weixin_42522167/article/details/112241143
		final File tmp = new File("d:/test/dde_safe.csv");
		final CsvWriter writer = CsvUtil.getWriter(tmp, CharsetUtil.UTF_8);
		//设置 dde 安全模式
		writer.setDdeSafe(true);
		writer.write(
			new String[] {"=12+23"},
			new String[] {"%0A=12+23"},
			new String[] {"=;=12+23"},
			new String[] {"-3+2+cmd |' /C calc' !A0"},
			new String[] {"@SUM(cmd|'/c calc'!A0)"}
		);
		writer.close();
	}

	@Test
	@Disabled
	public void writeAppendTest(){
		final CsvWriter writer = CsvUtil.getWriter(
				FileUtil.file("d:/test/writeAppendTest.csv"),
				CharsetUtil.GBK, true);

		writer.writeHeaderLine("name", "gender", "address");
		writer.writeLine("张三", "男", "XX市XX区");
		writer.writeLine("李四", "男", "XX市XX区,01号");

		writer.writeLine("张三2", "男", "XX市XX区");
		writer.writeLine("李四2", "男", "XX市XX区,01号");
		writer.close();
	}
}
