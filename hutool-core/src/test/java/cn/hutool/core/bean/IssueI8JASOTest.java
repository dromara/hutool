package cn.hutool.core.bean;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.Setter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueI8JASOTest {

	@Test
	public void copyTest() {
		final UserOne userOne = new UserOne();
		userOne.setEmail("123@qq.com");
		final UserTwo userTwo = new UserTwo();
		BeanUtil.copyProperties(userOne, userTwo);
		assertEquals(userOne.getEmail(), userTwo.getEmail());
	}

	@Data
	public static class UserOne {
		private Long id;
		@Alias("邮箱")
		private String email;
	}

	@Data
	public static class UserTwo {
		private Long id;
		@Alias("邮箱")
		private String email;
	}
}
