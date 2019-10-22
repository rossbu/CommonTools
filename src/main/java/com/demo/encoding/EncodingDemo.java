package com.demo.encoding;

import com.google.common.net.UrlEscapers;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


/**
 * 1. StringEscapeUtils  from apache common langs have broken the backward compatible. need to use the new one
 * <p>
 * &#nnnn;
 * &#xhhhh;
 * nnnn is the code point in decimal form,
 * hhhh is the code point in hexadecimal form.
 * The x must be lowercase in XML documents. The nnnn or hhhh may be any number of digits and may include leading zeros.
 * Predefined entities in XML, . The XML specification defines five "predefined entities" representing special characters, and requires that all XML processors honor them
 * <p>
 * Name	Character	Unicode code point (decimal)	Standard	Name
 * quot	"	U+0022 (34)	XML 1.0	quotation mark
 * amp	&	U+0026 (38)	XML 1.0	ampersand
 * apos	'	U+0027 (39)	XML 1.0	apostrophe (1.0: apostrophe-quote)
 * lt	    <	U+003C (60)	XML 1.0	less-than sign
 * gt	    >	U+003E (62)	XML 1.0	greater-than sign
 * <p>
 * The HTML 5 specification additionally provides mappings from the names to Unicode character sequences using JSON.
 * &copy;   is equal to &#169;    which represents copy sign.
 */


public class EncodingDemo {
    private static final String URL_TO_ESCAPE = "http://www.google.com?somevar=abc123&someothervar";

    public static void main(String... args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        System.out.println("System default charset is : " + Charset.defaultCharset());
        base64Test();
        unicode2Value();
        forceCp1252print();
        toUniCodeTest();
        urlEncoderTest();
        guavaEscapes();
        jerseyEncoder();
        apacheHttpClientUrlBuilder();
        apacheCommonText();
        jira_LANG480();
        testescape();
        testPureAscii();
        testAsciiEncoding();
        try {
            FileUtils.writeByteArrayToFile(new File("c:\\temp\\test-utf-8.txt"),"NÜRNBERG STRAUßFURTER".getBytes(StandardCharsets.UTF_8));
            FileUtils.writeByteArrayToFile(new File("c:\\temp\\test-utf-ISO-8859.txt"),"NÜRNBERG STRAUßFURTER".getBytes(StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(0x007f);
        System.out.println(0x00ff);
        System.out.println(0xffff);
        int alpha = 0x16777215;
        int beta = 376926741;
        System.out.println(alpha + " = " + beta);

    }

    private static void base64Test() throws UnsupportedEncodingException {
        // Encode
        String asB64 = Base64.getEncoder().encodeToString("some string".getBytes("utf-8"));
        System.out.println(asB64); // Output will be: c29tZSBzdHJpbmc=

        // Decode
        byte[] asBytes = Base64.getDecoder().decode("c29tZSBzdHJpbmc=");
        System.out.println(new String(asBytes, "utf-8")); // And the output is: some string

    }

    public static void testPureAscii (){
        String test = "Réal";
        System.out.println(test + " isPureAscii() : " + isPureAscii(test));
        test = "Real";
        System.out.println(test + " isPureAscii() : " + isPureAscii(test));
        String input = "eéaà";
        String output = input.replaceAll("[^\\p{ASCII}]", "");
        System.out.println("eéaà after replacement is: " + output);

        /*
         * output :
         *   Réal isPureAscii() : false
         *   Real isPureAscii() : true
         */
    }
    public static boolean isPureAscii(String v) {
        byte bytearray []  = v.getBytes();
        CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
        try {
            CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
            r.toString();
        }
        catch(CharacterCodingException e) {
            return false;
        }
        return true;
    }
    public static boolean isPureAsciiByEncoder(String v) {
        return Charset.forName("US-ASCII").newEncoder().canEncode(v);
        // or "ISO-8859-1" for ISO Latin 1 or StandardCharsets.US_ASCII with JDK1.7+
    }
    /**
     * test if cp1252 can print char > 126 .  it's NOT.
     *
     * @throws UnsupportedEncodingException
     */
    private static void forceCp1252print() throws UnsupportedEncodingException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, "CP1252"), true);
        out.println("cp 1252 output > 126 chars , ( NOT ABLE TO DISPLAY ): " + "\u0064\u60F3\u8981\u6709\u5C0A\u4E25\u7684\u51FA\u5356\u81EA\u5DF1\u7684\u826F\u5FC3\u0089");
    }


    /**
     * https://issues.apache.org/jira/browse/LANG-480
     *
     * @throws UnsupportedEncodingException
     */
    private static void jira_LANG480() throws UnsupportedEncodingException {
        //this is the utf8 representation of the character: COUNTING ROD UNIT DIGIT THREE in unicode codepoint: U+1D362
        byte[] data = new byte[]{(byte) 0xF0, (byte) 0x9D, (byte) 0x8D, (byte) 0xA2};
        //output is: &#55348;&#57186; should be: &#119650;
        System.out.println("jira issue reported to Apache lang team before 3.0 with escapate html4 '" + StringEscapeUtils.escapeHtml4(new String(data, "UTF8")) + "'");
    }

