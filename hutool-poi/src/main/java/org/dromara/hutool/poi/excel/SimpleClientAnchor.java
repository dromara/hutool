/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel;

import org.apache.poi.ss.usermodel.ClientAnchor;

/**
 * 简单的ClientAnchor实现，此对象表示一个图形或绘图在Excel中的位置和大小，参数表示：
 * <ul>
 *     <li>dx1和dy1表示在左上角单元格中的偏移量，col1和row1表示左上角单元格</li>
 *     <li>dx2和dy2表示在右下角单元格中的偏移量，col2和row2表示右下角单元格</li>
 * </ul>
 * 图示见：https://www.cnblogs.com/sunyl/p/7527703.html
 */
public class SimpleClientAnchor implements ClientAnchor {

	private int dx1;
	private int dy1;
	private int dy2;
	private int dx2;

	private int col1;
	private int row1;
	private int col2;
	private int row2;

	private AnchorType anchorType = AnchorType.MOVE_AND_RESIZE;

	/**
	 * 构造<br>
	 * 通过定义左上角和右下角的单元格，创建一个ClientAnchor对象<br>
	 * 默认偏移为0，默认type为： {@link AnchorType#MOVE_AND_RESIZE}.
	 *
	 * @param col1 指定起始的列，下标从0开始
	 * @param row1 指定起始的行，下标从0开始
	 * @param col2 指定结束的列，下标从0开始
	 * @param row2 指定结束的行，下标从0开始
	 */
	public SimpleClientAnchor(final int col1, final int row1, final int col2, final int row2) {
		this(0, 0, 0, 0, col1, row1, col2, row2);
	}

	/**
	 * 构造<br>
	 * 通过定义左上角和右下角的单元格，以及单元格中的偏移量，创建一个ClientAnchor对象<br>
	 * 默认type为： {@link AnchorType#MOVE_AND_RESIZE}.
	 *
	 * @param dx1  起始单元格中的x偏移像素
	 * @param dy1  起始单元格中的y偏移像素
	 * @param dx2  结束单元格中的x偏移像素
	 * @param dy2  结束单元格中的y偏移像素
	 * @param col1 指定起始的列，下标从0开始
	 * @param row1 指定起始的行，下标从0开始
	 * @param col2 指定结束的列，下标从0开始
	 * @param row2 指定结束的行，下标从0开始
	 */
	public SimpleClientAnchor(final int dx1, final int dy1, final int dx2, final int dy2, final int col1, final int row1, final int col2, final int row2) {
		this.dx1 = dx1;
		this.dy1 = dy1;
		this.dx2 = dx2;
		this.dy2 = dy2;
		this.col1 = col1;
		this.row1 = row1;
		this.col2 = col2;
		this.row2 = row2;
	}

	@Override
	public int getDx1() {
		return this.dx1;
	}

	@Override
	public void setDx1(final int dx1) {
		this.dx1 = dx1;
	}

	@Override
	public int getDy1() {
		return this.dy1;
	}

	@Override
	public void setDy1(final int dy1) {
		this.dy1 = dy1;
	}

	@Override
	public int getDx2() {
		return this.dx2;
	}

	@Override
	public void setDx2(final int dx2) {
		this.dx2 = dx2;
	}

	@Override
	public int getDy2() {
		return this.dy2;
	}

	@Override
	public void setDy2(final int dy2) {
		this.dy2 = dy2;
	}

	@Override
	public short getCol1() {
		return (short) this.col1;
	}

	@Override
	public void setCol1(final int col1) {
		this.col1 = col1;
	}

	@Override
	public int getRow1() {
		return this.row1;
	}

	@Override
	public void setRow1(final int row1) {
		this.row1 = row1;
	}

	@Override
	public short getCol2() {
		return (short) this.col2;
	}

	@Override
	public void setCol2(final int col2) {
		this.col2 = col2;
	}

	@Override
	public int getRow2() {
		return this.row2;
	}

	@Override
	public void setRow2(final int row2) {
		this.row2 = row2;
	}

	@Override
	public AnchorType getAnchorType() {
		return this.anchorType;
	}

	@Override
	public void setAnchorType(final AnchorType anchorType) {
		this.anchorType = anchorType;
	}

	/**
	 * 将当前对象中的值复制到目标对象中
	 *
	 * @param clientAnchor {@link ClientAnchor}
	 * @return 目标对象
	 */
	public ClientAnchor copyTo(final ClientAnchor clientAnchor) {
		clientAnchor.setDx1(this.dx1);
		clientAnchor.setDy1(this.dy1);
		clientAnchor.setDx2(this.dx2);
		clientAnchor.setDy2(this.dy2);
		clientAnchor.setCol1(this.col1);
		clientAnchor.setRow1(this.row1);
		clientAnchor.setCol2(this.col2);
		clientAnchor.setRow2(this.row2);
		clientAnchor.setAnchorType(this.anchorType);
		return clientAnchor;
	}
}
