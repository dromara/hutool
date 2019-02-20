package cn.hutool.socket.aio;

import java.nio.ByteBuffer;

public interface MsgDecoder<T>{
	T decode(ByteBuffer buffer);
}
