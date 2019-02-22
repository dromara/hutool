package cn.hutool.socket.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

/**
 * AIO会话<br>
 * 每个客户端对应一个会话对象
 * 
 * @author looly
 *
 */
public class AioSession {

	private AsynchronousSocketChannel channel;
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	private IoAction<ByteBuffer> ioAction;

	/**
	 * 构造
	 * 
	 * @param channel {@link AsynchronousSocketChannel}
	 */
	public AioSession(AsynchronousSocketChannel channel, IoAction<ByteBuffer> ioAction) {
		this.channel = channel;
		this.readBuffer = ByteBuffer.allocate(IoUtil.DEFAULT_BUFFER_SIZE);
		this.writeBuffer = ByteBuffer.allocate(IoUtil.DEFAULT_BUFFER_SIZE);
		this.ioAction = ioAction;
	}

	/**
	 * 获取{@link AsynchronousSocketChannel}
	 * 
	 * @return {@link AsynchronousSocketChannel}
	 */
	public AsynchronousSocketChannel getChannel() {
		return this.channel;
	}

	/**
	 * 获取读取Buffer
	 * 
	 * @return 读取Buffer
	 */
	public ByteBuffer getReadBuffer() {
		return this.readBuffer;
	}

	/**
	 * 获取写Buffer
	 * 
	 * @return 写Buffer
	 */
	public ByteBuffer getWriteBuffer() {
		return this.writeBuffer;
	}

	/**
	 * 获取消息处理器
	 * 
	 * @return {@link IoAction}
	 */
	public IoAction<ByteBuffer> getIoAction() {
		return this.ioAction;
	}

	/**
	 * 读取数据到Buffer
	 * 
	 * @return this
	 */
	public AioSession read() {
		return read(new CompletionHandler<Integer, AioSession>() {

			@Override
			public void completed(Integer result, AioSession session) {
				readBuffer.flip();// 读模式
				ioAction.doAction(session, readBuffer);
			}

			@Override
			public void failed(Throwable exc, AioSession session) {
				ioAction.failed(exc, session);
			}
		});
	}

	/**
	 * 读取数据到Buffer
	 * 
	 * @param handler {@link CompletionHandler}
	 * @return this
	 */
	public AioSession read(CompletionHandler<Integer, AioSession> handler) {
		if (isOpen()) {
			this.readBuffer.clear();
			this.channel.read(this.readBuffer, this, handler);
		}
		return this;
	}

	/**
	 * 写数据到目标端，并关闭输出
	 * 
	 * @return this
	 */
	public AioSession writeAndClose(ByteBuffer data) {
		write(data);
		return closeOut();
	}

	/**
	 * 写数据到目标端
	 * 
	 * @return {@link Future}
	 */
	public Future<Integer> write(ByteBuffer data) {
		return this.channel.write(data);
	}
	
	/**
	 * 写数据到目标端
	 * 
	 * @param handler {@link CompletionHandler}
	 * @return this
	 */
	public AioSession write(ByteBuffer data, CompletionHandler<Integer, AioSession> handler) {
		this.channel.write(data, this, handler);
		return this;
	}

	/**
	 * 会话是否打开状态<br>
	 * 当Socket保持连接时会话始终打开
	 * 
	 * @return 会话是否打开状态
	 */
	public boolean isOpen() {
		return (null == this.channel) ? false : this.channel.isOpen();
	}

	/**
	 * 关闭输出
	 * 
	 * @return this
	 */
	public AioSession closeIn() {
		if (null != this.channel) {
			try {
				this.channel.shutdownInput();
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this;
	}

	/**
	 * 关闭输出
	 * 
	 * @return this
	 */
	public AioSession closeOut() {
		if (null != this.channel) {
			try {
				this.channel.shutdownOutput();
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this;
	}

	/**
	 * 关闭会话
	 * 
	 * @return this
	 */
	public void close() {
		IoUtil.close(this.channel);
	}
}
