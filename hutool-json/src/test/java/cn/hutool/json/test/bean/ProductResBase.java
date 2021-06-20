package cn.hutool.json.test.bean;

import java.io.Serializable;

/**
 * @author wangyan E-mail:wangyan@pospt.cn
 * @version 创建时间：2017年9月11日 上午9:33:01 类说明
 */
public class ProductResBase implements Serializable {

	private static final long serialVersionUID = -6708040074002451511L;
	/**
	 * 请求结果成功0
	 */
	public static final int REQUEST_RESULT_SUCCESS = 0;
	/**
	 * 请求结果失败 1
	 */
	public static final int REQUEST_RESULT_FIAL = 1;
	/**
	 * 成功code
	 */
	public static final String REQUEST_CODE_SUCCESS = "0000";
	/**
	 * 结果 成功0 失败1
	 */
	private int resResult = 0;
	private String resCode = "0000";
	private String resMsg = "success";

	/**
	 * 成本总计
	 */
	private Integer costTotal;

	public Integer getCostTotal() {
		return costTotal;
	}

	public ProductResBase setCostTotal(Integer costTotal) {
		this.costTotal = costTotal;
		return this;
	}

	public int getResResult() {
		return resResult;
	}

	public String getResCode() {
		return resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public ProductResBase setResResult(int resResult) {
		this.resResult = resResult;
		return this;
	}

	public ProductResBase setResCode(String resCode) {
		this.resCode = resCode;
		return this;
	}

	public ProductResBase setResMsg(String resMsg) {
		this.resMsg = resMsg;
		return this;
	}
}
