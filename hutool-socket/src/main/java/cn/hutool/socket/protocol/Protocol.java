/*
 * Copyright (c) 2017, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: Protocol.java
 * Date: 2017-11-25
 * Author: sandao
 */

package cn.hutool.socket.protocol;

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
