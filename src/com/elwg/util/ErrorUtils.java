package com.elwg.util;

/**
 * @cn:定义错误的类
 * @en:a class which aims to describe the errors of the api
 * @author zm
 * @since 2014/1/8
 */
public class ErrorUtils {

	//定义相关的变量
	public static String HTTPGET_ERROR = "method failed, the error code is:";
	
	/*
	 * @cn:定义粉丝爬取过程中的错误信息
	 * @en:Define the errors in craering the fans of some Weibo user;
	 * */
	public static String ERROR_INPUTSTREAMNULL = "输入数据流为空!";
	public static String ERROR_INPUTSTREAMNULLRESONS = "可能的原因如下：\n1、页面的地址发生变化；2、程序爬取时间过长，IP被封，请稍后试一试；";
	public static String ERROR_REXNULL = "匹配到的内容为空，也许正则表达式有误!";
	public static String ERROR_REXTOOLS = "请尝试使用正则表达式工具：http://pan.baidu.com/s/1ntjqyTz，并在Java中进行转义即可。";
	
	//定义登陆过程中的错误
	public static String AUTHORWRONG = "用户名或密码错误，请检查!";
	
	//定义爬取个人用户信息时候的错误
	public static String ERROR_DATANULL = "系统爬取到的数据为空，请检查!";
	
	//爬取微博内容的时候的错误
	public static String ERROR_WEIBO_STARTTIME = "没有设置起始时间，请检查!";
	public static String ERROR_TIMEDISORDER = "起始时间比终止时间大，请检查!";
	public static String ERROR_TIMEWRONG = "时间设置有误，请检查时间是否按照2014-1-16 9:13的格式输入!";
}
