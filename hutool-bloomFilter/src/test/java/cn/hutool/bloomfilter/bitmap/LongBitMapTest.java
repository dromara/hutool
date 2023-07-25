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
		final long min = 0;
		final long max = 93750000L * MACHINE64 - 1;

		LongBitMap bm = LongBitMaps.create();
		this.test(bm, min, max);
	}

	@Test
	public void testLongOneMap() {
		final long min = 1;
		final long max = 1;

		LongBitMap bm = LongBitMaps.createOfRange(min, max);
		this.test(bm, min, max);
	}

	@Test
	public void testLongRangeMap() {
		final long min = -1000;
		final long max = 1000;

		LongBitMap bm = LongBitMaps.createOfRange(min, max);
		this.test(bm, min, max);
	}


	private void test(LongBitMap bm, long min, long max) {
		System.out.println("bm: " + bm.getClass().getSimpleName() + ", size: " + ObjectSizeCalculator.getObjectSize(bm));

		// range: min and max
		Assert.assertEquals(bm.getMin(), min);
		Assert.assertEquals(bm.getMax(), max);

		// add: in range
		Assert.assertFalse(bm.contains(min));
		Assert.assertFalse(bm.contains(max));
		bm.add(min);
		bm.add(max);
		Assert.assertTrue(bm.contains(min));
		Assert.assertTrue(bm.contains(max));
		// add: out range
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			bm.add(min - 1);
		});
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			bm.add(max + 1);
		});
		Assert.assertFalse(bm.contains(min - 1));
		Assert.assertFalse(bm.contains(max + 1));

		// remove: in range
		bm.remove(min);
		bm.remove(max);
		Assert.assertFalse(bm.contains(min));
		Assert.assertFalse(bm.contains(max));
		// remove: out range
		bm.remove(min - 1);
		bm.remove(max + 1);
		Assert.assertFalse(bm.contains(min - 1));
		Assert.assertFalse(bm.contains(max + 1));

		// other value
		long median = (max + min) / 2;
		Assert.assertFalse(bm.contains(median));
		bm.add(median);
		Assert.assertTrue(bm.contains(median));
		bm.remove(median);
		Assert.assertFalse(bm.contains(median));
	}
}
