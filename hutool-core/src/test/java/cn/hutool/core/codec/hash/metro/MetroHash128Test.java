package cn.hutool.core.codec.hash.metro;

import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.codec.Number128;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MetroHash128Test {
	@Test
	public void testEmpty() {
		Assert.assertEquals("5F3CA3D41D1CB4606B14684C65FB6", h128(""));
	}

	@Test
	public void test1Low() {
		Assert.assertEquals("E84D9EA70174C3184AC6E55552310F85", h128("a"));
	}

	@Test
	public void test1High() {
		Assert.assertEquals("9A5BCED4C3CA98CADE13388E3C14C215", h128("é"));
	}

	@Test
	public void test2Palindrome() {
		Assert.assertEquals("3DDDF558587273E1FD034EC7CC917AC8", h128("aa"));
	}

	@Test
	public void test2() {
		Assert.assertEquals("458E6A18B65C38AD2552335402A068A2", h128("ab"));
	}

	@Test
	public void test3PalindromeLow() {
		Assert.assertEquals("19725A6E67A8DD1A84E3C844A20DA938", h128("aaa"));
	}

	@Test
	public void test3PalindromeHigh() {
		Assert.assertEquals("1DD9CC1D29B5080D5F9F171FB2C50CBB", h128("ééé"));
	}

	@Test
	public void test3() {
		Assert.assertEquals("89AB9CDB9FAF7BA71CD86385C1F801A5", h128("abc"));
	}

	@Test
	public void test4Palindrome() {
		Assert.assertEquals("AFD0BBB3764CA0539E46B914B8CB8911", h128("poop"));
	}

	@Test
	public void test4() {
		Assert.assertEquals("D11B6DB94FE20E3884F3829AD6613D19", h128("fart"));
	}

	@Test
	public void test5() {
		Assert.assertEquals("D45A3A74885F9C842081929D2E9A3A3B", h128("Hello"));
	}

	@Test
	public void test12() {
		Assert.assertEquals("A7CEC59B03A9053BA6009EEEC81E81F5", h128("Hello World!"));
	}

	@Test
	public void test31() {
		Assert.assertEquals("980CA7496A1B26D24E529DFB2B3A870",
				h128("The Quick Brown Fox Jumped Over"));
	}

	@Test
	public void test32() {
		Assert.assertEquals("76663CEA442E22F86A6CB41FBA896B9B",
				h128("The Quick Brown Fox Jumped Over "));
	}

	@Test
	public void testLonger() {
		Assert.assertEquals("7010B2D7C8A3515AE3CA4DBBD9ED30D0",
				h128("The Quick Brown Fox Jumped Over The Lazy Dog"));
	}

	static final byte[] ENDIAN =
			StrUtil.utf8Bytes("012345678901234567890123456789012345678901234567890123456789012");

	@Test
	public void testLittleEndian() {
		final ByteBuffer output = ByteBuffer.allocate(16);
		MetroHash128.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.LITTLE_ENDIAN);
		Assert.assertEquals("C77CE2BFA4ED9F9B0548B2AC5074A297", hex(output.array()));
	}

	@Test
	public void testBigEndian() {
		final ByteBuffer output = ByteBuffer.allocate(16);
		MetroHash128.of(0).apply(ByteBuffer.wrap(ENDIAN)).write(output, ByteOrder.BIG_ENDIAN);
		Assert.assertEquals("97A27450ACB248059B9FEDA4BFE27CC7", hex(output.array()));
	}

	static String h128(final String input) {
		final MetroHash128 mh = MetroHash128.of(0).apply(ByteBuffer.wrap(StrUtil.utf8Bytes(input)));
		final Number128 hash = mh.get();
		return hex(hash.getHighValue()) + hex(hash.getLowValue());
	}

	private static String hex(final long value){
		return HexUtil.toHex(value).toUpperCase();
	}

	private static String hex(final byte[] bytes){
		return HexUtil.encodeHexStr(bytes).toUpperCase();
	}
}
