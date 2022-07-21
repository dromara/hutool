package cn.hutool.core.io.unit;

import org.junit.Assert;
import org.junit.Test;

/**
 * 文件大小单位转换，方便于各种单位之间的转换
 * @author zrh 455741807@qq.com
 * @date 2022-07-21
 */
public class FileSizeUnitTest {
	private static final long Byte = 1;
	private static final long KB = 1024;
	private static final long MB = (KB * KB);
	private static final long GB = (KB * MB);
	private static final long TB = (KB * GB);

	@Test
	public void toByte() {
		long byteS = 123, kbS = 1, mbS = 1, gbS = 1, tbS = 1;

		long byteT = FileSizeUnit.BYTE.toByte(byteS);
		long kbT = FileSizeUnit.KB.toByte(kbS);
		long mbT = FileSizeUnit.MB.toByte(mbS);
		long gbT = FileSizeUnit.GB.toByte(gbS);
		long tbT = FileSizeUnit.TB.toByte(tbS);

		Assert.assertEquals(byteS, byteT);
		Assert.assertEquals(kbS*(KB/Byte), kbT);
		Assert.assertEquals(mbS*(MB/Byte), mbT);
		Assert.assertEquals(gbS*(GB/Byte), gbT);
		Assert.assertEquals(tbS*(TB/Byte), tbT);


	}

	@Test
	public void toKB() {
		long byteS = 1230, kbS = 456, mbS = 1, gbS = 1, tbS = 1;

		long byteT = FileSizeUnit.BYTE.toKB(byteS);
		long kbT = FileSizeUnit.KB.toKB(kbS);
		long mbT = FileSizeUnit.MB.toKB(mbS);
		long gbT = FileSizeUnit.GB.toKB(gbS);
		long tbT = FileSizeUnit.TB.toKB(tbS);

		Assert.assertEquals(byteS/(KB/Byte), byteT);
		Assert.assertEquals(kbS, kbT);
		Assert.assertEquals(mbS*(MB/KB), mbT);
		Assert.assertEquals(gbS*(GB/KB), gbT);
		Assert.assertEquals(tbS*(TB/KB), tbT);
	}

	@Test
	public void toMB() {
		long byteS = 1230000, kbS = 456000, mbS = 789, gbS = 1, tbS = 1;

		long byteT = FileSizeUnit.BYTE.toMB(byteS);
		long kbT = FileSizeUnit.KB.toMB(kbS);
		long mbT = FileSizeUnit.MB.toMB(mbS);
		long gbT = FileSizeUnit.GB.toMB(gbS);
		long tbT = FileSizeUnit.TB.toMB(tbS);

		Assert.assertEquals(byteS/(MB/Byte), byteT);
		Assert.assertEquals(kbS/(MB/KB), kbT);
		Assert.assertEquals(mbS, mbT);
		Assert.assertEquals(gbS*(GB/MB), gbT);
		Assert.assertEquals(tbS*(TB/MB), tbT);
	}

	@Test
	public void toGB() {
		long byteS = 1230000000, kbS = 456000000, mbS = 789000, gbS = 1, tbS = 1;

		long byteT = FileSizeUnit.BYTE.toGB(byteS);
		long kbT = FileSizeUnit.KB.toGB(kbS);
		long mbT = FileSizeUnit.MB.toGB(mbS);
		long gbT = FileSizeUnit.GB.toGB(gbS);
		long tbT = FileSizeUnit.TB.toGB(tbS);

		Assert.assertEquals(byteS/(GB/Byte), byteT);
		Assert.assertEquals(kbS/(GB/KB), kbT);
		Assert.assertEquals(mbS/(GB/MB), mbT);
		Assert.assertEquals(gbS, gbT);
		Assert.assertEquals(tbS*(TB/GB), tbT);

	}

	@Test
	public void toTB() {
		long byteS = 1230000000000L, kbS = 111000000000L, mbS = 789000000L, gbS = 1024, tbS = 1;

		long byteT = FileSizeUnit.BYTE.toTB(byteS);
		long kbT = FileSizeUnit.KB.toTB(kbS);
		long mbT = FileSizeUnit.MB.toTB(mbS);
		long gbT = FileSizeUnit.GB.toTB(gbS);
		long tbT = FileSizeUnit.TB.toTB(tbS);

		Assert.assertEquals(byteS/(TB/Byte), byteT);
		Assert.assertEquals(kbS/(TB/KB), kbT);
		Assert.assertEquals(mbS/(TB/MB), mbT);
		Assert.assertEquals(gbS/(TB/GB), gbT);
		Assert.assertEquals(tbS, tbT);
	}

	@Test
	public void readableSizeStr1() {
		long size = 1024*1024;
		String sizeStr = FileSizeUnit.readableSizeStr(size);
		Assert.assertEquals("1.0MB", sizeStr);
	}

	@Test
	public void readableSizeStr2() {
		long size = 123*1024;
		String sizeStr = FileSizeUnit.readableSizeStr(size, FileSizeUnit.MB);
		Assert.assertEquals("123.0GB", sizeStr);
	}

}
