package cn.hutool.json;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Issues1881Test {

	@Accessors(chain = true)
	@Data
	static class ThingsHolderContactVO implements Serializable {

		private static final long serialVersionUID = -8727337936070932370L;
		private Long id;
		private Integer type;
		private String memberId;
		private String name;
		private String phone;
		private String avatar;
		private Long createTime;
	}

	@Test
	public void parseTest(){
		List<ThingsHolderContactVO> holderContactVOList = new ArrayList<>();
		holderContactVOList.add(new ThingsHolderContactVO().setId(1L).setName("1"));
		holderContactVOList.add(new ThingsHolderContactVO().setId(2L).setName("2"));

		Assert.assertEquals("[{\"name\":\"1\",\"id\":1},{\"name\":\"2\",\"id\":2}]", JSONUtil.parseArray(holderContactVOList).toString());
	}
}
