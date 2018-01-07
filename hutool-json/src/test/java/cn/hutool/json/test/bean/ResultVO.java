package cn.hutool.json.test.bean;

import java.io.Serializable;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;

public class ResultVO implements Serializable {
	private static final long serialVersionUID = 2161496499087970946L;

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 处理结果
	 */
	private String data;
	/**
	 * 产品号
	 */
	private String proCode;
	/**
	 * 订单号
	 */
	private String outNo;
	/**
	 * 是否收费 y收费 n 不收费
	 */
	private String fee;

	/**
	 * URL
	 */
	private String url;

	/**
	 *
	 */
	private String msg = "aaaa";

	/**
	 * 错误编号
	 */
	private int code = 2;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
    public String toString() {
        return "ResultVO{" +
                "success=" + success +
                ", data='" + data + '\'' +
                ", proCode='" + proCode + '\'' +
                ", outNo='" + outNo + '\'' +
                ", fee='" + fee + '\'' +
                ", url='" + url + '\'' +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
	
	public static void main(String[] args) {
		ResultVO vo = new ResultVO();
		String str = JSONUtil.toJsonStr(vo);
		Console.log(str);
	}
}