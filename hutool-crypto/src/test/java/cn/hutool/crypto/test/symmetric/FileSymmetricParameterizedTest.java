package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件对称加密算法单元测试
 *
 * @author vincentruan
 */
@RunWith(Parameterized.class)
public class FileSymmetricParameterizedTest {

	private final SymmetricCrypto symmetricCrypto;

	private final static String hugeFilePath = "F:\\dm\\huge\\huge-data.data";

	private final static StopWatch stopWatch = new StopWatch();

	public FileSymmetricParameterizedTest(SymmetricCrypto symmetricCrypto) {
		this.symmetricCrypto = symmetricCrypto;
	}

	@BeforeClass
	public static void createHugeFile() throws IOException {
		// write a huge file to disk
		try (OutputStream out = FileUtil.getOutputStream(hugeFilePath)) {
			for (int i = 0; i < 50_000; i++) {
				out.write(RandomUtil.randomBytes(IoUtil.DEFAULT_LARGE_BUFFER_SIZE));
			}
			out.flush();
		}
		System.out.println("源文件大小[" + FileUtil.readableFileSize(new File(hugeFilePath)) + "]");
	}

	@AfterClass
	public static void delHugeFile() throws IOException {
		FileUtil.del(hugeFilePath);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> prepareData() {
		final SecureRandom random = RandomUtil.getSecureRandom("QWEqwertyuio!@#$%^&*1234567DFGHJcvbnzxcvbnm,,../.l".getBytes());
		// 测试数据
		Set<Class<?>> subClassSet = ClassScanner.scanPackageBySuper("cn.hutool.crypto.symmetric", SymmetricCrypto.class);
		// 将数组转换成集合返回
		return subClassSet.stream().map(ReflectUtil::newInstanceIfPossible).filter(Objects::nonNull).map(c -> new Object[]{c}).collect(Collectors.toList());
	}

	/**
	 * 性能测试 （312.5 MB大小的文件，JVM 256MB，CPU 4核E3），对等加解密的四套算法中，AES最快，DESede最慢
	 * DESede加密后文件大小[1.53 GB]
	 * StopWatch 'DESede': running time = 135794673000 ns
	 * ---------------------------------------------
	 * ns         %     Task name
	 * ---------------------------------------------
	 * 67976797200  050%  DESede加密
	 * 67817875800  050%  DESede解密
	 *
	 * AES加密后文件大小[1.53 GB]
	 * StopWatch 'AES': running time = 28730252100 ns
	 * ---------------------------------------------
	 * ns         %     Task name
	 * ---------------------------------------------
	 * 12801193000  045%  AES加密
	 * 15929059100  055%  AES解密
	 *
	 * DES加密后文件大小[1.53 GB]
	 * StopWatch 'DES': running time = 50617259100 ns
	 * ---------------------------------------------
	 * ns         %     Task name
	 * ---------------------------------------------
	 * 25141611700  050%  DES加密
	 * 25475647400  050%  DES解密
	 *
	 * SM4加密后文件大小[1.53 GB]
	 * StopWatch 'SM4': running time = 49606409800 ns
	 * ---------------------------------------------
	 * ns         %     Task name
	 * ---------------------------------------------
	 * 23948870200  048%  SM4加密
	 * 25657539600  052%  SM4解密
	 * @throws IOException
	 */
	@Test
	public void testEND() throws IOException {
		String shortClassName = symmetricCrypto.getClass().getSimpleName();
		File originalFile = new File(hugeFilePath);
		String originalMd5 = SecureUtil.md5(originalFile);

		Path encryptedFilePath = Paths.get(originalFile.getParent(), originalFile.getName() + "." + shortClassName + ".enc");
		Path decryptedFilePath = Paths.get(originalFile.getParent(), originalFile.getName() + "." + shortClassName + ".dec");

		try (BufferedInputStream bis = FileUtil.getInputStream(originalFile)) {
			stopWatch.start(shortClassName + "加密");
			File encryptedFile = symmetricCrypto.encrypt(bis, encryptedFilePath);
			stopWatch.stop();
			System.out.println(shortClassName + "加密后文件大小[" + FileUtil.readableFileSize(encryptedFile) + "]");
			try (BufferedInputStream encBis = FileUtil.getInputStream(encryptedFile)) {
				stopWatch.start(shortClassName + "解密");
				File decryptedFile = symmetricCrypto.decrypt(encBis, decryptedFilePath);
				stopWatch.stop();
				String decryptedMd5 = new MD5().digestHex(decryptedFile);
				System.out.println(stopWatch.prettyPrint());
				Assert.assertEquals(shortClassName + " md5 check failed.", originalMd5, decryptedMd5);
			}
		} finally {
			FileUtil.delQuietly(encryptedFilePath);
			FileUtil.delQuietly(decryptedFilePath);
		}
	}

}
