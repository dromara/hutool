package com.xiaoleilu.hutool.poi.excel.sax;

import java.io.File;
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
import com.xiaoleilu.hutool.poi.excel.ExcelUtil;
import com.xiaoleilu.hutool.poi.exceptions.POIException;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel2003格式的事件-用户模型方式读取器，在Hutool中，统一将此归类为Sax读取<br>
 * 参考：http://www.cnblogs.com/wshsdlau/p/5643862.html
 * 
 * @author looly
 *
 */
public class Excel03SaxReader implements HSSFListener {

	private POIFSFileSystem fs;

	/** 如果为公式，true表示输出公式计算后的结果值，false表示输出公式本身 */
	private boolean isOutputFormulaValues = true;
	
	/** 用于解析公式 */
	private SheetRecordCollectingListener workbookBuildingListener;
	/** 子工作簿，用于公式计算 */
	private HSSFWorkbook stubWorkbook;

	/** 静态字符串表 */
	private SSTRecord sstRecord;

	private FormatTrackingHSSFListener formatListener;

	/** Sheet边界记录，此Record中可以获得Sheet名 */
	private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();

	private boolean isOutputNextStringRecord;

	// 存储行记录的容器
	private List<Object> rowCellList = new ArrayList<>();

	/** 自定义需要处理的sheet编号，如果-1表示处理所有sheet */
	private int sheetIndex = -1;
	// 当前表索引
	private int curSheetIndex = -1;

	private RowHandler rowHandler;

	/**
	 * 构造
	 * 
	 * @param rowHandler 行处理器
	 */
	public Excel03SaxReader(RowHandler rowHandler) {
		this.rowHandler = rowHandler;
	}

	// ------------------------------------------------------------------------------ Read start
	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @param path 文件路径，可以为相对ClassPath路径，也可以是绝对路径
	 * @param sheetIndex sheet序列号，如果为-1表示处理所有sheet
	 * @throws POIException IO异常包装
	 */
	public void read(String path, int sheetIndex) throws POIException {
		read(FileUtil.getInputStream(path), sheetIndex);
	}

	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @param file 文件
	 * @param sheetIndex sheet序列号，如果为-1表示处理所有sheet
	 * @throws POIException IO异常包装
	 */
	public void read(File file, int sheetIndex) throws POIException {
		read(FileUtil.getInputStream(file), sheetIndex);
	}

