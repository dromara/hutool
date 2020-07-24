package cn.hutool.socket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.nio.NioClient;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClientTest {

    @SneakyThrows
    public static void main(String[] args) {
        NioClient client = new NioClient("127.0.0.1", 8080) {
            @SneakyThrows
            @Override
            protected void read(SocketChannel sc) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //从channel读数据到缓冲区
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0){
                    //Flips this buffer.  The limit is set to the current position and then
                    // the position is set to zero，就是表示要从起始位置开始读取数据
                    readBuffer.flip();
                    //eturns the number of elements between the current position and the  limit.
                    // 要读取的字节长度
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //将缓冲区的数据读到bytes数组
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("the read client receive message: " + body);
                    doWrite(sc, body);
                }else if(readBytes < 0){
                    sc.close();
                }
            }
        };
        if (client.waitConnect()) {
            client.listen();
        }
        ByteBuffer buffer = ByteBuffer.wrap("client 发生到 server".getBytes());
        client.write(buffer);
        if(!buffer.hasRemaining()) {
            System.err.println("第一次发送成功...");
        }
        buffer = ByteBuffer.wrap("client 再次发生到 server".getBytes());
        client.write(buffer);
        if(!buffer.hasRemaining()) {
            System.err.println("第二次发送成功...");
        }
    }

    //发送请求
    private static void doWriteRequest(SocketChannel socketChannel) throws Exception{
        System.err.println("start connect...");

        //创建ByteBuffer对象，会放入数据
        ByteBuffer byteBuffer = ByteBuffer.allocate("Hello nio.example.Server!".getBytes().length);
        byteBuffer.put("Hello nio.example.Server!".getBytes());
        byteBuffer.flip();
        //写数据
        socketChannel.write(byteBuffer);
        if(!byteBuffer.hasRemaining()) {
            System.err.println("Send request success...");
        }
    }

    //读取服务端的响应
    private static void doRead(SelectionKey selectionKey) throws Exception{
        SocketChannel socketChannel = ((SocketChannel) selectionKey.channel());
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = socketChannel.read(byteBuffer);
        System.out.println("Recv:" + new String(byteBuffer.array(), 0 ,len));
    }

    public static void doWrite(SocketChannel channel, String response) throws IOException {
        response = "我们已收到消息："+response;
        if(!StrUtil.isBlank(response)){
            byte []  bytes = response.getBytes();
            //分配一个bytes的length长度的ByteBuffer
            ByteBuffer write = ByteBuffer.allocate(bytes.length);
            //将返回数据写入缓冲区
            write.put(bytes);
            write.flip();
            //将缓冲数据写入渠道，返回给客户端
            channel.write(write);
        }
    }
}