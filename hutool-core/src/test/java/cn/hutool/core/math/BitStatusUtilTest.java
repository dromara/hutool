package cn.hutool.core.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitStatusUtilTest {

	@Test
	void add() {
		assertEquals(BitStatusUtil.add(0x1100, 0x0010),0x1110);
		assertEquals(BitStatusUtil.add(0x1101, 0x0010),0x1111);
		assertEquals(BitStatusUtil.add(0x0000, 0x0000),0x0000);
		assertEquals(BitStatusUtil.add(0x0010, 0x0000),0x0010);
	}

	@Test
	void has() {
		assertEquals(BitStatusUtil.has(0x1100, 0x0010),false);
		assertEquals(BitStatusUtil.has(0x1101, 0x0010),false);
		assertThrows(Exception.class,() -> BitStatusUtil.has(0x0000, 0x0000));
	}

	@Test
	void remove() {
		assertEquals(BitStatusUtil.remove(0x1100, 0x0010),0x1100);
		assertEquals(BitStatusUtil.remove(0x1111, 0x0010),0x1101);
		assertThrows(Exception.class,() -> BitStatusUtil.remove(0x0000, 0x0000));
	}

	@Test
	void clear() {
		assertEquals(BitStatusUtil.clear(),0);
	}
}
