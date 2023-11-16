package org.dromara.hutool.core.lang;



import org.dromara.hutool.core.text.StrUtil;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author wh
 *
 */
public class ByteString {

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final ByteString EMPTY = new ByteString(EMPTY_BYTE_ARRAY);
    // END EXTRA
    private final byte[] bytes;
    private volatile int hash = 0;


    private ByteString( final byte[] bytes ) {
        this.bytes = bytes;
    }

    // START EXTRA
    // internal package access to avoid double memory allocation
    static ByteString wrap( byte[] bytes ) {
        return new ByteString(bytes);
    }

    /**
     * 将 bytes 写回
     * {@link OutputStream}.
     *
     * @param out 输出流
     * @param bs  输入的bytes
     * @throws IOException 异常
     */
    public static void writeTo( OutputStream out, ByteString bs ) throws IOException {
        out.write(bs.bytes);
    }

    /**
     * 将 bytes 写回
     * {@link DataOutput}.
     *
     * @param out 输出流
     * @param bs  输入的bytes
     * @throws IOException 异常
     */
    public static void writeTo( DataOutput out, ByteString bs ) throws IOException {
        out.write(bs.bytes);
    }

    /**
     * 拷贝 部分内容并生成新的 ByteString
     *
     * @param bytes  数组
     * @param offset 偏移量
     * @param size   大小
     * @return ByteString  new  ByteString
     */
    public static ByteString copyFrom( final byte[] bytes, final int offset,
                                       final int size ) {
        final byte[] copy = new byte[size];
        System.arraycopy(bytes, offset, copy, 0, size);
        return new ByteString(copy);
    }

    /**
     * 拷贝并生成新的对象
     *
     * @param bytes 数组
     * @return ByteString
     */
    public static ByteString copyFrom( final byte[] bytes ) {
        return copyFrom(bytes, 0, bytes.length);
    }

    // =================================================================
    // byte[] -> ByteString

    /**
     * 使用命名字符集将 {@code text} 编码为字节序列，并将结果作为 {@code ByteString} 返回。
     *
     * @param text        text
     * @param charsetName name
     * @return ByteString  ByteString
     */
    public static ByteString copyFrom( final String text, final String charsetName ) {
        try {
            return new ByteString(text.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(charsetName + " not supported?", e);
        }
    }

    /**
     * 将 {@code text} 编码为 UTF-8 字节序列，并将结果作为 {@code ByteString} 返回。
     *
     * @param text 字符串
     * @return
     */


    public static ByteString copyFromUtf8( final String text ) {
        byte[] bytes = StrUtil.bytes(text, Charset.forName("UTF-8"));
        return new ByteString(bytes);

    }

    /**
     * 如果两者的内容都匹配，则返回 true。
     */
    public static boolean equals( ByteString bs, ByteString other, boolean checkHash ) {
        final int size = bs.bytes.length;
        if (size != other.bytes.length) {
            return false;
        }

        if (checkHash) {
            // volatile reads
            final int h1 = bs.hash, h2 = other.hash;
            if (h1 != 0 && h2 != 0 && h1 != h2) {
                return false;
            }
        }

        final byte[] thisBytes = bs.bytes;
        final byte[] otherBytes = other.bytes;
        for (int i = 0; i < size; i++) {
            if (thisBytes[i] != otherBytes[i]) {
                return false;
            }
        }

        return true;
    }



    public static ByteString bytesDefaultValue( String bytes ) {
        return new ByteString(byteArrayDefaultValue(bytes));
    }

    /**
     * 生成的代码调用帮助器来构造字节数组字段的默认值。
     * <p>
     *但用于字节字段。 在这种情况下，我们只需要第二个
     * 两个技巧——允许我们将原始字节嵌入为带有 ISO-8859-1 编码的字符串文字。
     *
     * @param bytes 字符串
     * @return
     */
    public static byte[] byteArrayDefaultValue( String bytes ) {
        try {
            return bytes.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // This should never happen since all JVMs are required to implement
            // ISO-8859-1.
            throw new IllegalStateException(
                    "Java VM does not support a standard character set.", e);
        }
    }

    // =================================================================
    // ByteString -> byte[]

    // internal package access to avoid double memory allocation
    byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return String.format("<ByteString@%s size=%d>",
                Integer.toHexString(System.identityHashCode(this)), size());
    }

    /**
     * 根据索引 获取 bytes 数组的 的值
     *
     * @param index 下标值
     * @throws ArrayIndexOutOfBoundsException {@code index} is &lt; 0 or &gt;= size
     */
    public byte byteAt( final int index ) {
        return bytes[index];
    }

    /**
     * 获取 bytes 的大小，主要指length.
     */
    public int size() {
        return bytes.length;
    }



    /**
     * 主要判断 是否为空
     * 返回 {@code true} 如果 {@code 0}, {@code false} 否则返回 falsee.
     */
    public boolean isEmpty() {
        return bytes.length == 0;
    }



    /**
     * 根据偏移量 拷贝到新的目标点中
     *
     * @param target buffer to copy into
     * @param offset in the target buffer
     */
    public void copyTo( final byte[] target, final int offset ) {
        System.arraycopy(bytes, 0, target, offset, bytes.length);
    }

    /**
     * 将字节复制到缓冲区中。
     *
     * @param target       buffer to copy into
     * @param sourceOffset offset within these bytes
     * @param targetOffset offset within the target buffer
     * @param size         number of bytes to copy
     */
    public void copyTo( final byte[] target, final int sourceOffset,
                        final int targetOffset,
                        final int size ) {
        System.arraycopy(bytes, sourceOffset, target, targetOffset, size);
    }

    /**
     * 将字节复制到 {@code byte[]}。
     */
    public byte[] toByteArray() {
        final int size = bytes.length;
        final byte[] copy = new byte[size];
        System.arraycopy(bytes, 0, copy, 0, size);
        return copy;
    }

    /**
     * 使用相同的支持字节数组构造一个新的只读 {@code java.nio.ByteBuffer}。
     */
    public ByteBuffer asReadOnlyByteBuffer() {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.asReadOnlyBuffer();
    }

    /**
     * 通过将字节解码为 UTF-8 构造一个新的 {@code String}。
     */
    public String toStringUtf8() {

        return StrUtil.utf8Str(bytes);
    }

    @Override
    public boolean equals( final Object o ) {
        return o == this || (o instanceof ByteString && equals(this, (ByteString) o, false));
    }



    /**
     * 如果内部数组的内容与提供的数组匹配，则返回 true。
     */
    public boolean equals( final byte[] data ) {
        return equals(data, 0, data.length);
    }

    /**
     * 如果内部数组的内容与提供的数组匹配，则返回 true。
     */
    public boolean equals( final byte[] data, int offset, final int len ) {
        final byte[] bytes = this.bytes;
        if (len != bytes.length)
            return false;

        for (int i = 0; i < len; ) {
            if (bytes[i++] != data[offset++]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = hash;

        if (h == 0) {
            final byte[] thisBytes = bytes;
            final int size = bytes.length;

            h = size;
            for (int i = 0; i < size; i++) {
                h = h * 31 + thisBytes[i];
            }
            if (h == 0) {
                h = 1;
            }

            hash = h;
        }

        return h;
    }
}
