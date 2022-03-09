package cn.hutool.json;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * 用于测试1970年前的日期（负数）还有int类型的数字转日期可能导致的转换失败问题。
 */
public class Issue677Test {

	@Test
	public void toBeanTest(){
		final AuditResultDto dto = new AuditResultDto();
		dto.setDate(DateUtil.date(-1497600000));

		final String jsonStr = JSONUtil.toJsonStr(dto);
		final AuditResultDto auditResultDto = JSONUtil.toBean(jsonStr, AuditResultDto.class);
		Assert.assertEquals("Mon Dec 15 00:00:00 CST 1969", auditResultDto.getDate().toString().replace("GMT+08:00", "CST"));
	}

	@Data
	public static class AuditResultDto{
		private Date date;
	}
}
