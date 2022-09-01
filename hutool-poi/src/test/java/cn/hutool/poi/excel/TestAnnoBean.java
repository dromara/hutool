package cn.hutool.poi.excel;

import cn.hutool.poi.excel.annotation.HeadTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnnoBean {

	@HeadTitle(title = "人员姓名",index = 1)
	private String name;

	@HeadTitle(title = "人员年龄",index = 1,ignore = true)
	private int age;

	@HeadTitle(title = "人员成绩",index = 2)
	private double score;

	private boolean isPass;

	@HeadTitle(ignore = true)
	private Date examDate;

	private String address;
}
