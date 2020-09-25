package cn.hutool.core.text.escape;

import cn.hutool.core.text.replacer.LookupReplacer;
import cn.hutool.core.text.replacer.ReplacerChain;

/**
 * HTML4的ESCAPE
 * 参考：Commons Lang3
 *
 * @author looly
 *
 */
public class Html4Escape extends ReplacerChain {
	private static final long serialVersionUID = 1L;

	protected static final String[][] BASIC_ESCAPE = { //
			{ "\"", "&quot;" }, // " - double-quote
			{ "&", "&amp;" }, // & - ampersand
			{ "<", "&lt;" }, // < - less-than
			{ ">", "&gt;" }, // > - greater-than
	};

	protected static final String[][] ISO8859_1_ESCAPE = { //
			{ "\u00A0", "&nbsp;" }, // non-breaking space
			{ "\u00A1", "&iexcl;" }, // inverted exclamation mark
			{ "\u00A2", "&cent;" }, // cent sign
			{ "\u00A3", "&pound;" }, // pound sign
			{ "\u00A4", "&curren;" }, // currency sign
			{ "\u00A5", "&yen;" }, // yen sign = yuan sign
			{ "\u00A6", "&brvbar;" }, // broken bar = broken vertical bar
			{ "\u00A7", "&sect;" }, // section sign
			{ "\u00A8", "&uml;" }, // diaeresis = spacing diaeresis
			{ "\u00A9", "&copy;" }, // � - copyright sign
			{ "\u00AA", "&ordf;" }, // feminine ordinal indicator
			{ "\u00AB", "&laquo;" }, // left-pointing double angle quotation mark = left pointing guillemet
			{ "\u00AC", "&not;" }, // not sign
			{ "\u00AD", "&shy;" }, // soft hyphen = discretionary hyphen
			{ "\u00AE", "&reg;" }, // � - registered trademark sign
			{ "\u00AF", "&macr;" }, // macron = spacing macron = overline = APL overbar
			{ "\u00B0", "&deg;" }, // degree sign
			{ "\u00B1", "&plusmn;" }, // plus-minus sign = plus-or-minus sign
			{ "\u00B2", "&sup2;" }, // superscript two = superscript digit two = squared
			{ "\u00B3", "&sup3;" }, // superscript three = superscript digit three = cubed
			{ "\u00B4", "&acute;" }, // acute accent = spacing acute
			{ "\u00B5", "&micro;" }, // micro sign
			{ "\u00B6", "&para;" }, // pilcrow sign = paragraph sign
			{ "\u00B7", "&middot;" }, // middle dot = Georgian comma = Greek middle dot
			{ "\u00B8", "&cedil;" }, // cedilla = spacing cedilla
			{ "\u00B9", "&sup1;" }, // superscript one = superscript digit one
			{ "\u00BA", "&ordm;" }, // masculine ordinal indicator
			{ "\u00BB", "&raquo;" }, // right-pointing double angle quotation mark = right pointing guillemet
			{ "\u00BC", "&frac14;" }, // vulgar fraction one quarter = fraction one quarter
			{ "\u00BD", "&frac12;" }, // vulgar fraction one half = fraction one half
			{ "\u00BE", "&frac34;" }, // vulgar fraction three quarters = fraction three quarters
			{ "\u00BF", "&iquest;" }, // inverted question mark = turned question mark
			{ "\u00C0", "&Agrave;" }, // � - uppercase A, grave accent
			{ "\u00C1", "&Aacute;" }, // � - uppercase A, acute accent
			{ "\u00C2", "&Acirc;" }, // � - uppercase A, circumflex accent
			{ "\u00C3", "&Atilde;" }, // � - uppercase A, tilde
			{ "\u00C4", "&Auml;" }, // � - uppercase A, umlaut
			{ "\u00C5", "&Aring;" }, // � - uppercase A, ring
			{ "\u00C6", "&AElig;" }, // � - uppercase AE
			{ "\u00C7", "&Ccedil;" }, // � - uppercase C, cedilla
			{ "\u00C8", "&Egrave;" }, // � - uppercase E, grave accent
			{ "\u00C9", "&Eacute;" }, // � - uppercase E, acute accent
			{ "\u00CA", "&Ecirc;" }, // � - uppercase E, circumflex accent
			{ "\u00CB", "&Euml;" }, // � - uppercase E, umlaut
			{ "\u00CC", "&Igrave;" }, // � - uppercase I, grave accent
			{ "\u00CD", "&Iacute;" }, // � - uppercase I, acute accent
			{ "\u00CE", "&Icirc;" }, // � - uppercase I, circumflex accent
			{ "\u00CF", "&Iuml;" }, // � - uppercase I, umlaut
			{ "\u00D0", "&ETH;" }, // � - uppercase Eth, Icelandic
			{ "\u00D1", "&Ntilde;" }, // � - uppercase N, tilde
			{ "\u00D2", "&Ograve;" }, // � - uppercase O, grave accent
			{ "\u00D3", "&Oacute;" }, // � - uppercase O, acute accent
			{ "\u00D4", "&Ocirc;" }, // � - uppercase O, circumflex accent
			{ "\u00D5", "&Otilde;" }, // � - uppercase O, tilde
			{ "\u00D6", "&Ouml;" }, // � - uppercase O, umlaut
			{ "\u00D7", "&times;" }, // multiplication sign
			{ "\u00D8", "&Oslash;" }, // � - uppercase O, slash
			{ "\u00D9", "&Ugrave;" }, // � - uppercase U, grave accent
			{ "\u00DA", "&Uacute;" }, // � - uppercase U, acute accent
			{ "\u00DB", "&Ucirc;" }, // � - uppercase U, circumflex accent
			{ "\u00DC", "&Uuml;" }, // � - uppercase U, umlaut
			{ "\u00DD", "&Yacute;" }, // � - uppercase Y, acute accent
			{ "\u00DE", "&THORN;" }, // � - uppercase THORN, Icelandic
			{ "\u00DF", "&szlig;" }, // � - lowercase sharps, German
			{ "\u00E0", "&agrave;" }, // � - lowercase a, grave accent
			{ "\u00E1", "&aacute;" }, // � - lowercase a, acute accent
			{ "\u00E2", "&acirc;" }, // � - lowercase a, circumflex accent
			{ "\u00E3", "&atilde;" }, // � - lowercase a, tilde
			{ "\u00E4", "&auml;" }, // � - lowercase a, umlaut
			{ "\u00E5", "&aring;" }, // � - lowercase a, ring
			{ "\u00E6", "&aelig;" }, // � - lowercase ae
			{ "\u00E7", "&ccedil;" }, // � - lowercase c, cedilla
			{ "\u00E8", "&egrave;" }, // � - lowercase e, grave accent
			{ "\u00E9", "&eacute;" }, // � - lowercase e, acute accent
			{ "\u00EA", "&ecirc;" }, // � - lowercase e, circumflex accent
			{ "\u00EB", "&euml;" }, // � - lowercase e, umlaut
			{ "\u00EC", "&igrave;" }, // � - lowercase i, grave accent
			{ "\u00ED", "&iacute;" }, // � - lowercase i, acute accent
			{ "\u00EE", "&icirc;" }, // � - lowercase i, circumflex accent
			{ "\u00EF", "&iuml;" }, // � - lowercase i, umlaut
			{ "\u00F0", "&eth;" }, // � - lowercase eth, Icelandic
			{ "\u00F1", "&ntilde;" }, // � - lowercase n, tilde
			{ "\u00F2", "&ograve;" }, // � - lowercase o, grave accent
			{ "\u00F3", "&oacute;" }, // � - lowercase o, acute accent
			{ "\u00F4", "&ocirc;" }, // � - lowercase o, circumflex accent
			{ "\u00F5", "&otilde;" }, // � - lowercase o, tilde
			{ "\u00F6", "&ouml;" }, // � - lowercase o, umlaut
			{ "\u00F7", "&divide;" }, // division sign
			{ "\u00F8", "&oslash;" }, // � - lowercase o, slash
			{ "\u00F9", "&ugrave;" }, // � - lowercase u, grave accent
			{ "\u00FA", "&uacute;" }, // � - lowercase u, acute accent
			{ "\u00FB", "&ucirc;" }, // � - lowercase u, circumflex accent
			{ "\u00FC", "&uuml;" }, // � - lowercase u, umlaut
			{ "\u00FD", "&yacute;" }, // � - lowercase y, acute accent
			{ "\u00FE", "&thorn;" }, // � - lowercase thorn, Icelandic
			{ "\u00FF", "&yuml;" }, // � - lowercase y, umlaut
	};

