package cn.hutool.core.text;

/**
 * 静态 String 池,方便业务工程中直接使用,而不是声明的每个类都是魔术值
 *
 * @author senssic
 */
public interface StrPool {
  String HTTPS = "https";
  String HTTP = "http";
  String AMPERSAND = "&";
  String AND = "and";
  String OR = "or";
  String AT = "@";
  String ASTERISK = "*";
  String STAR = ASTERISK;
  char SLASH = '/';
  char BACK_SLASH = '\\';
  String DOUBLE_SLASH = "//";
  String COLON = ":";
  String COMMA = ",";
  String DASH = "-";
  String DOLLAR = "$";
  String DOT = ".";
  String EMPTY = "";
  String EMPTY_JSON = "{}";
  String EQUALS = "=";
  String FALSE = "false";
  String HASH = "#";
  String HAT = "^";
  String LEFT_BRACE = "{";
  String LEFT_BRACKET = "(";
  String LEFT_CHEV = "<";
  String NEWLINE = "\n";
  String N = "n";
  String NO = "no";
  String NULL = "null";
  String OFF = "off";
  String ON = "on";
  String PERCENT = "%";
  String PIPE = "|";
  String PLUS = "+";
  String QUESTION_MARK = "?";
  String EXCLAMATION_MARK = "!";
  String QUOTE = "\"";
  String RETURN = "\r";
  String TAB = "\t";
  String RIGHT_BRACE = "}";
  String RIGHT_BRACKET = ")";
  String RIGHT_CHEV = ">";
  String SEMICOLON = ";";
  String SINGLE_QUOTE = "'";
  String BACKTICK = "`";
  String SPACE = " ";
  String TILDA = "~";
  String LEFT_SQ_BRACKET = "[";
  String RIGHT_SQ_BRACKET = "]";
  String TRUE = "true";
  String UNDERSCORE = "_";
  String UTF_8 = "UTF-8";
  String GBK = "GBK";
  String ISO_8859_1 = "ISO-8859-1";
  String Y = "y";
  String YES = "yes";
  String ONE = "1";
  String ZERO = "0";
  String DOLLAR_LEFT_BRACE = "${";
  char U_A = 'A';
  char L_A = 'a';
  char U_Z = 'Z';
  char L_Z = 'z';
}
