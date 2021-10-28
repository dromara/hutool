package cn.hutool.poi.word;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : lin.chen1
 * @version : 1.0.0.0
 * @date : Created at 2021/10/28
 */
public class WorldXParserTest {
	@Test
	public void testMergeCell() throws Exception {

		@SuppressWarnings("resource")
		XWPFDocument document = new XWPFDocument();

		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("The table:");

		// create table
		XWPFTable table = document.createTable(3, 5);

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 5; col++) {
				table.getRow(row).getCell(col).setText("row " + row + ", col " + col);
			}
		}

		// create and set column widths for all columns in all rows
		// most examples don't set the type of the CTTblWidth but this
		// is necessary for working in all office versions
		for (int col = 0; col < 5; col++) {
			CTTblWidth tblWidth = CTTblWidth.Factory.newInstance();
			tblWidth.setW(BigInteger.valueOf(1000));
			tblWidth.setType(STTblWidth.DXA);
			for (int row = 0; row < 3; row++) {
				CTTcPr tcPr = table.getRow(row).getCell(col).getCTTc().getTcPr();
				if (tcPr != null) {
					tcPr.setTcW(tblWidth);
				} else {
					tcPr = CTTcPr.Factory.newInstance();
					tcPr.setTcW(tblWidth);
					table.getRow(row).getCell(col).getCTTc().setTcPr(tcPr);
				}
			}
		}

		// using the merge methods
		WorldXParser.mergeCellVertically(table, 0, 0, 1);
		WorldXParser.mergeCellVertically(table, 0, 1, 2);
		WorldXParser.mergeCellHorizontally(table, 1, 2, 3);
		WorldXParser.mergeCellHorizontally(table, 2, 1, 4);

		FileOutputStream out = new FileOutputStream("create_table.docx");
		document.write(out);

		System.out.println("create_table.docx written successully");
	}

	/***
	 * 测试根据模板导出报表
	 *
	 */
	@Test
	public void testExportReport() throws Exception {
		String testDir = System.getProperty("user.dir") + "/hot-deploy/websdt/template/";
		FileInputStream in = new FileInputStream(testDir + "relationExport.docx");


		Map<String, Object> context = new HashMap<>();
		Map<String, Object> relation = new HashMap<>();
		context.put("list", CollectionUtil.toList(relation));


		relation.put("upstreamDocName", "需求文档");
		relation.put("docName", "设计文档");
		relation.put("upstreamDocTitle", "需求文档");
		relation.put("docTitle", "设计文档");
		relation.put("upReqTitle", "需求");
		relation.put("upSectionTitle", "章节");
		relation.put("reqTitle", "标识");
		relation.put("sectionTitle", "章节");

		Map<String, Object> req = new HashMap<>();
		relation.put("reqList", CollectionUtil.toList(req, req));

		req.put("reqName", "设计1");
		req.put("section", "标题1");
		req.put("upReqName", "需求1");
		req.put("upSection", "标题1");

		WorldXParser worldXParser = new WorldXParser(in, context);
		XWPFDocument doc = worldXParser.parsedDoc;
		FileOutputStream out = new FileOutputStream(testDir + "result2.docx");
		doc.write(out);

		out.flush();
		out.close();
	}
}
