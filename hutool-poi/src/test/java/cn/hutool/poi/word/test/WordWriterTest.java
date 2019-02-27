package cn.hutool.poi.word.test;

import java.awt.Font;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.word.Word07Writer;

public class WordWriterTest {
	
	@Test
	@Ignore
	public void writeTest() {
		Word07Writer writer = new Word07Writer();
		writer.addText(new Font("方正小标宋简体", Font.PLAIN, 22), "我是第一部分", "我是第二部分");
		writer.addText(new Font("宋体", Font.PLAIN, 22), "我是正文第一部分", "我是正文第二部分");
		writer.flush(FileUtil.file("e:/wordWrite.docx"));
		writer.close();
		Console.log("OK");
	}
}
