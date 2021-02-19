package cn.hutool.json.test.bean;

import java.util.List;

public class Price {

	private List<List<ADT>> ADT;

	public void setADT(List<List<ADT>> ADT) {
		this.ADT = ADT;
	}

	public List<List<ADT>> getADT() {
		return ADT;
	}
}