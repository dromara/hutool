/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.poi.excel.cell;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 当单元格不存在时使用此对象表示，得到的值都为null,此对象只用于标注单元格所在位置信息。
 *
 * @author looly
 * @since 5.5.0
 */
public class NullCell implements Cell {

	private final Row row;
	private final int columnIndex;

	/**
	 * 构造
	 *
	 * @param row         行
	 * @param columnIndex 列号，从0开始
	 */
	public NullCell(final Row row, final int columnIndex) {
		this.row = row;
		this.columnIndex = columnIndex;
	}

	@Override
	public int getColumnIndex() {
		return this.columnIndex;
	}

	@Override
	public int getRowIndex() {
		return getRow().getRowNum();
	}

	@Override
	public Sheet getSheet() {
		return getRow().getSheet();
	}

	@Override
	public Row getRow() {
		return this.row;
	}

	@Deprecated
	public void setCellType(final CellType cellType) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setBlank() {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public CellType getCellType() {
		return null;
	}

	@Override
	public CellType getCachedFormulaResultType() {
		return null;
	}

	@Override
	public void setCellValue(final double value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellValue(final Date value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellValue(final LocalDateTime value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellValue(final Calendar value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellValue(final RichTextString value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellValue(final String value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellFormula(final String formula) throws FormulaParseException, IllegalStateException {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void removeFormula() throws IllegalStateException {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public String getCellFormula() {
		return null;
	}

	@Override
	public double getNumericCellValue() {
		throw new UnsupportedOperationException("Cell value is null!");
	}

	@Override
	public Date getDateCellValue() {
		return null;
	}

	@Override
	public LocalDateTime getLocalDateTimeCellValue() {
		return null;
	}

	@Override
	public RichTextString getRichStringCellValue() {
		return null;
	}

	@Override
	public String getStringCellValue() {
		return null;
	}

	@Override
	public void setCellValue(final boolean value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void setCellErrorValue(final byte value) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public boolean getBooleanCellValue() {
		throw new UnsupportedOperationException("Cell value is null!");
	}

	@Override
	public byte getErrorCellValue() {
		throw new UnsupportedOperationException("Cell value is null!");
	}

	@Override
	public void setCellStyle(final CellStyle style) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public CellStyle getCellStyle() {
		return null;
	}

	@Override
	public void setAsActiveCell() {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public CellAddress getAddress() {
		return null;
	}

	@Override
	public void setCellComment(final Comment comment) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public Comment getCellComment() {
		return null;
	}

	@Override
	public void removeCellComment() {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public Hyperlink getHyperlink() {
		return null;
	}

	@Override
	public void setHyperlink(final Hyperlink link) {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public void removeHyperlink() {
		throw new UnsupportedOperationException("Can not set any thing to null cell!");
	}

	@Override
	public CellRangeAddress getArrayFormulaRange() {
		return null;
	}

	@Override
	public boolean isPartOfArrayFormulaGroup() {
		throw new UnsupportedOperationException("Cell value is null!");
	}
}
