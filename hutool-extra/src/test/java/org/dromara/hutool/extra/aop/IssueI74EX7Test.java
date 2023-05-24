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

package org.dromara.hutool.extra.aop;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.extra.aop.aspects.SimpleAspect;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class IssueI74EX7Test {

	@Test
	void proxyTest() {
		final SmsBlend smsBlend = new SmsBlendImpl(1);
		ProxyUtil.proxy(smsBlend, new SimpleAspect(){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean before(final Object target, final Method method, final Object[] args) {
				Console.log("切面进入");
				return true;
			}

			@Override
			public boolean after(final Object target, final Method method, final Object[] args, final Object returnVal) {
				Console.log("代理Object："+target.toString());
				Console.log("代理方法："+method.getName());
				Console.log("代理参数："+ Arrays.toString(args));
				return super.after(target, method, args, returnVal);
			}
		});
	}

	public interface SmsBlend{
		void send();
	}

	public static class SmsBlendImpl implements SmsBlend{

		private int status;

		public SmsBlendImpl(final int status) {
			this.status = status;
		}

		@Override
		public void send() {
			Console.log("sms send." + status);
		}
	}
}
