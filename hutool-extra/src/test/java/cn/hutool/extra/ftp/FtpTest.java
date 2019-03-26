package cn.hutool.extra.ftp;

import org.apache.velocity.texen.util.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;

public class FtpTest {
	
	@Test
	@Ignore
	public void uploadTest() {
		Ftp ftp = new Ftp("looly.centos");
		
		boolean upload = ftp.upload("/file/", FileUtil.file("E:/qrcodeWithLogo.jpg"));
		
		Console.log(upload);
		
		IoUtil.close(ftp);
	}
}
