package cn.hutool.setting.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import cn.hutool.setting.dialect.Props;
import lombok.Data;

/**
 * Setting单元测试
 * 
 * @author Looly
 *
 */
public class PropsTest {

	@Before
	public void init() {
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
	}

	@Test
	public void propTest() {
		Props props = new Props("test.properties");
		String user = props.getProperty("user");
		Assert.assertEquals(user, "root");

		String driver = props.getStr("driver");
		Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
	}

	@Test
	@Ignore
	public void propTestForAbsPAth() {
		Props props = new Props("d:/test.properties");
		String user = props.getProperty("user");
		Assert.assertEquals(user, "root");

		String driver = props.getStr("driver");
		Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
	}
	
	@Test
	public void toBeanTest() {
		Props props = Props.getProp("to_bean_test.properties");
		
		ConfigProperties cfg = props.toBean(ConfigProperties.class, "mail");
		Assert.assertEquals("mailer@mail.com", cfg.getHost());
		Assert.assertEquals(9000, cfg.getPort());
		Assert.assertEquals("mailer@mail.com", cfg.getFrom());
		
		Assert.assertEquals("john", cfg.getCredentials().getUsername());
		Assert.assertEquals("password", cfg.getCredentials().getPassword());
		Assert.assertEquals("SHA1", cfg.getCredentials().getAuthMethod());
		
		Assert.assertEquals("true", cfg.getAdditionalHeaders().get("redelivery"));
		Assert.assertEquals("true", cfg.getAdditionalHeaders().get("secure"));
		
		Assert.assertEquals("admin@mail.com", cfg.getDefaultRecipients().get(0));
		Assert.assertEquals("owner@mail.com", cfg.getDefaultRecipients().get(1));
	}

	@Data
	public static class ConfigProperties {
		private String host;
		private int port;
		private String from;
		private Credentials credentials;
		private List<String> defaultRecipients;
		private Map<String, String> additionalHeaders;
	}
	
	@Data
	public static class Credentials {
		private String authMethod;
		private String username;
		private String password;
	}
}
