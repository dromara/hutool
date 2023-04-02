package org.dromara.hutool.core.io;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.iter.LineIter;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.io.stream.EmptyOutputStream;
import org.dromara.hutool.core.io.stream.StrInputStream;
import org.dromara.hutool.core.lang.func.SerConsumer;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class IoUtilTest {

	@Test
	public void readBytesTest() {
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"));
		Assertions.assertEquals(22807, bytes.length);
	}

	@Test
	public void readBytesWithLengthTest() {
		// 读取固定长度
		final int limit = RandomUtil.randomInt(22807);
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream("hutool.jpg"), limit);
		Assertions.assertEquals(limit, bytes.length);
	}

	@Test
	public void readLinesTest() {
		try (final BufferedReader reader = ResourceUtil.getUtf8Reader("test_lines.csv")) {
			IoUtil.readLines(reader, (SerConsumer<String>) Assertions::assertNotNull);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Test
	public void readUtf8LinesTest() {
		final List<String> strings = IoUtil.readUtf8Lines(ResourceUtil.getStream("text.txt"), ListUtil.of());
		Assertions.assertEquals(3, strings.size());
	}

	@Test
	public void readUtf8LinesTest2() {
		IoUtil.readUtf8Lines(ResourceUtil.getStream("text.txt"),
			(SerConsumer<String>) Assertions::assertNotNull);
	}

	@Test
	public void toBufferedTest() {
		final BufferedInputStream stream = IoUtil.toBuffered(
				new ByteArrayInputStream("hutool".getBytes()), IoUtil.DEFAULT_BUFFER_SIZE);

		Assertions.assertNotNull(stream);
		Assertions.assertEquals("hutool", IoUtil.readUtf8(stream));
	}

	@Test
	public void toBufferedOfOutTest() {
		final BufferedOutputStream stream = IoUtil.toBuffered(
				EmptyOutputStream.INSTANCE, 512);

		Assertions.assertNotNull(stream);
	}

	@Test
	public void toBufferedOfReaderTest() {
		final BufferedReader reader = IoUtil.toBuffered(
				new StringReader("hutool"), 512);

		Assertions.assertNotNull(reader);

		Assertions.assertEquals("hutool", IoUtil.read(reader));
	}

	@Test
	public void toBufferedOfWriterTest() throws IOException {
		final StringWriter stringWriter = new StringWriter();
		final BufferedWriter writer = IoUtil.toBuffered(
				stringWriter, 512);

		Assertions.assertNotNull(writer);
		writer.write("hutool");
		writer.flush();

		Assertions.assertEquals("hutool", stringWriter.toString());
	}

	@Test
	public void toBufferedOfWriterTest2() throws IOException {
		final StringWriter stringWriter = new StringWriter();
		final BufferedWriter writer = IoUtil.toBuffered(stringWriter);

		Assertions.assertNotNull(writer);
		writer.write("hutool");
		writer.flush();

		Assertions.assertEquals("hutool", stringWriter.toString());
	}

	@Test
	public void toPushBackReaderTest() throws IOException {
		final PushbackReader reader = IoUtil.toPushBackReader(new StringReader("hutool"), 12);
		final int read = reader.read();
		Assertions.assertEquals('h', read);
		reader.unread(read);

		Assertions.assertEquals("hutool", IoUtil.read(reader));
	}

	@Test
	public void toAvailableStreamTest() {
		final InputStream in = IoUtil.toAvailableStream(StrInputStream.ofUtf8("hutool"));
		final String read = IoUtil.readUtf8(in);
		Assertions.assertEquals("hutool", read);
	}

	@Test
	public void writeCloseTest() {
		IoUtil.writeClose(EmptyOutputStream.INSTANCE, "hutool".getBytes());
	}

	@Test
	public void writeUtf8Test() {
		IoUtil.writeUtf8(EmptyOutputStream.INSTANCE, false, "hutool");
	}

	@Test
	public void closeIfPossibleTest() {
		IoUtil.closeIfPossible(new Object());
	}

	@Test
	public void contentEqualsTest() {
		final boolean b = IoUtil.contentEquals(new StringReader("hutool"), new StringReader("hutool"));
		Assertions.assertTrue(b);
	}

	@Test
	public void lineIterTest() {
		final LineIter strings = IoUtil.lineIter(ResourceUtil.getStream("text.txt"), CharsetUtil.UTF_8);
		strings.forEach(Assertions::assertNotNull);
	}
}
