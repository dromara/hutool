package cn.hutool.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CharsetDetectReaderTest {

    @Test
    public void read() throws IOException {
        String text =
                "`Hutool`是一个功能丰富且易用的Java工具库，通过诸多实用工具类的使用，旨在帮助开发者快速、便捷地完成各类开发任务。\n";
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append(text);
        }
        byte[] fullBytes = longText.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(fullBytes);
        CharsetDetectReader reader = new CharsetDetectReader(stream);
        char[] buffer = new char[26];
        reader.read(buffer);
        String term = new String(buffer);
        assertEquals("`Hutool`是一个功能丰富且易用的Java工具库", term);
    }

    @Test
    public void readGb18030() throws IOException {
        String text =
                "`Hutool`是一个功能丰富且易用的Java工具库，通过诸多实用工具类的使用，旨在帮助开发者快速、便捷地完成各类开发任务。\n";
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append(text);
        }
        byte[] fullBytes = longText.toString().getBytes(Charset.forName("GB18030"));
        ByteArrayInputStream stream = new ByteArrayInputStream(fullBytes);
        CharsetDetectReader reader = new CharsetDetectReader(stream);
        char[] buffer = new char[26];
        reader.read(buffer);
        String term = new String(buffer);
        assertEquals("`Hutool`是一个功能丰富且易用的Java工具库", term);
    }
}