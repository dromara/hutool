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

package org.dromara.hutool.http.server.engine.undertow;

import io.undertow.Undertow;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.http.server.engine.AbstractServerEngine;

import javax.net.ssl.SSLContext;

/**
 * Undertow引擎实现
 *
 * @author looly
 */
public class UndertowEngine extends AbstractServerEngine {

	private Undertow undertow;

	/**
	 * 构造
	 */
	public UndertowEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(Undertow.class);
	}

	@Override
	public void start() {
		initEngine();
		undertow.start();
	}

	@Override
	public Undertow getRawEngine() {
		return this.undertow;
	}

	@Override
	protected void reset() {
		if(null != this.undertow){
			this.undertow.stop();
			this.undertow = null;
		}
	}

	@Override
	protected void initEngine() {
		if (null != this.undertow) {
			return;
		}
		final Undertow.Builder builder = Undertow.builder()
			.setHandler(exchange -> {
				this.handler.handle(
					new UndertowRequest(exchange),
					new UndertowResponse(exchange));
			});

		final SSLContext sslContext = this.config.getSslContext();
		if(null != sslContext){
			builder.addHttpsListener(this.config.getPort(), this.config.getHost(), sslContext);
		}else{
			builder.addHttpListener(this.config.getPort(), this.config.getHost());
		}
		this.undertow = builder.build();
	}
}
