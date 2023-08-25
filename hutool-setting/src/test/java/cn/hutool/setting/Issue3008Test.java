package cn.hutool.setting;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue3008Test {

	/**
	 * 数组字段追加后生成新的数组，造成赋值丢失<br>
	 * 修复见：BeanUtil.setFieldValue
	 */
	@Test
	public void toBeanTest() {
		final Props props = PropsUtil.get("issue3008");
		final MyUser user = props.toBean(MyUser.class, "person");
		Assert.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(user.getHobby()));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}
}
