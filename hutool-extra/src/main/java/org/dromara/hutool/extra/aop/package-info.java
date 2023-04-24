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

/**
 * JDK动态代理封装，提供非IOC下的切面支持，封装包括：
 * <ul>
 *     <li>基于{@link java.lang.reflect.Proxy}代理</li>
 *     <li>基于Spring-cglib代理</li>
 * </ul>
 * 考虑到cglib库不再更新且对JDK9+兼容性问题，不再封装
 * <pre>
 *                            createEngine               proxy
 *        ProxyEngineFactory       =》      ProxyEngine    =》  Proxy
 * </pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.extra.aop;
