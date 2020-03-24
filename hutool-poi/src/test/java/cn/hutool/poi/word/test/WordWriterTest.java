package cn.hutool.poi.word.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.word.Word07Writer;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Font;
import java.io.File;

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

	@Test
	@Ignore
	public void writePicTest() {
		Word07Writer writer = new Word07Writer();
		writer.addPicture(new File("d:\\test\\qrcodeCustom.jpg"), 100, 200);
		// 写出到文件
		writer.flush(FileUtil.file("d:/test/writePic.docx"));
		// 关闭
		writer.close();
	}
}
