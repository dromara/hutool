package cn.hutool.core.img;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;

public class ImgTest {
	
	@Test
	@Ignore
	public void cutTest1() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).cut(0, 0, 200).write(FileUtil.file("e:/pic/face_radis.png"));
	}
	
	@Test
	@Ignore
	public void compressTest() {
		Img.from(FileUtil.file("f:/test/4347273249269e3fb272341acc42d4e.jpg")).setQuality(0.8).write(FileUtil.file("f:/test/test_dest.jpg"));
	}
	
	@Test
	@Ignore
	public void roundTest() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).round(0.5).write(FileUtil.file("e:/pic/face_round.png"));
	}
}
