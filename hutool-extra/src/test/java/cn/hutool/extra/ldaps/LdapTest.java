package cn.hutool.extra.ldap;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.ldaps.Ldap;
import cn.hutool.extra.ldaps.LdapConfig;
import org.junit.Ignore;
import org.junit.Test;

public class LdapTest {

	@Test
	@Ignore
	public void ldapTest() {
		LdapConfig conf = new LdapConfig();
		conf.setDomain("@ldaps.hundsun.com.cn");
		conf.setHost("ldaps.hundsun.com.cn");
		conf.setPort(636);
		conf.setProtocol("ldaps://");
		conf.setUser("zhaosheng");
		conf.setPassword("Sun@@123456");
		conf.setKeystore("/home/root/zookeeper/JRE/jre1.8.0_181/lib/security/cacerts");
		Ldap ldap = new Ldap(conf);
		boolean isSuccess = ldap.init();
		Console.log(isSuccess);
	}
}
