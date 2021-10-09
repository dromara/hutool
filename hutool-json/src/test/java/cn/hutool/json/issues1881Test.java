package cn.hutool.json;

import cn.hutool.core.lang.Console;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class issues1881Test {
	@Accessors(chain = true)
	@Data
	public class ThingsHolderContactVO implements Serializable {

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

		Console.log(JSONUtil.parseArray(holderContactVOList));
	}
}
