package com.elwg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.dom4j.Document;

import com.elwg.tools.HtmltoXML;

/**
 * @cn:常量类
 * @en:Constant class
 * @author Ivsunshine
 * @since 2014/1/7
 */
public class Constant {

	//定义变量
	public static int CRAWER_TYPE_NORMAL = 0;
	public static int CRAWER_TYPE_WEIBO = 1;
	public static String utf8CharSet = "utf-8";//设置默认的编码格式
	public static String gb2312CharSet = "gb2312";//设置默认的编码格式
	
	//定义常用函数
	 /**
	 * @cn: 将url的内容存入到OutputStream中
	 * @param url: the url you want to get the content
	 * @param client: the client you need to crawer the web content
	 * @param charCode: the charCode you want to get the web content, if null or "", it will be given utf-8 format
	 * @return os: contains the url web content outputstream
	 */
	public static void getPageContent(String url, HttpClient client, OutputStream os, String charCode) {
		try {
			HttpGet getMethod = new HttpGet(url);
			HttpResponse response = client.execute(getMethod);
			if (charCode == "" || charCode == null){
				charCode = Constant.utf8CharSet;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), charCode));
			String line;
			PrintWriter pw = new PrintWriter(os);
			while (null != (line = br.readLine())) {
				pw.println(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @cn: 比较两个输入时间是否处于两个时间之间
	 * @en: compare whether the input time in the two times
	 * @param date2Input: the input time
	 * @param dateStart: the start time
	 * @param dateEnd: the end time
	 * @return if yes, return true, else no
	 */
	public static boolean compareDate(String date2Input, String dateStart, String dateEnd) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		try {
			long time1 = df.parse(dateStart).getTime();
			long time2 = df.parse(dateEnd).getTime();
			long time_terminate = df.parse(date2Input).getTime();
			if ((time1 < time_terminate) && (time_terminate < time2)) {// 如果处于time1和time2之间，则直接返回true;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @cn:比较两个时间的先后顺序
	 * @en:compare the two param times
	 * @param firstDate
	 * @param secondDate
	 * @return if firstDate < secondDate, true, else false
	 */
	public static boolean compareDate (String firstDate, String secondDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			long time1 = df.parse(firstDate).getTime();
			long time2 = df.parse(secondDate).getTime();
			if (time1 < time2) {// 如果处于time1小于time2，则直接返回true;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @cn: 获取当前的时间，按照yyyy-MM-dd HH:mm:ss的格式
	 * @en: get the current time
	 * @return: yyyy-MM-dd HH:mm:ss format
	 */
	public static String getCurrentTime (){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
}
