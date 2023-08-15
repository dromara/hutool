/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.ssh;

/**
 * Jsch支持的Channel类型
 *
 * @author looly
 * @since 4.5.2
 */
public enum ChannelType {
	/** Session */
	SESSION("session"),
	/** shell */
	SHELL("shell"),
	/** exec */
	EXEC("exec"),
	/** x11 */
	X11("x11"),
	/** agent forwarding */
	AGENT_FORWARDING("auth-agent@openssh.com"),
	/** direct tcpip */
	DIRECT_TCPIP("direct-tcpip"),
	/** forwarded tcpip */
	FORWARDED_TCPIP("forwarded-tcpip"),
	/** sftp */
	SFTP("sftp"),
	/** subsystem */
	SUBSYSTEM("subsystem");

	/** channel值 */
	private final String value;

	/**
	 * 构造
	 *
	 * @param value 类型值
	 */
	ChannelType(final String value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public String getValue() {
		return this.value;
	}
}
