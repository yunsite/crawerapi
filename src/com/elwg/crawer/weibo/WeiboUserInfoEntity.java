package com.elwg.crawer.weibo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;

import com.elwg.tools.HttpGetUrlContent;
import com.elwg.util.ErrorUtils;
import com.elwg.util.weibo.WeiboConstant;

/**
 * @cn:Weibo用户个人信息实体类
 * @en:Weibo user infomation entity
 * @author Ivsunshine
 * @since 2014/1/9
 */
public class WeiboUserInfoEntity {

	//相关的变量定义
	@SuppressWarnings("unused")
	private String user_id = "";

	//数据爬取的相关的对象
	private HttpClient client = null;
	
	//是否显示调试内容
//	private boolean isShowLog = false;
	
	// 构造函数1
	public WeiboUserInfoEntity(HttpClient client) {
		this.client = client;
	}

	/**
	 * @cn:获取微博的用户信息
	 * @en:get the infomation of the special userId user
	 * @param user_id 用户的user_id
	 * @param isShowLog 是否显示网页内容，供调试用
	 * @param patterns 匹配用户信息的正则表达式，因为用户信息的种类繁多，所以有匹配不同内容的正则表达式
	 * @return 用户的实体
	 */
	public WeiboUserInfo sendInfoRequest(String user_id, boolean isShowLog, ArrayList<String> patterns) {
		String info_url = "http://weibo.com/" + user_id + "/info";
		//其格式为：http://weibo.com/1000349667/info

//		this.isShowLog = isShowLog;
		String data = null;
		WeiboUserInfo weiboUserInfo = new WeiboUserInfo();//创建对象存储
		weiboUserInfo.setUserId(user_id);
		try {
			HttpGetUrlContent httpContent = new HttpGetUrlContent(client);
			data = httpContent.getUrlContent(info_url).replace("\\r", "").replace("\\n", "").replace("\\t", "").replace("\\", "");
			if (data == null || data == ""){
				System.out.println(ErrorUtils.ERROR_DATANULL + "\nURL为：http://weibo.com/" + user_id + "/info；\n" + ErrorUtils.ERROR_INPUTSTREAMNULLRESONS);
				System.exit(0);
			}
			
			//是否显示网页内容
			if (isShowLog){
				System.out.println("Web content：" + data);
				System.out.println(ErrorUtils.ERROR_REXTOOLS);
			}
			
			Pattern pattern_basic = Pattern.compile(WeiboConstant.userPatterns.get(0));
			Matcher matcher_basic = pattern_basic.matcher(data);
			
			//查找除了标签之外的其他信息
			while (matcher_basic.find()){
				weiboUserInfo.setHeadImgUrl(matcher_basic.group(1));
				weiboUserInfo.setNickName(matcher_basic.group(2));
				weiboUserInfo.setPageId(matcher_basic.group(3));
				weiboUserInfo.setFollowCount(Integer.parseInt(matcher_basic.group(4)));
				weiboUserInfo.setFansCount(Integer.parseInt(matcher_basic.group(5)));
				weiboUserInfo.setMsgCount(Integer.parseInt(matcher_basic.group(6)));
				weiboUserInfo.setLocation(matcher_basic.group(7));
				weiboUserInfo.setSex(matcher_basic.group(8));
				weiboUserInfo.setRegisterTime(matcher_basic.group(9));
			}
			
			//个性域名
			Pattern pattern_domain = Pattern.compile(WeiboConstant.userPatterns.get(1));
			Matcher matcher_domain = pattern_domain.matcher(data);
			while (matcher_domain.find()){
				weiboUserInfo.setIndiviDomain(matcher_domain.group(1));
			}
			
			//简介信息
			Pattern pattern_briefIntro = Pattern.compile(WeiboConstant.userPatterns.get(2));
			Matcher matcher_briefIntro = pattern_briefIntro.matcher(data);
			while (matcher_briefIntro.find()){
				weiboUserInfo.setBriefIntro(matcher_briefIntro.group(1));
			}
			
			//性取向信息
			Pattern pattern_sexLove = Pattern.compile(WeiboConstant.userPatterns.get(3));
			Matcher matcher_sexLove = pattern_sexLove.matcher(data);
			while (matcher_sexLove.find()){
				weiboUserInfo.setSexLove(matcher_sexLove.group(1));
			}
			
			//生日信息
			Pattern pattern_birthday = Pattern.compile(WeiboConstant.userPatterns.get(4));
			Matcher matcher_birthday = pattern_birthday.matcher(data);
			while (matcher_birthday.find()){
				weiboUserInfo.setBirthday(matcher_birthday.group(1));
			}
			
			//公司信息
			Pattern pattern_company = Pattern.compile(WeiboConstant.userPatterns.get(5));
			Matcher matcher_company = pattern_company.matcher(data);
			while (matcher_company.find()){
				weiboUserInfo.setCompany(matcher_company.group(1));
			}
			
			//教育信息
			Pattern pattern_education = Pattern.compile(WeiboConstant.userPatterns.get(6));
			Matcher matcher_education = pattern_education.matcher(data);
			while (matcher_education.find()){
				weiboUserInfo.setEducation(matcher_education.group(1));
			}
			
			//匹配标签部分
			Pattern pattern_tags = Pattern.compile(WeiboConstant.userPatterns.get(7));
			Matcher matcher_tags = pattern_tags.matcher(data);
			String tags = "";
			
			while (matcher_tags.find()){
				tags = tags + matcher_tags.group(1) + ";";
			}
			
			if (tags.length() > 1){
				tags = tags.substring(0, tags.length() - 1);
			}
			weiboUserInfo.setTags(tags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weiboUserInfo;
	}

}
