package cn.hutool.core.bean.copier;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;

public class Issue2718Test {

	@Setter
	private static class Deployment{
		public String getResources() {
			// setIgnoreProperties会被转换为propertiesFilter，这个filter是过滤键和值的，因此会获取源对象的值（调用getXXX方法），然后做判断。因此此方法会被执行
			return resources;
		}

		private String resources;


	}

	@Test
	public void copyTest(){
		final Deployment deployment = new Deployment();
		deployment.setResources("test");
		final LinkedHashMap<String, Object> target = BeanCopier
				.create(deployment, new LinkedHashMap<String, Object>(), CopyOptions.create().setIgnoreProperties("resources"))
				.copy();

		Assert.assertTrue(target.isEmpty());
	}
}
