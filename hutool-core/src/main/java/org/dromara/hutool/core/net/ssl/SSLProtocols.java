/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.net.ssl;

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
