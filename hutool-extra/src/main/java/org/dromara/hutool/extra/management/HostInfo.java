/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.extra.management;

import org.dromara.hutool.core.net.NetUtil;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * 代表当前主机的信息。
 */
public class HostInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String HOST_NAME;
	private final String HOST_ADDRESS;

	public HostInfo() {
		final InetAddress localhost = NetUtil.getLocalhostV4();
		if(null != localhost){
			HOST_NAME = localhost.getHostName();
			HOST_ADDRESS = localhost.getHostAddress();
		} else{
			HOST_NAME = null;
			HOST_ADDRESS = null;
		}
	}

	/**
	 * 取得当前主机的名称。
	 *
	 * <p>
	 * 例如：{@code "webserver1"}
	 * </p>
	 *
	 * @return 主机名
	 */
	public final String getName() {
		return HOST_NAME;
	}

	/**
	 * 取得当前主机的地址。
	 *
	 * <p>
	 * 例如：{@code "192.168.0.1"}
	 * </p>
	 *
	 * @return 主机地址
	 */
	public final String getAddress() {
		return HOST_ADDRESS;
	}

	/**
	 * 将当前主机的信息转换成字符串。
	 *
	 * @return 主机信息的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "Host Name:    ", getName());
		ManagementUtil.append(builder, "Host Address: ", getAddress());

		return builder.toString();
	}

}
