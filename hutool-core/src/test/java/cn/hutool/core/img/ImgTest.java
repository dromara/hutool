package cn.hutool.core.img;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;

public class ImgTest {
	
	@Test
	@Ignore
	public void cutTest1() {
		Img.from(FileUtil.file("e:/face.jpg")).cut(0, 0, 200).write(FileUtil.file("e:/face_radis.png"));
	}
}
