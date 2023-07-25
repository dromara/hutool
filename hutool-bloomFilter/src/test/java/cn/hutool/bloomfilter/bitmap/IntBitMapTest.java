package cn.hutool.bloomfilter.bitmap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see IntBitMaps
 */
public class IntBitMapTest {

	@Test
	public void testIntMap() {
		final int min = 0;
		final long max = Integer.MAX_VALUE;

		IntBitMap bm = IntBitMaps.create();
		this.test(bm, min, max);
	}

	@Test
	public void testIntOneMap() {
		final int min = 1;
		final int max = 1;

		IntBitMap bm = IntBitMaps.createOfRange(min, max);
		this.test(bm, min, max);
	}

	@Test
	public void testIntRangeMap() {
		final int min = -1000;
		final int max = 1000;

		IntBitMap bm = IntBitMaps.createOfRange(min, max);
		this.test(bm, min, max);
	}


	private void test(IntBitMap bm, int min, long max) {
		System.out.println("bm: " + bm.getClass().getSimpleName() + ", objectSize: " + ObjectSizeCalculator.getObjectSize(bm));

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
