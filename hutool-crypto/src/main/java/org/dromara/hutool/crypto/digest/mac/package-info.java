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

/**
 * <p>
 * MAC，全称为“Message Authentication Code”，中文名“消息鉴别码”。
 * </p>
 *
 * <p>
 * HMAC，全称为“Hash Message Authentication Code”，中文名“散列消息鉴别码”<br>
 * 主要是利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。<br>
 * 一般的，消息鉴别码用于验证传输于两个共 同享有一个密钥的单位之间的消息。<br>
 * HMAC 可以与任何迭代散列函数捆绑使用。MD5 和 SHA-1 就是这种散列函数。HMAC 还可以使用一个用于计算和确认消息鉴别值的密钥。
 * </p>
 *
 * <pre>{@code
 *         MacEngineFactory
 *               ||(创建)
 *           MacEngine----------------（包装）-----------------> Mac
 *          _____|_______________                                |
 *         /                     \                              HMac
 *  JCEMacEngine             BCMacEngine
 *                            /        \
 *                   BCHMacEngine  CBCBlockCipherMacEngine
 *                                          |
 *                                     SM4MacEngine
 * }</pre>
 *
 * 通过MacEngine，封装支持了BouncyCastle和JCE实现的一些MAC算法，通过MacEngineFactory自动根据算法名称创建对应对象。
 *
 * @author Looly
 * @since 4.5.13
 */
package org.dromara.hutool.crypto.digest.mac;
