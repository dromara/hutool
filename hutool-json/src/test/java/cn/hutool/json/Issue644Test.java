package cn.hutool.json;

import cn.hutool.core.date.TimeUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 问题反馈对象中有JDK8日期对象时转换失败，5.0.7修复
 */
public class Issue644Test {

	@Test
	public void toBeanTest(){
		final BeanWithDate beanWithDate = new BeanWithDate();
		beanWithDate.setDate(LocalDateTime.now());

		final JSONObject jsonObject = JSONUtil.parseObj(beanWithDate);

		BeanWithDate beanWithDate2 = JSONUtil.toBean(jsonObject, BeanWithDate.class);
		Assertions.assertEquals(TimeUtil.formatNormal(beanWithDate.getDate()),
				TimeUtil.formatNormal(beanWithDate2.getDate()));

		beanWithDate2 = JSONUtil.toBean(jsonObject.toString(), BeanWithDate.class);
		Assertions.assertEquals(TimeUtil.formatNormal(beanWithDate.getDate()),
				TimeUtil.formatNormal(beanWithDate2.getDate()));
	}

	@Data
	static class BeanWithDate{
		private LocalDateTime date;
	}
}
