package org.dromara.hutool.setting;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.setting.props.Props;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Props单元测试
 *
 * @author Looly
 *
 */
public class PropsTest {

	@Test
	public void propTest() {
		//noinspection MismatchedQueryAndUpdateOfCollection
		final Props props = new Props("test.properties");
		final String user = props.getProperty("user");
		Assertions.assertEquals(user, "root");

		final String driver = props.getStr("driver");
		Assertions.assertEquals(driver, "com.mysql.jdbc.Driver");
	}

	@Test
	@Disabled
	public void propTestForAbsPAth() {
		//noinspection MismatchedQueryAndUpdateOfCollection
		final Props props = new Props("d:/test.properties");
		final String user = props.getProperty("user");
		Assertions.assertEquals(user, "root");

		final String driver = props.getStr("driver");
		Assertions.assertEquals(driver, "com.mysql.jdbc.Driver");
	}

	@Test
	public void toBeanTest() {
		final Props props = Props.of("to_bean_test.properties");

		final ConfigProperties cfg = props.toBean(ConfigProperties.class, "mail");
		Assertions.assertEquals("mailer@mail.com", cfg.getHost());
		Assertions.assertEquals(9000, cfg.getPort());
		Assertions.assertEquals("mailer@mail.com", cfg.getFrom());

		Assertions.assertEquals("john", cfg.getCredentials().getUsername());
		Assertions.assertEquals("password", cfg.getCredentials().getPassword());
		Assertions.assertEquals("SHA1", cfg.getCredentials().getAuthMethod());

		Assertions.assertEquals("true", cfg.getAdditionalHeaders().get("redelivery"));
		Assertions.assertEquals("true", cfg.getAdditionalHeaders().get("secure"));

		Assertions.assertEquals("admin@mail.com", cfg.getDefaultRecipients().get(0));
		Assertions.assertEquals("owner@mail.com", cfg.getDefaultRecipients().get(1));
	}

	@Test
	public void toBeanWithNullPrefixTest(){
		final Props configProp = new Props();

		configProp.set("createTime", Objects.requireNonNull(DateUtil.parse("2020-01-01")));
		configProp.set("isInit", true);
		configProp.set("stairPlan", 1);
		configProp.set("stageNum", 2);
		configProp.set("version", 3);
		final SystemConfig systemConfig = configProp.toBean(SystemConfig.class);

		Assertions.assertEquals(DateUtil.parse("2020-01-01"), systemConfig.getCreateTime());
		Assertions.assertEquals(true, systemConfig.getIsInit());
		Assertions.assertEquals("1", systemConfig.getStairPlan());
		Assertions.assertEquals(new Integer(2), systemConfig.getStageNum());
		Assertions.assertEquals("3", systemConfig.getVersion());
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