    private static void testescape() {
        System.out.println("escapeHTML 嗯 : " + escapeHTML("嗯“"));
        System.out.println("escapeHTML ’: " + escapeHTML("’"));
        System.out.println("escapeHTML € | 好 | ⌻ | ë | ~ | d | k |  \u0089 | 65 | 想要有尊严的出卖自己的良心  : " + escapeHTML("€ | 好 | ⌻ | ë | ~ | d | k |  \u0089 | 65 | 想要有尊严的出卖自己的良心 "));

    }

    private static void apacheCommonText() throws UnsupportedEncodingException {
        String unicode = "\u304B\u304C\u3068";
        String escaped = StringEscapeUtils.escapeHtml4(unicode);
        System.out.println("apache common escaped Text: " + escaped);
        System.out.println("apache common escaped html4:  " + StringEscapeUtils.unescapeHtml4("&#235; &nbsp; abc &#24515; d") );
    }

    /**
     * Unicode was originally designed as a fixed-width 16-bit character encoding.
     * The primitive data type char in the Java programming language was intended to take advantage of this design by providing a simple data type that could hold any character.
     * random access cannot be done efficiently with UTF-8. So all things weighed up, UCS-2 was seemingly the best choice at the time, particularly as the no supplementary characters had been allocated by that stage.
     * This then left UTF-16 as the easiest natural progression beyond that.
     * <p>
     * <p>
     * &#x60F3;&#x8981;&#x6709;&#x5C0A;&#x4E25;&#x7684;&#x51FA;&#x5356;&#x81EA;&#x5DF1;&#x7684;&#x826F;&#x5FC3;
     *        /*
     *              €  as below euro sign
     *
     *             UTF-8 Encoding:	    0xE2 0x82 0xAC
     *             UTF-16 Encoding:	0x20AC
     *             HTML Entity:
     *                 &#8364;
     *                 &#x20AC;
     *                 &euro;
     *
     *             心
     *
     *             UTF-8 Encoding:	    0xE5 0xBF 0x83
     *             UTF-16 Encoding:	0x5FC3
     *             HTML Entity:
     *                 &#24515;
     *                 &#x5FC3;
     * @throws UnsupportedEncodingException
     */
    private static void unicode2Value() throws UnsupportedEncodingException {
        String testStr = "Ü - Ö - 好 - ⌻ - ë - ~ - d - k - \\u0089 - 65 - 想要有尊严的出卖自己的良心";
        printBytes(testStr);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(testStr);
        System.out.println("\nAbove bytes are encoded by UTF-8 ,show text :  " + new String(byteBuffer.array(), "UTF-8"));
        String strA_ISO_8859_1_i = new String(testStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        System.out.println("Encoded by utf8 but Wrongly decoded by iso8859-1 : " + strA_ISO_8859_1_i);

    }

    /**
     * testStr should be seperated by |
     * @param testStr
     */
    public static void printBytes(String testStr) {
        System.out.println("------------ below prints bytes (only first 10)---------------");
        String format = "|%1$-10s|%2$-10s|%3$-10s|%4$-10s|%5$-10s|%6$-10s|%7$-10s|%8$-10s|%9$-10s|%10$-10s|%11$-20s\n";
        String[] splittedStr = testStr.split("-");
//        System.out.format(format,splittedStr);
        Arrays.stream(splittedStr).forEach(
                e -> {
                    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(e.trim());
                    byte[] byteArray = byteBuffer.array();
                    for (int k = 0; k < byteArray.length; k++) {
                        String hexedValue = UnicodeFormatter.byteToHex(byteArray[k]);
                        String printedString = "0x" + hexedValue;
                        System.out.format(printedString);
                    }
                    System.out.format("\n");
                }
        );
        System.out.println("------------ below prints bytes - end ---------------");
    }

    private static void toUniCodeTest() {
        System.out.println("------------ below are unicode of test text ---------------");
        System.out.println("Hello World : "+ toUnicode("Hello World"));
        System.out.println("😊 : "+ toUnicode("😊"));
        System.out.println("🥰 : "+toUnicode("🥰"));
        System.out.println("` : "+toUnicode("`"));
        System.out.println("' : "+toUnicode("'"));
        System.out.println("‘ : "+toUnicode("‘"));
        System.out.println("’ : "+toUnicode("’"));

        System.out.println("------------ below are unicode of test text , End ---------------");
    }

    /**
     * Java and C uses 0x or 0X to represent hexadecimal numbers. Characters ('A' to 'F') in a hexadecimal number can be written either in upper case or lower case.
     * <p>
     * 0xff and 255 are exactly the same thing  15 x 16 + 15 = 255
     *
     * @param text
     * @return
     */
    public static String toUnicode(String text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            int codePoint = text.codePointAt(i);
            // Skip over the second char in a surrogate pair
            if (codePoint > 0xffff) {  // 65535
                i++;
            }
            String hex = Integer.toHexString(codePoint);
            sb.append("\\u");
            for (int j = 0; j < 4 - hex.length(); j++) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static void testAsciiEncoding() throws MalformedURLException, URISyntaxException {
        String urlStr = "http://www.example.com/CEREC® Materials & Accessories/IPS Empress® CAD.pdf?somevar=abc123&someothervar";
        URL url = new URL(urlStr);
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        System.out.println("ascII encoding: " + uri.toASCIIString());

    }

    /**
     * as of 3.6, use commons-text StringEscapeUtils instead
     *
     * @throws URISyntaxException
     */
    private static void apacheHttpClientUrlBuilder() throws URISyntaxException {
        URIBuilder ub = new URIBuilder("http://www.google.com");
        ub.addParameter("q", "random word £500 bank \\$");
        String url = ub.toString();
        System.out.println("apache common uribuilder: " + url);
        String sJava = "\\u0048\\u0065\\u006C\\u006C\\u006F";
        System.out.println("apache common StringEscapeUtils.unescapeJava(sJava): " + StringEscapeUtils.unescapeJava(sJava));
    }

    private static void jerseyEncoder() throws InterruptedException {
        Thread.sleep(200);
//        URI url = UriBuilder.fromUri(UUID.randomUUID().toString()).build();
        System.out.println("jersey uribuilder: " + UriBuilder.fromPath("http://www.query.com/").queryParam("key", "{val}").build());
    }

    private static void guavaEscapes() {
        String urlEscaped = UrlEscapers.urlPathSegmentEscaper().escape(URL_TO_ESCAPE);
        System.out.println("guava: " + urlEscaped);
    }

    private static void urlEncoderTest() throws UnsupportedEncodingException {
        System.out.println("&#24515; URLDecoded with UTF-8: " + URLDecoder.decode("&#24515;", "UTF-8"));
        String urlEscaped = URLEncoder.encode(URL_TO_ESCAPE, "UTF-8")
//                .replaceAll("/+", "%20")
//                .replaceAll("%21", "!")
//                .replaceAll("%27", "'")
//                .replaceAll("%28", "(")
//                .replaceAll("%29", ")")
//                .replaceAll("%7E", "~")
                ;

        System.out.println("url is encoded by urlEncoder: " + urlEscaped);

        String test = "";
        try {
            test = URLEncoder.encode("^^&*#W%#tbu&amp;", "UTF-8");
            System.out.println("encoded : " + test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("decoded: " + URLDecoder.decode(test, "UTF-8"));

    }

    public static String escapeXML(String inputString) {
        return (inputString != null) ? StringEscapeUtils.escapeXml10(inputString) : "";
    }


    /**
     * this is to escape characters  quote,  ampersand, less than, greater than, apostophone. and all extended ASCII > 126 and all other non ascII characters.
     *
     * @param inputString
     * @return
     */
    public static String escapeHTML(String inputString) {

        if (inputString == null) {
            return "";
        }

        StringBuffer outputBuffer = new StringBuffer();
        for (int i = 0; i < inputString.length(); i++) {

            char ch = inputString.charAt(i);

            switch (ch) {

                case '"':
                    outputBuffer.append("&quot;");
                    break;

                case '&':
                    outputBuffer.append("&amp;");
                    break;

                case '<':
                    outputBuffer.append("&lt;");
                    break;

                case '>':
                    outputBuffer.append("&gt;");
                    break;

                case '\'':
                    outputBuffer.append("&apos;");
                    break;

                default:
                    if (ch > 126) {

                        int in = ch;
                        String s = (in) + "";
                        outputBuffer.append("&#" + s + ";");

                    } else {

                        outputBuffer.append(ch);
                    }
                    break;
            }
        }

        return outputBuffer.toString();
    }

    /**
     * escapeHTMLForUTF is similar to escapeHTML,except the default part
     * which is modified not to encode the char.
     * <p>
     * Encodes the input String into "HTML safe" format using entity
     * references and escape sequences.  <b>This method will be deprecated
     * soon</b>.
     *
     * <p>
     * Note that this method encode the apostrophy into
     * <code>&amp;apos;</code>  which is not an HTML standard, but since
     * this is an existing method used by a lot of old code, we don't
     * change it for now, and may deprecate / remove it later.  <b>Please
     * use  {@link #escapeXML(String)}  for future applications</b>
     * </p>
     *
     * @param inputString the string to be encoded.
     * @return the string after encoding.
     */
    public static String escapeHTMLForUTF(String inputString) {

        if (inputString == null) {
            return "";
        }

        StringBuffer outputBuffer = new StringBuffer();
        for (int i = 0; i < inputString.length(); i++) {

            char ch = inputString.charAt(i);

            switch (ch) {

                case '"':
                    outputBuffer.append("&quot;");
                    break;

                case '&':
                    outputBuffer.append("&amp;");
                    break;

                case '<':
                    outputBuffer.append("&lt;");
                    break;

                case '>':
                    outputBuffer.append("&gt;");
                    break;

                case '\'':
                    outputBuffer.append("&apos;");
                    break;

                default:
                    outputBuffer.append(ch);
                    break;
            }
        }

        return outputBuffer.toString();
    }
}

class UnicodeFormatter {

    static public String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char hexDigit[] = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    static public String charToHex(char c) {
        // Returns hex String representation of char c
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }

} // class