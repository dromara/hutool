package cn.hutool.json;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;

public class Issue2564Test {
	@Getter
	@Setter
	class A implements Serializable {
		private static final long serialVersionUID = -5282562537774493119L;
	}
	@Test
	public void testHutoolJsonUitl(){
		String x = "{}";
		A a = JSONUtil.toBean(x, A.class);
		Assert.assertNotNull(a);
	}
}
