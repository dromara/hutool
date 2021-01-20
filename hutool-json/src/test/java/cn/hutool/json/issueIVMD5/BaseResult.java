package cn.hutool.json.issueIVMD5;

import java.util.List;

import lombok.Data;

@Data
public class BaseResult<E> {
	
	public BaseResult() {
	}
	
	private int result;
	private List<E> data;
	private E data2;
	private String nextDataUri;
	private String message;
	private int dataCount;
	
}