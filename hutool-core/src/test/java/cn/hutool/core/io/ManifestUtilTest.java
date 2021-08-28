package cn.hutool.core.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.jar.Manifest;

public class ManifestUtilTest {

	@Test
	public void getManiFestTest(){
		final Manifest manifest = ManifestUtil.getManifest(Test.class);
		Assert.assertNotNull(manifest);
	}
}
