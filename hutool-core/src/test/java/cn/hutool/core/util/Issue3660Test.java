package cn.hutool.core.util;

import org.junit.Test;

import java.util.List;

public class Issue3660Test {
	@Test
	public void splitTest() {
		System.out.println(StrUtil.split("", ','));
	}
}
