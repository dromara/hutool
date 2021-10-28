package cn.hutool.poi.word;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.script.ScriptUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.script.Bindings;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : aspojo
 * @version : 1.0.0.0
 * @date : Created at 10/28/2021
 */
public class WordXParser {
	static String ifPrefix = "$if_";
	int uniqueNum = 1;
	Pattern forEachBeginReg;// forEach开始正则
	Pattern forEachEndReg;// forEach结束正则
	Pattern variableReg;// 变量表达式正则
	Pattern mergeReg;// 合并单元格正则

	Pattern ifBeginReg;//if开始
	Pattern ifEndReg;//if结束

	Map<String, Object> rootContext;// 顶级上下文
	Stack<String> stack = new Stack<>();// 存储forEach
	WordXParser.Scope currentScope;// 当前作用域

	private final XWPFDocument parsedDoc;//编译完的word文档对象

	public XWPFDocument getParsedDoc() {
		return parsedDoc;
	}

	/**
	 * 垂直合并单元格
	 */
	public static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			CTVMerge vmerge = CTVMerge.Factory.newInstance();
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				vmerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				vmerge.setVal(STMerge.CONTINUE);
			}
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			// Try getting the TcPr. Not simply setting an new one every time.
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr != null) {
				tcPr.setVMerge(vmerge);
			} else {
				// only set an new TcPr if there is not one already
				tcPr = CTTcPr.Factory.newInstance();
				tcPr.setVMerge(vmerge);
				cell.getCTTc().setTcPr(tcPr);
			}
		}
	}

	/**
	 * 水平合并单元格
	 */
	public static void mergeCellHorizontally(XWPFTable table, int row, int fromCol, int toCol) {
		for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
			CTHMerge hmerge = CTHMerge.Factory.newInstance();
			if (colIndex == fromCol) {
				// The first merged cell is set with RESTART merge value
				hmerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				hmerge.setVal(STMerge.CONTINUE);
			}
			XWPFTableCell cell = table.getRow(row).getCell(colIndex);
			// Try getting the TcPr. Not simply setting an new one every time.
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr != null) {
				tcPr.setHMerge(hmerge);
			} else {
				// only set an new TcPr if there is not one already
				tcPr = CTTcPr.Factory.newInstance();
				tcPr.setHMerge(hmerge);
				cell.getCTTc().setTcPr(tcPr);
			}
		}
	}

	/**
	 * 拷贝一个table但是没有加入doc
	 */
	public static XWPFTable copyTable(XWPFDocument doc, XWPFTable sourceTable) {
		// Copying a existing table
		// Create a new CTTbl for the
		CTTbl ctTbl = CTTbl.Factory.newInstance();
		// new table
		// Copy the template table's CTTbl
		ctTbl.set(sourceTable.getCTTbl());
		// Create a new table
		// using the CTTbl upon
		return new XWPFTable(ctTbl, doc);
	}

	/**
	 * 拷贝一行
	 */
	public static XWPFTableRow copyTableRow(XWPFTableRow row, XWPFTable targetTable) {
		CTRow ctRow = CTRow.Factory.newInstance();
		ctRow.set(row.getCtRow());
		return new XWPFTableRow(ctRow, targetTable);
	}

	public static void cloneParagraph(XWPFParagraph clone, XWPFParagraph source) {
		clone.getCTP().setPPr(source.getCTP().getPPr());
		cloneNumbering(clone, source);
		for (XWPFRun r : source.getRuns()) {
			XWPFRun nr = clone.createRun();
			cloneRun(nr, r);
		}
	}

	public static void cloneNumbering(XWPFParagraph clone, XWPFParagraph source) {
		XWPFNumbering numbering = source.getDocument().getNumbering();
		int currentIlvl = -1;
		CTNumPr numPr = null;
		// word使用列表的两种方式分别是：
		// 1. 先指定文本的样式（例如先选择文本为一级标题）再指定文本应用的列表样式
		// 2. 直接选择使用标题列表
		// 以下判断就是针对上述两种情况作出的处理
		if ((source.getCTP().getPPr() != null) && (source.getCTP().getPPr().getNumPr() != null)) {
			numPr = source.getCTP().getPPr().getNumPr();
		} else {
			XWPFStyle style = source.getDocument().getStyles().getStyle(source.getStyleID());
			if (style != null && style.getCTStyle() != null && style.getCTStyle().getPPr() != null) {
				numPr = style.getCTStyle().getPPr().getNumPr();
			}
		}
		BigInteger currentNumId = null;
		if (numPr != null) {
			if (numPr.getIlvl() != null) {
				currentIlvl = numPr.getIlvl().getVal().intValue();
			} else {
				currentIlvl = 0;
			}
			if (numPr.getNumId() != null) {
				currentNumId = numPr.getNumId().getVal();
			}
		}

		if ((currentNumId != null) && (numbering != null) && currentIlvl != -1) {
			clone.getCTP().getPPr().setNumPr(numPr);
		}

	}


	public static void cloneRun(XWPFRun clone, XWPFRun source) {
		clone.getCTR().setBrArray((CTBr[]) source.getCTR().getBrList().toArray());
		CTRPr rPr = clone.getCTR().isSetRPr() ? clone.getCTR().getRPr() : clone.getCTR().addNewRPr();
		rPr.set(source.getCTR().getRPr());
		String text = source.getText(0);
		if (StrUtil.isNotEmpty(text)) {
			clone.setText(text);
		}
	}

	public WordXParser(InputStream in, Map<String, Object> context) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		IoUtil.copy(in, bos);

		parsedDoc = new XWPFDocument(new ByteArrayInputStream(bos.toByteArray()));

		List<IBodyElement> bodyElements = parsedDoc.getBodyElements();
		for (int i = bodyElements.size(); i >= 0; i--) {
			parsedDoc.removeBodyElement(i);
		}

		defaultParse(new ByteArrayInputStream(bos.toByteArray()), context);
	}

	public void defaultParse(InputStream in, Map<String, Object> context) throws IOException {
		startParse(in, context
				, "\\{\\$foreach\\(([^\\)\\}]*)\\)\\}"
				, "\\{\\$endfor\\}"
				, "\\{\\$([^\\}]*)\\}"
				, "\\{\\$Merge\\(([^\\)\\}]*)\\)\\}"
				, "\\{\\$if\\(([^\\)\\}]*)\\)\\}"
				, "\\{\\$endif\\}"
		);
		in.close();
	}

	public void startParse(InputStream in, Map<String, Object> context, String forEachBeginReg, String forEachEndReg,
						   String variableReg, String mergeReg, String ifBeginReg, String ifEndReg) throws IOException {


		XWPFDocument doc = new XWPFDocument(in);
		this.rootContext = context;
		this.forEachBeginReg = Pattern.compile(forEachBeginReg);
		this.forEachEndReg = Pattern.compile(forEachEndReg);
		this.variableReg = Pattern.compile(variableReg);
		this.mergeReg = Pattern.compile(mergeReg);
		this.ifBeginReg = Pattern.compile(ifBeginReg);
		this.ifEndReg = Pattern.compile(ifEndReg);
		List<IBodyElement> bodyElements = doc.getBodyElements();
		currentScope = new WordXParser.Scope(WordXParser.Scope.root, null);
		stack.push(currentScope.name);
		//编译模版，构造作用域
		for (IBodyElement ele : bodyElements) {
			boolean shouldSave = true;
			if (ele instanceof XWPFParagraph) {
				//如果当前段落是语法标记（例如forEach），则做两件事情：
				//1. 构建相应的作用域。2. 标记当前段落不参与文档输出
				//如果当前段落不是是语法标记，则应该保留
				XWPFParagraph p = (XWPFParagraph) ele;
				String text = p.getText();
				//这里考虑到一个段落中可以有多个语法标记，所以将每个match都执行一遍
				boolean shouldNotSave = matchBegin(text, this.forEachBeginReg);
				shouldNotSave = shouldNotSave || matchBegin(text, this.ifBeginReg);
				shouldNotSave = shouldNotSave || matchEnd(text, this.forEachEndReg);
				shouldNotSave = shouldNotSave || matchEnd(text, this.ifEndReg);
				shouldSave = !shouldNotSave;
			} else {
				//不是段落的其它元素要保留，例如表格。
				Console.log("{}:{}", "非段落元素", ele.getClass());
			}
			if (shouldSave && currentScope != null) {
				currentScope.addChild(ele);
			}
		}
		//开始从根作用域开始输出文档
		loopScope(WordXParser.Scope.allScopeMap.get(WordXParser.Scope.root), context);

		// 填充页眉和页脚
		fillHeaderAndFooter();
	}

	private void fillHeaderAndFooter() {
		List<XWPFHeader> headerList = parsedDoc.getHeaderList();
		if (CollectionUtil.isNotEmpty(headerList)) {
			for (XWPFHeader header : headerList) {
				fillParagraphList(header.getParagraphs(), rootContext);
			}
		}
		List<XWPFFooter> footerList = parsedDoc.getFooterList();
		if (CollectionUtil.isNotEmpty(footerList)) {
			for (XWPFFooter footer : footerList) {
				fillParagraphList(footer.getParagraphs(), rootContext);
			}
		}
	}

	//匹配开始标记
	private boolean matchBegin(String text, Pattern reg) {
		Matcher matcher = reg.matcher(text);
		if (matcher.find()) {
			String name = matcher.group(1);
			String condition = null;
			if (reg == ifBeginReg) {
				//给if也创造一个作用域，但是它的名称只是一个标识，不代表上下文的名称,我们将在实际解析时处理这个情况
				condition = name;
				name = ifPrefix + getUniqueNum();

			}
			beginNewScope(name, condition);
			return true;
		}
		return false;
	}

	/**
	 * 开始新的作用域
	 */
	private void beginNewScope(String name, String condition) {

		Object[] array = stack.toArray();
		StringBuilder key = new StringBuilder();
		for (Object obj : array) {
			key.append(obj).append(".");
		}
		stack.push(name);
		currentScope = new WordXParser.Scope(key + name, condition);
	}

	/**
	 * 匹配结束标记
	 */
	private boolean matchEnd(String text, Pattern reg) {
		Matcher matcher = reg.matcher(text);
		if (matcher.find()) {
			endScope();
			return true;
		}
		return false;
	}

	private void endScope() {
		stack.pop();
		if (stack.isEmpty()) {

			currentScope = null;
		} else {
			currentScope = WordXParser.Scope.allScopeMap
					.get(currentScope.name.substring(0, currentScope.name.lastIndexOf('.')));
		}
	}

	@SuppressWarnings({"unchecked"})
	private void loopScope(WordXParser.Scope scope, Map<String, Object> context) {
		for (Object child : scope.children) {
			if (child instanceof WordXParser.Scope) {
				WordXParser.Scope childScope = (WordXParser.Scope) child;
				if (childScope.condition != null) {//在作用域内

					try {
						Bindings bindings = ScriptUtil.getGroovyEngine().createBindings();
						bindings.putAll(context);
						boolean ok = (boolean) ScriptUtil.getGroovyEngine().eval(childScope.condition, bindings);
						if (ok) {//if作用域沿用其父作用域
							loopScope(childScope, context);
						}
						continue;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				List<Map<String, Object>> childContextList = (List<Map<String, Object>>) context
						.get(childScope.shortName);
				if (CollectionUtil.isEmpty(childContextList)) {
					// 当子作用域数据为空时不解析子作用域
					Console.error("{}:{}", "子作用域数据为空", childScope.name);
					continue;
				}
				if (isLoopRow(childScope)) {

					loopScope(childScope, childContextList.get(0));
					for (int i = 1; i < childContextList.size(); i++) {
						loopFillRow(childScope, childContextList.get(i));
					}
				} else {
					for (Map<String, Object> childContext : childContextList) {
						loopScope(childScope, childContext);
					}
				}

			} else {
				if (child instanceof XWPFParagraph) {
					// 拷贝一段
					XWPFParagraph newp = parsedDoc.createParagraph();
					cloneParagraph(newp, (XWPFParagraph) child);
					fillParagraph(newp, context);

				} else if (child instanceof XWPFTable) {
					//填充一个表格（或者是一个表头）
					//说明：当向word中先后插入两个表格并紧邻，那么这两个表格就会合并为一个表格，
					//所以在这里先插入一个表头，再后续插入数据行
					XWPFTable newTable = copyTable(parsedDoc, (XWPFTable) child);
					fillTable(newTable, context);
					parsedDoc.setTable(getTablePos(parsedDoc, parsedDoc.createTable()), newTable);
				} else {
					Console.error("{}:{}", "暂未解析", child);
				}
			}

		}


	}

	/**
	 * 是否在构造表格
	 */
	protected boolean isLoopRow(WordXParser.Scope scope) {
		return scope.children.size() == 1 && scope.children.get(0) instanceof XWPFTable;
	}

	private void fillTable(XWPFTable table, Map<String, Object> context) {
		List<XWPFTableRow> rows = table.getRows();
		for (XWPFTableRow row : rows) {
			fillRow(table, row, context);
		}

	}

	/**
	 * 填充表格数据
	 */
	private void loopFillRow(WordXParser.Scope scope, Map<String, Object> context) {
		XWPFTable table = (XWPFTable) scope.eleList.get(0);
		XWPFTableRow row = table.getRow(0);
		int tableIndex = parsedDoc.getTables().size() - 1;

		XWPFTable newTable = parsedDoc.getTables().get(tableIndex);
		XWPFTableRow newRow = copyTableRow(row, newTable);

		List<Map<String, Integer>> mergeList = fillRow(newTable, newRow, context);

		newTable.addRow(newRow);

		newTable = copyTable(parsedDoc, newTable);
		for (Map<String, Integer> map : mergeList) {
			mergeCellVertically(newTable, map.get("cellIndex"), map.get("fromRow"), map.get("toRow"));
		}

		parsedDoc.setTable(tableIndex, newTable);


	}

	private List<Map<String, Integer>> fillRow(XWPFTable table, XWPFTableRow row, Map<String, Object> context) {
		List<Map<String, Integer>> returnValue = new ArrayList<>();
		List<XWPFTableCell> tableCells = row.getTableCells();
		int rowNum = table.getNumberOfRows();
		for (int cellIndex = 0; cellIndex < tableCells.size(); cellIndex++) {
			XWPFTableCell cell = tableCells.get(cellIndex);
			String text = cell.getText();
			Matcher matcher = mergeReg.matcher(text);
			if (matcher.find()) {
				// 需要合并行
				fillMergeCell(cell, context);
				int fromRow = -1;
				for (int rowIndex = rowNum - 1; rowIndex > -1; rowIndex--) {
					// 向上行查看，单元格内容相同就合并。
					XWPFTableRow prevRow = table.getRow(rowIndex);
					XWPFTableCell prevCell = prevRow.getCell(cellIndex);
					String prevText = prevCell.getText();
					String cellText = cell.getText();
					if (StrUtil.equals(cellText, prevText)) {
						// 与上一行的对应列值相同
						fromRow = rowIndex;
					} else {
						// 与上一行内容不相同，跳出循环
						break;
					}
				}
				if (fromRow != -1) {
					// 合并
					Map<String, Integer> map = new HashMap<>(3);
					returnValue.add(map);
					map.put("cellIndex", cellIndex);
					map.put("fromRow", fromRow);
					map.put("toRow", rowNum);
				}
			} else {
				fillCell(cell, context);
			}

		}
		return returnValue;

	}

	private void fillCell(XWPFTableCell cell, Map<String, Object> context) {
		List<XWPFParagraph> paragraphs = cell.getParagraphs();
		for (XWPFParagraph paragraph : paragraphs) {
			fillParagraph(paragraph, context);
		}
	}

	private void fillMergeCell(XWPFTableCell cell, Map<String, Object> context) {
		List<XWPFParagraph> paragraphs = cell.getParagraphs();
		for (XWPFParagraph paragraph : paragraphs) {
			changeParagraphText(paragraph, replaceText(paragraph.getParagraphText(), mergeReg, context));
		}
	}

	private void fillParagraphList(List<XWPFParagraph> paragraphs, Map<String, Object> context) {
		for (XWPFParagraph paragraph : paragraphs) {
			fillParagraph(paragraph, context);
		}
	}

	private void fillParagraph(XWPFParagraph p, Map<String, Object> context) {
		changeParagraphText(p, replaceText(p.getParagraphText(), variableReg, context));
	}

	// 将新文本填充到段落中
	public void changeParagraphText(XWPFParagraph p, List<Object> textList) {
		if (p == null) {
			return;
		}
		List<XWPFRun> runs = p.getRuns();
		if (runs.size() > 0) {
			// 先清空段落原有文本
			for (int i = runs.size() - 1; i > 0; i--) {
				p.removeRun(i);
			}
			XWPFRun run = runs.get(0);
			// 清空run中的字符
			run.setText("", 0);
			for (Object newText : textList) {
				// 当newText为string时直接替换
				if (newText instanceof String) {
					String[] textArr = newText.toString().split("\n");
					for (int i = 0; i < textArr.length; i++) {
						if (i != 0) {
							// 添加换行符
							run.addBreak();
						}

						run.setText(textArr[i]);


					}
				} else {
					// 其他类型，诸如 int，double。。。直接替换
					run.setText(newText + "");
				}

			}
//
		}

	}


	public int getTablePos(XWPFDocument doc, XWPFTable t) {
		return doc.getTablePos(doc.getPosOfTable(t));
	}

	// 根据上下文，将段落文本替换为目标文本，文本可能为一个外部文档。
	private static List<Object> replaceText(String text, Pattern pattern, Map<String, Object> context) {
		List<Object> textList = new ArrayList<>();
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String complete = matcher.group(0);
			String variableName = matcher.group(1);
			Object variableValue = context.get(variableName);
			if (variableValue == null) {
				variableValue = "";
			}
			int si = text.indexOf(complete);
			textList.add(text.substring(0, si));
			textList.add(variableValue);
			text = text.substring(si + complete.length());
		}
		textList.add(text);
		return textList;
	}


	private int getUniqueNum() {
		return uniqueNum++;
	}

	/**
	 * 作用域
	 *
	 * @author 陈林
	 */
	public static class Scope {
		public static final String root = "root";
		String name;
		String shortName;
		String condition;
		private final List<IBodyElement> eleList = new ArrayList<>();
		private final List<Object> children = new ArrayList<>();
		// 所有作用域
		public static Map<String, WordXParser.Scope> allScopeMap = new HashMap<>();
		public static List<IBodyElement> allElementList = new ArrayList<>();

		public Scope(String name, String condition) {
			this.condition = condition;
			if (!StrUtil.equals(root, name)) {
				String parentName;
				if (name.contains(".")) {
					int indexOfSep = name.lastIndexOf('.');
					parentName = name.substring(0, indexOfSep);
					shortName = name.substring(indexOfSep + 1);
				} else {
					parentName = root;
					shortName = name;
				}
				WordXParser.Scope parentScope = allScopeMap.get(parentName);
				parentScope.children.add(this);
			} else {
				shortName = name;
			}

			this.name = name;

			allScopeMap.put(this.name, this);
		}

		public void addChild(IBodyElement ele) {
			eleList.add(ele);
			allElementList.add(ele);
			children.add(ele);
		}

		@Override
		public String toString() {
			return "{shortName:" + shortName + ",child:" + children + "}";
		}

	}
}
