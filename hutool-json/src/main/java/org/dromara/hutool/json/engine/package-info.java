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

/**
 * JSON SPI 实现<br>
 * 通过实现{@link org.dromara.hutool.json.engine.JSONEngine} 以使用不同的JSON库完成：
 * <ul>
 *     <li>JSON序列化，即Java Bean（POJO）对象转为JSON字符串</li>
 *     <li>JSON反序列化，即JSON字符串转为Java Bean（POJO）对象</li>
 * </ul>
 */
package org.dromara.hutool.json.engine;
