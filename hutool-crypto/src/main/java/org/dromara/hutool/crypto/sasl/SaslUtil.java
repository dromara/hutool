/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.sasl;

import org.dromara.hutool.crypto.CryptoException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import java.util.Map;

/**
 * 解简单认证和安全层（SASL）工具类
 *
 * @author Looly
 * @since 6.0.0
 */
public class SaslUtil {
	/**
	 * 创建一个SASL服务器
	 * 这个方法封装了Sasl服务器的创建过程，提供一个统一的方式来初始化SASL服务器实例
	 *
	 * @param mechanism  身份验证机制
	 * @param protocol   应用程序协议，例如"SCRAM-SHA-256"
	 * @param serverName 服务器名称，用于身份验证
	 * @param props      与机制相关的属性，可以是任何相关的配置参数
	 * @param cbh        回调处理器，用于身份验证过程中的用户交互
	 * @return {@link SaslServer}
	 * @throws CryptoException 如果在创建SASL服务器过程中发生错误
	 */
	public static SaslServer createServer(final String mechanism,
										  final String protocol,
										  final String serverName,
										  final Map<String, ?> props,
										  final CallbackHandler cbh) {
		try {
			return Sasl.createSaslServer(mechanism, protocol, serverName, props, cbh);
		} catch (final SaslException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 创建SASL客户端<br>
	 * 它封装了{@code Sasl.createSaslClient}方法，提供了一层抽象，以便更方便地处理SASL客户端的创建
	 *
	 * @param mechanisms      尝试多个身份验证机制的名称
	 * @param authorizationId 授权ID，用于代表用户进行身份验证
	 * @param protocol        此SASL客户端所涉及的协议
	 * @param serverName      服务器的名称
	 * @param props           额外的属性，用于身份验证过程
	 * @param cbh             回调处理器，用于处理身份验证过程中的交互
	 * @return 返回创建的SASL客户端实例
	 * @throws CryptoException 如果SASL客户端创建过程中发生异常
	 */
	public static SaslClient createClient(final String[] mechanisms,
										  final String authorizationId,
										  final String protocol,
										  final String serverName,
										  final Map<String, ?> props,
										  final CallbackHandler cbh) {
		try {
			return Sasl.createSaslClient(mechanisms, authorizationId, protocol, serverName, props, cbh);
		} catch (final SaslException e) {
			throw new CryptoException(e);
		}
	}
}
