package org.dromara.hutool.core.text.replacer;

import org.dromara.hutool.core.text.finder.MultiStrFinder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cmm
 * @date 2024/8/12 14:49
 */
public class HighMultiReplacerV2Test {
	@Test
	public void test1(){
		HashMap<String, String> replaceMap = new HashMap<>();
		replaceMap.put("sha","SHA");
		replaceMap.put("asa","ASA");
		replaceMap.put("ha","HA");
		replaceMap.put("hex","HEX");
		HighMultiReplacerV2 replacer = new HighMultiReplacerV2(replaceMap);
		String text = "asdasahhxxeshaaahexaaasa";
		CharSequence apply = replacer.apply(text);
		replaceMap.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});
		System.out.println(text);
		System.out.println(apply);
	}


	@Test
	public void test2(){
		HashMap<String, String> replaceMap = new HashMap<>();
		replaceMap.put("沙漠","什么");
		replaceMap.put("撒","厦");
		replaceMap.put("哈","蛤");
		replaceMap.put("海克斯","害可是");
		HighMultiReplacerV2 replacer = new HighMultiReplacerV2(replaceMap);
		String text = "撒哈拉大沙漠，你看哈哈哈。hex码中文写成海克斯科技，海克，沙子收拾收拾，撤退，撒下了句点";
		CharSequence apply = replacer.apply(text);
		replaceMap.forEach((k,v) -> {
			System.out.println(k + ":" + v);
		});
		System.out.println(text);
		System.out.println(apply);

	}
}
