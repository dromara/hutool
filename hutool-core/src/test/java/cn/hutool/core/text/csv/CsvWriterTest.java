package cn.hutool.core.text.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CsvWriterTest {

	@Test
	@Ignore
	public void writeWithAliasTest(){
		final CsvWriteConfig csvWriteConfig = CsvWriteConfig.defaultConfig()
				.addHeaderAlias("name", "姓名")
				.addHeaderAlias("gender", "性别");

		final CsvWriter writer = CsvUtil.getWriter(
				FileUtil.file("d:/test/csvAliasTest.csv"),
				CharsetUtil.CHARSET_GBK, false, csvWriteConfig);

		writer.writeHeaderLine("name", "gender", "address");
		writer.writeLine("张三", "男", "XX市XX区");
		writer.writeLine("李四", "男", "XX市XX区,01号");
		writer.close();
	}

	@Test
	@Ignore
	public void issue2255Test(){
		String fileName = "D:/test/" + new Random().nextInt(100) + "-a.csv";
		CsvWriter writer = CsvUtil.getWriter(fileName, CharsetUtil.CHARSET_UTF_8);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			list.add(i+"");
		}
		Console.log("{} : {}", fileName, list.size());
		for (String s : list) {
			writer.writeLine(s);
		}
		writer.close();
	}
}
