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
