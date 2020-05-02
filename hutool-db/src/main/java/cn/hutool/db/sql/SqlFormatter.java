package cn.hutool.db.sql;

import java.util.*;

/**
 * SQL格式化器 from Hibernate
 * @author looly
 */
public class SqlFormatter {
	private static final Set<String> BEGIN_CLAUSES = new HashSet<>();
	private static final Set<String> END_CLAUSES = new HashSet<>();
	private static final Set<String> LOGICAL = new HashSet<>();
	private static final Set<String> QUANTIFIERS = new HashSet<>();
	private static final Set<String> DML = new HashSet<>();
	private static final Set<String> MISC = new HashSet<>();
	
	static {
		BEGIN_CLAUSES.add("left");
		BEGIN_CLAUSES.add("right");
		BEGIN_CLAUSES.add("inner");
		BEGIN_CLAUSES.add("outer");
		BEGIN_CLAUSES.add("group");
		BEGIN_CLAUSES.add("order");
		
		END_CLAUSES.add("where");
		END_CLAUSES.add("set");
		END_CLAUSES.add("having");
		END_CLAUSES.add("join");
		END_CLAUSES.add("from");
		END_CLAUSES.add("by");
		END_CLAUSES.add("into");
		END_CLAUSES.add("union");
		
		LOGICAL.add("and");
		LOGICAL.add("or");
		LOGICAL.add("when");
		LOGICAL.add("else");
		LOGICAL.add("end");
		
		QUANTIFIERS.add("in");
		QUANTIFIERS.add("all");
		QUANTIFIERS.add("exists");
		QUANTIFIERS.add("some");
		QUANTIFIERS.add("any");
		
		DML.add("insert");
		DML.add("update");
		DML.add("delete");
		
		MISC.add("select");
		MISC.add("on");
	}
	
	private static final String indentString = "    ";
	private static final String initial = "\n    ";

	public static String format(String source) {
		return new FormatProcess(source).perform().trim();
	}
	
	//------------------------------------------------------------------------------------------------

	private static class FormatProcess {
		boolean beginLine = true;
		boolean afterBeginBeforeEnd = false;
		boolean afterByOrSetOrFromOrSelect = false;
//		boolean afterValues = false;
		boolean afterOn = false;
		boolean afterBetween = false;
		boolean afterInsert = false;
		int inFunction = 0;
		int parensSinceSelect = 0;
		private final LinkedList<Integer> parenCounts = new LinkedList<>();
		private final LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<>();

		int indent = 1;

		StringBuffer result = new StringBuffer();
		StringTokenizer tokens;
		String lastToken;
		String token;
		String lcToken;

		public FormatProcess(String sql) {
			this.tokens = new StringTokenizer(sql, "()+*/-=<>'`\"[], \n\r\f\t", true);
		}

		public String perform() {
			this.result.append(initial);

			while (this.tokens.hasMoreTokens()) {
				this.token = this.tokens.nextToken();
				this.lcToken = this.token.toLowerCase();

				if ("'".equals(this.token)) {
					String t;
					do {
						t = this.tokens.nextToken();
						this.token += t;
					} while ((!"'".equals(t)) && (this.tokens.hasMoreTokens()));
				} else if ("\"".equals(this.token)) {
					String t;
					do {
						t = this.tokens.nextToken();
						this.token += t;
					} while (!"\"".equals(t));
				}

				if ((this.afterByOrSetOrFromOrSelect) && (",".equals(this.token))) {
					commaAfterByOrFromOrSelect();
				} else if ((this.afterOn) && (",".equals(this.token))) {
					commaAfterOn();
				} else if ("(".equals(this.token)) {
					openParen();
				} else if (")".equals(this.token)) {
					closeParen();
				} else if (BEGIN_CLAUSES.contains(this.lcToken)) {
					beginNewClause();
				} else if (END_CLAUSES.contains(this.lcToken)) {
					endNewClause();
				} else if ("select".equals(this.lcToken)) {
					select();
				} else if (DML.contains(this.lcToken)) {
					updateOrInsertOrDelete();
				} else if ("values".equals(this.lcToken)) {
					values();
				} else if ("on".equals(this.lcToken)) {
					on();
				} else if ((this.afterBetween) && ("and".equals(this.lcToken))) {
					misc();
					this.afterBetween = false;
				} else if (LOGICAL.contains(this.lcToken)) {
					logical();
				} else if (isWhitespace(this.token)) {
					white();
				} else {
					misc();
				}

				if (false == isWhitespace(this.token)) {
					this.lastToken = this.lcToken;
				}
			}

			return this.result.toString();
		}

