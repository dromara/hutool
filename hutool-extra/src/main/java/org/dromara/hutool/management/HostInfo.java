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

package org.dromara.hutool.management;

import org.dromara.hutool.net.NetUtil;

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
		final InetAddress localhost = NetUtil.getLocalhost();
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
