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
	
	@Test
	@Ignore
	public void reconnectIfTimeoutTest() throws InterruptedException {
		Ftp ftp = new Ftp("localhost");

		Console.log("打印pwd: " + ftp.pwd());

		Console.log("休眠一段时间，然后再次发送pwd命令，抛出异常表明连接超时");
		Thread.sleep(35 * 1000);

		try{
			Console.log("打印pwd: " + ftp.pwd());
		}catch (FtpException e) {
			e.printStackTrace();
		}

		Console.log("判断是否超时并重连...");
		ftp.reconnectIfTimeout();

		Console.log("打印pwd: " + ftp.pwd());

		IoUtil.close(ftp);
	}

	@Test
	@Ignore
	public void initTest() {
		Ftp ftp = new Ftp("localhost",21,"vpclub-jinan-008","5634156",Ftp.DEFAULT_CHARSET,FtpMode.Passive);
		Console.log("打印pwd: " + ftp.pwd());
	}
}
