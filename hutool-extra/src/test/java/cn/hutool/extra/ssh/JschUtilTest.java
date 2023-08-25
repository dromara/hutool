package cn.hutool.extra.ssh;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import com.jcraft.jsch.Session;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Jsch工具类单元测试
 *
 * @author looly
 *
 */
public class JschUtilTest {

	@Test
	@Ignore
	public void bindPortTest() {
		//新建会话，此会话用于ssh连接到跳板机（堡垒机），此处为10.1.1.1:22
		Session session = JschUtil.getSession("looly.centos", 22, "test", "123456");
		// 将堡垒机保护的内网8080端口映射到localhost，我们就可以通过访问http://localhost:8080/访问内网服务了
		JschUtil.bindPort(session, "172.20.12.123", 8080, 8080);
	}


	@Test
	@Ignore
	public void bindRemotePort() throws InterruptedException {
		// 建立会话
		Session session = JschUtil.getSession("looly.centos", 22, "test", "123456");
		// 绑定ssh服务端8089端口到本机的8000端口上
		boolean b = JschUtil.bindRemotePort(session, 8089, "localhost", 8000);
		Assert.assertTrue(b);
		// 保证一直运行
//		while (true){
//			Thread.sleep(3000);
//		}
	}

	@Test
	@Ignore
	public void sftpTest() {
		Session session = JschUtil.getSession("looly.centos", 22, "root", "123456");
		Sftp sftp = JschUtil.createSftp(session);
		sftp.mkDirs("/opt/test/aaa/bbb");
		Console.log("OK");
	}

	@Test
	@Ignore
	public void reconnectIfTimeoutTest() throws InterruptedException {
		Session session = JschUtil.getSession("sunnyserver", 22,"mysftp","liuyang1234");
		Sftp sftp = JschUtil.createSftp(session);

		Console.log("打印pwd: " + sftp.pwd());
		Console.log("cd / : " + sftp.cd("/"));
		Console.log("休眠一段时间，查看是否超时");
		Thread.sleep(30 * 1000);

		try{
			// 当连接超时时，isConnected()仍然返回true，pwd命令也能正常返回，因此，利用发送cd命令的返回结果，来判断是否连接超时
			Console.log("isConnected " + sftp.getClient().isConnected());
			Console.log("打印pwd: " + sftp.pwd());
			Console.log("cd / : " + sftp.cd("/"));
		}catch (JschRuntimeException e) {
			e.printStackTrace();
		}

		Console.log("调用reconnectIfTimeout方法，判断是否超时并重连");
		sftp.reconnectIfTimeout();

		Console.log("打印pwd: " + sftp.pwd());

		IoUtil.close(sftp);
	}

	@Test
	@Ignore
	public void getSessionTest(){
		JschUtil.getSession("192.168.1.134", 22, "root", "aaa", null);
	}
	@Test
	@Ignore
	public void sftpPrivateKeyTest(){
		Session session = JschUtil.getSession("192.168.1.109", 22, "root", ("-----BEGIN RSA PRIVATE KEY-----\n" +
			"MIIEpAIBAAKCAQEA5SJ1bjhSA0uQJjbbF/LCFiQvs+nMKgkSnSE+JEll7azv7jnh\n" +
			"oBEJdg63tf66oDXCDCMdrYbTtenw1TqkQI2PO8sHuvAZ2UUjqk5zlcrWLiNTCWBw\n" +
			"IgIxbVj3/nQliaZSufLxepf8qr6wXWP/PG6p+ScFhTSGjK2Z4r/t9cqPaTtfYJye\n" +
			"lbXNsrn0JK99XGj3cNvHzljAHRUCwGRTiHAOJ7Gk9WLGcZQRKuMPrQFMnoLoxZ8m\n" +
			"0y+1+AoND6pKLad9/52JCHBi5d7XgwPPEKxVqi8Aklpgb45G4A3FXSKx3tkolXAP\n" +
			"3zGm95CwmqdI9Q8t72SkOWDJR4lFPb12k6H0FwIDAQABAoIBAQCTeO4jllQSktuf\n" +
			"/MZeT3vjTD73iI5Cr7wvLWoVaLgVlKyHovE4WD7CoQ5UMDJlUrQlo6RCPvibqIm8\n" +
			"cxWsBnAdh7rd8hJw6DLgNcXmrrnS0CFtc4g4Gzk8q3pRZueSBF5SF66bvJ5+NmTE\n" +
			"dsubVY5IMXk4FmpSuJjGe8jn3QsYKkptOa/s28UekaWzqnstIx42IgS33w7qpUx9\n" +
			"v0XKoOCj41HxwGYexNmOiIufh6dEzZtNZGQb+f7JiIpbClpCXO0Dfi+jkNOBjI6Z\n" +
			"VMLxIdAZpb9Q97g03hWxH+ZQ9UBeYc9n4p2UzKMnXMBM0lu/cx++hyu6OfAxL8Yr\n" +
			"eeZEMIrBAoGBAPslHABM0tCf2UC0vs0mfQFqQiO+Mu9Lp6wGbtvNPt+aNdw+ia+g\n" +
			"CPcvBWdpMKq6eg8XEwLZ59YyoBDlipCG4h/5Xq4wyf77ydamy9mdH6nOcuUDy9KH\n" +
			"07O39wDdU8EH9Jq29lUYIQgkakRGpi45unq10eo8zAy0ggN1hgI1tYwpAoGBAOmQ\n" +
			"bB8hj1e1ozrrMWSv/xIntmRGswR+RwLCJECJy/Oai3SvdB9L1Rh1SUOD6Q/vakXd\n" +
			"pYAzEkEAxm6h+YecxhlwiOFi8rLAWYMLu31LH7WGGVM7SvwLxB/YmL/hpZ0Jqrc6\n" +
			"Au8MoQtFDtP7IntRkC70Sx7GoKqKudFJlnzyv4Y/AoGAHImn9+TC48/2KOMg90DT\n" +
			"XZDMeTFIqmZnZCXK/RECfvgP/LnifWFrA2OFcq3CSPQtoH9XurA2JuHTzHe42hlp\n" +
			"ooZ8msCSg3XrBogni8/N5EbASYO36nFivf4+hAuiU8HqqpX1wc+fGUTCCoYYphIL\n" +
			"PZxhgQNtkFgGmgwFsUSXH5kCgYEAtvxoSSeQ1yW+Qb3cD8d3LjEgy4U8YavRVI7n\n" +
			"ugx7VlphIcUIVDCkPio9gQDKyqpG93/EVyEsDvNdg3WxOpcP+QRaqUJNZNAgEPRT\n" +
			"KsF9kUkDdFsCz18kg9K9Ma/Ggbb+Idj4TXL2hQ7QpDGf/T+Ul8TbSbxSSeqv1BE0\n" +
			"LqY8eR8CgYBqzKpZcqnNuuwZgdLEnZ/rQoeubw4cHoMWBOkP0N/+mgcE/eEpkr0n\n" +
			"9llf+wJg96adgqhwlEHkOMQdsF+FfQA5yJWW4FHA8bfA9YsxjBgheL2RU1Z2iTp4\n" +
			"aLmoAsh17p8MGk/3Zfh10t3tq4c67WlFS2kX2qPBXuwDnm51iNyp2A==\n" +
			"-----END RSA PRIVATE KEY-----\n").getBytes(StandardCharsets.UTF_8), null);
		Sftp sftp = JschUtil.createSftp(session);
		sftp.mkDirs("/opt/test/aaa1/bbb1");
		Console.log("OK");
	}
}
