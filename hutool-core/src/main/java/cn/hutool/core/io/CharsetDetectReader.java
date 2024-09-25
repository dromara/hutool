package cn.hutool.core.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 自动检测字符编码的Reader
 *
 * @author Fulai
 * @since 2024-09-25
 */
public class CharsetDetectReader extends Reader {
    private static final int DETECT_LENGTH = 8192;
    private final int detectLength;
    private final InputStream in;
    private final Charset defaultCharset;
    private InputStreamReader isr;

    /**
     * 创建字符自动检测Reader，默认utf-8编码
     *
     * @param in 输入流
     */
    public CharsetDetectReader(InputStream in) {
        this(in, DETECT_LENGTH, StandardCharsets.UTF_8);
    }

    /**
     * 创建字符自动检测Reader
     *
     * @param in             输入流
     * @param defaultCharset 探测失败使用的字符编码
     */
    public CharsetDetectReader(InputStream in, Charset defaultCharset) {
        this(in, DETECT_LENGTH, defaultCharset);
    }

    /**
     * 创建字符自动检测Reader
     *
     * @param in             输入流
     * @param detectLength   读取头部字节数
     * @param defaultCharset 探测失败使用的字符编码
     */
    public CharsetDetectReader(InputStream in, int detectLength, Charset defaultCharset) {
        this.in = in;
        this.detectLength = detectLength;
        this.defaultCharset = defaultCharset;
    }

    private void ensureReader() throws IOException {
        if (isr == null) {
            //支持标记
            InputStream markStream;
            if (in.markSupported()) {
                markStream = in;
            } else {
                markStream = new BufferedInputStream(in);
            }

            //探测字符编码
            Charset charset = CharsetDetector.detectStream(markStream, detectLength);
            if (charset == null) {
                charset = defaultCharset;
            }
            if (charset == null) {
                throw new NullPointerException("can't detect charset coding, and default charset is null");
            }
            this.isr = new InputStreamReader(markStream, charset);
        }
    }

    @Override
    public int read() throws IOException {
        ensureReader();
        return isr.read();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        ensureReader();
        return isr.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        if (isr != null) {
            isr.close();
        }
    }
}