	/**
	 * 遍历excel下所有的sheet
	 * 
	 * @param excelStream Excel文件流
	 * @param sheetIndex sheet序列号，如果为-1表示处理所有sheet
	 * @throws POIException IO异常包装
	 */
	public void read(InputStream excelStream, int sheetIndex) throws POIException {
		this.sheetIndex = sheetIndex;
		try {
			this.fs = new POIFSFileSystem(excelStream);
			formatListener = new FormatTrackingHSSFListener(new MissingRecordAwareHSSFListener(this));
			final HSSFRequest request = new HSSFRequest();
			if (isOutputFormulaValues) {
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
	// ------------------------------------------------------------------------------ Read end

	/**
	 * 获得Sheet序号，如果处理所有sheet，获得最大的Sheet序号，从0开始
	 * @return sheet序号
	 */
	public int getSheetIndex() {
		return this.sheetIndex;
	}

	/**
	 * 获得Sheet名，如果处理所有sheet，获得后一个Sheet名，从0开始
	 * 
	 * @return Sheet名
	 */
	public String getSheetName() {
		if (this.boundSheetRecords.size() > this.sheetIndex) {
			return this.boundSheetRecords.get(this.sheetIndex).getSheetname();
		}
		return null;
	}

	/**
	 * HSSFListener 监听方法，处理 Record
	 * 
	 * @param record 记录
	 */
	@Override
	public void processRecord(Record record) {
		String value = null;

		switch (record.getSid()) {
		case BoundSheetRecord.sid:
			// Sheet边界记录，此Record中可以获得Sheet名
			boundSheetRecords.add((BoundSheetRecord) record);
			break;
		case BOFRecord.sid:
			BOFRecord bofRecord = (BOFRecord) record;
			if (bofRecord.getType() == BOFRecord.TYPE_WORKSHEET) {
				// 如果有需要，则建立子工作薄
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}
				curSheetIndex++;
			}
			break;
		case SSTRecord.sid:
			// 静态字符串表
			sstRecord = (SSTRecord) record;
			break;
		case BlankRecord.sid:
			// 空白记录
			BlankRecord brec = (BlankRecord) record;
			rowCellList.add(brec.getColumn(), StrUtil.EMPTY);
			break;
		case BoolErrRecord.sid: // 单元格为布尔类型
			BoolErrRecord berec = (BoolErrRecord) record;
			rowCellList.add(berec.getColumn(), berec.getBooleanValue());
			break;
		case FormulaRecord.sid: // 单元格为公式类型
			FormulaRecord frec = (FormulaRecord) record;
			if (isOutputFormulaValues) {
				if (Double.isNaN(frec.getValue())) {
					// Formula result is a string
					// This is stored in the next record
					isOutputNextStringRecord = true;
				} else {
					value = formatListener.formatNumberDateCell(frec);
				}
			} else {
				value = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
			}
			rowCellList.add(frec.getColumn(), value);
			break;
		case StringRecord.sid:// 单元格中公式的字符串
			if (isOutputNextStringRecord) {
				// String for formula
				StringRecord srec = (StringRecord) record;
				value = srec.getString();
				isOutputNextStringRecord = false;
			}
			break;
		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;
			value = StrUtil.nullToEmpty(lrec.getValue()).trim();
			this.rowCellList.add(lrec.getColumn(), value);
			break;
		case LabelSSTRecord.sid: // 单元格为字符串类型
			LabelSSTRecord lsrec = (LabelSSTRecord) record;
			if (sstRecord == null) {
				rowCellList.add(lsrec.getColumn(), StrUtil.EMPTY);
			} else {
				value = sstRecord.getString(lsrec.getSSTIndex()).toString();
				value = StrUtil.nullToEmpty(value).trim();
				rowCellList.add(lsrec.getColumn(), value);
			}
			break;
		case NumberRecord.sid: // 单元格为数字类型
			NumberRecord numrec = (NumberRecord) record;
//			value = formatListener.formatNumberDateCell(numrec);
//			value = StrUtil.nullToEmpty(value).trim();
			
			// Get the built in format, if there is one
			final int formatIndex = formatListener.getFormatIndex(numrec);
			final String formatString = formatListener.getFormatString(numrec);
			//数字类型转为Date、Double或者Long，Date类型要考虑是否基于1904年，在此统一设定为否
			final Object valueObj = ExcelUtil.getHSSFNumericValue(numrec.getValue(), formatIndex, formatString, false);
			
			// 向容器加入列值
			rowCellList.add(numrec.getColumn(), valueObj);
			break;
		default:
			break;
		}

		// 空值的操作
		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			rowCellList.add(mc.getColumn(), StrUtil.EMPTY);
		}

		// 行结束时的操作
		if (record instanceof LastCellOfRowDummyRecord) {
			processLastCell((LastCellOfRowDummyRecord) record);
		}
	}

	// ---------------------------------------------------------------------------------------------- Private method start
	/**
	 * 处理行结束后的操作，{@link LastCellOfRowDummyRecord}是行结束的标识Record
	 * 
	 * @param lastCell 行结束的标识Record
	 */
	private void processLastCell(LastCellOfRowDummyRecord lastCell) {
		// 每行结束时， 调用handle() 方法
		this.rowHandler.handle(curSheetIndex, lastCell.getRow(), this.rowCellList);
		// 清空行Cache
		this.rowCellList.clear();
	}
	// ---------------------------------------------------------------------------------------------- Private method end
}
