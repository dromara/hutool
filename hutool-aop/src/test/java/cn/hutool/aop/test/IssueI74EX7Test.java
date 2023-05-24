/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.aop.test;

import cn.hutool.aop.aspects.SimpleAspect;
import cn.hutool.aop.proxy.CglibProxyFactory;
import cn.hutool.aop.proxy.JdkProxyFactory;
import cn.hutool.aop.proxy.ProxyFactory;
import cn.hutool.aop.proxy.SpringCglibProxyFactory;
import cn.hutool.core.lang.Console;
import lombok.Setter;
import org.junit.Test;

public class IssueI74EX7Test {
	@Test
	public void proxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		final ProxyFactory engine = new JdkProxyFactory();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	/**
	 * https://gitee.com/dromara/hutool/issues/I74EX7<br>
	 * Enhancer.create()默认调用无参构造，有参构造或者多个构造没有很好的兼容。
	 *
	 */
	@Test
	public void cglibProxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		final ProxyFactory engine = new CglibProxyFactory();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	/**
	 * https://gitee.com/dromara/hutool/issues/I74EX7<br>
	 * Enhancer.create()默认调用无参构造，有参构造或者多个构造没有很好的兼容。
	 *
	 */
	@Test
	public void springCglibProxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		final ProxyFactory engine = new SpringCglibProxyFactory();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	@Test
	public void springCglibProxyWithoutConstructorTest() {
		final SmsBlend smsBlend = new SmsBlendImplWithoutConstructor();
		final ProxyFactory engine = new SpringCglibProxyFactory();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	public interface SmsBlend{
		void send();
	}

	public static class SmsBlendImpl implements SmsBlend{

		private final int status;

		public SmsBlendImpl(final int status) {
			this.status = status;
		}

		@Override
		public void send() {
			Console.log("sms send." + status);
		}
	}

	@Setter
	public static class SmsBlendImplWithoutConstructor implements SmsBlend{

		private int status;

		@Override
		public void send() {
			Console.log("sms send." + status);
		}
	}
}
