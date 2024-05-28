package cn.hutool.jwt;

import org.junit.Assert;
import org.junit.Test;

public class InvalidTokenTest {
	@Test
	public void test() {
		String s = "1eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.1eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoyNTE2MjM5MDIyfQ.kggx5ylVTJWq_-c7mW8hRehry-ikLBQNNeW33k8nKbQ";

		Exception e = Assert.assertThrows(JWTException.class, () -> {
			JWT.of(s);
		});

		Assert.assertEquals("Invalid token: JSON parse error!", e.getMessage());
	}
}
