package cn.hutool.bloomfilter.bitmap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Assert;
import org.junit.Test;

import static cn.hutool.bloomfilter.bitmap.BitMap.MACHINE64;

/**
 * @see LongBitMaps
 */
public class LongBitMapTest {

	@Test
	public void testLongMap() {
		long min = 0L;
		long max = 93750000L * MACHINE64 - 1;
		LongBitMap bm = LongBitMaps.create();
		Assert.assertEquals(bm.getClass(), LongMap.class);
		this.test(bm, min, max);
	}

	@Test
	public void testLongOneMap() {
		long min = 1L;
		long max = 1L;
		LongBitMap bm = LongBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), LongOneMap.class);
		this.test(bm, min, max);
	}

	@Test
	public void testLongRangeMap() {
		long min = -1001L;
		long max = 1000L;
		LongBitMap bm = LongBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), LongRangeMap.class);
		this.test(bm, min, max);

		min = 2001L;
		max = 3000L;
		bm = LongBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), LongRangeMap.class);
		this.test(bm, min, max);

		{
			//max = LongBitMap.MAX_TOTAL; // 太大了，会内存溢出，没法测：java.lang.OutOfMemoryError
			max = Integer.MAX_VALUE;

			min = 1;
			bm = LongBitMaps.createOfRange(min, max);
			Assert.assertEquals(bm.getClass(), LongRangeMap.class);
			this.test(bm, min, max);
		}

		{
			max = LongBitMap.MAX_TOTAL;

			min = 0;
			testTooBiggerRange(bm, min, max);

			min = -1;
			testTooBiggerRange(bm, min, max);

			min = Long.MIN_VALUE;
			testTooBiggerRange(bm, min, max);
		}
	}


	private void test(LongBitMap bm, long min, long max) {
		System.out.println("bm: " + bm.getClass().getSimpleName() + ", objectSize: " + ObjectSizeCalculator.getObjectSize(bm));

		// range: min and max
		Assert.assertEquals(bm.getMin(), min);
		Assert.assertEquals(bm.getMax(), max);

		// in range
		testInRangeValue(bm, min);
		testInRangeValue(bm, max);
		// out range
		testOutRangeValue(bm, min - 1);
		testOutRangeValue(bm, max + 1);

		// other value
		long median = (max + min) / 2;
		this.testInRangeValue(bm, median);
	}

	private void testInRangeValue(LongBitMap bm, long i) {
		Assert.assertFalse(bm.contains(i));
		bm.add(i);
		Assert.assertTrue(bm.contains(i));
		bm.remove(i);
		Assert.assertFalse(bm.contains(i));
	}

	private void testOutRangeValue(LongBitMap bm, long i) {
		Assert.assertTrue(i > bm.getMax() || i < bm.getMin());
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			if (i > bm.getMax()) {
				bm.add(i + 1);
			} else {
				bm.add(i - 1);
			}
		});
		Assert.assertFalse(bm.contains(i));
		bm.remove(i);
		Assert.assertFalse(bm.contains(i));
	}

	private void testTooBiggerRange(LongBitMap bm, final long minFinal, final long maxFinal) {
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			LongBitMaps.createOfRange(minFinal, maxFinal);
		});
	}
}
