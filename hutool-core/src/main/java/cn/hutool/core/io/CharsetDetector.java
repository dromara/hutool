package cn.hutool.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;

/**
 * 编码探测器
 *
 * @author looly
 * @since 5.4.7
 */
public class CharsetDetector {

    /**
     * 默认的参与测试的编码
     */
    private static final Charset[] DEFAULT_CHARSETS;

    static {
        String[] names =
                {"UTF-8", "GBK", "GB2312", "GB18030", "UTF-16BE", "UTF-16LE", "UTF-16", "BIG5", "UNICODE", "US-ASCII"};
        DEFAULT_CHARSETS = Convert.convert(Charset[].class, names);
    }

    /**
     * 探测文件编码
     *
     * @param file     文件
     * @param charsets 需要测试用的编码，null或空使用默认的编码数组
     * @return 编码
     * @since 5.6.7
     */
    public static Charset detect(File file, Charset... charsets) {
        return detect(FileUtil.getInputStream(file), charsets);
    }

    /**
     * 探测编码<br>
     * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持reset方法的流
     *
     * @param in       流，使用后关闭此流
     * @param charsets 需要测试用的编码，null或空使用默认的编码数组
     * @return 编码
     */
    public static Charset detect(InputStream in, Charset... charsets) {
        return detect(IoUtil.DEFAULT_LARGE_BUFFER_SIZE, in, charsets);
    }

    /**
     * 探测编码<br>
     * 注意：此方法会读取流的一部分，然后关闭流，如重复使用流，请使用支持reset方法的流
     *
     * @param bufferSize 自定义缓存大小，即每次检查的长度
     * @param in         流，使用后关闭此流
     * @param charsets   需要测试用的编码，null或空使用默认的编码数组
     * @return 编码
     * @since 5.7.10
     */
    public static Charset detect(int bufferSize, InputStream in, Charset... charsets) {
        if (ArrayUtil.isEmpty(charsets)) {
            charsets = DEFAULT_CHARSETS;
        }

        int len;
        final byte[] buffer = new byte[bufferSize];
        try {
            while ((len = in.read(buffer)) > -1) {
                for (Charset charset : charsets) {
                    final CharsetDecoder decoder = charset.newDecoder();
                    if (identify(buffer, len, decoder)) {
                        return charset;
                    }
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IoUtil.close(in);
        }
        return null;
    }

    /**
     * 判断是否为UTF8编码，允许buffer包含被截断的中文字符
     *
     * @param buffer 待检测的byte数组
     * @param len    读取长度
     * @return 是否
     */
    public static boolean isUsAscii(byte[] buffer, int len) {
        //ascii最大字节数为1
        return isCharset(buffer, len, StandardCharsets.US_ASCII, 1);
    }

    /**
     * 判断是否为UTF8编码，允许buffer包含被截断的中文字符
     *
     * @param buffer 待检测的byte数组
     * @param len    读取长度
     * @return 是否
     */
    public static boolean isUtf8(byte[] buffer, int len) {
        //Utf8最大字节数为4
        return isCharset(buffer, len, StandardCharsets.UTF_8, 2);
    }

    /**
     * 判断是否为GBK编码，允许buffer包含被截断的中文字符
     *
     * @param buffer 待检测的byte数组
     * @param len    读取长度
     * @return 是否
     */
    public static boolean isGbk(byte[] buffer, int len) {
        //GBK最大字节数为2
        return isCharset(buffer, len, Charset.forName("GBK"), 2);
    }

    /**
     * 判断是否为GB18030编码，允许buffer包含被截断的中文字符。
     * GB18030兼容GBK，GBK兼容GB2312, GB2312兼容ASCII
     *
     * @param buffer 待检测的byte数组
     * @param len    读取长度
     * @return 是否
     */
    public static boolean isGb18030(byte[] buffer, int len) {
        //GB18030最大字节数为4
        return isCharset(buffer, len, Charset.forName("GB18030"), 4);
    }

    /**
     * 检测byte数组是否是指定的编码
     *
     * @param buffer  待检测的byte数组
     * @param len     字节长度
     * @param maxTrim 为防止中文截断时编码检测失败，可能需要去除尾部的几个字节
     * @param charset 要检测的编码
     * @return 是否检测成功
     */
    public static boolean isCharset(byte[] buffer, int len, Charset charset, int maxTrim) {
        for (int t = 0; t < maxTrim; t++) {
            int end = len - t;
            if (end > 0) {
                CharsetDecoder decoder = charset.newDecoder();
                if (identify(buffer, end, decoder)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 读取流的开头指定字节数，并探测字符编码。
     * 如果输入流长度小于detectLength，则可以完整探测编码。否则只能检测几个常见编码。
     *
     * @param in            输入流，要求支持mark()方法，输入流不会被关闭
     * @param detectLength  读取头部字节数
     * @param probeCharsets 可能的字符编码集
     * @return 探测到的编码，为null表示探测失败
     */
    public static Charset detectStream(InputStream in, int detectLength, Charset... probeCharsets) throws IOException {
        if (!in.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark() method");
        }

        //标记位置
        in.mark(detectLength);
        //读取头部字节
        byte[] buffer = new byte[detectLength];
        //实际读取到的长度
        int len = in.read(buffer);
        //重置流读取位置
        in.reset();

        //如已读取到完整内容，则探测编码
        if (len < detectLength) {
            //已读取到完整内容时
            byte[] fullBytes = new byte[len];
            System.arraycopy(buffer, 0, fullBytes, 0, len);
            return detectFull(fullBytes, probeCharsets);
        }
        if (len == detectLength) {
            return detectFull(buffer, probeCharsets);
        }

        //如果只读取到部分内容，则只能探测几个常见的编码
        if (isUsAscii(buffer, len)) {
            return StandardCharsets.US_ASCII;
        }
        if (isUtf8(buffer, len)) {
            return StandardCharsets.UTF_8;
        }
        if (isGbk(buffer, len)) {
            //已读取部分内容
            return Charset.forName("GBK");
        }
        if (isGb18030(buffer, len)) {
            //已读取部分内容
            return Charset.forName("GB18030");
        }
        //为确保结果正确，不会对非完整内容进行探测
        return null;
    }

    /**
     * 探测byte数组可能的编码
     *
     * @param fullBytes 待检测的byte数组，应当是完整的内容，不能有字符截断的情况
     * @param charsets  需要测试用的编码，null或空使用默认的编码数组
     * @return 编码
     */
    public static Charset detectFull(byte[] fullBytes, Charset... charsets) {
        if (ArrayUtil.isEmpty(charsets)) {
            charsets = DEFAULT_CHARSETS;
        }
        for (Charset charset : charsets) {
            final CharsetDecoder decoder = charset.newDecoder();
            if (identify(fullBytes, fullBytes.length, decoder)) {
                return charset;
            }
        }
        return null;
    }

    /**
     * 通过try的方式测试指定bytes是否可以被解码，从而判断是否为指定编码
     *
     * @param bytes   测试的bytes
     * @param decoder 解码器
     * @return 是否是指定编码
     */
    private static boolean identify(byte[] bytes, int len, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes, 0, len));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
}
