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

package cn.hutool.core.net.ssl;

/**
 * SSL或TLS协议
 *
 * @author looly
 * @since 5.7.8
 */
public interface SSLProtocols {

	/**
	 * Supports some version of SSL; may support other versions
	 */
	String SSL = "SSL";
	/**
	 * Supports SSL version 2 or later; may support other versions
	 */
	String SSLv2 = "SSLv2";
	/**
	 * Supports SSL version 3; may support other versions
	 */
	String SSLv3 = "SSLv3";

	/**
	 * Supports some version of TLS; may support other versions
	 */
	String TLS = "TLS";
	/**
	 * Supports RFC 2246: TLS version 1.0 ; may support other versions
	 */
	String TLSv1 = "TLSv1";
	/**
	 * Supports RFC 4346: TLS version 1.1 ; may support other versions
	 */
	String TLSv11 = "TLSv1.1";
	/**
	 * Supports RFC 5246: TLS version 1.2 ; may support other versions
	 */
	String TLSv12 = "TLSv1.2";
}
