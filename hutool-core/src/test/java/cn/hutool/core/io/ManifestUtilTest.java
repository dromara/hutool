package cn.hutool.core.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.jar.Manifest;

public class ManifestUtilTest {

	@Test
	public void getManiFestTest(){
		final Manifest manifest = ManifestUtil.getManifest(Test.class);
		assertNotNull(manifest);
	}
}
