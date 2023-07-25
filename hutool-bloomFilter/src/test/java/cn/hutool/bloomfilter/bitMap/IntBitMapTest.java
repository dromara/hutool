package cn.hutool.bloomfilter.bitMap;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author wangliang181230
 * @see IntBitMaps
 */
public class IntBitMapTest {

	@Test
	public void testIntMap() {
		long min = 0;
		long max = Integer.MAX_VALUE;
		IntBitMap bm = IntBitMaps.create();
		Assert.assertEquals(bm.getClass(), IntMap.class);
		this.test(bm, min, max);
	}

	@Test
	public void testIntOneMap() {
		long min = 1;
		long max = 1;
		IntBitMap bm = IntBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), SingleValueBitMap.class);
		this.test(bm, min, max);
	}

	@Test
	public void testIntRangeMap() {
		long min = -1001;
		long max = 1000;
		IntBitMap bm = IntBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), IntRangeMap.class);
		this.test(bm, min, max);

		min = 2001;
		max = 3000;
		bm = IntBitMaps.createOfRange(min, max);
		Assert.assertEquals(bm.getClass(), IntRangeMap.class);
		this.test(bm, min, max);

		{
			//max = IntBitMap.MAX_TOTAL; // 太大了，会内存溢出，没法测：java.lang.OutOfMemoryError
			max = Integer.MAX_VALUE;

			min = 1;
			bm = IntBitMaps.createOfRange(min, max);
			Assert.assertEquals(bm.getClass(), IntRangeMap.class);
			this.test(bm, min, max);

			min = 0;
			bm = IntBitMaps.createOfRange(min, max);
			Assert.assertEquals(bm.getClass(), IntMap.class);
			this.test(bm, min, max);

			min = -1;
			bm = IntBitMaps.createOfRange(min, max);
			Assert.assertEquals(bm.getClass(), IntRangeMap.class);
			this.test(bm, min, max);

			min = Integer.MIN_VALUE;
			bm = IntBitMaps.createOfRange(min, max);
			Assert.assertEquals(bm.getClass(), IntRangeMap.class);
			this.test(bm, min, max);
		}

		{
			max = IntBitMap.MAX_TOTAL;

			min = 0;
			testTooBiggerRange(min, max);

			min = -1;
			testTooBiggerRange(min, max);

			min = Integer.MIN_VALUE;
			testTooBiggerRange(min, max);
		}
	}


	private void test(IntBitMap bm, long min, long max) {
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

	private void testInRangeValue(IntBitMap bm, long i) {
		Assert.assertFalse(bm.contains(i));
		bm.add(i);
		Assert.assertTrue(bm.contains(i));
		bm.remove(i);
		Assert.assertFalse(bm.contains(i));
	}

	private void testOutRangeValue(IntBitMap bm, long i) {
		Assert.assertTrue(i > bm.getMax() || i < bm.getMin());
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			if (i > bm.getMax()) {
				bm.add(i + 1);
			} else {
				bm.add(i - 1);
			}
		});
		Assert.assertFalse(bm.contains(i));
	}

	private void testTooBiggerRange(final long minFinal, final long maxFinal) {
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			IntBitMaps.createOfRange(minFinal, maxFinal);
		});
	}
}
