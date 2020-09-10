package cn.hutool.setting.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import cn.hutool.setting.dialect.Props;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
		//noinspection MismatchedQueryAndUpdateOfCollection
		Props props = new Props("test.properties");
		String user = props.getProperty("user");
		Assert.assertEquals(user, "root");

		String driver = props.getStr("driver");
		Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
	}

	@Test
	@Ignore
	public void propTestForAbsPAth() {
		//noinspection MismatchedQueryAndUpdateOfCollection
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

	@Test
	public void toBeanWithNullPrefixTest(){
		Props configProp = new Props();

		configProp.setProperty("createTime", Objects.requireNonNull(DateUtil.parse("2020-01-01")));
		configProp.setProperty("isInit", true);
		configProp.setProperty("stairPlan", 1);
		configProp.setProperty("stageNum", 2);
		configProp.setProperty("version", 3);
		SystemConfig systemConfig = configProp.toBean(SystemConfig.class);

		Assert.assertEquals(DateUtil.parse("2020-01-01"), systemConfig.getCreateTime());
		Assert.assertEquals(true, systemConfig.getIsInit());
		Assert.assertEquals("1", systemConfig.getStairPlan());
		Assert.assertEquals(new Integer(2), systemConfig.getStageNum());
		Assert.assertEquals("3", systemConfig.getVersion());
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

	@Data
	public static class SystemConfig {
		private Boolean isInit;//是否初始化
		private Date createTime;//系统创建时间
		private String version;//系统版本
		private String ServiceOS;//服务器系统
		private String stairPlan;//周期计划 1 自然年周期，2 自然月周期，3 季度周期 4 自定义周期
		private Date startDate;//周期开始日期
		private Date nextStartDate;//下一周期开始日期
		private Integer stageNum;//阶段数目
		private Integer[] stageContent;//阶段详情
		private Date theStageTime;//当前阶段开始日期
		private Date nextStageTime;//当前阶段结束日期/下一阶段开始日期
	}
}
