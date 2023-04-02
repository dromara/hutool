package org.dromara.hutool.test.bean.report;

import org.dromara.hutool.text.StrUtil;

/**
 * 测试步骤报告
 * @author xuwangcheng
 * @version 20181012
 *
 */
public class StepReport {
	private static int step_id = 0;

	private int stepId = getId();
	/**
	 * 步骤名称
	 */
	private String stepName;
	/**
	 * 元素名称
	 */
	private String elementName;
	/**
	 * 元素定位器
	 */
	private String location;
	/**
	 * 参数
	 */
	private String params;
	/**
	 * 结果
	 */
	private String result;
	/**
	 * 操作名称，中文
	 */
	private String actionName;
	/**
	 * 测试时间
	 */
	private String testTime;

	/**
	 * 测试状态：true-成功 false-失败
	 */
	private boolean status = true;
	/**
	 * 备注信息
	 */
	private String mark;
	/**
	 * 截图路径：相对路径
	 */
	private String screenshot;

	private static synchronized int getId() {
		return step_id++;
	}

	public int getStepId() {
		return stepId;
	}

	public void setStepId(final int stepId) {
		this.stepId = stepId;
	}

	public void setResult(final String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(final String stepName) {
		this.stepName = stepName;
	}

	public void setStepName() {
		this.stepName = this.actionName + (StrUtil.isBlank(this.elementName) ? "" : " => " + this.elementName);
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(final String elementName) {
		this.elementName = elementName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public String getParams() {
		return params;
	}

	public void setParams(final String params) {
		this.params = params;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(final String testTime) {
		this.testTime = testTime;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(final boolean status) {
		this.status = status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(final String mark) {
		this.mark = mark;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(final String screenshot) {
		this.screenshot = screenshot;
	}

	@Override
	public String toString() {
		return "StepReport [stepId=" + stepId + ", stepName=" + stepName + ", elementName=" + elementName
				+ ", location=" + location + ", params=" + params + ", result=" + result + ", actionName=" + actionName
				+ ", testTime=" + testTime + ", status=" + status + ", mark=" + mark + ", screenshot=" + screenshot
				+ "]";
	}
}
