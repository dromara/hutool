package cn.hutool.http.client;

import cn.hutool.http.client.engine.ClientEngineFactory;
import org.junit.Assert;
import org.junit.Test;

public class ClientEngineFactoryTest {
	@Test
	public void getTest() {
		final ClientEngine clientEngineFactory = ClientEngineFactory.get();
		Assert.assertNotNull(clientEngineFactory);
	}
}
