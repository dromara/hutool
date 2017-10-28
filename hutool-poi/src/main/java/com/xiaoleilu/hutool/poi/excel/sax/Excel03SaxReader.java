package com.xiaoleilu.hutool.poi.excel.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.poi.exceptions.POIException;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel2003格式的Sax方式读取器<br>
 * 参考：http://www.cnblogs.com/wshsdlau/p/5643862.html
 * 
 * @author looly
 *
 */
public class Excel03SaxReader implements HSSFListener {

	private POIFSFileSystem fs;

	private int minColumns = -1;
	private int lastRowNumber;
	private int lastColumnNumber;

	/** 如果为公式，true表示输出公式计算后的结果值，false表示输出公式本身 */
	private boolean outputFormulaValues = true;

	/** 用于解析公式 */
	private SheetRecordCollectingListener workbookBuildingListener;

	// excel2003工作薄
	private HSSFWorkbook stubWorkbook;

	// Records we pick up as we process
	private SSTRecord sstRecord;

	private FormatTrackingHSSFListener formatListener;

	private BoundSheetRecord[] orderedBSRs;

	private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();

	// For handling formulas with string results
	private int nextRow;

	private int nextColumn;

	private boolean outputNextStringRecord;

	// 当前行
	private int curRow = 0;

	// 存储行记录的容器
	private List<String> rowCellList = new ArrayList<String>();

	// 表索引
	private int sheetIndex = -1;
	private String sheetName;

	private RowHandler rowHandler;

	/**
	 * 构造
	 * 
	 * @param rowHandler 行处理器
	 */
	public Excel03SaxReader(RowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}
	
	/**
	 * 获取Sheet名
	 * @return Sheet名
	 */
	public String getSheetName() {
		return this.sheetName;
	}

	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @param path 文件
	 */
	public void read(String path) {
		read(FileUtil.getInputStream(path));
	}

	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @throws IOException
	 */
	public void read(InputStream excelStream) {
		try {
			this.fs = new POIFSFileSystem(excelStream);
			MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
			formatListener = new FormatTrackingHSSFListener(listener);
			HSSFRequest request = new HSSFRequest();
			if (outputFormulaValues) {
				request.addListenerForAllRecords(formatListener);
			} else {
				workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
				request.addListenerForAllRecords(workbookBuildingListener);
			}
			final HSSFEventFactory factory = new HSSFEventFactory();
			factory.processWorkbookEvents(request, fs);
		} catch (IOException e) {
			throw new POIException(e);
		}
	}

	/**
	 * HSSFListener 监听方法，处理 Record
	 */
	@Override
	public void processRecord(Record record) {
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;
		String value = null;
		switch (record.getSid()) {
		case BoundSheetRecord.sid:
			boundSheetRecords.add((BoundSheetRecord) record);
			break;
		case BOFRecord.sid:
			BOFRecord br = (BOFRecord) record;
			if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
				// 如果有需要，则建立子工作薄
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}

				sheetIndex++;
				if (orderedBSRs == null) {
					orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
				}
				sheetName = orderedBSRs[sheetIndex].getSheetname();
			}
			break;

		case SSTRecord.sid:
			sstRecord = (SSTRecord) record;
			break;

		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;
			thisRow = brec.getRow();
			thisColumn = brec.getColumn();
			thisStr = "";
			rowCellList.add(thisColumn, thisStr);
			break;
		case BoolErrRecord.sid: // 单元格为布尔类型
			BoolErrRecord berec = (BoolErrRecord) record;
			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			thisStr = berec.getBooleanValue() + "";
			rowCellList.add(thisColumn, thisStr);
			break;

		case FormulaRecord.sid: // 单元格为公式类型
			FormulaRecord frec = (FormulaRecord) record;
			thisRow = frec.getRow();
			thisColumn = frec.getColumn();
			if (outputFormulaValues) {
				if (Double.isNaN(frec.getValue())) {
					// Formula result is a string
					// This is stored in the next record
					outputNextStringRecord = true;
					nextRow = frec.getRow();
					nextColumn = frec.getColumn();
				} else {
					thisStr = formatListener.formatNumberDateCell(frec);
				}
			} else {
				thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
			}
			rowCellList.add(thisColumn, thisStr);
			break;
		case StringRecord.sid:// 单元格中公式的字符串
			if (outputNextStringRecord) {
				// String for formula
				StringRecord srec = (StringRecord) record;
				thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;
		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;
			curRow = thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			value = lrec.getValue();
			value = StrUtil.isBlank(value) ? StrUtil.SPACE : value.trim();
			this.rowCellList.add(thisColumn, value);
			break;
		case LabelSSTRecord.sid: // 单元格为字符串类型
			LabelSSTRecord lsrec = (LabelSSTRecord) record;
			curRow = thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			if (sstRecord == null) {
				rowCellList.add(thisColumn, StrUtil.SPACE);
			} else {
				value = sstRecord.getString(lsrec.getSSTIndex()).toString();
				value = StrUtil.isBlank(value) ? StrUtil.SPACE : value.trim();
				rowCellList.add(thisColumn, value);
			}
			break;
		case NumberRecord.sid: // 单元格为数字类型
			NumberRecord numrec = (NumberRecord) record;
			curRow = thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();
			value = formatListener.formatNumberDateCell(numrec);
			value = StrUtil.isBlank(value) ? StrUtil.SPACE : value.trim();
			// 向容器加入列值
			rowCellList.add(thisColumn, value);
			break;
		default:
			break;
		}

		// 遇到新行的操作
		if (thisRow != -1 && thisRow != lastRowNumber) {
			lastColumnNumber = -1;
		}

		// 空值的操作
		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			curRow = thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			rowCellList.add(thisColumn, StrUtil.SPACE);
		}

		// 更新行和列的值
		if (thisRow > -1) {
			lastRowNumber = thisRow;
		}
		if (thisColumn > -1) {
			lastColumnNumber = thisColumn;
		}

		// 行结束时的操作
		if (record instanceof LastCellOfRowDummyRecord) {
			if (minColumns > 0) {
				// 列值重新置空
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
			}
			lastColumnNumber = -1;

			// 每行结束时， 调用getRows() 方法
			rowHandler.handle(sheetIndex, curRow, rowCellList);
			// 清空容器
			rowCellList.clear();
		}
	}
}
