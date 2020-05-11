package com.android.example.core.utils

import java.io.StringWriter
import java.util.HashMap

object StringEscapeUtils {

    private val escapeChars = HashMap<String, Char>()

    init {

        // Special characters for HTML
        escapeChars["&amp;"] = '\u0026'
        escapeChars["&lt;"] = '\u003C'
        escapeChars["&gt;"] = '\u003E'
        escapeChars["&quot;"] = '\u0022'

        escapeChars["&OElig;"] = '\u0152'
        escapeChars["&oelig;"] = '\u0153'
        escapeChars["&Scaron;"] = '\u0160'
        escapeChars["&scaron;"] = '\u0161'
        escapeChars["&Yuml;"] = '\u0178'
        escapeChars["&circ;"] = '\u02C6'
        escapeChars["&tilde;"] = '\u02DC'
        escapeChars["&ensp;"] = '\u2002'
        escapeChars["&emsp;"] = '\u2003'
        escapeChars["&thinsp;"] = '\u2009'
        escapeChars["&zwnj;"] = '\u200C'
        escapeChars["&zwj;"] = '\u200D'
        escapeChars["&lrm;"] = '\u200E'
        escapeChars["&rlm;"] = '\u200F'
        escapeChars["&ndash;"] = '\u2013'
        escapeChars["&mdash;"] = '\u2014'
        escapeChars["&lsquo;"] = '\u2018'
        escapeChars["&rsquo;"] = '\u2019'
        escapeChars["&sbquo;"] = '\u201A'
        escapeChars["&ldquo;"] = '\u201C'
        escapeChars["&rdquo;"] = '\u201D'
        escapeChars["&bdquo;"] = '\u201E'
        escapeChars["&dagger;"] = '\u2020'
        escapeChars["&Dagger;"] = '\u2021'
        escapeChars["&permil;"] = '\u2030'
        escapeChars["&lsaquo;"] = '\u2039'
        escapeChars["&rsaquo;"] = '\u203A'
        escapeChars["&euro;"] = '\u20AC'

        // Character entity references for ISO 8859-1 characters
        escapeChars["&nbsp;"] = '\u00A0'
        escapeChars["&iexcl;"] = '\u00A1'
        escapeChars["&cent;"] = '\u00A2'
        escapeChars["&pound;"] = '\u00A3'
        escapeChars["&curren;"] = '\u00A4'
        escapeChars["&yen;"] = '\u00A5'
        escapeChars["&brvbar;"] = '\u00A6'
        escapeChars["&sect;"] = '\u00A7'
        escapeChars["&uml;"] = '\u00A8'
        escapeChars["&copy;"] = '\u00A9'
        escapeChars["&ordf;"] = '\u00AA'
        escapeChars["&laquo;"] = '\u00AB'
        escapeChars["&not;"] = '\u00AC'
        escapeChars["&shy;"] = '\u00AD'
        escapeChars["&reg;"] = '\u00AE'
        escapeChars["&macr;"] = '\u00AF'
        escapeChars["&deg;"] = '\u00B0'
        escapeChars["&plusmn;"] = '\u00B1'
        escapeChars["&sup2;"] = '\u00B2'
        escapeChars["&sup3;"] = '\u00B3'
        escapeChars["&acute;"] = '\u00B4'
        escapeChars["&micro;"] = '\u00B5'
        escapeChars["&para;"] = '\u00B6'
        escapeChars["&middot;"] = '\u00B7'
        escapeChars["&cedil;"] = '\u00B8'
        escapeChars["&sup1;"] = '\u00B9'
        escapeChars["&ordm;"] = '\u00BA'
        escapeChars["&raquo;"] = '\u00BB'
        escapeChars["&frac14;"] = '\u00BC'
        escapeChars["&frac12;"] = '\u00BD'
        escapeChars["&frac34;"] = '\u00BE'
        escapeChars["&iquest;"] = '\u00BF'
        escapeChars["&Agrave;"] = '\u00C0'
        escapeChars["&Aacute;"] = '\u00C1'
        escapeChars["&Acirc;"] = '\u00C2'
        escapeChars["&Atilde;"] = '\u00C3'
        escapeChars["&Auml;"] = '\u00C4'
        escapeChars["&Aring;"] = '\u00C5'
        escapeChars["&AElig;"] = '\u00C6'
        escapeChars["&Ccedil;"] = '\u00C7'
        escapeChars["&Egrave;"] = '\u00C8'
        escapeChars["&Eacute;"] = '\u00C9'
        escapeChars["&Ecirc;"] = '\u00CA'
        escapeChars["&Euml;"] = '\u00CB'
        escapeChars["&Igrave;"] = '\u00CC'
        escapeChars["&Iacute;"] = '\u00CD'
        escapeChars["&Icirc;"] = '\u00CE'
        escapeChars["&Iuml;"] = '\u00CF'
        escapeChars["&ETH;"] = '\u00D0'
        escapeChars["&Ntilde;"] = '\u00D1'
        escapeChars["&Ograve;"] = '\u00D2'
        escapeChars["&Oacute;"] = '\u00D3'
        escapeChars["&Ocirc;"] = '\u00D4'
        escapeChars["&Otilde;"] = '\u00D5'
        escapeChars["&Ouml;"] = '\u00D6'
        escapeChars["&times;"] = '\u00D7'
        escapeChars["&Oslash;"] = '\u00D8'
        escapeChars["&Ugrave;"] = '\u00D9'
        escapeChars["&Uacute;"] = '\u00DA'
        escapeChars["&Ucirc;"] = '\u00DB'
        escapeChars["&Uuml;"] = '\u00DC'
        escapeChars["&Yacute;"] = '\u00DD'
        escapeChars["&THORN;"] = '\u00DE'
        escapeChars["&szlig;"] = '\u00DF'
        escapeChars["&agrave;"] = '\u00E0'
        escapeChars["&aacute;"] = '\u00E1'
        escapeChars["&acirc;"] = '\u00E2'
        escapeChars["&atilde;"] = '\u00E3'
        escapeChars["&auml;"] = '\u00E4'
        escapeChars["&aring;"] = '\u00E5'
        escapeChars["&aelig;"] = '\u00E6'
        escapeChars["&ccedil;"] = '\u00E7'
        escapeChars["&egrave;"] = '\u00E8'
        escapeChars["&eacute;"] = '\u00E9'
        escapeChars["&ecirc;"] = '\u00EA'
        escapeChars["&euml;"] = '\u00EB'
        escapeChars["&igrave;"] = '\u00EC'
        escapeChars["&iacute;"] = '\u00ED'
        escapeChars["&icirc;"] = '\u00EE'
        escapeChars["&iuml;"] = '\u00EF'
        escapeChars["&eth;"] = '\u00F0'
        escapeChars["&ntilde;"] = '\u00F1'
        escapeChars["&ograve;"] = '\u00F2'
        escapeChars["&oacute;"] = '\u00F3'
        escapeChars["&ocirc;"] = '\u00F4'
        escapeChars["&otilde;"] = '\u00F5'
        escapeChars["&ouml;"] = '\u00F6'
        escapeChars["&divide;"] = '\u00F7'
        escapeChars["&oslash;"] = '\u00F8'
        escapeChars["&ugrave;"] = '\u00F9'
        escapeChars["&uacute;"] = '\u00FA'
        escapeChars["&ucirc;"] = '\u00FB'
        escapeChars["&uuml;"] = '\u00FC'
        escapeChars["&yacute;"] = '\u00FD'
        escapeChars["&thorn;"] = '\u00FE'
        escapeChars["&yuml;"] = '\u00FF'

        // Mathematical, Greek and Symbolic characters for HTML
        escapeChars["&fnof;"] = '\u0192'
        escapeChars["&Alpha;"] = '\u0391'
        escapeChars["&Beta;"] = '\u0392'
        escapeChars["&Gamma;"] = '\u0393'
        escapeChars["&Delta;"] = '\u0394'
        escapeChars["&Epsilon;"] = '\u0395'
        escapeChars["&Zeta;"] = '\u0396'
        escapeChars["&Eta;"] = '\u0397'
        escapeChars["&Theta;"] = '\u0398'
        escapeChars["&Iota;"] = '\u0399'
        escapeChars["&Kappa;"] = '\u039A'
        escapeChars["&Lambda;"] = '\u039B'
        escapeChars["&Mu;"] = '\u039C'
        escapeChars["&Nu;"] = '\u039D'
        escapeChars["&Xi;"] = '\u039E'
        escapeChars["&Omicron;"] = '\u039F'
        escapeChars["&Pi;"] = '\u03A0'
        escapeChars["&Rho;"] = '\u03A1'
        escapeChars["&Sigma;"] = '\u03A3'
        escapeChars["&Tau;"] = '\u03A4'
        escapeChars["&Upsilon;"] = '\u03A5'
        escapeChars["&Phi;"] = '\u03A6'
        escapeChars["&Chi;"] = '\u03A7'
        escapeChars["&Psi;"] = '\u03A8'
        escapeChars["&Omega;"] = '\u03A9'
        escapeChars["&alpha;"] = '\u03B1'
        escapeChars["&beta;"] = '\u03B2'
        escapeChars["&gamma;"] = '\u03B3'
        escapeChars["&delta;"] = '\u03B4'
        escapeChars["&epsilon;"] = '\u03B5'
        escapeChars["&zeta;"] = '\u03B6'
        escapeChars["&eta;"] = '\u03B7'
        escapeChars["&theta;"] = '\u03B8'
        escapeChars["&iota;"] = '\u03B9'
        escapeChars["&kappa;"] = '\u03BA'
        escapeChars["&lambda;"] = '\u03BB'
        escapeChars["&mu;"] = '\u03BC'
        escapeChars["&nu;"] = '\u03BD'
        escapeChars["&xi;"] = '\u03BE'
        escapeChars["&omicron;"] = '\u03BF'
        escapeChars["&pi;"] = '\u03C0'
        escapeChars["&rho;"] = '\u03C1'
        escapeChars["&sigmaf;"] = '\u03C2'
        escapeChars["&sigma;"] = '\u03C3'
        escapeChars["&tau;"] = '\u03C4'
        escapeChars["&upsilon;"] = '\u03C5'
        escapeChars["&phi;"] = '\u03C6'
        escapeChars["&chi;"] = '\u03C7'
        escapeChars["&psi;"] = '\u03C8'
        escapeChars["&omega;"] = '\u03C9'
        escapeChars["&thetasym;"] = '\u03D1'
        escapeChars["&upsih;"] = '\u03D2'
        escapeChars["&piv;"] = '\u03D6'
        escapeChars["&bull;"] = '\u2022'
        escapeChars["&hellip;"] = '\u2026'
        escapeChars["&prime;"] = '\u2032'
        escapeChars["&Prime;"] = '\u2033'
        escapeChars["&oline;"] = '\u203E'
        escapeChars["&frasl;"] = '\u2044'
        escapeChars["&weierp;"] = '\u2118'
        escapeChars["&image;"] = '\u2111'
        escapeChars["&real;"] = '\u211C'
        escapeChars["&trade;"] = '\u2122'
        escapeChars["&alefsym;"] = '\u2135'
        escapeChars["&larr;"] = '\u2190'
        escapeChars["&uarr;"] = '\u2191'
        escapeChars["&rarr;"] = '\u2192'
        escapeChars["&darr;"] = '\u2193'
        escapeChars["&harr;"] = '\u2194'
        escapeChars["&crarr;"] = '\u21B5'
        escapeChars["&lArr;"] = '\u21D0'
        escapeChars["&uArr;"] = '\u21D1'
        escapeChars["&rArr;"] = '\u21D2'
        escapeChars["&dArr;"] = '\u21D3'
        escapeChars["&hArr;"] = '\u21D4'
        escapeChars["&forall;"] = '\u2200'
        escapeChars["&part;"] = '\u2202'
        escapeChars["&exist;"] = '\u2203'
        escapeChars["&empty;"] = '\u2205'
        escapeChars["&nabla;"] = '\u2207'
        escapeChars["&isin;"] = '\u2208'
        escapeChars["&notin;"] = '\u2209'
        escapeChars["&ni;"] = '\u220B'
        escapeChars["&prod;"] = '\u220F'
        escapeChars["&sum;"] = '\u2211'
        escapeChars["&minus;"] = '\u2212'
        escapeChars["&lowast;"] = '\u2217'
        escapeChars["&radic;"] = '\u221A'
        escapeChars["&prop;"] = '\u221D'
        escapeChars["&infin;"] = '\u221E'
        escapeChars["&ang;"] = '\u2220'
        escapeChars["&and;"] = '\u2227'
        escapeChars["&or;"] = '\u2228'
        escapeChars["&cap;"] = '\u2229'
        escapeChars["&cup;"] = '\u222A'
        escapeChars["&int;"] = '\u222B'
        escapeChars["&there4;"] = '\u2234'
        escapeChars["&sim;"] = '\u223C'
        escapeChars["&cong;"] = '\u2245'
        escapeChars["&asymp;"] = '\u2248'
        escapeChars["&ne;"] = '\u2260'
        escapeChars["&equiv;"] = '\u2261'
        escapeChars["&le;"] = '\u2264'
        escapeChars["&ge;"] = '\u2265'
        escapeChars["&sub;"] = '\u2282'
        escapeChars["&sup;"] = '\u2283'
        escapeChars["&nsub;"] = '\u2284'
        escapeChars["&sube;"] = '\u2286'
        escapeChars["&supe;"] = '\u2287'
        escapeChars["&oplus;"] = '\u2295'
        escapeChars["&otimes;"] = '\u2297'
        escapeChars["&perp;"] = '\u22A5'
        escapeChars["&sdot;"] = '\u22C5'
        escapeChars["&lceil;"] = '\u2308'
        escapeChars["&rceil;"] = '\u2309'
        escapeChars["&lfloor;"] = '\u230A'
        escapeChars["&rfloor;"] = '\u230B'
        escapeChars["&lang;"] = '\u2329'
        escapeChars["&rang;"] = '\u232A'
        escapeChars["&loz;"] = '\u25CA'
        escapeChars["&spades;"] = '\u2660'
        escapeChars["&clubs;"] = '\u2663'
        escapeChars["&hearts;"] = '\u2665'
        escapeChars["&diams;"] = '\u2666'
    }