	protected static final String[][] HTML40_EXTENDED_ESCAPE = {
			// <!-- Latin Extended-B -->
			{ "\u0192", "&fnof;" }, // latin small f with hook = function= florin, U+0192 ISOtech -->
			// <!-- Greek -->
			{ "\u0391", "&Alpha;" }, // greek capital letter alpha, U+0391 -->
			{ "\u0392", "&Beta;" }, // greek capital letter beta, U+0392 -->
			{ "\u0393", "&Gamma;" }, // greek capital letter gamma,U+0393 ISOgrk3 -->
			{ "\u0394", "&Delta;" }, // greek capital letter delta,U+0394 ISOgrk3 -->
			{ "\u0395", "&Epsilon;" }, // greek capital letter epsilon, U+0395 -->
			{ "\u0396", "&Zeta;" }, // greek capital letter zeta, U+0396 -->
			{ "\u0397", "&Eta;" }, // greek capital letter eta, U+0397 -->
			{ "\u0398", "&Theta;" }, // greek capital letter theta,U+0398 ISOgrk3 -->
			{ "\u0399", "&Iota;" }, // greek capital letter iota, U+0399 -->
			{ "\u039A", "&Kappa;" }, // greek capital letter kappa, U+039A -->
			{ "\u039B", "&Lambda;" }, // greek capital letter lambda,U+039B ISOgrk3 -->
			{ "\u039C", "&Mu;" }, // greek capital letter mu, U+039C -->
			{ "\u039D", "&Nu;" }, // greek capital letter nu, U+039D -->
			{ "\u039E", "&Xi;" }, // greek capital letter xi, U+039E ISOgrk3 -->
			{ "\u039F", "&Omicron;" }, // greek capital letter omicron, U+039F -->
			{ "\u03A0", "&Pi;" }, // greek capital letter pi, U+03A0 ISOgrk3 -->
			{ "\u03A1", "&Rho;" }, // greek capital letter rho, U+03A1 -->
			// <!-- there is no Sigmaf, and no U+03A2 character either -->
			{ "\u03A3", "&Sigma;" }, // greek capital letter sigma,U+03A3 ISOgrk3 -->
			{ "\u03A4", "&Tau;" }, // greek capital letter tau, U+03A4 -->
			{ "\u03A5", "&Upsilon;" }, // greek capital letter upsilon,U+03A5 ISOgrk3 -->
			{ "\u03A6", "&Phi;" }, // greek capital letter phi,U+03A6 ISOgrk3 -->
			{ "\u03A7", "&Chi;" }, // greek capital letter chi, U+03A7 -->
			{ "\u03A8", "&Psi;" }, // greek capital letter psi,U+03A8 ISOgrk3 -->
			{ "\u03A9", "&Omega;" }, // greek capital letter omega,U+03A9 ISOgrk3 -->
			{ "\u03B1", "&alpha;" }, // greek small letter alpha,U+03B1 ISOgrk3 -->
			{ "\u03B2", "&beta;" }, // greek small letter beta, U+03B2 ISOgrk3 -->
			{ "\u03B3", "&gamma;" }, // greek small letter gamma,U+03B3 ISOgrk3 -->
			{ "\u03B4", "&delta;" }, // greek small letter delta,U+03B4 ISOgrk3 -->
			{ "\u03B5", "&epsilon;" }, // greek small letter epsilon,U+03B5 ISOgrk3 -->
			{ "\u03B6", "&zeta;" }, // greek small letter zeta, U+03B6 ISOgrk3 -->
			{ "\u03B7", "&eta;" }, // greek small letter eta, U+03B7 ISOgrk3 -->
			{ "\u03B8", "&theta;" }, // greek small letter theta,U+03B8 ISOgrk3 -->
			{ "\u03B9", "&iota;" }, // greek small letter iota, U+03B9 ISOgrk3 -->
			{ "\u03BA", "&kappa;" }, // greek small letter kappa,U+03BA ISOgrk3 -->
			{ "\u03BB", "&lambda;" }, // greek small letter lambda,U+03BB ISOgrk3 -->
			{ "\u03BC", "&mu;" }, // greek small letter mu, U+03BC ISOgrk3 -->
			{ "\u03BD", "&nu;" }, // greek small letter nu, U+03BD ISOgrk3 -->
			{ "\u03BE", "&xi;" }, // greek small letter xi, U+03BE ISOgrk3 -->
			{ "\u03BF", "&omicron;" }, // greek small letter omicron, U+03BF NEW -->
			{ "\u03C0", "&pi;" }, // greek small letter pi, U+03C0 ISOgrk3 -->
			{ "\u03C1", "&rho;" }, // greek small letter rho, U+03C1 ISOgrk3 -->
			{ "\u03C2", "&sigmaf;" }, // greek small letter final sigma,U+03C2 ISOgrk3 -->
			{ "\u03C3", "&sigma;" }, // greek small letter sigma,U+03C3 ISOgrk3 -->
			{ "\u03C4", "&tau;" }, // greek small letter tau, U+03C4 ISOgrk3 -->
			{ "\u03C5", "&upsilon;" }, // greek small letter upsilon,U+03C5 ISOgrk3 -->
			{ "\u03C6", "&phi;" }, // greek small letter phi, U+03C6 ISOgrk3 -->
			{ "\u03C7", "&chi;" }, // greek small letter chi, U+03C7 ISOgrk3 -->
			{ "\u03C8", "&psi;" }, // greek small letter psi, U+03C8 ISOgrk3 -->
			{ "\u03C9", "&omega;" }, // greek small letter omega,U+03C9 ISOgrk3 -->
			{ "\u03D1", "&thetasym;" }, // greek small letter theta symbol,U+03D1 NEW -->
			{ "\u03D2", "&upsih;" }, // greek upsilon with hook symbol,U+03D2 NEW -->
			{ "\u03D6", "&piv;" }, // greek pi symbol, U+03D6 ISOgrk3 -->
			// <!-- General Punctuation -->
			{ "\u2022", "&bull;" }, // bullet = black small circle,U+2022 ISOpub -->
			// <!-- bullet is NOT the same as bullet operator, U+2219 -->
			{ "\u2026", "&hellip;" }, // horizontal ellipsis = three dot leader,U+2026 ISOpub -->
			{ "\u2032", "&prime;" }, // prime = minutes = feet, U+2032 ISOtech -->
			{ "\u2033", "&Prime;" }, // double prime = seconds = inches,U+2033 ISOtech -->
			{ "\u203E", "&oline;" }, // overline = spacing overscore,U+203E NEW -->
			{ "\u2044", "&frasl;" }, // fraction slash, U+2044 NEW -->
			// <!-- Letterlike Symbols -->
			{ "\u2118", "&weierp;" }, // script capital P = power set= Weierstrass p, U+2118 ISOamso -->
			{ "\u2111", "&image;" }, // blackletter capital I = imaginary part,U+2111 ISOamso -->
			{ "\u211C", "&real;" }, // blackletter capital R = real part symbol,U+211C ISOamso -->
			{ "\u2122", "&trade;" }, // trade mark sign, U+2122 ISOnum -->
			{ "\u2135", "&alefsym;" }, // alef symbol = first transfinite cardinal,U+2135 NEW -->
			// <!-- alef symbol is NOT the same as hebrew letter alef,U+05D0 although the
			// same glyph could be used to depict both characters -->
			// <!-- Arrows -->
			{ "\u2190", "&larr;" }, // leftwards arrow, U+2190 ISOnum -->
			{ "\u2191", "&uarr;" }, // upwards arrow, U+2191 ISOnum-->
			{ "\u2192", "&rarr;" }, // rightwards arrow, U+2192 ISOnum -->
			{ "\u2193", "&darr;" }, // downwards arrow, U+2193 ISOnum -->
			{ "\u2194", "&harr;" }, // left right arrow, U+2194 ISOamsa -->
			{ "\u21B5", "&crarr;" }, // downwards arrow with corner leftwards= carriage return, U+21B5 NEW -->
			{ "\u21D0", "&lArr;" }, // leftwards double arrow, U+21D0 ISOtech -->
			// <!-- ISO 10646 does not say that lArr is the same as the 'is implied by'
			// arrow but also does not have any other character for that function.
			// So ? lArr canbe used for 'is implied by' as ISOtech suggests -->
			{ "\u21D1", "&uArr;" }, // upwards double arrow, U+21D1 ISOamsa -->
			{ "\u21D2", "&rArr;" }, // rightwards double arrow,U+21D2 ISOtech -->
			// <!-- ISO 10646 does not say this is the 'implies' character but does not
			// have another character with this function so ?rArr can be used for
			// 'implies' as ISOtech suggests -->
			{ "\u21D3", "&dArr;" }, // downwards double arrow, U+21D3 ISOamsa -->
			{ "\u21D4", "&hArr;" }, // left right double arrow,U+21D4 ISOamsa -->
			// <!-- Mathematical Operators -->
			{ "\u2200", "&forall;" }, // for all, U+2200 ISOtech -->
			{ "\u2202", "&part;" }, // partial differential, U+2202 ISOtech -->
			{ "\u2203", "&exist;" }, // there exists, U+2203 ISOtech -->
			{ "\u2205", "&empty;" }, // empty set = null set = diameter,U+2205 ISOamso -->
			{ "\u2207", "&nabla;" }, // nabla = backward difference,U+2207 ISOtech -->
			{ "\u2208", "&isin;" }, // element of, U+2208 ISOtech -->
			{ "\u2209", "&notin;" }, // not an element of, U+2209 ISOtech -->
			{ "\u220B", "&ni;" }, // contains as member, U+220B ISOtech -->
			// <!-- should there be a more memorable name than 'ni'? -->
			{ "\u220F", "&prod;" }, // n-ary product = product sign,U+220F ISOamsb -->
			// <!-- prod is NOT the same character as U+03A0 'greek capital letter pi'
			// though the same glyph might be used for both -->
			{ "\u2211", "&sum;" }, // n-ary summation, U+2211 ISOamsb -->
			// <!-- sum is NOT the same character as U+03A3 'greek capital letter sigma'
			// though the same glyph might be used for both -->
			{ "\u2212", "&minus;" }, // minus sign, U+2212 ISOtech -->
			{ "\u2217", "&lowast;" }, // asterisk operator, U+2217 ISOtech -->
			{ "\u221A", "&radic;" }, // square root = radical sign,U+221A ISOtech -->
			{ "\u221D", "&prop;" }, // proportional to, U+221D ISOtech -->
			{ "\u221E", "&infin;" }, // infinity, U+221E ISOtech -->
			{ "\u2220", "&ang;" }, // angle, U+2220 ISOamso -->
			{ "\u2227", "&and;" }, // logical and = wedge, U+2227 ISOtech -->
			{ "\u2228", "&or;" }, // logical or = vee, U+2228 ISOtech -->
			{ "\u2229", "&cap;" }, // intersection = cap, U+2229 ISOtech -->
			{ "\u222A", "&cup;" }, // union = cup, U+222A ISOtech -->
			{ "\u222B", "&int;" }, // integral, U+222B ISOtech -->
			{ "\u2234", "&there4;" }, // therefore, U+2234 ISOtech -->
			{ "\u223C", "&sim;" }, // tilde operator = varies with = similar to,U+223C ISOtech -->
			// <!-- tilde operator is NOT the same character as the tilde, U+007E,although
			// the same glyph might be used to represent both -->
			{ "\u2245", "&cong;" }, // approximately equal to, U+2245 ISOtech -->
			{ "\u2248", "&asymp;" }, // almost equal to = asymptotic to,U+2248 ISOamsr -->
			{ "\u2260", "&ne;" }, // not equal to, U+2260 ISOtech -->
			{ "\u2261", "&equiv;" }, // identical to, U+2261 ISOtech -->
			{ "\u2264", "&le;" }, // less-than or equal to, U+2264 ISOtech -->
			{ "\u2265", "&ge;" }, // greater-than or equal to,U+2265 ISOtech -->
			{ "\u2282", "&sub;" }, // subset of, U+2282 ISOtech -->
			{ "\u2283", "&sup;" }, // superset of, U+2283 ISOtech -->
			// <!-- note that nsup, 'not a superset of, U+2283' is not covered by the
			// Symbol font encoding and is not included. Should it be, for symmetry?
			// It is in ISOamsn --> <!ENTITY nsub", "8836"},
			// not a subset of, U+2284 ISOamsn -->
			{ "\u2286", "&sube;" }, // subset of or equal to, U+2286 ISOtech -->
			{ "\u2287", "&supe;" }, // superset of or equal to,U+2287 ISOtech -->
			{ "\u2295", "&oplus;" }, // circled plus = direct sum,U+2295 ISOamsb -->
			{ "\u2297", "&otimes;" }, // circled times = vector product,U+2297 ISOamsb -->
			{ "\u22A5", "&perp;" }, // up tack = orthogonal to = perpendicular,U+22A5 ISOtech -->
			{ "\u22C5", "&sdot;" }, // dot operator, U+22C5 ISOamsb -->
			// <!-- dot operator is NOT the same character as U+00B7 middle dot -->
			// <!-- Miscellaneous Technical -->
			{ "\u2308", "&lceil;" }, // left ceiling = apl upstile,U+2308 ISOamsc -->
			{ "\u2309", "&rceil;" }, // right ceiling, U+2309 ISOamsc -->
			{ "\u230A", "&lfloor;" }, // left floor = apl downstile,U+230A ISOamsc -->
			{ "\u230B", "&rfloor;" }, // right floor, U+230B ISOamsc -->
			{ "\u2329", "&lang;" }, // left-pointing angle bracket = bra,U+2329 ISOtech -->
			// <!-- lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation
			// mark' -->
			{ "\u232A", "&rang;" }, // right-pointing angle bracket = ket,U+232A ISOtech -->
			// <!-- rang is NOT the same character as U+003E 'greater than' or U+203A
			// 'single right-pointing angle quotation mark' -->
			// <!-- Geometric Shapes -->
			{ "\u25CA", "&loz;" }, // lozenge, U+25CA ISOpub -->
			// <!-- Miscellaneous Symbols -->
			{ "\u2660", "&spades;" }, // black spade suit, U+2660 ISOpub -->
			// <!-- black here seems to mean filled as opposed to hollow -->
			{ "\u2663", "&clubs;" }, // black club suit = shamrock,U+2663 ISOpub -->
			{ "\u2665", "&hearts;" }, // black heart suit = valentine,U+2665 ISOpub -->
			{ "\u2666", "&diams;" }, // black diamond suit, U+2666 ISOpub -->

			// <!-- Latin Extended-A -->
			{ "\u0152", "&OElig;" }, // -- latin capital ligature OE,U+0152 ISOlat2 -->
			{ "\u0153", "&oelig;" }, // -- latin small ligature oe, U+0153 ISOlat2 -->
			// <!-- ligature is a misnomer, this is a separate character in some languages -->
			{ "\u0160", "&Scaron;" }, // -- latin capital letter S with caron,U+0160 ISOlat2 -->
			{ "\u0161", "&scaron;" }, // -- latin small letter s with caron,U+0161 ISOlat2 -->
			{ "\u0178", "&Yuml;" }, // -- latin capital letter Y with diaeresis,U+0178 ISOlat2 -->
			// <!-- Spacing Modifier Letters -->
			{ "\u02C6", "&circ;" }, // -- modifier letter circumflex accent,U+02C6 ISOpub -->
			{ "\u02DC", "&tilde;" }, // small tilde, U+02DC ISOdia -->
			// <!-- General Punctuation -->
			{ "\u2002", "&ensp;" }, // en space, U+2002 ISOpub -->
			{ "\u2003", "&emsp;" }, // em space, U+2003 ISOpub -->
			{ "\u2009", "&thinsp;" }, // thin space, U+2009 ISOpub -->
			{ "\u200C", "&zwnj;" }, // zero width non-joiner,U+200C NEW RFC 2070 -->
			{ "\u200D", "&zwj;" }, // zero width joiner, U+200D NEW RFC 2070 -->
			{ "\u200E", "&lrm;" }, // left-to-right mark, U+200E NEW RFC 2070 -->
			{ "\u200F", "&rlm;" }, // right-to-left mark, U+200F NEW RFC 2070 -->
			{ "\u2013", "&ndash;" }, // en dash, U+2013 ISOpub -->
			{ "\u2014", "&mdash;" }, // em dash, U+2014 ISOpub -->
			{ "\u2018", "&lsquo;" }, // left single quotation mark,U+2018 ISOnum -->
			{ "\u2019", "&rsquo;" }, // right single quotation mark,U+2019 ISOnum -->
			{ "\u201A", "&sbquo;" }, // single low-9 quotation mark, U+201A NEW -->
			{ "\u201C", "&ldquo;" }, // left double quotation mark,U+201C ISOnum -->
			{ "\u201D", "&rdquo;" }, // right double quotation mark,U+201D ISOnum -->
			{ "\u201E", "&bdquo;" }, // double low-9 quotation mark, U+201E NEW -->
			{ "\u2020", "&dagger;" }, // dagger, U+2020 ISOpub -->
			{ "\u2021", "&Dagger;" }, // double dagger, U+2021 ISOpub -->
			{ "\u2030", "&permil;" }, // per mille sign, U+2030 ISOtech -->
			{ "\u2039", "&lsaquo;" }, // single left-pointing angle quotation mark,U+2039 ISO proposed -->
			// <!-- lsaquo is proposed but not yet ISO standardized -->
			{ "\u203A", "&rsaquo;" }, // single right-pointing angle quotation mark,U+203A ISO proposed -->
			// <!-- rsaquo is proposed but not yet ISO standardized -->
			{ "\u20AC", "&euro;" }, // -- euro sign, U+20AC NEW -->
	};

	public Html4Escape() {
		addChain(new LookupReplacer(BASIC_ESCAPE));
		addChain(new LookupReplacer(ISO8859_1_ESCAPE));
		addChain(new LookupReplacer(HTML40_EXTENDED_ESCAPE));
	}
}
