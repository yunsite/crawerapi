package com.elwg.util.weibo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与新浪微博有关的常量类
 * 
 * @author Ivsunshine
 * @since 2014/1/7
 */
public class WeiboConstant {

	// 是否是Weibo的HttpClient
	public static int WEIBO_HTTPCLIENT = 1;

	// 是否采用Weibo的Auth认证，即使用其默认的API接口获取数据
	public static int WEIBO_API = 2;

	// 微博的测试账号集，可以修改添加等
	public static String[][] WEIBO_ACCOUNTS = {
			{ "weibo_api2@126.com", "weibo_api2@126.com", "weibo_api3@126.com",
					"weibo_api4@126.com" },
			{ "weibo_api", "weibo_api", "weibo_api", "weibo_api" } };

	/*
	 * Weibo用户信息的正则表达式集 1、用户头像、昵称、关注数目、粉丝数目、所在地、性别、注册时间 2、用户个性域名 3、用户简介 4、用户性取向
	 * 5、用户生日 6、用户公司信息 7、用户教育信息 8、用户标签信息
	 */
	public static ArrayList<String> userPatterns = new ArrayList<String>();
	static {
		userPatterns
				.add("pf_head_pic\"\\s+><img\\s+src=\"(.*?)\"\\s+alt=\"(.*?)\".*?href=\"\\/p\\/(\\d+)\\/follow.*?follow\">(\\d+).*?fans\">(\\d+).*?weibo\">(\\d+).*?所在地<\\/div><div\\s+class=\"con\">(.*?)<\\/div>.*?性别<\\/div><div\\s+class=\"con\">(.*?)<\\/div>.*?注册时间<\\/div><div\\s+class=\"con\">(.*?)<\\/div>");
		userPatterns.add("infdomain\">(.*?)<\\/a>");
		userPatterns.add("简介<\\/div><div\\s+class=\"con\">(.*?)<\\/div>");
		userPatterns.add("性取向<\\/div><div\\s+class=\"con\">(.*?)<\\/div>");
		userPatterns.add("生日<\\/div><div\\s+class=\"con\">(.*?)<\\/div>");
		userPatterns.add("公司<\\/div>(.*?)<\\/div><\\/div><\\!\\-\\-");
		userPatterns
				.add("教育信息</legend></fieldset><div\\s+class=\"btns\"></div></form>(.*?)</div><!--");
		userPatterns.add("node-type=\"tag\">(.*?)</span>");
	}

	// 用户粉丝的正则表达式
	public static String fansPatterns = "action-data=\"uid=(\\d+)&fnick=(\\w*[\\u4E00-\\u9FA5]*\\w*-*\\w*[\\u4E00-\\u9FA5]*\\w*)?&sex=(\\w+)\">.*?male\"></em>([\u4E00-\u9FA5]*\\s*[\u4E00-\u9FA5]*)?.*?/follow\"\\s+>(\\d+)</a>.*?/fans\"\\s+>(\\d+)</a>.*?\"\\s+>(\\d+)</a>.*?\"info\">(.*?)</div>";
//	public static String fansPatterns = "action-data=";

	/*
	 * 微博粉丝的爬取部分
	 */
	public static int fansLimitPage = 10;// 新浪微博限制爬取的页面数目

	/**
	 * @cn:将微博的时间格式化成正常的时间
	 * @en:format the weibo status time to normal time
	 * @param time
	 * @return the normal time, string type
	 */
	public static String formatTime(String time) {
		String result = "";
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour, minute = 0;
		Pattern pattern = null;
		Matcher matcher = null;
		String timeDay = year + "-" + month + "-" + day + " ";
		if (time == null || time == ""){
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			result = timeDay + hour + ":" + minute;
		}else if (time.contains("前")) {
			pattern = Pattern.compile("(\\d+)分钟前");
			matcher = pattern.matcher(time);
			while (matcher.find()) {
				c.add(Calendar.MINUTE, -Integer.valueOf(matcher.group(1)));
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);
				result = timeDay + hour + ":" + minute;
			}
		} else if (time.contains("今天")) {
			String[] times = time.split(" ");
			result = timeDay + times[1];
		} else if (time.contains("月") || time.contains("日")) {
			pattern = Pattern.compile("(\\d+)月(\\d+)日\\s(.*)");
			matcher = pattern.matcher(time);
			while (matcher.find()) {
				result = year + "-" + matcher.group(1) + "-" + matcher.group(2)
						+ " " + matcher.group(3);
			}
		} else {
			result = time;
		}
		return result;
	}
}
