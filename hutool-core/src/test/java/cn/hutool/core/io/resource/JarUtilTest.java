package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class JarUtilTest {


	@Test
	public void getJarDir() {
		String jarDir = JarUtil.getDir(this.getClass());
		System.out.println(jarDir);
	}

	@Test
	public void readResource() throws IOException {
		List<String> jarResource = JarUtil.readUtf8Lines(this.getClass(), "test.xml");
		Assert.assertNotNull(jarResource);

		URL jarUrl = JarUtil.getJarUrl(this.getClass(), "test.properties");
		String readString = FileUtil.readString(jarUrl, CharsetUtil.defaultCharset());
		Assert.assertNotNull(readString);
	}

	@Test
	public void readJarResource() throws IOException {
		List<String> jarResource = JarUtil.readUtf8Lines(Assert.class, "META-INF/MANIFEST.MF");
		Assert.assertNotNull(jarResource);
		jarResource = JarUtil.readUtf8Lines(Assert.class, "org/junit/Test.class");
		Assert.assertNotNull(jarResource);
	}

}
