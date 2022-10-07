package cn.hutool.core.lang;

import cn.hutool.core.lang.builder.GenericBuilder;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DictTest {
	@Test
	public void dictTest(){
		final Dict dict = Dict.of()
				.set("key1", 1)//int
				.set("key2", 1000L)//long
				.set("key3", DateTime.now());//Date

		final Long v2 = dict.getLong("key2");
		Assert.assertEquals(Long.valueOf(1000L), v2);
	}

	@Test
	public void dictTest2(){
		final Dict dict = new Dict(true);
		final Map<String, Object> map = new HashMap<>();
		map.put("A", 1);

		dict.putAll(map);

		Assert.assertEquals(1, dict.get("A"));
		Assert.assertEquals(1, dict.get("a"));
	}

	@Test
	public void ofTest(){
		final Dict dict = Dict.ofKvs(
				"RED", "#FF0000",
				"GREEN", "#00FF00",
				"BLUE", "#0000FF"
		);

		Assert.assertEquals("#FF0000", dict.get("RED"));
		Assert.assertEquals("#00FF00", dict.get("GREEN"));
		Assert.assertEquals("#0000FF", dict.get("BLUE"));
	}

	@Test
	public void removeEqualTest(){
		final Dict dict = Dict.ofKvs(
			"key1", null
		);

		final Dict dict2 = Dict.ofKvs(
			"key1", null
		);

		dict.removeEqual(dict2);

		Assert.assertTrue(dict.isEmpty());
	}

	@Test
	public void setFieldsTest() {
		final User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
		final Dict dict = Dict.of();
		dict.setFields(user::getNickname, user::getUsername);
		Assert.assertEquals("hutool", dict.get("username"));
		Assert.assertNull(dict.get("nickname"));

		// get by lambda
		Assert.assertEquals("hutool", dict.get(User::getUsername));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class User {
		private String username;
		private String nickname;
	}
}
