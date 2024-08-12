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

package org.dromara.hutool.socket.protocol;

/**
 * 协议接口<br>
 * 通过实现此接口完成消息的编码和解码
 *
 * <p>
 * 所有Socket使用相同协议对象，类成员变量和对象成员变量易造成并发读写问题。
 * </p>
 *
 * @author Looly
 */
public interface Protocol<T> extends MsgEncoder<T>, MsgDecoder<T> {

}