    private const val MIN_ESCAPE = 2
    private const val MAX_ESCAPE = 6


    fun escapeHtml(source: String?): String {
        if (null == source) {
            return ""
        }

        var encodedString: StringBuffer? = null
        val stringToEncodeArray = source.toCharArray()
        var lastMatch = -1
        var difference: Int

        for (i in stringToEncodeArray.indices) {
            val charToEncode = stringToEncodeArray[i]

            if (escapeChars.containsValue(charToEncode)) {
                if (null == encodedString) {
                    encodedString = StringBuffer(source.length)
                }
                difference = i - (lastMatch + 1)
                if (difference > 0) {
                    encodedString.append(stringToEncodeArray, lastMatch + 1, difference)
                }
                encodedString.append(escapeChars.entries.first { it.value == charToEncode }.key)
                lastMatch = i
            }
        }

        return if (null == encodedString) {
            source
        } else {
            difference = stringToEncodeArray.size - (lastMatch + 1)
            if (difference > 0) {
                encodedString.append(stringToEncodeArray, lastMatch + 1, difference)
            }
            encodedString.toString()
        }
    }


    fun unescapeHtml(input: String): String {
        var writer: StringWriter? = null
        val len = input.length
        var i = 1
        var st = 0
        while (true) {
            // look for '&'
            while (i < len && input[i - 1] != '&')
                i++
            if (i >= len)
                break

            // found '&', look for ';'
            var j = i
            while (j < len && j < i + MAX_ESCAPE + 1 && input[j] != ';')
                j++
            if (j == len || j < i + MIN_ESCAPE || j == i + MAX_ESCAPE + 1) {
                i++
                continue
            }

            // found escape
            if (input[i] == '#') {
                // numeric escape
                var k = i + 1
                var radix = 10

                val firstChar = input[k]
                if (firstChar == 'x' || firstChar == 'X') {
                    k++
                    radix = 16
                }

                try {
                    val entityValue = Integer.parseInt(input.substring(k, j), radix)

                    if (writer == null)
                        writer = StringWriter(input.length)
                    writer.append(input.substring(st, i - 1))

                    if (entityValue > 0xFFFF) {
                        val chrs = Character.toChars(entityValue)
                        writer.write(chrs[0].toInt())
                        writer.write(chrs[1].toInt())
                    } else {
                        writer.write(entityValue)
                    }

                } catch (ex: NumberFormatException) {
                    i++
                    continue
                }

            } else {
                // named escape
                val value = escapeChars["&${input.substring(i, j)};"]
                if (value == null) {
                    i++
                    continue
                }

                if (writer == null)
                    writer = StringWriter(input.length)
                writer.append(input.substring(st, i - 1))

                writer.append(value)
            }

            // skip escape
            st = j + 1
            i = st
        }

        if (writer != null) {
            writer.append(input.substring(st, len))
            return writer.toString()
        }
        return input
    }
}