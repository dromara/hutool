package org.dromara.hutool.ssh;

import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.lang.Console;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Jsch工具类单元测试
 *
 * @author looly
 *
 */
public class JschUtilTest {

	@Test
	@Disabled
	public void bindPortTest() {
		//新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
		final Session session = JschUtil.getSession("looly.centos", 22, "test", "123456");
		// 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
		JschUtil.bindPort(session, "172.20.12.123", 8080, 8080);
	}


	@Test
	@Disabled
	public void bindRemotePort() {
		// 建立会话
		final Session session = JschUtil.getSession("looly.centos", 22, "test", "123456");
		// 绑定ssh服务端8089端口到本机的8000端口上
		final boolean b = JschUtil.bindRemotePort(session, 8089, "localhost", 8000);
		Assertions.assertTrue(b);
		// 保证一直运行
	}

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void sftpTest() {
		final Session session = JschUtil.getSession("looly.centos", 22, "root", "123456");
		final Sftp sftp = JschUtil.createSftp(session);
		sftp.mkDirs("/opt/test/aaa/bbb");
		Console.log("OK");
	}

	@Test
	@Disabled
	public void reconnectIfTimeoutTest() throws InterruptedException {
		final Session session = JschUtil.getSession("sunnyserver", 22,"mysftp","liuyang1234");
		final Sftp sftp = JschUtil.createSftp(session);

		Console.log("打印pwd: " + sftp.pwd());
		Console.log("cd / : " + sftp.cd("/"));
		Console.log("休眠一段时间，查看是否超时");
		Thread.sleep(30 * 1000);

		try{
			// 当连接超时时，isConnected()仍然返回true，pwd命令也能正常返回，因此，利用发送cd命令的返回结果，来判断是否连接超时
			Console.log("isConnected " + sftp.getClient().isConnected());
			Console.log("打印pwd: " + sftp.pwd());
			Console.log("cd / : " + sftp.cd("/"));
		}catch (final JschRuntimeException e) {
			e.printStackTrace();
		}

		Console.log("调用reconnectIfTimeout方法，判断是否超时并重连");
		sftp.reconnectIfTimeout();

		Console.log("打印pwd: " + sftp.pwd());

		IoUtil.closeQuietly(sftp);
	}

	@Test
	@Disabled
	public void getSessionTest(){
		JschUtil.getSession("192.168.1.134", 22, "root", "aaa", null);
	}
}
