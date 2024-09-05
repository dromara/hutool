/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.aop;

import lombok.Setter;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.aop.aspects.SimpleAspect;
import org.dromara.hutool.extra.aop.engine.ProxyEngine;
import org.dromara.hutool.extra.aop.engine.jdk.JdkProxyEngine;
import org.dromara.hutool.extra.aop.engine.spring.SpringCglibProxyEngine;
import org.junit.jupiter.api.Test;

public class IssueI74EX7Test {

	@Test
	void proxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		final ProxyEngine engine = new JdkProxyEngine();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	/**
	 * https://gitee.com/dromara/hutool/issues/I74EX7<br>
	 * Enhancer.create()默认调用无参构造，有参构造或者多个构造没有很好的兼容。
	 *
	 */
	@Test
	void cglibProxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		final ProxyEngine engine = new SpringCglibProxyEngine();
		engine.proxy(smsBlend, new SimpleAspect());
	}

	@Test
	void cglibProxyWithoutConstructorTest() {
		final SmsBlend smsBlend = new SmsBlendImplWithoutConstructor();
		final ProxyEngine engine = new SpringCglibProxyEngine();
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
