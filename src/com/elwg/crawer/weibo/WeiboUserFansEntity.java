package com.elwg.crawer.weibo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.elwg.util.Constant;
import com.elwg.util.ErrorUtils;
import com.elwg.util.weibo.WeiboConstant;

/**
 * @cn:Weibo用户粉丝实体类
 * @en:Weibo user fans entity
 * @author Ivsunshine
 * @since 2014/1/9
 */
public class WeiboUserFansEntity {

	/*
	 * @cn:定义相关的变量
	 * @en:Define some relation params
	 * */
	private String userId = null; // user_id
	
	private HttpClient client = null;// httpclient
	
	private int fansListPageNum = 0;//fans page number
	
	private ArrayList<WeiboUserInfo> fansLists = new ArrayList<WeiboUserInfo>();// fans lists
	
	//定义存储的流
	private ByteArrayOutputStream bout = new ByteArrayOutputStream();
	
	//定义是否显示Log信息
	private boolean isShowLog = false;
	
	/**
	 * Construct function
	 * @param client
	 */
	public WeiboUserFansEntity(HttpClient client) {
		this.client = client;
	}

	/**
	 * @cn:获取粉丝的列表
	 * @en:get the lists of the fans
	 * @param userId : the user with this userId you want to crawer
	 * @param fansCount: the count of the user you want to crawer
	 * @param isShowLog: whether you want to show the Web content or some log info to help you make new regex or other things 
	 * @param patternStr: the pattern string you want to regex the web content, if null, it will be give the original regex
	 * @return ArrayList<WeiboUserInfo>
	 * @throws Exception
	 */
	public ArrayList<WeiboUserInfo> sendFansListRequest(String userId, int fansCount, boolean isShowLog, String patternStr) {
		this.userId = userId;
		this.isShowLog = isShowLog;
		while (true) {
			//当粉丝列表的数目和用户给定的数目一致的时候，直接返回
			if (this.fansLists.size() >= fansCount){
				List<WeiboUserInfo> result = this.fansLists.subList(0, fansCount);
				return new ArrayList<WeiboUserInfo>(result);
			}
			this.getNextFansList();
			byte[] buff = bout.toByteArray();
			ByteArrayInputStream bin = new ByteArrayInputStream(buff);
			if (bout.size() == 0){//如果读取到的流的内容为空
				System.out.println(ErrorUtils.ERROR_INPUTSTREAMNULL + "\n" + ErrorUtils.ERROR_INPUTSTREAMNULLRESONS);
				return this.fansLists;
			}
			if (patternStr == null || patternStr == ""){
				patternStr = WeiboConstant.fansPatterns;
			}
			this.ParseFansInfoFromStream(bin, patternStr);
			this.fansListPageNum++;// 将粉丝的page + 1即爬取下一页的信息
			if (this.fansListPageNum > WeiboConstant.fansLimitPage){//当粉丝的页面数据>10的时候必须停止，因为会有重复
				return this.fansLists;
			}
		}
	}

	/**
	 * @cn: 将下一页的内容存入到OutputStream中
	 * @en: put the next page's content to the outputstream
	 * @param out
	 */
	private void getNextFansList() {
		String url = "http://weibo.com/" + this.userId + "/fans?page="
				+ String.valueOf(fansListPageNum);
		//将网页内容写入到outputStream中
		getPageContent(url, Constant.utf8CharSet);
	}
	
	private void getPageContent(String url, String charCode) {
		try {
			HttpGet getMethod = new HttpGet(url);
			HttpResponse response = this.client.execute(getMethod);
			if (charCode == "" || charCode == null){
				charCode = Constant.utf8CharSet;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), charCode));
			String line;
			bout.reset();//重置
			PrintWriter pw = new PrintWriter(bout);
			while (null != (line = br.readLine())) {
				pw.println(line);
			}
			pw.flush();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @cn:将用户的粉丝信息从InputStream中解析出来
	 * @en:Parse the fans infomation from the inputStream param
	 * @param in 输入的数据流
	 */
	private void ParseFansInfoFromStream(InputStream in, String patternStr) {
		try {
			String line;
			String result;
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();// 关闭buffer

			result = sb.toString().replace("\\r", "").replace("\\n", "")
					.replace("\\t", "").replace("\\", "");
			if (this.isShowLog){
				System.out.println("Web Content：" + result);
			}
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(result);
			while (matcher.find()) {
				WeiboUserInfo userInfo = new WeiboUserInfo();
				userInfo.setUserId(matcher.group(1));
				userInfo.setNickName(matcher.group(2));
				userInfo.setSex(matcher.group(3));
				userInfo.setLocation(matcher.group(4));
				userInfo.setFollowCount(Integer.valueOf(matcher.group(5)));
				userInfo.setFansCount(Integer.valueOf(matcher.group(6)));
				userInfo.setMsgCount(Integer.valueOf(matcher.group(7)));
				userInfo.setBriefIntro(matcher.group(8));
				//如果不包含则添加
				if (!this.fansLists.contains(userInfo)){
					this.fansLists.add(userInfo);
				}
				if (this.isShowLog){
					showFansInfo(userInfo);
				}
			}
			//经过一轮匹配发现数据为空
			if (this.fansLists.size() == 0){
				System.out.println(ErrorUtils.ERROR_REXNULL + "\n" + ErrorUtils.ERROR_REXTOOLS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @cn:展示粉丝的消息
	 * @en:show the infomation of the fans
	 * @param entity
	 */
	private void showFansInfo(WeiboUserInfo userInfo) {
		System.out
				.printf("-------------------------------------------------------------------\n"
						+ "ShowInfo:UID:%s\tuserName:%s\tsex:%s\terea:%s\tfollowCount:%d\tfansCount:%d\tmsgCount:%d\t",
						userInfo.getUserId(), userInfo.getNickName(), userInfo.getSex(),
						userInfo.getLocation(), userInfo.getFollowCount(),
						userInfo.getFansCount(), userInfo.getMsgCount());
	}

}