		private void commaAfterOn() {
			out();
			this.indent -= 1;
			newline();
			this.afterOn = false;
			this.afterByOrSetOrFromOrSelect = true;
		}

		private void commaAfterByOrFromOrSelect() {
			out();
			newline();
		}

		private void logical() {
			if ("end".equals(this.lcToken)) {
				this.indent -= 1;
			}
			newline();
			out();
			this.beginLine = false;
		}

		private void on() {
			this.indent += 1;
			this.afterOn = true;
			newline();
			out();
			this.beginLine = false;
		}

		private void misc() {
			out();
			if ("between".equals(this.lcToken)) {
				this.afterBetween = true;
			}
			if (this.afterInsert) {
				newline();
				this.afterInsert = false;
			} else {
				this.beginLine = false;
				if ("case".equals(this.lcToken)) {
					this.indent += 1;
				}
			}
		}

		private void white() {
			if (!this.beginLine) {
				this.result.append(" ");
			}
		}

		private void updateOrInsertOrDelete() {
			out();
			this.indent += 1;
			this.beginLine = false;
			if ("update".equals(this.lcToken)) {
				newline();
			}
			if ("insert".equals(this.lcToken)) {
				this.afterInsert = true;
			}
		}

		private void select() {
			out();
			this.indent += 1;
			newline();
			this.parenCounts.addLast(this.parensSinceSelect);
			this.afterByOrFromOrSelects.addLast(this.afterByOrSetOrFromOrSelect);
			this.parensSinceSelect = 0;
			this.afterByOrSetOrFromOrSelect = true;
		}

		private void out() {
			this.result.append(this.token);
		}

		private void endNewClause() {
			if (!this.afterBeginBeforeEnd) {
				this.indent -= 1;
				if (this.afterOn) {
					this.indent -= 1;
					this.afterOn = false;
				}
				newline();
			}
			out();
			if (!"union".equals(this.lcToken)) {
				this.indent += 1;
			}
			newline();
			this.afterBeginBeforeEnd = false;
			this.afterByOrSetOrFromOrSelect = (("by".equals(this.lcToken)) || ("set".equals(this.lcToken)) || ("from".equals(this.lcToken)));
		}

		private void beginNewClause() {
			if (!this.afterBeginBeforeEnd) {
				if (this.afterOn) {
					this.indent -= 1;
					this.afterOn = false;
				}
				this.indent -= 1;
				newline();
			}
			out();
			this.beginLine = false;
			this.afterBeginBeforeEnd = true;
		}

		private void values() {
			this.indent -= 1;
			newline();
			out();
			this.indent += 1;
			newline();
//			this.afterValues = true;
		}

		private void closeParen() {
			this.parensSinceSelect -= 1;
			if (this.parensSinceSelect < 0) {
				this.indent -= 1;
				this.parensSinceSelect = this.parenCounts.removeLast();
				this.afterByOrSetOrFromOrSelect = this.afterByOrFromOrSelects.removeLast();
			}
			if (this.inFunction > 0) {
				this.inFunction -= 1;
			} else {
				if (!this.afterByOrSetOrFromOrSelect) {
					this.indent -= 1;
					newline();
				}
			}
			out();
			this.beginLine = false;
		}

		private void openParen() {
			if ((isFunctionName(this.lastToken)) || (this.inFunction > 0)) {
				this.inFunction += 1;
			}
			this.beginLine = false;
			if (this.inFunction > 0) {
				out();
			} else {
				out();
				if (!this.afterByOrSetOrFromOrSelect) {
					this.indent += 1;
					newline();
					this.beginLine = true;
				}
			}
			this.parensSinceSelect += 1;
		}

		private static boolean isFunctionName(String tok) {
			char begin = tok.charAt(0);
			boolean isIdentifier = (Character.isJavaIdentifierStart(begin)) || ('"' == begin);
			return (isIdentifier) && (!LOGICAL.contains(tok)) && (!END_CLAUSES.contains(tok)) && (!QUANTIFIERS.contains(tok)) && (!DML.contains(tok)) && (!MISC.contains(tok));
		}

		private static boolean isWhitespace(String token) {
			return " \n\r\f\t".contains(token);
		}

		private void newline() {
			this.result.append("\n");
			for (int i = 0; i < this.indent; i++) {
				this.result.append(indentString);
			}
			this.beginLine = true;
		}
	}
}
