package com.elwg.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

	public StringHelper() {
		
	}
	/**
	 * 检测字符串中是否包含'\xA3\x94\xD3\xD0\xB0\xAE...'编码的字符
	 * */
	public static boolean detectHex(String dataStr){
		if(dataStr == null){
	        return true;
	    }
		Pattern pattern = Pattern.compile("(\\\\x(\\p{XDigit}{2})){3}");
		Matcher matcher = pattern.matcher(dataStr);
		while(matcher.find()){
				return false;
		}
		return true;
	}
	/**
	 * 
	 * 将 \u9ed1\u8272\u7248\u56fe 编码的字符串，转为可阅读的文字
	 * 用于新浪微博 正文解析
	 * @param dataStr 
	 * @return
	 * 
	 * @since  crawler_weibo　Ver1.0
	 */
	public static String decodeUnicode(String dataStr) {
	    if(dataStr == null){
	        return null;
	    }
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
        Matcher matcher = pattern.matcher(dataStr);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            dataStr = dataStr.replace(matcher.group(1), ch + "");    
        }
        return dataStr;
    } 

	/**
	 * 字符串的长度是否大于0 
	 * @param str
	 * @return 
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 判断字符串是否为null 或者 空白
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input){
		return (input == null || "".equals(input.trim()));
	}
	
	/**
	 * xml保留字符轉義
	 * 
	 * @param s
	 * @return
	 */
	public static String tranXMLChars(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"",
				"&quot;");
	}

	public static String convertFromXMLChars(String s) {
		return s.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll(
				"&gt;", ">").replaceAll("&apos;", "'").replaceAll("&quot;",
				"\"");
	}

	/**
	 * input的value轉義,（xml保留字符除'轉義)
	 * @param s
	 * @return
	 */
	public static String tranVALUEChars(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;").replaceAll("\"", "&quot;");
	}

	/**
	 * 处理Oracle 的转义字符，使like 查询可以正确匹配
	 * ' ——> ''
	 * & ——> '||char(38)||'	（10g中ms 不用转义）
	 * % ——> \% 'escape'\
	 * _ ——> \_ 'escape'\ 
	 * 
	 */
	public static String transformLikeSql(String s) {

		boolean isContain = false;
		String result = (s == null) ? "" : s;
		if (result.indexOf("%") != -1) {
			isContain = true;
		} else if (result.indexOf("_") != -1) {
			isContain = true;
		}
		result = result.replaceAll("'", "''");
		if (!isContain) {
			return "%" + result + "%";
		}

		result = "%" + result.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_")
				+ "%";
		result += "'escape'\\";
		return result;
	}

	/**
	 * 处理普通的sql，解决条件中'的问题，用于非like SQL条件 的拼装
	 * @param s
	 * @return
	 */
	public static String transformSql(String s) {
		String result = (s == null) ? "" : s;
		return result.replaceAll("'", "''");
	}

	
	public static String convertLongArrayIdToString(long[] longArray){
		String result = "";
		for(long input : longArray){
			result += input+ ",";
		}
		if(result.length() == 0){
			return result;
		}else{
			return result.substring(0,result.length()-1);
		}
	}
	
	public static String convertStringArrayIdToString(String[] strArray){
		String result = "";
		for(String input : strArray){
			result += input+ ",";
		}
		return result;
	}
	
	public static String joinStringArray(Object[] strArray, String joinStr) {
		String result = "";
		for(Object str : strArray) {
			result += str.toString() + joinStr;
		}
		return result;
	}
	/**
	 * 截断字符串
	 * 如果字符串长度小于需要长度，则返回字符串本身
	 * @param src
	 * @param length
	 * @param suffix
	 * @return
	 */
	public static String truncate(String src, int length, String suffix){
		if(src == null)
			return src;
		src = src.trim();
		if(src.length() <= length)
			return src;
		return src.substring(0, length) + suffix;
	}
	
	/**
	 * 对html中符号进行转义
	 * @param s
	 * @param replacement
	 * @return
	 */
	public static String escapeHtmlNotation(String s, String replacement){
		return s.replaceAll("<.+?>", replacement);
	}
	public static String shortenURI(String uri){
		return StringHelper.shortenURI(uri, 30, 10);
	}
	public static String shortenURI(String uri,int totalLength,int subfixLength){
		int urlLength = uri.length();
		String subfix = urlLength<totalLength?"":(" ... "+uri.substring(urlLength-subfixLength, urlLength));
		return "".equals(subfix)?uri:(uri.substring(0,totalLength - subfixLength) + subfix);
	}
	
	/**
     * Create an absolute URL from a relative link.
     * @param link The reslative portion of a URL.
     * @param strict If <code>true</code> a link starting with '?' is handled
     * according to <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>,
     * otherwise the common interpretation of a query appended to the base
     * is used instead.
     * @return The fully qualified URL or the original link if it was absolute
     * already or a failure occured.
     */
    public static String getAbsoluteURL(String link, String base, boolean strict) {
        URL url;
        String ret;

        if ((null == link) || ("".equals(link)))
            ret = "";
        else
            try {
                if (null == base)
                    ret = link;
                else {
                    url = constructUrl(link, base, strict);
                    ret = url.toExternalForm();
                }
            } catch (MalformedURLException murle) {
                ret = link;
            }

        return (ret);
    }
    
    /**
     * Build a URL from the link and base provided.
     * @param link The (relative) URI.
     * @param base The base URL of the page, either from the &lt;BASE&gt; tag
     * or, if none, the URL the page is being fetched from.
     * @param strict If <code>true</code> a link starting with '?' is handled
     * according to <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>,
     * otherwise the common interpretation of a query appended to the base
     * is used instead.
     * @return An absolute URL.
     * @exception MalformedURLException If creating the URL fails.
     */
    private static URL constructUrl(String link, String base, boolean strict)
            throws MalformedURLException {
        String path;
        boolean modified;
        boolean absolute;
        int index;
        URL url; // constructed URL combining relative link and base

        // Bug #1461473 Relative links starting with ?
        if (!strict && ('?' == link.charAt(0))) { // remove query part of base if any
            if (-1 != (index = base.lastIndexOf('?')))
                base = base.substring(0, index);
            url = new URL(base + link);
        } else
            url = new URL(new URL(base), link);
        path = url.getFile();
        modified = false;
        absolute = link.startsWith("/");
        if (!absolute) { // we prefer to fix incorrect relative links
            // this doesn't fix them all, just the ones at the start
            while (path.startsWith("/.")) {
                if (path.startsWith("/../")) {
                    path = path.substring(3);
                    modified = true;
                } else if (path.startsWith("/./") || path.startsWith("/.")) {
                    path = path.substring(2);
                    modified = true;
                } else
                    break;
            }
        }
        // fix backslashes
        while (-1 != (index = path.indexOf("/\\"))) {
            path = path.substring(0, index + 1) + path.substring(index + 2);
            modified = true;
        }
        if (modified)
            url = new URL(url, path);

        return (url);
    }
    
    
    
    public static void main(String[] args){
        String file = "src/template/html.txt";
        String html = FileHelper.readFile(file,"utf-8");
//        System.out.println("html: "+html);
        html = transformLikeSql(html);
        html = decodeUnicode(html);
        System.out.println("html: "+html);
//        FileHelper.write("1.txt", decodeUnicode(html));
//        FileHelper.write("2.txt", html);
    }
}
