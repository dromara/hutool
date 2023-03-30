package cn.hutool.http.client;

import cn.hutool.http.client.engine.ClientEngineFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientEngineFactoryTest {
	@Test
	public void getTest() {
		final ClientEngine clientEngineFactory = ClientEngineFactory.get();
		Assertions.assertNotNull(clientEngineFactory);
	}
}
