package cn.hutool.core.event;

import org.junit.Assert;
import org.junit.Test;


public class ListenerDecorateTest {

	@Test
	public void testCompareTo() {
		ListenerDecorate<Object, Object> listenerDecorate1 = ListenerDecorate.build().order(5);
		ListenerDecorate<Object, Object> listenerDecorate2 = ListenerDecorate.build().order(4);

		int compare = listenerDecorate1.compareTo(listenerDecorate2);
		// 5 > 4。所以 listenerDecorate1 > listenerDecorate2
		Assert.assertEquals(1, compare);

	}
}
