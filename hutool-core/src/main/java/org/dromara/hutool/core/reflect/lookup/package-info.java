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
 * {@link java.lang.invoke.MethodHandles.Lookup} 创建封装<br>
 * <p>
 * jdk8中如果直接调用{@link java.lang.invoke.MethodHandles#lookup()}获取到的{@link java.lang.invoke.MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 * </p>
 *
 * <p>
 * 参考：
 * <p><a href="https://blog.csdn.net/u013202238/article/details/108687086">https://blog.csdn.net/u013202238/article/details/108687086</a></p>
 */
package org.dromara.hutool.core.reflect.lookup;
